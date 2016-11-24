package functional.network;

import math.*;
import common.*;
import functional.strategy.*;

import java.util.*;

/**
 * This class defines the semantics of a many-to-one correlation operation.
 * This class implements the abstract methods of the Correlation base class.
 * Since Correlation is a generic base class that also declares the syntax
 * for many-to-many mappings, many-to-one correlation classes, like this one,
 * must adapt its inputs and outputs for the many-to-many case.
 *
 * Classes such as HaltingCriteria, FunctionalCorrelation, and the strategy
 * classes are used to train and correlate the data mappings.
 *
 * Since the correlate (FunctionalCorrelation) will be determined and defined
 * via the genetic algorithm, the genotype must be stored so that it can be
 * re-instantiated after being persisted.
 */
public class ManyToOneCorrelation extends Correlation
{

   private FunctionalCorrelation _correlate;
   private HaltingCriteria       _haltingCriteria;
   private ArrayList< Double >   _genotype;
   private Interval              _targetInterval;
   private Interval[]            _variableIntervals;
   private int                   _genomeLength    = 31;
   private int                   _populationSize  = 100;
   static  final long            serialVersionUID = -8164797492740797738L;


   /**
    * In this case, the correlate will most likey be determined via the training
    * strategies given the independent and dependent data values.
    */
   public ManyToOneCorrelation()
   {
      _haltingCriteria = new HaltingCriteria();
//       _haltingCriteria.setElapsedTimeTolerance( 18000000 ); // 5 hours
//       _haltingCriteria.setElapsedTimeTolerance( 7200000 ); // 2 hours
//       _haltingCriteria.setElapsedTimeTolerance( 600000 ); // 10 minutes
//       _haltingCriteria.setElapsedTimeTolerance( 180000 ); // 3 minutes
//       _haltingCriteria.setElapsedTimeTolerance( 60000 ); // 1 minute
//       _haltingCriteria.setElapsedTimeTolerance( 30000 ); // 30 seconds
      _haltingCriteria.setElapsedTimeTolerance( 10000 ); // 10 seconds
   }

   /**
    * In this case of the constructor, the name or identification of this 
    * particular correlation is given.
    */
   public ManyToOneCorrelation( HaltingCriteria criteria )
   {
      _haltingCriteria = criteria;
   }

   /**
    * In this case of the constructor, the name or identification of this 
    * particular correlation is given.
    */
   public ManyToOneCorrelation( HaltingCriteria criteria, String name )
   {
      setName( name );
      _haltingCriteria = criteria;
   }

   /**
    * In this case of the constructor, the name or identification of this 
    * particular correlation is given.
    */
   public ManyToOneCorrelation( String name )
   {
      this();
      setName( name );
   }

   /**
    * In this case of the constructor, the correlate can not be instantiated since
    * the independent data streams are not yet available.
    */
   public ManyToOneCorrelation( ArrayList< Double > genotype )
   {
      this();
      _genotype = genotype;
   }

