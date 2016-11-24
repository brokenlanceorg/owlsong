package linkparser;

import java.util.*;

/**
 *
 */
public class Sentence
{

   // COMMENT
   private ArrayList< Phrase > _phrases = new ArrayList< Phrase >();
   private String              _rawData;

   /**
    *
    */
   public void setPhrases( ArrayList< Phrase > constits )
   {
      _phrases = constits;
   }

   /**
    *
    */
   public void setLine( String line )
   {
      _rawData = line;
   }

   /**
    *
    */
   public void addPhrase( Phrase phrase )
   {
      System.out.println( "adding phrase: " + phrase );
      _phrases.add( phrase );
   }

   /**
    *
    */
   public String getRawLine()
   {
      return _rawData;
   }

   /**
    *
    */
   public ArrayList< Phrase > getPhrases()
   {
      return _phrases;
   }

   /**
    *
    */
   public String toString()
   {
      return _rawData;
   }
}
