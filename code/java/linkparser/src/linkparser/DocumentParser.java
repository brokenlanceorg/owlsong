package linkparser;

import java.util.*;
import java.io.*;
import java.net.*;

import common.*;

import edu.stanford.nlp.web.HTMLParser;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.parser.lexparser.*;
import edu.stanford.nlp.ling.*;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.item.*;
import edu.mit.jwi.morph.*;
import edu.mit.jwi.*;

/**
 *  This class will read in all the lines from a file (can look at memory issues later),
 *  and pass to the Paragraph parser, which parses paragraphs and then passes to the
 *  Sentence parser, which parses the sentences, and then passes to the Constituent parser.
 */
public class DocumentParser implements ISentenceParserConstants
{

   private String    _dataSource;
   private String    _fileSeparator;
   private String    _homeDirectory;
   private Document  _document;
   private HashMap   _corrections;

   /**
    * Default
    */
   public DocumentParser()
   {
      setup();
   }

   /**
    * 
    */
   public DocumentParser( String fileName )
   {
      setup();
      setFileName( fileName );
   }

   /**
    * 
    */
   private void setup()
   {
      _homeDirectory = System.getProperties().getProperty( "user.home" );
      _fileSeparator = System.getProperties().getProperty( "file.separator" );
      populateCorrections();
   }
 
   /**
    *
    */
   protected void setFileName( String fileName )
   {
      _dataSource = _homeDirectory + _fileSeparator + "Documents" + _fileSeparator + fileName;
   }
   /**
    * 
    */
   public Document getDocument()
   {
      return _document;
   }

   /**
    * 
    */
   protected void populateCorrections()
   {
      common.FileReader file = 
         new common.FileReader( _homeDirectory + _fileSeparator + "Documents" + 
                                _fileSeparator + "autocorrect.txt", "\n" );
      Vector corrections = file.getVectorOfWords();
      _corrections = new HashMap();
 
      for( Iterator iter = corrections.iterator(); iter.hasNext(); )
      {
         String line = (String)iter.next();
         StringTokenizer tokenizer = new StringTokenizer( line );

         String key = tokenizer.nextToken();
         StringBuffer value = new StringBuffer( tokenizer.nextToken() );

         while( tokenizer.hasMoreTokens() )
         {
            value.append( " " + tokenizer.nextToken() );
         }

//          System.out.println( "Key is: " + key + " value is: " + value );
         _corrections.put( key, value.toString() );
      }
   }

   /**
    * 
    */
   protected boolean isAbbreviation( String word )
   {
      boolean isAbbreviation = false;
 
      for( int i=0; i<ABBREVIATIONS.length; i++ )
      {
         if( word.equals( ABBREVIATIONS[i] ) )
         {
            isAbbreviation = true;
            break;
         }
      }

      return isAbbreviation;
   }

   /**
    *  Unfortunately, we have to check all terminal punctuation
    *  cases individually, instead of generically.
    */
   protected boolean isEndOfSentence( String word )
   {
      boolean isEnd = false;

      if( word.endsWith( "." ) )
      {
         if( !isAbbreviation( word ) )
         {
            isEnd = true;
         }
      }
      else if( word.endsWith( "?" ) || word.endsWith( "!" ) )
      {
         isEnd = true;
      }
      else if( word.endsWith( ".\"" ) || word.endsWith( "!\"" ) || word.endsWith( "?\"" ) )
      {
         isEnd = true;
      }

      return isEnd;
   }

   /**
    * 
    */
   protected String autoCorrect( String word )
   {
      String theWord = (String)_corrections.get( word );

      if( theWord == null )
      {
         theWord = word;
      }

      return theWord;
   }

   /**
    * 
    */
   public Document parse( String fileName )
   {
      setFileName( fileName );
      return parse();
   }

