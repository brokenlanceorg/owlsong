package grafix;

import java.util.*;
import java.io.*;
import math.*;
import math.cluster.*;

import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * This class will take as an argument an array of data values that it will
 * output into a Targa format file.
 * The data values will be pixel data points.
 */
public class TargaFile
{

   private   String               _fileName;
   protected BufferedOutputStream _fileHandle;
   protected BufferedInputStream  _inputStream;
   private   int                  _colorDepth = 24;
   private   int                  _type       = 2;
   private   int                  _descriptor = 32;

   /**
    *
    */
   public TargaFile()
   {
      _fileName = "targa.file.tga";
   }

   /**
    *
    */
   public TargaFile( String name )
   {
      _fileName = name;
   }

   /**
    *
    */
   private void openFile()
   {
      try
      {
         _fileHandle = new BufferedOutputStream( new FileOutputStream( new File( _fileName ) ) );
      }
      catch( FileNotFoundException e )
      {
         System.err.println( "File Not Found Exception: " + e );
      }
   }

   /**
    *
    * The header should look something like this:
    * 00000000  00 00 02 00 00 00 00 00  00 00 00 00 1c 01 14 00  |................|
    * 00000010  18 00                                             |................|
    *
    */
   private void writeTargaHeader( int width, int height )
   {
      openFile();

      try
      {
         int ibyte = 0;
         // ID Length field (1 byte)
         _fileHandle.write( ibyte );
         // Color map type (1 byte) -- 0 no color map, 1 includes color map
         _fileHandle.write( ibyte );
         // Image type (1 byte) -- 2 = uncompressed, true-color
         ibyte = _type;
         _fileHandle.write( ibyte );
         // Color Map specification (5 bytes) -- all null
         ibyte = 0;
         _fileHandle.write( ibyte );
         _fileHandle.write( ibyte );
         _fileHandle.write( ibyte );
         _fileHandle.write( ibyte );
         _fileHandle.write( ibyte );
         // Image specification (10 bytes) --
         // X origin (2 bytes)
         _fileHandle.write( ibyte );
         _fileHandle.write( ibyte );
         // Y origin (2 bytes)
         _fileHandle.write( ibyte );
         _fileHandle.write( ibyte );
         // Image width (2 bytes)
         ibyte = width;
         _fileHandle.write( ibyte );
         ibyte = Integer.rotateRight( width, 8 );
         _fileHandle.write( ibyte );
         // Image height (2 bytes)
         ibyte = height;
         _fileHandle.write( ibyte );
         ibyte = Integer.rotateRight( height, 8 );
         _fileHandle.write( ibyte );
         // Pixel depth
         ibyte = _colorDepth;
         _fileHandle.write( ibyte );
         // Image descriptor -- bits 0-3 number of alpha channel bits, bits 5 & 4 is the pixel ordering
         // screen destination of first pixel         bit 5   bit 4
         // bottom left                                  0      0
         // bottom right                                 0      1
         // top left                                     1      0
         // top right                                    1      1
         // bits 7 & 6 must be zero
         // 0010:0000 -- top left
         // 0001:0000 -- bottom right
//          ibyte = 32; 0x20
         ibyte = _descriptor;
         _fileHandle.write( ibyte );
      }
      catch( IOException e )
      {
         System.err.println( "IOException during write of Targa Header file: " + e );
      }

   }

   /**
    *
    */
   private void writeImageData( RGBColorPoint[][] image )
   {
      try
      {
         writeTargaHeader( image.length, image[0].length );
         System.out.println( "width:  " + image.length );
         System.out.println( "height: " + image[ 0 ].length );
         if( _colorDepth == 8 )
         {
            for( int r=0; r<image.length; r++ )
            {
               for( int c=0; c<image[r].length; c++ )
               {
                  _fileHandle.write( image[ r ][ c ].getRedComponent() );
//                   System.out.println( "red is: " + image[ r ][ c ].getRedComponent() );
               }
            }
         }
         else
         {
            for( int r=0; r<image.length; r++ )
            {
               for( int c=0; c<image[r].length; c++ )
               {
                  _fileHandle.write( image[ r ][ c ].getRedComponent() );
                  _fileHandle.write( image[ r ][ c ].getGreenComponent() );
                  _fileHandle.write( image[ r ][ c ].getBlueComponent() );
               }
            }
         }
      }
      catch( IOException e )
      {
         System.err.println( "Unable to write image data: " + e );
      }
      finally
      {
         try
         {
            _fileHandle.close();
         }
         catch( IOException e )
         {
            System.err.println( "Caught IOException closing file: " + e );
         }
      }
   }

