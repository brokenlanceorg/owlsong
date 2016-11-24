package functional.algorithm;

import genetic.*;
import genetic.impl.*;
import common.*;
import math.*;

import java.util.*;

/**
 * This class implements the semantics of a training or heuristic learning algorithm based on
 * the Genetic Algorithm.
 */
public class GeneticWeightsAlgorithm extends CorrelationAlgorithm
{

   private ArrayList< Double >         _bestGenotype;
   private FunctionalWeightsIndividual _bestIndividual;

   /**
    *
    */
   public GeneticWeightsAlgorithm()
   {
   }

   /**
    *
    */
   public GeneticWeightsAlgorithm( HaltingCriteria criteria )
   {
      super( criteria );
   }

   /**
    *
    */
   public GeneticWeightsAlgorithm( HaltingCriteria criteria, FunctionalCorrelation function )
   {
      super( criteria, function );
   }

   /**
    *
    * @param double[] the best weights as determined by the gradient algorithm (or any other algorithm).
    */
   public ArrayList< Double > getGenotype()
   {
      return _bestGenotype;
   }

   /**
    * This method assumes that the client has set the evolved correlate on the super class.
    */
   public void train()
   {
      // before we evolve the populations, make sure that the algorithm has the 
      // latest data training set:
      EnvironmentCache env = new EnvironmentCache();
      env.setDataStreams( getTrainingInputData() );
      env.setTargetStream( getTrainingTargetData() );
      env.setCorrelate( getCorrelate() );
      _bestIndividual = new FunctionalWeightsIndividual();
      _bestIndividual.setEnvironmentCache( env );
      ArrayList< Double > ones = new ArrayList< Double >();
      for( int i=0; i<getCorrelate().getNumberOfNodes(); i++ )
      {
         ones.add( 1.0 );
      }
      _bestIndividual.setGenotype( ones );
      _bestIndividual = (FunctionalWeightsIndividual) _bestIndividual.clone();
      _bestIndividual.evaluateFitness();
System.out.println( "best fitness is: " + _bestIndividual.getFitness() );
System.out.println( "best org is: " + _bestIndividual );
      // in order to refresh all the queues and randomize the population, we need to re-instantiate:
      GeneticAlgorithm ga = 
         new GeneticAlgorithm( getHaltingCriteria(), "genetic.impl.FunctionalWeightsIndividual", env );
      if( _bestIndividual != null )
      {
         ga.setBestIndividual( _bestIndividual );
      }
System.out.println( "About to call evolve" );
      _bestIndividual = (FunctionalWeightsIndividual) ga.evolve();
System.out.println( "Finished call to evolve" );
      _bestGenotype = _bestIndividual.getGenotype();
      getCorrelate().setWeights( _bestGenotype );
   }

}
