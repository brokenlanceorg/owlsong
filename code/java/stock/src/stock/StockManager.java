package stock;

import java.util.regex.*;
import java.util.*;
import java.math.*;
import java.io.*;
import java.lang.reflect.*;
import common.*;
import common.WebPageReader.*;
import math.*;
import functional.network.*;
import openkinect.forest.*;
import openkinect.forest.DecisionForest.*;

/**
 *
 *  New Procedure:
 *   1) Get Russell for 1 month
 *   2) Find all that are above 70 percent
 *   3) For each, download year's worth
 *   4) Find all that are above 60 percent
 *   5) Simulate the intersection.
 *
 */
public class StockManager
{
 
   private StockCollection       _theStockCollection       = null;
   private StockCollectionLoader _theStockCollectionLoader = null;
   private StockElement          _Q                        = null;
   private ArrayList             _prevConfidences          = null;
   private double                _TotalWorth               = 0;
   private Random                _randomGenerator          = new Random();
   private MathUtilities         _mathUtil                 = new MathUtilities();
   private HashMap< String, StockElement > _stockMap       = new HashMap< String, StockElement >();

//   static 
//   {
//      System.loadLibrary( "StockManager" );
//   } // end static load

   /**
    *
    */
   public StockManager()
   {
      _theStockCollection = new StockCollection();
   } // end default constructor

   /**
    *
    */
   public StockManager( LoadEnum theEnum )
   {
      if( theEnum.equals( LoadEnum.LOAD_LAST ) )
      {
         System.out.println( "Loading LAST only..." );
         _theStockCollection = new StockCollection( true );
         _theStockCollectionLoader = new StockCollectionLoader( false );
         _theStockCollectionLoader.loadUpdate( _theStockCollection );
      } // end if
      else if( theEnum.equals( LoadEnum.LOAD_ALL ) )
      {
         System.out.println( "Loading ALL only..." );
         _theStockCollection = new StockCollection( false );
         _theStockCollectionLoader = new StockCollectionLoader();
         _theStockCollectionLoader.loadDividends( _theStockCollection );
      } // end else
      else if( theEnum.equals( LoadEnum.LOAD_ALL_YEAR ) )
      {
         //System.out.println( "Loading ALL YEAR only..." );
         _theStockCollection = new StockCollection( false );
         _theStockCollectionLoader = new StockCollectionLoader();
         _theStockCollectionLoader.loadYear( _theStockCollection );
      } // end else
   } // end constructor

   /**
    *
    */
   public void outputCalibration()
   {
      Collection theColl = _theStockCollection.getStockElementCollection();
      Iterator theIter = theColl.iterator();
      StringBuffer str = null;
      BufferedWriter output = null;
      
      try {
         output = new BufferedWriter( new FileWriter( "calibration.rank" ) );

         while( theIter.hasNext() )
         {
            StockElement theElem = (StockElement)theIter.next();

/*
            if( theElem.getVolume() < 1000000 )
            //if( theElem.getVolume() < 500000 )
            {
               continue;
            } // end if low volume
*/
            str = new StringBuffer( theElem.getName() );
            str.append( ", " ).append( theElem.getVolume() );
/*
            str.append( " " ).append( theElem.getWorth() );
            str.append( " " ).append( theElem.getLastBuy() );
            str.append( " " ).append( theElem.getDaysAbove() );
*/
            output.write( str.toString() );
            output.newLine();
         } // end while
         output.flush();
         output.close();
      } catch( IOException e ) {}

   } // end outputCalibration

   /**
    *
    */
   public void outputVolumeCalibration( int window )
   {
      ArrayList< String > names =_theStockCollection.getStockNames();

      for( String name : names )
      {
         StockElement stock = getStockElement( name );
         ArrayList< Double > volumes = stock.getVolumes();
         int start = volumes.size() - window - 1;
         ArrayList< Double > v = new ArrayList< Double >();
         if( start < 0 )
         {
            continue;
         }

         for( int i=0; i<window; i++ )
         {
            v.add( volumes.get( start + i ) );
         }
         StatUtilities s = new StatUtilities();
         s.calculateStats( v );
         System.out.println( name + ", median volume:, " + s.getMedian() );
      }
   }

   /**
    *
    */
   public void outputRankings()
   {
      Collection VarColl = _theStockCollection.getSortedVarianceCollection();
      Iterator   theIter = VarColl.iterator();
      int rank = 500;

      while( theIter.hasNext() )
      {
         StockElement theElem = (StockElement)theIter.next();
         theElem.setVarianceRank( rank-- );
      } // end while

      Collection StabColl = _theStockCollection.getSortedStabilityCollection();
      theIter = StabColl.iterator();
      rank = 500;

      while( theIter.hasNext() )
      {
         StockElement theElem = (StockElement)theIter.next();
         theElem.setStabilityRank( rank-- );
      } // end while

      Collection theColl = _theStockCollection.getStockElementCollection();
      theIter = theColl.iterator();
      StringBuffer str = null;
      BufferedWriter output = null;
      
      try {
         output = new BufferedWriter( new FileWriter( "output.rank" ) );

         while( theIter.hasNext() )
         {
            StockElement theElem = (StockElement)theIter.next();
            str = new StringBuffer( theElem.getName() );
            str.append( " " ).append( theElem.getVarianceRank() ).append( " " );
            str.append( theElem.getStabilityRank() );
            output.write( str.toString() );
            output.newLine();
         } // end while
         output.flush();
         output.close();
      } catch( IOException e ) {}

   } // end outputRankings

   /**
    *
    */
   public void outputResults()
   {
      _theStockCollectionLoader.loadConfidence( _theStockCollection );
      Collection theColl = _theStockCollection.getSortedConfidenceCollection();
      Iterator theIter = theColl.iterator();
      StringBuffer str = null;
      BufferedWriter output = null;
      _prevConfidences = new ArrayList();
      int i = _theStockCollection.getSortedSize();

      try {
         output = new BufferedWriter( new FileWriter( "results.dat" ) );

         while( theIter.hasNext() )
         {
            StockElement theElem = (StockElement)theIter.next();
            str = new StringBuffer( theElem.getName() );
            _prevConfidences.add( theElem.getName() );
            _prevConfidences.add( new Integer( i-- ) );
            str.append( " " ).append( theElem.getConfidence() );
            output.write( str.toString() );
            output.newLine();
         } // end while
         output.flush();
         output.close();
      } catch( IOException e ) {}

   } // end outputResults

   /**
    *
    */
   public StockElement getWatchedStockElement( String name )
   {
      Pattern p = Pattern.compile( "([^<>]+).*" );
      Matcher m = p.matcher( name );
      String realName = "";
      if( m.matches() )
      {
         realName = m.group( 1 );
      }
      StockElement stock = getAmendedStockElement( realName );
      stock.setName( name );
      _theStockCollection.loadWatcherData( stock );
      return stock;
   }

   /**
    *
    */
   public void updateWatchedStockElement( StockElement stock )
   {
      _theStockCollection.updateWatchedStockElement( stock );
   }

   /**
    *
    */
   public void insertWatchedStockElement( StockElement stock, double delta )
   {
      _theStockCollection.insertWatchedStockElement( stock, delta );
   }

   /**
    *
    */
   public void clearWatchedStockElements()
   {
      _theStockCollection.clearWatchedStockElements();
   }

   /**
    *
    */
   public StockElement getStockElement( String name )
   {
      return _theStockCollection.getStockElement( name );
   } // end getStockElement

   /**
    *
    */
   public StockElement getAmendedStockElement( String name )
   {
      StockElement anElem = _theStockCollection.getStockElement( name );
      WebPageReader page = new WebPageReader();
      anElem.addCloseReal( page.getLastTrade( name.toLowerCase() ) );
      anElem.addOpenReal( page.getOpen() );
      anElem.addHighReal( page.getHigh() );
      anElem.addLowReal( page.getLow() );

      return anElem;
   }

   /**
    *
    */
   public void saveData()
   {
      //_theStockCollection.saveData();
//      Collection theColl = _theStockCollection.getStockElementCollection();
//      Collection theColl = _prevConfidences.values();
//      Iterator theIter = theColl.iterator();
      StringBuffer str = null;
      BufferedWriter output = null;
      
      try {
         output = new BufferedWriter( new FileWriter( "PrevConfidence.dat" ) );

/*
         while( theIter.hasNext() )
         {
            StockElement theElem = (StockElement)theIter.next();
            str = new StringBuffer( theElem.getName() );
//            str.append( " " ).append( theElem.getConfidence() );
            str.append( " " ).append( theElem.getPrevConfidence() );
            output.write( str.toString() );
            output.newLine();
         } // end while
*/
	   for( int i=0; i<_prevConfidences.size(); i+=2 )
         {
            str = new StringBuffer( _prevConfidences.get( i ).toString() );
            str.append( " " ).append( _prevConfidences.get( i + 1 ).toString() );
            output.write( str.toString() );
            output.newLine();
         } // end for

         output.flush();
         output.close();
      } catch( IOException e ) {}

   } // end getStockElement

   /**
    *
    */
   public void outputConfidenceResults()
   {
      outputRankings();
      //StockConfidenceWorker theConf = new StockConfidenceWorker();
      //theConf.outputConfidences();
      outputResults();
   } // end outputConfidenceResults
 
   /**
    *
    */
   public Collection getSortedVarianceCollection()
   {
      return _theStockCollection.getSortedVarianceCollection();
   } // end getSortedVarianceCollection

   /**
    *
    */
   public Collection getSortedStabilityCollection()
   {
      return _theStockCollection.getSortedStabilityCollection();
   } // end getSortedStabilityCollection

   /**
    *
    */
   public Collection getBestSortedCollection()
   {
      Collection theColl = _theStockCollection.getBestSortedCollection();
      Iterator theIter = theColl.iterator();

      StringBuffer str = null;
      BufferedWriter output = null;
      
      try {
         output = new BufferedWriter( new FileWriter( "BestPicks.csv" ) );

         while( theIter.hasNext() )
         {
            StockElement theElem = (StockElement)theIter.next();
            str = new StringBuffer( theElem.getName() );
            //str.append( "," ).append( theElem.getLastVarianceSMA() );
            //str.append( "," ).append( theElem.getLastStabilitySMA() );
            str.append( "," ).append( theElem.getConfidence() );
            output.write( str.toString() );
            output.newLine();
         } // end while

         output.flush();
         output.close();
      } catch( IOException e ) 
      {
          System.err.println( "Error writing best picks: " + e );
      } // end catch

      return theColl;
   } // end getBestSortedCollection

   /**
    *
    */
   public Collection getSortedDensityCollection()
   {
      return _theStockCollection.getSortedDensityCollection();
   } // end getSortedDensityColleciton

   /**
    *
    */
   public Collection getSortedDensityCollection2()
   {
      return _theStockCollection.getSortedDensityCollection2();
   } // end getSortedDensityColleciton

   /**
    *
    */
   public void outputDensityCollection()
   {
      Collection theColl = _theStockCollection.getSortedDensityCollection();
      Iterator theIter = theColl.iterator();

      StringBuffer str = null;
      BufferedWriter output = null;
      
      try {
         output = new BufferedWriter( new FileWriter( "Densities.csv" ) );

         while( theIter.hasNext() )
         {
            StockElement theElem = (StockElement)theIter.next();
            str = new StringBuffer( theElem.getName() );
            str.append( "," ).append( theElem.getDensity() );
            str.append( "," ).append( theElem.getVarianceMass() );
            str.append( "," ).append( theElem.getStabilityMass() );
            str.append( "," ).append( theElem.getStabilityRank() );
            str.append( "," ).append( theElem.getDaysAbove() );
            output.write( str.toString() );
            output.newLine();
         } // end while

         output.flush();
         output.close();
      } catch( IOException e ) 
      {
          System.err.println( "Error writing densities: " + e );
      } // end catch

   } // end outputDensityCollection

   /**
    *
    */
   public Collection getLargestVarianceCollection()
   {
      return _theStockCollection.getLargestVarianceCollection();
   } // end getLargestDifferences

   /**
    *
    */
   public void outputLargestVariances()
   {
      Collection   theColl = _theStockCollection.getLargestVarianceCollection();
      Iterator     theIter = theColl.iterator();
      StockElement theElem = null;
      ArrayList    theList = null;

      StringBuffer str = null;
      BufferedWriter output = null;
      
      try {
         output = new BufferedWriter( new FileWriter( "LargestVariance.csv" ) );

         while( theIter.hasNext() )
         {
            theElem = (StockElement)theIter.next();
            str = new StringBuffer( theElem.getName() );
/*
            theList = theElem.getVariance();

            str.append( "," ).append( theElem.getLastVarianceSMA() );
            str.append( "," ).append( theElem.getLastStabilitySMA() );
*/
            output.write( str.toString() );
            output.newLine();
         } // end while

         output.flush();
         output.close();
      } catch( IOException e ) 
      {
          System.out.println( "Error writing best picks: " + e );
      } // end catch

   } // outputLargestDifferences

   /**
    *
    */
   public void outputDays()
   {
      Collection   theColl = _theStockCollection.getStockElementCollection();
      Iterator     theIter = theColl.iterator();
      StockElement theElem = null;
      ArrayList    theList = null;

      StringBuffer str = null;
      BufferedWriter output = null;
      
      try {
         output = new BufferedWriter( new FileWriter( "days.dat" ) );

         while( theIter.hasNext() )
         {
/*
            theElem = (StockElement)theIter.next();
            str = new StringBuffer( theElem.getName() );
            theList = theElem.getListOfDaysAbove();

            for( int i=0; i<theList.size(); i++ )
            {
               if( i == 0 && theList.size() >= 3 )
               {
                  continue;
               } // end if
               str.append( " " ).append( theList.get( i ).toString() );
            } // end for loop
*/
            output.write( str.toString() );
            output.newLine();
         } // end while

         output.flush();
         output.close();
      } catch( IOException e ) 
      {
          System.err.println( "Error writing days: " + e );
      } // end catch
   } // end outputDays

   /**
    * This method will calculate two Linear Models of the data. The Linear Model
    * simply fits a line to the given stock data, then the stocks are sorted
    * based on the slope of the fited line. Those with the flatest slope are sorted
    * to the bottom.
    * This first data set of the first half of the data, while the second set is the
    * latter half of the data set. The first set is just a gauge to test, while the
    * second half is the set that will actually be used to enter into the trade.
    * The data set for each security is one year (so each half of the data set is 6 months).
    * The securities used in the calculations are the QQQ securities.
    * The QQQ members can be found at: http://quotes.nasdaq.com/quote.dll?page=nasdaq100 
    * 
    */
   public void calculateCValues()
   {
      Collection   theColl = _theStockCollection.getStockElementCollection();
      StockElement theElem = null;
      Iterator theIter = theColl.iterator();

      for( int i=0; i<2; i++ )
      {
         System.out.println( "#################################################### i is: " + i );
         ArrayList sorted = new ArrayList();
         theIter = theColl.iterator();
         while( theIter.hasNext() )
         {
            theElem = (StockElement)theIter.next();
            //theElem.calculateLinearValues( i, stepSize );
            if( i == 0 )
               theElem.calculateCValues();
            else
               theElem.calculateLValues();
            theElem.setCompareToSMAvalue();
            double value = theElem.getThreshold();
//System.out.println( "a value is: " + value );
            if( value >= 0 && value < 0.0001 )
            {
//System.out.println( "a value is: zero" );
               sorted.add( theElem );
            }
         } // end while

         // to print out the results:
         Collections.sort( sorted );
         theIter = sorted.iterator();
         while( theIter.hasNext() )
         {
            theElem = (StockElement)theIter.next();
            System.out.println( theElem );
         } // end while

      } // end for loop


/*
      theIter = theColl.iterator();

      System.out.println( "" );

      while( theIter.hasNext() )
      {
         theElem = (StockElement)theIter.next();
         theElem.calculateLValues();
         System.out.println( theElem );
      }



*/
/*
      Collection theSortedList = theList.values();
      Iterator iter = theSortedList.iterator();
      int thresh = theSortedList.size() - 10;
      int count = 0;
      int half = 0;

      while( iter.hasNext() )
      {
         theElem = (StockElement)iter.next();
         if( count++ > thresh )
         {
            half = (int)theElem.getSize() / 2;
            System.out.println( theElem.getName() + " " + theElem.getClose( half ) + " " + theElem.getLastClose() );
         }
      }

      // first, let's get the QQQ element:
      StockElement theQElem = getStockElement( "QQQ" );
      theQElem.calculateCValues();

      if( theQElem == null )
      {
         System.out.println( "Couldn't find the Q element..." );
         theQElem.calculateCValues();
      }

      for( int i=1; i<theQElem.getSize(); i++ )
      {
         Iterator theIter = theColl.iterator();

         double current = 0;
         double previous = 0;
         double ratio = 0;
         double mean = 0;
         int count = 0;

         while( theIter.hasNext() )
         {
            theElem = (StockElement)theIter.next();

            if( theElem.getName().equals( "QQQ" ) ||
                (!theElem.getName().equals( "QCOM" ) &&
                 !theElem.getName().equals( "AMGN" ) &&
                 !theElem.getName().equals( "AAPL" ) &&
                 !theElem.getName().equals( "SHLD" ) &&
                 !theElem.getName().equals( "GILD" ) &&
                 !theElem.getName().equals( "CSCO" ) &&
                 !theElem.getName().equals( "SYMC" ) &&
                 !theElem.getName().equals( "RIMM" ) &&
                 !theElem.getName().equals( "BRCM" ) &&
                 !theElem.getName().equals( "SEBL" )  )
               )
            {
               continue;
            }

            current = theElem.getClose( i );
            previous = theElem.getClose( i - 1 );
   
            if( previous != 0 )
            {
               ratio = current / previous;

               mean += ratio;
               count++;
            }

            previous = current;
         } // end while loop

         if( count != 0 )
         {
            mean /= (double)count;
            System.out.println( mean );
         }
      } // end while loop

      System.out.println( "QQQ" );

      double current = 0;
      double previous = 0;
      double ratio = 0;

      for( int i=0; i<theQElem.getSize(); i++ )
      {
         current = theQElem.getClose( i );
   
         if( previous != 0 )
         {
            ratio = current / previous;
            System.out.println( ratio );
         }

         previous = current;
      } // end for loop
*/

   } // end calculateCValues

   /**
    *
    */
   public double getTotalWorth()
   {
      return _TotalWorth;
   } // end getTotalWorth

   /**
    *
    */
   public Collection getActiveCollection()
   {
      return _theStockCollection.getActiveCollection();
   } // end getTotalWorth

   /**
    *
    */
   public Collection getSortedConfidenceCollection()
   {
      return _theStockCollection.getSortedConfidenceCollection();
   } // end getSortedConfidenceCollection

   /**
    *
    */
   public Collection getSortedSMACollection()
   {
      return _theStockCollection.getSortedSMACollection();
   } // end getSortedSMACollection

   /**
    *
    */
   public Collection getSortedLossCollection()
   {
      return _theStockCollection.getSortedLossCollection();
   } // end getSortedLossCollection

   /**
    *
    */
   public Collection getSortedWorthCollection()
   {
      return _theStockCollection.getSortedWorthCollection();
   } // end getSortedWorthCollection

   /**
    *
    */
   private native double getConfidence( double sma, double worth, double tlr );

   /**
    * For Testing Purposes only....
    */
   public static void main( String[] args )
   {
      StockManager aMan = new StockManager();

/*
      if( args.length > 0 && args[0].equals( "1" ) )
         aMan = new StockManager( LoadEnum.LOAD_ALL );
      else
         aMan = new StockManager( LoadEnum.LOAD_LAST );

      aMan.outputRankings();
      //StockConfidenceWorker theConf = new StockConfidenceWorker();
      //theConf.outputConfidences();
      // Fuzzy logic engine runs..
      aMan.outputResults();
*/

      aMan.calculateProbabilities();
      //aMan.saveData();
      //if( args.length > 1 )
         //System.out.println( aMan.getStockElement( args[1] ) );
   } // end main

   /**
    *
    */
   public void calculateProbabilities()
   {
/*
      common.FileReader theIntervalFile = new common.FileReader( "Intervals.dat" );
      String[] theIntervals = theIntervalFile.getArrayOfWords();
      double[][] theIntervalData = new double[ (int)(theIntervals.length / 2) ][];
      int pos = 0;

      for( int i=0; i<theIntervalData.length; i++ )
      {
         theIntervalData[i]    = new double[ 2 ];
         theIntervalData[i][0] = Double.parseDouble( theIntervals[pos++] );
         theIntervalData[i][1] = Double.parseDouble( theIntervals[pos++] );
      } // end initialization
*/

      Collection   theColl = _theStockCollection.getStockElementCollection();
      Iterator     theIter = theColl.iterator();
      StockElement theElem = null;
      double[]     thePoints = null;
      double[]     temp = null;

      while( theIter.hasNext() )
      {
         theElem = (StockElement)theIter.next();
         //theElem.calculateProbabilities( theIntervalData );
         if( theElem.calculateStatistics() == 0 )
         {
            System.out.println( theElem.toDataString() );
         }
/*
         temp = theElem.getMACD();

         if( thePoints == null )
         {
            thePoints = new double[ temp.length ];
         }

         for( int i=0; i<temp.length; i++ )
         {
            thePoints[ i ] += temp[ i ];
         }
*/

      }

/*

      for( int i=0; i<temp.length; i++ )
      {
         System.out.println( thePoints[ i ] );
      }
*/

   } // end calculateProbabilities

   /**
    *
    */
   private double findIt( String stock, String[] words )
   {
      double worth = -1;
      for( int i=0; i<words.length; i+=10 )
      {
         if( words[i].equals( stock ) )
         {
            worth = Double.parseDouble( words[i+8] );
            return worth;
         } // end if
      } // end for

      return worth;
   } // end findIt

   /**
    *
    */
   public void compareDataFiles()
   {
      common.FileReader fileOne   = new common.FileReader( "datafiles.dat" );
      String[] theFiles = fileOne.getArrayOfWords();
      _prevConfidences = new ArrayList(); // contains maps of data

      for( int i=0; i<theFiles.length; i++ )
      {
         readData( theFiles[i] );
      } // end for

      outputWorthRankings();
/*
      common.FileReader fileOne   = new common.FileReader( "12_20_2002.dat" );
      common.FileReader fileTwo   = new common.FileReader( "7_20_2002.dat" );
      common.FileReader fileThree = new common.FileReader( "2_20_2002.dat" );
      common.FileReader fileFour  = new common.FileReader( "9_20_2001.dat" );
      common.FileReader fileFive  = new common.FileReader( "4_20_2001.dat" );

      String[] wordsOne = fileOne.getArrayOfWords();
      String[] wordsTwo = fileTwo.getArrayOfWords();
      String[] wordsThree = fileThree.getArrayOfWords();
      String[] wordsFour = fileFour.getArrayOfWords();
      String[] wordsFive = fileFive.getArrayOfWords();
      double theWorth = 0;
      double meanWorth = 0;
      double[] theWorths = new double[ 5 ];

      // position 8 is the worth:
      for( int i=0; i<wordsOne.length; i+=10 )
      {
         theWorth = findIt( wordsOne[i], wordsOne );
         meanWorth = 0;
         if( theWorth < 0 )
         {
            continue;
         }
         meanWorth += theWorth;
         theWorths[0] = theWorth;
         theWorth = findIt( wordsOne[i], wordsTwo );
         if( theWorth < 0 )
         {
            continue;
         }
         meanWorth += theWorth;
         theWorths[1] = theWorth;
         theWorth = findIt( wordsOne[i], wordsThree );
         if( theWorth < 0 )
         {
            continue;
         }
         meanWorth += theWorth;
         theWorths[2] = theWorth;
         theWorth = findIt( wordsOne[i], wordsFour );
         if( theWorth < 0 )
         {
            continue;
         }
         meanWorth += theWorth;
         theWorths[3] = theWorth;
         theWorth = findIt( wordsOne[i], wordsFive );
         if( theWorth < 0 )
         {
            continue;
         }
         meanWorth += theWorth;
         theWorths[4] = theWorth;

         // if we get here, then we're good to go.
         System.out.println( wordsOne[i] + " " + theWorths[0] + " " + theWorths[1] + " " + theWorths[2] + " " + theWorths[3] + " " + theWorths[4] + " " + (meanWorth / 5) );

      } // end for 
*/
   } // end compareDataFiles

   /**
    *
    */
   private void readData( String fileName )
   {
      common.FileReader fileOne   = new common.FileReader( fileName );
      TreeMap theSortedWorths = new TreeMap();
      String[] wordsOne = fileOne.getArrayOfWords();

      for( int i=0; i<wordsOne.length; i+=6 )
      {
         theSortedWorths.put( new Double( wordsOne[i+5] ), wordsOne[i] );
      } // end for
      _prevConfidences.add( theSortedWorths.values() );
   } // end readData

   /**
    *
    */
   private void outputWorthRankings()
   {
      ArrayList theLists = new ArrayList();

      // reverse the list:
      for( int i=0; i<_prevConfidences.size(); i++ )
      {
         Collection theColl = ((Collection)_prevConfidences.get(i));
         Iterator theIter = theColl.iterator();
         ArrayList innerList = new ArrayList( theColl.size() );

         while( theIter.hasNext() )
         {
            String name = (String)theIter.next();
            innerList.add( 0, name );
         }

         theLists.add( innerList );
      } // end for


       ArrayList innerList = (ArrayList)theLists.get(0);

       for( int j=0; j<innerList.size(); j++ )
       {
          // name we want to find in other lists:
          String name = (String)innerList.get(j);
          StringBuffer theDataString = new StringBuffer( name + " " + j );
          int totalDiff = 0;
          int lastPos = j;
          int position = 0;
 
          for( int i=1; i<theLists.size(); i++ )
          {
             ArrayList dataList = (ArrayList)theLists.get(i);
             position = dataList.lastIndexOf( name );
             if( position == -1 )
             {
                position = 500;
             }
             totalDiff += (position - lastPos);
             theDataString.append( " " );
             theDataString.append( position );
             lastPos = position;
          } // end inner for

          theDataString.append( " " ).append( totalDiff );

          System.out.println( theDataString );
       } // end for
   

   } // end outputRankings

   /**
    *  Simulation is essentially the following:
    *
    * 1) Break up the days into groups of a certain size (20).
    * 2) Starting with the second group and for each group:
    *    3) Calculate the statistics for the previous group
    *    4) For each day in current group:
    *    5) Find all actives for that day
    *    6) Find most probable active for that day
    *    7) If probability is above threshold, then:
    *       8) Trade that active that day and sum value
    *
    *  New Procedure
    *
    */
   public void simulateSBD()
   {
      Collection theColl = _theStockCollection.getStockElementCollection();
      StockElement theElem = null;
      Iterator     theIter = theColl.iterator();
      theElem = (StockElement)theIter.next();

      // 1) break up days:
      int numberOfDays = theElem.getSize();
      int groupSize = 20;
      int numberOfGroups = (int)(numberOfDays / groupSize);
      int start = 0;
      int end = 0;
      int tradingDays = 0;
  
      double worth = 0;

      System.out.println( "num groups: " + numberOfGroups );
      System.out.println( "num days: " + numberOfDays );

      // 2) starting with second group and for each:
      for( int i=1; i<numberOfGroups; i++ )
      {
         theColl = _theStockCollection.getStockElementCollection();
         theIter = theColl.iterator();
         theElem = null;
         start = (i-1)*groupSize;
         end = (i-1)*groupSize + groupSize;
   
         // 3) calculate stats for all stocks
         while( theIter.hasNext() )
         {
            theElem = (StockElement)theIter.next();
            if( theElem.calculateStatistics( start, end ) == 0 )
            {
               //System.out.println( theElem.toDataString() );
            }
         } // end while

         start += groupSize;
         end += groupSize;

         // 4) cycle through all days in this group:
         for( int j=start; j<end; j++ )
         {
            theIter = theColl.iterator();
            theElem = null;
            ArrayList actives = new ArrayList();
            StockElement bestElement = null;

            // 5) find all actives on this day:
            while( theIter.hasNext() )
            {
               // may want to weed out incomplete data sets:
               theElem = (StockElement)theIter.next();
               if( theElem.isActive2( j ) && theElem.isInitialized() )
               {
                  actives.add( theElem );
               }
            } // end while

            double largest = -20000000;
            // 6) find most probable active:
            for( int k=0; k<actives.size(); k++ )
            {
               theElem = (StockElement)actives.get( k );
               // may want to include total occurences also:
               if( theElem.getStochasticEMA() > largest && theElem.isInitialized() )
               {
                  largest = theElem.getStochasticEMA(); 
                  bestElement = theElem;
               } // end if
            } // end for

            // 7) If probability if above threshold
            if( largest >= 0.6 )
            {
               // 8) trade that day:
               worth += bestElement.trade( j );
               tradingDays++;
            } // end if high prob

         } // end for

      } // end for each group

      System.out.println( "worth is: " + worth );
      System.out.println( "trading days is: " + tradingDays );

   } // end simulateSBD

   /**
    *
    */
   public StockElement getLikeStockElement( String stock )
   {
      Collection theColl = _theStockCollection.getStockElementCollection();
      StockElement theElem = null;
      StockElement theBestElem = null;
      StockElement thisElem = getStockElement( stock );

      if( thisElem == null )
      {
         return new StockElement();
      }

      Iterator     theIter = theColl.iterator();
      double[] thisOpen = thisElem.getScaledOpen();
      double[] thisClose = thisElem.getScaledClose();
      double[] open = null;
      double[] close = null;
      double openDiff = 0;
      double closeDiff = 0;
      double difference = 0;
      double bestDifference = 20000;

      while( theIter.hasNext() )
      {
         openDiff = 0;
         closeDiff = 0;
         difference = 0;

         theElem = (StockElement)theIter.next();
         if( theElem.getName().equals( thisElem.getName() ) )
         {
            continue;
         }

         // need to optimize this piece:
         open = theElem.getScaledOpen();
         close = theElem.getScaledClose();

         if(    open.length != thisOpen.length
             && close.length != thisClose.length )
         {
            continue;
         }

         for( int i=0; i<open.length; i++ )
         {
            openDiff += Math.abs( open[i] - thisOpen[i] );
         }

         for( int i=0; i<close.length; i++ )
         {
            closeDiff += Math.abs( close[i] - thisClose[i] );
         }

         difference = (openDiff + closeDiff) / 2;

         if( difference < bestDifference )
         {
            theBestElem = theElem;
            theBestElem.setConfidence( difference );
            bestDifference = difference;
//System.out.println( "Found new best: " + theBestElem.getName() + " " + difference );
         }
      }

      return theBestElem;
   } // end getLikeStockElement

