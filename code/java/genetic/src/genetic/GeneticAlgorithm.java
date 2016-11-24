package genetic;

import java.util.*;
import common.*;

/**
 * The main driver class for PRIFGA -- Parallel Random Immigrant Fractal Genetic Algorithm.
 * The basic idea is that there will be "waves" of immigrants whose entry points into
 * the population will be of a random nature. Further, the immigrants will move in the
 * genotype space in a fractal random pattern not unlike the canine search algorithm.
 * Then once the movements become small, the canonical GA kicks in. The entire system
 * will be parallel to take advantage of multi-core CPUs.
 * The random immigrants will ensure diversity while being coupled with the canine search
 * algorithm will allow efficient search of the genspace (genotype space).
 *
 * 1 Choose initial population
 * 2 While termination condition has not been met:
 *    3 Breed new offspring
 *    4 Evaluate offspring fitness and update environment
 *    5 Replace oldest population with new breed
 * 
 * 
 * 1 The genotype at each position will reside in [0 ,1]; 
 * it is up to the implementation to interpret and scale the genes. 
 * The initialization of a new set of individuals should be able to be 
 * called at a later time (before the RIF phase in III).
 * 
 * 2 The termination condition will most likely be number of generations.
 * 
 * 3 Breeding place new offspring on breeding queues and spawn threads.
 *    For each new offspring
 *       If in RIF phase
 *          Choose genotype via random immigrant fractal algorithm
 *       else
 *          For each gene position x
 *             Choose most fit parent via roulette wheel and get position x gene
 *             Set gene x
 *             Mutate gene x via mutation probability
 * 
 * 4 Place offspring on evaluation queue and spawn evaluation threads.
 * 
 * 5 Based on generation lifespan, terminate old individuals and add new offspring.
 *
 * We'll leave the rif_wave_rate, but it appears that with the logistics of crossover,
 * a (2 * gensize * lifesize) will be a more optimal rate.
 */
public class GeneticAlgorithm
{
   // This is the general halting criteria
   private HaltingCriteria _haltingCriteria = new HaltingCriteria();

   // This is the fitness criterion
   private double _terminationFitness = 1e300;

   // This is the probability of doing a mutation
   private double _mutationRate = 0.5;

   // This is the number of offspring born every generation
   private int _generationSize = 100;

   // This is the number of generations to run before quitting
   private int _numberOfEpochs = 1000000000;

   // This is the number of CPUs that are available for processing default has been 10
   private int _numberOfThreads = 10;

   // This is the length of time to run
   private long _executionTime = 180000; // default 3 mins

   // This is the number of generations an individual will live
   private int _lifespanExpectancy = 1;

   // This is the number of epochs when to kick in RIF
   private int _RIFWaveRate;

   // Keeps track of the RIF phase
   private boolean _isRIFPhase;

   // Keeps track of any state data that other threads require
   private ArrayList<Double> _data = new ArrayList<Double>();

   // This handles the distribution of the new offspring
   private Queue<Individual> _breedingQueue = new LinkedList<Individual>();

   // This handles the distribution of the new offspring
   private ArrayList<Individual> _incubationQueue = new ArrayList<Individual>();

   // This handles the distribution of the new offspring
   private ArrayList<Individual> _evaluatedQueue = new ArrayList<Individual>();

   // This handles the distribution of the new offspring
   private Queue<Individual> _evaluationQueue = new LinkedList<Individual>();

   // This keeps track of all the breeder threads
   private ArrayList<Thread> _breederThreads = new ArrayList<Thread>();

   // This keeps track of all the evaluator threads
   private ArrayList<Thread> _evaluatorThreads = new ArrayList<Thread>();

   // This holds all the eligible parents for the current generation
   private ArrayList<Individual> _eligibleParents = new ArrayList<Individual>();

   // The current best-fit individual
   private Individual _bestIndividual;

   // The class that implements the Individual interface
   private String _implementationClass;

   // Every individual can have access to the EnvironmentCache
   private EnvironmentCache  _environmentCache;
   private SelectionStrategy _selectionStrategy = new BiformSelectionStrategy();
   private MutationStrategy  _mutationStrategy  = new RIFMutationStrategy();
   private Double            _populationFitness = 0.0;

   // This applies to only the new species
   private int _genomeLength = 17050;

   /**
    * Default Constructor.
    */
   public GeneticAlgorithm()
   {
      loadConfigurationProperties();
      initialize();
   }

   /**
    * Constructor.
    */
   public GeneticAlgorithm( long executionTime, int generationSize, int epochs, double fitness )
   {
      setExecutionTime( executionTime );
      setGenerationSize( generationSize );
      setNumberOfEpochs( epochs );
      setTerminationFitness( fitness );
      initialize();
   }

