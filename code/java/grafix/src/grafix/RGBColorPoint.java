package grafix;

import java.util.*;

/**
 * The RGB values lie within the interval [0, 1]
 * The data is stored in such a way that the upper left corner is 0, 0
 * the point below is 0, 1, and below that is 0, 2, etc.
 * The point to the right of the upper corner is 1, 0, the point below that is
 * 1, 1
 */
public class RGBColorPoint implements ColorPoint
{

   private int    _maxByte = 255;
   private double _red;
   private double _green;
   private double _blue;

   /**
    *
    */
   public RGBColorPoint()
   {
   }

   /**
    *
    */
   public RGBColorPoint( double r, double g, double b )
   {
      _red   = r;
      _green = g;
      _blue  = b;
   }

   /**
    *
    */
   public RGBColorPoint( int r, int g, int b )
   {
      _red   = (double) r / (double) _maxByte;
      _green = (double) g / (double) _maxByte;
      _blue  = (double) b / (double) _maxByte;
   }

   /**
    *
    */
   public double getRed()
   {
      return _red;
   }

   /**
    *
    */
   public double getGreen()
   {
      return _green;
   }

   /**
    *
    */
   public double getBlue()
   {
      return _blue;
   }

   /**
    *
    */
   public void setRed( double r )
   {
      _red = r;
   }

   /**
    *
    */
   public void setGreen( double g )
   {
      _green = g;
   }

   /**
    *
    */
   public void getBlue( double b )
   {
      _blue = b;
   }

   /**
    *
    */
   public int getRedComponent()
   {
      return (int)(_red * _maxByte);
   }

   /**
    *
    */
   public int getGreenComponent()
   {
      return (int)(_green * _maxByte);
   }

   /**
    *
    */
   public int getBlueComponent()
   {
      return (int)(_blue * _maxByte);
   }

   /**
    *
    */
   public ColorPoint getCIEColorPoint()
   {
      double x   = 0;
      double x1  = 0;
      double y   = 0;
      double z   = 0;
      double z1  = 0;
      double sum = 0;

      x1 = (0.478 * _red) + (0.299 * _green) + (0.175 * _blue);
      z  = (0.263 * _red) + (0.655 * _green) + (0.081 * _blue);
      z1 = (0.020 * _red) + (0.160 * _green) + (0.908 * _blue);

      sum = x1 + z + z1;
      x = x1 / sum;
      y = z / sum;

      return (new CIEColorPoint( x, y, z ));
   }

   /**
    *
    */
   public ColorPoint getRGBColorPoint()
   {
      return this;
   }

   /**
    *
    */
   public ColorPoint getHLSColorPoint()
   {
      double min = Math.min( Math.min( _red, _green ), _blue );
      double max = Math.max( Math.max( _red, _green ), _blue );
      double d   = 0;
      double h   = 0;
      double l   = ( min + max ) / 2;
      double s   = 0;

      if( max == min )
      {
         s = 0;
         h = -1;
      }
      else
      {
         d = max - min;

         // Calculate the Saturation:
         if( l <= 0.5 )
         {
            s = ( d / ( max + min ) );
         }
         else
         {
            s = ( d / ( 2 - d ) );
         }

         // Calculate the Hue:
         if( _red == max )
         {
            h = ( ( _green - _blue ) / d );
         }
         else if( _green == max )
         {
            h = 2 + ( ( _blue - _red ) / d );
         }
         else if( _blue == max )
         {
            h = 4 + ( ( _red - _green ) / d );
         }

         h *= 60;
         if( h < 0 )
         {
            h += 360;
         }
      }

      return (new HLSColorPoint( h, l, s ));
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public boolean equals( Object obj )
   {
      boolean       e   = false;
      RGBColorPoint rhs = null;

      if( obj != null )
      {
         if( obj instanceof RGBColorPoint )
         {
            rhs = (RGBColorPoint) obj;
            e   =    ( getRedComponent()   == rhs.getRedComponent() ) 
                  && ( getGreenComponent() == rhs.getGreenComponent() )
                  && ( getBlueComponent()  == rhs.getBlueComponent() );
         }
      }

      return e;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public int hashCode()
   {
      return ( getRedComponent() + getGreenComponent() + getBlueComponent() );
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public String toString()
   {
      String s = "Red: ";
      s += getRedComponent();
      s += " Green: ";
      s += getGreenComponent();
      s += " Blue: ";
      s += getBlueComponent();
      return s;
   }

}
