package linkparser;

import java.util.*;
import java.util.regex.*;

/**
 * This class takes an ArrayList of Strings and decomposes it to paragraphs.
 */
public class ParagraphParser implements ISentenceParserConstants
{

   // the collection of paragraphs
   private ArrayList< String > _paragraphs;

   /**
    *
    */
   public ParagraphParser()
   {
      _paragraphs = new ArrayList< String >();
   }

   /**
    *
    */
   public ArrayList< String > parseRawData( String data )
   {
      return getParagraphs();
   }

   /**
    *
    */
   public ArrayList< String > getParagraphs()
   {
      return _paragraphs;
   }

   /**
    *
    */
   public static void parse( Paragraph para )
   {
      ArrayList< String > rawLines = para.getRawLines();
      StringBuffer currentSentence = new StringBuffer();
      Sentence sent = new Sentence();
      boolean firstWord = true;
      for( String line : rawLines )
      {
         // split the line into individual words separated by any number of spaces
         String[] tokens = line.split( " +" );
         for( int i=0; i<tokens.length; i++ )
         {
            if( !firstWord )
            {
               currentSentence.append( " " );
            }
            currentSentence.append( tokens[ i ] );
            firstWord = false;
            // match words ending with ending punctuation
            if( tokens[ i ].matches( ".+[\\!\\?\\.\\;][\\'\"]*" ) )
            {
               sent.setLine( currentSentence.toString() );
               SentenceParser.parse( sent );
               para.addSentence( sent );
               sent = new Sentence();
               currentSentence = new StringBuffer();
               firstWord = true;
            }
         }
      }
   }

}
