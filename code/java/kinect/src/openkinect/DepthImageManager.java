package openkinect;

import java.util.*;
import java.math.*;
import java.awt.*;

import grafix.*;
import common.*;
import math.*;
import math.cluster.*;
import math.cluster.VectorQuantizer.*;
import openkinect.forest.*;
import openkinect.forest.DecisionForest.*;
import openkinect.domain.*;

/**
 *
 */
public class DepthImageManager implements Runnable
{

   private DataDeque< int[][] >                _dataDeque;
   private DataDeque< ArrayList< Integer[] > > _depthMaskDeque;
   private boolean                             _canProcess       = true;
   private boolean                             _train            = false;
   private boolean                             _prune            = false;
   private int                                 _numberOfClusters = 5;
   private int                                 _objectMinSize    = 2000;
   private int                                 _forestSize       = 2;
   private int                                 _treeSplitSize    = 2;
   private int                                 _treeGenomeLength = 41;
   private double                              _vectorDiff       = 2.0;
   private GrafixUtilities                     _gu               = new GrafixUtilities();
   private MathUtilities                       _mu               = new MathUtilities();
   private VectorQuantizer                     _vq               = null;
   private LatticeQuantizer                    _lq               = null;
   private ConnectedComponentQuantizer         _ccq              = null;
   private String                              _name             = null;
   private DecisionForest                      _decisionForest   = null;

   /**
    *
    */
   public DepthImageManager()
   {
      initialize();
   }

   /**
    *
    */
   public DepthImageManager( String                              name,
                             DataDeque< int[][] >                d, 
                             int                                 n, 
                             String                              train, 
                             String                              prune,
                             int                                 forestSize,
                             int                                 treeSplitSize,
                             int                                 treeGenomeLength,
                             double                              vectorDiff, 
                             DataDeque< ArrayList< Integer[] > > depthMaskDeque )
   {
      _name             = name + ".DIM";
      _dataDeque        = d;
      _numberOfClusters = n;
      _forestSize       = forestSize;
      _treeSplitSize    = treeSplitSize;
      _treeGenomeLength = treeGenomeLength;
      _vectorDiff       = vectorDiff;
      _depthMaskDeque   = depthMaskDeque;

      if( "on".equalsIgnoreCase( train ) )
      {
         System.out.println( "Depth cluster training is ON." );
         _train = true;
      }
      else
      {
         System.out.println( "Depth cluster training is OFF." );
         _train = false;
      }
      if( "on".equalsIgnoreCase( prune ) )
      {
         System.out.println( "Depth cluster pruning is ON." );
         _prune = true;
      }
      else
      {
         System.out.println( "Depth cluster pruning is OFF." );
         _prune = false;
      }
      initialize();
   }

   /**
    *
    */
   private void initialize()
   {
      _vq  = ( VectorQuantizer ) ( new VectorQuantizer( _name + ".VQ" ) ).getDAO().deserialize();
      _lq  = new LatticeQuantizer();

      if( _vq == null )
      {
         _vq = new VectorQuantizer( _name + ".VQ" );
         _vq.setNumberOfClusters( _numberOfClusters );
         _vq.setNumberOfDimensions( 1 );
         _vq.setThreshold( 0.10 );
         _vq.initialize();
      }

      System.out.println( "VectorQuantizer: " + _vq );

      if( _prune )
      {
         System.out.println( "Prune is on so will reset VQ counts." );
         _vq.reset();
      }

      _decisionForest = ( DecisionForest ) ( new DecisionForest( _name + ".DF" ) ).getDAO().deserialize();
      if( _decisionForest == null )
      {
         _decisionForest = new DecisionForest( _name + ".DF", _forestSize, _vectorDiff, _treeSplitSize );
         _decisionForest.setTreeGenomeLength( _treeGenomeLength );
      }

      System.out.println( "Decision Forest: " + _decisionForest );
   }

