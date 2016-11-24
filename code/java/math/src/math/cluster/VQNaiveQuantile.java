package math.cluster;

import java.util.*;

/**
 * 
 */
public class VQNaiveQuantile extends VQQuantileBase
{

   /**
    * @param TYPE
    * @return TYPE
    */
   public static void main( String[] args )
   {
      VQNaiveQuantile q = new VQNaiveQuantile();
      q.setPrecision( 40 );

      double p = 2 * Math.PI;
      double u = 0;
      double v = 0;
      double n = 0;

      for( int i=0; i<100000; i++ )
      {
         u = Math.random();
         v = Math.random();
         n = Math.sqrt( -2.0 * Math.log( u ) ) * Math.sin( p * v );
         q.addDataPoint( 2000 + ( 500 * n ) );
      }

      q.printQuantiles();
   }

   /**
    * Purge an item from the quantile representation to maintain the precision.
    * @param Double -- the value that was just added to the quantile datastore.
    */
   protected void purge( Double d )
   {
      Double a = null;
      if( Math.random() < 0.5 )
      {
         a = _quantiles.lower( d );
         if( a == null )
         {
            a = _quantiles.higher( d );
         }
      }
      else
      {
         a = _quantiles.higher( d );
         if( a == null )
         {
            a = _quantiles.lower( d );
         }
      }
      _quantiles.remove( a );
      _quantiles.remove( d );
      _quantiles.add( ( a + d ) / 2.0 );
   }

   /**
    * @param TYPE
    * @return TYPE
    */
   public void printQuantiles()
   {
      Iterator< Double > i = _quantiles.iterator();

      double previous = 0.0;
      double current  = 0.0;

      while( i.hasNext() )
      {
         current = i.next();
//          System.out.println( i.next() );
         System.out.println( current - previous );
         previous =  current;
      }
   }

}
