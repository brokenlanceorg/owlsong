package com.ultrastudios.ui.models;

import java.util.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import com.ultrastudios.money.*;
import java.math.BigDecimal;
import java.math.MathContext;


/**
 *
 */
public class TransactionTableModel extends AbstractTableModel
{

   private String[]            _columnNames;
   private Object[][]          _tableData;
   private InventoryTableModel _inventoryTableModel;
   private String              _identifier;

   /**
    *
    */
   public TransactionTableModel()
   {
      initialize();
   }

   /**
    *
    */
   public TransactionTableModel( InventoryTableModel model )
   {
      initialize();
      _inventoryTableModel = model;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void clear()
   {
      _tableData = new Object[ 1 ][ _columnNames.length ];
   }

   /**
    *
    */
   private void initialize()
   {
      _columnNames       = new String[ 7 ];
      _columnNames[ 0 ]  = "UID";
      _columnNames[ 1 ]  = "Transaction Item";
      _columnNames[ 2 ]  = "Cash/Check Deposit";
      _columnNames[ 3 ]  = "Credit Card Deposit";
      _columnNames[ 4 ]  = "Void Amount";
      _columnNames[ 5 ]  = "Tax Amount";
      _columnNames[ 6 ]  = "Transaction Date";
                     
      _identifier = (String) System.getProperties().get( "ultra.id.short" );
      _tableData = new Object[ 1 ][ _columnNames.length ];
      
      // Just some test data:
      _tableData[ 0 ][ 0 ] = "T:" + _identifier + ":1";
      _tableData[ 0 ][ 1 ] = "Drawer Open";
      _tableData[ 0 ][ 2 ] = ( new MoneyAmount( BigDecimal.ZERO )).getAmount();
      _tableData[ 0 ][ 3 ] = ( new MoneyAmount( BigDecimal.ZERO )).getAmount();
      _tableData[ 0 ][ 4 ] = ( new MoneyAmount( BigDecimal.ZERO )).getAmount();
      _tableData[ 0 ][ 5 ] = ( new MoneyAmount( BigDecimal.ZERO )).getAmount();
      _tableData[ 0 ][ 6 ] = new Date();

   }

   @Override
   public int getColumnCount() 
   {
      return _columnNames.length;
   }

   @Override
   public int getRowCount() 
   {
      return _tableData.length;
   }

   @Override
   public String getColumnName( int col ) 
   {
      return _columnNames[ col ];
   }

   @Override
   public Object getValueAt( int row, int col ) 
   {
      if( row < _tableData.length )
      {
         if( col < _tableData[ row ].length )
         {
            return _tableData[ row ][ col ];
         }
      }
      return null;
   }

   /**
    * JTable uses this method to determine the default renderer/
    * editor for each cell.  If we didn't implement this method,
    * then the last column would contain text ( "true"/"false" ),
    * rather than a check box.
    */
   @Override
   public Class getColumnClass( int c ) 
   {
      for( int i=0; i<_tableData.length; i++ )
      {
         Object o = getValueAt( i, c );
         if( o != null )
         {
            return o.getClass();
         }
      }
      return String.class;
   }

   /**
    * Don't need to implement this method unless your table's
    * editable.
    */
   @Override
   public boolean isCellEditable( int row, int col ) 
   {
      // Note that the data/cell address is constant,
      // no matter where the cell appears onscreen.
      if ( col == 0 || col == 6 )
      {
         return false;
      }
      else if( col == 1 && _tableData[ row ][ 1 ] != null && ((String) _tableData[ row ][ 1 ]).startsWith( "I:" ) )
      {
         return false;
      }
      else if( col != 1 && _tableData[ row ][ 1 ] == null )
      {
         return false;
      }
      else if(    _tableData[ row ][ col ] != null 
               && _tableData[ row ][ col ] instanceof BigDecimal 
               && !( (BigDecimal) _tableData[ row ][ col ]).equals( BigDecimal.ZERO.setScale( 2, BigDecimal.ROUND_HALF_EVEN ) ) )
      {
         return false;
      }

      return true;
   }

   /*
    * Don't need to implement this method unless your table's
    * data can change.
    */
   @Override
   public void setValueAt( Object value, int row, int col ) 
   {
      if( _tableData.length <= row )
      {
         addRow();
      }

      if( value instanceof BigDecimal )
      {
         value = (new MoneyAmount( (BigDecimal) value )).getAmount();
      }

      _tableData[ row ][ col ] = value;

      if( col == 2 || col == 3 )
      {
         if( _tableData[ row ][ 1 ] != null && ((String)_tableData[ row ][ 1 ]).matches( ".*([Oo]pen|[Cc]lose).*" ) )
         {
            return;
         }
         MoneyAmount cash   = new MoneyAmount( BigDecimal.ZERO );
         MoneyAmount credit = new MoneyAmount( BigDecimal.ZERO );

         if( _tableData[ row ][ 2 ] != null )
         {
            cash = new MoneyAmount( (BigDecimal)_tableData[ row ][ 2 ] );
         }
         if( _tableData[ row ][ 3 ] != null )
         {
            credit = new MoneyAmount( (BigDecimal)_tableData[ row ][ 3 ] );
         }

         MoneyAmount total       = cash.add( credit );
         MoneyAmount totalWitTax = total.markUp( new BigDecimal( "6" ) );
         _tableData[ row ][ 5 ] = totalWitTax.getAmount();
         fireTableCellUpdated( row, 5 );

         // now update the inventory table:
         int invRow = _inventoryTableModel.getRowForItemCode( (String) _tableData[ row ][ 1 ] );
         if( invRow != -1 )
         {
            _inventoryTableModel.setValueAt( new Date(), invRow, 10 );
         }
      }
      else if( col == 1 )
      {
         setValueAt( new BigDecimal( "" + _inventoryTableModel.getPriceForItemCode( (String) value ) ), row, 3 );
         fireTableCellUpdated( row, 3 );
      }

      fireTableCellUpdated( row, col );
   }

   /*
    * Don't need to implement this method unless your table's
    * data can change.
    */
   public void setValueAtLiteral( Object value, int row, int col ) 
   {
      if( _tableData.length <= row )
      {
         addRow();
      }

      if( value instanceof BigDecimal )
      {
         value = (new MoneyAmount( (BigDecimal) value )).getAmount();
      }

      _tableData[ row ][ col ] = value;
   }

   /**
    *
    */
   public void addRow()
   {
      if( _tableData == null || _tableData.length == 0 )
      {
         initialize();
         fireTableDataChanged();
         return;
      }

      Object[][] temp = new Object[ _tableData.length + 1 ][ _tableData[ 0 ].length ];
      String lastUID = (String) _tableData[ _tableData.length - 1 ][ 0 ];

      for(int i=0; i<_tableData.length; i++)
      {
         for(int j=0; j<_tableData[i].length; j++)
         {
            temp[i][j] = _tableData[i][j];
         }
      }

      _tableData = temp;

      if( lastUID != null && lastUID.length() > 5 )
      {
         lastUID = lastUID.substring( 5 );
         try
         {
            int t = Integer.parseInt( lastUID );
            lastUID = "T:" + _identifier + ":" + (t + 1);
         }
         catch( NumberFormatException e )
         {
            lastUID = "T:" + _identifier + ":1";
         }
         _tableData[ _tableData.length - 1 ][ 0 ] = lastUID;
      }
      _tableData[ _tableData.length - 1 ][ 2 ] = ( new MoneyAmount( BigDecimal.ZERO )).getAmount();
      _tableData[ _tableData.length - 1 ][ 3 ] = ( new MoneyAmount( BigDecimal.ZERO )).getAmount();
      _tableData[ _tableData.length - 1 ][ 4 ] = ( new MoneyAmount( BigDecimal.ZERO )).getAmount();
      _tableData[ _tableData.length - 1 ][ 5 ] = ( new MoneyAmount( BigDecimal.ZERO )).getAmount();
      _tableData[ _tableData.length - 1 ][ 6 ] = new Date();

      fireTableDataChanged();
   }

   /**
    *
    */
   public void deleteRow(int row)
   {
      Object[][] temp = new Object[ _tableData.length - 1 ][ _tableData[0].length ];
      for(int i=0; i<_tableData.length; i++)
      {
         if(i != row)
         {
            for(int j=0; j<_tableData[i].length; j++)
            {
               if(i > row)
               {
                  temp[i - 1][j] = _tableData[i][j];
               }
               else
               {
                  temp[i][j] = _tableData[i][j];
               }
            }
         }
      }
      _tableData = temp;
      fireTableDataChanged();
   }

   /**
    *
    */
   private void printDebugData() 
   {
      int numRows = getRowCount();
      int numCols = getColumnCount();

      for ( int i=0; i < numRows; i++ ) 
      {
         System.out.print( "    row " + i + ":" );
         for ( int j=0; j < numCols; j++ ) 
         {
            System.out.print( "  " + _tableData[ i ][ j ] );
         }
         System.out.println();
      }
      System.out.println( "--------------------------" );
   }
}
