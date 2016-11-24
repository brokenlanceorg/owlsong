package math.graph;

import java.util.*;

/**
 * This class provides the functionality that implements Dijkstra's Algorithm
 * of finding the shortest path between a starting vertex and all other vertices
 * in a directed or undirected positive value graph.
 */
public class DijkstraShortestPath
{

   // The normal adjacency matrix holds the weight value between each pair of vertices
   // The matrix is read in such a way that the row represents the "from" vertex and 
   // the column represents the "to" vertex. Thus, if an edge exists pointing from 
   // vertex 3 to vertex 5 with a weight of 9, then row 3 column 5 will have a value of 9.
   // It is assumed that the initial source vertex is the zeroth position.
   private double[][] _adjacencyWeights;

   // Holds the labels ( the previous vertex in the path and the associated distance ) 
   // of the sanned vertices.
   private double[][] _scannedVertexLabels;

   /**
    *
    */
   public DijkstraShortestPath()
   {
   }

   /**
    *
    */
   public DijkstraShortestPath( double[][] m )
   {
      setAdjacencyWeights( m );
   }

   /**
    *
    * @param double[][]
    */
   public void setAdjacencyWeights( double[][] adjacencyWeights )
   {
      _adjacencyWeights    = adjacencyWeights;
      _scannedVertexLabels = null;
   }

   /**
    *
    * @return double[][]
    */
   public double[][] getAdjacencyWeights()
   {
      return _adjacencyWeights;
   }

   /**
    * We assume that the setAdjacencyWeights() method has been called or the
    * constructor called with the adjacencty matrix weights.
    *
    * @param TYPE
    * @return TYPE
    */
   public double[][] solve()
   {
      if( _scannedVertexLabels == null )
      {
         findShortestPath();
      }

      return _scannedVertexLabels;
   }

   /**
    *
    */
   private void initialize()
   {
   }

   /**
    *
    */
   private void findShortestPath()
   {
      int                                       n                     = _adjacencyWeights.length;
      VertexLabel                               minCurrent            = null;
                                                _scannedVertexLabels  = new double[ n ][ 2 ];
      HashSet< Integer >                        v                     = new HashSet< Integer >();
      ArrayList< PriorityQueue< VertexLabel > > pqs                   = new ArrayList< PriorityQueue< VertexLabel > >( n );
      PriorityQueue< VertexLabel >              pq                    = new PriorityQueue< VertexLabel >();
      PriorityQueue< VertexLabel >              tpq                   = null;

      _scannedVertexLabels[ 0 ][ 0 ] = 0;
      _scannedVertexLabels[ 0 ][ 1 ] = 0;
      pqs.add( null );

      // just initializations:
      for( int i=1; i<n; i++ )
      {
         v.add( i );
         PriorityQueue< VertexLabel > p = new PriorityQueue< VertexLabel >();
         p.add( new VertexLabel( i, 0, _adjacencyWeights[ 0 ][ i ] ) );
         pqs.add( p );
      }

      // main loop:
      while( v.isEmpty() == false )
      {
         pq.clear();
         for( int vertex : v )
         {
            minCurrent = pqs.get( vertex ).poll();
            if( minCurrent != null )
            {
               pq.add( minCurrent );
               if( minCurrent.getDistance() < Double.MAX_VALUE )
               {
                  pqs.get( vertex ).add( minCurrent );
               }
            }
         }
         minCurrent = pq.poll();
         if( minCurrent.getDistance() == Double.MAX_VALUE )
         {
            return;
         }
         v.remove( minCurrent.getVertextID() );
         _scannedVertexLabels[ minCurrent.getVertextID() ][ 0 ] = minCurrent.getPreviousVertextID();
         _scannedVertexLabels[ minCurrent.getVertextID() ][ 1 ] = minCurrent.getDistance();
         for( int pv : v )
         {
            tpq = pqs.get( pv );
            tpq.add( new VertexLabel( pv, minCurrent.getVertextID(), minCurrent.getDistance() + _adjacencyWeights[ minCurrent.getVertextID() ][ pv ] ) );
         }
      }
   }

   /**
    *
    */
   private class VertexLabel implements Comparable< VertexLabel >
   {

      private int    _vertextID;
      private int    _previousVertextID;
      private double _distance;

      /**
       *
       */
      private VertexLabel()
      {
      }

      /**
       *
       */
      private VertexLabel( int v, int p, double d )
      {
         setVertextID( v );
         setPreviousVertextID( p );
         setDistance( d );
      }

      /**
       *
       * @param int
       */
      public void setVertextID( int vertextID )
      {
         _vertextID = vertextID;
      }

      /**
       *
       * @return int
       */
      public int getVertextID()
      {
         return _vertextID;
      }

      /**
       *
       * @param int
       */
      public void setPreviousVertextID( int previousVertextID )
      {
         _previousVertextID = previousVertextID;
      }

      /**
       *
       * @return int
       */
      public int getPreviousVertextID()
      {
         return _previousVertextID;
      }

      /**
       *
       * @param double
       */
      public void setDistance( double distance )
      {
         _distance = distance;
      }

      /**
       *
       * @return double
       */
      public double getDistance()
      {
         return _distance;
      }

      /**
       *
       */
      public boolean equals( Object o )
      {
         boolean value = false;
         if( VertexLabel.class == o.getClass() )
         {
            VertexLabel other = (VertexLabel)o;
            value = ( getVertextID() == other.getVertextID() );
         }
         return value;
      }

      /**
       *
       */
      public int hashCode()
      {
         return Integer.valueOf( getVertextID() ).hashCode();
      }

      /**
       * Note that this method is not consistent with the equals method.
       *
       * @param TYPE
       * @return TYPE
       */
      public int compareTo( VertexLabel v )
      {
         int value = 0;

         if( getDistance() < v.getDistance() )
         {
            value = -1;
         }
         else if( getDistance() > v.getDistance() )
         {
            value = 1;
         }

         return value;
      }

      /**
       *
       */
      public String toString()
      {
         String value = "\nVertex Label:\n";
         value += "   Vertex ID: " + getVertextID() + "\n";
         value += "   Previous Vertex ID: " + getPreviousVertextID() + "\n";
         value += "   Distance: " + getDistance() + "\n";
         return value;
      }

   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public static void main( String[] args )
   {
      int n = 2000;
      int c = 0;
      int b = 200;
      int[] sz = new int[ b ];
      long[] t = new long[ b ];
      for( int k=10; k<n; k+=10 )
      {
         double[][] am = new double[ k ][ k ];
         sz[ c ] = k;
         for( int i=0; i<am.length; i++ )
         {
            for( int j=0; j<am[ i ].length; j++ )
            {
               if( i == j )
               {
                  am[ i ][ j ] = Double.MAX_VALUE;
               }
               else
               {
                  am[ i ][ j ] = Math.random();
               }
            }
         }
         long start = System.currentTimeMillis();
         DijkstraShortestPath dss = new DijkstraShortestPath( am );
         double[][] s = dss.solve();
         t[ c++ ] = ( System.currentTimeMillis() - start );
//          System.out.println( "total time: " + ( System.currentTimeMillis() - start ) );
      }
      for( int i=0; i<b; i++ )
      {
         System.out.println( sz[ i ] + ", " + t[ i ] );
      }
   }

}
