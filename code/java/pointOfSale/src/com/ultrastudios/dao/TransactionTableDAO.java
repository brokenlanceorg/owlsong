package com.ultrastudios.dao;

import com.ultrastudios.ui.models.*;
import org.joda.time.DateTime;
import java.util.*;
import java.io.*;
import java.math.*;

/**
 *
 */
public class TransactionTableDAO
{

   private TransactionTableModel _tableModel;

   /**
    *
    */
   public TransactionTableDAO()
   {
   }

   /**
    *
    */
   public TransactionTableDAO( TransactionTableModel model )
   {
      _tableModel = model;
   }

   /**
    *
    */
   public void setTableModel( TransactionTableModel model )
   {
      _tableModel = model;
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
    * @param TYPE
    * @return TYPE
    */
   public void loadData( String[] words )
   {
      _tableModel.clear();
      int c = _tableModel.getColumnCount();
      int r = 0;
      boolean load = false;

      for( int i=0; i<words.length; i++ )
      {
         if( "_TABLE_DATA_".equals( words[ i ] ) )
         {
            if( "TransactionData".equals( words[ ++i ] ) )
            {
               load = true;
               i++;
            }
            else
            {
               load = false;
            }
         }
         if( load && !"null".equals( words[ i ] ) )
         {
            int pos = (i - 4);
            if( pos % c == 0 )
            {
               _tableModel.setValueAtLiteral( new String( words[ i ] ), r, 0 );
            }
            else if( pos % c == 1 )
            {
               _tableModel.setValueAtLiteral( new String( words[ i ] ), r, 1 );
            }
            else if( pos % c == 2 )
            {
               _tableModel.setValueAtLiteral( new BigDecimal( words[ i ] ), r, 2 );
            }
            else if( pos % c == 3 )
            {
               _tableModel.setValueAtLiteral( new BigDecimal( words[ i ] ), r, 3 );
            }
            else if( pos % c == 4 )
            {
               _tableModel.setValueAtLiteral( new BigDecimal( words[ i ] ), r, 4 );
            }
            else if( pos % c == 5 )
            {
               _tableModel.setValueAtLiteral( new BigDecimal( words[ i ] ), r, 5 );
            }
            else if( pos % c == 6 )
            {
               _tableModel.setValueAtLiteral( (new DateTime( words[ i ] )).toDate(), r++, 6 );
            }
         }
      }
   }

   /**
    *
    */
   public void persistData( BufferedWriter writer ) throws IOException
   {
      writer.newLine();
      writer.write( "_TABLE_DATA_,TransactionData" );
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