package grafix;

import java.awt.image.*;
import java.awt.Color;
import java.util.*;
import java.io.*;

import javax.imageio.*;

import math.*;

/**
 *
 */
public class GrafixUtilities
{

   /**
    *
    */
   public GrafixUtilities()
   {
   }

   /**
    *
    * @param  String        filename and location of the image to load.
    * @return RGBColorPoint data or null if file isn't found.
    */
   public RGBColorPoint[][] getRGBData2( String filename )
   {
      BufferedImage bi = null;

      try
      {
         bi = ImageIO.read( new File( filename ) );
      }
      catch( IOException e )
      {
         System.err.println( "Unable to load image file: " + e );
         return null;
      }

      int[]             pixels    = bi.getRGB( 0, 0, bi.getWidth(), bi.getHeight(), null, 0, bi.getWidth());
      RGBColorPoint[][] pixelData = new RGBColorPoint[ bi.getWidth() ][ bi.getHeight() ];
      Color             c         = null;

      for( int i=0, k=0; i<pixelData.length; i++ )
      {
         for( int j=0; j<pixelData[ i ].length; j++ )
         {
            c                   = new Color( pixels[ k++ ] );
            pixelData[ i ][ j ] = new RGBColorPoint( c.getRed(), c.getGreen(), c.getBlue() );
         }
      }

      return pixelData;
   }

   /**
    *
    * @param  String        filename and location of the image to load.
    * @return RGBColorPoint data or null if file isn't found.
    */
   public RGBColorPoint[][] getRGBData( String filename )
   {
      BufferedImage bi = null;

      try
      {
         bi = ImageIO.read( new File( filename ) );
      }
      catch( IOException e )
      {
         System.err.println( "Unable to load image file: " + e );
         return null;
      }

      RGBColorPoint[][] pixelData = new RGBColorPoint[ bi.getWidth() ][ bi.getHeight() ];
      Color             c         = null;

      for( int i=0; i<pixelData.length; i++ )
      {
         for( int j=0; j<pixelData[ 0 ].length; j++ )
         {
            c                   = new Color( bi.getRGB( i, j ) );
            pixelData[ i ][ j ] = new RGBColorPoint( c.getRed(), c.getGreen(), c.getBlue() );
         }
      }

      return pixelData;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void putRGBData2( RGBColorPoint[][] data, String filename, String format )
   {
      int[] pixels = new int[ data.length * data[ 0 ].length ];
      Color c      = null;

      for( int i=0, k=0; i<data.length; i++ )
      {
         for( int j=0; j<data[ i ].length; j++ )
         {
            c             = new Color( data[ i ][ j ].getRedComponent(), data[ i ][ j ].getGreenComponent(), data[ i ][ j ].getBlueComponent() );
            pixels[ k++ ] = c.getRGB();
         }
      }

      try
      {
         BufferedImage bi = new BufferedImage( data.length, data[ 0 ].length, BufferedImage.TYPE_INT_RGB );
         bi.setRGB( 0, 0, data.length, data[ 0 ].length, pixels, 0, data.length );
         ImageIO.write( bi, format, new File( filename ) );
      }
      catch( IOException e )
      {
         System.out.println( "Unable to write RGB data: " + e );
      }
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void putRGBData( RGBColorPoint[][] data, String filename, String format )
   {
      Color c = null;

      try
      {
         BufferedImage bi = new BufferedImage( data.length, data[ 0 ].length, BufferedImage.TYPE_INT_RGB );

         for( int i=0, k=0; i<data.length; i++ )
         {
            for( int j=0; j<data[ i ].length; j++ )
            {
               c = new Color( data[ i ][ j ].getRedComponent(), 
                              data[ i ][ j ].getGreenComponent(), 
                              data[ i ][ j ].getBlueComponent() );
               bi.setRGB( i, j, c.getRGB() );
            }
         }

         ImageIO.write( bi, format, new File( filename ) );
      }
      catch( IOException e )
      {
         System.out.println( "Unable to write RGB data: " + e );
      }
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void printAvailableFormats()
   {
      String[] formats = ImageIO.getWriterFormatNames();
      for( int i=0; i<formats.length; i++ )
      {
         System.out.println( "A format: " + formats[ i ] );
      }
   }

   /**
    * Since we're dealing with Hilbert mappings, we assume the matrices are square and a power of 2.
    *
    *
    * @param RGBColorPoint[][]
    * @param int -- the number of square blocks per row or column.
    * @return Size is: n x n x width x width
    */
   public RGBColorPoint[][][][] getHilbertTiling( RGBColorPoint[][] data, int width )
   {
      int                   offset = width / 2;
      int                   n      = (data.length / offset) - 1;
      RGBColorPoint[][][][] tiling = new RGBColorPoint[ n ][ n ][][];
      RGBColorPoint[][]     t      = null;

      for( int i=0; i<n; i++ )
      {
         for( int j=0; j<n; j++ )
         {
            t = new RGBColorPoint[ width ][ width ];
            for( int k=0; k<width; k++ )
            {
               for( int w=0; w<width; w++ )
               {
                  t[ k ][ w ] = data[ (i * offset) + k ][ (j * offset) + w ];
               }
            }
            tiling[ i ][ j ] = t;
         }
      }

      return tiling;
   }

   /**
    *
    * The formula for luminosity is 0.21 R + 0.71 G + 0.07 B.
    *
    * @param TYPE
    * @return TYPE
    */
   public RGBColorPoint[][] convertToGrayscale( RGBColorPoint[][] source )
   {
      RGBColorPoint[][] grayscale = new RGBColorPoint[ source.length ][ source[ 0 ].length ];
      double luminosity = 0;

      for( int i=0; i<source.length; i++ )
      {
         for( int j=0; j<source[ i ].length; j++ )
         {
            luminosity = source[ i ][ j ].getRed()   * 0.21 +
                         source[ i ][ j ].getGreen() * 0.71 +
                         source[ i ][ j ].getBlue()  * 0.07;
            grayscale[ i ][ j ] = new RGBColorPoint( luminosity, luminosity, luminosity );
         }
      }

      return grayscale;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public double[][] getInvariantFeatureVector( ArrayList< int[] > points )
   {
      double[][]    r  = new double[ 2 ][];
      double[]      v  = new double[ points.size() ];
      StatUtilities su = new StatUtilities();
      MathUtilities mu = new MathUtilities();

      Double[] xlist = new Double[ points.size() ];
      Double[] ylist = new Double[ points.size() ];
      int      pos   = 0;

      for( int[] point : points )
      {
         xlist[ pos   ] = new Double( point[ 0 ] );
         ylist[ pos++ ] = new Double( point[ 1 ] );
      }

      double[] xl = new double[ xlist.length ];
      double[] yl = new double[ ylist.length ];

      for( int i=0; i<xlist.length; i++ )
      {
         xl[ i ] = xlist[ i ];
         yl[ i ] = ylist[ i ];
      }

      double   xmedian = su.quickMedian( xl );
      double   ymedian = su.quickMedian( yl );
      double[] medians = new double[ 2 ];
      pos              = 0;
      medians[ 0 ]     = xmedian;
      medians[ 1 ]     = ymedian;
                       
      for( int[] point : points )
      {
         v[ pos++ ] = mu.getEuclideanDistance2D( xmedian, ymedian, (double) point[ 0 ], (double) point[ 1 ] );
      }

      Arrays.sort( v );

      r[ 0 ] = medians;
      r[ 1 ] = v;

      return r;
   }

}
