package math.cluster;

import java.util.*;
import common.*;
import math.*;

/**
 * MeanShiftQuantizer.
 * 
 * This class is intended to use the VectorQuantizer class' output as the input for this algorithm.
 * The idea is that a large number of initial random clusters will be generated in the initialization
 * of VectorQuantizer. Then, it will quantize those clusters based on the input vectors. This quantized
 * set of vector clusters can then be used as the fodder for this mean shift algorithm.
 * 
 *   1) Based on the VectorQuantizer object, determine the median distance between the clusters,
 *      and the threshold percentile limit for which the mean shift window.
 *   2) while each cluster update is greater than the threshold do:
 *   3)    for each cluster do:
 *   4)       find its neighbors within the mean shift window
 *   5)       determine the center of mass of all these neighbors
 *   6)       set the cluster update to this new point
 *   7) determine how many clusters there are
 *   8) label each original cluster based on the clusters created
 *   9) use these data points and clusters in a kNN algorithm...
 *
 * NOTE: The following describes how to use this class:
 *       1) Use the VectorQuantizer class to add all the points (addPoint)
 *       2) Call "reset" on the VectorQuantizer to clear the counts
 *       3) Use the "getCluster" on all the points (or as many as possible)
 *       4) Create the MeanShiftQuantizer, set the VectorQuantizer and call optimize
 *   The VectorQuantizer is used in this manner because in practice, there will be too many
 *   data points to keep track of -- the VectorQuantizer helps in keeping a "summarized" 
 *   or "clusterized" data list that captures the essence of the myriad data points.
 *
 */
public class MeanShiftQuantizer implements Persistable
{

   private String                                    _name;
   private VectorQuantizer                           _vectorQuantizer;
   private MathUtilities                             _util;
   private StatUtilities                             _stat;
   private double                                    _windowDistance;
   private double                                    _cdfThreshold;
   private int                                       _numberOfDimensions;
   private ArrayList< double[] >                     _points;
   private ArrayList< double[] >                     _clusters;
   private double[][]                                _pointDistances;
   private int[]                                     _membership;
   private HashMap< Integer, ArrayList< double[] > > _clusterPointMap;

   /**
    *
    */
   public MeanShiftQuantizer()
   {
   }

