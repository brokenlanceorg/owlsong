package linkparser;

import java.util.*;
import java.util.regex.*;
import java.io.*;

/**
 * conjunctions include:
 *    coordinating: and, but, or, so, for, nor, yet
 *    correlative: both..and, either..or, neither..nor, not only..but also, whether..or
 *    subordinating: 
 *      TIME: after, before, when, while, since, until     
 *      CAUSE + EFFECT: because, since, now that, as, in order that, so              
 *      OPPOSITION: although, though, even though, whereas, while           
 *      CONDITION: if, unless, only if, whether or not, even if, in case (that)
 */
public class SentenceParser implements ISentenceParserConstants
{

   private String         _semanticDifferentialFileName;
   private String         _constituentFileName;
   private BufferedWriter _semanticDiffOutput;
   private int            _maxPoolSize = 200;
   private HashSet        _initialGenePool;

   /**
    * Default
    */
   public SentenceParser()
   {
      Properties props = System.getProperties();
      StringBuffer fileName = new StringBuffer( props.getProperty( "user.home" ) );
      fileName.append( "\\UserData\\SemanticDifferential.txt" );
      _semanticDifferentialFileName = fileName.toString();
      _constituentFileName = new String( props.getProperty( "user.home" ) +  "\\UserData\\" );
   }

   /**
    *  The basic idea is as follows:
    * 
    * 1)  For each *unique* constituent in the conversation input:
    *     2) Add word to gene pool. If the word is not a common word then:
    *        3) Search concept net for related words, and add to gene pool.
    *     4) See if word exists in context library. (multiple row result)
    *        5) If so, then select all constituents that have those serial numbers + 1 and add to gene pool (common function)
    *     6) See if word exists in constituents table (multiple row result)
    *        7) If so, then find contexts where context library equals serial numbers.
    *        8) Then, find all constituents with those serial numbers. (common function)
    * 9)  For each gene,  (if SD doesn't exist, then fail out for gene)
    *     10) determine quick SD and write to disk. (common function)
    *     11) determine similar SD constituents, add to gene pool and write to disk (common function).
    *     12) Retrieve the data from the constituents, constituents_left, and the context table if this is a context word.
    * 9)  For each gene,  (if SD doesn't exist, then fail out for gene)
    *     10) determine quick SD and write to disk. (common function)
    *     11) determine similar SD constituents, add to gene pool and write to disk (common function).
    *     13) write data to disk (constituent_<number>) each constituent as appears in table is a file -- not unique
    * 
    * 
    */
   public void beginConversation( ArrayList sentences )
   {
   }

   /**
    * 
    */
   private void closeFile( BufferedWriter file )
   {
      try
      {
         file.flush();
         file.close();
      }
      catch( IOException ie ) { /* ignore */ }
   }

   /**
    * Will parse the sentence out and place the data in 
    * the appropriate system files.
    */
   public void beginParse( String sentence, int deleteResources )
   {
      // Then, we populate the semantic differentials for all words in sentence
      populateSemanticDifferentials( sentence );

      // refresh the database with new sentence data.
      (new SentenceUpdater()).updateDatabase();
   }

   /**
    *  Need to get semantic differentials for both the word
    *  and its lower case spelling since we won't be sure
    *  which the link parser will use.
    * 
    * 
    */
   protected void populateSemanticDifferentials( String sentence )
   {
      StringTokenizer tokenizer = new StringTokenizer( sentence, " " );

      try
      {
         _semanticDiffOutput = new BufferedWriter( new FileWriter( _semanticDifferentialFileName ) );
      }
      catch( IOException e )
      {
         System.out.println( "Caught exception writing out to: " + _semanticDifferentialFileName );
      }
      finally
      {
         try
         {
            _semanticDiffOutput.flush();
            _semanticDiffOutput.close();
         }
         catch( IOException ie ) { /* ignore */ }
      }
   }

   /**
    * 
    */
   public boolean containsUpperCase( String word )
   {
      boolean isUpper = false;

      for( int i=0; i<word.length(); i++ )
      {
         if( Character.isUpperCase( word.charAt( i ) ) )
         {
            isUpper = true;
            break;
         }
      }

      return isUpper;
   }

   /**
    * 
    * 
    */
   protected String stripAllPunctuation( String word )
   {
      word = stripContraction( word );

      if( matchesAbbreviation( word ) )
      {
         // even though is an abbreviation, we still must strip
         // any trailing punctuation ",;:..."
         word = stripTrailingPunctuation( word );
      }
      // Since it's not an abbreviation, we can strip the punctuation
      else
      {
         word = stripPunctuation( word );
      }

      return word;
   }

   /**
    * 
    * 
    */
   protected boolean matchesAbbreviation( String word )
   {
      boolean matches = false;
 
      for( int i=0; i<ABBREVIATIONS.length; i++ )
      {
         if( word.indexOf( ABBREVIATIONS[i] ) != -1 )
         {
            matches = true;
            break;
         }
      }

      return matches;
   }

