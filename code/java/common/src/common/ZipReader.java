/**
 * File   : ZipReader.java
 * Author : Brandon Benham
 * Date   : 2/20/01
 *
 */

package common;

import java.util.*;
import java.io.*;
import java.util.zip.*;

/**
 * This class has the ability to read and unzip zip or jar files
 */
public class ZipReader
{

   private ZipInputStream   _theZipStream = null;
   private HashMap          _theData      = null;
   private ArrayList        _fileNames    = null; // of String

   /**
    * Constructor : Given a filename, the constructor will open it
    *             : and read in the uncompressed text data
    * @param String The file name of the zip or jar file to open
    */
   public ZipReader( String fileName )
   {
      try 
      {
         if( fileName != null )
         {
            FileInputStream temp = new FileInputStream( fileName );
            BufferedInputStream temp2 = new BufferedInputStream( temp );
            _theZipStream = new ZipInputStream( temp2 );
            loadData();             
         } // end if not null
      } 
      catch( IOException e )
      {
         System.out.println( "Error opening zip file: " + e );
      } // end catch
   } // end constructor

   protected void loadData() throws IOException
   {
      ZipEntry theEnt = null;
      _theData        = new HashMap();
      _fileNames      = new ArrayList();

      while( (theEnt = _theZipStream.getNextEntry()) != null )
      {
         int theSize = (int)theEnt.getSize();
         byte[] bArray = new byte[ theSize ];
         int count = 0;
         int readSize = 0;
         _fileNames.add( theEnt.getName() );

         while( theSize - count > 0 )
         {
            if( (readSize = _theZipStream.read( bArray, count, theSize - count )) == -1 )
               break;
            count += readSize;
         } // end while

         /*
         int len = bArray.length;
         StringBuffer theString = new StringBuffer( len );
         for( int i=0; i<len; i++ )
         {
            theString.append( (char)(bArray[i] & 255) );
         } // end for

         _theData.put( theEnt.getName(), theString.toString() );
         */
         _theData.put( theEnt.getName(), bArray );
           
      } // end while
   } // end loadData
 
   public ArrayList getFileNames()
   {
      return _fileNames;
   } // end getFileNames

   public String getLastFileName()
   {
      return (String)_fileNames.get( _fileNames.size() - 1 );
   } // end getLastFileName

   /**
    * Public method getDataMap : Returns the HashMap that contains the textual
    *                          : data contained in the zip or jar file.
    * @return HashMap Whose keys are the individual file names and whose values
    *                 are the data contained in those files.
    */
   public HashMap getDataMap()
   {
      return _theData;
   } // end getDataMap

   /**
    * Public method writeData : Unconpresses the text files to disk
    */
   public void writeData()
   {
      try 
      {
         //BufferedWriter  theWriter = null;
         BufferedOutputStream  theWriter = null;
         Collection theColl = _theData.values();
         Iterator   theIter = theColl.iterator();
         Set        theSet  = _theData.keySet();
         Iterator   keyIter = theSet.iterator();
    
         while( theIter.hasNext() )
         {
            String fileName = (String)keyIter.next();
            //theWriter = new BufferedWriter( new FileWriter( fileName ) );
            theWriter = new BufferedOutputStream( new FileOutputStream( fileName ) );
            //String theStr = (String)theIter.next();
            byte[] theStr = (byte[])theIter.next();
            theWriter.write( theStr );
            theWriter.close();
         } // end while
   
      }
      catch( IOException e )
      {
         System.out.println( "Error writing data to file!" + e );
      } // end catch
   } // end writeData

   /**
    * Public method main : Testing purposes only
    */
   public static void main( String[] args )
   {
      if( args.length >= 1 )
      {
         ZipReader test = new ZipReader( args[0] );
         System.out.println( test.getLastFileName() );
         if( args.length >= 2 )
            test.writeData();
      } // end if args
   } // end main

} // end class
