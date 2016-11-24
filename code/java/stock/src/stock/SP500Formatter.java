package stock;

import common.*;
import java.io.*;

public class SP500Formatter
{
   String[] _theSecurities = null;
   String   _theSeparator = null;

   public SP500Formatter()
   {
      _theSeparator = System.getProperty( "file.separator" );
      common.FileReader theFile = new common.FileReader( "SP500" + _theSeparator + "SP500.dat" );
      _theSecurities = theFile.getArrayOfWords();
   } // end constructor

   public void updateDatabase()
   {
      common.FileReader theInputFile  = null;
      String[]          theInputData  = null;
      DatabaseUpdater   theDBUpdater  = new DatabaseUpdater();

      try
      {
         for( int i=0; i<_theSecurities.length; i++ )
         {
            theInputFile = new common.FileReader( "SP500" + _theSeparator + 
               _theSecurities[i] + ".csv", "," );
            theInputData = theInputFile.getArrayOfWords();
            int offset = 6;
            int adjOffset = 0;
            
            if( theInputData.length < 6 )
            {
               continue;
            }

            // Look for the adjusted close tag in lines such as:
            // Date,Open,High,Low,Close,Volume,Adj Close

            // This call will only create the table if it doesn't already exist:
            theDBUpdater.createTable( _theSecurities[i] );

            if( theInputData[6].indexOf( "Adj" ) != -1 )
            {
               offset = 7;
               adjOffset = 1;
            }

            for( int j=offset; j<theInputData.length; j+=offset )
            {
               theDBUpdater.updateDatabase( _theSecurities[i],
                                            theInputData[j],
                                            theInputData[j+1],
                                            theInputData[j+2],
                                            theInputData[j+3],
                                            theInputData[j+4],
                                            theInputData[j+5],
                                            theInputData[j+6] );
            } // end inner for loop

            // do some GCing
            System.gc();
            Thread.yield();
            if( ((i+1) % 100) == 0 )
               try { Thread.sleep( 10 ); } catch( InterruptedException ie ) {}

         } // end for loop
      }
      finally
      {
      } // end catch
   } // end formatSP500Data

   public void formatSP500Data()
   {
      BufferedWriter    theOutputFile = null;
      common.FileReader theInputFile  = null;
      String[]          theInputData  = null;
      StringBuffer      theOutputLine = null;

      try
      {
         theOutputFile = new BufferedWriter( new FileWriter( "SP500" + 
            _theSeparator + "sp500hst.txt" ) );

         for( int i=0; i<_theSecurities.length; i++ )
         {
            // TODO:
            // taking this out temporarily since we may want this in the DB
            // But, will need to figure out what to do with these...
            //removeDividends( _theSecurities[i] );
            theInputFile = new common.FileReader( "SP500" + _theSeparator + 
               _theSecurities[i] + ".csv", "," );
            theInputData = theInputFile.getArrayOfWords();
            int offset = 6;
            int adjOffset = 0;
            
            // Look for the adjusted close tag in lines such as:
            // Date,Open,High,Low,Close,Volume,Adj Close

            if( theInputData[6].indexOf( "Adj" ) != -1 )
            {
               offset = 7;
               adjOffset = 1;
            }

/*
System.out.println( _theSecurities[i] + " Data line: " + theInputData[0] + " 0 " 
                                  + theInputData[1] + " 1 " 
                                  + theInputData[2] + " 2 "
                                  + theInputData[3] + " 3 "
                                  + theInputData[4] + " 4 "
                                  + theInputData[5] + " 5 "
                                  + theInputData[6] + " 6 "
                                  + theInputData[7] + " 7 " );
*/

            for( int j=(theInputData.length-1); j>offset; j-=offset )
            {
               theOutputLine = new StringBuffer( theInputData[j-5-adjOffset] );
               theOutputLine.append( "," );
               theOutputLine.append( _theSecurities[i] );
               theOutputLine.append( "," );
               theOutputLine.append( theInputData[j-4-adjOffset] );
               theOutputLine.append( "," );
               theOutputLine.append( theInputData[j-3-adjOffset] );
               theOutputLine.append( "," );
               theOutputLine.append( theInputData[j-2-adjOffset] );
               theOutputLine.append( "," );
               theOutputLine.append( theInputData[j-1-adjOffset] );
               theOutputLine.append( "," );
               theOutputLine.append( theInputData[j-adjOffset] );
               theOutputLine.append( "\n" );
               theOutputFile.write( theOutputLine.toString() );
            } // end inner for loop
            System.gc();
            Thread.yield();
            if( ((i+1) % 100) == 0 )
               try { Thread.sleep( 10 ); } catch( InterruptedException ie ) {}
            //System.out.println( "Formatted: " + (i + 1) );
         } // end for loop
         theOutputFile.close();
      }
      catch( IOException ioe )
      {
         System.out.println( "Caught an IOException: " + ioe );
      } // end catch
   } // end formatSP500Data

