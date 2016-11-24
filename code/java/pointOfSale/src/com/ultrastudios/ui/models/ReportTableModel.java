package com.ultrastudios.ui.models;

import java.util.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.*;
import org.joda.time.DateTime;
import org.joda.time.DateMidnight;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import com.ultrastudios.money.*;
import java.math.BigDecimal;
import java.math.MathContext;


/**
 *
 */
public class ReportTableModel extends AbstractTableModel
{

   private String[]              _columnNames;
   private Object[][]            _tableData;
   private TransactionTableModel _transactionTableModel;
   private JTable                _transactionTable;

   /**
    *
    */
   public ReportTableModel()
   {
      initialize();
   }

   /**
    *
    */
   public ReportTableModel( JTable table )
   {
      initialize();
      _transactionTable      = table;
      _transactionTableModel = (TransactionTableModel) _transactionTable.getModel();
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void setTransactionTable( JTable table )
   {
      _transactionTable      = table;
      _transactionTableModel = (TransactionTableModel) _transactionTable.getModel();
   }

   /**
    *
    */
   private void initialize()
   {
      _columnNames       = new String[ 10 ];
      _columnNames[ 0 ]  = "Date";
      _columnNames[ 1 ]  = "Net 1";
      _columnNames[ 2 ]  = "Tax";
      _columnNames[ 3 ]  = "Net 2";
      _columnNames[ 4 ]  = "Credit Card Deposit";
      _columnNames[ 5 ]  = "Void";
      _columnNames[ 6 ]  = "Bank Deposit Cash & Checks";
      _columnNames[ 7 ]  = "Drawer Open Count";
      _columnNames[ 8 ]  = "Drawer Close Count";
      _columnNames[ 9 ]  = "Return - Short / Over";
                     
      _tableData = new Object[ 1 ][ _columnNames.length ];
      
      // Just some test data:
      _tableData[ 0 ][ 0 ] = new Date();
      _tableData[ 0 ][ 1 ] = ( new MoneyAmount( BigDecimal.ZERO )).getAmount();
      _tableData[ 0 ][ 2 ] = ( new MoneyAmount( BigDecimal.ZERO )).getAmount();
      _tableData[ 0 ][ 3 ] = ( new MoneyAmount( BigDecimal.ZERO )).getAmount();
      _tableData[ 0 ][ 4 ] = ( new MoneyAmount( BigDecimal.ZERO )).getAmount();
      _tableData[ 0 ][ 5 ] = ( new MoneyAmount( BigDecimal.ZERO )).getAmount();
      _tableData[ 0 ][ 6 ] = ( new MoneyAmount( BigDecimal.ZERO )).getAmount();
      _tableData[ 0 ][ 7 ] = ( new MoneyAmount( BigDecimal.ZERO )).getAmount();
      _tableData[ 0 ][ 8 ] = ( new MoneyAmount( BigDecimal.ZERO )).getAmount();
      _tableData[ 0 ][ 9 ] = ( new MoneyAmount( BigDecimal.ZERO )).getAmount();
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
      return false;
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
      fireTableCellUpdated( row, col );
   }

   /**
    *
    */
   public void addRow()
   {
      Object[][] temp = new Object[ _tableData.length + 1 ][ _tableData[ 0 ].length ];

      for(int i=0; i<_tableData.length; i++)
      {
         for(int j=0; j<_tableData[i].length; j++)
         {
            temp[i][j] = _tableData[i][j];
         }
      }

      _tableData = temp;

      _tableData[ _tableData.length - 1 ][ 0 ] = new Date();
      _tableData[ _tableData.length - 1 ][ 1 ] = ( new MoneyAmount( BigDecimal.ZERO )).getAmount();
      _tableData[ _tableData.length - 1 ][ 2 ] = ( new MoneyAmount( BigDecimal.ZERO )).getAmount();
      _tableData[ _tableData.length - 1 ][ 3 ] = ( new MoneyAmount( BigDecimal.ZERO )).getAmount();
      _tableData[ _tableData.length - 1 ][ 4 ] = ( new MoneyAmount( BigDecimal.ZERO )).getAmount();
      _tableData[ _tableData.length - 1 ][ 5 ] = ( new MoneyAmount( BigDecimal.ZERO )).getAmount();
      _tableData[ _tableData.length - 1 ][ 6 ] = ( new MoneyAmount( BigDecimal.ZERO )).getAmount();
      _tableData[ _tableData.length - 1 ][ 7 ] = ( new MoneyAmount( BigDecimal.ZERO )).getAmount();
      _tableData[ _tableData.length - 1 ][ 8 ] = ( new MoneyAmount( BigDecimal.ZERO )).getAmount();
      _tableData[ _tableData.length - 1 ][ 9 ] = ( new MoneyAmount( BigDecimal.ZERO )).getAmount();

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
    * Transaction Table model columns are:
    *
    * _columnNames[ 0 ]  = "UID";
    * _columnNames[ 1 ]  = "Transaction Item";
    * _columnNames[ 2 ]  = "Cash/Check Deposit";
    * _columnNames[ 3 ]  = "Credit Card Deposit";
    * _columnNames[ 4 ]  = "Void Amount";
    * _columnNames[ 5 ]  = "Tax Amount";
    * _columnNames[ 6 ]  = "Transaction Date";
    *
    * Report Table model columns are:
    *
    * _columnNames[ 0 ]  = "Date";
    * _columnNames[ 1 ]  = "Net 1";
    * _columnNames[ 2 ]  = "Tax";
    * _columnNames[ 3 ]  = "Net 2";
    * _columnNames[ 4 ]  = "Credit Card Deposit";
    * _columnNames[ 5 ]  = "Void";
    * _columnNames[ 6 ]  = "Bank Deposit Cash & Checks";
    * _columnNames[ 7 ]  = "Drawer Open Count";
    * _columnNames[ 8 ]  = "Drawer Close Count";
    * _columnNames[ 9 ]  = "Return - Short / Over";
    *
    * @param TYPE
    * @return TYPE
    */
   public void recalculate( int[] rows )
   {
      DateMidnight previous = null;
      int row = _transactionTable.convertRowIndexToModel( rows[ 0 ] );
      int myRow = 0;

      DateMidnight day = ( new DateTime( (Date) _transactionTableModel.getValueAt( row, 6 ) ) ).toDateMidnight();
      MoneyAmount cash = new MoneyAmount( (BigDecimal) _transactionTableModel.getValueAt( row, 2 ) );
      MoneyAmount credit = new MoneyAmount( (BigDecimal) _transactionTableModel.getValueAt( row, 3 ) );
      MoneyAmount voidAmount = new MoneyAmount( (BigDecimal) _transactionTableModel.getValueAt( row, 4 ) );
      MoneyAmount tax = new MoneyAmount( (BigDecimal) _transactionTableModel.getValueAt( row, 5 ) );

      initialize();

      _tableData[ myRow ][ 0 ] = day.toDate();
      Object o = _transactionTableModel.getValueAt( row, 1 );
      if( o != null && ((String) o).matches( ".*[Oo]pen.*" ) )
      {
         _tableData[ myRow ][ 7 ] = cash.getAmount();
         _tableData[ myRow ][ 8 ] = ( new MoneyAmount( BigDecimal.ZERO )).getAmount();
      }
      else if( o != null && ((String) o).matches( ".*[Cc]lose.*" ) )
      {
         _tableData[ myRow ][ 8 ] = cash.getAmount();
         _tableData[ myRow ][ 7 ] = ( new MoneyAmount( BigDecimal.ZERO )).getAmount();
      }
      else
      {
         _tableData[ myRow ][ 1 ] = cash.add( credit ).getAmount();
         _tableData[ myRow ][ 2 ] = tax.getAmount();
         _tableData[ myRow ][ 3 ] = cash.add( credit ).add( tax ).getAmount();
         _tableData[ myRow ][ 4 ] = credit.getAmount();
         _tableData[ myRow ][ 5 ] = voidAmount.getAmount();
         _tableData[ myRow ][ 6 ] = cash.getAmount();
         _tableData[ myRow ][ 7 ] = ( new MoneyAmount( BigDecimal.ZERO )).getAmount();
         _tableData[ myRow ][ 8 ] = ( new MoneyAmount( BigDecimal.ZERO )).getAmount();
      }
      _tableData[ myRow ][ 9 ] = ( new MoneyAmount( (BigDecimal)_tableData[ 0 ][ 8 ] ) ).subtract( new MoneyAmount( (BigDecimal)_tableData[ 0 ][ 7 ] ) ).getAmount();
      previous = day;

      for( int i=1; i<rows.length; i++ )
      {
         row = _transactionTable.convertRowIndexToModel( rows[ i ] );
         day = ( new DateTime( (Date) _transactionTableModel.getValueAt( row, 6 ) ) ).toDateMidnight();

         // New day and row...
         if( day.isAfter( previous ) )
         {
            addRow();
            myRow++;
            day = ( new DateTime( (Date) _transactionTableModel.getValueAt( row, 6 ) ) ).toDateMidnight();
            cash = new MoneyAmount( (BigDecimal) _transactionTableModel.getValueAt( row, 2 ) );
            credit = new MoneyAmount( (BigDecimal) _transactionTableModel.getValueAt( row, 3 ) );
            voidAmount = new MoneyAmount( (BigDecimal) _transactionTableModel.getValueAt( row, 4 ) );
            tax = new MoneyAmount( (BigDecimal) _transactionTableModel.getValueAt( row, 5 ) );
            _tableData[ myRow ][ 0 ] = day.toDate();
            _tableData[ myRow ][ 2 ] = tax.getAmount();
            _tableData[ myRow ][ 5 ] = voidAmount.getAmount();
            o = _transactionTableModel.getValueAt( row, 1 );
            if( o != null && ((String) o).matches( ".*[Oo]pen.*" ) )
            {
               _tableData[ myRow ][ 7 ] = cash.getAmount();
               _tableData[ myRow ][ 1 ] = ( new MoneyAmount( BigDecimal.ZERO )).getAmount();
               _tableData[ myRow ][ 3 ] = ( new MoneyAmount( BigDecimal.ZERO )).getAmount();
            }
            else if( o != null && ((String) o).matches( ".*[Cc]lose.*" ) )
            {
               _tableData[ myRow ][ 8 ] = cash.getAmount();
               _tableData[ myRow ][ 1 ] = ( new MoneyAmount( BigDecimal.ZERO )).getAmount();
               _tableData[ myRow ][ 3 ] = ( new MoneyAmount( BigDecimal.ZERO )).getAmount();
            }
            else
            {
               _tableData[ myRow ][ 1 ] = cash.add( credit ).getAmount();
               _tableData[ myRow ][ 3 ] = cash.add( credit ).add( tax ).getAmount();
               _tableData[ myRow ][ 4 ] = credit.getAmount();
               _tableData[ myRow ][ 6 ] = cash.getAmount();
            }
            _tableData[ myRow ][ 9 ] = ( new MoneyAmount( (BigDecimal)_tableData[ 0 ][ 8 ] ) ).subtract( new MoneyAmount( (BigDecimal)_tableData[ 0 ][ 7 ] ) ).getAmount();
         }
         // Same day and row...
         else
         {
            cash = new MoneyAmount( (BigDecimal) _transactionTableModel.getValueAt( row, 2 ) );
            credit = new MoneyAmount( (BigDecimal) _transactionTableModel.getValueAt( row, 3 ) );
            voidAmount = new MoneyAmount( (BigDecimal) _transactionTableModel.getValueAt( row, 4 ) );
            tax = new MoneyAmount( (BigDecimal) _transactionTableModel.getValueAt( row, 5 ) );
            _tableData[ myRow ][ 2 ] = ( new MoneyAmount( (BigDecimal)_tableData[ myRow ][ 2 ] ) ).add( tax ).getAmount();
            _tableData[ myRow ][ 5 ] = ( new MoneyAmount( (BigDecimal)_tableData[ myRow ][ 5 ] ) ).add( voidAmount ).getAmount();
            o = _transactionTableModel.getValueAt( row, 1 );
            if( o != null && ((String) o).matches( ".*[Oo]pen.*" ) )
            {
               _tableData[ myRow ][ 7 ] = ( new MoneyAmount( (BigDecimal)_tableData[ myRow ][ 7 ] ) ).add( cash ).getAmount();
            }
            else if( o != null && ((String) o).matches( ".*[Cc]lose.*" ) )
            {
               _tableData[ myRow ][ 8 ] = ( new MoneyAmount( (BigDecimal)_tableData[ myRow ][ 8 ] ) ).add( cash ).getAmount();
            }
            else
            {
               _tableData[ myRow ][ 1 ] = ( new MoneyAmount( (BigDecimal)_tableData[ myRow ][ 1 ] ) ).add( cash ).add( credit ).getAmount();
               _tableData[ myRow ][ 3 ] = ( new MoneyAmount( (BigDecimal)_tableData[ myRow ][ 3 ] ) ).add( cash ).add( credit ).add( tax ).getAmount();
               _tableData[ myRow ][ 4 ] = ( new MoneyAmount( (BigDecimal)_tableData[ myRow ][ 4 ] ) ).add( credit ).getAmount();
               _tableData[ myRow ][ 6 ] = ( new MoneyAmount( (BigDecimal)_tableData[ myRow ][ 6 ] ) ).add( cash ).getAmount();
            }
            _tableData[ myRow ][ 9 ] = ( new MoneyAmount( (BigDecimal)_tableData[ myRow ][ 8 ] ) ).subtract( new MoneyAmount( (BigDecimal)_tableData[ myRow ][ 7 ] ) ).getAmount();
         }

         previous = day;
      }

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
