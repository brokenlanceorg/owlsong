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
 * One hierarchical level up is the ModuleNetwork which is a collection of SimpleNetworks mapped 
 * in different ways to the same target data. For example, each independent variable is transposed
 * and mapped to the same target output via a SimpleNetwork.
 *
 * One level further up is TessellationNetwork, a collection of NetworkModules which are "tiled" 
 * to cover the independent variables since only small sizes are efficient. 
 * Additionally, this class has a "recognition" mechanism to determine if this instance handles the input data.
 *
 * The running time is proportional to the following parameters: 
 *    elapsed time set in the genetic.properties in the GA module
 *    the number of estimates for each output variable
 *    the size of the output vector
 *    and somewhat the size of the input vector
 *
 * NOTE: Something to be noted is that the training data is transposed when compared to the actual
 * evaluate method. It was just easier to think of vector instances as arrays of instances for each
 * vector position for training and visualization. Then, when the evaluate method runs, it only takes
 * a single vector -- not a collection of vector instances as the FunctionCorrelation object requires.
 *
 * NOTE: The training data is defined in terms of independent variables. So, for instance, if we have
 * 3 independent variables forming a sequence of 3-vectors in time. The data for the 3-vectors
 * < x1, x2, x3, ..., xn > would be in a two dimensional array such as:
 *  x1, x2, x3, ..., xn
 *   1,  2,  3, ...,  n
 * n+1,n+2,n+3, ...,n+n
 * etc.,...
 *
 * TODO: Need a way to figure out if this network is mature or not and where to load and save params.
 *
 */
public class FunctionalNetwork
{

   // The independent input variables on which to train this network.
   private ArrayList< ArrayList < Double > > _inputVariables;

   // The independent output variables on which to train this network.
   private ArrayList< ArrayList < Double > > _outputVariables;

   // The FunctionalCorrelation objects
   private ArrayList< ArrayList < FunctionalCorrelation > > _functions;

   // The number of instances to estimate or train for each output variable
   private int _numberOfEstimates = -1;

   // The number of miliseconds to train the FunctionalCorrelation networks.
   private long _trainingTime = Long.MAX_VALUE;

   // The genotypes from the GeneticAlgorithm. For each output variable,
   // we have _numberOfEstimates genomes, which is each an array list of doubles.
   private ArrayList< ArrayList< ArrayList < Double > > > _genomes;

   // These we need to hold on to so that we can invert back to the orginal domain
   private ArrayList< Interval > _inputIntervals;
   private ArrayList< Interval > _outputIntervals;

   private boolean _isMature;

   /**
    * The FunctionalNetwork is a collection of FunctionalCorrelation objects each of which maps
    * all the input vector instances to a single output instance. Moreover, each indepdenent
    * variable, or node, will be represented multiple times. Then, all the data is collected,
    * and the median is chosen as the final output value.
    */
   public FunctionalNetwork()
   {
      setNumberOfEstimates( 5 );
   }

   /**
    * The FunctionalNetwork is a collection of FunctionalCorrelation objects each of which maps
    * all the input vector instances to a single output instance. Moreover, each indepdenent
    * variable, or node, will be represented multiple times. Then, all the data is collected,
    * and the median is chosen as the final output value.
    * @param int The number of instances to estimate for each variable or node.
    */
   public FunctionalNetwork( int instances )
   {
      setNumberOfEstimates( instances );
   }

   /**
    * The FunctionalNetwork is a collection of FunctionalCorrelation objects each of which maps
    * all the input vector instances to a single output instance. Moreover, each indepdenent
    * variable, or node, will be represented multiple times. Then, all the data is collected,
    * and the median is chosen as the final output value.
    */
   public FunctionalNetwork( ArrayList< ArrayList< Double > > input,
                             ArrayList< ArrayList< Double > > output )
   {
      setInputVariables( input );
      setOutputVariables( output );
   }

   /**
    * The FunctionalNetwork is a collection of FunctionalCorrelation objects each of which maps
    * all the input vector instances to a single output instance. Moreover, each indepdenent
    * variable, or node, will be represented multiple times. Then, all the data is collected,
    * and the median is chosen as the final output value.
    */
   public FunctionalNetwork( int instances,
                             ArrayList< ArrayList< Double > > input,
                             ArrayList< ArrayList< Double > > output )
   {
      setNumberOfEstimates( instances );
      setInputVariables( input );
      setOutputVariables( output );
   }

   /**
    * We'll always map the inputs into [0, pi]
    */
   public void setInputVariables( ArrayList< ArrayList< Double > > input )
   {
      _inputIntervals = new ArrayList< Interval >( input.size() );
      _inputVariables = new ArrayList< ArrayList< Double > >( input.size() );
      Interval interval = null;

      for( ArrayList< Double > line : input )
      {
         interval = new Interval( 0, Math.PI );
         _inputVariables.add( interval.mapData( line ) );
         _inputIntervals.add( interval );
      }

   }

   /**
    * We'll always map the inputs into [0, 1]
    */
   public void setOutputVariables( ArrayList< ArrayList< Double > > output )
   {
      _functions = new ArrayList< ArrayList< FunctionalCorrelation > >( output.size() );
      _outputIntervals = new ArrayList< Interval >( output.size() );
      _outputVariables = new ArrayList< ArrayList< Double > >( output.size() );
      Interval interval = null;

      for( ArrayList< Double > line : output )
      {
         interval = new Interval( 0, 1 );
         _outputVariables.add( interval.mapData( line ) );
         _outputIntervals.add( interval );
      }
   }

