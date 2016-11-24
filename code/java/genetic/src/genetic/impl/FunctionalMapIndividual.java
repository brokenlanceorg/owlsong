package genetic.impl;

import genetic.*;
import math.*;

import java.math.*;
import java.util.*;

/**
 * 
 */
public class FunctionalMapIndividual extends Individual
{

   /**
    * Default constructor.
    */
   public FunctionalMapIndividual()
   {
   }

   /**
    * Implementation of Interface.
    * This is the proper implementation, so please take note.
    */
   public Individual clone()
   {
      Individual indy = new FunctionalMapIndividual();
      indy.setEnvironmentCache( getEnvironmentCache() );
      indy.setFitness( _myFitness );

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

      return indy;
   }

   /**
    * Implementation of Interface.
    * A power set transform of 5 elements gives 155 elements.
    */
   public void randomizeGenome()
   {
      int s = getEnvironmentCache().getGenomeLength();
      _myGenotype = new ArrayList< Double >( s );
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
      _myFitness = 0; // This is crucial for the algorithm to work
      double[][] streams = getEnvironmentCache().getDataStreams();
      double[] target = getEnvironmentCache().getTargetStream();
      FunctionalCorrelation root = FunctionalCorrelation.buildBinaryTree( _myGenotype, streams ); 

      for( int i=0; i<target.length; i++ )
      {
         double value = 0;
         ArrayList< Double > data = new ArrayList< Double >();
         for( int j=0; j<100; j++ )
         {
            value = root.evaluate( i );
            data.add( value );
            streams[ 0 ][ 0 ] = value;
//             System.out.println( "value: " + value );
         }
         StatUtilities s = new StatUtilities();
         s.calculateStats( data );
         _myFitness += Math.abs( s.getMean() - target[ i ] );
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
//       double[] target = getEnvironmentCache().getTargetStream();
      // These statements will print out the equation tree:
      double[][] streams = getEnvironmentCache().getDataStreams();
      FunctionalCorrelation root = FunctionalCorrelation.buildBinaryTree( _myGenotype, streams ); 
      String result = root.toString( 0 );
      result += "\nFitness: " + _myFitness + "\n";

//       result += "\nGenome: ";
//       for( double gene : _myGenotype )
//       {
//          result += gene + " ";
//       }

//       String result = "Fitness: " + _myFitness + "\n";

//       result += "predictions:\n";
//       double fitness = 0;
//       for( int i=0; i<target.length; i++ )
//       {
//          double value = root.evaluate( i );
//          double targetValue = target[ i ];
//          fitness += Math.abs( value - targetValue );
// //          result += value + " " + targetValue + "\n";
//          result += value + "\n";
//       }
//       result += "fitness: " + (1 / fitness);

      return result;
   }
}
