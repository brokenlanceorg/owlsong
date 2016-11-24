package common;

/**
 * This class implements the basic notions of halting an iterative or
 * recursive process. Essentially, any current iterative or recursive
 * algorithm checks for a halt condition under some set of the following
 * criteria:
 * 1) Error Tolerance -- when the error, adequately measured, has been
 *                       reduced below a certain threshold.
 * 2) Fitness Tolerance -- when the fitness, adequately measured, has
 *                         exceeded a certain threshold.
 * 3) Epochal Tolerance -- when the number of iterations or recursions
 *                         has exceeded a certain threshold.
 * 4) Elapsed Time Tolerance -- when the elapsed running time of the 
 *                              algorithm has exceeded a threshold.
 */
public class HaltingCriteria implements Persistable
{

   // We'll default these to some common tolerance values
   private double _errorTolerance       = 0.00001;
   private double _fitnessTolerance     = 1e300;
   private long   _epochalTolerance     = 30000000;
   private long   _elapsedTimeTolerance = 180000;

   /**
    * Sets some common default values for the halting criteria.
    */
   public HaltingCriteria()
   {
   }

   /**
    *
    */
   public HaltingCriteria( double err, double fit, long epoch, long time )
   {
      _errorTolerance       = err;
      _fitnessTolerance     = fit;
      _epochalTolerance     = epoch;
      _elapsedTimeTolerance = time;
   }

   /**
    *
    */
   public void setErrorTolerance( double err )
   {
      _errorTolerance = err;
   }

   /**
    *
    */
   public double getErrorTolerance()
   {
      return _errorTolerance;
   }

   /**
    *
    */
   public void setFitnessTolerance( double fit )
   {
      _fitnessTolerance = fit;
   }

   /**
    *
    */
   public double getFitnessTolerance()
   {
      return _fitnessTolerance;
   }

   /**
    *
    */
   public void setEpochalTolerance( long epoch )
   {
      _epochalTolerance = epoch;
   }

   /**
    *
    */
   public long getEpochalTolerance()
   {
      return _epochalTolerance;
   }

   /**
    *
    */
   public void setElapsedTimeTolerance( long time )
   {
      _elapsedTimeTolerance = time;
   }

   /**
    *
    */
   public long getElapsedTimeTolerance()
   {
      return _elapsedTimeTolerance;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public DAOBase getDAO()
   {
      return null;
   }

}
