package math.cluster;

import java.util.*;
import common.*;
import math.*;

/**
 * VectorQuantizer.
 * This class provides the functionality to "cluster" or perform vector quantization on data points.
 * We use the algorithm as described by Ray Kurzweil in his book "How to Create a Mind."
 * The algorithm is as follows:
 *   1)   Get the number of clusters from the user.
 *   2)   Get the number of dimensions from the user.
 *   3)   Initialize a uniformly-spaced set of clusters in the space and calculate the distances between.
 *   4)   Given a new data point find it's closest cluster using Euclidean metric.
 *   5)   If this distance is the smallest distance between clusters, then return cluster.
 *   6)   Otherwise, make data point the centroid of a new cluster and merge the smallest
 *           two clusters by averaging them together into one cluster.
 *
 *
 */
public class VectorQuantizer implements Persistable
{

   private String                     _name;
   private int                        _numberOfClusters;
   private int                        _numberOfDimensions;
   private int                        _x;
   private int                        _y;
   private long                       _totalCount;
   private double                     _smallestDistance;
   private double                     _threshold          = 0.01;
   private double[][]                 _clusterDistances;
   private int[]                      _clusterCounts;
   private ArrayList< double[] >      _clusters;
   private MathUtilities              _util               = new MathUtilities();
   private TreeMap< Double, Integer > _centroidClusterMap = null;

   /**
    *
    */
   public VectorQuantizer()
   {
   }

   /**
    *
    */
   public VectorQuantizer( String name )
   {
      setName( name );
   }

   /**
    * In this case, the _numberOfClusters variable is per dimension.
    */
   private void initializeLattice()
   {
      _clusters       = new ArrayList< double[] >();
      double stepSize = 1.0 / _numberOfClusters;
      double offset   = stepSize / 2.0;
      int[]  v        = new int[ _numberOfDimensions ];
      int    c        = 0;
      
      recursiveLoop( v, c, stepSize, offset );
   }

   /**
    *
    */
   private void recursiveLoop( int[] vars, int current, double stepSize, double offset )
   {
      System.out.println( "current is: " + current );
      System.out.println( "vars[ 0 ] is: " + vars[ 0 ] );
      System.out.println( "vars[ 1 ] is: " + vars[ 1 ] );
      System.out.println( "vars[ 2 ] is: " + vars[ 2 ] );

      if( current == _numberOfDimensions )
      {
         return;
      }

      for( ; vars[ current ]<_numberOfClusters; vars[ current ]++ )
      {
         System.out.println( "current: " + current + " vars[ current ]: " + vars[ current ] );
         recursiveLoop( vars, ++current, stepSize, offset );
         if( current == _numberOfDimensions )
         {
            break;
         }
      }

   }

   /**
    *
    */
   private void initializeRandom()
   {
      _clusters = new ArrayList< double[] >();

      for( int i=0; i<_numberOfClusters; i++ )
      {
         double[] a = new double[ _numberOfDimensions ];
         for( int j=0; j<_numberOfDimensions; j++ )
         {
            a[ j ] = ( _util.random() );
         }
         _clusters.add( a );
      }
   }

