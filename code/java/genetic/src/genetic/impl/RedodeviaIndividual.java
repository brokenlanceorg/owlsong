package genetic.impl;

import genetic.*;
import math.*;

import java.math.*;
import java.util.*;

/**
 * 
 *  Redodevia - REDuction Of DEVIAtion.
 * 
 * 
 */
public class RedodeviaIndividual extends Individual
{

   /**
    * Default constructor.
    */
   public RedodeviaIndividual()
   {
   }

   /**
    * Implementation of Interface.
    * This is the proper implementation, so please take note.
    */
   public Individual clone()
   {
      Individual indy = new RedodeviaIndividual();
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
      _myFitness                    = 0; // This is crucial for the algorithm to work
      double[][]            streams = getEnvironmentCache().getDataStreams();
      double[]              data    = new double[ streams[ 0 ].length ];
      FunctionalCorrelation root    = FunctionalCorrelation.buildBinaryTree( _myGenotype, streams ); 
      StatUtilities         s       = null;
      HashSet< Integer >    vars    = root.getDependentVariables();
      int                   n       = 0;

      // If the number of dependent variables is not the same as the number of variables in
      // the data file, then we don't even want to go further.
      for( Integer var : vars )
      {
         if( var > -1 )
         {
            n++;
         }
      }
      if( n != streams.length )
      {
         return Double.MIN_VALUE;
      }

      for( int i=0; i<streams[ 0 ].length; i++ )
      {
         data[ i ] = root.evaluate( i );
         if( Double.isNaN( data[ i ] ) )
         {
            System.out.println( "found a NaN!!!" );
            return Double.MIN_VALUE;
         }
         s = new StatUtilities( data );
      }

      _myFitness = s.getDeviation();

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
      double[][]            streams = getEnvironmentCache().getDataStreams();
      FunctionalCorrelation root    = FunctionalCorrelation.buildBinaryTree( _myGenotype, streams ); 
      String                result  = root.toString( 0 );

      result += "\nFitness: " + _myFitness + "\n";

      return result;
   }
   
   /**
    *
    */
   public String toStringFinal()
   {
      double[][]            streams = getEnvironmentCache().getDataStreams();
      FunctionalCorrelation root    = FunctionalCorrelation.buildBinaryTree( _myGenotype, streams ); 
      String                result  = root.toString( 0 );

      result += "\nFitness: " + _myFitness + "\n";
      result += "\nGenome: ";

      for( double gene : _myGenotype )
      {
         result += gene + " ";
      }

      result += "\npredictions:\n";
      double fitness = 0;
      for( int i=0; i<streams[ 0 ].length; i++ )
      {
         double value = root.evaluate( i );
         if( Double.isNaN( value ) )
         {
            System.out.println( "found a NaN!" );
         }
         result += value + "\n";
      }
      HashSet< Integer > vars = root.getDependentVariables();
      for( Integer var : vars )
      {
         result += "A variable is: " + var + "\n";
      }

      return result;
   }
}
