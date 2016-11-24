package math;

import common.*;
import java.util.*;

/**
 *
 */
public class GaussianElimination
{
   private double            _determinant;
   private ArrayList<Double> _operations;

   /**
    *
    */
   public GaussianElimination()
   {
      _determinant = 1.0;
      _operations = new ArrayList<Double>();
      System.out.println( "determinant (1) is now: " + _determinant );
   }

   /**
    *
    */
   protected double[][] copyMatrix( double[][] input )
   {
      double[][] result = new double[ input.length ][];
      for( int i=0; i<input.length; i++ )
      {
         result[ i ] = new double[ input[ i ].length ];
         for( int j=0; j<input[ i ].length; j++ )
         {
            result[ i ][ j ] = input[ i ][ j ];
         }
      }
      return result;
   }

   /**
    * Assumes that the columns are equal in length.
    */
   public double[][] swapRows( double[][] matrix, int source, int dest )
   {
      if( source != dest )
      {
         double temp = 0;
         _determinant *= -1.0;
         for( int i=0; i<matrix[ source ].length; i++ )
         {
            temp = matrix[ dest ][ i ];
            matrix[ dest ][ i ] = matrix[ source ][ i ];
            matrix[ source ][ i ] = temp;
         }
      }
      return matrix;
   }

   /**
    * It is assumed that the parameter is not null in any dimension.
    * This method will factorize the input matrix into an upper 
    * triangular matrix via GaussianElimination.
    * It is also assumed that this will be an m x (m + 1) sized
    * matrix (an augmented m x m matrix).
    */
   public double[][] getFactorization( double[][] matrix )
   {
      double[][] factor = copyMatrix( matrix );

      for( int c=0; c<factor[ 0 ].length - 2; c++ )
      {
         int maxRow = 0;
         double maxValue = -1 * Double.MAX_VALUE;
         // To speed up the algorithm, it's really only necessary
         // to look at the lower triangular values for the max
         // (The algorithm is still stable.)
         for( int r=c; r<factor.length; r++ )
         {
            if( Math.abs( factor[ r ][ c ] ) > maxValue )
            {
               maxValue = Math.abs( factor[ r ][ c ] );
               maxRow = r;
            }
         }
         factor = swapRows( factor, c, maxRow );
         // maxRow and maxValue are now set properly
         for( int r=c+1; r<factor[ 0 ].length - 1; r++ )
         {
            double scale = factor[ r ][ c ] / factor[ c ][ c ];
            for( int k=c; k<factor[ r ].length; k++ )
            {
               factor[ r ][ k ] -= ( scale * factor[ c ][ k ] );
            }
         }
      }

      return factor;
   }

   /**
    *
    */
   public void printMatrix( double[][] matrix )
   {
      for( int i=0; i<matrix.length; i++ )
      {
         for( int j=0; j<matrix[ i ].length; j++ )
         {
            System.out.print( matrix[ i ][ j ] + " " );
         }
         System.out.println( "" );
      }
   }

   /**
    *
    */
   public double[] backSubstitute( double[][] matrix )
   {
      double[] values = new double[ matrix.length ];

      // loop up through each row:
      for( int i=values.length-1; i>=0; i-- )
      {
         double sum = 0;
         // form the sum for all but first
         for( int j=i+1; j<matrix[ i ].length-1; j++ )
         {
            sum += ( matrix[ i ][ j ] * values[ j ] );
         }
         double temp = ( matrix[ i ][ matrix[ i ].length - 1 ] - sum );
         values[ i ] = temp / matrix[ i ][ i ];
      }

      return values;
   }

   /**
    *
    */
   public double calculateDeterminant( double[][] matrix )
   {
      _determinant = 1.0;
      double[][] U = getFactorization( matrix );
      double temp = 1.0;
      
      for( int i=0; i<matrix.length; i++ )
      {
         _determinant *= U[i][i];
      }

      return _determinant;
   }

   /**
    *
    */
   public double[] solveLinearSystem( double[][] system )
   {
      return backSubstitute( getFactorization( system ) );
   }

