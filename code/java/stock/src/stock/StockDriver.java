package stock;

import common.*;
import math.*;
import java.util.*;
import java.io.*;

/**
  The output file's structure is:
<html>

	<head>
		<title>
		The Results
		</title>

	<link rel=StyleSheet href="Declarations.css" type="text/css">

	</head>

<body>

	<body bgcolor=black>

      <img src="graybar1.jpg" width=95% height=9px>

      <center>
      <table width=80% cellspacing=0 cellpadding=10 border=0>

      <tr>
      <td bgcolor=#222222 valign=center>
      <div class=bodyWhiteSmallJustify>
         Text
      </div>
      </td>

	<td bgcolor=#222222 valign=top>
      <div class=bodyWhiteSmallJustify>
         Text
      </div>
      </td>
      </tr>

      </table>
      </center>


</body>
</html>
*/

public class StockDriver
{
   StockManager _theManager        = new StockManager();
   FileWriter   _theFile           = null;
   String       _fileName          = null;
   String       _stockName         = null;
   int          _location          = 0;
   int          _length            = 0;
   int          _numPredictions    = 0;
   int          _intervalsBack     = 1;
   ArrayList< String > _intervalsBack     = new ArrayList< String >();
   
   /**
    *
    */
   public StockDriver()
   {
      Calendar theCal = new GregorianCalendar();
      _fileName = new String( "Results_" + (theCal.get( Calendar.MONTH ) + 1) );
      _fileName = _fileName + "_" + theCal.get( Calendar.DAY_OF_MONTH );
      _fileName = _fileName + "_" + theCal.get( Calendar.YEAR );
      _fileName = _fileName + ".html";
   } // end default constructor

   /**
    *
    */
   public StockDriver( String stockName, String pos, String length )
   {
      try
      {
         _stockName = stockName;
         _location = Integer.parseInt( pos );
         _length = Integer.parseInt( length );
      }
      catch( NumberFormatException e )
      {
         System.out.println( "Invalid number specified: " + e );
      }
   } // end default constructor

   /**
    *
    */
   public StockDriver( String month, String day, String year, boolean regular )
   {
      if( regular == true )
      {
         _fileName = new String( "UResults_" + month + 
                                 "_" + day + "_" + year + ".html" );
      }
      else
      {
         _fileName = new String( "Results_" + month + 
                                 "_" + day + "_" + year + ".html" );
      } // end else
   } // end default constructor

   /**
    *
    */
   private Object[] getActiveArray()
   {
      //Collection theColl    = _theManager.getActiveCollection();
      Collection   theColl    = _theManager.getSortedConfidenceCollection();
      Iterator     theIter    = theColl.iterator();
      StockElement theElement = null;
      ArrayList theList = new ArrayList();
       
      while( theIter.hasNext() )
      {
         theElement = (StockElement)theIter.next();
         theList.add( theElement );
      } // end while
      return theList.toArray();
   } // end getActiveArray

   /**
    *
    */
   public void runQQQPositions()
   {
      StockCollection theColl = new StockCollection();
      ArrayList<String> theNames = theColl.getNasdaq100StockNames();
      StockElement theElem = null;
      _length = 130;
      int daysBack = 2000;
      int numberPredictions = (int)((double)daysBack / (double)_length);

      for( int i=numberPredictions; i>0; i-- )
      {
         for( String name : theNames )
         {
            System.out.println( "A QQQ stock is: " + name );
            theElem = _theManager.getStockElement( name );
            System.out.println( "The QQQ elem size is: " + theElem.getSize() );
            _location = theElem.getSize() - (numberPredictions * _length);
            if( _location < 0 )
            {
               continue;
            }
         }
      }
   }

   /**
    * We assume that the prediction length is as it has been
    * which is 40 days.
    *
    */
   public void runNasdaq100QQQ()
   {
      StockCollection theColl = new StockCollection();
      ArrayList<String> theNames = theColl.getNasdaq100StockNames();
      StockElement theElem = null;
      _length = 130;
      HashMap map = new HashMap();
      map.put( new Integer( 5 ), new Double ( 0 ) );
      map.put( new Integer( 10 ), new Double ( 0 ) );
      map.put( new Integer( 20 ), new Double ( 0 ) );

      for( String name : theNames )
      {
         _stockName = name;
         System.out.println( "A QQQ stock is: " + _stockName );
         theElem = _theManager.getStockElement( _stockName );
         System.out.println( "The QQQ elem size is: " + theElem.getSize() );
         _location = theElem.getSize() - 1400;
//         _location = theElem.getIndexOfDate( "2002-01-02" );
         if( _location < 0 )
         {
            System.out.println( "****************Date not found****************" );
//            _location = (int)((double)theElem.getSize() / (double)2);
            _location = 0;
         }
         System.out.println( "The QQQ elem location is: " + _location );

         _numPredictions = (int)((double)(theElem.getSize() - _location - _length - 1) / (double)(_length));
         System.out.println( "_numPredictions: " + _numPredictions );
         for( int i=0; i<_numPredictions; i++ )
         {
            if( _location >= 0 )
            {
               _location += _length;
               System.out.println( "Now predicting for location: " + _location );
               _theManager.analyzeAgainstQQQ( _stockName, _location, _length, map );
            }
         }
      }

      System.out.println( ",,data for 5:," + 
                          map.get( new Integer( 5 ) ) + ",data for 10:," +  
                          map.get( new Integer( 10 ) ) + ",data for 20:," +
                          map.get( new Integer( 20 ) ) );

   }

   /**
    *
    */
   public void runNasdaq100()
   {
      StockCollection theColl = new StockCollection();
      ArrayList<String> theNames = theColl.getNasdaq100StockNames();
      StockElement theElem = null;

      ArrayList<Double> movingAverages = null;
      ArrayList<Double> closes = null;
      ArrayList<Double> differencesSum = null;

      Random randomGenerator = new Random();
      double threshold = 20;

      // this is the SMA window size:
      _length = 130;

      for( String name : theNames )
      {
         _stockName = name;
         System.out.println( "A QQQ stock is: " + _stockName );
         theElem = _theManager.getStockElement( _stockName );
         theElem.calculateMovingAverages( _length );
         theElem.calculateMADifferenceSum();
         movingAverages = theElem.getMovingAverages();
         closes = theElem.getAllCloses();
         differencesSum = theElem.getMADifferenceSum();
 
         double close = 0;
         double previousClose = 0;
         double movingAverage = 0;
         double previousMovingAverage = 0;

         double buySum = 0;
         double buyPoint = -1D;
         int buyPosition = -1;

         for( int i=0; i<movingAverages.size(); i++ )
         {
            if( i < _length )
            {
//               System.out.println( closes.get( i + _length ) + "," + movingAverages.get( i ) );
            }
            else
            {
//               System.out.println( closes.get( i + _length ) + "," + 
//                  movingAverages.get( i ) + "," + differencesSum.get( i - _length ) );
               close = closes.get( i + _length );
               movingAverage = closes.get( i );
            }

            if( buyPoint < 0 )
            {
               if( (close > movingAverage) && (previousClose < previousMovingAverage) )
               {
                  if( randomGenerator.nextInt( 100 ) < threshold )
                  {
                     buyPoint = close;
                     buyPosition = ( i + _length );
                     buySum = differencesSum.get( i - _length );
                  }
               }
            }
            else if( buyPoint > 0 )
            {
               if( (close < movingAverage) && (previousClose > previousMovingAverage) )
               {
                  System.out.println( ((close - buyPoint) / close) + "," + 
                     buyPosition + "," + (i + _length) + "," + buySum );
                  buyPoint = -1D;
               }
            }

            previousClose = close;
            previousMovingAverage = movingAverage;
         }
      }
   }

