package genetic;

/**
 * This is the base class for any Mutation algorithms.
 */
public interface MutationStrategy 
{

   /**
    * This method will mutate an individual as per the implemented mutation strategy.
    * @param Individual This is the individual that is being breed. It's genotype must be specified.
    */
   public void mutate( Individual offspring, Double rifArgument, Double mutationRate );

}
