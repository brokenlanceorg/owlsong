package wikimedia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import database.DBBase;

/**
 * 
 */
public class WikiDatabaseUpdater extends DBBase
{
   public String _dateIndex;
   public String _timeIndex;

   /**
    * Default constructor. We will automatically use the finance database.
    */
   public WikiDatabaseUpdater( String date, String time )
   {
      super( "finance" );
      _dateIndex = date;
      _timeIndex = time;
   }

   /**
    * 
    */
   public void updateDatabase( String project, 
                               String page,
                               String count )
   {
      System.out.println( "Will begin update for pagecount: project: " + 
                          project + " page: " + page + " count: " + count );

      Object[] values = { _dateIndex, _timeIndex, project, page, count };
      executeUpdate( "insert into pagecounts values ( ?, ?, ?, ?, ? )", values );
      closeStatement();
   }

}
