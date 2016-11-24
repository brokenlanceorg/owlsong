package functional.algorithm;

import common.*;
import math.*;

import java.util.*;

/**
 * This class further refines the base training algorithm class to a FunctionalCorrelation
 * training class. In this context, there will always be the input or independent variables
 * training data and the dependent or target output training data values.
 */
public abstract class CorrelationAlgorithm extends  TrainingAlgorithm 
{

   private FunctionalCorrelation _correlate;
   private double[][]            _trainingInputData;
   private double[]              _trainingTargetData;

   /**
    *
    */
   public CorrelationAlgorithm()
   {
   }

   /**
    *
    */
   public CorrelationAlgorithm( FunctionalCorrelation function )
   {
      _correlate = function;
   }

   /**
    *
    */
   public CorrelationAlgorithm( HaltingCriteria criteria )
   {
      super( criteria );
   }

   /**
    *
    */
   public CorrelationAlgorithm( HaltingCriteria criteria, FunctionalCorrelation function )
   {
      super( criteria );
      _correlate = function;
   }

   /**
    *
    */
   public void setCorrelate( FunctionalCorrelation function )
   {
      _correlate = function;
   }

   /**
    *
    */
   public FunctionalCorrelation getCorrelate()
   {
      return _correlate;
   }

   /**
    *
    */
   public void setTrainingInputData( double[][] data )
   {
      _trainingInputData = data;
   }

   /**
    *
    */
   public double[][] getTrainingInputData()
   {
      return _trainingInputData;
   }

   /**
    *
    */
   public void setTrainingTargetData( double[] data )
   {
      _trainingTargetData = data;
   }

   /**
    *
    */
   public double[] getTrainingTargetData()
   {
      return _trainingTargetData;
   }

}
