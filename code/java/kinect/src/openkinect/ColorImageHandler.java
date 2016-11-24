package openkinect;

import grafix.*;
import org.openkinect.Image;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.io.File;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 */
public class ColorImageHandler implements Image
{
   private RGBColorPoint[][]              _data        = null;
   private int                            _width       = 640;
   private int                            _height      = 480;
   private boolean                        _outputImage = true;
   private BufferedImage                  _colorImage  = null;
   private DataDeque< RGBColorPoint[][] > _dataDequeue = null;

   /**
    *
    */
   public ColorImageHandler()
   {
   }

   /**
    *
    */
   public ColorImageHandler( int w, int h, DataDeque< RGBColorPoint[][] > d )
   {
      _width      = w;
      _height     = h;
      _data       = new RGBColorPoint[ _width ][ _height ];

      setDataDequeue( d );

      if( _outputImage )
      {
         _colorImage = new BufferedImage( _width, _height, BufferedImage.TYPE_3BYTE_BGR );
      }
   }

   /**
    *
    */
   public void data( ByteBuffer data )
   {
      int         r      = 0;
      int         g      = 0;
      int         b      = 0;
      int         pixel  = 0;
      int         offset = 0;

      for( int y=0; y<_height; y++ )
      {
         for( int x=0; x<_width; x++ )
         {
            offset          = 3 * ( y * _width + x );
            r               = data.get( offset + 2 ) & 0xFF;
            g               = data.get( offset + 1 ) & 0xFF;
            b               = data.get( offset + 0 ) & 0xFF;
            pixel           =       (0xFF) << 24
                              | (b & 0xFF) << 16
                              | (g & 0xFF) << 8
                              | (r & 0xFF) << 0;
            _data[ x ][ y ] = new RGBColorPoint( r, g, b );
            _colorImage.setRGB( x, y, pixel );
         }
      }

      _dataDequeue.add( _data );

      if( _outputImage )
      {
         try
         {
            ImageIO.write( _colorImage, "jpg", new File( "/home/brandon/dist/test-images" + File.separator + "kinect.color.jpg" ) );
         }
         catch( IOException e )
         {
            System.err.println( "Unable to write color image data: " + e );
         }
      }
   }

   /**
    *
    */
   private int getIntensity( short depth )
   {
      int d     = Math.round( (1 - ( depth / 2047f ) ) * 255f );
      int pixel =   (0xFF)     << 24
                  | (d & 0xFF) << 16
                  | (d & 0xFF) << 8
                  | (d & 0xFF) << 0;
      return pixel;
   }

   private int getRGB( short depth )
   {
      int   r = 0;
      int   g = 0;
      int   b = 0;
      float v = depth / 2047f;
      
      v  = (float) Math.pow( v, 3 ) * 6;
      v *= ( 6 * 256 );

      int pval = Math.round( v );
      int lb   = pval & 0xff;

      switch( pval>>8 ) 
      {
         case 0:
            b = 255;
            g = 255 - lb;
            r = 255 - lb;
         break;
         case 1:
            b = 255;
            g = lb;
            r = 0;
         break;
         case 2:
            b = 255 - lb;
            g = 255;
            r = 0;
         break;
         case 3:
            b = 0;
            g = 255;
            r = lb;
         break;
         case 4:
            b = 0;
            g = 255 - lb;
            r = 255;
         break;
         case 5:
            b = 0;
            g = 0;
            r = 255 - lb;
         break;
         default:
            r = 0;
            g = 0;
            b = 0;
         break;
      }

      int pixel =   (0xFF)     << 24
                  | (b & 0xFF) << 16
                  | (g & 0xFF) << 8
                  | (r & 0xFF) << 0;

      return pixel;
   }

   /**
    *
    * @param boolean
    */
   public void setOutputImage( boolean outputImage )
   {
      _outputImage = outputImage;

      if( _outputImage )
      {
         _colorImage = new BufferedImage( _width, _height, BufferedImage.TYPE_3BYTE_BGR );
      }
      else
      {
         _colorImage = null;
      }
   }

   /**
    *
    * @return boolean
    */
   public boolean getOutputImage()
   {
      return _outputImage;
   }

   /**
    *
    * @param DataDeque< int[][] > _dataDequeue
    */
   public void setDataDequeue( DataDeque< RGBColorPoint[][] > dataDequeue )
   {
      _dataDequeue = dataDequeue;
   }

   /**
    *
    * @return DataDeque< RGBColorPoint[][] > _dataDequeue
    */
   public DataDeque< RGBColorPoint[][] > getDataDequeue()
   {
      return _dataDequeue;
   }

}
