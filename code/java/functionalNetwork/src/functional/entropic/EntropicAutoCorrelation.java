package functional.entropic;

import java.util.*;
import java.awt.Image;

// import com.sun.jimi.core.*;
import functional.network.*;
import grafix.*;
import common.*;
import math.*;

/**
 * EntropicAutoCorrelation.
 * This class will encompass the operations and data members related
 * to the computation of the auto, or self, functional correlations
 * of the entropic portraits (phase space, Lypunov, approximate entropy)
 * of a given two-dimensional dataset or matrix.
 * The only data that needs to be persisted is the correlations, since
 * there will really be no need for the entropic portrait.
 *
 * Remember that the entropic portrait is composed of the three different
 * statistics, and further that each one of these is a three-dimensional
 * matrix. For these purposes, there is really no order to these matrices.
 * That is to say, simply iterating through them from i to j to k should
 * suffice for the functional correlation data lines--so long as the same
 * ordering is maintained throughout.
 *
 */
public class EntropicAutoCorrelation implements Persistable
{
   // In each respective collection, the positional index also 
   // represents the target variable in the complete set of 
   // independent variables. These are not set by the client.
   private ArrayList< ManyToOneCorrelation > _phaseSpaces  = new ArrayList< ManyToOneCorrelation >();
   private ArrayList< ManyToOneCorrelation > _lyapunovs    = new ArrayList< ManyToOneCorrelation >();
   private ArrayList< ManyToOneCorrelation > _appEntropies = new ArrayList< ManyToOneCorrelation >();

   // These member data variables are used by the EntropicHasher
   // and set by the client.
   private int _blockSize;
   private int _blockSizeNLR;
   private int _hiddenMatrixSize;
   private int _finalMatrixSize;
   private int _embeddingTimeLag         = 80; // hasher.setEmbeddingLag( 80 );
   private int _scaleSize                = 256;

   // This is the halting criteria for each MTOC.
   // So, if the total elapsed running time is X seconds, each 
   // ManyToOneCorrelation will get about:
   // X / ( 3 * _finalMatrixSize * _finalMatrixSize ) seconds.
   // Set by the client.
   private HaltingCriteria _criteria;
   private int             _genomeLength = 15; // correlation.setGenomeLength( 15 );
   private String          _name;

   /**
    *
    */
   public EntropicAutoCorrelation()
   {
      setName( "entropic-auto-correlation" );
   }

   /**
    *
    */
   public EntropicAutoCorrelation( String name )
   {
      setName( name );
   }

   /**
    *
    */
   public DAOBase getDAO()
   {
      DAOBase dao = new EntropicAutoCorrelationDAO();

      if( getName() != null )
      {
         dao.setFileName( getName() + ".ser" );
      }

      return dao;
   }

   /**
    *
    */
   public class EntropicAutoCorrelationDAO extends DAOBase< EntropicAutoCorrelation > { }
   
   /**
    * This method will read in the jpeg image located at the given filename, and
    * will perform the following actions.
    *
    * 1) Convert the jpeg image into a targa image format.
    * 2) Convert the targa image into grayscale.
    * 3) Convert the grayscale image into a two-dimensional matrix of real numbers.
    * 4) Hash the matrix into its entropic portrait.
    * 5) For each entropic portrait component create an MTOC for each variable.
    * 6) Train each MTOC for the re-calculated HaltingCriteria (see formula).
    *
    * @param String -- the location of the jpeg image for which to auto correlate, 
    *                  minus the extension.
    */
   public void autoCorrelate( String filename )
   {
      // 1) Convert the jpeg image into a targa image format.
      /*
      try
      {
         Image i = Jimi.getImage( filename + ".jpg" );
         Image j = i.getScaledInstance( _scaleSize, _scaleSize, i.SCALE_SMOOTH );
         Jimi.putImage( j, filename + ".tga" );
      }
      catch( JimiException e )
      {
         System.out.println( "Exception: " + e );
      }
      */

      // 2) Convert the targa image into grayscale.
      TargaFile t = new TargaFile( filename + ".tga" );

      RGBColorPoint[][] image = t.convertToGrayscale( t.readTargaFile() );
//       image = t.fadeBorder( t.hilbertBlurMask( image ), 0.15 );
      image = t.hilbertBlurMask( image );
      double[][] data = new double[ image.length ][ image[ 0 ].length ];

      // 3) Convert the grayscale image into a two-dimensional matrix of real numbers.
      for( int i=0; i<data.length; i++ )
      {
         for( int j=0; j<data[ 0 ].length; j++ )
         {
            data[ i ][ j ] = image[ i ][ j ].getRed();
         }
      }

      // 4) Hash the matrix into its entropic portrait.
      EntropicHasher hasher = new EntropicHasher( getBlockSize(),        getBlockSizeNLR(), 
                                                  getHiddenMatrixSize(), getFinalMatrixSize() );
      hasher.setEmbeddingLag( getEmbeddingTimeLag() );
      EntropicPortrait portrait = hasher.getEntropicPortrait( data );

      // 5) For each entropic portrait component create an MTOC for each variable.
      createMTOCs( portrait );

      // 6) Train each MTOC for the re-calculated HaltingCriteria (see formula).
      train();
   }