   /**
    * This is the standard solution model. For example, suppose you have the following:
    * 3 to 1 on Cambridge to win and 1 to 4 on Oxford. 
    * But a second bookie has Cambridge evens (1 to 1) and Oxford at 1 to 2.
    * 37.50 on Cambridge with bookie 1 and 100 on Oxford with bookie 2. profit = 12.50.
    * But, if we don't know what our profit is going to be, we just use the standard matrix
    * with all equations equaling 1.
    * So, in the above scenario, we'd have a diagonal matrix of 0.5 and 3 with -1 elsewhere.
    * This will give a solution vector of < 8, 3 >, i.e., 11 dollars to win a guranteed 1 dollar.
    */
   public void performDiagonalSolution()
   {
      FileReader file = new FileReader( "odds.dat", " " );
      String[] words = file.getArrayOfWords();
      double[][] system = new double[ words.length ][ words.length + 1 ];

      for( int i=0; i<system.length; i++ )
      {
         for( int j=0; j<system[ i ].length; j++ )
         {
            if( i == j )
            {
               try
               {
                  system[ i ][ j ] = Double.valueOf( words[ i ] );
               }
               catch( NumberFormatException e )
               {
                  System.out.println( "Unable to parse number: " + e );
               }
            }
            else if( j == system[ i ].length - 1 )
            {
               system[ i ][ j ] = 1.0;
            }
            else
            {
               system[ i ][ j ] = -1.0;
            }
         }
      }

      System.out.println( "The system of equations is:" );
      printMatrix( system );

      double[] solution = solveLinearSystem( system );
      System.out.println( "Solution Vector:" );
      for( int i=0; i<solution.length; i++ )
      {
         System.out.println( solution[ i ] );
      }
   }

   /**
    * This is used for the equal profit scenario. For instance, each time
    * the price moves into the strike price, the profit is the same.
    */
   public void performUpperTriangularSolution()
   {
      FileReader file = new FileReader( "odds.dat", " " );
      String[] words = file.getArrayOfWords();
      double[][] system = new double[ words.length ][ words.length + 1 ];

      for( int i=0; i<system.length; i++ )
      {
         for( int j=0; j<system[ i ].length; j++ )
         {
            if( i == j )
            {
               try
               {
                  system[ i ][ j ] = Double.valueOf( words[ i ] );
               }
               catch( NumberFormatException e )
               {
                  System.out.println( "Unable to parse number: " + e );
               }
            }
            else if( j == system[ i ].length - 1 )
            {
               system[ i ][ j ] = 1.0;
            }
            else if( i > j )
            {
               system[ i ][ j ] = 0.0;
            }
            else
            {
               system[ i ][ j ] = -1.0;
            }
         }
      }

      System.out.println( "The system of equations is:" );
      printMatrix( system );

      double[] solution = solveLinearSystem( system );
      System.out.println( "Solution Vector:" );
      for( int i=0; i<solution.length; i++ )
      {
         System.out.println( solution[ i ] );
      }
   }

   /**
    * This is used for the hedge profit scenario. For instance, each time
    * the price moves into the strike price, the profit will be the same
    * for the previous profit (i.e., minimizes how much we spend on the 
    * out-of-the-money bets).
    */
   public void performSymmetricSolution()
   {
      FileReader file = new FileReader( "odds.dat", " " );
      String[] words = file.getArrayOfWords();
      double[][] system = new double[ words.length ][ words.length + 1 ];

      for( int i=0; i<system.length; i++ )
      {
         for( int j=0; j<system[ i ].length; j++ )
         {
            if( i == j )
            {
               try
               {
                  system[ i ][ j ] = Double.valueOf( words[ i ] );
               }
               catch( NumberFormatException e )
               {
                  System.out.println( "Unable to parse number: " + e );
               }
            }
            else if( j == system[ i ].length - 1 )
            {
               system[ i ][ j ] = 1.0;
            }
            else if( i > j )
            {
               system[ i ][ j ] = 1.0;
            }
            else
            {
               system[ i ][ j ] = -1.0;
            }
         }
      }

      System.out.println( "The system of equations is:" );
      printMatrix( system );

      double[] solution = solveLinearSystem( system );
      System.out.println( "Solution Vector:" );
      for( int i=0; i<solution.length; i++ )
      {
         System.out.println( solution[ i ] );
      }
      _determinant = 1.0;
      System.out.println( "The determinant is: " + calculateDeterminant( system ) );
   }

   /**
    * 
    * 
    */
   public void performGeneralSolution()
   {
      FileReader file = new FileReader( "matrix.dat", ";" );
      String[] words = file.getArrayOfWords();
      double[][] system = new double[ words.length ][];

      for( int i=0; i<words.length; i++ )
      {
//          System.out.println( "A word is: " + words[ i ] );
         String[] items = words[i].split( ", *" );
         system[ i ] = new double[ items.length ];
         for( int j=0; j<items.length; j++ )
         {
            system[ i ][ j ] = Double.parseDouble( items[ j ] );
         }
      }

      System.out.println( "The system of equations is:" );
      printMatrix( system );

      double[] solution = solveLinearSystem( system );
      System.out.println( "Solution Vector:" );

      for( int i=0; i<solution.length; i++ )
      {
         System.out.println( solution[ i ] );
      }
   }

