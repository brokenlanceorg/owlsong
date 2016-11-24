package stock;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import database.DBBase;

/**
 * Note the following stocks had to change names because of keyword confict:
 * ALL = ALLT
 * IN = INXX
 * INT = INTTT
 * KEY = KEYY
 * KEYS = KEYSY
 * 
 */
public class DatabaseUpdater extends DBBase
{

   /**
    * Default constructor. We will automatically use the finance database.
    */
   public DatabaseUpdater()
   {
      super( "finance" );
   }

   /**
    * Here, we create the table for the given security name.
    * 
    * The fields in the raw data from Yahoo appears in the format:
    *   Date Open High Low Close Volume Adj Close
    * 
    * The format of the security table is as follows:
    *   Date     DATE
    *   Open     DOUBLE(5,2)
    *   High     DOUBLE(5,2)
    *   Low      DOUBLE(5,2)
    *   Close    DOUBLE(5,2)
    *   Volume   BIGINT
    *   AdjClose DOUBLE(5,2)
    * 
    */
   public void createTable( String stockName )
   {
      if( !doesTableExist( stockName ) )
      {
         System.out.println( "Will create table for: " + stockName );
      }

      // create our array that holds the stock name
      Object[] values = { stockName };

      // first enter this stock into our securities table:
      executeUpdate( "insert into securities values (?)", values );
      closeStatement();

      // next create the table:
      String createString = "create table " + 
                            stockName + 
                            " ( date Date PRIMARY KEY, open DOUBLE(6,2), " + 
                            "high DOUBLE(6,2), low DOUBLE(6,2), " + 
                            "close DOUBLE(6,2), volume BIGINT unsigned, " +
                            "adjClose DOUBLE(6,2) )";
      executeUpdate( createString );
      closeStatement();
   }

   /**
    * Here, we create the table for the given security name.
    * 
    * The fields in the raw data from Yahoo appears in the format:
    *   Date Open High Low Close Volume Adj Close
    * 
    * The format of the security table is as follows:
    *   Date     DATE
    *   Open     DOUBLE(5,2)
    *   High     DOUBLE(5,2)
    *   Low      DOUBLE(5,2)
    *   Close    DOUBLE(5,2)
    *   Volume   BIGINT
    *   AdjClose DOUBLE(5,2)
    * 
    */
   public void createDividendTable( String stockName )
   {
      if( !doesTableExist( stockName ) )
      {
         System.out.println( "Will create div table for: " + stockName );
      }

      // create our array that holds the stock name
      Object[] values = { stockName };

      // first enter this stock into our securities table:
      executeUpdate( "insert into securities values (?)", values );
      closeStatement();

      // next create the table:
      String createString = "create table " + 
                            stockName + 
                            "_DIV ( date Date PRIMARY KEY, dividend DOUBLE(6,2) )";
      executeUpdate( createString );
      closeStatement();
   }

   /**
    * Updates the database for the given stock name and file name.
    */
   public void updateDatabase( String name, 
                               String date,
                               String open,
                               String high,
                               String low,
                               String close,
                               String volume,
                               String adjClose )
   {
      Object[] values = { date, open, high, low, close, volume, adjClose };
      executeUpdate( "insert into " + name + " values ( ?, ?, ?, ?, ?, ?, ? )", values );
      closeStatement();
      /*
      System.out.println( "Will begin update for table: " + 
                          name + " date: " + date + " open " + open + 
                          " high: " + high + " low: " + low + " close: " + 
                          close + " volume: " + volume + " adjClose: " + adjClose );
      */
   }

   /**
    * Updates the database for the given stock name and file name.
    */
   public void updateDividendDatabase( String name, 
                                       String date,
                                       String dividend )
   {
      Object[] values = { date, dividend };
      executeUpdate( "insert into " + name + "_DIV values ( ?, ? )", values );
      closeStatement();
      /*
      System.out.println( "Will begin update for table: " + 
                          name + " date: " + date + " open " + open + 
                          " high: " + high + " low: " + low + " close: " + 
                          close + " volume: " + volume + " adjClose: " + adjClose );
      */
   }

