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
public class NonLinearIFSIndividual extends Individual
{

   /**
    * Default constructor.
    */
   public NonLinearIFSIndividual()
   {
   }

   /**
    * Implementation of Interface.
    * This is the proper implementation, so please take note.
    */
   public Individual clone()
   {
      Individual indy = new NonLinearIFSIndividual();
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
    */
   public double evaluateFitness()
   {
      _myFitness = 0; // This is crucial for the algorithm to work
      double[][] data    = getEnvironmentCache().getDataStreams();
      double[]   target  = getEnvironmentCache().getTargetStream();
      double temp = 0;
      double[][] initial = new double[ 2 ][ 1 ];
      initial[ 0 ][ 0 ] = data[ 0 ][ 0 ];
      initial[ 1 ][ 0 ] = target[ 0 ];
      int numberOfDataPoints = 20;
      int steps = 6;
      double stepSize = 0.2;
      double[][] pointA = new double[ 2 ][ 1 ];
      double[][] pointB = new double[ 2 ][ 1 ];
      double[] pointC = null;
      double[] pointD = null;
      double dist1 = 0;
      double dist2 = 0;
      // genome size will be individual genome size * operators * variables
      NonLinearFractalFunction f = new NonLinearFractalFunction( numberOfDataPoints - 1, 2 );
      f.setInitialPoint( initial );
      f.setResolutionWidth( numberOfDataPoints );
      double[] genome = new double[ _myGenotype.size() ];
      for( int i=0; i<genome.length; i++ )
      {
         genome[ i ] = _myGenotype.get( i );
      }
      f.setGenome( genome );
      for( int j=0; j<5; j++ )
      {
         _myFitness = 0; // This is crucial for the algorithm to work

         double[] values = f.createFractalData();

         // ensure that the mapping is contractive:
         for( int i=0; i<steps; i++ )
         {
            pointA[ 0 ][ 0 ] = i * stepSize;
            for( int k=0; k<steps; k++ )
            {
               pointA[ 1 ][ 0 ] = k * stepSize;
               for( int w=0; w<steps; w++ )
               {
                  pointB[ 0 ][ 0 ] = w * stepSize;
                  for( int z=0; z<steps; z++ )
                  {
                     pointB[ 1 ][ 0 ] = z * stepSize;
                     dist1 = (new MathUtilities()).getEuclideanDistance2D( pointA[ 0 ][ 0 ], pointA[ 1 ][ 0 ], 
                                                                   pointB[ 0 ][ 0 ], pointB[ 1 ][ 0 ] );
                     for( int s=0; s<numberOfDataPoints - 1; s++ )
                     {
                        pointC = f.evaluateOperator( s, pointA );
                        pointD = f.evaluateOperator( s, pointB );
                        dist2 = (new MathUtilities()).getEuclideanDistance2D( pointC[ 0 ], pointC[ 1 ], 
                                                                      pointD[ 0 ], pointD[ 1 ] );
                        if( dist2 > dist1 )
                        {
                           return Double.MAX_VALUE;
                        }
                     }
                  }
               }
            }
         }

         for( int i=0; i<target.length; i++ )
         {
            _myFitness += Math.abs( values[ i ] - target[ i ] );
         }

         _myFitness /= (double)target.length;

         if( _myFitness != 0 )
         {
            _myFitness = 1 / _myFitness;
         }
         else
         {
            _myFitness = Double.MAX_VALUE;
         }
         temp += _myFitness;
      }

      _myFitness = temp / 5.0;

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
      String result = "Predictions:\n";
      _myFitness = 0; // This is crucial for the algorithm to work
      double[][] data    = getEnvironmentCache().getDataStreams();
      double[]   target  = getEnvironmentCache().getTargetStream();
      int numberOfDataPoints = 20;
      // genome size will be individual genome size * operators * variables
      NonLinearFractalFunction f = new NonLinearFractalFunction( numberOfDataPoints - 1, 2 );
      double temp = 0;
      double[][] initial = new double[ 2 ][ 1 ];
      initial[ 0 ][ 0 ] = data[ 0 ][ 0 ];
      initial[ 1 ][ 0 ] = target[ 0 ];
      f.setInitialPoint( initial );
      f.setResolutionWidth( numberOfDataPoints );
      double[] genome = new double[ _myGenotype.size() ];
      for( int i=0; i<genome.length; i++ )
      {
         genome[ i ] = _myGenotype.get( i );
      }
      f.setGenome( genome );
      double[] values = f.createFractalData();

      for( int i=0; i<target.length; i++ )
      {
         _myFitness += Math.abs( values[ i ] - target[ i ] );
         result += values[ i ] + "\n";
      }

      _myFitness /= (double)target.length;

      if( _myFitness != 0 )
      {
         _myFitness = 1 / _myFitness;
      }
      else
      {
         _myFitness = Double.MAX_VALUE;
      }
       
      result += "Fitness: " + _myFitness;

      return result;
   }

}
