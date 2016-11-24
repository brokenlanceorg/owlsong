package genetic;

import java.math.*;

/**
 * Just a test class for PRIFGA Individual.
 */
public class TestIndividual extends Individual
{

   /**
    * Default constructor.
    */
   public TestIndividual()
   {
      _myAge = 0;
   }

   /**
    * Implementation of Interface.
    */
   public double evaluateFitness()
   {
      _myFitness = 0;

      for( Double d : _myGenotype )
      {
         _myFitness += d;
      }

      return _myFitness;
   }

   /**
    * Implementation of Interface.
    */
   public Individual clone()
   {
      return new TestIndividual();
   }

   /**
    * Implementation of Interface.
    * It is up to this class to know how big the genome needs to be
    * initially.
    */
   public void randomizeGenome()
   {
      for( int i=0; i<5; i++ )
      {
         _myGenotype.add( new Double( Math.random() ) );
      }
   }
}
