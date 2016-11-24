package com.ultrastudios.ui.models;

import java.util.Date;
import javax.swing.table.AbstractTableModel;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import com.ultrastudios.money.*;
import java.math.BigDecimal;
import java.math.MathContext;

/**
 *
 */
public class InventoryTableModel extends AbstractTableModel
{

   private String[]   _columnNames;
   private Object[][] _tableData;

   /**
    *
    */
   public InventoryTableModel()
   {
      initialize();
   }

   /**
    *
    */
   private void initialize()
   {
      _columnNames       = new String[ 14 ];
      _columnNames[ 0 ]  = "UID";
      _columnNames[ 1 ]  = "Item Description";
      _columnNames[ 2 ]  = "Vendor";
      _columnNames[ 3 ]  = "Color";
      _columnNames[ 4 ]  = "Size";
      _columnNames[ 5 ]  = "Cost";
      _columnNames[ 6 ]  = "Mark Up%";
      _columnNames[ 7 ]  = "Price";
      _columnNames[ 8 ]  = "Profit";
      _columnNames[ 9 ]  = "Date on Floor";
      _columnNames[ 10 ] = "Date Sold";
      _columnNames[ 11 ] = "Discount Price";
      _columnNames[ 12 ] = "Return";
      _columnNames[ 13 ] = "Trade";
                     
      _tableData = new Object[ 1 ][ _columnNames.length ];

      // Just some test data:
      _tableData[ 0 ][ 0 ] = "I:1";
      _tableData[ 0 ][ 1 ] = "";
      _tableData[ 0 ][ 2 ] = "";
      _tableData[ 0 ][ 3 ] = "";
      _tableData[ 0 ][ 4 ] = "";
      _tableData[ 0 ][ 5 ] = BigDecimal.ZERO;
      _tableData[ 0 ][ 6 ] = BigDecimal.ZERO;
      _tableData[ 0 ][ 7 ] = BigDecimal.ZERO;
      _tableData[ 0 ][ 8 ] = BigDecimal.ZERO;
      _tableData[ 0 ][ 9  ] = new Date();
      _tableData[ 0 ][ 10 ] = new Date();
      _tableData[ 0 ][ 11 ] = BigDecimal.ZERO;
      _tableData[ 0 ][ 12 ] = new Boolean( false );
      _tableData[ 0 ][ 13 ] = new Boolean( false );
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
      return _tableData[ row ][ col ];
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
      if( col == 0 || col == 7 || col == 8 || col == 10 )
      {
         return false;
      }
      else 
      {
         return true;
      }
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
      _tableData[ row ][ col ] = value;

      if((col == 5 || col == 6) && _tableData[row][5] != null && _tableData[row][6] != null)
      {
         MoneyAmount cost = new MoneyAmount( (BigDecimal)_tableData[row][5] );
         _tableData[row][7] = (cost.markUp( (BigDecimal)_tableData[row][6] )).getAmount();
         fireTableCellUpdated( row, 7 );
         if(_tableData[row][11] == null)
         {
            MoneyAmount price = new MoneyAmount( (BigDecimal)_tableData[row][7] );
            _tableData[row][8] = (price.subtract(cost)).getAmount();
         }
         else
         {
            MoneyAmount discountPrice = new MoneyAmount( (BigDecimal)_tableData[row][11] );
            _tableData[row][8] = (discountPrice.subtract(cost)).getAmount();
         }
         fireTableCellUpdated( row, 8 );
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

      Object[][] temp = new Object[ _tableData.length + 1 ][ _tableData[0].length ];
      String lastUID = (String) _tableData[ _tableData.length - 1 ][ 0 ];

      for(int i=0; i<_tableData.length; i++)
      {
         for(int j=0; j<_tableData[i].length; j++)
         {
            temp[i][j] = _tableData[i][j];
         }
      }

      _tableData = temp;

      if( lastUID != null && lastUID.length() > 2 )
      {
         lastUID = lastUID.substring( 2 );
         try
         {
            int t = Integer.parseInt( lastUID );
            lastUID = "I:" + (t + 1);
         }
         catch( NumberFormatException e )
         {
            lastUID = "I:1";
         }
         _tableData[ _tableData.length - 1 ][ 0 ] = lastUID;
      }
      _tableData[ _tableData.length - 1 ][ 9 ] = new Date();

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
    * @param TYPE
    * @return TYPE
    */
   public BigDecimal getPriceForItemCode( String code )
   {
      for( int i=0; i<_tableData.length; i++ )
      {
         if( code.equals( _tableData[ i ][ 0 ] ) )
         {
            if( _tableData[ i ][ 11 ] != null )
            {
               return (BigDecimal) _tableData[ i ][ 11 ];
            }
            else
            {
               return (BigDecimal) _tableData[ i ][ 7 ];
            }
         }
      }
      return BigDecimal.ZERO;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public int getRowForItemCode( String code )
   {
      for( int i=0; i<_tableData.length; i++ )
      {
         if( code.equals( _tableData[ i ][ 0 ] ) )
         {
            return i;
         }
      }
      return -1;
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
