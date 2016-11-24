package genetic;

// import stock.*;
import math.*;
import math.graph.*;
import common.*;
import grafix.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * This class will keep track of environmental objects that
 * the population can change and interact with.
 * <strike>This class will be implemented as a Singleton and will 
 * be thread-safe.</strike>
 * This class is no longer a singleton since in the general case, there
 * may be many concurrent instances of the genetic algorithm all solving 
 * the same problem (Individual), but each using a slightly different 
 * version of the environment data.
 */
public class EnvironmentCache implements Serializable
{

   // The singleton instance
//    private final EnvironmentCache _myInstance = new EnvironmentCache();

   // The stock collection
//    private final StockManager _stockManager = new StockManager();
//    private ArrayList<ArrayList<Double>> _streams;
   private double[][]            _streams;
   private double[]              _targetStream;
   private double[]              _weightValues;
   private int[][]               _adjacencyMatrix;
   private ArrayList< Vertex >   _vertexList;
   private ArrayList< Double >   _powerSet;
   private ArrayList< Short >    _sortOrder;
   private FunctionalCorrelation _correlate;
   private int                   _genomeLength = 0;
   private RGBColorPoint[][]     _imageTarget;

   /**
    * Default private constructor.
    */
   public EnvironmentCache()
   {
   }

   /**
    * Returns the instance.
   public EnvironmentCache getInstance()
   {
      return _myInstance;
   }
    */

   /**
    * Sets the list of data streams.
   public synchronized void setDataStreams( ArrayList< ArrayList< Double > > streams )
   {
      _streams = streams;
   }
    */

   /**
    * Returns a list of all the stock names in the database.
   public synchronized ArrayList<String> getStockNames()
   {
      // no need to cache these names since the StockCollection does it for us.
      return _stockManager.getStockNames();
   }
    */

   /**
    * Returns the StockElement given the security name.
   public synchronized StockElement getStockElement( String name )
   {
      return _stockManager.getStockElement( name );
   }
    */

   /**
    *
    * @param double[] the weight values used by FunctionalCorrelation objects.
    */
   public void setWeights( double[] weights )
   {
      _weightValues = weights;
   }

   /**
    *
    * @return double[] the weight values used by FunctionalCorrelation objects.
    */
   public double[] getWeights()
   {
      return _weightValues;
   }

   /**
    *
    */
   public void setDataStreams( double[][] streams )
   {
      _streams = streams;
   }

