package genetic;

import java.util.*;

/**
 * This is the base class for any Selection algorithms.
 */
public interface SelectionStrategy 
{

   /**
    * This method will breed an individual as per the
    * implemented breeding strategy.
    * @param Individual This is the individual that is 
    * being breed. It's genotype must be specified.
    * @param ArrayList<Individual> these are the 
    * potential parents for the offspring.
    */
   public void breed( Individual offspring, ArrayList<Individual> eligibleParents, Double totalFitness );

}