   /**
    *
    */
   public void findSimilarQQQStocks()
   {
      Collection theColl = _theStockCollection.getStockElementCollection();
      StockElement qqqElem = getStockElement( "QQQ" );
      StockElement theElem = null;
      Iterator iterator = theColl.iterator();
      double[] theCloses = null;
      double[] theQQQCloses = qqqElem.getScaledClose();
      double temp = 0;
      double total = 0;
      StringBuffer outputLine = null;

      while( iterator.hasNext() )
      {
         theElem = (StockElement)iterator.next();
         if( theElem.getName().equals( "QQQ" ) )
         {
            continue;
         }
         outputLine = new StringBuffer( theElem.getName() );
         theCloses = theElem.getScaledClose();

         int limit = theCloses.length < theQQQCloses.length ? theCloses.length : theQQQCloses.length;

         for( int i=0; i<limit; i++ )
         {
            temp = (theCloses[i] - theQQQCloses[i]);
            total += (temp * temp);
         }
         outputLine.append( " " + total + " " + limit );
         System.out.println( outputLine );
      }
   }

   /**
    *
    */
   public void calculateDividends()
   {
      Collection   theColl = _theStockCollection.getStockElementCollection();
      Iterator     theIter = theColl.iterator();
      StockElement theElem = null;

      while( theIter.hasNext() )
      {
         theElem = (StockElement)theIter.next();
         theElem.calculateDividends();
      } // end while

   } // end

   /**
    *
    */
   private StockElement getQQQ()
   {
      if( _Q == null )
      {
         _Q = _theStockCollection.getStockElement( "QQQ" );
         _Q.calculatePercentages();
      }
      return _Q;
   }

   /**
    *
    */
   public ArrayList compare( StockElement element1, int start, int length, StockElement element2 )
   {
      ArrayList<Double> close1 = element1.getAllClosesP();
      ArrayList<Double> close2 = element2.getAllClosesP();
/*
*/
      StockElement Q = getQQQ();
      ArrayList<Double> closeQ = Q.getAllClosesP();
      ArrayList<Double> list = new ArrayList<Double>();
      ArrayList<Double> listQ = new ArrayList<Double>();
      double bestError = 30000;
      int location = 0;
      int Qlocation = Q.getSize() - (element1.getSize() - start);
      int limit = element2.getSize() - (element1.getSize() - start + length);

      for( int i=start; i<(start + length); i++ )
      {
         list.add( close1.get( i ) );
      }
      
/*
*/
      for( int i=Qlocation; i<(Qlocation + length); i++ )
      {
         listQ.add( closeQ.get( i ) );
      }
      
      //for( int i=0; i<(close2.size() - length); i++ )
      for( int i=0; i<limit; i++ )
      {
         double error = 0;
         double c1 = 0;
         double c2 = 0;
         double cq = 0;
         double cqq = 0;
         int temp = 0;

         for( int j=0; j<list.size(); j++ )
         {
            c1 = list.get( j );
            c2 = close2.get( i + j );
            error += Math.abs(c1 - c2);
            temp = closeQ.size() - ( close2.size() - i );
            if( temp < 0 )
            {
               error += 1000;
            }
            else
            {
               cq = listQ.get( j );
               cqq = closeQ.get( temp );
               error += Math.abs(cq - cqq);
            }
/*
*/
//            System.out.println( "j is: " + j + " c1 is: " + c1 + " c2 is: " + c2 + 
//               " cq is: " + cq + " cqq is: " + cqq + " error is: " + error );
         }

         if( error < bestError )
         {
            //System.out.println( "found a new best error: " + error );
            location = i;
            bestError = error;
         }
      }
      ArrayList ret = new ArrayList();
      ret.add( new Double( bestError ) );
      ret.add( new Integer( location ) );
      return ret;
   }

   /**
    *
    */
   public void printElementRun( String name, Integer pos, Integer length, double[] prediction )
   {
      StockElement theElem = _theStockCollection.getStockElement( name );
      ArrayList<Double> close1 = theElem.getAllCloses();
      int end = ((pos + length) > close1.size()) ? close1.size() : (pos + length);

      System.out.println( "# data for: " + name );
      int j = 1;
      double c = 0;
      double p = 0;
      double previous = 0;
      double predictionOpen = 0;
      double[] predictions = new double[ prediction.length ];
      double open = 0;
      int half = (int)(predictions.length / 2);
      boolean canBuy = true;
      int openPos = 0;
      int closePos = 0;

      for( int i=pos; i<end; i++ )
      {
         c = close1.get( i );
         p = c;
         if( i > pos )
         {
            p = previous * prediction[ j++ ];
         }
         //System.out.println( close1.get( i ) );
         System.out.println( c + "," + p );
         previous = p;
         predictions[ j - 1 ] = p;
         if( i == (pos + half) )
         {
            predictionOpen = p;
            open = c;
            openPos = i;
         }
      }

      // finish printing out the prediction entries:
      for( ;j<prediction.length; )
      {
         p = previous * prediction[ j++ ];
         System.out.println( c + "," + p );
         previous = p;
         predictions[ j - 1 ] = p;
         canBuy = false;
      }

      double close = -1;
      // calculate the buy points
      for( int i=half;i<predictions.length;i++ )
      {
         if( predictions[ i ] > close )
         {
            j = i;
            close = predictions[ i ];
         }
      }
System.out.println( "prediction open, prediction close, diff" );
System.out.println( ",,,,," + predictionOpen + "," + close + "," + (close - predictionOpen) );
      if( canBuy ) 
      {
         close = close1.get( pos + j );
         closePos = pos + j;
         double result = theElem.performBuy( 10000, openPos, closePos );
System.out.println( "actual open, actual close, result" );
System.out.println( ",,,,,," + open + "," + close + "," + result );
      }

   }

   /**
    *
    */
   public void printElementBackRun( String name, Integer pos, Integer length )
   {
      StockElement theElem = _theStockCollection.getStockElement( name );
      ArrayList<Double> close1 = theElem.getAllCloses();

      if( pos >= 0 )
      {
         System.out.println( "# back data for: " + name );
         for( int i=pos; i<(pos + length); i++ )
         {
            System.out.println( close1.get( i ) );
         }
         System.out.println( "" );
      }
   }

   /**
    * Umm, Whatever...
    *
    */
   public void analyzeAgainstQQQ( String stock, int start, int length, HashMap map )
   {
      StockElement elem = _theStockCollection.getStockElement( stock );
      int limit = start + length;
      Iterator iter = (map.keySet()).iterator();
//      elem.calculatePercentages();

//      if( elem.getGaussianMedian( (start - length), start ) < 0 )
//      {
         while( iter.hasNext() )
         {
            int rand = getRandomInt();
            Object obj = iter.next();
            int amount = (Integer)obj;
            double begin = elem.getClose( start );
            if( rand < amount && (begin > 15 && begin < 20) )
            {
               double buyResult = elem.performBuy( ((double)10000 / (double)amount), start, (start + length) );
               double current = (Double)map.get( obj );
               current += buyResult;
               map.put( obj, new Double( current ) );
            }
//         }
      }

//      elem.calculatePercentages();
//      StockElement Q = getQQQ();
//      ArrayList<Double> close1 = elem.getAllClosesP();
//      ArrayList<Double> closeQ = Q.getAllClosesP();
//      int Qlocation = Q.getSize() - (elem.getSize() - start);
//      double currentError = 0;
//      double currentSlope = 0;
//      double slopeError = 0;

/*
      for( int i=start; i<(limit + length); i++ )
      {
System.out.println( close1.get( i ) + "," + closeQ.get( (i - start) + Qlocation ) );
         if( i < limit )
         {
            currentError += Math.abs( close1.get( i ) - closeQ.get( (i - start) + Qlocation ) );
         }
      }
*/

//      elem.performLinearModel( start, limit );
//      currentSlope = elem.getThreshold();
//      slopeError = elem.getSMAvalue();
//      double buyResult = elem.performBuy( 1000, limit, (limit + length) );
//System.out.println( ",,," + stock + ",slope:," + currentSlope + ", error:," + slopeError + ",Qerror:," + currentError + ",buy:," + buyResult + ",random," + getRandomBoolean() + "," + getRandomInt() );
   }

   /**
    * A test was run for ORCL from 4500 on to about 4600. This looked good,
    * but the data was going straight up, so anyone can do well there.
    * Look at 4000 to 4100 -- there, the data was going straight down.
    *
    *
    */
   public void analyze( String stock, int initialLoc, int length )
   {
      ArrayList<String> names = _theStockCollection.getStockNames();
      StockElement elem = _theStockCollection.getStockElement( stock );
      elem.calculatePercentages();
      ArrayList values = null;
      double bestError = 333000000;
      double currentError = 333000000;
      String bestName = null;
      Integer location = null;
      TreeMap<Double, ArrayList> sortedMap = new TreeMap<Double, ArrayList>();
      int limit = 0;
      // change the following values to change the intervals of prediction
      int trainLength = length;
      int predictionLength = (2 * trainLength);
      int initialLocation = initialLoc;

      for( String name : names )
      {
         //if( limit++ > 100 ) break;
         StockElement theElement = _theStockCollection.getStockElement( name );
         theElement.calculatePercentages();

         if( !name.equals( stock ) )
         {
            //System.out.println( "#A stock name is: " + name );
            //values = compare( orcl, 4571, 30, theElement );
            values = compare( elem, initialLocation, trainLength, theElement );
            currentError = (Double)values.get( 0 );
            values.add( name );
            sortedMap.put( currentError, values );
         }

         System.gc();
         Thread.yield();
         try { Thread.sleep( 10 ); } catch( InterruptedException e ) {}
         // just to test it out
         //System.out.println( "A stock elem is: " + theElement );
      }

      double[] prediction = new double[ predictionLength ];
      for( int w=0; w<predictionLength; w++ )
      {
         prediction[w] = 0;
      }
      Set<Double> theKeys = sortedMap.keySet();
      Iterator<Double> iter = theKeys.iterator();
      int i = 0;

      // change this to change the sample size of prediction:
      int sampleSize = 15;
      int sampledSize = 0;
      boolean isAllUp = true;
      boolean isAllUp005 = true;
      boolean isAllUp01 = true;
      boolean isAllUp015 = true;
      boolean isAllUp02 = true;

//      System.out.println( "" );
//      System.out.println( "Stock,slope,error" );

      while( iter.hasNext() )
      {
         Double error = iter.next();
         ArrayList value = sortedMap.get( error );
         Integer position = (Integer)value.get( 1 );
         String stockName = (String)value.get( 2 );
//System.out.println( "getting data for: " + stockName + " pos: " + position );
//System.out.println( "" );
         if( i++ < sampleSize )
         {
//System.out.println( "A similar stock: " + stockName + " position: " + position );
            StockElement theStockElement = _theStockCollection.getStockElement( stockName );
            theStockElement.calculatePercentages();
            ArrayList<Double> closes = theStockElement.getAllClosesP();
            int k = 0;
            Double aClose = 0.00;
            int end = position + predictionLength;
            if( end > closes.size() )
            {
               end = position;
            }
            else
            {
               sampledSize++;
            }
            for( int j=position; j<end; j++ )
            {
               aClose = closes.get( j );
//System.out.println( "a pred is: " + prediction[ k ] );
//System.out.println( aClose );
               prediction[ k++ ] += aClose;
            }

            // For these stocks that matched, there should be data in the "future" for which
            // to calculate the linear model.
/*
            int start = position + length;
            int ending = start + length;
            double slope = theStockElement.getThreshold();
            double slopeError = theStockElement.getSMAvalue();
            if( ending >= closes.size() )
            {
               ending = closes.size() - 1;
            }
            if( start >= closes.size() || (start == ending) )
            {
               slope = -1;
               slopeError = 100;
            }
            else
            {
               theStockElement.performLinearModel( start, ending );
               slope = theStockElement.getThreshold();
               slopeError = theStockElement.getSMAvalue();
            }
            if( slope < 0 )
            {
               isAllUp = false;
            }
            if( slope < 0.005 )
            {
               isAllUp005 = false;
            }
            if( slope < 0.01 )
            {
               isAllUp01 = false;
            }
            if( slope < 0.015 )
            {
               isAllUp015 = false;
            }
            if( slope < 0.02 )
            {
               isAllUp02 = false;
            }
            System.out.println( stockName + "," + slope + "," + slopeError );
*/

            // just to print out what it was before
            //printElementBackRun( stockName, (position - trainLength), predictionLength );
         }
         //System.out.println( "Error: " + error + " name: " + name );
      }

/*
      System.out.println( "isAllUp: " + isAllUp + 
                          " isAllUp005: " + isAllUp005 +
                          " isAllUp01: " + isAllUp01 +
                          " isAllUp015: " + isAllUp015 +
                          " isAllUp02: " + isAllUp02 );

      double buyResult = 0;
      int start = initialLocation + length;
      int end = start + length;
      // check to see if we buy 
      if( isAllUp )
      {
         buyResult = elem.performBuy( 10000, start, end );
         System.out.println( "0,,," + buyResult );
      }
      if( isAllUp005 )
      {
         buyResult = elem.performBuy( 10000, start, end );
         System.out.println( "005,,,," + buyResult );
      }
      if( isAllUp01 )
      {
         buyResult = elem.performBuy( 10000, start, end );
         System.out.println( "01,,,,," + buyResult );
      }
      if( isAllUp015 )
      {
         buyResult = elem.performBuy( 10000, start, end );
         System.out.println( "015,,,,,," + buyResult );
      }
      if( isAllUp02 )
      {
         buyResult = elem.performBuy( 10000, start, end );
         System.out.println( "02,,,,,,," + buyResult );
      }
*/

      for( i=0; i<prediction.length; i++ )
      {
         prediction[ i ] /= (double)sampledSize;
         //System.out.println( prediction[ i ] );
      }
      System.out.println( "" );

      printElementRun( stock, initialLocation, predictionLength, prediction );
      //printElementBackRun( stock, (initialLocation - trainLength), predictionLength );
      //System.out.println( "The best data was name: " + bestName + " error: " + bestError + " loc: " + location );
   }

   /**
    *
    */
   public boolean getRandomBoolean()
   {
      return (_randomGenerator.nextBoolean());
   }

   /**
    *
    */
   public int getRandomInt()
   {
      return (_randomGenerator.nextInt( 100 ));
   }

   /**
    * Returns a list of all the stock names in the database.
    */
   public ArrayList<String> getStockNames()
   {
      // no need to cache these names since the StockCollection does it for us.
      return _theStockCollection.getStockNames();
   }

   /**
    *
    */
   public double performBuy( double cash, double open, double close )
   {
      int shares = (int)(cash / open);
//       System.out.println( "bought: " + shares + " shares at: " + open );
      double amount = (double)shares * open;
      double result = ((double)(shares * close)) - amount;
//       System.out.println( "sold: " + (shares * close) + " at: " + close );
      result -= 10.0; // commissions
//       System.out.println( "result: " + result );
      return result;
   }

   /**
    * Here, we calculate some statistics for the coherence between various stock prices.
    * Since it would require too many calculations for compare each stock with all the other
    * stocks, we just compare each stock to an index security. Then, we also print the simple
    * stats of the change in the stock for each interval. The algorithm is as follows:
    * 1) Get the reference stock
    * 2) Get a list of all the stocks
    * 3) For each stock:
    *    a) retrieve the resolution window of closes
    *   .. just easier to write the code...but then, you should always prototype in pseudocode...
    *
    * 1.4
    * 2.5
    *
    *
    *
    *
    *
    */
   public void calculateCoherenceStatsStatic( int resolution )
   {
      StockElement reference = getStockElement( "QQQ" );
//       StockElement reference = getStockElement( "SPY" );
//       StockElement reference = getStockElement( "MSFT" );
//       StockElement reference = getStockElement( "INTC" );
//       StockElement reference = getStockElement( "ORCL" );
      ArrayList< Double > refCloses = reference.getAllCloses();
      ArrayList< String > names =_theStockCollection.getNasdaq100StockNames();
//       ArrayList< String > names =_theStockCollection.getSP500StockNames();
      double[] coherence = new double[ refCloses.size() - (2 * resolution) ];
      int count = 0;

      for( String name : names )
      {
         StockElement stock = getStockElement( name );
//          System.out.println( "Last date for " + name + " is: " + stock.getDate( stock.getDates().size() - 1 ) );
         ArrayList< Double > stockCloses = stock.getAllCloses();
         if( stockCloses.size() < refCloses.size() )
         {
            continue;
         }
//          System.out.println( "performing stock: " + name );
         int length = Math.min( refCloses.size(), stockCloses.size() );
         count++;

         for( int i=0; i<length - (2 * resolution); i++ )
         {
            double[] refPrices = new double[ resolution ];
            for( int j=(refCloses.size() - length + i), n=0; n<resolution; j++, n++ )
            {
               refPrices[ n ] = refCloses.get( j );
//                if( i == 0 )
//                {
//                   System.out.println( "ref start: " + refCloses.get( j ) );
//                }
            }
            refPrices = (new MathUtilities()).normalize( refPrices, 1 );

            double[] stockPrices = new double[ resolution ];
            for( int j=(stockCloses.size() - length + i), n=0; n<resolution; j++, n++ )
            {
               stockPrices[ n ] = stockCloses.get( j );
            }
            stockPrices = (new MathUtilities()).normalize( stockPrices, 1 );

            for( int k=0; k<resolution; k++ )
            {
//                System.out.println( "ref: " + refPrices[k] );
//                System.out.println( "stock: " + stockPrices[k] );
               coherence[ i ] += Math.abs( stockPrices[ k ] - refPrices[ k ] );
            }
         }
      }

      for( int i=0; i<coherence.length; i++ )
      {
         coherence[ i ] /= (double)count;
      }

      // Now, run through the data 
      double cash = 10000;
      double longThreshold = 0.75;
      double bestLong = 0.0;
      double bestShort = 0.0;
      double shortThreshold = 1.51;
      double increment = 0.005;
      double bestCash = -100000;
      double windowSize = 2.0;
      count = (int) (windowSize / increment);
      System.out.println( "\ncoherence, change, change + 1, correlated" );

      for( int i=0; i<coherence.length; i++ )
      {
               System.out.print( coherence[ i ] + ", " );

         // When the start is just the last day, the data is uncorrelated,
         // but when the start is the full week of coherence, the data is correlated.
//          double start = refCloses.get( i + resolution - 2 );
         double start = refCloses.get( i );
         double end = refCloses.get( i + resolution - 1 );
         double precedent = (end / start);
//          if( i == 0 )
//          {
//             System.out.println( "ref start precedent: " + start );
//             System.out.println( "ref end precedent: " + end );
//          }
               System.out.print( precedent + ", " );
         start = refCloses.get( i + resolution - 1 );
         end = refCloses.get( i + (2 * resolution) - 1 );
//          end = refCloses.get( i + resolution );
//          if( i == 0 )
//          {
//             System.out.println( "ref start consequent: " + start );
//             System.out.println( "ref end consequent: " + end );
//          }
//          end = refCloses.get( i + 2 * resolution - 1 );
         double consequent = (end / start);
               System.out.print( consequent + ", " );

               if(    (precedent >= 1 && consequent >= 1 ) 
                   || (precedent <= 1 && consequent <= 1 ) )
               {
                  System.out.println( "1" );
               }
               else
               {
                  System.out.println( "0" );
               }

         if( coherence[i] <= longThreshold )
//                if( coherence[i] <= 0.75 )
//          if( coherence[i] <= 1.4 )
         {
            // this is a long position
            if( precedent >= 1 )
            {
               cash += performBuy( cash, start, end );
//                System.out.println( "0.89, long, " + cash );
            }
            // otherwise, we short it
            else
            {
               cash += performBuy( cash, end, start );
//                System.out.println( "0.89, short, " + cash );
            }
         }
//          else if( coherence[i] >= 2.5 )
//                else if( coherence[i] >= 1.06 )
         else if( coherence[i] >= shortThreshold )
         {
            // we short it
            if( precedent >= 1 )
            {
               cash += performBuy( cash, end, start );
  //              System.out.println( "0.01, short, " + cash );
            }
            // this is a long position
            else
            {
               cash += performBuy( cash, start, end );
    //            System.out.println( "0.01, long, " + cash );
            }
         }
//          System.out.println( "cash: " + cash );
      }

      System.out.println( "\nBest Total Cash: " + bestCash );
      System.out.println( "\nBest Long Threshold: " + bestLong );
      System.out.println( "\nBest Short Threshold: " + bestShort );
   }

   /**
    * Here, we calculate some statistics for the coherence between various stock prices.
    * Since it would require too many calculations for compare each stock with all the other
    * stocks, we just compare each stock to an index security. Then, we also print the simple
    * stats of the change in the stock for each interval. The algorithm is as follows:
    * 1) Get the reference stock
    * 2) Get a list of all the stocks
    * 3) For each stock:
    *    a) retrieve the resolution window of closes
    *   .. just easier to write the code...but then, you should always prototype in pseudocode...
    *
    * 1.4
    * 2.5
    *
    *
    *
    *
    *
    */
   public void calculateCoherenceStats( int resolution )
   {
      StockElement reference = getStockElement( "QQQ" );
      ArrayList< Double > refCloses = reference.getAllCloses();
      ArrayList< String > names =_theStockCollection.getNasdaq100StockNames();
      double[] coherence = new double[ refCloses.size() - (2 * resolution) ];
      HashMap< String, StockElement > cache = new HashMap< String, StockElement >();
      int count = 0;

      for( int i=0; i<refCloses.size() - (2 * resolution); i++ )
      {
         double[] refPrices = new double[ resolution ];
         for( int j=0; j<resolution; j++ )
         {
            refPrices[ j ] = refCloses.get( i + j );
         }
         refPrices = (new MathUtilities()).normalize( refPrices, 1 );
         int offset = -1;

         for( String name : names )
         {
            StockElement stock = cache.get( name );
            if( stock == null )
            {
               stock = getStockElement( name );
               cache.put( name, stock );
            }

            ArrayList< Double > stockCloses = stock.getAllCloses();
            // now find where this stock's data matches up with the reference data
            for( int j=0; j<stockCloses.size(); j++ )
            {
               if( stock.getDate( j ).trim().equalsIgnoreCase( reference.getDate( i ).trim() ) )
               {
                  offset = j;
                  break;
               }
            }

            if( offset == -1 || stockCloses.size() <= offset + (2 * resolution) )
            {
               continue;
            }

            count++;
            double[] stockPrices = new double[ resolution ];
            for( int j=0; j<resolution; j++ )
            {
               stockPrices[ j ] = stockCloses.get( offset + j );
            }
            stockPrices = (new MathUtilities()).normalize( stockPrices, 1 );

            for( int k=0; k<resolution; k++ )
            {
//                System.out.println( "ref: " + refPrices[k] );
//                System.out.println( "stock: " + stockPrices[k] );
               coherence[ i ] += Math.abs( stockPrices[ k ] - refPrices[ k ] );
            }
         }
      }

      for( int i=0; i<coherence.length; i++ )
      {
         coherence[ i ] /= count;
      }

      // Now, run through the data 
      double cash = 10000;
      double longThreshold = 0.01;
      double bestLong = 0.0;
      double bestShort = 0.0;
      double shortThreshold = 0.01;
      double increment = 0.005;
      double bestCash = -100000;
//       double windowSize = 10.0;
      double windowSize = 2.0;
      count = (int) (windowSize / increment);
      System.out.println( "\ncoherence, change, change + 1, correlated" );

      for( int w=0; w<count; w++ )
      {
         shortThreshold = 0.01;
         for( int k=0; k<count; k++ )
         {
            cash = 10000;
            for( int i=0; i<coherence.length; i++ )
            {
//                System.out.print( coherence[ i ] + ", " );

               // When the start is just the last day, the data is uncorrelated,
               // but when the start is the full week of coherence, the data is correlated.
//                double start = refCloses.get( i + resolution - 2 );
               double start = refCloses.get( i );
               double end = refCloses.get( i + resolution - 1 );
               double precedent = (end / start);
      //          if( i == 0 )
      //          {
      //             System.out.println( "ref start precedent: " + start );
      //             System.out.println( "ref end precedent: " + end );
      //          }
//                System.out.print( precedent + ", " );
               start = refCloses.get( i + resolution - 1 );
//                end = refCloses.get( i + (2 * resolution - 1) );
               end = refCloses.get( i + resolution );
      //          if( i == 0 )
      //          {
      //             System.out.println( "ref start consequent: " + start );
      //             System.out.println( "ref end consequent: " + end );
      //          }
      //          end = refCloses.get( i + 2 * resolution - 1 );
               double consequent = (end / start);
//                System.out.print( consequent + ", " );

//                if(    (precedent >= 1 && consequent >= 1 ) 
//                    || (precedent <= 1 && consequent <= 1 ) )
//                {
//                   System.out.println( "1" );
//                }
//                else
//                {
//                   System.out.println( "0" );
//                }

               if( coherence[i] <= longThreshold )
//                if( coherence[i] <= 0.75 )
      //          if( coherence[i] <= 1.4 )
               {
                  // this is a long position
                  if( precedent >= 1 )
                  {
                     cash += performBuy( cash, start, end );
                  }
                  // otherwise, we short it
                  else
                  {
                     cash += performBuy( cash, end, start );
                  }
               }
      //          else if( coherence[i] >= 2.5 )
//                else if( coherence[i] >= 1.06 )
               else if( coherence[i] >= shortThreshold )
               {
                  // we short it
                  if( precedent >= 1 )
                  {
                     cash += performBuy( cash, end, start );
                  }
                  // this is a long position
                  else
                  {
                     cash += performBuy( cash, start, end );
                  }
               }
      //          System.out.println( "cash: " + cash );
            }

            if( cash > bestCash )
            {
//                System.out.println( "setting a best cash: " + cash );
               bestCash = cash;
               bestLong = longThreshold;
               bestShort = shortThreshold;
            }

            shortThreshold += increment;
//             System.out.println( "shortThreshold is: " + shortThreshold );
         }
         longThreshold += increment;
//          System.out.println( "longThreshold is: " + longThreshold );
      }

      System.out.println( "\nBest Total Cash: " + bestCash );
      System.out.println( "\nBest Long Threshold: " + bestLong );
      System.out.println( "\nBest Short Threshold: " + bestShort );
   }

   /**
    *
    */
   public void calculateLatestUnderCurrent()
   {
      StockElement reference = getAmendedStockElement( "QQQ" );
      ArrayList< Double > refCloses = reference.getAllCloses();
      ArrayList< String > names =_theStockCollection.getNasdaq100StockNames();
      double coherence = 0;
      int count = 0;

      for( String name : names )
      {
         StockElement stock = getAmendedStockElement( name );
         ArrayList< Double > stockCloses = stock.getAllCloses();

         if( stockCloses.size() < refCloses.size() )
         {
            continue;
         }

         count++;
         double[] refPrices = new double[ 2 ];
         refPrices[ 0 ] = refCloses.get( refCloses.size() - 2 );
         refPrices[ 1 ] = refCloses.get( refCloses.size() - 1 );
         refPrices = (new MathUtilities()).normalize( refPrices, 1 );

         double[] stockPrices = new double[ 2 ];
         stockPrices[ 0 ] = stockCloses.get( stockCloses.size() - 2 );
         stockPrices[ 1 ] = stockCloses.get( stockCloses.size() - 1 );
         stockPrices = (new MathUtilities()).normalize( stockPrices, 1 );

         coherence += Math.abs( stockPrices[ 0 ] - refPrices[ 0 ] );
         coherence += Math.abs( stockPrices[ 1 ] - refPrices[ 1 ] );
      }

      coherence /= count;
      System.out.println( "The undercurrent coherence is: " + coherence );
      double open = refCloses.get( refCloses.size() - 2 );
      double close = refCloses.get( refCloses.size() - 1 );
      double antecedent = (close / open);
      System.out.println( "The open price is: " + open );
      System.out.println( "The close price is: " + close );
      System.out.println( "The antecedent is: " + antecedent );
   }

