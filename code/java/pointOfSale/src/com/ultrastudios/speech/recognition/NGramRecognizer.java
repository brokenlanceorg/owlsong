package com.ultrastudios.speech.recognition;

import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;

/**
 *
 * ARPA model training
 * 
 * The process for creating a language model is as follows:
 * 
 * 1) Prepare a reference text that will be used to generate the language model. 
 * The language model toolkit expects its input to be in the form of normalized text files, 
 * with utterances delimited by <s> and </s> tags. A number of input filters are available 
 * for specific corpora such as Switchboard, ISL and NIST meetings, and HUB5 transcripts. 
 * The result should be the set of sentences that are bounded by the start and end sentence 
 * markers: <s> and </s>. Here's an example:
 * 
 * <s> generally cloudy today with scattered outbreaks of rain and drizzle persistent and heavy at times </s>
 * <s> some dry intervals also with hazy sunshine especially in eastern parts in the morning </s>
 * <s> highest temperatures nine to thirteen Celsius in a light or moderate mainly east south east breeze </s>
 * <s> cloudy damp and misty today with spells of rain and drizzle in most places much of this rain will be 
 * light and patchy but heavier rain may develop in the west later </s>
 * 
 * More data will generate better language models. The weather.txt file from sphinx4 
 * (used to generate the weather language model) contains nearly 100,000 sentences.
 *
 * The following tools are found in the /lib/sphinx/cmuclmtk-0.7-win32 project.
 * 
 * 2) Generate the vocabulary file. This is a list of all the words in the file:
 * 
 * text2wfreq < weather.txt | wfreq2vocab > weather.tmp.vocab
 * 
 * 3) You may want to edit the vocabulary file to remove words (numbers, misspellings, names). 
 * If you find misspellings, it is a good idea to fix them in the input transcript.
 * 
 * 4) If you want a closed vocabulary language model (a language model that has no provisions for unknown words), 
 * then you should remove sentences from your input transcript that contain words that are not in your vocabulary file.
 * 
 * 5) Generate the arpa format language model with the commands:
 * 
 * text2idngram -vocab weather.vocab -idngram weather.idngram < weather.closed.txt
 * idngram2lm -vocab_type 0 -idngram weather.idngram -vocab weather.vocab -arpa weather.arpa
 * 
 * 6) Generate the CMU binary form (DMP)
 * 
 * sphinx_lm_convert -i weather.arpa -o weather.lm.DMP
 * 
 */
public class NGramRecognizer
{

   private Recognizer _recognizer;

   /**
    *
    */
   public NGramRecognizer()
   {
      initialize();
   }

   /**
    *
    */
   private void initialize()
   {
      ConfigurationManager cm = new ConfigurationManager( NGramRecognizer.class.getResource( "ultra.config.xml" ) );
      _recognizer = (Recognizer) cm.lookup( "recognizer" );
      _recognizer.allocate();

      // start the microphone or exit if the programm if this is not possible
      Microphone microphone = (Microphone) cm.lookup( "microphone" );
      if( !microphone.startRecording() ) 
      {
         System.err.println( "Cannot start microphone." );
         _recognizer.deallocate();
      }

      System.out.println( "Speech recognizer initialized." );
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public String recognizeSpeech()
   {
      Result result = _recognizer.recognize();
      String resultText = "";

      if( result != null ) 
      {
         resultText = result.getBestFinalResultNoFiller();
      } 
      else 
      {
         System.out.println( "I can't hear what you said." );
      }

      return resultText;
   }

}
