package math;

import java.util.*;

/**
 * This class captures the "portrait" of the various entropy-related
 * calculations for a given multidimensional dataseries -- currently
 * two dimensional structures are considered but can be extended to
 * higher dimensional structures. The portrait will be composed of the
 * following statistics:
 *   1) Phase Space Dimension
 *   2) Lyapunov Exponent
 *   3) Approximate Entropy
 */
public class EntropicPortrait
{

   // this matrix holds the phase space dimensions
   private double[][][] _phaseSpaces;

   // this matrix holds the Lyapunov exponents
   private double[][][] _lyapunovExponents;

   // this matrix holds the approximate entropy values
   private double[][][] _approximateEntropies;

   /**
    *
    */
   public EntropicPortrait()
   {
   }

   /**
    *
    */
   public EntropicPortrait( double[][][] p, double[][][] l, double[][][] a )
   {
      _phaseSpaces           = p;
      _lyapunovExponents     = l;
      _approximateEntropies  = a;
   }

   /**
    *
    */
   public void setPhaseSpaces( double[][][] phase )
   {
      _phaseSpaces = phase;
   }

   /**
    *
    */
   public double[][][] getPhaseSpaces()
   {
      return _phaseSpaces;
   }

   /**
    *
    */
   public void setLyapunovExponents( double[][][] lya )
   {
      _lyapunovExponents = lya;
   }

   /**
    *
    */
   public double[][][] getLyapunovExponents()
   {
      return _lyapunovExponents;
   }

   /**
    *
    */
   public void setApproximateEntropies( double[][][] appen )
   {
      _approximateEntropies = appen;
   }

   /**
    *
    */
   public double[][][] getApproximateEntropies()
   {
      return _approximateEntropies;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public String toString()
   {
      String s = "Phase Space:\n";

      for( int i=0; i<_phaseSpaces.length; i++ )
      {
         for( int j=0; j<_phaseSpaces[ i ].length; j++ )
         {
            s += "\n";
            for( int k=0; k<_phaseSpaces[ i ][ j ].length; k++ )
            {
               s += _phaseSpaces[ i ][ j ][ k ] + "\n";
            }
         }
      }

      s += "\nLyapunov Exponents:\n";

      for( int i=0; i<_lyapunovExponents.length; i++ )
      {
         for( int j=0; j<_lyapunovExponents[ i ].length; j++ )
         {
            s += "\n";
            for( int k=0; k<_lyapunovExponents[ i ][ j ].length; k++ )
            {
               s += _lyapunovExponents[ i ][ j ][ k ] + "\n";
            }
         }
      }

      s += "\nApproximate Entropies:\n";

      for( int i=0; i<_approximateEntropies.length; i++ )
      {
         for( int j=0; j<_approximateEntropies[ i ].length; j++ )
         {
            s += "\n";
            for( int k=0; k<_approximateEntropies[ i ][ j ].length; k++ )
            {
               s += _approximateEntropies[ i ][ j ][ k ] + "\n";
            }
         }
      }

      return s;
   }

}
