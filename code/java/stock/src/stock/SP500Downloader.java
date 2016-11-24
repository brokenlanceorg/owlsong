package stock;

import java.net.*;
import java.io.*;
import java.util.*;
import java.security.*;
import common.*;

public class SP500Downloader
{
   String[]       _theSecurities  = null;
   StringBuffer   _theDateString  = null;
   //String         _theEndString   = "&x=.csv";
   //*        &y=0&g=d&ignore=.csv
   // http://table.finance.yahoo.com/table.csv?s=QQQ&a=02&b=10&c=1999&d=02&e=2&f=2004&g=w&ignore=.csv
   // http://chart.yahoo.com/table.csv?BMET&a=2&b=2&c=1999&d=2&e=2&f=2004&g=w&ignore=.csv
   //                                &g=w&ignore=.csv
   //String         _theEndString   = "&g=w&ignore=.csv";
   String         _theEndString   = "&g=d&ignore=.csv";
   String         _theStartString = "http://table.finance.yahoo.com/table.csv?s=";
   String         _theSeparator = null;
   //String         _theUpdateString = "http://finance.yahoo.com/d/quotes.csv?s=";
   //String         _theUpdateString = "http://finance.yahoo.com/q?s="; //http://finance.yahoo.com/q?s=jcp
   //                                 http://table.finance.yahoo.com/table.csv?s=
   String         _theUpdateString = "http://table.finance.yahoo.com/table.csv?s=";
   boolean        _loadAll         = false;
   int            _month           = 0;
   int            _day             = 0;
   int            _year            = 0;
   int            _monthAdj        = 0;
   int            _yearAdj         = 1;
   String         _theMonth        = null;
   String         _theDay          = null;
   String         _theYear         = null;

   /**
    * Downloads data from yahoo from the URL:
    * http://chart.yahoo.com/table.csv?a=8&b=9&c=2003&d=9&e=10&f=2003&s=msft&y=0&g=d&ignore=.csv
    * http://chart.yahoo.com/table.csv?s=adbe&a=10&b=18&c=2001&d=1&e=19&f=2002&g=d&q=q&y=0&z=adbe&x=.csv
    * The latest URL format is the following:
    * http://chart.yahoo.com/table.csv?a=9&b=17&c=2001&d=9&e=17&f=2002&s=rx&y=0&g=d&ignore=.csv
    *
    * The latest(3/2/04) URL format is the following:
    * http://table.finance.yahoo.com/table.csv?s=QQQ&a=02&b=10&c=1999&d=02&e=2&f=2004&g=w&ignore=.csv
    *
    * The update URL is:
    * http://finance.yahoo.com/d/quotes.csv?s=r   &f=sl1d1t1c1ohgv&e=.csv
    * http://finance.yahoo.com/d/quotes.csv?s=msft&f=sl1d1t1c1ohgv&e=.csv
    */
   public SP500Downloader( String month, String day, String year, boolean loadAll )
   {
      _loadAll = loadAll;
      _theSeparator = System.getProperty( "file.separator" );
      String securityFile = "SP500" + _theSeparator + "SP500.dat";
      common.FileReader theSecFile = new common.FileReader( securityFile );
      _theSecurities = theSecFile.getArrayOfWords();
      _theMonth = month;
      _theDay = day;
      _theYear = year;
      formatDateString( null );
   } // end constructor

   public SP500Downloader( String month, String day, String year )
   {
      this( month, day, year, false ); 
   } // end constructor

   private void formatDateString( String stockName )
   {
      DatabaseUpdater theDB = new DatabaseUpdater();
      String lastDate = "";

      if( stockName != null )
      {
         lastDate = theDB.getLastDate( stockName );
      }

      String startYearString = "1980";
      String startMonthString = _theMonth;
      int theMonth = Integer.parseInt( _theMonth );
      int startMonth = theMonth;
      String startDayString = _theDay;

      if( !_loadAll && stockName != null )
      {
         System.out.println( "The stock name is: " + stockName );
         int position = lastDate.indexOf( "-" );
         int endPosition = lastDate.lastIndexOf( "-" );
         startYearString = lastDate.substring( 0, position );
         startMonthString = lastDate.substring( position + 1, endPosition );
         startDayString = lastDate.substring( endPosition + 1 );
         startMonth = Integer.parseInt( startMonthString );

         System.out.println( "The start year is: " + startYearString );
         System.out.println( "The start Month is: " + startMonthString );
         System.out.println( "The start Day is: " + startDayString );
      }

      _theDateString = new StringBuffer( "&a=" );
      _theDateString.append( startMonth - 1 );
      _theDateString.append( "&b=" );
      _theDateString.append( startDayString );
      _theDateString.append( "&c=" );
      _theDateString.append( startYearString );
      _theDateString.append( "&d=" );
      _theDateString.append( theMonth - 1 );
      _theDateString.append( "&e=" );
      _theDateString.append( _theDay );
      _theDateString.append( "&f=" );
      _theDateString.append( _theYear );
   }