   /**
    *
    */
   public void run()
   {
      RGBColorPoint[][]                      image           = null;
      int[][]                                data            = null;
      double[]                               point           = new double[ 1 ];
      double[]                               vector          = null;
      double[]                               nvector         = null;
      double[]                               rvector         = null;
      BigInteger[]                           classes         = null;
      String[]                               addresses       = null;
      double[][]                             nearestFrame    = null;
      double[][]                             featureVector   = null;
      double                                 mm              = 0.3;
      double                                 ss              = 1 - mm;
      double                                 aa              = Math.log( 2000 );
      double                                 bb              = Math.log( 640 * 480 ) - aa;
      double                                 cc              = 0;
      double                                 dd              = 0;
      int                                    cluster         = -1;
      int                                    red             = -1;
      int                                    count           = 0;
      int                                    objectCount     = 0;
      long                                   s               = -1;
      TreeSet< Double >                      centroids       = new TreeSet< Double >();
      Set< Integer >                         clusters        = null;
      ArrayList< int[] >                     points          = null;
      HashMap< Integer, ArrayList< int[] > > clusterPointMap = null;
      ImageObject                            imageObject     = null;

      while( _canProcess )
      {
         s    = System.currentTimeMillis();
         data = _dataDeque.poll();

         centroids.clear();

         if( data == null )
         {
            continue;
         }

         if( nearestFrame == null )
         {
            // DEBUG >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            image        = new RGBColorPoint[ data.length ][ data[ 0 ].length ];
            // DEBUG <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
            nearestFrame = new double[ data.length ][ data[ 0 ].length ];
         }

         for( int i=0; i<data.length; i++ )
         {
            for( int j=0; j<data[ 0 ].length; j++ )
            {
               red        = ( new Color( data[ i ][ j ] ) ).getRed();
               point[ 0 ] = new Double( ( double ) red / 255d );         // now mapped into [0, 1]

               if( _train )
               {
                  cluster = _vq.addPoint( point );
               }
               else
               {
                  cluster = _vq.getCluster( point );
               }

               point                  = _vq.getCentroid( cluster );
               // DEBUG >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
               image[ i ][ j ]        = new RGBColorPoint( point[ 0 ], point[ 0 ], point[ 0 ] );
               // DEBUG <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
               nearestFrame[ i ][ j ] = point[ 0 ];

               centroids.add( point[ 0 ] );
            }
         }

         // DEBUG >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
         _gu.putRGBData( image, "test-images/kinect.depth.clustered.png", "PNG" );
         // DEBUG <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

         point[ 0 ] = centroids.pollLast();
         count      = 0;

         for( int i=0; i<data.length; i++ )
         {
            for( int j=0; j<data[ 0 ].length; j++ )
            {
               if( nearestFrame[ i ][ j ] != point[ 0 ] )
               {
                  nearestFrame[ i ][ j ] = 0.0;
               }
               else
               {
                  count++;
               }
            }
         }

         cc = Math.log( count );
         dd = ( ( 1 - ( ( cc - aa ) / bb ) ) * ss ) + mm;
         dd = ( dd > 1.0 ) ? 1.0 : dd;
         dd = ( dd < mm  ) ? mm  : dd;

         _lq.setClusterPointMap( null );
         _lq.quickQuantize( nearestFrame, dd );

         clusterPointMap = _lq.getClusterPointMap();
         clusters        = clusterPointMap.keySet();
         points          = null;
         objectCount     = 0;

         for( Integer c : clusters )
         {
            points = clusterPointMap.get( c );
            if( points.size() < _objectMinSize )
            {
               continue;
            }

            // DEBUG >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            for( int i=0; i<image.length; i++ )
            {
               for( int j=0; j<image[ 0 ].length; j++ )
               {
                  image[ i ][ j ] = new RGBColorPoint( 0, 0, 0 );
               }
            }
            for( int[] p : points )
            {
               image[ p[ 0 ] ][ p[ 1 ] ] = new RGBColorPoint( point[ 0 ], point[ 0 ], point[ 0 ] );
            }
            _gu.putRGBData( image, "test-images/kinect.depth.object." + c + ".png", "PNG" );
            // DEBUG <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

            // Here, we can put the data on the color dequeue before we start recognizing the object;
            // the bulk of the work has been done at this point, so there's no need for the color
            // manager to duplicate that calculation.
            ArrayList< Integer[] > ip = new ArrayList< Integer[] >();
            for( int[] p : points )
            {
               Integer[] a = new Integer[ 2 ];
               a[ 0 ]      = new Integer( p[ 0 ] );
               a[ 1 ]      = new Integer( p[ 1 ] );
               ip.add( a );
            }
            _depthMaskDeque.add( ip );

//             System.out.println( "size of mask deque in image manager: " + _depthMaskDeque.size() );

            featureVector  = _gu.getInvariantFeatureVector( points );
            vector         = featureVector[ 1 ];
            nvector        = _mu.normalize( vector, 1.0 );
            rvector        = _mu.getReducedVector( nvector, _objectMinSize );
            classes        = _decisionForest.classify( rvector );
            addresses      = _decisionForest.getTreeAddresses();
            imageObject    = new ImageObject( featureVector[ 0 ][ 0 ], featureVector[ 0 ][ 1 ], classes, addresses );

            // DEBUG >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            System.out.println( "DIM object " + ( ++objectCount ) + " ---> " + imageObject );
            // DEBUG <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
         }
         System.out.println( "Time: " + ( System.currentTimeMillis() - s ) );
//          System.gc();
//          try { Thread.sleep( 5 ); } catch( InterruptedException e ) { }
      }

      if( _prune )
      {
         _vq.pruneClusters();
      }

      VectorQuantizerDAO vqDAO = ( VectorQuantizerDAO ) _vq.getDAO();
      vqDAO.serialize( _vq );
      DecisionForestDAO dfDAO = ( DecisionForestDAO ) _decisionForest.getDAO();
      dfDAO.serialize( _decisionForest );
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
   public void setNumberOfClusters( int numberOfClusters )
   {
      _numberOfClusters = numberOfClusters;
   }

   /**
    *
    * @return int
    */
   public int getNumberOfClusters()
   {
      return _numberOfClusters;
   }

}
