package com.ultrastudios.dao;

import com.ultrastudios.ui.models.*;
import org.joda.time.DateTime;
import java.util.*;
import java.io.*;

/**
 *
 */
public class DepartmentTableDAO
{

   private DepartmentTableModel _tableModel;

   /**
    *
    */
   public DepartmentTableDAO()
   {
   }

   /**
    *
    */
   public DepartmentTableDAO( DepartmentTableModel model )
   {
      _tableModel = model;
   }

   /**
    *
    */
   public void setTableModel( DepartmentTableModel model )
   {
      _tableModel = model;
   }

   /**
    *
    */
   public DepartmentTableModel getTableModel()
   {
      return _tableModel;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void loadData( String[] words )
   {
      int c = _tableModel.getColumnCount();
      int r = 0;
      boolean load = false;

      for( int i=0; i<words.length; i++ )
      {
         if( "_TABLE_DATA_".equals( words[ i ] ) )
         {
            if( "DepartmentData".equals( words[ ++i ] ) )
            {
               load = true;
               i++;
            }
            else
            {
               load = false;
            }
         }
         if( load )
         {
            int pos = (i - 2);
            if( pos % c == 0 )
            {
               _tableModel.setValueAt( new String( words[ i ] ), r, 0 );
            }
            else if( pos % c == 1 )
            {
               _tableModel.setValueAt( new String( words[ i ] ), r, 1 );
            }
            else if( pos % c == 2 )
            {
               _tableModel.setValueAt( new String( words[ i ] ), r, 2 );
            }
            else if( pos % c == 3 )
            {
               _tableModel.setValueAt( (new DateTime( words[ i ] )).toDate(), r++, 3 );
            }
         }
      }
   }

   /**
    *
    */
   public void persistData( BufferedWriter writer ) throws IOException
   {
      writer.write( "_TABLE_DATA_,DepartmentData" );
      for( int i=0; i<_tableModel.getRowCount(); i++ )
      {
         writer.newLine();
         for( int j=0; j<_tableModel.getColumnCount(); j++ )
         {
            Object data = _tableModel.getValueAt( i, j );
            if( data == null )
            {
               writer.write( "null," );
            }
            else
            {
               if( data instanceof Date )
               {
                  writer.write( (new DateTime( (Date)data )) + "," );
               }
               else
               {
                  writer.write( data + "," );
               }
            }
         }
      }
   }

}
