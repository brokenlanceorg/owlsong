package genetic;

import java.util.*;
import math.MathUtilities;

/**
 * Handles the specifics of the Biform crossover operator.
 *
 */
public class BiformSelectionStrategy implements SelectionStrategy 
{

   /**
    * This method will breed an individual as per the implemented breeding strategy.
    * @param Individual This is the individual that is being breed. It's genotype must be specified.
    * @param ArrayList<Individual> these are the potential parents for the offspring.
    */
   public void breed( Individual offspring, ArrayList<Individual> eligibleParents, Double totalFitness )
   {
      MathUtilities mu = new MathUtilities();

      Individual        p1        = eligibleParents.get( 0 );
      Individual        p2        = null;
      ArrayList<Double> g1        = p1.getGenotype();
      ArrayList<Double> g2        = null;
      double            sum       = p1.getFitness();
      double            threshold = mu.random() * totalFitness;

      ArrayList<Double> oGenotype = offspring.getGenotype();

      for( int i=1; i<eligibleParents.size(); i++ )
      {
         p2   = eligibleParents.get( i );
         sum += p2.getFitness();
         if( sum >= threshold )
         {
            break;
         }
      }

      g2 = p2.getGenotype();

      for( int i=0; i<g1.size(); i++ )
      {
         if( mu.random() <= 0.5 )
         {
            oGenotype.set( i, g1.get( i ) );
         }
         else
         {
            oGenotype.set( i, g2.get( i ) );
         }
      }
   }

}
