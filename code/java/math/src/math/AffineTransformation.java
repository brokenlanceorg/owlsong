package math;

import java.util.*;

/**
 * This class contains the algorithm for an affine transformation in two dimensions.
 * The basic idea for the two-dimensional case is the following transformation:
 * w( x1, x2 ) = ( a * x1 + b * x2 + e, c * x1 + d * x2 + f )
 * Another way to notate this using matrix and vector symbols is:
 * w[ x1, x2 ] = [ a, b ] * [ x1 ] + [ e ]   or equivalently:
 *               [ c, d ]   [ x2 ]   [ f ]
 * w( x1, x2 ) = A * x' + t', where A is a matrix and x' and t' are vectors.
 */
public class AffineTransformation
{

   private double _a;
   private double _b;
   private double _c;
   private double _d;
   private double _e;
   private double _f;
   private double _probability = -1;
   private double _contractivity = -1;
   private double[] _contractivityConditions = null;

   /**
    *
    */
   public AffineTransformation()
   {
   }

   /**
    *
    */
   public AffineTransformation( double a, double b, double c, double d, double e, double f )
   {
      _a = a;;
      _b = b;;
      _c = c;;
      _d = d;;
      _e = e;;
      _f = f;;
//       makeContractive();
   }

   /**
    *
    */
   public void setProbability( double p )
   {
      _probability = p;
   }

   /**
    * Lazy calculation.
    * This is a pre-calculation, it is fully set by the AffineSystem class.
    */
   public double getProbability()
   {
      if( _probability == -1 )
      {
         _probability = Math.abs( (_a * _d) - (_b * _c) );
         if( _probability == 0 )
         {
            _probability = 0.001;
         }
      }

      return _probability;
   }

   /**
    * The contractivity factor, s, for an affine transformation is a number s in [0, 1]
    * such that the following hold in the Euclidean metric:
    * d( w( x1 ), w( x2 ) ) <= s * d( x1, x2 )
    * in other words, s >= d( w( x1 ), w( x2 ) ) / d( x1, x2 )
    * Since this holds for arbitrary x1, x2 in the metric space, we can choose them in
    * 2D to be ( 0, 0 ) and ( 1, 1 ) for ease of computations. 
    */
   public double getContractivity()
   {
      if( _contractivity == -1 )
      {
         double[] value1 = evaluate( (double)0, (double)0 );
         double[] value2 = evaluate( (double)1, (double)1 );
         double d1 = (new MathUtilities()).getEuclideanDistance2D( value1[ 0 ], value1[ 1 ], value2[ 0 ], value2[ 1 ] );
         _contractivity = ( d1 / 1.414213562 );
      }

      return _contractivity;
   }

   /**
    * The conditions for an affine linear map (contraction) are the following:
    * a^2 + c^2 < 1
    * b^2 + d^2 < 1
    * a^2 + b^2 + c^2 + d^2 < 1 + ( a*d + c*b )^2
    */
   public double[] getContractivityConditions()
   {
      if( _contractivityConditions == null )
      {
         _contractivityConditions = new double[ 4 ];;
         _contractivityConditions[ 0 ] = ( _a * _a ) + ( _c * _c );
         _contractivityConditions[ 1 ] = ( _b * _b ) + ( _d * _d );
         _contractivityConditions[ 2 ] = ( _a * _a ) + ( _b * _b ) + ( _c * _c ) + ( _d * _d );
         _contractivityConditions[ 3 ] = ( _a * _d ) + ( _c * _b );
         _contractivityConditions[ 3 ] *= _contractivityConditions[ 3 ];
         _contractivityConditions[ 3 ] += 1;
      }

      return _contractivityConditions;
   }

   /**
    *
    */
   public boolean isContractive()
   {
      boolean isContractive = false;
      double[] conditions = getContractivityConditions();

      if(    conditions[ 0 ] < 1
          && conditions[ 1 ] < 1
          && conditions[ 2 ] < conditions[ 3 ] )
      {
         isContractive = true;
      }

      return isContractive;
   }

   /**
    * This method may not necessarily succeed, it merely attempts to make the 
    * parameters contractive.
    * We attempt to do this by making one of a or c small and one of b or d small
    * and one of a or d large and one of c or b large. Thus, the following should
    * be done to maximize the contractivity chance:
    */
   protected void makeContractive()
   {
      if( isContractive() == false )
      {
         ArrayList<Double> params = new ArrayList<Double>( 4 );
         params.add( _a );
         params.add( _b );
         params.add( _c );
         params.add( _d );
         Collections.sort( params );
         _c = params.get( 0 );
         _b = params.get( 1 );
         _d = params.get( 2 );
         _a = params.get( 3 );
      }
   }

   /**
    *
    */
   public double[] evaluate( double x, double y )
   {
      double[] result = new double[ 2 ];

      result[ 0 ] = ( _a * x ) + ( _b * y ) + _e;
      result[ 1 ] = ( _c * x ) + ( _d * y ) + _f;

      return result;
   }

   /**
    *
    */
   public String toString()
   {
      String result = new String( "\nProbability = " );
      result += getProbability() + "\n";
      result += "Contractivity = " + getContractivity() + "\n";
      result += "isContractive = " + isContractive() + "\n";
      double[] conditions = getContractivityConditions();
      result += "Contractivity Condition 1 = " + conditions[0] + "\n";
      result += "Contractivity Condition 2 = " + conditions[1] + "\n";
      result += "Contractivity Condition 3 = " + conditions[2] + "\n";
      result += "Contractivity Condition 4 = " + conditions[3] + "\n";
      result += " a = " + _a + "\n";
      result += " b = " + _b + "\n";
      result += " c = " + _c + "\n";
      result += " d = " + _d + "\n";
      result += " e = " + _e + "\n";
      result += " f = " + _f + "\n";

      return result;
   }

   /**
    *
    */
   public static void main( String[] args )
   {
      AffineTransformation atrans = new AffineTransformation( Math.random(), 
                                                              Math.random(), 
                                                              Math.random(), 
                                                              Math.random(), 
                                                              Math.random(), 
                                                              Math.random() );
//       AffineTransformation atrans = new AffineTransformation( 0.5, 0, 0, 0.5, 1, 1 );
      double[] result = null;
      System.out.println( "\nThe affine transformation is: " + atrans );
      for( int i=0; i<5; i++ )
      {
         double x = (double)i / (double)10;
         for( int j=0; j<5; j++ )
         {
            double y = (double)j / (double)5;
            result = atrans.evaluate( x, y );
            System.out.println( x + ", " + y + " ,==>, " + result[ 0 ] + ", " + result[ 1 ] );
         }
      }
   }

}
