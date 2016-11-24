package fuzzy;

import java.math.*;

/**
 * Implements a Gaussian Fuzzy Set.
 * A Gaussian FuzzySet requires two parameters to create.
 *
 * The formula for the Gaussian or normal curve is:
 * g( x ) = exp( -1 * ( (x - b)^2 / (2 * width)^2 ) )
 */
public class GaussianFuzzySet extends FuzzySet
{

   // Stores the denominator in the formula.
   private double _denominator;

   /**
    * Constructor.
    */
   public GaussianFuzzySet( double center, double width )
   {
      super( center, width );

      _denominator = 2 * _rightEndpoint;
      _denominator *= _denominator;
      _denominator *= -1;

      if( _denominator == 0 )
      {
         _myValue = -1;
      }
   }

   /**
    * Returns the tuthValue for the given observable.
    * Override FuzzySet's implmentation since the 
    * bell-curve sets operate a little differently.
    */
   public double truthValue( double observable )
   {
      evaluate( observable );

      if( _isNOT )
      {
         _myValue = 1 - _myValue;
      }

      return _myValue;
   }

   /**
    * Returns the tuthValue for the given observable.
    * The formula for the Gaussian or normal curve is:
    * g( x ) = exp( -1 * ( (x - b)^2 / (2 * width)^2 ) )
    */
   public double evaluate( double observable )
   {
      // Check that we wouldn't have a divide-by-zero
      if( _myValue != -1 )
      {
         double arg = (observable - _leftEndpoint);
         arg *= arg;
         arg /= _denominator;
         _myValue = Math.exp( arg );
      }

      return _myValue;
   }

   /**
    * For testing purposes only.
    */
   public static void main( String[] args )
   {
      FuzzySet aSet = new GaussianFuzzySet( 3, 1 );
      aSet.setNotOperator();

      double arg = -0.5;
      while( arg <= 15 )
      {
         System.out.println( arg + "," + aSet.truthValue( arg ) );
         arg += 0.05;
      }
   }

}
