package functional.strategy;

import functional.algorithm.*;
import common.*;

import java.util.*;

/**
 * This class defines and declares the basics of the TrainingStrategy hierarchy.
 * This is the superclass to all meta-algorithms that consist of other sub-algorithms
 * that can be used in one way or another to learn or train particular behaviors.
 */
public abstract class TrainingStrategy
{

   protected HaltingCriteria _haltingCriteria;

   /**
    *
    */
   public TrainingStrategy()
   {
   }

   /**
    *
    */
   public TrainingStrategy( HaltingCriteria criteria )
   {
      _haltingCriteria = criteria;
   }

   /**
    *
    */
   public abstract void train();

}
