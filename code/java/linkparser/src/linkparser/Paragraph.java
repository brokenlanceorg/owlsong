package linkparser;

import java.util.*;

/**
 *
 */
public class Paragraph
{

   // COMMENT
   private ArrayList< Sentence > _sentences = new ArrayList< Sentence >();
   private ArrayList< String >   _rawLines = new ArrayList< String >();

   /**
    *
    */
   public ArrayList< String > getRawLines()
   {
      return _rawLines;
   }

   /**
    *
    */
   public ArrayList< Sentence > getSentences()
   {
      return _sentences;
   }

   /**
    *
    */
   public void addLine( String line )
   {
      _rawLines.add( line );
   }
   
   /**
    *
    */
   public void addSentence( Sentence sent )
   {
      System.out.println( "Adding a sentence: " + sent );
      _sentences.add( sent );
   }
   
   /**
    *
    */
   public String toString()
   {
      StringBuffer result = new StringBuffer();

      for( String line : _rawLines )
      {
         result.append( line + "\n" );
      }

      return result.toString();
   }

}
