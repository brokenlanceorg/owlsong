package functional.classifier;

import math.*;
import common.*;
import genetic.*;
import genetic.impl.*;

import java.util.*;

/**
 * 
 */
public class SurjectiveFunctionalBinaryClassifier implements Persistable
{

   private FunctionalCorrelation                _correlate;
   private SurjectiveBinaryClassifierIndividual _bestIndividual;
   private HaltingCriteria                      _haltingCriteria;
   private ArrayList< Double >                  _genotype;
   private int                                  _genomeLength    = 31;
   private int                                  _threadCount     = 10;
   private int                                  _length          = 10;
   private double                               _theta           = 10.0;
   private String                               _name;
   private double[][]                           _targets;
   private double[][]                           _variable;


   /**
    * In this case, the correlate will most likey be determined via the training
    * strategies given the independent and dependent data values.
    */
   public SurjectiveFunctionalBinaryClassifier()
   {
      _haltingCriteria = new HaltingCriteria();
//       _haltingCriteria.setElapsedTimeTolerance( 18000000 ); // 5 hours
//       _haltingCriteria.setElapsedTimeTolerance( 7200000 ); // 2 hours
      _haltingCriteria.setElapsedTimeTolerance( 3600000 ); // 1 hour
//       _haltingCriteria.setElapsedTimeTolerance( 1800000 ); // 30 minutes
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
   public SurjectiveFunctionalBinaryClassifier( HaltingCriteria criteria )
   {
      _haltingCriteria = criteria;
   }

   /**
    * In this case of the constructor, the name or identification of this 
    * particular correlation is given.
    */
   public SurjectiveFunctionalBinaryClassifier( HaltingCriteria criteria, String name )
   {
      setName( name );
      _haltingCriteria = criteria;
   }

   /**
    * In this case of the constructor, the name or identification of this 
    * particular correlation is given.
    */
   public SurjectiveFunctionalBinaryClassifier( HaltingCriteria criteria, String name, int l )
   {
      setName( name );
      setGenomeLength( l );
      _haltingCriteria = criteria;
   }

   /**
    * In this case of the constructor, the name or identification of this 
    * particular correlation is given.
    */
   public SurjectiveFunctionalBinaryClassifier( String name )
   {
      this();
      setName( name );
   }

   /**
    * In this case of the constructor, the correlate can not be instantiated since
    * the independent data streams are not yet available.
    */
   public SurjectiveFunctionalBinaryClassifier( ArrayList< Double > genotype )
   {
      this();
      _genotype = genotype;
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
   public void setThreadCount( int count )
   {
      _threadCount = count;
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
      GeneticAlgorithm ga = null;

      if( _correlate != null )
      {
         ga = new GeneticAlgorithm( _haltingCriteria,
                                    "genetic.impl.SurjectiveBinaryClassifierIndividual",
                                    _bestIndividual.getEnvironmentCache(),
                                    _genomeLength,
                                    _threadCount );
         ga.setBestIndividual( _bestIndividual );
      }
      else
      {
         EnvironmentCache ec = new EnvironmentCache();
         ec.setDataStreams( _targets );
         ec.setGenomeLength( _genomeLength );
         ga = new GeneticAlgorithm( _haltingCriteria,
                                    "genetic.impl.SurjectiveBinaryClassifierIndividual",
                                    ec,    
                                    _genomeLength,
                                    _threadCount );
      }

      _bestIndividual = (SurjectiveBinaryClassifierIndividual) ga.evolve();
      _genotype       = _bestIndividual.getGenotype();
//       System.out.println( "Best Individual: " + _bestIndividual.toStringFinal() );
      instantiateCorrelate( _targets[ 0 ].length );
   }

   /**
    * Assumption is that train has been called already.
    * @param double[] -- the vector which needs to be classified.
    * @return boolean -- returns true if this is classified positively.
    */
   public boolean classify( double[] vector )
   {
      double  local = 0.0;
      double  value = 0.0;
      boolean c     = false;

      if( _correlate == null )
      {
         instantiateCorrelate( vector.length );
      }
      _correlate.setDataStreams( _variable );

      for( int j=0; j<_length; j++ )
      {
         _variable[ 0 ][ j ] = vector[ j ];
      }

      for( int j=0; j<_length; j++ )
      {
         value = _correlate.evaluate( j );

         if(    Double.isNaN( value ) 
             || Double.isInfinite( value ) )
         {
            value = Double.MAX_VALUE;
         }
         local += Math.abs( value - vector[ j ] );
      }

      if( local <= _theta )
      {
         c = true;
      }

      return c;
   }

   /**
    *
    */
   private void instantiateCorrelate( int length )
   {
      _length                     = length;
      if( _targets == null || _targets[ 0 ] == null )
      {
         System.out.println( "_targets is null!!!!!!" );
      }
      if( _genotype == null )
      {
         System.out.println( "_genotype is null!!!!!!" );
      }
      _theta                      = (double) ( (double) _targets[ 0 ].length * _genotype.get( 0 ) );
      ArrayList< Double > genome  = new ArrayList< Double >( _genotype.size() - 1 );

      for( int i=1; i<_genotype.size(); i++ )
      {
         genome.add( _genotype.get( i ) );
      }

      _variable  = new double[ 1 ][ _length ];
      _correlate = FunctionalCorrelation.buildBinaryTree( genome, _variable );
   }

   /**
    *
    * @return SurjectiveFunctionalBinaryClassifierDAO -- The impl class of DAOBase type.
    */
   public DAOBase getDAO()
   {
      DAOBase dao = new SurjectiveFunctionalBinaryClassifierDAO();

      if( getName() != null )
      {
         dao.setFileName( getName() + ".ser" );
      }

      return dao;
   }

   /**
    *
    */
   public class SurjectiveFunctionalBinaryClassifierDAO extends DAOBase< SurjectiveFunctionalBinaryClassifier >
   {
   }

   /**
    *
    */
   private static double[][] getVariablesFromFile()
   {
      // get the independent data set:
      common.FileReader reader             = new FileReader( "variables.dat", "," );
      String[]          words              = reader.getArrayOfWords();
      int               numberOfVariables  = Integer.parseInt( words[ 0 ] );
      int               numberOfDataPoints = (numberOfVariables == 1) ? (words.length - 1) : (words.length) / numberOfVariables;
      int               count              = 1;
      double[][]        streams            = new double[ numberOfVariables ][ numberOfDataPoints ];
      double[]          temp               = null;

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
    *
    * @param String
    */
   public void setName( String name )
   {
      _name = name;
   }

   /**
    *
    * @return String
    */
   public String getName()
   {
      return _name;
   }

   /**
    *
    * @param double[][]
    */
   public void setTargets( double[][] targets )
   {
      _targets = new double[ targets.length  ][ targets[ 0 ].length ];

      for( int i=0; i<targets.length; i++ )
      {
         for( int j=0; j<targets[ 0 ].length; j++ )
         {
            _targets[ i ][ j ] = targets[ i ][ j ];
         }
      }
   }

   /**
    *
    * @return double[][]
    */
   public double[][] getTargets()
   {
      return _targets;
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
            System.out.println( "Will create a new SurjectiveFunctionalBinaryClassifier: " + args[ 1 ] );
            MathUtilities              utils       = new MathUtilities();
            HaltingCriteria            criteria    = new HaltingCriteria();
            SurjectiveFunctionalBinaryClassifier correlation = new SurjectiveFunctionalBinaryClassifier( args[ 1 ] );
            criteria.setElapsedTimeTolerance( utils.getTime( args[ 2 ] ) );
            correlation.setHaltingCriteria( criteria );
            correlation.setTargets( getVariablesFromFile() );
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
            System.out.println( "Will train an existing SurjectiveFunctionalBinaryClassifier: " + args[ 1 ] );
            SurjectiveFunctionalBinaryClassifierDAO dao = 
               (SurjectiveFunctionalBinaryClassifierDAO) (new SurjectiveFunctionalBinaryClassifier( args[ 1 ] )).getDAO();
            SurjectiveFunctionalBinaryClassifier correlation = dao.deserialize();
            correlation.setTargets( getVariablesFromFile() );
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
            System.out.println( "Will test an existing SurjectiveFunctionalBinaryClassifier: " + args[ 1 ] );
            double[]   input = getTargetFromFile();
            SurjectiveFunctionalBinaryClassifierDAO dao = 
               (SurjectiveFunctionalBinaryClassifierDAO) (new SurjectiveFunctionalBinaryClassifier( args[ 1 ] )).getDAO();
            SurjectiveFunctionalBinaryClassifier correlation = dao.deserialize();
            System.out.println( "Classify on target.dat vector: " + correlation.classify( input ) );
         }
      }
      else
      {
         System.out.println( "" );
         System.out.println( "SurjectiveFunctionalBinaryClassifier Usage:" );
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
