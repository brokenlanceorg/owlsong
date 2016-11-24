package functional.network;

import java.util.*;
import genetic.*;
import math.*;

/**
 *
 * The general idea for the neural network inference engine is to be composed of smaller networks
 * each adept at handling their own area of expertise. 
 *
 * At the low-level, there's the FunctionalCorrelation class that defines a single variable in 
 * terms of several independent variables; this class can handle only small amounts of data 
 * (when making the error level low, and keeping the training time low).
 *
 * The benefit of this method is that it should extrapolate better than traditional networks and each
 * network is different than the others, which adds to the diversity.
 *
 * Next, is the FunctionalNetwork class; this class takes the FunctionalCorrelation one step further
 * by mapping the multiple independent variables to multiple independent variables.
 *
 * One hierarchical level up is the ModuleNetwork which is a collection of FunctionalNetwork mapped 
 * in different ways to the same target data. For example, there will be a regular mapping of the input
 * to the output, the mapping of the current input to the previous output.
 *
 * One level further up is TessellationNetwork, a collection of NetworkModules which are "tiled" 
 * to cover the independent variables since only small sizes are efficient. 
 * Additionally, this class has a "recognition" mechanism to determine if this instance handles the input data.
 *
 * note: the training data is defined in terms of independent variables. so, for instance, if we have
 * 3 independent variables forming a sequence of 3-vectors in time. The data for the 3-vectors
 * < x1, x2, x3, ..., xn > would be in a two dimensional array such as:
 *  x1, x2, x3, ..., xn
 *   1,  2,  3, ...,  n
 * n+1,n+2,n+3, ...,n+n
 * etc.,...
 *
 * The running time is proportional to the following parameters: 
 *    the number of FunctionalNetworks, which is twice the size of the number of input variables.
 *    
 *    
 *    
 *    
 *
 */
public class ModuleNetwork
{

   // The input is an array list of instances
   private ArrayList< ArrayList< Double > > _inputVariables;

   // The output is an array list of instances
   private ArrayList< ArrayList< Double > > _outputVariables;

   // The collection of our functional networks
//    private ArrayList< FunctionalNetwork > _networks;
   // This maps the current input to the current output
   private FunctionalNetwork _currentNetwork;
   // This maps the current input to the previous output
   private FunctionalNetwork _previousNetwork;

   // The number of estimates for each FunctionalNetwork to make
   private int _numberOfEstimates = -1;

   // The window size for the non-regular networks in percentage
   private double _windowSize = 0.3; // default 30 percent

   /**
    * This class will create a collection of FunctionalNetworks which each will submit
    * it's own estimate for the output of the mapping function. The first network will
    * be a regular network which will map the usual inputs to the outputs in the data
    * as it exists.
    */
   public ModuleNetwork()
   {
      setNumberOfEstimates( 3 );
   }

   /**
    * This class will create a collection of FunctionalNetworks which each will submit
    * it's own estimate for the output of the mapping function. The first network will
    * be a regular network which will map the usual inputs to the outputs in the data
    * as it exists.
    */
   public ModuleNetwork( int estimates )
   {
      setNumberOfEstimates( estimates );
   }

   /**
    * This class will create a collection of FunctionalNetworks which each will submit
    * it's own estimate for the output of the mapping function. The first network will
    * be a regular network which will map the usual inputs to the outputs in the data
    * as it exists.
    */
   public ModuleNetwork( int estimates, 
                         ArrayList< ArrayList< Double > > input,
                         ArrayList< ArrayList< Double > > output )
   {
      setNumberOfEstimates( estimates );
      setInputVariables( input );
      setOutputVariables( output );
      setupNetworks();
   }

   /**
    *
    */
   public void setWindowSize( double size )
   {
      _windowSize = size;
//       _networks = null;
   }

