package grafix;

import java.util.*;

/**
 * This class stores the base data in the CIE format.
 * ColorPoint rgb = new RGBColorPoint();
 * ColorPoint cie = rgb.getCIEColorPoint();
 */
public interface ColorPoint
{

   /**
    *
    */
   public ColorPoint getCIEColorPoint();

   /**
    *
    */
   public ColorPoint getRGBColorPoint();

   /**
    *
    */
   public ColorPoint getHLSColorPoint();

}
