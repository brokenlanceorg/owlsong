package genetic.impl;

import genetic.*;
import math.*;
import math.graph.*;
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
public class GCILPIndividual extends Individual
{

   /**
    * Default constructor.
    */
   public GCILPIndividual()
   {
   }

   /**
    * Implementation of Interface.
    * This is the proper implementation, so please take note.
    */
   public Individual clone()
   {
      Individual indy = new GCILPIndividual();
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
    *
    */
   private int getColor( double v, int maxDegree, double stepSize )
   {
      for( int j=0; j<maxDegree; j++ )
      {
         double base = j * stepSize;
         if( v > base && v <= ( base + stepSize ) )
         {
            return j;
         }
      }
      return 0;
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
      _myFitness                     = 0; // This is crucial for the algorithm to work
      int[][] m                      = getEnvironmentCache().getAdjacencyMatrix();
      ArrayList< Vertex > vertexList = getEnvironmentCache().getSortedVertexList();
      Set s = new TreeSet< Integer >();

      int[] vertices = new int[ _myGenotype.size() ];
      int maxDegree = vertexList.get( 0 ).getDegree() + 1;
      double stepSize = 1 / (double) maxDegree;

      // first, let's assign the colors
      for( int i=0; i<_myGenotype.size(); i++ )
      {
         vertices[ i ] = getColor( _myGenotype.get( i ), maxDegree, stepSize );
         s.add( vertices[ i ] );
      }

      for( Vertex v : vertexList )
      {
         for( int i=0; i<m.length; i++ )
         {
            if( m[ v.getVertexID() ][ i ] == 1 )
            {
               if( vertices[ v.getVertexID() ] == vertices[ i ] )
               {
                  return Double.MIN_VALUE;
               }
            }
         }
         v.setColor( vertices[ v.getVertexID() ] );
      }

      _myFitness = 1 / (double) s.size();
      
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
      String result = "\nVetex Coloring:\n";

      ArrayList< Vertex > vertexList = getEnvironmentCache().getSortedVertexList();
      int[] vertices = new int[ _myGenotype.size() ];
      int maxDegree = vertexList.get( 0 ).getDegree() + 1;
      double stepSize = 1 / (double) maxDegree;

      for( int i=0; i<_myGenotype.size(); i++ )
      {
         vertices[ i ] = getColor( _myGenotype.get( i ), maxDegree, stepSize );
      }

      for( Vertex v : vertexList )
      {
         v.setColor( vertices[ v.getVertexID() ] );
         result += v;
      }

      return result;
   }

}
