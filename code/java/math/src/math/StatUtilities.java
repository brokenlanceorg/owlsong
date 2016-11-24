package math;

import java.util.*;
import common.*;

/**
 *
 * This class will calculate the mean, variance, and standard deviation for a data sequence.
 *
 */
public class StatUtilities implements Persistable
{

   // holds the various values that will be calculated
   private double   _mean;
   private double   _median;
   private double   _deviation;
   private double[] _data;

   /**
    * Call this constructor to use the quick median method.
    */
   public StatUtilities()
   {
   }

   /**
    *
    */
   public StatUtilities( double[] data )
   {
      _data = data;
      calculateStats( data );
   }

   /**
    *
    */
   public StatUtilities( ArrayList< Double > data )
   {
      setData( data );
      calculateStats( _data );
   }

   /**
    * @param TYPE
    * @return TYPE
    */
   public void setData( ArrayList< Double > data )
   {
      _data = new double[ data.size() ];

      for( int i=0; i<_data.length; i++ )
      {
         _data[ i ] = data.get( i ).doubleValue();
      }
   }

   /**
    * @param TYPE
    * @return TYPE
    */
   public double[] getData()
   {
      return _data;
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
    */
   public double getMean()
   {
      return _mean;
   }

   /**
    *
    */
   public double getMedian()
   {
      return _median;
   }

   /**
    *
    */
   public double getDeviation()
   {
      return _deviation;
   }

   /**
    * Don't pass in null or empty lists!
    */
   public void calculateStats( ArrayList< Double > data )
   {
      double sum = 0;

      // first, calculate the mean
      for( double value : data )
      {
         sum += value;
      }
      _mean = sum / data.size();

      // next, calculate the variance
      double temp = 0;
             sum  = 0;
      for( double value : data )
      {
         temp  = ( value - _mean );
         temp *= temp;
         sum  += temp;
      }
      sum /= (double)(data.size());

      // finally, valculate the deviation
      _deviation = Math.sqrt( sum );

//       long start = System.currentTimeMillis();
      // calculate the median
      ArrayList< Double > sorted = (ArrayList< Double >) data.clone();
      Collections.sort( sorted );
      int mod = sorted.size() % 2;
      if( mod == 0 )
      {
         int    pos   = sorted.size() / 2;
         double left  = sorted.get( pos );
         double right = sorted.get( pos - 1 );
         _median = ( left + right ) / 2;
      }
      else
      {
         _median = sorted.get( (sorted.size() / 2) );
      }
//       System.out.println( "median time: " + ( System.currentTimeMillis() - start ) );
   }

   /**
    * Don't pass in null or empty lists!
    */
   public void calculateStats( double[] data )
   {
      double sum = 0;

      // first, calculate the mean
      for( double value : data )
      {
         sum += value;
      }
      _mean = sum / data.length;

      // next, calculate the variance
      double temp = 0;
             sum  = 0;
      for( double value : data )
      {
         temp  = ( value - _mean );
         temp *= temp;
         sum  += temp;
      }
      sum /= (double)(data.length);

      // finally, valculate the deviation
      _deviation = Math.sqrt( sum );
      ArrayList< Double > sorted = new ArrayList< Double >();
      for( int i=0; i<data.length; i++ )
      {
          sorted.add( data[ i ] );
      }
      Collections.sort( sorted );
      int mod = sorted.size() % 2;
      if( mod == 0 )
      {
         int    pos   = sorted.size() / 2;
         double left  = sorted.get( pos );
         double right = sorted.get( pos - 1 );
         _median = ( left + right ) / 2;
      }
      else
      {
         _median = sorted.get( (sorted.size() / 2) );
      }
   }

   /**
    * @see quickQuantile( double[]a, int k )
    * @param TYPE
    * @return TYPE
    */
   public double quickMedian( double[] a )
   {
      int n = a.length >> 1;
      quickQuantile( a, n, 0, a.length - 1 );
      _median = a[ n ];
      return _median;
   }

   /**
    * This method computes the median using a "quick" divide and conquer
    * method analagous to the quick sort sorting algorithm.
    *
    *
    * @param double[] -- the array of data. NOTE: the array is modified during this procedure.
    * @param int -- the quantile to compute: n / 2 for median, n / 3 for the first quartile, etc.
    */
   public void quickQuantile( double[] a, int k, int l, int r )
   {
      int i;
      int j;
      double x = 0;
      double t = 0;

      while( l < r )
      {
         x = a[ k ];
         i = l; 
         j = r;
         do
         {
            while( a[ i ] < x )
            {
               i++;
            }
            while( x < a[ j ] )
            {
               j--;
            }
            if( i <= j )
            {
               t = a[ i ];
               a[ i ] = a[ j ];
               a[ j ] = t;
               i++;
               j--;
            }
         } 
         while( i <= j );
         if( j < k )
         {
            l = i;
         }
         if( k < i )
         {
            r = j; 
         }
      }
   }

   /**
    * @param ArrayList< Double >
    * @return double -- the Pearson Correlation Coefficient
    */
   public double calculateCorrelation( ArrayList< Double > data )
   {
      StatUtilities rhs = new StatUtilities( data );
      double        cc  = 0.0;
      double[]      d2  = rhs.getData();

      for( int j=0; j<_data.length; j++ )
      {
         cc += (   ( ( _data[ j ] - getMean()     ) / getDeviation()     ) 
                 * ( (    d2[ j ] - rhs.getMean() ) / rhs.getDeviation() )  );
      }

      cc /= ( double ) ( _data.length );

      return cc;
   }

   /**
    * @param ArrayList< Double >
    * @return double -- the Pearson Correlation Coefficient
    */
   public double calculateCorrelation( StatUtilities rhs )
   {
      double   cc  = 0.0;
      double[] d2  = rhs.getData();

      for( int j=0; j<_data.length; j++ )
      {
         cc += (   ( ( _data[ j ] - getMean()     ) / getDeviation()     ) 
                 * ( (    d2[ j ] - rhs.getMean() ) / rhs.getDeviation() )  );
      }

      cc /= ( double ) ( _data.length );

      return cc;
   }

   /**
    *
   private void split( double[] a, double x, ref int i, ref int j )
   {
      double t = 0;
      do
      {
         while( a[ i ] < x )
         {
            i++;
         }
         while( x < a[ j ] )
         {
            j--;
         }
         if( i <= j )
         {
            t = a[ i ];
            a[ i ] = a[ j ];
            a[ j ] = t;
            i++;
            j--;
         }
      } 
      while( i <= j );
   }
    */

   /**
    *
    */
   public String toString()
   {
      StringBuffer data = new StringBuffer();
      data.append( "Mean value: " + _mean );
      data.append( " Median value: " + _median );
      data.append( " Deviation value: " + _deviation );
      return data.toString();
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public static void main( String[] args )
   {
      double[] a = new double[ 1000001 ];
      ArrayList< Double > al = new ArrayList< Double >();
      for( int i=0; i<1000001; i++ )
      {
         a[ i ] = MathUtilities.random();
         al.add( a[ i ] );
      }
      StatUtilities s = new StatUtilities( al );
      System.out.println( "median: " + s.getMedian() );
      long start = System.currentTimeMillis();
      System.out.println( "quick median: " + s.quickMedian( a ) );
      System.out.println( "quick median time: " + ( System.currentTimeMillis() - start ) );

      al = new ArrayList< Double >();
      al.add( 1.0 );
      al.add( 2.0 );
      al.add( 3.0 );
      al.add( 4.0 );
      al.add( 5.0 );
      al.add( 6.0 );
      al.add( 7.0 );
      al.add( 8.0 );
      al.add( 9.0 );
      ArrayList< Double > a2 = new ArrayList< Double >();
      a2.add( 1.2 );
      a2.add( 2.2 );
      a2.add( 3.2 );
      a2.add( 4.2 );
      a2.add( 5.2 );
      a2.add( 6.2 );
      a2.add( 7.2 );
      a2.add( 8.2 );
      a2.add( 90.2 );
      StatUtilities s1 = new StatUtilities( al );
      System.out.println( "CC is: " + s1.calculateCorrelation( a2 ) );
      int c = 0;
      for( int i=0; i<10; i++ )
      {
         for( int j=0; j<i; j++ )
         {
            System.out.println( "i j: " + i + " " + j );
            c++;
         }
      }
      System.out.println( "count: " + c );
   }

}
