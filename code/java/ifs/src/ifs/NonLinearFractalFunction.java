package ifs;

import java.util.*;

import genetic.*;
import genetic.impl.*;
import common.*;
import grafix.*;
import math.*;

/**
 * The NonLinear fractal function is a set of Hutchinson operators
 * W_n( x_1, x_2, ..., x_i ) : R^i --> R^i, for n in [1, N] with N = (J - 1),
 * and where J is the number of i-tuples of interpolation data.
 * Each W_n is composed of i closed-form correlation functions.
 * Then, using the IFS algorithm (from the shadow theorem), an attractor is
 * generated interpolating the i-tuples data provided the following conditions
 * are met:
 *
 * The W_n operator maps the left-most data point to the left-most data point
 * of the nth interval
 * AND
 * the W_n operator maps the right-most data point to the right-most data point
 * of the nth interval
 *
 * If i is greater than 2, then the attractor can be projected on a two-dimensional
 * plane and plotted to generate a one-dimensional function.
 *
 */
public class NonLinearFractalFunction
{
   // Instead of keeping arrays of the individual variables (x, y, etc.),
   // we will store all of them in a two-dimensional array so that we can
   // more easily store multiple data functions. In this scheme, the rows
   // represent distinct variables, while the columns represent data instances.
   private FunctionalCorrelation[][] _operators;
   private double[][]                _interpolationData;
   private double[]                  _functionValues;
   // The genome size should be 8 * N * i
   private double[]                  _genome;
   private int                       _numberOfIterations = 100000;
   private int                       _numberOfOperators  = 1000;
   private int                       _numberOfVariables  = 1000;
   private int                       _numberOfThreads    = 10;
   private int                       _operatorsPerThread = 10;
   private int                       _resolutionWidth    = 1000;
   private int                       _genomeSize         = 8; // depth-3 full binary tree
   private long                      _trainingTime       = 1000;
   private double                    _sma                = 0.1;
   private HaltingCriteria           _criteria           = new HaltingCriteria();
   private boolean                   _initialized        = false;

   /**
    *
    */
   public NonLinearFractalFunction()
   {
   }

   /**
    *
    */
   public NonLinearFractalFunction( int o, int v )
   {
      _numberOfOperators = o;
      _numberOfVariables = v;
   }

