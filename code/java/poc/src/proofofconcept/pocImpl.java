package proofofconcept;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.imageio.*;

/**
 *
 */
@WebService( endpointInterface = "proofofconcept.poc" )
public class pocImpl implements poc 
{

   /**
    * Very basic method to perform various tests.
    * @return String
    */
   public String performProofOfConcept() 
   {
      String result = "Hello World!";
      double value  = 0.91415928;
      byte bValue   = ( byte )( value * 255 );

      System.out.println( "The byte value is: " + bValue );
      System.out.println( "Will return the value: " + result );

      double arg1      = 0.01;
      double arg2      = 0.9;
      double arg11     = 0;
      double arg22     = 0;
      double v1        = 1.8;
      double v2        = 1.8002777;
      double last1     = 0;
      double last2     = 0.1;
      double threshold = 0.99;
      double s1        = 8 / (2 * Math.PI / v1);
      double s2        = 8 / (2 * Math.PI / v2);
      double shift     = 0.005;
      double sfreq     = 0.0001;
      long   start     = System.currentTimeMillis();

      // dom( sin )   = [     0, 2pi  ] length = 2 * pi
      // dom( tanh )  = [    -4, 4    ] length = 8
      // dom( 1 / x ) = [ -1, 1 ] length = 2
      for( int i=0; i<1000000; i++ )
      {
         arg1 += 0.001;
         arg2 += 0.001;
         s1 = 8 / (2 * Math.PI / v1);
         s2 = 8 / (2 * Math.PI / v2);
         arg11 = ( s1 * v1 * arg1 ) - 4;
         arg22 = ( s2 * v2 * arg2 ) - 4;
         if( arg11 < 0.0 )
         {
            arg11 = -1 * Math.abs( arg11 + 4 );
         }
         else
         {
            arg11 = 4 - arg11;
         }
         if( arg22 < 0.0 )
         {
            arg22 = -1 * Math.abs( arg22 + 4 );
         }
         else
         {
            arg22 = 4 - arg22;
         }

         value = Math.sin( v1 * arg1 );
         if( last1 < 0.0 && value >= 0.0 )
         {
            if( last2 >= 0.0 )
            {
               arg2 -= shift;
               if( arg2 < 0.0 )
               {
                  arg2 = 0.0;
               }
            }
            else
            {
               arg2 += shift;
            }
            arg22 = sfreq * Math.tanh( arg22 );
            System.out.println( "ONE firing: " + i + " v2: " + v2 + " arg2: " + arg2 + " arg1: " + arg1 + " arg22: " + arg22 );
            arg1 = 0;
         }
         last1 = value;

         value = Math.sin( v2 * arg2 );
         if( last2 < 0.0 && value >= 0.0 )
         {
            if( last1 >= 0.0 )
            {
               arg1 -= shift;
               if( arg1 < 0.0 )
               {
                  arg1 = 0.0;
               }
            }
            else
            {
               arg1 += shift;
            }
            arg11 = sfreq * Math.tanh( arg11 );
//             v1 += arg11;
            System.out.println( "TWO firing: " + i + " v1: " + v1 + " arg1: " + arg1 + " arg2: " + arg2 + " arg11: " + arg11 );
            arg2 = 0;
         }
         last2 = value;
      }
      System.out.println( "Time: " + ( System.currentTimeMillis() - start ) );

      return result;
   }

}
