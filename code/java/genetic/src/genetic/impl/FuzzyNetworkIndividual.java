/**
 *  This class is the Individual class for the Fuzzy Neural Network.
 *  If this idea proves to be reasonable, this functionality (the 
 *  ability to train a fuzzy network) can be built into another
 *  wrapping class so that the training data instances can be 
 *  set in the singleton repository.
 */

package genetic.impl;

import genetic.*;
import fuzzy.network.*;
import fuzzy.configuration.*;
import fuzzy.*;
import math.*;

import java.math.*;
import java.util.*;

/**
 * 
 */
public class FuzzyNetworkIndividual extends Individual
{

   // Statically store the configuration for speed gain
   private static FuzzyNetworkConfiguration _networkConfig;

   // Inititalize our static members
   static
   {
//      _networkConfig = (new XMLParser()).parseXML( "config" );
      _networkConfig = (new XMLParser()).parseXML( "simple" );
   }

   /**
    * Default constructor.
    */
   public FuzzyNetworkIndividual()
   {
      _myAge = 0;
      _myFitness = 0;
   }

   /**
    * Implementation of Interface.
    */
   public Individual clone()
   {
      return new FuzzyNetworkIndividual();
   }

   /**
    * Implementation of Interface.
    * The genome can be relatively small here, since we're
    * using our power set compression algorithm, and indeed
    * it must be rather small since we can end up with a 
    * combinatorial explotion otherwise.
    */
   public void randomizeGenome()
   {
//      for( int i=0; i<(3); i++ )
      for( int i=0; i<(13); i++ )
//      for( int i=0; i<(1194); i++ )
      {
         _myGenotype.add( new Double( Math.random() ) );
      }
   }

   /**
    * Implementation of Interface.
    * See class header for more information.
    */
   public double evaluateFitness()
   {
//      System.out.println( "Entering evaluateFitness" );

      _myFitness = 0;

//      System.out.println( "Before power set: " + Runtime.getRuntime().freeMemory() / 1024 );
      // create the fuzzy neural network:
      ArrayList<Double> params = (new PowerSetTransform()).calculateTransform( _myGenotype );
//      System.out.println( "After power set: " + Runtime.getRuntime().freeMemory() / 1024 );
//      System.out.println( "about to construct network" );
      FuzzyNeuralNetwork net = new FuzzyNeuralNetwork( _networkConfig, params );
//      FuzzyNeuralNetwork net = new FuzzyNeuralNetwork( _networkConfig, _myGenotype );

//      System.out.println( "About to initialize repository" );

      // Test the training methods:
      TrainingDataRepository.initializeRepository( _networkConfig, 10 );
//      TrainingDataRepository.initializeRepository( _networkConfig, 2 );
//      System.out.println( "About to train network" );
      double train = net.train( TrainingDataRepository.getInstance().getObservables(), 
                                TrainingDataRepository.getInstance().getOutputs() );

      if( train == 0 )
      {
         _myFitness = Double.MAX_VALUE;
      }
      else
      {
         _myFitness = 1 / train;
      }

//      System.out.println( "Fitness value: " + _myFitness );

      return _myFitness;
   }

}
