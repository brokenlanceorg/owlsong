package math.cluster;

import java.util.*;

import common.*;

/**
 *
 */
public class ConnectedComponentQuantizer implements Persistable
{

   private String                                 _name;
   private int[][]                                _labels;
   private HashMap< Integer, ArrayList< int[] > > _clusterPointMap;

   /**
    *
    */
   public ConnectedComponentQuantizer()
   {
   }

   /**
    *
    */
   public ConnectedComponentQuantizer( String name )
   {
      setName( name );
   }

   /**
    *
    */
   public class ConnectedComponentQuantizerDAO extends DAOBase< ConnectedComponentQuantizer >
   {
   }

   /**
    * @return ManyToOneCorrelationDAO -- The impl class of DAOBase type.
    */
   public DAOBase getDAO()
   {
      DAOBase dao = new ConnectedComponentQuantizerDAO();

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
    * Step 1: If A[0,0] > 0 then increment L and set Q[0, 0] = L. 
    * This takes care of the first pixel in the image.
    *
    * Step 2: Label the pixels in row y = 0. 
    * For x = 1 to N-1, check the value of A[x, 0]. 
    * If A[x, 0] > 0 and A[x, 0] = A[x-1, 0] then set Q[x, 0] = Q[x-1, 0]. 
    * If A[x, 0] > 0 and A[x, 0] != A[x-1, 0] then increment L and set Q[x, 0] = L
    *
    * Step 3: Label the rest of the rows. 
    * For y = 1 to M-1 do the following. 
    * If A[0, y] > 0 and A[0, y] = A[0, y-1] then set Q[0, y] = Q[0, y-1].
    * If A[0, y] > 0 and A[0, y] != A[0, y-1] then increment L and set Q[0, y] = L. 
    *
    * This takes care of labeling the first element in the row.
    * current pixel as p, the neighbor to the left as s and the one above as t. 
    * If A[p]  = A[s] but A[p] != A[t] then set Q[p] = Q[s]. 
    * If A[p] != A[s] but A[p]  = A[t] then set Q[s] = Q[t]. 
    * If A[p] != A[t] and A[p] != A[s] then increment L and set Q[ p] = L. 
    * If A[p]  = A[t], A[p] = A[s] and Q[s] = Q[t] then set Q[p] = Q[t]. 
    * This takes care of all the cases except A[ p] = A[t], A[p] = A[s]
    * and Q[s] != Q[t].  Let L1 be the smaller value and L2
    * be the larger value. Then set 1 Q[p] = L1 and EQ[L2] = L1.
    *
    */
   public void quantize( double[][] data )
   {
      HashMap< Integer, Integer > eq = new HashMap< Integer, Integer >();
      _labels                        = new int[ data.length ][ data[ 0 ].length ];
      int                         L  = 0;

      if( data[ 0 ][ 0 ] > 0.0 )
      {
         _labels[ 0 ][ 0 ] = ++L;
      }

      for( int i=1; i<data.length; i++ )
      {
         if(    data[ i ][ 0 ] > 0.0 
             && data[ i ][ 0 ] == data[ i - 1 ][ 0 ] )
         {
            _labels[ i ][ 0 ] = _labels[ i - 1 ][ 0 ];
         }
         else if(    data[ i ][ 0 ] > 0.0 
                  && data[ i ][ 0 ] != data[ i - 1 ][ 0 ] )
         {
            _labels[ i ][ 0 ] = ++L;
         }
      }

      for( int j=1; j<data[ 0 ].length; j++ )
      {
         if(    data[ 0 ][ j ]  >  0.0 
             && data[ 0 ][ j ]  == data[ 0 ][ j - 1 ] )
         {
            _labels[ 0 ][ j ] = _labels[ 0 ][ j - 1 ];
         }
         else if(    data[ 0 ][ j ] >  0.0 
                  && data[ 0 ][ j ] != data[ 0 ][ j - 1 ] )
         {
            _labels[ 0 ][ j ] = ++L;
         }
         for( int i=1; i<data.length; i++ )
         {
            if(    data[ i ][ j ] == data[ i - 1 ][ j ] 
                && data[ i ][ j ] != data[ i ][ j - 1 ] )
            {
               _labels[ i ][ j ] = _labels[ i - 1 ][ j ];
            }
            else if(    data[ i ][ j ] != data[ i - 1 ][ j ] 
                     && data[ i ][ j ] == data[ i ][ j - 1 ] )
            {
               _labels[ i ][ j ] = _labels[ i ][ j - 1 ];
            }
            else if(    data[ i ][ j ] != data[ i - 1 ][ j ] 
                     && data[ i ][ j ] != data[ i ][ j - 1 ] )
            {
               _labels[ i ][ j ] = ++L;
            }
            else if(       data[ i ][ j ]     ==    data[ i - 1 ][ j ] 
                     &&    data[ i ][ j ]     ==    data[ i ][ j - 1 ] 
                     && _labels[ i - 1 ][ j ] == _labels[ i ][ j - 1 ] )
            {
               _labels[ i ][ j ] = _labels[ i ][ j - 1 ];
            }
            else if(       data[ i ][ j ]     ==    data[ i - 1 ][ j ] 
                     &&    data[ i ][ j ]     ==    data[ i ][ j - 1 ] 
                     && _labels[ i - 1 ][ j ] != _labels[ i ][ j - 1 ] )
            {
               _labels[ i ][ j ] = Math.min( _labels[ i - 1 ][ j ], _labels[ i ][ j - 1 ] );
               eq.put( new Integer( Math.max( _labels[ i - 1 ][ j ], _labels[ i ][ j - 1 ] ) ), 
                       new Integer( _labels[ i ][ j ] ) );
            }
         }
      }

      Integer            p        = null;
      Integer            c        = null;
      TreeSet< Integer > clusters = new TreeSet< Integer >();
      int                cluster  = 0;
      int                pos      = 0;

      for( int i=0; i<_labels.length; i++ )
      {
         for( int j=0; j<_labels[ 0 ].length; j++ )
         {
            c = new Integer( _labels[ i ][ j ] );
            while( ( p = eq.get( new Integer( c ) ) ) != null )
            {
               c = p;
            }
            _labels[ i ][ j ] = ( c );
            clusters.add( new Integer( _labels[ i ][ j ] ) );
         }
      }

      for( int j=0; j<_labels.length; j++ )
      {
         for( int k=0; k<_labels[ 0 ].length; k++ )
         {
            Iterator< Integer > iter = clusters.iterator();
            pos                      = 0;
            while( iter.hasNext() )
            {
               pos++;
               cluster = iter.next();
               if( _labels[ j ][ k ] == cluster )
               {
                  _labels[ j ][ k ] = pos;
                  break;
               }
            }
         }
      }
   }

   /**
    *
    * @param int[][]
    */
   public void setClusterTags( int[][] labels )
   {
      _labels = labels;
   }

   /**
    *
    * @return int[][]
    */
   public int[][] getClusterTags()
   {
      return _labels;
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
         int[] point = null;

         _clusterPointMap = new HashMap< Integer, ArrayList< int[] > >();

         for( int i=0; i<_labels.length; i++ )
         {
            for( int j=0; j<_labels[ 0 ].length; j++ )
            {
               if( _labels[ i ][ j ] > 0 )
               {
                  point      = new int[ 2 ];
                  point[ 0 ] = i;
                  point[ 1 ] = j;
                  addPoint( _labels[ j ][ j ], point );
               }
            }
         }
      }

      return _clusterPointMap;
   }

   /**
    *
    */
   private void addPoint( int cluster, int[] point )
   {
      ArrayList< int[] > points = _clusterPointMap.get( cluster );
      if( points == null )
      {
         points = new ArrayList< int[] >();
         _clusterPointMap.put( cluster, points );
      }
      points.add( point );
   }

}
