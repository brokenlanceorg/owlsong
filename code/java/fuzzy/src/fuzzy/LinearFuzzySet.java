package fuzzy;

/**
 * Implements a linear Fuzzy Set.
 * A linear FuzzySet requires three parameters to create.
 * the left endpoint, the right endpoint, and whether or not
 * it's increasing or decreasing.
 * The set for the increasing case goes from 
 * [left, 0] to [right, 1]
 * and for the decreasing case goes from
 * [left, 1] to [right, 0]
 */
public class LinearFuzzySet extends FuzzySet
{

   // Keeps track of the slope for this set
   private double _slope;

   // Keeps track of the intercept
   private double _intercept;

   /**
    * Constructor.
    */
   public LinearFuzzySet( double left, double right, double increasing )
   {
      super( left, right, increasing );

      // calculate slope intercept form
      // first calc slope = (y2 - y1) / (x2 - x1)
      double temp = _rightEndpoint - _leftEndpoint;
      if( temp != 0 )
      {
         _slope = 1 / temp;
         if( !_isIncreasing )
         {
            _slope *= -1;
            _intercept = -1 * (_slope * _rightEndpoint);
         }
         else
         {
            _intercept = -1 * (_slope * _leftEndpoint);
         }
      }
      else
      {
         _myValue = -1;
      }
   }

   /**
    * Returns the tuthValue for the given observable.
    */
   public double evaluate( double observable )
   {
      return (_slope * observable + _intercept);
   }

   /**
    * For testing purposes only.
    */
   public static void main( String[] args )
   {
      FuzzySet aSet = new LinearFuzzySet( 0.01, 4.95, 0.5 );

      double arg = 0;
      while( arg <= 5 )
      {
         System.out.println( arg + "," + aSet.truthValue( arg ) );
         arg += 0.05;
      }
   }

}
