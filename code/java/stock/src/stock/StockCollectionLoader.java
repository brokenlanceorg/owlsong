package stock;

import java.util.*;
import common.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import database.DBBase;

/**
 *
 */
public class StockCollectionLoader extends DBBase
{

   private ArrayList _theFileArray = null;
   private String[] _theDataArray  = null;
   private String _updateFileName  = "update.zip";
   private String _yearFileName    = "sp500hst.txt";
   private String _theSeparator    = null;
   private String _yearZipName     = null;

   // this string will be used to load the data for a given stock
   private static String _selectString = "select date, " +
                                                "open, " +
                                                "high, " +
                                                "low, " +
                                                "close, " +
                                                "volume, " +
                                                "adjClose " +
                                                "from ";

   private static String _watcherSelectString = "select strike, last_trade from WATCHER where stock = '";

   /**
    *
    */
   public StockCollectionLoader()
   {
      super( "finance" );
      _theSeparator    = System.getProperty( "file.separator" );
      _yearZipName     = "datafiles" + _theSeparator + "full_set.zip";
   } // end default constructor

   /**
    *
    */
   public StockCollectionLoader( boolean loadAll )
   {
      super( "finance" );
      _theSeparator    = System.getProperty( "file.separator" );
      _yearZipName     = "datafiles" + _theSeparator + "full_set.zip";

      if( loadAll )
         loadAllData();
      else
         loadData();
   } // end default constructor

   /**
    *
    */
   public StockCollectionLoader( String fileName )
   {
      super( "finance" );
      _theSeparator    = System.getProperty( "file.separator" );
      _yearZipName     = "datafiles" + _theSeparator + "full_set.zip";
      _updateFileName = fileName;
      loadData();
   } // end constructor

   /**
    *
    */
   protected void loadData()
   {
//      ZipReader theZip = new ZipReader( _updateFileName );
//      theZip.writeData();
//      String lastFile = theZip.getLastFileName();
//      theZip = new ZipReader( lastFile );
//      theZip.writeData();
//      lastFile = theZip.getLastFileName();
      FileReader theDataFile = new FileReader( _yearFileName, "," );
      _theDataArray = theDataFile.getArrayOfWords();
   } // end loadData

   /**
    *
    */
   protected void loadAllData()
   {
//      ZipReader theZip = new ZipReader( _updateFileName );
//      theZip.writeData();
//      ArrayList fileNames = theZip.getFileNames();
//      _theFileArray = new ArrayList();

//      for( int i=0; i<fileNames.size(); i++ )
//      {
//         theZip = new ZipReader( (String)fileNames.get(i) );
//         theZip.writeData();
//         String lastFile = theZip.getLastFileName();
         FileReader theDataFile = new FileReader( _yearFileName, "," );
         _theDataArray = theDataFile.getArrayOfWords();
         _theFileArray.add( _theDataArray );
//      } // end while
 
   } // end loadAllData

   /**
    *
    */
   public void loadDividends( StockCollection theCollection )
   {
      common.FileReader theSecFile = new common.FileReader( "SP500" + _theSeparator + "SP500.dat" );
      String[] theSecurities = theSecFile.getArrayOfWords();
      StockElement theElement = null;

      for( int i=0; i<theSecurities.length; i++ )
      {
         common.FileReader theData = new common.FileReader( "SP500" + 
            _theSeparator + theSecurities[i] + ".csv", "," );
         String[] theWords = theData.getArrayOfWords();
         theElement = new StockElement( theSecurities[i] );

         for( int j=2; j<theWords.length; j+=2 )
         {
            if( j+1 < theWords.length )
            {
               theElement.addDividend( theWords[j], theWords[j+1] );
                // System.out.println( theSecurities[i] + " " + theWords[j] + " " + theWords[j+1] );
            }
         }

         // last thing:
         theCollection.addElement( theElement );
      }
   } // end loadUpdate

   /**
    *
    */
   public void loadUpdate( StockCollection theCollection )
   {
      for( int i=0; i<_theDataArray.length; i+=6 )
      {
         theCollection.update( _theDataArray[i], _theDataArray[i+1], _theDataArray[i+2], _theDataArray[i+4] );
      } // end for loop
   } // end loadUpdate

   /**
    *
    */
   public void loadAllUpdate( StockCollection theCollection )
   {
      for( int i=0; i<_theFileArray.size(); i++ )
      {
         String[] theDataArray = (String[])_theFileArray.get( i );
         for( int j=0; j<theDataArray.length; j+=6 )
         {
            theCollection.update( theDataArray[j], theDataArray[j+1], theDataArray[j+2], theDataArray[j+4] );
         } // end inner for
      } // end for loop
   } // end loadUpdate

