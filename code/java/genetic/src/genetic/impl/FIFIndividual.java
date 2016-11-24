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
public class FIFIndividual extends Individual
{

   /**
    * Default constructor.
    */
   public FIFIndividual()
   {
   }

   /**
    * Implementation of Interface.
    * This is the proper implementation, so please take note.
    */
   public Individual clone()
   {
      Individual indy = new FIFIndividual();
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
    * The genome size needs to be target.length * 4 * 2 + 1
    */
   public double evaluateFitness()
   {
      int dataPoints = 5;
      double fitness = 0;
//       System.out.println( "In eval fitness..." );
      // may need to try to do a non-normalized version of the data
      _myGenotype = (new MathUtilities()).normalize( _myGenotype, 1 );
      double[][] streams         = getEnvironmentCache().getDataStreams();
      double[]   target          = getEnvironmentCache().getTargetStream();
      FractalInterpolationFunctionIFS ifs = new FractalInterpolationFunctionIFS( null, target.length, target.length, 50000);
      ifs.setInitialX( streams[ 0 ] );
      ifs.setInitialY( target );
      ifs.setInitialZ( streams[ 0 ] );
      int pos = 0;
      double[] d = new double[ target.length ];
      for( int i=0; i<target.length; i++ )
      {
         d[ i ] = _myGenotype.get( pos++ );
         if( _myGenotype.get( pos++ ) < 0.5 )
         {
            d[ i ] *= -1;
         }
      }
      ifs.setParamD( d );
      double[] h = new double[ target.length ];
      for( int i=0; i<target.length; i++ )
      {
         h[ i ] = _myGenotype.get( pos++ );
         if( _myGenotype.get( pos++ ) < 0.5 )
         {
            h[ i ] *= -1;
         }
      }
      ifs.setParamH( h );
      double[] l = new double[ target.length ];
      for( int i=0; i<target.length; i++ )
      {
         l[ i ] = _myGenotype.get( pos++ );
         if( _myGenotype.get( pos++ ) < 0.5 )
         {
            l[ i ] *= -1;
         }
      }
      ifs.setParamL( l );
      double[] m = new double[ target.length ];
      for( int i=0; i<target.length; i++ )
      {
         m[ i ] = _myGenotype.get( pos++ );
         if( _myGenotype.get( pos++ ) < 0.5 )
         {
            m[ i ] *= -1;
         }
      }
      ifs.setParamM( m );
      for( int j=0; j<dataPoints; j++ )
      {
         _myFitness                 = 0; // This is crucial for the algorithm to work
         ArrayList< Double > v = ifs.createFractalData();
   //       v = (new MathUtilities()).normalize( v, 1 );

         for( int i=0; i<target.length; i++ )
         {
            _myFitness += Math.abs( v.get( i ) - target[ i ] );
         }

         _myFitness /= target.length;

         if( _myFitness != 0 )
         {
            _myFitness = 1 / _myFitness;
         }
         else
         {
            _myFitness = Double.MAX_VALUE;
         }
         fitness += _myFitness;
      }
      _myFitness /= (double)dataPoints;
//       System.out.println( "leaving eval fitness..." );

      return _myFitness;
   }

   /**
    *
    */
   public String toString()
   {
      String result = "\nFitness: " + _myFitness + "\n";
      return result;
   }
   
   /**
    *
    */
   public String toStringFinal()
   {
      String result = "genome:\n";
      _myFitness                 = 0; // This is crucial for the algorithm to work
      // may need to try to do a non-normalized version of the data
      double[][] streams         = getEnvironmentCache().getDataStreams();
      double[]   target          = getEnvironmentCache().getTargetStream();
      FractalInterpolationFunctionIFS ifs = new FractalInterpolationFunctionIFS( null, target.length, target.length, 50000);
      ifs.setInitialX( streams[ 0 ] );
      ifs.setInitialY( target );
      ifs.setInitialZ( streams[ 0 ] );
      int pos = 0;
      double[] d = new double[ target.length ];
      for( int i=0; i<target.length; i++ )
      {
         d[ i ] = _myGenotype.get( pos++ );
         if( _myGenotype.get( pos++ ) < 0.5 )
         {
            d[ i ] *= -1;
         }
         result += d[ i ] + " ";
      }
      ifs.setParamD( d );
      double[] h = new double[ target.length ];
      for( int i=0; i<target.length; i++ )
      {
         h[ i ] = _myGenotype.get( pos++ );
         if( _myGenotype.get( pos++ ) < 0.5 )
         {
            h[ i ] *= -1;
         }
         result += h[ i ] + " ";
      }
      ifs.setParamH( h );
      double[] l = new double[ target.length ];
      for( int i=0; i<target.length; i++ )
      {
         l[ i ] = _myGenotype.get( pos++ );
         if( _myGenotype.get( pos++ ) < 0.5 )
         {
            l[ i ] *= -1;
         }
         result += l[ i ] + " ";
      }
      ifs.setParamL( l );
      double[] m = new double[ target.length ];
      for( int i=0; i<target.length; i++ )
      {
         m[ i ] = _myGenotype.get( pos++ );
         if( _myGenotype.get( pos++ ) < 0.5 )
         {
            m[ i ] *= -1;
         }
         result += m[ i ] + " ";
      }
      ifs.setParamM( m );
      ArrayList< Double > v = ifs.createFractalData();
//       v = (new MathUtilities()).normalize( v, 1 );

      result += "\nPredictions:\n";
      for( int i=0; i<target.length; i++ )
      {
         result += v.get( i ) + "\n";
         _myFitness += Math.abs( v.get( i ) - target[ i ] );
      }

      _myFitness /= target.length;

      if( _myFitness != 0 )
      {
         _myFitness = 1 / _myFitness;
      }
      else
      {
         _myFitness = Double.MAX_VALUE;
      }
      result += "\nFitness: " + _myFitness;

      return result;
   }
}
