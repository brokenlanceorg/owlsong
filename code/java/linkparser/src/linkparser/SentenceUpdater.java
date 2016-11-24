package linkparser;

import java.sql.*;
import java.util.*;
import java.io.*;

import database.*;
import common.*;

/**
 * 
 */
public class SentenceUpdater extends DBBase
{

   private boolean _isInitialized;
   private String _sentenceFileName;
   private long _serialNumber; // could use a BigInteger here

   /**
    * Default
    */
   public SentenceUpdater()
   {
      Properties props = System.getProperties();
      StringBuffer fileName = new StringBuffer( props.getProperty( "user.home" ) );
      fileName.append( "\\UserData\\Sentence.txt" );
      _sentenceFileName = fileName.toString();

      if( !_isInitialized )
      {
         executeUpdate( "use semantical" );
         _isInitialized = true;
      }

      _serialNumber = -1;
   }

   /**
    *
    */
   protected void getSerialNumber()
   {
      if( _serialNumber == -1 )
      {
         ResultSet resultSet = executeQuery( "select max( serial_number ) from context_library" );

         try
         {
            if( resultSet.next() )
            {
               _serialNumber = resultSet.getLong( "max( serial_number )" );
            }
         }
         catch( SQLException e )
         {
            System.out.println( "Caught an exception retrieving serial_number: " + e );
         }
         finally
         {
            closeResultSet( resultSet );
         }

      }
      _serialNumber++;

   }

   /**
    *  Updates the database with the data found in the output sentence file.
    */
   protected void updateContextLibrary( String context, double linkage )
   {
      String sqlStatement = "insert into context_library values( ?, ?, ? )";
      Object[] objects = { new Long( _serialNumber ), context, new Double( linkage ) };
      executeUpdate( sqlStatement, objects );
   }

   /**
    *  Updates the database with the data found in the output sentence file.
    */
   protected void updateConstituents( String constituent, String inflected )
   {
      String sqlStatement = "insert into constituents values( ?, ?, ? )";
      Object[] objects = { constituent, inflected, new Long( _serialNumber ) };
      executeUpdate( sqlStatement, objects );
   }

   /**
    *  Updates the database with the data found in the output sentence file.
    */
   protected void updateConstituentsLeft( String constituent, String domains,
                                          String linkType, String linkedConstituent )
   {
      String sqlStatement = "insert into constituents_left values( ?, ?, ?, ?, ? )";
      Object[] objects = { constituent, domains, linkType, 
                           linkedConstituent, new Long( _serialNumber ) };
      executeUpdate( sqlStatement, objects );
   }

   /**
    *  Updates the database with the data found in the output sentence file.
    */
   protected void updateConstituentsRight( String constituent, String domains,
                                          String linkType, String linkedConstituent )
   {
      String sqlStatement = "insert into constituents_right values( ?, ?, ?, ?, ? )";
      Object[] objects = { constituent, domains, linkType, 
                           linkedConstituent, new Long( _serialNumber ) };
      executeUpdate( sqlStatement, objects );
   }

   /**
    *  Updates the database with the data found in the output sentence file.
    */
   public void updateDatabase()
   {
      getSerialNumber();
      common.FileReader inputFile = new common.FileReader( _sentenceFileName );
      Vector words = inputFile.getVectorOfWords();
      int state = 0;
      String word = null;
      String constituent = null;
      String inflected = null;
      String linkType = null;
      String linkedConstituent = null;
      String domainLinks = null;

      for( Iterator iter = words.iterator(); iter.hasNext(); )
      {

         switch( state )
         {
            case 0 :

               word = (String)iter.next();
               // found the context of the sentce
               String context = word;

               // the next word is the linkage
               word = (String)iter.next();

               double linkage = 0;
               try 
               {
                  linkage = Double.parseDouble( word );
               }
               catch( NumberFormatException e )
               {
                  System.out.println( "Caught exception getting linkage: " + e );
                  System.exit( 1 );
               }

               updateContextLibrary( context, linkage );
               word = (String)iter.next();
               state = 1;

            break;

            case 1 :

               constituent = word;
               inflected = (String)iter.next();
               updateConstituents( constituent, inflected );
               state = 2;

            break;

            case 2 :

               linkType = null;
               linkedConstituent = null;
               domainLinks = null;

               word = (String)iter.next();

               if( "LEFT_LINKS".equals( word ) )
               {
                  linkType = (String)iter.next();
                  word = (String)iter.next();
                  if( "DOMAIN_LINKS".equals( word ) )
                  {
                     domainLinks = (String)iter.next();
                     linkedConstituent = (String)iter.next();
                  }
                  else
                  {
                     linkedConstituent = word;
                  }

                  updateConstituentsLeft( constituent, domainLinks, 
                                          linkType, linkedConstituent );
               }
               // This code really doesn't get hit, 
               // but leaving it just in case we need the right links later
               else if( "RIGHT_LINKS".equals( word ) )
               {
                  linkType = (String)iter.next();
                  word = (String)iter.next();
                  if( "DOMAIN_LINKS".equals( word ) )
                  {
                     domainLinks = (String)iter.next();
                     linkedConstituent = (String)iter.next();
                  }
                  else
                  {
                     linkedConstituent = word;
                  }

                  updateConstituentsRight( constituent, domainLinks, 
                                           linkType, linkedConstituent );
               }
               else
               {
                  // if neither matched, then it must be a new constituent
                  state = 1;
               }

            break;

         } // end switch
      }
   }

}