   /**
    * 
    */
   public Document parse()
   {
      _document = new Document();
      Paragraph para = new Paragraph();

      try
      {
         BufferedReader reader = new BufferedReader( new java.io.FileReader( _dataSource ) );
         String line = null;
         String previousLine = null;
         boolean added = false;

         while( (line = reader.readLine()) != null )
         {
            if( line.matches( "^ *$" ) )
            {
               if( added == false )
               {
                  _document.addParagraph( para );
                  para = new Paragraph();
                  added = true;
               }
            }
            else
            {
               para.addLine( line );
               added = false;
            }
         }

         // check last paragraph
         if( added == false )
         {
            _document.addParagraph( para );
            para = null;
         }
      }
      catch( IOException e )
      {
         System.out.println( "Caught exception while reading the document: " + e );
      }

      return _document;
   }

   /**
    * 
    */
   private void printHypernyms( IDictionary dictionary, ISynset hypernym, int depth )
   {
      System.out.println( "" );
      for( int i=0; i<depth; i++ )
      {
         System.out.print( " " );
      }
      List< ISynsetID > hypernyms = hypernym.getRelatedSynsets( Pointer.HYPERNYM );
      if( hypernyms != null && hypernyms.size() > 0 )
      {
         ISynset synset = dictionary.getSynset( hypernyms.get( 0 ) );
         System.out.print( synset.getGloss() );
         printHypernyms( dictionary, synset, (depth + 1) ); 
      }
   }

   /**
    *
    */
   private void printWordsForSynset( IDictionary dictionary, ISynsetID synsetID )
   {
      System.out.print( "\n    Related Synset ID: " + synsetID );
      List< IWord > synonyms = dictionary.getSynset( synsetID ).getWords();
      for( IWord synWord : synonyms )
      {
         System.out.print( " " + synWord.getLemma() );
      }
      System.out.println( "" );
   }