   /**
    *
    */
   public void calculateCoherenceStatsStatic2( int resolution )
   {
      StockElement reference = getStockElement( "QQQ" );
      ArrayList< Double > refCloses = reference.getAllCloses();
//       ArrayList< String > names =_theStockCollection.getNasdaq100StockNames();
      ArrayList< String > names =_theStockCollection.getLiquidStockNames();
      HashMap< String, StockElement > _stocks = new HashMap< String, StockElement >();
      double cash = 10000;
//       System.out.println( "Reference:, QQQ ,last index:, " + ( refCloses.size() - resolution - 1 ) );
//       System.out.println( "Reference:, QQQ ,last date:, " + reference.getDate( refCloses.size() - resolution - 1 ) );

      for( int i=0; i<refCloses.size() - resolution - 1; i += resolution )
//       for( int i=2000; i<refCloses.size() - resolution - 1; i += resolution )
      {
         ArrayList< Double > highest = null;
         ArrayList< Double > lowest = null;
         StockElement highestStock = null;
         double largestHigh = -1 * Double.MAX_VALUE;
         double largestLow = Double.MAX_VALUE;
         double temp = 0;
         double open = 0;
         double close = 0;
         int largestOffset = -1;
         int lowestOffset = -1;
         int offset = -1;

         for( String name : names )
         {
            StockElement stock = _stocks.get( name );
            if( stock == null )
            {
               stock = getStockElement( name );
               _stocks.put( name, stock );
            }
            ArrayList< Double > stockCloses = stock.getAllCloses();

            for( int j=0; j<stockCloses.size(); j++ )
            {
               if( stock.getDate( j ).equals( reference.getDate( i ) ) )
               {
                  offset = j;
                  break;
               }
            }

            if( offset == -1 || stockCloses.size() <= offset + ( 2 * resolution) )
            {
               continue;
            }

            temp = ( stockCloses.get( offset + resolution - 1 ) / stockCloses.get( offset ) ) -
                   ( refCloses.get( i + resolution - 1 ) / refCloses.get( i ) );
//             temp = ( stockCloses.get( offset + resolution ) / stockCloses.get( offset ) ) -
//                    ( refCloses.get( i + resolution ) / refCloses.get( i ) );

//             if( i%100 == 0 )
//             {
//                System.out.println( "index: " + i );
//                System.out.println( "Reference:, QQQ ,date open:, " + reference.getDate( i ) );
//                System.out.println( "Reference:, QQQ ,date close:, " + reference.getDate( i + resolution - 1 ) );
//                System.out.println( "Reference:, QQQ ,open:, " + refCloses.get( i ) );
//                System.out.println( "Reference:, QQQ ,close:, " + refCloses.get( i + resolution - 1 ) );
//                System.out.println( "Stock:, " + name + " ,date open:, " + stock.getDate( offset ) );
//                System.out.println( "Stock:, " + name + " ,date close:, " + stock.getDate( offset + resolution - 1 ) );
//                System.out.println( "Stock:, " + name + " ,open:, " + stockCloses.get( offset ) );
//                System.out.println( "Stock:, " + name + " ,close:, " + stockCloses.get( offset + resolution - 1 ) );
//             }

            if( temp > largestHigh )
            {
               highest = stockCloses;
               largestHigh = temp;
               largestOffset = offset;
               highestStock = stock;
//                if( i%100 == 0 )
//                {
//                   System.out.println( "found largest:, " + name );
//                }
            }
            else if( temp < largestLow )
            {
               lowest = stockCloses;
               largestLow = temp;
               lowestOffset = offset;
//                if( i%100 == 0 )
//                {
//                   System.out.println( "found lowest:, " + name );
//                }
            }

         } // for all stock names

         // so far, res - 1 and 2 * res gave best results.
         if( highest != null )
         {
//             open = highest.get( largestOffset + resolution );
            open = highest.get( largestOffset + resolution - 1 );
//             close = highest.get( largestOffset + resolution );
//             close = highest.get( largestOffset + 2 * resolution - 1 );
            close = highest.get( largestOffset + 2 * resolution );
            temp = close / open;
            // short the high
            // Well, now we try to straddle
            double tcash = cash;
            cash += performBuy( tcash, open, close );
            cash += performBuy( tcash, close, open );
            System.out.println( "net sell: " + (performBuy( tcash, open, close ) + performBuy( tcash, close, open )) 
                  + " on: " + tcash );
//             System.out.println( "high:, " + temp );
            System.out.println( "buying:, " + highestStock.getName() + " on:, " + 
//                                 highestStock.getDate( largestOffset + resolution ) + 
                                highestStock.getDate( largestOffset + resolution - 1 ) + 
//                                 " and selling on:, " + highestStock.getDate( largestOffset + 2 * resolution - 1 ) +
                                " and selling on:, " + highestStock.getDate( largestOffset + 2 * resolution ) +
                                " the start date:, " + highestStock.getDate( largestOffset ) +
                                " the start end date:, " + highestStock.getDate( largestOffset + resolution - 1 ) );
         }

         if( lowest != null )
         {
//             open = lowest.get( lowestOffset + resolution );
            open = lowest.get( lowestOffset + resolution - 1 );
//             close = lowest.get( lowestOffset + resolution );
//             close = lowest.get( lowestOffset + 2 * resolution - 1 );
            close = lowest.get( lowestOffset + 2 * resolution );
            temp = close / open;
            // buy the low
            // Well, now we try to straddle
            double tcash = cash;
            cash += performBuy( tcash, close, open );
            cash += performBuy( tcash, open, close );
//             System.out.println( "low:, " + temp );
         }
         
         System.out.println( "cash at:, " + reference.getDate( i ) + " is:, " + cash );

      } // for a reference prices

      System.out.println( "total cash:, " + cash );
   }

   /**
    * This method should be used at the end-all be-all for calculating various statistics
    * as they relate to a reference security such as depository receipts.
    * The basic idea is that we will compute the various statistics for the first interval
    * and then compute them for the second half of the interval. Then we can printout and
    * find any correlations.
    */
   public void calculateAllStatistics( int window )
   {
      StockElement                    reference  = getStockElement( "QQQ" );
      ArrayList< Double >             refCloses  = reference.getAllCloses();
      ArrayList< String >             names      = _theStockCollection.getNasdaq100StockNames();
      HashMap< String, StockElement > cache      = new HashMap< String, StockElement >();
      // size of 3 and interval of .05 seem to work pretty damn well
      int entropySize = 1;
//       double entropyInterval = 1;
      double entropyInterval = 0.1;

      System.out.println( "date, referenceEntropy, referenceDeviation, delta , " + 
               "referenceEntropy2 + , referenceDeviation2 , delta2" );

      // loop through each day in the reference security
      for( int i=0; i<refCloses.size() - window - 1; i += window )
      {
         // first, get the data we need from the reference stock so that we don't do this
         // during every iteration of the stock loop.
         double[] referenceArray = new double[ window ];
         for( int j=0; j<window; j++ )
         {
            referenceArray[j] = refCloses.get( i + j );
         }
         double delta = referenceArray[ referenceArray.length - 1 ] / referenceArray[ 0 ];
         referenceArray = (new MathUtilities()).normalize( referenceArray, 1 );
         double referenceEntropy = (new ApproximateEntropy( referenceArray, entropySize, entropyInterval )).getEntropy();
         StatUtilities s = new StatUtilities();
         s.calculateStats( referenceArray );
         double referenceDeviation = s.getDeviation();

         double[] referenceArray2 = new double[ window ];
         for( int j=window-1; j<2*window-1; j++ )
         {
            referenceArray2[j-window+1] = refCloses.get( i + j );
         }
         double delta2 = referenceArray2[ referenceArray2.length - 1 ] / referenceArray2[ 0 ];
         referenceArray2 = (new MathUtilities()).normalize( referenceArray2, 1 );
         double referenceEntropy2 = (new ApproximateEntropy( referenceArray2, entropySize, entropyInterval )).getEntropy();
         s.calculateStats( referenceArray2 );
         double referenceDeviation2 = s.getDeviation();

         int count = 1;

         /*
         count = 0;
         // loop through each stock for that day
         for( String name : names )
         {
            // get the element from the cache or update the cache
            StockElement stock = cache.get( name );
            if( stock == null )
            {
               stock = getStockElement( name );
               cache.put( name, stock );
            }

            // get the data we need to work with
            ArrayList< Double > stockCloses = stock.getAllCloses();
            int offset = -1;

            // now find where this stock's data matches up with the reference data
            for( int j=0; j<stockCloses.size(); j++ )
            {
               if( stock.getDate( j ).trim().equalsIgnoreCase( reference.getDate( i ).trim() ) )
               {
                  offset = j;
                  break;
               }
            }

            if( offset == -1 || stockCloses.size() <= offset + (2 * window) )
            {
               continue;
            }

            count++;
            double[] stockArray = new double[ window ];
            for( int j=0; j<window; j++ )
            {
               stockArray[j] = stockCloses.get( offset + j );
            }
            delta += stockArray[ stockArray.length - 1 ] / stockArray[ 0 ];
            stockArray = (new MathUtilities()).normalize( stockArray, 1 );
            referenceEntropy += (new ApproximateEntropy( stockArray, entropySize, entropyInterval )).getEntropy();
            s.calculateStats( referenceArray );
            referenceDeviation += s.getDeviation();

            double[] stockArray2 = new double[ window ];
            for( int j=window-1; j<2*window-1; j++ )
            {
               stockArray2[j-window+1] = stockCloses.get( offset + j );
            }
            delta2 += stockArray2[ stockArray2.length - 1 ] / stockArray2[ 0 ];
            stockArray2 = (new MathUtilities()).normalize( stockArray2, 1 );
            referenceEntropy2 += (new ApproximateEntropy( stockArray2, entropySize, entropyInterval )).getEntropy();
            s.calculateStats( stockArray2 );
            referenceDeviation2 += s.getDeviation();
         } // for all stock names
         */

         referenceEntropy /= count;
         referenceDeviation /= count;
         delta /= count;
         referenceEntropy2 /= count;
         referenceDeviation2 /= count;
         delta2 /= count;

         System.out.println( reference.getDate( i ) + ", " + referenceEntropy + ", " + 
               referenceDeviation + ", " + delta + ", " + 
               referenceEntropy2 + ", " + referenceDeviation2 + ", " + delta2 );

      } // for a reference prices
   }

   /**
    *
    */
   public void calculateLatestNasdaqa100Movers()
   {
      StockElement reference = getAmendedStockElement( "QQQ" );
      ArrayList< Double > refCloses = reference.getAllCloses();
//       ArrayList< String > names =_theStockCollection.getNasdaq100StockNames();
      ArrayList< String > names = _theStockCollection.getLiquidStockNames();
      double largestGain = -1 * Double.MAX_VALUE;
      double largestLoss = Double.MAX_VALUE;
      StockElement largestElement = null;
      StockElement lowestElement = null;

      int s = refCloses.size();
      double q = ( refCloses.get( s - 1 ) / refCloses.get( s - 2 ) );

      for( String name : names )
      {
         StockElement stock = getAmendedStockElement( name );
         ArrayList< Double > stockCloses = stock.getAllCloses();

         s = stockCloses.size();
//          System.out.println( name + " close: " + stockCloses.get( s - 1 ) + 
//                " open: " + stockCloses.get( s - 2 ) );
         double e = ( stockCloses.get( s - 1 ) / stockCloses.get( s - 2 ) );
         double t = e - q;
         if( t > largestGain )
         {
            largestGain = t;
            largestElement = stock;
         }
         if( t < largestLoss )
         {
            largestLoss = t;
            lowestElement = stock;
         }
      }

      System.out.println( "The largest gainer is: " + largestElement.getName() );
      System.out.println( "with a gain of: " + largestGain );
      System.out.println( "The largest looser is: " + lowestElement.getName() );
      System.out.println( "with a loss of: " + largestLoss );
   }

   /**
    * 
    * 
    */
   public void calculateCompetitionTheory( int window, int hold )
   {
      StockElement                    reference  = getStockElement( "QQQ" );
      ArrayList< Double >             refCloses  = reference.getAllCloses();
//       ArrayList< String >             names      = _theStockCollection.getNasdaq100StockNames();
      ArrayList< String >             names      = _theStockCollection.getLiquidStockNames();
      HashMap< String, StockElement > cache      = new HashMap< String, StockElement >();

      // loop through each day in the reference security
      for( int i=window; i<refCloses.size()-hold; i++ )
      {
         HashMap< String, ArrayList< Double > > scores = new HashMap< String, ArrayList< Double > >();
//          HashMap< String, Long > wins = new HashMap< String, Long >();
//          HashMap< String, Long > losses = new HashMap< String, Long >();
         HashMap< String, Double > wins = new HashMap< String, Double >();
         HashMap< String, Double > losses = new HashMap< String, Double >();
         HashMap< String, Double > future = new HashMap< String, Double >();

         // loop through each stock for that day
         for( String name : names )
         {
            // get the element from the cache or update the cache
            StockElement stock = cache.get( name );
            if( stock == null )
            {
               stock = getStockElement( name );
               cache.put( name, stock );
            }

            // get the data we need to work with
            ArrayList< Double > stockCloses = stock.getAllCloses();
            int offset = -1;

            // now find where this stock's data matches up with the reference data
            for( int j=0; j<stockCloses.size(); j++ )
            {
               if( stock.getDate( j ).trim().equalsIgnoreCase( reference.getDate( i ).trim() ) )
               {
                  offset = j;
                  break;
               }
            }

            if( offset == -1 || (offset - window) < 0 || stockCloses.size() < (offset + hold) )
            {
               continue;
            }

//             System.out.println( "running stock: " + name + " with offset: " + offset );
            ArrayList< Double > pValues = new ArrayList< Double >();
            for( int j=(offset-window+1); j<(offset+1); j++ )
            {
               double p = stockCloses.get( j ) / stockCloses.get( j - 1 );
               pValues.add( p );
//                System.out.println( "  adding a pValue: " + p );
            }
            scores.put( name, pValues );
//             wins.put( name, new Long( 0 ) );
//             losses.put( name, new Long( 0 ) );
            wins.put( name, new Double( 0 ) );
            losses.put( name, new Double( 0 ) );
            double p = stockCloses.get( offset + hold ) / stockCloses.get( offset );
            future.put( name, p );
         }

         for( int j=0; j<window; j++ )
         {
            Set< String > keys = scores.keySet();
            for( String key : keys )
            {
//                System.out.println( "Comparing stock: " + key );
               ArrayList< Double > keyValues = scores.get( key );
               double kP = keyValues.get( j );
               double w = wins.get( key );
               double l = losses.get( key );
               for( String opp : keys )
               {
                  if( key.equals( opp ) )
                  {
                     continue;
                  }
//                   System.out.println( "to stock: " + opp );
                  ArrayList< Double > oppValues = scores.get( opp );
                  double oP = oppValues.get( j );
//                   System.out.println( " kP is: " + kP + " oP is: " + oP );
                  if( kP >= 1 && oP >= 1 )
                  {
                     if( kP > oP )
                     {
//                         double oppw = wins.get( opp );
//                         System.out.println( "opp wins: " + oppw + " for: " + opp);
//                         w += (1 + oppw);
                        w += (1);
                     }
                  }
                  else if( kP < 1 && oP < 1 )
                  {
                     if( kP < oP )
                     {
//                         double oppl = losses.get( opp );
//                         l += (1 + oppl);
                        l += (1);
                     }
                  }
               }
               wins.put( key, new Double( w ) );
               losses.put( key, new Double( l ) );
            }
         }

         System.out.println( "The date is:, " + reference.getDate( i ) );
         TreeMap< Double, String > winSort = new TreeMap< Double, String >();
         TreeMap< Double, String > lossSort = new TreeMap< Double, String >();
         Set< String > keys = wins.keySet();
         for( String key : keys )
         {
            winSort.put( wins.get( key ), key );
            lossSort.put( losses.get( key ), key );
//             System.out.println( key + " ,wins, " + wins.get( key ) + " ,losses, " + losses.get( key ) + 
//                                 " ,hold:, " + future.get( key ) );
         }

         Collection< String > winNames = winSort.values();
         Collection< String > lossNames = lossSort.values();
         int outer = 0;
         for( String name : winNames )
         {
            outer++;
            int inner = 0;
//             System.out.println( "a sorted win: " + name );
            for( String other : lossNames )
            {
               inner++;
//                System.out.println( "a sorted loss: " + other );
               if( name.equals( other )  )
               {
                  if( outer <= inner )
                  {
                     System.out.println( name + " ,order, 1, " + wins.get( name ) + " , " + future.get( name ) );
                  }
                  else
                  {
                     System.out.println( name + " ,order, 0, " + wins.get( name ) + " , " + future.get( name ) );
                  }
                  break;
               }
            }
         }

      } // for a reference prices
   }

   /**
    * 
    * 
    */
   public double[] calculateGravityTheory( int window, int hold, double longRatio, double highRatio, double lowRatio )
   {
      StockElement                    reference  = getStockElement( "QQQ" );
//       StockElement                    reference  = getStockElement( "SPY" );
      ArrayList< Double >             refCloses  = reference.getAllCloses();
      ArrayList< String >             names      = _theStockCollection.getLiquidStockNames();
      HashMap< String, StockElement > cache      = new HashMap< String, StockElement >();
      double cash = 10000;
      double[] result = new double[ 2 ];
      ArrayList< Double > futures = new ArrayList< Double >();
      ArrayList< Double > lows = new ArrayList< Double >();
      ArrayList< Double > highs = new ArrayList< Double >();
      HashMap< Integer, Double > rankAvg = new HashMap< Integer, Double >();
      int count = 0;
      int countt = 0;

      int cc = 0;
      for( String name : names )
      {
         rankAvg.put( new Integer( cc++ ), 0.0 );
      }
      rankAvg.put( new Integer( cc++ ), 0.0 );

      // loop through each day in the reference security
      for( int i=window; i<refCloses.size()-hold; i+= hold )
//       for( int i=2000; i<refCloses.size()-hold; i+= hold )
      {
         count++;
         HashMap< String, ArrayList< Double > > scores = new HashMap< String, ArrayList< Double > >();
         TreeMap< Double, String > market = new TreeMap< Double, String >();
         TreeMap< Double, String > rank = new TreeMap< Double, String >();
         HashMap< String, Double > future = new HashMap< String, Double >();
         HashMap< String, Double > past = new HashMap< String, Double >();
         HashMap< String, Double > volume = new HashMap< String, Double >();
         HashMap< String, Double > low = new HashMap< String, Double >();
         HashMap< String, Double > high = new HashMap< String, Double >();
         HashMap< String, Integer > offsets = new HashMap< String, Integer >();
         int pastWindow = 9;
         double q = 0;

         // loop through each stock for that day
         for( String name : names )
         {
            // get the element from the cache or update the cache
            StockElement stock = cache.get( name );
            if( stock == null )
            {
               stock = getStockElement( name );
               cache.put( name, stock );
            }

            // get the data we need to work with
            ArrayList< Double > stockCloses = stock.getAllCloses();
            ArrayList< Double > volumes = stock.getVolumes();
            int offset = -1;

            // now find where this stock's data matches up with the reference data
            for( int j=0; j<stockCloses.size(); j++ )
            {
               if( stock.getDate( j ).trim().equalsIgnoreCase( reference.getDate( i ).trim() ) )
               {
                  offset = j;
                  break;
               }
            }

            if( offset == -1 || (offset - window) < 0 || stockCloses.size() < (offset + hold) )
            {
               continue;
            }

            // Spread on a low day: median: 0.9975786924939468
            // Spread on a low day + lower than previous low: median: 1.0
            // Spread on a low day + greater than previous low: median: 0.9997501249375311
            // Next Close on a low day + greater than previous low: Median: 1.0
            // Next Close on a low day + lower than previous low: median: 1.0
            // Next Close on a low day: median: 0.9952038369304558
            // Next Close on a low day + spread is up or equal: median: 0.9907993966817497
            // Next Close on a low day + spread is up: median: 0.9886589743484719
            // Next Close on a low day + spread is up 1.002: median: 0.9882609687134395
            // Next Close on a low day + spread is up 1.01: median: 0.9766730481865645
            // Next Close on a low day + spread is up 1.01 and price > 10: median: 0.9713726951015087
            // Next Close on a low day + spread is up 1.01 and price > 20: median: 0.9519230769230769
            // Next Close on a low day + spread is up 1.01 and price > 30: median: 0.94924398120963
            // Next Close on a low day + spread is up 1.01 and price > 40: median: 0.9649296686153161
            // Next Close on a low day + spread is up 1.01 and price [30, 40]: median: 0.9054252998909488
            double entry = 0;
            double exit = 0;
            double prev = stock.getLow( offset - 1 );
            double next = stock.getOpen( offset + 1 );
            double openn = stock.getOpen( offset );
            double highh = stock.getHigh( offset );
            double loww = stock.getLow( offset );
            double closee = stock.getClose( offset );
            if( highh <= openn )
            {
               entry = closee;
//                if( closee <= loww )
               {
//                   if( next < closee )
//                   if( (next / closee) > 1.01 && closee >= 30.0 && closee <= 40.00 )
//                   if( (next / closee) > 1.01 && closee > 10.0 )
//                   if( (next / closee) < 0.98 )
//                   if( (next / closee) > 1.01 )
//                   {
//                      // check the spread:
//                      exit = stock.getClose( offset + 1 );
//                   }
//                   else
//                   {
//                      exit = next;
//                   }
//                   cash += performBuy( (cash / 2), exit, entry );

//                   if( (next / closee) < 0.98 )
                  if( (next / closee) < 0.97 )
                  {
//                      exit = stock.getClose( offset + 1 );
                     exit = next;
                     countt++;
                     double t = entry / exit;
                     futures.add( t );
                  }
                  else
                  {
//                      if( entry <= stock.getHigh( offset + 1 ) )
                     if( next < entry )
                     {
//                         exit = entry;
                        exit = stock.getClose( offset + 1 );
                     }
                     else
                     {
                        if( entry >= stock.getLow( offset + 1 ) )
                        {
                           exit = entry;
                        }
                     }
                  }
                  cash += performBuy( (cash), exit, entry );
                  System.out.println( stock.getDate( offset ) + " ,cash:, " + cash );
               }
            }

//             if( open > 10.0 )
//             if( stock.getClose( offset ) > 10.0 )
//             {
//                continue;
//             }
            offsets.put( name, offset );
            double hi = -1 * Double.MAX_VALUE;
            double l = Double.MAX_VALUE;
            for( int w=0; w<hold+1; w++ )
            {
               double thi = stock.getHigh( offset + w );
               if( thi > hi )
               {
                  hi = thi;
               }
               double tl = stock.getLow( offset + w );
               if( tl < l )
               {
                  l = tl;
               }
            }
            hi /= stockCloses.get( offset );
            l /= stockCloses.get( offset );

            double v = volumes.get( offset ) / volumes.get( offset - window );
            double p = stockCloses.get( offset ) / stockCloses.get( offset - window );
            double f = stockCloses.get( offset + hold ) / stockCloses.get( offset );
            double h = 0;
            if( (offset - pastWindow) > -1 )
            {
               h = stockCloses.get( offset ) / stockCloses.get( offset - pastWindow );
            }
            market.put( p, name );
            rank.put( f, name );
            future.put( name, f );
            past.put( name, h );
            volume.put( name, v );
            low.put( name, l );
            high.put( name, hi );
         }
         Collection< Double > keys = market.keySet();
         int mid = (int)(keys.size() / 2);
         double median = 0;
         double lowest = 0;
         double highest = 0;
         int c = 0;
         for( double d : keys )
         {
            c++;
            if( c == 1 )
            {
               lowest = d;
            } 
            else if( c == mid )
            {
               median = d;
            }
            double average = rankAvg.get( new Integer( c ) );
            String stock = market.get( d );
            average += future.get( stock );
            rankAvg.put( c, average );
         }
         String lowStock = market.get( lowest );
         String highStock = market.get( highest );
         double hi = high.get( lowStock );
         double l = low.get( lowStock );
         double v = volume.get( lowStock );
         double p = future.get( lowStock );
         double h = past.get( lowStock );
         double s = 1 / p;
//          futures.add( p );

         if( (i - pastWindow) > -1 )
         {
            q = refCloses.get( i ) / refCloses.get( i - pastWindow );
         }

//          System.out.println( "median:, " + median + " , " + lowStock + " , " + lowest + " , " + p
//                              + " , " + h + " , " + q );
//                              + " , " + highStock + " , " + highest + " , " + future.get( highStock ) );

//          System.out.println( "\n" + reference.getDate( i ) );
         double longg = cash * longRatio;
         double shortt = cash - longg;
//          longg *= p;
//          longg -= 10;
//          shortt *= s;
//          shortt -= 10;
//          cash = ( longg + shortt );
//          System.out.println( reference.getDate( i ) + " ,cash:, " + cash + " ,stock:, " + lowStock + " ,past:, " + h +
//                              " ,vol:, " + v + " ,low:, " + l + " ,high:, " + hi + " ,future:, " + p );

         // try another entry and exit strategy on the same security
         int offset = offsets.get( lowStock );
         StockElement stock = cache.get( lowStock );
         double open = stock.getClose( offset );
         double plusOne = stock.getClose( offset + 1 );
         double o = plusOne;
         double absHigh = -1 * Double.MAX_VALUE;
         double absLow = Double.MAX_VALUE;
         double longClose = 0;
         double shortOpen = 0;
         double ht = open * highRatio;
         double lt = open * lowRatio;
         double close = 0;
         double previous = 0;
         boolean isUp = false;
         for( int k=1; k<hold+1; k++ )
         {
            close = stock.getClose( offset + k );
            if( k == 1 && o > open )
            {
               isUp = true;
            }
            if( isUp && k > 1 )
            {
               double t = stock.getOpen( offset + k );
               if( t > previous )
               {
                  t = stock.getLow( offset + k );
                  if( t < previous )
                  {
                     break;
                  }
               }
               else
               {
                  break;
               }
            }
            else if( k > 1 )
            {
               double t = stock.getOpen( offset + k );
               if( t < previous )
               {
                  t = stock.getHigh( offset + k );
                  if( t > previous )
                  {
                     break;
                  }
               }
               else
               {
                  break;
               }
            }
            double current = stock.getHigh( offset + k );
            if( current > absHigh )
            {
               absHigh = current;
            }
            if( longClose == 0 )
            {
               if( current >= ht )
               {
                  longClose = ht;
               }
            }
            current = stock.getLow( offset + k );
            if( current < absLow )
            {
               absLow = current;
            }
            if( shortOpen == 0 )
            {
               if( current <= lt )
               {
                  shortOpen = lt;
               }
            }
            previous = close;
         }
         if( longClose == 0 )
         {
            longClose = stock.getClose( offset + hold );
         }
         if( shortOpen == 0 )
         {
            shortOpen = stock.getClose( offset + hold );
         }
//          if( isUp )
//          {
//             cash += performBuy( longg, o, close );
//          }
//          else
//          {
//             cash += performBuy( shortt, close, o );
//          }
//          cash += performBuy( longg, open, longClose );
//          cash += performBuy( shortt, shortOpen, open );
//          if( plusOne / open >= 1.0 )
//          {
//             highs.add( absHigh / plusOne );
//          }
//          else
//          {
//             lows.add( absLow / plusOne );
//          }
//          System.out.println( reference.getDate( i ) + " ,stock:, " + lowStock + " ,open:, " + open + 
//                              " ,o:, " + o + " ,close:, " + close + " ,count:, " + c + 
//                              " ,longClose:, " + longClose + " ,shortOpen:, " + shortOpen + " ,cash:, " + cash );
// 
//          Collection< Double > rKeys = rank.keySet();
//          int r = 0;
//          for( Double d : rKeys )
//          {
//             r++;
//             String stock = rank.get( d );
//             Collection< Double > mKeys = market.keySet();
//             int m = 0;
//             for( Double k : mKeys )
//             {
//                m++;
//                String marketStock = market.get( k );
//                if( stock.equals( marketStock ) )
//                {
//                   System.out.println( stock + " ,rank:, " + r + " ,previous:, " + m );
//                   break;
//                }
//             }
//          }
      }

//       Collection< Integer > aKeys = rankAvg.keySet();
//       for( Integer ak : aKeys )
//       {
//          double d = rankAvg.get( ak );
//          d /= (double)count;
//          System.out.println( ak + " ,avg:, " + d );
//       }

//       System.out.println( "cash is:, " + cash );
      StatUtilities s = new StatUtilities();
      s.calculateStats( futures );
      result[0] = cash;
      result[1] = s.getMedian();
      System.out.println( "count: " + countt + " median: " + result[1] + " cash: " + cash );
//       s.calculateStats( highs );
//       System.out.print( "High median: " + s.getMedian() );
//       s.calculateStats( lows );
//       System.out.println( " Low median: " + s.getMedian() );

      return result;
   }

   /**
    * 
    * 
    */
   public double[] calculateGravityTheoryShort( int window, int hold, double shortRatio )
   {
      StockElement                    reference  = getStockElement( "QQQ" );
      ArrayList< Double >             refCloses  = reference.getAllCloses();
      ArrayList< String >             names      = _theStockCollection.getLiquidStockNames();
      HashMap< String, StockElement > cache      = new HashMap< String, StockElement >();
      double cash = 10000;
      double[] result = new double[ 2 ];
      ArrayList< Double > futures = new ArrayList< Double >();

      // loop through each day in the reference security
//       for( int i=window; i<refCloses.size()-hold; i++ )
      for( int i=window; i<refCloses.size()-hold; i+= hold )
      {
         HashMap< String, ArrayList< Double > > scores = new HashMap< String, ArrayList< Double > >();
         TreeMap< Double, String > market = new TreeMap< Double, String >();
         HashMap< String, Double > future = new HashMap< String, Double >();
         HashMap< String, Double > past = new HashMap< String, Double >();
         int pastWindow = 3;
         double q = 0;

         // loop through each stock for that day
         for( String name : names )
         {
            // get the element from the cache or update the cache
            StockElement stock = cache.get( name );
            if( stock == null )
            {
               stock = getStockElement( name );
               cache.put( name, stock );
            }

            // get the data we need to work with
            ArrayList< Double > stockCloses = stock.getAllCloses();
            int offset = -1;

            // now find where this stock's data matches up with the reference data
            for( int j=0; j<stockCloses.size(); j++ )
            {
               if( stock.getDate( j ).trim().equalsIgnoreCase( reference.getDate( i ).trim() ) )
               {
                  offset = j;
                  break;
               }
            }

            if( offset == -1 || (offset - window) < 0 || stockCloses.size() < (offset + hold) )
            {
               continue;
            }

            double p = stockCloses.get( offset ) / stockCloses.get( offset - window );
            double f = stockCloses.get( offset ) / stockCloses.get( offset + hold );
            double h = 0;
            if( (offset - pastWindow) > -1 )
            {
               h = stockCloses.get( offset ) / stockCloses.get( offset - pastWindow );
            }
            market.put( p, name );
            future.put( name, f );
            past.put( name, h );
         }
         Collection< Double > keys = market.keySet();
         int mid = (int)(keys.size() / 2);
         double median = 0;
         double lowest = 0;
         double highest = 0;
         boolean setLowest = false;
         int c = 0;
         for( double d : keys )
         {
            if( !setLowest )
            {
               lowest = d;
               setLowest = true;
            }
            if( c++ == mid )
            {
               median = d;
            }
            highest = d;
         }
         String lowStock = market.get( lowest );
         String highStock = market.get( highest );
         double p = future.get( highStock );
         double h = past.get( highStock );
         double l = 1 / p;
         if( (i - pastWindow) > -1 )
         {
            q = refCloses.get( i ) / refCloses.get( i - pastWindow );
         }
//          System.out.println( "median:, " + median + " , " + highStock + " , " + highest + " , " + p
//                              + " , " + h + " , " + q );
         double shortt = cash * shortRatio;
         double longg = cash - shortt;
         shortt *= p;
         shortt -= 10;
         longg *= l;
         longg -= 10;
         cash = ( longg + shortt );
//          System.out.println( reference.getDate( i ) + " ,cash:, " + cash );
         System.out.println( reference.getDate( i ) + " ,cash:, " + cash + " ,stock:, " + highStock + " ,past:, " + h );
         futures.add( p );
      }

//       System.out.println( "cash is:, " + cash );
      StatUtilities s = new StatUtilities();
      s.calculateStats( futures );
      result[0] = cash;
      result[1] = s.getMedian();

      return result;
   }

   /**
    * Since 1999, the following numbers worked for the gravity theory:
    * Short Highs: 2, 9, 0.7 == 4.21E+31
    * Long  Highs: 1, 9, 0.6 == 5.72E+26
    */
   public void calculateLiquidMovers()
   {
      ArrayList< String > names =_theStockCollection.getLiquidStockNames();
      double largestGain = -1 * Double.MAX_VALUE;
      double largestLoss = Double.MAX_VALUE;
      StockElement largestElement = null;
      StockElement lowestElement = null;

      for( String name : names )
      {
         StockElement stock = getAmendedStockElement( name );
         ArrayList< Double > stockCloses = stock.getAllCloses();
         int size = stockCloses.size() - 1;
         double s = ( stockCloses.get( size ) / stockCloses.get( size - 2 ) );
         double l = ( stockCloses.get( size ) / stockCloses.get( size - 1 ) );

         if( l > largestGain )
         {
            largestGain = l;
            largestElement = stock;
         }
         else if( s < largestLoss )
         {
            largestLoss = s;
            lowestElement = stock;
         }
      }

      System.out.println( "The largest gainer is: " + largestElement.getName() );
      System.out.println( "with a gain of: " + largestGain );
      System.out.println( "The largest looser is: " + lowestElement.getName() );
      System.out.println( "with a loss of: " + largestLoss );
   }

