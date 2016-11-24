package math;

import common.FileReader;
import java.util.*;

/**
 * 
 *  The basic idea behind the Dynamical System of Regression Models (DySoRM)
 *  is that we use the non-linear regression models of FunctionCorrelations
 *  to create a dynamical system or iterated function system to extrapolate
 *  the relationships between time-based variables.
 * 
 * The system will be instantiated with the basic fundamentals and then from that
 * it will extrapolate its predictions. The fundamentals include:
 * 1) an array whose rows are the number of dependent variables and whose columns
 *    are the number of data points to extrapolate. The zeroth entries are
 *    the initial data points.
 * 2) the genotype for each non-linear regression model -- will be a single array list.
 * 3) the number of iterations per data point.
 *
 * The DySoRM algorithm is as follows:
 *   Each dependent variable will have an initial data point (the zeroth elements).
 *   for the number of points to extrapolate:
 *      for the number of iteration per data point:
 *         for all i != j do
 *            x_j = f_j( x_i )
 *      use either the median, average, min, max, or last of the iteration 
 *      or a combination thereof as the extrapolated point
 * 
 *
 *
 */
public class DySoRM
{

   private double[][]              _extrapolations;
   private FunctionalCorrelation[] _regressionModels;

   /**
    * @deprecated
    */
   public DySoRM()
   {
   }

   /**
    * We assume that the lengths are correct.
    * The best way to ensure this, is to create the initial two-dimensional array with 
    * a size of rows = number of dependent variables and columns = number of data points
    * The size of the genome should then be individual regression model genome size 
    * times the number of independent variables.
    * So, in short we have:
    * Tg = g * v
    * extrapolation array size = m x n
    * m = v
    * n = e
    * Where g is the individual genome size, v is the number of independent variables,
    * and e is the number of extrapolated data points requested.
    */
   public DySoRM( ArrayList< Double > genome, double[][] initial )
   {
      _extrapolations = initial;
      _regressionModels = new FunctionalCorrelation[ _extrapolations.length ];
      int gSize = genome.size() / _extrapolations.length;
      int p = 0;

      for( int i=0; i<_regressionModels.length; i++ )
      {
         ArrayList< Double > g = new ArrayList< Double >();
         for( int j=0; j<gSize; j++ )
         {
            g.add( genome.get( p++ ) );
         }
//          _regressionModels[ i ] = FunctionalCorrelation.buildBinaryTree( g );
      }
   }

   /**
    * The DySoRM algorithm is as follows:
    *   Each dependent variable will have an initial data point (the zeroth elements).
    *   for the number of points to extrapolate:
    *      for the number of iteration per data point:
    *         for all i != j do
    *            x_j = f_j( x_i )
    *      use either the median, average, min, max, or last of the iteration 
    *      or a combination thereof as the extrapolated point
    *
    *
    */
   public double[][] evaluate()
   {
      double[][] arguments = new double[ _extrapolations.length - 1 ][ 1 ];

      // for each point to extrapolate:
      for( int i=1; i<_extrapolations[ 0 ].length; i++ )
      {
         // for each variable (k != m)
         for( int k=0; k<_regressionModels.length; k++ )
         {
            // create the arguments vector -- everything but the current
            for( int m=0, n=0; m<_extrapolations.length; m++ )
            {
               if( m != k )
               {
                  arguments[ n++ ][ 0 ] = _extrapolations[ m ][ i - 1 ];
               }
            }
            _regressionModels[ k ].setDataStreams( arguments );
            _extrapolations[ k ][ i ] = _regressionModels[ k ].evaluate( 0 );
         }
      }

      return _extrapolations;
   }

   /**
    * The DySoRM algorithm is as follows:
    *   Each dependent variable will have an initial data point (the zeroth elements).
    *   for the number of points to extrapolate:
    *      for the number of iteration per data point:
    *         for all i != j do
    *            x_j = f_j( x_i )
    *      use either the median, average, min, max, or last of the iteration 
    *      or a combination thereof as the extrapolated point
    *
    *
    * @param int -- The index position to pull from data streams.
    */
   public double[][] evaluate( int numberOfIterations )
   {
      double[][] intermediate = new double[ _regressionModels.length ][ numberOfIterations ];
      double[][] arguments = new double[ _extrapolations.length - 1 ][ 1 ];

      // for each point to extrapolate:
      for( int i=1; i<_extrapolations[ 0 ].length; i++ )
      {
         // set the initial data point for the iterations
         for( int r=0; r<_regressionModels.length; r++ )
         {
            intermediate[ r ][ 0 ] = _extrapolations[ r ][ i - 1 ];
         }

         // for the number of iterations
         for( int j=1; j<numberOfIterations; j++ )
         {
            // for each variable (k != m)
            for( int k=0; k<_regressionModels.length; k++ )
            {
               // create the arguments vector -- everything but the current
               for( int m=0, n=0; m<_extrapolations.length; m++ )
               {
                  if( m != k )
                  {
                     arguments[ n++ ][ 0 ] = intermediate[ m ][ j - 1 ];
                  }
               }
               _regressionModels[ k ].setDataStreams( arguments );
               intermediate[ k ][ j ] = _regressionModels[ k ].evaluate( 0 );
            }
         }

         // now we need to transform the intermediate points into the current point
         // for now, we just grab the last iteration point
         for( int r=0; r<_extrapolations.length; r++ )
         {
            _extrapolations[ r ][ i ] = intermediate[ r ][ intermediate[ r ].length - 1 ];
         }
      }

      return _extrapolations;
   }

