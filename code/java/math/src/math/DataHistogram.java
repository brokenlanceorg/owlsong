package math;

import java.util.*;

/**
 *
 */
public class DataHistogram
{

   private DataPoint[][] _histogram;
   private int           _width;
   private int           _height;

   /**
    *
    */
   public DataHistogram()
   {
   }

   /**
    *
    */
   public DataHistogram( int width, int height )
   {
      _width = width;
      _height = height;
      _histogram = new DataPoint[ _height ][ _width ];

      for( int i=0; i<_height; i++ )
      {
         for( int j=0; j<_width; j++ )
         {
            _histogram[ i ][ j ] = new DataPoint();
         }
      }
   }

   /**
    *
    */
   public void addDataPoint( int w, int h, DataPoint data )
   {
      double[] payload = _histogram[ h ][ w ].getPayload();
      if( data.getPayload().length > 0 )
      {
         payload[ 0 ] += data.getPayload()[ 0 ];
      }
      if( data.getPayload().length > 1 )
      {
         payload[ 1 ] += data.getPayload()[ 1 ];
      }
      payload[ 2 ] += 1.0;
      _histogram[ h ][ w ].setPayload( payload );
   }

   /**
    *
    */
   public double getCount( int w, int h )
   {
      return _histogram[ h ][ w ].getPayload()[ 2 ];
   }

   /**
    *
    */
   public DataPoint[][] getHistogram()
   {
      return _histogram;
   }

}
