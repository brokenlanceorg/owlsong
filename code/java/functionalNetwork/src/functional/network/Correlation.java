package functional.network;

import java.util.*;
import common.*;
import math.*;

/**
 * This class is the base class for the Correlation hierarchy.
 * The basic semantic encapsulated here is the notion of a 
 * general mapping or correlation (via some algorithm) from
 * one set of independent data to another set of dependent
 * data values.
 * There are two main methods therefore: one method to train
 * or learn the mapping and another to predict or correlate
 * given some set of independent data values.
 */
public abstract class Correlation implements Persistable
{

   // The rows are the different independent variables while the
   // columns are the different data instances.
   private double[][] _independentData;

   // In the case of ManyToOne subclasses, the zeroth row will 
   // contain all the training instances.
   private double[][] _dependentData;

   // The name or identification of this particular correlation.
   private String     _name;

   // If the independent data has been mapped into an interval, we will need to
   // be able to recover these mappings in order to map future data into the
   // same interval for the correlation to operate upon.
   private Interval[] _independentMaps;

   // Likewise, there will be dependent maps for the dependent data.
   private Interval[] _dependentMaps;

   // Likewise, there will be dependent maps for the dependent data.
   private Interval _dependentMap;

   /**
    *
    */
   public Correlation()
   {
   }

   /**
    *
    */
   public Correlation( String name )
   {
      setName( _name );
   }

   /**
    * This is not a deep copy of the data values.
    */
   public Correlation( double[][] data, double[][] target )
   {
      _independentData = data;
      _dependentData = target;
   }

   /**
    * Should this perform a deep copy?
    */
   public void setIndependentData( double[][] data )
   {
      _independentData = data;
   }

   /**
    *
    */
   public double[][] getIndependentData()
   {
      return _independentData;
   }

   /**
    * Should this perform a deep copy?
    */
   public void setDependentData( double[][] data )
   {
      _dependentData = data;
   }

   /**
    *
    */
   public double[][] getDependentData()
   {
      return _dependentData;
   }

   /**
    *
    * @param String -- The name of this correlation.
    */
   public void setName( String name )
   {
      _name = name;
   }

   /**
    *
    * @return String -- The name of this correlation.
    */
   public String getName()
   {
      return _name;
   }

   /**
    * 
    */
   public void setIndependentMaps( Interval[] maps )
   {
      _independentMaps = maps;
   }

   /**
    *
    */
   public Interval[] getIndependentMaps()
   {
      return _independentMaps;
   }

   /**
    * 
    */
   public void setDependentMaps( Interval[] maps )
   {
      _dependentMaps = maps;
   }

   /**
    *
    */
   public Interval[] getDependentMaps()
   {
      return _dependentMaps;
   }

   /**
    * 
    */
   public void setDependentMap( Interval map )
   {
      _dependentMap = map;
   }

   /**
    *
    */
   public Interval getDependentMap()
   {
      return _dependentMap;
   }

   /**
    *
    */
   public abstract void train();

   /**
    * @param double[] All subclasses will take as input an array (one-dimensional
    * data values) with which it will predict or correlate as best it can.
    * @return double[] In the case of the ManyToOne classes, this will just be
    * an array with a single data output value in the zeroth position; in the other
    * cases such as the ManyToMany subclasses, the return will be a vector.
    */
   public abstract double[] correlate( double[] input );

}
