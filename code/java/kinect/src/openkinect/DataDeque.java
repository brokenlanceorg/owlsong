package openkinect;

import java.util.*;

/**
 *
 */
public class DataDeque< T >
{

   private T               _data;
   private ArrayDeque< T > _dataDeque;
   private int             _depth = 10;

   /**
    *
    */
   public DataDeque()
   {
      initialize();
   }

   /**
    *
    */
   public DataDeque( int depth )
   {
      _depth = depth;
      initialize();
   }

   /**
    *
    */
   private void initialize()
   {
      _dataDeque = new ArrayDeque< T >();
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public int size()
   {
      return _dataDeque.size();
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public synchronized T poll()
   {
      return _dataDeque.poll();
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public synchronized void add( T t )
   {
      if( _dataDeque.size() >= _depth )
      {
         _dataDeque.poll();
      }
      _dataDeque.add( t );
//       System.out.print( _dataDeque.size() + " " );
   }

}