   private void formatDividendDateString( String stockName )
   {
      DatabaseUpdater theDB = new DatabaseUpdater();
      String lastDate = "";

      if( stockName != null )
      {
         lastDate = theDB.getLastDividendDate( stockName );
      }

      String startYearString = "1980";
      String startMonthString = _theMonth;
      int theMonth = Integer.parseInt( _theMonth );
      int startMonth = theMonth;
      String startDayString = _theDay;

      if( !_loadAll && stockName != null )
      {
         System.out.println( "The stock name is: " + stockName );
         int position = lastDate.indexOf( "-" );
         int endPosition = lastDate.lastIndexOf( "-" );
         startYearString = lastDate.substring( 0, position );
         startMonthString = lastDate.substring( position + 1, endPosition );
         startDayString = lastDate.substring( endPosition + 1 );
         startMonth = Integer.parseInt( startMonthString );

         System.out.println( "The start year is: " + startYearString );
         System.out.println( "The start Month is: " + startMonthString );
         System.out.println( "The start Day is: " + startDayString );
      }

      _theDateString = new StringBuffer( "&a=" );
      _theDateString.append( startMonth - 1 );
      _theDateString.append( "&b=" );
      _theDateString.append( startDayString );
      _theDateString.append( "&c=" );
      _theDateString.append( startYearString );
      _theDateString.append( "&d=" );
      _theDateString.append( theMonth - 1 );
      _theDateString.append( "&e=" );
      _theDateString.append( _theDay );
      _theDateString.append( "&f=" );
      _theDateString.append( _theYear );
   }

   private void getDate()
   {
      Calendar theCal = new GregorianCalendar();
      _month          = theCal.get( Calendar.MONTH ) + 1;
      _day            = theCal.get( Calendar.DAY_OF_MONTH );
      _year           = theCal.get( Calendar.YEAR );
   } // end getDate

   public String getMonthString()
   {
      String month = "" + _month;
      if( month.length() == 1 )
      {
         month = "0" + month;
      }
      return month;
   }

   public String getDayString()
   {
      String day = "" + _day;
      if( day.length() == 1 )
      {
         day = "0" + day;
      }
      return day;
   }

   /* http://chart.yahoo.com/table.csv?
    *        a=8&b=9&c=2003&d=9&e=10&f=2003&s=
    *        msft
    *        &y=0&g=d&ignore=.csv
    *
    * http://chart.yahoo.com/table.csv?
    *        a=8&b=9&c=2003&d=9&e=10&f=2003&s=
    *        msft
    *        &y=0&g=d&ignore=.csv
    *
    * http://chart.yahoo.com/table.csv?a=8&b=9&c=2003&d=9&e=10&f=2003&s=msft&y=0&g=d&ignore=.csv
    * Dividends:
    * http://ichart.finance.yahoo.com/table.csv?s=QQQ&a=02&b=10&c=1999&d=11&e=7&f=2011&g=v&ignore=.csv
    */
   public void downloadAllData()
   {
      URL                theURL        = null;
      BufferedWriter     theOutputFile = null;
      BufferedReader     theReader     = null;
      InputStream        theStream     = null;
      String             value         = null;

      try
      {
         for( int i=0; i<_theSecurities.length; i++ )
         {
            formatDateString( _theSecurities[i] );
            theURL = new URL( _theStartString   + 
                              _theSecurities[i] + 
                              _theDateString    + 
                              _theEndString );
//System.out.println( theURL.toString() );
            theOutputFile = new BufferedWriter( new FileWriter( "SP500" + 
               _theSeparator + _theSecurities[i] + ".csv" ) );
            try 
            {
               theStream = theURL.openStream();
            } // end catch
            catch( IOException ioe )
            {
               System.err.println( "Caught an IOExcep: " + ioe  + " on security: " + _theSecurities[i] );
               continue;
            } // end catch
            theReader = new BufferedReader( new InputStreamReader( theStream ) );
            while( (value = theReader.readLine()) != null )
            {
               theOutputFile.write( value + "\n" );
            } // end while
            theOutputFile.close();
            theReader.close();
            if( ((i+1) % 100) == 0 )
            {
               System.gc();
               Thread.yield();
               try { Thread.sleep( 10 ); } catch( InterruptedException ie ) {}
            }
            System.out.println( "Downloader: " + (i + 1) );
         } // end for loop
      }
      catch( MalformedURLException me )
      {
         System.err.println( "Caught a MalformedURLException: " + me );
try { Thread.sleep( 60000 ); } catch( InterruptedException ie ) {}
         System.exit( 1 );
      } // end catch
      catch( UnknownHostException unhoe )
      {
         System.err.println( "Ensure that the computer is connected to the Internet: " + unhoe );
try { Thread.sleep( 60000 ); } catch( InterruptedException ie ) {}
         System.exit( 1 );
      } // end catch
      catch( IOException ioe )
      {
         System.err.println( "Caught an IOExcep: " + ioe );
try { Thread.sleep( 60000 ); } catch( InterruptedException ie ) {}
         System.exit( 1 );
      } // end catch
   } // end downloadAllData

