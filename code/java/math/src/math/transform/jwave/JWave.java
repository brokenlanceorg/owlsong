/**
 * JWave - Java implementation of wavelet transform algorithms
 *
 * Copyright 2010-2011 Christian Scheiblich
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 * This file JWave.java is part of JWave.
 *
 * @author Christian Scheiblich
 * date 23.02.2010 14:26:47
 * contact source@linux23.de
 */
package math.transform.jwave;

import math.transform.jwave.handlers.BasicTransform;
import math.transform.jwave.handlers.DiscreteFourierTransform;
import math.transform.jwave.handlers.DiscreteWaveletTransform;
import math.transform.jwave.handlers.FastWaveletTransform;
import math.transform.jwave.handlers.TransformInterface;
import math.transform.jwave.handlers.WaveletPacketTransform;
import math.transform.jwave.handlers.wavelets.Coif06;
import math.transform.jwave.handlers.wavelets.Daub02;
import math.transform.jwave.handlers.wavelets.Daub03;
import math.transform.jwave.handlers.wavelets.Daub04;
import math.transform.jwave.handlers.wavelets.Haar02;
import math.transform.jwave.handlers.wavelets.Lege02;
import math.transform.jwave.handlers.wavelets.Lege04;
import math.transform.jwave.handlers.wavelets.Lege06;
import math.transform.jwave.handlers.wavelets.Wavelet;
import math.transform.jwave.handlers.wavelets.WaveletInterface;

/**
 * Main class for doing little test runs for different transform types and
 * different wavelets without JUnit.
 * 
 * @date 23.02.2010 14:26:47
 * @author Christian Scheiblich
 */
public class JWave {

  /**
   * Constructor.
   * 
   * @date 23.02.2010 14:26:47
   * @author Christian Scheiblich
   */
  public JWave( ) {
  } // JWave

  /**
   * Main method for doing little test runs for different transform types and
   * different wavelets without JUnit. Requesting the transform type and the
   * type of wavelet to be used else usage is printed.
   * 
   * @date 23.02.2010 14:26:47
   * @author Christian Scheiblich
   * @param args
   *          [transformType] [waveletType]
   */
  public static void main( String[ ] args ) {

    String waveletTypeList = "Haar02, Lege02, Daub02, Lege04, Daub03, Lege06, Coif06, Daub04";

    if( args.length < 2 && args.length >3 ) {
      System.err.println( "usage: JWave [transformType] {waveletType} {iteration-Optional}" );
      System.err.println( "" );
      System.err.println( "transformType: DFT, FWT, WPT, DWT" );
      System.err.println( "waveletType : " + waveletTypeList );
      return;
    } // if args

    String wType = args[ 1 ];
    WaveletInterface wavelet = null;
    if( wType.equalsIgnoreCase( "haar02" ) )
      wavelet = new Haar02( );
    else if( wType.equalsIgnoreCase( "lege02" ) )
      wavelet = new Lege02( );
    else if( wType.equalsIgnoreCase( "daub04" ) )
      wavelet = new Daub02( );
    else if( wType.equalsIgnoreCase( "lege04" ) )
      wavelet = new Lege04( );
    else if( wType.equalsIgnoreCase( "daub06" ) )
      wavelet = new Daub03( );
    else if( wType.equalsIgnoreCase( "lege06" ) )
      wavelet = new Lege06( );
    else if( wType.equalsIgnoreCase( "coif06" ) )
      wavelet = new Coif06( );
    else if( wType.equalsIgnoreCase( "daub08" ) )
      wavelet = new Daub04( );
    else {
      System.err.println( "usage: JWave [transformType] {waveletType}" );
      System.err.println( "" );
      System.err.println( "available wavelets are " + waveletTypeList );
      return;
    } // if wType

    String tType = args[ 0 ];
    TransformInterface bWave = null;
    if( tType.equalsIgnoreCase( "dft" ) )
      bWave = new DiscreteFourierTransform( );
    else if( tType.equalsIgnoreCase( "fwt" ) )
      bWave = new FastWaveletTransform( wavelet );
    else if( tType.equalsIgnoreCase( "wpt" ) )
      bWave = new WaveletPacketTransform( wavelet );
    else if( tType.equalsIgnoreCase( "dwt" ) )
      bWave = new DiscreteWaveletTransform( wavelet );
    else {
      System.err.println( "usage: JWave [transformType] {waveletType}" );
      System.err.println( "" );
      System.err.println( "available transforms are DFT, FWT, WPT, DFT" );
      return;
    } // if tType
    
    // instance of transform
    Transform t ;
    
    if(args.length>2 ){
      int waveIter = Integer.parseInt(args[2]);
      t = new Transform( bWave, waveIter );
    }else{
      t = new Transform( bWave );
    }
      
//     double[ ] arrTime = { 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1., 1. };
    double[ ] arrTime = { 
//       0.658494, 0.908494, 0.725481, 0.658494, 1.707532, 2.457532, 0.908494,-0.024519,-1.000000, -2.000000, -3.000000, -4.000000  };
      0.0, 0.0, 0.725481, 0.658494, 1.0, 0.0, 0.0,-0.0,-0.000000, -0.000000, -0.000000, -0.000000  };

    System.out.println( "" );
    System.out.println( "time domain:" );
    for( int p = 0; p < arrTime.length; p++ )
//       System.out.printf( "%9.6f", arrTime[ p ] );
      System.out.println( arrTime[ p ] );
    System.out.println( "" );

    double[ ] arrFreqOrHilb = t.forward( arrTime ); // 1-D forward transform

    if( bWave instanceof DiscreteFourierTransform )
      System.out.println( "frequency domain:" );
    else
      System.out.println( "Hilbert domain:" );
    for( int p = 0; p < arrTime.length; p++ )
      System.out.printf( "%9.6f", arrFreqOrHilb[ p ] );
    System.out.println( "" );

    double[ ] arrReco = t.reverse( arrFreqOrHilb ); // 1-D reverse transform

    System.out.println( "reconstruction:" );
    for( int p = 0; p < arrTime.length; p++ )
//       System.out.printf( "%9.6f", arrReco[ p ] );
      System.out.println( arrReco[ p ] );
    System.out.println( "" );

  } // main

} // class
