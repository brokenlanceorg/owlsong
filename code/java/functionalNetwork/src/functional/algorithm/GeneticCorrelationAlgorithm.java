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
public class GeneticCorrelationAlgorithm extends CorrelationAlgorithm
{

   private ArrayList< Double >  _bestGenotype;
   private double[]             _bestWeights;
   private FunctionalIndividual _bestIndividual;
   private int                  _genomeLength   = 0;
   private int                  _populationSize = 10;

   /**
    *
    */
   public GeneticCorrelationAlgorithm()
   {
   }

   /**
    *
    */
   public GeneticCorrelationAlgorithm( HaltingCriteria criteria )
   {
      super( criteria );
   }

   /**
    *
    */
   public GeneticCorrelationAlgorithm( HaltingCriteria criteria, FunctionalCorrelation function )
   {
      super( criteria, function );
   }

   /**
    *
    * @param int -- The number of threads for the GA to run with.
    */
   public void setPopulationSize( int size )
   {
      _populationSize = size;
   }

   /**
    *
    * @param double[] the best weights as determined by the gradient algorithm (or any other algorithm).
    */
   public void setWeights( double[] weights )
   {
      _bestWeights = weights;
   }

   /**
    *
    * @return ArrayList< Double > -- The best genotype found thus far.
    */
   public ArrayList< Double > getGenotype()
   {
      return _bestGenotype;
   }

   /**
    *
    * @param ArrayList< Double > -- Sets the genotype so that it can be used for training
    * after a deserialization event.
    */
   public void setGenotype( ArrayList< Double > genotype )
   {
      _bestGenotype = genotype;
      setCorrelate( FunctionalCorrelation.buildBinaryTree( _bestGenotype, getTrainingInputData() ) );
      _bestIndividual = new FunctionalIndividual();
      _bestIndividual.setGenotype( _bestGenotype );
      EnvironmentCache env = new EnvironmentCache();
      env.setDataStreams( getTrainingInputData() );
      env.setTargetStream( getTrainingTargetData() );
      _bestIndividual.setEnvironmentCache( env );
      _bestIndividual.evaluateFitness();
   }

   /**
    *
    * @param int -- The length of the genome for the solutions.
    */
   public void setGenomeLength( int length )
   {
      _genomeLength = length;
   }

   /**
    * 
    */
   public void train()
   {
// System.out.println( Thread.currentThread().getName() + " GeneticCorrAlg instantiating..." );
      // in order to refresh all the queues and randomize the population, we need to re-instantiate:
      GeneticAlgorithm ga = null;

      try
      {
//          System.out.println( Thread.currentThread().getName() + " sleeping and yielding" );
         Thread.yield();
         Thread.sleep( 1 );
      }
      catch( InterruptedException e )
      {
         System.err.println( Thread.currentThread().getName() + " recieved interruption: " + e );
      }

      if( _bestIndividual != null )
      {
         ga = new GeneticAlgorithm( getHaltingCriteria(), 
                                    "genetic.impl.FunctionalIndividual", 
                                    _bestIndividual.getEnvironmentCache(),
                                    _genomeLength, 
                                    _populationSize );
         ga.setBestIndividual( _bestIndividual );
      }
      else
      {
         // this env cache must be set before the GA is instantiated because the constructor will
         // actually call evaluateFitness on the first generation before we call evolve.
         EnvironmentCache env = new EnvironmentCache();
         env.setDataStreams( getTrainingInputData() );
         env.setTargetStream( getTrainingTargetData() );
         env.setGenomeLength( _genomeLength );
//    public GeneticAlgorithm( HaltingCriteria criteria, 
//                             String name, 
//                             EnvironmentCache cache, 
//                             int length, 
//                             int count )
         ga = new GeneticAlgorithm( getHaltingCriteria(), 
                                    "genetic.impl.FunctionalIndividual", 
                                    env, 
                                    _genomeLength, 
                                    _populationSize );
      }

// System.out.println( Thread.currentThread().getName() + " About to call evolve" );
      _bestIndividual = (FunctionalIndividual) ga.evolve();
// System.out.println( Thread.currentThread().getName() + " Finished call to evolve" );
      _bestGenotype = _bestIndividual.getGenotype();
      setCorrelate( FunctionalCorrelation.buildBinaryTree( _bestGenotype, getTrainingInputData() ) );
// System.out.println( Thread.currentThread().getName() + " exiting train in GA" );
   }

}
