package math;

import java.util.*;

/**
 * This class encapsulates a system of affine transformations.
 * The result of the system is obtained via a call to the evaluate method.
 * This method performs the following actions:
 * 1) determines via the determinate probabilities which transformation is used
 * 2) applies the transformation to that input data point
 */
public class AffineSystem
{

   private AffineTransformation[] _transformations;
   private double                 _totalProbability = 0;

   /**
    *
    */
   public AffineSystem()
   {
   }

   /**
    *
    */
   public AffineSystem( int num )
   {
      _transformations = new AffineTransformation[ num ];
      for( int i=0; i<_transformations.length; i++ )
      {
         _transformations[ i ] = new AffineTransformation( Math.random(),
                                                           Math.random(),
                                                           Math.random(),
                                                           Math.random(),
                                                           Math.random(),
                                                           Math.random() );
         while(    _transformations[ i ].getContractivity() > 1
                && _transformations[ i ].isContractive() == false )
         {
            _transformations[ i ] = new AffineTransformation( Math.random(),
                                                              Math.random(),
                                                              Math.random(),
                                                              Math.random(),
                                                              Math.random(),
                                                              Math.random() );
         }
         _totalProbability += _transformations[ i ].getProbability();
      }

      for( int i=0; i<_transformations.length; i++ )
      {
         _transformations[ i ].setProbability( _transformations[ i ].getProbability() / _totalProbability );
      }
   }

   /**
    * Since there's 6 parameters to specify a single affine transformation, we
    * assume that we're taking blocks of 6 for each affine map.
    */
   public AffineSystem( double[] parameters )
   {
      int num = 0;
      _transformations = new AffineTransformation[ parameters.length / 6 ];

      for( int i=0; i<parameters.length; i+=6 )
      {
         _transformations[ num ] = new AffineTransformation( parameters[ i   ], 
                                                             parameters[ i+1 ],
                                                             parameters[ i+2 ],
                                                             parameters[ i+3 ],
                                                             parameters[ i+4 ],
                                                             parameters[ i+5 ]);
         _totalProbability += _transformations[ num++ ].getProbability();
      }

      for( int i=0; i<_transformations.length; i++ )
      {
         _transformations[ i ].setProbability( _transformations[ i ].getProbability() / _totalProbability );
      }
   }

   /**
    *
    */
   public boolean isContractive()
   {
      boolean isContractive = true;

      for( int i=0; i<_transformations.length; i++ )
      {
         if( _transformations[i].isContractive() == false )
         {
            isContractive = false;
            break;
         }
      }

      return isContractive;
   }

   /**
    *
    */
   public boolean isFullContractive()
   {
      boolean isContractive = true;

      for( int i=0; i<_transformations.length; i++ )
      {
         if( _transformations[i].getContractivity() > 1.0 )
         {
            isContractive = false;
            break;
         }
      }

      return isContractive;
   }

   /**
    * To determine which function we'll use, we use the roulette wheel method of probability.
    */
   public double[] evaluate( double x, double y )
   {
      double[] result = new double[ 3 ];
      double[] temp = null;
      double threshold = Math.random();
      double sum = 0;

      for( int i=0; i<_transformations.length; i++ )
      {
         sum += _transformations[ i ].getProbability();
         if( sum >= threshold )
         {
            temp = _transformations[ i ].evaluate( x, y );
            result[ 0 ] = temp[ 0 ];
            result[ 1 ] = temp[ 1 ];
            result[ 2 ] = (double) i;
            break;
         }
      }

      if( result == null )
      {
         System.out.println( "sum never broke threshold!!!" );
         temp = _transformations[ _transformations.length - 1 ].evaluate( x, y );
         result[ 0 ] = temp[ 0 ];
         result[ 1 ] = temp[ 1 ];
         result[ 2 ] = (double) (_transformations.length - 1);
      }

      return result;
   }

   /**
    *
    */
   public double[] getProbabilities()
   {
      double[] probs = new double[ _transformations.length ];
      for( int i=0; i<_transformations.length; i++ )
      {
         probs[ i ] = _transformations[ i ].getProbability();
      }
      return probs;
   }

   /**
    *
    */
   public String toString()
   {
      String result = new String();

      for( int i=0; i<_transformations.length; i++ )
      {
         result += _transformations[ i ].toString();
      }

      return result;
   }

   /**
    *
    */
   public static void main( String[] args )
   {
      double[] params = new double[ 18 ];

      params[ 0 ]  = 0.5;
      params[ 1 ]  = 0.0;
      params[ 2 ]  = 0.0;
      params[ 3 ]  = 0.5;
      params[ 4 ]  = 1.0;
      params[ 5 ]  = 1.0;
      params[ 6 ]  = 0.5;
      params[ 7 ]  = 0.0;
      params[ 8 ]  = 0.0;
      params[ 9 ]  = 0.5;
      params[ 10 ] = 1.0;
      params[ 11 ] = 50.0;
      params[ 12 ] = 0.5;
      params[ 13 ] = 0.0;
      params[ 14 ] = 0.0;
      params[ 15 ] = 0.5;
      params[ 16 ] = 50.0;
      params[ 17 ] = 50.0;

//       for( int i=0; i<18; i++ )
//       {
//          params[ i ] = Math.random();
//       }

      AffineSystem system = new AffineSystem( params );
      System.out.println( "The system is: " + system );

      double x = Math.random();
      double y = Math.random();

      for( int i=0; i<10; i++ )
      {
         double[] map = system.evaluate( x, y );
         x = map[ 0 ];
         y = map[ 1 ];
         System.out.println( x + "," + y );
      }
   }

}
