package functional.algorithm;

import genetic.*;
import genetic.impl.*;
import common.*;
import math.*;

import java.util.*;

/**
 * This class defines the Gradient descent algorithm for the FunctionalCorrelation class.
 * This algorithm is more fully described and elucidated in the white paper "Neural Network
 * Design Patterns" by Brandon Benham.
 * In essence, here's how the algorithm works: the functional correlation objects were modified
 * to include a weight parameter in the functional correlation equation. So, each independent 
 * variable was scaled by its own weight adjustment. Then, these weights are incrementally
 * adjusted via the gradient descent to further refine the error tolerance if possible.
 */
public class GradientCorrelationAlgorithm extends CorrelationAlgorithm
{

   private double[] _bestWeights;
   private double   _alpha = 0.5; // learning rate parameter
   private double   _stepSize = 0.001;

   /**
    *
    */
   public GradientCorrelationAlgorithm()
   {
   }

   /**
    *
    */
   public GradientCorrelationAlgorithm( HaltingCriteria criteria )
   {
      super( criteria );
   }

   /**
    *
    * @return double[] -- the best weights as determined by the gradient descent algorithm.
    */
   public double[] getWeights()
   {
      return _bestWeights;
   }

   /**
    *
    * @param double the step size to be used when calulating the partial derivatives.
    */
   public void setStepSize( double size )
   {
      _stepSize = size;
   }

   /**
    *
    * @param double -- the learning rate value.
    */
   public void setAlpha( double alpha )
   {
      _alpha = alpha;
   }

   /**
    *
    */
   private double[] copyWeights( double[] weights )
   {
      double[] w = new double[ weights.length ];

      for( int i=0; i<weights.length; i++ )
      {
         w[ i ] = weights[ i ];
      }
      return w;
   }

   /**
    * Train method based on the gradient descent algorithm.
    * 1) For each training target instance do:
    *    2) For each independent variable do:
    *       3) calculate delta_t (error sum squared)
    *       4) update weight for this variable using partials and alpha
    *       5) if total error is below threshold quit
    */
   public void train()
   {
      FunctionalCorrelation correlate = getCorrelate();
      HashSet< Integer > variables = correlate.getVariables();
      long start = System.currentTimeMillis();
      long elapsed = 0;
      double error = 1e30;
      double halfStep = _stepSize / 2;

      while(    elapsed < getHaltingCriteria().getElapsedTimeTolerance() 
             && error   > getHaltingCriteria().getErrorTolerance() )
      {
         double[] target = getTrainingTargetData();
         double delta = 0;
         error = 0;

         for( int i=0; i<target.length; i++ )
         {
            for( int var : variables )
            {
               double value = correlate.evaluate( i );
// System.out.println( "evaluate is: " + value );
//                delta = Math.abs( value - target[ i ] );
               delta = ( value - target[ i ] );
               error += Math.abs( delta );

               // calcluate the partials with respect to the weights
               _bestWeights = correlate.getWeights();
               double previous = _bestWeights[ var ];

               _bestWeights[ var ] = ( previous + halfStep );
               correlate.setWeights( _bestWeights );
               value = correlate.evaluate( i );
// System.out.println( "f( x + h ) is: " + value );

               _bestWeights[ var ] = ( previous - halfStep );
               correlate.setWeights( _bestWeights );
               value -= correlate.evaluate( i );
               value /= _stepSize;
// System.out.println( "[ f( x + h ) - f( x - h ) ] / 2h is: " + value );

               // now, update the weights based on this data:
// System.out.println( "Previous was: " + previous );
               previous -= ( _alpha * delta * value * (getTrainingInputData())[ var ][ i ] );
// System.out.println( "Previous is now: " + previous );
               _bestWeights[ var ] = previous;
               correlate.setWeights( _bestWeights );
            }
         }
         // finally, update the elapsed time:
         elapsed = System.currentTimeMillis() - start;
System.out.println( "error is now: " + error );
System.out.println( "elapsed is now: " + elapsed + " tolerance is: " + getHaltingCriteria().getElapsedTimeTolerance() );
      }
   }

}
