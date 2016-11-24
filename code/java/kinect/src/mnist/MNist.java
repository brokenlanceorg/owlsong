package mnist;

import common.*;
import grafix.*;
import math.*;
import openkinect.*;
import openkinect.forest.*;

import java.util.*;
import java.math.*;

/**
 *
 */
public class MNist
{
   private boolean                               _first         = true;
   private DecisionForest                        _forest        = new DecisionForest( "mnist-forest", 1, 0.02, 3 );
   private HashMap< Integer, ArrayList< Long > > _classAverages = new HashMap< Integer, ArrayList< Long > >();
   private HashMap< Integer, Long >              _classMedians  = new HashMap< Integer, Long >();

   /**
    *
    */
   private MNist()
   {
      _forest = ( DecisionForest ) ( new DecisionForest( "mnist-forest" ) ).getDAO().deserialize();
      if( _forest == null )
      {
         _forest        = new DecisionForest( "mnist-forest", 1, 0.005, 3 );
         _forest.setIsAsynchronous( false );
      }
//       _classAverages.put( 0, 0.0 );
//       _classAverages.put( 1, 0.0 );
//       _classAverages.put( 2, 0.0 );
//       _classAverages.put( 3, 0.0 );
//       _classAverages.put( 4, 0.0 );
//       _classAverages.put( 5, 0.0 );
//       _classAverages.put( 6, 0.0 );
//       _classAverages.put( 7, 0.0 );
//       _classAverages.put( 8, 0.0 );
//       _classAverages.put( 9, 0.0 );
      _classAverages.put( 0, new ArrayList< Long >() );
      _classAverages.put( 1, new ArrayList< Long >() );
      _classAverages.put( 2, new ArrayList< Long >() );
      _classAverages.put( 3, new ArrayList< Long >() );
      _classAverages.put( 4, new ArrayList< Long >() );
      _classAverages.put( 5, new ArrayList< Long >() );
      _classAverages.put( 6, new ArrayList< Long >() );
      _classAverages.put( 7, new ArrayList< Long >() );
      _classAverages.put( 8, new ArrayList< Long >() );
      _classAverages.put( 9, new ArrayList< Long >() );
   }

   /**
    *
    */
   private String[] getFileNames()
   {
      String[] strings = new String[ 10 ];

      strings[ 0 ] = "mnist/mnist_train0.jpg";
      strings[ 1 ] = "mnist/mnist_train1.jpg";
      strings[ 2 ] = "mnist/mnist_train2.jpg";
      strings[ 3 ] = "mnist/mnist_train3.jpg";
      strings[ 4 ] = "mnist/mnist_train4.jpg";
      strings[ 5 ] = "mnist/mnist_train5.jpg";
      strings[ 6 ] = "mnist/mnist_train6.jpg";
      strings[ 7 ] = "mnist/mnist_train7.jpg";
      strings[ 8 ] = "mnist/mnist_train8.jpg";
      strings[ 9 ] = "mnist/mnist_train9.jpg";

      return strings;
   }

   /**
    *
    */
   private String[] getTestFileNames()
   {
      String[] strings = new String[ 10 ];

      strings[ 0 ] = "mnist/mnist_test0.jpg";
      strings[ 1 ] = "mnist/mnist_test1.jpg";
      strings[ 2 ] = "mnist/mnist_test2.jpg";
      strings[ 3 ] = "mnist/mnist_test3.jpg";
      strings[ 4 ] = "mnist/mnist_test4.jpg";
      strings[ 5 ] = "mnist/mnist_test5.jpg";
      strings[ 6 ] = "mnist/mnist_test6.jpg";
      strings[ 7 ] = "mnist/mnist_test7.jpg";
      strings[ 8 ] = "mnist/mnist_test8.jpg";
      strings[ 9 ] = "mnist/mnist_test9.jpg";

      return strings;
   }

   /**
    *
    */
   private RGBColorPoint[][] getSmallImage( int xoffset, int yoffset, RGBColorPoint[][] image )
   {
      RGBColorPoint[][] smallImage = new RGBColorPoint[ 28 ][ 28 ];

      for( int i=0; i<28; i++ )
      {
         for( int j=0; j<28; j++ )
         {
            smallImage[ i ][ j ] = image[ xoffset + i ][ yoffset + j ];
         }
      }

      return smallImage;
   }

