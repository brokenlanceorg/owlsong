package common;

import java.io.Serializable;

/**
 * This interface declares the basic methods required to enable serialization of the file system type.
 * To use, simply implement this interface on a class that you want to serialize, then define the 
 * inner class XXXDAO which extends the DAOBase< XXX > class. Then, the below method "getDAO" will
 * return an instance of that DAOBase class.
 */
public interface Persistable extends Serializable
{

   /**
    *
    */
   public DAOBase getDAO();

}
