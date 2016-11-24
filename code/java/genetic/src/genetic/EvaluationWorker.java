package genetic;

import java.util.*;

/**
 * The EvaluationWorker class is a Runnable class that can pull individuals
 * off the evaluation queue and evaluates them according to their evaluateFitness.
 * This threaded class will listen to its associated queue and pull offspring
 * and perform it's operation on them to create new offspring.
 */
public class EvaluationWorker implements Runnable
{

   // The queue on which breeding occurs.
   private Individual _individual;

   /**
    * Default Constructor.
    */
   public EvaluationWorker()
   {
   }

   /**
    * Constructor.
    */
   public EvaluationWorker( Individual i )
   {
      _individual = i;
   }

   /**
    * Implementation of Runnable Interface.
    */
   public void run()
   {
      _individual.evaluateFitness();
   }

}
