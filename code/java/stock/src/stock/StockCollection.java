package stock;

import common.FileReader;
import common.ZipReader;
import java.util.*;
import java.io.*;

/**
 * Class StockCollection
 */
public class StockCollection
{

   private String                    _fileName      = "persistence.dat";
   private ArrayList<String>         _stockNames    = null;
   private int                       _sortedSize    = 0;
   private StockCollectionLoader     _theLoader     = new StockCollectionLoader();
   private WeakHashMap<String, StockElement> _stockElements = new WeakHashMap<String, StockElement>();

   /**
    *
    */
   public StockCollection()
   {
   } // end default constructor

   /**
    *
    */
   public StockCollection( boolean loadData )
   {
      if( loadData )
         loadData();
   } // end constructor

   /**
    *
    */
   public StockCollection( String fileName )
   {
      _fileName = fileName;
      loadData();
   } // end constructor

   /**
    *
    */
   protected void loadData()
   {
      FileReader   theFile   = new FileReader( _fileName );
      String[]     wordArray = theFile.getArrayOfWords();
      StockElement theElem   = null;
      //_stockElements         = new HashMap();

      for( int i=0; i<wordArray.length; i+=3 )
      {
         theElem = new StockElement( wordArray[i], wordArray[i+1], wordArray[i+2] );
         _stockElements.put( theElem.getName(), theElem );
      } // end for

   } // end loadData

   /**
    *
    */
   public void addElement( StockElement theElem )
   {
      _stockElements.put( theElem.getName(), theElem );
   }

   /**
    *
    */
   protected void saveData()
   {
      try
      {
         Iterator theIter = (getStockElementCollection()).iterator();
         BufferedWriter theWriter = null;
         while( theIter.hasNext() )
         {
            StockElement theElem = (StockElement)theIter.next();

            theWriter = new BufferedWriter( new FileWriter( "ParamData\\" + theElem.getName() + ".csv" ) );
            StringBuffer str = null;
            ArrayList[] theParamData = theElem.getParamRankData();
            //ArrayList theParamData = theElem.getParameterList();
            StockParameterData theData;
            str = new StringBuffer();

/*
            for( int i=0; i<theParamData.size(); i++ )
            { 
               theData = (StockParameterData)theParamData.get( i );
               str.append( theData.toString() );
               str.append( "\n" );
            } // end for
*/

            for( int i=0; i<365; i++ )
            {
               for( int j=0; j<theParamData.length; j++ )
               {
                  if( theParamData[ j ] != null && i < theParamData[ j ].size() )
                  {
                     str.append( (theParamData[ j ].get( i )).toString() );
                  } // end if element exists
                  if( j != (theParamData.length - 1) )
                  {
                     str.append( "," );
                  }
               } // end for
               str.append( "\n" );
            } // end for

            theWriter.write( str.toString() );
            theWriter.flush();
            theWriter.close();

         } // end while
      }
      catch( IOException e )
      {
         System.err.println( "Error persisting data: " + e );
      } // end catch
   } // end saveData

   /**
    *
    */
   public void update( String name, String open, String high, String close )
   {
      StockElement theElement = (StockElement)_stockElements.get( name );
      //System.out.println( "update: " + name );
      if( theElement == null )
      {
         theElement = new StockElement( name );
         _stockElements.put( theElement.getName(), theElement );
      } // end if null
      theElement.addOpen( open );
      theElement.addHigh( high );
      theElement.addClose( close );
   }  // end update

   /**
    *
    */
   public void update( String date, String name, String open, String high, String low, String close, String vol )
   {
      StockElement theElement = (StockElement)_stockElements.get( name );
      if( theElement == null )
      {
         theElement = new StockElement( name );
         _stockElements.put( theElement.getName(), theElement );
      } // end if null
      theElement.addDate( date );
      theElement.addOpen( open );
      theElement.addHigh( high );
      theElement.addLow( low );
      theElement.addClose( close );
      theElement.addVolume( vol );
   }  // end update