   public int getMonth()
   {
      return _month;
   } // end getMonth

   public int getDay()
   {
      return _day;
   } // end getDay

   public int getYear()
   {
      return _year;
   } // end getYear

   private int findIndex( String[] lines, String data )
   {
      int index = -1;

      for( int j=0; j<lines.length; j++ )
      {
         if( lines[j].indexOf( data ) != -1 )
         {
            index = j;
            break;
         }
      } // end for loop
      
      return index;
   }

   private int search( String theStock, String searchString ) throws IOException
   {
      int value = -1;
      common.FileReader theInputFile = 
           new common.FileReader( "SP500" + _theSeparator + "quotes.htm", "," );
      String[] theWords = theInputFile.getArrayOfWords();

      //System.out.println( "\nabout to get data....:" );

      //try { Thread.sleep( 5000 ); } catch( InterruptedException ie ) {}

      BufferedWriter theWriter = new BufferedWriter( new FileWriter( "output.txt", true ) );

      //System.out.println( "\nGetting data for: " + theStock );

      for( int j=0; j<theWords.length; j++ )
      {
         if( theWords[j].indexOf( searchString ) != -1 )
         {
            //System.out.println( " found: " + searchString );
            //System.out.println( " in: " + theWords[j] );
            //try { Thread.sleep( 9000 ); } catch( InterruptedException ie ) {}
            value = j;
            int position = theWords[ value ].indexOf( "right");
            String theDate = theWords[ value ].substring( position + 7, position + 16 );
            theWriter.write( theStock + " " + theDate + "\n" );
            System.out.println( theStock + " " + theDate + "\n" );
            break;
         }
      } // end for loop

      theWriter.flush();
      theWriter.close();

      return value;
   }

   private void updateDataFile( String theStock ) throws IOException
   {
      common.FileReader theInputFile = 
           new common.FileReader( "SP500" + _theSeparator + "quotes.htm", "`" );
      String[] theWords = theInputFile.getArrayOfWords();

      //System.out.println( "Getting data for: " + theStock );

      // open and close are on line 32
      // day's range is on line 33
      /*
      for( int j=0; j<theWords.length; j++ )
      {
         //if( theWords[j].indexOf( "Volume" ) != -1 )
         if( theWords[j].indexOf( "Open:" ) != -1 )
         //if( theWords[j].indexOf( "Day's Range" ) != -1 )
         //if( theWords[j].indexOf( "Last Trade" ) != -1 )
         {
            System.out.println( j + " is: " + theWords[j] + " of: " + theWords.length );
         }
      } // end for loop
      */

      int offset = findIndex( theWords, "Open:" );
      // Need to parse out the open, high, low, close, and volume:
      int start = theWords[offset].indexOf( "tabledata1" );

      // if no data:
      if( start == -1 )
      {
         System.out.println( "Returning! No DATA!" );
         return;
      }

      int end = theWords[offset].indexOf( "</td>", start );
      String open = theWords[offset].substring( start + 12, end );

      if( open.equals( "N/A" ) )
      {
         open = "0";
      }

      //System.out.println( " the open is: " + open );

      offset = findIndex( theWords, "Last Trade:" );
      start = theWords[offset].indexOf( "tabledata1" );
      end = theWords[offset].indexOf( "</b>", start );
      String close = theWords[offset].substring( start + 20, end );

      if( close.equals( "N/A" ) )
      {
         close = "0";
      }

      //System.out.println( " the close is: " + close );

      offset = findIndex( theWords, "Day's Range:" );
      start = theWords[offset].indexOf( "tabledata1" );
      end = theWords[offset].indexOf( " - ", start );
      String low = theWords[offset].substring( start + 12, end );

      if( low.equals( "N/A" ) )
      {
         low = "0";
      }

      //System.out.println( " the low is: " + low );

      start = theWords[offset].indexOf( " - ", start );
      end = theWords[offset].indexOf( "</td>", start );
      String high = theWords[offset].substring( start + 3, end );

      if( high.equals( "N/A" ) )
      {
         high = "0";
      }

      //System.out.println( " the high is: " + high );

      common.FileReader theDataFile = 
           new common.FileReader( "SP500" + _theSeparator + theStock + ".csv" );
      String[] theDataWords = theDataFile.getArrayOfWords();

      BufferedWriter theOutputFile = new BufferedWriter( 
                new FileWriter( "SP500" + _theSeparator + theStock + ".csv" ) );
 
//System.out.println( "data is0: " + theDataWords[0] );
//System.out.println( "data is1: " + theDataWords[1] );
//try { Thread.sleep( 6000 ); } catch( InterruptedException ie ) {}

      String ending = "";

      if( theDataWords[0].indexOf( "Adj." ) != -1 )
      {
         ending = ",1";
      }

      StringBuffer outputLine = new StringBuffer( "" + _day + "-" + _month + "-" + _year );
      outputLine.append( "," );
      outputLine.append( open );
      outputLine.append( "," );
      outputLine.append( high );
      outputLine.append( "," );
      outputLine.append( low );
      outputLine.append( "," );
      outputLine.append( close );
      outputLine.append( "," );
      outputLine.append( "10000000" );
      outputLine.append( ending );

      for( int i=0; i<theDataWords.length; i++ )
      {
         if( i == 1 )
         {
            theOutputFile.write( outputLine.toString() );
            theOutputFile.write( "\n" );
         } // end if first line
         theOutputFile.write( theDataWords[i] );
         theOutputFile.write( "\n" );

      } // end for
      theOutputFile.flush();
      theOutputFile.close();

   } // end updateDataFile