   /**
    * Constructor.
    */
   public GeneticAlgorithm( long executionTime, int epochs, String name )
   {
      setExecutionTime( executionTime );
      setNumberOfEpochs( epochs );
      setImplementationClass( name );
      initialize();
   }

   /**
    * Constructor.
    */
   public GeneticAlgorithm( long executionTime, String name )
   {
      setExecutionTime( executionTime );
      setImplementationClass( name );
      initialize();
   }

   /**
    * Constructor.
    */
   public GeneticAlgorithm( HaltingCriteria criteria, String name )
   {
      _haltingCriteria = criteria;
      setImplementationClass( name );
      initialize();
   }

   /**
    * Constructor.
    */
   public GeneticAlgorithm( HaltingCriteria criteria, String name, int length )
   {
      setImplementationClass( name );
      _haltingCriteria = criteria;
      _genomeLength    = length;
      initialize();
   }

   /**
    * Constructor.
    */
   public GeneticAlgorithm( HaltingCriteria criteria, String name, EnvironmentCache cache )
   {
      _haltingCriteria = criteria;
      setImplementationClass( name );
      setEnvironmentCache( cache );
      initialize();
   }

   /**
    * Constructor.
    */
   public GeneticAlgorithm( HaltingCriteria criteria, String name, EnvironmentCache cache, int length )
   {
      _genomeLength    = length;
      _haltingCriteria = criteria;
      setImplementationClass( name );
      setEnvironmentCache( cache );
      initialize();
   }

   /**
    * Constructor.
    */
   public GeneticAlgorithm( HaltingCriteria criteria, String name, EnvironmentCache cache, int length, int size )
   {
      _genomeLength    = length;
      _haltingCriteria = criteria;
      _generationSize  = size;
      setImplementationClass( name );
      setEnvironmentCache( cache );
      initialize();
   }

   /**
    *
    */
   private void initialize()
   {
      if( getEnvironmentCache() == null )
      {
         setEnvironmentCache( new EnvironmentCache() );
      }

      try
      {
         Class implClass = Class.forName( _implementationClass );
         _bestIndividual = (Individual)implClass.newInstance();
         _bestIndividual.setEnvironmentCache( getEnvironmentCache() );
         _bestIndividual.randomizeGenome();
         _bestIndividual.evaluateFitness();
      }
      catch( ClassNotFoundException e )
      {
         System.err.println( "Unable to find class: " + _implementationClass + "\nException: " + e );
         System.exit( 1 );
      }
      catch( InstantiationException e )
      {
         System.err.println( "Unable to instantiate class: " + _implementationClass + "\nException: " + e );
         System.exit( 1 );
      }
      catch( IllegalAccessException e )
      {
         System.err.println( "Unable to instantiate class: " + _implementationClass + "\nException: " + e );
         System.exit( 1 );
      }

      _eligibleParents.addAll( createRandomImmigrants( true ) );
   }

   /**
    *
    */
   private void setTerminationFitness( double fitness )
   {
      _terminationFitness = fitness;
      _haltingCriteria.setFitnessTolerance( _terminationFitness );
   }

   /**
    *
    */
   private void setMutationRate( double rate )
   {
      _mutationRate = rate;
   }

   /**
    *
    */
   private void setGenerationSize( int size )
   {
      _generationSize = size;
   }

   /**
    *
    */
   private void setNumberOfEpochs( int epochs )
   {
      _numberOfEpochs = epochs;
      _haltingCriteria.setEpochalTolerance( _numberOfEpochs );
   }

   /**
    *
    */
   private void setNumberOfThreads( int threads )
   {
      _numberOfThreads = threads;
   }

   /**
    *
    */
   private void setExecutionTime( long time )
   {
      _executionTime = time;
      _haltingCriteria.setElapsedTimeTolerance( _executionTime );
   }

   /**
    *
    */
   private void setLifespanExpectancy( int life )
   {
      _lifespanExpectancy = life;
   }

   /**
    *
    */
   private void setRIFWaveRate( int rate )
   {
      _RIFWaveRate = rate;
   }

   /**
    *
    */
   private void setImplementationClass( String name )
   {
      _implementationClass = name;
   }

   /**
    *
    * @param Individual -- the individual that can be further refined if the GA has already been run.
    */
   public void setBestIndividual( Individual best )
   {
      _bestIndividual = best;
   }

