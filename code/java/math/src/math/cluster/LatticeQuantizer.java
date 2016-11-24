package math.cluster;

import java.util.*;
import common.*;
import math.*;

/**
 * The idea behind this quantizer is the idea of nearby touching points in a lattice-like structure.
 * Points that are close to each other are touching, and these are part of the same cluster.
 * This is the same idea as the nearest neighbor quantizer except that these points are in a lattice
 * and not separated by an arbitrary distance. This can be useful in a 2D pixel array.
 *
 *
 */
public class LatticeQuantizer implements Persistable
{

   private String                                 _name;
   private int                                    _cluster = 1;
   private int[][]                                _clusterTags;
   private HashMap< Integer, ArrayList< int[] > > _clusterPointMap;

   /**
    *
    */
   public LatticeQuantizer()
   {
   }

   /**
    *
    */
   public LatticeQuantizer( String name )
   {
      setName( name );
   }

   /**
    *
    */
   public class LatticeQuantizerDAO extends DAOBase< LatticeQuantizer >
   {
   }

   /**
    *
    * @return ManyToOneCorrelationDAO -- The impl class of DAOBase type.
    */
   public DAOBase getDAO()
   {
      DAOBase dao = new LatticeQuantizerDAO();

      if( getName() != null )
      {
         dao.setFileName( getName() + ".ser" );
      }

      return dao;
   }

   /**
    *
    * @param double[][] -- the data lattice to clusterize.
    * @param double     -- the percentage of reduction of the original matrix.
    */
   public void quickQuantize( double[][] lattice, double p )
   {
      int           w  = ( int ) ( ( double ) lattice.length * p );
      int           h  = ( int ) ( ( double ) lattice[ 0 ].length * p );
      MathUtilities mu = new MathUtilities();
      double[][]    m  = mu.getReducedMatrix( lattice, w, h );

      quantize( m, true );

      // now re-map the cluster tags to the larger size image:
      int[][] clusterTags = new int[ lattice.length ][ lattice[ 0 ].length ];

      for( int i=0; i<lattice.length; i++ )
      {
         for( int j=0; j<lattice[ 0 ].length; j++ )
         {
            w = (int)( p * (double) i );
            h = (int)( p * (double) j );
//             if( lattice[ j ][ j ] > 0.0 )
            if(    w < _clusterTags.length 
                && h < _clusterTags[ 0 ].length )
            {
               clusterTags[ i ][ j ] = _clusterTags[ (int)( p * (double) i ) ][ (int)( p * (double) j ) ];
            }
         }
      }

      _clusterTags = clusterTags;
   }

   /**
    * If the image is fairly solid, the param should be false; otherwise if the data is sparse, set to true.
    * @param boolean This will perform further refinement of the clusters by coalescing many of the smaller clusters into the 
    *                bigger nearest cluster. If this isn't specified, the caller can determine the appropriatness of the cluster
    *                by its size. Note that when true, the method runs much longer in duration.
    */
   public void quantize( double[][] lattice, boolean refine )
   {
      _clusterTags = new int[ lattice.length ][ lattice[ 0 ].length ];
      double[][] l = clone( lattice );
      _cluster     = 1;
      
      while( ( l = quantizeLattice( l ) ) != null )
      {
         _cluster++;
//          System.out.println( "new cluster: " + _cluster );
      }

      if( refine )
      {
         // this section here takes the lion's share of the performance time:
         int a = ( int ) ( ( double ) _clusterTags.length      * 0.03 );
         int b = ( int ) ( ( double ) _clusterTags[ 0 ].length * 0.03 );
         for( int i=0; i<_clusterTags.length; i++ )
         {
            for( int j=0; j<_clusterTags[ 0 ].length; j++ )
            {
               if( _clusterTags[ i ][ j ] != 0 )
               {
                  int                         xstart = ( i < a ) ? 0 : i - a;
                  int                         ystart = ( j < b ) ? 0 : j - b;
                  int                         xend   = ( i > _clusterTags.length - a - 1 )      ? _clusterTags.length - 1 : i + a;
                  int                         yend   = ( j > _clusterTags[ 0 ].length - b - 1 ) ? _clusterTags[ 0 ].length - 1 : j + b;
                  HashMap< Integer, Integer > counts = new HashMap< Integer, Integer >();

                  for( int m=xstart; m<xend; m++ )
                  {
                     for( int n=ystart; n<yend; n++ )
                     {
                        if( _clusterTags[ m ][ n ] != 0 )
                        {
                           Integer c = counts.get( _clusterTags[ m ][ n ] );
                           if( c == null )
                           {
                              c = 0;
                           }
                           c++;
                           counts.put( _clusterTags[ m ][ n ], c );
                        }
                     }
                  }

                  Set< Integer > keys    = counts.keySet();
                  int            max     = Integer.MIN_VALUE;
                  int            c       = 0;
                  int            cluster = 0;

                  for( Integer k : keys )
                  {
                     c = counts.get( k );
                     if( c > max )
                     {
                        cluster = k;
                        max     = c;
                     }
                  }
                  
                  if( cluster != 0 )
                  {
                     _clusterTags[ i ][ j ] = cluster;
                  }
               }
            }
         }
      }
   }

