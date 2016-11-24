package database;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

/**
 * All classes that blah, blah
 *
 *
 */
public abstract class DBBase
{

   private Connection _theConnection;
   private Statement _theStatement;
   private String _executeString;
   private int _total;

   static
   {
      try
      {
         Class.forName( "com.mysql.jdbc.Driver" );
      }
      catch( Exception e ) 
      {
         System.out.println( "Caught an exception loading JDBC driver: " + e );
      }
   }

   /**
    * Base constructor loads the connection up front.
    */
   public DBBase()
   {
      try
      {
         _theConnection = DriverManager.getConnection( "jdbc:mysql://localhost/test?user=mysql" );
      }
      catch( SQLException ex )
      {
         System.out.println( "Caught an exception making DB connection: " + ex );
      }
   }

   /**
    * Base constructor loads the connection up front.
    */
   public DBBase( String dbName )
   {
      try
      {
         _theConnection = DriverManager.getConnection( "jdbc:mysql://localhost/" + dbName + "?user=root&password=13g@nd@1f" );
      }
      catch( SQLException ex )
      {
         System.out.println( "Caught an exception making DB connection: " + ex );
      }
   }

   /**
    * 
    */
   protected void setExecuteString( String executeString )
   {
      _executeString = executeString;
   }

   /**
    * 
    */
   protected void prepare( String executeString )
   {
      _executeString = executeString;
   }

   /**
    *  Simply execute update the passed in string.
    */
   protected int executeUpdate( String update )
   {
      int ret = -1;
      Statement statement = null;

      try
      {
         statement = _theConnection.createStatement();
         ret = statement.executeUpdate( update );
      }
      catch( SQLException ex )
      {
         System.out.println( "Caught an exception executing update: " + ex );
      }
      finally
      {
         _theStatement = statement;
      }

      return ret;
   }

   /**
    *  execute update with params for select statements.
    */
   protected int executeUpdate( String update, Object[] params )
   {
      PreparedStatement statement = null;
      int ret = 0;

      try
      {
	 statement = _theConnection.prepareStatement( update );

         for( int i=0; i<params.length; i++ )
         {
	    statement.setObject( (i + 1), params[ i ] );
         }

	 ret = statement.executeUpdate();
      }
      catch( SQLException ex )
      {
         System.out.println( "Caught an exception executing update: " + ex );
      }
      finally
      {
         _theStatement = statement;
      }

      return ret;
   }

   /**
    *  execute update with params for select statements.
    *  Important: don't forget to close the resultset!!!!
    */
   protected ResultSet executeQuery( String update, Object[] params )
   {
      PreparedStatement statement = null;
      ResultSet resultset = null;

      try
      {
	 statement = _theConnection.prepareStatement( update );

         for( int i=0; i<params.length; i++ )
         {
	    statement.setObject( (i + 1), params[ i ] );
         }

	 resultset = statement.executeQuery();
      }
      catch( SQLException ex )
      {
         System.out.println( "Caught an exception executing update: " + ex );
      }
      finally
      {
         _theStatement = statement;
      }

      return resultset;
   }

   /**
    *  execute update with params for select statements.
    *  Important: don't forget to close the resultset!!!!
    */
   protected ResultSet executeQuery( String update, String param )
   {
      Object[] array = { param };
      return executeQuery( update, array );
   }

   /**
    *  execute update with params for select statements.
    *  Important: don't forget to close the resultset!!!!
    */
   protected ResultSet executeQuery( String update )
   {
      Statement statement = null;
      ResultSet resultset = null;

      try
      {
	 statement = _theConnection.createStatement();
	 resultset = statement.executeQuery( update );
      }
      catch( SQLException ex )
      {
         System.out.println( "Caught an exception executing update: " + ex );
      }
      finally
      {
         _theStatement = statement;
      }

      return resultset;
   }

   /**
    *  
    *
    */
   public boolean doesTableExist( String table )
   {
      boolean doesExist = false;

      ResultSet resultSet = executeQuery( "desc " + table );

      try
      {
         if( resultSet == null )
         {
            doesExist = false;
         }
         else
         {
            doesExist = true;
         }
      }
      finally
      {
         closeResultSet( resultSet );
      }

      return doesExist;
   }

   /**
    * 
    */
   protected void closeStatement()
   {
      try
      {
         if( _theStatement != null )
         {
            _theStatement.close();
         }
      }
      catch( SQLException sqlx ) { /* ignore */ }
   }

   /**
    * Also closes the statement object.
    */
   protected void closeResultSet( ResultSet set )
   {
      try
      {
         if( set != null )
         {
            set.close();
         }
         if( _theStatement != null )
         {
            _theStatement.close();
         }
      }
      catch( SQLException sqlx ) { /* ignore */ }
   }
}
