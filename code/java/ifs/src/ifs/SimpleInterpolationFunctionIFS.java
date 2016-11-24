package ifs;

import java.util.*;
import common.*;
import math.*;
import grafix.*;

/**
 *
 */
public class SimpleInterpolationFunctionIFS
{
   // These are all of the same length, which is the number of data points
   private double[]            _initialX; // user supplied
   private double[]            _initialY; // user supplied
//    private double[]            _initialZ; // user supplied
   private double[]            _a;
   private double[]            _c;
   private double[]            _d;        // user generated
   private double[]            _e;
   private double[]            _f;
//    private double[]            _g;
//    private double[]            _h;        // user generated
//    private double[]            _k;
//    private double[]            _l;        // user generated
//    private double[]            _m;        // user generated

   private int                 _numberOfIterations;
   private int                 _width;
   private int                 _height;
   private int                 _xSuperSampleSize;
   private int                 _ySuperSampleSize;
   private double              _maxDensity = Double.MIN_VALUE;
   private double              _gamma;
   private double              _sma = 0.1;
   private double              _xMin = Double.MAX_VALUE;
   private double              _xMax = Double.MIN_VALUE;
   private double              _yMin = Double.MAX_VALUE;
   private double              _yMax = Double.MIN_VALUE;
   private String              _fileName;
   private ArrayList< Double > _xValues;
   private ArrayList< Double > _yValues;
//    private ArrayList< Double > _zValues;
   private DataHistogram       _histogram;

   /**
    *
    */
   public SimpleInterpolationFunctionIFS()
   {
   }

   /**
    *
    */
   public SimpleInterpolationFunctionIFS( String name, int w, int h, int num )
   {
      _fileName           = name;
      _width              = w;
      _height             = h;
      _numberOfIterations = num;
      _xValues = new ArrayList< Double >( 1000000 );
      _yValues = new ArrayList< Double >( 1000000 );
//       _zValues = new ArrayList< Double >( 1000000 );
      _gamma = 1 / 2.2;
   }

   /**
    *
    */
   public void setInitialX( double[] x )
   {
      _initialX = x;
   }

   /**
    *
    */
   public void setInitialY( double[] y )
   {
      _initialY = y;
   }

   /**
    *
   public void setInitialZ( double[] z )
   {
      _initialZ = z;
   }
    */

   /**
    *
    */
   public void setParamD( double[] d )
   {
      _d = d;
   }

   /**
    *
   public void setParamH( double[] h )
   {
      _h = h;
   }
    */

   /**
    *
   public void setParamL( double[] l )
   {
      _l = l;
   }
    */

   /**
    *
   public void setParamM( double[] m )
   {
      _m = m;
   }
    */