   /**
    *
    *
    */
   public void currentMinorTheory( ArrayList<String> theNames )
   {
      System.out.println( "Performing current minor theory" );

      double lowest = 1000000;
      String lowestName = null;
      StockElement theElem = null;
      StockElement lowestElement = null;

      // Simply cycle through all the securities and see what is the lowest
      for( String name : theNames )
      {
         System.out.println( "Checking: " + name );
         theElem = _theManager.getStockElement( name );
         if( theElem.getSize() > 0 && theElem.getClose( theElem.getSize() - 1 ) < lowest )
         {
            lowest = theElem.getClose( theElem.getSize() - 1 );
            lowestName = name;
            lowestElement = theElem;
            System.out.println( "Found a new lowest: " + name );
         }
      }
      System.out.println( "The lowest is: " + lowestName );
      System.out.println( "The lowest close is: " + lowestElement.getClose( lowestElement.getSize() - 1 ) );
   }

   /**
    *
    *
    */
   public void minorTheory()
   {
      StockCollection theColl = new StockCollection();
//      ArrayList<String> theNames = theColl.getNasdaq100StockNames();
//      ArrayList<String> theNames = theColl.getLiquidStockNames();
//      ArrayList<String> theNames = theColl.getStockNames();
//      ArrayList<String> theNames = theColl.getDJIAStockNames();
//      ArrayList<String> theNames = theColl.getRussell1000StockNames();
//      ArrayList<String> theNames = theColl.getRussell2000StockNames();
//      ArrayList<String> theNames = theColl.getRussell3000StockNames();
//      ArrayList<String> theNames = theColl.getSP500StockNames();
      ArrayList<String> theNames = theColl.getSP1500StockNames();
//      ArrayList<StockElement> theElements = new ArrayList<StockElement>();

      // Cache up the stock elements -- can't really do this for big indexes.
/*
      for( String name : theNames )
      {
         theElements.add( _theManager.getStockElement( name ) );
      }
*/

      int holdLength = 50;
      int intervalEnd = 100;
      int maxHoldLength = 300;
      int holdLengthAdj = 50;
      int origHoldLength = holdLength;
      double totalValue = 0;
      StockElement IBM = _theManager.getStockElement( "IBM" );
      ArrayList<String> theDates = IBM.getDates();

//      for( ; holdLength>holdLengthAdj; holdLength-=holdLengthAdj )
//      { 
         _length = 272;
//         _length = 272 + 265;
//         _length = 272 + 265 + 265;
//         _length = 272 + 265 + 265 + 265;
//         _length = 272 + 265 + 265 + 265 + 265;
         intervalEnd = _length - 265;
         origHoldLength = holdLength;
//         for( ; _length>origHoldLength; _length-=100 )
//         {
            int buyPos = -1;
            int buyNumber = 1;
            StockElement buyElem = null;
            StockElement theElem = null;
            double cash = 1000;
            double entry = 0;
            int count = 0;
            String today = null;
            HashMap<String, Double> closeMap = null;
            ArrayList<MiniElement> buys = null;

            for( int i=(theDates.size() - _length); i<(theDates.size() - intervalEnd); i++ )
            {
               today = theDates.get( i );
               double lowest = 10000;
               TreeMap<Double, MiniElement> sorted = new TreeMap<Double, MiniElement>();

               // look for a buy:
               if( buyPos < 0 )
               {
//                  for( StockElement theElem : theElements )
                  for( String name : theNames )
                  {
//                     System.out.println( "Checking: " + name );
                     theElem = _theManager.getStockElement( name );
                     if( theElem == null || theElem.getName() == null )
                     {
                        continue;
                     }
                     closeMap = theElem.getCloseHash();
                     Double theClose = closeMap.get( today );
                     if( theClose == null )
                     continue;
                     sorted.put( theClose, new MiniElement( theElem.getName(), theClose ) );
                  }
                  int j = 0;
                  buys = new ArrayList<MiniElement>();
                  for( Iterator iter = sorted.values().iterator(); iter.hasNext(); )
                  {
                     MiniElement ele = (MiniElement)iter.next();
                     if( j++ < buyNumber )
                     {
                        buys.add( ele );
                        System.out.println( "buying element: " + ele.getName() + " close: " + ele.getEntry() );
                     }
                  }
                  buyPos = i;
//                  System.out.println( "Buying: " + buyElem.getName() + " at position: " + 
//                    buyPos + " value: " + entry );
               }
               else if( buyPos > 0 )
               {
                  if( (i - buyPos) >= holdLength )
                  {
                     today = theDates.get( i );
                     double value = 0;
                     int previousbuyPos = buyPos;
                     buyPos = -1;
                     for( int pos=0; pos<buys.size(); pos++ )
                     {
                        MiniElement buy = buys.get( pos );
                        if( buy == null ) 
                        {
System.out.println( "the buy item is null!!! for: " + today );
                           continue;
                        }
                        buyElem = _theManager.getStockElement( buy.getName() );
                        if( buyElem == null ) 
                        {
System.out.println( "the buy elem is null!!! for: " + buy.getName() );
                           continue;
                        }
                        closeMap = buyElem.getCloseHash();
                        Double theClose = closeMap.get( today );
//                     System.out.println( "Attempting Sell: " + buyElem.getName() + 
//                         " at position: " + i + " value: " + theClose );
                        if( theClose == null )
                        {
System.out.println( "No data for exit on: " + buyElem.getName() + " canceling buy" );
                           buys.remove( pos-- );
                           continue;
                        }
                        value = buyElem.performBuy( cash, buy.getEntry(), theClose );
                        if( value < 0 )
                        {
//                           System.out.println( "Found a negative value, reseting" );
                           buyPos = previousbuyPos;
                           continue;
                        }
                        System.out.println( "Trading:," + buyElem.getName() + ",at:," + 
                          entry + ",and at:," + 
                          theClose + ",for value:," + value +
                          ",the day is:," + i );
                        totalValue += value;
//                        cash += value;
                        buys.remove( pos-- );
                     }
                     buyElem = null;
                     holdLength = origHoldLength;
                  }
               }
            }
            System.out.println( "The total value is:," + totalValue );
            System.out.println( "The cash value is:," + cash );
//         }
//      }
   }