   /**
    * We can get the list of irregular verbs here: http://en.wikipedia.org/wiki/List_of_English_irregular_verbs
    *
    * @param String the file to parse.
    */
   public void test( String fileName )
   {
      // Linkparser setup:
      DocumentPreprocessor processor = new DocumentPreprocessor();
      LexicalizedParser parser = new LexicalizedParser( "englishPCFG.ser.gz" );

      // WordNet setup:
      URL url = null;
      try
      {
         url = new URL( "file", null, "/home/brandon/Public/dict" );
      }
      catch( MalformedURLException e )
      {
         System.out.println( "Malformed URL: " + e );
      }
      IDictionary dictionary = new Dictionary( url );
      dictionary.open();

      try
      {
         List sentences = processor.getSentencesFromText( fileName );
         TreebankLanguagePack treeBank = new PennTreebankLanguagePack();
         GrammaticalStructureFactory factory = treeBank.grammaticalStructureFactory();
         SemanticHeadFinder semanticHeadFinder = new SemanticHeadFinder();
         ModCollinsHeadFinder collinsHeadFinder = new ModCollinsHeadFinder();
         WordnetStemmer stemmer = new WordnetStemmer( dictionary );
         HashMap posHash = new HashMap();
         ArrayList< String > words = new ArrayList< String >();

         for( Object o : sentences )
         {
            System.out.println( "\nThe sentence is: " + o );
            for( Object obj : (List)o )
            {
               HasWord word = ( HasWord )obj;
//                if( !word.word().matches( "[,.;:'\"{}\\[\\]<>?!#$^&*()]" ) && !word.word().matches( "^-.*-$" ) )
               {
                  words.add( word.word() );
                  System.out.print( " " + word.word() );
               }
            }
            if( parser.parse( (List)o ) )
            {
               Tree tree = parser.getBestParse();
               GrammaticalStructure gs = factory.newGrammaticalStructure( tree );
               List< TypedDependency > types = gs.typedDependencies( true );

               String collinsHead = tree.headTerminal( collinsHeadFinder ).label().value();
               String semanticHead = tree.headTerminal( semanticHeadFinder ).label().value();
               if( collinsHead.equals( semanticHead ) )
               {
                  System.out.println( "The head terminal is: " + collinsHead );
               }
               else
               {
                  System.out.println( "The head terminal is: " + collinsHead + " " + semanticHead );
               }

               // figure out the part of speech:
               Iterator< Tree > iter = tree.iterator();
               String pos = null;
               int count = 0;
               while( iter.hasNext() )
               {
                  Tree t = iter.next();
                  if( words.contains( t.label().value() ) && !t.label().value().equals( pos ) )
                  {
                     count++;
                     posHash.put( t.label().value() + "_" + count, pos );
                     System.out.println( t.label().value() + "_" + count + " --> " + pos );

                     // Now, get the related synsets:
                     IIndexWord iWord = null;
                     String base = t.label().value();
                     if( pos.startsWith( "N" ) )
                     {
                        List< String > stems = stemmer.findStems( base, POS.NOUN );
                        if( stems != null && stems.size() > 0 )
                        {
                           base = stems.get( 0 );
                        }
                        iWord = dictionary.getIndexWord( base, POS.NOUN );
                     }
                     else if( pos.startsWith( "V" ) )
                     {
                        List< String > stems = stemmer.findStems( base, POS.VERB );
//                         System.out.println( "stems are: " + stems );
                        if( stems != null && stems.size() > 0 )
                        {
                           base = stems.get( 0 );
                        }
//                         System.out.println( "base is: " + base );
                        iWord = dictionary.getIndexWord( base, POS.VERB );
                     }
                     else if( pos.startsWith( "R" ) )
                     {
                        List< String > stems = stemmer.findStems( base, POS.ADVERB );
                        if( stems != null && stems.size() > 0 )
                        {
                           base = stems.get( 0 );
                        }
                        iWord = dictionary.getIndexWord( base, POS.ADVERB );
                     }
                     else if( pos.startsWith( "J" ) )
                     {
                        List< String > stems = stemmer.findStems( base, POS.ADJECTIVE );
                        if( stems != null && stems.size() > 0 )
                        {
                           base = stems.get( 0 );
                        }
                        iWord = dictionary.getIndexWord( base, POS.ADJECTIVE );
                     }

                     // print out the word, gloss, related words, and the hypernym hierarchy
                     if( iWord != null )
                     {
                        List< IWordID > wordIDs = iWord.getWordIDs();
                        for( IWordID wordID : wordIDs )
                        {
                           IWord word = dictionary.getWord( wordID );
                           System.out.println( "\n Lemma: " + word.getLemma() );
                           System.out.println( " Gloss: " + word.getSynset().getGloss() );

                           // This will give us similar lexical entries for the word
                           List< IWordID > wIDs = word.getRelatedWords();
                           for( IWordID wid : wIDs )
                           {
                              IWord w = dictionary.getWord( wid );
                              System.out.println( " Related word: " + w.getLemma() );
                           }

                           // need to also get all related synsets as well since this will contain more
                           // information that we can't afford to loose or do away with
                           ISynset synset = word.getSynset();
                           System.out.print( " Hypernyms: " );
                           printHypernyms( dictionary, synset, 1 ); 
                           System.out.print( "" );
                        }
                     }
                  }
                  pos = t.label().value();
               }
               
               // cycle through all the typed dependencies:
               for( TypedDependency type : types )
               {
                  System.out.println( type.gov().label().value() + " ( " + type.gov().index() + " ) " + 
                        " -- " + type.reln().getLongName() + " ( " + type.reln().getShortName() + " ) " + 
                        " --> " + type.dep().label().value() + 
                        " ( " + type.dep().index() + " ) " );
               }
            }
         }
      }
      catch( IOException e )
      {
         System.out.println( "Caught an IO: " + e );
      }

      /*

      System.out.println( "ID: " + wordID );
      System.out.println( "Lemma: " + word.getLemma() );
      System.out.println( "Gloss: " + word.getSynset().getGloss() );

      ISynset synset = word.getSynset();

      System.out.println( "Other meanings: " );
      List< IWordID > wIDs = iWord.getWordIDs();
      for( IWordID id : wIDs )
      {
         IWord w = dictionary.getWord( id );
         System.out.println( "Lemma: " + w.getLemma() );
         System.out.println( "Gloss: " + w.getSynset().getGloss() );
      }

      // This will give us the synonyms for the synset (meaning)
      for( IWord w : synset.getWords() )
      {
         System.out.println( "Synonym: " + w.getLemma() );
      }

      // This will give us the hypernyms (generalizations) of the synset
      // as opposed to hyponyms (specificities) of the synset
      List< ISynsetID > hypernyms = synset.getRelatedSynsets( Pointer.HYPERNYM );
      List< IWord > words = null;
      for( ISynsetID synid : hypernyms )
      {
         words = dictionary.getSynset( synid ).getWords();
         System.out.println( "Hypernym ID: " + synid );
         for( IWord synWord : words )
         {
            System.out.println( "   Hypernym: " + synWord.getLemma() );
         }
      }

      // note that there are two types of relations in Wordnet: Lexical and Semantic.
      // Semantic links relate synsets to each other, while lexical relate word forms.
      
      // This will give us similar items maybe...
      hypernyms = synset.getRelatedSynsets( Pointer.SIMILAR_TO );
      words = null;
      for( ISynsetID synid : hypernyms )
      {
         words = dictionary.getSynset( synid ).getWords();
         System.out.println( "Similar to ID: " + synid );
         for( IWord synWord : words )
         {
            System.out.println( "   Similar to: " + synWord.getLemma() );
         }
      }

      // This will give us all similar synsets regardless of the type
      hypernyms = synset.getRelatedSynsets();
      words = null;
      for( ISynsetID synid : hypernyms )
      {
         words = dictionary.getSynset( synid ).getWords();
         System.out.println( "   Related Synset ID: " + synid );
         for( IWord synWord : words )
         {
            System.out.println( "      Related Synset: " + synWord.getLemma() );
         }
      }

      // This will give us similar lexical entries for the word
      List< IWordID > wordIDs = word.getRelatedWords();
      for( IWordID wid : wordIDs )
      {
         IWord w = dictionary.getWord( wid );
         System.out.println( "Related word: " + w.getLemma() );
      }

      // This will give us similar verb frames
      List< IVerbFrame > verbs = word.getVerbFrames();
      for( IVerbFrame verb : verbs )
      {
         System.out.println( "Related verb: " + verb );
      }
      */

   }