   /**
    *
    */
   public void update( String name, String conf )
   {
      StockElement theElement = (StockElement)_stockElements.get( name );
      theElement.setConfidence( conf );
   } // end update

   /**
    *
    */
   public void updatePrevious( String name, String conf )
   {
      StockElement theElement = (StockElement)_stockElements.get( name );
      if( theElement != null )
         theElement.setPrevConfidence( conf );
   } // end update

   /**
    *
    */
   public Collection getSortedVarianceCollection()
   {
      Collection theColl      = _stockElements.values();
      Iterator   theIter      = theColl.iterator();
      TreeMap    theSortedMap = new TreeMap();
      StockElement theElem    = null;

      while( theIter.hasNext() )
      {
         theElem = (StockElement)theIter.next();
         //theSortedMap.put( theElem.getLastVarianceSMA(), theElem );
      } // end while

      return theSortedMap.values();

   } // end getSortedVarianceCollection

   /**
    *
    */
   public Collection getSortedStabilityCollection()
   {
      Collection theColl      = _stockElements.values();
      Iterator   theIter      = theColl.iterator();
      TreeMap    theSortedMap = new TreeMap();
      StockElement theElem    = null;

      while( theIter.hasNext() )
      {
         theElem = (StockElement)theIter.next();
         //theSortedMap.put( theElem.getLastStabilitySMA(), theElem );
      } // end while

      return theSortedMap.values();

   } // end getSortedStabilityCollection

/*
   public Collection getSortedConfidenceCollection()
   {
      Collection theColl = getStockElementCollection();
      Iterator theIter   = theColl.iterator();
      ArrayList theList  = new ArrayList();

      while( theIter.hasNext() )
         theList.add( theIter.next() );

      Collections.sort( theList );

      return (Collection)theList;
   } // end getSortedConfidenceCollection
*/

   /**
    *
    */
   public Collection getBestSortedCollection()
   {
      Collection   theColl = getSortedVarianceCollection();
      Iterator     theIter = theColl.iterator();
      TreeMap      theBest = new TreeMap();
      StockElement theElem = null;
      ArrayList    theList = new ArrayList();

      while( theIter.hasNext() )
         theList.add( theIter.next() );

      // Could make this a parameter
      for( int i=theList.size()-1; i>475; i--)
      {
         theElem = (StockElement)theList.get( i );
         theBest.put( new Integer( theElem.getStabilityRank() ), theElem );
      } // end for
       
      return theBest.values();

   } // end getBestSortedCollection

   /**
    *
    */
   public Collection getLargestVarianceCollection()
   {
      Collection   theColl = getStockElementCollection();
      Iterator     theIter = theColl.iterator();
      TreeMap      theBest = new TreeMap();
      StockElement theElem = null;
      double       theDiff = 0;
      double       temp    = 0;

      while( theIter.hasNext() )
      {
         theElem = (StockElement)theIter.next();
         //theDiff = theElem.getLastVarianceSMA().doubleValue();
         if( theDiff < 1 ) continue;
         //temp    = theElem.getLastStabilitySMA().doubleValue();
         if( temp < 0 ) temp *= -1;
         theDiff = theDiff - temp;
         theBest.put( new Double( theDiff ), theElem );
      } // end while

      System.out.println( "the size is: " + theBest.size() );
       
      return theBest.values();

   } // end getLargestVarCollection

   /**
    *
    */
   public Collection getSortedDensityCollection()
   {
      Collection theColl      = _stockElements.values();
      Iterator   theIter      = theColl.iterator();
      TreeMap    theSortedMap = new TreeMap();
      StockElement theElem    = null;

      while( theIter.hasNext() )
      {
         theElem = (StockElement)theIter.next();
         theSortedMap.put( theElem.getDensity(), theElem );
      } // end while

      return theSortedMap.values();

   } // end getSortedDensityCollection

