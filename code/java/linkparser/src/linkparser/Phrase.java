package linkparser;

import java.util.*;

/**
 *
 */
public class Phrase
{

   // COMMENT
   private ArrayList< Constituent > _constituents = new ArrayList< Constituent >();
   private String                   _rawData;

   /**
    *
    */
   public void setConstituents( ArrayList< Constituent > constits )
   {
      _constituents = constits;
   }

   /**
    *
    */
   public void setLine( String line )
   {
      _rawData = line;
   }

   /**
    *
    */
   public void addConstituent( Constituent con )
   {
      System.out.println( "adding constituent: " + con );
      _constituents.add( con );
   }

   /**
    *
    */
   public String getRawLine()
   {
      return _rawData;
   }

   /**
    *
    */
   public ArrayList< Constituent > getConstituents()
   {
      return _constituents;
   }

   /**
    *
    */
   public String toString()
   {
      return _rawData;
   }
}