   /**
    * In this case of the constructor, the correlate can be instantiated since the
    * independent data is available.
    */
   public ManyToOneCorrelation( ArrayList< Double > genotype, 
                                double[][] independentData, 
                                double[] dependentData )
   {
      this();
      _genotype = genotype;
      setIndependentData( independentData );
      setDependentData( dependentData );
      _correlate = FunctionalCorrelation.buildBinaryTree( _genotype, independentData );
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void setInterval( Interval interval )
   {
      _targetInterval = interval;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public HashSet< Integer > getVariables()
   {
      return _correlate.getVariables();
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
      _variableIntervals = new Interval[ data.length ];
      double[][] mapped = new double[ data.length ][];
      for( int i=0; i<data.length; i++ )
      {
         _variableIntervals[ i ] = new Interval();
         mapped[ i ] = _variableIntervals[ i ].mapData( data[ i ] );
      }
      super.setIndependentData( mapped );
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public double[][] getIndependentDataInverted()
   {
      double[][] data     = getIndependentData();
      double[][] inverted = new double[ data.length ][ data[ 0 ].length ];

      for( int i=0; i<data.length; i++ )
      {
         for( int j=0; j<data[ i ].length; j++ )
         {
            inverted[ i ][ j ] = _variableIntervals[ i ].invert( data[ i ][ j ] );
         }
      }

      return inverted;
   }

   /**
    * This is not a deep copy, thus the real location of the data
    * should be kept immutable as long as possible.
    */
   public void setDependentData( double[] data )
   {
      double[][] dependentData = new double[ 1 ][];
      _targetInterval = new Interval();
      dependentData[ 0 ] = _targetInterval.mapData( data );
      super.setDependentData( dependentData );
   }

   /**
    * This is not a deep copy, thus the real location of the data
    * should be kept immutable as long as possible.
    * @Override -- so that the _targetInterval is created properly
    */
   public void setDependentData( double[][] data )
   {
      double[][] dependentData = new double[ 1 ][];
      _targetInterval = new Interval();
      dependentData[ 0 ] = _targetInterval.mapData( data[ 0 ] );
      super.setDependentData( dependentData );
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public double[] getDependentDataInverted()
   {
      double[] data     = ( getDependentData() )[ 0 ];
      double[] inverted = new double[ data.length ];

      for( int i=0; i<data.length; i++ )
      {
         inverted[ i ] = _targetInterval.invert( data[ i ] );
      }

      return inverted;
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
    * @param HaltingCriteria -- The criteria for the training.
    */
   public void setHaltingCriteria( HaltingCriteria criteria )
   {
      _haltingCriteria = criteria;
   }

   /**
    *
    */
   public void train()
   {
      PhaseCorrelationStrategy strategy = new PhaseCorrelationStrategy( _haltingCriteria );
      strategy.setPopulationSize( _populationSize );
      strategy.setTrainingInputData( getIndependentData() );
      strategy.setTrainingTargetData( (getDependentData())[0] );
      if( _genotype != null )
      {
         strategy.setGenotype( _genotype );
      }
      strategy.setGenomeLength( _genomeLength );
      strategy.train();
      _correlate = strategy.getCorrelate();
      _genotype  = strategy.getGenotype();
   }

   /**
    * Since the correlate considers the independent data values as column vectors
    * we have to transpose the input data here. Thus, for example, the correlate
    * interprets the independent data as a matrix in which the rows are the 
    * independent variables and the columns are the individual instances of those
    * variables.
    *
    * @param double[] column vector of independent data on which to predict.
    * @return double[] column vector of prediction in which only position zero has a data result.
    */
   public double[] correlate( double[] input )
   {
      double[]   output  = new double[ 1 ];
      double[][] streams = new double[ input.length ][ 1 ];

      // transpose data for the correlate
      for( int i=0; i<input.length; i++ )
      {
         streams[ i ][ 0 ] = input[ i ];
      }
      _correlate.setDataStreams( streams );
      output[ 0 ] = _targetInterval.invert( _correlate.evaluate( 0 ) );

      return output;
   }

   /**
    *
    * @return ManyToOneCorrelationDAO -- The impl class of DAOBase type.
    */
   public DAOBase getDAO()
   {
      DAOBase dao = new ManyToOneCorrelationDAO();

      if( getName() != null )
      {
         dao.setFileName( getName() + ".ser" );
      }

      return dao;
   }

   /**
    *
    */
   public class ManyToOneCorrelationDAO extends DAOBase< ManyToOneCorrelation >
   {
   }

   /**
    *
    */
   private static double[][] getVariablesFromFile()
   {
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
         streams[ i ] = (new MathUtilities()).normalize( temp, 1 );
      }

      return streams;
   }

   /**
    *
    */
   private static double[] getTargetFromFile()
   {
      // now get the dependent target data set:
      common.FileReader reader = new FileReader( "target.dat", "," );
      String[] words = reader.getArrayOfWords();
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

      return targetStream;
   }

   /**
    * For testing.
    * The general syntax is the following:
    * create <system name>  <training time> [<genome length>]
    * train  <system name> [<training time>]
    * test   <system name>
    */
   public static void main( String[] args )
   {
      if( args.length > 1 )
      {
         // syntax for create:
         // create <name> <training time> [<genome length>]
         if( "create".equals( args[ 0 ] ) )
         {
            System.out.println( "Will create a new ManyToOneCorrelation: " + args[ 1 ] );
            MathUtilities utils = new MathUtilities();
            HaltingCriteria criteria = new HaltingCriteria();
            criteria.setElapsedTimeTolerance( utils.getTime( args[ 2 ] ) );
            ManyToOneCorrelation correlation = new ManyToOneCorrelation( args[ 1 ] );
            correlation.setHaltingCriteria( criteria );
            correlation.setIndependentData( getVariablesFromFile() );
            correlation.setDependentData( getTargetFromFile() );
            if( args.length > 3 )
            {
               correlation.setGenomeLength( Integer.parseInt( args[ 3 ] ) );
            }
            correlation.train();
            correlation.getDAO().serialize( correlation );
         }
         // syntax for train:
         // train <name> [<training time>]
         else if( "train".equals( args[ 0 ] ) )
         {
            System.out.println( "Will train an existing ManyToOneCorrelation: " + args[ 1 ] );
            ManyToOneCorrelationDAO dao = 
               (ManyToOneCorrelationDAO) (new ManyToOneCorrelation( args[ 1 ] )).getDAO();
            ManyToOneCorrelation correlation = dao.deserialize();
            // adding this to test changing data.
            correlation.setIndependentData( getVariablesFromFile() );
            correlation.setDependentData( getTargetFromFile() );
            if( args.length > 2 )
            {
               HaltingCriteria criteria = new HaltingCriteria();
               criteria.setElapsedTimeTolerance( (new MathUtilities()).getTime( args[ 2 ] ) );
               correlation.setHaltingCriteria( criteria );
            }
            System.out.println( "Will now train again..." );
            correlation.train();
            dao.serialize( correlation );
         }
         // syntax for test is:
         // test <name>
         else if( "test".equals( args[ 0 ] ) )
         {
            System.out.println( "Will test an existing ManyToOneCorrelation: " + args[ 1 ] );
            double[][]              variables   = getVariablesFromFile();
            double[]                input       = new double[ variables.length ];
            ManyToOneCorrelationDAO dao         = (ManyToOneCorrelationDAO) (new ManyToOneCorrelation( args[ 1 ] )).getDAO();
            ManyToOneCorrelation    correlation = dao.deserialize();

            for( int i=0; i<variables[ 0 ].length; i++ )
            {
               for( int j=0; j<variables.length; j++ )
               {
                  input[ j ] = variables[ j ][ i ];
               }
               System.out.println( ( correlation.correlate( input ) )[0] );
            }
         }
      }
      else
      {
         System.out.println( "" );
         System.out.println( "ManyToOneCorrelation Usage:" );
         System.out.println( "create <system name>  <training time> [<genome length>]" );
         System.out.println( "train  <system name> [<training time>]" );
         System.out.println( "test   <system name>" );
         System.out.println( "" );
         System.out.println( "Example:" );
         System.out.println( "java -jar ~/dist/functionalNetwork/functionalNetwork.jar create system1 1h5m 31" );
         System.out.println( "java -jar ~/dist/functionalNetwork/functionalNetwork.jar train system1 1h5m" );
      }
   }

}
