package com.ultrastudios.speech.commands;

import com.ultrastudios.ui.*;
import com.ultrastudios.ui.models.*;
import javax.swing.JTable;
import java.util.*;
import java.math.*;

/**
 *
 */
public class TransactionCashSpeechCommand implements SpeechCommand
{

   private TransactionTableModel _tableModel;
   private JTable                _table;
   private String                _columnName = "Cash/Check Deposit";
   private String                _command    = "cash";

   /**
    *
    */
   public TransactionCashSpeechCommand()
   {
   }

   /**
    *
    */
   public TransactionCashSpeechCommand( CommandDispatcher dispatch )
   {
//       dispatch.register( this );
   }

   /**
    *
    */
   public TransactionCashSpeechCommand( JTable table )
   {
      _table = table;
      _tableModel = (TransactionTableModel) _table.getModel();
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
    * @param TransactionTableModel
    * @return TransactionTableModel
    */
   public void handleCommand( String command )
   {
      if( command.startsWith( _command ) && command.length() > _command.length() )
      {
         System.out.println( "TransactionCashSpeechCommand will handle command: " + command );
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