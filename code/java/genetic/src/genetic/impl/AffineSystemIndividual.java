package genetic.impl;

import genetic.*;
import grafix.*;
import math.*;
import ifs.*;

import java.math.*;
import java.util.*;

/**
 * 
 */
public class AffineSystemIndividual extends Individual
{

   /**
    * Default constructor.
    * Genome: 
    */
   public AffineSystemIndividual()
   {
   }

   /**
    * Implementation of Interface.
    * This is the proper implementation, so please take note.
    */
   public Individual clone()
   {
      Individual indy = new AffineSystemIndividual();
      getEnvironmentCache().setGenomeLength( 24 );
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
      MathUtilities mu = new MathUtilities();
      int           s  = getEnvironmentCache().getGenomeLength();
      _myGenotype      = new ArrayList< Double >( s );

      for( int i=0; i<s; i++ )
      {
         _myGenotype.add( mu.random() );
      }
   }

   /**
    * Implementation of Interface.
    * This is image run4 image 2
    * Probability = 0.835573110538329
    * Probability = 0.06932640621040896
    * Probability = 0.09510048325126202
    */
   public double evaluateFitness()
   {
      RGBColorPoint[][] target = getEnvironmentCache().getImageTarget();

      double[] params = new double[ _myGenotype.size() ];
      for( int i=0; i<params.length; i++ )
      {
         params[ i ] = _myGenotype.get( i );
      }

      // HLSColorPoint[][] imageData = new HLSColorPoint[ _height ][ _width ];
      // public AffineIFS( double[] params, String name, int w, int h, int num )
      AffineIFS  sys     = new AffineIFS( params, null,  target[ 0 ].length, target.length, 100000 );
      double[][] rawData = sys.createRawFractalData();

      _myFitness = 0;
      for( int i=0; i<rawData.length; i++ )
      {
         for( int j=0; j<rawData[ 0 ].length; j++ )
         {
            _myFitness += Math.abs( rawData[ i ][ j ] - target[ i ][ j ].getRed() );
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
      System.out.println( "fitness: " + _myFitness );

      return _myFitness;
   }

   /**
    *
    */
   public String toString()
   {
      double[] params  = new double[ _myGenotype.size() ];
      String   result  = "\nFitness: " + _myFitness + "\n";
//       for( int i=0; i<params.length; i++ )
//       {
//          params[ i ] = _myGenotype.get( i );
//       }
//       result += "\nFitness: " + _myFitness + "\n";

      return result;
   }
}
