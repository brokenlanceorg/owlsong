package genetic.impl;

import genetic.*;
import math.*;

import java.math.*;
import java.util.*;

/**
 * This class will operate upon an already trained FunctionalCorrelation object 
 * and to train and adjust its weight values. 
 * Note that there only need to be as many genes as there are independent variables
 * at this time. If this approach proves to be profitable, we may want to extend
 * this notion of weights or scaling to any node in the tree hierarchy and evolve that.
 */
public class FunctionalWeightsIndividual extends Individual
{

   /**
    * Default constructor.
    */
   public FunctionalWeightsIndividual()
   {
   }

   /**
    * Generic one-size-fits-all Implementation of Interface.
    * This is the proper implementation, so please take note.
    */
   public Individual clone()
   {
      Individual indy = new FunctionalWeightsIndividual();
      indy.setEnvironmentCache( getEnvironmentCache() );

      if( _myGenotype == null || _myGenotype.size() == 0 )
      {
         indy.randomizeGenome();
      }
      else
      {
         // Make a deep copy of the genotype
         ArrayList<Double> genotype = new ArrayList<Double>();
         for( double gene : _myGenotype )
         {
            genotype.add( gene );
         }
         indy.setGenotype( genotype );
      }
      indy.setFitness( _myFitness );

      return indy;
   }

   /**
    * Implementation of Interface.
    * A power set transform of 5 elements gives 155 elements.
    */
   public void randomizeGenome()
   {
      FunctionalCorrelation correlate = getEnvironmentCache().getCorrelate();
      int s                           = correlate.getNumberOfNodes();
//       System.out.println( "randomize size: " + s );
      _myGenotype                     = new ArrayList< Double >( s );

      for( int i=0; i<s; i++ )
      {
         _myGenotype.add( MathUtilities.random() );
      }
   }

   /**
    * Implementation of Interface.
    */
   public double evaluateFitness()
   {
      _myFitness                      = 0; // This is crucial for the algorithm to work
      FunctionalCorrelation correlate = getEnvironmentCache().getCorrelate();
      double[][] streams              = getEnvironmentCache().getDataStreams();
      double[] target                 = getEnvironmentCache().getTargetStream();

      correlate.setWeights( _myGenotype );

      for( int i=0; i<target.length; i++ )
      {
         _myFitness += Math.abs( correlate.evaluate( i ) - target[ i ] );
      }

      if( _myFitness != 0 )
      {
         _myFitness = 1 / _myFitness;
      }
      else
      {
         _myFitness = Double.MAX_VALUE;
      }

      return _myFitness;
   }

   /**
    *
    */
   public String toString()
   {
      FunctionalCorrelation correlate = getEnvironmentCache().getCorrelate();
      double[][] streams              = getEnvironmentCache().getDataStreams();
      double[] target                 = getEnvironmentCache().getTargetStream();
      String result                   = correlate.toString( 0 );

      correlate.setWeights( _myGenotype );
      result += "\nGenome: ";
      for( double gene : _myGenotype )
      {
         result += gene + " ";
      }
      result += "\nFitness: " + _myFitness + "\n";

      result += "predictions:\n";
      double fitness = 0;
      for( int i=0; i<target.length; i++ )
      {
         double value = correlate.evaluate( i );
         double targetValue = target[ i ];
         System.out.println( value );
         fitness += Math.abs( value - targetValue );
         result += value + "\n";
      }
      result += "fitness: " + (1 / fitness);

      return result;
   }
}