   /**
    *
    * It is optimal to buy on the day that the trend is observed.
    *
    *
    */
   public void simulateDecoherence()
   {
      StockCollection theColl = new StockCollection();
      ArrayList<String> theNames = theColl.getNasdaq100StockNames();
      StockElement theElem = null;
      StockElement theQElem = _theManager.getStockElement( "QQQQ" );
      theQElem.calculatePercentages();

      ArrayList<Double> closesPQ = theQElem.getAllClosesP();
      ArrayList<Double> closesP = null;
      HashMap<String, ArrayList> buys = new HashMap<String, ArrayList>();

      _length = 1800;
      double cash = 10000;
      double cashThreshold = 1000;
      double cashLimit = 10000;
      int diffThreshold = 6;
      int holdLength = 100;

      // first, we cycle through all the days
//      for( int i=_length; i>0; i-- )
      for( int i=_length; i>901; i-- )
      {
         System.out.println( "############################## i is: " + i );
         HashMap<String, Integer> potentialBuys = new HashMap<String, Integer>();

         // inefficient, but easy to code
         for( String name : theNames )
         {
            _stockName = name;
            theElem = _theManager.getStockElement( _stockName );
            theElem.calculatePercentages();
            closesP = theElem.getAllClosesP();

            // make sure it's not too small
            if( (closesP.size() - i) <= 0 || (closesPQ.size() - i) > 0 )
            {
               continue;
            }

            // check for buy signal
            if(    (closesPQ.get( closesPQ.size() - i )     < 1 && closesP.get( closesP.size() - i )     > 1) 
                && (closesPQ.get( closesPQ.size() - i - 1 ) < 1 && closesP.get( closesP.size() - i - 1 ) > 1) 
                && (closesPQ.get( closesPQ.size() - i - 2 ) < 1 && closesP.get( closesP.size() - i - 2 ) > 1) 
            )
            {
               System.out.println( "a potential buy: " + name + " pos: " + i );
               potentialBuys.put( name, (closesP.size() - i) );
            }

            // check for sell signal
            ArrayList<String> sells = new ArrayList<String>();
            Set<String> keys = buys.keySet();
            for( String stockName : keys )
            {
               theElem = _theManager.getStockElement( stockName );
               ArrayList temp = buys.get( stockName );
               double amount = (Double)temp.get( 0 );
               int start = (Integer)temp.get( 1 );
               int end = (theElem.getSize() - i);
               if( (end - start) >= holdLength )
               {
                  double value = theElem.performNormalBuy( amount, start, end );
                  System.out.println( "selling: " + stockName + " start: " + start + 
                                      " end " + end + " amount " + amount + " profit " + value );
                  cash += value;
                  sells.add( stockName );
                  System.out.println( "cash is now: " + cash );
               }
            }
            for( String sellName : sells )
            {
               buys.remove( sellName );
            }
         }

         // only attempt buys if we have more than our cash threshold
         if( cash >= cashThreshold )
         {
            Set<String> keys = potentialBuys.keySet();
            double available = (cash >= cashLimit) ? cashLimit : cash;
            for( String stockName : keys )
            {
               System.out.println( "checking potential: " + stockName );
               double amount = available / (double)potentialBuys.size();
               int diffUp = 0;
               int diffDown = 0;
               theElem = _theManager.getStockElement( stockName );
               theElem.calculatePercentages();
               closesP = theElem.getAllClosesP();

               for( int j=0; j<50; j++ )
               {
                  if( (closesPQ.get( closesPQ.size() - i - j ) < 1 && closesP.get( closesP.size() - i - j ) > 1) )
                  {
                     diffUp++;
                  }
                  if( (closesPQ.get( closesPQ.size() - i - j ) > 1 && closesP.get( closesP.size() - i - j ) < 1) )
                  {
                     diffDown++;
                  }
               }
               System.out.println( "potential: " + stockName + " diffUp " + diffUp + " diffDown " + diffDown );

               // perform buy if the threshold is met
               if( (diffUp - diffDown) >= diffThreshold )
               {
                  ArrayList temp = new ArrayList(); // 0 is double, 1 is int
                  temp.add( amount ); // amount
                  temp.add( potentialBuys.get( stockName ) ); // position
                  buys.put( stockName, temp );
                  cash -= amount;
                  System.out.println( "buying: " + stockName + " start: " + temp.get( 1 ) + " amount " + amount );
               }
            }

            keys = buys.keySet();
            for( String stockName : keys )
            {
               potentialBuys.remove( stockName );
            }
         }
      }
      System.out.println( "The total cash is now: " + cash );
   }

   /**
    *
    * It is optimal to buy on the day that the trend is observed.
    *
    *
    */
   public void runNasdaq100Trend()
   {
      StockCollection theColl = new StockCollection();
      ArrayList<String> theNames = theColl.getNasdaq100StockNames();
      StockElement theElem = null;
      StockElement theQElem = _theManager.getStockElement( "QQQQ" );
      theQElem.calculatePercentages();

      ArrayList<Double> closesPQ = theQElem.getAllClosesP();
      ArrayList<Double> closesP = null;
      HashMap<Integer, Integer> buys = new HashMap<Integer, Integer>();
      HashMap<Integer, Integer> sells = new HashMap<Integer, Integer>();

      _length = 1000;
      for( int i=0; i<_length; i++ )
      {
         buys.put( i, new Integer( 0 ) );
         sells.put( i, new Integer( 0 ) );
      }
/*
*/

      for( int holdLength=100; holdLength<101; holdLength++ )
      {
      for( int diffThresh=6; diffThresh<7; diffThresh++ )
      {
      double totalValue = 0;
      int totalCount = 0;
      int totalWinCount = 0;
      int totalLossCount = 0;

      for( String name : theNames )
      {
         _stockName = name;
//         System.out.println( "A QQQ stock is: " + _stockName );
         theElem = _theManager.getStockElement( _stockName );

         if( theElem.getSize() <= _length )
         {
            continue;
         }

         theElem.calculatePercentages();
         closesP = theElem.getAllClosesP();
         int buyPoint = -1;
         int bi = -1;
         double value = 0;

         for( int i=_length; i>0; i-- )
         {
            // buy signal
/* 5-day signal
            if(    buyPoint < 0 
                && (closesPQ.get( closesPQ.size() - i )     < 1 && closesP.get( closesP.size() - i )     > 1) 
                && (closesPQ.get( closesPQ.size() - i - 1 ) < 1 && closesP.get( closesP.size() - i - 1 ) > 1) 
                && (closesPQ.get( closesPQ.size() - i - 2 ) < 1 && closesP.get( closesP.size() - i - 2 ) > 1) 
                && (closesPQ.get( closesPQ.size() - i - 3 ) < 1 && closesP.get( closesP.size() - i - 3 ) > 1) 
                && (closesPQ.get( closesPQ.size() - i - 4 ) < 1 && closesP.get( closesP.size() - i - 4 ) > 1) 
*/
/* 4-day signal
            if(    buyPoint < 0 
                && (closesPQ.get( closesPQ.size() - i )     < 1 && closesP.get( closesP.size() - i )     > 1) 
                && (closesPQ.get( closesPQ.size() - i - 1 ) < 1 && closesP.get( closesP.size() - i - 1 ) > 1) 
                && (closesPQ.get( closesPQ.size() - i - 2 ) < 1 && closesP.get( closesP.size() - i - 2 ) > 1) 
                && (closesPQ.get( closesPQ.size() - i - 3 ) < 1 && closesP.get( closesP.size() - i - 3 ) > 1) 
*/
/* Initial 3-day signal -- worked the best
*/
            if(    buyPoint < 0 
                && (closesPQ.get( closesPQ.size() - i )     < 1 && closesP.get( closesP.size() - i )     > 1) 
                && (closesPQ.get( closesPQ.size() - i - 1 ) < 1 && closesP.get( closesP.size() - i - 1 ) > 1) 
                && (closesPQ.get( closesPQ.size() - i - 2 ) < 1 && closesP.get( closesP.size() - i - 2 ) > 1) 
/* 2-day sig
            if(    buyPoint < 0 
                && (closesPQ.get( closesPQ.size() - i )     < 1 && closesP.get( closesP.size() - i )     > 1) 
                && (closesPQ.get( closesPQ.size() - i - 1 ) < 1 && closesP.get( closesP.size() - i - 1 ) > 1) 
*/
            )
            {
               buyPoint = ( closesP.size() - i + 0 );
               bi = i;
               double diffSum = 0;
               int diffCount = 0;
               int downCount = 0;
               for( int j=0; j<50; j++ )
               {
                  diffSum += ( closesP.get( closesP.size() - i - j ) - closesPQ.get( closesPQ.size() - i - j ) );
                  if( (closesPQ.get( closesPQ.size() - i - j ) < 1 && closesP.get( closesP.size() - i - j ) > 1) )
                  {
                     diffCount++;
                  }
                  if( (closesPQ.get( closesPQ.size() - i - j ) > 1 && closesP.get( closesP.size() - i - j ) < 1) )
                  {
                     downCount++;
                  }
               }
               if( (diffCount - downCount) <= diffThresh )
               {
                  buyPoint = -1;
                  continue;
               }
//               System.out.println( _stockName + ",Buy point:," + i + "," + buyPoint + ", price:," 
//                  + theElem.getClose( buyPoint ) + ",sum:," + diffSum + ",count," + 
//                    diffCount + ",downCount," + downCount );
            
            }
            // sell signal
            else if(    buyPoint > 0 
                     && ( (closesP.size() - i) - buyPoint ) >= holdLength )
            {
               int sellPoint = ( closesP.size() - i );
               double buy = theElem.performBuy( 10000, buyPoint, sellPoint );
               value += buy;
               System.out.println( _stockName + ",buyPoint," + bi + ",Sell point:," + i + "," + buy );
//               System.out.println( _stockName + ",Sell point:," + i + "," +
//                  sellPoint + ", price:," + theElem.getClose( sellPoint ) + ",high percentage," + 
//                  (theElem.getClose( theElem.getHigh( buyPoint, sellPoint ) ) /
//                   theElem.getClose( buyPoint )) + ",sell percentage:," +
//                  (theElem.getClose( sellPoint ) / theElem.getClose( buyPoint ) ) );
               buyPoint = -1;
               totalCount++;
               if( buy > 0 )
               {
                  totalWinCount++;
               }
               else
               {
                  totalLossCount++;
               }
            }
            if(    (closesPQ.get( closesPQ.size() - i )     < 1 && closesP.get( closesP.size() - i )     > 1) 
                && (closesPQ.get( closesPQ.size() - i - 1 ) < 1 && closesP.get( closesP.size() - i - 1 ) > 1) 
                && (closesPQ.get( closesPQ.size() - i - 2 ) < 1 && closesP.get( closesP.size() - i - 2 ) > 1) 
            )
            {
               buys.put( ( i - 1 ), buys.get( ( i - 1 ) ) + 1 );
            } else
            if(    (closesPQ.get( closesPQ.size() - i )     > 1 && closesP.get( closesP.size() - i )     < 1) 
                && (closesPQ.get( closesPQ.size() - i - 1 ) > 1 && closesP.get( closesP.size() - i - 1 ) < 1) 
                && (closesPQ.get( closesPQ.size() - i - 2 ) > 1 && closesP.get( closesP.size() - i - 2 ) < 1) 
            )
            {
               sells.put( ( i - 1 ), sells.get( ( i - 1 ) ) + 1 );
            }
         }
//         System.out.println( _stockName + ",Stock value:," + value + ",downCount," + downCount );
         totalValue += value;
      }

      System.out.println( "\nHold, Total Count, Diff Thresh, Win Percent, Loss Percent, " +
                          "Average Value, Average Value with Hold" );
      System.out.println( holdLength + "," + totalCount + "," + diffThresh + "," +
                          ((double)totalWinCount / (double)totalCount) + "," +
                          ((double)totalLossCount / (double)totalCount) + "," +
                          (totalValue / totalCount) + "," +
                          (totalValue / totalCount / (double)(holdLength)) );

      System.out.println( "\nBuy points: " );
      for( int i=0; i<_length; i++ )
      {
         System.out.println( i + "," + buys.get( i ) );
      }
      System.out.println( "\nSell points: " );
      for( int i=0; i<_length; i++ )
      {
         System.out.println( i + "," + sells.get( i ) );
      }
/*
*/
      }
      }
   }

