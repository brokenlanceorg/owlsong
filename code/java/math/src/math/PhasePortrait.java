package math;

import java.util.*;
import common.*;

/**
 * Here, we calculate the dimensionality of the phase space portrait.
 * Consider the correlation function of the attractor:
 * C(r) = 1 / N^2 sum_i,j^N( o( r - abs( X_i - X_j ) ) )
 * where o() is the Heaviside function - 0 if x < 0, 1 if x > 0.
 * Assuming C(r) varies as r^d, we have: ln( C(r) ) = d * ln( r )
 * The algorithm is as follows:
 * 1) For successively higher values of n of phase space, calculate C(r)
 * 2) Calculate d near the origin (i.e., small values of r) to see how it changes with r.
 * 3) If d verses n is saturated beyond some small values of n and d is the dimension
 *    of the attractor.
 */
public class PhasePortrait
{

   private double[]            _timeSeries;
   private double              _radius = 1.1;
   private double              _radiusLog;
//    private double              _toleranceR = 25;
//    private double              _toleranceA = 1.0;
   private double              _toleranceR = 10;
   private double              _toleranceA = 500;
   private int                 _maxDimension = 25;

   /**
    *
    */
   public PhasePortrait()
   {
   }

   /**
    *
    */
   public PhasePortrait( double[] v )
   {
      _timeSeries = v;
      _radiusLog = Math.log( _radius );
   }

