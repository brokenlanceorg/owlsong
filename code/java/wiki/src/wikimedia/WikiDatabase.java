package wikimedia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import database.DBBase;

/**
 * 
 */
public class WikiDatabase extends DBBase
{
   private static String _selectString = "select count from pagecounts where date = '";


   /**
    * Default constructor. We will automatically use the finance database.
    */
   public WikiDatabase( String date, String time )
   {
      super( "finance" );
   }

   /**
    * 
    */
   public int getPageCount( String date, 
                            String time,
                            String project,
                            String page )
   {
      System.out.println( "Will select for pagecount: project: " + 
                          project + " page: " + page );

      ResultSet result = null;
      int       count  = -1;

      try
      {
         result = executeQuery( _selectString + date + "'" + " order by date" );
   
         while( result != null && result.next() )
         {
            count = Integer.parseInt( result.getString( "count" ) );
         }

      }
      catch( SQLException e )
      {
         System.err.println( "Caught Exception while going through result set: " + e );
      }
      finally
      {
         closeResultSet( result );
      }

      return count;
   }

}