   /**
    *
    */
   public String toString( int depth )
   {
      StringBuffer result = new StringBuffer( "\nDySoRM:" );
      result.append( "\nInitial Conditions:" );
      result.append( "\n< " );

      for( int i=0; i<_extrapolations.length; i++ )
      {
         result.append( _extrapolations[i][0] + " " );
      }
      result.append( ">" );

      for( int i=0; i<_regressionModels.length; i++ )
      {
         result.append( "\nNon-Linear Regression Model " + i + ":" );
         result.append( _regressionModels[ i ].toString( 0 ) );
      }

      return result.toString();
   }

   /**
    * Don't forget that this can return negative values, so may want to 
    * take the absolute value, or something....
    */
   public static void main( String[] args )
   {
      /*
      // randomicity test
      int numberOfVariables = 3;
      int indyGenomeSize = 15;
      int numberOfExtrapolations = 25;
      double[][] extrapolations = new double[ numberOfVariables ][ numberOfExtrapolations ];
      ArrayList< Double > genome = new ArrayList< Double >();
      for( int i=0; i<(indyGenomeSize * numberOfVariables); i++ )
      {
         genome.add( Math.random() );
      }

      for( int i=0; i<numberOfVariables; i++ )
      {
         extrapolations[ i ][ 0 ] = Math.random();
      }

      DySoRM dyn = new DySoRM( genome, extrapolations );
      extrapolations = dyn.evaluate( 5 );

      for( int i=0; i<numberOfVariables; i++ )
      {
         for( int j=0; j<numberOfExtrapolations; j++ )
         {
            System.out.println( "( " + i + ", " + j + " ) = " + extrapolations[ i ][ j ]  );
         }
      }

      */

      // First, we read in the genome
      common.FileReader reader = new FileReader( "dysorm-genome.dat", " " );
      String[] words = reader.getArrayOfWords();
      ArrayList<Double> genome = new ArrayList<Double>();

      for( int i=0; i<words.length; i++ )
      {
         try
         {
            genome.add( Double.parseDouble( words[i] ) );
         }
         catch( NumberFormatException e )
         {
            System.err.println( "Caught exception reading genome: " + e );
         }
      }

      // Next, we read in the data variables
      reader = new FileReader( "dysorm-variables.dat", " " );
      words = reader.getArrayOfWords();
      int        numberOfVariables = Integer.parseInt( words[ 0 ] );
      int        numberOfDataPoints = (numberOfVariables == 1) ? (words.length - 1) : (words.length) / numberOfVariables;
      double[][] streams = new double[ numberOfVariables ][ numberOfDataPoints ];
      double[][] input = new double[ numberOfVariables ][ numberOfDataPoints ];
      double[]   t = null;
      int        count = 1;

      System.out.println( "The number of variables is: " + numberOfVariables );
      System.out.println( "The number of data points is: " + numberOfDataPoints );

      for( int i=0; i<numberOfVariables; i++ )
      {
         t = new double[ numberOfDataPoints ];
         for( int j=0; j<numberOfDataPoints; j++ )
         {
            try
            {
               t[ j ] = Double.parseDouble( words[ count++ ] );
//                System.out.println( "a value: " + t[j] );
            }
            catch( NumberFormatException e )
            {
               System.err.println( "Caught exception parsing data: " + e );
            }
         }
         streams[ i ] = (new MathUtilities()).normalize( t, 1 );
//          streams[ i ] = (new MathUtilities()).normalize( t, Math.PI );
//          streams[ i ] = t;
      }

      // At this point, streams contains all the data that the regression models were trained on
      // with the additional last point which will serve as the initial condition for the dynamic
      // system. So, we need to move this last point to be the first data entry; then, the dysorm
      // will iterate on that and overwrite the rest of the array with "predictions".
      for( int i=0; i<numberOfVariables; i++ )
      {
         streams[ i ][ 0 ] = streams[ i ][ streams[ i ].length - 1 ];
      }

      DySoRM dyn = new DySoRM( genome, streams );
//       double[][] extrapolations = dyn.evaluate( 20 );
      double[][] extrapolations = dyn.evaluate();
      for( int j=0; j<numberOfDataPoints; j++ )
      {
         for( int i=0; i<numberOfVariables; i++ )
         {
            System.out.print( extrapolations[ i ][ j ] + ", "  );
         }
         System.out.println( "" );
      }
      System.out.println( dyn.toString( 0 ) );
      
   }
}