   /**
    *
    */
   public void setSuperSampling( int x, int y )
   {
      _xSuperSampleSize = x;
      _ySuperSampleSize = y;
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
      initialize();
      iterateSystem();
      Interval interval = new Interval();
      _xValues = interval.mapData( _xValues );
//       _xMin = interval.getMinValue();
//       _xMax = interval.getMaxValue();
      interval = new Interval();
      _yValues = interval.mapData( _yValues );
//       _yMin = interval.getMinValue();
//       _yMax = interval.getMaxValue();
      createDataHistogram();
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
    * The idea here is to return the function values for the FIF: f(x)
    * Since the user will supply the set of interpolation data, the domain
    * is understood to be the encompassing interval spaced into equal steps
    * returnvalue.size() in number.
    * 
    */
   public ArrayList< Double > createFractalData()
   {
      initialize();
      // after this call, the array lists _xValues, _yValues, and _zValues
      // will have the iterated orbits of the unscaled system.
      iterateSystem();
      Interval interval = new Interval();
      _xValues = interval.mapData( _xValues );
      ArrayList< Double > values = new ArrayList< Double >( _width );
      ArrayList[] data = new ArrayList[ _width ];
      double x = 0;
      double y = 0;
      double v = 0;
      int  pos = 0;

      for( int i=0; i<_width; i++ )
      {
         data[ i ] = new ArrayList();
         values.add( Double.MIN_VALUE );
      }

//       for( int i=0; i<_width; i++ )
//       {
//          values.add( Double.MIN_VALUE );
//       }

      for( int i=0; i<_xValues.size(); i++ )
      {
         x = _xValues.get( i );
         y = _yValues.get( i );
         pos = (int)( x * values.size() );
         if( pos >= _width )
         {
            pos = _width - 1;
         }
         else if( pos < 0 )
         {
            pos = 0;
         }

         data[ pos ].add( y );

//          v = values.get( pos );
//          if( v != Double.MIN_VALUE )
//          {
//             y = v + _sma * ( y - v );
//          }
//          values.set( pos, y );
      }

      for( int i=0; i<_width; i++ )
      {
         StatUtilities s = new StatUtilities( data[ i ] );
         values.set( i, s.getMedian() );
      }

      return values;
   }

   /**
    *
    */
   protected void iterateSystem()
   {
      double x  = _initialX[ 0 ];
      double y  = _initialY[ 0 ];
//       double z  = _initialZ[ 0 ];
      double nx = 0;
      double ny = 0;
      double nz = 0;
      int    kk = 0;

      for( int i=0; i<_numberOfIterations; i++ )
      {
         kk = ( (int) ( (new MathUtilities()).random() * ( _initialX.length - 1 ) - 0.00001 ) ) + 1;
         nx = _a[ kk ] * x + _e[ kk ];
         ny = _c[ kk ] * x + _d[ kk ] * y + _f[ kk ];
//          nz = _k[ kk ] * x + _l[ kk ] * y + _m[ kk ] * z + _g[ kk ];
         x = nx;
         y = ny;
//          z = nz;
         _xValues.add( x );
         _yValues.add( y );
//          _zValues.add( z );
         if( y > _yMax )
         {
            _yMax = y;
         }
      }
//       System.out.println( "max Y: " + _yMax );
   }

   /**
    *
    */
   protected void createDataHistogram()
   {
      _histogram = new DataHistogram( _width, _height );
      double scaleX = (_width - 1);
      double scaleY = (_height - 1);

      for( int i=0; i<_numberOfIterations; i++ )
      {
         int x = (int) (_xValues.get( i ) * scaleX);
         int y = (int) (_yValues.get( i ) * scaleY);
         DataPoint point = new DataPoint();
         _histogram.addDataPoint( x, y, point );
         if( _histogram.getCount( x, y ) > _maxDensity  )
         {
            _maxDensity = _histogram.getCount( x, y );
         }
      }
//       System.out.println( "max density: " + _maxDensity );
   }

   /**
    *
    */
   public HLSColorPoint[][] getImageDataArray()
   {
      HLSColorPoint[][] imageData = new HLSColorPoint[ _height ][ _width ];
      DataPoint[][] hist = _histogram.getHistogram();
      double maxDensity = Double.MIN_VALUE;

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
               double density = payload[ 2 ] / _maxDensity;
//                imageData[ i ][ j ] = new HLSColorPoint( 360, 1, density );
               imageData[ i ][ j ] = new HLSColorPoint( 1, 0.3, density );
            }
            else
            {
               imageData[ i ][ j ] = new HLSColorPoint( 0, 0, 0 );
            }
         }
      }
