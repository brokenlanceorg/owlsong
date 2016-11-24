package com.ultrastudios.ui;

import com.ultrastudios.speech.commands.*;
import java.util.*;

/**
 *
 */
public class CommandDispatcher
{

   private ArrayList< SpeechCommand > _speechCommands;

   /**
    *
    */
   public CommandDispatcher()
   {
      _speechCommands = new ArrayList< SpeechCommand >();
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void register( SpeechCommand command )
   {
      _speechCommands.add( command );
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void handleCommand( String speech )
   {
      for( SpeechCommand command : _speechCommands )
      {
         command.handleCommand( speech );
      }
   }

}