   /**
    * Currently, this will run a prediction from the location given
    * for numPredictions each with a length specified.
    * Each prediction is spaced by 5 days.
    *
    */
   public void outputHTML()
   {
      for( int i=0; i<_numPredictions; i++ )
      {
         System.out.println( "Now predicting for location: " + (_location + (i * 5)) );
         _theManager.analyze( _stockName, (_location + (i * 5)), _length );
      }

/** Add this code back to output the HTML file **
      try
      {
         _theFile = new FileWriter( _fileName );
         outputHeader();
         for( int i=theArray.length-1; i>-1; i-- )
         //while( theIter.hasNext() )
         {
            theElement = (StockElement)theArray[i];
            //theElement = (StockElement)theIter.next();
            
            if( (++counter % 2) == 0 )
               _theFile.write( "<tr> <td bgcolor=#222222 valign=center>" );
            else
               _theFile.write( "<tr> <td bgcolor=#333333 valign=center>" );
            _theFile.write( "<div class=bodyWhiteSmallJustify>" );
            _theFile.write( "Stock Name:<br>" );
            _theFile.write( "SMAConfidence:<br>" );
            _theFile.write( "Best Conseq:<br>" );
            _theFile.write( "Best Depth:<br>" );
            _theFile.write( "Threshold:<br>" );
            _theFile.write( "Confidence:<br>" );
            //_theFile.write( "Variance SMA:<br>" );
            _theFile.write( "Density:<br>" );
            _theFile.write( "Worth:<br>" );
            _theFile.write( "Real Worth:<br>" );
            _theFile.write( "SMA Worth:<br>" );
            _theFile.write( "TL Ratio:<br>" );
            _theFile.write( "High / Last Buy / Total Buys:<br>" );
            _theFile.write( "Days Increasing:<br>" );
            _theFile.write( "Fast EMA:<br>" );
            _theFile.write( "Slow EMA:<br>" );
            _theFile.write( "Signal EMA:<br>" );
            _theFile.write( "MACD Mass:<br>" );
            _theFile.write( "Stochastic days:<br>" );
            _theFile.write( "Stochastic EMA:<br>" );
            _theFile.write( "Fast Stochastic:<br>" );
            _theFile.write( "Current Stochastic:<br>" );
            _theFile.write( "Previous Stochastic:<br>" );
            //_theFile.write( "Volume Scale:<br>" );
            //_theFile.write( "Final Worth:<br>" );
            _theFile.write( " </div> </td> " );

            if( (counter % 2) == 0 )
               _theFile.write( "<td bgcolor=#222222 valign=center>" );
            else
               _theFile.write( "<td bgcolor=#333333 valign=center>" );
            _theFile.write( "<div class=bodyWhiteSmallJustify>" );
            _theFile.write( theElement.getName() + "<br>" );
            _theFile.write( theElement.getSMAvalue() + "<br>" );
            _theFile.write( theElement.getBestConseq() + "<br>" );
            _theFile.write( theElement.getBestDepth() + "<br>" );
            _theFile.write( theElement.getThreshold() + "<br>" );
            _theFile.write( "<b>" + theElement.getConfidence() + "</b><br>" );
            //_theFile.write( theElement.getLastVarianceSMA() + "<br>" );
            _theFile.write( theElement.getDensity() + "<br>" );
            _theFile.write( theElement.getWorth() + "<br>" );
            _theFile.write( theElement.getRealWorth() + "<br>" );
            _theFile.write( theElement.getStabilityMass() + "<br>" );
            _theFile.write( theElement.getTotalLoss() + "<br>" );
            _theFile.write( theElement.getHighBuy() + " / " + 
                            theElement.getLastBuy() + " / " +
                            theElement.getTotalBuys() + "<br>" );
            _theFile.write( theElement.getDaysAbove() + "<br>" );
            _theFile.write( theElement.getFastEMA() + "<br>" );
            _theFile.write( theElement.getSlowEMA() + "<br>" );
            _theFile.write( theElement.getSignalEMA() + "<br>" );
            _theFile.write( theElement.getMACDMass() + "<br>" );
            _theFile.write( theElement.getStochasticDays() + "<br>" );
            _theFile.write( theElement.getStochasticEMA() + "<br>" );
            _theFile.write( theElement.getFastStochastic() + "<br>" );
            _theFile.write( theElement.getCurrStochastic() + "<br>" );
            _theFile.write( theElement.getPrevStochastic() + "<br>" );
            //_theFile.write( theElement.getVolumeWorth() + "<br>" );
            //_theFile.write( theElement.getVolumeScale() + "<br>" );
            //_theFile.write( theElement.getFinalWorth() + "<br>" );
            _theFile.write( " </div> </td> </tr>" );
         } // end while loop
         outputTrailer();
      }
      catch( IOException ioe )
      {
         System.out.println( "Caught an exception while writing file: " + ioe );
         ioe.printStackTrace();
      } // end catch
      finally
      {
         try
         {
            _theFile.flush();
            _theFile.close();
         } catch( IOException ioe ) { }
      } // end finally
*/
      
   } // end outputHTML