   /**
    *
    */
   public Collection getSortedDensityCollection2()
   {
      Collection theColl      = getSortedDensityCollection();
      Iterator   theIter      = theColl.iterator();
      TreeMap    theSortedMap = new TreeMap();
      StockElement theElem    = null;
      int count = 0;
      int increase = 0;

      while( theIter.hasNext() )
      {
         theElem = (StockElement)theIter.next();
         if( theElem.getVarianceMass().doubleValue() > 6 )
         {
            theSortedMap.put( theElem.getDensity(), theElem );
            count++;
            if( theElem.isIncreasing() )
               increase++;
         } // end if variance > 6
      } // end while
      System.out.println( "The Number of elements with variance mass > 6 is: " + count );
      System.out.println( "The Number of elements increasing is: " + increase );

      return theSortedMap.values();

   } // end getSortedDensityCollection

   /**
    *
    */
   public Collection getActiveCollection()
   {
      Collection   theColl      = _stockElements.values();
      Iterator     theIter      = theColl.iterator();
      ArrayList    theList      = new ArrayList();
      StockElement theElement   = null;

      while( theIter.hasNext() )
      {
         theElement = (StockElement)theIter.next();
//System.out.println( "Checking element: " + theElement.getName() + " value: " + theElement.getThreshold() );
         //if(    theElement.isActive2( theElement.getSize(), true ) 
         if(    theElement.isActive( theElement.getSize(), true ) 
             && theElement.isInitialized() )
         {
            theList.add( theElement );
         } // end if
      } // end while

      //Collections.sort( theList );

      return theList;

   } // end getActiveCollection

   /**
    *
    */
   public int getSortedSize()
   {
      return _sortedSize;
   } // end getSortedSize

   /**
    *
    */
   public Collection getStockElementCollection()
   {
      return _stockElements.values();
   } // end getStockCollection

   /**
    *
    */
   public Collection getSortedConfidenceCollection()
   {
      Collection theColl      = getActiveCollection();
      //Collection theColl      = getStockElementCollection();
      Iterator   theIter      = theColl.iterator();
      TreeMap    theSortedMap = new TreeMap();
      StockElement theElem    = null;

      while( theIter.hasNext() )
      {
         theElem = (StockElement)theIter.next();
         //theSortedMap.put( new Double( theElem.getThreshold() ), theElem );
         //theSortedMap.put( new Double( theElem.getSMAvalue() ), theElem );
         theSortedMap.put( new Double( theElem.getWorth() ), theElem );
      } // end while

      return theSortedMap.values();

   } // end getSortedConfidenceCollection

   /**
    *
    */
   public Collection getSortedSMACollection()
   {
      Collection theColl      = getActiveCollection();
      Iterator   theIter      = theColl.iterator();
      TreeMap    theSortedMap = new TreeMap();
      StockElement theElem    = null;

      while( theIter.hasNext() )
      {
         theElem = (StockElement)theIter.next();
         theSortedMap.put( theElem.getStabilityMass(), theElem );
      } // end while

      return theSortedMap.values();

   } // end getSortedConfidenceCollection

   /**
    *
    */
   public Collection getSortedLossCollection()
   {
      Collection theColl      = getActiveCollection();
      Iterator   theIter      = theColl.iterator();
      TreeMap    theSortedMap = new TreeMap();
      StockElement theElem    = null;

      while( theIter.hasNext() )
      {
         theElem = (StockElement)theIter.next();
         theSortedMap.put( new Double( theElem.getTotalLoss() ), theElem );
      } // end while

      return theSortedMap.values();

   } // end getSortedLossCollection

   /**
    *
    */
   public Collection getSortedWorthCollection()
   {
      Collection theColl      = getActiveCollection();
      Iterator   theIter      = theColl.iterator();
      TreeMap    theSortedMap = new TreeMap();
      StockElement theElem    = null;

      while( theIter.hasNext() )
      {
         theElem = (StockElement)theIter.next();
         theSortedMap.put( new Double( theElem.getRealWorth() ), theElem );
      } // end while

      theColl = theSortedMap.values();
      theIter = theColl.iterator();
      ArrayList theList = new ArrayList();
      while( theIter.hasNext() )
      {
         //theList.add( 0, theIter.next() );
         theList.add( theIter.next() );
      } // end while 

      return theList;

   } // end getSortedWorthCollection

