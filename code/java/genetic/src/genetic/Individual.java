package genetic;

import java.util.*;
import java.io.*;
import math.MathUtilities;

/**
 * The interface that all genetic individuals must implement.
 * 
 * 
 * 
 */
public abstract class Individual implements Serializable, Comparable< Individual >
{

   // The last calculated fitness value
   protected double _myFitness = -1;

   // The number of epochs this individual has lived
   protected int _myAge = -1;

   // The genotype -- which is 
   protected ArrayList<Double> _myGenotype = new ArrayList<Double>();

   // Every individual can have access to the EnvironmentCache
   private EnvironmentCache _environmentCache;

   // For the clone method:
   private String _implementationClass;

   // For the clone method:
   private int _genomeLength;

   /**
    * Implementation of Interface.
    */
   public abstract double evaluateFitness();

   /**
    * Implementation of Interface.
    * This is the proper implementation, so please take note.
    */
   public Individual clone()
   {
      Individual indy = getIndividualInstance();
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
      int           s  = getGenomeLength();
      _myGenotype      = new ArrayList< Double >( s );

      for( int i=0; i<s; i++ )
      {
         _myGenotype.add( mu.random() );
      }
   }

   /**
    *
    */
   private Individual getIndividualInstance()
   {
      Individual indy = null;

      try
      {
         Class implClass = Class.forName( _implementationClass );
         indy            = (Individual)implClass.newInstance();
      }
      catch( ClassNotFoundException e )
      {
         System.err.println( "Unable to find class: " + _implementationClass + "\nException: " + e );
         System.exit( 1 );
      }
      catch( InstantiationException e )
      {
         System.err.println( "Unable to instantiate class: " + _implementationClass + "\nException: " + e );
         System.exit( 1 );
      }
      catch( IllegalAccessException e )
      {
         System.err.println( "Unable to instantiate class: " + _implementationClass + "\nException: " + e );
         System.exit( 1 );
      }

      return indy;
   }

   /**
    * Returns the last calculated fitness value.
    */
   public double getFitness()
   {
      return _myFitness;
   }

   /**
    * Sets the last calculated fitness value.
    */
   public void setFitness( double fitness )
   {
      _myFitness = fitness;
   }

   /**
    * Returns the age of this instance.
    */
   public int getAge()
   {
      return _myAge;
   }

   /**
    * Ages this individual by an epoch.
    */
   public void age()
   {
      _myAge++;
   }

   /**
    * Retrieves the genotype.
    */
   public ArrayList<Double> getGenotype()
   {
      return _myGenotype;
   }

   /**
    * Sets the genotype.
    */
   public void setGenotype( ArrayList<Double> genotype )
   {
      _myGenotype = genotype;
   }

   /**
    *
    */
   public void setEnvironmentCache( EnvironmentCache cache )
   {
      _environmentCache = cache;
   }

   /**
    *
    */
   public EnvironmentCache getEnvironmentCache()
   {
      return _environmentCache;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void setImplementationClass( String c )
   {
      _implementationClass = c;
   }

   /**
    *
    */
   public void setGenomeLength( int g )
   {
      _genomeLength = g;
   }

   /**
    *
    */
   public int getGenomeLength()
   {
      return _genomeLength;
   }

   /**
    * This will sort in descending order
    */
   public int compareTo( Individual o )
   {
      if( getFitness() < o.getFitness() )
      {
         return 1;
      }
      else if( getFitness() > o.getFitness() )
      {
         return -1;
      }
      return 0;
   }

   /**
    * toString.
    */
   public String toString()
   {
      StringBuffer me = new StringBuffer( "Genotype for " + super.toString() );

      for( Double d : _myGenotype )
      {
         me.append( " " );
         me.append( d );
      }

      me.append( " fitness: " + _myFitness );

      return me.toString();
   }

   /**
    * toString.
    */
   public String toStringFinal()
   {
      return "";
   }

}
