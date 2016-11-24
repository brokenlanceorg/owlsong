package proofofconcept;

import javax.xml.ws.Endpoint;

/**
 * This simple class will publish the equally simple pocImpl to the address:
 * http://127.0.0.1:9876/poc
 */
public class pocPublisher
{

   /**
    * The usual main method.
    * @param String[] args
    */
   public static void main( String[] args )
   {
      Endpoint.publish( "http://127.0.0.1:9876/poc", new pocImpl() );
//       Endpoint.publish( "http://192.168.1.103:9876/poc", new pocImpl() );
   }

}
