package genetic;

import java.util.*;

/**
 * Handles the specifics of the Uniform crossover operator.
 *
 */
public class UniformSelectionStrategy implements SelectionStrategy 
{

   /**
    * This method will breed an individual as per the
    * implemented breeding strategy.
    * @param Individual This is the individual that is 
    * being breed. It's genotype must be specified.
    * @param ArrayList<Individual> these are the 
    * potential parents for the offspring.
    */
   public void breed( Individual offspring, ArrayList<Individual> eligibleParents, Double totalFitness )
   {
//       System.out.println( "UniformSelectionStrategy::breed parents size: " + eligibleParents.size() );
//       System.out.println( "UniformSelectionStrategy::breed total fitness is: " + totalFitness );

      ArrayList<Double> genotype = eligibleParents.get( 0 ).getGenotype();
//       System.out.println( "UniformSelectionStrategy::breed first parent size: " + genotype.size() );
      ArrayList<Double> oGenotype = offspring.getGenotype();

      for( int i=0; i<genotype.size(); i++ )
      {
         double sum = 0;
         double threshold = Math.random() * totalFitness;
//          System.out.println( "UniformSelectionStrategy::breed threshold is: " + threshold );
         for( int j=0; j<eligibleParents.size(); j++ )
         {
            Individual parent = eligibleParents.get( j );
//             System.out.println( "UniformSelectionStrategy::breed parent fitness is: " + parent.getFitness() );
            sum += parent.getFitness();
//             System.out.println( "UniformSelectionStrategy::breed sum is: " + sum );
            if( sum >= threshold )
            {
//                System.out.println( "UniformSelectionStrategy::breed setting gene" );
               ArrayList<Double> pGen = parent.getGenotype();
               oGenotype.set( i, pGen.get( i ) );
               break;
            }
         }
      }
   }

}
