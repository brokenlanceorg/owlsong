/**
 * This class will hold all the configuration values as read
 * from the XML configuration file.
 */

package fuzzy.configuration;

/**
 * This class will hold all the configuration values as read
 * from the XML configuration file.
 */
public class LayerConfiguration
{
   // Number of neurons for this layer
   int _numberOfNeurons;

   // Number of fuzzy sets for this layer
   int _numberOfSets;

   /**
    *
    */
   public void setNumberOfNeurons( String n )
   {
      try
      {
         _numberOfNeurons = Integer.parseInt( n );
      }
      catch( NumberFormatException e )
      {
         _numberOfNeurons = 0;
      }
   }

   /**
    *
    */
   public void setNumberOfFuzzySets( String m )
   {
      try
      {
         _numberOfSets = Integer.parseInt( m );
      }
      catch( NumberFormatException e )
      {
         _numberOfSets = 0;
      }
   }

   /**
    *
    */
   public int getNumberOfNeurons()
   {
      return _numberOfNeurons;
   }

   /**
    *
    */
   public int getNumberOfFuzzySets()
   {
      return _numberOfSets;
   }

}