   /**
    * 
    * 
    */
   public void calculateSpreadRebound()
   {
      ArrayList< String > names        = _theStockCollection.getLiquidStockNames();
      ArrayList< Double > transactions = new ArrayList< Double >();

      for( String name : names )
      {
         StockElement stock = getStockElement( name );
         double bestCash = -1 * Double.MAX_VALUE;
         double low = 0;
         double open = 0;
         double high = 0;
         double close = 0;
         double entry = 0;
         double ratio = 0;
         double longFloor = 0;
         double shortFloor = 0;
         double longCeiling = 0;
         double shortCeiling = 0;
         double transaction = 0;
         double bestLongFloor = 0;
         double bestShortFloor = 0;
         double bestLongCeiling = 0;
         double bestShortCeiling = 0;
         for( longFloor=1.0; longFloor<2.0; longFloor+=0.1 )
         {
            for( longCeiling=longFloor; longCeiling<2.0; longCeiling+=0.1 )
            {
               for( shortFloor=1.0; shortFloor>0.5; shortFloor-=0.1 )
               {
                  for( shortCeiling=shortFloor; shortCeiling>0.5; shortCeiling-=0.1 )
                  {
                     double cash = 10000;
                     for( int i=1; i<stock.getNumberOfDays() - 1; i++ )
                     {
                        entry = stock.getClose( i - 1 );
                        open = stock.getOpen( i );
                        ratio = open / entry;

                        // we're in the long exit scenario and short rebound
                        if( ratio > longFloor && ratio < longCeiling )
                        {
                           transaction = performBuy( (cash / 2), entry, open );
//                            transactions.add( transaction );
                           cash += transaction;
                           low = stock.getLow( i );
//                            System.out.println( name + " ,1trans:, " + transaction );
                           if( low <= entry )
                           {
                              transaction = performBuy( (cash / 2), entry, entry );
//                               transactions.add( transaction );
                              cash += transaction;
//                               System.out.println( name + " ,2trans:, " + transaction );
                           }
                           else
                           {
                              close = stock.getClose( i + 1 ); // could change this to the day after...
                              transaction = performBuy( (cash / 2), close, entry );
//                               transactions.add( transaction );
                              cash += transaction;
//                               System.out.println( name + " ,3trans:, " + transaction );
                           }
                        }
                        // we're in the short exit scenario and long rebound
                        else if( ratio < shortFloor && ratio > shortCeiling )
                        {
                           transaction = performBuy( (cash / 2), open, entry );
//                            transactions.add( transaction );
                           cash += transaction;
                           high = stock.getHigh( i );
//                            System.out.println( name + " ,4trans:, " + transaction );
                           if( high >= entry )
                           {
                              transaction = performBuy( (cash / 2), entry, entry );
//                               transactions.add( transaction );
                              cash += transaction;
//                               System.out.println( name + " ,5trans:, " + transaction );
                           }
                           else
                           {
                              close = stock.getClose( i + 1 ); // could change this to the day after...
                              transaction = performBuy( (cash / 2), entry, close );
//                               transactions.add( transaction );
                              cash += transaction;
//                               System.out.println( name + " ,6trans:, " + transaction );
                           }
                        }
                        // otherwise, we just exit both positions
                        else
                        {
                           transaction = performBuy( (cash / 2), entry, open );
//                            transactions.add( transaction );
                           cash += transaction;
                           transaction = performBuy( (cash / 2), open, entry );
//                            transactions.add( transaction );
                           cash += transaction;
                        }
                     }
                     if( cash > bestCash )
                     {
                        bestCash = cash;
                        bestLongFloor = longFloor;
                        bestLongCeiling = longCeiling;
                        bestShortFloor = shortFloor;
                        bestShortCeiling = shortCeiling;
                     }
                  }
               }
            }
         }
//          StatUtilities s = new StatUtilities();
//          s.calculateStats( transactions );
         System.out.println( name + " ,cash:, " + bestCash + " ,longFloor:, " + bestLongFloor + " ,longCeiling:, " +
                             bestLongCeiling + " ,shortFloor:, " + bestShortFloor + " ,shortCeiling:, " + 
                             bestShortCeiling );
//                              bestShortCeiling + " ,medianTrans:, " + s.getMedian() );
      }
   }

   /**
    *
    */
   private double[] mapIntoArray( ArrayList< Double > list )
   {
      double[] a = new double[ list.size() ];
      list = (new MathUtilities()).normalize( list, 1 );
      for( int i=0; i<list.size(); i++ )
      {
         a[i] = list.get( i );
      }
      return a;
   }

   /**
    * 
    * 
    */
   public void calculateVolatilityStats( int pastWindow, int futureWindow )
   {
      ArrayList< String > names = _theStockCollection.getLiquidStockNames();

      for( String name : names )
      {
         StockElement stock = getStockElement( name );
         for( int i=pastWindow+1; i<(stock.getNumberOfDays() - futureWindow); i+=futureWindow )
         {
            ArrayList< Double > all = new ArrayList< Double >();
            ArrayList< Double > gains = new ArrayList< Double >();
            ArrayList< Double > losses = new ArrayList< Double >();
            // first, calculate deviation, entropy
            for( int j=(i - pastWindow); j<(i+1); j++ )
            {
               double p = stock.getClose( j ) / stock.getClose( j - 1 );
               all.add( p );
               if( p >= 1.0 )
               {
                  gains.add( p );
               }
               else
               {
                  losses.add( p );
               }
            }
            double[] a = mapIntoArray( all );
            System.out.print( name );

            ApproximateEntropy entropy = new ApproximateEntropy( a, 1, 0.05 );
            System.out.print( " ,all-1-0.05, " );
            System.out.print( entropy.getEntropy() );

            entropy = new ApproximateEntropy( a, 5, 0.05 );
            System.out.print( " ,all-5-0.05, " );
            System.out.print( entropy.getEntropy() );

            entropy = new ApproximateEntropy( a, 1, 0.5 );
            System.out.print( " ,all-1-0.5, " );
            System.out.print( entropy.getEntropy() );

            entropy = new ApproximateEntropy( a, 5, 0.5 );
            System.out.print( " ,all-5-0.5, " );
            System.out.print( entropy.getEntropy() );

            entropy = new ApproximateEntropy( a, 1, 0.001 );
            System.out.print( " ,all-1-0.001, " );
            System.out.print( entropy.getEntropy() );

            entropy = new ApproximateEntropy( a, 5, 0.001 );
            System.out.print( " ,all-5-0.001, " );
            System.out.print( entropy.getEntropy() );

            StatUtilities s = new StatUtilities();
            s.calculateStats( a );
            System.out.print( " ,all-deviation, " );
            System.out.print( s.getDeviation() );

            a = mapIntoArray( gains );

            entropy = new ApproximateEntropy( a, 1, 0.05 );
            System.out.print( " ,gain-1-0.05, " );
            System.out.print( entropy.getEntropy() );

            entropy = new ApproximateEntropy( a, 5, 0.05 );
            System.out.print( " ,gain-5-0.05, " );
            System.out.print( entropy.getEntropy() );

            entropy = new ApproximateEntropy( a, 1, 0.5 );
            System.out.print( " ,gain-1-0.5, " );
            System.out.print( entropy.getEntropy() );

            entropy = new ApproximateEntropy( a, 5, 0.5 );
            System.out.print( " ,gain-5-0.5, " );
            System.out.print( entropy.getEntropy() );

            entropy = new ApproximateEntropy( a, 1, 0.001 );
            System.out.print( " ,gain-1-0.001, " );
            System.out.print( entropy.getEntropy() );

            entropy = new ApproximateEntropy( a, 5, 0.001 );
            System.out.print( " ,gain-5-0.001, " );
            System.out.print( entropy.getEntropy() );

            s = new StatUtilities();
            s.calculateStats( a );
            System.out.print( " ,gain-deviation, " );
            System.out.print( s.getDeviation() );

            a = mapIntoArray( losses );

            entropy = new ApproximateEntropy( a, 1, 0.05 );
            System.out.print( " ,loss-1-0.05, " );
            System.out.print( entropy.getEntropy() );

            entropy = new ApproximateEntropy( a, 5, 0.05 );
            System.out.print( " ,loss-5-0.05, " );
            System.out.print( entropy.getEntropy() );

            entropy = new ApproximateEntropy( a, 1, 0.5 );
            System.out.print( " ,loss-1-0.5, " );
            System.out.print( entropy.getEntropy() );

            entropy = new ApproximateEntropy( a, 5, 0.5 );
            System.out.print( " ,loss-5-0.5, " );
            System.out.print( entropy.getEntropy() );

            entropy = new ApproximateEntropy( a, 1, 0.001 );
            System.out.print( " ,loss-1-0.001, " );
            System.out.print( entropy.getEntropy() );

            entropy = new ApproximateEntropy( a, 5, 0.001 );
            System.out.print( " ,loss-5-0.001, " );
            System.out.print( entropy.getEntropy() );

            s = new StatUtilities();
            s.calculateStats( a );
            System.out.print( " ,loss-deviation, " );
            System.out.print( s.getDeviation() );

            // now get data on futures
            ArrayList< Double > f = new ArrayList< Double >();
            for( int j=i; j<(i + futureWindow); j++ )
            {
               f.add( stock.getClose( j ) );
            }
            System.out.print( " ,closeRatio, " );
            System.out.print( stock.getClose( i + futureWindow ) / stock.getClose( i ) );
            System.out.print( " ,highRatio, " );
            double high = Collections.max( f );
            System.out.print( high / stock.getClose( i ) );
            System.out.print( " ,lowRatio, " );
            double low = Collections.min( f );
            System.out.print( low / stock.getClose( i ) );
            System.out.println( " ,date, " + stock.getDate( i ) );
         }
      }
   }

   /**
    * Distribution theory.
    * Since the stock movements are obviously correlated at each time instant even though each stock
    * is affected proportionally, there should be some predictable statistics falling out of the
    * distributions for large time frames (law of large numbers).
    * For instance, if we look at the high/low/close ratios for all the stocks over a large time frame, 
    * each time frame's distribution should be uniform within a margin of error.
    * Then, it might be the case that we can look at how these distributions evolve over time and 
    * predict when the overall high/low has been hit for the collection as a whole -- which then implies
    * that any sample of stocks will have most likely hit near its high/low perforce.
    * So, we have several questions that must be answered:
    *  1) Are the high/low distributions uniform over large time frames (large sample sizes)?
    *  2) Do most stocks hit their respective high/low at generally the same time within margin of error?
    *  3) If question (1) is true, what is the smallest time frame to achieve uniformity?
    * Thus, if (1) and (2) are true, then we can invest in a random portfolio, and then make exit and entry
    * decisions based on the distribution of the market as a whole, and (3) can optimize that strategy.
    * If all this is true, then it seems that there is a fractal componenet to all this -- some sort of
    * "fractalness" seems apropos here...just not sure how. Perhaps it's the self-similar nature of the
    * movements over time frame scales, or the coordinated movements amongst the stocks at any given moment 
    * of time over proportion scales.
    */
   public void calculateDistribution( int window )
   {
      StockElement                            reference  = getStockElement( "QQQ" );
      ArrayList< Double >                     refCloses  = reference.getAllCloses();
      ArrayList< String >                     names      = _theStockCollection.getLiquidStockNames();
      HashMap< String, StockElement >         cache      = new HashMap< String, StockElement >();
      HashMap< Integer, ArrayList< Double > > hDistros   = new HashMap< Integer, ArrayList< Double > >();
      HashMap< Integer, ArrayList< Double > > lDistros   = new HashMap< Integer, ArrayList< Double > >();
      HashMap< Integer, HashMap< Integer, Integer > > hPositions 
         = new HashMap< Integer, HashMap< Integer, Integer > >();
      HashMap< Integer, HashMap< Integer, Integer > > lPositions 
         = new HashMap< Integer, HashMap< Integer, Integer > >();
      int windowCount = 0;
      int stockCount = 0;

      // loop through each day in the reference security
      for( int i=0; i<refCloses.size() - window; i += window )
      {
         ArrayList< Double > highs = new ArrayList< Double >();
         ArrayList< Double > lows = new ArrayList< Double >();
         HashMap< Integer, Integer > highPos = new HashMap< Integer, Integer >();
         HashMap< Integer, Integer > lowPos = new HashMap< Integer, Integer >();
         ArrayList< Double > pos = new ArrayList< Double >();
         stockCount = 0;

         for( int j=0; j<window; j++ )
         {
            highPos.put( j, 0 );
            lowPos.put( j, 0 );
         }

         // loop through each stock for that day
         for( String name : names )
         {
            // get the element from the cache or update the cache
            StockElement stock = cache.get( name );
            if( stock == null )
            {
               stock = getStockElement( name );
               cache.put( name, stock );
            }

            // get the data we need to work with
            ArrayList< Double > stockCloses = stock.getAllCloses();
            int offset = -1;

            if( stockCloses.size() < refCloses.size() )
            {
               continue;
            }

            // now find where this stock's data matches up with the reference data
            for( int j=0; j<stockCloses.size(); j++ )
            {
               if( stock.getDate( j ).trim().equalsIgnoreCase( reference.getDate( i ).trim() ) )
               {
                  offset = j;
                  break;
               }
            }

            if( offset == -1 )
            {
               continue;
            }

            double open = stockCloses.get( offset );
            double high = 1;
            double low = 1;
            double close = 0;
            double t = 0;
            int hp = 1;
            int lp = 1;

            // find the high and the low for this window
            for( int j=1; j<window; j++ )
            {
               close = stockCloses.get( offset + j );
               if( close > open )
               {
                  t = close / open;
                  if( t > high )
                  {
                     high = t;
                     hp = j;
                     highPos.put( j, (highPos.get( j ) + 1) );
                  }
               }
               else if( open > close )
               {
                  t = open / close;
                  if( t > low )
                  {
                     low = t;
                     lp = j;
                     lowPos.put( j, (lowPos.get( j ) + 1) );
                  }
               }
            }

            highs.add( high );
            lows.add( low );
            stockCount++;
         } // for all stock names
         Collections.sort( highs );
         Collections.sort( lows );
         hDistros.put( windowCount, highs );
         lDistros.put( windowCount, lows );
         hPositions.put( windowCount, highPos );
         lPositions.put( windowCount, lowPos );
         windowCount++;
      } // for a reference prices

      System.out.println( "highs..." );
      Set< Integer > keys = hDistros.keySet();
      for( int i=0; i<(stockCount-1); i++ )
      {
         for( Integer key : keys )
         {
            ArrayList< Double > h = hDistros.get( key );
            System.out.print( h.get( i ) + "," );
         }
         System.out.println( "" );
      }
      
      System.out.println( "" );
      System.out.println( "lows..." );
      keys = lDistros.keySet();
      for( int i=0; i<(stockCount-1); i++ )
      {
         for( Integer key : keys )
         {
            ArrayList< Double > l = lDistros.get( key );
            System.out.print( l.get( i ) + "," );
         }
         System.out.println( "" );
      }
      
      System.out.println( "" );
      System.out.println( "high positions..." );
      keys = hPositions.keySet();
      for( int i=1; i<(window); i++ )
      {
         for( Integer key : keys )
         {
            HashMap< Integer, Integer > h = hPositions.get( key );
            System.out.print( h.get( i ) + "," );
         }
         System.out.println( "" );
      }
      
      System.out.println( "" );
      System.out.println( "low positions..." );
      keys = lPositions.keySet();
      for( int i=1; i<(window); i++ )
      {
         for( Integer key : keys )
         {
            HashMap< Integer, Integer > l = lPositions.get( key );
            System.out.print( l.get( i ) + "," );
         }
         System.out.println( "" );
      }
   }

   /**
    * 
    * 
    */
   public void calculatePremiums( String month, String year )
   {
      ArrayList< String > names = _theStockCollection.getLiquidStockNames();

      System.out.println( "stock, current price, strike price, OOTM strike, call, OOTM call, payout, OOTM payout" );
      for( String name : names )
      {
         WebPageReader page = new WebPageReader();
         page.getContent( name.toLowerCase(), month, year );
         System.out.println( name + "," + page.getCurrentPrice() + "," + page.getStrikePrice() + "," +
                             page.getOOTMStrikePrice() + "," + page.getCallPremium() + "," +
                             page.getOOTMCallPremium() + "," + page.getPayout() + "," +
                             page.getOOTMPayout() );
      }
   }

   /**
    *
    */
   public ArrayList< Double > loadGenotype( String fileName )
   {
      common.FileReader file = new common.FileReader( fileName, " " );
      String[] genes = file.getArrayOfWords();
      ArrayList< Double > genotype = new ArrayList< Double >();

      for( String gene : genes )
      {
         try
         {
            genotype.add( Double.parseDouble( gene ) );
//             System.out.println( "gene is: " + gene );
         }
         catch( NumberFormatException e )
         {
            System.err.println( "unable to load genome: " + e );
         }
      }

      return genotype;
   }

   /**
    * First half domain is [0, 0.499934443] 
    * Second half domain is [0.500065557, 1]
    * 
    */
   public void simulate( int days )
   {
      /*
//       System.out.println( "loading first genome..." );
      ArrayList< Double > g1 = loadGenotype( "genome-1.dat" );
      FunctionalCorrelation f1 = FunctionalCorrelation.buildBinaryTree( g1 );
//       System.out.println( "loading second genome..." );
      ArrayList< Double > g2 = loadGenotype( "genome-2.dat" );
      FunctionalCorrelation f2 = FunctionalCorrelation.buildBinaryTree( g2 );
      double midpoint = 0.499934443;
//       double price = 62.5; // first IBM price point
      double price = 7.34; // first IBM price point adjusted for splits
      double[][] data = new double[ 1 ][ 1 ];
      
      for( int i=0; i<days; i++ )
      {
         double r = Math.random();
         double p = 0;

//          System.out.println( "random: " + r );

         if( r < midpoint )
         {
            r = Math.random();
//             r /= midpoint;
//             System.out.println( "random (1): " + r );
            data[0][0] = r;
            f1.setDataStreams( data );
            p = f1.evaluate( 0 );
//             System.out.println( "p (1): " + p );
            p *= 0.23497017;
            p += 0.76502983;
//             System.out.println( "p (1)': " + p );
         }
         else
         {
            r = Math.random();
//             r -= midpoint;
//             r /= midpoint;
//             System.out.println( "random (2): " + r );
            data[0][0] = r;
            f2.setDataStreams( data );
            p = f2.evaluate( 0 );
//             System.out.println( "p (2): " + p );
            p *= 0.131699048;
            p += 1;
//             System.out.println( "p (2)': " + p );
         }
         price *= p;
         System.out.println( price );
      }
      */
   }

   /**
    * This method will compute the Uniformly Random Distribution Convertible functions (data points) 
    * for the given stock symbol. UniCon(tions).
    * This will calculate family of probability functions for the one-month option time-frame.
    * Each trading year contains about 252 trading days. With 12 months of trading, that means
    * there is ~21 day trading month days.
    * So, do we want to end up with 20 unicon functions, or should we step weekly to end up with
    * only 4 unicons per stock? The probabilities shouldn't change that much between each day
    * that much.
    * The resulting unicontions should allow us to create models to simulate the underlying phenomena,
    * and to answer questions such as: what is the probability that the stock price will reach X in the
    * next Y days?
    *
    * The strategy is as follows:
    * 1) Step through all stock data by the window size (which should be by the month)
    * 2) For each window-size of data points:
    * 3)   
    *
    *
    */
   public void calculateMonthlyUnicon( String name )
   {
      // Get the static data that will hold for all loops
      ArrayList< ArrayList< Double > > unicons = new ArrayList< ArrayList< Double > >();
      StockElement        stock   = getStockElement( name );
      int                 step    = 5;

      // loop through our window sizes
      for( int i=5; i<25; i+=step )
      {
         // Data that can change on each iterative loop
         ArrayList< Double > rates = new ArrayList< Double >();

         // loop through 
         for( int j=0; j<(stock.getSize() - i); j+=i )
         {
            double current = 0;
            double open = stock.getOpen( j );
            double maxLow  = Double.MAX_VALUE;
            double maxHigh = -1 * Double.MAX_VALUE;

            for( int k=0; k<i; k++ )
            {
               current = stock.getClose( j + k );

               if( current > maxHigh )
               {
                  maxHigh = current;
               }
               else if( current < maxLow )
               {
                  maxLow = current;
               }
            }
            rates.add( (maxHigh / open) );
            rates.add( (maxLow / open) );
//             if( (maxHigh / open) > 10 )
//             {
//                System.out.println( "high: " + maxHigh + " open: " + open + " ratio: " + (maxHigh / open) );
//             }
         }

         unicons.add( rates );
      }

      for( ArrayList< Double > list : unicons )
      {
         Collections.sort( list );
         System.out.println( "" );
         System.out.println( "UniCon:" );
         for( double d : list )
         {
            System.out.println( d );
         }
      }
   }

   /**
    *
    */
   public void calculateSwings( int days )
   {
      ArrayList< String > names = _theStockCollection.getLiquidStockNames();

      for( String name : names )
      {
         StockElement stock = getStockElement( name );
         ArrayList< Double > totalSwings = new ArrayList< Double > ();

         for( int i=0; i<(stock.getSize() - days); i+=days )
         {
            double current = 0;
            double open = stock.getOpen( i );
            double strike = Math.ceil( open );
            double previousClose = open;
            double high = 0;
            double low = 0;
            double close = 0;
            int swings = 0;
//             System.out.println( "i: " + i );

            for( int k=0; k<days; k++ )
            {
//                System.out.println( "k: " + k );
               current = stock.getOpen( i + k );

               if( previousClose < strike && current > strike )
               {
                  swings++;
//                   System.out.println( "strike: " + strike + " prevClose: " + previousClose + " current: " + current );
               }
               else if( previousClose > strike && current < strike )
               {
                  swings++;
//                   System.out.println( "strike: " + strike + " prevClose: " + previousClose + " current: " + current );
               }

               if( current < strike )
               {
                  high = stock.getHigh( i + k );
                  if( high > strike )
                  {
                     swings++;
//                      System.out.println( "strike: " + strike + " high: " + high + " current: " + current );
                     close = stock.getClose( i + k );
                     if( close < strike )
                     {
                        swings++;
//                         System.out.println( "strike: " + strike + " close(1): " + close + " current: " + current );
                     }
                  }
               }
               else if( current > strike )
               {
                  low = stock.getLow( i + k );
                  if( low < strike )
                  {
                     swings++;
//                      System.out.println( "strike: " + strike + " low: " + low + " current: " + current );
                     close = stock.getClose( i + k );
                     if( close > strike )
                     {
                        swings++;
//                         System.out.println( "strike: " + strike + " close: " + close + " current: " + current );
                     }
                  }
               }

               previousClose = stock.getClose( i + k );
            }
            totalSwings.add( 0.0 + swings );
         }
         StatUtilities s = new StatUtilities();
         s.calculateStats( totalSwings );
         System.out.println( name + " ,mean, " + s.getMean() + " ,median, " + 
                             s.getMedian() + " ,stdev, " + s.getDeviation() );
      }
   }

   /**
    * This method will perform a variant of the log optimal strategy on the given stock and a riskless
    * asset (cash).
    *
    *
    */
   public void logOptimalOne( int days )
   {
      ArrayList< String > names = _theStockCollection.getLiquidStockNames();

      for( String name : names )
      {
         StockElement stock = getStockElement( name );
         ArrayList< Double > totalSwings = new ArrayList< Double > ();
         double cash = 10000;
         int shares = 0;
         int count = 0;
         double open = 0;
         double previousOpen = 0;
         double wealth = 0;

//          for( int i=0; i<(stock.getSize() - days); i+=days )
         for( int i=((int)(stock.getSize() / 2)); i<(stock.getSize() - days); i+=days )
         {
            count++;
            // first, determine how much can be invested
//             open = stock.getOpen( i );
            open = stock.getAdjClose( i );
            if( previousOpen != 0.0 )
            {
               ArrayList< Double > unicon = stock.getUnicon( 0, i, days );
               double ratio = (open / previousOpen);
               int c = 0;
//                System.out.println( name + " i: " + i + " size: " + unicon.size() );
               double u = 0;

               while( ratio >= u )
               {
                  u = unicon.get( c++ );
//                   System.out.println( "ratio: " + ratio + " u: " + u );
                  if( c == unicon.size() )
                  {
                     break;
                  }
//                   if( ratio >= u )
//                   {
//                      System.out.println( "ratio: " + ratio + " u: " + u + " c: " + c );
//                      break;
//                   }
               }
               double p = (double)c / (double)unicon.size();

               // if our positions have gone up, sell some
               if( p >= 0.9 )
//                if( wealth > cash )
               {
//                   System.out.println( "p is greater than: " + p );
                  wealth = shares * open;
                  double amount = ( wealth - cash ) * 0.5;
                  int tshares = (int) ( amount / open );
                  cash += ( tshares * open );
                  shares -= tshares;
                  cash -= 5; // commisions
               }
               // otherwise, we buy more
               else if( p <= 0.1 )
               {
//                   System.out.println( "p is less than: " + p );
                  wealth = shares * open;
                  double amount = (cash - wealth) * 0.5;
                  int tshares = (int) ( amount / open );
                  cash -= ( tshares * open );
                  shares += tshares;
                  cash -= 5; // commisions
               }
            }
            previousOpen = open;
         }
         // divest at the end
         cash += wealth;
         System.out.println( name + " , " + cash + " , " + count );
      }
   }