   /**
    *
    */
   public void outputHeader() throws IOException
   {
      _theFile.write( " <html> <head> <title> The Results </title> " );
      _theFile.write( "<link rel=StyleSheet href=\"Declarations.css\" " );
      _theFile.write( "type=\"text/css\"> </head> <body> <body bgcolor=black> " );
      _theFile.write( "<center><img src=\"graybar1.jpg\" width=80% " );
      _theFile.write( " height=3px> </center> <center> " );
      _theFile.write( "<table width=80% cellspacing=0 cellpadding=10 border=0>" );

      _theFile.write( "<tr> <td bgcolor=#000000 valign=center>" );
      _theFile.write( "<div class=bodyGreenSmallJustify>" );
      _theFile.write( "Total Worth:<br>" );
      _theFile.write( " </div> </td> " );
      _theFile.write( "<td bgcolor=#000000 valign=center>" );
      _theFile.write( "<div class=bodyGreenSmallJustify>" );
      _theFile.write( _theManager.getTotalWorth() + "<br>" );
      _theFile.write( " </div> </td> </tr>" );
   } // end outputHeader

   /**
    *
    */
   public void outputTrailer() throws IOException
   {
      _theFile.write( " </table> </center>" );
      _theFile.write( "<center><img src=\"graybar1.jpg\" width=80% " );
      _theFile.write( " height=3px> </center> " );
      _theFile.write( " </body> </html>" );
   } // end outputTrailer

   /**
    *
    */
   private void handleDividends()
   {
      _theManager = new StockManager( LoadEnum.LOAD_ALL );
      _theManager.calculateDividends();
   }

   /**
    * 
    */
   private void handleSingle( String[] args )
   {
      String name = args[0];
      int location = Integer.parseInt( args[1] );
      int length = Integer.parseInt( args[2] );
  
      try
      {
         _theManager.analyze( args[0], Integer.parseInt( args[1] ), Integer.parseInt( args[2] ) );
      }
      catch( NumberFormatException e )
      {
         System.out.println( "Exception: " + e );
      }
   }

   /**
    * Performs the "modified" linear method.
    */
   private void handleLinear()
   {
      StockCollection theColl = new StockCollection();
      ArrayList<String> theNames = theColl.getNasdaq100StockNames();
      StockElement theElem = null;
      //_length = 130;                 // in days, this is 6 months of trading
      _length = 40;                 // in days, this is 2 months of trading
      int totalLength = _intervalsBack * _length; // this will go back this many half years.
      double error = 0;
      double slope = 0;
      double smallest = 3000;
      HashMap theSlopes = new HashMap<String, Double>();

      System.out.println( "Name,Location,Slope,Error" );

      for( String name : theNames )
      {
         _stockName = name;
         theElem = _theManager.getStockElement( _stockName );
         _location = theElem.getSize() - totalLength;
         theElem.performLinearModel( _location, (_location + _length) );
         slope = theElem.getThreshold();
         error = theElem.getSMAvalue();
         if( slope > 0 && ( slope < smallest ) )
         {
            smallest = slope;
         }
         System.out.println( _stockName + "," + _location + "," + slope + "," + error );
         theSlopes.put( _stockName, slope );
      }

      System.out.println( "The smallest was: " + smallest );

      for( String name : theNames )
      {
         slope = (Double)theSlopes.get( name );
         if( slope == smallest )
         {
            System.out.println( "Analyzing: " + name );
            theElem = _theManager.getStockElement( name );
            _location = theElem.getSize() - totalLength;
            _theManager.analyze( name, _location, _length );
         }
      }
   }

   /**
    *
    */
   public void testAutoCorrelation()
   {
      double arg = 0;
      double temp = 0;
      double freq = Math.PI;
      ArrayList<Double> list = new ArrayList<Double>();

      System.out.println( "The values of the function are: " );
      System.out.println( "" );
      Random ran = new Random();
      double value = 0;

      while( arg < 80 )
      {
      value = 0;
      for( int i=0; i<100; i++ )
      {
         temp = 10 * Math.sin( arg ) + Math.sin( freq * arg );
//         temp = 10 * Math.sin( arg );
         temp *= (ran.nextDouble() + 0.5); // not normal distro
//         arg += 0.15;
         value += temp;
      }
         temp = value / 100;
         list.add( temp );
         System.out.println( temp );
         arg += 0.7;
      }

      System.out.println( "" );
      System.out.println( "The auto correlations of the function are: " );
      System.out.println( "" );

      AutoCorrelation ac = new AutoCorrelation( list );
      ArrayList<Double> acList = ac.getCorrelationSet();
      for( double item : acList )
      {
         System.out.println( item );
      }
   }

   /**
    *
    */
   public void printAutoCorrelation()
   {
      StockCollection theColl = new StockCollection();
      ArrayList<String> theNames = theColl.getNasdaq100StockNames();
      StockElement theElem = null;
      int windowSize = 32;
      int daysBack = 265;
      String maxName = null;
      Double maxTrans = -1D;
      int position = 0;

      for( String name : theNames )
      {
         _stockName = name;
         System.out.println( "A QQQ stock is: " + _stockName );
         theElem = _theManager.getStockElement( _stockName );
         theElem.calculatePercentages();
//         theElem.calculateAverages( 3 );
         ArrayList<Double> closes = theElem.getAllClosesP();
//         ArrayList<Double> closes = theElem.getAverages();
         ArrayList<String> averagesList = new ArrayList<String>();
         ArrayList<String> correlationsList = new ArrayList<String>();
         ArrayList<String> transformsList = new ArrayList<String>();
         maxName = null;
         maxTrans = -1D;
         position = 0;

         if( (closes.size() - daysBack - windowSize) > 0 )
         for( int i=(closes.size() - daysBack); i<closes.size(); i+=windowSize )
         {
            ArrayList<Double> theList = new ArrayList<Double>();
            String averages = new String();
            String correlations = new String();
            String transforms = new String();
            Double average = 0D;
            Double averageClose = 0D;
            Double max = -10000.00;

            for( int j=(i - windowSize); j<i; j++ )
            {
               theList.add( 1 - closes.get( j ) );
               averageClose += ( 1 - closes.get( j ) );
            }
            averageClose /= (theList.size() + 1);
            System.out.println( "The average close is: " + averageClose );

            for( double close : theList )
            {
               averages += ( close + "," );
            }
            averagesList.add( averages );

            AutoCorrelation ac = new AutoCorrelation( theList );
            ArrayList<Double> acList = ac.getCorrelationSet();
            for( double value : acList )
            {
               correlations += ( value + "," );
            }
            correlationsList.add( correlations );

            FourierTransform ft = new FourierTransform( acList );
            ft.calculateTransform();
            ArrayList<Double> ftList = ft.getRealMapping();
            for( double value : ftList )
            {
               transforms += ( value + "," );
               average += value;
               if( value > max )
               {
                  max = value;
               }
            }
            average /= (ftList.size() + 1);
            double high = (max - average);
            transformsList.add( transforms + high );
            if( high > maxTrans )
            {
               maxTrans = high;
               maxName = name;
               position = i;
            }
         }
 
         System.out.println( ",,The max was: " + maxTrans + " ,at position:, " + position + "," + name );

/*
         System.out.println( "" );
         System.out.println( "Averages:" );
         System.out.println( "" );
         for( String data : averagesList )
         {
            System.out.println( data );
         }

         System.out.println( "" );
         System.out.println( "Correlations:" );
         System.out.println( "" );
         for( String data : correlationsList )
         {
            System.out.println( data );
         }

         System.out.println( "" );
         System.out.println( "Transforms:" );
         System.out.println( "" );
         for( String data : transformsList )
         {
            System.out.println( data );
         }
*/

      }
   }

