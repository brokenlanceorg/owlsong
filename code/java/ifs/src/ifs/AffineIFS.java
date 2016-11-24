package ifs;

import java.util.*;
import common.*;
import math.*;
import grafix.*;

/**
 *
 */
public class AffineIFS
{

   private AffineSystem        _affineSystem;
   private int                 _numberOfSystems;
   private int                 _numberOfIterations;
   private int                 _width;
   private int                 _height;
   private int                 _xSuperSampleSize;
   private int                 _ySuperSampleSize;
   private double              _maxDensity = Double.MIN_VALUE;
   private double              _gamma;
   private double              _x;
   private double              _y;
   private double              _xMin;
   private double              _xMax;
   private double              _yMin;
   private double              _yMax;
   private String              _fileName;
   private ArrayList< Double > _xValues;
   private ArrayList< Double > _yValues;
   private ArrayList< Double > _fValues;
   private ArrayList< Double > _dValues;
   private DataHistogram       _histogram;

   /**
    *
    */
   public AffineIFS()
   {
   }

   /**
    *
    */
   public AffineIFS( double[] params, String name, int w, int h, int num )
   {
      _affineSystem       = new AffineSystem( params );
      _fileName           = name;
      _width              = w;
      _height             = h;
      _numberOfIterations = num;
      _xValues = new ArrayList< Double >( 1000000 );
      _yValues = new ArrayList< Double >( 1000000 );
      _fValues = new ArrayList< Double >( 1000000 );
      _dValues = new ArrayList< Double >( 1000000 );
      _histogram = new DataHistogram( w, h );
      _gamma = 1 / 2.2;
   }

   /**
    *
    */
   public AffineIFS( int numSystems, String name, int w, int h, int num )
   {
      _numberOfSystems    = numSystems;
      _affineSystem       = new AffineSystem( _numberOfSystems );
      _fileName           = name;
      _width              = w;
      _height             = h;
      _numberOfIterations = num;
      _xValues = new ArrayList< Double >( 1000000 );
      _yValues = new ArrayList< Double >( 1000000 );
      _fValues = new ArrayList< Double >( 1000000 );
      _dValues = new ArrayList< Double >( 1000000 );
      _histogram = new DataHistogram( w, h );
      _gamma = 1 / 2.2;
   }

   /**
    *
    */
   public void setSuperSampling( int x, int y )
   {
      _xSuperSampleSize = x;
      _ySuperSampleSize = y;
   }

   /**
    *
    */
   public boolean isContractive()
   {
      return _affineSystem.isContractive();
   }

   /**
    *
    */
   public boolean isFullContractive()
   {
      return _affineSystem.isFullContractive();
   }

   /**
    * This is the main driver method for the IFS. Here, we perform the following:
    * 1) iterate the system not keeping the first 20 points
    * 2) determine what the max mappings were in the two dimensions
    * 3) based on the max and the size of the image, plot the points in the array
    * 4) write the array to file
    */
   public void createFractal()
   {
      iterateSystem();
      Interval interval = new Interval();
      _xValues = interval.mapData( _xValues );
      _xMin = interval.getMinValue();
      _xMax = interval.getMaxValue();
      interval = new Interval();
      _yValues = interval.mapData( _yValues );
      _yMin = interval.getMinValue();
      _yMax = interval.getMaxValue();
      createDataHistogram();
      if( _numberOfIterations > 1000000 )
      {
         iterateDataHistogram();
      }
      HLSColorPoint[][] imageData = getImageDataArray();
      TargaFile targa = null;
      if( _xSuperSampleSize > 0 )
      {
         targa = new SuperSampledTargaFile( _fileName, _xSuperSampleSize, _ySuperSampleSize );
      }
      else
      {
         targa = new TargaFile( _fileName );
      }
      targa.writeTargaFile( imageData );
   }

   /**
    * This is the main driver method for the IFS. Here, we perform the following:
    * 1) iterate the system not keeping the first 20 points
    * 2) determine what the max mappings were in the two dimensions
    * 3) based on the max and the size of the image, plot the points in the array
    * 4) write the array to file
    */
   public HLSColorPoint[][] createFractalData()
   {
      iterateSystem();
      Interval interval = new Interval();
      _xValues = interval.mapData( _xValues );
      _xMin = interval.getMinValue();
      _xMax = interval.getMaxValue();
      interval = new Interval();
      _yValues = interval.mapData( _yValues );
      _yMin = interval.getMinValue();
      _yMax = interval.getMaxValue();
      createDataHistogram();
      if( _numberOfIterations > 1000000 )
      {
         iterateDataHistogram();
      }
      HLSColorPoint[][] imageData = getImageDataArray();
      return imageData;
   }

   /**
    * This is the main driver method for the IFS. Here, we perform the following:
    * 1) iterate the system not keeping the first 20 points
    * 2) based on the max and the size of the image, plot the points in the array
    */
   public double[][] createRawFractalData()
   {
      iterateSystem();
      return createRawDataHistogram();
   }