   /**
    *
    * To map current input to previous output, you will map:
    * 1, 2, 3  ==> 1, 2, 3
    * 4, 5, 6  ==> 4, 5, 6
    * 7, 8, 9  ==> 7, 8, 9
    *
    * into:
    *
    * 4, 5, 6  ==> 1, 2, 3
    * 7, 8, 9  ==> 4, 5, 6
    *
    * To map previous input to current output, you will map:
    * 1, 2, 3  ==> 1, 2, 3
    * 4, 5, 6  ==> 4, 5, 6
    * 7, 8, 9  ==> 7, 8, 9
    *
    * into:
    *
    * 1, 2, 3  ==> 4, 5, 6
    * 4, 5, 6  ==> 7, 8, 9
    *
    */
   protected void setupNetworks()
   {
      // The regular network
      _currentNetwork = new FunctionalNetwork( _numberOfEstimates, _inputVariables, _outputVariables );
      // The non-regular current input to previous output
      ArrayList< ArrayList< Double > > input = getFirstInstancesRemoved( 1, _inputVariables );
      ArrayList< ArrayList< Double > > output = getLastInstancesRemoved( 1, _outputVariables );
      _previousNetwork = new FunctionalNetwork( _numberOfEstimates, input, output );

//       // the number of networks will be the number of inputs plus one for the regular network
//       _networks = new ArrayList< FunctionalNetwork >( 2 * _inputVariables.size() );
//       int winSize = -1;
// 
//       // first, create the regular networks -- we create this many to keep the non-regular
//       // transposed networks from swamping the median result.
//       for( ArrayList< Double > var : _inputVariables )
//       {
//          if( winSize <= 0 )
//          {
//             winSize = (int)( _windowSize * var.size() );
//          }
//          _networks.add( new FunctionalNetwork( _numberOfEstimates, _inputVariables, _outputVariables ) );
//       }
//       
//       // second, create the transposed networks
//       for( ArrayList< Double > var : _inputVariables )
//       {
//          Sequence seq = new Sequence( var );
//          ArrayList< ArrayList< Double > > trans = seq.getTransposedSequence( winSize );
//          ArrayList< ArrayList< Double > > outputs = getModifiedOutputs( _outputVariables, winSize );
//          _networks.add( new FunctionalNetwork( _numberOfEstimates, trans, outputs ) );
//       }
   }

   /**
    * This method will remove the first num instances from the vector instances.
    * @param num -- the number of instances to remove
    */
   public ArrayList< ArrayList< Double > > getFirstInstancesRemoved( int num, ArrayList< ArrayList< Double > > data )
   {
      ArrayList< ArrayList< Double > > result = new ArrayList< ArrayList< Double > >( data.size() );

      for( ArrayList< Double > list : data )
      {
         ArrayList< Double > temp = new ArrayList< Double >( list.size() - num );
         for( int i=num; i<list.size(); i++ )
         {
            temp.add( list.get( i ) );
         }
         result.add( temp );
      }

      return result;
   }

   /**
    * This method will remove the last num instances from the vector instances.
    * @param num the number of last instances to remove
    */
   public ArrayList< ArrayList< Double > > getLastInstancesRemoved( int num, ArrayList< ArrayList< Double > > data )
   {
      ArrayList< ArrayList< Double > > result = new ArrayList< ArrayList< Double > >( data.size() );

      for( ArrayList< Double > list : data )
      {
         ArrayList< Double > temp = new ArrayList< Double >( list.size() - num );
         for( int i=0; i<(list.size() - num); i++ )
         {
            temp.add( list.get( i ) );
         }
         result.add( temp );
      }

      return result;
   }

   /**
    *
    */
   protected ArrayList< ArrayList< Double > > getModifiedOutputs( ArrayList< ArrayList< Double > > data, int size )
   {
      ArrayList< ArrayList< Double > > result = new ArrayList< ArrayList< Double > >( data.size() );

      for( ArrayList< Double > list : data )
      {
         ArrayList< Double > temp = new ArrayList< Double >( list.size() - size );
         for( int i=(size - 1); i<list.size(); i++ )
         {
            temp.add( list.get( i ) );
         }
         result.add( temp );
      }

      return result;
   }

   /**
    *
    */
   public void setNumberOfEstimates( int estimates )
   {
      _numberOfEstimates = estimates;
   }

   /**
    *
    */
   public void setInputVariables( ArrayList< ArrayList < Double > > input )
   {
      _inputVariables = input;
   }