   private void removeDividends( String security )
   {
      BufferedWriter    theOutputFile = null;
      common.FileReader theInputFile  = null;
      String[]          theInputData  = null;
      StringBuffer      theOutputLine = null;

      try
      {
         theInputFile = new common.FileReader( "SP500" + _theSeparator + security + ".csv", "\n" );
         theInputData = theInputFile.getArrayOfWords();
         theOutputFile = new BufferedWriter( new FileWriter( "SP500" + _theSeparator + 
            security + ".csv" ) );

         for( int j=0; j<theInputData.length; j++ )
         {
            if( theInputData[j].length() < 29 )
            {
               continue;
            }
            theOutputFile.write( theInputData[j] );
            theOutputFile.write( "\n" );
         } // end inner for loop
         System.gc();
         Thread.yield();
         theOutputFile.close();
      }
      catch( IOException ioe )
      {
         System.err.println( "Caught an IOException: " + ioe );
      } // end catch
   }

   public void updateDividendDatabase()
   {
      common.FileReader theInputFile  = null;
      String[]          theInputData  = null;
      DatabaseUpdater   theDBUpdater  = new DatabaseUpdater();

      try
      {
         for( int i=0; i<_theSecurities.length; i++ )
         {
            theInputFile = new common.FileReader( "SP500" + _theSeparator + 
               _theSecurities[i] + ".csv", "," );
            theInputData = theInputFile.getArrayOfWords();
            int offset = 2;
            
            // if there's no data, go to next
            if( theInputData.length < 2 )
            {
               continue;
            }

            // Look for the adjusted close tag in lines such as:
            // Date,Dividends
            // Date,Open,High,Low,Close,Volume,Adj Close

            // This call will only create the table if it doesn't already exist:
            theDBUpdater.createDividendTable( _theSecurities[ i ] );

//             if( theInputData[6].indexOf( "Adj" ) != -1 )
//             {
//                offset = 7;
//             }

            for( int j=offset; j<theInputData.length; j+=offset )
            {
               theDBUpdater.updateDividendDatabase( _theSecurities[ i ],
                                                    theInputData[ j ],
                                                    theInputData[ j + 1 ] );
            } // end inner for loop

            // do some GCing
            System.gc();
            Thread.yield();
            if( ((i+1) % 100) == 0 )
               try { Thread.sleep( 10 ); } catch( InterruptedException ie ) {}

         } // end for loop
      }
      finally
      {
      } // end catch

   } // end updateDividendDatabase

   public static void main( String args[] )
   {
      boolean database   = Boolean.getBoolean( "database" );
      boolean isDividend = Boolean.getBoolean( "div" );

      SP500Formatter theFormatter = new SP500Formatter();

      if( database )
      {
         theFormatter.updateDatabase();
      }
      else if( isDividend )
      {
         theFormatter.updateDividendDatabase();
      }
      else
      {
         theFormatter.formatSP500Data();
      }
   } // end main

} // end SP500Formatter
