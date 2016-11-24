package common;

import java.io.*;
import java.util.*;

/**
 *
 */
public class DAOBase< T >
{

   private String _filename = "object.ser";

   /**
    *
    */
   public DAOBase()
   {
   }

   /**
    *
    */
   public DAOBase( String filename )
   {
      _filename = filename;
   }

   /**
    * @param String -- The filename to which to store the object.
    */
   public void setFileName( String filename )
   {
      _filename = filename;
   }

   /**
    * Performs serialization semantics.
    * This current implementation will serialize the object to the 
    * file system at the given location.
    *
    * @param T -- The object to be serialized.
    */
   public void serialize( T object )
   {
      ObjectOutputStream oos = null;
      try
      {
         oos = new ObjectOutputStream( new FileOutputStream( _filename ) );
         oos.writeObject( object );
         oos.close();
      }
      catch( IOException e )
      {
         System.err.println( "Error serializing: " + e );
      }
   }

   /**
    * Performs deserialization semantics.
    * This current implementation will deserialize the object from the 
    * file system at the given location.
    *
    * @return T -- The object that was deserialized from the file system.
    */
   public T deserialize()
   {
      T obj = null;
      ObjectInputStream ois = null;
      try
      {
         ois = new ObjectInputStream( new FileInputStream( _filename ) );
         obj = (T) ois.readObject();
         ois.close();
      }
      catch( ClassNotFoundException e )
      {
         System.err.println( "Error deserializing: " + e );
      }
      catch( IOException e )
      {
         System.err.println( "Error deserializing: " + e );
      }

      return obj;
   }

}