   // http://finance.yahoo.com/d/quotes.csv?s=msft&f=sl1d1t1c1ohgv&e=.csv
   /**
    * This now gets the data from yahoo detailed quote:
    *   http://finance.yahoo.com/q?s=amd&d=t
    */
   public void downloadUpdate()
   {
      URL                theURL        = null;
      BufferedWriter     theOutputFile = null;
      BufferedReader     theReader     = null;
      InputStream        theStream     = null;
      String             value         = null;
      //String             theEndString  = "&d=t";
      String             theEndString  = "";

      try
      {
         for( int i=0; i<_theSecurities.length; i++ )
         {
            theURL = new URL( _theUpdateString   + 
                              _theSecurities[i] + 
                              theEndString );
            HttpURLConnection theHTTP = (HttpURLConnection)(theURL.openConnection());
            //theOutputFile = new 
               //BufferedWriter( new FileWriter( "SP500\\" + _theSecurities[i] + ".csv", true ) );
            theOutputFile = new 
               BufferedWriter( new FileWriter( "SP500" + _theSeparator + "quotes.htm" ) );
            theStream = theURL.openStream();
            theReader = new BufferedReader( new InputStreamReader( theStream ) );

            while( (value = theReader.readLine()) != null )
            {
               theOutputFile.write( value + "\n" );
            } // end while
            theOutputFile.close();
            theReader.close();

            updateDataFile( _theSecurities[i] );

            if( ((i+1) % 100) == 0 )
            {
               System.gc();
               Thread.yield();
               try { Thread.sleep( 10 ); } catch( InterruptedException ie ) {}
            }

            System.out.println( "Downloader: " + (i + 1) );

         } // end for loop
      }
      catch( MalformedURLException me )
      {
         System.err.println( "Caught a MalformedURLException: " + me );
         System.exit( 1 );
      } // end catch
      catch( UnknownHostException unhoe )
      {
         System.err.println( "Ensure that the computer is connected to the Internet: " + unhoe );
         System.exit( 1 );
      } // end catch
      catch( IOException ioe )
      {
         System.err.println( "Caught an IOExcep: " + ioe );
         System.exit( 1 );
      } // end catch

   } // end downloadUpdate

   /**
    * Loops through the stocks in the company events page.
    * http://finance.yahoo.com/q/ce?s=CBK
    */
   public void downloadLoop()
   {
      URL                theURL        = null;
      BufferedWriter     theOutputFile = null;
      BufferedReader     theReader     = null;
      InputStream        theStream     = null;
      int                iValue        = 0;
      String             theEndString  = "http://finance.yahoo.com/q/ce?s=";
      String             theLine       = null;

      try
      {
         for( int i=0; i<_theSecurities.length; i++ )
         {
System.out.println( "About to create URL..." );

            theURL = new URL( theEndString + 
                              _theSecurities[i] + "\n" );

            HttpURLConnection theHTTP = (HttpURLConnection)(theURL.openConnection());
            theOutputFile = new 
               BufferedWriter( new FileWriter( "SP500" + _theSeparator + "quotes.htm" ) );

System.out.println( "About to connect..." );

            //System.out.println( "the http permission: " + theHTTP.getPermission() );
            //theStream = theURL.openStream();
            theHTTP.connect();

System.out.println( "About to open the data stream..." );

            theStream = theHTTP.getInputStream();
            theReader = new BufferedReader( new InputStreamReader( theStream ) );

            while( (theLine = theReader.readLine()) != null )
            {
               theOutputFile.write( theLine + "\n" );
            } // end while
            theOutputFile.close();

            String theString = new String( "Earnings announcement" );

            search( _theSecurities[i], theString );

            if( ((i+1) % 10) == 0 )
            {
               System.gc();
               Thread.yield();
               try { Thread.sleep( 10 ); } catch( InterruptedException ie ) {}
            }

            theHTTP.disconnect();
            theReader.close();

            System.out.println( "Searcher: " + (i + 1) );

         } // end for loop
         //try { Thread.sleep( 10000 ); } catch( InterruptedException ie ) {}
      }
      catch( MalformedURLException me )
      {
         System.err.println( "Caught a MalformedURLException: " + me );
         System.exit( 1 );
      } // end catch
      catch( UnknownHostException unhoe )
      {
         System.err.println( "Ensure that the computer is connected to the Internet: " + unhoe );
         System.exit( 1 );
      } // end catch
      catch( IOException ioe )
      {
         System.err.println( "Caught an IOExcep: " + ioe );
         try { Thread.sleep( 10000 ); } catch( InterruptedException ie ) {}
         System.exit( 1 );
      } // end catch

   } // end downloadUpdate

