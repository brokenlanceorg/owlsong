package common;

import java.util.*;
import java.io.*;

/**
 *  This class encapsulates functionality required to read a properties
 *  file found somewhere in the classpath.
 *  This class is a Singleton that caches the various properties objects.
 */
public class PropertiesReader
{

   // Singletone instance
   private static PropertiesReader _myInstance;

   // To cache the various properties objects.
   private HashMap _myProperties;

   /**
    * Singleton constructor.
    */
   private PropertiesReader()
   {
      _myProperties = new HashMap();
   }

   /**
    * Returns the singletone instance.
    */
   public static PropertiesReader getInstance()
   {
      if( _myInstance == null )
      {
         _myInstance = new PropertiesReader();
      }

      return _myInstance;
   }

   /**
    * This loads the properties from the file.
    * @param String The file name of the properties file. e.g., "Configuration.properties"
    * @return Properties The actual Properties object.
    */
   public Properties getProperties( String fileName )
   {
      Properties theProps = (Properties)_myProperties.get( fileName );

      if( theProps == null )
      {
         theProps = new Properties();
         InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream( fileName );

         try
         {
            if( inputStream != null )
            {
               theProps.load( inputStream );
            }
         }
         catch( IOException e )
         {
            System.err.println( "exception during property loading: " + e );
         }
         _myProperties.put( fileName, theProps );
      }

      return theProps;
   }

   /**
    * Just for testing.
    */
   public static void main( String[] args )
   {
      Properties theProps = PropertiesReader.getInstance().getProperties( "test.properties" );
      System.out.println( "The props 1 is: " + theProps ); 
      theProps = null;
      theProps = PropertiesReader.getInstance().getProperties( "test.properties" );
      System.out.println( "The props 2 is: " + theProps ); 
   }

}
