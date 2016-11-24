package openkinect.domain;

import java.util.*;
import java.math.*;

/**
 *
 */
public class ImageObject
{
   private double       _x;
   private double       _y;
   private BigInteger[] _featureClass;
   private String[]     _addresses;

   /**
    *
    */
   public ImageObject()
   {
   }

   /**
    *
    */
   public ImageObject( double x, double y, BigInteger[] classes, String[] addresses )
   {
      setX( x );
      setY( y );
      setFeatureClass( classes );
      setTreeAddresses( addresses );
   }

   /**
    *
    * @param double
    */
   public void setX( double x )
   {
      _x = x;
   }

   /**
    *
    * @return double
    */
   public double getX()
   {
      return _x;
   }

   /**
    *
    * @param double
    */
   public void setY( double y )
   {
      _y = y;
   }

   /**
    *
    * @return double
    */
   public double getY()
   {
      return _y;
   }

   /**
    *
    * @param double[]
    */
   public void setFeatureClass( BigInteger[] featureClass )
   {
      _featureClass = new BigInteger[ featureClass.length ];
      for( int i=0; i<_featureClass.length; i++ )
      {
         _featureClass[ i ] = featureClass[ i ];
      }
   }

   /**
    *
    * @return BigInteger[]
    */
   public BigInteger[] getFeatureClass()
   {
      return _featureClass;
   }

   /**
    *
    * @param String[]
    */
   public void setTreeAddresses( String[] addresses )
   {
      _addresses = new String[ addresses.length ];
      for( int i=0; i<_addresses.length; i++ )
      {
         _addresses[ i ] = addresses[ i ];
      }
   }

   /**
    *
    * @return String[]
    */
   public String[] getTreeAddresses()
   {
      return _addresses;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public String toString()
   {
      String r = "\n   Object Class: ";

      for( int i=0; i<_featureClass.length; i++ )
      {
         r += _featureClass[ i ] + " ";
      }

      r += "\n   Object Tree Address: ";

      for( int i=0; i<_addresses.length; i++ )
      {
         r += _addresses[ i ] + " ";
      }

      r += "\n   Object Centroid: " + _x + " " + _y;

      return r;
   }

}