   /**
    * 
    * 
    */
   public String stripTrailingPunctuation( String word )
   {
      StringBuffer theWord = new StringBuffer( word );
 
      for( int i=(theWord.length() - 1); (theWord.charAt(i) != '.' ); i-- )
      {
         char theChar = theWord.charAt( i );
         if( INTERNAL_PUNCTUATION.indexOf( theChar ) != -1 )
         {
            theWord.deleteCharAt( i-- );
         }
         else if( TERMINAL_PUNCTUATION.indexOf( theChar ) != -1 )
         {
            theWord.deleteCharAt( i-- );
         }
      }

      return theWord.toString();
   }

   /**
    * 
    * 
    */
   public String stripContraction( String word )
   {
      StringBuffer stripped = new StringBuffer( word );

      for( int i=0; i<stripped.length(); i++ )
      {
         char theChar = stripped.charAt( i );
         if( theChar == '\'' )
         {
            int begin = i;
            while( (++i < stripped.length()) && 
                   Character.isLetter( stripped.charAt( i ) ) );
            stripped.delete( begin, i + 1 );
            
         }
      }

      return stripped.toString();
   }

   /**
    * 
    * 
    */
   public String stripPunctuation( String word )
   {
      StringBuffer stripped = new StringBuffer( word );

      for( int i=0; i<stripped.length(); i++ )
      {
         char theChar = stripped.charAt( i );
         if( INTERNAL_PUNCTUATION.indexOf( theChar ) != -1 )
         {
            stripped.deleteCharAt( i-- );
         }
         else if( TERMINAL_PUNCTUATION.indexOf( theChar ) != -1 )
         {
            stripped.deleteCharAt( i-- );
         }
      }

      return stripped.toString();
   }
   
   /**
    * This code is NOT complete. I'll finish it up later...
    * Phrase Punctuation to be mindful of:
    * "'(),--:[]{}
    * "
    * '
    * ()
    * ,
    * --
    * :
    * []
    * {}
    */
   public static void parsePhrase( Phrase phrase )
   {
      String line = phrase.getRawLine();
      String[] words = line.split( " +" );
      boolean inPhrase = false;
      boolean firstWord = true;
      String phraseDelim = null;
      StringBuffer currentPhrase = null;
      
      for( int i=0; i<words.length; i++ )
      {
         // Start adding words regardless of whether we're in phrase or not
         if( !firstWord )
         {
            currentPhrase.append( " " );
         }
         currentPhrase.append( words[ i ] );
         firstWord = false;

         if( !inPhrase )
         {
            // Check for beginning phrases
            Pattern p1 = Pattern.compile( "^([\"\\(\\[\\{])(.+)$" );
            Matcher m1 = p1.matcher( words[ i ] );
            // Check for beginning end phrases
            Pattern p2 = Pattern.compile( "^(.+)([:\\,\\)\\]\\}])$" );
            Matcher m2 = p2.matcher( words[ i ] );

            if( m1.matches() )
            {
               System.out.println( "phrase begin match: " + words[ i ] );
               System.out.println( "group 1: " + m1.group( 1 ) );
               System.out.println( "group 2: " + m1.group( 2 ) );
            }
            else if( m2.matches() )
            {
               System.out.println( "phrase begin match: " + words[ i ] );
               System.out.println( "group 1: " + m2.group( 1 ) );
               System.out.println( "group 2: " + m2.group( 2 ) );
            }
         }
      }
   }

   /**
    *
    */
   public static void parse( Sentence sentence )
   {
      String line = sentence.getRawLine();
      String[] words = line.split( " +" );
      boolean inPhrase = false;
      boolean firstWord = true;
      String phraseDelim = null;
      StringBuffer currentPhrase = null;
      int state = -1;
      
      for( int i=0; i<words.length; i++ )
      {
         // Start adding words regardless of whether we're in phrase or not
         if( !firstWord )
         {
            currentPhrase.append( " " );
         }
         currentPhrase.append( words[ i ] );
         firstWord = false;

         if( !inPhrase )
         {
            // Check for beginning phrases
            Pattern p1 = Pattern.compile( "^([\"])(.+)$" );
            Matcher m1 = p1.matcher( words[ i ] );
            // Check for beginning phrases
            Pattern p11 = Pattern.compile( "^([\\(\\[\\{])(.+)$" );
            Matcher m11 = p1.matcher( words[ i ] );
            // Check for beginning end phrases
            Pattern p2 = Pattern.compile( "^(.+)([\":\\,\\)\\]\\}])$" );
            Matcher m2 = p2.matcher( words[ i ] );

            if( m1.matches() )
            {
               state = -1;
               System.out.println( "phrase begin match: " + words[ i ] );
               System.out.println( "group 1: " + m1.group( 1 ) );
               System.out.println( "group 2: " + m1.group( 2 ) );
            }
            else if( m2.matches() )
            {
               // here, we've ended the quote phrase
               if( state == -1 )
               {
//                   COMMAND;
               }
               state = -1;
               System.out.println( "phrase begin match: " + words[ i ] );
               System.out.println( "group 1: " + m2.group( 1 ) );
               System.out.println( "group 2: " + m2.group( 2 ) );
            }
         }
      }

      Phrase phrase = new Phrase();
      phrase.setLine( sentence.getRawLine() );
      SentenceParser.parsePhrase( phrase );
   }
}
