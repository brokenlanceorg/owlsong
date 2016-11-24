package grafix;

import java.util.*;

/**
 * x is in [0, 1]
 * y is in [0, 1]
 * z is in [0, 1]
 */
public class CIEColorPoint implements ColorPoint
{

   private double _x;
   private double _y;
   private double _z;

   /**
    *
    */
   public CIEColorPoint()
   {
   }

   /**
    *
    */
   public CIEColorPoint( double x, double y, double z )
   {
      _x = x;
      _y = y;
      _z = z;
   }

   /**
    *
    */
   public double getX()
   {
      return _x;
   }

   /**
    *
    */
   public double getY()
   {
      return _y;
   }

   /**
    *
    */
   public double getZ()
   {
      return _z;
   }

   /**
    *
    */
   public CIEColorPoint mixColorPoint( CIEColorPoint point )
   {
      double t1 = 0;
      double t2 = 0;
      double t3 = 0;
      double x  = 0;
      double y  = 0;
      double z  = 0;

      if( _y != 0 )
      {
         t1 = _z / _y;
      }

      if( point.getY() != 0 )
      {
         t2 = point.getZ() / point.getY();
      }

      t3 = t1 + t2;

      if( t3 != 0 )
      {
         x = ( (_x * t1) + (point.getX() * t2) ) / (t3);
         y = ( (_y * t1) + (point.getY() * t2) ) / (t3);
      }

      z = _z + point.getZ();

      return ( new CIEColorPoint( x, y, z ) );
   }

   /**
    *
    */
   public ColorPoint getCIEColorPoint()
   {
      return this;
   }

   /**
    *
    */
   public ColorPoint getRGBColorPoint()
   {
      double x = 0;
      double z = 0;
      double r = 0;
      double g = 0;
      double b = 0;

      if( _y != 0 )
      {
         x = _x * _z / _y;
         z = ( 1 - _x - _y ) * _z / _y;
      }

      r = ( 2.739 * x ) - ( 1.145 * _z ) - ( 0.424 * z );
      g = (-1.119 * x ) + ( 2.029 * _z ) + ( 0.033 * z );
      b = ( 0.138 * x ) - ( 0.333 * _z ) + ( 1.105 * z );

      return ( new RGBColorPoint( r, g, b ) );
   }

   /**
    *
    */
   public ColorPoint getHLSColorPoint()
   {
      return ( (RGBColorPoint) getRGBColorPoint() ).getHLSColorPoint();
   }

}
