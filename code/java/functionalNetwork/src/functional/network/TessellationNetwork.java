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
 * One hierarchical level up is the TessellationNetwork which is a collection of FunctionalNetwork mapped 
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
 *    
 *    
 *
 */
public class TessellationNetwork
{

   // The input is an array list of instances
   private ArrayList< ArrayList< Double > > _inputVariables;

   // The output is an array list of instances
   private ArrayList< ArrayList< Double > > _outputVariables;

   // The number of estimates for each FunctionalNetwork to make
   private int _numberOfEstimates = -1;

   // The number of vector instances for each ModuleNetwork to train on
   private int _windowSize = -1;

   // How much "overlap" does each ModuleNetwork have over the previous
   private double _overlapPercentage = -1;

   // what is considered a "match" between the input vector and the ModuleNetwork
   private double _recognitionThreshold = -1;

   // the collection of all the "tiled" ModuleNetworks
   private ArrayList< ModuleNetwork > _networks;

   // need to hold on to the last output response to deterime how to evaluate and invert
   private ArrayList< Double > _lastOutput;

   /**
    * 
    * 
    * 
    * 
    */
   public TessellationNetwork()
   {
      setNumberOfEstimates( 3 );
      setWindowSize( 10 );
      setOverlapPercentage( 0.50 );
      setRecognitionThreshold( 0.005 );
   }

   /**
    * 
    * 
    * 
    * 
    */
   public TessellationNetwork( int estimates, int winSize, double overlap, double thresh )
   {
      setNumberOfEstimates( estimates );
      setWindowSize( winSize );
      setOverlapPercentage( overlap );
      setRecognitionThreshold( thresh );
   }

   /**
    * 
    * 
    * 
    * 
    */
   public TessellationNetwork( int estimates, int winSize, double overlap, double thresh,
                               ArrayList< ArrayList< Double > > input,
                               ArrayList< ArrayList< Double > > output )
   {
      setNumberOfEstimates( estimates );
      setWindowSize( winSize );
      setOverlapPercentage( overlap );
      setRecognitionThreshold( thresh );
      setInputVariables( input );
      setOutputVariables( output );
      setupNetworks();
   }