   /**
    *
    */
   public void setOutputVariables( ArrayList< ArrayList < Double > > output )
   {
      _outputVariables = output;
   }

   /**
    *
    */
   public void train()
   {
//       if( _networks == null )
//       {
//          setupNetworks();
//       }
// 
//       for( FunctionalNetwork network : _networks )
//       {
//          network.train();
//       }
      _currentNetwork.train();
      _previousNetwork.train();
   }

   /**
    *
    */
   public ArrayList< Double > evaluate( ArrayList< Double > input )
   {
      return _currentNetwork.evaluate( input );
   }

   /**
    * @param current input
    * @param previous output
    */
   public double getInputDistance( ArrayList< Double > input, ArrayList< Double > previousOutput )
   {
      ArrayList< Double > output = _previousNetwork.evaluate( input );
      double result = Double.MAX_VALUE;

      if( output.size() == previousOutput.size() )
      {
         result = 0;
         for( int i=0; i<output.size(); i++ )
         {
            result += Math.abs( output.get( i ) - previousOutput.get( i ) );
         }
      }

      return result;
   }

   /**
    *
    */
   public ArrayList< Double > invert( ArrayList< Double > input )
   {
      return _currentNetwork.invert( input );
   }

   /**
    *
    */
   public void test()
   {
      System.out.println( "Testing a network:" );
      ArrayList< ArrayList< Double > > outputVariables = _currentNetwork.getOutputVariables();
      ArrayList< ArrayList< Double > > inputVariables = _currentNetwork.getInputVariables();

      ArrayList< Double > first = outputVariables.get( 0 );

      for( int i=0; i<first.size(); i++ )
      {
         System.out.print( "\nreal output vector:, " );
         for( ArrayList< Double > output : outputVariables )
         {
            System.out.print( output.get( i ) + "," );
         }
      }

      ArrayList< Double > previousOutput = null;
      for( int i=0; i<first.size(); i++ )
      {
         ArrayList< Double > inputVector = new ArrayList< Double >( inputVariables.size() );
         ArrayList< Double > outputVector = null;
         double distance = 0;

         if( previousOutput != null )
         {
            distance = getInputDistance( inputVector, previousOutput );
         }
         System.out.print( "\ndistance:," + distance + ",a mapping:, " );
         for( ArrayList< Double > input : inputVariables )
         {
            inputVector.add( input.get( i ) );
            System.out.print( input.get( i ) + "," );
         }

         outputVector = evaluate( inputVector );
         previousOutput = (ArrayList< Double >)outputVector.clone();

         // print out the results:
         System.out.print( ",==>," );
         for( Double out : outputVector )
         {
            System.out.print( out + "," );
         }

         // print out the original data values:
         outputVector = invert( outputVector );
         System.out.print( ",==>," );
         for( Double out : outputVector )
         {
            System.out.print( out + "," );
         }
      }
   }

   /**
    * Still need to add save and restore methods.
    */
   public static void main( String[] args )
   {
      int numberOfInputs = 3;
      int numberOfOutputs = 3;
      int numberOfInstances = 10;
      ArrayList< ArrayList< Double > > inputs = new ArrayList< ArrayList< Double > >( numberOfInputs );
      ArrayList< ArrayList< Double > > outputs = new ArrayList< ArrayList< Double > >( numberOfOutputs );

      System.out.println( "Creating input data..." );
      // First, create our test data
      for( int i=0; i<numberOfInputs; i++ )
      {
         ArrayList< Double > input = new ArrayList< Double >( numberOfInstances );
         for( int j=0; j<numberOfInstances; j++ )
         {
            input.add( Math.random() );
         }
         inputs.add( input );
      }

      System.out.println( "Creating output data..." );
      for( int i=0; i<numberOfOutputs; i++ )
      {
         ArrayList< Double > output = new ArrayList< Double >( numberOfInstances );
         for( int j=0; j<numberOfInstances; j++ )
         {
            output.add( Math.random() );
         }
         outputs.add( output );
      }

      ModuleNetwork network = new ModuleNetwork( 3, inputs, outputs );
      network.train();
      network.test();
   }

}
