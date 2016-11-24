package genetic;

import java.util.*;
import math.MathUtilities;

/**
 * This is the Random Immigrant Fractal (RIF) mutation strategy.
 */
public class RIFMutationStrategy implements MutationStrategy
{

   /**
    * This method will mutate an individual as per the implemented mutation strategy.
    * @param Individual This is the individual that is being breed. It's genotype must be specified.
    */
   public void mutate( Individual offspring, Double rifArgument, Double mutationRate )
   {
      MathUtilities mu = new MathUtilities();
      ArrayList<Double> genotype = offspring.getGenotype();
      for( int i=0; i<genotype.size(); i++ )
      {
         Double g = genotype.get( i );
         Double rand = mu.random();
         if( rifArgument < 0 )
         {
            if( rand <= mutationRate )
            {
// System.out.println( "RIFMutationStrategy::mutate non-RIF before: " + g );
               g = mu.random();
               genotype.set( i, g );
// System.out.println( "RIFMutationStrategy::mutate non-RIF after: " + g );
            }
         }
         else
         {
            //System.out.println( "RIFMutationStrategy::mutate RIF before: " + g );
            g += (rand * rifArgument);
            g = (g > 1) ? (g - 1) : g;
            genotype.set( i, g );
            //System.out.println( "RIFMutationStrategy::mutate RIF after: " + g );
         }
      }
   }

}
