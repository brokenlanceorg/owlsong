package com.ultrastudios.speech.commands;

import com.ultrastudios.ui.*;
import com.ultrastudios.ui.models.*;
import javax.swing.JTable;

/**
 *
 */
public class InventoryDescriptionSpeechCommand implements SpeechCommand
{

   private InventoryTableModel _tableModel;
   private JTable              _table;

   /**
    *
    */
   public InventoryDescriptionSpeechCommand()
   {
   }

   /**
    *
    */
   public InventoryDescriptionSpeechCommand( CommandDispatcher dispatch )
   {
      dispatch.register( this );
   }

   /**
    *
    */
   public InventoryDescriptionSpeechCommand( JTable table )
   {
      _table = table;
      _tableModel = (InventoryTableModel) _table.getModel();
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
    */
   private int getColumnByName( String columnName )
   {
      for( int i=0; i<_table.getColumnCount(); i++ )
      {
         if( columnName.equalsIgnoreCase( _table.getColumnName( i ) ) )
         {
            return i;
         }
      }

      return -1;
   }

   /**
    *
    * @param InventoryTableModel
    * @return InventoryTableModel
    */
   public void handleCommand( String command )
   {
      if( command.startsWith( "description" ) )
      {
         System.out.println( "InventoryDescriptionSpeechCommand will handle command: " + command );
         String description = command.substring( 12 );
         int row = _table.getSelectedRow();
         int col = getColumnByName( "item description" );
         if( row == -1 )
         {
            _tableModel.addRow();
            row = _tableModel.getRowCount() - 1;
            _table.setRowSelectionInterval( row, row );
         }
         else
         {
            row = _table.convertRowIndexToModel( row );
         }
         _tableModel.setValueAt( description, row, col );
      }
   }
   
}
