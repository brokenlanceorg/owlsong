package math.graph;

import java.util.*;

/**
 *
 */
public class KruskalMinimalSpanningTree
{

   private ArrayList< Edge >             _minimalSpanningTree;
   private ArrayList< Edge >             _adjacencyList;
   private HashSet< HashSet< Edge > >    _forest;

   /**
    *
    */
   public KruskalMinimalSpanningTree()
   {
   }

   /**
    *
    */
   public KruskalMinimalSpanningTree( ArrayList< Edge > l )
   {
      _adjacencyList = l;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public ArrayList< Edge > getMinimalSpanningTree()
   {
      if( _minimalSpanningTree == null )
      {
         findMinimalSpanningTree();
      }

      return _minimalSpanningTree;
   }

   /**
    *
    */
   private void findMinimalSpanningTree()
   {
      _minimalSpanningTree = new ArrayList< Edge >();
      Collections.sort( _adjacencyList );
      HashSet< Integer > vertices = new HashSet< Integer >();
      HashSet< Integer > c1 = null;
      HashSet< Integer > c2 = null;

      // Iniitialize the forest:
      for( Edge e : _adjacencyList )
      {
         vertices.add( e.getLeftVertex() );
         vertices.add( e.getRightVertex() );
      }

      HashSet< HashSet< Integer > > V = new HashSet< HashSet< Integer > >( vertices.size() );

      for( int v : vertices )
      {
         HashSet< Integer > c = new HashSet< Integer >();
         c.add( v );
         V.add( c );
      }

      // Now start main loop:
      for( Edge e : _adjacencyList )
      {
         c1 = null;
         c2 = null;
         for( HashSet< Integer > c : V )
         {
            if(    ( c.size() == vertices.size() )
                || (    c.contains( e.getLeftVertex() ) 
                     && c.contains( e.getRightVertex() ) ) )
            {
               c1 = c2 = null;
               break;
            }
            if( c1 == null && c.contains( e.getLeftVertex() ) )
            {
               c1 = c;
            }
            if( c2 == null && c.contains( e.getRightVertex() ) )
            {
               c2 = c;
            }
         }

         if( c1 != c2 )
         {
            if( c1.size() < c2.size() )
            {
               c2.addAll( c1 );
               V.remove( c1 );
            }
            else
            {
               c1.addAll( c2 );
               V.remove( c2 );
            }
            _minimalSpanningTree.add( e );
         }
      }
   }

}
