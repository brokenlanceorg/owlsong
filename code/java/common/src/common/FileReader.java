//******************************************************************************
// File       :    FileReader.java
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose    :    
//            :    
// Author     :    Brandon Benham
// Date       :    02/16/00
//******************************************************************************

package common;

import java.lang.*;
import java.io.*;
import java.util.*;

/**
 * This class is used to read words from a file, the words
 *  will be considered to be change values for pvc's, svc's, or ckts.
 *
 * @author		Brandon Benham
 * @version		1.0
 */
public class FileReader
{
    private Vector           _theWords = null;
    private RandomAccessFile _theFile  = null;
    private String           _theDelim = null;

   /**
     * Method: Constructor.
     * Purpose: Default.
     *          
     * 
     * @return		void.
     * @exception	IOException.
     * @throws		None.
     */
    public FileReader()
    {
    } // end constructor

    /**
     * Method: Constructor.
     * Purpose: Creates the FileReader object, also creates the RandomAccessFile object.
     *          
     * 
     * @param		String fileName.
     * @return		void.
     * @exception	IOException.
     * @throws		None.
     */
    public FileReader( String fileName )
    {
        _theDelim = null;

        try
        {
            _theFile = new RandomAccessFile( fileName, "r" );
            buildWordArray();
        } catch( IOException e )
        {
            System.out.println( "Unable to open file!!!!: " + e );
        } // end catch
    
    } // end constructor

    /**
     * Method: Constructor.
     * Purpose: Creates the FileReader object, also creates the RandomAccessFile object.
     *          
     * 
     * @param		String fileName.
     * @param		String theDelimeter.
     * @return		void.
     * @exception	IOException.
     * @throws		None.
     */
    public FileReader( String fileName, String delim )
    {
        _theDelim = delim;

        try
        {
            _theFile = new RandomAccessFile( fileName, "r" );
            buildWordArray();
        } catch( IOException e )
        {
            System.out.println( "Unable to open file!!!!: " + e );
        } // end catch
    
    } // end constructor

    /**
     * Method: BuildWordArray.
     * Purpose: Pulls words out of the file stream and puts them into the word array.
     *          The file must be in a certain format for things to work properly. Comment lines
     *          begin with the usual '//' while words are separated by spaces
     *          
     * @param		None.
     * @return		void.
     * @exception	IOException.
     * @throws		None.
     */
    private void buildWordArray()
    {
        _theWords      = new Vector();
        String theWord = null;
        String theLine = null;
        
        try
        {
            while( (theLine = _theFile.readLine()) != null )
            {
                if( theLine.startsWith( "//" ) || theLine.startsWith( "#" ) )
                   continue;

               StringTokenizer TokenIzer = null;
               if( _theDelim == null )
                  TokenIzer = new StringTokenizer( theLine );
               else
                  TokenIzer = new StringTokenizer( theLine, _theDelim );

               while( TokenIzer.hasMoreTokens() )
               {
                   theWord = TokenIzer.nextToken();
                   if( theWord.trim().length() > 0 )
                      _theWords.add( theWord.trim() );
               } // end while loop
            } // end while file not empty
        } catch( IOException e )
        {
            System.out.println( "Caught an error in buildWordArray: " + e );
        } // end catch
        finally
        {
           try { _theFile.close(); } catch( IOException e ) {}
        } // end finally
    
    } // end BuildWordArray

    public Vector getVectorOfWords()
    {
      return _theWords;
    } // end getWords

    public String[] getArrayOfWords()
    {
      String[] theWordArray = new String[ _theWords.size() ];
      _theWords.copyInto( theWordArray );
      return theWordArray;
    } // end getArrayOfWords

    public int getNumberOfElements()
    {
      return _theWords.size();
    } // end getNumberOfElements

    public String getLastElement()
    {
      return (String) _theWords.elementAt( getNumberOfElements() - 1 );
    } // end getLastElement
    
    public String getFileAsString( String filename )
    {
       StringBuffer theFile = new StringBuffer();
 
       try
       {
          BufferedReader theReader = new BufferedReader( new java.io.FileReader( filename ) );
          String theLine = null;
 
          while( (theLine = theReader.readLine()) != null )
          {
             theFile.append( theLine );
          }
          
          theReader.close();
       }
       catch( IOException ioe )
       {
          System.out.println( ioe );
       }
 
       return theFile.toString();
    }

} // end class FileReader
