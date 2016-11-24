package openkinect;

import common.*;
import math.*;
import grafix.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.*;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.*;
import javax.imageio.ImageIO;

import org.openkinect.Acceleration;
import org.openkinect.Context;
import org.openkinect.Device;
import org.openkinect.Image;
import org.openkinect.LEDStatus;

/**
 *
 */
public class KinectManager implements Runnable
{

   private boolean                        _canProcess    = true;
   private int                            _width         = 640;
   private int                            _height        = 480;
   private Device                         _device        = null;
   private Context                        _context       = null;
   private String                         _accelerometer = "off";
   private DataDeque< int[][] >           _depthDequeue  = null;
   private DataDeque< RGBColorPoint[][] > _colorDequeue  = null;

   /**
    *
    */
   public KinectManager()
   {
      initialize();
   }

   /**
    *
    */
   public KinectManager( int w, int h, String a, DataDeque d, DataDeque c )
   {
      setWidth( w );
      setHeight( h );
      setAccelerometer( a );
      setDepthDequeue( d );
      setColorDequeue( c );
      initialize();
   }

   /**
    *
    */
   private void initialize()
   {
      _context = Context.getContext();

      if( _context.devices() < 1 )
      {
         System.out.println( "No Kinect devices found." );
      }
      else
      {
         _device = _context.getDevice( 0 );
      }

      /* 
         Possible values for the LEDStatus are:
            LEDStatus.LED_RED
            LEDStatus.LED_GREEN
            LEDStatus.LED_YELLOW
            LEDStatus.LED_BLINK_GREEN
            LEDStatus.LED_BLINK_YELLOW
            LEDStatus.LED_BLINK_RED_YELLOW
            LEDStatus.LED_BLINK_GREEN
      */
      _device.light( LEDStatus.LED_GREEN );
      _device.tilt( 5 ); // negative values point down, positive point upwards
      _device.depth( new DepthImageHandler( _width, _height, _depthDequeue ) );
      _device.color( new ColorImageHandler( _width, _height, _colorDequeue ) );

      if( "on".equalsIgnoreCase( _accelerometer ) )
      {
         _device.acceleration( new Acceleration()
            {
               public void direction( float x, float y, float z )
               {
                  System.out.printf( "Acceleration: %f %f %f\n", x, y, z );
               }
            });
      }
      else
      {
         _device.acceleration( null );
      }
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void run()
   {
      processEvents();
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void processEvents()
   {
      while( _canProcess )
      {
         _context.processEvents();
      }

      _device.depth( null );
      _device.color( null );
      _device.tilt( -10 );
      _device.light( LEDStatus.LED_RED );
      _device.dispose();
      System.out.println( "disposed device..." );
   }

   /**
    *
    * @param boolean
    */
   public void setCanProcess( boolean canProcess )
   {
      _canProcess = canProcess;
   }

   /**
    *
    * @return boolean
    */
   public boolean getCanProcess()
   {
      return _canProcess;
   }

   /**
    *
    * @param int
    */
   public void setWidth( int width )
   {
      _width = width;
   }

   /**
    *
    * @return int
    */
   public int getWidth()
   {
      return _width;
   }

   /**
    *
    * @param int
    */
   public void setHeight( int height )
   {
      _height = height;
   }

   /**
    *
    * @return int
    */
   public int getHeight()
   {
      return _height;
   }

   /**
    *
    * @param String
    */
   public void setAccelerometer( String accelerometer )
   {
      _accelerometer = accelerometer;
   }

   /**
    *
    * @return String
    */
   public String getAccelerometer()
   {
      return _accelerometer;
   }

   /**
    *
    */
   public void setDepthDequeue( DataDeque< int[][] > depthDequeue )
   {
      _depthDequeue = depthDequeue;
   }

   /**
    *
    */
   public DataDeque< int[][] > getDepthDequeue()
   {
      return _depthDequeue;
   }

   /**
    *
    * @param DataDeque< RGBColorPoint[][] > _colorDequeue
    */
   public void setColorDequeue( DataDeque< RGBColorPoint[][] > colorDequeue )
   {
      _colorDequeue = colorDequeue;
   }

   /**
    *
    * @return DataDeque< RGBColorPoint[][] > _colorDequeue
    */
   public DataDeque< RGBColorPoint[][] > getColorDequeue()
   {
      return _colorDequeue;
   }

}
