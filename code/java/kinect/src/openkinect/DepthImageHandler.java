package openkinect;

import org.openkinect.Image;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.io.File;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * final BufferedImage color = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
 * final BufferedImage depth = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
 * final BufferedImage depth = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
 */
public class DepthImageHandler implements Image
{
   private int[][]              _data         = null;
   private int                  _width        = 640;
   private int                  _height       = 480;
   private boolean              _outputImage  = true;
   private BufferedImage        _depthImage   = null;
   private DataDeque< int[][] > _depthDequeue = null;

   /**
    *
    */
   public DepthImageHandler()
   {
   }

   /**
    *
    */
   public DepthImageHandler( int w, int h, DataDeque< int[][] > d )
   {
      _width        = w;
      _height       = h;
      _depthDequeue = d;

      if( _outputImage )
      {
         _depthImage = new BufferedImage( _width, _height, BufferedImage.TYPE_BYTE_GRAY );
      }
   }

   /**
    *
    */
   public void data( ByteBuffer image )
   {
      _data              = new int[ _width ][ _height ];
      ShortBuffer data   = image.asShortBuffer();
      short       d      = 0;
      int         offset = 0;

      for( int y=0; y<_height; y++ )
      {
         for( int x=0; x<_width; x++ )
         {
            offset          = ( y * _width + x );
            d               = data.get( offset );
            _data[ x ][ y ] = getIntensity( d );
         }
      }

      _depthDequeue.add( _data );

      if( _outputImage )
      {
         for( int y=0; y<_height; y++ )
         {
            for( int x=0; x<_width; x++ )
            {
               _depthImage.setRGB( x, y, _data[ x ][ y ] );
            }
         }
         try
         {
            ImageIO.write( _depthImage, "jpg", new File( "/home/brandon/dist/test-images" + File.separator + "kinect.depth.jpg" ) );
         }
         catch( IOException e )
         {
            System.out.println( "Unable to write depth image data: " + e );
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
            g = 255-lb;
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
         _depthImage = new BufferedImage( _width, _height, BufferedImage.TYPE_BYTE_GRAY );
      }
      else
      {
         _depthImage = null;
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

}
