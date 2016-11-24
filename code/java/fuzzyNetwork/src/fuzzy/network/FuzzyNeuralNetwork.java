/**
 * This class performs the Fuzzy Neural Network functionality.
 * The network is the classic neural network feed-forward
 * architecture except that the nodes are Fuzzy Rule Systems.
 * 
 */

package fuzzy.network;

import java.util.*;
import fuzzy.configuration.*;
import fuzzy.*;
import math.*;

/**
 * The neural net will be architected in the same
 * manner and composed of nodes, which will do
 * most of the information processing (as opposed
 * to the connections and synapses previously).
 * To figure out how many params, notice that
 * each node will be a fuzzy rule which needs
 * to know the number of fuzzy sets and observables
 * involved (m x n), while the spec param is the
 * number of fuzzy sets times the number of obs. 
 * So, then the number of params is
 * ((m * 4) + (m * n)) * N, where N is the total
 * number of neurons in the system. Note that the
 * number of observables varies by layer.
 * A better way to look at the number of params
 * involved is to consider the architecture.
 * Since a network can be multi-layered, the 
 * total number of nodes can be calculated:
 * N = i + n1 + n2 + ... + nn, where i is the 
 * number of input observables, n1 is the number
 * of first layer nodes, etc. Then the number of
 * params will be: P = (((m1 * 4) + (m1 * n0)) * n1) +
 * (((m2 * 4) + (m2 * n1)) * n2) + ... +
 * (((mm * 4) + (mm * n_n-1)) * nn)
 * Finally, the overall system parameters that need
 * to be specified are: n0, n1, ..., nn, m1, m2, ...,
 * mm -- these will determine the network structure.
 * Then we must have P <= |p(g)|, where p() is the power
 * set and g is the set of genes.
 * Now, the next obvious question is how do we handle
 * network plasticity?
 */
public class FuzzyNeuralNetwork
{
   // Number of parameters required
   int _numberOfParams;

   // The network nodes
   FuzzyRule[][] _network;

   // Keep the config just in case
   FuzzyNetworkConfiguration _config;

   /**
    *
    */
   public FuzzyNeuralNetwork()
   {
   }

   /**
    *
    */
   public FuzzyNeuralNetwork( String name )
   {
      this( (new XMLParser()).parseXML( "simple" ) );
   }

   /**
    *
    */
   public FuzzyNeuralNetwork( FuzzyNetworkConfiguration config )
   {
      this( config, (new PowerSetTransform()).calculateTransform( config.getParameters() ) );
   }

   /**
    *
    */
   public FuzzyNeuralNetwork( FuzzyNetworkConfiguration config, ArrayList<Double> params )
   {
      _config = config;
      initialize();

      if( _numberOfParams <= params.size() )
      {
         constructNetwork( params );
      }
      else
      {
         System.out.println( "Number of params required is: " + _numberOfParams );
         System.out.println( "Number of params passed is: " + params.size() );
      }
   }

   /**
    * Then the number of
    * params will be: P = (((m1 * 4) + (m1 * n0)) * n1) +
    * (((m2 * 4) + (m2 * n1)) * n2) + ... +
    * (((mm * 4) + (mm * n_n-1)) * nn)
    */
   private void initialize()
   {
      ArrayList<LayerConfiguration> layers = _config.getLayerConfigurations();
      _network = new FuzzyRule[ layers.size() - 1 ][];
      _numberOfParams = 0;
      int previous = (layers.get(0)).getNumberOfNeurons();
      int current = 0;
      int sets = 0;

      // start at 1 since the 0th layer is the input layer
      for( int i=1; i<layers.size(); i++ )
      {
         LayerConfiguration layer = layers.get( i );
         _network[ i-1 ] = new FuzzyRule[ layer.getNumberOfNeurons() ];
         current = layer.getNumberOfNeurons();
         sets = layer.getNumberOfFuzzySets();
         _numberOfParams += ( ( (sets * 4) + (sets * previous) ) * current );
         previous = current;
      }
   }

