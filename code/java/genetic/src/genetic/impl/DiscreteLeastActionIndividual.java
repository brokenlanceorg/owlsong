package genetic.impl;

import genetic.*;
import math.*;

import java.math.*;
import java.util.*;

/**
 * The basic idea is that we are implementing a brute-force search (via genetic algorithm) of a discrete version of the 
 * principle of least action for a given set of data points that represent the true worldline of the phenomenon in question.
 * So, since the data in the cache are actual true world lines -- but wait second, in physics given the constraints, there
 * is a unique worldline; however, here we have many...
 * 
 * 
 */
public class DiscreteLeastActionIndividual extends Individual
{

   /**
    * Default constructor.
    */
   public DiscreteLeastActionIndividual()
   {
   }

   /**
    * Implementation of Interface.
    * This is the proper implementation, so please take note.
    */
   public Individual clone()
   {
      Individual indy = new DiscreteLeastActionIndividual();
      indy.setEnvironmentCache( getEnvironmentCache() );
      indy.setFitness( _myFitness );

      if( _myGenotype == null || _myGenotype.size() == 0 )
      {
         indy.randomizeGenome();
      }
      else
      {
         // Make a deep copy of the genotype
         ArrayList<Double> genotype = new ArrayList<Double>();
         for( double gene : _myGenotype )
         {
            genotype.add( gene );
         }
         indy.setGenotype( genotype );
      }

      return indy;
   }

   /**
    * Implementation of Interface.
    * A power set transform of 5 elements gives 155 elements.
    */
   public void randomizeGenome()
   {
      int s = getEnvironmentCache().getGenomeLength();
      _myGenotype = new ArrayList< Double >( s );
      for( int i=0; i<s; i++ )
      {
         _myGenotype.add( MathUtilities.random() );
      }
   }
   /**
    *
    */
   private double[][] getRandomStreams()
   {
      double[][] streams = getEnvironmentCache().getDataStreams();
      double[][] s       = new double[ streams.length ][ streams[ 0 ].length ];
      double     r       = 0;
      
      for( int i=0; i<streams.length; i++ )
      {
         for( int j=0; j<streams[ i ].length; j++ )
         {
            if( MathUtilities.random() >= 0.5 )
            {
               s[ i ][ j ] = MathUtilities.random() * streams[ i ][ j ];
            }
            else
            {
               r = MathUtilities.random();
               if( r != 0.0 )
               {
                  s[ i ][ j ] = streams[ i ][ j ] / r;
               }
            }
         }
      }

      return s;
   }


   /**
    * Implementation of Interface.
    */
   public double evaluateFitness()
   {
      _myFitness                    = 0; // This is crucial for the algorithm to work
      int                   n       = 0;
      int                   N       = 10000;
      double[][]            streams = getEnvironmentCache().getDataStreams();
      double[]              data    = new double[ N ];
      double[]              evals   = new double[ streams[ 0 ].length ];
      double                s       = 0;
      FunctionalCorrelation root    = FunctionalCorrelation.buildBinaryTree( _myGenotype, streams ); 
      HashSet< Integer >    vars    = root.getDependentVariables();
      StatUtilities         stat    = null;

      // If the number of dependent variables is not the same as the number of variables in
      // the data file, then we don't even want to go further.
      for( Integer var : vars )
      {
         if( var > -1 )
         {
            n++;
         }
      }
      if( n != streams.length )
      {
         return Double.MIN_VALUE;
      }

      for( int i=0; i<N; i++ )
      {
         data[ i ] = 0;
      }

      for( int i=0; i<streams[ 0 ].length; i++ )
      {
         evals[ i ] = root.evaluate( i );
         s += evals[ i ];
         if( Double.isNaN( s ) || s < 0.0 )
         {
//             System.out.println( "found a NaN!!!" );
            return Double.MIN_VALUE;
         }
      }
      if( s <= 0.0 || Double.isInfinite( s ) )
      {
         return Double.MIN_VALUE;
      }
      stat = new StatUtilities( evals );
      if( stat.getDeviation() <= 10 )
      {
         return Double.MIN_VALUE;
      }
//       System.out.println( "deviation: " + stat.getDeviation() );

      for( int j=0; j<N; j++ )
      {
         root.setDataStreams( getRandomStreams() );
         for( int i=0; i<streams[ 0 ].length; i++ )
         {
            data[ j ] += root.evaluate( i );
            if( Double.isNaN( data[ j ] ) || s < 0.0 )
            {
//                System.out.println( "found a NaN!!!" );
               return Double.MIN_VALUE;
            }
         }
         if( data[ j ] <= 0.0 || Double.isInfinite( data[ j ] ) )
         {
            return Double.MIN_VALUE;
         }
      }

      MedianSort sorter = new MedianSort();
      sorter.sort( data );
      n = 0;
      for( int i=0; i<N; i++ )
      {
//          System.out.println( i + " comparing s: " + s + " to: " + data[ i ] );
         if( s > data[ i ] )
         {
            n++;
         }
         else
         {
            break;
         }
      }

      _myFitness = (double)n;

      if( _myFitness != 0 )
      {
         _myFitness = 1 / _myFitness;
      }
      else
      {
         _myFitness = Double.MAX_VALUE;
      }

//       _myFitness *= stat.getDeviation();

//       System.out.println( "fitness is: " + _myFitness );

      return _myFitness;
   }

   /**
    *
    */
   public String toString()
   {
      double[][]            streams = getEnvironmentCache().getDataStreams();
      FunctionalCorrelation root    = FunctionalCorrelation.buildBinaryTree( _myGenotype, streams ); 
      String                result  = root.toString( 0 );

      result += "\nFitness: " + _myFitness + "\n";

      return result;
   }
   
   /**
    *
    */
   public String toStringFinal()
   {
      double[][]            streams = getEnvironmentCache().getDataStreams();
      double[]              evals   = new double[ streams[ 0 ].length ];
      FunctionalCorrelation root    = FunctionalCorrelation.buildBinaryTree( _myGenotype, streams ); 
      String                result  = root.toString( 0 );
      double                sum     = 0;

      result += "\nFitness: " + _myFitness + "\n";
      result += "\nGenome: ";

      for( double gene : _myGenotype )
      {
         result += gene + " ";
      }

      result += "\npredictions:\n";
      double fitness = 0;
      for( int i=0; i<streams[ 0 ].length; i++ )
      {
         double value = root.evaluate( i );
         evals[ i ] = value;
         if( Double.isNaN( value ) )
         {
            System.out.println( "found a NaN!" );
         }
         result += value + "\n";
         sum += value;
      }
      StatUtilities stat = new StatUtilities( evals );
      result += "\nDiscrete sum: " + sum + "\n";
      result += "\nDeviation: " + stat.getDeviation() + "\n";
      HashSet< Integer > vars = root.getDependentVariables();
      for( Integer var : vars )
      {
         result += "A variable is: " + var + "\n";
      }

      return result;
   }
}
