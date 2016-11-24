package math.cluster;

/**
 * This interface defines the Vector Quantization Quantile framework.
 * The technique of Vector Quantization is uesed to collect quantile
 * statistics for a very large and online dataset.
 */
public interface VQQuantile
{

   /**
    * This method determines the level of precision for the quantiles
    * by determining the number of "buckets" to maintain for each 
    * quantile value.
    * @param int -- The number of quantile "buckets" to maintain.
    */
   public void setPrecision( int n );

   /**
    * This method will return the value for the smallest quantile that is 
    * greater than or equal to q.
    * @param  double -- the qth quantile
    * @return double -- the underlying value for the qth quantile
    */
   public double getQuantile( double q );

   /**
    * The value d will be added to the quantile data collection.
    * This method will run in log( n ) time where n is the precision
    * of the quantile collection.
    */
   public void addDataPoint( double d );

}
