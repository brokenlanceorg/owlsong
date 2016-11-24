import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

public class DBTester
{

   private Connection _theConnection = null;
   private int _total = 0;

   public DBTester()
   {
      try
      {
         System.out.println( "About to get the driver..." );
         Class.forName( "com.mysql.jdbc.Driver" );
         System.out.println( "Got the driver..." );
      }
      catch( Exception e ) 
      {
         System.out.println( "Got an exception getting driver: " + e );
      }

      try
      {
         System.out.println( "About to get the connection..." );
         Connection theConnection = DriverManager.getConnection( "jdbc:mysql://localhost/test?user=mysql" );
         System.out.println( "Got the connection..." );
	 _theConnection = theConnection;
         System.out.println( "The URL is: " + _theConnection.getMetaData().getURL() );
      }
      catch( SQLException ex )
      {
         System.out.println( "The error is: " + ex.toString() );
         System.out.println( "The error message is: " + ex.getMessage() );
         System.out.println( "The SQLState is: " + ex.getSQLState() );
         System.out.println( "The Vendor Error is: " + ex.getErrorCode() );
      }
   }

   private void handleDataArray( ArrayList aList, int count )
   {
      if( count >= 1 )
      {
         System.out.println( "returning" );
         return;
      }
      else
      {
         System.out.println( "level: " + count );
      }

      HashSet temp = new HashSet();

      for( Iterator iter = ((HashSet)aList.get( count )).iterator(); iter.hasNext(); )
      {
         String value = (String)iter.next();
	 ResultSet resultset = null;
	 PreparedStatement thePrepState = null;

         try
         {
	    thePrepState = _theConnection.prepareStatement( "Select * from conceptuallyrelatedto where antecedent like ?" );
	    thePrepState.setObject( 1, "%" + value + "%" );
	    resultset = thePrepState.executeQuery();
   
            while( resultset != null && resultset.next() )
            {
               //System.out.println( "The data (1) is: " + resultset.getString( 1 ) );
               //System.out.println( "The data (2) is: " + resultset.getString( 2 ) );
               StringTokenizer tokenizer = new StringTokenizer( resultset.getString( 1 ) );
               while( tokenizer.hasMoreTokens() )
               {
                  temp.add( tokenizer.nextToken() );
               }
               tokenizer = new StringTokenizer( resultset.getString( 2 ) );
               while( tokenizer.hasMoreTokens() )
               {
                  temp.add( tokenizer.nextToken() );
               }
            }
         }
         catch( SQLException ex )
         {
            System.out.println( "Caught an exception: " + ex );
         }
         finally
         {
            if( resultset != null )
            {
               try
               {
                  resultset.close();
               }
               catch( SQLException ex ) {}
               resultset = null;
            }
            if( thePrepState != null )
            {
               try
               {
                  thePrepState.close();
               }
               catch( SQLException ex ) {}
                thePrepState = null;
            }
         }
      }

      aList.set( count, temp );
      handleDataArray( aList, count + 1 );
   }

   public void performQuery()
   {
      Statement statement = null;
      ResultSet resultset = null;
      HashSet theData = new HashSet();
      ArrayList theList = new ArrayList();
   
      try
      {
         statement = _theConnection.createStatement();
	 resultset = statement.executeQuery( "Select version(), sin( pi()/4);" );

         while( resultset != null && resultset.next() )
         {
            System.out.println( "The data (1) is: " + resultset.getString( 1 ) );
            System.out.println( "The data (2) is: " + resultset.getString( 2 ) );
         }

	 //int ret = statement.executeUpdate( "CREATE table test_table ( name varchar(20), data longtext);" );
         //System.out.println( "The executeUpdate response is: " + ret );
	 //int ret = statement.executeUpdate( "INSERT into test_table values( 'test1', 'Some test data...');" );
	 int ret = statement.executeUpdate( "use semantical" );
         //System.out.println( "The executeUpdate response is: " + ret );

	 PreparedStatement thePrepState = 
            _theConnection.prepareStatement( "Select * from conceptuallyrelatedto where antecedent like ?" );
	 thePrepState.setObject( 1, "%picnic%" );
	 resultset = thePrepState.executeQuery();

         // the data should actually be put in a set instead of a list:
         while( resultset != null && resultset.next() )
         {
            //System.out.println( "The data (1) is: " + resultset.getString( 1 ) );
            //System.out.println( "The data (2) is: " + resultset.getString( 2 ) );
            StringTokenizer tokenizer = new StringTokenizer( resultset.getString( 1 ) );
            while( tokenizer.hasMoreTokens() )
            {
               theData.add( tokenizer.nextToken() );
               _total++;
            }
            tokenizer = new StringTokenizer( resultset.getString( 2 ) );
            while( tokenizer.hasMoreTokens() )
            {
               theData.add( tokenizer.nextToken() );
               _total++;
            }
         }
         theList.add( theData );
         handleDataArray( theList, 0 );
      }
      catch( SQLException ex )
      {
         System.out.println( "Caught an exception: " + ex );
      }
      finally
      {
         if( resultset != null )
         {
            try
            {
               resultset.close();
            }
            catch( SQLException ex ) {}
            resultset = null;
         }
         if( statement != null )
         {
            try
            {
               statement.close();
            }
            catch( SQLException ex ) {}
            statement = null;
         }
      }

      int count = 0;
      int totalcount = 0;
      for( int i=0; i<theList.size(); i++ )
      {
         theData = (HashSet)theList.get( i );
         for( Iterator iter = theData.iterator(); iter.hasNext(); )
         {
            totalcount++;
            //System.out.println( iter.next() );
            //if( ((String)iter.next()).indexOf( "happy" ) != -1 )
            if( ((String)iter.next()).indexOf( "small" ) != -1 )
            {
               count++;
            }
         }
      }
      System.out.println( "The total count is: " + totalcount );
      System.out.println( "The count is: " + count );
   }

   public static void main( String[] args )
   {
      System.out.println( "In the main function..." );
      DBTester theTest = new DBTester();
      theTest.performQuery();
   }
}