   /**
    * In this methodology, we buy sometime after the IPO and hold
    * for some amount of time.
    *
    * Don't forget, for our data only goes back to 1980, so we
    * need to make sure that the data set isn't too large; 
    * because if it is too large, it's not an IPO buy.
    *
    *
    */
   private void performIPO()
   {
      StockCollection theColl = new StockCollection();
//      ArrayList<String> theNames = theColl.getNasdaq100StockNames();
//      ArrayList<String> theNames = theColl.getLiquidStockNames();
//      ArrayList<String> theNames = theColl.getStockNames();
//      ArrayList<String> theNames = theColl.getDJIAStockNames();
//      ArrayList<String> theNames = theColl.getRussell1000StockNames();
//      ArrayList<String> theNames = theColl.getRussell2000StockNames();
      ArrayList<String> theNames = theColl.getRussell3000StockNames();
//      ArrayList<String> theNames = theColl.getSP500StockNames();
//      ArrayList<String> theNames = theColl.getSP1500StockNames();
      HashMap<String, Double> results = new HashMap<String, Double>();
      HashMap<String, Integer> counts = new HashMap<String, Integer>();

      int minInitialPeriod = 10;
      int maxInitialPeriod = 500;
      int minHoldPeriod = 50;
      int maxHoldPeriod = 600;
      double entry = 0;
      double exit = 0;

      for( String stockName : theNames )
      {
         StockElement element = _theManager.getStockElement( stockName );
         System.out.println( "Element: " + element.getName() );

         if( element.getSize() < 7000 )
         {
            for( int i=minInitialPeriod; i<maxInitialPeriod; i++ )
            {
               for( int j=minHoldPeriod; j<maxHoldPeriod; j++ )
               {
                  double value = element.performBuy( 10000, i, (i+j) );
                  Double current = results.get( i + "," + j );
                  if( current == null )
                  {
                     current = new Double( 0 );
                  }
                  Integer count = counts.get( i + "," + j );
                  if( count == null )
                  {
                     count = new Integer( 0 );
                  }
                  current += value;
                  count++;
                  results.put( (i + "," + j), current );
                  counts.put( (i + "," + j), count );
               }
            }
         }
      }

      System.out.println( "\nTotal Results\n" );

      for( Iterator iter = results.keySet().iterator();
           iter.hasNext(); )
      {
         String key = (String)iter.next();
         System.out.println( key + "," + results.get( key ) + "," + counts.get( key ) );
      }
   }

   /**
    * 
    * 
    *
    * 
    * 
    */
   private void findHedgeCollars()
   {
      StockCollection theColl = new StockCollection();
      ArrayList<String> theNames = theColl.getSP500StockNames();
//       System.out.println( "The number of SP500: " + theNames.size() );
      TreeMap<Double, String> sorted = new TreeMap<Double, String>();

      System.out.println( "S&P 500 Securities:" );
      System.out.println( "Security, Last Close, Last Volume, Deviation, Rate Of Change, Recent Rate of Change" );

      for( String name : theNames )
      {
         StockElement theElem = _theManager.getStockElement( name );
         double last = theElem.getLastClose();
//          if( last <= 25.0 )
         {
            double floor = Math.floor( last );
            double deviation = theElem.getStandardDeviation( 5 );
            double ROC = theElem.getRateOfChange( 90 );
            double recentROC = theElem.getRateOfChange( 10 );
//             sorted.put( (last - floor), name + ", " + 
//                                         theElem.getLastClose() + ", " + 
//                                         theElem.getLastVolume() + ", " + 
//                                         deviation + ", " + 
//                                         ROC + ", " + 
//                                         recentROC );
            System.out.println( name + ", " + theElem.getLastClose() + ", " +
                  theElem.getLastVolume() + ", " + deviation + ", " + ROC + ", " + recentROC );
         }
      }

//       for( Iterator iter = sorted.values().iterator(); iter.hasNext(); )
//       {
//          String data = (String)iter.next();
//          System.out.println( data );
//       }

      theColl = new StockCollection();
      theNames = theColl.getSP1000StockNames();
//       System.out.println( "The number of SP1000: " + theNames.size() );
      sorted = new TreeMap<Double, String>();

      System.out.println( "\nS&P 1000 Securities:" );
      System.out.println( "Security, Last Close, Last Volume, Deviation, Rate Of Change, Recent Rate of Change" );

      for( String name : theNames )
      {
         StockElement theElem = _theManager.getStockElement( name );
         double last = theElem.getLastClose();
//          if( last <= 25.0 )
         {
            double floor = Math.floor( last );
            double deviation = theElem.getStandardDeviation( 5 );
            double ROC = theElem.getRateOfChange( 90 );
            double recentROC = theElem.getRateOfChange( 10 );
//             sorted.put( (last - floor), name + ", " + 
//                                         theElem.getLastClose() + ", " + 
//                                         theElem.getLastVolume() + ", " + 
//                                         deviation + ", " + 
//                                         ROC + ", " + 
//                                         recentROC );
            System.out.println( name + ", " + theElem.getLastClose() + ", " +
                  theElem.getLastVolume() + ", " + deviation + ", " + ROC + ", " + recentROC );
         }
      }

      for( Iterator iter = sorted.values().iterator(); iter.hasNext(); )
      {
         String data = (String)iter.next();
         System.out.println( data );
      }
   }

