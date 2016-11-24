package com.ultrastudios.speech.commands;

import com.ultrastudios.ui.*;
import com.ultrastudios.ui.models.*;
import javax.swing.JTable;
import java.util.*;
import java.math.*;

/**
 *
 */
public class InventoryAddRowSpeechCommand implements SpeechCommand
{

   private InventoryTableModel _tableModel;
   private JTable              _table;
   private String              _columnName = "add row";
   private String              _command    = "add row";

   /**
    *
    */
   public InventoryAddRowSpeechCommand()
   {
   }

   /**
    *
    */
   public InventoryAddRowSpeechCommand( CommandDispatcher dispatch )
   {
//       dispatch.register( this );
   }

   /**
    *
    */
   public InventoryAddRowSpeechCommand( JTable table )
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
         _tableModel.addRow();
         int row = _tableModel.getRowCount() - 1;
         _table.setRowSelectionInterval( row, row );
         
      }
   }
   
}
