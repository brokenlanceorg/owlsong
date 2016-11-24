package grafix;

import java.util.*;
import java.io.*;
import math.*;

/**
 * This class will take as an argument an array of data values that it will
 * output into a Targa format file.
 * The data values will be pixel data points, that will be downsampled by 
 * the specified values in the constructor.
 */
public class SuperSampledTargaFile extends TargaFile
{

   private int    _rDownSize;
   private int    _cDownSize;

   /**
    *
    */
   public SuperSampledTargaFile()
   {
      super();
   }

   /**
    *
    */
   public SuperSampledTargaFile( String name )
   {
      super( name );
   }

   /**
    *
    */
   public SuperSampledTargaFile( int r, int c )
   {
      super();
      _rDownSize = r;
      _cDownSize = c;
   }

   /**
    *
    */
   public SuperSampledTargaFile( String name, int r, int c )
   {
      super( name );
      _rDownSize = r;
      _cDownSize = c;
   }

   /**
    *
    */
   public void writeTargaFile( RGBColorPoint[][] image )
   {
      writeImageData( image );
   }

   /**
    *
    */
   public void writeTargaFile( HLSColorPoint[][] image )
   {
      long start = System.currentTimeMillis();
      RGBColorPoint[][] data = new RGBColorPoint[ image.length ][ image[ 0 ].length ];

      for( int r=0; r<image.length; r++ )
      {
         for( int c=0; c<image[r].length; c++ )
         {
            data[ r ][ c ] = (RGBColorPoint) image[ r ][ c ].getRGBColorPoint();
         }
      }
      System.out.println( "Time to convert from HLS: " + (System.currentTimeMillis() - start) );

      writeImageData( data );
   }

   /**
    *
    */
   public void writeTargaFile( CIEColorPoint[][] image )
   {
      long start = System.currentTimeMillis();
      RGBColorPoint[][] data = new RGBColorPoint[ image.length ][ image[ 0 ].length ];

      for( int r=0; r<image.length; r++ )
      {
         for( int c=0; c<image[r].length; c++ )
         {
            data[ r ][ c ] = (RGBColorPoint) image[ r ][ c ].getRGBColorPoint();
         }
      }
      System.out.println( "Time to convert from CIE: " + (System.currentTimeMillis() - start) );

      writeImageData( data );
   }

   /**
    * Here, the basic idea is to step through the image considering each pixel in the 
    * final image as being composed of xDownSize x yDownSize subpixels.
    * The assumption is that the downsampled image size is an integral multiple 
    * of the supersampled image size.
    */
   public void writeImageData( RGBColorPoint[][] image )
   {
      long start = System.currentTimeMillis();
      int rFinalSize = image.length / _rDownSize;
      int cFinalSize = image[0].length / _cDownSize;
      int rFinal = 0;
      int sampleSize = _rDownSize * _cDownSize;

      RGBColorPoint[][] output = new RGBColorPoint[ rFinalSize ][ cFinalSize ];

      for( int r=(_rDownSize-1); r<image.length; r+=_rDownSize,rFinal++ )
      {
         int cFinal = 0;
         for( int c=(_cDownSize-1); c<image[r].length; c+=_cDownSize,cFinal++ )
         {
            double red = 0;
            double green = 0;
            double blue = 0;
            for( int i=(r-_rDownSize+1); i<=r; i++ )
            {
               for( int j=(c-_cDownSize+1); j<=c; j++ )
               {
                  red += image[ i ][ j ].getRed();
                  green += image[ i ][ j ].getGreen();
                  blue += image[ i ][ j ].getBlue();
               }
            }
            red /= (double)sampleSize;
            green /= (double)sampleSize;
            blue /= (double)sampleSize;
            output[ rFinal ][ cFinal ] = new RGBColorPoint( red, green, blue );
         }
      }
      System.out.println( "Time to downsample: " + (System.currentTimeMillis() - start) );

      super.writeTargaFile( output );
   }