   /**
    * Maps a 3-dimensional matrix into a two-dimensional matrix.
    */
   private double[][] transform( double[][][] input )
   {
      double[][] output = new double[ input.length * input[ 0 ].length ][ input[ 0 ][ 0 ].length ];
      int        pos    = 0;

      for( int i=0; i<input.length; i++ )
      {
         for( int j=0; j<input[ i ].length; j++ )
         {
            for( int k=0; k<input[ i ][ j ].length; k++ )
            {
               output[ pos ][ k ] = input[ i ][ j ][ k ];
            }
            pos++;
         }
      }
      
      return output;
   }

   /**
    * The createMTOCs procedure can be broken down into the following.
    *
    * 1) map the 3-space matrix into a 2-space matrix.
    * 2) for each variable in the 2-space, take it as the target and 
    *    the remaining as the variables
    * 3) instantiate the MTOC with the depedent and independent vars from above.
    */
   private void createMTOCs( EntropicPortrait portrait )
   {
      long l = (long) ( (double) _criteria.getElapsedTimeTolerance() / 
                        (double)( 3 * _finalMatrixSize * _finalMatrixSize ) );
      HaltingCriteria criteria = new HaltingCriteria();
      criteria.setElapsedTimeTolerance( l );

      createPhaseMTOCs( portrait, criteria, ( getName() + "-mtoc-phase" ) );
      createLyapunovMTOCs( portrait, criteria, ( getName() + "-mtoc-lya" ) );
      createApproximateEntropyMTOCs( portrait, criteria, ( getName() + "-mtoc-appen" ) );
   }

   /**
    *
    */
   private void createPhaseMTOCs( EntropicPortrait portrait, HaltingCriteria criteria, String name )
   {
      // First, let's contruct the phase space MTOCs
      double[][] phase = transform( portrait.getPhaseSpaces() );

      for( int i=0; i<phase.length; i++ )
      {
         // Need to instantiate new memory locations since the MTOC is only a shallow copy
         double[][] variables  = new double[ phase.length - 1 ][ phase[ 0 ].length ];
         double[]   target     = new double[ phase[ 0 ].length ];
         int        pos        = 0;

         for( int j=0; j<phase.length; j++ )
         {
            // target case
            if( i == j )
            {
               for( int k=0; k<phase[ j ].length; k++ )
               {
                  target[ k ] = phase[ j ][ k ];
               }
            }
            // variables case
            else
            {
               for( int k=0; k<phase[ j ].length; k++ )
               {
                  variables[ pos ][ k ] = phase[ j ][ k ];
               }
               pos++;
            }
         }

         ManyToOneCorrelation correlation = new ManyToOneCorrelation( name + "-" + i );
         correlation.setHaltingCriteria( criteria );
         correlation.setIndependentData( variables );
         correlation.setDependentData( target );
         correlation.setGenomeLength( getGenomeLength() );
         _phaseSpaces.add( correlation );
      }
   }

   /**
    *
    */
   private void createLyapunovMTOCs( EntropicPortrait portrait, HaltingCriteria criteria, String name )
   {
      // First, let's contruct the lya space MTOCs
      double[][] lya = transform( portrait.getLyapunovExponents() );

      for( int i=0; i<lya.length; i++ )
      {
         // Need to instantiate new memory locations since the MTOC is only a shallow copy
         double[][] variables  = new double[ lya.length - 1 ][ lya[ 0 ].length ];
         double[]   target     = new double[ lya[ 0 ].length ];
         int        pos        = 0;

         for( int j=0; j<lya.length; j++ )
         {
            // target case
            if( i == j )
            {
               for( int k=0; k<lya[ j ].length; k++ )
               {
                  target[ k ] = lya[ j ][ k ];
               }
            }
            // variables case
            else
            {
               for( int k=0; k<lya[ j ].length; k++ )
               {
                  variables[ pos ][ k ] = lya[ j ][ k ];
               }
               pos++;
            }
         }

         ManyToOneCorrelation correlation = new ManyToOneCorrelation( name + "-" + i );
         correlation.setHaltingCriteria( criteria );
         correlation.setIndependentData( variables );
         correlation.setDependentData( target );
         correlation.setGenomeLength( getGenomeLength() );
         _lyapunovs.add( correlation );
      }
   }