   /**
    *
    */
   public void evolveImage( int attempts )
   {
      HLSColorPoint[][] bestImageData = null;
      long leastBlack = Long.MAX_VALUE;

      for( int i=0; i<attempts; i++ )
      {
         _affineSystem = null;
         _xValues = null;
         _yValues = null;
         _fValues = null;
         _dValues = null;
         _histogram = null;
         try { System.gc(); Thread.yield(); Thread.sleep( 50 ); } catch( InterruptedException e ) { }
         _affineSystem = new AffineSystem( _numberOfSystems );
         _xValues = new ArrayList< Double >( 1000000 );
         _yValues = new ArrayList< Double >( 1000000 );
         _fValues = new ArrayList< Double >( 1000000 );
         _dValues = new ArrayList< Double >( 1000000 );
         _histogram = new DataHistogram( _width, _height );
         HLSColorPoint[][] imageData = createFractalData();
         int blackCount = 0;

         for( int r=0; r<imageData.length; r++ )
         {
            for( int c=0; c<imageData[r].length; c++ )
            {
               if( imageData[r][c].getL() == 0 )
               {
                  blackCount++;
               }
            }
         }
         if( blackCount < leastBlack )
         {
            leastBlack = blackCount;
            bestImageData = imageData;
         }
      }
      TargaFile targa = null;
      if( _xSuperSampleSize > 0 )
      {
         targa = new SuperSampledTargaFile( _fileName, _xSuperSampleSize, _ySuperSampleSize );
      }
      else
      {
         targa = new TargaFile( _fileName );
      }
      targa.writeTargaFile( bestImageData );
   }

   /**
    *
    */
   protected void iterateSystem()
   {
      _x = (new MathUtilities()).random();
      _y = (new MathUtilities()).random();
      double[] eval = null;
      double dist = 0;
      long num = (_numberOfIterations > 1000000) ? 1000000 : _numberOfIterations;

      for( int i=0; i<num; i++ )
      {
         eval = _affineSystem.evaluate( _x, _y );
         dist = (new MathUtilities()).getEuclideanDistance2D( _x, _y, eval[0], eval[1] );
         _x = eval[ 0 ];
         _y = eval[ 1 ];
         if( i > 20 )
         {
            _xValues.add( _x );
            _yValues.add( _y );
            _fValues.add( eval[ 2 ] );
            _dValues.add( dist );
         }
      }
   }

   /**
    *
    */
   protected void iterateDataHistogram()
   {
      double scaleX = (_width - 1);
      double scaleY = (_height - 1);
      double[] eval = null;
      double dist = 0;
      double hue = 0;
      int x = 0;
      int y = 0;
      _xValues = null;
      _yValues = null;
      _fValues = null;
      _dValues = null;

      try { System.gc(); Thread.yield(); Thread.sleep( 50 ); } catch( InterruptedException e ) { }

      for( int i=1000000; i<_numberOfIterations; i++ )
      {
         eval = _affineSystem.evaluate( _x, _y );
         dist = (new MathUtilities()).getEuclideanDistance2D( _x, _y, eval[0], eval[1] );
         _x   = eval[ 0 ];
         _y   = eval[ 1 ];
         hue  = eval[ 2 ];
         DataPoint point = new DataPoint( 2 );
         point.addPayload( hue );
         point.addPayload( dist );
         x = (int) ( ((_x - _xMin) / _xMax) * scaleX);
         if( x >= _width )
         {
            x = _width - 1;
            System.out.print( "'" );
         }
         if( x < 0 )
         {
            x = 0;
            System.out.print( "." );
         }
         y = (int) ( ((_y - _yMin) / _yMax) * scaleY);
         if( y >= _height )
         {
            y = _height - 1;
            System.out.print( "-" );
         }
         if( y < 0 )
         {
            y = 0;
            System.out.print( "_" );
         }
         _histogram.addDataPoint( x, y, point );
         if( _histogram.getCount( x, y ) > _maxDensity  )
         {
            _maxDensity = _histogram.getCount( x, y );
         }
      }
   }

   /**
    *
    */
   protected void createDataHistogram()
   {
      double scaleX = (_width - 1);
      double scaleY = (_height - 1);
      double hue    = 0;
      double dist   = 0;
      long   num    = (_numberOfIterations > 1000000) ? 1000000 : _numberOfIterations;

      for( int i=0; i<(num - 21); i++ )
      {
         int x = (int) (_xValues.get( i ) * scaleX);
         int y = (int) (_yValues.get( i ) * scaleY);
         hue  = _fValues.get( i );
         dist = _dValues.get( i );
         DataPoint point = new DataPoint( 2 );
         point.addPayload( hue );
         point.addPayload( dist );
         _histogram.addDataPoint( x, y, point );
         if( _histogram.getCount( x, y ) > _maxDensity  )
         {
            _maxDensity = _histogram.getCount( x, y );
         }
      }
   }