   /**
    *
    */
   public ArrayList< ArrayList< Double > > getOutputVariables()
   {
      return _outputVariables;
   }

   /**
    *
    */
   public ArrayList< ArrayList< Double > > getInputVariables()
   {
      return _inputVariables;
   }

   /**
    * In this method, we also instantiate the genome container.
    */
   public void setNumberOfEstimates( int estimates )
   {
      _numberOfEstimates = estimates;
      _genomes = new ArrayList< ArrayList< ArrayList< Double > > >( _numberOfEstimates );
   }

   /**
    * What we'll do is set the training time, but it will only be somewhat accurate since
    * each FunctionalCorrelation genetic algorithm has it's own training time setting.
    */
   public void setTrainingTime( long time )
   {
      _trainingTime = time;
   }

   /**
    *
    */
   public void train()
   {
      long currentTime = System.currentTimeMillis();
      long endTime = currentTime + _trainingTime;
      Individual best = null;
      EnvironmentCache cache = null;
      // this will normalize the data input values into [0, pi]
//       cache.setDataStreams( _inputVariables );
      int j = 0; // just to update the screen with progress

      // First, iterate for each output variable
      for( ArrayList< Double > variable : _outputVariables )
      {
         System.out.println( "Processing output variable function: " + (++j) );
         // check to see if we need to quit first
         if( currentTime < endTime )
         {
            break;
         }

         ArrayList< ArrayList< Double > > genomes = new ArrayList< ArrayList< Double > >();
         ArrayList< FunctionalCorrelation > functions = new ArrayList< FunctionalCorrelation >();
//          cache.setTargetStream( variable );

         for( int i=0; i<_numberOfEstimates; i++ )
         {
            // in order for this call to work properly, the genetic.properties
            // in the genetic.jar needs to be configured properly
            best = (new GeneticAlgorithm()).evolve();
            genomes.add( best.getGenotype() );
//             FunctionalCorrelation function = FunctionalCorrelation.buildBinaryTree( best.getGenotype() );
//             functions.add( function );
         }

         _genomes.add( genomes );
         _functions.add( functions );

         // finally, update the timestamp
         currentTime = System.currentTimeMillis();
      }
      _isMature = true;
   }

   /**
    *
    */
   public ArrayList< Double > evaluate( ArrayList< Double > argument )
   {
      ArrayList< Double > result = new ArrayList< Double >( _functions.size() );

      for( ArrayList< FunctionalCorrelation > functionList : _functions )
      {
         ArrayList< Double > values = new ArrayList< Double >( _numberOfEstimates );
         for( FunctionalCorrelation function : functionList )
         {
//             function.setDataStreams( createDataStreams( argument ) );
            values.add( function.evaluate( 0 ) );
         }
         StatUtilities util = new StatUtilities();
         util.calculateStats( values );
         result.add( util.getMedian() );
      }

      return result;
   }

   /**
    *
    */
   private ArrayList< ArrayList< Double > > createDataStreams( ArrayList< Double > data )
   {
      ArrayList< ArrayList< Double > > streams = new ArrayList< ArrayList< Double > >( data.size() );

      for( double item : data )
      {
         ArrayList< Double > single = new ArrayList< Double >( 1 );
         single.add( item );
         streams.add( single );
      }

      return streams;
   }

   /**
    * This method will re-map the input vector into the scaled output vector data
    * that it is supposed to represent.
    */
   public ArrayList< Double > invert( ArrayList< Double > input )
   {
      ArrayList< Double > output = new ArrayList< Double >( input.size() );
      int i = 0;

      for( Double data : input )
      {
         Interval interval = _outputIntervals.get( i++ );
         output.add( interval.invert( data ) );
      }

      return output;
   }

   /**
    * The data as it's trained is not how we would normally represent it. 
    * In actuality, we think of this data as a sequence of vectors, so
    * we need to transpose this data slightly before we can run it through
    * the mill.
    */
   public void test()
   {
      if( _isMature )
      {
         ArrayList< Double > first = _outputVariables.get( 0 );
         for( int i=0; i<first.size(); i++ )
         {
            System.out.print( "\nreal output vector:, " );
            for( ArrayList< Double > output : _outputVariables )
            {
               System.out.print( output.get( i ) + "," );
            }
         }

         first = _inputVariables.get( 0 );
         for( int i=0; i<first.size(); i++ )
         {
            ArrayList< Double > inputVector = new ArrayList< Double >( _inputVariables.size() );
            ArrayList< Double > outputVector = null;

            System.out.print( "\na mapping:, " );
            for( ArrayList< Double > input : _inputVariables )
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

            // print out the original data values:
            outputVector = invert( outputVector );
            System.out.print( ",==>," );
            for( Double out : outputVector )
            {
               System.out.print( out + "," );
            }
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

      System.out.println( "Instantiating network..." );
      // number of estimates, inputs, and outputs
      FunctionalNetwork net = new FunctionalNetwork( 3, inputs, outputs );
      System.out.println( "About to train network..." );
      net.train();
      System.out.println( "About to test network..." );
      net.test();
   }

}