   /**
    * Loops through the stocks in the company events page.
    * http://finance.yahoo.com/q/ce?s=CBK
    */
   public void downloadSocketLoop()
   {
      BufferedWriter     theOutputFile = null;
      BufferedWriter     theOutputStream = null;
      BufferedReader     theStream     = null;
      String             value         = null;
      String             theEndString  = "/q/ce?s=";
      String             theCommand    = null;
      _theUpdateString = "finance.yahoo.com";
      Socket             theSocket     = null;

      try
      {
System.out.println( "About to create Socket..." );
         //theSocket     = new Socket( InetAddress.getByName( _theUpdateString ), 80 );
         theSocket     = new Socket( _theUpdateString, 80 );
System.out.println( "About to open the output stream..." );
         theOutputStream = new BufferedWriter( new OutputStreamWriter( theSocket.getOutputStream() ) );
System.out.println( "About to open the data stream..." );
         theStream = new BufferedReader( new InputStreamReader( theSocket.getInputStream() ) );

         for( int i=0; i<_theSecurities.length; i++ )
         {
            theCommand = "GET " + theEndString + _theSecurities[i] + "\n";
            theOutputStream.write( theCommand );
            theOutputStream.flush();

            theOutputFile = new 
               BufferedWriter( new FileWriter( "SP500" + _theSeparator + "quotes.htm" ) );

System.out.println( "About to open the data stream..." );

            value = theStream.readLine();
            while( value != null )
            {
               theOutputFile.write( value );
               value = theStream.readLine();
            } // end while
            theOutputFile.close();

            String theString = new String( "Earnings announcement" );

            search( _theSecurities[i], theString );

            if( ((i+1) % 100) == 0 )
            {
               System.gc();
               Thread.yield();
               //try { Thread.sleep( 10 ); } catch( InterruptedException ie ) {}
            }

            System.out.println( "Searcher: " + (i + 1) );

         } // end for loop
         //try { Thread.sleep( 10000 ); } catch( InterruptedException ie ) {}
      }
      catch( MalformedURLException me )
      {
         System.err.println( "Caught a MalformedURLException: " + me );
         try { Thread.sleep( 10000 ); } catch( InterruptedException ie ) {}
         System.exit( 1 );
      } // end catch
      catch( UnknownHostException unhoe )
      {
         System.err.println( "Ensure that the computer is connected to the Internet: " + unhoe );
         try { Thread.sleep( 10000 ); } catch( InterruptedException ie ) {}
         System.exit( 1 );
      } // end catch
      catch( IOException ioe )
      {
         System.err.println( "Caught an IOExcep: " + ioe );
         try { Thread.sleep( 10000 ); } catch( InterruptedException ie ) {}
         System.exit( 1 );
      } // end catch

   } // end downloadUpdate

   /**
    * News Headlines:
    * http://finance.yahoo.com/q/h?s=MSFT
    */
   public void downloadNews( String dayString )
   {
      URL                theURL        = null;
      BufferedWriter     theOutputFile = null;
      BufferedWriter     theResultFile = null;
      BufferedReader     theReader     = null;
      InputStream        theStream     = null;
      int                iValue        = 0;
      String             theEndString  = "http://finance.yahoo.com/q/h?s=";
      String             theLine       = null;

      try
      {
         theResultFile = new 
            //BufferedWriter( new FileWriter( "d:\\bb\\java\\stock\\results.txt" ) );
            BufferedWriter( new FileWriter( "results.txt" ) );

         for( int i=0; i<_theSecurities.length; i++ )
         {
            theURL = new URL( theEndString + 
                              _theSecurities[i] + "\n" );

            theOutputFile = new 
               BufferedWriter( new FileWriter( "SP500" + _theSeparator + "news.htm" ) );

            //System.out.println( "#######################About to connect..." );

            HttpURLConnection theHTTP = (HttpURLConnection)(theURL.openConnection());
            theHTTP.connect();
            //System.out.println( "#######################About to get input stram..." );

            theReader = new BufferedReader( new InputStreamReader( theHTTP.getInputStream() ) );

            //System.out.println( "#######################About to read..." );

            while( (theLine = theReader.readLine()) != null )
            {
               theOutputFile.write( theLine + "\n" );
            } // end while
            theOutputFile.close();

            int num = findHeadlines( dayString );

            System.out.println( _theSecurities[i] + " " + num );
            theResultFile.write( _theSecurities[i] + " " + num + "\n" );
            theResultFile.flush();

            theHTTP.disconnect();
            theReader.close();

            if( ((i+1) % 10) == 0 )
            {
               System.gc();
               Thread.yield();
               try { Thread.sleep( 10 ); } catch( InterruptedException ie ) {}
            }

            //System.out.println( "Searcher: " + (i + 1) );

         } // end for loop
         //try { Thread.sleep( 10000 ); } catch( InterruptedException ie ) {}
         theResultFile.close();
      }
      catch( MalformedURLException me )
      {
         System.err.println( "Caught a MalformedURLException: " + me );
         try { Thread.sleep( 10000 ); } catch( InterruptedException ie ) {}
         System.exit( 1 );
      } // end catch
      catch( UnknownHostException unhoe )
      {
         System.err.println( "Ensure that the computer is connected to the Internet: " + unhoe );
         try { Thread.sleep( 10000 ); } catch( InterruptedException ie ) {}
         System.exit( 1 );
      } // end catch
      catch( IOException ioe )
      {
         System.err.println( "Caught an IOExcep: " + ioe );
         try { Thread.sleep( 10000 ); } catch( InterruptedException ie ) {}
         System.exit( 1 );
      } // end catch
      finally
      {
         System.out.println( "in finally block....." );
         try { Thread.sleep( 6000 ); } catch( InterruptedException ie ) {}
         //System.exit( 1 );
      } // end catch

   } // end downloadNews

