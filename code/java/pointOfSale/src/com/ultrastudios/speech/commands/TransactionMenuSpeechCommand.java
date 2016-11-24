package com.ultrastudios.speech.commands;

import com.ultrastudios.ui.*;
import com.ultrastudios.ui.models.*;
import javax.swing.JMenuItem;
import java.util.*;
import java.math.*;

/**
 *
 */
public class TransactionMenuSpeechCommand implements SpeechCommand
{

   private TransactionTableModel _tableModel;
   private JMenuItem             _menuItem;
   private String                _columnName = "Menu/Check Deposit";
   private String                _command    = "transaction";

   /**
    *
    */
   public TransactionMenuSpeechCommand()
   {
   }

   /**
    *
    */
   public TransactionMenuSpeechCommand( CommandDispatcher dispatch )
   {
//       dispatch.register( this );
   }

   /**
    *
    */
   public TransactionMenuSpeechCommand( JMenuItem item )
   {
      _menuItem = item;
   }

   /**
    *
    */
   public void setTableModel( TransactionTableModel tm )
   {
      _tableModel = tm;
   }

   /**
    *
    */
   public TransactionTableModel getTableModel()
   {
      return _tableModel;
   }

   /**
    *
    * @param TransactionTableModel
    * @return TransactionTableModel
    */
   public void handleCommand( String command )
   {
      if( command.startsWith( _command ) )
      {
         _menuItem.doClick();
      }
   }
   
}