   /**
    *
    */
   protected double[][] createRawDataHistogram()
   {
      double     scaleX    = (_width - 1);
      double     scaleY    = (_height - 1);
      long       num       = (_numberOfIterations > 1000000) ? 1000000 : _numberOfIterations;
      double     max       = Double.MIN_VALUE;
      double[][] imageData = new double[ _height ][ _width ];

      for( int i=0; i<(num - 21); i++ )
      {
         int x = (int) (_xValues.get( i ) * scaleX);
         int y = (int) (_yValues.get( i ) * scaleY);
         if( x < imageData.length )
         {
            if( y < imageData[ 0 ].length )
            {
               imageData[ x ][ y ] += 1.0;
//                System.out.println( "x: " + x + " y: " + y );
            }
//             else
//             {
//                System.out.println( "-" );
//             }
         }
//          else
//          {
//             System.out.println( "." );
//          }
      }
      for( int j=0; j<imageData.length; j++ )
      {
         for( int k=0; k<imageData[ 0 ].length; k++ )
         {
            if( imageData[ j ][ k ] > max )
            {
               max = imageData[ j ][ k ];
            }
         }
      }
      for( int j=0; j<imageData.length; j++ )
      {
         for( int k=0; k<imageData[ 0 ].length; k++ )
         {
            imageData[ j ][ k ] /= max;
         }
      }

      return imageData;
   }

   /**
    *
    */
   public HLSColorPoint[][] getImageDataArray()
   {
      HLSColorPoint[][] imageData = new HLSColorPoint[ _height ][ _width ];
      DataPoint[][] hist = _histogram.getHistogram();
      double maxHue = Double.MIN_VALUE;
      double maxDist = Double.MIN_VALUE;
      double maxDensity = Double.MIN_VALUE;

      for( int i=0; i<_height; i++ )
      {
         for( int j=0; j<_width; j++ )
         {
            imageData[ i ][ j ] = new HLSColorPoint( 0, 0, 0 );
            DataPoint point = hist[ i ][ j ];
            // 0 is hue
            // 1 is distance
            // 2 is count
            double[] payload = point.getPayload();

            if( payload[ 2 ] > 0 )
            {
               payload[ 0 ] /= payload[ 2 ];
               payload[ 1 ] /= payload[ 2 ];
               if( payload[ 0 ] > maxHue )
               {
                  maxHue = payload[ 0 ];
               }
               if( payload[ 1 ] > maxDist )
               {
                  maxDist = payload[ 1 ];
               }
               if( payload[ 2 ] > maxDensity )
               {
                  maxDensity = payload[ 2 ];
               }
            }
         }
      }

      for( int i=0; i<_height; i++ )
      {
         for( int j=0; j<_width; j++ )
         {
            DataPoint point = hist[ i ][ j ];
            // 0 is hue
            // 1 is distance
            // 2 is count
            double[] payload = point.getPayload();

            if( payload[ 2 ] > 0 )
            {
               double hue = payload[ 0 ] / maxHue;
               double distance = payload[ 1 ] / maxDist;
               double density = payload[ 2 ] / maxDensity;

               hue *= 360;
               imageData[ i ][ j ] = new HLSColorPoint( hue, Math.pow( density, _gamma ), distance );
            }
         }
      }

      return imageData;
   }

   /**
    *
    */
   public String toString()
   {
      return _affineSystem.toString();
   }

   /**
    *
    */
   public static void main( String[] args )
   {
//       AffineIFS ifs = new AffineIFS( 2, "evolved.tga", 2400, 2400, 30000000 );
//       ifs.setSuperSampling( 3, 3 );
//       ifs.evolveImage( 10000 );

      if( args.length > 0 )
      {
         String fileName = "random." + args[0] + ".tga";
         AffineIFS ifs = new AffineIFS( 3, fileName, 2400, 2400, 30000000 );
         System.out.println( ifs );
         ifs.setSuperSampling( 3, 3 );
         ifs.createFractal();
      }
      else
      {
         FileReader paramFile = new FileReader( "IFSParams.dat" );
         Vector< String > words = (Vector< String >) paramFile.getVectorOfWords();
         double[] params = new double[ words.size() ];
         System.out.println( "Number of params is: " + params.length );
         int pos = 0;

         for( String word : words )
         {
            params[ pos++ ] = Double.valueOf( word );
         }

//          AffineIFS ifs = new AffineIFS( params, "param.tga", 2400, 2400, 90000000 );
         AffineIFS ifs = new AffineIFS( params, "param.tga", 400, 400, 900000 );
         System.out.println( ifs );
         ifs.setSuperSampling( 3, 3 );
         ifs.createFractal();
         double[][] data = ifs.createRawFractalData();
         for( int i=0; i<data.length; i++ )
         {
            for( int j=0; j<data[ i ].length; j++ )
            {
               System.out.print( data[ i ][ j ] + " " );
            }
            System.out.println( "" );
         }
         GrafixUtilities   gu    = new GrafixUtilities();
         RGBColorPoint[][] image = gu.getRGBData2( "Apiatan.jpg" );
         for( int i=0; i<image.length; i++ )
         {
            for( int j=0; j<image[ i ].length; j++ )
            {
               System.out.print( image[ i ][ j ].getRed() + " " );
            }
            System.out.println( "" );
         }
      }
   }

}
