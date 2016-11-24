package com.ultrastudios.ui.tasks;

import com.ultrastudios.ui.*;
import com.ultrastudios.speech.recognition.*;
import javax.swing.*;
import java.util.*;

/**
 * The first parameter in SwingWorker is the return value of "doInBackground" while the second parameter
 * is the argument to the method "process".
 */
public class SpeechTask extends SwingWorker< Void, String > 
{

   private NGramRecognizer   _recognizer;
   private CommandDispatcher _dispatcher;
   private JCheckBoxMenuItem _checkBox;

   /**
    *
    */
   public SpeechTask()
   {
      initialize();
   }

   /**
    *
    */
   public SpeechTask( CommandDispatcher dispatcher )
   {
      setDispatcher( dispatcher );
      initialize();
   }

   /**
    *
    */
   public void setDispatcher( CommandDispatcher dispatcher )
   {
      _dispatcher = dispatcher;
   }

   /**
    *
    */
   public void setCheckBoxMenuItem( JCheckBoxMenuItem item )
   {
      _checkBox = item;
   }

   /**
    *
    */
   private void initialize()
   {
      _recognizer = new NGramRecognizer();
   }

   /**
    *
    */
   @Override
   @SuppressWarnings("SleepWhileInLoop")
   protected Void doInBackground() 
   {
      int i = 0;
      while( i > -1 )
      {
         publish( _recognizer.recognizeSpeech() );
      }
      return null;
   }

   /**
    *
    */
   @Override
   protected void process( List< String > textList ) 
   {
      String text = textList.get( textList.size() - 1 );
      if( _checkBox.getState() )
      {
         _dispatcher.handleCommand( text );
      }
      else
      {
         System.out.println( "Audio off -- will not handle data: " + text );
      }
   }

}
