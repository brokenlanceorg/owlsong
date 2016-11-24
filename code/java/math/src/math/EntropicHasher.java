package math;

import java.util.*;
import java.math.*;

/**
 * This class provides the ability to calculate entropy-based hashes of a 
 * large dataset.
 * The original dataset will be a matrix of raw data values representing objects
 * such as images or phase data or time-series data. This dataset matrix will be 
 * partitioned into square sub-matrices of side length 2^s, call s blockSize.
 * The blockSize, s, must be used in the power-2 form because the Hilbert curve is
 * used to decompose the data into a time-series and this requires blocks of length
 * powers of 2.
 * These sub-matrices are unfolded via the Hilbert curve method into a time-series
 * dataset. To determine the size of the second-level matrix, we note that it takes
 * about 500 milliseconds to compute the entropic data for a dataseries of size 2^6.
 * This means that 1096 of these computations will take roughly 8 minutes. This implies
 * that the second-level matricies will have sides of length 2^5. There will be a sub-
 * matrix for each entropic statistic (phase, Lyapunov, entropy). These second-level
 * matrices can be called "hidden" matrices.
 * Finally, each second-level matrix is then broken down into sub-blocks of size about
 * 2^4 x 2^4 to create a set of datasets that form the datalines for the NLR.
 * The NLR is then processed to determine causal dependencies.
 *
 * n       2^n     (2^n)^2 = 2^(2n)  Running time in minutes of 2^6 size blocks     Number of NLR variables
 * 1       2       4                 0.033333333                                    1
 * 2       4       16                0.133333333                                    4
 * 3       8       64                0.533333333                                    9
 * 4       16      256               2.133333333                                    16
 * 5       32      1024              8.533333333                                    25
 * 6       64      4096              34.13333333                                    36
 * 7       128     16384             136.5333333                                    49
 * 8       256     65536             546.1333333                                    64
 * 9       512     262144            2184.533333                                    81
 * 10      1024    1048576           8738.133333                                    100
 * 11      2048    4194304           34952.53333                                    121
 * 12      4096    16777216          139810.1333                                    144
 * 13      8192    67108864          559240.5333                                    169
 * 14      16384   268435456         2236962.133                                    196
 * 15      32768   1073741824        8947848.533                                    225
 * 16      65536   4294967296        35791394.13                                    256
 *
 */
public class EntropicHasher
{

   // This holds the value of the block size used to compute the second-level matrix
   // and is a power of two -- it's a power of two because of the Hilbert curve.
   private int _blockSize;

   // Holds the block size that will be used by the Hilbert curve to compute the datasets
   // for the NLR -- this is the block size computed on the second-level matrices.
   // Like the block size, this number is a power of two.
   private int _blockSizeNLR;

   // This is the size of the hidden matrix size and should be chosen such that 
   // hidden_size x hidden_size = a number so that the entropic calclulations
   // complete in a reasonable amount of time.
   private int _hiddenMatrixSize;

   // This is the size of the final matrix whose square is the number of NLR variables.
   private int _finalMatrixSize;

   // This is the length of the "lag" used when embedding the time series data into
   // the multidimensional phase space.
   private int _embeddingLag = 200;

   /**
    *
    */
   public EntropicHasher()
   {
   }

   /**
    * @param int -- block size of the Hilbert curve. 
    * @param int -- block size of the hidden matrix for the NLR Hilbert curve.
    * @param int -- side length of the hidden matrix.
    * @param int -- side length of the final matrix.
    */
   public EntropicHasher( int blockSize, int blockSizeNLR, int hiddenSize, int finalSize )
   {
      setBlockSize( blockSize );
      setBlockSizeNLR( blockSizeNLR );
      setHiddenMatrixSize( hiddenSize );
      setFinalMatrixSize( finalSize );
   }

   /**
    * Usually set to something like 6.
    */
   public void setBlockSize( int blockSize )
   {
      _blockSize = (int) Math.pow( 2, blockSize );
   }

   /**
    *
    */
   public int getBlockSize()
   {
      return _blockSize;
   }

   /**
    * Usually set to something like 4 and will be a perfect square.
    */
   public void setBlockSizeNLR( int blockSize )
   {
      _blockSizeNLR = (int) Math.pow( 2, blockSize );
   }

   /**
    *
    */
   public int getBlockSizeNLR()
   {
      return _blockSizeNLR;
   }

   /**
    * Usually set to something like 4 and will be a perfect square.
    */
   public void setFinalMatrixSize( int size )
   {
      _finalMatrixSize = size;
   }

   /**
    *
    */
   public int getFinalMatrixSize()
   {
      return _finalMatrixSize;
   }

   /**
    *
    */
   public int getNumberOfNLRVariables()
   {
      return _finalMatrixSize * _finalMatrixSize;
   }

   /**
    * Usually set to something like 5 or 7.
    */
   public void setHiddenMatrixSize( int blockSize )
   {
      _hiddenMatrixSize = blockSize;
   }

   /**
    *
    */
   public int getHiddenMatrixSize()
   {
      return _hiddenMatrixSize;
   }

   /**
    * Usually set to something like 200.
    */
   public void setEmbeddingLag( int lag )
   {
      _embeddingLag = lag;
   }

   /**
    *
    */
   public int getEmbeddingLag()
   {
      return _embeddingLag;
   }

   /**
    * This method will perform the actual entropic hash functions on the input dataset.
    * Some important notes:
    *   The embedding lag isn't checked for boundary conditions.
    *   The maximum phase space dimension is hard-coded to 20.
    *
    * @param double[][] -- a matrix of time series data.
    * @return EntropicPortrait -- the Entropic "hash" of the input matrix.
    */
   public EntropicPortrait getEntropicPortrait( double[][] matrix )
   {
      EntropicPortrait portrait     = new EntropicPortrait();
      MathUtilities    utils        = new MathUtilities();
      double[][][]     ds           = utils.hilbertDownsample( matrix, _blockSize, _hiddenMatrixSize );
      double[][]       lya          = new double[ _hiddenMatrixSize ][ _hiddenMatrixSize ];
      double[][]       appen        = new double[ _hiddenMatrixSize ][ _hiddenMatrixSize ];
      double[][]       phase        = new double[ _hiddenMatrixSize ][ _hiddenMatrixSize ];
      int              maxDimension = 10;
      int              N            = ds[ 0 ][ 0 ].length - maxDimension * _embeddingLag;
      System.out.println( "N is: " + N );

      for( int i=0; i<_hiddenMatrixSize; i++ )
      {
         for( int j=0; j<_hiddenMatrixSize; j++ )
         {
            PhasePortrait pp = new PhasePortrait( ds[ i ][ j ] );
            pp.setMaximumDimension( maxDimension );
            phase[ i ][ j ] = (double) pp.computeFalseNeighbors( _embeddingLag, N );
            lya[ i ][ j ]= pp.computeLargestLyapunov( (int) phase[ i ][ j ], _embeddingLag );
            ApproximateEntropy ae = new ApproximateEntropy( ds[ i ][ j ], 1, 0.05 );
            appen[ i ][ j ] = ae.getEntropy();
         }
      }

      portrait.setPhaseSpaces( utils.hilbertDownsample( phase, _blockSizeNLR, _finalMatrixSize ) );
      portrait.setLyapunovExponents( utils.hilbertDownsample( lya, _blockSizeNLR, _finalMatrixSize ) );
      portrait.setApproximateEntropies( utils.hilbertDownsample( appen, _blockSizeNLR, _finalMatrixSize ) );

      return portrait;
   }

}
