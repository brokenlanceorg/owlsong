package com.ultrastudios.speech.commands;

import com.ultrastudios.ui.*;
import com.ultrastudios.ui.models.*;
import javax.swing.JTable;

/**
 *
 */
public class InventoryReturnSpeechCommand implements SpeechCommand
{

   private InventoryTableModel _tableModel;
   private JTable              _table;
   private String              _columnName = "return";
   private String              _command    = "return";

   /**
    *
    */
   public InventoryReturnSpeechCommand()
   {
   }

   /**
    *
    */
   public InventoryReturnSpeechCommand( CommandDispatcher dispatch )
   {
   }

   /**
    *
    */
   public InventoryReturnSpeechCommand( JTable table )
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
      if( command.startsWith( _command ) && command.length() >= _command.length() )
      {
         System.out.println( "InventoryReturnSpeechCommand will handle command: " + command );
         int row = _table.getSelectedRow();
         int col = getColumnByName( _columnName );
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
         Object data = _tableModel.getValueAt( row, col );
         Boolean value = Boolean.FALSE;
         if( data == null )
         {
            value = Boolean.TRUE;
         }
         else
         {
            boolean b = ((Boolean)data).booleanValue();
            if( b )
            {
               value = Boolean.FALSE;
            }
            else
            {
               value = Boolean.TRUE;
            }
         }
         _tableModel.setValueAt( value, row, col );
      }
   }
   
}
