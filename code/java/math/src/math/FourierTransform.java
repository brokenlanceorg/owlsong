package math;

import java.util.*;

/**
 *
 */
public class FourierTransform
{

   private ArrayList<Double> _inputValues = null;
   private ArrayList<Double> _realMapping = null;
   private ArrayList<Double> _imaginaryMapping = null;

   /**
    *
    */
   public FourierTransform()
   {
   }

   /**
    *
    */
   public FourierTransform( ArrayList<Double> values )
   {
      _inputValues = values;
   }

   /**
    *
    */
   public ArrayList<Double> getRealMapping()
   {
      return _realMapping;
   }

   /**
    *
    */
   public ArrayList<Double> getImaginaryMapping()
   {
      return _imaginaryMapping;
   }

   /**
    * The transform is given by:
    *   X_k = Sum_n [x_n e^( -(2 pi i)/N k n )]  (k E N)
    *  and note that:
    *   e^(-i x) = cos( x ) - i sin( x )
    *
    *
    */
   public void calculateTransform()
   {
      _realMapping = new ArrayList<Double>();
      _imaginaryMapping = new ArrayList<Double>();
      double temp = 0;
      double temp2 = 0;
      double scaler = (2 * Math.PI) / (double)(_inputValues.size() + 1);
      int n = 0;

      // for all k:
      for( int i=0; i<_inputValues.size(); i++ )
      {
         n = 0;
         temp = 0;
         temp2 = 0;

         // for all n:
         for( double value : _inputValues )
         {
            temp += (value * Math.cos( scaler * i * n ));
            temp2 += (value * Math.sin( scaler * i * n ));
            n++;
         }

         _realMapping.add( temp );
         _imaginaryMapping.add( temp2 );
      }
   }

   /**
    *
    */
   public static void main( String[] args )
   {
      ArrayList<Double> testValues = new ArrayList<Double>();
      double arg = 0;
      double temp = 0;

      System.out.println( "The test values are: " );
      System.out.println( "" );

      while( arg < 500 )
      {
         arg += 0.05;
//         temp = Math.sin( arg * (Math.PI / 2) ) + Math.sin( arg ) + Math.sin( arg * Math.PI );
//         temp = Math.sin( arg * (Math.PI / 2) ) + Math.sin( arg );
//         temp = Math.sin( arg * 2 );
         temp = Math.sin( arg * Math.PI );
         testValues.add( temp );
         System.out.println( arg + "," + temp );
      }

      FourierTransform ft = new FourierTransform( testValues );
      ft.calculateTransform();
      ArrayList<Double> r = ft.getRealMapping();

      System.out.println( "The real values are: " );
      System.out.println( "" );

      for( double value : r )
      {
         System.out.println( value );
      }

      System.out.println( "The imag values are: " );
      System.out.println( "" );
      r = ft.getImaginaryMapping();

      for( double value : r )
      {
         System.out.println( value );
      }

   }

}
