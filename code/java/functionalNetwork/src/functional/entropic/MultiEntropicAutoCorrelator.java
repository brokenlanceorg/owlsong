package functional.entropic;

import java.util.*;

import functional.entropic.EntropicAutoCorrelation.*;
import common.*;
import math.*;

/**
 * MultiEntropicAutoCorrelator.
 * This class encapsulates many or multiple EntropicAutoCorrelation objects.
 * The main idea is that several images are emitted from a source and which
 * classes can be grouped together representing a certain class or action
 * that can be taken given an object from that group of similar images.
 * Need a way to map these auto correlation objects to these actions or 
 * class names.
 */
public class MultiEntropicAutoCorrelator implements Persistable
{

   private ArrayList< EntropicAutoCorrelation > _correlations;
   private String                               _name;

   /**
    *
    */
   public MultiEntropicAutoCorrelator()
   {
      setName( "multi-entropic-auto-correlator" );
      _correlations = new ArrayList< EntropicAutoCorrelation >();
   }

   /**
    *
    */
   public MultiEntropicAutoCorrelator( String name )
   {
      setName( name );
      _correlations = new ArrayList< EntropicAutoCorrelation >();
   }

   /**
    *
    */
   public void setCorrelations( ArrayList< EntropicAutoCorrelation > c )
   {
      _correlations = c;
   }

   /**
    *
    */
   public ArrayList< EntropicAutoCorrelation > getCorrelations()
   {
      return _correlations;
   }

   /**
    *
    */
   public DAOBase getDAO()
   {
      DAOBase dao = new MultiEntropicAutoCorrelatorDAO();

      if( getName() != null )
      {
         dao.setFileName( getName() + ".ser" );
      }

      return dao;
   }

   /**
    *
    */
   public class MultiEntropicAutoCorrelatorDAO extends DAOBase< MultiEntropicAutoCorrelator > { }
   