   /**
    * Updates the database for the given data for the table OPTIONS.
    * +--------------+-------------+------+-----+---------+-------+
    * | Field        | Type        | Null | Key | Default | Extra |
    * +--------------+-------------+------+-----+---------+-------+
    * | stock_symbol | varchar(6)  | YES  |     | NULL    |       |
    * | option_delta | double      | YES  |     | NULL    |       |
    * | stock_delta  | double      | YES  |     | NULL    |       |
    * | option_depth | tinyint(4)  | YES  |     | NULL    |       |
    * | days_exipre  | smallint(6) | YES  |     | NULL    |       |
    * | in_money     | tinyint(1)  | YES  |     | NULL    |       |
    * | date         | date        | YES  |     | NULL    |       |
    * +--------------+-------------+------+-----+---------+-------+
    */
   public void updateOptions( String  stock_symbol, 
                              double  option_delta,
                              double  stock_delta,
                              int     option_depth,
                              int     days,
                              boolean in_money )
   {
      Object[] values = { stock_symbol, 
                          option_delta, 
                          stock_delta, 
                          option_depth, 
                          days, 
                          in_money, 
                          new Date() };
      executeUpdate( "insert into OPTIONS (stock_symbol, option_delta, stock_delta, option_depth, days_exipre, in_money, date ) values ( ?, ?, ?, ?, ?, ?, ? )", values );
      closeStatement();
   }

   /**
    * Returns the last date for which a download was made.
    * Note the following stocks had to change names because of keyword confict:
    * ALL = ALLT
    * IN = INXX
    * INT = INTTT
    * KEY = KEYY
    * KEYS = KEYSY
    */
   public String getLastDate( String stock )
   {
      ResultSet result = null;
      String lastDate = null;

      if( stock.equals( "ALL" ) )
      {
         stock = "ALLT";
      }
      else if( stock.equals( "IN" ) )
      {
         stock = "INXX";
      }
      else if( stock.equals( "INT" ) )
      {
         stock = "INTTT";
      }
      else if( stock.equals( "KEY" ) )
      {
         stock = "KEYY";
      }
      else if( stock.equals( "KEYS" ) )
      {
         stock = "KEYSY";
      }

      try
      {
         result = executeQuery( "select max( date ) from " + stock );
         if( result != null && result.next() )
         {
            lastDate = result.getString( 1 );
         }
      }
      catch( SQLException se )
      {
         System.err.println( "*** Caught exception getting last download date!!! --> " + se );
      }
      finally
      {
         closeResultSet( result );
      }

      if( lastDate == null )
      {
         lastDate = "1980-01-01";
      }

      return lastDate;
   }

   /**
    * Returns the last date for which a download was made.
    * Note the following stocks had to change names because of keyword confict:
    * ALL = ALLT
    * IN = INXX
    * INT = INTTT
    * KEY = KEYY
    * KEYS = KEYSY
    */
   public String getLastDividendDate( String stock )
   {
      ResultSet result = null;
      String lastDate = null;

      if( stock.equals( "ALL" ) )
      {
         stock = "ALLT";
      }
      else if( stock.equals( "IN" ) )
      {
         stock = "INXX";
      }
      else if( stock.equals( "INT" ) )
      {
         stock = "INTTT";
      }
      else if( stock.equals( "KEY" ) )
      {
         stock = "KEYY";
      }
      else if( stock.equals( "KEYS" ) )
      {
         stock = "KEYSY";
      }

      try
      {
         result = executeQuery( "select max( date ) from " + stock + "_DIV" );
         if( result != null && result.next() )
         {
            lastDate = result.getString( 1 );
         }
      }
      catch( SQLException se )
      {
         System.err.println( "*** Caught exception getting last download date!!! --> " + se );
      }
      finally
      {
         closeResultSet( result );
      }

      if( lastDate == null )
      {
         lastDate = "1980-01-01";
      }

      return lastDate;
   }

}
