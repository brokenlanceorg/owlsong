package math;

import java.io.*;
import java.util.*;

/**
 *
 * Interval: This class will calculate interval mappings. The constructor has the target interval,
 * then there are methods to add data points or setters for all the data at once. The getter will
 * lazily calculate the mapping based on a boolean. The boolean is reset in the setters. There will
 * be additional getters to get the mapping constants and additional methods to invert a value.
 *
 */
public class Interval implements Serializable
{

   // These hold the interval values to which the input data will be mapped
   private double _left;
   private double _right;

   // These hold the scale constants
   private double _min;
   private double _max;

   // The pre-mapped and mapped data
   private ArrayList< Double > _rawData;
   private ArrayList< Double > _mappedData;

   // boolean check for calculating the mapping
   private boolean _mapped;

   /**
    *
    */
   public Interval()
   {
      this( 0, 1 );
   }

   /**
    *
    */
   public Interval( double left, double right )
   {
      setLeftInterval( right );
      setRightInterval( right );
   }

   /**
    *
    */
   public void setLeftInterval( double left )
   {
      _left = left;
   }

   /**
    *
    */
   public double getLeftInterval()
   {
      return _left;
   }

   /**
    *
    */
   public void setRightInterval( double right )
   {
      _right = right;
   }

   /**
    *
    */
   public double getRightInterval()
   {
      return _right;
   }

   /**
    *
    */
   public double getMinValue()
   {
      return _min;
   }

   /**
    *
    */
   public double getMaxValue()
   {
      return _max;
   }

   /**
    *
    */
   public void setRawData( ArrayList< Double > data )
   {
      _rawData = data;
      _mapped = false;
   }

   /**
    *
    */
   public void setRawData( double[] data )
   {
      for( int i=0; i<data.length; i++ )
      {
         addDataItem( data[ i ] );
      }
      _mapped = false;
   }

   /**
    *
    */
   public ArrayList< Double > getRawData()
   {
      return _rawData;
   }

   /**
    *
    */
   public void addDataItem( double value )
   {
      if( _rawData == null )
      {
         _rawData = new ArrayList< Double >();
      }
      _rawData.add( value );
      _mapped = false;
   }

   /**
    *
    */
   public ArrayList< Double > getMappedData()
   {
      if( _mapped == false )
      {
         mapRawData();
      }

      return _mappedData;
   }

   /**
    *
    */
   private void mapRawData()
   {
      _min = Double.MAX_VALUE;
      _max = Double.MIN_VALUE;
      _mappedData = new ArrayList< Double >( _rawData.size() );

      // First, need to find the min value in the array
      for( double value : _rawData )
      {
         if( value < _min )
         {
            _min = value;
         }
      }

      // Next, translate by the min amount
      for( double value : _rawData )
      {
         double temp = (value - _min);
         _mappedData.add( temp );
         if( temp > _max )
         {
            _max = temp;
         }
      }

      // Finally, scale by max
      for( int i=0; i<_mappedData.size(); i++ )
      {
         _mappedData.set( i, ( _right * ( _mappedData.get( i ) / _max ) ) );
      }

      _mapped = true;
   }

   /**
    *
    */
   public double invert( double value )
   {
      if( _mapped == false )
      {
         mapRawData();
      }
      double inverted = (value / _right);
      inverted *= _max;
      inverted += _min;
      return inverted;
   }

   /**
    *
    */
   public double map( double value )
   {
      if( _mapped == false )
      {
         mapRawData();
      }
      double mapped = value;
      mapped -= _min;
      mapped /= _max;
      mapped *= _right;
      return mapped;
   }

   /**
    *
    */
   public ArrayList< Double > mapData( ArrayList< Double > data )
   {
      setRawData( data );
      return getMappedData();
   }

   /**
    *
    */
   public double[] mapData( double[] data )
   {
      setRawData( data );
      ArrayList< Double > ma = getMappedData();
      double[] mapped = new double[ ma.size() ];

      for( int i=0; i<mapped.length; i++ )
      {
         mapped[ i ] = ma.get( i );
      }

      return mapped;
   }

}