   /**
    * Returns a list of all the stock names in the database.
    */
   public synchronized double[][] getDataStreams()
   {
      if( _streams == null )
      {
         common.FileReader reader = new common.FileReader( "variables.dat", "," );
         String[] words = reader.getArrayOfWords();
         int numberOfVariables = Integer.parseInt( words[ 0 ] );
         int numberOfDataPoints = (numberOfVariables == 1) ? (words.length - 1) : (words.length) / numberOfVariables;
         int count = 1;

         _streams = new double[ numberOfVariables ][ numberOfDataPoints ];
         double[] temp = null;

         for( int i=0; i<numberOfVariables; i++ )
         {
            temp = new double[ numberOfDataPoints ];
            for( int j=0; j<numberOfDataPoints; j++ )
            {
               try
               {
                  temp[ j ] = Double.parseDouble( words[ count++ ] );
               }
               catch( NumberFormatException e )
               {
                  System.err.println( "Caught exception parsing data: " + e );
               }
            }
//             _streams.add( (new MathUtilities()).normalize( temp, Math.PI ) );
            _streams[ i ] = (new MathUtilities()).normalize( temp, 1 );
         }

         // This section is only temporary it can be commented out for speed up, though it may not hurt
         // to leave this in to make sure the entropy levels aren't too high for the dependent function.
         System.out.println( "number of variables: " + numberOfVariables );
         System.out.println( "number of data points: " + numberOfDataPoints );
//          double a, b;
// 
//          for( int i=0; i<_streams.length; i++ )
//          {
//             ApproximateEntropy theAppEn = new ApproximateEntropy( _streams[ i ], 1, 0.05 );
//             a = theAppEn.getEntropy();
//             System.out.println( "The (1, 0.05) approximate entropy for " + i + " is: " + a );
//             theAppEn = new ApproximateEntropy( _streams[ i ], 2, 0.05 );
//             System.out.println( "The (2, 0.05) approximate entropy for " + i + " is: " + theAppEn.getEntropy() );
//             theAppEn = new ApproximateEntropy( _streams[ i ], 5, 0.05 );
//             System.out.println( "The (5, 0.05) approximate entropy for " + i + " is: " + theAppEn.getEntropy() );
//             theAppEn = new ApproximateEntropy( _streams[ i ], 10, 0.05 );
//             b = theAppEn.getEntropy();
//             System.out.println( "The (10, 0.05) approximate entropy for " + i + " is: " + b );
//             System.out.println( "The approximate entropy ratio for " + i + " is: " + (a/b) + "\n" );
//          }
      }

      return _streams;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public synchronized double[][] getRandomDataStreams()
   {
      if( _streams == null )
      {
         int numberOfVariables = 10;
         int numberOfInstances = 20;
         int numberOfConcurrent = 5;
         double[] temp = null;
         _streams = new double[ numberOfVariables * numberOfConcurrent ][ numberOfInstances ];
         // The datastreams are interpreted such that the rows are the variables
         // and the columns represent the data point instances.
         for( int i=0; i<numberOfVariables; i++ )
         {
//             System.out.println( "\nvariable: " + i );
            temp = new double[ numberOfInstances + numberOfConcurrent ];
            for( int k=0; k<(numberOfInstances + numberOfConcurrent); k++ )
            {
               temp[ k ] = (new MathUtilities()).random();
            }
            double[] con = new double[ numberOfConcurrent ];
            for( int k=0; k<numberOfInstances; k++ )
            {
//                System.out.println( "\ninstance: " + k );
               for( int j=0; j<numberOfConcurrent; j++ )
               {
                  con[ j ] = temp[ k + j ];
               }
               con = (new MathUtilities()).normalize( con, 1 );
               for( int j=0; j<numberOfConcurrent; j++ )
               {
                  _streams[ i  + j ][ k ] = con[ j ];
//                   System.out.print( _streams[ i + j ][ k ] + " " );
               }
            }
         }
         _streams = doubleArray( _streams );
         _streams = doubleArray( _streams );
         _streams = doubleArray( _streams );
         _streams = doubleArray( _streams );
         System.out.println( "size is now: " + _streams.length + " " + _streams[ 0 ].length );
      }

      return _streams;
   }

   /**
    *
    */
   private double[][] doubleArray( double[][] a )
   {
      System.out.println( "size is: " + a.length + " " + a[ 0 ].length );
      double[][] b = new double[ 2 * a.length ][ a[ 0 ].length ];
      for( int i=0; i<a.length; i++ )
      {
         for( int j=0; j<a[i].length; j++ )
         {
            b[ i ][ j ] = a[ i ][ j ];
            b[ 2 * i ][ j ] = a[ i ][ j ];
         }
      }
      return b;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public synchronized double[] getRandomTargetStream()
   {
      if( _targetStream == null )
      {
         int numberOfInstances = 20;
         int window = 10;
         int offset = 5;
         StatUtilities s = new StatUtilities();

         double[] hold = new double[ numberOfInstances + offset ];
         double[] random = new double[ numberOfInstances + window + offset ];
         for( int j=0; j<numberOfInstances + window + offset; j++ )
         {
            random[ j ] = (new MathUtilities()).random();
         }
         for( int i=0; i<numberOfInstances + offset; i++ )
         {
            double[] temp = new double[ window ];
            for( int j=0; j<window; j++ )
            {
               temp[ j ] = random[ i + j ];
            }
            s.calculateStats( temp );
            hold[ i ] = s.getMean();
         }
         for( int j=0; j<numberOfInstances; j++ )
         {
            hold[ j ] =  hold[ j + offset ] - hold[ j ];
         }
         _targetStream = new double[ numberOfInstances ];
         for( int i=0; i<_targetStream.length; i++ )
         {
            _targetStream[ i ] = hold[ i ];
         }
         _targetStream = (new MathUtilities()).normalize( _targetStream, 1 );
         for( int i=0; i<_targetStream.length; i++ )
         {
            System.out.println( _targetStream[ i ] );
         }
      }

      return _targetStream;
   }

   /**
    * Returns a list of all the stock names in the database.
   public synchronized ArrayList< ArrayList< Double > > getDataStreams()
   {
      if( _streams == null )
      {
         common.FileReader reader = new FileReader( "variables.dat", "," );
         String[] words = reader.getArrayOfWords();
         int numberOfVariables = Integer.parseInt( words[ 0 ] );
         int numberOfDataPoints = (numberOfVariables == 1) ? (words.length - 1) : (words.length) / numberOfVariables;
         int count = 1;

         _streams = new ArrayList<ArrayList<Double>>();
         ArrayList<Double> temp = null;

         for( int i=0; i<numberOfVariables; i++ )
         {
            temp = new ArrayList< Double >();
            for( int j=0; j<numberOfDataPoints; j++ )
            {
               try
               {
                  temp.add( Double.parseDouble( words[ count++ ] ) );
               }
               catch( NumberFormatException e )
               {
                  System.err.println( "Caught exception parsing data: " + e );
               }
            }
//             _streams.add( (new MathUtilities()).normalize( temp, Math.PI ) );
            _streams.add( (new MathUtilities()).normalize( temp, 1 ) );
         }

         // This section is only tempoary it can be commented out for speed up, though it may not hurt
         // to leave this in to make sure the entropy levels aren't too high for the dependent function.
         System.out.println( "number of variables: " + numberOfVariables );
         System.out.println( "number of data points: " + numberOfDataPoints );
         double a, b;
         for( int i=0; i<_streams.size(); i++ )
         {
            ArrayList<Double> variable = _streams.get( i );
//             variable = (new MathUtilities()).normalize( variable, Math.PI );
            variable = (new MathUtilities()).normalize( variable, 1 );
            double[] theArray = new double[ variable.size() ];
            for( int j=0; j<temp.size(); j++ )
            {
               theArray[ j ] = variable.get( j );  
            }
            ApproximateEntropy theAppEn = new ApproximateEntropy( theArray, 1, 0.05 );
            a = theAppEn.getEntropy();
            System.out.println( "The (1, 0.05) approximate entropy for " + i + " is: " + a );
            theAppEn = new ApproximateEntropy( theArray, 2, 0.05 );
            System.out.println( "The (2, 0.05) approximate entropy for " + i + " is: " + theAppEn.getEntropy() );
            theAppEn = new ApproximateEntropy( theArray, 5, 0.05 );
            System.out.println( "The (5, 0.05) approximate entropy for " + i + " is: " + theAppEn.getEntropy() );
            theAppEn = new ApproximateEntropy( theArray, 10, 0.05 );
            b = theAppEn.getEntropy();
            System.out.println( "The (10, 0.05) approximate entropy for " + i + " is: " + b );
            System.out.println( "The approximate entropy ratio for " + i + " is: " + (a/b) + "\n" );
         }
      }

      return _streams;
   }
   */

   /**
    *
    */
   public synchronized void setTargetStream( double[] target )
   {
      _targetStream = target;
   }

   /**
    * Returns a list of all the stock names in the database.
    */
   public synchronized double[] getTargetStream()
   {
      if( _targetStream == null )
      {
         common.FileReader reader = new common.FileReader( "target.dat", "," );
         String[] words = reader.getArrayOfWords();
         _targetStream = new double[ words.length ];

         for( int i=0; i<words.length; i++ )
         {
            try
            {
               _targetStream[ i ] = ( Double.parseDouble( words[ i ] ) );
            }
            catch( NumberFormatException e )
            {
               System.err.println( "Caught exception parsing data: " + e );
            }
         }
         
         // This section is only tempoary it can be commented out for speed up, though it may not hurt
         // to leave this in to make sure the entropy levels aren't too high for the dependent function.
//          double[] theArray = new double[ _targetStream.size() ];
//          double a, b;
//          for( int j=0; j<_targetStream.size(); j++ )
//          {
//             theArray[ j ] = _targetStream.get( j );  
//          }
//          ApproximateEntropy theAppEn = new ApproximateEntropy( theArray, 1, 0.05 );
//          a = theAppEn.getEntropy();
//          System.out.println( "The (1, 0.05) approximate entropy for target variable is: " + a );
//          theAppEn = new ApproximateEntropy( theArray, 2, 0.05 );
//          System.out.println( "The (2, 0.05) approximate entropy for target variable is: " + theAppEn.getEntropy() );
//          theAppEn = new ApproximateEntropy( theArray, 5, 0.05 );
//          System.out.println( "The (5, 0.05) approximate entropy for target variable is: " + theAppEn.getEntropy() );
//          theAppEn = new ApproximateEntropy( theArray, 10, 0.05 );
//          b = theAppEn.getEntropy();
//          System.out.println( "The (10, 0.05) approximate entropy for target variable is: " + b );
//          System.out.println( "The approximate entropy ratio for target variable is: " + (a/b) + "\n" );
      }

      return _targetStream;
   }

   /**
    * Returns a list of all the stock names in the database.
    */
   public synchronized ArrayList<Double> getPowerSet()
   {
      if( _powerSet == null )
      {
         _powerSet = new ArrayList<Double>();
         _powerSet.add(0.004722463819316736 );
         _powerSet.add(0.22850670427198183 );
         _powerSet.add(0.22198984811892408 );
         _powerSet.add(0.0484601555674824 );
         _powerSet.add(0.869130455489564 );
         _powerSet.add(0.09147158684634982 );
         _powerSet.add(0.05613826664870114 );
         _powerSet.add(0.6010224564977841 );
         _powerSet.add(0.621987042449111 );
         _powerSet.add(0.26872507092483855 );
         _powerSet.add(0.64574966232395 );
         _powerSet.add(0.30418661171718964 );
         _powerSet.add(0.1569590176793949 );
         _powerSet.add(0.050327248349840814 );
         _powerSet.add(0.8084064702679509 );
         _powerSet.add(0.7408992700007442 );
         _powerSet.add(0.1346645755595941 );
         _powerSet.add(0.7528857504485833 );
         _powerSet.add(0.9917047595861581 );
         _powerSet.add(0.1484018421099782 );
         _powerSet.add(0.6419165672573829 );
         _powerSet.add(0.802228110857784 );
         _powerSet.add(0.5244189363171256 );
         _powerSet.add(0.34789472919202624 );
         _powerSet.add(0.16319336155577746 );
         _powerSet.add(0.9518254311971763 );
         _powerSet.add(0.5443551614375873 );
         _powerSet.add(0.9022775916990086 );
         _powerSet.add(0.9522687962479005 );
         _powerSet.add(0.45518897081597776 );
         _powerSet.add(0.666791888445769 );
         _powerSet.add(0.33353532124655705 );
         _powerSet.add(0.7877780569397076 );
         _powerSet.add(0.6895703774474279 );
         _powerSet.add(0.9096118616355584 );
         _powerSet.add(0.6127620561540169 );
         _powerSet.add(0.40561322426078705 );
         _powerSet.add(0.807969160375656 );
         _powerSet.add(0.874724041784659 );
         _powerSet.add(0.1068535263971705 );
         _powerSet.add(0.8726783718977849 );
         _powerSet.add(0.013864958347204914 );
         _powerSet.add(0.2702338494477107 );
         _powerSet.add(0.5854563651439687 );
         _powerSet.add(0.07293246029888267 );
         _powerSet.add(0.524774495372194 );
         _powerSet.add(0.5186116686750014 );
         _powerSet.add(0.438887955675639 );
         _powerSet.add(0.27677563020686424 );
         _powerSet.add(0.32335572629399134 );
         _powerSet.add(0.06276553298359455 );
         _powerSet.add(0.5485640798421335 );
         _powerSet.add(0.6033677730031528 );
         _powerSet.add(0.20455604967713348 );
         _powerSet.add(0.11437340943010987 );
         _powerSet.add(0.03291227990584722 );
         _powerSet.add(0.39365780566965325 );
         _powerSet.add(0.25456766904666206 );
         _powerSet.add(0.31181718256090096 );
         _powerSet.add(0.5574000401562108 );
         _powerSet.add(0.7067224729237652 );
         _powerSet.add(0.43203553022645746 );
         _powerSet.add(0.6858162648055549 );
         _powerSet.add(0.8997760144477206 );
         _powerSet.add(0.02698399850214328 );
         _powerSet.add(0.36674562200139893 );
         _powerSet.add(0.8648418329324284 );
         _powerSet.add(0.5390637234692548 );
         _powerSet.add(0.2992485285845261 );
         _powerSet.add(0.01392788647332932 );
         _powerSet.add(0.7353273684449244 );
         _powerSet.add(0.4138830807715087 );
         _powerSet.add(0.8715416738983011 );
         _powerSet.add(0.3326334925738691 );
         _powerSet.add(0.06155195994483309 );
         _powerSet.add(0.3765607351422591 );
         _powerSet.add(0.3387980995477686 );
         _powerSet.add(0.7397811037643828 );
         _powerSet.add(0.3412789076319883 );
         _powerSet.add(0.9332340209909649 );
         _powerSet.add(0.21169063776473884 );
         _powerSet.add(0.8117291346317107 );
         _powerSet.add(0.08296318539385439 );
         _powerSet.add(0.410739913587975 );
         _powerSet.add(0.41834800411908235 );
         _powerSet.add(0.6540272298333017 );
         _powerSet.add(0.8204732216158168 );
         _powerSet.add(0.0415074542131042 );
         _powerSet.add(0.3519652629285971 );
         _powerSet.add(0.3322327105270878 );
         _powerSet.add(0.05513495659762424 );
         _powerSet.add(0.5550467585774267 );
         _powerSet.add(0.5341201325026304 );
         _powerSet.add(0.43607222884220453 );
         _powerSet.add(0.34992355908050765 );
         _powerSet.add(0.6888167363300757 );
         _powerSet.add(0.6483542479034936 );
         _powerSet.add(0.052485021419288236 );
         _powerSet.add(0.4532569470914014 );
         _powerSet.add(0.15479398834200642 );
         _powerSet.add(0.40159898799511484 );
         _powerSet.add(0.44056258272002313 );
         _powerSet.add(0.6848726468143729 );
         _powerSet.add(0.45596885641717577 );
         _powerSet.add(0.3483813944978089 );
         _powerSet.add(0.4242716056835084 );
         _powerSet.add(0.6475245372554 );
         _powerSet.add(0.6683562424473067 );
         _powerSet.add(0.36125770679482494 );
         _powerSet.add(0.18564767797811121 );
         _powerSet.add(0.5546845359820007 );
         _powerSet.add(0.13701383277170676 );
         _powerSet.add(0.18056633203155914 );
         _powerSet.add(0.43901953973400887 );
         _powerSet.add(0.4565635946260329 );
         _powerSet.add(0.47322226968431225 );
         _powerSet.add(0.8457032660420937 );
         _powerSet.add(0.920494158047501 );
         _powerSet.add(0.032980017708431064 );
         _powerSet.add(0.0037200639195582585 );
         _powerSet.add(0.66225638797995 );
         _powerSet.add(0.08011366834365752 );
         _powerSet.add(0.11291737177068095 );
         _powerSet.add(0.478142875650173 );
         _powerSet.add(0.3414276179984723 );
         _powerSet.add(0.2771153555431779 );
         _powerSet.add(0.7926628317454193 );
         _powerSet.add(0.9645339863131078 );
         _powerSet.add(0.041415271205855086 );
         _powerSet.add(0.7772036211560902 );
         _powerSet.add(0.7106439015496044 );
         _powerSet.add(0.8701388668434482 );
         _powerSet.add(0.7149560924970834 );
         _powerSet.add(0.6973926044218476 );
         _powerSet.add(0.2436838769392229 );
         _powerSet.add(0.856485739947513 );
         _powerSet.add(0.5916858371572132 );
         _powerSet.add(0.5207749796045742 );
         _powerSet.add(0.49526567434479885 );
         _powerSet.add(0.8682958276989041 );
         _powerSet.add(0.029342544940780035 );
         _powerSet.add(0.6897618149781096 );
         _powerSet.add(0.34076928299370723 );
         _powerSet.add(0.18137007335407374 );
         _powerSet.add(0.03795985188655637 );
         _powerSet.add(0.6689619969801504 );
         _powerSet.add(0.5472546584442605 );
         _powerSet.add(0.8371822055132998 );
         _powerSet.add(0.40651682966282054 );
         _powerSet.add(0.7011601960194808 );
         _powerSet.add(0.17519325296765675 );
         _powerSet.add(0.8688332785818665 );
         _powerSet.add(0.39624873887408885 );
         _powerSet.add(0.24026434725018886 );
         _powerSet.add(0.005918136234805971 );

         short pos = 0;
         HashMap< Double, Short > map = new HashMap< Double, Short >();
         for( Double key : _powerSet )
         {
            map.put( key, new Short( pos++ ) );
         } 

         Collections.sort( _powerSet );
         _sortOrder = new ArrayList< Short >();

         for( Double key : _powerSet )
         {
//             System.out.println( map.get( key ) );
            _sortOrder.add( map.get( key ) );
         } 
      }

      return _powerSet;
   }

   /**
    *
    */
   public synchronized ArrayList< Short > getSortOrder()
   {
      return _sortOrder;
   }

   /**
    * Returns a list of all the stock names in the database.
    */
   public synchronized double[][] getDysormVariables()
   {
      if( _streams == null )
      {
         common.FileReader reader = new common.FileReader( "dysorm-variables.dat", " " );
         String[] words = reader.getArrayOfWords();
         int      numberOfVariables = Integer.parseInt( words[ 0 ] );
         int      numberOfDataPoints = (numberOfVariables == 1) ? (words.length - 1) : 
                     (words.length) / numberOfVariables;
         _streams = new double[ numberOfVariables ][ numberOfDataPoints ];
         double[]   t = null;
         int        count = 1;

//          System.out.println( "The number of variables is: " + numberOfVariables );
//          System.out.println( "The number of data points is: " + numberOfDataPoints );

         for( int i=0; i<numberOfVariables; i++ )
         {
            t = new double[ numberOfDataPoints ];
            for( int j=0; j<numberOfDataPoints; j++ )
            {
               try
               {
                  t[ j ] = Double.parseDouble( words[ count++ ] );
               }
               catch( NumberFormatException e )
               {
                  System.err.println( "Caught exception parsing data: " + e );
               }
            }
            _streams[ i ] = (new MathUtilities()).normalize( t, 1 );
//             _streams[ i ] = (new MathUtilities()).normalize( t, Math.PI );
//             _streams[ i ] = t;
         }
      }

      return _streams;
   }

   /**
    * 
    */
   public synchronized double[][] getTargetImage()
   {
      if( _streams == null )
      {
         TargaFile imageFile = new TargaFile( "/home/brandon/raw.tga" );
         RGBColorPoint[][] image = imageFile.readTargaFile();
         _streams = new double[ image.length ][ image[ 0 ].length ];
         for( int i=0; i<_streams.length; i++ )
         {
            for( int j=0; j<_streams[ i ].length; j++ )
            {
               _streams[ i ][ j ] = ( (HLSColorPoint) image[ i ][ j ].getHLSColorPoint() ).getL();
            }
         }
      }

      return _streams;
   }

   /**
    *
    * @param FunctionalCorrelation -- the evolved or trained FunctionalCorrelation object.
    */
   public void setCorrelate( FunctionalCorrelation correlate )
   {
      _correlate = correlate;
   }

   /**
    *
    * @return FunctionalCorrelation -- the evolved or trained FunctionalCorrelation object.
    */
   public FunctionalCorrelation getCorrelate()
   {
      return _correlate;
   }

   /**
    *
    * @param int -- The length of the new breeds.
    */
   public void setGenomeLength( int length )
   {
      _genomeLength = length;
   }

   /**
    *
    * @return int -- The length of the new breeds.
    */
   public int getGenomeLength()
   {
      return _genomeLength;
   }

   /**
    *
    * @return double[ i ][ i ]
    */
   public int[][] getAdjacencyMatrix()
   {
      if( _adjacencyMatrix == null )
      {
         _adjacencyMatrix = new int[ getGenomeLength() ][ getGenomeLength() ];

         MathUtilities util = new MathUtilities();
         for( int i=0; i<getGenomeLength(); i++ )
         {
            for( int j=0; j<getGenomeLength(); j++ )
            {
               _adjacencyMatrix[ i ][ j ] = Integer.MAX_VALUE;
               if( i != j && util.random() <= 0.3 )
               {
                  _adjacencyMatrix[ i ][ j ] = 1;
               }
            }
         }
//          _adjacencyMatrix[ 0 ][ 0 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 0 ][ 1 ] = 1;
//          _adjacencyMatrix[ 0 ][ 2 ] = 1;
//          _adjacencyMatrix[ 0 ][ 3 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 0 ][ 4 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 0 ][ 5 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 1 ][ 0 ] = 1;
//          _adjacencyMatrix[ 1 ][ 1 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 1 ][ 2 ] = 1;
//          _adjacencyMatrix[ 1 ][ 3 ] = 1;
//          _adjacencyMatrix[ 1 ][ 4 ] = 1;
//          _adjacencyMatrix[ 1 ][ 5 ] = 1;
//          _adjacencyMatrix[ 2 ][ 0 ] = 1;
//          _adjacencyMatrix[ 2 ][ 1 ] = 1;
//          _adjacencyMatrix[ 2 ][ 2 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 2 ][ 3 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 2 ][ 4 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 2 ][ 5 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 3 ][ 0 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 3 ][ 1 ] = 1;
//          _adjacencyMatrix[ 3 ][ 2 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 3 ][ 3 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 3 ][ 4 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 3 ][ 5 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 4 ][ 0 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 4 ][ 1 ] = 1;
//          _adjacencyMatrix[ 4 ][ 2 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 4 ][ 3 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 4 ][ 4 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 4 ][ 5 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 5 ][ 0 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 5 ][ 1 ] = 1;
//          _adjacencyMatrix[ 5 ][ 2 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 5 ][ 3 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 5 ][ 4 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 5 ][ 5 ] = Integer.MAX_VALUE;
         
//          _adjacencyMatrix[ 0 ][ 0 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 0 ][ 1 ] = 1;
//          _adjacencyMatrix[ 0 ][ 2 ] = 1;
//          _adjacencyMatrix[ 0 ][ 3 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 0 ][ 4 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 0 ][ 5 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 1 ][ 0 ] = 1;
//          _adjacencyMatrix[ 1 ][ 1 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 1 ][ 2 ] = 1;
//          _adjacencyMatrix[ 1 ][ 3 ] = 1;
//          _adjacencyMatrix[ 1 ][ 4 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 1 ][ 5 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 2 ][ 0 ] = 1;
//          _adjacencyMatrix[ 2 ][ 1 ] = 1;
//          _adjacencyMatrix[ 2 ][ 2 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 2 ][ 3 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 2 ][ 4 ] = 1;
//          _adjacencyMatrix[ 2 ][ 5 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 3 ][ 0 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 3 ][ 1 ] = 1;
//          _adjacencyMatrix[ 3 ][ 2 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 3 ][ 3 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 3 ][ 4 ] = 1;
//          _adjacencyMatrix[ 3 ][ 5 ] = 1;
//          _adjacencyMatrix[ 4 ][ 0 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 4 ][ 1 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 4 ][ 2 ] = 1;
//          _adjacencyMatrix[ 4 ][ 3 ] = 1;
//          _adjacencyMatrix[ 4 ][ 4 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 4 ][ 5 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 5 ][ 0 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 5 ][ 1 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 5 ][ 2 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 5 ][ 3 ] = 1;
//          _adjacencyMatrix[ 5 ][ 4 ] = Integer.MAX_VALUE;
//          _adjacencyMatrix[ 5 ][ 5 ] = Integer.MAX_VALUE;
      }
      return _adjacencyMatrix;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public ArrayList< Vertex > getSortedVertexList()
   {
      if( _vertexList == null )
      {
         GraphUtilities util = new GraphUtilities();
         _vertexList = util.getSortedVertexList( util.getLaplacianMatrix( getAdjacencyMatrix() ) );
         Collections.reverse( _vertexList );
      }
      return _vertexList;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public RGBColorPoint[][] getImageTarget()
   {
      if( _imageTarget == null )
      {
         GrafixUtilities gu = new GrafixUtilities();
         _imageTarget       = gu.convertToGrayscale( gu.getRGBData2( "/home/brandon/dist/Apiatan.jpg" ) );
      }
      return _imageTarget;
   }

}
