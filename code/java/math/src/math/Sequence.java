package math;

import java.util.*;

/**
 * This class will take a data sequence and compute various transformations
 * and transpositions on that data.
 */
public class Sequence
{

   // here we hold the raw data sequence
   private ArrayList< Double > _sequence;

   /**
    *
    */
   public Sequence()
   {
   }

   /**
    *
    */
   public Sequence( ArrayList< Double > data )
   {
      setSequence( data );
   }

   /**
    *
    */
   public void setSequence( ArrayList< Double > data )
   {
      _sequence = data;
   }

   /**
    *
    */
   public ArrayList< Double > getSequence()
   {
      return _sequence;
   }

   /**
    * Based on the window size parameter, this method will transpose a linear
    * sequence into a "moving window" of separate independent sequences. 
    * For example, the data sequence:
    * 1
    * 2
    * 3
    * 4
    * 5
    * 6
    * 7
    * 8 
    * 9
    * with a window size of 3 will be transposed into the following 3 variables (sequences):
    * 1 2 3
    * 2 3 4
    * 3 4 5
    * 4 5 6
    * 5 6 7 
    * 6 7 8
    * 7 8 9
    *
    * Another example with a window size of 5:
    * 1
    * 2
    * 3
    * 4
    * 5
    * 6
    * 7
    * 8
    * 9
    * maps to the following variables:
    * 1 2 3 4 5
    * 2 3 4 5 6
    * 3 4 5 6 7 
    * 4 5 6 7 8 
    * 5 6 7 8 9
    *
    * @param int the window size or number of independent variables to produce
    * @return a two dimensional array of doubles.
    */
   public ArrayList< ArrayList< Double > > getTransposedSequence( int windowSize )
   {
      ArrayList< ArrayList< Double > > trans = new ArrayList< ArrayList< Double > >( windowSize );

      for( int i=0; i<windowSize; i++ )
      {
         ArrayList< Double > temp = new ArrayList< Double >( _sequence.size() );
         for( int j=i; j<(_sequence.size() - (windowSize - i - 1)); j++ )
         {
            temp.add( _sequence.get( j ) );
         }
         trans.add( temp );
      }

      return trans;
   }

}
