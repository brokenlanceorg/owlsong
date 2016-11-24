package math;

import java.util.*;

/**
 *
 */
public class MedianSort
{

   /**
    *
    */
   public MedianSort()
   {
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void sort( ArrayList< Double > a )
   {
      ArrayDeque< Segment > queue   = new ArrayDeque< Segment >();
      Segment               segment = null;

      queue.add( new Segment( 0, a.size() - 1 ) );

      while( ( segment = queue.poll() ) != null )
      {
         quickQuantile( a, segment.getMidpoint(), segment.getLeft(), segment.getRight() );
         if( ( segment.getMidpoint() - segment.getLeft() ) >= 2 )
         {
            queue.add( new Segment( segment.getLeft(), segment.getMidpoint() ) );
         }
         if( ( segment.getRight() - segment.getMidpoint() ) >= 2 )
         {
            queue.add( new Segment( segment.getMidpoint(), segment.getRight() ) );
         }
      }
      
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void sort( int[] a )
   {
      ArrayDeque< Segment > queue = new ArrayDeque< Segment >();
      Segment               segment = null;

      queue.add( new Segment( 0, a.length - 1 ) );

      while( ( segment = queue.poll() ) != null )
      {
         quickQuantile( a, segment.getMidpoint(), segment.getLeft(), segment.getRight() );
         if( ( segment.getMidpoint() - segment.getLeft() ) >= 2 )
         {
            queue.add( new Segment( segment.getLeft(), segment.getMidpoint() ) );
         }
         if( ( segment.getRight() - segment.getMidpoint() ) >= 2 )
         {
            queue.add( new Segment( segment.getMidpoint(), segment.getRight() ) );
         }
      }
      
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void sort( double[] a )
   {
      ArrayDeque< Segment > queue = new ArrayDeque< Segment >();
      Segment               segment = null;

      queue.add( new Segment( 0, a.length - 1 ) );

      while( ( segment = queue.poll() ) != null )
      {
         quickQuantile( a, segment.getMidpoint(), segment.getLeft(), segment.getRight() );
         if( ( segment.getMidpoint() - segment.getLeft() ) >= 2 )
         {
            queue.add( new Segment( segment.getLeft(), segment.getMidpoint() ) );
         }
         if( ( segment.getRight() - segment.getMidpoint() ) >= 2 )
         {
            queue.add( new Segment( segment.getMidpoint(), segment.getRight() ) );
         }
      }
      
   }

   /**
    * This method computes the median using a "quick" divide and conquer
    * method analagous to the quick sort sorting algorithm.
    *
    *
    * @param double[] -- the array of data. NOTE: the array is modified during this procedure.
    * @param int -- the quantile to compute: n / 2 for median, n / 4 for the first quartile, etc.
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
    * This method computes the median using a "quick" divide and conquer
    * method analagous to the quick sort sorting algorithm.
    *
    *
    * @param double[] -- the array of data. NOTE: the array is modified during this procedure.
    * @param int -- the quantile to compute: n / 2 for median, n / 4 for the first quartile, etc.
    */
   public void quickQuantile( int[] a, int k, int l, int r )
   {
      int i;
      int j;
      int x = 0;
      int t = 0;

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
    * This method computes the median using a "quick" divide and conquer
    * method analagous to the quick sort sorting algorithm.
    *
    *
    * @param double[] -- the array of data. NOTE: the array is modified during this procedure.
    * @param int -- the quantile to compute: n / 2 for median, n / 4 for the first quartile, etc.
    */
   public void quickQuantile( ArrayList< Double > a, int k, int l, int r )
   {
      int i;
      int j;
      double x = 0;
      double t = 0;

      while( l < r )
      {
         x = a.get( k );
         i = l;
         j = r;
         do
         {
            while( a.get( i ) < x )
            {
               i++;
            }
            while( x < a.get( j ) )
            {
               j--;
            }
            if( i <= j )
            {
               t = a.get( i );
               a.set( i, a.get( j ) );
               a.set( j, t );
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
    *
    * @param TYPE
    * @return TYPE
    */
   public double quickMedian( double[] a )
   {
      int k = a.length >> 1;
      quickQuantile( a, k, 0, a.length - 1 );
      return a[ k ];
   }

   /**
    *
    */
   public class Segment
   {
      private int _left;
      private int _right;
      private int _midpoint;

      /**
       *
       */
      public Segment()
      {
      }

      /**
       *
       */
      public Segment( int l, int r )
      {
         setLeft( l );
         setRight( r );
         setMidpoint( l + ( ( r - l ) >> 1 ) );
      }

      /**
       *
       * @param int
       */
      public void setLeft( int left )
      {
         _left = left;
      }

      /**
       *
       * @return int
       */
      public int getLeft()
      {
         return _left;
      }

      /**
       *
       * @param int
       */
      public void setRight( int right )
      {
         _right = right;
      }

      /**
       *
       * @return int
       */
      public int getRight()
      {
         return _right;
      }

      /**
       *
       * @param int
       */
      public void setMidpoint( int midpoint )
      {
         _midpoint = midpoint;
      }

      /**
       *
       * @return int
       */
      public int getMidpoint()
      {
         return _midpoint;
      }

   }

}