   /**
    *
    * The Penn Treebank POS tagset.
    * ____________________________________________________________________________________________________
    * 1. CC Coordinating conjunction                            25. TO to
    * 2. CD Cardinal number                                     26. UH Interjection
    * 3. DT Determiner                                          27. VB Verb, base form
    * 4. EX Existential there                                   28. VBD Verb, past tense
    * 5. FW Foreign word                                        29. VBG Verb, gerund/present
    * 6. IN Preposition/subordinating participle conjunction    30. VBN Verb, past participle
    * 7. JJ Adjective                                           31. VBP Verb, non-3rd ps. sing. present
    * 8. JJR Adjective, comparative                             32. VBZ Verb, 3rd ps. sing. present
    * 9. JJS Adjective, superlative                             33. WDT wh-determiner
    * 10. LS List item marker                                   34. WP wh-pronoun
    * 11. MD Modal                                              35. WP$ Possessive wh-pronoun
    * 12. NN Noun, singular or mass                             36. WRB wh-adverb
    * 13. NNS Noun, plural                                      37. # Pound sign
    * 14. NNP Proper noun, singular                             38. $ Dollar sign
    * 15. NNPS Proper noun, plural                              39. . Sentence-final punctuation
    * 16. PDT Predeterminer                                     40. , Comma
    * 17. POS Possessive ending                                 41. : Colon, semi-colon
    * 18. PRP Personal pronoun                                  42. ( Left bracket character
    * 19. PP$ Possessive pronoun                                43. ) Right bracket character
    * 20. RB Adverb                                             44. " Straight double quote
    * 21. RBR Adverb, comparative                               45. ' Left open single quote
    * 22. RBS Adverb, superlative                               46. " Left open double quote
    * 23. RP Particle                                           47. ' Right close single quote
    * 24. SYM Symbol (mathematical or scientific)               48. " Right close double quote
    *
    * Elimination of lexically recoverable distinctions.
    * ____________________________________________________________________________________________________
    * sing/VB     be/VB     do/VB     have/VB
    * sings/VBZ   is/VBZ    does/VBZ  has/VBZ
    * sang/VBD    was/VBD   did/VBD   had/VBD
    * singing/VBG being/VBG doing/VBG having/VBG
    * sung/VBN    been/VBN  done/VBN  had/VBN
    *
    * The Penn Treebank syntactic tagset.
    * ____________________________________________________________________________________________________
    * 1. ADJP            Adjective phrase
    * 2. ADVP            Adverb phrase
    * 3. NP              Noun phrase
    * 4. PP              Prepositional phrase
    * 5. S               Simple declarative clause
    * 6. SBAR            Clause introduced by subordinating conjunction or 0 (see below)
    * 7. SBARQ           Direct question introduced by wh-word or wh-phrase
    * 8. SINV            Declarative sentence with subject-aux inversion
    * 9. SQ              Subconstituent of SBARQ excluding wh-word or wh-phrase
    * 10. VP             Verb phrase
    * 11. WHADVP         wh-adverb phrase
    * 12. WHNP           wh-noun phrase
    * 13. WHPP           wh-prepositional phrase
    * 14. X              Constituent of unknown or uncertain category
    * Null elements      
    * 1. *               "Understood" subject of infinitive or imperative
    * 2. 0               Zero variant of that in subordinate clauses
    * 3. T               Trace--marks position where moved wh-constituent is interpreted
    * 4. NIL             Marks position where preposition is interpreted in pied-piping contexts
    *
    */
   public static void main( String[] args )
   {
      DocumentParser doc = new DocumentParser();
      doc.test( args[ 0 ] );
//       doc.parse( "test.txt" );
//
/*
      DocumentPreprocessor processor = new DocumentPreprocessor();
//       Options ops = new Options();
//       String[] arg = new String[ 4 ];
//       arg[ 0 ] = "-outputFormat";
//       arg[ 1 ] = "typedDependencies";
//       arg[ 2 ] = "-outputFormatOptions";
//       arg[ 3 ] = "CCPropagatedDependencies";
//       ops.setOptionOrWarn( arg, 0 );
//       ops.setOptionOrWarn( arg, 2 );
//       TreebankLangParserParams params = ops.tlpParams;
//       TreeTransformer tc = params.collinizer();
//       LexicalizedParser parser = new LexicalizedParser( "englishPCFG.ser.gz", ops );
      LexicalizedParser parser = new LexicalizedParser( "englishPCFG.ser.gz" );
      try
      {
         List sentences = processor.getSentencesFromText( args[0] );
         ArrayList< String > subjects = new ArrayList();
         for( Object o : sentences )
         {
            System.out.println( "####################### A sentence: " + o );
            boolean res = parser.parse( (List)o );
            System.out.println( "parser result: " + res );
            Tree tree = parser.getBestParse();
//             Tree tree = tc.transformTree( ptree );
//             System.out.println( "parser score: " + tree.score() );
//             System.out.println( "parser hashCode: " + tree.hashCode() );
//             tree.percolateHeads( (HeadFinder)new ModCollinsHeadFinder() );
            TreebankLanguagePack treeBank = new PennTreebankLanguagePack();
            GrammaticalStructureFactory factory = treeBank.grammaticalStructureFactory();
            GrammaticalStructure gs = factory.newGrammaticalStructure( tree );
//             Collection< TypedDependency > types = gs.allTypedDependencies();
//             Collection< TypedDependency > types = gs.typedDependenciesCollapsedTree();
//             List< TypedDependency > types = gs.typedDependenciesCCprocessed( true );
//             List< TypedDependency > types = gs.typedDependenciesCCprocessed();
//             List< TypedDependency > types = gs.typedDependenciesCollapsed( true );
            List< TypedDependency > types = gs.typedDependencies( true );
            for( TypedDependency type : types )
            {
               System.out.println( type.gov().label().value() + " ( " + type.gov().index() + " ) " + 
                     " -- " + type.reln().getLongName() + " ( " + type.reln().getShortName() + " ) " + 
//                      " ( " + type.reln().getSpecific() + " ) " + 
//                      " ( " + type.reln().getParent() + " ) " + 
                     " --> " + type.dep().label().value() + 
                     " ( " + type.dep().index() + " ) " );
//                System.out.println( "grammatical relation: " + EnglishGrammaticalRelations.valueOf( type.reln().getShortName() ) );
               if( "nsubj".equals( type.reln().getShortName() ) )
               {
                  subjects.add( type.dep().label().value() );
               }
            }
//             System.out.println( "###################################################################" );
            Iterator< Tree > iter = tree.iterator();
            String pos = null;
            while( iter.hasNext() )
            {
               Tree t = iter.next();
//                System.out.println( "The tree node is: " + t );
               System.out.println( "The tree nodeString is: " + t.nodeString() );
//                System.out.println( "The tree node pennString is: " + t.pennString() );
               System.out.println( "The tree node isLeaf is: " + t.isLeaf() );
               System.out.println( "The tree node isPhrasal is: " + t.isPhrasal() );
               System.out.println( "The tree node isPreTerminal is: " + t.isPreTerminal() );
               System.out.println( "The tree node isPrePreTerminal is: " + t.isPrePreTerminal() );
               System.out.println( "The tree node isUnaryRewrite is: " + t.isUnaryRewrite() );
//                System.out.println( "The tree node label is: " + t.label().value() );
//                if( t.isUnaryRewrite() && t.isPreTerminal() )
//                if( !t.nodeString().matches( ".*?( +|ROOT).*+" ) )
               {
                  System.out.println( "####The tree nodeString is: " + t.nodeString() );
                  System.out.println( "####The tree node label is: " + t.label().value() );
                  System.out.println( "####The POS is: " + pos );
               }
               pos = t.label().value();
            }
//             System.out.println( "The head is: " + (new ModCollinsHeadFinder()).determineHead( tree ).nodeString() );
//             TreeGraphNode tgn = new TreeGraphNode( tree, (TreeGraph)gs );
//             EnglishGrammaticalStructure egs = new EnglishGrammaticalStructure( tree );
//             System.out.println( "The subject is: " + ((EnglishGrammaticalStructure)gs).getSubject( tgn ) );
//             System.out.println( "The root is: " + egs.root().label().value() );
            System.out.println( "The head terminal is: " + tree.headTerminal( new ModCollinsHeadFinder() ).label().value() );
//             System.out.println( "The head terminal depth is: " + tree.depth( tree.headTerminal( new ModCollinsHeadFinder() ) ) );
//             System.out.println( "The head terminal index is: " + tree.nodeNumber( tree.headTerminal( new ModCollinsHeadFinder() ) ) );
            System.out.println( "The head terminal (2) is: " + tree.headTerminal( new SemanticHeadFinder() ) );
//             System.out.println( "The head terminal (3) is: " + tree.headTerminal( new CollinsHeadFinder() ) );
//             System.out.println( "The head pre terminal is: " + tree.headPreTerminal( new ModCollinsHeadFinder() ) );
         }
         for( String sub : subjects )
         {
            System.out.println( "A subject: " + sub );
         }

      }
      catch( IOException e )
      {
         System.out.println( "Caught an IO: " + e );
      }

      // ############################################################################
      // WordNet API Calls
      // ############################################################################
      URL url = null;
      try
      {
         url = new URL( "file", null, "/home/brandon/Public/dict" );
      }
      catch( MalformedURLException e )
      {
         System.out.println( "Malformed URL: " + e );
      }
      IDictionary dictionary = new Dictionary( url );
      dictionary.open();

//       IIndexWord iWord = dictionary.getIndexWord( "happy", POS.ADJECTIVE );
//       IIndexWord iWord = dictionary.getIndexWord( "quickly", POS.ADVERB );
//       IIndexWord iWord = dictionary.getIndexWord( "dividend", POS.NOUN );
//       IIndexWord iWord = dictionary.getIndexWord( "dog", POS.NOUN );
//       IIndexWord iWord = dictionary.getIndexWord( "man", POS.NOUN );
//       IIndexWord iWord = dictionary.getIndexWord( "run", POS.VERB );
//       IIndexWord iWord = dictionary.getIndexWord( "earned run", POS.NOUN );
//       IIndexWord iWord = dictionary.getIndexWord( "constituent", POS.NOUN );
//       IIndexWord iWord = dictionary.getIndexWord( "chains", POS.NOUN );
      IIndexWord iWord = dictionary.getIndexWord( "hamper", POS.NOUN );
      IWordID wordID = iWord.getWordIDs().get( 0 );
      IWord word = dictionary.getWord( wordID );

      System.out.println( "ID: " + wordID );
      System.out.println( "Lemma: " + word.getLemma() );
      System.out.println( "Gloss: " + word.getSynset().getGloss() );

      ISynset synset = word.getSynset();

      System.out.println( "Other meanings: " );
      List< IWordID > wIDs = iWord.getWordIDs();
      for( IWordID id : wIDs )
      {
         IWord w = dictionary.getWord( id );
         System.out.println( "Lemma: " + w.getLemma() );
         System.out.println( "Gloss: " + w.getSynset().getGloss() );
      }

      // This will give us the synonyms for the synset (meaning)
      for( IWord w : synset.getWords() )
      {
         System.out.println( "Synonym: " + w.getLemma() );
      }

      // This will give us the hypernyms (generalizations) of the synset
      // as opposed to hyponyms (specificities) of the synset
      List< ISynsetID > hypernyms = synset.getRelatedSynsets( Pointer.HYPERNYM );
      List< IWord > words = null;
      for( ISynsetID synid : hypernyms )
      {
         words = dictionary.getSynset( synid ).getWords();
         System.out.println( "Hypernym ID: " + synid );
         for( IWord synWord : words )
         {
            System.out.println( "   Hypernym: " + synWord.getLemma() );
         }
      }

      // note that there are two types of relations in Wordnet: Lexical and Semantic.
      // Semantic links relate synsets to each other, while lexical relate word forms.
      
      // This will give us similar items maybe...
      hypernyms = synset.getRelatedSynsets( Pointer.SIMILAR_TO );
      words = null;
      for( ISynsetID synid : hypernyms )
      {
         words = dictionary.getSynset( synid ).getWords();
         System.out.println( "Similar to ID: " + synid );
         for( IWord synWord : words )
         {
            System.out.println( "   Similar to: " + synWord.getLemma() );
         }
      }

      // This will give us all similar synsets regardless of the type
      hypernyms = synset.getRelatedSynsets();
      words = null;
      for( ISynsetID synid : hypernyms )
      {
         words = dictionary.getSynset( synid ).getWords();
         System.out.println( "   Related Synset ID: " + synid );
         for( IWord synWord : words )
         {
            System.out.println( "      Related Synset: " + synWord.getLemma() );
         }
      }

      // This will give us similar lexical entries for the word
      List< IWordID > wordIDs = word.getRelatedWords();
      for( IWordID wid : wordIDs )
      {
         IWord w = dictionary.getWord( wid );
         System.out.println( "Related word: " + w.getLemma() );
      }

      // This will give us similar verb frames
      List< IVerbFrame > verbs = word.getVerbFrames();
      for( IVerbFrame verb : verbs )
      {
         System.out.println( "Related verb: " + verb );
      }

      */
   }
}