   /**
    * Loads up all the configuration items from the properties file.
    * Currently, the only values that matter are:
    * 1) _terminationFitness = HaltingCriteria
    * 2) _numberOfEpochs     = HaltingCriteria
    * 3) _executionTime      = HaltingCriteria
    * 4) _generationSize     = number of threads
    * 5) _implementationClass
    * 6) * _genomeLength     = optional as it is set by the impl class
    */
   private void loadConfigurationProperties()
   {
      Properties props = PropertiesReader.getInstance().getProperties( "genetic.properties" );

      try
      {
         _terminationFitness  = Double.parseDouble( (String)props.get( "termination_fitness" ) );
         _lifespanExpectancy  = Integer.parseInt( (String)props.get( "lifespan_expectancy" ) );
         _numberOfThreads     = Integer.parseInt( (String)props.get( "number_of_threads" ) );
         _generationSize      = Integer.parseInt( (String)props.get( "generation_size" ) );
         _numberOfEpochs      = Integer.parseInt( (String)props.get( "number_of_epochs" ) );
         _mutationRate        = Double.parseDouble( (String)props.get( "mutation_rate" ) );
         _implementationClass = (String)props.get( "implementation_class" );
         _executionTime       = Long.parseLong( (String)props.get( "execution_time" ) );

         _haltingCriteria.setFitnessTolerance( _terminationFitness );
         _haltingCriteria.setEpochalTolerance( _numberOfEpochs );
         _haltingCriteria.setElapsedTimeTolerance( _executionTime );
      }
      catch( NumberFormatException e )
      {
         System.err.println( "Unable to load properties file!" );
      }
   }

   /**
    * Creates a random collection of Immigrants.
    */
   private ArrayList<Individual> createRandomImmigrants( boolean all )
   {
      ArrayList<Individual> im = new ArrayList<Individual>();
      _populationFitness = 0.0;

      if( all )
      {
            for( int j=0; j<_generationSize; j++ )
            {
               Individual indy = _bestIndividual.clone();
               indy.setEnvironmentCache( _environmentCache );
               indy.randomizeGenome();
               _populationFitness += indy.evaluateFitness();
               indy.age();
               im.add( indy );
            }
      }
      else
      {
         for( int j=0; j<_generationSize; j++ )
         {
            Individual indy = _bestIndividual.clone();
            indy.setEnvironmentCache( _environmentCache );
            indy.randomizeGenome();
            im.add( indy );
         }
      }

      return im;
   }

   /**
    * The main entry point into the algorithm.
    * In this methodology, we keep the threads and phases in lock-step
    * this will prevent things from getting haphazard but may allow
    * brief periods of thread starvation.
    */
   public Individual evolve()
   {
      long       currentTime       = 0;
      long       startTime         = System.currentTimeMillis();
      int        currentEpoch      = 0;
      double     rifArgument       = 0;
      Individual indy              = null;
      Thread     t                 = null;

      ArrayList< Thread > threads = new ArrayList< Thread >();

      // Since this will be evaluated rather frequently, we need to make sure that
      // _bestIndividual.getFitness() is not an expensive operation, i.e., it is only
      // a "getter" and not a full evaluation computation.
      // Note that _bestIndividual should never be null.
      while(    ( _bestIndividual.getFitness() < _haltingCriteria.getFitnessTolerance() )
             && ( currentEpoch < _haltingCriteria.getEpochalTolerance() )
             && ( currentTime  < _haltingCriteria.getElapsedTimeTolerance() ) )
      {
         Collections.sort( _eligibleParents );

         _evaluatedQueue.clear();
         _evaluatedQueue.add( _bestIndividual.clone() );

         for( int i=1; i<_generationSize; i++ )
         {
            indy = _bestIndividual.clone();

            _selectionStrategy.breed( indy, _eligibleParents, _populationFitness );
            _mutationStrategy.mutate( indy, -1.0, _mutationRate );

            _evaluatedQueue.add( indy );
            // number of threads is same as population size....
            t = new Thread( new EvaluationWorker( indy ) );
            threads.add( t );
            t.start();
         }

         for( Thread thread : threads )
         {
            try
            {
               thread.join();
            }
            catch( InterruptedException e )
            {
               System.out.println( "Caught InterruptedException: " + e );
            }
         }

         threads.clear();
         _populationFitness = 0.0;

         for( Individual e : _evaluatedQueue )
         {
            e.age();
            if( e.getFitness() > _bestIndividual.getFitness() )
            {
               _bestIndividual = e.clone();
            }
            _populationFitness += e.getFitness();
         }

         _eligibleParents.clear();
         _eligibleParents.addAll( _evaluatedQueue );

         currentEpoch++;
         currentTime = System.currentTimeMillis() - startTime;
      }

      return _bestIndividual;
   }

   /**
    *
    */
   public void setEnvironmentCache( EnvironmentCache cache )
   {
      _environmentCache = cache;
   }

   /**
    *
    */
   public EnvironmentCache getEnvironmentCache()
   {
      return _environmentCache;
   }

   /**
    * Main entry.
    */
   public static void main( String[] args )
   {
      long             start = System.currentTimeMillis();
      GeneticAlgorithm ga    = new GeneticAlgorithm();
      Individual       best  = ga.evolve();

      System.out.println( "The best individual was: "    + best.toStringFinal() );
      System.out.println( "The total elapsed time was: " + (System.currentTimeMillis() - start) );
   }

}