   /**
    *  0       1                        2               3            4             5               6              7         8            9
    * create <name> <JPEG filename without ext> <running time> <genome length> <block size> <block size NLR> <hidden size> <final size> <lag>
    *
    * @param TYPE
    * @return TYPE
    */
   public void addEntropicAutoCorrelation( String filename,  String time,    int length,    int blockSize, 
                                           int blockSizeNLR, int hiddenSize, int finalSize, int timeLag )
   {
      EntropicAutoCorrelation auto     = new EntropicAutoCorrelation( getName() + "-" + _correlations.size() + 1 );
      HaltingCriteria         criteria = new HaltingCriteria();
      criteria.setElapsedTimeTolerance( (new MathUtilities()).getTime( time ) );

      auto.setHaltingCriteria(  criteria );
      auto.setGenomeLength(     length );
      auto.setBlockSize(        blockSize );
      auto.setBlockSizeNLR(     blockSizeNLR );
      auto.setHiddenMatrixSize( hiddenSize );
      auto.setFinalMatrixSize(  finalSize );
      auto.setEmbeddingTimeLag( timeLag );

      _correlations.add( auto );

      // now perform the actual correllation:
      auto.autoCorrelate( filename );
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void addEntropicAutoCorrelation( EntropicAutoCorrelation auto )
   {
      for( EntropicAutoCorrelation correlation : _correlations )
      {
         if( correlation.getName().equals( auto.getName() ) )
         {
            System.out.println( "EntropicAutoCorrelation: " + auto.getName() + " already exists." );
            return;
         }
      }
      _correlations.add( auto );
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void train( HaltingCriteria criteria )
   {
      for( EntropicAutoCorrelation correlation : _correlations )
      {
         correlation.setHaltingCriteria( criteria );
      }
      train();
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void train()
   {
      for( EntropicAutoCorrelation correlation : _correlations )
      {
         System.out.println( "Training: " + correlation.getName() );
         correlation.train();
      }
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void test()
   {
      for( EntropicAutoCorrelation correlation : _correlations )
      {
         System.out.println( "Test of: " + correlation.getName() );
         correlation.test();
      }
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public int getGenomeLength()
   {
      return _correlations.get( 0 ).getGenomeLength();
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public int getBlockSize()
   {
      return _correlations.get( 0 ).getBlockSize();
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public int getBlockSizeNLR()
   {
      return _correlations.get( 0 ).getBlockSizeNLR();
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public int getHiddenMatrixSize()
   {
      return _correlations.get( 0 ).getHiddenMatrixSize();
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public int getFinalMatrixSize()
   {
      return _correlations.get( 0 ).getFinalMatrixSize();
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public int getEmbeddingTimeLag()
   {
      return _correlations.get( 0 ).getEmbeddingTimeLag();
   }

   /**
    *
    * @param String -- the filename of the JPEG image without the extension.
    * @return int -- the index of the closest EntropicAutoCorrelation.
    */
   public int interpret( String filename )
   {
      int    closest     = -1;
      String closestName = "";
      double min         = Double.MAX_VALUE;

      EntropicAutoCorrelation auto     = new EntropicAutoCorrelation( filename );
      HaltingCriteria         criteria = new HaltingCriteria();
      criteria.setElapsedTimeTolerance( 0 );

      auto.setHaltingCriteria(  criteria );
      auto.setGenomeLength(     getGenomeLength() );
      auto.setBlockSize(        getBlockSize() );
      auto.setBlockSizeNLR(     getBlockSizeNLR() );
      auto.setHiddenMatrixSize( getHiddenMatrixSize() );
      auto.setFinalMatrixSize(  getFinalMatrixSize() );
      auto.setEmbeddingTimeLag( getEmbeddingTimeLag() );

      // now perform the actual correllation:
      auto.autoCorrelate( filename );

      int c = 0;
      for( EntropicAutoCorrelation correlation : _correlations )
      {
         double t = auto.compare( correlation );
         if( t < min )
         {
            min         = t;
            closest     = c;
            closestName = correlation.getName();
         }
         c++;
      }

      System.out.println( "The distance is: " + min + " to " + closestName );

      return closest;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void list()
   {
      int c = 0;
      for( EntropicAutoCorrelation correlation : _correlations )
      {
         System.out.println( "Index " + c++ + " EntropicAutoCorrelation: " + correlation.getName() );
      }
   }

   /**
    * For testing purposes.
    *
    * Syntax:
    *  0       1                        2               3            4             5               6              7         8            9
    * create <name> <JPEG filename without ext> <running time> <genome length> <block size> <block size NLR> <hidden size> <final size> <lag> [<scale size>]
    * train <name> [ <running time> ]
    * test <name>
    * add <name> <JPEG filename without ext> <running time> <genome length> <block size> <block size NLR> <hidden size> <final size> <lag>
    * add <name> <entropic auto correlation name>
    * interpret <name> <JPEG filename without ext>
    *
    * @param String[]
    */
   public static void main( String[] args )
   {
      if( args.length > 1 )
      {
         if( "create".equals( args[ 0 ] ) )
         {
            MultiEntropicAutoCorrelator multi = new MultiEntropicAutoCorrelator( args[ 1 ] );
            if( args.length > 2 )
            {
               multi.addEntropicAutoCorrelation( args[ 2 ], args[ 3 ], 
                                                 Integer.parseInt( args[ 4 ] ), Integer.parseInt( args[ 5 ] ),
                                                 Integer.parseInt( args[ 6 ] ), Integer.parseInt( args[ 7 ] ),
                                                 Integer.parseInt( args[ 8 ] ), Integer.parseInt( args[ 9 ] ) );
            }

            // finally, persist the data:
            multi.getDAO().serialize( multi );
         }
         else if( "train".equals( args[ 0 ] ) )
         {
            MultiEntropicAutoCorrelatorDAO dao   = (MultiEntropicAutoCorrelatorDAO) ( new MultiEntropicAutoCorrelator( args[ 1 ] ) ).getDAO();
            MultiEntropicAutoCorrelator    multi = dao.deserialize();
            if( args.length > 2 )
            {
               HaltingCriteria criteria = new HaltingCriteria();
               criteria.setElapsedTimeTolerance( ( new MathUtilities() ).getTime( args[ 1 ] ) );
               multi.train( criteria );
            }
            else
            {
               multi.train();
            }

            // finally, persist the data:
            multi.getDAO().serialize( multi );
         }
         else if( "test".equals( args[ 0 ] ) )
         {
            MultiEntropicAutoCorrelatorDAO dao   = (MultiEntropicAutoCorrelatorDAO) ( new MultiEntropicAutoCorrelator( args[ 1 ] ) ).getDAO();
            MultiEntropicAutoCorrelator    multi = dao.deserialize();
            multi.test();
         }
         else if( "add".equals( args[ 0 ] ) )
         {
            MultiEntropicAutoCorrelatorDAO dao   = (MultiEntropicAutoCorrelatorDAO) ( new MultiEntropicAutoCorrelator( args[ 1 ] ) ).getDAO();
            MultiEntropicAutoCorrelator    multi = dao.deserialize();
            if( args.length > 3 )
            {
               multi.addEntropicAutoCorrelation( args[ 2 ], args[ 3 ], 
                                                 Integer.parseInt( args[ 4 ] ), Integer.parseInt( args[ 5 ] ),
                                                 Integer.parseInt( args[ 6 ] ), Integer.parseInt( args[ 7 ] ),
                                                 Integer.parseInt( args[ 8 ] ), Integer.parseInt( args[ 9 ] ) );
            }
            else
            {
               EntropicAutoCorrelationDAO edao = (EntropicAutoCorrelationDAO) ( new EntropicAutoCorrelation( args[ 2 ] ) ).getDAO();
               EntropicAutoCorrelation    auto = edao.deserialize();
               multi.addEntropicAutoCorrelation( auto );
            }

            // finally, persist the data:
            multi.getDAO().serialize( multi );
         }
         else if( "interpret".equals( args[ 0 ] ) )
         {
            MultiEntropicAutoCorrelatorDAO dao   = (MultiEntropicAutoCorrelatorDAO) ( new MultiEntropicAutoCorrelator( args[ 1 ] ) ).getDAO();
            MultiEntropicAutoCorrelator    multi = dao.deserialize();
            System.out.println( "The closest match is: " + multi.interpret( args[ 2 ] ) );
         }
         else if( "list".equals( args[ 0 ] ) )
         {
            MultiEntropicAutoCorrelatorDAO dao   = (MultiEntropicAutoCorrelatorDAO) ( new MultiEntropicAutoCorrelator( args[ 1 ] ) ).getDAO();
            MultiEntropicAutoCorrelator    multi = dao.deserialize();
            multi.list();
         }
      }
      else
      {
         System.out.println( "" );
         System.out.println( "Usage:" );
         System.out.println( "create <name> [ <JPEG filename without ext> <running time> <genome length> <block size> <block size NLR> <hidden size> <final size> <lag> ] [<scale size>]" );
         System.out.println( "train <name> [ <running time> ]" );
         System.out.println( "test <name>" );
         System.out.println( "add <name> <JPEG filename without ext> <running time> <genome length> <block size> <block size NLR> <hidden size> <final size> <lag> [<scale size>]" );
         System.out.println( "add <name> <entropic auto correlation name>" );
         System.out.println( "interpret <name> <JPEG filename without ext>" );
         System.out.println( "list <name>" );
         System.out.println( "" );
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

}
