package openkinect;

import java.util.*;

import common.*;
import grafix.*;

/**
 *
 */
public class KinectDriver implements Runnable
{

   private int                                 _stackDepth;
   private int                                 _colorStackDepth;
   private int                                 _numberOfClusters;
   private int                                 _numberOfColorClusters;
   private int                                 _width;
   private int                                 _height;
   private int                                 _treeGenomeLength;
   private int                                 _colorTreeGenomeLength;
   private int                                 _treeSplitSize;
   private int                                 _colorTreeSplitSize;
   private int                                 _forestSize;
   private double                              _treeVectorDiff;
   private double                              _colorTreeVectorDiff;
   private KinectManager                       _kinectManager      = null;
   private DepthImageManager                   _depthManager       = null;
   private ColorImageManager                   _colorManager       = null;
   private Thread                              _managerThread      = null;
   private Thread                              _depthManagerThread = null;
   private Thread                              _colorManagerThread = null;
   private String                              _accelerometer      = "off";
   private String                              _depthTrain         = "off";
   private String                              _colorTrain         = "off";
   private String                              _depthPrune         = "off";
   private String                              _colorPrune         = "off";
   private String                              _systemName         = "DEFAULT";
   private DataDeque< int[][] >                _depthDeque;
   private DataDeque< RGBColorPoint[][] >      _colorDeque;
   private DataDeque< ArrayList< Integer[] > > _depthMaskDeque;

   /**
    *
    */
   public KinectDriver()
   {
      loadConfigurationProperties();
      Runtime.getRuntime().addShutdownHook( new Thread( this ) );
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void process()
   {
      _depthDeque         = new DataDeque< int[][] >( _stackDepth );
      _colorDeque         = new DataDeque< RGBColorPoint[][] >( _colorStackDepth );
      _depthMaskDeque     = new DataDeque< ArrayList< Integer[] > >( _stackDepth );
      _kinectManager      = new KinectManager( _width, _height, _accelerometer, _depthDeque, _colorDeque );
      _depthManager       = new DepthImageManager( _systemName,
                                                   _depthDeque, 
                                                   _numberOfClusters, 
                                                   _depthTrain, 
                                                   _depthPrune,
                                                   _forestSize,
                                                   _treeSplitSize,
                                                   _treeGenomeLength,
                                                   _treeVectorDiff, 
                                                   _depthMaskDeque );
      _colorManager       = new ColorImageManager( _systemName,
                                                   _colorDeque, 
                                                   _numberOfColorClusters, 
                                                   _colorTrain, 
                                                   _colorPrune,
                                                   _forestSize,
                                                   _colorTreeSplitSize,
                                                   _colorTreeGenomeLength,
                                                   _colorTreeVectorDiff, 
                                                   _depthMaskDeque );
      _managerThread      = new Thread( _kinectManager );
      _depthManagerThread = new Thread( _depthManager );
      _colorManagerThread = new Thread( _colorManager );

      _managerThread.start();
      _depthManagerThread.start();
      _colorManagerThread.start();

      try
      {
         System.out.println( "waiting to join thread (hit enter to join)..." );
         Scanner s = new Scanner( System.in );
         s.nextLine();
         _kinectManager.setCanProcess( false );
         _depthManager.setCanProcess( false );
         _colorManager.setCanProcess( false );
         _managerThread.join();
         _depthManagerThread.join();
         _colorManagerThread.join();
      }
      catch( InterruptedException e )
      {
         System.err.println( "InterruptedException: " + e );
      }
      System.out.println( "joined manager threads..." );
   }

   /**
    * Loads up all the configuration items from the properties file.
    */
   private void loadConfigurationProperties()
   {
      Properties props = PropertiesReader.getInstance().getProperties( "kinect.properties" );

      try
      {
         _numberOfClusters      = Integer.parseInt( (String) props.get( "kinect.depth.clusters" ) );
         _numberOfColorClusters = Integer.parseInt( (String) props.get( "kinect.color.clusters" ) );
         _stackDepth            = Integer.parseInt( (String) props.get( "depth.stack.depth" ) );
         _colorStackDepth       = Integer.parseInt( (String) props.get( "color.stack.depth" ) );
         _width                 = Integer.parseInt( (String) props.get( "kinect.image.width" ) );
         _height                = Integer.parseInt( (String) props.get( "kinect.image.height" ) );
         _treeGenomeLength      = Integer.parseInt( (String) props.get( "depth.forest.tree.classifier.genome.length" ) );
         _colorTreeGenomeLength = Integer.parseInt( (String) props.get( "color.forest.tree.classifier.genome.length" ) );
         _treeSplitSize         = Integer.parseInt( (String) props.get( "depth.forest.tree.split.size" ) );
         _colorTreeSplitSize    = Integer.parseInt( (String) props.get( "color.forest.tree.split.size" ) );
         _forestSize            = Integer.parseInt( (String) props.get( "forest.size" ) );
         _treeVectorDiff        = Double.parseDouble( (String) props.get( "depth.forest.tree.vector.difference" ) );
         _colorTreeVectorDiff   = Double.parseDouble( (String) props.get( "color.forest.tree.vector.difference" ) );
         _accelerometer         = (String) props.get( "kinect.accelerometer" );
         _depthTrain            = (String) props.get( "kinect.depth.train" );
         _colorTrain            = (String) props.get( "kinect.color.train" );
         _depthPrune            = (String) props.get( "kinect.depth.prune" );
         _colorPrune            = (String) props.get( "kinect.color.prune" );
         _systemName            = (String) props.get( "kinect.system.name" );
      }
      catch( NumberFormatException e )
      {
         System.err.println( "Unable to load properties file!" );
      }
   }

   /**
    * Handles Shutdown Tasks.
    */
   public void run()
   {
      System.out.println( "Performing shutdown tasks..." );
      _kinectManager.setCanProcess( false );
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public static void main( String[] args )
   {
      KinectDriver kd = new KinectDriver();
      kd.process();
      System.out.println( "exiting main..." );
   }

}
