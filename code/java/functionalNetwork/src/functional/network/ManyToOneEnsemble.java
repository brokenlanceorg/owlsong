package functional.network;

import math.*;
import common.*;
import functional.strategy.*;

import java.util.*;

/**
 * This class defines the semantics of a *many* many-to-one correlation operation.
 * This class implements the abstract methods of the Correlation base class.
 * Since Correlation is a generic base class that also declares the syntax
 * for many-to-many mappings, many-to-one correlation classes, like this one,
 * must adapt its inputs and outputs for the many-to-many case.
 *
 * Classes such as HaltingCriteria, FunctionalCorrelation, and the strategy
 * classes are used to train and correlate the data mappings.
 *
 * Since this class is an ensemble of many correlations, there needs to be an
 * algorithm of some sort to pick out the correct correlation, or calculate
 * the correct output given the ensemble of predictions. Since it is the case
 * that each member of the ensemble is itself a prediction, and the average
 * response being random or psuedorandom worst case, the mean or median are
 * probably as good as any "ensemble collapsing" algorithm. 
 * However, should we blindly collapse all predictions or just the ones that 
 * are below a certain error threshold...
 * Consider how this is used: only at the higher levels will we be trying to
 * figure out which output to use, thus at that time, the "best" error rate
 * will come into play. Here, we are only interested in collapsing all of our
 * available output data points into one value.
 */
public class ManyToOneEnsemble extends Correlation
{

   // Each member of the ensemble, predicts the potential output result.
   private ArrayList< ManyToOneCorrelation > _ensemble;

   // The halting criteria applies to the ensemble as a whole, thus the training
   // criteria for each member is the overall halting criteria divided by the
   // ensemble size.
   private HaltingCriteria                   _haltingCriteria;

   // The number of members in the ensemble
   private int                               _ensembleSize = 50;

   // The size of the genome upon creation can be changed.
   private int                               _genomeLength = 200;

   // The number of threads for the GA to run with:
   private int                               _populationSize = 10;

   // This will be used to set the ensemble to multithreaded in createEnsemble
   private boolean                           _isMultiThreaded = false;

   // Setting this to some value:
   static final long                         serialVersionUID = -7413944476828361166L;

   /**
    * In this case, the correlate will most likey be determined via the training
    * strategies given the independent and dependent data values.
    */
   public ManyToOneEnsemble()
   {
      _haltingCriteria = new HaltingCriteria();
      _haltingCriteria.setElapsedTimeTolerance( 345600000 ); // 4 days
//       _haltingCriteria.setElapsedTimeTolerance( 86400000 ); // 24 hours / 1 day
//       _haltingCriteria.setElapsedTimeTolerance( 43200000 ); // 12 hours
//       _haltingCriteria.setElapsedTimeTolerance( 28800000 ); // 8 hours
//       _haltingCriteria.setElapsedTimeTolerance( 18000000 ); // 5 hours
//       _haltingCriteria.setElapsedTimeTolerance( 7200000 ); // 2 hours
//       _haltingCriteria.setElapsedTimeTolerance( 3600000 ); // 1 hour
//       _haltingCriteria.setElapsedTimeTolerance( 600000 ); // 10 minutes
//       _haltingCriteria.setElapsedTimeTolerance( 180000 ); // 3 minutes
//       _haltingCriteria.setElapsedTimeTolerance( 60000 ); // 1 minute
//       _haltingCriteria.setElapsedTimeTolerance( 30000 ); // 30 seconds
//       _haltingCriteria.setElapsedTimeTolerance( 10000 ); // 10 seconds
   }

   /**
    * In this case of the constructor, the name or identification of this 
    * particular correlation is given.
    */
   public ManyToOneEnsemble( String name )
   {
      this();
      setName( name );
   }

   /**
    * In this case of the constructor, the name or identification of this 
    * particular correlation is given.
    */
   public ManyToOneEnsemble( HaltingCriteria criteria, String name )
   {
      setName( name );
      _haltingCriteria = criteria;
   }

