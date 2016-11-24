package com.ultrastudios.dao;

import com.ultrastudios.ui.models.*;
import org.joda.time.DateTime;
import java.util.*;
import java.io.*;
import java.math.*;

/**
 *
 */
public class ReportTableDAO
{

   private ReportTableModel _tableModel;

   /**
    *
    */
   public ReportTableDAO()
   {
   }

   /**
    *
    */
   public ReportTableDAO( ReportTableModel model )
   {
      _tableModel = model;
   }

   /**
    *
    */
   public void setTableModel( ReportTableModel model )
   {
      _tableModel = model;
   }

   /**
    *
    */
   public ReportTableModel getTableModel()
   {
      return _tableModel;
   }

   /**
    *
    */
   public void persistData( BufferedWriter writer ) throws IOException
   {
      for( int i=0; i<_tableModel.getColumnCount(); i++ )
      {
         writer.write( _tableModel.getColumnName( i ) + "," );
      }

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
                  DateTime dt = new DateTime( (Date) data );
                  writer.write( dt.dayOfMonth().getAsText() + " " +
                                dt.monthOfYear().getAsText() + " " + 
                                dt.year().getAsText() + "," );
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
