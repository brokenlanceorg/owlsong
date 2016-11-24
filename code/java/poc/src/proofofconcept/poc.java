package proofofconcept;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

/**
 *
 */
@WebService
// @SOAPBinding( style = Style.RPC )
public interface poc
{

   /**
    * Very basic method to perform various tests.
    * @return String
    */
   @WebMethod
   public String performProofOfConcept();

}
