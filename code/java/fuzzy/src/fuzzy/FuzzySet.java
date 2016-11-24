package fuzzy;

/**
 * The interface that all FuzzySets must implement.
 * Not sure how to design this just yet.
 * It needs to be as streamlined as possible since these sets will
 * be used in huge for-loops and such constructs.
 * The unary NOT operator can be an attribute of the fuzzy set.
 * In reality, these sets are used in longer logic sentences with 
 * conjunctions -- is there a way to exploit this typical usage?
 * So, from the bottom-up, we'd have observables, fuzzy sets,
 * conjunctions, fuzzy rules, conjunctors, fuzzy rule sets,
 *
 *
 * e.g., if 
 * set1( obs1 ) and set2( obs1 ) and set3( obs2 ) or set4( obs3)
 * and set5( obs3 ) ... then ??
 * actually, in general, we would have an m x n system:
 * s1( o1 ) c s1( o2 ) c s1( o3 ) c ... c s1( on )
 * s2( o1 ) c s2( o2 ) c s2( o3 ) c ... c s2( on )
 * s3( o1 ) c s3( o2 ) c s3( o3 ) c ... c s3( on )
 * ...
 * sm( o1 ) c sm( o2 ) c sm( o3 ) c ... c sm( on )
 * During evaluation, this collapses into a vector consq_m()
 * which can further be collapsed by taking the median?
 * I think this design is superflous because each rule
 * could just be an individual in the swarm and choosing
 * the median is just another way of choosing an individual.
 * Instead, just conjoin the entire thing since there really
 * is no consequent.
 */
public abstract class FuzzySet 
{

   // Holds the truth value for this set
   protected double _myValue;

   // Holds the left endpoint value.
   protected double _leftEndpoint;

   // Holds the right endpoint value.
   protected double _rightEndpoint;

   // Determines whether or not the set is increasing.
   protected boolean _isIncreasing;

   // Holds the NOT value
   protected boolean _isNOT = false;

   /**
    * Default constructor.
    */
   public FuzzySet()
   {
   }

   /**
    * Constructor.
    * This is the constructor for bell curves.
    */
   public FuzzySet( double center, double width )
   {
      _leftEndpoint = center;
      _rightEndpoint = width;
   }

   /**
    * Constructor.
    * The endpoints can be in any order but they will get sorted out
    * and the increasing value denotes increasing set if greater than
    * or equal to 0.5.
    */
   public FuzzySet( double left, double right, double increasing )
   {
      _leftEndpoint = left;
      _rightEndpoint = right;
      if( _leftEndpoint > _rightEndpoint )
      {
         double temp = _rightEndpoint;
         _rightEndpoint = _leftEndpoint;
         _leftEndpoint = temp;
      }
      if( increasing >= 0.5 )
      {
         _isIncreasing = true;
      }
      else
      {
         _isIncreasing = false;
      }
   }

   /**
    * Returns the tuthValue for the given observable.
    */
   public double truthValue( double observable )
   {
      if( _myValue != -1 )
      {
         // calculate the truth value if the equation is valid.
         if( observable < _leftEndpoint )
         {
            if( _isIncreasing )
            {
               _myValue = 0;
            }
            else
            {
               _myValue = 1;
            }
         }
         else if( observable > _rightEndpoint )
         {
            if( _isIncreasing )
            {
               _myValue = 1;
            }
            else
            {
               _myValue = 0;
            }
         }
         else
         {
            _myValue = evaluate( observable );
         }
      }

      if( _isNOT )
      {
         _myValue = 1 - _myValue;
      }

      return _myValue;
   }

   /**
    * Returns the tuthValue for the given observable.
    */
   public abstract double evaluate( double observable );

   /**
    * Returns the tuthValue for the last evaluation.
    */
   public double getTruthValue()
   {
      return _myValue;
   }

   /**
    * Sets the NOT operator.
    */
   public void setNotOperator()
   {
      _isNOT = true;
   }

   /**
    * Sets the regular operator.
    */
   public void setRegularOperator()
   {
      _isNOT = false;
   }

}