   /**
    * Here, the basic idea is to step through the image considering each pixel in the 
    * final image as being composed of xDownSize x yDownSize subpixels.
    * The assumption is that the downsampled image size is an integral multiple 
    * of the supersampled image size.
    */
   public void writeImageData( CIEColorPoint[][] image )
   {
      int rFinalSize = image.length / _rDownSize;
      int cFinalSize = image[0].length / _cDownSize;
      int rFinal = 0;
      int sampleSize = _rDownSize * _cDownSize;

      RGBColorPoint[][] output = new RGBColorPoint[ rFinalSize ][ cFinalSize ];

      for( int r=(_rDownSize-1); r<image.length; r+=_rDownSize,rFinal++ )
      {
         int cFinal = 0;
         for( int c=(_cDownSize-1); c<image[r].length; c+=_cDownSize,cFinal++ )
         {
            double x = 0;
            double y = 0;
            double z = 0;
            for( int i=(r-_rDownSize+1); i<=r; i++ )
            {
               for( int j=(c-_cDownSize+1); j<=c; j++ )
               {
                  x += image[ i ][ j ].getX();
                  y += image[ i ][ j ].getY();
                  z += image[ i ][ j ].getZ();
               }
            }
            x /= (double)sampleSize;
            y /= (double)sampleSize;
            z /= (double)sampleSize;
            CIEColorPoint cie = new CIEColorPoint( x, y, z );
            output[ rFinal ][ cFinal ] = (RGBColorPoint) cie.getRGBColorPoint();
         }
      }

      super.writeTargaFile( output );
   }

   /**
    *
    */
   public static void main( String[] args )
   {
      SuperSampledTargaFile sstfile = new SuperSampledTargaFile( 2, 2 );

      short rowSize = 2400;
      short colSize = 1200;
      RGBColorPoint[][] image = new RGBColorPoint[ rowSize ][ colSize ];
//       CIEColorPoint[][] image = new CIEColorPoint[ rowSize ][ colSize ];
//       HLSColorPoint[][] image = new HLSColorPoint[ rowSize ][ colSize ];

      for( int r=0; r<rowSize; r++ )
      {
         for( int c=0; c<colSize; c++ )
         {
//             image[r][c] = new RGBColorPoint( Math.random(), Math.random(), Math.random() );
            image[r][c] = new RGBColorPoint( (double)r / (double)rowSize, 0, 0 );
//             image[r][c] = new RGBColorPoint( 0, 0, (double)r / (double)rowSize );
//             image[r][c] = new CIEColorPoint( (double)r / (double)rowSize, 0, 0 );
//             image[r][c] = new HLSColorPoint( 360 * ((double)r / (double)rowSize), 1, 1 );
         }
      }

      sstfile.writeTargaFile( image );

//       TargaFile tfile = new TargaFile( "orig.tga" );
//       tfile.writeTargaFile( image );

//       int d = MathUtilities.getHilbertDistance( 10, 15, 16 );
//       System.out.println( "distance is: " + d );
//       int[] a = MathUtilities.getHilbertPair( d, 16 );
//       System.out.println( "pair is: " + a[ 0 ] + ", " + a[ 1 ] );

//       TargaFile inputFile = new TargaFile( "/home/brandon/ultra_5.tga" );
//       TargaFile inputFile = new TargaFile( "/home/brandon/ultra_6.tga" );
//       TargaFile inputFile = new TargaFile( "/home/brandon/dist/targa.file.tga" );
      TargaFile inputFile = new TargaFile( "/home/brandon/raw.tga" );
      RGBColorPoint[][] imageArray = inputFile.readTargaFile();

//       int l = imageArray.length * imageArray[ 0 ].length;
//       int[] p = null;
//       for( int i=0; i<l; i++ )
//       {
//          p = MathUtilities.getHilbertPair( i, 256 );
//          HLSColorPoint hls = (HLSColorPoint) imageArray[ p[ 0 ] ][ p[ 1 ] ].getHLSColorPoint();
//          System.out.println( hls.getH() + ", " + hls.getL() );
//       }
      for( int i=0; i<imageArray.length; i++ )
      {
         for( int j=0; j<imageArray[i].length; j++ )
         {
            HLSColorPoint hls = (HLSColorPoint) imageArray[ i ][ j ].getHLSColorPoint();
//             System.out.println( "h is: " + hls.getH() );
//             if( hls.getL() < 0.08 )
//             {
//                imageArray[ i ][ j ] = new RGBColorPoint( 0, 0, 0 );
//             }
            if( hls.getH() < 200 || hls.getH() > 250 )
            {
               imageArray[ i ][ j ] = new RGBColorPoint( 0, 0, 0 );
            }
         }
      }

      TargaFile tfile = new TargaFile( "orig.tga" );
      tfile.writeTargaFile( imageArray );

//       for( int i=0; i<imageArray.length; i++ )
//       {
//          System.out.println( "row: " + i );
//          for( int j=0; j<imageArray[ i ].length; j++ )
//          {
//             System.out.println( "luminosity: " + ((HLSColorPoint)imageArray[ i ][ j ].getHLSColorPoint()).getL() );
//          }
//       }
   }

}
