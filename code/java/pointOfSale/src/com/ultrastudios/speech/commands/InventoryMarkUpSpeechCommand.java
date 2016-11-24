package com.ultrastudios.speech.commands;

import com.ultrastudios.ui.*;
import com.ultrastudios.ui.models.*;
import javax.swing.JTable;
import java.util.*;
import java.math.*;

/**
 *
 */
public class InventoryMarkUpSpeechCommand implements SpeechCommand
{

   private InventoryTableModel _tableModel;
   private JTable              _table;
   private String              _columnName = "mark up%";
   private String              _command    = "mark up";

   /**
    *
    */
   public InventoryMarkUpSpeechCommand()
   {
   }

   /**
    *
    */
   public InventoryMarkUpSpeechCommand( CommandDispatcher dispatch )
   {
//       dispatch.register( this );
   }

   /**
    *
    */
   public InventoryMarkUpSpeechCommand( JTable table )
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
      if( command.startsWith( _command ) && command.length() > _command.length() )
      {
         System.out.println( "InventoryMarkUpSpeechCommand will handle command: " + command );
         String data = command.substring( _command.length() + 1 );
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
         StringTokenizer tokenizer = new StringTokenizer( data );
         StringBuffer number = new StringBuffer();
         while( tokenizer.hasMoreTokens() )
         {
            String item = tokenizer.nextToken();
            if( "zero".equals( item ) )
            {
               number.append( "0" );
            }
            else if( "one".equals( item ) )
            {
               number.append( "1" );
            }
            else if( "two".equals( item ) )
            {
               number.append( "2" );
            }
            else if( "three".equals( item ) )
            {
               number.append( "3" );
            }
            else if( "four".equals( item ) )
            {
               number.append( "4" );
            }
            else if( "five".equals( item ) )
            {
               number.append( "5" );
            }
            else if( "six".equals( item ) )
            {
               number.append( "6" );
            }
            else if( "seven".equals( item ) )
            {
               number.append( "7" );
            }
            else if( "eight".equals( item ) )
            {
               number.append( "8" );
            }
            else if( "nine".equals( item ) )
            {
               number.append( "9" );
            }
            else if( "point".equals( item ) || "dot".equals( item ) )
            {
               number.append( "." );
            }
         }
         data = number.toString();
         BigDecimal n = null;
         try
         {
            n = new BigDecimal( data );
         }
         catch( NumberFormatException e )
         {
            n = BigDecimal.ZERO;
         }
         _tableModel.setValueAt( n, row, col );
      }
   }
   
}
