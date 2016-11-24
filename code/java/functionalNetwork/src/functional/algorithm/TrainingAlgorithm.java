package functional.algorithm;

import common.*;

import java.util.*;

/**
 * This class declares the operations for the base class for all training-type
 * algorithms, i.e., heuristic algorithms that require some specified length of
 * time to train in order to "learn" some particular behavior.
 */
public abstract class TrainingAlgorithm
{

   private HaltingCriteria _haltingCriteria;

   /**
    *
    */
   public TrainingAlgorithm()
   {
   }

   /**
    *
    */
   public TrainingAlgorithm( HaltingCriteria criteria )
   {
      _haltingCriteria = criteria;
   }

   /**
    *
    */
   public void setHaltingCriteria( HaltingCriteria criteria )
   {
      _haltingCriteria = criteria;
   }

   /**
    *
    */
   public HaltingCriteria getHaltingCriteria()
   {
      return _haltingCriteria;
   }

   /**
    *
    */
   public abstract void train();

}