   /**
    *
    */
   public void loadConfidence( StockCollection theCollection )
   {
      FileReader theConfFile = new FileReader( "confidence.dat" );
      String[] wordArray = theConfFile.getArrayOfWords();

      for( int i=0; i<wordArray.length; i+=2 )
      {
         theCollection.update( wordArray[i].trim(), wordArray[i+1] );
      } // end for
   } // end loadConfidence

   /**
    *
    */
   public void loadPreviousConfidence( StockCollection theCollection )
   {
      FileReader theConfFile = new FileReader( "PrevConfidence.dat" );
      String[] wordArray = theConfFile.getArrayOfWords();
      
      for( int i=0; i<wordArray.length; i+=2 )
      {
         theCollection.updatePrevious( wordArray[i].trim(), wordArray[i+1] );
      } // end for
   } // end loadPreviousConfidence

   /**
    *
    */
   public void loadDays( StockCollection theCollection )
   {
      FileReader theDayFile = new FileReader( "days.dat" );
      String[] wordArray = theDayFile.getArrayOfWords();
      StockElement theElement = null;
      String       theWord    = null;
      //boolean      firstDay   = false;

      for( int i=0; i<wordArray.length; i++ )
      {
         theWord = wordArray[i];
         if( theWord.length() > 1 || !Character.isDigit( theWord.charAt( 0 ) ) )
         {
            if( theElement != null )
            {
               theElement.setLastDay();
            } // end if not null
            theElement = theCollection.getStockElement( theWord );
            //firstDay = true;
         } // end if stock name
         else
         {
            if( theElement != null /* && !firstDay */ )
            {
               theElement.addDayAbove( theWord );
            } // end if good
            //firstDay = false;
         } // end if day value
      } // end for
   } // end loadDays

   /**
    *
    */
   public void loadYear( StockCollection theCollection )
   {
//      ZipReader theZip = new ZipReader( _yearZipName );
//      theZip.writeData();
//      theZip = null;

//System.out.println( "The filename is: " + _yearFileName );

      FileReader theYearFile  = new FileReader( _yearFileName, "," );
      String[] theDataArray   = theYearFile.getArrayOfWords();
      StockElement theElement = null;
      String       theWord    = null;
      int          position   = 0;
      // A typical entry in the file looks like:
      // 20001130, A, 53, 53.1875, 50.625, 52.1875, 35288
      theYearFile = null;

      for( int j=0; j<theDataArray.length; j+=7 )
      {
         if( j == 700000 )
         //if( j == 70000 )
         {
            //System.out.println( "Cleaning up memory..." );
            String[] newArray = new String[ theDataArray.length - j ];
            position = 0;
            for( int k=j; k<theDataArray.length; k++ )
            {
               newArray[ position++ ] = theDataArray[ k ];
            } // end for
            theDataArray = newArray;
            j = 0;
            System.gc();
            Thread.yield();
            try { Thread.sleep( 100 ); } catch( InterruptedException e ) {}
         } // end if
//System.out.println( theDataArray[j+1] + " " + theDataArray[j+2] + " " + theDataArray[j+6] );
         theCollection.update( theDataArray[j  ], theDataArray[j+1], theDataArray[j+2], 
                               theDataArray[j+3], theDataArray[j+4], 
                               theDataArray[j+5], theDataArray[j+6] );
      } // end inner for
   } // end loadYear

