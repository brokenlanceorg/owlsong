package com.ultrastudios.ui.models;

import java.util.Date;
import javax.swing.table.AbstractTableModel;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;


/**
 *
 */
public class DepartmentTableModel extends AbstractTableModel
{

   private String[]   _columnNames;
   private Object[][] _tableData;

   /**
    *
    */
   public DepartmentTableModel()
   {
      initialize();
   }

   /**
    *
    */
   private void initialize()
   {
      _columnNames = new String[ 4 ];
      _columnNames[ 0 ] = "UID";
      _columnNames[ 1 ] = "Department Name";
      _columnNames[ 2 ] = "Vendors";
      _columnNames[ 3 ] = "Date Created";

      _tableData = new Object[ 1 ][ _columnNames.length ];

      // setup data types
      _tableData[ 0 ][ 0 ] = "D:1";
      _tableData[ 0 ][ 1 ] = "";
      _tableData[ 0 ][ 2 ] = "";
      _tableData[ 0 ][ 3 ] = new Date();
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

   /*
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

   /*
    * Don't need to implement this method unless your table's
    * editable.
    */
   @Override
   public boolean isCellEditable( int row, int col ) 
   {
      // Note that the data/cell address is constant,
      // no matter where the cell appears onscreen.
      if ( col > -1 ) 
      {
         return true;
      }
      else 
      {
         return false;
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
      fireTableCellUpdated( row, col );
   }

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
            lastUID = "D:" + (t + 1);
         }
         catch( NumberFormatException e )
         {
            lastUID = "D:1";
         }
         _tableData[ _tableData.length - 1 ][ 0 ] = lastUID;
      }
      _tableData[ _tableData.length - 1 ][ 3 ] = new Date();

      fireTableDataChanged();
   }

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