   /**
    *
    * @param  TYPE
    * @return TYPE
    */
   public void initialize()
   {
      _clusterDistances = new double[ _numberOfClusters ][ _numberOfClusters ];
      _clusterCounts    = new int[ _numberOfClusters ];
      initializeRandom();
      calculateDistances();
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public String toString()
   {
      String r = "\n" + getName() + ":\n\n";

      for( double[] centroid : _clusters )
      {
         for( int i=0; i<centroid.length; i++ )
         {
            r += ( centroid[ i ] + "," );
         }
         r += "\n";
      }

      for( int i=0; i<_clusterCounts.length; i++ )
      {
         r += "\n" + ( "Cluster: " + i + " count: " + _clusterCounts[ i ] );
      }

      r += ( "\n\nNumber of clusters: " + _numberOfClusters );
      r += ( "\nTotal count: "          + _totalCount       + "\n" );

      return r;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void calculateMinimumDistance()
   {
      _smallestDistance = Double.MAX_VALUE;

      for( int i=0; i<_clusterDistances.length; i++ )
      {
         for( int j=0; j<i; j++ )
         {
            if( i == j )
            {
               System.out.println( "SHOULD NOT BE HERE 1" );
            }
            if( _clusterDistances[ i ][ j ] < _smallestDistance )
            {
               _smallestDistance = _clusterDistances[ i ][ j ];
               _x                = i;
               _y                = j;
            }
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
      double[] y = _clusters.get( n );
      double   d = 0;

      for( int i=0; i<_numberOfClusters; i++ )
      {
         if( i == n )
         {
            _clusterDistances[ i ][ n ] = 0;
            _clusterDistances[ n ][ i ] = 0;
         }
         else
         {
            x = _clusters.get( i );
            d = _util.getEuclideanDistance( x, y );
            _clusterDistances[ i ][ n ] = d;
            _clusterDistances[ n ][ i ] = d;
         }
      }
   }

   /**
    * 
    */
   private void calculateDistances()
   {
      _smallestDistance = Double.MAX_VALUE;
      double[] x = null;
      double[] y = null;

      for( int i=0; i<_numberOfClusters; i++ )
      {
         x = _clusters.get( i );
         for( int j=0; j<i; j++ )
         {
            y                           = _clusters.get( j );
            _clusterDistances[ i ][ j ] = _util.getEuclideanDistance( x, y );
            _clusterDistances[ j ][ i ] = _clusterDistances[ i ][ j ];

            if( _clusterDistances[ i ][ j ] < _smallestDistance )
            {
               _smallestDistance = _clusterDistances[ i ][ j ];
               _x                = i;
               _y                = j;
            }
         }
      }
   }

   /**
    * Once the clusters have been determined and trained, this method can
    * quickly return the cluster for the given point.
    * @param double[]
    * @return int
    */
   public int getCluster( double[] point )
   {
      double d = 0;
      double s = Double.MAX_VALUE;
      int    y = -1;

      for( int i=0; i<_numberOfClusters; i++ )
      {
         double[] x = _clusters.get( i );
         d          = _util.getEuclideanDistance( x, point );

         if( d < s )
         {
            y = i;
            s = d;
         }
      }
      _totalCount++;
      _clusterCounts[ y ]++;

      return y;
   }

   /**
    * This is the algorithm as defined in McKay's "Information Theory."
    *
    * While centroids don't change do:
    *   Randomly choose a data point
    *   Determine which centroid it's closest to
    *   Merge with that centroid
    *
    * @param TYPE
    * @return TYPE
   public void addPoints( ArrayList< double[] > points )
   {
      double[] c = null;
      double[] p = null;
      double   d = Double.MAX_VALUE;
      double   t = 0;
      int      i = 0;
      int      k = 0;

      while( d > 0 )
      {
         if( k++ % _numberOfClusters == 0 )
         {
            d = 0;
         }

         i = (int) Math.floor( Math.random() * points.size() );
         p = points.get( i );
         i = getCluster( p );
         c = _clusters.get( i );

         for( int j=0; j<p.length; j++ )
         {
            t      =  c[ j ] + 0.1 * ( p[ j ] - c[ j ] );
            d      += Math.abs( t - c[ j ] );
            c[ j ] =  t;
         }
      }

   }
    */

   /**
    * This method will add the data point to the set of existing centroid data and return its
    * corresponding centroid data point.
    *
    * @param TYPE
    * @return TYPE
    */
   public int addPoint( double[] point )
   {
      double d = 0;
      double s = _smallestDistance;
      int    y = -1;

      for( int i=0; i<_numberOfClusters; i++ )
      {
         double[] x = _clusters.get( i );
         d          = _util.getEuclideanDistance( x, point );

         if( d < s )
         {
            y = i;
            s = d;
         }
      }

      // in this case, we need to merge with an existing centroid
      if( y > -1 )
      {
         double[] m = merge( _clusters.get( y ), point );
         _clusters.set( y, m );
         calculateDistance( y );
         _clusterCounts[ y ]++;
      }
      // in this case, we are a new centroid and two others need to merge
      else
      {
         double[] m = merge( _clusters.get( _x ), _clusters.get( _y ) );
         _clusters.set( _x, m );
         _clusters.set( _y, ( point ) );
         y = _y;
         calculateDistance( _x );
         calculateDistance( _y );
         _clusterCounts[ _x ] = 1;
         _clusterCounts[ _y ] = 0;
      }

      calculateMinimumDistance();
      _totalCount++;

      return y;
   }

   /**
    *
    */
   private double[] copy( double[] p )
   {
      double[] c = new double[ p.length ];

      for( int i=0; i<p.length; i++ )
      {
         c[ i ] = ( p[ i ] );
      }

      return c;
   }

   /**
    *
    */
   private double[] merge( double[] x, double[] y )
   {
      double[] m = new double[ x.length ];

      for( int i=0; i<m.length; i++ )
      {
         m[ i ] = ( ( x[ i ] + y[ i ] ) / 2.0 );
      }

      return m;
   }

   /**
    * This method can be run after *all* the data has been seen so that the clusters will be
    * an optimal set, i.e., no extraneous clusters not contributing to capturing the behaviour.
    */
   public void pruneClusters()
   {
      ArrayList< double[] > clusters = new ArrayList< double[] >();
      int                   maxCount = Integer.MIN_VALUE;
      double                ratio;

      for( int i=0; i<_numberOfClusters; i++ )
      {
         if( _clusterCounts[ i ] > maxCount )
         {
            maxCount = _clusterCounts[ i ];
         }
      }

//       System.out.println( "" );

      for( int i=0; i<_numberOfClusters; i++ )
      {

         ratio = (double) _clusterCounts[ i ] / (double) maxCount;

//          System.out.println( i + ",count:," + _clusterCounts[ i ] + ",ratio:," + ratio );

//          if( ratio > _threshold && _clusterCounts[ i ] > 0 )
         if( _clusterCounts[ i ] > 0 )
         {
            clusters.add( ( _clusters.get( i ) ) );
         }
      }

      _clusters         = clusters;
      _numberOfClusters = _clusters.size();
      _clusterCounts    = new int[ _numberOfClusters ];
      _clusterDistances = new double[ _numberOfClusters ][ _numberOfClusters ];
      _totalCount       = 0;
      calculateDistances();
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public double[] getDistanceCDF()
   {
      double[] cdf = new double[ _numberOfClusters * ( _numberOfClusters - 1 ) / 2 ];
      int      p   = 0;

      for( int i=0; i<_numberOfClusters; i++ )
      {
         for( int j=0; j<i; j++ )
         {
            cdf[ p++ ] = _clusterDistances[ i ][ j ];
         }
      }

      Arrays.sort( cdf );

      return cdf;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void reset()
   {
      _clusterCounts = new int[ _numberOfClusters ];
      _totalCount    = 0;
   }

   /**
    *
    */
   public class VectorQuantizerDAO extends DAOBase< VectorQuantizer >
   {
   }

   /**
    *
    * @return ManyToOneCorrelationDAO -- The impl class of DAOBase type.
    */
   public DAOBase getDAO()
   {
      DAOBase dao = new VectorQuantizerDAO();

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
    * Sets the total number of clusters.
    * In the case of a lattice, this is the number of clusters per dimension, not the total.
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
    * @param int
    */
   public void setNumberOfDimensions( int numberOfDimensions )
   {
      _numberOfDimensions = numberOfDimensions;
   }

   /**
    *
    * @return int
    */
   public int getNumberOfDimensions()
   {
      return _numberOfDimensions;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public double[] getCentroid( int i )
   {
      double[] a = _clusters.get( i );
      double[] c = new double[ _numberOfDimensions ];

      for( int j=0; j<c.length; j++ )
      {
         c[ j ] = ( a[ j ] );
      }

      return c;
   }

   /**
    *
    * @param long
    */
   public void setTotalCount( long totalCount )
   {
      _totalCount = totalCount;
   }

   /**
    *
    * @return long
    */
   public long getTotalCount()
   {
      return _totalCount;
   }

   /**
    * This value will represent the "coarsness" of the clusters. So, a smaller value will allow clusters with smaller
    * relative sizes when compared with the other clusters. Thus, if it is known that all clusters should be about
    * equal in sizs (number of members), then this value should be larger; however, if it is known that there are some
    * clusters that will have a relatively small membership size, then set this value smaller.
    * @param double
    */
   public void setThreshold( double threshold )
   {
      _threshold = threshold;
   }

   /**
    *
    * @return double
    */
   public double getThreshold()
   {
      return _threshold;
   }

   /**
    *
    * @return ArrayList< double[] >  _clusters
    */
   public ArrayList< double[] > getClusters()
   {
      return _clusters;
   }

   /**
    *
    * @return double[][]
    */
   public double[][] getClusterDistances()
   {
      return _clusterDistances;
   }

   /**
    *
    */
   public void setCentroidClusterMap( TreeMap< Double, Integer > centroidClusterMap )
   {
      _centroidClusterMap = centroidClusterMap;
   }

   /**
    * Only appropriate for one-dimensional data.
    */
   public TreeMap< Double, Integer > getCentroidClusterMap()
   {
      if( _centroidClusterMap == null )
      {
         _centroidClusterMap = new TreeMap< Double, Integer >();
         double[] centroid   = null;
         for( int i=0; i<_clusters.size(); i++ )
         {
            centroid = _clusters.get( i );
            _centroidClusterMap.put( centroid[ 0 ], i );
         }
      }

      return _centroidClusterMap;
   }

   /**
    *
    * Only appropriate for one-dimensional data.
    */
   public int getClusterOrder( int cluster )
   {
      Collection< Integer > values = getCentroidClusterMap().values();
      int                   order  = 0;

      for( Integer value : values )
      {
         if( value == cluster )
         {
            return order;
         }
         order++;
      }

      return order;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public static void main( String[] args )
   {
      ArrayList< Double[] > points  = new ArrayList< Double[] >();
      int                   dims    = 1;
      int                   count   = 500;
      int                   cluster = 500;
      VectorQuantizer       vq      = new VectorQuantizer( "TestVQ" );
      MathUtilities         u       = new MathUtilities();
      Double[]              rpoints = new Double[ count ];
      double                max     = Double.MIN_VALUE;
      double[]              point   = new double[ dims ];

      VectorQuantizerDAO dao = (VectorQuantizerDAO) vq.getDAO();
      vq = dao.deserialize();
      if( vq == null )
      {
         System.out.println( "VQ couldn't be deserialized..." );
         vq = new VectorQuantizer( "TestVQ" );
         vq.setNumberOfClusters( 5 );
         vq.setNumberOfDimensions( 1 );
         vq.setThreshold( 0.10 );
         vq.initialize();
      }

      System.out.println( "VectorQuantizer: " + vq  );

      for( int i=0; i<count; i++ )
      {
         for( int j=0; j<dims; j++ )
         {
            point[ j ] = u.random();
         }
//          cluster = vq.addPoint( point );
         cluster = vq.getCluster( point );
         System.out.println( "a Cluster: " + cluster + " for point: " + point[ 0 ] );
         point   = vq.getCentroid( cluster );
         System.out.println( "Centroid is: " + point[ 0 ] );
      }

      dao = (VectorQuantizerDAO) vq.getDAO();
      dao.serialize( vq );

      /*
      for( int i=0; i<count; i++ )
      {
         double a = u.random();
         double b = u.random();
         rpoints[ i ] = new Double( Math.sqrt( -2 * Math.log( a ) ) * Math.sin( 2 * Math.PI * b ) );
         if( Math.abs( rpoints[ i ] ) > max )
         {
            max = Math.abs( rpoints[ i ] );
         }
      }
      for( int i=0; i<count; i++ )
      {
         rpoints[ i ] /= ( max * 5 );
         vq.addPoint( obj );
      }

      for( int j=0; j<count; j++ )
      {
         Double[] p = new Double[ dims ];
         double shift = 0.5;
         for( int k=0; k<dims; k++ )
         {
            if( u.random() < 0.5 )
            {
               p[ k ] = rpoints[ j ];
            }
            else
            {
               p[ k ] = rpoints[ j ] + shift;
            }
            System.out.print( p[ k ] + "," );
            points.add( p );
         }
         System.out.println( "" );
      }

      for( Double[] obj : points )
      {
         vq.addPoint( obj );
      }

      System.out.println( "\nBefore pruning: " );
      for( int s=0; s<vq.getNumberOfClusters(); s++ )
      {
         Double[] d = vq.getCentroid( s );
//          System.out.println( d[ 0 ] + "," + d[ 1 ] );
         System.out.println( d[ 0 ] );
      }

      vq.pruneClusters();
      for( int r=points.size() - 1; r>-1; r-- )
      {
         Double[] obj = points.get( r );
         vq.addPoint( obj );
      }
      for( int r=points.size() - 1; r>-1; r-- )
      {
         Double[] obj = points.get( r );
         vq.getCluster( obj );
      }
      vq.pruneClusters();

      System.out.println( "\nAfter pruning: " );
      for( int s=0; s<vq.getNumberOfClusters(); s++ )
      {
         Double[] d = vq.getCentroid( s );
//          System.out.println( d[ 0 ] + "," + d[ 1 ] );
         System.out.println( d[ 0 ] );
      }

      // here, we cluster the clusters
      VectorQuantizer vq2 = new VectorQuantizer();
      vq2.setNumberOfClusters( vq.getNumberOfClusters() );
      vq2.setNumberOfDimensions( 2 );
      vq2.setThreshold( 0.05 );
      vq2.initialize();

      for( int i=0; i<vq.getNumberOfClusters(); i++ )
      {
         vq2.addPoint( vq.getCentroid( i ) );
      }
      for( int j=0; j<50; j++ )
      {
         for( int i=0; i<vq.getNumberOfClusters(); i++ )
         {
            vq2.getCluster( vq.getCentroid( i ) );
         }
      }
      vq2.pruneClusters();

      System.out.println( "\nAfter cluster pruning: " );
      for( int s=0; s<vq2.getNumberOfClusters(); s++ )
      {
         Double[] d = vq2.getCentroid( s );
         System.out.println( d[ 0 ] + "," + d[ 1 ] );
      }

      // here, we cluster the clusters
      VectorQuantizer vq3 = new VectorQuantizer();
      vq3.setNumberOfClusters( vq2.getNumberOfClusters() );
      vq3.setNumberOfDimensions( 2 );
      vq3.setThreshold( 0.05 );
      vq3.initialize();
      for( int i=0; i<vq2.getNumberOfClusters(); i++ )
      {
         vq3.addPoint( vq2.getCentroid( i ) );
      }
      for( int j=0; j<50; j++ )
      {
         for( int i=0; i<vq2.getNumberOfClusters(); i++ )
         {
            vq3.getCluster( vq2.getCentroid( i ) );
         }
      }
      vq3.pruneClusters();

      System.out.println( "\nAfter cluster pruning: " );
      for( int s=0; s<vq3.getNumberOfClusters(); s++ )
      {
         Double[] d = vq3.getCentroid( s );
         System.out.println( d[ 0 ] + "," + d[ 1 ] );
      }
      */

   }

}
