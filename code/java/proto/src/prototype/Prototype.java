package prototype;

import java.util.*;

import java.math.*;
import math.*;
import math.cluster.*;
import common.*;
import grafix.*;
import grafix.RGBColorPoint;
import functional.network.*;
import functional.network.ManyToOneCorrelation.ManyToOneCorrelationDAO;

/**
 *
 */
public class Prototype implements Runnable
{

   /**
    *
    */
   public Prototype()
   {
      String localVar;
   }

   /**
    *
    */
   private void testMTOC()
   {
      // here, the image would be blockSize x blockSize
      int blockSize = 8;
      int variables = 28;
      int total     = blockSize * blockSize;
      int instances = total / variables;

      double[]      data = new double[ total ];
      MathUtilities mu   = new MathUtilities();

      for( int i=0; i<total; i++ )
      {
         data[ i ] = mu.random();
         System.out.println( "a random point: " + data[ i ] );
      }

      double[][] vars = mu.deconstructTimeSeries( data, variables );

      for( int i=0; i<variables; i++ )
      {
         System.out.println( "Variable: " + i );
         for( int j=0; j<instances; j++ )
         {
            System.out.println( vars[ i ][ j ] );
         }
      }

      vars = mu.normalize( vars );
      System.out.println( "#########################After normalization....###################" );

      for( int i=0; i<variables; i++ )
      {
         System.out.println( "Variable: " + i );
         for( int j=0; j<instances; j++ )
         {
            System.out.println( vars[ i ][ j ] );
         }
      }

      data = new double[ instances ];
      for( int i=0; i<instances; i++ )
      {
         data[ i ] = mu.random();
      }
      data = mu.normalize( data, 1.0 );

      // Testing the ManyToOne..
      System.out.println( "Will create a new ManyToOneCorrelation..." );

      HaltingCriteria criteria = new HaltingCriteria();
      criteria.setElapsedTimeTolerance( mu.getTime( "1s" ) );

      ManyToOneCorrelation correlation = new ManyToOneCorrelation( "test-MTOC" );
      correlation.setHaltingCriteria( criteria );
      correlation.setIndependentData( vars );
      correlation.setDependentData( data );
      correlation.setGenomeLength( ( vars.length * 2 ) );
      correlation.train();

      double[] input = new double[ vars.length ];
      for( int i=0; i<vars[ 0 ].length; i++ )
      {
         for( int j=0; j<vars.length; j++ )
         {
            input[ j ] = vars[ j ][ i ];
         }
         System.out.println( ( correlation.correlate( input ) )[ 0 ] + " , " + data[ i ] );
      }
   }

   /**
    *
    */
   private void testMTOC2()
   {
//       correlation.getDAO().serialize( correlation );
//       ManyToOneCorrelationDAO dao = 
//          (ManyToOneCorrelationDAO) (new ManyToOneCorrelation( args[ 1 ] )).getDAO();
//       ManyToOneCorrelation correlation = dao.deserialize();
//       dao.serialize( correlation );
//
      // here, the image would be blockSize x blockSize
      int blockSize = 8;
      int variables = 16;
      int total     = blockSize * blockSize;
      int instances = total / variables;

      double[]      data = new double[ total ];
      MathUtilities mu   = new MathUtilities();

      for( int i=0; i<total; i++ )
      {
         data[ i ] = mu.random();
         System.out.println( "a random point: " + data[ i ] );
      }

      double[][] vars = mu.deconstructTimeSeries( data, variables );

      for( int i=0; i<variables; i++ )
      {
         System.out.println( "Variable: " + i );
         for( int j=0; j<instances; j++ )
         {
            System.out.println( vars[ i ][ j ] );
         }
      }

      vars = mu.normalize( vars );
      System.out.println( "#########################After normalization....###################" );

      for( int i=0; i<variables; i++ )
      {
         System.out.println( "Variable: " + i );
         for( int j=0; j<instances; j++ )
         {
            System.out.println( vars[ i ][ j ] );
         }
      }

      data = new double[ instances ];
      for( int i=0; i<instances; i++ )
      {
         data[ i ] = mu.random();
      }
      data = mu.normalize( data, 1.0 );

      // Testing the ManyToOne..
      System.out.println( "Will create a new ManyToOneCorrelation..." );

      HaltingCriteria criteria = new HaltingCriteria();
      criteria.setElapsedTimeTolerance( mu.getTime( "1s" ) );

      ManyToOneCorrelation correlation = new ManyToOneCorrelation( "test-MTOC" );
      correlation.setHaltingCriteria( criteria );
      correlation.setIndependentData( vars );
      correlation.setDependentData( data );
      correlation.setGenomeLength( ( vars.length * 2 ) );
      correlation.train();

      double[] input = new double[ vars.length ];
      for( int i=0; i<vars[ 0 ].length; i++ )
      {
         for( int j=0; j<vars.length; j++ )
         {
            input[ j ] = vars[ j ][ i ];
         }
         System.out.println( ( correlation.correlate( input ) )[ 0 ] + " , " + data[ i ] );
      }
   }

   /**
    *
    */
   private void testMTOC3()
   {
      int variables = 28;
      int instances = 10;
      double[][] vars = new double[ variables ][ instances ];

      MathUtilities mu   = new MathUtilities();

      for( int i=0; i<variables; i++ )
      {
         for( int j=0; j<instances; j++ )
         {
            vars[ i ][ j ] = mu.random();
         }
      }

      vars = mu.normalize( vars );
      System.out.println( "#########################After normalization....###################" );

      for( int i=0; i<variables; i++ )
      {
         System.out.println( "Variable: " + i );
         for( int j=0; j<instances; j++ )
         {
            System.out.println( vars[ i ][ j ] );
         }
      }

      double[] data = new double[ instances ];
      for( int i=0; i<instances; i++ )
      {
         data[ i ] = mu.random();
      }
      data = mu.normalize( data, 1.0 );

      // Testing the ManyToOne..
      System.out.println( "Will create a new ManyToOneCorrelation..." );

      HaltingCriteria criteria = new HaltingCriteria();
      criteria.setElapsedTimeTolerance( mu.getTime( "1s" ) );

      ManyToOneCorrelation correlation = new ManyToOneCorrelation( "test-MTOC" );
      correlation.setHaltingCriteria( criteria );
      correlation.setIndependentData( vars );
      correlation.setDependentData( data );
      correlation.setGenomeLength( ( vars.length * 2 ) );
      correlation.train();
      System.out.println( "About to serialize..." );
      correlation.getDAO().serialize( correlation );

      double[] input = new double[ vars.length ];
      for( int i=0; i<vars[ 0 ].length; i++ )
      {
         for( int j=0; j<vars.length; j++ )
         {
            input[ j ] = vars[ j ][ i ];
         }
         System.out.println( ( correlation.correlate( input ) )[ 0 ] + " , " + data[ i ] );
      }
   }

   /**
    *
    */
   private void testMTOC4()
   {
      ManyToOneCorrelationDAO dao         = (ManyToOneCorrelationDAO) (new ManyToOneCorrelation( "test-MTOC" )).getDAO();
      ManyToOneCorrelation    correlation = dao.deserialize();
      GrafixUtilities         gu          = new GrafixUtilities();
      RGBColorPoint[][]       pixels1     = gu.getRGBData( "test-image.jpg" );
      RGBColorPoint[][]       pixels2     = new RGBColorPoint[ pixels1.length ][ pixels1[ 0 ].length ];
      double[]                input       = new double[ pixels1.length ];

      for( int i=0; i<pixels1[ 0 ].length; i++ )
      {
         for( int j=0; j<pixels1.length; j++ )
         {
            input[ j ] = pixels1[ j ][ i ].getBlue();
         }
         System.out.println( ( correlation.correlate( input ) )[ 0 ] );
         pixels2[ 0 ][ i ] = new RGBColorPoint( 0.0, 0.0, ( correlation.correlate( input ) )[ 0 ] );
         for( int k=1; k<pixels2.length; k++ )
         {
            pixels2[ k ][ i ] = pixels2[ 0 ][ i ];
         }
      }
      gu.putRGBData( pixels2, "mtoc-test-image.jpg", "BMP" );
   }