   private int findHeadlines( String dayString )
   {
System.out.println( "#######################################in findHeadlines..." );
      common.FileReader theReader = new common.FileReader();
      String theText = theReader.getFileAsString( "SP500" + _theSeparator + "news.htm" );

      int startPosition = theText.indexOf( dayString + ", 2003</b></td>" );

      if( startPosition == -1 )
      {
         return 0;
      }

      // if we get here, there must be an end somewhere.

      int endPosition = theText.indexOf( "2003</b></td>", startPosition + 12 );

      if( endPosition == -1 )
      {
         endPosition = theText.indexOf( "</table>", startPosition + 12 );
      }

      // We want to count these things: <td width="1%">

      String subtext = theText.substring( startPosition, endPosition );
      int count = 0;
      startPosition = 0;

      while( startPosition != -1 )
      {
         startPosition = subtext.indexOf( "<td width=\"1%\">", startPosition );
         if( startPosition != -1 )
         {
            count++;
            startPosition += 1;
         }
      }

      //System.out.println( "the count is: " + count );
      return count;

   }

   /**
    * News Headlines:
    * 
    * http://biz.yahoo.com/c/20031024/u.html
    * http://finance.yahoo.com/q/h?s=ATSN&t=2003-10-23
    */
   public void downloadNewsTime()
   {
      URL                theURL        = null;
      BufferedWriter     theOutputFile = null;
      BufferedWriter     theResultFile = null;
      BufferedReader     theReader     = null;
      InputStream        theStream     = null;
      int                iValue        = 0;
      String             theEndString  = "http://biz.yahoo.com/c/";
      String             theMidString  = "/u.html";
      String             theLine       = null;

      try
      {
         //theResultFile = new 
            //BufferedWriter( new FileWriter( "results.txt" ) );

            theURL = new URL( theEndString + 
                              _year + getMonthString() + getDayString() + 
                              theMidString );

            theOutputFile = new 
               BufferedWriter( new FileWriter( "SP500" + _theSeparator + "news.htm" ) );

            HttpURLConnection theHTTP = (HttpURLConnection)(theURL.openConnection());
            theHTTP.connect();

            theReader = new BufferedReader( new InputStreamReader( theHTTP.getInputStream() ) );

            while( (theLine = theReader.readLine()) != null )
            {
               theOutputFile.write( theLine + "\n" );
            } // end while
            theOutputFile.close();

            ArrayList theSecurities = getUpgrades();
            downloadUpgradeTimes( theSecurities );
/*
            System.out.println( _theSecurities[i] + " " + num );
            theResultFile.write( _theSecurities[i] + " " + num + "\n" );
            theResultFile.flush();
*/

            theHTTP.disconnect();
            theReader.close();

         //try { Thread.sleep( 10000 ); } catch( InterruptedException ie ) {}
         //theResultFile.close();
      }
      catch( MalformedURLException me )
      {
         System.err.println( "Caught a MalformedURLException: " + me );
         try { Thread.sleep( 10000 ); } catch( InterruptedException ie ) {}
         System.exit( 1 );
      } // end catch
      catch( UnknownHostException unhoe )
      {
         System.err.println( "Ensure that the computer is connected to the Internet: " + unhoe );
         try { Thread.sleep( 10000 ); } catch( InterruptedException ie ) {}
         System.exit( 1 );
      } // end catch
      catch( IOException ioe )
      {
         System.err.println( "Caught an IOExcep: " + ioe );
         try { Thread.sleep( 10000 ); } catch( InterruptedException ie ) {}
         System.exit( 1 );
      } // end catch
      finally
      {
         //System.out.println( "in finally block....." );
         //try { Thread.sleep( 6000 ); } catch( InterruptedException ie ) {}
         //System.exit( 1 );
      } // end catch

   } // end downloadNews

