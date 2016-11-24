package genetic;

import java.util.*;

/**
 * The BreederWorker class is a Runnable class that can pull individuals
 * off the breeding queue and breed them according to the SelectionStrategy.
 * This threaded class will listen to its associated queue and pull offspring
 * and perform it's operation on them to create new offspring.
 */
public class BreederWorker implements Runnable
{

   // The queue on which this thread listens
   private Queue<Individual> _breedingQueue;

   // The arraylist that holds bred individuals
   private ArrayList<Individual> _incubationQueue;

   // The arraylist that holds potential parents
   private ArrayList<Individual> _eligibleParents;

   // The Selection Strategy algorithm
   private SelectionStrategy _selectionStrategy;

   // The Mutation Strategy algorithm
   private MutationStrategy _mutationStrategy;

   // The arraylist that holds data
   private ArrayList<Double> _data;

   // The mutation rate when not in the RIF phase
   private Double _mutationRate;

   /**
    * Default Constructor.
    */
   public BreederWorker()
   {
   }

   /**
    * Constructor.
    */
   public BreederWorker( Queue<Individual> q, ArrayList<Individual> q2,
                         ArrayList<Individual> p, SelectionStrategy s,
                         ArrayList<Double> data, MutationStrategy m,
                         Double mRate )
   {
      _breedingQueue = q;
      _incubationQueue = q2;
      _eligibleParents = p;
      _selectionStrategy = s;
      _mutationStrategy = m;
      _data = data;
      _mutationRate = mRate;
   }

   /**
    * Implementation of Runnable Interface.
    */
   public void run()
   {
      Individual i = null;
      while( true )
      {
         synchronized( _breedingQueue )
         {
            System.out.println( "Breeder thread got lock on breeder queue size: " + _breedingQueue.size() );
            while( _breedingQueue.peek() == null )
            {
               try
               {
                  _breedingQueue.wait();
               }
               catch( InterruptedException e )
               {
                  return;
               }
            }
            System.out.println( "Breeder thread finished waiting on breeder queue" );
//
            i = _breedingQueue.poll();

//             System.out.println( "Breeder thread genome before: " );
//             for( double gene : i.getGenotype() )
//             {
//                System.out.println( "gene: " + gene );
//             }

            _selectionStrategy.breed( i, _eligibleParents, _data.get( 0 ) );
            _mutationStrategy.mutate( i, _data.get( 1 ), _mutationRate );
//             System.out.println( "Breeder thread genome after: " );
//             for( double gene : i.getGenotype() )
//             {
//                System.out.println( "gene: " + gene );
//             }
         }

         // at this point, the new individual can be bred
         synchronized( _incubationQueue )
         {
            System.out.println( "Breeder thread got lock on incubation queue" );
            _incubationQueue.add( i );
            System.out.println( "Breeder thread got lock on incubation queue size is now: " + _incubationQueue.size() );
            _incubationQueue.notifyAll();
         }
      }
   }

}
