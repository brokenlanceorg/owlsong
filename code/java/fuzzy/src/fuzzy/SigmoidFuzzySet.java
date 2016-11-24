package fuzzy;

import java.math.*;

/**
 * Implements a Sigmoid Fuzzy Set.
 * A Sigmoid FuzzySet requires three parameters to create.
 * the left endpoint, the right endpoint, and whether or not
 * it's increasing or decreasing.
 * For the sigmoid, we use tanh, thus, we must map the 
 * imput domain into [-pi, pi] then add 1 and divide by 2.
 */
public class SigmoidFuzzySet extends FuzzySet
{

   // Keeps track of the length of the domain interval.
   private double _length;

   /**
    * Constructor.
    */
   public SigmoidFuzzySet( double left, double right, double increasing )
   {
      super( left, right, increasing );
      _length = _rightEndpoint - _leftEndpoint;
      if( _length == 0 )
      {
         _myValue = -1;
      }
   }

   /**
    * Returns the tuthValue for the given observable.
    */
   public double evaluate( double observable )
   {
      // Check that we wouldn't have a divide-by-zero
      if( _myValue != -1 )
      {
         double arg = (observable - _leftEndpoint) / _length;
         arg *= 2 * Math.PI;
         arg -= Math.PI;
         _myValue = Math.tanh( arg );
         _myValue += 1;
         _myValue /= 2;
         if( !_isIncreasing )
         {
            _myValue = 1 - _myValue;
         }
      }

      return _myValue;
   }

   /**
    * For testing purposes only.
    */
   public static void main( String[] args )
   {
      FuzzySet aSet = new SigmoidFuzzySet( 0.01, 4.90, 0.2 );
      aSet.setNotOperator();

      double arg = -0.5;
      while( arg <= 5 )
      {
         System.out.println( arg + "," + aSet.truthValue( arg ) );
         arg += 0.05;
      }
   }

}
