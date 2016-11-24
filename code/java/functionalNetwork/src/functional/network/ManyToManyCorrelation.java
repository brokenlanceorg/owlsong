package functional.network;

import math.*;
import common.*;
import functional.strategy.*;

import java.util.*;
import java.util.regex.*;

/**
 * This class defines a many-to-many mapping correlation operation.
 * The correlation is done via many ManyToManyCorrelation objects with each
 * ensemble representing one output variable. Each ensemble operates on
 * all the independent variables but obviously outputs its singular
 * output value. Taken as a whole, this will perform a many-to-many
 * correlation operation.
 */
public class ManyToManyCorrelation extends Correlation
{

   // Each ensemble predicts one output result.
   private ArrayList< ManyToOneEnsemble > _ensembles;

   // The halting criteria applies to the ensemble as a whole, thus the training
   // criteria for each member is the overall halting criteria divided by the
   // ensemble size.
   private HaltingCriteria                _haltingCriteria;

   // The number of members in each ensemble which represents each output variable.
   private int                            _ensembleSize = 5;

   // The size of the genome upon creation can be changed.
   private int                            _genomeLength = 30;

   // This will be used to set the ensemble to multithreaded in createEnsemble
   private boolean                        _isMultiThreaded = false;

   // Setting this to some value:
   static final long                      serialVersionUID = -2050709623565937774L;