   /**
    * In this case of the constructor, we assume that the network hasn't been created
    * because the client is specifying the size of the ensemble which implies that
    * it needs to be created to that particular size; otherwise, the ensemble would
    * be deserialized and resurrected in that manner.
    */
   public ManyToOneEnsemble( HaltingCriteria criteria, String name, int size )
   {
      setName( name );
      _haltingCriteria = criteria;
      _ensembleSize = size;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void printVariables()
   {
      for( ManyToOneCorrelation corr : _ensemble )
      {
         System.out.println( "An element in the Ensemble (" + getName() + "): " );
         HashSet< Integer > vars = corr.getVariables();
         for( Integer var : vars )
         {
            System.out.println( "  a variable: " + var );
         }
      }
   }

   /**
    *
    * @param boolean
    */
   public void setMultiThreaded( boolean multi )
   {
      _isMultiThreaded = multi;
   }

   /**
    *
    * @param boolean
    */
   public void setMultiThreaded()
   {
      setMultiThreaded( true );
   }

   /**
    *
    * @param int -- The number of threads for the GA to run with.
    */
   public void setPopulationSize( int size )
   {
      _populationSize = size;
   }

   /**
    * This is not a deep copy, thus the real location of the data
    * should be kept immutable as long as possible.
    */
   public void setIndependentData( double[][] data )
   {
      super.setIndependentData( data );
   }

   /**
    * This is not a deep copy, thus the real location of the data
    * should be kept immutable as long as possible.
   */
   public void setDependentData( double[] data )
   {
      System.out.println( "In ManyToOneEnsemble::setDependentData" );
      double[][] dependentData = new double[ 1 ][];
      dependentData[ 0 ] = data;
      super.setDependentData( dependentData );
   }

   /**
    *
    * @param int -- The number of members in the correlation ensemble.
    */
   public void setEnsembleSize( int size )
   {
      _ensembleSize = size;
   }

   /**
    *
    * @return int -- The number of members in the correlation ensemble.
    */
   public int getEnsembleSize()
   {
      return _ensembleSize;
   }

   /**
    *
    * @param HaltingCriteria -- The criteria for which to halt the training algorithm.
    */
   public void setHaltingCriteria( HaltingCriteria criteria )
   {
      _haltingCriteria = criteria;
   }

   /**
    *
    * @param int -- The length of the genome for the solutions.
    */
   public void setGenomeLength( int length )
   {
      _genomeLength = length;
   }

   /**
    * In this method since we're creating the ensemble from scratch, we
    * assume that we have the following necessary pieces of data:
    * 1) name identifier
    * 2) size of the ensemble
    * 3) halting criteria
    * 4) Training data
    */
   public void createEnsemble()
   {
      _ensemble = new ArrayList< ManyToOneCorrelation >( _ensembleSize );
      HaltingCriteria subCriteria = new HaltingCriteria();
      if( _isMultiThreaded )
      {
         subCriteria.setElapsedTimeTolerance( _haltingCriteria.getElapsedTimeTolerance() );
      }
      else
      {
         subCriteria.setElapsedTimeTolerance( _haltingCriteria.getElapsedTimeTolerance() / _ensembleSize );
      }

      for( int i=0; i<_ensembleSize; i++ )
      {
         ManyToOneCorrelation manyToOne = new ManyToOneCorrelation( subCriteria, ( getName() + "-" + i ) );
         manyToOne.setIndependentData( getIndependentData() );
         // This needs to be set via the double[] overloaded method
         manyToOne.setDependentData( getDependentData() );
         manyToOne.setGenomeLength( _genomeLength );
         manyToOne.setPopulationSize( _populationSize );
         _ensemble.add( manyToOne );
      }
   }

   /**
    *
    */
   public void train()
   {
      HaltingCriteria subCriteria = new HaltingCriteria();
      if( _isMultiThreaded )
      {
         System.out.println( "Using Multithreading" );
         subCriteria.setElapsedTimeTolerance( _haltingCriteria.getElapsedTimeTolerance() );
         ArrayList< Thread > threads = new ArrayList< Thread >();
         for( ManyToOneCorrelation correlation : _ensemble )
         {
            correlation.setHaltingCriteria( subCriteria );
            ManyToOneEnsemble.Trainer trainer = new ManyToOneEnsemble.Trainer();
            trainer.setEnsemble( correlation );
            Thread t = new Thread( trainer );
            threads.add( t );
            t.start();
         }

         for( Thread t : threads )
         {
            try
            {
               t.join();
            }
            catch( InterruptedException e )
            {
               System.err.println( "Received thread exception during join: " + e );
            }
         }
      }
      else
      {
         System.out.println( "Not using Multithreading" );
         subCriteria.setElapsedTimeTolerance( _haltingCriteria.getElapsedTimeTolerance() / _ensembleSize );
         for( ManyToOneCorrelation correlation : _ensemble )
         {
            correlation.setHaltingCriteria( subCriteria );
            correlation.train();
         }
      }
   }

   /**
    * Since the correlate considers the independent data values as column vectors
    * we have to transpose the input data here. Thus, for example, the correlate
    * interprets the independent data as a matrix in which the rows are the 
    * independent variables and the columns are the individual instances of those
    * variables.
    *
    * @param double[] -- column vector of independent data on which to predict.
    * @return double[] -- column vector of prediction in which position zero has 
    * the result of the median operation on the ensemble outputs and position one
    * has the result of the mean operation on the ensemble outputs.
    */
   public double[] correlate( double[] input )
   {
      double[] output = new double[ 2 ];

      // collect all the ensemble output
      ArrayList< Double > ensemble = new ArrayList< Double >();

      for( ManyToOneCorrelation correlate : _ensemble )
      {
         ensemble.add( ( correlate.correlate( input ) )[ 0 ] );
      }

      // collapse the ensemble into a single value using the median:
      StatUtilities stat = new StatUtilities( ensemble );
      output[ 0 ] = stat.getMedian();
      output[ 1 ] = stat.getMean();

      return output;
   }

   /**
    *
    * @return ManyToOneEnsembleDAO -- The impl class of DAOBase type.
    */
   public DAOBase getDAO()
   {
      DAOBase dao = new ManyToOneEnsembleDAO();

      if( getName() != null )
      {
         dao.setFileName( getName() + ".ser" );
      }

      return dao;
   }

   /**
    *
    */
   public class ManyToOneEnsembleDAO extends DAOBase< ManyToOneEnsemble >
   {
   }

   /**
    *
    */
   public class Trainer implements Runnable 
   {
      ManyToOneCorrelation _ensemble;

      /**
       *
       */
      public void Trainer()
      {
      }

      /**
       *
       */
      public void Trainer( ManyToOneCorrelation ensemble )
      {
         _ensemble = ensemble;
      }

      /**
       *
       * @param TYPE
       * @return TYPE
       */
      public void setEnsemble( ManyToOneCorrelation ensemble )
      {
         _ensemble = ensemble;
      }

      /**
       *
       * @param TYPE
       * @return TYPE
       */
      public void run()
      {
         _ensemble.train();
      }

   }

   /**
    * For testing, we'll assume a few cases:
    * 1) We want to train a new data set of variables and targets and serialize under a given name.
    * 2) We want to further train an extant data set of a given name.
    * 3) We want to just print out the predictions of the current indepdenent vars of a given name.
    */
   public static void main( String[] args )
   {
      if( args.length == 1 )
      {
         System.out.println( "Will create a new correlation: " + args[ 0 ] );
         MathUtilities utils = new MathUtilities();
         ManyToOneEnsemble correlation = new ManyToOneEnsemble( args[ 0 ] );
         correlation.setMultiThreaded( true );

         // get the independent data set:
         common.FileReader reader = new FileReader( "variables.dat", "," );
         String[] words = reader.getArrayOfWords();
         int numberOfVariables = Integer.parseInt( words[ 0 ] );
         int numberOfDataPoints = (numberOfVariables == 1) ? (words.length - 1) : (words.length) / numberOfVariables;
         int count = 1;
         double[][] streams = new double[ numberOfVariables ][ numberOfDataPoints ];
         double[] temp = null;
         for( int i=0; i<numberOfVariables; i++ )
         {
            temp = new double[ numberOfDataPoints ];
            for( int j=0; j<numberOfDataPoints; j++ )
            {
               try
               {
                  temp[ j ] = Double.parseDouble( words[ count++ ] );
               }
               catch( NumberFormatException e )
               {
                  System.err.println( "Caught exception parsing data: " + e );
               }
            }
            streams[ i ] = utils.normalize( temp, 1 );
         }
         correlation.setIndependentData( streams );

         // now get the dependent target data set:
         reader = new FileReader( "target.dat", "," );
         words = reader.getArrayOfWords();
         double[] targetStream = new double[ words.length ];
         for( int i=0; i<words.length; i++ )
         {
            try
            {
               targetStream[ i ] = ( Double.parseDouble( words[ i ] ) );
            }
            catch( NumberFormatException e )
            {
               System.err.println( "Caught exception parsing data: " + e );
            }
         }
         targetStream = utils.normalize( targetStream, 1 );
         
         System.out.println( "about to set the dependent data..." );
         correlation.setDependentData( targetStream );
         System.out.println( "about to call createEnsemble..." );
         correlation.createEnsemble();
         correlation.train();
         ManyToOneEnsembleDAO dao = (ManyToOneEnsembleDAO) correlation.getDAO();
         dao.serialize( correlation );
      }
      else
      {
         ManyToOneEnsembleDAO dao = (ManyToOneEnsembleDAO) (new ManyToOneEnsemble( args[ 0 ] )).getDAO();
         ManyToOneEnsemble correlation = dao.deserialize();
         if( "train".equals( args[ 1 ] ) )
         {
            System.out.println( "Will train existing correlation: " + args[ 0 ] );
            correlation.train();
            dao.serialize( correlation );
         }
         else if( "test".equals( args[ 1 ] ) )
         {
            System.out.println( "Will test existing correlation: " + args[ 0 ] );
            double[][] streams = correlation.getIndependentData();
            double[]   input   = new double[ streams.length ];

            System.out.println( "median output:" );
            for( int i=0; i<streams[0].length; i++ )
            {
               for( int j=0; j<streams.length; j++ )
               {
                  input[ j ] = streams[ j ][ i ];
               }
               double output[] = correlation.correlate( input );
               System.out.println( output[0] );
            }

            System.out.println( "mean output:" );
            for( int i=0; i<streams[0].length; i++ )
            {
               for( int j=0; j<streams.length; j++ )
               {
                  input[ j ] = streams[ j ][ i ];
               }
               double output[] = correlation.correlate( input );
               System.out.println( output[1] );
            }
         }
         else if( "correlate".equals( args[ 1 ] ) )
         {
            // now get the dependent target data set:
            common.FileReader reader = new FileReader( "input.dat", "," );
            String[] words = reader.getArrayOfWords();
            double[] input = new double[ words.length ];
            for( int i=0; i<words.length; i++ )
            {
               try
               {
                  input[ i ] = ( Double.parseDouble( words[ i ] ) );
               }
               catch( NumberFormatException e )
               {
                  System.err.println( "Caught exception parsing data: " + e );
               }
            }
            double output[] = correlation.correlate( input );
            System.out.println( "median output:" );
            System.out.println( output[0] );
            System.out.println( "mean output:" );
            System.out.println( output[1] );
         }
      }
   }

}