   private String findCallTime()
   {
      common.FileReader theReader = new common.FileReader();
      String theText = theReader.getFileAsString( "SP500" + _theSeparator + "quotes.htm" );

      int startPosition = theText.indexOf( _day + ", " + _year + "</b></td>" );

      if( startPosition == -1 )
      {
         return null;
      }

      int endPosition = theText.indexOf( "upgraded", startPosition + 12 );

      if( endPosition == -1 )
      {
         return null;
      }

      startPosition = theText.indexOf( " - ", endPosition );
      endPosition = theText.indexOf( " - ", startPosition + 1 );

      String subtext = (theText.substring( startPosition + 2, endPosition )).trim();

      return subtext;
   }

   private ArrayList getUpgrades()
   {
      ArrayList theUpgrades = new ArrayList();

      common.FileReader theReader = new common.FileReader();
      String theText = theReader.getFileAsString( "SP500" + _theSeparator + "news.htm" );

      int position = theText.indexOf( "More</td></tr><tr><td" );
      int next = -1;

      while( ( (next = theText.indexOf( "</a></td><td", position )) != -1 ) )
      {
         int position2 = next;
         while( theText.charAt( --position2 ) != '>' );
         theUpgrades.add( theText.substring( ++position2, next ) );
         position = next + 1;
      }

      return theUpgrades;
   }

   /**
    * News Headlines:
    * http://finance.yahoo.com/q/h?s=ATSN&t=2003-10-23
    */
   public void downloadUpgradeTimes( ArrayList theStocks )
   {
      URL                theURL        = null;
      BufferedWriter     theOutputFile = null;
      BufferedWriter     theResultFile = null;
      BufferedReader     theReader     = null;
      InputStream        theStream     = null;
      int                iValue        = 0;
      String             theEndString  = "http://finance.yahoo.com/q/h?s=";
      String             theMidString  = "&t=";
      String             theLine       = null;
      _theSecurities = getArray( theStocks );

      try
      {
         theResultFile = new 
            BufferedWriter( new FileWriter( "SP500" + _theSeparator + getMonthString() + "-" +
                                            getDayString() + "-" + _year + ".csv" ) );

         for( int i=0; i<_theSecurities.length; i++ )
         {
            theURL = new URL( theEndString + 
                              _theSecurities[i] +  
                              theMidString +
                              _year + "-" + getMonthString() + "-" + getDayString() + "\n" );

            theOutputFile = new 
               BufferedWriter( new FileWriter( "SP500" + _theSeparator + "quotes.htm" ) );

            HttpURLConnection theHTTP = (HttpURLConnection)(theURL.openConnection());
            theHTTP.connect();

            theReader = new BufferedReader( new InputStreamReader( theHTTP.getInputStream() ) );

            while( (theLine = theReader.readLine()) != null )
            {
               theOutputFile.write( theLine + "\n" );
            } // end while
            theOutputFile.close();

            theHTTP.disconnect();
            theReader.close();

            String callTime = findCallTime();

            theResultFile.write( _theSecurities[i] + "," + callTime + "\n" );

            if( ((i+1) % 10) == 0 )
            {
               System.gc();
               Thread.yield();
               try { Thread.sleep( 10 ); } catch( InterruptedException ie ) {}
            }

            System.out.println( "Searcher: " + (i + 1) );

         } // end for loop
         //try { Thread.sleep( 10000 ); } catch( InterruptedException ie ) {}
         theResultFile.close();
      }
      catch( MalformedURLException me )
      {
         System.err.println( "Caught a MalformedURLException: " + me );
         try { Thread.sleep( 10000 ); } catch( InterruptedException ie ) {}
         System.exit( 1 );
      } // end catch
      catch( UnknownHostException unhoe )
      {
         System.err.println( "Ensure that the computer is connected to the Internet: " + unhoe );
         try { Thread.sleep( 10000 ); } catch( InterruptedException ie ) {}
         System.exit( 1 );
      } // end catch
      catch( IOException ioe )
      {
         System.err.println( "Caught an IOExcep: " + ioe );
         try { Thread.sleep( 10000 ); } catch( InterruptedException ie ) {}
         System.exit( 1 );
      } // end catch
      finally
      {
         //System.out.println( "in finally block....." );
         //try { Thread.sleep( 6000 ); } catch( InterruptedException ie ) {}
         //System.exit( 1 );
      } // end catch

   } // end downloadNews

   private String[] getArray( ArrayList theArray )
   {
      String[] returnArray = new String[ theArray.size() ];

      for( int i=0; i<theArray.size(); i++ )
      {
         returnArray[i] = (String)theArray.get( i );
      }

      return returnArray;
   }

