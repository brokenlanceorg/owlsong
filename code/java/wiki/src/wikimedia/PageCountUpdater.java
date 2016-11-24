package wikimedia;

import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 *
 */
public class PageCountUpdater
{
   public WikiDatabaseUpdater _db        = null;
   public String              _fileName  = "pagecounts-";
   public String              _dateIndex = null;
   public String              _timeIndex = null;

   /**
    *
    */
   public PageCountUpdater( String dateIndex, String timeIndex )
   {
      _dateIndex  = dateIndex;
      _timeIndex  = timeIndex;
      _fileName  += ( _dateIndex + "-" + _timeIndex );
      _db         = new WikiDatabaseUpdater( _dateIndex, _timeIndex );


      System.out.println( "PageCountUpdater will use file location: " + _fileName );
   }

   /**
    *
    */
   public void updateDatabase()
   {
      try
      {
         BufferedReader reader = new BufferedReader( new java.io.FileReader( _fileName ) );
         String         line   = null;
//          Pattern        p      = Pattern.compile( "^([^ ]*) ([^ ]*) ([^ ]*) .*$");
         Pattern        p      = Pattern.compile( "^([^ ]*) (DELL) ([^ ]*) .*$");
         Matcher        m      = null;

         while( ( line = reader.readLine() ) != null )
         {
            m = p.matcher( line );
            if( m.matches() )
            {
               System.out.println( "match: " + line );
               _db.updateDatabase( m.group( 1 ), m.group( 2 ), m.group( 3 ) );
            }
         }

         reader.close();
      }
      catch( IOException e )
      {
         System.err.println( "Caught exception during pagecount parsing: " + e );
      }
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public static void main( String[] args )
   {
      PageCountUpdater updater = new PageCountUpdater( args[ 0 ], args[ 1 ] );
      updater.updateDatabase();
   }

}
