package genetic.impl;

import genetic.*;
import math.*;

import java.math.*;
import java.util.*;

/**
 * 
 * To create a fully balanced binary tree, you'll need this many nodes for the respective levels:
 * 1  1
 * 2  3
 * 3  7
 * 4  15
 * 5  31
 * 6  63
 * 7  127
 * 8  255
 * 9  511
 * 10 1023
 * 11 2047
 * 12 4095
 * 13 8191
 * 14 16383
 * 15 32767
 * 16 65535
 * 17 131071
 * 18 262143
 * 19 524287
 * 20 1048575
 * 
 */
public class FunctionalFuzzyIndividual extends Individual
{

   /**
    * Default constructor.
    */
   public FunctionalFuzzyIndividual()
   {
   }

   /**
    * Implementation of Interface.
    * This is the proper implementation, so please take note.
    */
   public Individual clone()
   {
      Individual indy = new FunctionalFuzzyIndividual();
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
   public double evaluateFitness()
   {
      _myFitness = 0; // This is crucial for the algorithm to work
      double[][] streams = getEnvironmentCache().getRandomDataStreams();
      double[] target = getEnvironmentCache().getRandomTargetStream();
      int numberOfSets = 10;
      int totalSets = numberOfSets * (numberOfSets + 1) / 2;
      int numberofNodes = 31; // 5 levels deep
      int numberofConsequent = 127; // 7 levels deep
      FunctionalCorrelation consequent = null;
      ArrayList< Double > genome = null;
      int pos = 0;

      for( int k=numberOfSets; k>0; k-- )
      {
         double[][] outStreams = new double[ k ][ target.length ];
         for( int i=0; i<k; i++ )
         {
            genome = new ArrayList<Double>();
            for( int j=0; j<numberofNodes; j++ )
            {
               genome.add( _myGenotype.get( pos++ ) );
            }
            FunctionalCorrelation set = FunctionalCorrelation.buildBinaryTree( genome, streams ); 
            for( int w=0; w<target.length; w++ )
            {
               if( k == 1 )
               {
                  _myFitness += Math.abs( set.evaluate( w ) - target[ w ] );
               }
               else
               {
                  outStreams[ i ][ w ] = set.evaluate( w );
               }
            }
         }
         streams = new double[ k ][ target.length ];
         for( int w=0; w<k; w++ )
         {
            for( int z=0; z<target.length; z++ )
            {
               streams[ w ][ z ] = outStreams[ w ][ z ];
            }
         }
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
    */

   /**
    * Implementation of Interface.
    */
   public double evaluateFitness()
   {
      _myFitness = 0; // This is crucial for the algorithm to work
      double[][] streams = getEnvironmentCache().getRandomDataStreams();
      double[] target = getEnvironmentCache().getRandomTargetStream();
      int numberOfSets = 30;
      int numberofNodes = 7; // 3 levels deep
//       int numberofNodes = 31; // 5 levels deep
      int numberofConsequent = 301; // 8 levels deep
      FunctionalCorrelation[] sets = new FunctionalCorrelation[ numberOfSets ];
      FunctionalCorrelation consequent = null;
      double[][] outStreams = new double[ numberOfSets ][ target.length ];
      ArrayList< Double > genome = null;

      for( int i=0; i<numberOfSets; i++ )
      {
         genome = new ArrayList<Double>();
         for( int j=0; j<numberofNodes; j++ )
         {
            genome.add( _myGenotype.get( i + j ) );
         }
         sets[ i ] = FunctionalCorrelation.buildBinaryTree( genome, streams ); 
      }

      for( int i=0; i<target.length; i++ )
      {
         for( int j=0; j<numberOfSets; j++ )
         {
            outStreams[ j ][ i ] = sets[ j ].evaluate( i );
         }
      }

      genome = new ArrayList<Double>();
      for( int i=0; i<numberofConsequent; i++ )
      {
         genome.add( _myGenotype.get( numberOfSets * numberofNodes + i ) );
      }
      consequent = FunctionalCorrelation.buildBinaryTree( genome, outStreams ); 

      for( int i=0; i<target.length; i++ )
      {
         _myFitness += Math.abs( consequent.evaluate( i ) - target[ i ] );
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
   public String toString()
   {
//       _myFitness = 0; // This is crucial for the algorithm to work
      double[][] streams = getEnvironmentCache().getRandomDataStreams();
      double[] target = getEnvironmentCache().getRandomTargetStream();
      int numberOfSets = 10;
      int totalSets = numberOfSets * (numberOfSets + 1) / 2;
      int numberofNodes = 31; // 5 levels deep
      int numberofConsequent = 127; // 7 levels deep
      ArrayList< Double > genome = null;
      int pos = 0;
      String result = "***\n";

      for( int k=numberOfSets; k>0; k-- )
      {
         double[][] outStreams = new double[ k ][ target.length ];
         for( int i=0; i<k; i++ )
         {
            genome = new ArrayList<Double>();
            for( int j=0; j<numberofNodes; j++ )
            {
               genome.add( _myGenotype.get( pos++ ) );
            }
            FunctionalCorrelation set = FunctionalCorrelation.buildBinaryTree( genome, streams ); 
            for( int w=0; w<target.length; w++ )
            {
               if( k == 1 )
               {
                  result += set.evaluate( w ) + "\n";
               }
               else
               {
                  outStreams[ i ][ w ] = set.evaluate( w );
               }
            }
         }
         streams = new double[ k ][ target.length ];
         for( int w=0; w<k; w++ )
         {
            for( int z=0; z<target.length; z++ )
            {
               streams[ w ][ z ] = outStreams[ w ][ z ];
            }
         }
      }

      result += "\nFitness: " + _myFitness + "\n";

      return result;
   }
    */

   /**
    *
    */
   public String toString()
   {
      double[][] streams = getEnvironmentCache().getRandomDataStreams();
      double[] target = getEnvironmentCache().getRandomTargetStream();
      int numberOfSets = 30;
      int numberofNodes = 7; // 5 levels deep
      int numberofConsequent = 301; // 7 levels deep
      FunctionalCorrelation[] sets = new FunctionalCorrelation[ numberOfSets ];
      FunctionalCorrelation consequent = null;
      double[][] outStreams = new double[ numberOfSets ][ target.length ];
      ArrayList< Double > genome = null;

      for( int i=0; i<numberOfSets; i++ )
      {
         genome = new ArrayList<Double>();
         for( int j=0; j<numberofNodes; j++ )
         {
            genome.add( _myGenotype.get( i + j ) );
         }
         sets[ i ] = FunctionalCorrelation.buildBinaryTree( genome, streams ); 
      }

      for( int i=0; i<target.length; i++ )
      {
         for( int j=0; j<numberOfSets; j++ )
         {
            outStreams[ j ][ i ] = sets[ j ].evaluate( i );
         }
      }

      genome = new ArrayList<Double>();
      for( int i=0; i<numberofConsequent; i++ )
      {
         genome.add( _myGenotype.get( numberOfSets * numberofNodes + i ) );
      }
      consequent = FunctionalCorrelation.buildBinaryTree( genome, outStreams ); 

      String result = "***\n";
      for( int i=0; i<target.length; i++ )
      {
         result += consequent.evaluate( i ) + "\n";
      }
      result += "\nFitness: " + _myFitness + "\n";

      return result;
   }

}