   /**
    *
    */
   private void testVectorQuantizer()
   {
      // Test of the GrafixUtilities and the VectorQuantizer:
      GrafixUtilities   gu      = new GrafixUtilities();
//       RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/cattle.jpg" );
//       RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/kinect.depth.jpg" );
      RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/kinect.depth.4.jpg" );
//       RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/kinect.color.jpg" );
      RGBColorPoint[][] pixels2 = new RGBColorPoint[ pixels1.length ][ pixels1[ 0 ].length ];
      VectorQuantizer   vq      = new VectorQuantizer( "TestVQ" );
      double[]          a       = null;
      double[]          d       = new double[ 1 ];

      vq.setNumberOfClusters( 5 );
      vq.setNumberOfDimensions( 1 );
      vq.setThreshold( 0.10 );
      vq.initialize();

      for( int i=0; i<pixels1.length; i++ )
      {
         for( int j=0; j<pixels1[ i ].length; j++ )
         {
            d[ 0 ] = pixels1[ i ][ j ].getRed();
            vq.addPoint( d );
         }
      }
      vq.reset();
      for( int i=0; i<pixels1.length; i++ )
      {
         for( int j=0; j<pixels1[ i ].length; j++ )
         {
            d[ 0 ] = pixels1[ i ][ j ].getRed();
            vq.getCluster( d );
         }
      }

      int p = 0;
      vq.pruneClusters();
      System.out.println( "number of clusters: " + vq.getNumberOfClusters() );

      for( int k=0; k<vq.getNumberOfClusters(); k++ )
      {
         for( int i=0; i<pixels2.length; i++ )
         {
            for( int j=0; j<pixels2[ i ].length; j++ )
            {
               d[ 0 ] = pixels1[ i ][ j ].getRed();

               p                 = vq.getCluster( d );
               a                 = vq.getCentroid( p );
               if( p == k )
               {
                  pixels2[ i ][ j ] = new RGBColorPoint( a[ 0 ], a[ 0 ], a[ 0 ] );
               }
               else
               {
                  pixels2[ i ][ j ] = new RGBColorPoint( 0, 0, 0 );
               }
            }
         }
         gu.putRGBData( pixels2, "test-images/kinect.depth.4-" + k + ".png", "PNG" );
//       gu.putRGBData( pixels2, "test-images/kinect.color.5.png", "PNG" );
      }


      double[] cdf = vq.getDistanceCDF();
      for( int i=0; i<cdf.length; i++ )
      {
         System.out.println( "a distance: " + cdf[ i ] );
      }
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void testVectorQuantizer2()
   {
      GrafixUtilities   gu      = new GrafixUtilities();
      RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/cattle.jpg" );
      VectorQuantizer   vq      = new VectorQuantizer( "TestVQ" );
      double[]          d       = new double[ 3 ];
      int               w       = pixels1.length;
      int               h       = pixels1[ 0 ].length;

      System.out.println( "image width:  " + w );
      System.out.println( "image height: " + h );

      vq.setNumberOfClusters( 5 );
      vq.setNumberOfDimensions( 3 );
      vq.setThreshold( 0.10 );
      vq.initialize();

      for( int i=0; i<pixels1.length; i++ )
      {
         for( int j=0; j<pixels1[ i ].length; j++ )
         {
            d[ 0 ] = pixels1[ i ][ j ].getRed();
            d[ 1 ] = pixels1[ i ][ j ].getGreen();
            d[ 2 ] = pixels1[ i ][ j ].getBlue();

            vq.addPoint( d );
         }
      }

      vq.pruneClusters();
      System.out.println( "Number of clusters: " + vq.getNumberOfClusters() );

      HashMap< Integer, ArrayList< Integer[] > > clusterPointMapping = new HashMap< Integer, ArrayList< Integer[] > >();
      int[]                                  position            = null;
      int                                    c                   = -1;

      for( int i=0; i<pixels1.length; i++ )
      {
         for( int j=0; j<pixels1[ i ].length; j++ )
         {
            d[ 0 ] = pixels1[ i ][ j ].getRed();
            d[ 1 ] = pixels1[ i ][ j ].getGreen();
            d[ 2 ] = pixels1[ i ][ j ].getBlue();

            c = vq.getCluster( d );

            position      = new int[ 2 ];
            position[ 0 ] = i;
            position[ 1 ] = j;

            putPoint( clusterPointMapping, position, c );
         }
      }

      // just make sure what we're doing is valid:
      RGBColorPoint[][] pixels2 = new RGBColorPoint[ pixels1.length ][ pixels1[ 0 ].length ];
      double[]          a       = null;
      for( int i=0; i<pixels2.length; i++ )
      {
         for( int j=0; j<pixels2[ i ].length; j++ )
         {
            d[ 0 ] = pixels1[ i ][ j ].getRed();
            d[ 1 ] = pixels1[ i ][ j ].getGreen();
            d[ 2 ] = pixels1[ i ][ j ].getBlue();

            a                 = vq.getCentroid( vq.getCluster( d ) );
            pixels2[ i ][ j ] = new RGBColorPoint( a[ 0 ], a[ 1 ], a[ 2 ] );
         }
      }
      gu.putRGBData( pixels2, "test-images/cattle.rbg.prune.png", "PNG" );
      // end valid check

      // here
      Set< Integer > k = clusterPointMapping.keySet();
      vq = null;

      for( Integer key : k )
      {
         // now for each cluster, we want to cluster its points:
         ArrayList< Integer[] > points = clusterPointMapping.get( key );
         VectorQuantizer        vqq    = new VectorQuantizer( "TestVQQ" );
         double[]               dd     = new double[ 2 ];

         vqq.setNumberOfClusters( 5 );
         vqq.setNumberOfDimensions( 2 );
         vqq.setThreshold( 0.10 );
         vqq.initialize();

         System.out.println( "\nFor cluster: " + key + " size is: " + points.size() );

         MathUtilities mu = new MathUtilities();
         for( int i=0; i<points.size(); i++ )
         {
            int pos = (int) ((double) points.size() * mu.random() );
            Integer[] point = points.get( pos );
            dd[ 0 ] = point[ 0 ].doubleValue() / (double) w;
            dd[ 1 ] = point[ 1 ].doubleValue() / (double) h;
            vqq.addPoint( dd );
         }

         vqq.pruneClusters();
         System.out.println( "\nVQQ Number of clusters: " + vqq.getNumberOfClusters() );
         System.out.println( "The centroids are:" );
         for( int i=0; i<vqq.getNumberOfClusters(); i++ )
         {
            double[] cent = vqq.getCentroid( i );
            System.out.println( cent[ 0 ] + "," + cent[ 1 ] );
         }

         ArrayList< Double >                       xValues  = new ArrayList< Double >();
         HashMap< Integer, ArrayList< Double[] > > hh       = new HashMap< Integer, ArrayList< Double[] > >();

         for( Integer[] point : points )
         {
            dd[ 0 ] = point[ 0 ].doubleValue() / (double) w;
            dd[ 1 ] = point[ 1 ].doubleValue() / (double) h;

            c  = vqq.getCluster( dd );
            if( key == 0 )
            {
               pixels2[ point[ 0 ] ][ point[ 1 ] ] = new RGBColorPoint( (double)c / (double)vqq.getNumberOfClusters(), 0.0d, 0.0d );
            }

            putPoint( hh, dd, c );
         }
         gu.putRGBData( pixels2, "test-images/cattle.rbg.prune.x.png", "PNG" );

         /*
         Set< Integer > kk = hh.keySet();
         for( Integer q : kk )
         {
            ArrayList< Double[] > cc = hh.get( q );
            for( Double[] point : cc )
            {
               xValues.add( point[ 0 ] );
//                System.out.println( "a cluster: " + q );
//                System.out.println( "an x:      " + point[ 0 ] );
            }

            Collections.sort( xValues );
            System.out.println( "\nVQQ " + q + " Inverse CDF of x values:" );

            for( Double x : xValues )
            {
               System.out.println( "x value:, " + x );
            }
         }
         */

      }

   }

   /**
    *
    */
   private void putPoint( HashMap< Integer, ArrayList< Integer[] > > h, int[] p, int c )
   {
      ArrayList< Integer[] > points = h.get( c );

      if( points == null )
      {
         points = new ArrayList< Integer[] >();
         h.put( c, points );
      }

      Integer[] pp = new Integer[ 2 ];

      pp[ 0 ] = new Integer( p[ 0 ] );
      pp[ 1 ] = new Integer( p[ 1 ] );

      points.add( pp );
   }

   /**
    *
    */
   private void putPoint( HashMap< Integer, ArrayList< Double[] > > h, double[] p, int c )
   {
      ArrayList< Double[] > points = h.get( c );

      if( points == null )
      {
         points = new ArrayList< Double[] >();
         h.put( c, points );
      }
//       System.out.println( "adding a value: " + p[ 0 ] );

      Double[] pp = new Double[ 2 ];

      pp[ 0 ] = new Double( p[ 0 ] );
      pp[ 1 ] = new Double( p[ 1 ] );

      points.add( pp );
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void testVectorQuantizer3()
   {
      GrafixUtilities   gu      = new GrafixUtilities();
      MathUtilities     mu      = new MathUtilities();
      RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/cattle.jpg" );
//       RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/cattle-512.jpg" );
//       RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/cattle-300.jpg" );
      VectorQuantizer   vq      = new VectorQuantizer( "TestVQ" );
      double[]          d       = new double[ 3 ];
      int               w       = pixels1.length;
      int               h       = pixels1[ 0 ].length;

      System.out.println( "image width:  " + w );
      System.out.println( "image height: " + h );

      vq.setNumberOfClusters( 5 );
      vq.setNumberOfDimensions( 3 );
      vq.setThreshold( 0.10 );
      vq.initialize();

      for( int i=0; i<pixels1.length; i++ )
      {
         for( int j=0; j<pixels1[ i ].length; j++ )
         {
            d[ 0 ] = pixels1[ i ][ j ].getRed();
            d[ 1 ] = pixels1[ i ][ j ].getGreen();
            d[ 2 ] = pixels1[ i ][ j ].getBlue();

            vq.addPoint( d );
         }
      }
      for( int i=0; i<pixels1.length; i++ )
      {
         for( int j=0; j<pixels1[ i ].length; j++ )
         {
            d[ 0 ] = pixels1[ i ][ j ].getRed();
            d[ 1 ] = pixels1[ i ][ j ].getGreen();
            d[ 2 ] = pixels1[ i ][ j ].getBlue();

            vq.getCluster( d );
         }
      }

      vq.pruneClusters();
      System.out.println( "Number of clusters: " + vq.getNumberOfClusters() );

      // just make sure what we're doing is valid:
      RGBColorPoint[][] pixels2 = new RGBColorPoint[ pixels1.length ][ pixels1[ 0 ].length ];
      double[]          a       = null;
      for( int i=0; i<pixels2.length; i++ )
      {
         for( int j=0; j<pixels2[ i ].length; j++ )
         {
            d[ 0 ] = pixels1[ i ][ j ].getRed();
            d[ 1 ] = pixels1[ i ][ j ].getGreen();
            d[ 2 ] = pixels1[ i ][ j ].getBlue();

            a                 = vq.getCentroid( vq.getCluster( d ) );
            pixels2[ i ][ j ] = new RGBColorPoint( a[ 0 ], a[ 1 ], a[ 2 ] );
         }
      }
      gu.putRGBData( pixels2, "test-images/cattle.rbg.prune.png", "PNG" );
      // end valid check

      pixels2                  = gu.convertToGrayscale( pixels2 );
      double[][] hilbertMatrix = new double[ pixels2.length ][ pixels2[ 0 ].length ];

      for( int i=0; i<pixels2.length; i++ )
      {
         for( int j=0; j<pixels2[ i ].length; j++ )
         {
            d[ 0 ] = pixels2[ i ][ j ].getRed();
            d[ 1 ] = pixels2[ i ][ j ].getGreen();
            d[ 2 ] = pixels2[ i ][ j ].getBlue();

            hilbertMatrix[ i ][ j ] = d[ 0 ];
         }
      }

      double[]   hilbertArray     = mu.getSMAHilbertArray( hilbertMatrix, 0.55 );
      double[][] smaHilbertMatrix = mu.getHilbertMatrix( hilbertArray, w, h );

      for( int i=0; i<pixels2.length; i++ )
      {
         for( int j=0; j<pixels2[ i ].length; j++ )
         {
            pixels2[ i ][ j ] = new RGBColorPoint( smaHilbertMatrix[ i ][ j ], 
                                                   smaHilbertMatrix[ i ][ j ], 
                                                   smaHilbertMatrix[ i ][ j ] );
         }
      }
      gu.putRGBData( pixels2, "test-images/cattle.hilbert.png", "PNG" );
   }

   /**
    *
    */
   private void testVectorQuantizer4()
   {
      // Test of the GrafixUtilities and the VectorQuantizer:
      GrafixUtilities   gu      = new GrafixUtilities();
//       RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/cattle.jpg" );
//       RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/kinect.depth.jpg" );
//       RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/kinect.color.jpg" );
      RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/kinect.depth.4.jpg" );
      RGBColorPoint[][] pixels2 = new RGBColorPoint[ pixels1.length ][ pixels1[ 0 ].length ];

      VectorQuantizer          vq      = new VectorQuantizer( "TestVQ" );
      NearestNeighborQuantizer nn      = new NearestNeighborQuantizer();
      MeanShiftQuantizer       ms      = new MeanShiftQuantizer();
      double[]                 a       = null;
      double[]                 d       = new double[ 3 ];
      long                     s       = System.currentTimeMillis();

      System.out.println( "Initializing..." );

      vq.setNumberOfClusters( 10 );
      vq.setNumberOfDimensions( 3 );
      vq.initialize();

      for( int i=0; i<pixels1.length; i++ )
      {
         for( int j=0; j<pixels1[ i ].length; j++ )
         {
            d[ 0 ] = pixels1[ i ][ j ].getRed();
            d[ 1 ] = pixels1[ i ][ j ].getGreen();
            d[ 2 ] = pixels1[ i ][ j ].getBlue();

            vq.addPoint( d );
         }
      }

      System.out.println( "Time to perform addPoints:                " + ( System.currentTimeMillis() - s ) );
      s = System.currentTimeMillis();

      vq.reset();

      for( int i=0; i<pixels1.length; i++ )
      {
         for( int j=0; j<pixels1[ i ].length; j++ )
         {
            d[ 0 ] = pixels1[ i ][ j ].getRed();
            d[ 1 ] = pixels1[ i ][ j ].getGreen();
            d[ 2 ] = pixels1[ i ][ j ].getBlue();

            vq.getCentroid( vq.getCluster( d ) );
         }
      }

      vq.pruneClusters();

      System.out.println( "Finished pruning...number of VQ clusters: " + vq.getNumberOfClusters() );
      System.out.println( "Time to perform pruning:                  " + ( System.currentTimeMillis() - s ) );
      s = System.currentTimeMillis();

      ms.setVectorQuantizer( vq );
      ms.setCDFThreshold( 0.3 );
      ms.optimize();

      System.out.println( "Finished optimizing..." );
      System.out.println( "Time to perform optimizing:               " + ( System.currentTimeMillis() - s ) );
      s = System.currentTimeMillis();

      nn.setClusters( ms.getClusters() );
      nn.setClusterPointMap( ms.getClusterPointMap() );

      System.out.println( "Number of NN clusters:                    " + nn.getNumberOfClusters() );
      System.out.println( "Time to perform NN initialization:        " + ( System.currentTimeMillis() - s ) );
      s = System.currentTimeMillis();

      int p = 0;

      for( int i=0; i<pixels2.length; i++ )
      {
         for( int j=0; j<pixels2[ i ].length; j++ )
         {
            d[ 0 ] = pixels1[ i ][ j ].getRed();
            d[ 1 ] = pixels1[ i ][ j ].getGreen();
            d[ 2 ] = pixels1[ i ][ j ].getBlue();

            p                 = ( nn.getCluster( d ) );
            a                 = nn.getCentroid( p );
            pixels2[ i ][ j ] = new RGBColorPoint( a[ 0 ], a[ 1 ], a[ 2 ] );
         }
      }

      System.out.println( "Time to perform NN getCluster/Centroid:   " + ( System.currentTimeMillis() - s ) );

//       gu.putRGBData( pixels2, "test-images/cattle.knn.png", "PNG" );
      gu.putRGBData( pixels2, "test-images/kinect.depth.4.knn.png", "PNG" );
//       gu.putRGBData( pixels2, "test-images/kinect.color.knn.png", "PNG" );

   }

   /**
    *
    */
   private void testVectorQuantizer5()
   {
      // Test of the GrafixUtilities and the VectorQuantizer:
      GrafixUtilities   gu      = new GrafixUtilities();
//       RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/cattle.jpg" );
//       RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/kinect.depth.jpg" );
//       RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/kinect.color.jpg" );
      RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/kinect.depth.4.jpg" );
      RGBColorPoint[][] pixels2 = new RGBColorPoint[ pixels1.length ][ pixels1[ 0 ].length ];

      VectorQuantizer          vq      = new VectorQuantizer( "TestVQ" );
      NearestNeighborQuantizer nn      = new NearestNeighborQuantizer();
      MeanShiftQuantizer       ms      = new MeanShiftQuantizer();
      double[]                 a       = null;
      double[]                 d       = new double[ 1 ];
      long                     s       = System.currentTimeMillis();

      System.out.println( "Initializing..." );

      vq.setNumberOfClusters( 100 );
      vq.setNumberOfDimensions( 1 );
      vq.initialize();

      for( int i=0; i<pixels1.length; i++ )
      {
         for( int j=0; j<pixels1[ i ].length; j++ )
         {
            d[ 0 ] = pixels1[ i ][ j ].getRed();

            vq.addPoint( d );
         }
      }

      System.out.println( "Time to perform addPoints:                " + ( System.currentTimeMillis() - s ) );
      s = System.currentTimeMillis();

      vq.reset();

      for( int i=0; i<pixels1.length; i++ )
      {
         for( int j=0; j<pixels1[ i ].length; j++ )
         {
            d[ 0 ] = pixels1[ i ][ j ].getRed();

            vq.getCentroid( vq.getCluster( d ) );
         }
      }

      vq.pruneClusters();

      System.out.println( "Finished pruning...number of VQ clusters: " + vq.getNumberOfClusters() );
      System.out.println( "Time to perform pruning:                  " + ( System.currentTimeMillis() - s ) );
      s = System.currentTimeMillis();

      ms.setVectorQuantizer( vq );
      ms.setCDFThreshold( 0.1 );
      ms.optimize();

      System.out.println( "Finished optimizing..." );
      System.out.println( "Time to perform optimizing:               " + ( System.currentTimeMillis() - s ) );
      s = System.currentTimeMillis();

      nn.setClusters( ms.getClusters() );
      nn.setClusterPointMap( ms.getClusterPointMap() );

      System.out.println( "Number of NN clusters:                    " + nn.getNumberOfClusters() );
      System.out.println( "Time to perform NN initialization:        " + ( System.currentTimeMillis() - s ) );
      s = System.currentTimeMillis();

      int p = 0;

      for( int i=0; i<pixels2.length; i++ )
      {
         for( int j=0; j<pixels2[ i ].length; j++ )
         {
            d[ 0 ] = pixels1[ i ][ j ].getRed();

            p                 = ( nn.getCluster( d ) );
            a                 = nn.getCentroid( p );
            pixels2[ i ][ j ] = new RGBColorPoint( a[ 0 ], a[ 0 ], a[ 0 ] );
         }
      }

      System.out.println( "Time to perform NN getCluster/Centroid:   " + ( System.currentTimeMillis() - s ) );

//       gu.putRGBData( pixels2, "test-images/cattle.knn.png", "PNG" );
      gu.putRGBData( pixels2, "test-images/kinect.depth.4.knn.png", "PNG" );
//       gu.putRGBData( pixels2, "test-images/kinect.color.knn.png", "PNG" );

   }

   /**
    *
    */
   private void testVectorQuantizer6()
   {
      // Test of the GrafixUtilities and the VectorQuantizer:
      GrafixUtilities   gu      = new GrafixUtilities();
//       RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/cattle.jpg" );
//       RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/kinect.depth.jpg" );
//       RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/kinect.color.jpg" );
      RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/kinect.depth.4.jpg" );
      RGBColorPoint[][] pixels2 = new RGBColorPoint[ pixels1.length ][ pixels1[ 0 ].length ];

      VectorQuantizer          vq      = new VectorQuantizer( "TestVQ" );
      double[]                 a       = null;
      double[]                 d       = new double[ 1 ];
      long                     s       = System.currentTimeMillis();

      System.out.println( "Initializing..." );

      vq.setNumberOfClusters( 9 );
      vq.setNumberOfDimensions( 1 );
      vq.initialize();

      for( int i=0; i<pixels1.length; i++ )
      {
         for( int j=0; j<pixels1[ i ].length; j++ )
         {
            d[ 0 ] = pixels1[ i ][ j ].getRed();

            vq.addPoint( d );
         }
      }

      System.out.println( "Time to perform addPoints:                " + ( System.currentTimeMillis() - s ) );
      s = System.currentTimeMillis();

      vq.reset();

      for( int i=0; i<pixels1.length; i++ )
      {
         for( int j=0; j<pixels1[ i ].length; j++ )
         {
            d[ 0 ] = pixels1[ i ][ j ].getRed();

            vq.getCentroid( vq.getCluster( d ) );
         }
      }

      vq.pruneClusters();

      System.out.println( "Finished pruning...number of VQ clusters: " + vq.getNumberOfClusters() );
      System.out.println( "Time to perform pruning:                  " + ( System.currentTimeMillis() - s ) );
      s = System.currentTimeMillis();

      int p = 0;

      for( int i=0; i<pixels2.length; i++ )
      {
         for( int j=0; j<pixels2[ i ].length; j++ )
         {
            d[ 0 ] = pixels1[ i ][ j ].getRed();

            p                 = ( vq.getCluster( d ) );
            a                 = vq.getCentroid( p );
            pixels2[ i ][ j ] = new RGBColorPoint( a[ 0 ], a[ 0 ], a[ 0 ] );
         }
      }

      System.out.println( "Time to perform getCluster/Centroid:   " + ( System.currentTimeMillis() - s ) );

//       gu.putRGBData( pixels2, "test-images/cattle.knn.png", "PNG" );
      gu.putRGBData( pixels2, "test-images/kinect.depth.4.knn.png", "PNG" );
//       gu.putRGBData( pixels2, "test-images/kinect.color.knn.png", "PNG" );

   }
   /**
    *
    */
   private RGBColorPoint[][] getNewImageArray( int w, int h )
   {
      RGBColorPoint[][] pixels = new RGBColorPoint[ w ][ h ];

      for( int i=0; i<pixels.length; i++ )
      {
         for( int j=0; j<pixels[ i ].length; j++ )
         {
            pixels[ i ][ j ] = new RGBColorPoint( 0, 0, 0 );
         }
      }

      return pixels;
   }


   /**
    *
    */
   private void testVectorQuantizer7()
   {
      GrafixUtilities   gu      = new GrafixUtilities();
      RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/kinect.depth.4.jpg" );
      RGBColorPoint[][] pixels2 = new RGBColorPoint[ pixels1.length ][ pixels1[ 0 ].length ];

      VectorQuantizer          vq      = new VectorQuantizer( "TestVQ" );
      NearestNeighborQuantizer nn      = new NearestNeighborQuantizer();
      MeanShiftQuantizer       ms      = new MeanShiftQuantizer();
      double[]                 a       = null;
      double[]                 d       = new double[ 1 ];
      long                     s       = System.currentTimeMillis();

      System.out.println( "Initializing..." );

      vq.setNumberOfClusters( 50 );
      vq.setNumberOfDimensions( 1 );
      vq.initialize();

      for( int i=0; i<pixels1.length; i++ )
      {
         for( int j=0; j<pixels1[ i ].length; j++ )
         {
            d[ 0 ] = pixels1[ i ][ j ].getRed();
            vq.addPoint( d );
         }
      }

      System.out.println( "Time to perform addPoints:                " + ( System.currentTimeMillis() - s ) );
      s = System.currentTimeMillis();

      vq.reset();

      for( int i=0; i<pixels1.length; i++ )
      {
         for( int j=0; j<pixels1[ i ].length; j++ )
         {
            d[ 0 ] = pixels1[ i ][ j ].getRed();
            vq.getCluster( d );
         }
      }

      vq.pruneClusters();

      System.out.println( "Finished pruning...number of VQ clusters: " + vq.getNumberOfClusters() );
      System.out.println( "Time to perform pruning:                  " + ( System.currentTimeMillis() - s ) );
      s = System.currentTimeMillis();

      ms.setVectorQuantizer( vq );
      ms.setCDFThreshold( 0.1 );
      ms.optimize();

      System.out.println( "Finished optimizing..." );
      System.out.println( "Time to perform optimizing:               " + ( System.currentTimeMillis() - s ) );
      s = System.currentTimeMillis();

      nn.setClusters( ms.getClusters() );
      nn.setClusterPointMap( ms.getClusterPointMap() );

      System.out.println( "Number of NN clusters:                    " + nn.getNumberOfClusters() );
      System.out.println( "Time to perform NN initialization:        " + ( System.currentTimeMillis() - s ) );
      s = System.currentTimeMillis();

      int                                        p               = 0;
      int[]                                      point           = null;
      HashMap< Integer, ArrayList< Integer[] > > clusterPointMap = new HashMap< Integer, ArrayList< Integer[] > >();

      for( int i=0; i<pixels2.length; i++ )
      {
         for( int j=0; j<pixels2[ i ].length; j++ )
         {
            d[ 0 ]            = pixels1[ i ][ j ].getRed();
            p                 = nn.getCluster( d ); 
            a                 = nn.getCentroid( p );
            pixels2[ i ][ j ] = new RGBColorPoint( a[ 0 ], a[ 0 ], a[ 0 ] );
            point             = new int[ 2 ];
            point[ 0 ]        = i;
            point[ 1 ]        = j;
            putPoint( clusterPointMap, point, p );
         }
      }

      System.out.println( "Time to perform NN getCluster/Centroid:   " + ( System.currentTimeMillis() - s ) );

      gu.putRGBData( pixels2, "test-images/kinect.depth.4.knn.png", "PNG" );

      Set< Integer > keys = clusterPointMap.keySet();
      for( Integer cluster : keys )
      {
         System.out.println( "A cluster: " + cluster );
         ArrayList< Integer[] > points = clusterPointMap.get( cluster );
         vq                            = new VectorQuantizer( "TestVQ" );
         nn                            = new NearestNeighborQuantizer();
         ms                            = new MeanShiftQuantizer();

         vq.setNumberOfClusters( 50 );
         vq.setNumberOfDimensions( 2 );
         vq.initialize();

         for( Integer[] pp : points )
         {
            a = new double[ 2 ];
            a[ 0 ] = (double) pp[ 0 ] / (double) pixels2.length;
            a[ 1 ] = (double) pp[ 1 ] / (double) pixels2[ 0 ].length;
            vq.addPoint( a );
         }
         vq.reset();
         for( Integer[] pp : points )
         {
            a = new double[ 2 ];
            a[ 0 ] = (double) pp[ 0 ] / (double) pixels2.length;
            a[ 1 ] = (double) pp[ 1 ] / (double) pixels2[ 0 ].length;
            vq.getCluster( a );
         }
         vq.pruneClusters();

         ms.setVectorQuantizer( vq );
         ms.setCDFThreshold( 0.2 );
         ms.optimize();

         nn.setClusters( ms.getClusters() );
         nn.setClusterPointMap( ms.getClusterPointMap() );

         HashMap< Integer, ArrayList< Double[] > > objectPointsMap = new HashMap< Integer, ArrayList< Double[] > >();

         pixels2 = getNewImageArray( pixels2.length, pixels2[ 0 ].length );
         for( Integer[] pp : points )
         {
            a = new double[ 2 ];
            a[ 0 ] = (double) pp[ 0 ] / (double) pixels2.length;
            a[ 1 ] = (double) pp[ 1 ] / (double) pixels2[ 0 ].length;
            p      = nn.getCluster( a ); 
            putPoint( objectPointsMap, a, p );
            pixels2[ pp[ 0 ] ][ pp[ 1 ] ] = new RGBColorPoint( p * 50, 0, 0 );
         }

         gu.putRGBData( pixels2, "test-images/kinect.depth.4.knn.cluster-" + cluster + ".png", "PNG" );
      }


   }

   /**
    *
    */
   private void testVectorQuantizer8()
   {
      GrafixUtilities   gu      = new GrafixUtilities();
      MathUtilities     mu      = new MathUtilities();
//       RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/linear2.jpg" );
//       RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/kinect.depth.4.jpg" );
      RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/oval.jpg" );
      RGBColorPoint[][] pixels2 = new RGBColorPoint[ pixels1.length ][ pixels1[ 0 ].length ];

      double[][]               hilbertMatrix = new double[ pixels1.length ][ pixels1[ 0 ].length ];
      VectorQuantizer          vq      = new VectorQuantizer( "TestVQ" );
      double[]                 a       = null;
      double                   d       = 0;
      long                     s       = System.currentTimeMillis();

      System.out.println( "Initializing..." );

      for( int k=0; k<10; k++ )
      {
         for( int i=0; i<pixels1.length; i++ )
         {
            for( int j=0; j<pixels1[ i ].length; j++ )
            {
               hilbertMatrix[ i ][ j ] = pixels1[ i ][ j ].getRed();
            }
         }

         double[]   hilbertArray     = mu.getSMAHilbertArray( hilbertMatrix, 0.2 );
         double[][] smaHilbertMatrix = mu.getHilbertMatrix( hilbertArray, pixels2.length, pixels2[ 0 ].length );

         for( int i=0; i<pixels2.length; i++ )
         {
            for( int j=0; j<pixels2[ i ].length; j++ )
            {
               if( smaHilbertMatrix[ i ][ j ] > 0.001 )
               {
                  pixels2[ i ][ j ] = new RGBColorPoint( 0, 0, 0 );
               }
               else
               {
                  pixels2[ i ][ j ] = new RGBColorPoint( pixels1[ i ][ j ].getRed(), 
                                                         pixels1[ i ][ j ].getGreen(), 
                                                         pixels1[ i ][ j ].getBlue() );
   //                if( pixels2[ i ][ j ].getRed() > 0.0 )
   //                {
   //                   System.out.println( i + " " + j + " -> " + pixels2[ i ][ j ].getRed() );
   //                }
               }
   //             pixels2[ i ][ j ] = new RGBColorPoint( smaHilbertMatrix[ i ][ j ], 
   //                                                    smaHilbertMatrix[ i ][ j ], 
   //                                                    smaHilbertMatrix[ i ][ j ] );
            }
         }
         pixels1 = pixels2;
      }

      gu.putRGBData( pixels2, "test-images/oval-hilbert.png", "PNG" );
//       gu.putRGBData( pixels2, "test-images/kinect.depth.4-hilbert.png", "PNG" );
//       gu.putRGBData( pixels2, "test-images/linear2-hilbert.png", "PNG" );

   }

   /**
    *
    */
   private void testVectorQuantizer9()
   {
      GrafixUtilities   gu      = new GrafixUtilities();
      MathUtilities     mu      = new MathUtilities();
      StatUtilities     su      = new StatUtilities();
      RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/non-linear2.jpg" );
//       RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/linear2.jpg" );
//       RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/kinect.depth.4.jpg" );
//       RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/oval.jpg" );
      RGBColorPoint[][]     pixels2 = new RGBColorPoint[ pixels1.length ][ pixels1[ 0 ].length ];
      ArrayList< int[] > lastPoints = null;
      ArrayList< int[] > points     = null;

      int[]                    a       = null;
      double                   d       = 0.2;
      long                     s       = System.currentTimeMillis();
      boolean                  b       = true;

//       System.out.println( "Initializing..." );

      while( b )
      {
         System.out.println( "looping..." );
         b                         = false;
         pixels2                   = new RGBColorPoint[ pixels1.length ][ pixels1[ 0 ].length ];
         lastPoints                = points;
         points                    = new ArrayList< int[] >();

         for( int i=0; i<pixels2.length; i++ )
         {
            for( int j=0; j<pixels2[ i ].length; j++ )
            {
               if( hasBlackNeighbor( pixels1, i, j, d ) )
               {
                  // 0.015 is RGB 1
//                   pixels2[ i ][ j ] = new RGBColorPoint( pixels1[ i ][ j ].getRed()   / 2, 
//                                                          pixels1[ i ][ j ].getGreen() / 2, 
//                                                          pixels1[ i ][ j ].getBlue()  / 2 );
                  pixels2[ i ][ j ] = new RGBColorPoint( 0, 0, 0 );
               }
               else
               {
                  pixels2[ i ][ j ] = new RGBColorPoint( pixels1[ i ][ j ].getRed(), 
                                                         pixels1[ i ][ j ].getGreen(), 
                                                         pixels1[ i ][ j ].getBlue() );
               }
               if( pixels2[ i ][ j ].getRedComponent() > 0 )
               {
//                   System.out.println( "found a non-zero entry..." );
                  b      = true;
                  a      = new int[ 2 ];
                  a[ 0 ] = i;
                  a[ 1 ] = j;
                  points.add( a );
               }
            }
         }
         pixels1 = pixels2;
         System.out.println( "looped..." );
      }

//       System.out.println( "lastPoints count: " + lastPoints.size() );
      double[] xlist = new double[ lastPoints.size() ];
      double[] ylist = new double[ lastPoints.size() ];
      int      pos   = 0;
      int      x     = 0;
      int      y     = 0;
      ArrayList< Integer[] > save = new ArrayList< Integer[] >();
      Integer[]              an   = null;

      for( int[] point : lastPoints )
      {
         an = new Integer[ 2 ];
         an[ 0 ] = new Integer( point[ 0 ] );
         an[ 1 ] = new Integer( point[ 1 ] );
         save.add( an );
      }

      for( int[] point : lastPoints )
      {
//          System.out.println( "   " + point[ 0 ] + " " + point[ 1 ] );
         xlist[ pos ] = new Double( point[ 0 ] );
         ylist[ pos ] = new Double( point[ 1 ] );
         pos++;
      }
      x                 = (int) su.quickMedian( xlist );
      y                 = (int) su.quickMedian( ylist );
      a[ 0 ]            = x;
      a[ 1 ]            = y;
      double   min      = Double.MAX_VALUE;
      int[]    minPoint = new int[ 2 ];
      int[]    other    = new int[ 2 ];
//       System.out.println( "" );
      for( Integer[] p : save )
      {
         other[ 0 ] = p[ 0 ];
         other[ 1 ] = p[ 1 ];
         d = mu.getEuclideanDistance( other, a );
//          System.out.println( "dist: " + d );
         if( d < min )
         {
            min           = d;
            minPoint[ 0 ] = other[ 0 ];
            minPoint[ 1 ] = other[ 1 ];
         }
      }
      System.out.println( "last point: " + minPoint[ 0 ] + " " + minPoint[ 1 ] );

      int[][]    clusterTags = new int[ pixels2.length ][ pixels2[ 0 ].length ];
      double[][] data        = new double[ pixels2.length ][ pixels2[ 0 ].length ];
      pixels1                = gu.getRGBData( "test-images/non-linear2.jpg" );
//       pixels1                = gu.getRGBData( "test-images/linear2.jpg" );

      for( int i=0; i<pixels1.length; i++ )
      {
         for( int j=0; j<pixels1[ i ].length; j++ )
         {
            data[ i ][ j ] = pixels1[ i ][ j ].getRed();
         }
      }

      ArrayDeque< Integer[] > ondeck         = null;
      ArrayDeque< Integer[] > candidates     = new ArrayDeque< Integer[] >();
      int                     currentCluster = 1;
      Integer[]               begin          = new Integer[ 2 ];
      begin[ 0 ] = minPoint[ 0 ];
      begin[ 1 ] = minPoint[ 1 ];
      candidates.add( begin );

      while( candidates.size() > 0 )
      {
//          System.out.println( "candidates size: " + candidates.size() );
         ondeck = new ArrayDeque< Integer[] >();
         while( candidates.size() > 0 )
         {
            Integer[] point = candidates.poll();
            x     = point[ 0 ];
            y     = point[ 1 ];
            if( data[ x ][ y ] > 0.0 )
            {
               data       [ x ][ y ] = 0;
               clusterTags[ x ][ y ] = currentCluster;

               int xstart = ( x == 0 ) ? 0 : x - 1;
               int ystart = ( y == 0 ) ? 0 : y - 1;
               int xend   = ( x == data.length - 1 ) ? 0 : x + 2;
               int yend   = ( y == data.length - 1 ) ? 0 : y + 2;

               for( int i=xstart; i<xend; i++ )
               {
                  for( int j=ystart; j<yend; j++ )
                  {
                     if( data[ i ][ j ] > 0.0 )
                     {
                        Integer[] aa = new Integer[ 2 ];
                        aa[ 0 ] = i;
                        aa[ 1 ] = j;
                        ondeck.add( aa );
                     }
                  }
               }
            }
         }
         candidates = ondeck;
      }

      System.out.println( "time: " + ( System.currentTimeMillis() - s ) );

      // at the end, data has the first cluster erased, so it can be the start of the next overall iteration.

      pixels2 = new RGBColorPoint[ pixels1.length ][ pixels1[ 0 ].length ];
      for( int ss=0; ss<pixels2.length; ss++ )
      {
         for( int t=0; t<pixels2[ 0 ].length; t++ )
         {
            if( clusterTags[ ss ][ t ] > 0 )
            {
//                System.out.println( "cluster tag: " + ss + " " + t + " " + clusterTags[ ss ][ t ] );
               pixels2[ ss ][ t ] = pixels1[ ss ][ t ];
//                System.out.println( pixels2[ ss ][ t ].getRed() );
            }
            else
            {
               pixels2[ ss ][ t ] = new RGBColorPoint( 0, 0, 0 );
            }
         }
      }

//       gu.putRGBData( pixels2, "test-images/oval-hilbert.png", "PNG" );
//       gu.putRGBData( pixels2, "test-images/kinect.depth.4-hilbert.png", "PNG" );
//       gu.putRGBData( pixels2, "test-images/linear2-hilbert.png", "PNG" );
      gu.putRGBData( pixels2, "test-images/non-linear2-hilbert.png", "PNG" );

   }

   /**
    *
   private void testVectorQuantizer10()
   {
      GrafixUtilities              gu       = new GrafixUtilities();
      MathUtilities                mu       = new MathUtilities();
      LatticeQuantizer             lq       = new LatticeQuantizer();
      ConnectedComponentQuantizer  ccq      = new ConnectedComponentQuantizer();
      VectorQuantizer              vq       = new VectorQuantizer( "TestVQ" );
      double[]                     a        = null;
      double[]                     d        = new double[ 1 ];
      int                          c        = 0;

      vq.setNumberOfClusters( 5 );
      vq.setNumberOfDimensions( 1 );
      vq.setThreshold( 0.10 );
      vq.initialize();

//       RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/kinect.depth.4-8-2.jpg" );
//       RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/kinect.depth.4-2r.jpg" );
//       RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/kinect.depth.4-2.jpg" );
//       RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/kinect.depth.4-8.jpg" );
//       RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/non-linear4.jpg" );
//       RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/non-linear3.jpg" );
//       RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/non-linear2.jpg" );
//       RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/linear2.jpg" );
      RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/kinect.depth.4.jpg" );
//       RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/crosses.jpg" );
//       RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/oval.jpg" );
//       RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/oval2.jpg" );
//       RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/kinect.depth.clustered.jpg" );
//       RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/squares.jpg" );
//       RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/crosses1.jpg" );
      double[][]        data = new double[ pixels1.length ][ pixels1[ 0 ].length ];
      for( int i=0; i<pixels1.length; i++ )
      {
         for( int j=0; j<pixels1[ i ].length; j++ )
         {
            d[ 0 ] = pixels1[ i ][ j ].getRed();
            vq.addPoint( d );
         }
      }
      vq.reset();
      for( int i=0; i<pixels1.length; i++ )
      {
         for( int j=0; j<pixels1[ i ].length; j++ )
         {
            d[ 0 ] = pixels1[ i ][ j ].getRed();
            c = vq.getCluster( d );
            a = vq.getCentroid( c );
            data[ i ][ j ] = a[ 0 ];
            if( data[ i ][ j ] < 0.3 )
            {
               data[ i ][ j ] = 0.0;
            }
         }
      }

      long s = System.currentTimeMillis();
//       data = mu.getReducedMatrix( data, 64, 48 );
//       data = mu.getReducedMatrix( data, 128, 96 );

//       mu.makeSparseRandom( data, 0.01 );
//       mu.makeNonSparseRandom( data, 0.97, 2 );
//       mu.makeNonSparse( data, 0.7 );

//       lq.quantize( data, false );
//       lq.quantize( data, true );
      lq.quickQuantize( data, 0.2 );
//       ccq.quantize( data );
      System.out.println( "time: " + ( System.currentTimeMillis() - s ) );
      int[][] clusterTags = lq.getClusterTags();
//       int[][] clusterTags = ccq.getClusterTags();

      RGBColorPoint[][] pixels2 = new RGBColorPoint[ data.length ][ data[ 0 ].length ];
      for( int ss=0; ss<pixels2.length; ss++ )
      {
         for( int t=0; t<pixels2[ 0 ].length; t++ )
         {
            if( clusterTags[ ss ][ t ] > 0 )
            {
//                System.out.println( "cluster tag: " + ss + " " + t + " " + clusterTags[ ss ][ t ] );
//                pixels2[ ss ][ t ] = pixels1[ ss ][ t ];
               if( clusterTags[ ss ][ t ] == 1 )
               {
                  pixels2[ ss ][ t ] = new RGBColorPoint( 255, 0, 0 );
               }
               else if( clusterTags[ ss ][ t ] == 2 )
               {
                  pixels2[ ss ][ t ] = new RGBColorPoint( 0, 0, 255 );
               }
               else if( clusterTags[ ss ][ t ] == 3 )
               {
                  pixels2[ ss ][ t ] = new RGBColorPoint( 0, 255, 0 );
               }
               else if( clusterTags[ ss ][ t ] == 4 )
               {
                  pixels2[ ss ][ t ] = new RGBColorPoint( 255, 255, 0 );
               }
               else if( clusterTags[ ss ][ t ] == 5 )
               {
                  pixels2[ ss ][ t ] = new RGBColorPoint( 128, 255, 0 );
               }
               else if( clusterTags[ ss ][ t ] == 6 )
               {
                  pixels2[ ss ][ t ] = new RGBColorPoint( 255, 128, 0 );
               }
               else if( clusterTags[ ss ][ t ] == 7 )
               {
                  pixels2[ ss ][ t ] = new RGBColorPoint( 255, 128, 255 );
               }
               else
               {
                  pixels2[ ss ][ t ] = new RGBColorPoint( 255, 255, 255 );
               }
//                System.out.println( pixels2[ ss ][ t ].getRed() );
            }
            else
            {
               pixels2[ ss ][ t ] = new RGBColorPoint( 0, 0, 0 );
            }
         }
      }
//       gu.putRGBData( pixels2, "test-images/kinect.depth.4-8-2.png", "PNG" );
//       gu.putRGBData( pixels2, "test-images/kinect.depth.4-2-2r.png", "PNG" );
//       gu.putRGBData( pixels2, "test-images/kinect.depth.4-2-2.png", "PNG" );
      gu.putRGBData( pixels2, "test-images/kinect.depth.4.png", "PNG" );
//       gu.putRGBData( pixels2, "test-images/non-linear4-hilbert.png", "PNG" );
//       gu.putRGBData( pixels2, "test-images/non-linear3-hilbert.png", "PNG" );
//       gu.putRGBData( pixels2, "test-images/crosses-hilbert.png", "PNG" );
//       gu.putRGBData( pixels2, "test-images/squares-hilbert.png", "PNG" );
//       gu.putRGBData( pixels2, "test-images/oval-hilbert.png", "PNG" );
//       gu.putRGBData( pixels2, "test-images/oval2-hilbert.png", "PNG" );
//       gu.putRGBData( pixels2, "test-images/kinect.depth.clustered-hilbert.png", "PNG" );
//       gu.putRGBData( pixels2, "test-images/non-linear2-hilbert.png", "PNG" );
//       gu.putRGBData( pixels2, "test-images/linear2-hilbert.png", "PNG" );

      HashMap< Integer, ArrayList< int[] > > clusterPointMap = lq.getClusterPointMap();
//       HashMap< Integer, ArrayList< int[] > > clusterPointMap = ccq.getClusterPointMap();
      Set< Integer >                         keys            = clusterPointMap.keySet();
      double[][]                             vector          = null;
      double[]                               variable        = null;
      double[][]                             vars            = new double[ 10 ][];

      for( Integer cluster : keys )
      {
         ArrayList< int[] > points = clusterPointMap.get( cluster );
         vector                    = gu.getInvariantFeatureVector( points );
         vector                    = mu.normalize( vector, 1.0 );

         System.out.println( "Cluster: " + cluster + " size: " + points.size() );

         if( cluster == 1 )
         {
            double[] vector100 = mu.getReducedVector( vector, 2000 );
            System.err.println( "cluster 1: " );
            System.out.println( "invariant vector:" );
            for( int i=0; i<vector100.length; i++ )
            {
               System.out.println( vector100[ i ] );
            }

            for( int i=0; i<vars.length; i++ )
            {
//                vars[ 0 ] = mu.getContinuous1DVariable( vector100.length );
               vars[ i ] = mu.getRandomArray( vector100.length );
               Arrays.sort( vars[ i ] );
            }

            System.err.println( "Will Create a New ManyToOneCorrelation..." );
            System.out.println( "" );

            HaltingCriteria criteria = new HaltingCriteria();
            criteria.setElapsedTimeTolerance( mu.getTime( "5s" ) );

            ManyToOneCorrelation correlation = new ManyToOneCorrelation( "cluster1" );
            correlation.setHaltingCriteria( criteria );
            correlation.setIndependentData( vars );
            correlation.setDependentData( vector100 );
            correlation.setGenomeLength( 64 );
            correlation.train();

            double[] input = new double[ vars.length ];
            for( int i=0; i<vars[ 0 ].length; i++ )
            {
               for( int j=0; j<vars.length; j++ )
               {
                  input[ j ] = vars[ j ][ i ];
               }
               System.out.println( ( correlation.correlate( input ) )[ 0 ] );
            }
         }
         else if( cluster == 2 )
         {
//             System.err.println( "" );
//             System.err.println( "cluster 2: " );
//             System.out.println( "invariant vector before:" );
//             int l = (int) ( (double) vector.length / 100.0 );
//             for( int i=0; i<vector.length; i+=l )
//             {
//                System.out.println( vector[ i ] );
//             }
//             System.out.println( "" );
         }
      }
   }
    */

   /**
    *
    */
   private boolean hasBlackNeighbor( RGBColorPoint[][] pixels, int x, int y, double t )
   {
      int xstart = ( x == 0 ) ? 0 : x - 1;
      int ystart = ( y == 0 ) ? 0 : y - 1;
      int xend   = ( x == pixels.length - 1 ) ? 0 : x + 2;
      int yend   = ( y == pixels.length - 1 ) ? 0 : y + 2;

      for( int i=xstart; i<xend; i++ )
      {
         for( int j=ystart; j<yend; j++ )
         {
            if( pixels[ i ][ j ].getRed() <= t )
            {
               return true;
            }
         }
      }

      return false;
   }

   /**
    *
    */
   private void testImage()
   {
      GrafixUtilities   gu      = new GrafixUtilities();
      RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/kinect.depth.jpg" );
      RGBColorPoint[][] pixels2 = new RGBColorPoint[ pixels1.length ][ pixels1[ 0 ].length ];

      for( int i=0; i<pixels1.length; i++ )
      {
         pixels1[ i ][ 0 ] = new RGBColorPoint( 255, 0, 0 );
      }

      for( int i=0; i<pixels1[ 0 ].length; i++ )
      {
         pixels1[ 0 ][ i ] = new RGBColorPoint( 0, 0, 255 );
      }

      gu.putRGBData( pixels1, "test-images/kinect.depth.knn.png", "PNG" );
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void testMultiply()
   {
      MathUtilities mu = new MathUtilities();
      double[]      a  = mu.getRandomArray( 2 );
      double[][]    m  = mu.getRandomMatrix( 2, 4 );
      double[][]    s  = mu.getRandomMatrix( 4, 2 );
      double[]      r  = mu.multiply( a, m );

      System.out.println( "\nThe Array is:" );
      mu.printArray( a );

      System.out.println( "The Matrix 1 is:" );
      mu.printMatrix( m );

      System.out.println( "The Result Array is:" );
      mu.printArray( r );

      System.out.println( "The Matrix 2 is:" );
      mu.printMatrix( s );
      s = mu.multiply( s, m );

      System.out.println( "The Result Matrix is:" );
      mu.printMatrix( s );
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void testBit()
   {
      long       id = Long.MAX_VALUE;
      BigInteger bi = BigInteger.valueOf( 1 );
      BigInteger xi = null;

      for( int i=0; i<64; i++ )
      {
         bi = bi.shiftLeft( 1 );
         id = id >> 1;
         xi = BigInteger.valueOf( id );
         System.out.println( "id: " + id );
         System.out.println( "bi: " + bi );
         System.out.println( "bi ^ xi: " + bi.xor( xi ) );
         System.out.println( "i: " + i );
      }

      System.out.println( "23 ^ 34: " + ( 23 ^ 34 ) );
      BigInteger cl = new BigInteger( bi.toByteArray() );
      System.out.println( "cl is: " + cl + " bi is: " + bi );
      System.out.println( "cl == bi is: " + ( cl == bi ) );
      System.out.println( "cl.compareTO( bi ) is: " + ( cl.compareTo( bi ) ) );
   }

   /**
    * Green: 2 220 2
    * Blue:  0 136 254
    * @param TYPE
    * @return TYPE
    */
   public void findCircles()
   {
      GrafixUtilities   gu      = new GrafixUtilities();
//       RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/ingress.jpg" );
      RGBColorPoint[][] pixels1 = gu.getRGBData( "test-images/ingress.bmp" );
      RGBColorPoint     green   = new RGBColorPoint( 2, 220, 2 );
      RGBColorPoint     green2  = new RGBColorPoint( 3, 220, 3 );
      RGBColorPoint     blue    = new RGBColorPoint( 0, 136, 254 );
      double[][]        semi    = null;

      for( int i=0; i<pixels1.length; i++ )
      {
         for( int j=0; j<pixels1[ i ].length; j++ )
         {
            for( int r=5; r<12; r++ )
            {
               if(    ( i - r ) >= 0
                   && ( i + r ) <  pixels1.length
                   && ( j - r ) >= 0 
                   && ( j + r ) <  pixels1[ i ].length )
               {
                  // if a cross pattern matches:
                  if(    pixels1[ i - r   ][ j        ].equals( green2 )
                      && pixels1[ i + r   ][ j        ].equals( green2 ) 
                      && pixels1[ i       ][ j - r    ].equals( green2 ) 
                      && pixels1[ i       ][ j + r    ].equals( green2 )
                      )
                  {
                     int c = 0;
                     semi = getSemiCircleData( ( double )r, 16 );
                     for( int s=0; s<semi.length; s++ )
                     {
//                         if(    pixels1[ ( i + ( int )Math.round( semi[ s ][ 0 ] ) ) ][ ( j - ( int )Math.round( semi[ s ][ 1 ] ) ) ].equals( green2 )
//                             && pixels1[ ( i + ( int )Math.round( semi[ s ][ 0 ] ) ) ][ ( j + ( int )Math.round( semi[ s ][ 1 ] ) ) ].equals( green2 )
//                             && pixels1[ ( i - ( int )Math.round( semi[ s ][ 0 ] ) ) ][ ( j - ( int )Math.round( semi[ s ][ 1 ] ) ) ].equals( green2 )
//                             && pixels1[ ( i - ( int )Math.round( semi[ s ][ 0 ] ) ) ][ ( j + ( int )Math.round( semi[ s ][ 1 ] ) ) ].equals( green2 ) )
//                         {
//                            c++;
//                         }
                        if( pixels1[ ( i + ( int )Math.round( semi[ s ][ 0 ] ) ) ][ ( j - ( int )Math.round( semi[ s ][ 1 ] ) ) ].equals( green2 ) )
                        {
                           c++;
                        }
                        if( pixels1[ ( i + ( int )Math.round( semi[ s ][ 0 ] ) ) ][ ( j + ( int )Math.round( semi[ s ][ 1 ] ) ) ].equals( green2 ) )
                        {
                           c++;
                        }
                        if( pixels1[ ( i - ( int )Math.round( semi[ s ][ 0 ] ) ) ][ ( j - ( int )Math.round( semi[ s ][ 1 ] ) ) ].equals( green2 ) )
                        {
                           c++;
                        }
                        if( pixels1[ ( i - ( int )Math.round( semi[ s ][ 0 ] ) ) ][ ( j + ( int )Math.round( semi[ s ][ 1 ] ) ) ].equals( green2 ) )
                        {
                           c++;
                        }
                     }
                     if( c > 60 )
                     {
                        System.out.println( "potential: i: " + i + " j: " + j + " c: " + c );
                     }
                  }
               }
            }
         }
      }

      for( int i=0; i<pixels1.length; i++ )
      {
         for( int j=0; j<pixels1[ j ].length; j++ )
         {
//             if( !pixels1[ i ][ j ].equals( green ) )
            if( !pixels1[ i ][ j ].equals( green2 ) )
            {
               pixels1[ i ][ j ] = new RGBColorPoint( 0, 0, 0 );
            }
         }
      }
//       gu.putRGBData( pixels1, "ingress.green.jpg", "JPG" );
      gu.putRGBData( pixels1, "ingress.green.bmp", "BMP" );
   }

   /**
    *
    */
   private void testImageChannel()
   {
      GrafixUtilities   gu     = new GrafixUtilities();
      RGBColorPoint[][] pixels = gu.getRGBData2( "Apiatan.jpg" );

      for( int i=0; i<pixels.length; i++ )
      {
         for( int j=0; j<pixels[ i ].length; j++ )
         {
            pixels[ i ][ j ].setRed( 0.0 );
            pixels[ i ][ j ].setGreen( 0.0 );
         }
      }

      gu.putRGBData2( pixels, "Apiatan.blue.bmp", "BMP" );
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void run()
   {
      System.out.println( "in the shudown hook...." );
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void prototype()
   {
      Runtime.getRuntime().addShutdownHook( new Thread( this ) );
//       testMTOC();
//       testMTOC3();
      testMTOC4();
//       testImage();
//       testMultiply();
//       testVectorQuantizer();
//       testVectorQuantizer2();
//       testVectorQuantizer3();
//       testVectorQuantizer4();
//       testVectorQuantizer5();
//       testVectorQuantizer6();
//       testVectorQuantizer7();
//       testVectorQuantizer8();
//       testVectorQuantizer9();
//       testVectorQuantizer10();
//       testBit();
//       findCircles();
//       powerball();
//       testImageChannel();
   }

   /**
    *
    */
   private double[][] getSemiCircleData( double radius, int n )
   {
      double[][] data = new double[ n ][ 2 ];
      double     PI_2 = Math.PI / 2.0;
      double     step = PI_2 / ( double )n;
      double     arg  = 0.0;

      data[ 0 ][ 0 ] = radius;
      data[ 0 ][ 1 ] = 0.0;

      for( int k=1; k<n; k++ )
      {
         arg += step;
         data[ k ][ 0 ] = radius * Math.cos( arg );
         data[ k ][ 1 ] = radius * Math.sin( arg );
      }

      return data;
   }

   /**
    *
    * 0 : word is: Draw Date
    * 1 : word is: WB1
    * 2 : word is: WB2
    * 3 : word is: WB3
    * 4 : word is: WB4
    * 5 : word is: WB5
    * 6 : word is: PB
    * 7 : word is: PP
    * 0 : word is: 01/13/2016
    * 1 : word is: 08
    * 2 : word is: 27
    * 3 : word is: 34
    * 4 : word is: 04
    * 5 : word is: 19
    * 6 : word is: 10
    * 7 : word is: 2
    *
    */
   private void powerball()
   {
      FileReader                 f  = new FileReader( "powerball-2.csv", "," );
      Vector< String >           v  = f.getVectorOfWords();
      TreeMap< String, Integer > t0 = new TreeMap< String, Integer >();
      TreeMap< String, Integer > t1 = new TreeMap< String, Integer >();
      TreeMap< String, Integer > t2 = new TreeMap< String, Integer >();
      TreeMap< String, Integer > t3 = new TreeMap< String, Integer >();
      TreeMap< String, Integer > t4 = new TreeMap< String, Integer >();
      TreeMap< String, Integer > t5 = new TreeMap< String, Integer >();
      TreeMap< String, Integer > t6 = new TreeMap< String, Integer >();
      int                        c  = 0;
      int                        e  = 0;
      Integer                    n  = 0;

      for( String w : v )
      {
         e = ( c++ % 6 );
         n = t0.get( w );

         if( n != null )
         {
            t0.put( w, ( n + 1 ) );
         }
         else
         {
            t0.put( w, 1 );
         }
         switch( e )
         {
            case 0 :
               n = t1.get( w );
               if( n != null )
               {
                  t1.put( w, ( n + 1 ) );
               }
               else
               {
                  t1.put( w, 1 );
               }
            break;
            case 1 :
               n = t2.get( w );
               if( n != null )
               {
                  t2.put( w, ( n + 1 ) );
               }
               else
               {
                  t2.put( w, 1 );
               }
            break;
            case 2 :
               n = t3.get( w );
               if( n != null )
               {
                  t3.put( w, ( n + 1 ) );
               }
               else
               {
                  t3.put( w, 1 );
               }
            break;
            case 3 :
               n = t4.get( w );
               if( n != null )
               {
                  t4.put( w, ( n + 1 ) );
               }
               else
               {
                  t4.put( w, 1 );
               }
            break;
            case 4 :
               n = t5.get( w );
               if( n != null )
               {
                  t5.put( w, ( n + 1 ) );
               }
               else
               {
                  t5.put( w, 1 );
               }
            break;
            case 5 :
               n = t6.get( w );
               if( n != null )
               {
                  t6.put( w, ( n + 1 ) );
               }
               else
               {
                  t6.put( w, 1 );
               }
            break;
         }
      }
       
      System.out.println( "\nAll Data******************************************" );
      for( String key : t0.keySet() )
      {
         System.out.println( key + "," + t0.get( key ) );
      }
       
      System.out.println( "\nPosition 1 Data******************************************" );
      for( String key : t1.keySet() )
      {
         System.out.println( key + "," + t1.get( key ) );
      }
       
      System.out.println( "\nPosition 2 Data******************************************" );
      for( String key : t2.keySet() )
      {
         System.out.println( key + "," + t2.get( key ) );
      }
       
      System.out.println( "\nPosition 3 Data******************************************" );
      for( String key : t3.keySet() )
      {
         System.out.println( key + "," + t3.get( key ) );
      }
       
      System.out.println( "\nPosition 4 Data******************************************" );
      for( String key : t4.keySet() )
      {
         System.out.println( key + "," + t4.get( key ) );
      }
       
      System.out.println( "\nPosition 5 Data******************************************" );
      for( String key : t5.keySet() )
      {
         System.out.println( key + "," + t5.get( key ) );
      }
       
      System.out.println( "\nPosition 6 Data******************************************" );
      for( String key : t6.keySet() )
      {
         System.out.println( key + "," + t6.get( key ) );
      }
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public static void main( String[] args )
   {
      Prototype p = new Prototype();
      p.prototype();
   }

}