   /**
    * 
    * 
    * 
    * 
    */
   public TessellationNetwork( ArrayList< ArrayList< Double > > input,
                               ArrayList< ArrayList< Double > > output )
   {
      setNumberOfEstimates( 3 );
      setWindowSize( 10 );
      setOverlapPercentage( 0.50 );
      setRecognitionThreshold( 0.005 );
      setInputVariables( input );
      setOutputVariables( output );
      setupNetworks();
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
   public void setWindowSize( int size )
   {
      _windowSize = size;
   }

   /**
    *
    */
   public void setOverlapPercentage( double overlap )
   {
      _overlapPercentage = overlap;
   }

   /**
    *
    */
   public void setRecognitionThreshold( double thresh )
   {
      _recognitionThreshold = thresh;
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
    * In order to call this method, we need the data (inputs, outputs) set and 
    * populated. With that data, and the individual parameters, we can now
    * instantiate all of our ModuleNetworks.
    * The basic idea is to step through the data at the overlap intervals
    * and each time grab the window size number of lines and create a new
    * ModuleNetwork and add it to the collection.
    */
   protected void setupNetworks()
   {
      int stepSize = (int)((double)_windowSize * _overlapPercentage);
      ArrayList< Double > first = _inputVariables.get( 0 );
      _networks = new ArrayList< ModuleNetwork >( (int)((double)first.size() / (double)stepSize) );
      System.out.println( "The step size is: " + stepSize );

      for( int i=0; i<(first.size() - stepSize); i+=stepSize )
      {
         ArrayList< ArrayList< Double > > input = new ArrayList< ArrayList< Double > >( _inputVariables.size() );
         ArrayList< ArrayList< Double > > output = new ArrayList< ArrayList< Double > >( _outputVariables.size() );

         // first, get the inputs
         for( ArrayList< Double > list : _inputVariables )
         {
            ArrayList< Double > temp = new ArrayList< Double >( _windowSize );
            int end = (i + _windowSize) > list.size() ? list.size() : (i + _windowSize);
            for( int j=i; j<end; j++ )
            {
               temp.add( list.get( j ) );
            }
            input.add( temp );
         }

         // next, get the outputs
         for( ArrayList< Double > list : _outputVariables )
         {
            ArrayList< Double > temp = new ArrayList< Double >( _windowSize );
            int end = (i + _windowSize) > list.size() ? list.size() : (i + _windowSize);
            for( int j=i; j<end; j++ )
            {
               temp.add( list.get( j ) );
            }
            output.add( temp );
         }

         _networks.add( new ModuleNetwork( _numberOfEstimates, input, output ) );
      }
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
   public void train()
   {
      for( ModuleNetwork network : _networks )
      {
         network.train();
      }
   }

   /**
    * First, we have to determine which Module handles this input and to figure that
    * out, we need to have the previous output response. To determine which Module[s]
    * handle the input, we find all that are at or below our moving threshold.
    * If more than one, we find median of output, otherwise, we choose the best.
    */
   public ArrayList< Double > evaluate( ArrayList< Double > input )
   {
      ArrayList< Double > result = null;
      ArrayList< ModuleNetwork > potentials = new ArrayList< ModuleNetwork >( _networks.size() );

      // if this is the first pass, we don't know which module would handle this
      // so, we just choose the first in the network list.
      if( _lastOutput == null )
      {
         result = _networks.get( 0 ).evaluate( input );
      }
      else
      {
         double thresh = _recognitionThreshold;
         int count = -1;
         while( potentials.size() < 1 || ( ++count < _networks.size() ) )
         {
            for( ModuleNetwork network : _networks )
            {
               if( network.getInputDistance( input, _lastOutput ) < thresh )
               {
                  potentials.add( network );
               }
            }
            thresh *= 2;
         }
         if( potentials.size() == 0 )
         {
            result = _networks.get( 0 ).evaluate( input );
         }
         else
         {
            ArrayList< ArrayList< Double > > results = new ArrayList< ArrayList< Double > >( potentials.size() );
            for( ModuleNetwork network : potentials )
            {
               results.add( network.evaluate( input ) );
            }
            ArrayList< Double > first = results.get( 0 );
            result = new ArrayList< Double >( first.size() );
            for( int i=0; i<first.size(); i++ )
            {
               ArrayList< Double > temp = new ArrayList< Double >( results.size() );
               for( ArrayList< Double > data : results )
               {
                  temp.add( data.get( i ) );
               }
               StatUtilities stat = new StatUtilities();
               stat.calculateStats( temp );
               result.add( stat.getMedian() );
            }
         }
      }

      _lastOutput = result;

      return result;
   }

   /**
    *
    */
   public ArrayList< Double > invert( ArrayList< Double > input )
   {
      return null;
   }

   /**
    *
    */
   public void test()
   {
      System.out.println( "Testing a network:" );
      ArrayList< ArrayList< Double > > inputVariables = _inputVariables;
      ArrayList< Double > first = inputVariables.get( 0 );

      for( int i=0; i<first.size(); i++ )
      {
         ArrayList< Double > inputVector = new ArrayList< Double >( inputVariables.size() );
         ArrayList< Double > outputVector = null;

         System.out.print( "\na mapping:, " );
         for( ArrayList< Double > input : inputVariables )
         {
            inputVector.add( input.get( i ) );
            System.out.print( input.get( i ) + "," );
         }

         outputVector = evaluate( inputVector );

         // print out the results:
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
      int numberOfInstances = 20;
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

      TessellationNetwork network = new TessellationNetwork( inputs, outputs );
      network.train();
      network.test();
   }

}