   /**
    *
    */
   public String toString()
   {
      String str = new String( _stockElements.toString() );
      return str;
   } // end toString

   /**
    *
    */
   public int getSize()
   {
      return _stockElements.size();
   } // end getSize

   /**
    * Return the element from the weak reference cache
    */
   public StockElement getStockElement( String name )
   {
      StockElement theElement = _stockElements.get( name );

      if( theElement == null )
      {
         try
         {
            theElement = _theLoader.getStockElement( name );
         }
         catch( Exception e )
         {
         }
      }

      return theElement;
   } // end getStockElement

   /**
    *
    */
   public ArrayList<String> getSP1000StockNames()
   {
      if( _stockNames == null )
      {
         _stockNames = _theLoader.getSP1000StockNames();
      }
      return _stockNames;
   } // end getSP1000StockNames

   /**
    *
    */
   public ArrayList<String> getSP1500StockNames()
   {
      if( _stockNames == null )
      {
         _stockNames = getSP1000StockNames();
         ArrayList< String > names = getSP500StockNames();
         for( String name : names )
         {
            _stockNames.add( name );
         }
      }
      return _stockNames;
   }

   /**
    *
    */
   public ArrayList<String> getSP500StockNames()
   {
      if( _stockNames == null )
      {
         _stockNames = _theLoader.getSP500StockNames();
      }
      return _stockNames;
   } // end getSP500StockNames

   /**
    *
    */
   public ArrayList<String> getStockNames()
   {
      if( _stockNames == null )
      {
         _stockNames = _theLoader.getStockNames();
      }
      return _stockNames;
   } // end getStockNames

   /**
    *
    */
   public ArrayList<String> getNasdaq100StockNames()
   {
      if( _stockNames == null )
      {
         _stockNames = _theLoader.getNasdaq100StockNames();
      }
      return _stockNames;
   } // end getStockNames

   /**
    *
    */
   public ArrayList<String> getLiquidStockNames()
   {
      if( _stockNames == null )
      {
         _stockNames = _theLoader.getLiquidStockNames();
      }
      return _stockNames;
   } // end getStockNames

   /**
    *
    */
   public ArrayList<String> getRussell1000StockNames()
   {
      if( _stockNames == null )
      {
         _stockNames = _theLoader.getRussell1000StockNames();
      }
      return _stockNames;
   } // end getStockNames

   /**
    *
    */
   public ArrayList<String> getRussell2000StockNames()
   {
      if( _stockNames == null )
      {
         _stockNames = _theLoader.getRussell2000StockNames();
      }
      return _stockNames;
   } // end getStockNames

   /**
    *
    */
   public ArrayList<String> getRussell3000StockNames()
   {
      if( _stockNames == null )
      {
         _stockNames = _theLoader.getRussell3000StockNames();
      }
      return _stockNames;
   } // end getStockNames

   /**
    *
    */
   public ArrayList<String> getDJIAStockNames()
   {
      if( _stockNames == null )
      {
         _stockNames = _theLoader.getDJIAStockNames();
      }
      return _stockNames;
   } // end getStockNames

   /**
    *
    */
   public ArrayList<String> getWatchedNames()
   {
      return _theLoader.getWatchedNames();
   }

   /**
    *
    */
   public void loadWatcherData( StockElement stock )
   {
      _theLoader.loadWatcherData( stock );
   }

   /**
    *
    */
   public void updateWatchedStockElement( StockElement stock )
   {
      _theLoader.updateWatchedStockElement( stock );
   }

   /**
    *
    */
   public void insertWatchedStockElement( StockElement stock, double delta )
   {
      _theLoader.insertWatchedStockElement( stock, delta );
   }

   /**
    *
    */
   public void clearWatchedStockElements()
   {
      _theLoader.clearWatchedStockElements();
   }

} // end class Stock Collection
