package com.ultrastudios.speech.commands;

import com.ultrastudios.ui.*;
import com.ultrastudios.ui.models.*;
import javax.swing.*;

/**
 *
 */
public class InventoryFilterSpeechCommand implements SpeechCommand
{

   private InventoryTableModel _tableModel;
   private JButton             _button;
   private JTextField          _textField;
   private String              _columnName = "filter";
   private String              _command    = "filter";

   /**
    *
    */
   public InventoryFilterSpeechCommand()
   {
   }

   /**
    *
    */
   public InventoryFilterSpeechCommand( CommandDispatcher dispatch )
   {
//       dispatch.register( this );
   }

   /**
    *
    */
   public InventoryFilterSpeechCommand( JButton button, JTextField textField )
   {
      _button = button;
      _textField = textField;
   }

   /**
    *
    */
   public void setTableModel( InventoryTableModel tm )
   {
      _tableModel = tm;
   }

   /**
    *
    */
   public InventoryTableModel getTableModel()
   {
      return _tableModel;
   }

   /**
    *
    * @param InventoryTableModel
    * @return InventoryTableModel
    */
   public void handleCommand( String command )
   {
      if( command.startsWith( _command ) && command.length() > _command.length() )
      {
         System.out.println( "InventoryFilterSpeechCommand will handle command: " + command );
         String data = command.substring( _command.length() + 1 );
         if( "clear".equals( data ) )
         {
            _textField.setText( ".*" );
         }
         else
         {
            _textField.setText( data );
         }
         _button.doClick();
      }
   }
   
}