   private void parseLargeCapList( String page )
   {
      String token = "</A></font></td><td><font";
      int index = -1;
      int temp = 0;
      int start = 0;

      while( (index = page.indexOf( token, temp )) != -1 )
      {
         temp = index - 10;
         start = page.indexOf( ">", temp );
         System.out.println( page.substring( start + 1, index ) );
         temp = index + 1;
      }
   }

   /**
    * These dowload to spreadsheets:
    * http://ichart.finance.yahoo.com/table.csv?s=AAA&a=00&b=1&c=1960&d=09&e=26&f=2004&g=v&ignore=.csv
    * http://ichart.finance.yahoo.com/table.csv?s=AA&a=00&b=1&c=1960&d=09&e=26&f=2004&g=v&ignore=.csv
    * http://ichart.finance.yahoo.com/table.csv?s=QQQ&a=02&b=10&c=1999&d=11&e=7&f=2011&g=v&ignore=.csv
    */
   public void downloadDividends()
   {
      String base = "http://ichart.finance.yahoo.com/table.csv?s=";
//       String mid = "&a=00&b=1&c=1960";  // we'll just start from jan 1, 1960 regardless
      String tail = "&g=v&ignore=.csv";
      String value = null;
      URL theURL = null;
      BufferedWriter theOutputFile = null;
      BufferedReader theReader = null;

      for( int i=0; i<_theSecurities.length; i++ )
      {
         try
         {
            System.out.println( "Downloading: " + _theSecurities[i] );
            formatDividendDateString( _theSecurities[i] );
            theURL = new URL( base + 
                              _theSecurities[i] + 
                              _theDateString + 
                              tail );
            theOutputFile = new BufferedWriter( new FileWriter( "SP500" + _theSeparator + 
               _theSecurities[i] + ".csv" ) );
            theReader = new BufferedReader( new InputStreamReader( theURL.openStream() ) );
            while( (value = theReader.readLine()) != null )
            {
               theOutputFile.write( value + "\n" );
            } // end while
            theOutputFile.close();
            theReader.close();
         }
         catch( MalformedURLException me )
         {
            System.err.println( "MalformedURL Exception: " + me.toString() );
         }
         catch( IOException ie )
         {
            System.err.println( "IO Exception: " + ie.toString() );
//             i--;
         }
      } // end for loop
   } // end downloadDividends

   /**
    * http://screen.yahoo.com/b?mc=1000000000/&s=nm&db=stocks&vw=1
    * 
    * http://screen.yahoo.com/b?mc=1000000000/&s=nm&db=stocks&vw=1&b=21
    * 
    * http://screen.yahoo.com/b?mc=1000000000/&s=nm&db=stocks&vw=1&b=41
    * 
    * http://screen.yahoo.com/b?mc=1000000000/&s=nm&db=stocks&vw=1&b=61
    * 
    * Search for:
    * </A></font></td><td><font
    * then previous to ">" is the stock symbol
    */
   public void downloadLargeCapList()
   {
      String base = "http://screen.yahoo.com/b?mc=1000000000/&s=nm&db=stocks&vw=1&b=";
      try
      {
         for( int i=0; i<96; i++ )
         {
            URL theURL = new URL( base + (i * 20 + 1));
            InputStream theStream = theURL.openStream();
            BufferedReader theReader = new BufferedReader( new InputStreamReader( theStream ) );
            String value = null;
            StringBuffer thePage = new StringBuffer();
            
            while( (value = theReader.readLine()) != null )
            {
               thePage.append( value );
            } // end while
   
            theReader.close();
            parseLargeCapList( thePage.toString() );
         }
      }
      catch( MalformedURLException me )
      {
         System.err.println( "Caught malformed exception: " + me );
      }
      catch( IOException ioe )
      {
         System.err.println( "Caught IO exception: " + ioe );
      }
   }

   public static void main( String args[] )
   {
      boolean update = Boolean.getBoolean( "update" );
      boolean isSearch = Boolean.getBoolean( "search" );
      boolean isDividend = Boolean.getBoolean( "div" );
      boolean loadAll = Boolean.getBoolean( "loadAll" );

      SP500Downloader theDown = null;

      if( args.length == 3 )
      {
         theDown = new SP500Downloader( args[0], args[1], args[2], loadAll );
      }
      else
      {
         theDown = new SP500Downloader( null, null, null );
      }

      if( update )
      {
         //theDown.downloadUpdate();
         theDown.downloadLargeCapList();
      }
      else if( isSearch )
      {
         theDown.downloadNewsTime();
         //theDown.downloadNews( args[0] );
         //theDown.downloadLoop();
         //theDown.downloadSocketLoop();
      } // end else
      else if( isDividend )
      {
         theDown.downloadDividends();
      } // end else
      else 
      {
         theDown.downloadAllData();
      } // end else
   } // end main

} // end class SP500Downloader
