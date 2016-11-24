package math.cluster;

import java.util.*;
import java.math.*;

/**
 * This class will provide the basic implementations of a Vector
 * Quantization implementation of an online quantile collection.
 */
public abstract class VQQuantileBase implements VQQuantile
{
   protected TreeSet< Double > _quantiles   = new TreeSet< Double >();
   private   int               _precision   = 0;

   /**
    *
    */
   public VQQuantileBase()
   {
   }

   /**
    * This method determines the level of precision for the quantiles
    * by determining the number of "buckets" to maintain for each 
    * quantile value.
    * @param int -- The number of quantile "buckets" to maintain.
    */
   public void setPrecision( int n )
   {
      _precision = n;
   }

   /**
    * This method will return the value for the smallest quantile that is 
    * greater than or equal to q.
    * @param  double -- the qth quantile
    * @return double -- the underlying value for the qth quantile
    */
   public double getQuantile( double q )
   {
      return Double.MAX_VALUE;
   }

   /**
    * The value d will be added to the quantile data collection.
    * This method will run in log( n ) time where n is the precision
    * of the quantile collection.
    */
   public void addDataPoint( double d )
   {
      Double v = new Double( d );
      _quantiles.add( v );
      if( _quantiles.size() > _precision )
      {
         purge( v );
      }
   }

   /**
    * Purge an item from the quantile representation to maintain the precision.
    */
   protected abstract void purge( Double d );

}
