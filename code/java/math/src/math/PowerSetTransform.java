package math;

import java.util.*;

/**
 * This class will calculate the power set of the given set.
 * Right now, we will only support type Double.
 *
 */
public class PowerSetTransform
{

   /**
    *
    */
   public PowerSetTransform()
   {
   }

   /**
    *
    */
   public ArrayList<ArrayList<Double>> loopMethod( ArrayList<Double> input )
   {
      // the length of zeros
      int powerSetSize = (int)Math.pow( 2, input.size() );
      int modulus = powerSetSize / 2;
      boolean incexcl = true;
      
      ArrayList<ArrayList<Double>> resultList = new ArrayList<ArrayList<Double>>( powerSetSize );
      
      // for the size of the input set
      for( int i=0; i<input.size(); i++ )
      {
         for( int j=0; j<powerSetSize; j++ )
         {
            if( (j % modulus) == 0 )
            {
               incexcl = !incexcl;
            }
      
            // incexcl now holds the truth value
            if( incexcl )
            {
               ArrayList<Double> setList = null;
               if( resultList.size() <= j )
               {
                  setList = new ArrayList<Double>();
                  resultList.add( setList );
               }
               else
               {
                  setList = resultList.get( j );
               }
               setList.add( input.get( i ) );
            }
         }
         modulus /= 2;
      }

      return resultList;
   }

   /**
    *
    */
   public ArrayList<ArrayList<Double>> maskMethod( ArrayList<Double> input )
   {
//      System.out.println( "PowerSetTransform::maskMethod" );
      int powerSetSize = (int)Math.pow( 2, input.size() );
      ArrayList<ArrayList<Double>> resultList = new ArrayList<ArrayList<Double>>( powerSetSize );

      // start at 1 since 0 is the empty set.
      for( int i=1; i<powerSetSize; i++ )
      {
         ArrayList<Double> set = new ArrayList<Double>();
         resultList.add( set );
         int bitPosition = 1;
         for( int j=0; j<input.size(); j++ )
         {
            if( (bitPosition & i) > 0 )
            {
               set.add( input.get( j ) );
//                System.out.println( i + " adding element: " + input.get( j ) );
            }
            bitPosition *= 2;
         }
      }

      return resultList;
   }

   /**
    * This method will use the best method and will also collapse the
    * sets down to specific values.
    * To transform the sets into single values, we will perform the following
    * on each set greater than one element (polynomial sort of transformations):
    *  multiplication
    *  division pre
    *  division post
    *  addition
    *  subtraction
    *  bitwise AND  -- only for Int
    *  bitwise OR   -- only for Int
    *  bitwise XOR  -- only for Int
    *
    * The number of parameters this approach can generate is given by:
    * n = (2^m - (m + 1)) * 5 + m
    * where m is the number of input parameters.
    */
   public ArrayList<Double> calculateTransform( ArrayList<Double> input )
   {
//      System.out.println( "PowerSetTransform::calculateTransform" );

//      System.out.println( "PowerSetTransform::calculateTransform before mask" );
      ArrayList<ArrayList<Double>> powerSet = maskMethod( input );
      ArrayList<Double> transform = new ArrayList<Double>( powerSet.size() * 5 );
//      System.out.println( "PowerSetTransform::calculateTransform after mask: " + transform.size() );

      // for each power set
      for( ArrayList<Double> set : powerSet )
      {
//         System.out.println( "PowerSetTransform::calculateTransform for loop 1" );
         // First multiplicative
         Double value = set.get( 0 );
         // in this case, we must map the values to a single value
         if( set.size() > 1 )
         {
            Double current = null;
            for( int i=1; i<set.size(); i++ )
            {
               current = set.get( i );
               value *= current;
            }
         }
         else
         {
            value *= value;
         }
         transform.add( normalize( value ) );

//         System.out.println( "PowerSetTransform::calculateTransform for loop 2" );
         // next division pre
         value = set.get( 0 );
         // in this case, we must map the values to a single value
         if( set.size() > 1 )
         {
            Double current = null;
            for( int i=1; i<set.size(); i++ )
            {
               current = set.get( i );
               value /= current;
            }
         }
         else
         {
            value *= Math.PI;
         }
         transform.add( normalize( value ) );

//         System.out.println( "PowerSetTransform::calculateTransform for loop 3" );
         // next division post
         value = set.get( 0 );
         // in this case, we must map the values to a single value
         if( set.size() > 1 )
         {
            Double current = null;
            for( int i=1; i<set.size(); i++ )
            {
               current = set.get( i );
               value = current / value;
            }
         }
         else
         {
            value *= Math.E;
         }
         transform.add( normalize( value ) );

//         System.out.println( "PowerSetTransform::calculateTransform for loop 4" );
         // next addition
         value = set.get( 0 );
         // in this case, we must map the values to a single value
         if( set.size() > 1 )
         {
            Double current = null;
            for( int i=1; i<set.size(); i++ )
            {
               current = set.get( i );
               value += current;
            }
         }
         else
         {
            value += value;
         }
         transform.add( normalize( value ) );

//         System.out.println( "PowerSetTransform::calculateTransform for loop 5" );
         // next subtraction
         value = set.get( 0 );
         // in this case, we must map the values to a single value
         if( set.size() > 1 )
         {
            Double current = null;
            for( int i=1; i<set.size(); i++ )
            {
               current = set.get( i );
               value -= current;
            }
         }
         else
         {
            value = Math.pow( value, Math.PI );
         }
         transform.add( normalize( value ) );
      }

//      System.out.println( "PowerSetTransform::calculateTransform exit" );
      return transform;
   }

   /**
    * Maps the value back into [0, 1]
    */
   public Double normalize( Double value )
   {
      double temp = Math.abs( value );
      temp -= Math.floor( temp );

      return (temp);
   }

   /**
    *
    */
   public static void main( String[] args )
   {
      long start = 0;
      long total = 0;
      int count = 1;

      for( int k=0; k<count; k++ )
      {
         ArrayList<Double> testValues = new ArrayList<Double>();
         System.out.println( "Input set values:" );
         for( int i=0; i<3; i++ )
         {
            Double it = Math.random();
            System.out.println( it );
            testValues.add( it );
         }

         start = System.currentTimeMillis();
         PowerSetTransform ps = new PowerSetTransform();
         ArrayList<Double> powerSet = ps.calculateTransform( testValues );
         total += (System.currentTimeMillis() - start);

         System.out.println( "Powet set size: " + powerSet.size() );
/*
         System.out.println( "Powet set values:" );
         for( int i=0; i<powerSet.size(); i++ )
         {
            System.out.println( powerSet.get( i ) );
         }
*/
         short position = 30000;
         for( int i=0; i<60000; i++ )
         {
            System.out.println( "position is: " + Math.abs( position++ ) );
         }
      }

      double average = (double)total / (double)count;
      System.out.println( "The average was: " + average );
   }
}
