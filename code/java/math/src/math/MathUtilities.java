package math;

import common.*;
import java.math.*;
import java.util.*;
import java.util.regex.*;
import org.spaceroots.mantissa.random.MersenneTwister;

/**
 * This class contains various mathematical utilities.
 *
 */
public class MathUtilities implements Persistable
{

   // This PRNG is NOT thread safe
   private static final MersenneTwister _prng1 = new MersenneTwister();
   // This PRNG is thread safe
   private static final MTRandom        _prng2 = new MTRandom();

   /**
    * This method will map the input data array into [0, 1].
    * To invert the values back, or if the intermediary constants
    * are required, use the Interval class.
    * This method is kept separate from the overridden version to 
    * keep the runtime fast.
    * @param double[] the data array of values in [x, y]
    * @retrun double[] the data array of values in [0, 1]
    */
   public double[] normalize( double[] data, double scale )
   {
      double   min    = Double.MAX_VALUE;
      double   max    = Double.MIN_VALUE;
      double[] result = new double[ data.length ];

      // First, need to find the min value in the array
      for( int i=0; i<data.length; i++ )
      {
         double value = data[ i ];
         if( value < min )
         {
            min = value;
         }
      }

      // Next, translate by the min amount
      for( int i=0; i<data.length; i++ )
      {
         double value = data[ i ];
         double temp  = (value - min);
         result[ i ]  = temp;
         if( temp > max )
         {
            max = temp;
         }
      }

      // Finally, scale by max
      for( int i=0; i<result.length; i++ )
      {
         result[ i ] = ( scale * ( result[ i ] / max ) );
      }

      return result;
   }

   /**
    * This method will map the input data array into [0, 1].
    * To invert the values back, or if the intermediary constants
    * are required, use the Interval class.
    * This method is kept separate from the overridden version to 
    * keep the runtime fast.
    * @param ArrayList<Double> the data array of values in [x, y]
    * @retrun ArrayList<Double> the data array of values in [0, 1]
    */
   public ArrayList<Double> normalize( ArrayList<Double> data, double scale )
   {
      double min = Double.MAX_VALUE;
      double max = Double.MIN_VALUE;
      ArrayList<Double> result = new ArrayList<Double>();

      // First, need to find the min value in the array
      for( double value : data )
      {
         if( value < min )
         {
            min = value;
         }
      }

      // Next, translate by the min amount
      for( double value : data )
      {
         double temp = (value - min);
         result.add( temp );
         if( temp > max )
         {
            max = temp;
         }
      }

      // Finally, scale by max
      for( int i=0; i<result.size(); i++ )
      {
         result.set( i, (scale * ( result.get( i ) / max )) );
      }

      return result;
   }

   /**
    * This method will scale the data into the interval [0, 1].
    *
    * @param double[][] a two dimensional array typically representing rows as variables and columns as instances.
    * @return double[][]
    */
   public double[][] normalize( double[][] data )
   {
      double[][] result = new double[ data.length ][];

      for( int i=0; i<data.length; i++ )
      {
         result[ i ] = normalize( data[ i ], 1 );
      }

      return result;
   }

   /**
    * This returns the Euclidean distance between two points in ND space.
    */
   public double getEuclideanDistance( int[] x, int[] y )
   {
      double[] a = new double[ x.length ];
      double[] b = new double[ y.length ];

      for( int i=0; i<a.length; i++ )
      {
         a[ i ] = (double) x[ i ];
      }

      for( int j=0; j<b.length; j++ )
      {
         b[ j ] = (double) y[ j ];
      }

      return getEuclideanDistance( a, b );
   }

   /**
    * This returns the Euclidean distance between two points in ND space.
    */
   public double getEuclideanDistance( double[] x, double[] y )
   {
      double distance  = 0;
      double t         = 0;
      double s         = 0;

      for( int i=0; i<x.length; i++ )
      {
         t  = x[ i ] - y[ i ];
         s += (t * t);
      }

      distance = Math.sqrt( s );

      return distance;
   }

   /**
    * This returns the Euclidean distance between two points in ND space.
    */
   public double getEuclideanDistance( Double[] x, Double[] y )
   {
      double distance  = 0;
      double t         = 0;
      double s         = 0;

      for( int i=0; i<x.length; i++ )
      {
         t = x[ i ] - y[ i ];
         s += (t * t);
      }

      distance = Math.sqrt( s );

      return distance;
   }