   /**
    *
    */
   public ArrayList getSP1000StockNames()
   {
      ArrayList<String> theNames = new ArrayList<String>();
      ResultSet result = null;

      try
      {
         result = executeQuery( "select name from SP1000 order by name" );
   
         while( result != null && result.next() )
         {
            theNames.add( result.getString( "name" ) );
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

      return theNames;
   }

   /**
    *
    */
   public ArrayList getSP1500StockNames()
   {
      ArrayList<String> theNames = new ArrayList<String>();
      ResultSet result = null;

      try
      {
         result = executeQuery( "select name from SP1500 order by name" );
   
         while( result != null && result.next() )
         {
            theNames.add( result.getString( "name" ) );
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

      return theNames;
   }

   /**
    *
    */
   public ArrayList getSP500StockNames()
   {
      ArrayList<String> theNames = new ArrayList<String>();
      ResultSet result = null;

      try
      {
         result = executeQuery( "select name from SP500 order by name" );
   
         while( result != null && result.next() )
         {
            theNames.add( result.getString( "name" ) );
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

      return theNames;
   }

   /**
    *
    */
   public ArrayList getStockNames()
   {
      ArrayList<String> theNames = new ArrayList<String>();
      ResultSet result = null;

      try
      {
         result = executeQuery( "select name from securities order by name" );
   
         while( result != null && result.next() )
         {
            theNames.add( result.getString( "name" ) );
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

      return theNames;
   }

   /**
    *
    */
   public ArrayList getNasdaq100StockNames()
   {
      ArrayList<String> theNames = new ArrayList<String>();
      ResultSet result = null;

      try
      {
         result = executeQuery( "select name from NASDAQ100 order by name" );
   
         while( result != null && result.next() )
         {
            theNames.add( result.getString( "name" ) );
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

      return theNames;
   }

   /**
    *
    */
   public ArrayList getLiquidStockNames()
   {
      ArrayList<String> theNames = new ArrayList<String>();
      ResultSet result = null;

      try
      {
         result = executeQuery( "select name from LIQUID order by name" );
   
         while( result != null && result.next() )
         {
            theNames.add( result.getString( "name" ) );
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

      return theNames;
   }

   /**
    *
    */
   public ArrayList getDJIAStockNames()
   {
      ArrayList<String> theNames = new ArrayList<String>();
      ResultSet result = null;

      try
      {
         result = executeQuery( "select name from DJIA order by name" );
   
         while( result != null && result.next() )
         {
            theNames.add( result.getString( "name" ) );
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

      return theNames;
   }

   /**
    *
    */
   public ArrayList getRussell1000StockNames()
   {
      ArrayList<String> theNames = new ArrayList<String>();
      ResultSet result = null;

      try
      {
         result = executeQuery( "select name from RUSSELL1000 order by name" );
   
         while( result != null && result.next() )
         {
            theNames.add( result.getString( "name" ) );
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

      return theNames;
   }

   /**
    *
    */
   public ArrayList getRussell2000StockNames()
   {
      ArrayList<String> theNames = new ArrayList<String>();
      ResultSet result = null;

      try
      {
         result = executeQuery( "select name from RUSSELL2000 order by name" );
   
         while( result != null && result.next() )
         {
            theNames.add( result.getString( "name" ) );
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

      return theNames;
   }

   /**
    *
    */
   public ArrayList getRussell3000StockNames()
   {
      ArrayList<String> theNames = new ArrayList<String>();
      ResultSet result = null;

      try
      {
         result = executeQuery( "select name from RUSSELL3000 order by name" );
   
         while( result != null && result.next() )
         {
            theNames.add( result.getString( "name" ) );
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

      return theNames;
   }

   /**
    *
    */
   public ArrayList getWatchedNames()
   {
      ArrayList<String> theNames = new ArrayList<String>();
      ResultSet result = null;

      try
      {
         result = executeQuery( "select stock from WATCHER order by stock" );
   
         while( result != null && result.next() )
         {
            theNames.add( result.getString( "stock" ) );
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

      return theNames;
   }

   /**
    *
    */
   public StockElement getStockElement( String name )
   {
      StockElement theElement = new StockElement( name );
      ResultSet result = null;

      try
      {
         result = executeQuery( _selectString + name + " order by date" );
   
         while( result != null && result.next() )
         {
            theElement.addDate( result.getString( "date" ) );
            theElement.addOpen( result.getString( "open" ) );
            theElement.addHigh( result.getString( "high" ) );
            theElement.addLow( result.getString( "low" ) );
            theElement.addClose( result.getString( "close" ) );
            theElement.addVolume( result.getString( "volume" ) );
            theElement.addAdjClose( result.getString( "adjClose" ) );
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

      try
      {
         result = executeQuery( "select date, dividend from " + name + "_DIV order by date" );
   
         while( result != null && result.next() )
         {
            theElement.addDividendDate( result.getString( "date" ) );
            theElement.addDividend( result.getString( "dividend" ) );
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

      return theElement;
   }

   /**
    *
    */
   public void loadWatcherData( StockElement stock )
   {
      ResultSet result = null;

      try
      {
         result = executeQuery( _watcherSelectString + stock.getName() + "'" );
   
         while( result != null && result.next() )
         {
            stock.setStrikePrice( result.getString( "strike" ) );
            stock.setPreviousTrade( result.getString( "last_trade" ) );
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
   }

   /**
    *
    */
   public void updateWatchedStockElement( StockElement stock )
   {
      Object[] values = { stock.getLastClose(), stock.getName() };
      executeUpdate( "update WATCHER set last_trade = ? where stock = ?", values );
      closeStatement();
   }

   /**
    *
    */
   public void insertWatchedStockElement( StockElement stock, double delta )
   {
      Object[] values = { stock.getName(), stock.getLastClose() * delta, stock.getLastClose() };
      executeUpdate( "insert into WATCHER values ( ?, ?, ? )", values );
      closeStatement();
   }

   /**
    *
    */
   public void clearWatchedStockElements()
   {
      executeUpdate( "delete from WATCHER" );
      closeStatement();
   }

} // end class StockCollectionLoader
