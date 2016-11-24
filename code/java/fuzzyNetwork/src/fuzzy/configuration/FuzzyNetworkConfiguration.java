/**
 * This class will hold all the configuration values as read
 * from the XML configuration file.
 */

package fuzzy.configuration;

import java.util.*;

/**
 * This class will hold all the configuration values as read
 * from the XML configuration file.
 */
public class FuzzyNetworkConfiguration
{
   // Holds all of the layer configuration information
   ArrayList<LayerConfiguration> _layers;
   ArrayList<Double> _parameters;

   // The location of the data file.
   String _dataLocation;

   /**
    *  
    */
   public void addLayer( LayerConfiguration layer )
   {
      if( _layers == null )
      {
         _layers = new ArrayList<LayerConfiguration>();
      }
      _layers.add( layer );
   }

   /**
    *  
    */
   public ArrayList<LayerConfiguration> getLayerConfigurations()
   {
      return _layers;
   }

   /**
    *  
    */
   public String getDataLocation()
   {
      return _dataLocation;
   }

   /**
    *  
    */
   public ArrayList<Double> getParameters()
   {
      return _parameters;
   }

   /**
    *  
    */
   public void setDataLocation( String data )
   {
      _dataLocation = data;
   }

   /**
    *  
    */
   public void setParameters( ArrayList<Double> params )
   {
      _parameters = params;
   }

}
