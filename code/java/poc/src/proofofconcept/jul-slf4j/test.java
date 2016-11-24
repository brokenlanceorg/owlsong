import java.util.*;
import java.io.*;
import java.util.logging.*;
// import org.slf4j.bridge.*;

public class test
{
   private static final Logger _logger = Logger.getLogger( "com.aa.csp" );

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void test()
   {
      _logger.info( "testing info level logging" );
      _logger.fine( "testing fine fine logging" );
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public static void main( String[] args )
   {
      test t = new test();
      t.test();
   }

}
