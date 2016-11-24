package openkinect;

import java.awt.*;
import java.math.*;
import java.util.*;

import common.*;
import grafix.*;
import math.*;
import math.cluster.*;
import math.cluster.VectorQuantizer.*;
import openkinect.domain.*;
import openkinect.forest.*;
import openkinect.forest.DecisionForest.*;

/**
 *
 */
public class ColorImageManager implements Runnable
{

   private DataDeque< RGBColorPoint[][] >      _dataDeque;
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
   public ColorImageManager()
   {
      initialize();
   }

   /**
    *
    */
   public ColorImageManager( String                              name,
                             DataDeque< RGBColorPoint[][] >      d, 
                             int                                 n, 
                             String                              train, 
                             String                              prune,
                             int                                 forestSize,
                             int                                 treeSplitSize,
                             int                                 treeGenomeLength,
                             double                              vectorDiff, 
                             DataDeque< ArrayList< Integer[] > > depthMaskDeque )
   {
      _name             = name + ".CIM";
      _dataDeque        = d;
      _numberOfClusters = n;
      _forestSize       = forestSize;
      _treeSplitSize    = treeSplitSize;
      _treeGenomeLength = treeGenomeLength;
      _vectorDiff       = vectorDiff;
      _depthMaskDeque   = depthMaskDeque;

      if( "on".equalsIgnoreCase( train ) )
      {
         System.out.println( "Color cluster training is ON." );
         _train = true;
      }
      else
      {
         System.out.println( "Color cluster training is OFF." );
         _train = false;
      }
      if( "on".equalsIgnoreCase( prune ) )
      {
         System.out.println( "Color cluster pruning is ON." );
         _prune = true;
      }
      else
      {
         System.out.println( "Color cluster pruning is OFF." );
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
         _vq.setNumberOfDimensions( 3 );
         _vq.setThreshold( 0.10 );
         _vq.initialize();
      }

      System.out.println( "Color VectorQuantizer: " + _vq );

      if( _prune )
      {
         System.out.println( "Prune is on so will reset CIM.VQ counts." );
         _vq.reset();
      }

      _decisionForest = ( DecisionForest ) ( new DecisionForest( _name + ".DF" ) ).getDAO().deserialize();
      if( _decisionForest == null )
      {
         _decisionForest = new DecisionForest( _name + ".DF", _forestSize, _vectorDiff, _treeSplitSize );
         _decisionForest.setTreeGenomeLength( _treeGenomeLength );
      }

      System.out.println( "CIM Decision Forest: " + _decisionForest );
   }

   /**
    *
    */
   private ArrayList< ImageObject > getImageObjects()
   {
      return null;
   }

   /**
    *
    */
   public void run()
   {
      RGBColorPoint[][]                                image           = null;
      RGBColorPoint[][]                                data            = null;
      double[]                                         point           = new double[ 3 ];
      double[]                                         vector          = null;
      double[]                                         nvector         = null;
      double[]                                         rvector         = null;
      BigInteger[]                                     classes         = null;
      String[]                                         addresses       = null;
      double[][]                                       nearestFrame    = null;
      double[][]                                       featureVector   = null;
      int                                              cluster         = -1;
      int                                              red             = -1;
      int                                              count           = 0;
      int                                              objectCount     = 0;
      long                                             s               = -1;
      HashMap< Integer, ArrayList< int[] > >           clusterPointMap = null;
      HashMap< RGBColorPoint, ArrayList< Integer[] > > colors          = new HashMap< RGBColorPoint, ArrayList< Integer[] > >();
      ImageObject                                      imageObject     = null;

      while( _canProcess )
      {
         // need to sleep for a bit to give time for the depth mask deque to be populated.
         try { Thread.sleep( 10 ); } catch( InterruptedException e ) { }

         s                           = System.currentTimeMillis();
         data                        = _dataDeque.poll();
         ArrayList< Integer[] > mask = _depthMaskDeque.poll();
         int parallax                = 30;
         int adjusted                = 0;

         colors.clear();

         if( data == null || mask == null )
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

         for( int i=0; i<image.length; i++ )
         {
            for( int j=0; j<image[ i ].length; j++ )
            {
               image[ i ][ j ] = new RGBColorPoint( 0, 0, 0 );;
            }
         }
         for( Integer[] maskPoint : mask )
         {
            adjusted = maskPoint[ 0 ] - parallax;
            if( adjusted < 0 )
            {
               adjusted = 0;
            }
            image[ adjusted ][ maskPoint[ 1 ] ] = new RGBColorPoint( -1.0, -1.0, -1.0 );;
         }

         for( int i=0; i<data.length; i++ )
         {
            for( int j=0; j<data[ 0 ].length; j++ )
            {
               if( image[ i ][ j ].getRed() == -1 )
               {
                  point[ 0 ] = data[ i ][ j ].getRed();
                  point[ 1 ] = data[ i ][ j ].getGreen();
                  point[ 2 ] = data[ i ][ j ].getBlue();

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
                  image[ i ][ j ]        = new RGBColorPoint( point[ 0 ], point[ 1 ], point[ 2 ] );
                  // DEBUG <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                  add( colors, image[ i ][ j ], i, j );
               }
               nearestFrame[ i ][ j ] = point[ 0 ];
            }
         }

         // DEBUG >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
         _gu.putRGBData( image, "test-images/kinect.color.clustered.png", "PNG" );
         // DEBUG <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

         // What we will do here is that we won't even use the lattice quantizer on the color image data
         // because it doesn't really buy us anything when you think about it -- no sense in finding all
         // the objects and then creating an invariant vector from that, only to do the same thing all over
         // again with the other color objects from the different colors.

         for( RGBColorPoint c : colors.keySet() )
         {
            System.out.println( "a color: Red: " + c.getRedComponent() + " Green: " + c.getGreenComponent() + " Blue: " + c.getBlueComponent() );

            ArrayList< Integer[] > list = colors.get( c );

            featureVector  = _gu.getInvariantFeatureVector( getPrimitiveArrayList( list ) );
            vector         = featureVector[ 1 ];
            nvector        = _mu.normalize( vector, 1.0 );
            rvector        = _mu.getReducedVector( nvector, _objectMinSize );
            classes        = _decisionForest.classify( rvector );
            addresses      = _decisionForest.getTreeAddresses();
            imageObject    = new ImageObject( featureVector[ 0 ][ 0 ], featureVector[ 0 ][ 1 ], classes, addresses );

            // DEBUG >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            System.out.println( "CIM object " + ( ++objectCount ) + " ---> " + imageObject );
            // DEBUG <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
         }

         System.out.println( "CIM Time: " + ( System.currentTimeMillis() - s ) );
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
    */
   private void add( HashMap< RGBColorPoint, ArrayList< Integer[] > > map, RGBColorPoint c, int i, int j )
   {
      Integer[] a = new Integer[ 2 ];
      a[ 0 ]      = new Integer( i );
      a[ 1 ]      = new Integer( j );

      ArrayList< Integer[] > list = map.get( c );

      if( list == null )
      {
         list = new ArrayList< Integer[] >();
         map.put( c, list );
      }

      list.add( a );
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

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public ArrayList< int[] > getPrimitiveArrayList( ArrayList< Integer[] > points )
   {
      ArrayList< int[] > list = new ArrayList< int[] >( points.size() );

      for( Integer[] point : points )
      {
         int[] a = new int[ 2 ];
         a[ 0 ]  = point[ 0 ];
         a[ 1 ]  = point[ 1 ];
         list.add( a );
      }

      return list;
   }

}
