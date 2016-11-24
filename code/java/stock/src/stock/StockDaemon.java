package stock;

import java.util.*;
import java.io.*;

public class StockDaemon
{

   private boolean _useWeight = false;

   public StockDaemon()
   {
      _useWeight = Boolean.getBoolean( "regular" );
   } // end StockDaemon

   private void sleep( int waitLength )
   {
      try 
      { 
         Thread.sleep( waitLength ); 
      } // end try
      catch( InterruptedException ie ) 
      {
         System.err.println( "Caught exception during sleep: " + ie );
         ie.printStackTrace();
         System.exit( 1 );
      } // end catch
   } // end sleep

   private boolean checkTime()
   {
      boolean isGood = false;
      GregorianCalendar theCalendar = new GregorianCalendar();
      int hour = theCalendar.get( Calendar.HOUR_OF_DAY ); 
      //if( hour >= 20 && hour < 21 ) // 8 to 9
      if( hour >= 21 && hour < 22 ) // 9 to 10
      //if( hour >= 19 && hour < 20 ) // 1 to 2
      {
         isGood = true;
      } // end if good
 
      return isGood;
   } // end checkTime

   private void executeCommand( String theCommand )
   {
       
      try
      {
//System.out.println( "About to execute..." );
         Process theProc = Runtime.getRuntime().exec( theCommand );
//System.out.println( "Got the runtime..." );
         InputStream theStream = theProc.getInputStream();
//System.out.println( "Got the input stream..." );
         int i = 0;
         while( theStream.read() != -1 )
         {
             if( i++ == 30 )
                 return;
//System.out.print( "." );
         } // end while
      }
      catch( IOException ioe )
      {
         System.err.println( "Caught an io exception: " + ioe );
         ioe.printStackTrace();
      }
   } // end executeCommand

   /**
    * The method is as follows:
    *
    *   1) Start the dialer
    *   2) Wait until connect
    *   3) Download that day's data
    *   4) Close the dialer
    *   5) Make full_set.zip from sp500hst.txt
    *   6) Copy full_set.zip to day.zip
    *   7) Calculate cValues
    *
    */
   private void performDownload()
   {
      System.out.println( "Performing Download and Calculations...." );

      executeCommand( "StartDialer.bat" );
System.out.println( "About to sleep" );
      sleep( 60000 ); // sleep long enough for dialer to start
System.out.println( "Finished sleeping" );
      SP500Downloader theLoader = new SP500Downloader( null, null, null );
      //theLoader.downloadAllData();
      theLoader.downloadUpdate();
      executeCommand( "StopDialer.bat" );
      System.out.println( "Downloaded the data" );

      SP500Formatter theFormatter = new SP500Formatter();
      theFormatter.formatSP500Data();
      System.out.println( "Formatted the data" );

      executeCommand( "ZipData.bat" );
      System.out.println( "Ziped the data" );

      String month = Integer.toString( theLoader.getMonth() );
      String day = Integer.toString( theLoader.getDay() );
      String year = Integer.toString( theLoader.getYear() );

      executeCommand( "CopyZip.bat " + month + " " + day + " " + year );
      System.out.println( "copied Ziped data" );

      StockDriver theDriver = new StockDriver( month, day, year, _useWeight );
      theDriver.outputHTML();
   } // end performDownload

   public void daemon()
   {
      while( true )
      {
         //sleep( 3600000 ); // sleep for an hour
         sleep( 6000 );
         if( checkTime() == true )
         {
            performDownload();
         } // end iff
      } // end while loop
   } // end daemon

   public static void main( String args[] )
   {
      StockDaemon theDaemon = new StockDaemon();
      theDaemon.daemon();
   } // end main

} // end class StockDaemon
