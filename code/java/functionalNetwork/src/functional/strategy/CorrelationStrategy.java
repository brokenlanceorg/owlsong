package functional.strategy;

import functional.algorithm.*;
import common.*;
import math.*;

import java.util.*;

/**
 * This class performs the basic iterations of the various algorithms that can be executed.
 * Since each algorithm performs specific type of training hueristics, it was determined that
 * each would be stored here as opposed to a generic collection of TrainingAlgorithm types.
 * In this manner, the specifics can be handled without resort to 'instanceOf' semantics.
 */
public class CorrelationStrategy extends TrainingStrategy
{

   protected FunctionalCorrelation        _correlate;
   protected double[][]                   _trainingInputData;
   protected double[]                     _trainingTargetData;
   protected int                          _populationSize;
   protected GeneticCorrelationAlgorithm  _geneticAlgorithm;
   protected GeneticWeightsAlgorithm      _geneticWeightsAlgorithm;
   protected GradientCorrelationAlgorithm _gradientAlgorithm;

   /**
    *
    */
   public CorrelationStrategy()
   {
      super();
   }

   /**
    *
    */
   public CorrelationStrategy( HaltingCriteria criteria )
   {
      super( criteria );
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
    */
   public void setTrainingInputData( double[][] data )
   {
      _trainingInputData = data;
      _geneticAlgorithm.setTrainingInputData( data );
      _geneticWeightsAlgorithm.setTrainingInputData( data );
      _gradientAlgorithm.setTrainingInputData( data );
   }

   /**
    *
    */
   public void setTrainingTargetData( double[] data )
   {
      _trainingTargetData = data;
      _geneticAlgorithm.setTrainingTargetData( data );
      _geneticWeightsAlgorithm.setTrainingTargetData( data );
      _gradientAlgorithm.setTrainingTargetData( data );
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public FunctionalCorrelation getCorrelate()
   {
      return _correlate;
   }

   /**
    *
    * @param ArrayList< Double > -- The currently best genotype with which the genetic algorithm
    * should use when evolving after a deserialization event.
    */
   public void setGenotype( ArrayList< Double > genotype )
   {
      _geneticAlgorithm.setGenotype( genotype );
   }

   /**
    *
    * @return ArrayList< Double > -- The currently best genotype with which the genetic algorithm
    * should use when evolving after a deserialization event.
    */
   public ArrayList< Double > getGenotype()
   {
      return _geneticAlgorithm.getGenotype();
   }

   /**
    *
    * @param int -- The length of the genome for the solutions.
    */
   public void setGenomeLength( int length )
   {
      _geneticAlgorithm.setGenomeLength( length );
   }

   /**
    * Implementation of the base class method.
    */
   public void train()
   {
      long start = System.currentTimeMillis();
      long elapsed = 0;
// System.out.println( Thread.currentThread().getName() + " starting train in CS: " + _haltingCriteria.getElapsedTimeTolerance() );
      while( elapsed < _haltingCriteria.getElapsedTimeTolerance() )
      {
         // TODO: need to figure out how to set the weights back on the genetic algorithm
         // so that when the subclass is interleave things will work properly
// System.out.println( "Will now train with genetic algorithm" );
//          _geneticAlgorithm.setWeights( _gradientAlgorithm.getWeights() );
// System.out.println( Thread.currentThread().getName() + " about to train GA in CS: " + elapsed );
         _geneticAlgorithm.setPopulationSize( _populationSize );
         _geneticAlgorithm.train();
// System.out.println( Thread.currentThread().getName() + " returned from train GA in CS: " + elapsed );
         _correlate = _geneticAlgorithm.getCorrelate();
// System.out.println( "Will now train with genetic weights algorithm" );
//          _geneticWeightsAlgorithm.setCorrelate( _geneticAlgorithm.getCorrelate() );
//          _geneticWeightsAlgorithm.train();
// System.out.println( "Will now train with gradient algorithm" );
//          _gradientAlgorithm.setCorrelate( _geneticAlgorithm.getCorrelate() );
//          _gradientAlgorithm.train();
// System.out.println( "Finished training with gradient algorithm" );
         // finally, update the elapsed time
         elapsed = System.currentTimeMillis() - start;
      }
// System.out.println( Thread.currentThread().getName() + " ending train in CS: " + elapsed );
   }

}
