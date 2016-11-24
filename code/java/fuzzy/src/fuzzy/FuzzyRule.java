package fuzzy;

import java.util.*;
import java.math.*;

/**
 * The basic class for FuzzyRule system.
 * For other types of behavior, override this class.
 *
 * actually, in general, we would have an m x n system:
 * n( s1( o1 ) ) c s1( o2 ) c s1( o3 ) c ... c s1( on )
 * s2( o1 ) c s2( o2 ) c s2( o3 ) c ... c s2( on )
 * s3( o1 ) c s3( o2 ) c s3( o3 ) c ... c s3( on )
 * ...
 * sm( o1 ) c sm( o2 ) c sm( o3 ) c ... c sm( on )
 * where n() is the not or not(not) operator.
 *
 * This is the general case, and in practice, not all 
 * of these fuzzy sets and conjunctions will fire;
 * this data will also be a part of the rule set.
 *
 * Thus the overall system is m x n (m = fuzyz sets,
 * and n = observables).
 *
 * The size of the sets param should be the number of
 * fuzzy sets times 4. While the size of the spec param
 * should be the number of fuzzy sets times observables.
 */
public class FuzzyRule
{
   // Collection of type FuzzySet
   protected ArrayList<FuzzySet> _fuzzySets;

   // Collection of type Conjunctions
   protected ArrayList<Conjunction> _conjunctions;

   // Keeps track of the last calculated truth value
   protected double _myValue;

   // Holds the comination specification for the rule
   // Holds the comination specification for the rule
   // the size should be the number of sets times
   // the number of observables. Each number represents
   // whether the combination is used, and then if so
   // if it's a NOT or a regular operation.
   protected ArrayList<Double> _specification;

   /**
    * Default constructor.
    */
   public FuzzyRule()
   {
      _myValue = -1;
   }

   /**
    * Constructor.
    * We'll consider the case that the sets will be 
    * defined as [left, right, slope/type, conj]
    * while the spec specifies whether the set is
    * used or not with the observable and whether
    * or not it's a NOT operation.
    * @param ArrayList<Double> the specification of
    * the fuzzy rule system.
    */
   public FuzzyRule( ArrayList<Double> sets, 
                     ArrayList<Double> spec )
   {
      _myValue = -1;
      double left = 0;
      double right = 0;
      double type = 0;
      double conj = 0;
      double inc = 0;
      FuzzySet theSet = null;

      _fuzzySets = new ArrayList<FuzzySet>();
      _conjunctions = new ArrayList<Conjunction>();

      for( int i=0; i<sets.size(); i+=4 )
      {
         left = sets.get( i );
         right = sets.get( i + 1 );
         type = sets.get( i + 2 );
         conj = sets.get( i + 3 );
         inc = type;

         // increasing cases
         if( inc >= 0.5 )
         {
            type -= 0.5;
         }

         // case for gaussian
         if( type >= 0 && type < 0.166666 )
         {
            theSet = new GaussianFuzzySet( left, right );
         }
         // case for linear
         else if( type >= 0.166666 && type < 0.3333333 )
         {
            theSet = new LinearFuzzySet( left, right, inc );
         }
         // case for sigmoid
         else if( type >= 0.3333333 && type <= 0.5)
         {
            theSet = new SigmoidFuzzySet( left, right, inc );
         }
         // case for unkown
         else
         {
            System.err.println( "Unknown fuzzy set type: " + type );
         }

         _fuzzySets.add( theSet );
         _conjunctions.add( new Conjunction( conj ) );

      }

      _specification = spec;
   }

   /**
    * Returns the last calculated truth value.
    */
   public double getTruthValue()
   {
      return _myValue;
   }

   /**
    * Evaluates the fuzzy rule to deterimine the truth value
    * given a set of observables.
    */
   public double truthValue( ArrayList<Double> observables )
   {
      double truthValue = 0;
      double previousValue = -100;
      double observable = 0;
      double spec = 0;
      int position = 0;
      FuzzySet set = null;
      Conjunction conj = null;

      if( ( _fuzzySets.size() != _conjunctions.size() ) || 
          ( _fuzzySets.size() * observables.size() != _specification.size() ) )
      {
         System.err.println( "Returning due to size checks" );
         return _myValue;
      }

      for( int i=0; i<_fuzzySets.size(); i++ )
      {
         set = _fuzzySets.get( i );
         conj = _conjunctions.get( i );
         for( int j=0; j<observables.size(); j++ )
         {
            spec = _specification.get( position++ );

            // this combination is used
            if( spec >= 0.5 )
            {
               // NOT case
               if( spec >= 0.5 && spec < 0.75 )
               {
                  set.setNotOperator();
               }
               // regular case
               else
               {
                  set.setRegularOperator();
               }
            }
            // this combination is NOT used
            else
            {
               continue;
            }

            truthValue = set.truthValue( observables.get( j ) );
            if( previousValue != -100 )
            {
               _myValue = conj.conjunctify( previousValue, truthValue );
            }
            else
            {
               _myValue = truthValue;
            }
            previousValue = _myValue;
         }
      }

      return _myValue;
   }

   /**
    * For testing purposes only.
    *
    */
   public static void main( String[] args )
   {
      ArrayList<Double> sets = new ArrayList<Double>();
      ArrayList<Double> spec = new ArrayList<Double>();
      ArrayList<Double> obs = new ArrayList<Double>();

      // 10 fuzzy sets 40 params:
      for( int i=0; i<40; i++ )
      {
         sets.add( Math.random() );
      }

      // 10 observables x 10 sets
      for( int i=0; i<100; i++ )
      {
         spec.add( Math.random() );
      }

      // 10 observables
      for( int i=0; i<10; i++ )
      {
         obs.add( Math.random() );
      }

      FuzzyRule rule = new FuzzyRule( sets, spec );
      double result = rule.truthValue( obs );
      System.out.println( "The result is: " + result );

      // Printout of a gaussian Fuzzy set
//       FuzzySet theSet = new GaussianFuzzySet( 0.376611, 0.2 );
      FuzzySet theSet = new GaussianFuzzySet( 1.745329, 0.2 );
      double arg = 0;
      System.out.println( "The FuzzySet graph result is: " );
      for( int i=0; i<10; i++ )
      {
         arg = (double)i / 9 * Math.PI;
         System.out.println( theSet.truthValue( arg ) );
      }
   }

}