   /**
    *
    */
   public PhasePortrait( ArrayList< Double > v )
   {
      _timeSeries = (new MathUtilities()).toArray( v );
      _radiusLog = Math.log( _radius );
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void setMaximumDimension( int d )
   {
      _maxDimension = d;
   }

   /**
    *
    * @param ] 
    */
   public void setTimeSeries( double[] variables )
   {
      _timeSeries = variables;
   }

   /**
    *
    * @return double[][]
    */
   public double[] getTimeSeries()
   {
      return _timeSeries;
   }

   /**
    * Here, we calculate the dimensionality of the phase space portrait.
    * Consider the correlation function of the attractor:
    * C(r) = 1 / N^2 sum_i,j^N( o( r - abs( X_i - X_j ) ) )
    * where o() is the Heaviside function - 0 if x < 0, 1 if x > 0.
    * Assuming C(r) varies as r^d, we have: ln( C(r) ) = d * ln( r )
    * The algorithm is as follows:
    * 1) For successively higher values of n of phase space, calculate C(r)
    * 2) Calculate d near the origin (i.e., small values of r) to see how it changes with r.
    * 3) If d verses n is saturated beyond some small values of n, d is the dimension
    *    of the attractor.
    */
   public void calculateAttractorDimension( int lag, int N )
   {
      MathUtilities util = new MathUtilities();
      for( int i=2; i<_maxDimension; i++ )
      {
         double[][] v = util.deconstructTimeSeries( _timeSeries, i, N, lag );
         double count = 0;
         int l = v[ 0 ].length;

         for( int k=0; k<l; k++ )
         {
            for( int z=0; z<l; z++ )
            {
               if( k == z )
               {
                  continue;
               }

               double sum = 0;
               for( int j=0; j<v.length; j++ )
               {
                  sum += Math.abs( v[ j ][ k ] - v[ j ][ z ] );
               }

               if( sum <= _radius )
               {
                  count += 1.0;
               }
            }
         }

//          System.out.println( "count is: " + count );
         count /= (double)( N * N );
//          System.out.println( "count is now: " + count );
         double dim = Math.log( count ) / _radiusLog;
//          System.out.println( "N is: " + i + " count: " + count + " Dimension is: " + dim );
      }
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public int computeFalseNeighbors( int lag, int N )
   {
      double[][] d = new double[ N ][ N ];
      StatUtilities sd = new StatUtilities( _timeSeries );
      MathUtilities util = new MathUtilities();
      boolean dimensionNotSet = true;
      double proportion = 0;
      double distance = 0;
      double r = 0;
      double s = (double) ( N * ( N - 1 ) );
      int    dimension = 2;

      for( int i=2; i<_maxDimension; i++ )
      {
         double[][] v = util.deconstructTimeSeries( _timeSeries, i, N, lag );
         int falseNeighbors = 0;

         for( int j=0; j<N; j++ )
         {
            for( int k=0; k<N; k++ )
            {
               if( j == k )
               {
                  continue;
               }

               if( i == 2 )
               {
                  d[ j ][ k ] = Math.abs( v[ 0 ][ j ] - v[ 1 ][ k ] );
               }
               else
               {
                  distance = Math.abs( v[ v.length - 1 ][ j ] - v[ v.length - 1 ][ k ] );
//                   System.out.println( "d: " + distance );
                  r = distance / d[ j ][ k ];
                  if( r >= _toleranceR )
                  {
                     falseNeighbors++;
                  }
                  // perform false neighbor tests
                  d[ j ][ k ] += distance;
                  if( d[ j ][ k ] / sd.getDeviation() >= _toleranceA )
                  {
                     falseNeighbors++;
                  }
               }
            }
         }
         if( i > 2 )
         {
            proportion = 100 * ( (double)falseNeighbors / s );
//             System.out.println( i + " --> " + proportion );
            if( proportion <= 0.0 && dimensionNotSet )
            {
               dimension = i;
               dimensionNotSet = false;
            }
         }
      }
      if( dimensionNotSet )
      {
         dimension = _maxDimension;
      }

      return dimension;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public int computeFalseNeighbors( int lag )
   {
      StatUtilities sd = new StatUtilities( _timeSeries );
      MathUtilities util = new MathUtilities();
      boolean dimensionNotSet = true;
      double previous = 100;
      double proportion = 0;
      double distance = 0;
      double r = 0;
      int    dimension = 2;

      for( int i=2; i<_maxDimension; i++ )
      {
         double[][] v = util.deconstructTimeSeries( _timeSeries, i, lag );
         // since the size of the array is changing, we have to change how we compute
         // the false neighbor below
         int N = v[ 0 ].length;
         double[][] d = new double[ N ][ N ];
         int falseNeighbors = 0;

         for( int j=0; j<N; j++ )
         {
            for( int k=0; k<N; k++ )
            {
               if( j == k )
               {
                  continue;
               }

               if( i == 2 )
               {
                  d[ j ][ k ] = Math.abs( v[ 0 ][ j ] - v[ 1 ][ k ] );
               }
               else
               {
                  distance = Math.abs( v[ v.length - 1 ][ j ] - v[ v.length - 1 ][ k ] );
                  // since we change the size of the variables each time, we can't divide
                  // by the current distance since it would be zero (we have now other way of
                  // ascertaining this value, i.e., keeping values through dimensions).
//                   r = distance / d[ j ][ k ];
//                   System.out.println( "distance: " + distance );
                  d[ j ][ k ] += distance;
//                   System.out.println( "dev: " + d[ j ][ k ] / sd.getDeviation() );
                  if( distance >= _toleranceR )
//                   if( r >= _toleranceR ) see comment above
                  {
//                      System.out.println( "r is greater than tolR" );
                     falseNeighbors++;
                  }
                  // perform false neighbor tests
                  else if( d[ j ][ k ] / sd.getDeviation() >= _toleranceA )
                  {
//                      System.out.println( "dev: " + d[ j ][ k ] / sd.getDeviation() + " is greater than tolA" );
                     falseNeighbors++;
                  }
               }
            }
         }
         if( i > 2 )
         {
//             proportion = 100 * ( (double)falseNeighbors / ( N * (N - 1) ) );
            proportion = 100 * ( (double)falseNeighbors / Math.pow( N, i ) );
//             System.out.println( i + " --> " + proportion );
//             System.out.println( i + " --> " + Math.abs( previous - proportion ) );
//             System.out.println( "False neighbors is: " + falseNeighbors );
//             System.out.println( "count neighbors is: " + ( N * (N - 1) ) );
            if( Math.abs( previous - proportion ) <= 1e-30 && dimensionNotSet )
            {
               dimension = i;
               dimensionNotSet = false;
            }
            previous = proportion;
         }
      }
      if( dimensionNotSet )
      {
         dimension = _maxDimension;
      }

      return dimension;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public double computeLargestLyapunov( int dimension, int lag, int N )
   {
//       System.out.println( "In Lyapunov, using dimension: " + dimension );
      double[][] v = (new MathUtilities()).deconstructTimeSeries( _timeSeries, dimension, N, lag );
      ArrayList< Double > distances = new ArrayList< Double >();

      for( int i=0; i<N; i++ )
      {
         double min = Double.MAX_VALUE;

         for( int j=0; j<N; j++ )
         {
            if( i == j )
            {
               continue;
            }
            double d = 0;
            double temp = 0;
            for( int k=0; k<v.length; k++ )
            {
               temp = v[ k ][ i ] - v[ k ][ j ];
               temp *= temp;
               d += temp;
            }
            d = Math.sqrt( d );
            if( d < min )
            {
               min = d;
               distances.add( Math.log( d ) );
            }
         }
      }

      StatUtilities sd = new StatUtilities( distances );
//       System.out.println( "The exponent is: " + sd.getMean() );

      return sd.getMean();
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public double computeLargestLyapunov( int dimension, int lag )
   {
//       System.out.println( "In Lyapunov, using dimension: " + dimension );
      double[][] v = (new MathUtilities()).deconstructTimeSeries( _timeSeries, dimension, lag );
      int N = v[ 0 ].length;
      ArrayList< Double > distances = new ArrayList< Double >();

      for( int i=0; i<N; i++ )
      {
         double min = Double.MAX_VALUE;

         for( int j=0; j<N; j++ )
         {
            if( i == j )
            {
               continue;
            }
            double d = 0;
            double temp = 0;
            for( int k=0; k<v.length; k++ )
            {
               temp = v[ k ][ i ] - v[ k ][ j ];
               temp *= temp;
               d += temp;
            }
            d = Math.sqrt( d );
            if( d < min )
            {
               min = d;
               distances.add( Math.log( d ) );
            }
         }
      }

      StatUtilities sd = new StatUtilities( distances );
//       System.out.println( "The exponent is: " + sd.getMean() );

      return sd.getMean();
   }

   /**
    *
    */
   public static void main( String[] args )
   {
      common.FileReader reader = new FileReader( "raw.dat", " " );
      String[] words = reader.getArrayOfWords();
      ArrayList<Double> temp = new ArrayList<Double>();
      ArrayList<Double> delta = new ArrayList<Double>();
      int lag = Integer.parseInt( words[ 0 ] );
      int N = Integer.parseInt( words[ 1 ] );
      int maxDimension = 15;
      int d = 2;

      for( int i=2; i<words.length; i++ )
      {
         try
         {
            temp.add( Double.parseDouble( words[i] ) );
         }
         catch( NumberFormatException e )
         {
            System.err.println( "Caught exception reading genome: " + e );
         }
      }

      for( int i=1; i<temp.size(); i++ )
      {
         delta.add( temp.get( i ) / temp.get( i - 1 ) );
      }

      long start = System.currentTimeMillis();
//       PhasePortrait pp = new PhasePortrait( temp );
      PhasePortrait pp = new PhasePortrait( delta );
      pp.setMaximumDimension( maxDimension );
      N = Math.min( delta.size() - maxDimension * lag, N );
      if( N < 0 )
      {
         N = Integer.parseInt( words[ 1 ] );
         N = Math.max( delta.size() - maxDimension * lag, N );
//          System.out.println( "recalculating N." );
      }
//       System.out.println( "N is: " + N );
      d = pp.computeFalseNeighbors( lag, N );
//       double exp = pp.computeLargestLyapunov( d, lag, N );
      double exp = pp.computeLargestLyapunov( d, lag );
      ApproximateEntropy ae = new ApproximateEntropy( (new MathUtilities()).toArray( delta ), 1, 0.05 );
//       System.out.println( "Approximate Entropy is: " + ae.getEntropy() );
//       System.out.println( "time is: " + (System.currentTimeMillis() - start ) );
//       d = pp.computeFalseNeighbors( lag );
   }
}
