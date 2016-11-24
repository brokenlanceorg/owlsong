package math.graph;

import java.util.*;

/**
 *
 */
public class GraphUtilities
{

   /**
    *
    */
   public GraphUtilities()
   {
   }

   /**
    *
    * @param double[][] An adjacency matrix, which by definition is a square matrix.
    * @return double[][] The Lapacian matrix.
    */
   public int[][] getLaplacianMatrix( int[][] m )
   {
      int[][] l = new int[ m.length ][ m.length ];

      for( int i=0; i<m.length; i++ )
      {
         int sum = 0;
         for( int j=0; j<m.length; j++ )
         {
            l[ i ][ j ] = m[ i ][ j ];
            if( l[ i ][ j ] == 1 )
            {
               sum += 1;
            }
         }
         l[ i ][ i ] = sum;
      }

      return l;
   }

   /**
    *
    * @param double[][] A Laplacian matrix.
    * @return ArrayList< Vertex > An array list of Vertex objects sorted by the vertex degree.
    */
   public ArrayList< Vertex > getSortedVertexList( int[][] laplacian )
   {
      ArrayList< Vertex > list = new ArrayList< Vertex >();

      for( int i=0; i<laplacian.length; i++ )
      {
         list.add( new Vertex( i, laplacian[ i ][ i ] ) );
      }

      Collections.sort( list );

      return list;
   }

}
