package stock;

import java.util.*;
import java.io.*;
import java.net.*;
import java.security.*;
import common.*;

public class Tester
{
 
   common.FileReader   _theFile  = null;
   StockManager        _theMan   = null;

   public Tester()
   {
/*
      long time = System.currentTimeMillis();
      _theFile = new common.FileReader( "table.csv", "," );
      String[] theArray = _theFile.getArrayOfWords();
      StockElement theElement = new StockElement( "QQQ" );

      // typical line is:
      // 31-Aug-01,36.03,37.03,35.94,36.63,54295700

      for( int i=theArray.length; i>6; i-=6 )
      {
//System.out.println( "Adding: " + theArray[i-5] + theArray[i-4] + theArray[i-2] );
         theElement.addOpen(  theArray[ i - 5 ] );
         theElement.addHigh(  theArray[ i - 4 ] );
         theElement.addClose( theArray[ i - 2 ] );
      } // end for loop
      theElement.calculateCValues();
      System.out.println( "\nThe object is: \n" + theElement.toString() );

      /*
      StockElement theElement = null;
      _theMan = new StockManager( LoadEnum.LOAD_ALL_YEAR );
      theElement = _theMan.getStockElement( "BLL" );

      theElement.calculateCValues();

      System.out.println( "\nThe object is: \n" + theElement.toString() );

      System.out.println( "\n\nThe most actives are: " );
      Collection theColl = _theMan.getActiveCollection();
      Iterator theIter = theColl.iterator();
      while( theIter.hasNext() )
      {
         theElement = (StockElement)theIter.next();
         System.out.println( "\nThe object is: \n" + theElement.toString() );
      } // end while
      System.out.println( "The time is: " + (System.currentTimeMillis() - time) );
      */

   } // end default constructor

   public void downloadTest()
   {
      try
      {
            //URL theURL = new URL( "http://aacornsupport.sabre.com/VersionInfo.html" );
            URL theURL = new URL( "http://java.sun.com/products/jdk/1.2/docs/api/index.html" );
            HttpURLConnection theHTTP = (HttpURLConnection)(theURL.openConnection());
            Permission thePer = theHTTP.getPermission();
            BufferedWriter theOutputFile = 
                           new BufferedWriter( new FileWriter( "version.html" ) );

System.out.println( "using proxy: " + theHTTP.usingProxy() );
System.out.println( "the permission is: " + thePer );
System.out.println( "the permission actions are: " + thePer.getActions() );
System.out.println( "the getAllowUserInteraction: " + theHTTP.getAllowUserInteraction() );
            InputStream theStream = theURL.openStream();
            int iValue = theStream.read();
            while( iValue != -1 )
            {
               iValue = theStream.read();
               theOutputFile.write( iValue );
            } // end while
            theOutputFile.close();
      }
      catch( MalformedURLException me )
      {
         System.out.println( "Caught a MalformedURLException: " + me );
         System.exit( 1 );
      } // end catch
      catch( UnknownHostException unhoe )
      {
         System.out.println( "Ensure that the computer is connected to the Internet: " + unhoe );
         System.exit( 1 );
      } // end catch
      catch( IOException ioe )
      {
         System.out.println( "Caught an IOExcep: " + ioe );
         System.exit( 1 );
      } // end catch
   } // end downloadTest

   public void sleeperTest()
   {
      try
      {
         Thread.sleep( 120 );
         Process theProc = Runtime.getRuntime().exec( "temp.cmd" );
         InputStream theStream = theProc.getInputStream();
         while( theStream.read() != -1 );
      }
      catch( InterruptedException ignore )
      {
      }
      catch( IOException ig )
      {
      }
   } // end sleeperTest

   public static void main( String[] args )
   {
      Tester theTest = new Tester();
      theTest.sleeperTest();
      Properties theProps = System.getProperties();
      theProps.list( System.out );
   } // end main

} // end StockManager