   /**
    *
    */
   private BigInteger[] classify( RGBColorPoint[][] image, int label )
   {
      ArrayList< Integer > xpoints = new ArrayList< Integer >();
      ArrayList< Integer > ypoints = new ArrayList< Integer >();
      ArrayList< Double >  feature = new ArrayList< Double >();
      MathUtilities        mu      = new MathUtilities();
      BigInteger[]         classes = null;
      double[]             vector  = null;
      int                  xmid    = 0;
      int                  ymid    = 0;
      double               thresh  = 0.9;

      for( int i=0; i<28; i++ )
      {
         for( int j=0; j<28; j++ )
         {
            if(    image[ i ][ j ].getRed()   >= thresh )
            {
               xpoints.add( i );
               ypoints.add( j );
            }
         }
      }

      Collections.sort( xpoints );
      Collections.sort( ypoints );

      if( xpoints.size() > 0 )
      {
         xmid = xpoints.get( xpoints.size() / 2 );
         ymid = ypoints.get( ypoints.size() / 2 );

         for( int i=0; i<28; i++ )
         {
            for( int j=0; j<28; j++ )
            {
               if( image[ i ][ j ].getRed() >= thresh )
               {
                  feature.add( mu.getEuclideanDistance2D( xmid, ymid, i, j ) );
               }
            }
         }
//          Collections.sort( feature );
         vector  = mu.toArray( mu.normalize( feature, 1.0 ) );
         vector  = mu.getReducedVector( vector, 100 );
         System.out.print( "\nlabel:," + label );
         for( int i=0; i<vector.length; i++ )
         {
            System.out.print( "," + vector[ i ] );
         }
         System.out.println( "" );
//          classes = _forest.classify( vector );
//          System.out.println( label + " -- class: " + classes[ 0 ] );
      }

      return classes;
   }

   /**
    * gu.putRGBData2( out, "test-0.jpg", "JPG" );
    */
   private void train( String fileName, int label )
   {
      ArrayList< Integer > numbers   = new ArrayList< Integer >();
      ArrayList< Long >    average   = null;
      GrafixUtilities      gu        = new GrafixUtilities();
      MathUtilities        mu        = new MathUtilities();
      RGBColorPoint[][]    fullImage = gu.getRGBData2( fileName );
      BigInteger[]         clazz     = null;

      for( int i=0; i<fullImage.length; i+=28 )
      {
         for( int j=0; j<fullImage[ i ].length; j+=28 )
         {
            clazz   = classify( getSmallImage( i, j, fullImage ), label );
            if( clazz != null )
            {
               average = _classAverages.get( label );
               average.add( clazz[ 0 ].longValue() );
            }
         }
      }
   }

   /**
    * gu.putRGBData2( out, "test-0.jpg", "JPG" );
    */
   private void test( String fileName, int label )
   {
      ArrayList< Integer > numbers   = new ArrayList< Integer >();
      GrafixUtilities      gu        = new GrafixUtilities();
      MathUtilities        mu        = new MathUtilities();
      RGBColorPoint[][]    fullImage = gu.getRGBData2( fileName );
      BigInteger[]         clazz     = null;

      for( int i=0; i<fullImage.length; i+=28 )
      {
         for( int j=0; j<fullImage[ i ].length; j+=28 )
         {
            clazz = classify( getSmallImage( i, j, fullImage ), label );
            if( clazz != null )
            {
               System.out.println( "real class: " + label + " classified: " + getNearestClass( clazz ) );
            }
         }
      }
   }

   /**
    *
    */
   private Integer getNearestClass( BigInteger[] clazz )
   {
      Integer c = 0;
      long    m = 0;
      long    t = 0;
      long    d = Long.MAX_VALUE;

      for( Integer obj : _classMedians.keySet() )
      {
         m = _classMedians.get( obj );
         t = Math.abs( clazz[ 0 ].longValue() - m );
         if( t < d )
         {
            d = t;
            c = obj;
         }
      }

      return c;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void trainMNist()
   {
      String[] files = getFileNames();
      for( int i=0; i<files.length; i++ )
      {
         train( files[ i ], i );
      }
      _forest.getDAO().serialize( _forest );
   }

   /**
    *
    */
   private void calculateMedians()
   {
      for( Integer obj : _classAverages.keySet() )
      {
         ArrayList< Long > v = _classAverages.get( obj );
         Collections.sort( v );
         _classMedians.put( obj, v.get( v.size() / 2 ) );
         System.out.println( "class: " + obj + " median: " + _classMedians.get( obj ) );
      }
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void testMNist()
   {
      // call this to get the averages
      trainMNist();
      calculateMedians();

      String[] files = getTestFileNames();
      for( int i=0; i<files.length; i++ )
      {
         test( files[ i ], i );
      }
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void setCanSplit( boolean split )
   {
      _forest.setCanSplit( split );
   }

   /**
    *
    */
   public static void main( String[] args )
   {
      MNist test = new MNist();
      if( args.length > 0 )
      {
         if( "train".equals( args[ 0 ] ) )
         {
            test.setCanSplit( true );
            test.trainMNist();
         }
         else if( "test".equals( args[ 0 ] ) )
         {
            test.setCanSplit( false );
            test.testMNist();
         }
      }
   }

}