//       Y, then X:
//       imageData[ (int)(imageData[0].length * ((double)50 / (double)_yMax)) ][ (int)(imageData.length * 0.3) ] = new HLSColorPoint( 360, 1, 1 );
//       imageData[ (int)(imageData[0].length * ((double)40 / (double)_yMax)) ][ (int)(imageData.length * 0.6) ] = new HLSColorPoint( 360, 1, 1 );

      return imageData;
   }

   /**
    *
    */
   private void initialize()
   {
      int N = _initialX.length - 1;
      double b = _initialX[ N ] - _initialX[ 0 ];
      double p = _initialX[ N ] * _initialY[ 0 ] - _initialX[ 0 ] * _initialY[ N ];
      double q = _initialY[ N ] - _initialY[ 0 ];
//       double r = 0;
//       double s = 0;
      _a = new double[ (N + 1) ];
      _c = new double[ (N + 1) ];
      _e = new double[ (N + 1) ];
//       _k = new double[ (N + 1) ];
      _f = new double[ (N + 1) ];
//       _g = new double[ (N + 1) ];
      for( int i=1; i<(N + 1); i++ )
      {
//          p = _initialY[ i - 1 ] - _d[ i ] * _initialY[ 0 ] - _h[ i ] * _initialZ[ 0 ];
//          q = _initialZ[ i - 1 ] - _l[ i ] * _initialY[ 0 ] - _m[ i ] * _initialZ[ 0 ];
//          r = _initialY[ i ] - _d[ i ] * _initialY[ N ] - _h[ i ] * _initialZ[ N ];
//          s = _initialZ[ i ] - _l[ i ] * _initialY[ N ] - _m[ i ] * _initialZ[ N ];

         _a[ i ] = ( _initialX[ i ] - _initialX[ i - 1 ] ) / b;
         _c[ i ] = ( _initialY[ i ] - _initialY[ i - 1 ] - _d[ i ] * q ) / b;
         _e[ i ] = ( _initialX[ N ] * _initialX[ i - 1 ] - _initialX[ 0 ] * _initialX[ i ] ) / b;
//          _k[ i ] = ( s - q ) / b;
         _f[ i ] = ( _initialX[ N ] * _initialY[ i - 1 ] - _initialX[ 0 ] * _initialY[ i ] - _d[ i ] * p  ) / b;
//          _g[ i ] = q - _k[ i ] * _initialX[ 0 ];
      }
   }

   /**
    *
    */
   public String toString()
   {
      return "";
   }

   /**
    *
    */
   public static void main( String[] args )
   {
//       int size = 41;
//       int size = 163;
      int size = 600;
      if( args.length > 0 )
      {
         size = Integer.parseInt( args[ 0 ] );
      }
      SimpleInterpolationFunctionIFS ifs = 
         new SimpleInterpolationFunctionIFS( "sfif.tga", size, size, 1000000 );
      common.FileReader reader = new FileReader( "variables.dat", "," );
      String[] words = reader.getArrayOfWords();
      int numberOfVariables = Integer.parseInt( words[ 0 ] );
      int numberOfDataPoints = (numberOfVariables == 1) ? (words.length - 1) : (words.length) / numberOfVariables;
      int count = 1;

      double[][] variables = new double[ numberOfVariables ][ numberOfDataPoints ];
      double[] temp = null;
      for( int i=0; i<numberOfVariables; i++ )
      {
         temp = new double[ numberOfDataPoints ];
         for( int j=0; j<numberOfDataPoints; j++ )
         {
            try
            {
               temp[ j ] = Double.parseDouble( words[ count++ ] );
            }
            catch( NumberFormatException e )
            {
               System.err.println( "Caught exception parsing data: " + e );
            }
         }
         variables[ i ] = (new MathUtilities()).normalize( temp, 1 );
      }
      System.out.println( "number of variables: " + numberOfVariables );
      System.out.println( "number of data points: " + numberOfDataPoints );

      reader = new FileReader( "target.dat", "," );
      words = reader.getArrayOfWords();
      double[] targetStream = new double[ words.length ];
      for( int i=0; i<words.length; i++ )
      {
         try
         {
            targetStream[ i ] = ( Double.parseDouble( words[ i ] ) );
         }
         catch( NumberFormatException e )
         {
            System.err.println( "Caught exception parsing data: " + e );
         }
      }

      reader = new FileReader( "genome.dat", " " );
      words = reader.getArrayOfWords();
      double[] genome = new double[ words.length ];
      for( int i=0; i<words.length; i++ )
      {
         try
         {
            genome[ i ] = ( Double.parseDouble( words[ i ] ) );
         }
         catch( NumberFormatException e )
         {
            System.err.println( "Caught exception parsing data: " + e );
         }
      }

      double[] x = variables[ 0 ];
      double[] y = targetStream;
//       double[] z = variables[ 0 ];
      double[] d = new double[ numberOfDataPoints ];
//       double[] h = new double[ numberOfDataPoints ];
//       double[] l = new double[ numberOfDataPoints ];
//       double[] m = new double[ numberOfDataPoints ];
      int pos = 0;
      for( int i=0; i<numberOfDataPoints; i++ )
      {
         d[ i ] = genome[ pos++ ];
      }
//       for( int i=0; i<numberOfDataPoints; i++ )
//       {
//          h[ i ] = genome[ pos++ ];
//       }
//       for( int i=0; i<numberOfDataPoints; i++ )
//       {
//          l[ i ] = genome[ pos++ ];
//       }
//       for( int i=0; i<numberOfDataPoints; i++ )
//       {
//          m[ i ] = genome[ pos++ ];
//       }

      ifs.setInitialX( x );
      ifs.setInitialY( y );
//       ifs.setInitialZ( z );
      ifs.setParamD( d );
//       ifs.setParamH( h );
//       ifs.setParamL( l );
//       ifs.setParamM( m );
      ifs.createFractal();
      ArrayList< Double > v = ifs.createFractalData();
      System.out.println( "values are:" );
      for( Double obj : v )
      {
         System.out.println( obj );
      }

   }

}
