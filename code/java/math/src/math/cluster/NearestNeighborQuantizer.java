package math.cluster;

import java.util.*;
import common.*;
import math.*;

/**
 * NearestNeighborQuantizer.
 * 
 * This class is intended to use the MeanShiftQuantizer class' output as the input for this algorithm.
 * The idea is that the clusters determined by the mean shift algorithm will be used as the labeled
 * data points for this algorithm.
 * 
 * 
 *   1) Input a set of data points and their respective distances to each other.
 *   2) Input the cluster mapping to point data set.
 *   2) Input the number of nearest neighbors to consider.
 *   3) For an input point do:
 *   4)    determine the distance to all the other points.
 *   5)    find the N nearest neighbors
 *   6)    determine the majority class in these neighbors.
 *   7)    return this value as the class and the cluster center of mass.
 *   8) 
 *   9) 
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
public class NearestNeighborQuantizer implements Persistable
{

   private String                                    _name;
   private MathUtilities                             _util           = new MathUtilities();
   private int                                       _windowDistance = 13;
   private ArrayList< double[] >                     _points;
   private ArrayList< double[] >                     _clusters;
   private double[]                                  _pointDistances;
   private HashMap< Integer, ArrayList< double[] > > _clusterPointMap;
   private HashMap< String, Integer >                _pointClusterMap;
   private int[]                                     _nn             = new int[ _windowDistance ];

   /**
    *
    */
   public NearestNeighborQuantizer()
   {
   }

   /**
    *
    */
   public NearestNeighborQuantizer( String name )
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
      NearestNeighborQuantizer    nn      = new NearestNeighborQuantizer();
      MeanShiftQuantizer          ms      = new MeanShiftQuantizer();
      VectorQuantizer             vq      = new VectorQuantizer();
      MathUtilities               mu      = new MathUtilities();
      double[]                    p       = new double[ 2 ];
      double[]                    pp      = new double[ 2 ];

      System.out.println( "Initializing..." );

      vq.setNumberOfClusters( 1000 );
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
         random.add( p );

         pp      = new double[ 2 ];
         pp[ 0 ] = p[ 0 ] - 0.3;
         pp[ 1 ] = ( 1 - p[ 1 ] ) + 0.3;
         random.add( pp );

         arg += step;
      }

      Collections.shuffle( random );

      for( double[] d : random )
      {
         vq.addPoint( d );
      }

      // example of just random data points:
      int                   l      = 100;
      for( int i=0; i<l; i++ )
      {
         p = new double[ 2 ];
         p[ 0 ] = mu.random();
         p[ 1 ] = mu.random();
         random.add( p );
         vq.addPoint( p );
      }

      vq.reset();
      for( double[] d : random )
      {
         vq.getCluster( d );
      }

      vq.pruneClusters();

      System.out.println( "Finished pruning..." );

      ms.setVectorQuantizer( vq );
      ms.setCDFThreshold( 0.3 );
      ms.optimize();

      System.out.println( "Finished optimizing..." );

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

      nn.setClusters( ms.getClusters() );
      nn.setClusterPointMap( ms.getClusterPointMap() );

      int cluster = -1;

      p[ 0 ] = 0.4;
      p[ 1 ] = 0.6;
      cluster = nn.getCluster( p );
      System.out.println( "\n\np[ 0 ] is: " + p[ 0 ] );
      System.out.println( "p[ 1 ] is: " + p[ 1 ] );
      System.out.println( "The cluster is: " + cluster );
      p = nn.getCentroid( cluster );
      System.out.println( "Centroid is: " + p[ 0 ] + " " + p[ 1 ] );
      p[ 0 ] = 0.2;
      p[ 1 ] = 0.3;
      cluster = nn.getCluster( p );
      System.out.println( "\n\np[ 0 ] is: " + p[ 0 ] );
      System.out.println( "p[ 1 ] is: " + p[ 1 ] );
      System.out.println( "The cluster is: " + cluster );
      p = nn.getCentroid( cluster );
      System.out.println( "Centroid is: " + p[ 0 ] + " " + p[ 1 ] );
      p[ 0 ] = 0.8;
      p[ 1 ] = 0.8;
      cluster = nn.getCluster( p );
      System.out.println( "\n\np[ 0 ] is: " + p[ 0 ] );
      System.out.println( "p[ 1 ] is: " + p[ 1 ] );
      System.out.println( "The cluster is: " + cluster );
      p = nn.getCentroid( cluster );
      System.out.println( "Centroid is: " + p[ 0 ] + " " + p[ 1 ] );
   }

   /**
    *
    */
   public class NearestNeighborQuantizerDAO extends DAOBase< NearestNeighborQuantizer >
   {
   }

   /**
    *
    * @return ManyToOneCorrelationDAO -- The impl class of DAOBase type.
    */
   public DAOBase getDAO()
   {
      DAOBase dao = new NearestNeighborQuantizerDAO();

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
    * @param int
    */
   public void setWindowDistance( int windowDistance )
   {
      _windowDistance = windowDistance;
      _nn             = new int[ _windowDistance ];
   }

   /**
    *
    * @return int
    */
   public int getWindowDistance()
   {
      return _windowDistance;
   }

   /**
    *
    */
   private String getHash( double[] p )
   {
      StringBuffer hash = new StringBuffer( "" );

      for( int i=0; i<p.length; i++ )
      {
//          hash.append( Double.toHexString( p[ i ] ) );
         hash.append( Double.toString( p[ i ] ) );
      }

      return hash.toString();
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public int getNumberOfClusters()
   {
      int c = 0;

      if( _clusterPointMap != null )
      {
         Set s = _clusterPointMap.keySet();
         c     = s.size();
      }

      return c;
   }

   /**
    *
    */
   public void setClusterPointMap( HashMap< Integer, ArrayList< double[] > > clusterPointMap )
   {
      _clusterPointMap    = clusterPointMap;
      _pointClusterMap    = new HashMap< String, Integer >();
      _points             = new ArrayList< double[] >();
      Set< Integer > keys = _clusterPointMap.keySet();

      for( Integer key : keys )
      {
         ArrayList< double[] > points = _clusterPointMap.get( key );
         for( double[] point : points )
         {
            _pointClusterMap.put( getHash( point ), key );
            _points.add( point );
         }
      }

      _pointDistances = new double[ _points.size() ];
   }

   /**
    *
    */
   public HashMap< Integer, ArrayList< double[] > > getClusterPointMap()
   {
      return _clusterPointMap;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void getNearestNeighbors( double[] y )
   {
      double[]                    x    = null;
      HashMap< Double, double[] > map  = new HashMap< Double, double[] >( _points.size() );

      for( int i=0; i<_points.size(); i++ )
      {
         x                    = _points.get( i );
         _pointDistances[ i ] = _util.getEuclideanDistance( x, y );
//          if( _pointDistances[ i ] == 0.0 )
//          {
//             System.out.println( "dist is zero for x piont: " + x[ 0 ] + " " + x[ 1 ] );
//             System.out.println( "dist is zero for y piont: " + y[ 0 ] + " " + y[ 1 ] );
//          }
         map.put( _pointDistances[ i ], x );
      }

      Arrays.sort( _pointDistances );
      int     s = Math.min( _windowDistance, _pointDistances.length );
      int     i = 0;
      Integer w = -1;

      for( int j=0; j<s; j++ )
      {
         x = map.get( _pointDistances[ j ] );
         w = _pointClusterMap.get( getHash( x ) );
         if( w != null )
         {
            _nn[ i++ ] = w;
         }
      }
   }

   /**
    *
    */
   public void printList( ArrayList< Integer > list )
   {
      for( Integer obj : list )
      {
         System.out.println( obj );
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
   public void setPoints( ArrayList< double[] > points )
   {
      _points    = new ArrayList< double[] >( points.size() );
      double[] d = null;

      for( double[] p : points )
      {
         d = new double[ p.length ];
         for( int i=0; i<p.length; i++ )
         {
            d[ i ] = p[ i ];
         }
         _points.add( d );
      }

      _pointDistances = new double[ points.size() ];
   }

   /**
    *
    */
   public void setClusters( ArrayList< double[] > clusters )
   {
      _clusters  = new ArrayList< double[] >( clusters.size() );
      double[] d = null;

      for( double[] p : clusters )
      {
         d = new double[ p.length ];
         for( int i=0; i<p.length; i++ )
         {
            d[ i ] = p[ i ];
         }
         _clusters.add( d );
      }
   }

   /**
    *
    */
   public ArrayList< double[] > getClusters()
   {
      return _clusters;
   }

   /**
    *
    */
   public double[] getCentroid( int i )
   {
      return _clusters.get( i );
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public int getCluster( double[] p )
   {
      int cluster = -1;

      getNearestNeighbors( p );
      HashMap< Integer, Integer > counts = new HashMap< Integer, Integer >();

      for( Integer i : _nn )
      {
         Integer c = counts.get( i );
         if( c == null )
         {
            c = 0;
         }
         c++;
         counts.put( i, c );
      }

      Set< Integer > keys = counts.keySet();
      int            max  = Integer.MIN_VALUE;
      int            c    = 0;

      for( Integer k : keys )
      {
         c = counts.get( k );
         if( c > max )
         {
            cluster = k;
            max = c;
         }
      }

      return cluster;
   }

}
