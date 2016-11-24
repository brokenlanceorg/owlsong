package fuzzy;

import java.math.*;

/**
 * The basic class for Conjunction.
 * In order to specify other operations extend this class.
 * The type is typically derived from a genotype so it will
 * reside between 0 and 1.
 */
public class Conjunction
{
   // The type of conjunction
   protected double _type;

   /**
    * Default constructor.
    * The type is typically derived from a genotype so it will
    * reside between 0 and 1.
    */
   public Conjunction()
   {
   }

   /**
    * Constructor.
    * The type is typically derived from a genotype so it will
    * reside between 0 and 1.
    */
   public Conjunction( double type )
   {
      _type = type;
   }

   /**
    * Evaluates the conjunction.
    */
   public double conjunctify( double observable1, double observable2 )
   {
      double con = -1;
      double type = 10 * _type;
      int t = (int)Math.floor( type );

      switch( t )
      {
         case 0 :
         case 1 :
         case 2 :
         case 3 :
         case 4 :
            con = Math.min( observable1, observable2 );
         break;
         case 5 :
         case 6 :
         case 7 :
         case 8 :
         case 9 :
            con = Math.max( observable1, observable2 );
         break;
      }

      return con;
   }

   /**
    * For testing purposes only.
    */
   public static void main( String[] args )
   {
      Conjunction aCon1 = new Conjunction( 0.01 );
      aCon1.conjunctify( 0.4, 0.3 );

      Conjunction aCon2 = new Conjunction( 0.41 );
      aCon2.conjunctify( 0.4, 0.3 );

      Conjunction aCon3 = new Conjunction( 0.71 );
      aCon3.conjunctify( 0.4, 0.3 );

      Conjunction aCon4 = new Conjunction( 0.81 );
      aCon4.conjunctify( 0.7, 0.83 );
   }

}
