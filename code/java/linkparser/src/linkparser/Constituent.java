package linkparser;

import java.util.*;

/**
 *
 */
public class Constituent
{

   // COMMENT
   private Constituent _left;
   private Constituent _right;
   private String      _constituent;

   /**
    *
    */
   public Constituent getLeftConstituent()
   {
      return _left;
   }

   /**
    *
    */
   public Constituent getRightConstituent()
   {
      return _right;
   }

   /**
    *
    */
   public String getConstituent()
   {
      return _constituent;
   }

   /**
    *
    */
   public void setConstituent( String con )
   {
      _constituent = con;
   }

   /**
    *
    */
   public String toString()
   {
      return _constituent;
   }
}