   /**
    * This method will find and tag one cluster -- the largest in the lattice.
    * Making this one big monolithic method to squeeze out precious millis.
    */
   private double[][] quantizeLattice( double[][] lattice )
   {
      boolean       hasNonZeroPoint = true;
      StatUtilities su              = new StatUtilities();
      MathUtilities mu              = new MathUtilities();
      double[][]    original        = clone( lattice );
      double[][]    clone           = null;

      // perform the initial grass-fire algorithm to thin out the shape to the last densest point.
      ArrayList< Integer[] > lastPoints    = null;
      ArrayList< Integer[] > currentPoints = null;
      Integer[]              point         = null;
      while( hasNonZeroPoint )
      {
//          System.out.println( "top of loop." );
         hasNonZeroPoint = false;
         lastPoints      = currentPoints;
         currentPoints   = new ArrayList< Integer[] >();
         clone           = new double[ lattice.length ][ lattice[ 0 ].length ];
         for( int i=0; i<lattice.length; i++ )
         {
            for( int j=0; j<lattice[ 0 ].length; j++ )
            {
               if( lattice[ i ][ j ] > 0.0 && hasZeroNeighbor( lattice, i, j, 0.2 ) )
               {
// System.out.println( "lattice non zero and neighbor zero: " + i + " " + j );
                  clone[ i ][ j ] = 0.0;
               }
               else
               {
// System.out.println( "lattice zero or neighbor non zero: " + i + " " + j );
                  clone[ i ][ j ] = lattice[ i ][ j ];
               }
               // this could be a candidate for the densest point:
               if( clone[ i ][ j ] > 0.0 )
               {
// System.out.println( "candidate point: " + i + " " + j );
                  hasNonZeroPoint = true;
                  point           = new Integer[ 2 ];
                  point[ 0 ]      = i;
                  point[ 1 ]      = j;
                  currentPoints.add( point );
               }
            }
         }
         lattice = clone;
      }

      // handle the case in which the points weren't dense enough to make it through at least
      // two iterations of the above loop.
      if( lastPoints == null || lastPoints.size() == 0 )
      {
         lastPoints = new ArrayList< Integer[] >();
         for( int i=0; i<original.length; i++ )
         {
            for( int j=0; j<original[ 0 ].length; j++ )
            {
               if( original[ i ][ j ] > 0.0 )
               {
                  point           = new Integer[ 2 ];
                  point[ 0 ]      = i;
                  point[ 1 ]      = j;
                  lastPoints.add( point );
               }
            }
         }
      }

      double[]      xlist    = new double[ lastPoints.size() ];
      double[]      ylist    = new double[ lastPoints.size() ];
      double        min      = Double.MAX_VALUE;
      double        d        = 0;
      int           pos      = 0;
      int[]         pp       = new int[ 2 ];
      int[]         ppp      = new int[ 2 ];
      int[]         centroid = new int[ 2 ];
      for( Integer[] p : lastPoints )
      {
         xlist[ pos   ] = p[ 0 ];
         ylist[ pos++ ] = p[ 1 ];
      }
      pp[ 0 ] = ( int ) su.quickMedian( xlist );
      pp[ 1 ] = ( int ) su.quickMedian( ylist );
      for( Integer[] p : lastPoints )
      {
         ppp[ 0 ] = p[ 0 ];
         ppp[ 1 ] = p[ 1 ];
         d = mu.getEuclideanDistance( ppp, pp );
         if( d < min )
         {
            min = d;
            centroid[ 0 ] = ppp[ 0 ];
            centroid[ 1 ] = ppp[ 1 ];
         }
      }
//       System.out.println( "centroid: " + centroid[ 0 ] + " " + centroid[ 1 ] );

      lattice                            = original;
      clone                              = clone( lattice );
      ArrayDeque< Integer[] > ondeck     = null;
      ArrayDeque< Integer[] > candidates = new ArrayDeque< Integer[] >();
      int                     x          = 0;
      int                     y          = 0;
      point[ 0 ]                         = centroid[ 0 ];
      point[ 1 ]                         = centroid[ 1 ];

      candidates.add( point );

      while( candidates.size() > 0 )
      {
         ondeck = new ArrayDeque< Integer[] >();
         while( candidates.size() > 0 )
         {
            point = candidates.poll();
            x     = point[ 0 ];
            y     = point[ 1 ];
            if( clone[ x ][ y ] > 0.0 )
            {
               clone       [ x ][ y ] = 0;
               _clusterTags[ x ][ y ] = _cluster;

               int xstart = ( x == 0 ) ? 0 : x - 1;
               int ystart = ( y == 0 ) ? 0 : y - 1;
               int xend   = ( x == clone.length - 1 )      ? 0 : x + 2;
               int yend   = ( y == clone[ 0 ].length - 1 ) ? 0 : y + 2;

               for( int i=xstart; i<xend; i++ )
               {
                  for( int j=ystart; j<yend; j++ )
                  {
                     if( clone[ i ][ j ] > 0.0 )
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

      for( int i=0; i<clone.length; i++ )
      {
         for( int j=0; j<clone[ 0 ].length; j++ )
         {
            if( clone[ i ][ j ] > 0.0 )
            {
               return clone;
            }
         }
      }

      return null;
   }

   /**
    *
    */
   private boolean hasZeroNeighbor( double[][] lattice, int x, int y, double t )
   {
      int xstart = ( x == 0 ) ? 0 : x - 1;
      int ystart = ( y == 0 ) ? 0 : y - 1;
      int xend   = ( x == lattice.length - 1 ) ?      lattice.length - 1 : x + 2;
      int yend   = ( y == lattice[ 0 ].length - 1 ) ? lattice[ 0 ].length - 1 : y + 2;

      for( int i=xstart; i<xend; i++ )
      {
         for( int j=ystart; j<yend; j++ )
         {
            if( lattice[ i ][ j ] <= t )
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
   private double[][] clone( double[][] a )
   {
      double[][] b = new double[ a.length ][ a[ 0 ].length ];

      for( int i=0; i<a.length; i++ )
      {
         for( int j=0; j<a[ 0 ].length; j++ )
         {
            b[ i ][ j ] = a[ i ][ j ];
         }
      }

      return b;
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
    * @param HashMap< Integer, ArrayList< int[] > > _clusterPointMap
    */
   public void setClusterPointMap( HashMap< Integer, ArrayList< int[] > > clusterPointMap )
   {
      _clusterPointMap = clusterPointMap;
   }

   /**
    *
    * @return HashMap< Integer, ArrayList< int[] > > _clusterPointMap
    */
   public HashMap< Integer, ArrayList< int[] > > getClusterPointMap()
   {
      if( _clusterPointMap == null )
      {
         _clusterPointMap = new HashMap< Integer, ArrayList< int[] > >();
         for( int i=0; i<_clusterTags.length; i++ )
         {
            for( int j=0; j<_clusterTags[ 0 ].length; j++ )
            {
               if( _clusterTags[ i ][ j ] > 0 )
               {
                  int[] point = new int[ 2 ];
                  point[ 0 ] = i;
                  point[ 1 ] = j;
                  addPointToMap( point, _clusterTags[ i ][ j ] );
               }
            }
         }
      }

      return _clusterPointMap;
   }

   /**
    *
    */
   private void addPointToMap( int[] p, int i )
   {
      ArrayList< int[] > l = _clusterPointMap.get( i );
      if( l == null )
      {
         l = new ArrayList< int[] >();
         _clusterPointMap.put( new Integer( i ), l );
      }
      l.add( p );
   }

   /**
    *
    * @param int[][]
    */
   public void setClusterTags( int[][] clusterTags )
   {
      _clusterTags = clusterTags;
   }

   /**
    *
    * @return int[][]
    */
   public int[][] getClusterTags()
   {
      return _clusterTags;
   }

}
