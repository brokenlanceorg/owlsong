/**
 * This class will hold all the training data for the
 * Fuzzy Neural Network.
 */

package fuzzy.network;

import fuzzy.configuration.*;
import java.util.*;
import common.*;

/**
 * This class will hold all the training data for the
 * Fuzzy Neural Network.
 * 
 * If the user of this class sets the training data
 * examples, then those will be used; otherwise,
 * a random training data set is constructed.
 * 
 * 
 */
public class TrainingDataRepository
{
   // The config data for the network
   private static int _numberOfInstances;
   private static int _numberOfObservables;
   private static int _numberOfOutputs;
   private static ArrayList<ArrayList<Double>> _observables;
   private static ArrayList<ArrayList<Double>> _outputs;

   // Singleton instance
   private static TrainingDataRepository _myInstance;

   /**
    *  
    */
   private TrainingDataRepository()
   {
   }

   /**
    *  
    */
   public static TrainingDataRepository getInstance()
   {
      if( _myInstance == null )
      {
         _myInstance = new TrainingDataRepository();
      }

      return _myInstance;
   }

   /**
    *  
    */
   public static void initializeRepository( FuzzyNetworkConfiguration config, int n )
   {
      if( _numberOfObservables == 0 )
      {
         getInstance().setNumberOfObservables( 
            config.getLayerConfigurations().get(0).getNumberOfNeurons() );
         getInstance().setNumberOfOutputs( 
            config.getLayerConfigurations().get( 
               config.getLayerConfigurations().size() - 1 ).getNumberOfNeurons() );
         _numberOfInstances = n;

         _observables = new ArrayList<ArrayList<Double>>();
         _outputs = new ArrayList<ArrayList<Double>>();

         String[] words = (new FileReader( config.getDataLocation() )).getArrayOfWords();

         ArrayList<Double> temp = null;

         for( int i=0; i<words.length; )
         {
            temp = new ArrayList<Double>();
            for( int j=0; j<_numberOfObservables; j++ )
            {
               try {
                  temp.add( Double.parseDouble( words[ i++ ] ) );
               } catch( NumberFormatException e ) {}
            }
            _observables.add( temp );
            temp = new ArrayList<Double>();
            for( int j=0; j<_numberOfOutputs; j++ )
            {
               try {
                  temp.add( Double.parseDouble( words[ i++ ] ) );
               } catch( NumberFormatException e ) {}
            }
            _outputs.add( temp );
         }

         System.out.println( "Number of Observables: " + _numberOfObservables );
         System.out.println( "Number of Outputs: " + _numberOfOutputs );
         System.out.println( "Number of Instances: " + _numberOfInstances );
      }
   }

   /**
    *  
    */
   private static void setNumberOfObservables( int o )
   {
      _numberOfObservables = o;
   }

   /**
    *  
    */
   private static void setNumberOfOutputs( int o )
   {
      _numberOfOutputs = o;
   }

   /**
    *  
    */
   public static ArrayList<ArrayList<Double>> getObservables()
   {
      if( _observables == null )
      {
         _observables = new ArrayList<ArrayList<Double>>();
         for( int i=0; i<_numberOfInstances; i++ )
         {
            ArrayList<Double> instance = new ArrayList<Double>();
            for( int j=0; j<_numberOfObservables; j++ )
            {
               instance.add( Math.random() );
            }
            _observables.add( instance );
         }
      }

      return _observables;
   }

   /**
    *  
    */
   public static ArrayList<ArrayList<Double>> getOutputs()
   {
      if( _outputs == null )
      {
         _outputs = new ArrayList<ArrayList<Double>>();
         for( int i=0; i<_numberOfInstances; i++ )
         {
            ArrayList<Double> instance = new ArrayList<Double>();
            for( int j=0; j<_numberOfOutputs; j++ )
            {
               instance.add( Math.random() );
            }
            _outputs.add( instance );
         }
      }

      return _outputs;
   }

   /**
    *  
    */
   public static void setObservables( ArrayList<ArrayList<Double>> observables )
   {
      _observables = observables;
   }

   /**
    *  
    */
   public static void setOutputs( ArrayList<ArrayList<Double>> outputs )
   {
      _outputs = outputs;
   }

}