   /**
    *
    */
   private void createApproximateEntropyMTOCs( EntropicPortrait portrait, HaltingCriteria criteria, String name )
   {
      // First, let's contruct the approximate entropy space MTOCs
      double[][] appen = transform( portrait.getApproximateEntropies() );

      for( int i=0; i<appen.length; i++ )
      {
         // Need to instantiate new memory locations since the MTOC is only a shallow copy
         double[][] variables  = new double[ appen.length - 1 ][ appen[ 0 ].length ];
         double[]   target     = new double[ appen[ 0 ].length ];
         int        pos        = 0;

         for( int j=0; j<appen.length; j++ )
         {
            // target case
            if( i == j )
            {
               for( int k=0; k<appen[ j ].length; k++ )
               {
                  target[ k ] = appen[ j ][ k ];
               }
            }
            // variables case
            else
            {
               for( int k=0; k<appen[ j ].length; k++ )
               {
                  variables[ pos ][ k ] = appen[ j ][ k ];
               }
               pos++;
            }
         }

         ManyToOneCorrelation correlation = new ManyToOneCorrelation( name + "-" + i );
         correlation.setHaltingCriteria( criteria );
         correlation.setIndependentData( variables );
         correlation.setDependentData( target );
         correlation.setGenomeLength( getGenomeLength() );
         _appEntropies.add( correlation );
      }
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void train( HaltingCriteria criteria )
   {
      _criteria = criteria;
      long l = (long) ( (double) criteria.getElapsedTimeTolerance() / 
                        (double)( 3 * _finalMatrixSize * _finalMatrixSize ) );
      HaltingCriteria c = new HaltingCriteria();
      c.setElapsedTimeTolerance( l );

      for( ManyToOneCorrelation correlation : _phaseSpaces )
      {
         correlation.setHaltingCriteria( c );
      }

      for( ManyToOneCorrelation correlation : _lyapunovs )
      {
         correlation.setHaltingCriteria( c );
      }

      for( ManyToOneCorrelation correlation : _appEntropies )
      {
         correlation.setHaltingCriteria( c );
      }

      train();
   }

   /**
    * 
    */
   public void train()
   {
      System.out.println( "Training PhaseSpace MTOCs" );
      for( ManyToOneCorrelation correlation : _phaseSpaces )
      {
         correlation.train();
      }

      System.out.println( "Training Lyapunov MTOCs" );
      for( ManyToOneCorrelation correlation : _lyapunovs )
      {
         correlation.train();
      }

      System.out.println( "Training AppEntropy MTOCs" );
      for( ManyToOneCorrelation correlation : _appEntropies )
      {
         correlation.train();
      }
   }

   /**
    *
    * @param String
    */
   public void setName( String name )
   {
      _name = name;
   }

   /**
    *
    * @return String
    */
   public String getName()
   {
      return _name;
   }

   /**
    *
    * @param int
    */
   public void setBlockSize( int blockSize )
   {
      _blockSize = blockSize;
   }

   /**
    *
    * @return int
    */
   public int getBlockSize()
   {
      return _blockSize;
   }

   /**
    *
    * @param int
    */
   public void setBlockSizeNLR( int blockSizeNLR )
   {
      _blockSizeNLR = blockSizeNLR;
   }

   /**
    *
    * @return int
    */
   public int getBlockSizeNLR()
   {
      return _blockSizeNLR;
   }

   /**
    *
    * @param int
    */
   public void setHiddenMatrixSize( int hiddenMatrixSize )
   {
      _hiddenMatrixSize = hiddenMatrixSize;
   }

   /**
    *
    * @return int
    */
   public int getHiddenMatrixSize()
   {
      return _hiddenMatrixSize;
   }

   /**
    *
    * @param int
    */
   public void setFinalMatrixSize( int finalMatrixSize )
   {
      _finalMatrixSize = finalMatrixSize;
   }

   /**
    *
    * @return int
    */
   public int getFinalMatrixSize()
   {
      return _finalMatrixSize;
   }

   /**
    *
    * @param int
    */
   public void setEmbeddingTimeLag( int embeddingTimeLag )
   {
      _embeddingTimeLag = embeddingTimeLag;
   }

   /**
    *
    * @return int
    */
   public int getEmbeddingTimeLag()
   {
      return _embeddingTimeLag;
   }

   /**
    *
    * @param int
    */
   public void setGenomeLength( int genomeLength )
   {
      _genomeLength = genomeLength;
   }

   /**
    *
    * @return int
    */
   public int getGenomeLength()
   {
      return _genomeLength;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public ArrayList< ManyToOneCorrelation > getPhaseSpaceMTOCs()
   {
      return _phaseSpaces;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public ArrayList< ManyToOneCorrelation > getLyapunovMTOCs()
   {
      return _lyapunovs;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public ArrayList< ManyToOneCorrelation > getApproximateEntropyMTOCs()
   {
      return _appEntropies;
   }

   /**
    * This is the halting criteria for each MTOC. However, the
    * client will set the total training time.
    * So, if the total elapsed running time is X seconds, each 
    * ManyToOneCorrelation will get about:
    * X / ( 3 * _finalMatrixSize * _finalMatrixSize ) seconds.
    * Since this value is dependent upon the final matrix size
    * this will be recalculated whenever the training begins.
    * 
    * @param HaltingCriteria -- Set by the client.
    */
   public void setHaltingCriteria( HaltingCriteria criteria )
   {
      _criteria = criteria;
   }

   /**
    *
    * @return HaltingCriteria
    */
   public HaltingCriteria getHaltingCriteria()
   {
      return _criteria;
   }

   /**
    * Test method.
    * Here, we see how close these MTOCs predict the actual target or dependent data
    * given the independent variables.
    *
    * @param TYPE
    * @return TYPE
    */
   public void test()
   {
      int c = 0;
      // first, do phase spaces
      for( ManyToOneCorrelation correlation : _phaseSpaces )
      {
         System.out.println( "Phase Space Correlation: " + c++ );
         printCorrelationTest( correlation );
      }

      c = 0;
      // then, do lyapunov
      for( ManyToOneCorrelation correlation : _lyapunovs )
      {
         System.out.println( "Lyapunov Exponent Correlation: " + c++ );
         printCorrelationTest( correlation );
      }

      c = 0;
      // then, do appens
      for( ManyToOneCorrelation correlation : _appEntropies )
      {
         System.out.println( "Approximate Entropy Correlation: " + c++ );
         printCorrelationTest( correlation );
      }
   }

   /**
    *
    */
   private void printCorrelationTest( ManyToOneCorrelation correlation )
   {
      double[][] variables = correlation.getIndependentData();
      double[]   target    = correlation.getDependentDataInverted();
      double[]   input     = new double[ variables.length ];

      for( int i=0; i<variables[ 0 ].length; i++ )
      {
         for( int j=0; j<variables.length; j++ )
         {
            input[ j ] = variables[ j ][ i ];
         }
         System.out.println( target[ i ] + ", " + ( correlation.correlate( input ) )[ 0 ] );
      }
   }

   /**
    * Comparison method.
    * Here, we compare two EntropicAutoCorrelation objects. The idea is the the first
    * object (this) is being compared to the second (target) in the manner that:
    * the target's MTOCs are used to produce a prediction given this' independent data
    * and compared to this' dependent data to find the difference.
    *
    * @param TYPE
    * @return TYPE
    */
   public double compare( EntropicAutoCorrelation target )
   {
      double difference = 0;

      for( int i=0; i<_phaseSpaces.size(); i++ )
      {
         difference += compare( _phaseSpaces.get( i ), target.getPhaseSpaceMTOCs().get( i ) );
      }
      for( int i=0; i<_lyapunovs.size(); i++ )
      {
         difference += compare( _lyapunovs.get( i ), target.getLyapunovMTOCs().get( i ) );
      }
      for( int i=0; i<_appEntropies.size(); i++ )
      {
         difference += compare( _appEntropies.get( i ), target.getApproximateEntropyMTOCs().get( i ) );
      }
      double n = (double) ( ( target.getPhaseSpaceMTOCs().get( 0 ).getDependentData() )[ 0 ].length * _phaseSpaces.size() );
      difference /= ( 3d * n );

      return difference;
   }

   /**
    *
    */
   private double compare( ManyToOneCorrelation mtoc1, ManyToOneCorrelation mtoc2 )
   {
      double[][] variables  = mtoc1.getIndependentData();
      double[]   target     = mtoc1.getDependentDataInverted();
      double[]   input      = new double[ variables.length ];
      double     difference = 0;

      for( int i=0; i<variables[ 0 ].length; i++ )
      {
         for( int j=0; j<variables.length; j++ )
         {
            input[ j ] = variables[ j ][ i ];
         }
         difference += Math.abs( target[ i ] - ( mtoc2.correlate( input ) )[ 0 ] );
      }

      return difference;
   }

   /**
    *
    * @param int
    */
   public void setScaleSize( int scaleSize )
   {
      _scaleSize = scaleSize;
   }

   /**
    *
    * @return int
    */
   public int getScaleSize()
   {
      return _scaleSize;
   }

   /**
    * For testing purposes.
    *
    * Syntax:
    *  0       1                        2               3            4             5               6              7         8            9
    * create <name> <JPEG filename without ext> <running time> <genome length> <block size> <block size NLR> <hidden size> <final size> <lag> <scale size>
    * train <name> [ <running time> ]
    * test <name>
    * compare <mtoc name 1> <mtoc name 2> # tests how closely mtoc2 predicts mtoc1's data
    *
    * @param String[]
    */
   public static void main( String[] args )
   {
      if( args.length > 1 )
      {
         if( "create".equals( args[ 0 ] ) )
         {
            EntropicAutoCorrelation auto     = new EntropicAutoCorrelation( args[ 1 ] );
            HaltingCriteria         criteria = new HaltingCriteria();
            criteria.setElapsedTimeTolerance( (new MathUtilities()).getTime( args[ 3 ] ) );
            auto.setHaltingCriteria( criteria );

            auto.setGenomeLength(     Integer.parseInt( args[ 4  ] ) );
            auto.setBlockSize(        Integer.parseInt( args[ 5  ] ) );
            auto.setBlockSizeNLR(     Integer.parseInt( args[ 6  ] ) );
            auto.setHiddenMatrixSize( Integer.parseInt( args[ 7  ] ) );
            auto.setFinalMatrixSize(  Integer.parseInt( args[ 8  ] ) );
            auto.setEmbeddingTimeLag( Integer.parseInt( args[ 9  ] ) );
            if( args.length == 11 )
            {
               auto.setScaleSize( Integer.parseInt( args[ 10 ] ) );
            }

            // now perform the actual correllation:
            auto.autoCorrelate( args[ 2 ] );

            // finally, persist the data:
            auto.getDAO().serialize( auto );
         }
         else if( "train".equals( args[ 0 ] ) )
         {
            EntropicAutoCorrelationDAO dao = (EntropicAutoCorrelationDAO) ( new EntropicAutoCorrelation( args[ 1 ] ) ).getDAO();
            EntropicAutoCorrelation auto   = dao.deserialize();
            if( args.length > 2 )
            {
               HaltingCriteria criteria = new HaltingCriteria();
               criteria.setElapsedTimeTolerance( ( new MathUtilities() ).getTime( args[ 1 ] ) );
               auto.train( criteria );
            }
            else
            {
               auto.train();
            }

            // finally, persist the data:
            auto.getDAO().serialize( auto );
         }
         else if( "test".equals( args[ 0 ] ) )
         {
            EntropicAutoCorrelationDAO dao  = (EntropicAutoCorrelationDAO) ( new EntropicAutoCorrelation( args[ 1 ] ) ).getDAO();
            EntropicAutoCorrelation    auto = dao.deserialize();
            auto.test();
         }
         else if( "compare".equals( args[ 0 ] ) )
         {
            EntropicAutoCorrelationDAO dao   = (EntropicAutoCorrelationDAO) ( new EntropicAutoCorrelation( args[ 1 ] ) ).getDAO();
            EntropicAutoCorrelation    mtoc1 = dao.deserialize();

            dao                              = (EntropicAutoCorrelationDAO) ( new EntropicAutoCorrelation( args[ 2 ] ) ).getDAO();
            EntropicAutoCorrelation    mtoc2 = dao.deserialize();

            System.out.println( "Comparison of " + args[ 1 ] + " to " + args[ 2 ] + ", difference: " + mtoc1.compare( mtoc2 ) );
         }
      }
      else
      {
         System.out.println( "" );
         System.out.println( "Usage:" );
         System.out.println( "create <name> <JPEG filename without ext> <running time> <genome length> <block size> <block size NLR> <hidden size> <final size> <lag> [<scale size>]" );
         System.out.println( "train <name> [ <running time> ]" );
         System.out.println( "test <name>" );
         System.out.println( "compare <mtoc name 1> <mtoc name 2> # tests how closely mtoc2 predicts mtoc1's data" );
         System.out.println( "" );
      }
   }

}