   /**
    * The basic idea here is a primitive form of arbitrage.
    * Because of the way options are priced, we will restrict our interest to those stocks whose
    * price is between 10 and 25 dollars per share.
    * We begin by finding the stocks that have risen the most in the past X days (this will increase
    * the covered call premium).  Then we find the stock that is closest to the dollar boundary.
    * In this stock, enter a long position and write a call for a strike price of +1.
    * With the call premium, purchase a put option for a strike price of -y. This price is inconsequential
    * right now, since we can't simulate options prices yet.
    * Assume the covered call and long put options are contracts for one month.
    * Let O be the price of the long price, let Sc be the strike price of the call, let Sp be the strike
    * price of the put option, and let C be the close price for the month.
    * In this one month contract period, there are three cases:
    *
    * 1) The stock price rises to the call strike price.
    *       In this case, the call will be exercised, the stock will be sold at the strike (~+1) and
    *       the premium (which has been used) is kept. The put option is still valid until the end
    *       of the month, but the strategy can turnaround and enter a new position.
    *       Overall, there is a profit of a percentage Sc / O.
    * 2) The stock price never rises to the call strike and ends the month even or slightly above.
    *       Since the call won't be exercised, the premium is kept but expires useless. The best
    *       strategy is to hold the position to lock in the gains, and open a new buy-write, since
    *       most gains will be eaten by commission fees. Though, when and if the strike is hit, 
    *       profits will likely be smaller since the stock has gained some.
    * 3) The stock price never rises to the call strike and ends the month even or down.
    *       In this scenario, there are really two sub-cases:
    *       a) The stock price ends within the same dollar strike as O.
    *             Again, the call option probably won't be exercised.
    *             There is an unrealized loss, but since we're in the same strike zone, we can just
    *             re-open a long buy-write and start again.
    *       b) The stock ends down in the next +z strike zones.
    *             Here, depending on how far down the stock has dropped and when (early or late in 
    *             the contract period) a profit might be realized.
    *             This can be done since the call option probably won't be exercised.
    *             Most importantly, if this drop happens late, there will likely be no profits realized.
    *             The only thing that can come of this is exercising the options to sell at a higher 
    *             price -- but that eats into the covered call premium.
    */
   public void performStrategyX()
   {
      _theManager.outputDataForMTOE();
//       _theManager.findNearestQQQRecent();
//       _theManager.calculateCorrelation();
//       _theManager.performMCCPSimulation();
//       _theManager.recognizeQuantiles();
//       _theManager.mapQuantiles();
//       _theManager.determineQuantileDistance();
//       _theManager.performSimulation();
//       _theManager.calculateQSims();
//       _theManager.calculatePhasePortrait();
//       _theManager.testManyToMany( 10 );
//       _theManager.calcDividends();
//       _theManager.calcVarianceRatio();
//       _theManager.calcPowerDist();
//       _theManager.calcWindowMaxes();
//       _theManager.calculatePremiums( "04", "2011" );
//       _theManager.calculateMeanRates();
//       _theManager.outputLargeMovers();
//       _theManager.getLatestMovers();
//       _theManager.getPutOptionPrices();
//       _theManager.watchStocks();
//       _theManager.logOptimalTwo( 10 );
//       _theManager.logOptimalOne( 10 );
//       _theManager.calculateSwings( 20 );
//       _theManager.calculateMonthlyUnicon( "X" );
//       _theManager.simulate( 7629 );
//       _theManager.calculateDistribution( 25 );
//       _theManager.outputVolumeCalibration( 100 );
//       _theManager.calculateVolatilityStats( 260, 260 );
//       _theManager.calculateSpreadRebound();
//       _theManager.calculateLiquidMovers();
//       _theManager.calculateGravityTheory( 1, 9, 0.8, 3.0, 0.55 );
//       _theManager.calculateGravityTheory( 1, 9, 0.8, 30.0, 0.055 );
//       _theManager.calculateGravityTheory( 1, 3, 0.8, 30.0, 0.055 );
//       _theManager.calculateGravityTheoryShort( 2, 9, 0.73 );
//       _theManager.calculateGravityTheoryShort( 2, 1, 0.7 );
//       _theManager.calculateLatestNasdaqa100Movers();
//       _theManager.calculateLatestNasdaqa100Movers();
//       _theManager.calculateCompetitionTheory( 5, 1 );
//       _theManager.calculateAllStatistics( 5 );
//       _theManager.calculateLatestUnderCurrent();
//       _theManager.calculateCoherenceStatsStatic2( 2 );
//       _theManager.calculateCoherenceStatsStatic2( 5 );
//       _theManager.calculateCoherenceStatsStatic2( 10 );
//       _theManager.calculateCoherenceStatsStatic( 2 );
//       _theManager.calculateCoherenceStatsStatic( 5 );
//       _theManager.calculateCoherenceStatsStatic( 15 );
//       _theManager.calculateCoherenceStatsStatic( 45 );
//       _theManager.calculateCoherenceStatsStatic( 75 );
//       _theManager.calculateCoherenceStats( 2 );
//       _theManager.calculateCoherenceStats( 3 );
//       _theManager.calculateCoherenceStats( 5 );
//       _theManager.calculateCoherenceStats( 10 );
//       _theManager.calculateCoherenceStats( 20 );
//       _theManager.calculateCoherenceStats( 40 );

//       //       Testing out all window and hold values
//       for( int i=1; i<2; i++ )
//       {
//          for( int j=9; j<20; j++ )
//          {
//             double s = 0.1;
//             double t = 0.0;
//             int n = (int)(1.0 / s);
//             for( int k=0; k<n; k++ )
//             {
//                t += s;
// //                double[] r = _theManager.calculateGravityTheoryShort( i, j, t );
//                double[] r = _theManager.calculateGravityTheory( i, j, t );
//                System.out.println( "window:, " + i + " ,hold:, " + j + " ,mix ratio:, " + t +
//                                    " ,cash:, " + r[0] + " ,median:, " + r[1] );
//             }
//          }
//       }

//       int limit = 10;
//       double hs = 1.0;
//       double he = 3.0;
//       double hstep = (he - hs) / (double)limit;
// 
//       double ls = 0.1;
//       double le = 1.0;
//       double lstep = (le - ls) / (double)limit;
// 
//       double ms = 0.0;
//       double me = 1.0;
//       double mstep = (me - ms) / (double)limit;
//       double cash = 10000;
// 
//       for( int i=0; i<limit; i++ )
//       {
//          hs += hstep;
//          ls = 0.1;
//          for( int j=0; j<limit; j++ )
//          {
//             ls += lstep;
//             ms = 0.0;
//             for( int k=0; k<limit; k++ )
//             {
//                ms += mstep;
//                double[] r = _theManager.calculateGravityTheory( 1, 9, ms, hs, ls );
//                System.out.println( "mix:, " + ms + " ,high:, " + hs + " ,low:, " + ls +
//                                    " ,cash:, " + r[0] + " ,median:, " + r[1] );
//                System.out.println( "mix:, " + ms + " ,cash:, " + r[0] + " ,median:, " + r[1] );
//             }
//          }
//       }
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void testManyToMany( String date, String time, String e, String g )
   {
      _theManager.testManyToMany( date, time, e, g );
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void testManyToFew( String date, String time, String e, String g, String amend )
   {
      if( date.startsWith( "-" ) )
      {
         System.out.println( "Will perform currentOpenSpread" );
//          _theManager.currentManyToFew( date, time, e, g );
         _theManager.currentOpenSpread( date, time, e, g );
      }
      else
      {
//          System.out.println( "Will perform testManyToFew" );
         System.out.println( "Will perform trainOpenSpread" );
//          _theManager.testManyToFew( date, time, e, g );
         if( amend == null )
         {
            _theManager.trainOpenSpread( date, time, e, g, false );
         }
         else
         {
            _theManager.trainOpenSpread( date, time, e, g, true );
         }
      }
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void testManyToManyLong( String date, String time, String e, String g )
   {
      _theManager.testManyToManyLong( date, time, e, g );
   }

   /**
    *
    */
   public void performWatcher()
   {
      _theManager.watchStocks();
   }

   /**
    *
    */
   private void setNumberPredictions( int num )
   {
      _numPredictions = num;
   }

   /**
    *
    */
   private void setIntervalsBack( String num )
   {
      System.out.println( "Will set Intervals back to: " + num );
      try
      {
         _intervalsBack = Integer.parseInt( num );
      }
      catch( NumberFormatException e )
      {
         _intervalsBack = 1;
      }
      System.out.println( "Intervals back is: " + _intervalsBack );
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
    *        +--------------+---------------+------+-----+---------+-------+
    */
   private void gatherOptionsData()
   {
      _theManager.gatherOptionsData();
   }

   /**
    *
    */
   private void getLatestMovers()
   {
      _theManager.getLatestMovers();
   }

   /**
    *
    */
   private void handleArgs( String[] args )
   {
      if( args[0].equalsIgnoreCase( "confidence" ) )
      {
         _theManager = new StockManager( LoadEnum.LOAD_ALL_YEAR );
         _theManager.calculateProbabilities();
      }
      else if( args[0].equalsIgnoreCase( "compare" ) )
      {
         _theManager = new StockManager( LoadEnum.LOAD_ALL_YEAR );
         _theManager.compareDataFiles();
      } // end if
      else if( args[0].equalsIgnoreCase( "simulate" ) )
      {
         _theManager = new StockManager( LoadEnum.LOAD_ALL_YEAR );
         //_theManager.simulateSBD();
         _theManager.findSimilarQQQStocks();
      } // end if
      else if( args[0].equalsIgnoreCase( "loss" ) )
      {
         Collection theColl = _theManager.getSortedLossCollection();
         Iterator theIter = theColl.iterator();
         StockElement theElement = null;
         while( theIter.hasNext() )
         {
            theElement = (StockElement)theIter.next();
            System.out.println( theElement.toString() );
         } // end while
      } // end if
      else if( args[0].equalsIgnoreCase( "calibrate" ) )
      {
         _theManager = new StockManager( LoadEnum.LOAD_ALL_YEAR );
         _theManager.outputCalibration();
/*
         Collection theColl = _theManager.getStockElementCollection();
         Iterator theIter = theColl.iterator();
         StockElement theElement = null;

         
         BufferedWriter theFile = null;
         try
         {
            theFile = new BufferedWriter( new FileWriter( "calibrate.rank" ) );
            while( theIter.hasNext() )
            {
               theElement = (StockElement)theIter.next();
               if( theElement.getVolume() < 1000000 )
               {
                  continue;
               } // end if
               String theLine = theElement.getName() + " " + theElement.getRealWorth();
               theFile.write( theLine );
               theFile.newLine();
            } // end while
         } 
         catch( IOException ioe )
         {
            System.out.println( ioe );
         } // end catch
         finally
         {
            try {
               theFile.flush();
               theFile.close();
            } catch( IOException ioe ) {}
         } // end finally
*/
      } // end if
      else
      {
         _theManager = new StockManager( LoadEnum.LOAD_ALL_YEAR );
         StockElement theElem = null;
         for( int i=0; i<args.length; i++ )
         {
            theElem = _theManager.getLikeStockElement( args[i] );
            System.out.println( "The Similar stock to: " + args[i] + " is:\n\n" + theElem.toString() );
         } // end if
      } // end if
   } // end handArgs

   /**
    * Call like: stockDriver.sh INTU 1720 40  --> default number of pred is 45
    * Call like: stockDriver.sh INTU 1720 40 30 --> predictions is 30 in this case
    * Call like: stockDriver.sh  --> predictions is 30 in this case
    *
    *
    *
    *
    */
   public static void main( String[] args )
   {
      StockDriver theDriver = null;
      boolean isLinear = Boolean.getBoolean( "linear" );
      if( isLinear )
      {
         System.out.println( "Performing linear function..." );
         theDriver = new StockDriver();
         theDriver.gatherOptionsData();
//          theDriver.performStrategyX();
//          theDriver.setNumberPredictions( 1 );
//          theDriver.setIntervalsBack( args[1] );
//          theDriver.handleLinear();
         return;
      }

      boolean isSingle = Boolean.getBoolean( "single" );
      if( isSingle )
      {
         System.out.println( "Performing single function..." );
         theDriver = new StockDriver();
//          theDriver.handleSingle( args );
//          theDriver.testManyToMany( args[ 0 ], args[ 1 ], args[ 2 ], args[ 3 ] );
         if( args.length == 5 )
         {
            theDriver.testManyToFew( args[ 0 ], args[ 1 ], args[ 2 ], args[ 3 ], args[ 4 ] );
         }
         else
         {
            theDriver.testManyToFew( args[ 0 ], args[ 1 ], args[ 2 ], args[ 3 ], null );
         }
//          theDriver.testManyToManyLong( args[ 0 ], args[ 1 ], args[ 2 ], args[ 3 ] );

         return;
      }

      boolean regular = Boolean.getBoolean( "regular" );
      if( regular )
      {
         System.out.println( "Performing regular function..." );
         theDriver = new StockDriver();
         theDriver.getLatestMovers();
      }
      else if( args.length == 3 )
      {
         System.out.println( "Performing normal function (3)..." );
         //theDriver = new StockDriver( args[0], args[1], args[2], regular );
         theDriver = new StockDriver( args[0], args[1], args[2] );
         theDriver.setNumberPredictions( 45 );
         System.out.println( "Performing HTML function..." );
         theDriver.outputHTML();
      }
      else if( args.length == 4 )
      {
         System.out.println( "Performing normal function (4)..." );
         // stock name, position, length, number of predictions
         theDriver = new StockDriver( args[0], args[1], args[2] );
         theDriver.setNumberPredictions( Integer.parseInt( args[3] ) );
         System.out.println( "Performing HTML function..." );
         theDriver.outputHTML();
      }
      else if( args.length == 1 )
      {
         System.out.println( "Performing default function (1)..." );
         theDriver = new StockDriver();
         // Parameters are: date, train time, ensemble size, genome length
         theDriver.testManyToMany( args[ 0 ], args[ 1 ], args[ 2 ], args[ 3 ] );
//         ArrayList<String> theNames = theColl.getNasdaq100StockNames();
//         ArrayList<String> theNames = theColl.getLiquidStockNames();
//         ArrayList<String> theNames = theColl.getStockNames();
//         ArrayList<String> theNames = theColl.getRussell1000StockNames();
//         ArrayList<String> theNames = theColl.getRussell3000StockNames();
//         ArrayList<StockElement> theElements = new ArrayList<StockElement>();
//         theDriver.setNumberPredictions( 1 );
//         theDriver.setIntervalsBack( args[0] );
//         theDriver.runNasdaq100();
//         theDriver.runNasdaq100QQQ();
//          System.out.println( "***SP500***" );
//          StockCollection theColl = new StockCollection();
//          ArrayList<String> theNames = theColl.getSP500StockNames();
//          theDriver.currentMinorTheory( theNames );
// 
//          System.out.println( "***SP1500***" );
//          theColl = new StockCollection();
//          theNames = theColl.getSP1500StockNames();
//          theDriver.currentMinorTheory( theNames );
// 
//          System.out.println( "***Russell2000***" );
//          theColl = new StockCollection();
//          theNames = theColl.getRussell2000StockNames();
//          theDriver.currentMinorTheory( theNames );
// 
//          System.out.println( "***DJIA***" );
//          theColl = new StockCollection();
//          theNames = theColl.getDJIAStockNames();
//          theDriver.currentMinorTheory( theNames );
      }
      else
      {
//          System.out.println( "Performing default function..." );
         boolean isWatcher = Boolean.getBoolean( "watcher" );
         if( isWatcher )
         {
            System.out.println( "Performing watcher function..." );
            theDriver = new StockDriver();
            theDriver.performWatcher();
            return;
         }
         // perform the "real" default scenario:
         else
         {
            System.out.println( "Performing real default function..." );
            theDriver = new StockDriver();
//             theDriver.simulateDecoherence();
//             theDriver.runNasdaq100Trend();
//             theDriver.setNumberPredictions( 1 );
//             theDriver.runNasdaq100();
//             theDriver.runNasdaq100QQQ();
//             theDriver.printAutoCorrelation();
//             theDriver.testAutoCorrelation();
//             theDriver.minorTheory();
//             theDriver.performIPO();
//             theDriver.findHedgeCollars();
            theDriver.performStrategyX();
         }

      }

      //if( (args.length > 0) && (args.length < 3) )
      //{
         //theDriver.handleArgs( args );
      //} // end if
   }  // end main

   /**
    *
    */
   private class MiniElement
   {
      private String _name;
      private double _entry;
      public MiniElement( String name, double entry )
      {
         _name = name;
         _entry = entry;
      }
      public String getName()
      {
        return _name;
      }
      public double getEntry()
      {
        return _entry;
      }
   }

} // end class