   /**
    * In this case, the correlate will most likey be determined via the training
    * strategies given the independent and dependent data values.
    */
   public ManyToManyCorrelation()
   {
      _haltingCriteria = new HaltingCriteria();
//       _haltingCriteria.setElapsedTimeTolerance( 28800000 ); // 8 hours
//       _haltingCriteria.setElapsedTimeTolerance( 18000000 ); // 5 hours
//       _haltingCriteria.setElapsedTimeTolerance( 7200000 ); // 2 hours
      _haltingCriteria.setElapsedTimeTolerance( 1800000 ); // 30 minutes
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
   public ManyToManyCorrelation( String name )
   {
      this();
      setName( name );
   }

   /**
    * In this case of the constructor, the name or identification of this 
    * particular correlation is given.
    */
   public ManyToManyCorrelation( HaltingCriteria criteria, String name )
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
   public ManyToManyCorrelation( HaltingCriteria criteria, String name, int size )
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
      for( ManyToOneEnsemble ens : _ensembles )
      {
         ens.printVariables();
      }
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
   public void setDependentData( double[][] data )
   {
      super.setDependentData( data );
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
    * @param double[][] -- The matrix of the variables which will serve as both
    * the independent data and the dependent data -- they will just be mapped
    * to be in a sequence, i.e., the previous value becomes the input to map to
    * the next value, and so on.
    * The key thing to note is that data in the matrix is stored such that the
    * rows represent the independent variables and the columns represent each
    * instance of that variable.
    * Since the previous is mapped to the next, there is no last row to learn,
    * so we must stop the inputs at length - 1, and since there is no first row
    * to learn, we must start the outputs at 1 instead of 0. This means that
    * the inputs will be the original data set minus the last row, and the outputs
    * will be the original data set minus the first row -- and since we're dealing
    * with java matrices, this translates to columns instead of rows.
    */
   public void setSequenceData( double[][] data )
   {
      double[][] inputs = new double[ data.length ][ data[ 0 ].length - 1 ];
      double[][] output = new double[ data.length ][ data[ 0 ].length - 1 ];
      int offset = 1;

      for( int i=0; i<data.length; i++ )
      {
         for( int j=0; j<data[ i ].length - 1; j++ )
         {
            output[ i ][ j ] = data[ i ][ j + offset ];
            inputs[ i ][ j ] = data[ i ][ j ];
         }
      }
      setIndependentData( inputs );
      setDependentData( output );
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void setData( double[][] data )
   {
      setIndependentData( data );
      setDependentData( data );
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
    * @param int -- The length of the genome for the solutions.
    */
   public void setGenomeLength( int length )
   {
      _genomeLength = length;
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
    * In this method since we're creating the ensemble from scratch, we
    * assume that we have the following necessary pieces of data:
    * 1) name identifier
    * 2) size of the ensemble
    * 3) halting criteria
    * 4) Training data
    */
   public void createEnsemble()
   {
      _ensembles = new ArrayList< ManyToOneEnsemble >( getDependentData().length );
      HaltingCriteria subCriteria = new HaltingCriteria();
      subCriteria.setElapsedTimeTolerance( _haltingCriteria.getElapsedTimeTolerance() / 
            (long) getDependentData().length );

      for( int i=0; i<getDependentData().length; i++ )
      {
         ManyToOneEnsemble manyToOne = new ManyToOneEnsemble( 
               subCriteria, ( "MTM-" + getName() + "-" + i ), _ensembleSize );
         manyToOne.setIndependentData( getIndependentData() );
         manyToOne.setPopulationSize( 3 );
         manyToOne.setMultiThreaded( _isMultiThreaded );
         double[][] data = getDependentData();
         manyToOne.setDependentData( data[ i ] );
         manyToOne.setGenomeLength( _genomeLength );
         manyToOne.createEnsemble();
         _ensembles.add( manyToOne );
      }
   }

   /**
    *
    */
   public void train()
   {
      HaltingCriteria subCriteria = new HaltingCriteria();
      subCriteria.setElapsedTimeTolerance( _haltingCriteria.getElapsedTimeTolerance() / 
            (long) getDependentData().length );
      ArrayList< Thread > threads = null;
      long start = System.currentTimeMillis();

      while( _haltingCriteria.getElapsedTimeTolerance() > ( System.currentTimeMillis() - start ) )
      {
// System.out.println( Thread.currentThread().getName() + " ensemble size: " + _ensembles.size() );
         threads = new ArrayList< Thread >( _ensembles.size() );
         for( ManyToOneEnsemble correlation : _ensembles )
         {
            correlation.setHaltingCriteria( subCriteria );
            ManyToManyCorrelation.Trainer trainer = new ManyToManyCorrelation.Trainer();
            trainer.setEnsemble( correlation );
            Thread t = new Thread( trainer );
            threads.add( t );
// System.out.println( Thread.currentThread().getName() + " about to start: " + t.getName() );
            t.start();
// System.out.println( Thread.currentThread().getName() + " started: " + t.getName() );
         }
      
// System.out.println( Thread.currentThread().getName() + " joining with " + threads.size() + " threads." );
         for( Thread t : threads )
         {
            try
            {
// System.out.println( Thread.currentThread().getName() + " will now join with a thread: " + t.getName() );
// System.out.println( t.getName() + " status is: " + t.getState() );
               t.join();
// System.out.println( Thread.currentThread().getName() + " joined with a thread: " + t.getName() );
            }
            catch( InterruptedException e )
            {
               System.err.println( "Received thread exception during join: " + e );
            }
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
      double[] output = new double[ getDependentData().length ];
      int i = 0;

      // for now, we will use the median, can use position 1 for mean value.
      for( ManyToOneEnsemble correlate : _ensembles )
      {
         output[ i++ ] = ( correlate.correlate( input ) )[ 0 ];
      }

      return output;
   }

   /**
    * This method only makes sense in the case of an I x O system in which both I and O
    * are equal to some N, this is because the call will recursively call correlate using
    * the output to be the next input. The beginning data point will be the data contained
    * in the file "input.dat" -- in this manner, the prediction can be started at an 
    * arbitrary point as opposed to the last independent or dependent data point.
    *
    * @param int -- The number of prediction recursions.
    * @return double[][] -- The prediction matrix. The rows represent each instance
    * of prediction while the columns represent the data for that prediciton.
    */
   public double[][] predict( int n, double[] input )
   {
      double[][] predictions = new double[ n ][];

      for( int i=0; i<n; i++ )
      {
         double output[] = correlate( input );
         predictions[ i ] = new double[ output.length ];
         for( int j=0; j<output.length; j++ )
         {
            predictions[ i ][ j ] = output[ j ];
         }
         input = output;
      }

      return predictions;
   }

   /**
    * This method only makes sense in the case of an I x O system in which both I and O
    * are equal to some N, this is because the call will recursively call correlate using
    * the output to be the next input. The beginning data point will be the data contained
    * in the file "input.dat" -- in this manner, the prediction can be started at an 
    * arbitrary point as opposed to the last independent or dependent data point.
    *
    * @param int -- The number of prediction recursions.
    * @return double[][] -- The prediction matrix. The rows represent each instance
    * of prediction while the columns represent the data for that prediciton.
    */
   public double[][] predict( int n )
   {
      double[][] predictions = new double[ n ][];

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

      for( int i=0; i<n; i++ )
      {
         double output[] = correlate( input );
         predictions[ i ] = new double[ output.length ];
         for( int j=0; j<output.length; j++ )
         {
            predictions[ i ][ j ] = output[ j ];
         }
         input = output;
      }

      return predictions;
   }

   /**
    *
    * @return ManyToManyCorrelationDAO -- The impl class of DAOBase type.
    */
   public DAOBase getDAO()
   {
      DAOBase dao = new ManyToManyCorrelationDAO();

      if( getName() != null )
      {
         dao.setFileName( getName() + ".ser" );
      }

      return dao;
   }

   /**
    *
    */
   public class ManyToManyCorrelationDAO extends DAOBase< ManyToManyCorrelation >
   {
   }

   /**
    *
    */
   public class Trainer implements Runnable 
   {

      ManyToOneEnsemble _ensemble;

      /**
       *
       */
      public void Trainer()
      {
      }

      /**
       *
       */
      public void Trainer( ManyToOneEnsemble ensemble )
      {
         _ensemble = ensemble;
      }

      /**
       *
       * @param TYPE
       * @return TYPE
       */
      public void setEnsemble( ManyToOneEnsemble ensemble )
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
    * The syntax will be one of the following:
    * command <create> <elapsed time> <ensemble size> <genome size>
    * 0       1        2              3               4
    * command <create|train> <elapsed time>
    * 0       1              2             
    * command <create|train|test|correlate|predict>
    * 0       1                           
    * command <predict> <data points>
    * 0       1         2
    */
   public static void main( String[] args )
   {
      if( args.length > 1 )
      {
         HaltingCriteria criteria = new HaltingCriteria();
         MathUtilities utils = new MathUtilities();
         long time = -1;
         int ensembleSize = -1;
         int genomeSize = -1;
         if( args.length >= 3 )
         {
            time = utils.getTime( args[ 2 ] );
            criteria.setElapsedTimeTolerance( time );
         }
         if( args.length >= 4 )
         {
            try
            {
               ensembleSize = Integer.parseInt( args[ 3 ] );
            }
            catch( NumberFormatException e )
            {
               System.err.println( "Unable to read ensemble size: " + e );
            }
         }
         if( args.length >= 5 )
         {
            try
            {
               genomeSize = Integer.parseInt( args[ 4 ] );
            }
            catch( NumberFormatException e )
            {
               System.err.println( "Unable to read genome size: " + e );
            }
         }
            
         if( "create".equals( args[ 1 ] ) )
         {
            System.out.println( "Will create a new correlation: " + args[ 0 ] );
            ManyToManyCorrelation correlation = new ManyToManyCorrelation( args[ 0 ] );
            correlation.setHaltingCriteria( criteria );
            if( ensembleSize != -1 )
            {
               correlation.setEnsembleSize( ensembleSize );
            }
            if( genomeSize != -1 )
            {
               correlation.setGenomeLength( genomeSize );
            }

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
            correlation.setData( streams );
//             correlation.setSequenceData( streams );
//             correlation.setMultiThreaded();
            correlation.createEnsemble();
            correlation.train();
            ManyToManyCorrelationDAO dao = (ManyToManyCorrelationDAO) correlation.getDAO();
            dao.serialize( correlation );
         }
         else
         {
            ManyToManyCorrelationDAO dao = (ManyToManyCorrelationDAO) (new ManyToManyCorrelation( args[ 0 ] )).getDAO();
            ManyToManyCorrelation correlation = dao.deserialize();
            correlation.setHaltingCriteria( criteria );
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
               double[] input = new double[ streams.length ];

               System.out.println( "median output:" );
               for( int i=0; i<streams[0].length; i++ )
               {
//                   System.out.println( "data point[ " + i + " ]:" );
                  for( int j=0; j<streams.length; j++ )
                  {
                     input[ j ] = streams[ j ][ i ];
//                      System.out.print( " " + input[j] );
                  }
                  double output[] = correlation.correlate( input );
//                   System.out.print( " => " );
                  for( int j=0; j<output.length; j++ )
                  {
                     System.out.print( output[ j ] + "," );
                  }
                  System.out.println( "" );
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
               for( int i=0; i<output.length; i++ )
               {
                  System.out.print( " " + output[i] );
               }
               System.out.println( "" );
            }
            else if( "predict".equals( args[ 1 ] ) )
            {
               int points = 1;
               if( args.length >= 3 )
               {
                  try
                  {
                     points = Integer.parseInt( args[ 2 ] );
                  }
                  catch( NumberFormatException e )
                  {
                     System.err.println( "Unable to read number of points: " + e );
                  }
               }
               System.out.println( "Will predict " + points + " data points" );
               double[][] predictions = correlation.predict( points );
               for( int i=0; i<points; i++ )
               {
                  for( int j=0; j<predictions[ i ].length; j++ )
                  {
                     System.out.print( predictions[ i ][ j ] + "," );
                  }
                  System.out.println( "" );
               }
            }
         }
      }
   }

}
