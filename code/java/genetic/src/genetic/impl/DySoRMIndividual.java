package genetic.impl;

import genetic.*;
import math.*;

import java.math.*;
import java.util.*;

/**
 *  The basic idea behind the Dynamical System of Regression Models (DySoRM)
 *  is that we use the non-linear regression models of FunctionCorrelations
 *  to create a dynamical system or iterated function system to extrapolate
 *  the relationships between time-based variables.
 * 
 * The DySoRM algorithm is as follows:
 *   instantiate a DySoRM object.
 * 
 * 
 * 
 * 
 * 
 * 
 */
public class DySoRMIndividual extends Individual
{

   private final int _regessionModelGenomeSize = 17;
   private final int _iterations               = 2;

   /**
    * Default constructor.
    */
   public DySoRMIndividual()
   {
   }

   /**
    * Implementation of Interface.
    * This is the proper implementation, so please take note.
    */
   public Individual clone()
   {
      Individual indy = new DySoRMIndividual();

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
      _myGenotype = new ArrayList< Double >();
      int size = (getEnvironmentCache().getDysormVariables()).length * _regessionModelGenomeSize;

      for( int i=0; i<size; i++ )
      {
         _myGenotype.add( Math.random() );
      }
   }

   /**
    * Implementation of Interface.
    *
    *
    */
   public double evaluateFitness()
   {
      _myFitness = 0;
      double[][] streams = getEnvironmentCache().getDysormVariables();
      double[][] variables = new double[ streams.length ][ streams[ 0 ].length ];
      for( int i=0; i<streams.length; i++ )
      {
         for( int j=0; j<streams[ i ].length; j++ )
         {
            variables[ i ][ j ] = streams[ i ][ j ];
         }
      }
      DySoRM dyn = new DySoRM( _myGenotype, variables );
      double[][] extrapolations = dyn.evaluate( _iterations );

      for( int i=0; i<extrapolations.length; i++ )
      {
         for( int j=0; j<extrapolations[ i ].length; j++ )
         {
//             System.out.println( "variable is: " + streams[ i ][ j ] + " extrap: " + extrapolations[ i ][ j ] );

            if( extrapolations[ i ][ j ] != Double.NaN )
            {
               _myFitness += Math.abs( streams[ i ][ j ] - extrapolations[ i ][ j ] );
            }
            else
            {
               return Double.MIN_VALUE;
            }
         }
      }

      if( _myFitness != 0 )
      {
         _myFitness = 1 / _myFitness;
      }
      else if( _myFitness == Double.NaN )
      {
         _myFitness = Double.MIN_VALUE;
      }
      else
      {
         _myFitness = Double.MAX_VALUE;
      }

//       System.out.println( "Calculated a fitness of: " + _myFitness );

      return _myFitness;
   }

   /**
    *
    */
   public String toString()
   {
      String result = "\nGenome: ";
      for( double gene : _myGenotype )
      {
         result += gene + " ";
      }
      result += "\nFitness: " + _myFitness + "\n";

      return result;
   }
}
