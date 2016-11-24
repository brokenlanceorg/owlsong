import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import database.DBBase;

/**
 * This must be done by root on a new database such as "finance":
 * grant all privileges on finance to brandon@localhost;
 */
public class DBTest extends DBBase
{
   public DBTest()
   {
   }

   public DBTest( String dbName )
   {
      super( dbName );
   }

   public void createTable()
   {
      ResultSet result = null;
      try
      {
         System.out.println( "about to use the database..." );
         executeUpdate( "use finance" );
         System.out.println( "about to create the table..." );
         executeUpdate( "create table securities ( name varchar( 6 ) )" );
         System.out.println( "created the table" );
         executeUpdate( "commit" );
         System.out.println( "committed the table" );
      }
      catch( Exception e )
      {
         System.out.println( "Caught an Exception while going through result set: " + e );
      }
      finally
      {
         closeResultSet( result );
      }
   }

   public void performTestQuery()
   {
      ResultSet result = null;
      try
      {
         executeUpdate( "use test" );
         result = executeQuery( "select * from stocks where name like ?", "%PL%" );
   
         while( result != null && result.next() )
         {
            System.out.println( result.getString( "name" ) );
            System.out.println( result.getDouble( "price" ) );
         }

         System.out.println( "Checking for table stock: " + doesTableExist( "stocks" ) );
         System.out.println( "Checking for table birds: " + doesTableExist( "birds" ) );
      }
      catch( SQLException e )
      {
         System.out.println( "Caught Exception while going through result set: " + e );
      }
      finally
      {
         closeResultSet( result );
      }
   }

   public static void main( String[] args )
   {
      System.out.println( "In the main function..." );
      DBTest theTest = new DBTest( "finance" );
      theTest.performTestQuery();
      theTest.createTable();
   }
}
