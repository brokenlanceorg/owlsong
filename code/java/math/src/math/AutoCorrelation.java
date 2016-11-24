package math;

import java.util.*;

/**
 * Computes the auto correlation of a data set.
 */
public class AutoCorrelation
{

   ArrayList<Double> _dataSet;

   /**
    * We assume the data set has been normalized and prep'd.
    */
   public AutoCorrelation( ArrayList<Double> values )
   {
      _dataSet = values;
   }

   /**
    * The basic idea is as follows:
    *  for each lag value (which is as many as the data set has)
    *     compute dot product for appropriate values
    *
    *
    */
   public ArrayList<Double> getCorrelationSet()
   {
      ArrayList<Double> copy = new ArrayList<Double>( _dataSet );
      ArrayList<Double> correlationSet = new ArrayList<Double>();

      // i is the lag
      for( int i=0; i<_dataSet.size(); i++ )
      {
         double temp = 0;
         for( int j=0; j<(_dataSet.size() - i); j++ )
         {
            temp += (copy.get( j ) * _dataSet.get(i + j));
         }
         correlationSet.add( temp );
      }

      return correlationSet;
   }

}