   /**
    * Actually instantiates the network now that the param size is large enough.
    * Then the number of
    * params will be: P = (((m1 * 4) + (m1 * n0)) * n1) +
    * (((m2 * 4) + (m2 * n1)) * n2) + ... +
    * (((mm * 4) + (mm * n_n-1)) * nn)
    */
   private void constructNetwork( ArrayList<Double> params )
   {
      ArrayList<LayerConfiguration> layers = _config.getLayerConfigurations();
      int paramPos = 0;

//System.out.println( "length: " + _network.length );
      for( int i=0; i<_network.length; i++ )
      {
//System.out.println( "length[" + i + "]: " + _network[i].length );
         for( int j=0; j<_network[i].length; j++ )
         {
            // first layer is just the input layer
            LayerConfiguration layer = layers.get( i + 1 );
            int setSize = layer.getNumberOfFuzzySets();
            int len = setSize * 4;
            ArrayList<Double> sets = new ArrayList<Double>();
            ArrayList<Double> spec = new ArrayList<Double>();

            for( int k=0; k<len; k++ )
            {
               sets.add( params.get( paramPos++ ) );
            }

            layer = layers.get( i );
            len = setSize * layer.getNumberOfNeurons();

            for( int k=0; k<len; k++ )
            {
               spec.add( params.get( paramPos++ ) );
            }

            _network[i][j] = new FuzzyRule( sets, spec );
//System.out.println( "instantiating fuzzy rule at [" + i + "][" + j + "]" );
         }
      }
   }

   /**
    * 
    */
   public ArrayList<Double> recall( ArrayList<Double> input )
   {
      ArrayList<Double> observables = null;

      // go through each fuzzy rule node
      for( int i=0; i<_network.length; i++ )
      {
         observables = new ArrayList<Double>();
         for( int j=0; j<_network[i].length; j++ )
         {
            observables.add( _network[i][j].truthValue( input ) );
         }
         input = observables;
      }

      return observables;
   }

   /**
    * 
    */
   public Double train( ArrayList<ArrayList<Double>> inputs, ArrayList<ArrayList<Double>> outputs )
   {
      double sumSquared = 0;

      for( int i=0; i<inputs.size(); i++ )
      {
         ArrayList<Double> input = inputs.get( i );
         ArrayList<Double> output = outputs.get( i );
         ArrayList<Double> result = recall( input );

         for( int j=0; j<result.size(); j++ )
         {
            sumSquared += Math.abs( result.get( j ) - output.get( j ) );
         }
      }

      return sumSquared;
   }

   /**
    * 
    */
   public void test( ArrayList<ArrayList<Double>> inputs, ArrayList<ArrayList<Double>> outputs )
   {
      double sumSquared = 0;

      for( int i=0; i<inputs.size(); i++ )
      {
         ArrayList<Double> input = inputs.get( i );
         ArrayList<Double> output = outputs.get( i );
         ArrayList<Double> result = recall( input );

         System.out.print( "Input is: " );
         for( int j=0; j<input.size(); j++ )
         {
            System.out.print( input.get( j ) + " " );
         }

         System.out.print( "\nOutput is: " );
         for( int j=0; j<result.size(); j++ )
         {
            System.out.print( result.get( j ) + " " );
         }

         System.out.print( "\nShould be: " );
         for( int j=0; j<result.size(); j++ )
         {
            System.out.print( output.get( j ) + " " );
         }
         System.out.println( "" );
      }
   }

   /**
    * 
    */
   public FuzzyNetworkConfiguration getConfiguration()
   {
      return _config;
   }

   /**
    * Main method for testing.
    */
   public static void main( String[] args )
   {
/*
0.09993435968148935 0.03979130121182861 0.9843156418636992 0.9778646647338166 0.18052226004674776 0.8649901561748573 0.760451999197139 0.4926409100200647 fitness: 0.10549236885998577

0.9996659181504373 0.8776792014383388 0.23478567567932407 0.832465887728202 0.3257625034348235 0.26710027131638403 0.43991821748653936 0.5421607440637995 fitness: 0.11040986899761118

0.24666508593149683 0.2554166532550161 0.607349102205127 0.3879503439121368 0.05316577094244668 fitness: 0.1096196258013054

0.25272602482256756 0.25609459056584727 0.42738946093639324 0.7512430174076914 0.8350817100538542 fitness: 0.11560565156953928
*/

      // Construct the network
      FuzzyNeuralNetwork net = new FuzzyNeuralNetwork( "simple" );
      // Test the training methods:
      TrainingDataRepository.initializeRepository( net.getConfiguration(), 10 );
      double train = net.train( TrainingDataRepository.getInstance().getObservables(), 
                                TrainingDataRepository.getInstance().getOutputs() );
      net.test( TrainingDataRepository.getInstance().getObservables(), 
                TrainingDataRepository.getInstance().getOutputs() );

      System.out.println( "Training value: " + train );
   }

}