   /**
    *
    */
   public static void main( String[] args )
   {
//       int row = 5;
//       int column = 6;
      int row = 3;
      int column = 4;
      double[][] matrix = new double[ row ][ column ];
      
//       // row 1
      matrix[ 0 ][ 0 ] = 0.51;
      matrix[ 0 ][ 1 ] = 0.75;
      matrix[ 0 ][ 2 ] = 0.23;
      matrix[ 0 ][ 3 ] = 1;
//       // row 2
      matrix[ 1 ][ 0 ] = 0.45;
      matrix[ 1 ][ 1 ] = 0.65;
      matrix[ 1 ][ 2 ] = 0.73;
      matrix[ 1 ][ 3 ] = 1;
//       // row 3
      matrix[ 2 ][ 0 ] = 0.15;
      matrix[ 2 ][ 1 ] = 0.25;
      matrix[ 2 ][ 2 ] = 0.93;
      matrix[ 2 ][ 3 ] = 1;

//       // row 1
//       matrix[ 0 ][ 0 ] = 0.5;
//       matrix[ 0 ][ 1 ] = -1;
//       matrix[ 0 ][ 2 ] = 12.5;
//       // row 2
//       matrix[ 1 ][ 0 ] = -1;
//       matrix[ 1 ][ 1 ] = 3;
//       matrix[ 1 ][ 2 ] = 12.5;

//       // row 1 NBA championships
//       matrix[ 0 ][ 0 ] = 2;
//       matrix[ 0 ][ 1 ] = -1;
//       matrix[ 0 ][ 2 ] = -1;
//       matrix[ 0 ][ 3 ] = -1;
//       matrix[ 0 ][ 4 ] = 1;
//       matrix[ 0 ][ 5 ] = 10;
//       // row 2
//       matrix[ 1 ][ 0 ] = -1;
//       matrix[ 1 ][ 1 ] = 2.75;
//       matrix[ 1 ][ 2 ] = -1;
//       matrix[ 1 ][ 3 ] = -1;
//       matrix[ 1 ][ 4 ] = -1;
//       matrix[ 1 ][ 5 ] = 10;
//       // row 3
//       matrix[ 2 ][ 0 ] = -1;
//       matrix[ 2 ][ 1 ] = -1;
//       matrix[ 2 ][ 2 ] = 8;
//       matrix[ 2 ][ 3 ] = -1;
//       matrix[ 2 ][ 4 ] = -1;
//       matrix[ 2 ][ 5 ] = 10;
//       // row 4
//       matrix[ 3 ][ 0 ] = -1;
//       matrix[ 3 ][ 1 ] = -1;
//       matrix[ 3 ][ 2 ] = -1;
//       matrix[ 3 ][ 3 ] = 8.5;
//       matrix[ 3 ][ 4 ] = -1;
//       matrix[ 3 ][ 5 ] = 10;
//       // row 5
//       matrix[ 4 ][ 0 ] = -1;
//       matrix[ 4 ][ 1 ] = -1;
//       matrix[ 4 ][ 2 ] = -1;
//       matrix[ 4 ][ 3 ] = -1;
//       matrix[ 4 ][ 4 ] = 12;
//       matrix[ 4 ][ 5 ] = 10;

//       for( int i=0; i<row; i++ )
//       {
//          for( int j=0; j<column; j++ )
//          {
//             matrix[ i ][ j ] = 10 * Math.random();
//          }
//       }

//       double[] result = solveLinearSystem( matrix );
//       System.out.println( "Solution Vector:" );
//       for( int i=0; i<result.length; i++ )
//       {
//          System.out.println( result[ i ] );
//       }

      GaussianElimination object = new GaussianElimination();
      if( Boolean.getBoolean( "upper.triangular" ) )
      {
         object.performUpperTriangularSolution();
      }
      else if( Boolean.getBoolean( "symmetric" ) )
      {
         object.performSymmetricSolution();
      }
      else if( Boolean.getBoolean( "diagonal" ) )
      {
         object.performDiagonalSolution();
      }
      else
      {
         object.performGeneralSolution();
      }
      System.out.println( "The determinant is: " + object.calculateDeterminant( matrix ) );
   }

}