   /**
    *
    */
   public void writeTargaFile( RGBColorPoint[][] image )
   {
      long start = System.currentTimeMillis();
      writeImageData( image );
      System.out.println( "Time to write data to disk: " + (System.currentTimeMillis() - start) );
   }

   /**
    *
    */
   public void writeTargaFile( CIEColorPoint[][] image )
   {
      RGBColorPoint[][] data = new RGBColorPoint[ image.length ][ image[ 0 ].length ];

      for( int r=0; r<image.length; r++ )
      {
         for( int c=0; c<image[r].length; c++ )
         {
            data[ r ][ c ] = (RGBColorPoint) image[ r ][ c ].getRGBColorPoint();
         }
      }

      writeImageData( data );
   }

   /**
    *
    */
   public void writeTargaFile( HLSColorPoint[][] image )
   {
      RGBColorPoint[][] data = new RGBColorPoint[ image.length ][ image[ 0 ].length ];

      for( int r=0; r<image.length; r++ )
      {
         for( int c=0; c<image[r].length; c++ )
         {
            data[ r ][ c ] = (RGBColorPoint) image[ r ][ c ].getRGBColorPoint();
         }
      }

      writeImageData( data );
   }

   /**
    * For 32 bit depth images, the alpha channel is the last byte and is usually FF.
    * @param TYPE
    * @return TYPE
    */
   public RGBColorPoint[][] readTargaFile()
   {
      RGBColorPoint[][] imageArray = null;
      int ibyte = 0;
      try
      {
         _inputStream = new BufferedInputStream( new FileInputStream( new File( _fileName ) ) );
         // ID Length field (1 byte)
         _inputStream.read();
         // Color map type (1 byte) -- 0 no color map, 1 includes color map
         _inputStream.read();
         // Image type (1 byte) -- 2 = uncompressed, true-color
         _inputStream.read();
         // Color Map specification (5 bytes) -- all null
         _inputStream.read();
         _inputStream.read();
         _inputStream.read();
         _inputStream.read();
         _inputStream.read();
         // Image specification (10 bytes) --
         // X origin (2 bytes)
         _inputStream.read();
         _inputStream.read();
         // Y origin (2 bytes)
         _inputStream.read();
         _inputStream.read();
         // Image width (2 bytes)
         int width = _inputStream.read();
         ibyte = _inputStream.read();
         ibyte = Integer.rotateLeft( ibyte, 8 );
         width = width | ibyte;
         System.out.println( "width is: " + width );
         // Image height (2 bytes)
         int height = _inputStream.read();
         ibyte = _inputStream.read();
         ibyte = Integer.rotateLeft( ibyte, 8 );
         height = height | ibyte;
         System.out.println( "height is: " + height );
         // Pixel depth
         int depth = _inputStream.read();
         System.out.println( "depth: " + depth );
         // Image descriptor -- bits 0-3 number of alpha channel bits, bits 5 & 4 is the pixel ordering
         // screen destination of first pixel         bit 5   bit 4
         // bottom left                                  0      0
         // bottom right                                 0      1
         // top left                                     1      0
         // top right                                    1      1
         // bits 7 & 6 must be zero
         // 0010:0000
         _inputStream.read();
         int red   = 0;
         int green = 0;
         int blue  = 0;

         imageArray = new RGBColorPoint[ width ][ height ];

         // data is written in height x width
         for( int i=0; i<width; i++ )
         {
            for( int j=0; j<height; j++ )
            {
               // check to see if we're grayscale:
               if( depth == 8 )
               {
                  red = green = blue = _inputStream.read();
               }
               // otherwise it's trucolor:
               else
               {
                  red   = _inputStream.read();
                  green = _inputStream.read();
                  blue  = _inputStream.read();
               }
               // alpha channel:
               if( depth > 24 )
               {
                  _inputStream.read();
               }
               imageArray[ i ][ j ] = new RGBColorPoint( red, green, blue );
            }
         }
         while( _inputStream.available() != 0 )
         {
            ibyte = _inputStream.read();
         }
         _inputStream.close();
      }
      catch( FileNotFoundException e )
      {
         System.err.println( "File Not Found Exception: " + e );
      }
      catch( IOException e )
      {
         System.err.println( "IOException: " + e );
      }

      return imageArray;
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
    * @param int
    */
   public void setColorDepth( int colorDepth )
   {
      _colorDepth = colorDepth;
   }

   /**
    *
    * @return int
    */
   public int getColorDepth()
   {
      return _colorDepth;
   }

   /**
    *
    * @param int
    */
   public void setType( int type )
   {
      _type = type;
   }

   /**
    *
    * @return int
    */
   public int getType()
   {
      return _type;
   }

   /**
    *
    * @param int
    */
   public void setDescriptor( int descriptor )
   {
      _descriptor = descriptor;
   }

   /**
    *
    * @return int
    */
   public int getDescriptor()
   {
      return _descriptor;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public RGBColorPoint[][] hilbertBlur( RGBColorPoint[][] input )
   {
      int size = Math.min( input.length, input[ 0 ].length );
      return hilbertBlur( input, size, 0.7 );
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public RGBColorPoint[][] fadeBorder( RGBColorPoint[][] input, double border )
   {
      int k = (int)( border * (double) input.length );
      int l = (int)( border * (double) input[ 0 ].length );
      System.out.println( "k is: " + k );

      for( int i=0; i<k; i++ )
      {
         for( int j=0; j<input[ i ].length; j++ )
         {
            input[ i ][ j ] = new RGBColorPoint( 0d, 0d, 0d );
         }
      }

      for( int i=( input.length - k ); i<input.length; i++ )
      {
         for( int j=0; j<input[ i ].length; j++ )
         {
            input[ i ][ j ] = new RGBColorPoint( 0d, 0d, 0d );
         }
      }

      for( int i=0; i<input.length; i++ )
      {
         for( int j=0; j<l; j++ )
         {
            input[ i ][ j ] = new RGBColorPoint( 0d, 0d, 0d );
         }
      }

      for( int i=0; i<input.length; i++ )
      {
         for( int j=( input[ i ].length - l ); j<input[ i ].length; j++ )
         {
            input[ i ][ j ] = new RGBColorPoint( 0d, 0d, 0d );
         }
      }

      return input;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public RGBColorPoint[][] hilbertBlur( RGBColorPoint[][] input, int size, double sma )
   {
      RGBColorPoint[][] output = new RGBColorPoint[ size ][ size ];
      MathUtilities     mu     = new MathUtilities();
      double[]          a      = new double[ size * size ];
      int[]             point  = null;

      for( int i=0; i<size; i++ )
      {
         for( int j=0; j<size; j++ )
         {
            a[ mu.getHilbertDistance( i, j, a.length ) ] = input[ i ][ j ].getRed();
         }
      }

      System.out.println( "sma is: " + sma );
      output[ 0 ][ 0 ] = new RGBColorPoint( a[ 0 ], a[ 0 ], a[ 0 ] );
      for( int i=1; i<a.length; i++ )
      {
         a[ i ]                             = a[ i - 1 ] + sma * ( a[ i ] - a[ i - 1 ] );
         point                              = mu.getHilbertPair( i, a.length );
         output[ point[ 0 ] ][ point[ 1 ] ] = new RGBColorPoint( a[ i ], a[ i ], a[ i ] );
      }

      return output;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public RGBColorPoint[][] hilbertBlurMask( RGBColorPoint[][] input )
   {
      return hilbertBlurMask( input, input.length, 0.7 );
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public RGBColorPoint[][] hilbertBlurMask( RGBColorPoint[][] input, double sma )
   {
      System.out.println( "in hilbertBlurMask" );
      return hilbertBlurMask( input, input.length, sma );
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public RGBColorPoint[][] hilbertBlurMask( RGBColorPoint[][] input, int size, double sma )
   {
      RGBColorPoint[][] blur   = hilbertBlur( input, size, sma );
      RGBColorPoint[][] output = new RGBColorPoint[ size ][ size ];

      for( int i=0; i<size; i++ )
      {
         for( int j=0; j<size; j++ )
         {
            double color = Math.abs( blur[ i ][ j ].getRed() - input[ i ][ j ].getRed() );
            output[ i ][ j ] = new RGBColorPoint( color, color, color ); 
         }
      }

      return output;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public RGBColorPoint[][] gammaCorrection( RGBColorPoint[][] input, double gamma, double scale )
   {
      RGBColorPoint[][] output = new RGBColorPoint[ input.length ][ input[ 0 ].length ];

      for( int i=0; i<input.length; i++ )
      {
         for( int j=0; j<input[ i ].length; j++ )
         {
            double color     = scale * Math.pow( input[ i ][ j ].getRed(), gamma );
            output[ i ][ j ] = new RGBColorPoint( color, color, color );
         }
      }

      return output;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public RGBColorPoint[][] logGammaCorrection( RGBColorPoint[][] input, double gamma, double scale )
   {
      RGBColorPoint[][] output = new RGBColorPoint[ input.length ][ input[ 0 ].length ];
      double            b      = Math.abs( Math.log( gamma ) );

      for( int i=0; i<input.length; i++ )
      {
         for( int j=0; j<input[ i ].length; j++ )
         {
            double color = 
               input[ i ][ j ].getRed() < gamma ? input[ i ][ j ].getRed() + gamma : input[ i ][ j ].getRed();
            color            = scale * Math.abs( ( Math.log( color ) + b ) / b );
            output[ i ][ j ] = new RGBColorPoint( color, color, color );
         }
      }

      return output;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public RGBColorPoint[][] expGammaCorrection( RGBColorPoint[][] input, double gamma, double scale )
   {
      RGBColorPoint[][] output = new RGBColorPoint[ input.length ][ input[ 0 ].length ];
      double            m      = Math.exp( gamma );

      for( int i=0; i<input.length; i++ )
      {
         for( int j=0; j<input[ i ].length; j++ )
         {
            double color     = input[ i ][ j ].getRed() * gamma;
            color            = scale * Math.exp( color ) / m;
            output[ i ][ j ] = new RGBColorPoint( color, color, color );
         }
      }

      return output;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public RGBColorPoint[][] tanhGammaCorrection( RGBColorPoint[][] input, double gamma, double scale )
   {
      RGBColorPoint[][] output = new RGBColorPoint[ input.length ][ input[ 0 ].length ];
      double            h      = gamma / 2;

      for( int i=0; i<input.length; i++ )
      {
         for( int j=0; j<input[ i ].length; j++ )
         {
            double color     = ( input[ i ][ j ].getRed() * gamma ) - h;
            color            = ( scale * Math.tanh( color ) + 1 ) / 2;
            output[ i ][ j ] = new RGBColorPoint( color, color, color );
         }
      }

      return output;
   }

   /**
    *
    */
   public static void main( String[] args )
   {
      System.out.println( "Initializing..." );
      TargaFile tfile = new TargaFile();

      short rowSize = 16;
      short colSize = 16;
      RGBColorPoint red = new RGBColorPoint( 160, 38, 23 );
      RGBColorPoint blue = new RGBColorPoint( 0, 0, 1 );
      RGBColorPoint mix = new RGBColorPoint( 1, 0, 0 );
      System.out.println( "H: " + ((HLSColorPoint)red.getHLSColorPoint()).getH() );
      System.out.println( "L: " + ((HLSColorPoint)red.getHLSColorPoint()).getL());
      System.out.println( "S: " + ((HLSColorPoint)red.getHLSColorPoint()).getS());
//       HLSColorPoint redHLS = new HLSColorPoint( 0, 0.00196078431372549, 1 ); // red
//       H: 6.569343065693431
//       L: 0.3588235294117647
//       S: 0.7486338797814207
      HLSColorPoint redHLS = new HLSColorPoint( 6.5693430656934310, 0.3588235294117647, 0.74863387978142071 );
      System.out.println( "red: " + ((RGBColorPoint)redHLS.getRGBColorPoint()).getRedComponent() );
      System.out.println( "green: " + ((RGBColorPoint)redHLS.getRGBColorPoint()).getGreenComponent() );
      System.out.println( "blue: " + ((RGBColorPoint)redHLS.getRGBColorPoint()).getBlueComponent() );
//       CIEColorPoint red = (CIEColorPoint) (new RGBColorPoint( 1, 0, 0 )).getCIEColorPoint();
//       CIEColorPoint green = (CIEColorPoint) (new RGBColorPoint( 0, 1, 0 )).getCIEColorPoint();
//       CIEColorPoint blue = (CIEColorPoint) (new RGBColorPoint( 0, 0, 1 )).getCIEColorPoint();
//       CIEColorPoint mix = red.mixColorPoint( blue );
//       CIEColorPoint mix = red.mixColorPoint( green );

      RGBColorPoint[][] image = new RGBColorPoint[ rowSize ][ colSize ];
//       CIEColorPoint[][] image = new CIEColorPoint[ rowSize ][ colSize ];
//       HLSColorPoint[][] image = new HLSColorPoint[ rowSize ][ colSize ];

      System.out.println( "mix.getBlue: " + mix.getBlue() );
      System.out.println( "mix.getBlue new: " + ( ( mix.getBlue() + blue.getBlue() ) / 2) );

      for( int r=0; r<rowSize; r++ )
      {
         mix = new RGBColorPoint( (mix.getRed() + blue.getRed()) / 3,
                                   0,
                                  (mix.getBlue() + blue.getBlue()) / 3 );
//          System.out.println( "mix.getBlue: " + mix.getBlue() );
         for( int c=0; c<colSize; c++ )
         {
            image[r][c] = new RGBColorPoint( Math.random(), Math.random(), Math.random() );
//             image[r][c] = new RGBColorPoint( (double)r / (double)rowSize, 0, 0 );
//             image[r][c] = new RGBColorPoint( 0, 0, (double)r / (double)rowSize );
//             image[r][c] = new CIEColorPoint( (double)r / (double)rowSize, 0, 0 );
//             image[r][c] = new HLSColorPoint( 360 * ((double)r / (double)rowSize), 0.9, 0.5 );
//             System.out.println( "[ " + r + " ][ " + c + " ] = " + image[ r ][ c ].getBlue() );
         }
      }

      tfile.writeTargaFile( image );

      GrafixUtilities       gu     = new GrafixUtilities();
      RGBColorPoint[][][][] tiling = gu.getHilbertTiling( image, 4 );


      int[][] temp = new int[ 16 ][ 16 ];
      Random rand = new Random();
      for( int i=0; i<temp.length; i++ )
      {
         for( int j=0; j<temp[ i ].length; j++ )
         {
            temp[ i ][ j ] = rand.nextInt();
            System.out.println( i + " , " + j + " = " + temp[ i ][ j ] );
         }
      }
      System.out.println( "about to get hilbert tiling array list..." );
      MathUtilities            mu         = new MathUtilities();
      int[][][]                tArrayList = mu.getHilbertTilingArrayList( temp, 4 );
      // we have i x j sub-matrices of size 4 from the original 16 x 16 data array:
      // in this case i = j = 7
      for( int i=0; i<tArrayList.length; i++ )
      {
         for( int j=0; j<tArrayList[ i ].length; j++ )
         {
            System.out.println( i + " , " + j );
            // This is the hilbert curve itself for the sub-matrix i, j of width 4
            for( int k=0; k<tArrayList[ i ][ j ].length; k++ )
            {
               System.out.println( i + " , " + j + " = " + tArrayList[ i ][ j ][ k ] );
            }
         }
      }

      RGBColorPoint[][] pixels = gu.getRGBData( "I30.BryantIrvin4.jpg" );
      VectorQuantizer vq = new VectorQuantizer( "TestVQ" );
      vq.setNumberOfClusters( 10 );
      vq.setNumberOfDimensions( 3 );
      vq.initialize();
      double[] p = new double[ 3 ];
      double[] a = new double[ 3 ];

      for( int i=0; i<pixels.length; i++ )
      {
         for( int j=0; j<pixels[ i ].length; j++ )
         {
            p[ 0 ] = pixels[ i ][ j ].getRed();
            p[ 1 ] = pixels[ i ][ j ].getGreen();
            p[ 2 ] = pixels[ i ][ j ].getBlue();
            vq.addPoint( p );
//             System.out.println( "A pixel: " + pixels[ i ][ j ].getRed() + " " + pixels[ i ][ j ].getGreen() + " " + pixels[ i ][ j ].getBlue() );
         }
      }
      gu.printAvailableFormats();
      RGBColorPoint rgb = null;
      System.out.println( "will collapse data... using: " + vq );
      for( int i=0; i<pixels.length; i++ )
      {
         for( int j=0; j<pixels[ i ].length; j++ )
         {
            p[ 0 ] = pixels[ i ][ j ].getRed();
            p[ 1 ] = pixels[ i ][ j ].getGreen();
            p[ 2 ] = pixels[ i ][ j ].getBlue();
//             System.out.println( "pixel before: " + p[ 0 ] + " " + p[ 1 ] + " " + p[ 2 ] );
//             a = vq.getPoint( vq.getCentroid( p ) );
//             System.out.println( "pixel after: " + a[ 0 ] + " " + a[ 1 ] + " " + a[ 2 ] );
            rgb = new RGBColorPoint( a[ 0 ], a[ 1 ], a[ 2 ] );
            pixels[ i ][ j ] = rgb;
         }
      }

      gu.putRGBData( pixels, "I30.BryantIrvin4.png", "PNG" );
      System.out.println( "wrote PNG file." );

//       String filename = "grayscale.tga";
//       int l = 128;
//       if( args.length > 0 )
//       {
//          filename = args[ 0 ];
//          System.out.println( "Setting filename to: " + filename );
//       }
// 
//       tfile = new TargaFile( filename + "s.tga" );
//       TargaFile gray = new TargaFile( filename + "s-gray.tga" );
//       image = tfile.convertToGrayscale( tfile.readTargaFile() );
//       image = tfile.fadeBorder( tfile.hilbertBlurMask( tfile.convertToGrayscale( tfile.readTargaFile() ), 0.1 ), 0.15 );
//       image = tfile.gammaCorrection( 
//             tfile.hilbertBlurMask( tfile.convertToGrayscale( tfile.readTargaFile() ), 0.1 ),
//             1.0, 0.45 );
//       image = tfile.logGammaCorrection( 
//             tfile.hilbertBlurMask( tfile.convertToGrayscale( tfile.readTargaFile() ), 0.1 ),
//             0.0009, 1.0 );
//       image = tfile.expGammaCorrection( 
//             tfile.hilbertBlurMask( tfile.convertToGrayscale( tfile.readTargaFile() ), 0.1 ),
//             3.0, 1.0 );

//       System.out.println( "About to do tanh gamma correction..." );
//       image = tfile.tanhGammaCorrection( 
//             tfile.hilbertBlurMask( tfile.convertToGrayscale( tfile.readTargaFile() ), 0.1 ),
//             3.0, 1.0 );
//       gray.setColorDepth( 8 );
//       gray.setType( 3 );
         // bottom left                                  0      0
         // bottom right                                 0      1
         // top left                                     1      0
         // top right                                    1      1
         // bits 7 & 6 must be zero
         // 0010:0000 -- top left
//       gray.setDescriptor( 0 );
// //       gray.setDescriptor( 32 );
//       gray.writeTargaFile( image );
      /**
       * @param int -- block size of the Hilbert curve.
       * @param int -- block size of the hidden matrix for the NLR Hilbert curve.
       * @param int -- side length of the hidden matrix.
       * @param int -- side length of the final matrix.
       */
      // This takes about 6 mins to complete for a 320 x 240 image:
//       EntropicHasher hasher = new EntropicHasher( 6, 4, 32, 3 );
      // This takes about 1.4 seconds to complete for a 320 x 240 image:
//       EntropicHasher hasher = new EntropicHasher( 5, 2, 7, 2 );
//       hasher.setEmbeddingLag( 80 );
//       EntropicPortrait portrait = hasher.getEntropicPortrait( data );
//       System.out.println( "Time was: " + (System.currentTimeMillis() - start) + " The portrait is:" );
//       System.out.println( portrait );

//       double[][][] ds = mu.hilbertDownsample( data, 64, 32 );
//       for( int i=0; i<ds.length; i++ )
//       {
//          System.out.println( "i is: " + i );
//          for( int j=0; j<ds[ i ].length; j++ )
//          {
//             System.out.println( "j is: " + j );
//             for( int k=0; k<ds[ i ][ j ].length; k++ )
//             {
//                System.out.println( "a data point: " + ds[ i ][ j ][ k ]  );
//             }
//          }
//       }
      /*
      for( int i=0; i<image.length; i++ )
      {
//          System.out.println( "new line" );
         for( int j=0; j<image[ i ].length; j++ )
         {
            pos = mu.getHilbertDistance( i, j, l );
//             System.out.println( "pos is: " + pos );
            data.set( pos, image[ i ][ j ].getRed() );
//             System.out.println( "red: " + image[ i ][ j ].getRed() );
//             System.out.println( "green: " + image[ i ][ j ].getGreen() );
//             System.out.println( "blue: " + image[ i ][ j ].getBlue() );
//             HLSColorPoint hls = (HLSColorPoint) image[ i ][ j ].getHLSColorPoint();
//             System.out.println( "hue: " + hls.getH() );
//             System.out.println( "saturation: " + hls.getL() );
//             System.out.println( "luminosity: " + hls.getS() );
         }
      }
      for( Double obj : data )
      {
         System.out.println( obj );
      }
      */
   }

}