   /**
    * This method will perform a variant of the log optimal strategy on the given stock and a riskless
    * asset (cash).
    *
    *
    */
   public void logOptimalTwo( int days )
   {
      ArrayList< String > names = _theStockCollection.getLiquidStockNames();

      for( String name : names )
      {
         StockElement stock = getStockElement( name );
         ArrayList< Double > totalSwings = new ArrayList< Double > ();
         double cash = 10000;
//          double cash = 100000;
         int shares = 0;
         int count = 0;
         double open = 0;
         double previousOpen = 0;
         double wealth = 0;

         for( int i=(stock.getSize() - days); i<stock.getSize(); i++ )
         {
            count++;
            // first, determine how much can be invested
//             open = stock.getOpen( i );
            open = stock.getAdjClose( i );
            if( previousOpen != 0.0 )
            {
               wealth = shares * open;

               // if our positions have gone up, sell some
               if( wealth > cash )
               {
                  double amount = ( wealth - cash ) * 0.5;
                  int tshares = (int) ( amount / open );
                  cash += ( tshares * open );
                  shares -= tshares;
                  cash -= 5; // commisions
               }
               // otherwise, we buy more
               else
               {
                  double amount = (cash - wealth) * 0.5;
                  int tshares = (int) ( amount / open );
                  cash -= ( tshares * open );
                  shares += tshares;
                  cash -= 5; // commisions
               }
            }
            previousOpen = open;
         }
         // divest at the end
         cash += wealth;
         System.out.println( name + " , " + cash + " , " + count );
      }
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void calculateMeanRates()
   {
//       ArrayList< String > names = _theStockCollection.getLiquidStockNames();
//       ArrayList< String > names = _theStockCollection.getSP500StockNames();
      ArrayList< String > names = _theStockCollection.getNasdaq100StockNames();
      ArrayList< Double > weekendRates = new ArrayList< Double >();
      ArrayList< Double > weekdayRates = new ArrayList< Double >();

      for( String name : names )
      {
         StockElement stock = getStockElement( name );
         for( int i=1; i<(stock.getSize() - 2); i++ )
         {
//             System.out.println( "date is: " + stock.getDate( i ) );
            int currentDay = Integer.parseInt( stock.getDate( i ).substring( 8 ) );
//             System.out.println( "currentDay is: " + currentDay );

//             System.out.println( "next date is: " + stock.getDate( i + 1 ) );
            int nextDay = Integer.parseInt( stock.getDate( i + 1 ).substring( 8 ) );
//             System.out.println( "next day is: " + nextDay );

            double close = stock.getClose( i );
            double nextClose = stock.getClose( i + 1 );
            double ccc = nextClose / close;
            if( ccc < 1 )
            {
               ccc = 1 / ccc;
            }

            if( ccc >= 1.10 )
            {
               if( Math.abs( currentDay - nextDay ) == 1 )
               {
                  weekdayRates.add( ccc );
               }
               else
               {
                  weekendRates.add( ccc );
               }
            }
         }
      }

      StatUtilities weekend = new StatUtilities( weekendRates );
      StatUtilities weekday = new StatUtilities( weekdayRates );
      System.out.println( "weekend mean: " + weekend.getMean() + " median: " + 
                                             weekend.getMedian() + " weekend deviation: " + weekend.getDeviation() +
                          " weekday mean: " + weekday.getMean() + " median: " + 
                                              weekday.getMedian() + " weekday deviation: " + weekday.getDeviation() );
   }

   /**
    * Here, we calculate the average movement, either low or high, for the day after
    * for stocks that were the biggest gainers or loosers.
    *
    * @param TYPE
    * @return TYPE
    */
   public void outputLargeMovers()
   {
      ArrayList< String > names = _theStockCollection.getLiquidStockNames();
//       ArrayList< String > names = _theStockCollection.getSP500StockNames();
//       ArrayList< String > names = _theStockCollection.getNasdaq100StockNames();
      HashMap< String, StockElement > cache = new HashMap< String, StockElement >();
      int startPos = 10000000;
      int daysOut = 4;
      int daysBack = 2;

      // First, we need to figure out how far back we can start comparing stocks:
      for( String name : names )
      {
         StockElement stock = getStockElement( name );
         cache.put( name, stock );
         if( stock.getSize() < startPos )
         {
            startPos = stock.getSize();
            System.out.println( "set new start position: " + startPos + " for: " + stock.getName() );
         }
      }

      System.out.println( "max delta, spread, high, low, day close, next day close, prev vol" );
      double max = -1 * Double.MAX_VALUE;
      double max_s = 0;
      double max_h = 0;
      double max_l = 0;
      double max_c = 0;
      double max_ccc = 0;
      double max_vol = 0;

      for( int i=daysBack; i<(startPos - daysOut); i++ )
      {
         max = -1 * Double.MAX_VALUE;
         max_s = 0;
         max_h = 0;
         max_l = 0;
         max_c = 0;
         max_ccc = 0;
         max_vol = 0;
         for( String name : names )
         {
            StockElement stock = cache.get( name );
            double close = stock.getClose( i );
            double prevClose = stock.getClose( i - daysBack );
            double prevVol = stock.getVolume( i - daysBack );
            double vol = stock.getVolume( i );
            double cc = close / prevClose;
            double vc = vol / prevVol;
            if( cc < 1 )
            {
               cc = 1 / cc;
            }
            if( vc < 1 )
            {
               vc = 1 / vc;
            }
            if( cc > max )
//             if( vc > max )
            {
               double nextOpen = stock.getOpen( i + daysOut );
               double nextHigh = stock.getHigh( i + daysOut );
               double nextLow = stock.getLow( i + daysOut );
               double nextClose = stock.getClose( i + daysOut );
               double s = nextOpen / close;
               double h = nextHigh / nextOpen;
               double l = nextOpen / nextLow;
               double c = nextClose / nextOpen;
               double ccc = nextClose / close;
               if( s < 1 )
               {
                  s = 1 / s;
               }
               if( c < 1 )
               {
                  c = 1 / c;
               }
               if( ccc < 1 )
               {
                  ccc = 1 / ccc;
               }
               max = cc;
//                max = vc;
               max_s = s;
               max_h = h;
               max_l = l;
               max_c = c;
               max_ccc = ccc;
               max_vol = vol / prevVol;
            }
         }
         System.out.println( max + "," + max_s + "," + max_h + "," + max_l + "," + max_c + "," + max_ccc + 
                             "," + max_vol );
      }
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void getPutOptionPrices()
   {
      WebPageReader page = new WebPageReader();
      page.parsePutOptions( "SPY" );
      WebPageDAO dao = (WebPageDAO) page.getDAO();
      System.out.println( "about to serialize..." );
      dao.serialize( page );
      page = null;
      System.out.println( "about to de-serialize..." );
      page = dao.deserialize();

      ArrayList< OptionDataItem > ootm = page.getOOTMPuts();
      for( OptionDataItem obj : ootm )
      {
         System.out.println( "An OOTM put option: " + obj );
      }
      ArrayList< OptionDataItem > itm = page.getITMPuts();
      for( OptionDataItem obj : itm )
      {
         System.out.println( "An ITM put option: " + obj );
      }
   }

   /**
    * This method will perform basic training, testing, and a prediction of the ManyToManyCorrelation class.
    *
    * @param String -- The date at which to begin the test. Example: "2010-11-02"
    */
   public void testManyToManyLong( String date, String time, String ensembleSize, String genomeSize )
   {
      System.out.println( "The date will be: " + date );
      System.out.println( "The time will be: " + time );
      System.out.println( "The ensembleSize will be: " + ensembleSize );
      System.out.println( "The genomeSize will be: " + genomeSize );
      int trainingSize = 40000;
      int predictSize = 100;
      int stepSize = 20;
      int eSize = 0;
      int gSize = 0;
      int p = 0;
      long trainingTime = (new MathUtilities()).getTime( time );
      ArrayList< String > names = _theStockCollection.getNasdaq100StockNames();
      double[][] testData = new double[ 102 ][];
      double[][] pTestData = new double[ 102 ][];
      Interval[] maps = new Interval[ 102 ];
      names.add( "QQQ" ); // element 100
      names.add( "SPY" );  // element 101
      int k = 0;

      try
      {
         eSize = Integer.parseInt( ensembleSize );
         gSize = Integer.parseInt( genomeSize );
      }
      catch( NumberFormatException e )
      {
         System.err.println( "Unable to read ensemble size: " + e );
      }

      for( String name : names )
      {
         StockElement stock = getStockElement( name );
         if( trainingSize > ( stock.getSize() - predictSize ) )
         {
            trainingSize = ( stock.getSize() - predictSize );
         }
      }

      for( String name : names )
      {
         StockElement stock = getStockElement( name );
         stock.calculatePercentages();
         maps[ p ] = new Interval();
         int start = -1;

         // Now, pull the data to be normalized
         for( int i=0; i<trainingSize; i++ )
         {
            maps[ p ].addDataItem( stock.getCloseP( ( stock.getSize() - trainingSize - predictSize ) + i ) );
            if( stock.getName().equals( "QQQ" ) )
            {
               System.out.println( "QQQ close: " + 
                  stock.getCloseP( ( stock.getSize() - trainingSize - predictSize ) + i ) );
            }
         }

         ArrayList< Double > temp = maps[ p ].getMappedData();
         double[] t = new double[ trainingSize ];
         for( int i=0; i<t.length; i++ )
         {
            t[ i ] = temp.get( i );
            if( stock.getName().equals( "QQQ" ) )
            {
               System.out.println( "QQQ data point: " + t[ i ] );
            }
         }
         testData[ k ] = t;

         // Finally, pull the prediction data
         double[] t4 = new double[ predictSize ];
         for( int i=0; i<t4.length; i++ )
         {
            t4[ i ] = maps[ p ].map( stock.getCloseP( stock.getSize() - predictSize + i ) );
            if( stock.getName().equals( "QQQ" ) )
            {
               System.out.println( "QQQ training prediction close: " + t4[ i ] );
            }
         }
         pTestData[ k++ ] = t4;
         p++;
      }

      HaltingCriteria c = new HaltingCriteria();
      c.setElapsedTimeTolerance( trainingTime );
      ManyToManyCorrelation correlation = new ManyToManyCorrelation( date + "-e" + eSize + "-l" + gSize );
      correlation.setHaltingCriteria( c );
      correlation.setEnsembleSize( eSize );
      correlation.setGenomeLength( gSize );
      correlation.setData( testData );
//       correlation.setSequenceData( testData );
      correlation.createEnsemble();
      correlation.train();
      ManyToManyCorrelation.ManyToManyCorrelationDAO dao = 
         (ManyToManyCorrelation.ManyToManyCorrelationDAO) correlation.getDAO();
      dao.serialize( correlation );

      double[] input = new double[ testData.length ];
      double[] result = null;
      double totalError = 0;
      for( int i=0; i<testData[0].length; i++ )
      {
         double error = 0;
         for( int j=0; j<testData.length; j++ )
         {
            input[ j ] = testData[ j ][ i ];
         }
         if( result != null )
         {
            for( int j=0; j<input.length; j++ )
            {
               error += Math.abs( input[ j ] - result[ j ] );
            }
         }
         totalError += error;
         result = correlation.correlate( input );
      }
      System.out.println( "Total error was: " + totalError );

      totalError = 0;
      System.out.println( "Data to predict:" );
      for( int i=0; i<pTestData[0].length; i++ )
      {
         double error = 0;
         for( int j=0; j<pTestData.length; j++ )
         {
            input[ j ] = pTestData[ j ][ i ];
            System.out.print( pTestData[ j ][ i ] + "," );
         }
         System.out.println( "" );
         totalError += error;
         result = correlation.correlate( input );
      }

   }

   /**
    * This method will perform basic training, testing, and a prediction of the ManyToManyCorrelation class.
    *
    * @param String -- The date at which to begin the test. Example: "2010-11-02"
    */
   public void testManyToMany( String date, String time, String ensembleSize, String genomeSize )
   {
      System.out.println( "The date will be: " + date );
      System.out.println( "The time will be: " + time );
      System.out.println( "The ensembleSize will be: " + ensembleSize );
      System.out.println( "The genomeSize will be: " + genomeSize );
      int trainingSize = 20;
      int predictSize = 1;
      int eSize = 0;
      int gSize = 0;
      long trainingTime = (new MathUtilities()).getTime( time );
      ArrayList< String > names = _theStockCollection.getNasdaq100StockNames();
      double[][] testData = new double[ 102 ][];
      double[][] pTestData = new double[ 102 ][];
      names.add( "QQQ" );
      names.add( "SPY" );
      int k = 0;

      try
      {
         eSize = Integer.parseInt( ensembleSize );
         gSize = Integer.parseInt( genomeSize );
      }
      catch( NumberFormatException e )
      {
         System.err.println( "Unable to read ensemble size: " + e );
      }

      for( String name : names )
      {
         StockElement stock = getStockElement( name );
         stock.calculatePercentages();
         int start = -1;
         for( int i=0; i<stock.getSize(); i++ )
         {
            if( stock.getDate( i ).trim().equalsIgnoreCase( date ) )
            {
               start = i;
               break;
            }
         }
         if( ( start - trainingSize ) < 0 )
         {
            System.out.println( "Unable to find the date or not enough data for stock: " + stock.getName() );
            return;
         }
         double[] t = new double[ (trainingSize) + predictSize ];
         for( int i=0; i<t.length; i++ )
         {
//             t[ i ] = stock.getClose( ( start - trainingSize ) + i );
            t[ i ] = stock.getCloseP( ( start - trainingSize ) + i );
            if( stock.getName().equals( "QQQ" ) )
            {
               System.out.println( "QQQ point: " + t[ i ] );
            }
         }
         double[] t2 = (new MathUtilities()).normalize( t, 1 );
         double[] t3 = new double[ trainingSize ];
         for( int i=0; i<t3.length; i++ )
         {
            t3[ i ] = t2[ i ];
            if( stock.getName().equals( "QQQ" ) )
            {
               System.out.println( "QQQ data point: " + t3[ i ] );
            }
         }
         testData[ k ] = t3;
         double[] t4 = new double[ predictSize ];
         for( int i=0; i<t4.length; i++ )
         {
            t4[ i ] = t2[ (trainingSize) + i ];
         }
         pTestData[ k++ ] = t4;
      }

      HaltingCriteria c = new HaltingCriteria();
      c.setElapsedTimeTolerance( trainingTime );
      ManyToManyCorrelation correlation = new ManyToManyCorrelation( date + "-e" + eSize + "-l" + gSize );
      correlation.setHaltingCriteria( c );
      correlation.setEnsembleSize( eSize );
      correlation.setGenomeLength( gSize );
//       correlation.setSequenceData( testData );
      correlation.setData( testData );
      correlation.createEnsemble();
      correlation.train();
      ManyToManyCorrelation.ManyToManyCorrelationDAO dao = 
         (ManyToManyCorrelation.ManyToManyCorrelationDAO) correlation.getDAO();
      dao.serialize( correlation );

      double[] input = new double[ testData.length ];
      double[] result = null;
      double totalError = 0;
      for( int i=0; i<testData[0].length; i++ )
      {
         double error = 0;
         for( int j=0; j<testData.length; j++ )
         {
            input[ j ] = testData[ j ][ i ];
         }
         if( result != null )
         {
            for( int j=0; j<input.length; j++ )
            {
               error += Math.abs( input[ j ] - result[ j ] );
            }
         }
         totalError += error;
         result = correlation.correlate( input );
      }
      System.out.println( "Total error was: " + totalError );
      totalError = 0;
      System.out.println( "Last data point:" );
      for( int i=0; i<result.length; i++ )
      {
         System.out.print( input[ i ] + "," );
         if( result[ i ] > -1 && result[ i ] < 2 )
         {
            // do nothing
         }
         else
         {
            result[ i ] = 0;
         }
         totalError += Math.abs( result[ i ] - pTestData[ i ][ 0 ] );
      }
      System.out.println( "\nPrediction error was: " + totalError );

      System.out.println( "Data to predict:" );
      for( int i=0; i<pTestData[0].length; i++ )
      {
         for( int j=0; j<pTestData.length; j++ )
         {
            System.out.print( pTestData[ j ][ i ] + "," );
         }
         System.out.println( "" );
      }
      double[][] predicted = correlation.predict( 3, input );
      System.out.println( "Predicted data:" );
      for( int i=0; i<predictSize; i++ )
      {
         for( int j=0; j<predicted[ i ].length; j++ )
         {
            System.out.print( predicted[ i ][ j ] + "," );
         }
         System.out.println( "" );
      }
   }

   /**
    * This method will perform basic training, testing, and a prediction of the ManyToManyCorrelation class.
    *
    * @param String -- The date at which to begin the test. Example: "2010-11-02"
   public void currentManyToFew( String date, String time, String ensembleSize, String genomeSize )
   {
      System.out.println( "The date will be: " + date );
      System.out.println( "The time will be: " + time );
      System.out.println( "The ensembleSize will be: " + ensembleSize );
      System.out.println( "The genomeSize will be: " + genomeSize );
      int trainingSize = 20000;
      int predictSize = 1;
      int eSize = 0;
      int gSize = 0;
      int targets = 1;
      long trainingTime = (new MathUtilities()).getTime( time );
      ArrayList< String > names = _theStockCollection.getNasdaq100StockNames();
      double[][] testData = new double[ 100 ][];
      double[][] targetData = new double[ targets ][];
      double[][] pTestData = new double[ 100 ][];
      double[][] pTargetData = new double[ targets ][];
      Interval[] maps = new Interval[ 100 ];
      Interval[] targetMaps = new Interval[ targets ];
      ArrayList< String > targetNames = new ArrayList< String >();
      targetNames.add( "QQQ" );
      int k = 0;
      int p = 0;

      try
      {
         eSize = Integer.parseInt( ensembleSize );
         gSize = Integer.parseInt( genomeSize );
         predictSize = -1 * Integer.parseInt( date );
      }
      catch( NumberFormatException e )
      {
         System.err.println( "Unable to read ensemble size: " + e );
      }

      for( String name : names )
      {
         StockElement stock = getStockElement( name );
         if( trainingSize > ( stock.getSize() - predictSize ) )
         {
            trainingSize = ( stock.getSize() - predictSize );
         }
      }
      System.out.println( "trainingSize is: " + trainingSize );
      System.out.println( "predictSize is: " + predictSize );

      for( String name : names )
      {
         StockElement stock = getStockElement( name );
         stock.calculatePercentages();
         maps[ p ] = new Interval();

         // Now, pull the data to be normalized
         for( int i=0; i<trainingSize; i++ )
         {
            maps[ p ].addDataItem( stock.getCloseP( ( stock.getSize() - trainingSize - predictSize ) + i ) );
         }

         ArrayList< Double > temp = maps[ p ].getMappedData();
         double[] t = new double[ trainingSize ];
         for( int i=0; i<t.length; i++ )
         {
            t[ i ] = temp.get( i );
         }
         testData[ k ] = t;

         // Finally, pull the prediction data
         double[] t4 = new double[ predictSize ];
         for( int i=0; i<t4.length; i++ )
         {
            t4[ i ] = maps[ p ].map( stock.getCloseP( stock.getSize() - predictSize + i ) );
         }
         pTestData[ k++ ] = t4;
         p++;
      }

      k = 0;
      p = 0;
      for( String name : targetNames )
      {
         StockElement stock = getStockElement( name );
         stock.calculatePercentages();
         targetMaps[ p ] = new Interval();

         // Now, pull the data to be normalized
         for( int i=0; i<trainingSize; i++ )
         {
            targetMaps[ p ].addDataItem( stock.getCloseP( ( stock.getSize() - trainingSize - predictSize ) + i ) );
            if( stock.getName().equals( "QQQ" ) )
            {
               System.out.println( "QQQ close: " + 
                  stock.getCloseP( ( stock.getSize() - trainingSize - predictSize ) + i ) );
            }
         }

         ArrayList< Double > temp = targetMaps[ p ].getMappedData();
         double[] t = new double[ trainingSize ];
         for( int i=0; i<t.length; i++ )
         {
            t[ i ] = temp.get( i );
            if( stock.getName().equals( "QQQ" ) )
            {
               System.out.println( "QQQ data point: " + t[ i ] );
            }
         }
         targetData[ k ] = t;

         // Finally, pull the prediction data
         double[] t4 = new double[ predictSize ];
         for( int i=0; i<t4.length; i++ )
         {
            t4[ i ] = targetMaps[ p ].map( stock.getCloseP( stock.getSize() - predictSize + i ) );
            if( stock.getName().equals( "QQQ" ) )
            {
               System.out.println( "QQQ training prediction close: " + t4[ i ] );
            }
         }
         pTargetData[ k++ ] = t4;
         p++;
      }

      ManyToManyCorrelation.ManyToManyCorrelationDAO dao = (ManyToManyCorrelation.ManyToManyCorrelationDAO)
         (new ManyToManyCorrelation( predictSize + "-e" + eSize + "-l" + gSize + "i" )).getDAO();
      ManyToManyCorrelation correlation = dao.deserialize();

      double[] input = new double[ testData.length ];
      double[] result = null;
      double totalError = 0;
      for( int i=0; i<testData[0].length; i++ )
      {
         double error = 0;
         for( int j=0; j<testData.length; j++ )
         {
            input[ j ] = testData[ j ][ i ];
         }
         result = correlation.correlate( input );
         for( int j=0; j<targets; j++ )
         {
            error += Math.abs( targetData[ j ][ i ] - result[ j ] );
         }
         totalError += error;
      }
      System.out.println( "Total error was: " + totalError );

      totalError = 0;
      result = null;
      for( int i=0; i<pTargetData[0].length; i++ )
      {
         double error = 0;

         System.out.println( "Data to predict:" );
         for( int j=0; j<pTargetData.length; j++ )
         {
            System.out.print( pTargetData[ j ][ i ] + "," );
         }
         System.out.println( "" );

         for( int j=0; j<pTestData.length; j++ )
         {
            input[ j ] = pTestData[ j ][ i ];
         }
         result = correlation.correlate( input );
         System.out.println( "Predicted:" );
         for( int j=0; j<targets; j++ )
         {
            error += Math.abs( pTargetData[ j ][ i ] - result[ j ] );
            System.out.print( result[ j ] + "," );
         }
         System.out.println( "" );
         totalError += error;
      }
      System.out.println( "Prediction total error was: " + totalError );
   }
    */

   /**
    * This method will perform basic training, testing, and a prediction of the ManyToManyCorrelation class.
    *
    * @param String -- The date at which to begin the test. Example: "2010-11-02"
    */
   public void testManyToFew( String date, String time, String ensembleSize, String genomeSize )
   {
      System.out.println( "The date will be: " + date );
      System.out.println( "The time will be: " + time );
      System.out.println( "The ensembleSize will be: " + ensembleSize );
      System.out.println( "The genomeSize will be: " + genomeSize );
      int trainingSize = 20000;
      int predictSize = 100;
      int eSize = 0;
      int gSize = 0;
      int targets = 1;
      long trainingTime = (new MathUtilities()).getTime( time );
      ArrayList< String > names = _theStockCollection.getNasdaq100StockNames();
//       ArrayList< String > names = _theStockCollection.getSP500StockNames();
      double[][] testData = new double[ 100 ][];
      double[][] targetData = new double[ targets ][];
      double[][] pTestData = new double[ 100 ][];
      double[][] pTargetData = new double[ targets ][];
      Interval[] maps = new Interval[ 100 ];
      Interval[] targetMaps = new Interval[ targets ];
      ArrayList< String > targetNames = new ArrayList< String >();
      String targetStock = "QQQ";
//       String targetStock = "SPY";
      targetNames.add( targetStock );
      int k = 0;
      int p = 0;

      try
      {
         eSize = Integer.parseInt( ensembleSize );
         gSize = Integer.parseInt( genomeSize );
         predictSize = Integer.parseInt( date );
      }
      catch( NumberFormatException e )
      {
         System.err.println( "Unable to read ensemble size: " + e );
      }

      for( String name : names )
      {
         StockElement stock = getStockElement( name );
         if( trainingSize > ( stock.getSize() - predictSize ) )
         {
            trainingSize = ( stock.getSize() - predictSize );
         }
      }
      System.out.println( "trainingSize is: " + trainingSize );
      System.out.println( "predictSize is: " + predictSize );

      for( String name : names )
      {
         StockElement stock = getStockElement( name );
         stock.calculatePercentages();
         maps[ p ] = new Interval();

         // Now, pull the data to be normalized
         for( int i=0; i<trainingSize; i++ )
         {
            maps[ p ].addDataItem( stock.getCloseP( ( stock.getSize() - trainingSize - predictSize ) + i ) );
            if( stock.getName().equals( targetStock ) )
            {
               System.out.println( targetStock + " close: " + 
                  stock.getCloseP( ( stock.getSize() - trainingSize - predictSize ) + i ) );
            }
         }

         ArrayList< Double > temp = maps[ p ].getMappedData();
         double[] t = new double[ trainingSize ];
         for( int i=0; i<t.length; i++ )
         {
            t[ i ] = temp.get( i );
            if( stock.getName().equals( targetStock ) )
            {
               System.out.println( targetStock + " data point: " + t[ i ] );
            }
         }
         testData[ k ] = t;

         // Finally, pull the prediction data
         double[] t4 = new double[ predictSize ];
         for( int i=0; i<t4.length; i++ )
         {
            t4[ i ] = maps[ p ].map( stock.getCloseP( stock.getSize() - predictSize + i ) );
            if( stock.getName().equals( targetStock ) )
            {
               System.out.println( targetStock + " training prediction close: " + t4[ i ] );
            }
         }
         pTestData[ k++ ] = t4;
         p++;
      }

      k = 0;
      p = 0;
      for( String name : targetNames )
      {
         StockElement stock = getStockElement( name );
         stock.calculatePercentages();
         targetMaps[ p ] = new Interval();

         // Now, pull the data to be normalized
         for( int i=0; i<trainingSize; i++ )
         {
            targetMaps[ p ].addDataItem( stock.getCloseP( ( stock.getSize() - trainingSize - predictSize ) + i ) );
            if( stock.getName().equals( targetStock ) )
            {
               System.out.println( targetStock + " close: " + 
                  stock.getCloseP( ( stock.getSize() - trainingSize - predictSize ) + i ) );
            }
         }

         ArrayList< Double > temp = targetMaps[ p ].getMappedData();
         double[] t = new double[ trainingSize ];
         for( int i=0; i<t.length; i++ )
         {
            t[ i ] = temp.get( i );
            if( stock.getName().equals( targetStock ) )
            {
               System.out.println( targetStock + " data point: " + t[ i ] );
            }
         }
         targetData[ k ] = t;

         // Finally, pull the prediction data
         double[] t4 = new double[ predictSize ];
         for( int i=0; i<t4.length; i++ )
         {
            t4[ i ] = targetMaps[ p ].map( stock.getCloseP( stock.getSize() - predictSize + i ) );
            if( stock.getName().equals( targetStock ) )
            {
               System.out.println( targetStock + " training prediction close: " + t4[ i ] );
            }
         }
         pTargetData[ k++ ] = t4;
         p++;
      }

      HaltingCriteria c = new HaltingCriteria();
      c.setElapsedTimeTolerance( trainingTime );
      ManyToManyCorrelation correlation = new ManyToManyCorrelation( date + "-e" + eSize + "-l" + gSize + "i" );
      correlation.setHaltingCriteria( c );
      correlation.setEnsembleSize( eSize );
      correlation.setGenomeLength( gSize );
//       correlation.setSequenceData( testData );
//       correlation.setData( testData );
      correlation.setIndependentData( testData );
      correlation.setDependentData( targetData );
      correlation.setMultiThreaded();
      correlation.createEnsemble();
      correlation.train();
      ManyToManyCorrelation.ManyToManyCorrelationDAO dao = 
         (ManyToManyCorrelation.ManyToManyCorrelationDAO) correlation.getDAO();
      dao.serialize( correlation );

      double[] input = new double[ testData.length ];
      double[] result = null;
      double totalError = 0;
      for( int i=0; i<testData[0].length; i++ )
      {
         double error = 0;
         for( int j=0; j<testData.length; j++ )
         {
            input[ j ] = testData[ j ][ i ];
         }
         result = correlation.correlate( input );
         for( int j=0; j<targets; j++ )
         {
            error += Math.abs( targetData[ j ][ i ] - result[ j ] );
         }
         totalError += error;
      }
      System.out.println( "Total error was: " + totalError );

      totalError = 0;
      result = null;
      for( int i=0; i<pTargetData[0].length; i++ )
      {
         double error = 0;

         System.out.println( "Data to predict:" );
         for( int j=0; j<pTargetData.length; j++ )
         {
            System.out.print( pTargetData[ j ][ i ] + "," );
         }
         System.out.println( "" );

         for( int j=0; j<pTestData.length; j++ )
         {
            input[ j ] = pTestData[ j ][ i ];
         }
         result = correlation.correlate( input );
         System.out.println( "Predicted:" );
         for( int j=0; j<targets; j++ )
         {
            error += Math.abs( pTargetData[ j ][ i ] - result[ j ] );
            System.out.print( result[ j ] + "," );
         }
         System.out.println( "" );
         totalError += error;
      }
      System.out.println( "Prediction total error was: " + totalError );

      /*
      double[] input = new double[ testData.length ];
      double[] result = null;
      double totalError = 0;
      for( int i=0; i<testData[0].length; i++ )
      {
         double error = 0;
         for( int j=0; j<testData.length; j++ )
         {
            input[ j ] = testData[ j ][ i ];
         }
         if( result != null )
         {
            for( int j=0; j<input.length; j++ )
            {
               error += Math.abs( input[ j ] - result[ j ] );
            }
         }
         totalError += error;
         result = correlation.correlate( input );
      }
      System.out.println( "Total error was: " + totalError );
      totalError = 0;
      System.out.println( "Last data point:" );
      for( int i=0; i<result.length; i++ )
      {
         System.out.print( input[ i ] + "," );
         if( result[ i ] > -1 && result[ i ] < 2 )
         {
            // do nothing
         }
         else
         {
            result[ i ] = 0;
         }
         totalError += Math.abs( result[ i ] - pTestData[ i ][ 0 ] );
      }
      System.out.println( "\nPrediction error was: " + totalError );

      System.out.println( "Data to predict:" );
      for( int i=0; i<pTestData[0].length; i++ )
      {
         for( int j=0; j<pTestData.length; j++ )
         {
            System.out.print( pTestData[ j ][ i ] + "," );
         }
         System.out.println( "" );
      }
      double[][] predicted = correlation.predict( 3, input );
      System.out.println( "Predicted data:" );
      for( int i=0; i<predictSize; i++ )
      {
         for( int j=0; j<predicted[ i ].length; j++ )
         {
            System.out.print( predicted[ i ][ j ] + "," );
         }
         System.out.println( "" );
      }
      */
   }

   /**
    * This method will perform basic training, testing, and a prediction of the ManyToManyCorrelation class.
    *
    * @param String -- The date at which to begin the test. Example: "2010-11-02"
    */
   public void currentManyToFew( String date, String time, String ensembleSize, String genomeSize )
   {
      System.out.println( "The date will be: " + date );
      System.out.println( "The time will be: " + time );
      System.out.println( "The ensembleSize will be: " + ensembleSize );
      System.out.println( "The genomeSize will be: " + genomeSize );
      int trainingSize = 20000;
      int predictSize = 100;
      int eSize = 0;
      int gSize = 0;
      int targets = 1;
      long trainingTime = (new MathUtilities()).getTime( time );
      ArrayList< String > names = _theStockCollection.getNasdaq100StockNames();
//       ArrayList< String > names = _theStockCollection.getSP500StockNames();
      double[][] testData = new double[ 100 ][];
      double[][] targetData = new double[ targets ][];
      double[][] pTestData = new double[ 100 ][];
      double[][] pTargetData = new double[ targets ][];
      Interval[] maps = new Interval[ 100 ];
      Interval[] targetMaps = new Interval[ targets ];
      ArrayList< String > targetNames = new ArrayList< String >();
      String targetStock = "QQQ";
//       String targetStock = "SPY";
      targetNames.add( targetStock );
      int k = 0;
      int p = 0;

      try
      {
         eSize = Integer.parseInt( ensembleSize );
         gSize = Integer.parseInt( genomeSize );
         predictSize = -1 * Integer.parseInt( date );
      }
      catch( NumberFormatException e )
      {
         System.err.println( "Unable to read ensemble size: " + e );
      }

      for( String name : names )
      {
         StockElement stock = getStockElement( name );
         if( trainingSize > ( stock.getSize() - predictSize ) )
         {
            trainingSize = ( stock.getSize() - predictSize );
         }
      }
      System.out.println( "trainingSize is: " + trainingSize );
      System.out.println( "predictSize is: " + predictSize );

      for( String name : names )
      {
         StockElement stock = getStockElement( name );
         stock.calculatePercentages();
         maps[ p ] = new Interval();

         // Now, pull the data to be normalized
         for( int i=0; i<trainingSize; i++ )
         {
            maps[ p ].addDataItem( stock.getCloseP( ( stock.getSize() - trainingSize - predictSize ) + i ) );
//             if( stock.getName().equals( targetStock ) )
//             {
//                System.out.println( targetStock + " close: " + 
//                   stock.getCloseP( ( stock.getSize() - trainingSize - predictSize ) + i ) );
//             }
         }

         ArrayList< Double > temp = maps[ p ].getMappedData();
         double[] t = new double[ trainingSize ];
         for( int i=0; i<t.length; i++ )
         {
            t[ i ] = temp.get( i );
//             if( stock.getName().equals( targetStock ) )
//             {
//                System.out.println( targetStock + " data point: " + t[ i ] );
//             }
         }
         testData[ k ] = t;

         // Finally, pull the prediction data
         double[] t4 = new double[ predictSize ];
         for( int i=0; i<t4.length; i++ )
         {
            t4[ i ] = maps[ p ].map( stock.getCloseP( stock.getSize() - predictSize + i ) );
//             if( stock.getName().equals( targetStock ) )
//             {
//                System.out.println( targetStock + " training prediction close: " + t4[ i ] );
//             }
         }
         pTestData[ k++ ] = t4;
         p++;
      }

      k = 0;
      p = 0;
      for( String name : targetNames )
      {
         StockElement stock = getStockElement( name );
         stock.calculatePercentages();
         targetMaps[ p ] = new Interval();

         // Now, pull the data to be normalized
         for( int i=0; i<trainingSize; i++ )
         {
            targetMaps[ p ].addDataItem( stock.getCloseP( ( stock.getSize() - trainingSize - predictSize ) + i ) );
//             if( stock.getName().equals( targetStock ) )
//             {
//                System.out.println( targetStock + " close: " + 
//                   stock.getCloseP( ( stock.getSize() - trainingSize - predictSize ) + i ) );
//             }
         }

         ArrayList< Double > temp = targetMaps[ p ].getMappedData();
         double[] t = new double[ trainingSize ];
         for( int i=0; i<t.length; i++ )
         {
            t[ i ] = temp.get( i );
//             if( stock.getName().equals( targetStock ) )
//             {
//                System.out.println( targetStock + " data point: " + t[ i ] );
//             }
         }
         targetData[ k ] = t;

         // Finally, pull the prediction data
         double[] t4 = new double[ predictSize ];
         for( int i=0; i<t4.length; i++ )
         {
            t4[ i ] = targetMaps[ p ].map( stock.getCloseP( stock.getSize() - predictSize + i ) );
//             if( stock.getName().equals( targetStock ) )
//             {
//                System.out.println( targetStock + " training prediction close: " + t4[ i ] );
//             }
         }
         pTargetData[ k++ ] = t4;
         p++;
      }

      ManyToManyCorrelation.ManyToManyCorrelationDAO dao = (ManyToManyCorrelation.ManyToManyCorrelationDAO)
         (new ManyToManyCorrelation( "0-e" + eSize + "-l" + gSize + "i" )).getDAO();
      ManyToManyCorrelation correlation = dao.deserialize();

//       HaltingCriteria c = new HaltingCriteria();
//       c.setElapsedTimeTolerance( trainingTime );
//       ManyToManyCorrelation correlation = new ManyToManyCorrelation( date + "-e" + eSize + "-l" + gSize + "i" );
//       correlation.setHaltingCriteria( c );
//       correlation.setEnsembleSize( eSize );
//       correlation.setGenomeLength( gSize );
// //       correlation.setSequenceData( testData );
// //       correlation.setData( testData );
//       correlation.setIndependentData( testData );
//       correlation.setDependentData( targetData );
//       correlation.setMultiThreaded();
//       correlation.createEnsemble();
//       correlation.train();
//       ManyToManyCorrelation.ManyToManyCorrelationDAO dao = 
//          (ManyToManyCorrelation.ManyToManyCorrelationDAO) correlation.getDAO();
//       dao.serialize( correlation );

      double[] input = new double[ testData.length ];
      double[] result = null;
      double totalError = 0;
      for( int i=0; i<testData[0].length; i++ )
      {
         double error = 0;
         for( int j=0; j<testData.length; j++ )
         {
            input[ j ] = testData[ j ][ i ];
         }
         result = correlation.correlate( input );
         for( int j=0; j<targets; j++ )
         {
            error += Math.abs( targetData[ j ][ i ] - result[ j ] );
         }
         totalError += error;
      }
      System.out.println( "Total error was: " + totalError );

      totalError = 0;
      result = null;
      StockElement q = getStockElement( targetStock );
      for( int i=0; i<pTargetData[0].length; i++ )
      {
         double error = 0;

         System.out.println( "Data to predict:" );
         for( int j=0; j<pTargetData.length; j++ )
         {
//             System.out.print( targetMaps[ i ].invert( pTargetData[ j ][ i ] ) + "," );
            System.out.print( q.getClose( q.getSize() - predictSize + i ) );
         }
         System.out.println( "" );

         for( int j=0; j<pTestData.length; j++ )
         {
            input[ j ] = pTestData[ j ][ i ];
         }
         result = correlation.correlate( input );
         System.out.println( "Predicted:" );
         for( int j=0; j<targets; j++ )
         {
            error += Math.abs( pTargetData[ j ][ i ] - result[ j ] );
            System.out.print( q.getClose( q.getSize() - predictSize + i - 1 ) * targetMaps[ 0 ].invert( result[ j ] ) + "," );
         }
         System.out.println( "" );
         totalError += error;
      }
      System.out.println( "Prediction total error was: " + totalError );
//       correlation.printVariables();

      /*
      double[] input = new double[ testData.length ];
      double[] result = null;
      double totalError = 0;
      for( int i=0; i<testData[0].length; i++ )
      {
         double error = 0;
         for( int j=0; j<testData.length; j++ )
         {
            input[ j ] = testData[ j ][ i ];
         }
         if( result != null )
         {
            for( int j=0; j<input.length; j++ )
            {
               error += Math.abs( input[ j ] - result[ j ] );
            }
         }
         totalError += error;
         result = correlation.correlate( input );
      }
      System.out.println( "Total error was: " + totalError );
      totalError = 0;
      System.out.println( "Last data point:" );
      for( int i=0; i<result.length; i++ )
      {
         System.out.print( input[ i ] + "," );
         if( result[ i ] > -1 && result[ i ] < 2 )
         {
            // do nothing
         }
         else
         {
            result[ i ] = 0;
         }
         totalError += Math.abs( result[ i ] - pTestData[ i ][ 0 ] );
      }
      System.out.println( "\nPrediction error was: " + totalError );

      System.out.println( "Data to predict:" );
      for( int i=0; i<pTestData[0].length; i++ )
      {
         for( int j=0; j<pTestData.length; j++ )
         {
            System.out.print( pTestData[ j ][ i ] + "," );
         }
         System.out.println( "" );
      }
      double[][] predicted = correlation.predict( 3, input );
      System.out.println( "Predicted data:" );
      for( int i=0; i<predictSize; i++ )
      {
         for( int j=0; j<predicted[ i ].length; j++ )
         {
            System.out.print( predicted[ i ][ j ] + "," );
         }
         System.out.println( "" );
      }
      */
   }

   /**
    * Previously, this was run with the following parameters:
    *   trainingDays = 0
    *   time         = 2d20h
    *   ensembleSize = 5
    *   genomeSize   = 17
    *
    * For the prediction runs, this was run with the following parameters:
    *   trainingDays = -days
    *   time         = 0
    *   ensembleSize = 5
    *   genomeSize   = 17
    *
    * In the code below, the training data matrices that are given to the correlation will be interpreted
    * in the following way: the rows represent independent variables while the columns represent individual
    * instances of data for the respective variable.
    * This makes the manipulation of the data much easier, especially when normalizing and etc.
    *
    * @param 
    */
   public void trainOpenSpread( String trainingDays, 
                                String time, 
                                String ensembleSize, 
                                String genomeSize, 
                                boolean amend )
   {
      System.out.println( "The trainingDays will be: " + trainingDays );
      System.out.println( "The time will be: " + time );
      System.out.println( "The ensembleSize will be: " + ensembleSize );
      System.out.println( "The genomeSize will be: " + genomeSize );
      System.out.println( "The amend ind will be: " + amend );

      int trainingSize = 10;
      int eSize = 0;
      int gSize = 0;
      int targets = 1;
      long trainingTime = (new MathUtilities()).getTime( time );
      ArrayList< String > names = _theStockCollection.getNasdaq100StockNames();
      HashMap< String, StockElement > cache = getStockElements( names, amend );

      double[][] testData = new double[ 100 * 4 ][];
      double[][] targetData = new double[ targets ][];
      Interval[] maps = new Interval[ 100 * 4 ];

      Interval targetMap = new Interval();
      String targetStock = "QQQ";

      int k = 0;
      int p = 0;

      try
      {
         eSize = Integer.parseInt( ensembleSize );
         gSize = Integer.parseInt( genomeSize );
         trainingSize = Integer.parseInt( trainingDays );
      }
      catch( NumberFormatException e )
      {
         System.err.println( "Unable to read ensemble size: " + e );
      }

      // here, we check the elements again before we loop through to make sure the download was good:
      checkAndUpdateElements( cache, amend );

      // First, let's pull the independent training input data. 
      // There are four data lines for each asset -- 
      //    open spread, high spread, low spread and close spread.
      for( String name : names )
      {
         StockElement stock = cache.get( name );

         // Check to see if the data was downloaded okay, if not, try it again:
         if( stock.getLastOpen() == -1 || stock.getLastClose() == -1 )
         {
            if( amend == true )
            {
               System.out.println( "In train open spread, re-downloading an amended " + name );
               stock = getAmendedStockElement( name );
            }
            else
            {
               System.out.println( "In train open spread, re-downloading " + name );
               stock = getStockElement( name );
            }
            cache.put( name, stock );
         }

         stock.calculatePercentages();
         maps[ p ] = new Interval();
         maps[ p + 1 ] = new Interval();
         maps[ p + 2 ] = new Interval();
         maps[ p + 3 ] = new Interval();

         // Now, pull the data lines to be normalized, but since we're mapping the previous
         // day to the next day's open spread, we have to "shift" the data to get the correct
         // mapping in the data arrays. Thus, the independent data variables must be shifted
         // "back" one -- or the window should be shifted back so that we grab one back from 
         // the normal beginning but we don't grab the very last data instance.
         for( int i=(stock.getSize() - trainingSize - 1); i<stock.getSize() - 1; i++ )
         {
            // First data line: open spread
            maps[ p ].addDataItem( stock.getOpenSpreadP( i ) );
            // Next data line: high spread
            maps[ p + 1 ].addDataItem( stock.getHighSpreadP( i ) );
            // Next data line: low spread
            maps[ p + 2 ].addDataItem( stock.getLowSpreadP( i ) );
            // Next data line: close spread
            maps[ p + 3 ].addDataItem( stock.getCloseSpreadP( i ) );
         }

         // First data line: open spread
         ArrayList< Double > temp = maps[ p ].getMappedData();
         double[] t = new double[ trainingSize ];
         for( int j=0; j<t.length; j++ )
         {
            t[ j ] = temp.get( j );
         }
         testData[ k++ ] = t;

         // Next data line: high spread
         temp = maps[ p + 1 ].getMappedData();
         t = new double[ trainingSize ];
         for( int j=0; j<t.length; j++ )
         {
            t[ j ] = temp.get( j );
         }
         testData[ k++ ] = t;

         // Next data line: low spread
         temp = maps[ p + 2 ].getMappedData();
         t = new double[ trainingSize ];
         for( int j=0; j<t.length; j++ )
         {
            t[ j ] = temp.get( j );
         }
         testData[ k++ ] = t;

         // Next data line: close spread
         temp = maps[ p + 3 ].getMappedData();
         t = new double[ trainingSize ];
         for( int j=0; j<t.length; j++ )
         {
            t[ j ] = temp.get( j );
         }
         testData[ k++ ] = t;

         p += 4;
      }

      // Here, we will pull the target dependent data:
      StockElement stock = null;
      if( amend == true )
      {
         stock = getAmendedStockElement( targetStock );
      }
      else
      {
         stock = getStockElement( targetStock );
      }
      stock.calculatePercentages();

      // Now, pull the data to be normalized:
      for( int i=(stock.getSize() - trainingSize); i<stock.getSize(); i++ )
      {
         targetMap.addDataItem( stock.getOpenSpreadP( i ) );
         System.out.println( targetStock + " open spread: " + stock.getOpenSpreadP( i ) );
      }
      System.out.println( targetStock + " last close: " + stock.getLastClose() );
      System.out.println( targetStock + " last open: " + stock.getLastOpen() );

      // normalize the data into the dependent data array:
      ArrayList< Double > temp = targetMap.getMappedData();
      targetData[ 0 ] = new double[ trainingSize ];
      for( int i=0; i<targetData[ 0 ].length; i++ )
      {
         targetData[ 0 ][ i ] = temp.get( i );
         System.out.println( targetStock + " data point: " + targetData[ 0 ][ i ] );
      }

      // Now actually do the training and serialization:
      HaltingCriteria c = new HaltingCriteria();
      c.setElapsedTimeTolerance( trainingTime );
      ManyToManyCorrelation correlation = new ManyToManyCorrelation( trainingDays + "-e" + eSize + "-l" + gSize + "-sopen" );
      correlation.setHaltingCriteria( c );
      correlation.setEnsembleSize( eSize );
      correlation.setGenomeLength( gSize );
      correlation.setIndependentData( testData );
      correlation.setDependentData( targetData );
      correlation.setIndependentMaps( maps );
      correlation.setDependentMap( targetMap );
      correlation.setMultiThreaded();
      correlation.createEnsemble();
      correlation.train();
      ManyToManyCorrelation.ManyToManyCorrelationDAO dao = 
         (ManyToManyCorrelation.ManyToManyCorrelationDAO) correlation.getDAO();
      dao.serialize( correlation );

      // Just run a quick test to see how bad the error was for the training data:
      double[] input = new double[ testData.length ];
      double[] result = null;
      double totalError = 0;
      for( int i=0; i<testData[0].length; i++ )
      {
         double error = 0;
         for( int j=0; j<testData.length; j++ )
         {
            input[ j ] = testData[ j ][ i ];
         }
         result = correlation.correlate( input );
         for( int j=0; j<targets; j++ )
         {
            System.out.println( "target: " + targetData[ j ][ i ] );
            System.out.println( "result: " + result[ j ]  );
            error += Math.abs( targetData[ j ][ i ] - result[ j ] );
         }
         totalError += error;
      }
      System.out.println( "Total error was: " + totalError );

      // Since there's one extra data point, let's go ahead and try to predict 
      // what it will be -- but we can't compute the error yet...
      // Basically, what we want to do is grab the last data point for each stock
      // and each variable (in the same format/order as the test data) and map
      // it using the previously-used interval maps and then correlate from that
      // input data to get the next prediction value.
      result = null;
      p = 0;
      k = 0;
      for( String name : names )
      {
         stock = cache.get( name );
         input[ k ] = maps[ p ].map( stock.getOpenSpreadP( stock.getSize() - 1 ) );
         input[ k + 1 ] = maps[ p + 1 ].map( stock.getHighSpreadP( stock.getSize() - 1 ) );
         input[ k + 2 ] = maps[ p + 2 ].map( stock.getLowSpreadP( stock.getSize() - 1 ) );
         input[ k + 3 ] = maps[ p + 3 ].map( stock.getCloseSpreadP( stock.getSize() - 1 ) );
         p += 4;
         k += 4;
      }
      result = correlation.correlate( input );
      System.out.println( "Predicted: " + result[ 0 ] + " mapped: " + targetMap.invert( result[ 0 ] ) );
   }

   /**
    * Previously, this was run with the following parameters:
    *   trainingDays = 0
    *   time         = 2d20h
    *   ensembleSize = 5
    *   genomeSize   = 17
    *
    * For the prediction runs, this was run with the following parameters:
    *   trainingDays = -days
    *   time         = 0
    *   ensembleSize = 5
    *   genomeSize   = 17
    *
    * In the code below, the training data matrices that are given to the correlation will be interpreted
    * in the following way: the rows represent independent variables while the columns represent individual
    * instances of data for the respective variable.
    * This makes the manipulation of the data much easier, especially when normalizing and etc.
    *
    * @param 
    */
   public void currentOpenSpread( String trainingDays, String time, String ensembleSize, String genomeSize )
   {
      trainingDays = trainingDays.substring( 1 );
      System.out.println( "The trainingDays will be: " + trainingDays );
      System.out.println( "The time will be: " + time );
      System.out.println( "The ensembleSize will be: " + ensembleSize );
      System.out.println( "The genomeSize will be: " + genomeSize );

      boolean useAmended = false;
      int eSize = 0;
      int gSize = 0;
      ArrayList< String > names = _theStockCollection.getNasdaq100StockNames();

      double[] result = null;
      int k = 0;
      int p = 0;

      try
      {
         eSize = Integer.parseInt( ensembleSize );
         gSize = Integer.parseInt( genomeSize );
      }
      catch( NumberFormatException e )
      {
         System.err.println( "Unable to read ensemble size: " + e );
      }

      ManyToManyCorrelation.ManyToManyCorrelationDAO dao = (ManyToManyCorrelation.ManyToManyCorrelationDAO)
         (new ManyToManyCorrelation( trainingDays + "-e" + eSize + "-l" + gSize + "-sopen" )).getDAO();
      ManyToManyCorrelation correlation = dao.deserialize();

      Interval[] maps = correlation.getIndependentMaps();
      Interval targetMap = correlation.getDependentMap();
      double[] input = new double[ 100 * 4 ];

      // check to see if we need to pull the amended stock element
      if( "1".equals( time ) )
      {
         useAmended = true;
      }
      HashMap< String, StockElement > cache = getStockElements( names, useAmended );
      // here, we check the elements again before we loop through to make sure the download was good:
      checkAndUpdateElements( cache, useAmended );

      // Since there's one extra data point, let's go ahead and try to predict 
      // what it will be -- but we can't compute the error yet...
      // Basically, what we want to do is grab the last data point for each stock
      // and each variable (in the same format/order as the test data) and map
      // it using the previously-used interval maps and then correlate from that
      // input data to get the next prediction value.
      for( String name : names )
      {
         StockElement stock = cache.get( name );

         // Check to see if the data was downloaded okay, if not, try it again:
         if( stock.getLastOpen() == -1 || stock.getLastClose() == -1 )
         {
            if( useAmended == true )
            {
               System.out.println( "In current open spread, re-downloading an amended " + name );
               stock = getAmendedStockElement( name );
            }
            else
            {
               System.out.println( "In current open spread, re-downloading " + name );
               stock = getStockElement( name );
            }
            if( stock.getLastOpen() == -1 || stock.getLastClose() == -1 )
            {
               System.out.println( "In current open spread after re-download, " + name + " is still -1!" );
            }
            cache.put( name, stock );
         }

         stock.calculatePercentages();
         input[ k ] = maps[ p ].map( stock.getOpenSpreadP( stock.getSize() - 1 ) );
         input[ k + 1 ] = maps[ p + 1 ].map( stock.getHighSpreadP( stock.getSize() - 1 ) );
         input[ k + 2 ] = maps[ p + 2 ].map( stock.getLowSpreadP( stock.getSize() - 1 ) );
         input[ k + 3 ] = maps[ p + 3 ].map( stock.getCloseSpreadP( stock.getSize() - 1 ) );
         p += 4;
         k += 4;
      }
      result = correlation.correlate( input );
      System.out.println( "Predicted: " + result[ 0 ] + " mapped: " + targetMap.invert( result[ 0 ] ) );
   }

   /**
    *
    */
   private void checkAndUpdateElements( HashMap< String, StockElement > cache, boolean isAmended )
   {
      Set< String > names = cache.keySet();

      for( String name : names )
      {
         StockElement stock = cache.get( name );
         if( stock.getLastOpen() == -1 || stock.getLastClose() == -1 )
         {
            if( isAmended == true )
            {
               System.out.println( "In check and update, re-downloading an amended " + name );
               stock = getAmendedStockElement( name );
            }
            else
            {
               System.out.println( "In check and update, re-downloading " + name );
               stock = getStockElement( name );
            }
            if( stock.getLastOpen() == -1 || stock.getLastClose() == -1 )
            {
               System.out.println( "After re-download, " + name + " is still -1!" );
            }
            cache.put( name, stock );
         }
      }
   }

   /**
    * @param ArrayList< String > -- the list of stock ticker symbols.
    * @param boolean -- an indicator whether or not to get the latest amended data.
    */
   public HashMap< String, StockElement > getStockElements( ArrayList< String > names, boolean isAmended )
   {
      HashMap< String, StockElement > cache = new HashMap< String, StockElement >();
      StockElement                    stock = null;

      for( String name : names )
      {
         if( isAmended == true )
         {
            stock = getAmendedStockElement( name );
         }
         else
         {
            stock = getStockElement( name );
         }
         cache.put( name, stock );
      }

      return cache;
   }

   /**
    *
    */
   public String getLatestMovers()
   {
      ArrayList< String > names =_theStockCollection.getLiquidStockNames();
      double largestGain = -1 * Double.MAX_VALUE;
      StockElement best = null;

      for( String name : names )
      {
         StockElement stock = getAmendedStockElement( name );
         int s = stock.getSize();
         double r = ( stock.getClose( s - 1 ) / stock.getClose( s - 2 ) );

         if( r < 1 )
         {
            r = 1 / r;
         }

         System.out.println( name + ", " + stock.getClose( s - 1 ) + ", " + r );

         if( r > largestGain )
         {
            largestGain = r;
            best = stock;
         }
      }

      System.out.println( "The largest mover is,: " + best.getName() + ", " + largestGain );
      System.out.println( "The stock option prices are:" );
      WebPageReader page = new WebPageReader( best.getName() );
      page.parsePutOptions( best.getName() );
      ArrayList< OptionDataItem > ootm = page.getOOTMPuts();
      for( OptionDataItem obj : ootm )
      {
         System.out.println( "OOTM put option: " + obj );
      }
      ArrayList< OptionDataItem > itm = page.getITMPuts();
      for( OptionDataItem obj : itm )
      {
         System.out.println( "ITM put option: " + obj );
      }
      clearWatchedStockElements();
      String holder = best.getName();
      best.setName( holder + ">" );
      insertWatchedStockElement( best, 1.016 );
      best.setName( holder + "<" );
      insertWatchedStockElement( best, 1 / 1.016 );

      return best.getName();
   }

   /**
    * Here in this method we will perform just some data mining operations on a daily basis.
    * What we want to do is the following:
    *  1) For all the "liquid" securities, pull down all the options prices for the nearest
    *     expiration dates.
    *  2) Serialize this data for the next day's comparisons.
    *  3) Compare this data with yesterday's data to compute the delta percentages.
    *  4) Write this data to the database for later reports and data for the delta function.
    *     This data will consist of the following:
    *        a) stock symbol   -- string
    *        b) option delta   -- double
    *        c) stock delta    -- double
    *        d) option depth   -- int
    *        e) days to expire -- int
    *        f) ITM indicator  -- boolean
    *
    * The table definition is:
    *        +--------------+---------------+------+-----+---------+-------+
    *        | Field        | Type          | Null | Key | Default | Extra |
    *        +--------------+---------------+------+-----+---------+-------+
    *        | stock_symbol | varchar(6)    | YES  |     | NULL    |       |
    *        | option_delta | double        | YES  |     | NULL    |       |
    *        | stock_delta  | double        | YES  |     | NULL    |       |
    *        | option_depth | tinyint(4)    | YES  |     | NULL    |       |
    *        | days_exipre  | smallint(6)   | YES  |     | NULL    |       |
    *        | in_money     | tinyint(1)    | YES  |     | NULL    |       |
    *        | date         | date          | YES  |     | NULL    |       |
    *        +--------------+---------------+------+-----+---------+-------+
    */
   public void gatherOptionsData()
   {
      ArrayList< String > names = _theStockCollection.getLiquidStockNames();
      DatabaseUpdater db = new DatabaseUpdater();

      for( String name : names )
      {
         System.out.println( "processing: " + name );
         WebPageReader previous = new WebPageReader( name );
         WebPageDAO dao = (WebPageDAO) previous.getDAO();

         try
         {
            previous = dao.deserialize();
         }
         // In this case, just eat the exception and continue processing the others:
         catch( NullPointerException e )
         {
            System.out.println( "caught exception, most likely no file: " + e );
         }
         catch( Exception e )
         {
            System.out.println( "caught exception, most likely no file: " + e );
         }

         WebPageReader current = new WebPageReader( name );
         current.parsePutOptions( name );
//          System.out.println( "current options data for " + name + " is: " + current.getOOTMPuts() );

         if( previous != null )
         {
            System.out.println( "deserialized previous options data" );
            // First, let's calc the stock delta:
            double stock_delta = ( current.getCurrentPrice() / previous.getCurrentPrice() ) - 1;
            
            // Next, we'll parse out the in-the-money puts to calculate the option deltas for itm:
            boolean itm_indicator = true;
            ArrayList< OptionDataItem > current_itmputs = current.getITMPuts();
            ArrayList< OptionDataItem > previous_itmputs = previous.getITMPuts();
            parseAndUpdateOptionData( db, stock_delta, current_itmputs, previous_itmputs, itm_indicator );
            
            // Now, we'll parse out the out-of-the-money puts to calculate the option deltas for ootm:
            itm_indicator = false;
            ArrayList< OptionDataItem > current_ootmputs = current.getOOTMPuts();
            ArrayList< OptionDataItem > previous_ootmputs = previous.getOOTMPuts();
            parseAndUpdateOptionData( db, stock_delta, current_ootmputs, previous_ootmputs, itm_indicator );
         }

         dao = (WebPageDAO) current.getDAO();
         dao.serialize( current );
      }
   }

   /**
    *
    */
   private void parseAndUpdateOptionData( DatabaseUpdater db, 
                                          double stock_delta,
                                          ArrayList< OptionDataItem > current_itmputs,
                                          ArrayList< OptionDataItem > previous_itmputs,
                                          boolean itm_indicator )
   {
      int depth = 0;

      for( OptionDataItem current_option : current_itmputs )
      {
         depth++;
         double option_delta = -1;

         for( OptionDataItem previous_option : previous_itmputs )
         {
            if( current_option.getSymbol().equals( previous_option.getSymbol() ) )
            {
               option_delta = ( current_option.getLastPrice() / previous_option.getLastPrice() ) - 1;
               System.out.println( "matched put option: " + current_option.getSymbol() );
               break;
            }
         }
         if( option_delta != -1 )
         {
            db.updateOptions( current_option.getStockSymbol(), 
                              option_delta, 
                              stock_delta, 
                              depth, 
                              current_option.getDTE(), 
                              itm_indicator );
         }
      }
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void calcWindowMaxes()
   {
      ArrayList< String > names = _theStockCollection.getLiquidStockNames();

      for( String name : names )
      {
         StockElement stock = getStockElement( name );
         ArrayList< Double > values = stock.getUnicon( 0, -1, 254 );
         StatUtilities stats = new StatUtilities( values );
         System.out.println( "Stock: " + name + " ,mean:, " + stats.getMean() + " ,dev:, " + stats.getDeviation() );
//          System.out.println( "\nStock: " + name + " ,mean:, " + stats.getMean() + " ,dev:, " + stats.getDeviation() );
//          for( Double obj : values )
//          {
//             System.out.print( obj + ", " );
//          }
      }
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void calcPowerDist()
   {
      ArrayList< String > names = _theStockCollection.getLiquidStockNames();
      int preWindow = 5;
      int postWindow = 20;

      for( String name : names )
      {
         StockElement stock = getStockElement( name );
         double max = Double.MIN_VALUE;
         double min = Double.MAX_VALUE;
         double price = 0;
         double open = 0;
         double r1 = 0;
         double r2 = 0;

         System.out.println( "\n" + name );

         for( int i=0; i<stock.getSize(); i += (preWindow + postWindow) )
         {
            max = Double.MIN_VALUE;
            min = Double.MAX_VALUE;
            open = stock.getClose( i );
            for( int j=0; j<preWindow; j++ )
            {
               if( i + j >= stock.getSize() )
               {
                  break;
               }
               price = stock.getClose( i + j );
               if( price > max )
               {
                  max = price;
               }
               if( price < min )
               {
                  min = price;
               }
            }
            r1 = max / open;
            r2 = open / min;
            if( r1 < r2 )
            {
               r1 = r2;
            }
            System.out.print( r1 + "," );

            max = Double.MIN_VALUE;
            min = Double.MAX_VALUE;
            if( i + preWindow < stock.getSize() )
            {
               open = stock.getClose( i + preWindow );
            }
            for( int k=0; k<postWindow; k++ )
            {
               if( i + k + preWindow >= stock.getSize() )
               {
                  break;
               }
               price = stock.getClose( preWindow + i + k );
               if( price > max )
               {
                  max = price;
               }
               if( price < min )
               {
                  min = price;
               }
            }
            r1 = max / open;
            r2 = open / min;
            if( r1 < r2 )
            {
               r1 = r2;
            }
            System.out.println( r1 + "," );
         }
      }
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void calcVarianceRatio()
   {
      ArrayList< String > names = _theStockCollection.getLiquidStockNames();
      int window = 254;

      for( String name : names )
      {
         StockElement stock = getStockElement( name );
         double maxRatio = 0;
         int maxWindow = 0;
         for( int j=0; j<stock.getSize(); j++ )
         {
            double max = Double.MIN_VALUE;
            double min = Double.MAX_VALUE;
            double price = 0;
            double open = 0;
            double r1 = 0;
//             maxRatio = Double.MIN_VALUE;
//             maxWindow = Integer.MIN_VALUE;

            for( int i=5; i<window; i++ )
            {
               if( i + j >= stock.getSize() )
               {
                  break;
               }
               price = stock.getClose( j + i );
               if( price > max )
               {
                  max = price;
               }
               if( price < min )
               {
                  min = price;
               }
               if( max != Double.MIN_VALUE && min != Double.MAX_VALUE )
               {
                  r1 = ( max / min ) / (double)i;
               }
               if( r1 > maxRatio )
               {
                  maxRatio = r1;
                  maxWindow = i;
               }
            }
         }
         System.out.println( "Stock:, " + name + ", ratio:, " + maxRatio + ", window:, " + maxWindow );
      }
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void calcDividends()
   {
      ArrayList< String > names = _theStockCollection.getRussell3000StockNames();

      for( String name : names )
      {
         StockElement stock = getStockElement( name );
         if( stock.getLastDividendDate().equals( "" ) || stock.getAllDividends().size() <= 4 )
         {
            continue;
         }
         String year = stock.getLastDividendDate().substring( 0, stock.getLastDividendDate().indexOf( "-" ) );
         if( !year.equals( "2011" ) )
         {
            continue;
         }

         // First figure out the best estimate of the future dividends
         ArrayList< Double > dividends = stock.getAllDividends();
         ArrayList< String > dividendDates = stock.getAllDividendDates();
         ArrayList< Double > dividendYears = new ArrayList< Double >();
         int size = Math.min( dividends.size(), 15 );
         ArrayList< Double > divs = new ArrayList< Double >();
         for( int i=size; i>0; i-- )
         {
            divs.add( dividends.get( dividends.size() - i ) );
         }

         StatUtilities s = new StatUtilities( divs );
//          System.out.println( "median: " + s.getMedian() );
         // figure out how many consecutive payments have been at or above the median payout
         int consecutive = 0;
         int consecutiveYear = 1;
         String previousYear = "";
         for( int i=1; i<size; i++ )
         {
            String tyear = dividendDates.get( i ).substring( 0, dividendDates.get( i ).indexOf( "-" ) );
            if( tyear.equals( previousYear ) )
            {
               consecutiveYear++;
            }
            else
            {
               dividendYears.add( (double)consecutiveYear );
               consecutiveYear = 1;
            }
            previousYear = tyear;
//             System.out.println( "div is: " + dividends.get( dividends.size() - i ) );
            if( dividends.get( dividends.size() - i ) >= s.getMedian() )
            {
               consecutive++;
//                System.out.println( "div is greater than" );
            }
            else
            {
//                System.out.println( "div is NOT greater than" );
               break;
            }
         }
//          System.out.println( "consec is: " + consecutive );

         StatUtilities y = new StatUtilities( dividendYears );
         double cash = 20000;
         int n = ( int )( cash / stock.getLastClose() );
//          System.out.println( name + ", " + n );
         for( int i=1; ; i++ )
         {
            if( (i * 100) > n )
            {
               n = i - 1;
               break;
            }
         }
         if( n <= 0 )
         {
            continue;
         }
//          System.out.println( name + ", " + n );
         double payout = 100 * n * s.getMedian() * consecutive * y.getMedian();
         System.out.println( name                 + ", " + 
                             payout               + ", " + 
                             s.getMedian()        + ", " + 
                             stock.getLastClose() + ", " + 
                             consecutive          + ", " + 
                             y.getMedian() );
      }
   }

   /**
    * In this method, we "watch" stocks and if they make particular movements, we output a file.
    * In particular, the table WATCHER has the following structure:
    * +------------+-------------+------+-----+---------+-------+
    * | Field      | Type        | Null | Key | Default | Extra |
    * +------------+-------------+------+-----+---------+-------+
    * | stock      | varchar(6)  | YES  |     | NULL    |       |
    * | strike     | double(5,2) | YES  |     | NULL    |       |
    * | last_trade | double(5,2) | YES  |     | 0.00    |       |
    * +------------+-------------+------+-----+---------+-------+
    * Thus, each stock has a strike price for which we want to check transits and a last_trade
    * column to track what our last check found.
    * Given this information, we perform the following:
    *
    * if the last_trade value is less than the strike, then:
    *    if the current stock price greater than the strike, then write output file
    * else if the last_trade is greater than the strike, then:
    *    if the current stock price less than the strike, then write output file
    */
   public void watchStocks()
   {
      BufferedWriter output = null;
      
      ArrayList< String > names = _theStockCollection.getWatchedNames();

      for( String name : names )
      {
         StockElement stock = getWatchedStockElement( name );
         boolean writeFile = false;
         boolean isIncreasing = false;
//          System.out.println( "strike price is: " + stock.getStrikePrice() );
//          System.out.println( "previous trade is: " + stock.getPreviousTrade() );
//          System.out.println( "last trade is: " + stock.getLastClose() );

         if( stock.getPreviousTrade() < stock.getStrikePrice() )
         {
            if( stock.getLastClose() > stock.getStrikePrice() )
            {
               writeFile = true;
               isIncreasing = true;
            }
         }
//          else if( stock.getPreviousTrade() > stock.getStrikePrice() )
         else
         {
            if( stock.getLastClose() < stock.getStrikePrice() )
            {
               writeFile = true;
               isIncreasing = false;
            }
         }

         if( writeFile )
         {
            try
            {
               output = new BufferedWriter( new FileWriter( "/tmp/watched-stocks.txt" ) );
               output.write( name + " has" );
               if( isIncreasing )
               {
                  output.write( " risen above the strike: " );
               }
               else
               {
                  output.write( " fallen below the strike: " );
               }
               output.write( stock.getStrikePrice() + " with a current value of: " );
               output.write( stock.getLastClose() + " as of: " + (new Date()) );
               // Let's get the options price data now
               WebPageReader current = new WebPageReader( name );
               current.parsePutOptions( name );
               ArrayList< OptionDataItem > ootmputs = current.getOOTMPuts();
               int i = 0;
               output.write( "\n" );
               for( OptionDataItem put : ootmputs )
               {
                  output.write( "OOTM depth " + i++ + " put price: " + put.getLastPrice() );
                  output.newLine();
               }
               output.flush();
               output.close();
            }
            catch( IOException e )
            {
               System.err.println( "Error writing to watch file: " + e );
            }
         }

         updateWatchedStockElement( stock );
      }
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void calculatePhasePortrait()
   {
      int window = 125;
      int dimension = 5;
//       int length = 90;
//       int lag = 60;
      int length = 20;
      int lag = 10;
//       StockElement stock = getStockElement( "CSCO" );
      StockElement stock = getStockElement( "YHOO" );
//       StockElement stock = getStockElement( "SPY" );
//       StockElement stock = getStockElement( "QQQ" );
//       StockElement stock = getStockElement( "MSFT" );

//       ArrayList< Double > closes = new ArrayList< Double >();
//       double arg = 0;
//       for( int i=0; i<10000; i++ )
//       {
// //          closes.add( Math.sin( arg++ ) );
// //          closes.add( 1.0 + 0.00001 * (double)i );
// //          closes.add( 1.0 + (new MathUtilities()).random() );
//       }

      stock.calculatePercentages();
      ArrayList< Double > realCloses = stock.getAllCloses();
      ArrayList< Double > closes = stock.getAllClosesP();
      ArrayList< Double > lyapunov = new ArrayList< Double >();
      ArrayList< Integer > dimensions = new ArrayList< Integer >();
      int maxDim = Integer.MIN_VALUE;

      for( int i=0; i<closes.size() - window; i++ )
      {
         ArrayList< Double > raw = new ArrayList< Double >();
//          System.out.println( "" );
         for( int j=i; j<i+window; j++ )
         {
            raw.add( closes.get( j ) );
//             System.out.println( closes.get( j ) + "," + realCloses.get( j ) );
         }
//          System.out.println( "" );
         PhasePortrait pp = new PhasePortrait( raw );
         pp.setMaximumDimension( 8 );
         dimension = pp.computeFalseNeighbors( lag, length );
         dimensions.add( dimension );
         lyapunov.add( pp.computeLargestLyapunov( dimension, lag, length ) );
         if( dimension > maxDim )
         {
            maxDim = dimension;
         }
      }
      System.out.println( "Max Dimension: " + maxDim );
      for( int i=0; i<lyapunov.size(); i++ )
      {
         System.out.println( dimensions.get( i ) + ", " + lyapunov.get( i ) + ", " + realCloses.get( i + window ) );
      }
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void calculateQSims()
   {
      StockElement              qqq       = getStockElement( "QQQ" );
      qqq.calculatePercentages();
      HashMap< String, Double > qcloseMap = qqq.getCloseHash();
      TreeMap< Double, String > rankings  = new TreeMap< Double, String >();

      for( String name : _theStockCollection.getNasdaq100StockNames() )
      {
         ArrayList< Double > pp = new ArrayList< Double >();
         StatUtilities su       = new StatUtilities();
         StockElement  stock    = getStockElement( name );
         double        diff     = 0;
         int           errors   = 0;
         boolean       complete = true;

         stock.calculatePercentages();

         HashMap< String, Double > scloseMap = stock.getCloseHash();

         for( String key : qqq.getDates() )
         {
            Double sp = scloseMap.get( key );
            Double qp = qcloseMap.get( key );

            if( qp != null && qp < 0.97 && sp != null )
//             if( qp != null && sp != null )
            {
               diff = sp - qp; // here, we would like larger values
//                diff = Math.abs( sp - qp ); // here, the lowest value wins
               pp.add( diff );
            }
            else if( sp == null && qp != null )
            {
               complete = false;
               errors++;
            }
         }

//          if( complete )
         if( errors < 10 )
         {
            su.calculateStats( pp );
//             rankings.put( su.getMean(), name );
            rankings.put( su.getMedian(), name );
         }
      }

      // 
      // print out the results:
      // 
      for( Double key : rankings.keySet() )
      {
         String n = rankings.get( key );
         System.out.println( "security: " + n + " median: " + key );
//          System.out.println( "security: " + n + " mean: " + key );
      }
   }

   /**
    * The idea here is to simulate price movements using quantile approximations.
    * It is well known that just simulating usin the quantile approximations still does
    * not produce accurate simulations because price movements are not independent.
    * So we will average (or median) together several independent simulations and
    * since the average of variances scales by the square, this should produce a more
    * accurate overall simulation.
    */
   public void performSimulation()
   {
      StockElement        qqq    = getStockElement( "QQQ" );
      qqq.calculatePercentages();
      ArrayList< Double > closes = qqq.getAllClosesP();
      ArrayList< Double > last   = new ArrayList< Double >();
      ArrayList< Double > sim    = new ArrayList< Double >();
      StatUtilities       su     = new StatUtilities();
      MathUtilities       mu     = new MathUtilities();
      int                 l      = 100;
      int                 ll     = 1000;
      double              dd     = qqq.getClose( qqq.getAllCloses().size() - l );
      double              ddd    = dd;

      for( int i=closes.size() - l; i<closes.size(); i++ )
      {
         last.add( closes.get( i ) );
      }

      System.out.println( "" );

      for( Double dl : last )
      {
         ddd *= dl;
         System.out.println( ddd );
      }

      Collections.sort( last );
      System.out.println( "" );

      for( int j=0; j<l; j++ )
      {
         sim = new ArrayList< Double >();
         for( int k=0; k<ll; k++ )
         {
            sim.add( last.get( (int) ( mu.random() * last.size() ) ) );
         }
         su.calculateStats( sim );
         dd *= su.getMean();
//          dd *= su.getMedian();
         System.out.println( dd );
      }

   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void determineQuantileDistance()
   {
//       StockElement        qqq    = getStockElement( stock );
//       StockElement        qqq    = getStockElement( stock );
      String              stock  = "QQQ";
      StockElement        qqq    = getStockElement( stock );
      ArrayList< String > dates  = qqq.getDates();
      ArrayList< Double > q      = null;
      ArrayList< Double > p      = null;
      ArrayList< Double > dd     = new ArrayList< Double >();
      MathUtilities       mu     = new MathUtilities();
      StatUtilities       su     = new StatUtilities();
      int                 len    = 10;
      int                 step   = 5;
      String              d      = null;
      double              dist   = 0;

      qqq.calculatePercentages();

      for( int i=0; i<dates.size(); i+=step )
      {
         d = dates.get( i );
         q = qqq.getQuantile( d, len );

         if( p != null )
         {
            dist = mu.getAbsoluteDistance( p, q );
            dd.add( dist );
         }

         p = q;
      }

      Collections.sort( dd );

      for( Double obj : dd )
      {
         System.out.println( obj );
      }

      double[] quartiles = determineQuantileDistance( stock, len, step );
      System.out.println( "first quartile:  " + quartiles[ 0 ] );
      System.out.println( "second quartile: " + quartiles[ 1 ] );
      System.out.println( "third quartile:  " + quartiles[ 2 ] );
   }

   /**
    * This finds the quartiles for the average PDF distance.
    * @param TYPE
    * @return TYPE
    */
   public double[] determineQuantileDistance( String stock, int len, int step )
   {
      StockElement        qqq    = getStockElement( stock );
      ArrayList< String > dates  = qqq.getDates();
      ArrayList< Double > q      = null;
      ArrayList< Double > p      = null;
      ArrayList< Double > dd     = new ArrayList< Double >();
      MathUtilities       mu     = new MathUtilities();
      String              d      = null;
      double              dist   = 0;

      qqq.calculatePercentages();

      for( int i=0; i<dates.size(); i+=step )
      {
         d = dates.get( i );
         q = qqq.getQuantile( d, len );

         if( p != null )
         {
            dist = mu.getAbsoluteDistance( p, q );
            dd.add( dist );
         }

         p = q;
      }

      Collections.sort( dd );

      int      q1        = (int) ( 0.25 * dd.size() ) + 1;
      double[] quartiles = new double[ 3 ];
      quartiles[ 0 ]     = dd.get( q1 );
      quartiles[ 1 ]     = dd.get( 2 * q1 );
      quartiles[ 2 ]     = dd.get( 3 * q1 );

      return quartiles;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void recognizeQuantiles()
   {
//       String         name           = "QQQ";
//       String         name           = "MSFT";
//       String         name           = "AAPL";
//       String         name           = "GOOG";
//       String         name           = "CSCO";
//       String         name           = "ORCL";
      ArrayList< String > names = new ArrayList< String >();
      names.add( "QQQ"  );
      names.add( "MSFT" );
      names.add( "AAPL" );
      names.add( "GOOG" );
      names.add( "CSCO" );
      names.add( "ORCL" );
      for( String name : names )
      {
         StockElement   element        = getStockElement( name );
         int            window         = 10;
         int            step           = 2;
         double[]       quant          = determineQuantileDistance( name, window, step );
         DecisionForest decisionForest = ( DecisionForest ) ( new DecisionForest( name + ".DF" ) ).getDAO().deserialize();
         MathUtilities  mu             = new MathUtilities();

         element.calculatePercentages();

         if( decisionForest == null )
         {
            System.out.println( "Creating a new DecisionForest" );
            decisionForest = new DecisionForest( name + ".DF", 1, quant[ 0 ], 1 );
            decisionForest.setTreeGenomeLength( 32 );
            System.out.println( "CIM Decision Forest: " + decisionForest );
         }
         else
         {
            System.out.println( "Deserialized DecisionForest" );
            System.out.println( "CIM Decision Forest: " + decisionForest );
         }
         decisionForest.setIsAsynchronous( false );

         ArrayList< String > dates = element.getDates();
         for( int i=0; i<dates.size() - window; i+=step )
         {
            System.out.println( "Using a date of: " + dates.get( i ) );
            ArrayList< Double > quantile = element.getQuantile( dates.get( i ), window );

//             for( Double obj : quantile )
//             {
//                System.out.println( "a quantile value: " + obj );
//             }

            double[]            rvector    = mu.toArray( quantile );
            BigInteger[]        classes    = decisionForest.classify( rvector );
//             try { Thread.sleep( 1000 ); } catch( InterruptedException e ) { }
            String[]            addresses  = decisionForest.getTreeAddresses();

            for( int j=0; j<addresses.length; j++ )
            {
               System.out.println( "an address: " + addresses[ j ] );
            }
   //          for( int j=0; j<classes.length; j++ )
   //          {
   //             System.out.println( "a class: " + classes[ j ] );
   //          }
         }

//          try
//          {
//             System.out.println( "Will now sleep..." );
//             Thread.sleep( 1000 );
//             System.out.println( "Finished sleeping..." );
//          }
//          catch( InterruptedException e )
//          {
//             System.out.println( "Caught interrupted exception: " + e );
//          }

         DecisionForestDAO dfDAO = ( DecisionForestDAO ) decisionForest.getDAO();
         decisionForest.setCanSplit( false );
         dfDAO.serialize( decisionForest );
         System.out.println( "CIM Decision Forest: " + decisionForest );
      }
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public double mapQuantiles()
   {
      int            window             = 10;
      int            step               = 2;
      MathUtilities  mu                 = new MathUtilities();
      String         nameQQQ            = "QQQ";
      String         nameMSFT           = "MSFT";
      String         nameAAPL           = "AAPL";
      String         nameGOOG           = "GOOG";
      String         nameCSCO           = "CSCO";
      String         nameORCL           = "ORCL";
      StockElement   qqq                = getStockElement( nameQQQ  );
      StockElement   msft               = getStockElement( nameMSFT );
      StockElement   aapl               = getStockElement( nameAAPL );
      StockElement   goog               = getStockElement( nameGOOG );
      StockElement   csco               = getStockElement( nameCSCO );
      StockElement   orcl               = getStockElement( nameORCL );
      double         capital            = 20000.0;
      double         ratio              = 0.0;
      double[]       quantQQQ           = determineQuantileDistance( nameQQQ,  window, step );
      double[]       quantMSFT          = determineQuantileDistance( nameMSFT, window, step );
      double[]       quantAAPL          = determineQuantileDistance( nameAAPL, window, step );
      double[]       quantGOOG          = determineQuantileDistance( nameGOOG, window, step );
      double[]       quantCSCO          = determineQuantileDistance( nameCSCO, window, step );
      double[]       quantORCL          = determineQuantileDistance( nameORCL, window, step );
      DecisionForest decisionForestQQQ  = ( DecisionForest ) ( new DecisionForest( nameQQQ  + ".DF" ) ).getDAO().deserialize();
      DecisionForest decisionForestMSFT = ( DecisionForest ) ( new DecisionForest( nameMSFT + ".DF" ) ).getDAO().deserialize();
      DecisionForest decisionForestAAPL = ( DecisionForest ) ( new DecisionForest( nameAAPL + ".DF" ) ).getDAO().deserialize();
      DecisionForest decisionForestGOOG = ( DecisionForest ) ( new DecisionForest( nameGOOG + ".DF" ) ).getDAO().deserialize();
      DecisionForest decisionForestCSCO = ( DecisionForest ) ( new DecisionForest( nameCSCO + ".DF" ) ).getDAO().deserialize();
      DecisionForest decisionForestORCL = ( DecisionForest ) ( new DecisionForest( nameORCL + ".DF" ) ).getDAO().deserialize();

      qqq.calculatePercentages();
      msft.calculatePercentages();
      aapl.calculatePercentages();
      goog.calculatePercentages();
      csco.calculatePercentages();
      orcl.calculatePercentages();

      // for exmaple, <key> -> "QQQ" --> "class"     --> (BigInteger)
      // for exmaple, <key> -> "QQQ" --> "quantile"  --> (ArrayList< Double >)
      // for exmaple, <key> -> <key> --> "next-key"  --> (ArrayList< String >)
      // for exmaple, <key> -> <key> --> "simulacra" --> (HashMap< String, Double >)
      HashMap< String, HashMap< String, HashMap< String, Object > > > theMap = new HashMap< String, HashMap< String, HashMap< String, Object > > >();
      HashMap< String, Integer > counts               = new HashMap< String, Integer >();
      ArrayList< String >        dates                = qqq.getDates();
      String                     key                  = null;
      String                     previousKey          = null;
      String                     c                    = "class";
      String                     q                    = "quant";
      String                     n                    = "next-key";
      String                     s                    = "simulacra";
      Integer                    count                = null;
      ArrayList< String >        keyList              = null;

      for( int i=0; i<dates.size() - window; i+=step )
      {
//          System.out.println( "Using a date of: " + dates.get( i ) );
         ArrayList< Double > quantileQQQ  = qqq .getQuantile( dates.get( i ), window );
         ArrayList< Double > quantileMSFT = msft.getQuantile( dates.get( i ), window );
         ArrayList< Double > quantileAAPL = aapl.getQuantile( dates.get( i ), window );
         ArrayList< Double > quantileGOOG = goog.getQuantile( dates.get( i ), window );
         ArrayList< Double > quantileCSCO = csco.getQuantile( dates.get( i ), window );
         ArrayList< Double > quantileORCL = orcl.getQuantile( dates.get( i ), window );

         if(    quantileQQQ. size() == 0 
             || quantileMSFT.size() == 0 
             || quantileAAPL.size() == 0 
             || quantileGOOG.size() == 0 
             || quantileCSCO.size() == 0 
             || quantileORCL.size() == 0 )
         {
//             System.out.println( "continuing for date: " + dates.get( i ) );
            continue;
         }

         double[]     rvectorQQQ     = mu.toArray( quantileQQQ  );
         double[]     rvectorMSFT    = mu.toArray( quantileMSFT );
         double[]     rvectorAAPL    = mu.toArray( quantileAAPL );
         double[]     rvectorGOOG    = mu.toArray( quantileGOOG );
         double[]     rvectorCSCO    = mu.toArray( quantileCSCO );
         double[]     rvectorORCL    = mu.toArray( quantileORCL );

         BigInteger[] classesQQQ     = decisionForestQQQ .classify( rvectorQQQ  );
         BigInteger[] classesMSFT    = decisionForestMSFT.classify( rvectorMSFT );
         BigInteger[] classesAAPL    = decisionForestAAPL.classify( rvectorAAPL );
         BigInteger[] classesGOOG    = decisionForestGOOG.classify( rvectorGOOG );
         BigInteger[] classesCSCO    = decisionForestCSCO.classify( rvectorCSCO );
         BigInteger[] classesORCL    = decisionForestORCL.classify( rvectorORCL );

         String[]     addressesQQQ   = decisionForestQQQ .getTreeAddresses();
         String[]     addressesMSFT  = decisionForestMSFT.getTreeAddresses();
         String[]     addressesAAPL  = decisionForestAAPL.getTreeAddresses();
         String[]     addressesGOOG  = decisionForestGOOG.getTreeAddresses();
         String[]     addressesCSCO  = decisionForestCSCO.getTreeAddresses();
         String[]     addressesORCL  = decisionForestORCL.getTreeAddresses();

//          System.out.println( "QQQ  an address: " + addressesQQQ [ 0 ] );
//          System.out.println( "MSFT an address: " + addressesMSFT[ 0 ] );
//          System.out.println( "AAPL an address: " + addressesAAPL[ 0 ] );
//          System.out.println( "GOOG an address: " + addressesGOOG[ 0 ] );
//          System.out.println( "CSCO an address: " + addressesCSCO[ 0 ] );
//          System.out.println( "ORCL an address: " + addressesORCL[ 0 ] );

         key   = addressesQQQ [ 0 ] + "-" + addressesMSFT[ 0 ] + "-" + addressesAAPL[ 0 ] + "-" + 
                 addressesGOOG[ 0 ] + "-" + addressesCSCO[ 0 ] + "-" + addressesORCL[ 0 ];
         count = ( counts.get( key ) == null ) ? 1 : counts.get( key ) + 1;
         counts.put( key, count );
         previousKey = ( previousKey == null) ? key : previousKey;

         // first, get the top-level map for this key/context
         HashMap< String, HashMap< String, Object > > keyMap = theMap.get( key );
         if( keyMap == null )
         {
            keyMap = new HashMap< String, HashMap< String, Object > >();
            theMap.put( key, keyMap );
         }
         // update the next-key mapping if it exists
         HashMap< String, HashMap< String, Object > > previousKeyMap = theMap.get( previousKey );
         HashMap< String, Object >                    keyListMap     = previousKeyMap.get( previousKey );
         if( keyListMap == null )
         {
            keyListMap = new HashMap< String, Object >();
            previousKeyMap.put( previousKey, keyListMap );
         }
         keyList = (ArrayList< String >) keyListMap.get( n );
         if( keyList == null )
         {
            keyList = new ArrayList< String >();
            keyListMap.put( n, keyList );
         }
         keyList.add( key );
//          System.out.println( "added key: " + key + " for: " + previousKey );
         // now get the individual stock maps
         HashMap< String, Object > mapQQQ  = keyMap.get( nameQQQ  );
         if( mapQQQ == null )
         {
            mapQQQ = new HashMap< String, Object >();
            keyMap.put( nameQQQ, mapQQQ );
         }
         HashMap< String, Object > mapMSFT = keyMap.get( nameMSFT );
         if( mapMSFT == null )
         {
            mapMSFT = new HashMap< String, Object >();
            keyMap.put( nameMSFT, mapMSFT );
         }
         HashMap< String, Object > mapAAPL = keyMap.get( nameAAPL );
         if( mapAAPL == null )
         {
            mapAAPL = new HashMap< String, Object >();
            keyMap.put( nameAAPL, mapAAPL );
         }
         HashMap< String, Object > mapGOOG = keyMap.get( nameGOOG );
         if( mapGOOG == null )
         {
            mapGOOG = new HashMap< String, Object >();
            keyMap.put( nameGOOG, mapGOOG );
         }
         HashMap< String, Object > mapCSCO = keyMap.get( nameCSCO );
         if( mapCSCO == null )
         {
            mapCSCO = new HashMap< String, Object >();
            keyMap.put( nameCSCO, mapCSCO );
         }
         HashMap< String, Object > mapORCL = keyMap.get( nameORCL );
         if( mapORCL == null )
         {
            mapORCL = new HashMap< String, Object >();
            keyMap.put( nameORCL, mapORCL );
         }

         mapQQQ. put( c, classesQQQ [ 0 ] );
         mapMSFT.put( c, classesMSFT[ 0 ] );
         mapAAPL.put( c, classesAAPL[ 0 ] );
         mapGOOG.put( c, classesGOOG[ 0 ] );
         mapCSCO.put( c, classesCSCO[ 0 ] );
         mapORCL.put( c, classesORCL[ 0 ] );

         mapQQQ. put( q, quantileQQQ  );
         mapMSFT.put( q, quantileMSFT );
         mapAAPL.put( q, quantileAAPL );
         mapGOOG.put( q, quantileGOOG );
         mapCSCO.put( q, quantileCSCO );
         mapORCL.put( q, quantileORCL );

         previousKey = key;
      }

      // now do some print outs and things:
//       for( String obj : counts.keySet() )
//       {
//          Integer v = counts.get( obj );
//          System.out.println( "Key: " + obj + " count: " + v );
//       }

      ArrayList< String > nameList = new ArrayList< String >();
      nameList.add( nameQQQ  );
      nameList.add( nameMSFT );
      nameList.add( nameAAPL );
      nameList.add( nameGOOG );
      nameList.add( nameCSCO );
      nameList.add( nameORCL );
      long[] quartiles = getFirstQuartiles( theMap, nameList );
      for( int i=0; i<quartiles.length; i++ )
      {
         System.out.println( nameList.get( i ) + " first quartile: " + quartiles[ i ] );
      }
      determineSimulacra( theMap, quartiles );
      // print out the simulacra, if there are any
      for( String currentKey : theMap.keySet() )
      {
         HashMap< String, HashMap< String, Object > > firstKeyMap  = theMap.get( currentKey );
         HashMap< String, Object >                    secondKeyMap = firstKeyMap.get( currentKey );
         if( secondKeyMap == null )
         {
            continue;
         }
         HashMap< String, Double >                    simulacra    = (HashMap< String, Double >) secondKeyMap.get( s );
//          System.out.println( "Simulacra for: " + currentKey );
//          for( String simKey : simulacra.keySet() )
//          {
//             System.out.println( "   -> " + simulacra.get( simKey ) );
//          }
      }

      // Simulate -- the idea here is as follows.
      // For a given date, get the window-based quantile data and then classify for each stock.
      // Using the hash map data, find the context key that most closely matches this set of
      // classes scoring by overall distance. The closest match is all that is needed since
      // it will have its own simulacra that will include any other near matches to the current
      // set of classes/context--actually this is not true because this is the current match
      // not the simulacra of the "next-key" data set.
      // At any rate, with the closesest context key match, pull the "next-key" context value
      // and then for this context, put the "simulacra" to find all the contexts that are 
      // similar to this next key/context set.
      // Now random variates can be generated by using the roulette wheel strategy: for each 
      // simulacra, use the distance from it to the "next-key" object as the bin size.
      // Map those distances into [0, 1] and then take the complement to get the actual bin
      // widths for the roulette algorithm. In order for this to work, we'll need the assocaited
      // distances from the "next-key" to its simulacra.
      // Then it's a simple matter to produce the random variates using roulette wheel and the
      // quantile functions for the selected context key.
      // This, however, brings up a problem -- how do we handle the "next-key" context itself?
      // If its distance to itself is zero, the complement will be 1, and thus get chosen every
      // time in the roulette algorithm...I suppose we could make it the same value as the 
      // smallest distance in the simulacra group and then map all of them into [0, 1].
      double       qqq_error        = 0.0;
      double       agg_error        = 0.0;
      step                          = 20;
      boolean      in_position      = false;
      int          entry_point      = 0;
      int          count1           = 0;
      int          count2           = 0;
      int          count3           = 0;
      int          correct1         = 0;
      int          correct2         = 0;
      int          correct3         = 0;
      int          pred_length      = 20;
      for( int i=0; i<dates.size() - window - pred_length; i+=step )
      {
         System.out.println( "Using a date of: " + dates.get( i ) );
         ArrayList< Double > quantileQQQ  = qqq .getQuantile( dates.get( i ), window );
         ArrayList< Double > quantileMSFT = msft.getQuantile( dates.get( i ), window );
         ArrayList< Double > quantileAAPL = aapl.getQuantile( dates.get( i ), window );
         ArrayList< Double > quantileGOOG = goog.getQuantile( dates.get( i ), window );
         ArrayList< Double > quantileCSCO = csco.getQuantile( dates.get( i ), window );
         ArrayList< Double > quantileORCL = orcl.getQuantile( dates.get( i ), window );

         if(    quantileQQQ .size() == 0 
             || quantileMSFT.size() == 0 
             || quantileAAPL.size() == 0 
             || quantileGOOG.size() == 0 
             || quantileCSCO.size() == 0 
             || quantileORCL.size() == 0 )
         {
            continue;
         }

         double[]     rvectorQQQ     = mu.toArray( quantileQQQ  );
         double[]     rvectorMSFT    = mu.toArray( quantileMSFT );
         double[]     rvectorAAPL    = mu.toArray( quantileAAPL );
         double[]     rvectorGOOG    = mu.toArray( quantileGOOG );
         double[]     rvectorCSCO    = mu.toArray( quantileCSCO );
         double[]     rvectorORCL    = mu.toArray( quantileORCL );

         BigInteger[] classesQQQ     = decisionForestQQQ .classify( rvectorQQQ  );
         BigInteger[] classesMSFT    = decisionForestMSFT.classify( rvectorMSFT );
         BigInteger[] classesAAPL    = decisionForestAAPL.classify( rvectorAAPL );
         BigInteger[] classesGOOG    = decisionForestGOOG.classify( rvectorGOOG );
         BigInteger[] classesCSCO    = decisionForestCSCO.classify( rvectorCSCO );
         BigInteger[] classesORCL    = decisionForestORCL.classify( rvectorORCL );

         HashMap< String, HashMap< String, Object > > firstKeyMap  = null;
         HashMap< String, Object >                    secondKeyMap = null;
         HashMap< String, Object >                    stockMap     = null;
         HashMap< String, Double >                    simulacra    = null;

         int          qqq_date_index   = 0;
         int          msft_date_index  = 0;
         int          aapl_date_index  = 0;
         int          goog_date_index  = 0;
         int          csco_date_index  = 0;
         int          orcl_date_index  = 0;
         long         smallest         = Long.MAX_VALUE;
         String       smallestKey      = "";
         long         distance         = 0;
         double       variate          = 0.0;
         double       agg_variate      = 0.0;
         double       roulette         = 0.0;
         double       qqq_aggregate    = 0.0;
         double       qqq_previous     = 0.0;
         double       qqq_agg_previous = 0.0;
         double       msft_previous    = 0.0;
         double       aapl_previous    = 0.0;
         double       goog_previous    = 0.0;
         double       csco_previous    = 0.0;
         double       orcl_previous    = 0.0;
         boolean      agg_first        = true;
         boolean      qqq_first        = true;
         boolean      msft_first       = true;
         boolean      aapl_first       = true;
         boolean      goog_first       = true;
         boolean      csco_first       = true;
         boolean      orcl_first       = true;
                                       
         for( String currentKey : theMap.keySet() )
         {                             
            firstKeyMap  = theMap.get( currentKey );
            distance     = 0;

            stockMap     = firstKeyMap.get( nameQQQ );
            distance    += ( classesQQQ [ 0 ].xor( (BigInteger) stockMap.get( c ) ) ).longValue();
            stockMap     = firstKeyMap.get( nameMSFT );
            distance    += ( classesMSFT[ 0 ].xor( (BigInteger) stockMap.get( c ) ) ).longValue();
            stockMap     = firstKeyMap.get( nameAAPL );
            distance    += ( classesAAPL[ 0 ].xor( (BigInteger) stockMap.get( c ) ) ).longValue();
            stockMap     = firstKeyMap.get( nameGOOG );
            distance    += ( classesGOOG[ 0 ].xor( (BigInteger) stockMap.get( c ) ) ).longValue();
            stockMap     = firstKeyMap.get( nameCSCO );
            distance    += ( classesCSCO[ 0 ].xor( (BigInteger) stockMap.get( c ) ) ).longValue();
            stockMap     = firstKeyMap.get( nameORCL );
            distance    += ( classesORCL[ 0 ].xor( (BigInteger) stockMap.get( c ) ) ).longValue();

            if( distance < smallest )
            {
               smallest    = distance;
               smallestKey = currentKey;
            }
//             System.out.println( "currentKey: " + currentKey + " distance: " + distance );
         }

         firstKeyMap      = theMap.get( smallestKey );
         secondKeyMap     = firstKeyMap.get( smallestKey );
         simulacra        = (HashMap< String, Double >) secondKeyMap.get( s );
         qqq_date_index   = qqq .getDateIndex( dates.get( i ) );
         msft_date_index  = msft.getDateIndex( dates.get( i ) );
         aapl_date_index  = aapl.getDateIndex( dates.get( i ) );
         goog_date_index  = goog.getDateIndex( dates.get( i ) );
         csco_date_index  = csco.getDateIndex( dates.get( i ) );
         orcl_date_index  = orcl.getDateIndex( dates.get( i ) );

         System.out.println( "### QQQ Prediction for: " + dates.get( qqq_date_index + window - 1 ) + 
                             " through: " + dates.get( qqq_date_index + window + pred_length - 1 ) );
         System.out.println( "smallest dist: "        + smallest + 
                             " number of simulacra: " + simulacra.keySet().size() );
         System.out.println( "smallest key: " + smallestKey );

         for( int j=( window - 1 ); j<( window + pred_length ); j++ )
         {
            variate       = mu.random();
            smallestKey   = null;
            qqq_aggregate = 0.0;
            roulette      = 0.0;

            for( String obj : simulacra.keySet() )
            {
               roulette += simulacra.get( obj );

               if( roulette >= variate )
               {
                  smallestKey = obj;
                  break;
               }
            }
//             System.out.println( "chosen simulacra: " + smallestKey );

            firstKeyMap  = theMap.get( smallestKey );

            // MSFT
            stockMap     = firstKeyMap.get( nameMSFT );
            quantileMSFT = (ArrayList< Double >) stockMap.get( q );
            roulette     = quantileMSFT.get( (int) ( ( (double) quantileMSFT.size() ) * mu.random() ) );
            if( msft_first )
            {
               msft_first = false;
               variate    = msft.getClose( j + msft_date_index ) * roulette;
            }
            else
            {
               variate   = msft_previous * roulette;
            }
//             System.out.print( msft.getClose( j + msft_date_index + 1 ) + " , " + variate + " ,roultte: " + roulette + "," );
            msft_previous    = variate;
            qqq_aggregate   += roulette;

            // AAPL
            stockMap     = firstKeyMap.get( nameAAPL );
            quantileAAPL = (ArrayList< Double >) stockMap.get( q );
            roulette     = quantileAAPL.get( (int) ( ( (double) quantileAAPL.size() ) * mu.random() ) );
            if( aapl_first )
            {
               aapl_first = false;
               variate    = aapl.getClose( j + aapl_date_index ) * roulette;
            }
            else
            {
               variate   = aapl_previous * roulette;
            }
//             System.out.print( aapl.getClose( j + aapl_date_index + 1 ) + " , " + variate + " ,roultte: " + roulette + "," );
            aapl_previous    = variate;
            qqq_aggregate   += roulette;

            // GOOG
            stockMap     = firstKeyMap.get( nameGOOG );
            quantileGOOG = (ArrayList< Double >) stockMap.get( q );
            roulette     = quantileGOOG.get( (int) ( ( (double) quantileGOOG.size() ) * mu.random() ) );
            if( goog_first )
            {
               goog_first = false;
               variate    = goog.getClose( j + goog_date_index ) * roulette;
            }
            else
            {
               variate   = goog_previous * roulette;
            }
//             System.out.print( goog.getClose( j + goog_date_index + 1 ) + " , " + variate + " ,roultte: " + roulette + "," );
            goog_previous    = variate;
            qqq_aggregate   += roulette;

            // CSCO
            stockMap     = firstKeyMap.get( nameCSCO );
            quantileCSCO = (ArrayList< Double >) stockMap.get( q );
            roulette     = quantileCSCO.get( (int) ( ( (double) quantileCSCO.size() ) * mu.random() ) );
            if( csco_first )
            {
               csco_first = false;
               variate    = csco.getClose( j + csco_date_index ) * roulette;
            }
            else
            {
               variate   = csco_previous * roulette;
            }
//             System.out.print( csco.getClose( j + csco_date_index + 1 ) + " , " + variate + " ,roultte: " + roulette + "," );
            csco_previous    = variate;
            qqq_aggregate   += roulette;

            // ORCL
            stockMap     = firstKeyMap.get( nameORCL );
            quantileORCL = (ArrayList< Double >) stockMap.get( q );
            roulette     = quantileORCL.get( (int) ( ( (double) quantileORCL.size() ) * mu.random() ) );
            if( orcl_first )
            {
               orcl_first = false;
               variate    = orcl.getClose( j + orcl_date_index ) * roulette;
            }
            else
            {
               variate   = orcl_previous * roulette;
            }
//             System.out.print( orcl.getClose( j + orcl_date_index + 1 ) + " , " + variate + " ,roultte: " + roulette + "," );
            orcl_previous    = variate;
            qqq_aggregate   += roulette;
            qqq_aggregate   /= 5.0;

            // QQQ
            stockMap     = firstKeyMap.get( nameQQQ );
            quantileQQQ  = (ArrayList< Double >) stockMap.get( q );
            roulette     = quantileQQQ.get( (int) ( ( (double) quantileQQQ.size() ) * mu.random() ) );
            if( qqq_first )
            {
               qqq_first   = false;
               variate     = qqq.getClose( j + qqq_date_index ) * roulette;
               agg_variate = qqq.getClose( j + qqq_date_index ) * qqq_aggregate;
            }
            else
            {
               variate     = qqq_previous     * roulette;
               agg_variate = qqq_agg_previous * qqq_aggregate;
            }
            qqq_previous     = variate;
//             qqq_aggregate   += roulette;

//             qqq_aggregate   /= 6.0;
            
            // AGG
            if( agg_first )
            {
               agg_first   = false;
               agg_variate = qqq.getClose( j + qqq_date_index ) * qqq_aggregate;
            }
            else
            {
               agg_variate = qqq_agg_previous * qqq_aggregate;
            }
            qqq_agg_previous = agg_variate;

            System.out.print( qqq.getClose( j + qqq_date_index ) + ", " + qqq.getClose( j + qqq_date_index + 1 ) + ", " + variate + ", " + agg_variate + ", " + capital );
            System.out.println( "" );
         }

//          if(    variate     > qqq.getClose( qqq_date_index + window - 1 )
//              && agg_variate > qqq.getClose( qqq_date_index + window - 1 ) )
         /*
         if( agg_variate > qqq.getClose( qqq_date_index + window - 1 ) )
//          if( variate > qqq.getClose( qqq_date_index + window - 1 ) )
         {
            System.out.println( "buying at: "   + qqq.getClose( ( qqq_date_index + window - 1 ) ) + 
                                " selling at: " + qqq.getClose( ( qqq_date_index + window + pred_length - 1 ) ) );

            double amt = qqq.performBuy( capital, ( qqq_date_index + window - 1 ), ( qqq_date_index + window + pred_length - 1 ) );
            System.out.println( "amt:," + amt + ",ratio:," + ( qqq.getClose( ( qqq_date_index + window + pred_length - 1 ) ) / qqq.getClose( ( qqq_date_index + window - 1 ) ) ) );

            double open   = qqq.getClose( ( qqq_date_index + window - 1 ) );
            double close  = qqq.getClose( ( qqq_date_index + window + pred_length - 1 ) );
            int    shares = ( int )Math.ceil( capital / open );
            // 1st ootm
//             double ins    = 0.006 * open * shares;
            // 2nd ootm
            double ins    = 0.0033 * open * shares;
            // 3rd ootm
//             double ins    = 0.00175 * open * shares;
            // 1st ootm
//             if( ( close / open ) <= 0.985 )
            // 2nd ootm
            if( ( close / open ) <= 0.976 )
            // 3rd ootm
//             if( ( close / open ) <= 0.9664 )
            {
               // 1st ootm
//                capital += qqq.performBuy( capital, open, ( open * 0.985 ) );
               // 2nd ootm
               capital += qqq.performBuy( capital, open, ( open * 0.976 ) );
               // 3rd ootm
//                capital += qqq.performBuy( capital, open, ( open * 0.9664 ) );
               capital -= 30;
            }
            else
            {
               capital += amt;
               capital -= 15;
            }
            capital -= ins;
            entry_point++;
//             for( int j=( qqq_date_index + window - 1 ); j<( qqq_date_index + window + pred_length ); j++ )
//             {
//                System.out.println( j + " : a close: " + qqq.getClose( j ) + " count: " + entry_point + " correct: " + correct );
//             }
            if(   qqq.getClose( ( qqq_date_index + window - 1 ) ) 
                < qqq.getClose( ( qqq_date_index + window + pred_length - 1 ) ) )
            {
               correct++;
            }
         }
         System.out.println( "Correctness ratio: " + ( ( double )correct / ( double )entry_point ) );
         */
//          else if(    variate     < qqq.getClose( qqq_date_index + window - 1 )
//                   && agg_variate < qqq.getClose( qqq_date_index + window - 1 ) )
//          {
//             entry_point++;
//             if(   qqq.getClose( ( qqq_date_index + window - 1 ) ) 
//                 > qqq.getClose( ( qqq_date_index + window + pred_length ) ) )
//             {
//                correct++;
//             }
//          }
//          System.out.println( " count: " + entry_point + " correct: " + correct + " ratio: " + ( ( double ) correct / ( double ) entry_point ) );

//          if(    variate     < qqq.getClose( qqq_date_index + window - 1 )
//              && agg_variate < qqq.getClose( qqq_date_index + window - 1 ) )
//          {
//             capital += qqq.performBuy( capital, ( qqq_date_index + window + pred_length ), ( qqq_date_index + window - 1 ) );
//             for( int j=( qqq_date_index + window - 1 ); j<( qqq_date_index + window + pred_length ); j++ )
//             {
//                System.out.println( j + " : a close: " + qqq.getClose( j ) );
//             }
//          }

         if( in_position )
         {
//             if(    variate     > qqq.getClose( qqq_date_index + window - 1 )
//                 && agg_variate > qqq.getClose( qqq_date_index + window - 1 ) )
            if(   agg_variate     > qqq.getClose( qqq_date_index + window - 1 ) )
            {
               System.out.println( i + " : staying in position: " + qqq.getClose( qqq_date_index + window - 1 ) );
            }
            else
            {
               System.out.println( i + " : exiting position: " + qqq.getClose( entry_point ) + ", " + qqq.getClose( qqq_date_index + window - 1 ) );
               capital     += qqq.performBuy( capital, entry_point, qqq_date_index + window - 1 );
               in_position  = false;
            }
         }
         else
         {
//             if(    variate     > qqq.getClose( qqq_date_index + window - 1 )
//                 && agg_variate > qqq.getClose( qqq_date_index + window - 1 ) )
            if(   agg_variate     > qqq.getClose( qqq_date_index + window - 1 ) )
            {
//                entry_point = ( qqq_date_index + window - 1 );
               in_position = true;
            }
         }

         // determine correctness ratio...
         // For a 1 ootm 10 day put write, the no-loss window is: [ 0.98345, 1.00743 ]
         double open  = qqq.getClose( qqq_date_index + window - 1 );
         double close = qqq.getClose( qqq_date_index + window - 1 + pred_length );
         ratio        = agg_variate / open;
         entry_point++;
         if( ratio >= 0.98345 && ratio <= 1.00743 )
         {
            count1++;
            ratio = close / open;
            if( ratio >= 0.98345 && ratio <= 1.00743 )
            {
               correct1++;
            }
         }
         else if( ratio <= 0.98345 )
         {
            count2++;
            ratio = close / open;
            if( ratio <= 0.98345 )
//             if( ratio >= 0.98345 )
//             if( ratio >= 0.98345 && ratio <= 1.00743 )
//             if( ratio <= 1.00743 )
            {
               correct2++;
            }
         }
         else if( ratio >= 1.00743 )
         {
            count3++;
            ratio = close / open;
            if( ratio >= 1.00743 )
            {
               correct3++;
            }
         }
         /*
         if( ratio >= 1.0 )
         {
            ratio = close / open;
            if( ratio >= 1.0 )
//             if( ratio <= 1.0 )
            {
               correct++;
            }
         }
//          else if( ratio <= 1.0 )
         else 
         {
            ratio = close / open;
            if( ratio <= 1.0 )
//             if( ratio >= 1.0 )
            {
               correct++;
            }
         }
         */
         ratio = ( double )correct1 / ( double )count1;
         System.out.println( "Correctness ratio1: " + ratio );
         ratio = ( double )correct2 / ( double )count2;
         System.out.println( "Correctness ratio2: " + ratio );
         ratio = ( double )correct3 / ( double )count3;
         System.out.println( "Correctness ratio3: " + ratio );
      }

      // finally, save things back:
      DecisionForestDAO dfDAOQQQ  = ( DecisionForestDAO ) decisionForestQQQ .getDAO();
      DecisionForestDAO dfDAOMSFT = ( DecisionForestDAO ) decisionForestMSFT.getDAO();
      DecisionForestDAO dfDAOAAPL = ( DecisionForestDAO ) decisionForestAAPL.getDAO();
      DecisionForestDAO dfDAOGOOG = ( DecisionForestDAO ) decisionForestGOOG.getDAO();
      DecisionForestDAO dfDAOCSCO = ( DecisionForestDAO ) decisionForestCSCO.getDAO();
      DecisionForestDAO dfDAOORCL = ( DecisionForestDAO ) decisionForestORCL.getDAO();
      decisionForestQQQ .setCanSplit( false );
      decisionForestMSFT.setCanSplit( false );
      decisionForestAAPL.setCanSplit( false );
      decisionForestGOOG.setCanSplit( false );
      decisionForestCSCO.setCanSplit( false );
      decisionForestORCL.setCanSplit( false );
      dfDAOQQQ .serialize( decisionForestQQQ  );
      dfDAOMSFT.serialize( decisionForestMSFT );
      dfDAOAAPL.serialize( decisionForestAAPL );
      dfDAOGOOG.serialize( decisionForestGOOG );
      dfDAOCSCO.serialize( decisionForestCSCO );
      dfDAOORCL.serialize( decisionForestORCL );
//       return capital;
      return ratio;
   }

   /**
    *
    */
   private long[] getFirstQuartiles( HashMap< String, HashMap< String, HashMap< String, Object > > > theMap, 
                                     ArrayList< String >                                             names )
   {
      long[]   quartiles = new long[ names.size() ];
      String   c         = "class";
      String   q         = "quant";
      String   n         = "next-key";

      for( int i=0; i<names.size(); i++ )
      {
         String            name      = names.get( i );
         ArrayList< Long > distances = new ArrayList< Long >();

         for( String k : theMap.keySet() )
         {
            HashMap< String, HashMap< String, Object > > keyMap     = theMap.get( k );
            HashMap< String, Object >                    nextKeyMap = keyMap.get( k );
            HashMap< String, Object >                    mapQQQ     = keyMap.get( name  );
            if( nextKeyMap == null )
            {
               continue;
            }
            ArrayList< String >                          nextKeys   = ( ArrayList< String > ) nextKeyMap.get( n );
            BigInteger                                   pClass     = ( BigInteger ) mapQQQ.get( c );
            BigInteger                                   cClass     = null;
            BigInteger                                   dist       = null;

            for( String nextKey : nextKeys )
            {
               keyMap     = theMap.get( nextKey );
               mapQQQ     = keyMap.get( name  );
               cClass     = (BigInteger) mapQQQ.get( c );
               dist       = pClass.xor( cClass );

               if( dist.longValue() != 0 )
               {
                  distances.add( dist.longValue() );
               }
            }
         }

         Collections.sort( distances );
//          quartiles[ i ] = distances.get( (int)( distances.size() * 0.125 ) );
         quartiles[ i ] = distances.get( (int)( distances.size() * 0.72 ) );
//          quartiles[ i ] = distances.get( (int)( distances.size() * 0.78 ) );
      }

      return quartiles;
   }

   /**
    * Here, we add to the list of mappings, the similar keys to the current key.
    * for exmaple, <key> -> "QQQ" --> "class"     --> (BigInteger)
    * for exmaple, <key> -> "QQQ" --> "quantile"  --> (ArrayList< Double >)
    * for exmaple, <key> -> <key> --> "next-key"  --> (ArrayList< String >)
    * for exmaple, <key> -> <key> --> "simulacra" --> (HashMap< String, Double >)
    */
   private void determineSimulacra( HashMap< String, HashMap< String, HashMap< String, Object > > > theMap, 
                                    long[]                                                          quartiles )
   {
      BigInteger                currentClassQQQ    = null;
      BigInteger                currentClassMSFT   = null;
      BigInteger                currentClassAAPL   = null;
      BigInteger                currentClassGOOG   = null;
      BigInteger                currentClassCSCO   = null;
      BigInteger                currentClassORCL   = null;
      BigInteger                nextClassQQQ       = null;
      BigInteger                nextClassMSFT      = null;
      BigInteger                nextClassAAPL      = null;
      BigInteger                nextClassGOOG      = null;
      BigInteger                nextClassCSCO      = null;
      BigInteger                nextClassORCL      = null;
      HashMap< String, Object > stockMap           = null;
      String                    nameQQQ            = "QQQ";
      String                    nameMSFT           = "MSFT";
      String                    nameAAPL           = "AAPL";
      String                    nameGOOG           = "GOOG";
      String                    nameCSCO           = "CSCO";
      String                    nameORCL           = "ORCL";
      String                    c                  = "class";
      String                    q                  = "quant";
      String                    n                  = "next-key";
      String                    s                  = "simulacra";

      for( String currentKey : theMap.keySet() )
      {
         HashMap< String, HashMap< String, Object > > firstKeyMap  = theMap.get( currentKey );
         HashMap< String, Object >                    secondKeyMap = firstKeyMap.get( currentKey );
         HashMap< String, Double >                    simulacra    = new HashMap< String, Double >();
         double                                       distance     = 0.0;
         double                                       tempdist     = 0.0;
         double                                       smallest     = Double.MAX_VALUE;

         if( secondKeyMap == null )
         {
            continue;
         }
         else
         {
//             System.out.println( "##########################" );
            secondKeyMap.put( s, simulacra );
         }

         for( String nextKey : theMap.keySet() )
         {
            if( currentKey.equals( nextKey ) == false )
            {
               HashMap< String, HashMap< String, Object > > keyMap     = theMap.get( currentKey );
               HashMap< String, HashMap< String, Object > > nextKeyMap = theMap.get( nextKey );

               stockMap  = keyMap.get( nameQQQ );
               currentClassQQQ  = (BigInteger)  stockMap.get( c );
               stockMap  = keyMap.get( nameMSFT );
               currentClassMSFT  = (BigInteger) stockMap.get( c );
               stockMap  = keyMap.get( nameAAPL );
               currentClassAAPL  = (BigInteger) stockMap.get( c );
               stockMap  = keyMap.get( nameGOOG );
               currentClassGOOG  = (BigInteger) stockMap.get( c );
               stockMap  = keyMap.get( nameCSCO );
               currentClassCSCO  = (BigInteger) stockMap.get( c );
               stockMap  = keyMap.get( nameORCL );
               currentClassORCL  = (BigInteger) stockMap.get( c );

               stockMap  = nextKeyMap.get( nameQQQ );
               nextClassQQQ  = (BigInteger)  stockMap.get( c );
               stockMap  = nextKeyMap.get( nameMSFT );
               nextClassMSFT  = (BigInteger) stockMap.get( c );
               stockMap  = nextKeyMap.get( nameAAPL );
               nextClassAAPL  = (BigInteger) stockMap.get( c );
               stockMap  = nextKeyMap.get( nameGOOG );
               nextClassGOOG  = (BigInteger) stockMap.get( c );
               stockMap  = nextKeyMap.get( nameCSCO );
               nextClassCSCO  = (BigInteger) stockMap.get( c );
               stockMap  = nextKeyMap.get( nameORCL );
               nextClassORCL  = (BigInteger) stockMap.get( c );

               tempdist = currentClassQQQ.xor( nextClassQQQ ).doubleValue();
               if( tempdist > quartiles[ 0 ] )
               {
                  continue;
               }
               distance += tempdist;
               tempdist = currentClassMSFT.xor( nextClassMSFT ).doubleValue();
               if( tempdist > quartiles[ 1 ] )
               {
                  continue;
               }
               distance += tempdist;
               tempdist = currentClassAAPL.xor( nextClassAAPL ).doubleValue();
               if( tempdist > quartiles[ 2 ] )
               {
                  continue;
               }
               distance += tempdist;
               tempdist = currentClassGOOG.xor( nextClassGOOG ).doubleValue();
               if( tempdist > quartiles[ 3 ] )
               {
                  continue;
               }
               distance += tempdist;
               tempdist = currentClassCSCO.xor( nextClassCSCO ).doubleValue();
               if( tempdist > quartiles[ 4 ] )
               {
                  continue;
               }
               distance += tempdist;
               tempdist = currentClassORCL.xor( nextClassORCL ).doubleValue();
               if( tempdist > quartiles[ 5 ] )
               {
                  continue;
               }
               distance += tempdist;
               if( distance < smallest )
               {
                  smallest = distance;
               }
               
               // If we've gotten this far, then this key is similar to the current.
               simulacra.put( nextKey, distance );
               simulacra.put( currentKey, smallest );
            }
         }

         // What happens if we have only one value?
         Interval            inter     = new Interval();
         ArrayList< Double > distances = new ArrayList< Double >();
         double              sum       = 0;
         for( String key : simulacra.keySet() )
         {
            distances.add( simulacra.get( key ) );
         }
         inter.mapData( distances );
         for( String key : simulacra.keySet() )
         {
            double v = simulacra.get( key );
            tempdist = inter.map( v );
            v        = 1 - tempdist;
            sum     += v;
            simulacra.put( key, v );
         }
         // normalize the values:
         for( String key : simulacra.keySet() )
         {
            double v = simulacra.get( key );
            v       /= sum;
//             System.out.println( key + " setting roulette weight value: " + v );
            simulacra.put( key, v );
         }
         // handle the case in which this is an outlier with no simulacra:
         if( simulacra.keySet().size() == 0 )
         {
            simulacra.put( currentKey, 1.0 );
         }
      }
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void performMCCPSimulation()
   {
      ArrayList< Double > values = new ArrayList< Double >();
      for( int i=0; i<1; i++ )
//       for( int i=0; i<150; i++ )
//       for( int i=0; i<50; i++ )
      {
         values.add( mapQuantiles() );
      }
      StatUtilities su = new StatUtilities();
      su.calculateStats( values );
      System.out.println( "median: " + su.getMedian() + " mean: " + su.getMean() );
   }

   /**
    *
    */
   private void findNearest( double[][][] matrix )
   {
      double d = 0.0;
      double m = 0.0;
      int    z = -1;
      TreeMap< Double, Integer > sorted = new TreeMap< Double, Integer >();
//       System.out.println( "matrix: " + matrix[ 0 ].length + " matrix[ i ].length: " + matrix[ 0 ][ 0 ].length );

      for( int s=matrix.length - 1; s>0; s-- )
      {
         m = Double.MAX_VALUE;
         z = -1;
         sorted = new TreeMap< Double, Integer >();

         for( int t=0; t<s; t++ )
         {
            d = 0.0;

            for( int u=0; u<matrix[ s ].length; u++ )
            {
               for( int w=0; w<matrix[ s ].length; w++ )
               {
//                   System.out.println( "s: " + s + " t: " + t + " u: " + u + " w: " + w );
                  d += Math.abs( matrix[ s ][ u ][ w ] - matrix[ t ][ u ][ w ] );
               }
            }
//             System.out.println( "d: " + d + " m: " + m );
            if( d < m )
            {
               m = d;
               z = t;
            }
            sorted.put( new Double( d ), new Integer( t ) );
         }

         Set< Double > keys = sorted.keySet();
         for( Double key : keys )
         {
            System.out.println( "Day: " + s + " dist: " + key + " day: " + sorted.get( key ) );
         }
      }
   }

   /**
    *
    */
   private double[][] randomIterates( double[][] matrix )
   {
      MathUtilities util = new MathUtilities();
      double[][]    matr = new double[ matrix.length ][ matrix.length ];
      double[][]    temp = null;
      double        s    = 0.0;

      for( int i=0; i<matrix.length; i++ )
      {
         for( int j=0; j<matrix.length; j++ )
         {
            matr[ i ][ j ] = matrix[ i ][ j ];
         }
      }

      for( int k=0; k<50; k++ )
      {
         temp = util.multiply( matrix, matr );
         for( int i=0; i<matrix.length; i++ )
         {
            for( int j=0; j<matrix.length; j++ )
            {
               matr[ i ][ j ] = temp[ i ][ j ];
            }
         }
      }

//       System.out.println( "matr" );
      for( int i=0; i<matrix.length; i++ )
      {
//          System.out.println( "" );
         for( int j=0; j<matrix.length; j++ )
         {
            matr[ i ][ j ] = matrix[ i ][ j ];
//             System.out.print( matr[ i ][ j ] + "," );
         }
      }

      return matr;
   }

   /**
    *
    */
   private StatUtilities getStatUtility( String name, int pos )
   {
      StockElement stock = _stockMap.get( name );
      int          c     = 20;
      int          z     = 0;

      if( stock == null )
      {
         stock = getStockElement( name );
         _stockMap.put( name, stock );
      }

      ArrayList< Double > stockCloses = stock.getAllClosesP();
//       ArrayList< Double > stockCloses = stock.getAllCloses();
      double[]            closes      = new double[ c ];

      for( int j=( stockCloses.size() - pos - c ); j<( stockCloses.size() - pos ); j++ )
      {
         closes[ z++ ] = stockCloses.get( j );
//          if( name.equals( "AMAT" ) )
//          {
//             System.out.println( name + "," + pos + "," + closes[ z - 1 ] );
//          }
      }

      double[]      closes2 = _mathUtil.normalize( closes, 1 );
      StatUtilities stat    = new StatUtilities( closes2 );

      return stat;
   }

   /**
    *
    */
   public void calculateCorrelation()
   {
      int                 days  = 1;
      ArrayList< String > names = _theStockCollection.getNasdaq100StockNames();
      StockElement        qqq   = getStockElement( "QQQ" );
      ArrayList< String > dates = qqq.getDates();
      double[][]          tri   = new double[ names.size() ][ names.size() ];
      double[][][]        mmm   = new double[ days + 1 ][ tri.length ][ tri.length ];

      for( int k=0; k<names.size(); k++ )
      {
         tri[ k ][ k ] = 1.0;
      }

      for( int k=days; k>0; k-- )
      {
         ArrayList< Double > d = new ArrayList< Double >();

         for( int i=0; i<names.size(); i++ )
         {
//             System.out.println( "name: " + names.get( i ) );
            StatUtilities s1 = getStatUtility( names.get( i ), k );

            for( int j=0; j<i; j++ )
            {
               StatUtilities s2 = getStatUtility( names.get( j ), k );
               double        cc = s1.calculateCorrelation( s2 );
//                if( cc < -0.99 )
//                {
                  System.out.println( k + "," + names.get( i ) + "," + names.get( j ) + "," + cc );
//                }
               d.add( cc );
               tri[ j ][ i ] = cc;
            }
         }

//          double[][] m = randomIterates( tri );
// 
//          for( int r=0; r<m.length; r++ )
//          {
//             for( int s=0; s<m.length; s++ )
//             {
//                mmm[ k ][ r ][ s ] = m[ r ][ s ];
//             }
//          }

         StatUtilities s = new StatUtilities( d );
         System.out.println( dates.get( dates.size() - k ) + "," + s.getMean() + "," + s.getDeviation() );
      }

//       findNearest( mmm );
   }

   /**
    *
    */
   public void findNearestQQQRecent()
   {
      int                       days  = 255;
      ArrayList< String >       names = _theStockCollection.getNasdaq100StockNames();
      StockElement              qqq   = getStockElement( "QQQ" );
      ArrayList< Double >       q     = _mathUtil.normalize( qqq.getAllClosesP(), 1 );
      double                    d     = 0.0;
      TreeMap< Double, String > m     = new TreeMap< Double, String >();

      for( int i=0; i<names.size(); i++ )
      {
         StockElement        s = getStockElement( names.get( i ) );
         ArrayList< Double > c = _mathUtil.normalize( s.getAllClosesP(), 1 );
                             d = 0.0;

         for( int j=1; j<days; j++ )
         {
            d += Math.abs( c.get( c.size() - j ) - q.get( q.size() - j ) );
         }

         m.put( new Double( d ), s.getName() + "," + s.getLastClose() + "," + (qqq.getLastClose() / s.getLastClose() ) );
      }

      for( Double key : m.keySet() )
      {
         String v = m.get( key );
         System.out.println( v + "," + key );
      }
   }

   /**
    *
    */
   public void outputDataForMTOE()
   {
      ArrayList< String >       names = _theStockCollection.getNasdaq100StockNames();
      StockElement              qqq   = getStockElement( "QQQ" );
//       ArrayList< Double >       q     = _mathUtil.normalize( qqq.getAllClosesP(), 1 );
      ArrayList< Double >       q     = qqq.getAllClosesP();
      int                       n     = 0;
      int                       d     = 255;
      Double                    v;

      System.out.println( "Target:" );
      for( int j=d; j>0; j-- )
      {
         System.out.println( "" + q.get( q.size() - j ) );
      }

      System.out.println( "Variables:" );
      for( int i=0; i<names.size(); i++ )
      {
         StockElement        s = getStockElement( names.get( i ) );
//          ArrayList< Double > c = _mathUtil.normalize( s.getAllClosesP(), 1 );
         ArrayList< Double > c =  s.getAllClosesP();

         if( c.size() > d )
         {
            n++;
            for( int j=d; j>0; j-- )
            {
               v = c.get( c.size() - j );
               System.out.println( "" + v );
            }
         }
      }
      System.out.println( "Number of vars: " + n );
   }

} // end StockManager