   /**
    * Here, we return the absolute distance between the two vectors.
    * @param ArrayList< Double >
    * @param ArrayList< Double >
    * @return double
    */
   public double getAbsoluteDistance( ArrayList< Double > a, ArrayList< Double > b )
   {
      double dist = -1.0;

      if( a.size() == b.size() )
      {
         dist = 0;
         for( int k=0; k<a.size(); k++ )
         {
            dist += Math.abs( a.get( k ) - b.get( k ) );
         }
      }

      return dist;
   }

   /**
    * This returns the Euclidean distance between two points in 2D space.
    * The Euclidean metric is defined as:
    * Euclidean space is d( x, y ) = sqrt( ( x1 - y1 )^2 + ( x2 - y2 )^2 )
    */
   public double getEuclideanDistance2D( double x1, double x2, double y1, double y2 )
   {
      double distance;
      double t1 = x1 - y1;
      double t2 = t1 * t1;
      t1        = x2 - y2;
      t2        += ( t1 * t1 );
      distance  = Math.sqrt( t2 );

      return distance;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public long getLong( String value )
   {
      long num = 0;
      try
      {
         num = Long.parseLong( value );
      }
      catch( NumberFormatException e )
      {
         System.out.println( "Couldn't parse number: " + e );
      }

      return num;
   }

   /**
    *
    * @param String -- The time as specified by the following: DDHHMMSS
    * @return long -- The time as represented by milliseconds past the epoch.
    */
   public long getTime( String value )
   {
      long    millis         = 0;
      Pattern dayPattern     = Pattern.compile( ".*?([0-9]+)d.*" );
      Matcher dayMatcher     = dayPattern.matcher( value );
      Pattern hourPattern    = Pattern.compile( ".*?([0-9]+)h.*" );
      Matcher hourMatcher    = hourPattern.matcher( value );
      Pattern minPattern     = Pattern.compile( ".*?([0-9]+)m.*" );
      Matcher minMatcher     = minPattern.matcher( value );
      Pattern secondsPattern = Pattern.compile( ".*?([0-9]+)s.*" );
      Matcher secondsMatcher = secondsPattern.matcher( value );

      if( dayMatcher.matches() )
      {
         System.out.println( "matched day: " + dayMatcher.group( 1 ) );
         millis += ( 24 * getLong( dayMatcher.group( 1 ) ) * 60 * 60 * 1000 );
      }
      if( hourMatcher.matches() )
      {
         System.out.println( "matched hour: " + hourMatcher.group( 1 ) );
         millis += ( getLong( hourMatcher.group( 1 ) ) * 60 * 60 * 1000 );
      }
      if( minMatcher.matches() )
      {
         System.out.println( "matched min: " + minMatcher.group( 1 ) );
         millis += ( getLong( minMatcher.group( 1 ) ) * 60 * 1000 );
      }
      if( secondsMatcher.matches() )
      {
         System.out.println( "matched seconds: " + secondsMatcher.group( 1 ) );
         millis += ( getLong( secondsMatcher.group( 1 ) ) * 1000 );
      }
      System.out.println( "returning time value: " + millis );

      return millis;
   }

   /**
    * This method implements a powerful pseudo-random number generator developed by 
    * Makoto Matsumoto and Takuji Nishimura during 1996-1997.
    *
    * This generator features an extremely long period (2^19937-1) and 623-dimensional 
    * equidistribution up to 32 bits accuracy.
    *
    * @return double -- A pseudo-random number between 0.0 and 1.0.
    */
   public static double random()
   {
      return _prng2.nextDouble();
   }

   /**
    * This method will map the, presumably, row and column integer values into the distance
    * on the Hilbert space-filling curve from the zeroth point.
    * @param int The row of the 2D matrix.
    * @param int The column of the 2D matrix.
    * @param int The value n such that n = ( max( row ) ) = ( max( column ) )
    *            where row and column are powers of two.
    * @return int the position in the array list of the pair.
    */
   public int getHilbertDistance( int x, int y, int base2Level )
   {
      int t, rx, ry, s, dist = 0;
      for( s = ( base2Level >> 1 ); s > 0; s = ( s >> 1 ) )
      {
         rx = ( ( x & s ) > 0 ) ? 1 : 0;
         ry = ( ( y & s ) > 0 ) ? 1 : 0;
         dist += s * s * ( ( 3 * rx ) ^ ry );
         if( ry == 0 )
         {
             if( rx == 1 )
             {
                x = ( s - 1 ) - x;
                y = ( s - 1 ) - y;
             }
             t = x;
             x = y;
             y = t;
         }
      }
      return dist;
   }

   /**
    * This method will map the Hilbert curve distance point into the Cartesian plane.
    * void d2xy(int n, int d, int *x, int *y) 
    * {
    *    int rx, ry, s, t=d;
    *    *x = *y = 0;
    *    for (s=1; s<n; s*=2) {
    *        rx = 1 & (t/2);
    *        ry = 1 & (t ^ rx);
    *        rot(s, x, y, rx, ry);
    *        *x += s * rx;
    *        *y += s * ry;
    *        t /= 4;
    *    }
    * }
    * @param int The distance along the Hilbert curve from the origin point.
    * @param int The value n such that n = log_2( max( row ) ) = log_2( max( column ) )
    * @return int[] Array of the Cartesian pair. 
    *               NOTE: position 0 contains y and position 1 contains x.
    *               NOTE: The above is true only for certain values of size...buggy.
    */
   public int[] getHilbertPair( int hilbertDistance, int base2Level ) 
   {
      int[] pair = new int[ 2 ];
      int rx     = 0;
      int ry     = 0;
      int s      = 0;
      int t      = hilbertDistance;
      int te     = 0;
      pair[ 0 ]  = 0;
      pair[ 1 ]  = 0;

      for( s = 1; s < base2Level; s <<= 1 ) 
      {
         rx = 1 & ( t >> 1 );
         ry = 1 & ( t ^ rx );
         if( ry == 0 )
         {
             if( rx == 1 )
             {
                pair[ 0 ] = ( s - 1 ) - pair[ 0 ];
                pair[ 1 ] = ( s - 1 ) - pair[ 1 ];
             }
             te = pair[ 0 ];
             pair[ 0 ] = pair[ 1 ];
             pair[ 1 ] = te;
         }
         pair[ 0 ] += ( s * rx );
         pair[ 1 ] += ( s * ry );
         t >>= 2;
      }

      return pair;
   }

   /**
    * This method will unfold or decompose a time series data stream into a set of linearly 
    * independent discretized variables. The variables will be linearly independent so long
    * as the choice of the time lag is correct. These variables represent the multidimensional
    * phase portrait of the time series data.
    * The original data stream X_0(t) is broken into n variables by choosing N equidistant
    * points separated by a time lag T:
    *
    * X_0   = X_0(t_1),          ..., X_0(t_N)
    * X_1   = X_0(t_1 + T),      ..., X_0(t_N + T)
    * ...
    * X_n-1 = X_0(t_1 + (n-1)T), ..., X_0(t_N + (n-1)T)
    *
    * N will be chosen as large as possible within the constraints of the number of variables
    * and the time lag.
    *
    * @param An array list of a time series data stream.
    * @param int The number of independent variables.
    * @param int The length of the variable data instance points.
    * @param int The time lag between each variable.
    * @return double[][] A two-dimensional array of discretized linearly independent (provided
    *                    the lag is large enough) phase space variables.
    */
   public double[][] deconstructTimeSeries( ArrayList< Double > series, 
                                                   int numberOfVariables, 
                                                   int N, 
                                                   int lag )
   {
      double[][] variables = new double[ numberOfVariables ][ N ];

      for( int i=0; i<numberOfVariables; i++ )
      {
         int c = 0;
         for( int j=0; j<N; j++ )
         {
            variables[ i ][ j ] = series.get( i * lag + c++ );
         }
      }

      return variables;
   }

   /**
    * This method will unfold or decompose a time series data stream into a set of linearly 
    * independent discretized variables. The variables will be linearly independent so long
    * as the choice of the time lag is correct. These variables represent the multidimensional
    * phase portrait of the time series data.
    * The original data stream X_0(t) is broken into n variables by choosing N equidistant
    * points separated by a time lag T:
    *
    * X_0   = X_0(t_1),          ..., X_0(t_N)
    * X_1   = X_0(t_1 + T),      ..., X_0(t_N + T)
    * ...
    * X_n-1 = X_0(t_1 + (n-1)T), ..., X_0(t_N + (n-1)T)
    *
    * N will be chosen as large as possible within the constraints of the number of variables
    * and the time lag.
    *
    * @param An array list of a time series data stream.
    * @param int The number of independent variables.
    * @param int The length of the variable data instance points.
    * @param int The time lag between each variable.
    * @return double[][] A two-dimensional array of discretized linearly independent (provided
    *                    the lag is large enough) phase space variables.
    */
   public double[][] deconstructTimeSeries( double[] series, 
                                            int      numberOfVariables, 
                                            int      N, 
                                            int      lag )
   {
      double[][] variables = new double[ numberOfVariables ][ N ];

      for( int i=0; i<numberOfVariables; i++ )
      {
         int c = 0;
         for( int j=0; j<N; j++ )
         {
            variables[ i ][ j ] = series[ i * lag + c++ ];
         }
      }

      return variables;
   }

   /**
    * This method will unfold or decompose a time series data stream into a set of linearly 
    * independent discretized variables. The variables will be linearly independent so long
    * as the choice of the time lag is correct. These variables represent the multidimensional
    * phase portrait of the time series data.
    * The original data stream X_0(t) is broken into n variables by choosing N equidistant
    * points separated by a time lag T:
    *
    * X_0   = X_0(t_1),          ..., X_0(t_N)
    * X_1   = X_0(t_1 + T),      ..., X_0(t_N + T)
    * ...
    * X_n-1 = X_0(t_1 + (n-1)T), ..., X_0(t_N + (n-1)T)
    *
    * N will be chosen as large as possible within the constraints of the number of variables
    * and the time lag.
    *
    * @param An array list of a time series data stream.
    * @param int The number of independent variables.
    * @param int The length of the variable data instance points.
    * @param int The time lag between each variable.
    * @return double[][] A two-dimensional array of discretized linearly independent (provided
    *                    the lag is large enough) phase space variables.
    */
   public double[][] deconstructTimeSeries( ArrayList< Double > series, 
                                                   int numberOfVariables, 
                                                   int lag )
   {
      int N = series.size() - numberOfVariables * lag;
      while( N < 0 )
      {
         numberOfVariables--;
         N = series.size() - (numberOfVariables) * lag;
      }
      double[][] variables = new double[ numberOfVariables ][ N ];

      for( int i=0; i<numberOfVariables; i++ )
      {
         for( int j=0; j<N; j++ )
         {
            variables[ i ][ j ] = series.get( i * lag + j );
         }
      }

      return variables;
   }

   /**
    * This method will unfold or decompose a time series data stream into a set of linearly 
    * independent discretized variables. The variables will be linearly independent so long
    * as the choice of the time lag is correct. These variables represent the multidimensional
    * phase portrait of the time series data.
    * The original data stream X_0(t) is broken into n variables by choosing N equidistant
    * points separated by a time lag T:
    *
    * X_0   = X_0(t_1),          ..., X_0(t_N)
    * X_1   = X_0(t_1 + T),      ..., X_0(t_N + T)
    * ...
    * X_n-1 = X_0(t_1 + (n-1)T), ..., X_0(t_N + (n-1)T)
    *
    * N will be chosen as large as possible within the constraints of the number of variables
    * and the time lag.
    *
    * @param An array list of a time series data stream.
    * @param int The number of independent variables.
    * @param int The length of the variable data instance points.
    * @param int The time lag between each variable.
    * @return double[][] A two-dimensional array of discretized linearly independent (provided
    *                    the lag is large enough) phase space variables.
    */
   public double[][] deconstructTimeSeries( double[] series, 
                                            int      numberOfVariables, 
                                            int      lag )
   {
      int N = series.length - numberOfVariables * lag;
      while( N < 0 )
      {
         numberOfVariables--;
         N = series.length - (numberOfVariables) * lag;
      }
      double[][] variables = new double[ numberOfVariables ][ N ];

      for( int i=0; i<numberOfVariables; i++ )
      {
         for( int j=0; j<N; j++ )
         {
            variables[ i ][ j ] = series[ i * lag + j ];
         }
      }

      return variables;
   }

   /**
    * This method will unfold or decompose a time series data stream into a set of linearly
    * independent discretized variables.
    * Each data point in the time series (going "forward" in time through the data) is distributed 
    * to one of the variables in order. Thus if the data in the time series were:
    * 1
    * 2
    * 3
    * 4
    * 5
    * 6
    * ...
    * and there were 3 variables, then:
    * variable 1:
    * 1
    * 4
    * variable 2:
    * 2
    * 5
    * variable 3:
    * 3
    * 6
    * ...
    *
    * @param An array list of a time series data stream.
    * @param int The number of independent variables.
    * @return double[][] A two-dimensional array of discretized linearly independent (provided
    *                    the lag is large enough) phase space variables.
    */
   public double[][] deconstructTimeSeries( double[] series,
                                            int      numberOfVariables )
   {
      int        N         = series.length / numberOfVariables;
      double[][] variables = new double[ numberOfVariables ][ N ];

      for( int i=0; i<numberOfVariables; i++ )
      {
         for( int j=0; j<N; j++ )
         {
            variables[ i ][ j ] = series[ j * numberOfVariables + i ];
         }
      }

      return variables;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public double[] toArray( ArrayList< Double > list )
   {
      double[] a = new double[ list.size() ];
      for( int i=0; i<list.size(); i++ )
      {
         a[ i ] = new Double( list.get( i ) );
      }
      return a;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public ArrayList< Double > toArrayList( double[] a )
   {
      ArrayList< Double > list = new ArrayList< Double >();
      for( int i=0; i<a.length; i++ )
      {
         list.add( a[ i ] );
      }
      return list;
   }

   /**
    * This method will, in essence, downsample the input matrix into an unfolding
    * of the data into a lower dimensional matrix, but it's not a downsample per se
    * because all of the data is used and kept in the third dimension to be used
    * in a manner such as the entropic calculations.
    * @param double[][] -- the matrix to be downsampled or hashed.
    * @return int -- the power-of-two block size that determines the length of 
    *                the Hilbert curves stored in the third dimension.
    * @return int -- the size of the second-level or hidden matrix which is usually
    *                the hidden size and a power-of-two.
    */
   public double[][][] hilbertDownsample( double[][] matrix, int blockSize, int size )
   {
      // we have two different offsets since the source matrix may not be square
      int offsetH = ( matrix.length - blockSize ) / size;
      int offsetV = ( matrix[ 0 ].length - blockSize ) / size;
      int rank = blockSize * blockSize;
      int posX = 0;
      int posY = 0;
      int dist = 0;
      double[][][] downsample = new double[ size ][ size ][ rank ];
      int[][] hilbertTemplate = new int[ blockSize ][ blockSize ];

      // since all the subsamples are the same block size, we can just compute this
      // once and then use offsets to find the value in the original input matrix.
      for( int i=0; i<blockSize; i++ )
      {
         for( int j=0; j<blockSize; j++ )
         {
            hilbertTemplate[ i ][ j ] = getHilbertDistance( i, j, rank );
         }
      }

      for( int i=0; i<size; i++ )
      {
         posX = i * offsetH;
         for( int j=0; j<size; j++ )
         {
            posY = j * offsetV;
            for( int k=0; k<blockSize; k++ )
            {
               for( int l=0; l<blockSize; l++ )
               {
                  downsample[ i ][ j ][ hilbertTemplate[ k ][ l ] ] = matrix[ posX + k ][ posY + l ];
               }
            }
         }
      }

      return downsample;
   }

   /**
    * Since we're dealing with Hilbert mappings, we assume the matrices are square and a power of 2.
    * This method will take an input matrix and produce a matrix of overlapping matrices composed from the
    * input matrix of size width.
    *
    * @param int[][]
    * @param int -- the size of each tiled sub-matrix within the larger matrix.
    * @return Size is: n x n x width x width
    */
   public int[][][][] getHilbertTiling( int[][] data, int width )
   {
      int         offset = width / 2;
      int         n      = (data.length / offset) - 1;
      int[][][][] tiling = new int[ n ][ n ][][];
      int[][]     t      = null;

      for( int i=0; i<n; i++ )
      {
         for( int j=0; j<n; j++ )
         {
            t = new int[ width ][ width ];
            for( int k=0; k<width; k++ )
            {
               for( int w=0; w<width; w++ )
               {
                  t[ k ][ w ] = data[ (i * offset) + k ][ (j * offset) + w ];
               }
            }
            tiling[ i ][ j ] = t;
         }
      }

      return tiling;
   }

   /**
    * This is basically the same as getHilbertTiling except that the sub-matrix is mapped into an array list
    * using the Hilbert curve. 
    * NOTE: the resulting array dimensions may not be the same as the data dimension divided by width.
    *       it will, in general, be data.length / width - 1.
    *
    * @param TYPE
    * @return TYPE
    */
   public int[][][] getHilbertTilingArrayList( int[][] data, int width )
   {
      int[][][][] tiling    = getHilbertTiling( data, width );
      int[][][]   arrayList = new int[ tiling.length ][ tiling.length ][];

      for( int i=0; i<tiling.length; i++ )
      {
         for( int j=0; j<tiling.length; j++ )
         {
            arrayList[ i ][ j ] = getHilbertArrayList( tiling[ i ][ j ] );
         }
      }

      return arrayList;
   }

   /**
    * This method will turn the power-of-two length matrix into a Hilbert curve mapped array list.
    *
    * @param TYPE
    * @return TYPE
    */
   public int[] getHilbertArrayList( int[][] data )
   {
      int[] curve = new int[ data.length * data.length ];

      for( int i=0; i<data.length; i++ )
      {
         for( int j=0; j<data[ i ].length; j++ )
         {
            curve[ getHilbertDistance( i, j, data.length ) ] = data[ i ][ j ];
         }
      }

      return curve;
   }

   /**
    * This method will turn the power-of-two length matrix into a Hilbert curve mapped array list
    * with the exception that the input array need not be square or a power of two. This is 
    * accomplished via a larger "virtual" hilbert curve. If this virtual hilbert curve lies outside
    * the matrix, then the corresponding entry in the array will be -1.
    * First, we must find the log_2 of the max dimension of the matrix. Then we calculate the 
    * ceiling of this result, then exponentiate 2 to this power -- this will give us the size of the
    * virtual hilbert matrix. To do this, we need to use the law of logarithms: 
    * since b^( log_b( x ) ) = x  -- this is by the very definition of logarithms, then if we take 
    * the logaritm to the base of a of both sides, we get:
    * log_a( b^( log_b( x ) ) ) = log_a( x ) which of course simplifies to:
    * log_a( b ) * log_b( x )   = log_a( x )
    *
    * @param any size matrix.
    * @return the array mapped int array of the Hilbert mapping.
    */
   public double[] getHilbertArray( double[][] data )
   {
      // log_e( 2 ) * log_2( x ) = log_e( x ) ==> log_2( x ) = log_e( x ) / log_e( 2 )
      int      pos   = -1;
      int      size  = Math.max( data.length, data[ 0 ].length );
      double   log_2 = Math.ceil( Math.log( size ) / Math.log( 2 ) );
      size           = (int) Math.pow( 2.0, log_2 );
      double[] curve = new double[ size * size ];

      for( int i=0; i<size; i++ )
      {
         for( int j=0; j<size; j++ )
         {
            pos = getHilbertDistance( i, j, size );
            if( i >= data.length || j >= data[ 0 ].length )
            {
               curve[ pos ] = -1.0;
            }
            else
            {
               curve[ pos ] = data[ i ][ j ];
            }
         }
      }

      return curve;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public double[] getSMAArray( double[] a, double s )
   {
      double[]  sma = new double[ a.length ];
      sma[ 0 ]      = a[ 0 ];

      for( int j=1; j<a.length; j++ )
      {
         if( a[ j ] < 0 )
         {
            sma[ j ] = sma[ j - 1 ];
         }
         else
         {
            sma[ j ] = ( sma[ j - 1 ] + ( s * ( a[ j ] - sma[ j - 1 ] ) ) );
         }
      }
      
      return sma;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public double[] getSMAHilbertArray( double[][] data, double s )
   {
      double[] h   = getHilbertArray( data );
      double[] m   = getSMAArray( h, s );
      double[] sma = new double[ h.length ];

      for( int i=0; i<h.length; i++ )
      {
         if( h[ i ] < 0 )
         {
            sma[ i ] = -1;
         }
         else
         {
            sma[ i ] = Math.abs( h[ i ] - m[ i ] );
         }
      }

      return sma;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public double[][] getHilbertMatrix( double[] a, int w, int h )
   {
      double[][] m = new double[ w ][ h ];
      int[]      p = null;

      for( int i=0; i<a.length; i++ )
      {
         if( a[ i ] >= 0 )
         {
            p                     = getHilbertPair( i, a.length );
            m[ p[ 0 ] ][ p[ 1 ] ] = a[ i ];
         }
      }

      return m;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public double[][] multiply( double[][] a, double[][] b )
   {
      double[][] c = new double[ a.length ][ b[ 0 ].length ];

      for( int i=0; i<c.length; i++ )
      {
         for( int j=0; j<c[ 0 ].length; j++ )
         {
            for( int k=0; k<a.length; k++ )
            {
               c[ i ][ j ] += a[ i ][ k ] * b[ j ][ k ];
            }
         }
      }

      return c;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public double[] multiply( double[] a, double[][] b )
   {
      double[] c = new double[ b[ 0 ].length ];

      for( int j=0; j<b[ 0 ].length; j++ )
      {
         for( int i=0; i<a.length; i++ )
         {
            c[ j ] += ( a[ i ] * b[ i ][ j ] );
         }
      }

      return c;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public double[] getRandomArray( int s )
   {
      double[] a = new double[ s ];

      for( int i=0; i<a.length; i++ )
      {
         a[ i ] = random();
      }

      return a;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public double[][] getRandomMatrix( int w, int h )
   {
      double[][] a = new double[ w ][ h ];

      for( int i=0; i<a.length; i++ )
      {
         for( int j=0; j<a[ 0 ].length; j++ )
         {
            a[ i ][ j ] = random();
         }
      }

      return a;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void printMatrix( double[][] a )
   {
      System.out.println( "" );
      for( int k=0; k<a.length; k++ )
      {
         System.out.println( "" );
         for( int r=0; r<a[ 0 ].length; r++ )
         {
            System.out.print( a[ k ][ r ] + " " );
         }
      }
      System.out.println( "" );
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void printArray( double[] a )
   {
      System.out.println( "" );
      for( int k=0; k<a.length; k++ )
      {
         System.out.print( a[ k ] + " " );
      }
      System.out.println( "" );
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public DAOBase getDAO()
   {
      return null;
   }

   /**
    *
    * @param double -- Percentage of the number of non-zero entries to attempt a random speckle darkenting.
    */
   public void makeSparse( double[][] matrix, double p )
   {
      ArrayList< Integer > xlist = new ArrayList< Integer >();
      ArrayList< Integer > ylist = new ArrayList< Integer >();
      long                 count = 0;

      for( int i=0; i<matrix.length; i++ )
      {
         for( int j=0; j<matrix[ 0 ].length; j++ )
         {
            if( matrix[ i ][ j ] > 0.0 )
            {
               xlist.add( i );
               ylist.add( j );
               count++;
            }
         }
      }

      Collections.sort( xlist );
      Collections.sort( ylist );

      int x = 0;
      int y = 0;
      count = (long) ( (double) count * p );

      System.out.println( "count is: " + count );

      for( int k=0; k<count; k++ )
      {
         x                = xlist.get( (int) ( (double) xlist.size() * random() ) );
         y                = ylist.get( (int) ( (double) ylist.size() * random() ) );
         matrix[ x ][ y ] = 0.0;
      }
   }

   /**
    *
    * @param double -- Percentage of the number of non-zero entries to attempt a random speckle darkenting.
    */
   public void makeSparseRandom( double[][] matrix, double p )
   {
      for( int i=0; i<matrix.length; i++ )
      {
         for( int j=0; j<matrix[ 0 ].length; j++ )
         {
            if( random() <= p )
            {
               matrix[ i ][ j ] = 0.0;
            }
         }
      }
   }

   /**
    *
    * @param double -- Percentage of the number of non-zero entries to attempt a random speckle dithering.
    */
   public void makeNonSparseRandom( double[][] matrix, double p, int c )
   {
      for( int i=0; i<matrix.length; i++ )
      {
         for( int j=0; j<matrix[ 0 ].length; j++ )
         {
            if( random() <= p && matrix[ i ][ j ] == 0 )
            {
               int xstart = ( i == 0 ) ? 0 : i - 1;
               int ystart = ( j == 0 ) ? 0 : j - 1;
               int xend   = ( i == matrix.length - 1 )      ? matrix.length - 1      : i + 2;
               int yend   = ( j == matrix[ 0 ].length - 1 ) ? matrix[ 0 ].length - 1 : j + 2;
               int count  = 0;

               for( int m=xstart; m<xend; m++ )
               {
                  for( int n=ystart; n<yend; n++ )
                  {
                     if( matrix[ m ][ n ] > 0.0 )
                     {
                        count++;
                     }
                  }
               }
               if( count > c )
               {
                  matrix[ i ][ j ] = 1.0;
               }
            }
         }
      }
   }

   /**
    *
    * @param double -- Percentage of the number of non-zero entries to attempt a random speckle darkenting.
    */
   public void makeNonSparse( double[][] matrix, double p )
   {
      ArrayList< Integer > xlist = new ArrayList< Integer >();
      ArrayList< Integer > ylist = new ArrayList< Integer >();
      long                 count = 0;

      for( int i=0; i<matrix.length; i++ )
      {
         for( int j=0; j<matrix[ 0 ].length; j++ )
         {
            if( matrix[ i ][ j ] > 0.0 )
            {
               xlist.add( i );
               ylist.add( j );
               count++;
            }
         }
      }

      Collections.sort( xlist );
      Collections.sort( ylist );

      int x = 0;
      int y = 0;
      count = (long) ( (double) count * p );

//       System.out.println( "count is: " + count );

      for( int k=0; k<count; k++ )
      {
         x                = xlist.get( (int) ( (double) xlist.size() * random() ) );
         y                = ylist.get( (int) ( (double) ylist.size() * random() ) );
         matrix[ x ][ y ] = 1.0;
      }
   }

   /**
    * Analgous to the idea of mapping an arbitrary interval [a, b] into [0, 1],
    * we map percentiles of a vector into percentiles of another.
    * The relationship is based on i / N = j / M
    * where i is the current index of vector 1 of length N (size)
    * and j is the current index of vector 2 of length M (vector.length)
    */
   public double[] getReducedVector( double[] vector, int size )
   {
      double[] result = new double[ size ];
      double   pvalue = (double) vector.length / (double) size;

      for( int i=0; i<size; i++ )
      {
         result[ i ] = vector[ (int) ( i * pvalue ) ];
      }

      return result;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public double[][] getReducedMatrix( double[][] matrix, int w, int h )
   {
      double[][] result = new double[ w ][ h ];
      double[][] holder = new double[ matrix.length ][];
      double[]   vector = null;

      // first we go through and handle the columns
      for( int i=0; i<matrix.length; i++ )
      {
         vector = getReducedVector( matrix[ i ], h );
         holder[ i ] = vector;
      }

      // next, we go through and handle the rows
      for( int j=0; j<holder[ 0 ].length; j++ )
      {
         vector = new double[ holder.length ];
         for( int s=0; s<holder.length; s++ )
         {
            vector[ s ] = holder[ s ][ j ];
         }
         vector = getReducedVector( vector, w );
         for( int t=0; t<vector.length; t++ )
         {
            result[ t ][ j ] = vector[ t ];
         }
      }

      return result;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public double[] getContinuous1DVariable( int length )
   {
      double   step     = 1.0 / (double) length;
      double[] variable = new double[ length ];

      for( int i=1; i<variable.length; i++ )
      {
         variable[ i ] = variable[ i - 1 ] + step;
      }

      return variable;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
   public static double[] deepCopy( double[] v )
   {
      double[] vector = new double[ v.length ];

      for( int i=0; i<v.length; i++ )
      {
         vector[ i ] = new Double( v[ i ] );
      }

      return vector;
   }
    */

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public static Double[] deepCopy( double[] v )
   {
      Double[] vector = new Double[ v.length ];

      for( int i=0; i<v.length; i++ )
      {
         vector[ i ] = new Double( v[ i ] );
      }

      return vector;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public static double[][] deepCopy( double[][] m )
   {
      double[][] matrix = new double[ m.length ][ m[ 0 ].length ];

      for( int i=0; i<m.length; i++ )
      {
         for( int j=0; j<m[ 0 ].length; j++ )
         {
            matrix[ i ][ j ] = m[ i ][ j ];
         }
      }

      return matrix;
   }

   /**
    *
    * @param BigInteger
    * @param BigInteger
    * @return double The distance as measured by the tree path distance.
    */
   public BigInteger getDistance( BigInteger a, BigInteger b )
   {
      BigInteger d = null;

      if( a.bitLength() < b.bitLength() )
      {
         int c = b.bitLength() - a.bitLength();
         a = a.shiftLeft( c );
      }
      else if( a.bitLength() > b.bitLength() )
      {
         int c = a.bitLength() - b.bitLength();
         b = b.shiftLeft( c );
      }

      d = a.xor( b );

      return d;
   }

}
