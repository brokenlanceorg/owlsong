package genetic.impl;

import genetic.*;
import math.*;

import java.math.*;
import java.util.*;

/**
 *  The idea here is that this is a functional binary classifier in that vectors can be shown
 *  to the functional individual and it will assign it to one of two classes. Over the course
 *  of the domain, the entropy as to which class it will be assigned will be maximized.
 *  This is essentially the same as the SurjectiveBinaryClassifierIndividual except that the full domain
 *  is mapped -- i.e., a surjective mapping.
 */
public class SurjectiveBinaryClassifierIndividual extends Individual
{

   /**
    * Default constructor.
    */
   public SurjectiveBinaryClassifierIndividual()
   {
   }

   /**
    * Implementation of Interface.
    * This is the proper implementation, so please take note.
    */
   public Individual clone()
   {
      Individual indy = new SurjectiveBinaryClassifierIndividual();
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
      int s       = getEnvironmentCache().getGenomeLength();
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
      _myFitness = 0; // This is crucial for the algorithm to work this is because we can't leave the myFitness member variable
                      // in an "inconsistent" state because the GA will think that whatever value it was, it was an "earned" value.
      double[][]          targets = getEnvironmentCache().getDataStreams();
//       int                 length  = (int)    ( (double) targets[ 0 ].length * _myGenotype.get( 0 ) );
      int                 s1      = 0;
      int                 s2      = 0;
      double              e       = (double) ( (double) targets[ 0 ].length * _myGenotype.get( 0 ) );
      ArrayList< Double > genome  = new ArrayList< Double >( _myGenotype.size() - 1 );

      for( int i=1; i<_myGenotype.size(); i++ )
      {
         genome.add( _myGenotype.get( i ) );
      }
//       if( ( s1 + length ) >= targets[ 0 ].length )
//       {
//          s1 = targets[ 0 ].length - length;
//       }
//       if( ( s2 + length ) >= targets[ 0 ].length )
//       {
//          s2 = targets[ 0 ].length - length;
//       }

      double[][]            streams = new double[ 1 ][ targets[ 0 ].length ];
      double                local   = 0.0;
      double                value   = 0.0;
      int                   left    = 0;
      FunctionalCorrelation root    = FunctionalCorrelation.buildBinaryTree( genome, streams ); 

      for( int i=0; i<targets.length; i++ )
      {
         for( int j=0; j<targets[ 0 ].length; j++ )
         {
            streams[ 0 ][ j ] = targets[ i ][ j ];
         }

         local = 0;

         for( int j=0; j<targets[ 0 ].length; j++ )
         {
            value = root.evaluate( j );

            if(    Double.isNaN( value ) 
                || Double.isInfinite( value ) )
            {
               _myFitness = Double.MIN_VALUE;
               return _myFitness;
            }
            local += Math.abs( value - targets[ i ][ j ] );
         }

         if( local <= e )
         {
            left++;
         }
      }

      s1 = (int) ( (double) targets.length / 2.0 );
      if( ( targets.length % 2 ) == 0 )
      {
         if( left == s1 )
         {
            _myFitness = Double.MAX_VALUE;
            return _myFitness;
         }
      }
      else
      {
         s2 = s1 + 1;
         if( left == s1 || left == s2 )
         {
            _myFitness = Double.MAX_VALUE;
            return _myFitness;
         }
      }

      _myFitness  = (double) left / (double) targets.length;
      _myFitness -= 0.5;
      _myFitness  = Math.abs( _myFitness );

      if( _myFitness != 0.0 )
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
      String result = "\nFitness: " + _myFitness + "\n";
      return result;
   }
   
   /**
    *
    */
   public String toStringFinal()
   {
      String result = "";

      double[][]          targets = getEnvironmentCache().getDataStreams();
//       int                 length  = (int)    ( (double) targets[ 0 ].length * _myGenotype.get( 0 ) );
      int                 s1      = 0;
      int                 s2      = 0;
      double              e       = (double) ( (double) targets[ 0 ].length * _myGenotype.get( 0 ) );
      ArrayList< Double > genome  = new ArrayList< Double >( _myGenotype.size() - 1 );

      for( int i=1; i<_myGenotype.size(); i++ )
      {
         genome.add( _myGenotype.get( i ) );
      }
//       if( ( s1 + length ) >= targets[ 0 ].length )
//       {
//          s1 = targets[ 0 ].length - length;
//       }
//       if( ( s2 + length ) >= targets[ 0 ].length )
//       {
//          s2 = targets[ 0 ].length - length;
//       }

      double[][]            streams = new double[ 1 ][ targets[ 0 ].length ];
      double                local   = 0;
      int                   left    = 0;
      FunctionalCorrelation root    = FunctionalCorrelation.buildBinaryTree( genome, streams ); 

      result += "\ne:      " + e;
//       result += "\nlength: " + length;
//       result += "\ns1:     " + s1;
//       result += "\ns2:     " + s2;

      for( int i=0; i<targets.length; i++ )
      {
         for( int j=0; j<targets[ 0 ].length; j++ )
         {
            streams[ 0 ][ j ] = targets[ i ][ s1 + j ];
         }

         local = 0;

         for( int j=0; j<targets[ 0 ].length; j++ )
         {
            double value = root.evaluate( j );

            local  += Math.abs( value - targets[ i ][ j ] );
            result += "\n  A target: " + targets[ i ][ j ] + " value: " + value;
         }

         result += "\nLocal error: " + local;

         if( local <= e )
         {
            left++;
         }
      }

      result += "\nLeft count: " + left + " total count: " + targets.length;

      return result;
   }

}