   /**
    *
    */
   public MeanShiftQuantizer( String name )
   {
      setName( name );
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public static void main( String[] args )
   {
      MeanShiftQuantizer    ms      = new MeanShiftQuantizer();
      VectorQuantizer       vq      = new VectorQuantizer();
      MathUtilities         mu      = new MathUtilities();
      double[]              p       = null;
      double[]              pp      = null;

      vq.setNumberOfClusters( 10000 );
      vq.setNumberOfDimensions( 2 );
      vq.initialize();

      ArrayList< double[] > random = new ArrayList< double[] >();
      double                step   = 0.05;
      double                arg    = 0.0;

      // non-linearly separable data set
      while( arg <= Math.PI )
      {
         p      = new double[ 2 ];
         p[ 0 ] = ( Math.cos( arg ) / 4 ) + 0.6;
         p[ 1 ] = ( Math.sin( arg ) / 2 ) + 0.5;
//          System.out.println( p[ 0 ] + ", " + p[ 1 ] );
         random.add( p );
//          vq.addPoint( p );

         pp      = new double[ 2 ];
         pp[ 0 ] = p[ 0 ] - 0.3;
         pp[ 1 ] = ( 1 - p[ 1 ] ) + 0.3;
//          System.out.println( pp[ 0 ] + ", " + pp[ 1 ] );
         random.add( pp );
//          vq.addPoint( pp );

         arg += step;
      }

      Collections.shuffle( random );

      for( double[] d : random )
      {
         vq.addPoint( d );
      }

      // example of just random data points:
//       ArrayList< double[] > random = new ArrayList< double[] >();
      int                   l      = 100;
      for( int i=0; i<l; i++ )
      {
         p = new double[ 2 ];
         p[ 0 ] = mu.random();
         p[ 1 ] = mu.random();
         random.add( p );
//          vq.addPoint( p );
      }
//       vq.reset();
//       for( int i=0; i<l; i++ )
//       {
//          p = random.get( i );
//          vq.getCluster( p );
//       }

      vq.reset();
      for( double[] d : random )
      {
         vq.getCluster( d );
      }

      /*
      p = new double[ 2 ];
      p[ 0 ] = 0.22;
      p[ 1 ] = 0.51 ;
      vq.addPoint( p );
      System.out.println( p[ 0 ] + ", " + p[ 1 ] );
      p = new double[ 2 ];
      p[ 0 ] = 0.234;
      p[ 1 ] = 0.434;
      vq.addPoint( p );
      System.out.println( p[ 0 ] + ", " + p[ 1 ] );
      p = new double[ 2 ];
      p[ 0 ] = 0.22;
      p[ 1 ] = 0.68 ;
      vq.addPoint( p );
      System.out.println( p[ 0 ] + ", " + p[ 1 ] );
      p = new double[ 2 ];
      p[ 0 ] = 0.22;
      p[ 1 ] = 0.59 ;
      vq.addPoint( p );
      System.out.println( p[ 0 ] + ", " + p[ 1 ] );
      p = new double[ 2 ];
      p[ 0 ] = 0.25;
      p[ 1 ] = 0.38 ;
      vq.addPoint( p );
      System.out.println( p[ 0 ] + ", " + p[ 1 ] );
      p = new double[ 2 ];
      p[ 0 ] = 0.251;
      p[ 1 ] = 0.375;
      vq.addPoint( p );
      System.out.println( p[ 0 ] + ", " + p[ 1 ] );
      p = new double[ 2 ];
      p[ 0 ] = 0.3;
      p[ 1 ] = 0.77 ;
      vq.addPoint( p );
      System.out.println( p[ 0 ] + ", " + p[ 1 ] );
      p = new double[ 2 ];
      p[ 0 ] = 0.3;
      p[ 1 ] = 0.4  ;
      vq.addPoint( p );
      System.out.println( p[ 0 ] + ", " + p[ 1 ] );
      p = new double[ 2 ];
      p[ 0 ] = 0.35;
      p[ 1 ] = 0.375;
      vq.addPoint( p );
      System.out.println( p[ 0 ] + ", " + p[ 1 ] );
      p = new double[ 2 ];
      p[ 0 ] = 0.36;
      p[ 1 ] = 0.77 ;
      vq.addPoint( p );
      System.out.println( p[ 0 ] + ", " + p[ 1 ] );
      p = new double[ 2 ];
      p[ 0 ] = 0.375;
      p[ 1 ] = 0.79 ;
      vq.addPoint( p );
      System.out.println( p[ 0 ] + ", " + p[ 1 ] );
      p = new double[ 2 ];
      p[ 0 ] = 0.4;
      p[ 1 ] = 0.875;
      vq.addPoint( p );
      System.out.println( p[ 0 ] + ", " + p[ 1 ] );
      p = new double[ 2 ];
      p[ 0 ] = 0.48;
      p[ 1 ] = 0.88 ;
      vq.addPoint( p );
      System.out.println( p[ 0 ] + ", " + p[ 1 ] );
      p = new double[ 2 ];
      p[ 0 ] = 0.38;
      p[ 1 ] = 0.379;
      vq.addPoint( p );
      System.out.println( p[ 0 ] + ", " + p[ 1 ] );
      p = new double[ 2 ];
      p[ 0 ] = 0.42;
      p[ 1 ] = 0.42 ;
      vq.addPoint( p );
      System.out.println( p[ 0 ] + ", " + p[ 1 ] );
      p = new double[ 2 ];
      p[ 0 ] = 0.43;
      p[ 1 ] = 0.18 ;
      vq.addPoint( p );
      System.out.println( p[ 0 ] + ", " + p[ 1 ] );
      p = new double[ 2 ];
      p[ 0 ] = 0.41;
      p[ 1 ] = 0.6  ;
      vq.addPoint( p );
      System.out.println( p[ 0 ] + ", " + p[ 1 ] );
      p = new double[ 2 ];
      p[ 0 ] = 0.47;
      p[ 1 ] = 0.625;
      vq.addPoint( p );
      System.out.println( p[ 0 ] + ", " + p[ 1 ] );
      p = new double[ 2 ];
      p[ 0 ] = 0.48;
      p[ 1 ] = 0.23 ;
      vq.addPoint( p );
      System.out.println( p[ 0 ] + ", " + p[ 1 ] );
      p = new double[ 2 ];
      p[ 0 ] = 0.521;
      p[ 1 ] = 0.2  ;
      vq.addPoint( p );
      System.out.println( p[ 0 ] + ", " + p[ 1 ] );
      p = new double[ 2 ];
      p[ 0 ] = 0.53;
      p[ 1 ] = 0.68 ;
      vq.addPoint( p );
      System.out.println( p[ 0 ] + ", " + p[ 1 ] );
      p = new double[ 2 ];
      p[ 0 ] = 0.59;
      p[ 1 ] = 0.25 ;
      vq.addPoint( p );
      System.out.println( p[ 0 ] + ", " + p[ 1 ] );
      p = new double[ 2 ];
      p[ 0 ] = 0.61;
      p[ 1 ] = 0.25 ;
      vq.addPoint( p );
      System.out.println( p[ 0 ] + ", " + p[ 1 ] );
      p = new double[ 2 ];
      p[ 0 ] = 0.625;
      p[ 1 ] = 0.625;
      vq.addPoint( p );
      System.out.println( p[ 0 ] + ", " + p[ 1 ] );
      p = new double[ 2 ];
      p[ 0 ] = 0.61;
      p[ 1 ] = 0.7  ;
      vq.addPoint( p );
      System.out.println( p[ 0 ] + ", " + p[ 1 ] );
      p = new double[ 2 ];
      p[ 0 ] = 0.628;
      p[ 1 ] = 0.5  ;
      vq.addPoint( p );
      System.out.println( p[ 0 ] + ", " + p[ 1 ] );
      p = new double[ 2 ];
      p[ 0 ] = 0.63;
      p[ 1 ] = 0.6  ;
      vq.addPoint( p );
      System.out.println( p[ 0 ] + ", " + p[ 1 ] );
      p = new double[ 2 ];
      p[ 0 ] = 0.61;
      p[ 1 ] = 0.375;
      vq.addPoint( p );
      System.out.println( p[ 0 ] + ", " + p[ 1 ] );
      p = new double[ 2 ];
      p[ 0 ] = 0.68;
      p[ 1 ] = 0.61 ;
      vq.addPoint( p );
      System.out.println( p[ 0 ] + ", " + p[ 1 ] );
      p = new double[ 2 ];
      p[ 0 ] = 0.69;
      p[ 1 ] = 0.58 ;
      vq.addPoint( p );
      System.out.println( p[ 0 ] + ", " + p[ 1 ] );
      p = new double[ 2 ];
      p[ 0 ] = 0.65;
      p[ 1 ] = 0.72 ;
      vq.addPoint( p );
      System.out.println( p[ 0 ] + ", " + p[ 1 ] );
      p = new double[ 2 ];
      p[ 0 ] = 0.25;
      p[ 1 ] = 0.75 ;
      vq.addPoint( p );
      System.out.println( p[ 0 ] + ", " + p[ 1 ] );

      // do this to prune:
      vq.reset();
      p[ 0 ] = 0.22;
      p[ 1 ] = 0.51 ;
      vq.getCluster( p );
      p[ 0 ] = 0.234;
      p[ 1 ] = 0.434;
      vq.getCluster( p );
      p[ 0 ] = 0.22;
      p[ 1 ] = 0.68 ;
      vq.getCluster( p );
      p[ 0 ] = 0.22;
      p[ 1 ] = 0.59 ;
      vq.getCluster( p );
      p[ 0 ] = 0.25;
      p[ 1 ] = 0.38 ;
      vq.getCluster( p );
      p[ 0 ] = 0.251;
      p[ 1 ] = 0.375;
      vq.getCluster( p );
      p[ 0 ] = 0.3;
      p[ 1 ] = 0.77 ;
      vq.getCluster( p );
      p[ 0 ] = 0.3;
      p[ 1 ] = 0.4  ;
      vq.getCluster( p );
      p[ 0 ] = 0.35;
      p[ 1 ] = 0.375;
      vq.getCluster( p );
      p[ 0 ] = 0.36;
      p[ 1 ] = 0.77 ;
      vq.getCluster( p );
      p[ 0 ] = 0.375;
      p[ 1 ] = 0.79 ;
      vq.getCluster( p );
      p[ 0 ] = 0.4;
      p[ 1 ] = 0.875;
      vq.getCluster( p );
      p[ 0 ] = 0.48;
      p[ 1 ] = 0.88 ;
      vq.getCluster( p );
      p[ 0 ] = 0.38;
      p[ 1 ] = 0.379;
      vq.getCluster( p );
      p[ 0 ] = 0.42;
      p[ 1 ] = 0.42 ;
      vq.getCluster( p );
      p[ 0 ] = 0.43;
      p[ 1 ] = 0.18 ;
      vq.getCluster( p );
      p[ 0 ] = 0.41;
      p[ 1 ] = 0.6  ;
      vq.getCluster( p );
      p[ 0 ] = 0.47;
      p[ 1 ] = 0.625;
      vq.getCluster( p );
      p[ 0 ] = 0.48;
      p[ 1 ] = 0.23 ;
      vq.getCluster( p );
      p[ 0 ] = 0.521;
      p[ 1 ] = 0.2  ;
      vq.getCluster( p );
      p[ 0 ] = 0.53;
      p[ 1 ] = 0.68 ;
      vq.getCluster( p );
      p[ 0 ] = 0.59;
      p[ 1 ] = 0.25 ;
      vq.getCluster( p );
      p[ 0 ] = 0.61;
      p[ 1 ] = 0.25 ;
      vq.getCluster( p );
      p[ 0 ] = 0.625;
      p[ 1 ] = 0.625;
      vq.getCluster( p );
      p[ 0 ] = 0.61;
      p[ 1 ] = 0.7  ;
      vq.getCluster( p );
      p[ 0 ] = 0.628;
      p[ 1 ] = 0.5  ;
      vq.getCluster( p );
      p[ 0 ] = 0.63;
      p[ 1 ] = 0.6  ;
      vq.getCluster( p );
      p[ 0 ] = 0.61;
      p[ 1 ] = 0.375;
      vq.getCluster( p );
      p[ 0 ] = 0.68;
      p[ 1 ] = 0.61 ;
      vq.getCluster( p );
      p[ 0 ] = 0.69;
      p[ 1 ] = 0.58 ;
      vq.getCluster( p );
      p[ 0 ] = 0.65;
      p[ 1 ] = 0.72 ;
      vq.getCluster( p );
      p[ 0 ] = 0.25;
      p[ 1 ] = 0.75 ;
      vq.getCluster( p );
      */

      vq.pruneClusters();

      ms.setVectorQuantizer( vq );
      ms.setCDFThreshold( 0.3 );
      ms.optimize();

      // just print out stuff:
      ArrayList< double[] > clusters = ms.getClusters();
      System.out.println( "\nThe clusters are: " );
      ms.printList( clusters );

      System.out.println( "\nMemberships: " );

      HashMap< Integer, ArrayList< double[] > > clusterPointMap = ms.getClusterPointMap();
      Set< Integer > k = clusterPointMap.keySet();
      for( Integer key : k )
      {
         ArrayList< double[] > pointList = clusterPointMap.get( key );
         System.out.println( "\nPoints for cluster: " + key );
         ms.printList( pointList );
      }
   }

   /**
    *
    */
   public class MeanShiftQuantizerDAO extends DAOBase< MeanShiftQuantizer >
   {
   }

   /**
    *
    * @return ManyToOneCorrelationDAO -- The impl class of DAOBase type.
    */
   public DAOBase getDAO()
   {
      DAOBase dao = new MeanShiftQuantizerDAO();

      if( getName() != null )
      {
         dao.setFileName( getName() + ".ser" );
      }

      return dao;
   }

   /**
    *
    * @param String
    */
   public void setName( String name )
   {
      _name = name;
   }

   /**
    *
    * @return String
    */
   public String getName()
   {
      return _name;
   }

   /**
    *
    * @param VectorQuantizer
    */
   public void setVectorQuantizer( VectorQuantizer vectorQuantizer )
   {
      _vectorQuantizer = vectorQuantizer;
   }

   /**
    *
    * @return VectorQuantizer
    */
   public VectorQuantizer getVectorQuantizer()
   {
      return _vectorQuantizer;
   }

   /**
    *
    * @param double
    */
   public void setWindowDistance( double windowDistance )
   {
      _windowDistance = windowDistance;
   }

   /**
    *
    * @return double
    */
   public double getWindowDistance()
   {
      return _windowDistance;
   }

   /**
    *
    * @param double
    */
   public void setCDFThreshold( double cdfThreshold )
   {
      _cdfThreshold = cdfThreshold;
   }

   /**
    *
    * @return double
    */
   public double getCDFThreshold()
   {
      return _cdfThreshold;
   }

   /**
    * Assumes optimize has already been called.
    * @return HashMap< Integer, ArrayList< double[] > > _clusterPointMap
    */
   public HashMap< Integer, ArrayList< double[] > > getClusterPointMap()
   {
      if( _clusterPointMap == null )
      {
         determineClusterPointMap();
      }
      return _clusterPointMap;
   }

   /**
    *
    * @return ArrayList< double[] > _clusters
    */
   public ArrayList< double[] > getClusters()
   {
      return _clusters;
   }

   /**
    *
    */
   public ArrayList< double[] > getPoints()
   {
      return _points;
   }

   /**
    *
    * @return double[][]
    */
   public double[][] getPointDistances()
   {
      return _pointDistances;
   }

   /**
    *
    * @return int[]
    */
   public int[] getMembership()
   {
      return _membership;
   }

   /**
    *
    */
   private void initialize()
   {
      ArrayList< double[] > points           = _vectorQuantizer.getClusters();
      double[][]            clusterDistances = _vectorQuantizer.getClusterDistances();
      double[]              cdf              = _vectorQuantizer.getDistanceCDF();
      int                   pos              = (int) ( (int) cdf.length * _cdfThreshold );
      _windowDistance                        = cdf[ pos ];
      _points                                = new ArrayList< double[] >( points.size() );
      _pointDistances                        = new double[ clusterDistances.length ][ clusterDistances[ 0 ].length ];
      _util                                  = new MathUtilities();
      _stat                                  = new StatUtilities();
      _membership                            = new int[ points.size() ];
      _cdfThreshold                          = 0.3;
      _numberOfDimensions                    = -1;

      // we need to make a deep copy since we will be modifying it and needing the original later:
      for( double[] p : points )
      {
         if( _numberOfDimensions == -1 )
         {
            _numberOfDimensions = p.length;
         }
         double[] d = new double[ p.length ];
         for( int k=0; k<p.length; k++ )
         {
            d[ k ] = p[ k ];
         }
         _points.add( d );
      }
      for( int r=0; r<clusterDistances.length; r++ )
      {
         for( int s=0; s<clusterDistances[ r ].length; s++ )
         {
            _pointDistances[ r ][ s ] = clusterDistances[ r ][ s ];
         }
      }
   }

   /**
    * This will re-calculate the distance from this centroid to all the others.
    * @param TYPE
    * @return TYPE
    */
   public void calculateDistance( int n )
   {
      double[] x = null;
      double[] y = _points.get( n );
      double   d = 0;

      for( int i=0; i<_points.size(); i++ )
      {
         if( i == n )
         {
            _pointDistances[ i ][ n ] = 0;
            _pointDistances[ n ][ i ] = 0;
         }
         else
         {
            x                         = _points.get( i );
            d                         = _util.getEuclideanDistance( x, y );
            _pointDistances[ i ][ n ] = d;
            _pointDistances[ n ][ i ] = d;
         }
      }
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public ArrayList< double[] > getNearestNeighbors( int n )
   {
      ArrayList< double[] >       list = new ArrayList< double[] >();
      TreeMap< Double, double[] > map  = new TreeMap< Double, double[] >();

      for( int i=0; i<_pointDistances.length; i++ )
      {
         if( _pointDistances[ n ][ i ] <= _windowDistance )
         {
//             list.add( _points.get( i ) );
            map.put( _pointDistances[ n ][ i ], _points.get( i ) );
         }
      }

      Set< Double > keys = map.descendingKeySet();
      for( Double key : keys )
      {
         list.add( map.get( key ) );
      }

      return list;
   }

   /**
    *
    */
   private double[] getCenterOfMass( ArrayList< double[] > points )
   {
      double[] com = new double[ _numberOfDimensions ];
//       double[] v   = new double[ points.size() ];

      for( int k=0; k<_numberOfDimensions; k++ )
      {
         com[ k ] = ( points.get( 0 ) )[ k ];
         for( int r=1; r<points.size(); r++ )
         {
//             v[ r ] = ( points.get( r ) )[ k ];
            com[ k ] += ( 0.2 * ( ( points.get( r )[ k ] ) - com[ k ] ) );
         }
//          _stat.calculateStats( v );
//          com[ k ] = _stat.getMean();
//          com[ k ] = _stat.quickMedian( v );
      }

      return com;
   }

   /**
    *
    */
   public void printList( ArrayList< double[] > list )
   {
      for( double[] obj : list )
      {
         System.out.println( "" );
         printPoint( obj );
      }
   }

   /**
    *
    */
   private void printPoint( double[] p )
   {
      for( int t=0; t<p.length; t++ )
      {
         System.out.print( p[ t ] + ", " );
      }
   }

   /**
    *
    */
   private void addPointToMap( double[] p, int i )
   {
      ArrayList< double[] > l = _clusterPointMap.get( i );
      if( l == null )
      {
         l = new ArrayList< double[] >();
         _clusterPointMap.put( new Integer( i ), l );
      }
      l.add( p );
   }

   /**
    *
    */
   private void determineClusterPointMap()
   {
      ArrayList< double[] > points  = _vectorQuantizer.getClusters();
      double[]              p       = null;
      _clusterPointMap              = new HashMap< Integer, ArrayList< double[] > >();

      for( int r=0; r<points.size(); r++ )
      {
         p = points.get( r );
         addPointToMap( p, _membership[ r ] );
      }
   }

   /**
    *
    */
   private void determineClusters()
   {
      _clusters   = new ArrayList< double[] >();
      double[]  c = new double[ _numberOfDimensions ];
      double[]  p = _points.get( 0 );
      double    d = -1.0;
      int       z = -1;

      for( int i=0; i<_numberOfDimensions; i++ )
      {
         c[ i ] = p[ i ];
      }

      _clusters.add( c );
      _membership[ 0 ] = 0;

      for( int j=1; j<_points.size(); j++ )
      {
         p = _points.get( j );
         z = -1;

         for( int k=0; k<_clusters.size(); k++ )
         {
            c = _clusters.get( k );
            d = _util.getEuclideanDistance( p, c );
            if( d < 0.01 )
            {
               z                = k;
               _membership[ j ] = z;
               break;
            }
         }

         if( z == -1 )
         {
            c = new double[ _numberOfDimensions ];
            for( int i=0; i<_numberOfDimensions; i++ )
            {
               c[ i ] = p[ i ];
            }
            _clusters.add( c );
            _membership[ j ] = _clusters.size() - 1;
         }
      }
   }

   /**
    *
    */
   private void recalculateWindow()
   {
      double[]              cdf              = new double[ _pointDistances.length * _pointDistances.length ];
      int                   pos              = (int) ( (int) cdf.length * _cdfThreshold );
      int                   p                = 0;

      for( int i=0; i<_pointDistances.length; i++ )
      {
         for( int j=0; j<_pointDistances.length; j++ )
         {
            cdf[ p++ ] = _pointDistances[ i ][ j ];;
         }
      }

      Arrays.sort( cdf );
      _windowDistance = cdf[ pos ];
   }

   /**
    * Optimizse.
    *   1) Based on the VectorQuantizer object, determine the median distance between the clusters,
    *      and the threshold percentile limit for which the mean shift window.
    *   2) while each cluster update is greater than the threshold do:
    *   3)    for each cluster do:
    *   4)       find its neighbors within the mean shift window
    *   5)       determine the center of mass of all these neighbors
    *   6)       set the cluster update to this new point
    *   7) determine how many clusters there are
    *   8) label each original cluster based on the clusters created
    *   9) use these data points and clusters in a kNN algorithm...
    */
   public void optimize()
   {
      long                        start     = 0;
      double                      update    = 0;
      double                      updateSum = 10;
      double                      threshold = 1e-3;
      double[]                    com       = null;
      double[]                    current   = null;
      ArrayList< double[] >       nn        = null;

      initialize();

      while( updateSum > threshold )
      {
         updateSum = 0;

         for( int i=0; i<_points.size(); i++ )
         {
            current    = _points.get( i );
            nn         = getNearestNeighbors( i );
            com        = getCenterOfMass( nn );
            update     = _util.getEuclideanDistance( com, current );
            updateSum += update;

            _points.set( i, com );
            calculateDistance( i );
         }
      }

      determineClusters();
   }

}
