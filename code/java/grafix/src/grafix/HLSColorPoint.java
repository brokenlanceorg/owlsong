package grafix;

import java.util.*;

/**
 * Hue is in [0, 360]
 * Luminosity is in [0, 1]  -- 0.5 gives a good balance, 0.7 starts to get washed out, 0.1 is dark
 * Saturation is in [0, 1]
 */
public class HLSColorPoint implements ColorPoint
{

   private double _h;
   private double _l;
   private double _s;

   /**
    *
    */
   public HLSColorPoint()
   {
   }

   /**
    *
    */
   public HLSColorPoint( double h, double l, double s )
   {
      _h = h;
      _l = l;
      _s = s;
   }

   /**
    *
    */
   public double getH()
   {
      return _h;
   }

   /**
    *
    */
   public double getL()
   {
      return _l;
   }

   /**
    *
    */
   public double getS()
   {
      return _s;
   }

   /**
    *
    */
   public ColorPoint getCIEColorPoint()
   {
      return ( (RGBColorPoint) getRGBColorPoint() ).getCIEColorPoint();
   }

   /**
    *
    */
   public ColorPoint getRGBColorPoint()
   {
      double m = 0;
      double n = 0;
      double r = 0;
      double g = 0;
      double b = 0;

      if( _l <= 0.5 )
      {
         n = _l * ( 1 + _s );
      }
      else
      {
         n = _l + ( _s - ( _l * _s ) );
      }

      m = 2 * _l - n;

      // achromatic case, hue undefined
      if( ( _s == 0 ) && ( _h == -1 ) )
      {
         r = g = b = _l;
      }
      else
      {
         r = value( m, n, _h + 120 );
         g = value( m, n, _h );
         b = value( m, n, _h - 120 );
      }

      return ( new RGBColorPoint( r, g, b ) );
   }

   /**
    *
    */
   private double value( double a, double b, double hue )
   {

      if( hue > 360 )
      {
         hue = hue - 360;
      }
      else if( hue < 0 )
      {
         hue = hue + 360;
      }

      if( hue < 60 )
      {
         return ( a + ( b - a ) * ( hue / 60 ) );
      }
      else if( hue < 180 )
      {
         return b;
      }
      else if( hue < 240 )
      {
         return ( a + ( b - a ) * ( ( 240 - hue ) / 60 ) );
      }
      else
      {
         return a;
      }

   }

   /**
    *
    */
   public ColorPoint getHLSColorPoint()
   {
      return this;
   }

}
