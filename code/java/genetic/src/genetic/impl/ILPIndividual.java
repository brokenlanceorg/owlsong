package genetic.impl;

import genetic.*;
import math.*;
import ifs.*;

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
public class ILPIndividual extends Individual
{

   /**
    * Default constructor.
    */
   public ILPIndividual()
   {
   }

   /**
    * Implementation of Interface.
    * This is the proper implementation, so please take note.
    */
   public Individual clone()
   {
      Individual indy = new ILPIndividual();
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
         _myGenotype.add( (new MathUtilities()).random() );
      }
   }

   /**
    * Implementation of Interface.
    * Here, we will solve the minimal vertex cover problem.
    * Minimize sum c( v ) * x_v over all v in the Graph
    * subject to: x_u + x_v >= 1 for all u, v in the Graph's edges
    *           : x_v in { 0, 1 }
    */
   public double evaluateFitness()
   {
      _myFitness   = 0; // This is crucial for the algorithm to work
      int[][] m    = getEnvironmentCache().getAdjacencyMatrix();

      int[] vertices = new int[ _myGenotype.size() ];
      double sum = 0;

      for( int i=0; i<_myGenotype.size(); i++ )
      {
         vertices[ i ] = ( _myGenotype.get( i ) < 0.5 ) ? 0 : 1;
         sum += vertices[ i ];
      }

      for( int i=0; i<m.length; i++ )
      {
         for( int j=0; j<m[ i ].length; j++ )
         {
            // check to see if this is an edge:
            if( m[ i ][ j ] == 1 )
            {
               if( vertices[ i ] == 0 && vertices[ j ] == 0 )
               {
                  _myFitness = Double.MIN_VALUE;
                  return _myFitness;
               }
            }
         }
      }

      if( sum > 0.0 )
      {
         _myFitness = 1 / sum;
      }
      else
      {
         _myFitness = Double.MIN_VALUE;
      }
//       System.out.println( "fitness is: " + _myFitness );
      
      return _myFitness;
   }

   /**
    *
    */
   public String toString()
   {
      return "" + _myFitness;
   }

   /**
    *
    */
   public String toStringFinal()
   {
      String result = "Vetex Cover:\n";

      for( int i=0; i<_myGenotype.size(); i++ )
      {
         result += "  vertex: " + i + " membership: " + ( ( _myGenotype.get( i ) < 0.5 ) ? 0 : 1 ) + "\n";
      }
      return result;
   }

}