   /**
    *
    */
   public NonLinearFractalFunction( double[][] data )
   {
      _interpolationData = data;
      _numberOfOperators = _interpolationData[ 0 ].length - 1;
      _numberOfVariables = _interpolationData.length;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void setInitialPoint( double[][] point )
   {
      _interpolationData = point;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void setNumberOfOperators( int num )
   {
      _numberOfOperators = num;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void setNumberOfVariables( int num )
   {
      _numberOfVariables = num;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void setGenome( double[] genome )
   {
      _genome = genome;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void setGenomeSize( int size )
   {
      _genomeSize = size;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void setNumberOfThreads( int n )
   {
      _numberOfThreads = n;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void setTrainingTime( String trainingTime )
   {
      _trainingTime = (new MathUtilities()).getTime( trainingTime );
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void setResolutionWidth( int width )
   {
      _resolutionWidth = width;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void setNumberOfIterations( int num )
   {
      _numberOfIterations = num;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void train()
   {
      if( _numberOfOperators >= _numberOfThreads )
      {
         _operatorsPerThread = (int)Math.ceil( (double)_numberOfOperators / (double)_numberOfThreads );
      }
      else
      {
         _numberOfThreads = _numberOfOperators;
         _operatorsPerThread = 1;
      }

      _trainingTime /= ( _operatorsPerThread * _numberOfVariables );
      Thread[] threads = new Thread[ _numberOfThreads ];
      _criteria.setElapsedTimeTolerance( _trainingTime );

      System.out.println( "threads: " + _numberOfThreads );
      System.out.println( "ops per thread: " + _operatorsPerThread );
      System.out.println( "Training time per correlate is: " + _trainingTime );

      for( int i=0; i<_numberOfThreads; i++ )
      {
         threads[ i ] = new Thread( new Trainer( i ) );
         threads[ i ].start();
      }

      for( int i=0; i<_numberOfThreads; i++ )
      {
         try
         {
            threads[ i ].join();
         }
         catch( InterruptedException e )
         {
            System.err.println( "Exception during join: " + e );
         }
      }

      System.out.println( "Finished training all threads." );
   }

   /**
    * The idea here is to return the function values for the FIF: f(x)
    * Since the user will supply the set of interpolation data, the domain
    * is understood to be the encompassing interval spaced into equal steps
    * returnvalue.size() in number.
    * 
    */
   public double[] createFractalData()
   {
      initialize();
      iterateSystem();

      return _functionValues;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public double[] evaluateOperator( int n, double[][] point )
   {
      double[] result = new double[ _numberOfVariables ];

      for( int k=0; k<_numberOfVariables; k++ )
      {
         _operators[ n ][ k ].setDataStreams( point );
         result[ k ] = _operators[ n ][ k ].evaluate( 0 );
      }

      return result;
   }

   /**
    *
    */
   protected void iterateSystem()
   {
      int pos = 0;
      int x = 0;
      double[][] point = new double[ _numberOfVariables ][ 1 ];
      double[][] newPoint = new double[ _numberOfVariables ][ 1 ];

      // set the initial point
      for( int i=0; i<_numberOfVariables; i++ )
      {
         point[ i ][ 0 ] = _interpolationData[ i ][ 0 ];
//          System.out.println( "initial point: " + point[ i ][ 0 ] );
      }

      for( int i=0; i<_numberOfIterations; i++ )
      {
         pos = ( int )( ( (new MathUtilities()).random() * (double)_numberOfOperators ) - 0.000001 );
//          System.out.println( "rand pos: " + pos );
         for( int k=0; k<_numberOfVariables; k++ )
         {
            _operators[ pos ][ k ].setDataStreams( point );
            newPoint[ k ][ 0 ] = _operators[ pos ][ k ].evaluate( 0 );
//             System.out.println( "eval: " + newPoint[ k ][ 0 ] );
         }
         for( int k=0; k<_numberOfVariables; k++ )
         {
            point[ k ][ 0 ] = newPoint[ k ][ 0 ];
         }
         // now plot the projection -- we *might* want to capture all this data here,
         // and then in createFractalData normalize it back into [ 0, 1 ]...
         x = ( int )( point[ 0 ][ 0 ] * ( double )( _resolutionWidth - 1 ) );
//          System.out.println( "x is: " + x );

         if( x < 0 )
         {
            x = 0;
         }
         else if( x >= ( _resolutionWidth - 1 ) )
         {
            x = _resolutionWidth - 1;
         }

         if( _functionValues[ x ] == Double.MIN_VALUE )
         {
            _functionValues[ x ] = point[ 1 ][ 0 ];
         }
         else
         {
            _functionValues[ x ] += ( _sma * ( point[ 1 ][ 0 ] - _functionValues[ x ] ) );
         }
      }
   }

   /**
    * The NonLinear fractal function is a set of Hutchinson operators
    * W_n( x_1, x_2, ..., x_i ) : R^i --> R^i, for n in [1, N] with N = (J - 1),
    * and where J is the number of i-tuples of interpolation data.
    * Each W_n is composed of i closed-form correlation functions.
    * Then, using the IFS algorithm (from the shadow theorem), an attractor is
    * generated interpolating the i-tuples data provided the following conditions
    * are met:
    *
    * The W_n operator maps the left-most data point to the left-most data point
    * of the nth interval
    * AND
    * the W_n operator maps the right-most data point to the right-most data point
    * of the nth interval
    *
    * If i is greater than 2, then the attractor can be projected on a two-dimensional
    * plane and plotted to generate a one-dimensional function.
    *
    *
    */
   private void initialize()
   {
      _functionValues = new double[ _resolutionWidth ];

      for( int i=0; i<_resolutionWidth; i++ )
      {
         _functionValues[ i ] = Double.MIN_VALUE;
      }

      if( _genome == null )
      {
         // The genome size should be 8 * N * i
         _genome = new double[ _genomeSize * _numberOfOperators * _numberOfVariables ];
         train();
      }

      if( _initialized == false )
      {
         int pos = 0;
         _operators = new FunctionalCorrelation[ _numberOfOperators ][ _numberOfVariables ];
         for( int i=0; i<_numberOfOperators; i++ )
         {
            for( int j=0; j<_numberOfVariables; j++ )
            {
               ArrayList< Double > genome = new ArrayList< Double >();
               for( int k=0; k<_genomeSize; k++ )
               {
                  genome.add( _genome[ pos++ ] );
               }
               // buildBinaryTree( ArrayList<Double> list, double[][] streams )
               // Since we're just setting up the system, we can just put bogus data in the contructor
               // because at runtime this data will be the orbit of a single point in the IFS.
               double[][] data = new double[ 1 ][ 1 ];
               data[ 0 ][ 0 ] = 0;
               _operators[ i ][ j ] = FunctionalCorrelation.buildBinaryTree( genome, data );
            }
         }
         _initialized = true;
      }
   }

   /**
    *
    */
   public String toString()
   {
      String value = "\n";

      for( int i=0; i<_numberOfOperators; i++ )
      {
         value += "Operator " + i + "\n";
         for( int j=0; j<_numberOfVariables; j++ )
         {
            value += "Variable " + j + "\n";
            value += _operators[ i ][ j ].toString( 3 ) + "\n";
         }
      }
      value += "Genome:\n";
      for( int i=0; i<_genome.length; i++ )
      {
         value += _genome[ i ] + " ";
      }
      value += "\nTraingTime: " + _trainingTime;

      return value;
   }

   /**
    *  
    */
   public class Trainer implements Runnable
   {
      int _threadNumber;
      
      /**
       *
       * @param TYPE
       * @return TYPE
       */
      public Trainer()
      {
      }

      /**
       *
       * @param TYPE
       * @return TYPE
       */
      public Trainer( int myNum )
      {
         _threadNumber = myNum;
      }

      /**
       *
       *
       * The W_n operator maps the left-most data point to the left-most data point
       * of the nth interval
       * AND
       * the W_n operator maps the right-most data point to the right-most data point
       * of the nth interval
       *
       * @param TYPE
       * @return TYPE
       */
      public void run()
      {
         System.out.println( "Thread " + _threadNumber + " will train operators:" );
         int start = _threadNumber * _operatorsPerThread;
//          double[][] independentVariables = new double[ _numberOfVariables ][ 2 ];
         double[][] independentVariables = new double[ 1 ][ 2 ];
         independentVariables[ 0 ][ 0 ] = _interpolationData[ 0 ][ 0 ];
         independentVariables[ 0 ][ 1 ] = _interpolationData[ 0 ][ _numberOfOperators ];

//          // This data is the same regardless of operator or thread or variable:
//          for( int j=0; j<_numberOfVariables; j++ )
//          {
//             independentVariables[ j ][ 0 ] = _interpolationData[ j ][ 0 ];
//             independentVariables[ j ][ 1 ] = _interpolationData[ j ][ _numberOfOperators ];
//             System.out.println( _threadNumber + "| variable: " + j + " first instance: " + independentVariables[ j ][ 0 ] );
//             System.out.println( _threadNumber + "| variable: " + j + " last instance: " + independentVariables[ j ][ 1 ] );
//          }
         
         for( int i=0; i<_operatorsPerThread; i++ )
         {
            int pos = start + i;

            if( pos >= _numberOfOperators )
            {
               break;
            }

            System.out.println( _threadNumber + "|Will train operator position: " + pos );
            double[] dependentVariables = new double[ 2 ];

            for( int j=0; j<_numberOfVariables; j++ )
            {
               dependentVariables[ 0 ] = _interpolationData[ j ][ pos ];
               dependentVariables[ 1 ] = _interpolationData[ j ][ pos + 1 ];

               System.out.println( _threadNumber + "| " + j + " dependent variable: " + dependentVariables[ 0 ] );
               System.out.println( _threadNumber + "| " + j + " dependent variable: " + dependentVariables[ 1 ] );

               EnvironmentCache env = new EnvironmentCache();
               env.setDataStreams( independentVariables );
               env.setTargetStream( dependentVariables );
               env.setGenomeLength( _genomeSize );
               GeneticAlgorithm ga = new GeneticAlgorithm( _criteria, "genetic.impl.FunctionalIndividual",
                                          env, _genomeSize, 10 );

               FunctionalIndividual bestIndividual = (FunctionalIndividual) ga.evolve();
               ArrayList< Double > bestGenotype = bestIndividual.getGenotype();
               for( int k=0; k<bestGenotype.size(); k++ )
               {
                  int genePos = ( pos * _numberOfVariables * _genomeSize + j * _genomeSize );
                  System.out.println( _threadNumber + "| genePos is: " + genePos );
                  _genome[ k + genePos ] = bestGenotype.get( k );
               }
//          _genome = new double[ _genomeSize * _numberOfOperators * _numberOfVariables ];
            }
         }
      }

   }

   /**
    * The test data are:
    * ( 0.0, 0.4 ), ( 0.33, 1.0 ), ( 0.66, 0.5 ), ( 1.0, 0.235 )
    */
   public static void main( String[] args )
   {
      double[][] interpolationData = new double[ 2 ][ 4 ];
      interpolationData[ 0 ][ 0 ] = 0.0;
      interpolationData[ 0 ][ 1 ] = 0.33;
      interpolationData[ 0 ][ 2 ] = 0.66;
      interpolationData[ 0 ][ 3 ] = 1.0;
      interpolationData[ 1 ][ 0 ] = 0.4;
      interpolationData[ 1 ][ 1 ] = 1.0;
      interpolationData[ 1 ][ 2 ] = 0.5;
      interpolationData[ 1 ][ 3 ] = 0.235934;
      // The genome size should be 8 * N * i
      double[]   genome = new double[ 8 * 3 * 2 ];
      genome[ 0 ]  = 0.132342234;
      genome[ 1 ]  = 0.2342234;
      genome[ 2 ]  = 0.45742342234;
      genome[ 3 ]  = 0.03450832342234;
      genome[ 4 ]  = 0.95932342234;
      genome[ 5 ]  = 0.247242342234;
      genome[ 6 ]  = 0.8272402342234;
      genome[ 7 ]  = 0.724022342234;
      genome[ 8 ]  = 0.132342234;
      genome[ 9 ]  = 0.2342234;
      genome[ 10 ] = 0.45742342234;
      genome[ 11 ] = 0.03450832342234;
      genome[ 12 ] = 0.95932342234;
      genome[ 13 ] = 0.247242342234;
      genome[ 14 ] = 0.8272402342234;
      genome[ 15 ] = 0.724022342234;
      genome[ 16 ] = 0.32342234;
      genome[ 17 ] = 0.42234;
      genome[ 18 ] = 0.742342234;
      genome[ 19 ] = 0.450832342234;
      genome[ 20 ] = 0.932342234;
      genome[ 21 ] = 0.7242342234;
      genome[ 22 ] = 0.72402342234;
      genome[ 23 ] = 0.4022342234;
      genome[ 24 ] = 0.2342234;
      genome[ 25 ] = 0.42234;
      genome[ 26 ] = 0.742342234;
      genome[ 27 ] = 0.450832342234;
      genome[ 28 ] = 0.932342234;
      genome[ 29 ] = 0.7242342234;
      genome[ 30 ] = 0.72402342234;
      genome[ 31 ] = 0.4022342234;
      genome[ 32 ] = 0.4022342234;
      genome[ 33 ] = 0.4022342234;
      genome[ 34 ] = 0.4022342234;
      genome[ 35 ] = 0.4022342234;
      genome[ 36 ] = 0.4022342234;
      genome[ 37 ] = 0.4022342234;
      genome[ 38 ] = 0.4022342234;
      genome[ 39 ] = 0.4022342234;
      genome[ 40 ] = 0.4022342234;
      genome[ 41 ] = 0.4022342234;
      genome[ 42 ] = 0.4022342234;
      genome[ 43 ] = 0.4022342234;
      genome[ 44 ] = 0.4022342234;
      genome[ 45 ] = 0.4022342234;
      genome[ 46 ] = 0.4022342234;
      genome[ 47 ] = 0.4022342234;
      NonLinearFractalFunction function = new NonLinearFractalFunction( interpolationData );
      function.setGenome( genome );
      double[] f = function.createFractalData();
      for( int i=0; i<f.length; i++ )
      {
            System.out.println( f[ i ] );
      }
      System.out.println( "\nThe function is: " + function );

      function.setGenome( null );
      function.setTrainingTime( "1m" );
      function.setNumberOfThreads( 6 );
      function.setResolutionWidth( 4 );

//       System.out.println( "\nThe function is: " + function );
      System.out.println( "\nRun 1" );
      f = function.createFractalData();
      for( int i=0; i<f.length; i++ )
      {
         System.out.println( f[ i ] );
      }

      System.out.println( "\nRun 2" );
      f = function.createFractalData();
      for( int i=0; i<f.length; i++ )
      {
         System.out.println( f[ i ] );
      }

      System.out.println( "\nRun 3" );
      function.setNumberOfIterations( 100 );
      f = function.createFractalData();
      for( int i=0; i<f.length; i++ )
      {
         System.out.println( f[ i ] );
      }
   }

}
