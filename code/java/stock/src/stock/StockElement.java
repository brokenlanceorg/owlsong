package stock;

import java.util.*;
import java.io.*;
import common.*;
import math.*;


/**
 * Class : StockElement
 * 1 year's worth data = 254 trading days.
 */
public class StockElement implements Comparable<StockElement>
{

   private String    _StockName         = null;
   private double    _SMAvalue          = 0.025;           // default
   private double    _Volume            = 0;               // default
   private Double    _Confidence        = new Double( 0 ); // default
   private Double    _PrevConfidence    = new Double( 0 ); // default
   private Double    _StabilityMass     = new Double( 0 ); // This is used in calcCValues
   private Double    _VarianceMass      = new Double( 0 ); // default
   //private Double    _Density           = new Double( 0 ); // default
   private double    _Worth             = 0;               // default
   private double    _RealWorth         = 0;               // default
   private double    _Cash              = 10000;           // default
   private double    _Threshold         = 0.8;
   private double    _TotalLoss         = 0;
   private double    _StandardDeviation = 0;
   private double    _previousTrade     = 0;
   private double    _strikePrice       = 0;
   private double[]  _ParamRank         = null;
   private double[]  _MACDSum           = null;
   private double[]  _MACDDiffSum       = null;
   private double[]  _StochasticSum     = null;
   private double[]  _StochasticDiffSum = null;
   static private int[]  _TotalDiffs = null;
   // QQQ values:
   private double[]  _QQQMACDSum           = null;
   private double[]  _QQQMACDDiffSum       = null;
   private double[]  _QQQStochasticSum     = null;
   private double[]  _QQQStochasticDiffSum = null;
   private double[]  _ScaleClose        = null;
   private double[]  _ScaleOpen         = null;
   private double[]  _ScaleHigh         = null;
   private double[]  _ScaleLow          = null;
   private double[]  _QQQScaleClose     = null;
   private double[]  _QQQScaleOpen      = null;
   private double[]  _Simulations       = null;
   private double[]  _HighDiff          = null;
   private double[]  _eventValues       = null;
   private static int[] _counts         = null;
   private double    _FastEMA           = 0;
   private double    _SlowEMA           = 0;
   private double    _SignalEMA         = 0;
   private double    _MACDMass          = 0;
   private double    _StochasticEMA     = 0;
   private double    _CurrStochastic    = 0;
   private double    _PrevStochastic    = 0;
   private double    _FastStochastic    = 0;
   private double    _HighMean          = 0;
   private double    _CloseMean         = 0;
   private double    _HighDeviation     = 0;
   private double    _CloseDeviation    = 0;
   private double    _highestMean       = 0;
   private double    _lowestMean        = 0;
   private double    _bestHighVariance  = 0;
   private double    _entropy           = 0;
   private double    _entropy1          = 0;
   private double    _entropy2          = 0;
   private double    _entropy3          = 0;
   private double    _discreteEntropy   = 0;
   private double    _discreteEntropy1  = 0;
   private double    _discreteEntropy2  = 0;
   private double    _discreteEntropy3  = 0;
   private double    _proportionEntropy  = 0;
   private double    _proportionEntropy1 = 0;
   private double    _proportionEntropy2 = 0;
   private double    _proportionEntropy3 = 0;
   private ArrayList[] _ParamRankData   = null;
   private int       _window            = 0;
   private int       _bestDayHighMean   = 0;
   private int       _bestEvents        = 0;
   private int       _HighWorth         = 0;
   private int       _VARRank           = 500;             // default
   private int       _STABRank          = 500;             // default
   private int       _DaysAbove         = 30;              // default
   private int       _BestConseq        = 0;               // default
   private int       _BestDepth         = 0;               // default
   private int       _LastBuy           = 0;               // default
   private int       _RankNumber        = 10;              // default
   private int       _StochasticDays    = 0;               // default
   private int       _NumberOfActives   = 0;               // default
   private int       _NumberOfPredictions = 12;            // default
   private int       _NumberOfSamples   = 50;              // default
   private int       _compareType       = 0;               // 0 = by threshold, 1 = by SMA
   private ArrayList<Double>       _RealVariance       = null;
   private ArrayList<Double>       _DiscreteVariance   = null;
   private ArrayList<Double>       _closeMA            = new ArrayList<Double>();
   private ArrayList<Double>       _SMAnow             = null;
   private ArrayList<Double>       _SMArecent          = null;
   private ArrayList<Double>       _SMApast            = null;
   private ArrayList<String>       _dividendDate       = new ArrayList<String>();
   private ArrayList<String>       _Date               = new ArrayList<String>();
   private ArrayList<Double>       _Open               = new ArrayList<Double>();
   private ArrayList<Double>       _High               = new ArrayList<Double>();
   private ArrayList<Double>       _Low                = new ArrayList<Double>();
   private ArrayList<Double>       _Close              = new ArrayList<Double>();
   private ArrayList<Double>       _AdjClose           = new ArrayList<Double>();
   private ArrayList<Double>       _dividend           = new ArrayList<Double>();
   private ArrayList<Double>       _Volumes            = new ArrayList<Double>();
   private ArrayList<Double>       _VolumeAverages     = new ArrayList<Double>();
   private ArrayList<Double>       _OpenP              = new ArrayList<Double>();
   private ArrayList<Double>       _OpenSpreadP        = new ArrayList<Double>();
   private ArrayList<Double>       _HighP              = new ArrayList<Double>();
   private ArrayList<Double>       _HighSpreadP        = new ArrayList<Double>();
   private ArrayList<Double>       _LowP               = new ArrayList<Double>();
   private ArrayList<Double>       _LowSpreadP         = new ArrayList<Double>();
   private ArrayList<Double>       _CloseP             = new ArrayList<Double>();
   private ArrayList<Double>       _CloseSpreadP       = new ArrayList<Double>();
   private ArrayList<Double>       _AdjCloseP          = new ArrayList<Double>();
   private ArrayList<Double>       _MonthClose         = new ArrayList<Double>();
   private ArrayList<Double>       _MonthOpen          = new ArrayList<Double>();
   private ArrayList<Double>       _MonthHigh          = new ArrayList<Double>();
   private ArrayList<Double>       _MonthLow           = new ArrayList<Double>();
   private ArrayList<Double>       _closeAverages      = null;
   private ArrayList<Double>       _theFastEMA         = null;
   private ArrayList<Double>       _theSlowEMA         = null;
   private ArrayList<Double>       _theMACD            = null;
   private ArrayList<Double>       _theSignal          = null;
   private ArrayList<Double>       _differenceSum      = null;
   private ArrayList<Double>       _theStochastic      = null;
   private ArrayList<Double>       _theStochEMA        = null;
   private ArrayList<Double>       _theStochDiff       = null;
   private ArrayList<Double>       _dividends          = new ArrayList<Double>();
   private ArrayList<String>       _divExDates         = new ArrayList<String>();
   private StockParameterData      _theParameters      = null;
   private StockParameterData[]    _theParametersArray = null;
   private boolean                 _isIncreasing       = true;
   private boolean                 _isActive           = false;
   private boolean                 _initialized        = true;
   private ArrayList< Double >     _unicon             = null;
   private HashMap<String, Double> _closeHash          = null;

   /**
    * Default Constructor
    */
   public StockElement()
   {
   } // end default constructor

   /**
    * Default Constructor
    */
   public StockElement( String name )
   {
      this();
      _StockName = new String( name.trim() );
   } // end default constructor

   /**
    * Constructor
    */
   public StockElement( String name, String var_sma, String stability_sma )
   {
      this();
      _StockName = new String( name.trim() );
      //_VarianceSMA.add( Double.valueOf( var_sma ) );
      //_StabilitySMA.add( Double.valueOf( stability_sma ) );
   } // end constructor

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void setName( String name )
   {
      _StockName = name;
   }

   /**
    * Method : getName
    */
   public String getName()
   {
      return _StockName;
   } // end getName

   /**
    *
    */
   public int getSize()
   {
      return _Close.size();
   }

   /**
    * Method : getVolume
    */
   public double getVolume()
   {
      int size = 0;

      if( (size = _Close.size()) != 0 )
      {
         _Volume /= size;
      }
/*
*/
      return _Volume;
   } // end getVolume

   /**
    * This method will create a HashMap of dates to closes.
    * Not sure if this really needs to be a class member variable...
    */
   public HashMap<String, Double> getCloseHash()
   {
      if( _closeHash == null )
      {
         _closeHash = new HashMap<String, Double>();
         for( int i=0; i<_Close.size(); i++ ) 
         {
            _closeHash.put( _Date.get( i ), _Close.get( i ) );
         }
      }

      return _closeHash;
   }

   /**
    * Method : getVolumeAverage
    */
   public double getVolumeAverage( int i )
   {
      if( i < _VolumeAverages.size() )
      {
         return ((Double)_VolumeAverages.get( i )).doubleValue();
      }
      return -1;
   } // end getVolume

   /**
    * Method : getVolume
    */
   public ArrayList< Double > getVolumes()
   {
      return _Volumes;
   }

   /**
    * Method : getVolume
    */
   public double getVolume( int i )
   {
      if( _Volumes.size() != 0 && i < _Volumes.size() )
      {
         return ((Double)_Volumes.get( i )).doubleValue();
      }
      return -1;
   } // end getVolume

   /**
    * Method : getLastVolume
    */
   public double getLastVolume()
   {
      return getVolume( _Volumes.size() - 1 );
   }

   /**
    * Method : addVolume
    */
   public void addVolume( String vol )
   {
      double volu = Double.parseDouble( vol );
      _Volumes.add( new Double( volu ) );
/*
      // A_d = A_d-1 + SMA * (M - A_d-1)
      int size = _VolumeAverages.size();
      if( size == 0 )
      {
         _VolumeAverages.add( new Double( volu ) );
      }
      else
      {
         double previous = ((Double)_VolumeAverages.get( size - 1 )).doubleValue();
         _VolumeAverages.add( new Double( previous + 0.5 * ( volu - previous ) ) );
      }
*/

      //_Volume += Double.parseDouble( vol );

   } // end addVolume

   public void addMonthOpen( double value )
   {
      _MonthOpen.add( new Double( value ) );
   }

   public void addMonthClose( double value )
   {
      _MonthClose.add( new Double( value ) );
   }

   public void addMonthHigh( double value )
   {
      _MonthHigh.add( new Double( value ) );
   }

   public void addMonthLow( double value )
   {
      _MonthLow.add( new Double( value ) );
   }

   public double getMonthHigh( int day )
   {
      return ((Double)_MonthHigh.get( day )).doubleValue();
   }

   public double getMonthLow( int day )
   {
      return ((Double)_MonthLow.get( day )).doubleValue();
   }

   public double getMonthOpen( int day )
   {
      return ((Double)_MonthOpen.get( day )).doubleValue();
   }

   public double getMonthClose( int day )
   {
      return ((Double)_MonthClose.get( day )).doubleValue();
   }

   /**
    * Method : getConfidence
    */
   public Double getConfidence()
   {
      return _Confidence;
   } // end getConfidence

   /**
    * Method : getMACD
    */
   public double[] getMACD()
   {
      return _MACDSum;
   } // end getMACD

   /**
    * Method : setConfidence
    */
   public void setConfidence( String conf )
   {
      _Confidence = Double.valueOf( conf );
   } // end setConfidence

   /**
    * Method : setConfidence
    */
   public void setConfidence( double conf )
   {
      _Confidence = new Double( conf );
   } // end setConfidence

   /**
    * Method : getPrevConfidence
    */
   public Double getPrevConfidence()
   {
      return _PrevConfidence;
   } // end getPrevConfidence

   /**
    * Method : setPrevConfidence
    */
   public void setPrevConfidence( String conf )
   {
      _PrevConfidence = Double.valueOf( conf );
   } // end setPrevConfidence

   /**            
    * Method : getVarianceRank
    */
   public int getVarianceRank()
   {
      return _VARRank;
   } // end getVarianceRank

   /**
    * Method : setVarianceRank
    */
   public void setVarianceRank( int rank )
   {
      _VARRank = rank;
   } // end setVarianceRank

   /**
    * Method : getStabilityRank
    */
   public int getStabilityRank()
   {
      return _STABRank;
   } // end getStabilityRank

   /**
    * Method : setStabilityRank
    */
   public void setStabilityRank( int rank )
   {
      _STABRank = rank;
   } // end setStabilityRank

   /**
    * Method : addDate
    */
   public void addDate( String date )
   {
      _Date.add( date );
   } // end addDate

   /**
    * Method : addDate
    */
   public void addDividendDate( String date )
   {
      _dividendDate.add( date );
   }

   /**
    * 
    */
   public void addOpenReal( double open )
   {
      _Open.add( open );
   }

   /**
    * 
    */
   public void addHighReal( double high )
   {
      _High.add( high );
   }

   /**
    * 
    */
   public void addLowReal( double low )
   {
      _Low.add( low );
   }

   /**
    * 
    */
   public void addCloseReal( double close )
   {
      _Close.add( close );
   }

   /**
    * Method : addDividend
    */
   public void addDividend( String dividend )
   {
      _dividends.add( Double.valueOf( dividend ) );
   }

   /**
    * Method : addOpen
    */
   public void addOpen( String open )
   {
      _Open.add( Double.valueOf( open ) );
   } // end addOpen

   /**
    * Method : getLastOpen
    */
   public Double getLastOpen()
   {
      return (Double)_Open.get( _Open.size() - 1 );
   } // end getLastOpen

   /**
    * Method : getDaysAbove
    */
   public int getDaysAbove()
   {
      return _DaysAbove;
   } // end getDaysAbove

   /**
    * Method : addLow
    */
   public void addLow( String low )
   {
      _Low.add( Double.valueOf( low ) );
   }

   /**
    * Method : addHigh
    */
   public void addHigh( String high )
   {
      _High.add( Double.valueOf( high ) );

      // Calculate variance:
      // Since we don't want spikes to pollute the SMA calculations
      // We will cut off all values greater than 2.
/*
      double temp = getLastHigh().doubleValue() - getLastOpen().doubleValue();

      if( temp > _Threshold )
         _DiscreteVariance.add( new Integer( 1 ) );
      else
         _DiscreteVariance.add( new Integer( 0 ) );

      // Calcluate variance mass:
      _RealVariance.add( new Double( temp ) );
      _VarianceMass = new Double( _VarianceMass.doubleValue() + temp );

      // update density:
      if( _StabilityMass.doubleValue() != 0 )
         _Density = new Double( _VarianceMass.doubleValue() / _StabilityMass.doubleValue() );

      if( _Variance.size() == 0 )
      {
         if( temp >= 1 )
         {
            _Variance.add( new Double( 1 ) );
            _DaysAbove += 1;
         } // end if
         else
            _Variance.add( new Double( temp ) );
      }
      else
      {
         if( temp >= 1 )
            _DaysAbove += 1;
         if( temp > 2 )
         {
            _Variance.add( new Double( 2 ) );
         } // end if
         else
            _Variance.add( new Double( temp ) );
      } // end else

      // Calculate variance_sma:
      // A_d = A_d-1 + SMA * (M - A_d-1)
      if( _VarianceSMA.size() == 0 )
      {
         _VarianceSMA.add( getLastVariance() );
         return;
      } // end if
     
      double previous = getLastVarianceSMA().doubleValue();
      previous = previous + _SMAvalue * ( getLastVariance().doubleValue() - previous ); 
      _VarianceSMA.add( new Double( previous ) );
*/
   } // end addHigh

   /**
    * Method : getLastHigh
    */
   public Double getLastHigh()
   {
      return (Double)_High.get( _High.size() - 1 );
   } // end getLastHigh

   /**
    * Method : addClose
    */
   public void addAdjClose( String adjClose )
   {
      _AdjClose.add( Double.valueOf( adjClose ) );
   }

   /**
    * Method : addClose
    */
   public void addClose( String close )
   {
      //double previous = getLastClose().doubleValue();

      _Close.add( Double.valueOf( close ) );

/*
      double current = getLastClose().doubleValue();

      if( current > previous )
      {
         _DiscreteVariance.add( new Double( 1 ) );
      }
      else
      {
         _DiscreteVariance.add( new Double( 0 ) );
      }

      // Calculate stability_sma:
      // A_d = A_d-1 + SMA * (M - A_d-1)
      if( _StabilitySMA.size() == 0 )
      {
         _StabilitySMA.add( getLastClose() );
      } // end if
      else
      {
         previous = getLastStabilitySMA().doubleValue();
         previous = previous + _SMAvalue * ( getLastClose().doubleValue() - previous ); 
         _StabilitySMA.add( new Double( previous ) );
      } // end if

      // Calculate stability_sma:
      // A_d = A_d-1 + SMA * (M - A_d-1)
      if( _SMAnow.size() == 0 )
      {
         _SMAnow.add( getLastClose() );
      } // end if
      else
      {
         previous = getLastSMAnow().doubleValue();
         previous = previous + 0.9 * ( getLastClose().doubleValue() - previous ); 
         _SMAnow.add( new Double( previous ) );
      } // end if

      // Calculate stability_sma:
      // A_d = A_d-1 + SMA * (M - A_d-1)
      if( _SMArecent.size() == 0 )
      {
         _SMArecent.add( getLastClose() );
      } // end if
      else
      {
         previous = getLastSMArecent().doubleValue();
         previous = previous + 0.4 * ( getLastClose().doubleValue() - previous ); 
         _SMArecent.add( new Double( previous ) );
      } // end if

      // Calculate stability_sma:
      // A_d = A_d-1 + SMA * (M - A_d-1)
      if( _SMApast.size() == 0 )
      {
         _SMApast.add( getLastClose() );
      } // end if
      else
      {
         previous = getLastSMApast().doubleValue();
         previous = previous + 0.01 * ( getLastClose().doubleValue() - previous ); 
         _SMApast.add( new Double( previous ) );
      } // end if
*/

      // Calculate stability:
      // Since we don't want spikes to pollute the SMA calculations
      // We will cut off all values greater than 3.
/*
      double temp = getLastClose().doubleValue() - getLastOpen().doubleValue();
      double temp2 = temp;

      // Calclulate absolute values of stability:
      if( temp2 < 0 )
         temp2 *= -1;
      _RealStability.add( new Double( temp2 ) );

      // Calculate stability mass:
      _StabilityMass = new Double( _StabilityMass.doubleValue() + temp2 );

      // update density:
      if( _StabilityMass.doubleValue() != 0 )
         _Density = new Double( _VarianceMass.doubleValue() / _StabilityMass.doubleValue() );

      if( _Stability.size() == 0 )
      {
         if( temp > 1 )
            _Stability.add( new Double( 1 ) );
         else
            _Stability.add( new Double( temp ) );
      }
      else
      {
         if( temp > 2 )
            _Stability.add( new Double( 2 ) );
         else
            _Stability.add( new Double( temp ) );
      } // end else

*/
   } // end addClose

   /**
    * Method : getLastClose
    */
   public Double getLastClose()
   {
      Double last = new Double( 0 );

      if( _Close.size() > 0 )
      {
         last = (Double)_Close.get( _Close.size() - 1 );
      }

      return last;
   } // end getLastClose

   /**
    * Method : 
    */
   public Double getLastDividend()
   {
      Double last = new Double( 0 );

      if( _dividends.size() > 0 )
      {
         last = (Double)_dividends.get( _dividends.size() - 1 );
      }

      return last;
   }

   /**
    * Method : 
    */
   public String getLastDividendDate()
   {
      String last = "";

      if( _dividendDate.size() > 0 )
      {
         last = (String)_dividendDate.get( _dividendDate.size() - 1 );
      }

      return last;
   }

   /**
    * Method : getVariance
    */
/*
   public ArrayList getVariance()
   {
      return _Variance;
   } // end getVariance
*/

   /**
    * Method : getStability
    */
   public ArrayList getStability()
   {
      //return _Stability;
      return null;
   } // end getStability

   /**
    * Method : getLastVariance
    */
/*
   public Double getLastVariance()
   {
      return (Double)_Variance.get( _Variance.size() - 1 );
   } // end getLastVariance
*/

   /**
    * Method : getLastVarianceSMA
    */
/*
   public Double getLastVarianceSMA()
   {
      return (Double)_VarianceSMA.get( _VarianceSMA.size() - 1 );
   } // end getLastVarianceSMA
*/

   /**
    * Method : getLastStability
    */
/*
   public Double getLastStability()
   {
      return (Double)_Stability.get( _Stability.size() - 1 );
   } // end getLastVariance
*/

   /**
    * Method : getLastStabilitySMA
   public Double getLastStabilitySMA()
   {
      return (Double)_StabilitySMA.get( _StabilitySMA.size() - 1 );
   } // end getLastStabilitySMA
    */

   /**
    * Method : getLastStabilitySMA
    */
   public Double getLastSMAnow()
   {
      return (Double)_SMAnow.get( _SMAnow.size() - 1 );
   } 

   /**
    * Method : getLastStabilitySMA
    */
   public Double getLastSMArecent()
   {
      return (Double)_SMArecent.get( _SMArecent.size() - 1 );
   } 

   /**
    * Method : getLastStabilitySMA
    */
   public Double getLastSMApast()
   {
      return (Double)_SMApast.get( _SMApast.size() - 1 );
   } 

   /**
    * Method : getDensity
    */
   public Double getDensity()
   {
      //return _Density;
      return new Double( 0 );
   } // end getDensity

   /**
    * Method : getVarianceMass
    */
   public Double getVarianceMass()
   {
      //return _VarianceMass;
      return new Double( 0 );
   } // end getVarianceMass

   /**
    * Method : getStabilityMass
    */
   public Double getStabilityMass()
   {
      return _StabilityMass;
      //return new Double( 0 );
   } // end getStabilityMass

   /**
    * Method : getBestDepth
    */
   public int getBestDepth()
   {
      return _BestDepth;
   } // end getBestDepth

   /**
    * Method : getTotalLoss
    */
   public double getTotalLoss()
   {
      return _TotalLoss;
   } // end getTotalLoss

   /**
    * Method : getTotalLoss
    */
   public ArrayList<Double> getAverages()
   {
      return _closeAverages;
   }

   /**
    * Method : toDataString
    */
   public String toDataString()
   {
      StringBuffer str = new StringBuffer( _StockName ).append( "," );
      str.append( _entropy ).append( "," ); 
      str.append( _entropy1 ).append( "," ); 
      str.append( _entropy2 ).append( "," );  
      str.append( _entropy3 ).append( "," ); 
      str.append( _discreteEntropy ).append( "," );  
      str.append( _discreteEntropy1 ).append( "," ); 
      str.append( _discreteEntropy2 ).append( "," );  
      str.append( _discreteEntropy3 ).append( "," ); 
      str.append( _proportionEntropy ).append( "," );  
      str.append( _proportionEntropy1 ).append( "," ); 
      str.append( _proportionEntropy2 ).append( "," );  
      str.append( _proportionEntropy3 );

   //private double    _TotalLoss         = 0;
   //private double    _StandardDeviation = 0;
/*
      StringBuffer str = new StringBuffer( _StockName ).append( "\n" );
      str.append( _SMAvalue ).append( "\n" );
      str.append( _BestConseq ).append( "\n" );
      str.append( _BestDepth ).append( "\n" );
      str.append( _Threshold ).append( "\n" );
      str.append( _Confidence ).append( "\n" );
      //str.append( getLastVarianceSMA() ).append( "\n" );
      //str.append( _Density ).append( "\n" );
      str.append( _Worth ).append( "\n" );
      str.append( _RealWorth ).append( "\n" );
      str.append( _StabilityMass ).append( "\n" );
      str.append( _TotalLoss ).append( "\n" );
      str.append( _HighWorth ).append( " / " ).append( _LastBuy ).append( " / " ).append( _Open.size() );
      str.append( "\n" );
      str.append( _DaysAbove ).append( "\n" );
      str.append( _FastStochastic ).append( "\n" );
*/

      return str.toString();
   } // end toDataString

   /**
    * Method : toString
    */
   public String toString()
   {
/*
      StringBuffer str = new StringBuffer( "Stock Name:         " + _StockName ).append( "\n" );
      str.append( "SMA value is:       " + _SMAvalue ).append( "\n" );
      str.append( "Best Conseq is:     " ).append( _BestConseq ).append( "\n" );
      str.append( "Best Depth is :     " ).append( _BestDepth ).append( "\n" );
      str.append( "Worth is:           " ).append( _Worth ).append( "\n" );
      str.append( "Real Worth is:      " ).append( _RealWorth ).append( "\n" );
      str.append( "SMA  Worth is:      " ).append( _StabilityMass ).append( "\n" );
      str.append( "Threshold is:       " ).append( _Threshold ).append( "\n" );
      str.append( "IsActive is:        " ).append( _isActive ).append( "\n" );
      str.append( "Total Loss is:      " ).append( _TotalLoss ).append( "\n" );
      str.append( "High Worth is:      " ).append( _HighWorth ).append( " / " ).append( _LastBuy ).append( " / " ).append( _Open.size() ).append( "\n" );
      str.append( "total entropy:      " ).append( _entropy ).append( "\n" );
      str.append( "first entropy:      " ).append( _entropy1 ).append( "\n" );
      str.append( "second entropy:     " ).append( _entropy2 ).append( "\n" );
      str.append( "third entropy:      " ).append( _entropy3 ).append( "\n" );
      str.append( "total discrete entropy:    " ).append( _discreteEntropy ).append( "\n" );
      str.append( "first discrete entropy:    " ).append( _discreteEntropy1 ).append( "\n" );
      str.append( "second discrete entropy:   " ).append( _discreteEntropy2 ).append( "\n" );
      str.append( "third discrete entropy:    " ).append( _discreteEntropy3 ).append( "\n" );
      str.append( "total proportion entropy:  " ).append( _proportionEntropy ).append( "\n" );
      str.append( "first proportion entropy:  " ).append( _proportionEntropy1 ).append( "\n" );
      str.append( "second proportion entropy: " ).append( _proportionEntropy2 ).append( "\n" );
      str.append( "third proportion entropy:  " ).append( _proportionEntropy3 ).append( "\n" );

      if( _ScaleClose != null )
      for( int i=0; i<_ScaleClose.length; i++ )
         str.append( "ScaleClose:            " ).append( _ScaleClose[i] ).append( "\n" );

      if( _ScaleOpen != null )
      for( int i=0; i<_ScaleOpen.length; i++ )
         str.append( "ScaleOpen:            " ).append( _ScaleOpen[i] ).append( "\n" );

      if( _Simulations != null )
      for( int i=0; i<_Simulations.length; i++ )
         str.append( "Simulations:          " ).append( _Simulations[i] ).append( "\n" );

      if( _theParametersArray != null )
      for( int i=0; i<_theParametersArray.length; i++ )
         str.append( _theParametersArray[i].toString() ).append( "\n" );
      for( int i=0; i<_MACDSum.length; i++ )
         str.append( "MACDSum:            " ).append( _MACDSum[i] ).append( "\n" );
      for( int i=0; i<_MACDDiffSum.length; i++ )
         str.append( "MACDDiffSum:            " ).append( _MACDDiffSum[i] ).append( "\n" );
      for( int i=0; i<_StochasticSum.length; i++ )
         str.append( "StochasticSum:            " ).append( _StochasticSum[i] ).append( "\n" );
      for( int i=0; i<_StochasticDiffSum.length; i++ )
         str.append( "StochasticDiffSum:            " ).append( _StochasticDiffSum[i] ).append( "\n" );
System.out.println( "_LastBuy is: " + _LastBuy + " window is: " + _window + " size is: " + getSize() ); 

      int half = getSize() / 2;
      str.append( " " + _Threshold );
      str.append( " " + _SMAvalue );
      str.append( " " + getClose( _LastBuy ) );
      str.append( " " + getClose( _window ) );
      //str.append( " " + getClose( half ) );
      //str.append( " " + getLastClose() );
*/

      StringBuffer str = new StringBuffer( getName() + "\n" );

      for( int i=0; i<_Date.size(); i++ )
      {
         str.append( _Date.get( i ) + " " + _Open.get( i ) + " " + _Close.get( i ) + " " + _CloseP.get( i ) + "\n" );
      }

      return str.toString();
   } // end toString

   /**
    * Method : toArray
    */
   public String[] toArray()
   {
/*
      String[] theArray = new String[ 17 + 4 * _VarianceSMA.size() ];
      int index = 0;

      String str = new String( "Stock Name:         " + _StockName );
      theArray[index++] = str.toString();
      str = new String( "Confidence rank is: " + _Confidence );
      theArray[index++] = str.toString();
      str = new String( "Variance rank is:   " + _VARRank );
      theArray[index++] = str.toString();
      str = new String( "Stability rank is:  " + _STABRank );
      theArray[index++] = str.toString();
      str = new String( "SMA value is:       " + _SMAvalue );
      theArray[index++] = str.toString();
      str = new String( "Variance is:        " );
      theArray[index++] = str.toString();
      for( int i=0; i<_Variance.size(); i++ )
      {
         theArray[index++] = new String( "                    " + _Variance.get( i ) );
      } // end for
      str = new String( "Stability is:       " );
      theArray[index++] = str.toString();
      for( int i=0; i<_Stability.size(); i++ )
      {
         theArray[index++] = new String( "                    " + _Stability.get( i ) );
      } // end for
      str = new String( "VarianceSMA is:     " );
      theArray[index++] = str.toString();
      for( int i=0; i<_VarianceSMA.size(); i++ )
      {
         theArray[index++] = new String( "                    " + _VarianceSMA.get( i ) );
      } // end for
      str = new String( "StabilitySMA is:    " );
      theArray[index++] = str.toString();
      for( int i=0; i<_StabilitySMA.size(); i++ )
      {
         theArray[index++] = new String( "                    " + _StabilitySMA.get( i ) );
      } // end for
      //str = new String( "VarianceMass is: " + _VarianceMass );
      //theArray[index++] = str.toString();
      str = new String( "StabilityMass is: " + _StabilityMass );
      theArray[index++] = str.toString();
      //str = new String( "Density is: " + _Density );
      //theArray[index++] = str.toString();
      str = new String( "Open is: " + _Open );
      theArray[index++] = str.toString();
      str = new String( "High is: " + _High );
      theArray[index++] = str.toString();
      str = new String( "Close is: " + _Close );
      theArray[index++] = str.toString();
      str = new String( "Days are: " );

      for( int i=0; i<_ListOfDaysAbove.size(); i++ )
      {
         str = str + " " + ((Integer)_ListOfDaysAbove.get( i )).toString();
      } // end for
      theArray[index++] = str.toString();
      str = new String( "Is Increasing is: " + _isIncreasing );
      theArray[index++] = str.toString();
                                       
      return theArray;
*/
      return null;
   } // end toArray

   /**          
    *
    */
   public void setCompareToThreshold()
   {
      _compareType = 0;
   }

   /**          
    *
    */
   public void setCompareToSMAvalue()
   {
      _compareType = 1;
   }

   /**          
    * Method : compareTo
    *
    * Compares this object with the specified object for order. 
    * Returns a negative integer, zero, or a positive integer as 
    * this object is less than, equal to, or greater than the specified object.
    */
   public int compareTo( StockElement o )
   {
      switch( _compareType )
      {
         // compare by threshold
         case 0 :
            if( getThreshold() < o.getThreshold() )
            {
               return -1;
            }
            else if( getThreshold() > o.getThreshold() )
            {
               return 1;
            }
            else if( getThreshold() == o.getThreshold() )
            {
               return 0;
            }
         break;

         // compare by SMA
         case 1 :
            if( getSMAvalue() < o.getSMAvalue() )
            {
               return -1;
            }
            else if( getSMAvalue() > o.getSMAvalue() )
            {
               return 1;
            }
            else if( getSMAvalue() == o.getSMAvalue() )
            {
               return 0;
            }
         break;

         default :
         break;
      }

      return 1;
   } // end comprareTo

   /**          
    * Method : addDayAbove
    *        : 
    */
   public void addDayAbove( String numDays )
   {
      //_ListOfDaysAbove.add( new Integer( Integer.parseInt( numDays ) ) );
   } // end addDaysAbove

   public void setLastDay()
   {
/*
      addDayAbove( Integer.toString( _DaysAbove ) );
      for( int i=1; i<_ListOfDaysAbove.size(); i++ )
      {
         int prev = ((Integer)_ListOfDaysAbove.get( i - 1 )).intValue();
         int curr = ((Integer)_ListOfDaysAbove.get( i )).intValue();
         if( curr - prev < 0 )
         {
            _isIncreasing = false;
            break;
         } // end if not increasing
      } // end for
*/
   } // end setLastDay
   
/*
   public ArrayList getListOfDaysAbove()
   {
      return _ListOfDaysAbove;
   } // end getListOfDaysAbove
*/

   /**
    *
    */
   public boolean isIncreasing()
   {
      return _isIncreasing;
   } // end isIncreasing

   /**
    *
    */
   public double getMaxClose( int start, int end )
   {
      double max = -1000000;
      double temp = 0;
 
      for( int i=start; i<end; i++ )
      {
         temp = (double)_Close.get( i );
         if( temp > max )
         {
            max = temp;
         }
      }

      return max;
   }

   /**
    *
    */
   public int getIndexOfDate( String date )
   {
      int index = -1;
      for( int i=0; i<getSize(); i++ )
      {
         if( getDate( index ).equals( date ) )
         {
            index = i;
            break;
         }
      }
      return index;
   }

   /**
    *
    */
   protected double performNormalBuy( double amount, int start, int end )
   {
      double open = _Close.get( start );
      double close = _Close.get( end );
      int numberOfShares = (int)(amount / open);
      double result = ((double)(close * numberOfShares));

      return (result - 10);
   }

   /**
    *
    */
   protected double performBuy( double amount, double entry, double exit )
   {
      int numberOfShares = (int)(amount / entry);
      double bought = (double)(entry * numberOfShares);
      double result = ((double)(exit * numberOfShares)) - bought;
      return (result - 10);
   }

   /**
    *
    */
   protected double performBuy( double amount, int start, int end )
   {
      if( end >= _Close.size() )
      {
         return 0;
      }
      double open = _Close.get( start );
      double close = _Close.get( end );
/*
      double max = getMaxClose( start, end );
      double diff = max / open;
      if( diff >= 2 )
      {
         close = 2 * open;
      }
*/
      int numberOfShares = (int)(amount / open);
      double bought = (double)(open * numberOfShares);
      double result = ((double)(close * numberOfShares)) - bought;

      return (result - 10);
   }

   /**
    *
    */
   protected double performBuySimulation( int current, int previous )
   {
      double theBestResult = -2000000;
      double theResult = 0;
      double theWorth  = 0;
      double temp      = 0;
      double temp2     = 0;
      double bestSmaValue  = 0;
      double smaWorth  = 0;
      double dayTotal  = 0;
      double totalLoss = 0;
      double highWorth = 0;
      int numGood      = 0;
      int numBad       = 0;
      int currSum      = 0;
      int HighWorth    = 0;
      int lastBuy      = 0;
      _SMAvalue        = 0;
      _HighWorth       = 0;

      for( int k=current; k<5; k++ )
      {
         int[] discreteSum = getSummedDiscreteVariance( k );

         for( int i=current; i<discreteSum.length; i++ )
         {
/*
            if( discreteSum[i] == current && 
                discreteSum[i-1] == previous )
            {
               temp = ((Double)_Open.get( i )).doubleValue();
               currSum = (int)(_Cash / temp);
               temp2 = currSum * temp;
               temp2 += 5;
               if( ((Integer)_DiscreteVariance.get( i )).intValue() == 1 )
               {
                  temp += _Threshold;
               } // end if 
               else
               {
                  temp = ((Double)_Close.get( i )).doubleValue();
               } // end if 
               temp *= currSum;
               temp -= 5;

               dayTotal = temp - temp2;

               theWorth += dayTotal;
               smaWorth += 0.3 * (theWorth - smaWorth);

               if( theWorth > highWorth )
               {
                  highWorth = theWorth;
                  _HighWorth = i;
               } // end if high

               if( _useWeighted == true )
               {
                  theResult += ( i * (temp - temp2) );
                  if( dayTotal < 0 )
                  {
                     totalLoss += (i * dayTotal);
                     numBad += 1;
                  } // end if dayTotal negative
                  else
                  {
                     numGood += 1;
                  } // end if dayTotal negative
               }
               else
               {
                  theResult += dayTotal;
                  if( dayTotal < 0 )
                  {
                     totalLoss += dayTotal;
                     numBad += 1;
                  } // end if dayTotal negative
                  else
                  {
                     numGood += 1;
                  } // end if dayTotal negative
              } // end else if weighted

               if( (numGood + numBad ) == 0 )
               {
                  _SMAvalue = 1;
               }
               else
                  _SMAvalue = (double)( (double)numGood / (double)(numGood + numBad) );

               //_SMAvalue += 0.7 * (sma - _SMAvalue);
               //_SMAvalue += i * sma;

               //lastBuy = i;
               lastBuy++;

            } // end if good to buy
*/

         } // end i for
 
         if( theResult > theBestResult )
         {
            theBestResult = theResult;
            _BestDepth = k;
            bestSmaValue = _SMAvalue;
            _RealWorth = theWorth;
            _StabilityMass = new Double( smaWorth );
            _TotalLoss = totalLoss;
            HighWorth = _HighWorth;
            _LastBuy = lastBuy;
         } // end if best

         theResult = 0;
         theWorth  = 0;
         smaWorth  = 0;
         numGood   = 0;
         numBad    = 0;
         lastBuy   = 0;
         _SMAvalue = 0;
         totalLoss = 0;
         highWorth = 0;
         _HighWorth = 0;
      } // end k for

      //theResult = theBestResult;
      //_SMAvalue = bestSmaValue;
      //_HighWorth = HighWorth;

      return theResult;
   } // end performBuySimulation

   /**
    * 
    *
    */
   protected void performStochasticBuy()
   {
      calculateDifferences();
      calculateAverageDifferences();

      _Worth = 0;

      for( int i=_NumberOfSamples; i<_Open.size(); i++ )
      {
         _Worth += trade( i, _Simulations[i - _NumberOfSamples] * 0.5 );
      }
      System.out.println( _StockName + " " + _Worth );
   }

   /**
    * 
    *
    */
   protected double performWeeklyBuy( int beginDay )
   {
      double open = 0;
      double close = 0;
      double value = 0;
      _LastBuy = 0;
      _DaysAbove = 0;

      if( _window == 0 )
      {
         return 0;
      }

      for( int i=beginDay; i<(_Volumes.size() - _window); i++ )
      {
         //_DaysAbove++;
         // Check for a significant event day:
         if( _eventValues[i] > _Threshold && (getClose(i) / getClose(i-1) > 1) )
         {
            open = (getClose( i ) * _lowestMean);
            if( open < getLow( i + 1 ) )
            {
               open = getClose( i + 1 );
            }

            // find the max within the window:
            double max = -2000000;
            double temp = 0;

            // try changing the window to the best day value:
            for( int m=(i + 2); m<(i + _window); m++ )
            {
               temp = getHigh( m );
               if( temp > max )
               {
                  max = temp;
               }
            } // end find max loop

            close = (getClose( i ) * (_highestMean + _bestHighVariance) );
            if( close > max )
            {
               close = getClose( i + _window );
            }

            value += trade( open, close );
            _LastBuy++;
            //_counts[i]++;
         }
      }
      //System.out.println( getName() + "," + value );
      return value;
   }

   /**
    * Method calculateAppEn
    *
    */
   private double calculateAppEn( ArrayList list, int seqSize, double radius )
   {
      double result = 0;
      double[] theArray = new double[ list.size() ];

      for( int i=0; i<list.size(); i++ )
      {
         theArray[i] = ((Double)list.get( i )).doubleValue();
         //System.out.println( "The list item: " + theArray[i] );
      }

      ApproximateEntropy theAppEn = new ApproximateEntropy( theArray, seqSize, radius );
      // overall entropy of entire series
      result = theAppEn.getEntropy();

      return result;
   }

   /**
    * Method calculateAppEn
    *
   private double[] calculateAppEn( ArrayList list, int seqSize, double radius )
   {
      double[] returnArray = new double[4];
      double[] theArray = new double[ list.size() ];
      for( int i=0; i<list.size(); i++ )
      {
         theArray[i] = ((Double)list.get( i )).doubleValue();
      }

      ApproximateEntropy theAppEn = new ApproximateEntropy( theArray, seqSize, radius );
      // overall entropy of entire series
      returnArray[0] = theAppEn.getEntropy();

      int step = (int)(list.size() / 3);
      theArray = new double[ step ];
      for( int i=0; i<step; i++ )
      {
         theArray[i] = ((Double)list.get( i )).doubleValue();
      }
      theAppEn = new ApproximateEntropy( theArray, seqSize, radius );
      // first seg entropy
      returnArray[1] = theAppEn.getEntropy();

      int pos = 0;
      for( int i=step; i<(step + step); i++ )
      {
         theArray[pos++] = ((Double)list.get( i )).doubleValue();
      }
      theAppEn = new ApproximateEntropy( theArray, seqSize, radius );
      // second seg entropy
      returnArray[2] = theAppEn.getEntropy();

      pos = 0;
      theArray = new double[ list.size() - step + 1 ];
      for( int i=(step + step); i<list.size(); i++ )
      {
         theArray[pos++] = ((Double)list.get( i )).doubleValue();
      }
      theAppEn = new ApproximateEntropy( theArray, seqSize, radius );
      // third seg entropy
      returnArray[3] = theAppEn.getEntropy();

      return returnArray;
   }
    */

   /**
    * Method calculateAppEnValues
    *
   private void calculateAppEnValues()
   {
      double[] array = calculateAppEn( _Close, 5, 1 );
      _entropy = array[0];
      _entropy1 = array[1];
      _entropy2 = array[2];
      _entropy3 = array[3];
       
      ArrayList list = new ArrayList();
      for( int i=0; i<_DiscreteVariance.size(); i++ )
      {
         list.add( new Double( ((Integer)_DiscreteVariance.get(i)).intValue() ) );
      }
      array = calculateAppEn( list, 5, 0 );
      _discreteEntropy  = array[0];
      _discreteEntropy1 = array[1];
      _discreteEntropy2 = array[2];
      _discreteEntropy3 = array[3];

      list = new ArrayList();
      double current = 0;
      double previous = 0;
      for( int i=1; i<_Close.size(); i++ )
      {
         current = ((Double)_Close.get(i)).doubleValue();
         previous = ((Double)_Close.get(i-1)).doubleValue();
         list.add( new Double( current / previous ) );
      }
      array = calculateAppEn( list, 5, 0.1 );
      _proportionEntropy  = array[0];
      _proportionEntropy1 = array[1];
      _proportionEntropy2 = array[2];
      _proportionEntropy3 = array[3];
   }
    */

   /**
    * Method performBuySimulation
    *
    */
   public void performBuySimulation()
   {
      ArrayList entropyList = new ArrayList();
      ArrayList discreteList = new ArrayList();
      double entropy = 0;
      double discEntropy = 0;
      double threshold = (double)_Close.size() * 0.5;
      double buyAmount = 0;
      double buyClose = 0;
      double high = 0;
      int buyCount = 0;
      boolean inBuy = false;

      for( int i=0; i<_Close.size(); i++ )
      {
            double close = ((Double)_Close.get( i )).doubleValue();
            System.out.println( _StockName + " " + close + " " + " " + _SMAnow.get( i ) +
                                " " + _SMArecent.get( i ) + " " + _SMApast.get( i ) );
/*
         entropyList.add( _Close.get( i ) );
         discreteList.add( _DiscreteVariance.get( i ) );

         if( i > threshold )
         {
            //entropy = calculateAppEn( entropyList, 5, 1 );
            //discEntropy = calculateAppEn( discreteList, 5, 0 );
            double close = ((Double)_Close.get( i )).doubleValue();
   
            
            double current = ((Double)_StabilitySMA.get( i )).doubleValue();
            double previous = ((Double)_StabilitySMA.get( i - 1 )).doubleValue();
            double previous2 = ((Double)_StabilitySMA.get( i - 2 )).doubleValue();

            // buy situation:
            if( current > previous && previous < previous2 )
            {
               buyCount = (int)(_Cash / close);
               buyAmount = buyCount * close;
               System.out.println( _StockName + " " + close + " " + _StabilitySMA.get( i ) + " BUY" );
               inBuy = true;
               buyClose = close;
            }
            // sell situation:
            else if( inBuy && (current < previous && previous > previous2 ||
                     close >= buyClose * 1.03 ) )
            {
               _Worth += ((buyCount * close) - buyAmount - 10);
               System.out.println( _StockName + " " + close + " " + _StabilitySMA.get( i ) + " SELL " + high );
               inBuy = false;
               high = 0;
            }
            else
            {
               System.out.println( _StockName + " " + close + " " + _StabilitySMA.get( i ));
            }

            if( buyClose != 0 )
            {
               current = close / buyClose;
            }

            if( inBuy && current > high )
            {
               high = current;
            }
         }
*/
      }
   }

   /**
    *
    */
   public ArrayList<Double> getSubSet( ArrayList<Double> data, int begin, int end )
   {
      ArrayList<Double> temp = new ArrayList<Double>();

      for( int i=begin; i<end; i++ )
      {
//System.out.println( "adding: " + data.get( i ) );
         temp.add( data.get( i ) );
      }

      return temp;
   }

   /**
    *
    */
   public ArrayList<Double> mapIntoZeroOne( ArrayList<Double> data )
   {
      ArrayList<Double> temp = new ArrayList<Double>();
      ArrayList<Double> mapped = new ArrayList<Double>();

      double min = Collections.min( data );

      for( double value : data )
      {
         temp.add( (value - min) );
      }

      double max = Collections.max( temp );

      for( double value : temp )
      {
         double temp1 = value / max;
 //System.out.println( getName() + " " + temp1 );
         mapped.add( temp1 );
      }

      return mapped;
   }

   /**
    * Method performLinearModel
    * This will more acurately generate the slope of the data set.
    * After this call is complete, _Threshold contains the slope
    * while _SMAvalue contains the error.
    *
    */
   public void performLinearModel( int begin, int end )
   {
      //System.out.println( _StockName + " in linear model with begin and end: " + begin + " " + end + " size: " + _Close.size() );
      ArrayList<Double> data = getSubSet( _Close, begin, end );
      ArrayList<Double> mapped = mapIntoZeroOne( data );
      int length = mapped.size();
      double maxSlope = (double)1 / (double)length;
      double stepValue = 0.001;
      double currentSlope = -1 * maxSlope;
      double currentError = 0;
      double bestError = 1000000000;
      double value = 0;

      while( currentSlope <= maxSlope )
      {
         currentError = 0;

         for( int i=0; i<length; i++ )
         {
            // y = m * x + b
            value = ( currentSlope * (double)i ) + 0.5;
            currentError += Math.abs( value - mapped.get( i ) );
         }

         if( currentError < bestError )
         {
            _Threshold = currentSlope;
            _SMAvalue = currentError;
            bestError = currentError;
         }

         currentSlope += stepValue;
      }

   }

   /**
    * Method calculateLinearModel
    *
    */
   public void calculateLinearModel( int begin, int end )
   {
      ArrayList<Double> mapped = mapIntoZeroOne( _Close );

      double bestX = -1;
      double bestY = -1;
      double bestError = 100000;
      double error = 0;
      double x = 0;
      double y = 0;
      double step = 0.05;
      double b = 0;
      double m = 0;
      double value = 0;
      int half = (int)((double)mapped.size() / (double)2);

      if( begin < half )
      {
         end = half;
      }

      while( x <= 1 )
      {
         while( y <= 1 )
         {
            error = 0;
            m = (y - x) / half;
            b = y - (m * x);

            for( int i=begin; i<end; i++ )
            {
               value = (m * i) + b;
               error += Math.abs( value - mapped.get( i ) );
            }

            if( error < bestError )
            {
               bestError = error;
               bestX = x;
               bestY = y;
            }
      
            y += step;
         }
         x += step;
      }

      _Threshold = (bestY - bestX) / half;

/*
      if( begin != 0 )
      {
         m = _Threshold;
         b = bestY - (m * bestX);
         for( int i=begin; i<end; i++ )
         {
            value = (m * i) + b;
            System.out.println( getName() + " " + value + " " + mapped.get( i ) + " " + _Close.get( i ) );
         }
      }
*/

   }

   /**
    * Method calculateDifference
    * All this does is print out the difference between the open and close
    */
   public void calculateDifference( int begin, int end )
   {
      double open = _Close.get( begin );
      double close = _Close.get( end );
      double difference = close - open;
   }

   /**
    * Method calculateLinearValues
    *
    */
   public void calculateLinearValues( int step, int size )
   {
      int end = step + size;
      if( end >= getSize() )
      {
         end = getSize() - 1;
      }

      performLinearModel( step, end );

      _LastBuy = end;
      _window = end + size;
      if( _window >= getSize() )
      {
         _window = getSize() - 1;
      }
   }

   /**
    * Method calculateLValues
    *
    */
   public void calculateLValues()
   {
      //System.out.println( "in LValues..." );
      int half = getSize() / 2;
      //calculateLinearModel( half, getSize() );
      performLinearModel( half, getSize() );
      _LastBuy = half;
      _window = getSize() - 1;
   }

   /**
    * Method calculateCValues
    *
    */
   public void calculateCValues()
   {

      //System.out.println( "Stock name is: " + getName() );
      //System.out.println( "in CValues..." );

      int half = getSize() / 2;
      performLinearModel( 0, half );
      _LastBuy = 0;
      _window = half;
      //calculateLinearModel( 0, half );
      //calculateAppEnValues();
/*
      performBuySimulation();
      if( _counts == null )
      {
         _counts = new int[ _Volumes.size() ];
      }
      // comment out as needed:
      //calculateMACDValues();
      //calculateTotalValues();
      //calculateStatistics();
      //System.out.println( "The size is: " + _DaysAbove );
      int size = ((2 * _Volumes.size()) / 3 );
      calculateEventStatistics( size );

System.out.println( _StockName + "," + 
                    _bestEvents + "," + 
                    _highestMean + "," + 
                    _lowestMean + "," + 
                    _Threshold + "," + 
                    _window + "," + 
                    _bestDayHighMean + "," +
                    _bestHighVariance + "," +
                    _entropy + "," +
                    _entropy1 + "," +
                    _entropy2 + "," +
                    _entropy3 + "," +
                    _discreteEntropy + "," +
                    _discreteEntropy1 + "," +
                    _discreteEntropy2 + "," +
                    _discreteEntropy3 + "," +
      performWeeklyBuy( size ) + "," + _LastBuy );
      for( int i=0; i<_counts.length; i++ )
      {
         System.out.println( i + "," + _counts[i] );
      }
      //calculateCallStatistics();
      //performStochasticBuy();
      //performAnnualHighBuy();
      //performOpenGapBuy();
      //performMonthlyBuy();
      if( calculateActiveStatistics() == 1 )
      {
         System.out.println( _StockName + " is at high." );
      }

for( int i=0; i<_TotalDiffs.length;i++)
{
   System.out.println( "i: " + i + " " + _TotalDiffs[i] );
}

      //System.out.println( "The name is(after): " + _StockName );

      calculateScales();

      if( _isActive &&
          _Worth >= 0 &&
          _SMAvalue > 0.8 )
      {
         _isActive = true;
      }
      else
      {
         _isActive = false;
      }

      _Worth            = -2000000;
      double theWorth   = -2000000;
      double temp       = 0;
      double currSum    = 0;
      double prevSum    = 0;
      double sma        = 0;
      double smaWorth   = 0;
      double bestThresh = 0;
      double totalLoss  = 0;
      double fast       = 0;
      double slow       = 0;
      double signal     = 0;
      int    bestDepth  = 0;
      int    highWorth  = 0;
      int    lastBuy    = 0;

      if( _DiscreteVariance.size() < 10 )
      {
         _Worth = 0;
         return;
      } // end if 

      while( _Threshold < 2.5 )
      {
         for( int i=0; i<5; i++ )
         {
            temp = performBuySimulation( i + 1, i );
               
            if( temp > _Worth )
            {
               _Worth = temp;
               _BestConseq = i + 1;
               sma  = _SMAvalue;
               smaWorth  = _StabilityMass.doubleValue();
               bestDepth = _BestDepth;
               bestThresh = _Threshold;
               theWorth = _RealWorth;
               totalLoss = _TotalLoss;
               highWorth = _HighWorth;
               lastBuy = _LastBuy;
            } // end if best

         } // end for loop
         _Threshold += 0.2;
         recalcDiscreteVariance();
      } // end while

      _BestDepth = bestDepth;
      _Threshold = bestThresh;
      _RealWorth = theWorth;
      _StabilityMass = new Double( smaWorth );

      if( totalLoss != 0 )
      {
         _TotalLoss = -1 * (_Worth / totalLoss);
      }
      else
      {
         _TotalLoss = -100;
      }

      if( _TotalLoss == Double.NEGATIVE_INFINITY ||
          _TotalLoss == Double.POSITIVE_INFINITY ) 
      {
         _TotalLoss = 500;
      } // end if infinite
      _HighWorth = highWorth;
      _LastBuy = lastBuy;
      _SMAvalue = sma;

      if( _Worth < 100 )
         return;

      recalcDiscreteVariance();

      int s = _DiscreteVariance.size() - 1;
      for( int j=s; j>(s-_BestDepth); j-- )
      {
         currSum += ((Integer)_DiscreteVariance.get( j )).intValue();
      } // end current for

      if( currSum != _BestConseq )
         return;

      for( int j=s-1; j>(s-_BestDepth-1); j-- )
      {
         prevSum += ((Integer)_DiscreteVariance.get( j )).intValue();
      } // end current for

      if( prevSum == (_BestConseq - 1) && 
        (_RealWorth > 400 && _TotalLoss >= 1) )
      {
         _isActive = true;
      } // end if
*/

   } // end calculateCValues

   public int getOpenSize()
   {
      return _Open.size();
   }

   public double getWorth()
   {
      return _Worth;
   } // end getWorth

   public double getRealWorth()
   {
      return _RealWorth;
   } // end getWorth

   public int getBestConseq()
   {
      return _BestConseq;
   } // end getBestConseq

   public boolean isActive()
   {
      return _isActive;
   } // end isActive

   public double getSMAvalue()
   {
      return _SMAvalue;
   } // end getSMA

   public int getNumberOfPredictions()
   {
      return _NumberOfPredictions;
   } // end getNumberOfPredictions

   public int getNumberOfDays()
   {
      return _Date.size();
   }

   /**          
    * Method : main
    *        : For testing only.
    */
   public static void main( String[] args )
   {
      StockElement theElem = new StockElement( "AAPL", "1.235235", "4.12125" );
      theElem.addOpen( "75.235" );
      theElem.addHigh( "76.235" );
      theElem.addClose( "74.235" );
      theElem.addClose( "77.235" );
      theElem.addHigh( "78.235" );
      System.out.println( theElem );
   } // end main

   private int[] getSummedDiscreteVariance( int depth )
   {
      int[] discreteSum = new int[ _DiscreteVariance.size() ];
/*
      int currSum;

      for( int w=0; w<depth; w++ ) {
         discreteSum[ w ] = 0; }

      for( int i=depth; i<_DiscreteVariance.size(); i++ )
      {
         currSum = 0;
         for( int j=i-1; j>(i-depth-1); j-- )
         {
            currSum += ((Integer)_DiscreteVariance.get( j )).intValue();
         } // end current for
         discreteSum[ i ] = currSum;
      } // end for
*/
      return discreteSum;
   } // end getSummedDiscreteVariance

   public double getThreshold()
   {
      return _Threshold;
   } // end getThreshold

   public int getHighBuy()
   {
      return _HighWorth;
   } // end getHighWorth

   public int getLastBuy()
   {
      return _LastBuy;
   } // end getLastBuy

   public int getTotalBuys()
   {
      return _Open.size();
   } // end getTotalBuys

   public double getFastEMA()
   {
      return _FastEMA;
   } 

   public double getSlowEMA()
   {
      return _SlowEMA;
   } 

   public double getSignalEMA()
   {
      return _SignalEMA;
   } 

   public double getMACDMass()
   {
      return _MACDMass;
   } 

   public double getStochasticEMA()
   {
      return _StochasticEMA;
   } 

   public int getStochasticDays()
   {
      return _StochasticDays;
   } 

   public double getFastStochastic()
   {
      return _FastStochastic;
   } 

   public double getCurrStochastic()
   {
      return _CurrStochastic;
   } 

   public double getPrevStochastic()
   {
      return _PrevStochastic;
   } 

   public StockParameterData getStockParameterData()
   {
      return _theParameters;
   } 

   public void setQQQScaleOpen( double[] theArray )
   {
      _QQQScaleOpen = theArray;
   }

   public void setQQQScaleClose( double[] theArray )
   {
      _QQQScaleClose = theArray;
   }

   public double[] getQQQScaleOpen()
   {
      return _ScaleOpen;
   }

   public double[] getQQQScaleClose()
   {
      return _ScaleClose;
   }

   public void setStockParameterData( StockParameterData param )
   {
      _theParameters = param;

      // now pull out what we need:
      _QQQMACDSum           = _theParameters.getMACDSum();
      _QQQMACDDiffSum       = _theParameters.getMACDDiffSum();
      _QQQStochasticSum     = _theParameters.getStochasticSum();
      _QQQStochasticDiffSum = _theParameters.getStochasticDiffSum();
      
   } 

   private void recalcDiscreteVariance()
   {
/*
      double temp = 0;
      _DiscreteVariance = new ArrayList( _RealVariance.size() );

      for( int i=0; i<_RealVariance.size(); i++ )
      {
         temp = ((Double)_RealVariance.get( i )).doubleValue();
         if( temp > _Threshold )
            _DiscreteVariance.add( new Integer( 1 ) );
         else
            _DiscreteVariance.add( new Integer( 0 ) );
      } // end for
*/

   } // end recalcDiscreteVariance

   protected int findResultInTop( double theResult ) 
   {
      int isInTop = _RankNumber + 1;

      for( int i=0; i<_RankNumber; i++ )
      {
          if( theResult > _ParamRank[ i ] )
          {
             _ParamRank[ i ] = theResult;
             return i;
          } // end if bigger than extant
      } // end for

      return isInTop;
   } // end resultInTop

   protected int findResult( double theResult ) 
   {
      for( int i=0; i<_RankNumber; i++ )
      {
          if( theResult == _ParamRank[ i ] )
          {
             return i;
          } // end if bigger than extant
      } // end for

      return 0;
   } // end resultInTop

   public ArrayList[] getParamRankData()
   {
      return _ParamRankData;
   } // end getParamRankData

   // Check to see if it is an increasing sequence of length, len.
   private boolean isLength( ArrayList theList, int len )
   {
      boolean isLength = false;

      if( theList == null || theList.size() < (len + 2) )
      {
         return false;
      }

      double previous = 0;
      double current = 0;
      int count = 0;

      for( int i=(theList.size() - 1); i>2; i-- )
      {
         previous = ((Double)theList.get( i - 1 )).doubleValue();
         current = ((Double)theList.get( i )).doubleValue();


         if( current > previous )
         {
            count++;
         }
         else
         {
            break;
         }
      } // end backwards for loop

      _DaysAbove = count;

      if( count >= len )
      {
         isLength = true;
      }
  
      return isLength;
   }

   /**
    *
   protected void calculateMACD( double fast, double slow, double signal )
   {
      if( _TotalDiffs == null )
      {
         _TotalDiffs = new int[ _Close.size() ];
      }

      if( !_initialized )
      {
         _MACDSum           = new double[ _Close.size() ];
         _MACDDiffSum       = new double[ _Close.size() ];
         _StochasticSum     = new double[ _Close.size() ];
         _StochasticDiffSum = new double[ _Close.size() ];
         _initialized       = true;
      }

      _theFastEMA        = new ArrayList<Double>( _Close.size() );
      _theSlowEMA        = new ArrayList<Double>( _Close.size() );
      _theMACD           = new ArrayList<Double>( _Close.size() );
      _theSignal         = new ArrayList<Double>( _Close.size() );
      _theSignalDiff     = new ArrayList<Double>( _Close.size() );
      double fastEMA     = 0;
      double slowEMA     = 0;
      double signalEMA   = 0;
      double theClose    = 0;
      double theDiff     = 0;

      for( int l=0; l<_Close.size(); l++ )
      {
         //theClose = ((Double)_High.get( l )).doubleValue() - ((Double)_Open.get( l )).doubleValue();
         theClose = ((Double)_Close.get( l )).doubleValue();

         //theClose = ((Double)_High.get( l )).doubleValue();
         // first, compute fast EMA:
         fastEMA += fast * ( theClose - fastEMA );

         // next, compute slow EMA:
         slowEMA += slow * ( theClose - slowEMA );

         _theFastEMA.add( new Double( fastEMA ) );
         _theSlowEMA.add( new Double( slowEMA ) );

         // compute the MACD line:
         theClose = (fastEMA - slowEMA);
         _MACDSum[l] += theClose;
         _theMACD.add( new Double( theClose ) );

         // next, compute signal EMA:
         signalEMA += signal * ( theClose - signalEMA );
         _theSignal.add( new Double( signalEMA ) );

         // finally, compute the signal diff:
         theDiff = theClose - signalEMA;
         _theSignalDiff.add( new Double( theDiff ) );
         _MACDDiffSum[l] += theDiff;

         if( l > 1 && theDiff > 0 && _MACDDiffSum[l-1] < 0 )
         {
            _TotalDiffs[ l ] += 1;
         } 
         else if( l > 1 && theDiff < 0 && _MACDDiffSum[l-1] > 0 )
         {
            _TotalDiffs[ l ] += -1;
         } 
      } // end close for loop

   } // end calculateMACD

   protected void calculateMACD2( double fast, double slow, double signal )
   {
      if( !_initialized )
      {
         _MACDSum           = new double[ _Close.size() ];
         _MACDDiffSum       = new double[ _Close.size() ];
         _StochasticSum     = new double[ _Close.size() ];
         _StochasticDiffSum = new double[ _Close.size() ];
         _initialized       = true;
      }

      _theFastEMA        = new ArrayList<Double>( _Close.size() );
      _theSlowEMA        = new ArrayList<Double>( _Close.size() );
      _theMACD           = new ArrayList<Double>( _Close.size() );
      _theSignal         = new ArrayList<Double>( _Close.size() );
      _theSignalDiff     = new ArrayList<Double>( _Close.size() );
      double fastEMA     = 0;
      double slowEMA     = 0;
      double signalEMA   = 0;
      double theClose    = 0;
      double theDiff     = 0;

      for( int l=0; l<_Close.size(); l++ )
      {
         //theClose = ((Double)_High.get( l )).doubleValue() - ((Double)_Open.get( l )).doubleValue();
         theClose = ((Double)_Close.get( l )).doubleValue();

         //theClose = ((Double)_High.get( l )).doubleValue();
         // first, compute fast EMA:
         fastEMA += fast * ( theClose - fastEMA );

         // next, compute slow EMA:
         slowEMA += slow * ( theClose - slowEMA );

         _theFastEMA.add( new Double( fastEMA ) );
         _theSlowEMA.add( new Double( slowEMA ) );

         // compute the MACD line:
         theClose = (fastEMA - slowEMA);
         _MACDSum[l] += theClose;
         _theMACD.add( new Double( theClose ) );

         // next, compute signal EMA:
         signalEMA += signal * ( theClose - signalEMA );
         _theSignal.add( new Double( signalEMA ) );

         // finally, compute the signal diff:
         theDiff = theClose - signalEMA;
         _theSignalDiff.add( new Double( theDiff ) );
         _MACDDiffSum[l] += theDiff;
      } // end close for loop

   } // end calculateMACD

   protected boolean isMACD( int whichDay )
   {
      if( (whichDay - 3) < 0 )
      {
         return false;
      }

      // Check to see if crossover is above center line:
      if( ((Double)_theMACD.get( whichDay - 1 )).doubleValue() < 0 )
      {
         return false;
      }

      // check for current MACD indicator:
      double current = ((Double)_theSignalDiff.get( whichDay - 1 )).doubleValue();
      double previous = ((Double)_theSignalDiff.get( whichDay - 2 )).doubleValue();

      if( (current > 0) && (previous < 0) )
      {
         return true;
      }

      // go ahead and check for the previous MACD indicator:
      current = ((Double)_theSignalDiff.get( whichDay - 2 )).doubleValue();
      previous = ((Double)_theSignalDiff.get( whichDay - 3 )).doubleValue();

      if( (current > 0) && (previous < 0) )
      {
         return true;
      }

      return false;
   } // end isMACD
*/

   // days are 0-based:
   protected void calculateFastStochastic( int days, double ema )
   {

      double stoch = 0;
      double stochEMA = 0;
      double close = 0;
      double largest = -20000;
      double smallest = 20000;
      double theDiff = 0;

      _theStochastic = new ArrayList<Double>( _Close.size() );
      _theStochEMA   = new ArrayList<Double>( _Close.size() );
      _theStochDiff  = new ArrayList<Double>( _Close.size() );

      // pre-fill unused areas so that we match up with other arrays:
      for( int q=0; q<days; q++ )
      {
         _theStochastic.add( new Double( 0 ) );
         _theStochEMA.add( new Double( 0 ) );
         _theStochDiff.add( new Double( 0 ) );
      } // end for

      // days are 0-based:
      for( int l=days; l<_Close.size(); l++ )
      {

         largest = -20000;
         smallest = 20000;

         // Find the largest and smallest in the days window:
         for( int p=(l-days); p<=l; p++ )
         {
//            close = ((Double)_High.get( p )).doubleValue() -
//                    ((Double)_Open.get( p )).doubleValue();
            close = ((Double)_Close.get( p )).doubleValue();
            if( close > largest )
            {
               largest = close;
            }
            if( close < smallest )
            {
               smallest = close;
            }
         } // for search loop

//       close = ((Double)_High.get( p )).doubleValue() -
//                    ((Double)_Open.get( p )).doubleValue();
         close = ((Double)_Close.get( l )).doubleValue();

         // Calculate the fast stochastic value:
         stoch = 100 * ( (close - smallest) / ( largest - smallest ) );
         _StochasticSum[l] += 0.5 * ( stoch - _StochasticSum[l] );
         _theStochastic.add( new Double( stoch ) );
         
         // Now calculate the EMA of the stochastic:
         stochEMA += ema * ( stoch - stochEMA );
         _theStochEMA.add( new Double( stochEMA ) );

         // Finally, calc the difference:
         theDiff = stoch - stochEMA;
         _theStochDiff.add( new Double( theDiff ) );
         _StochasticDiffSum[l] += theDiff;
      } // end for loop

   } // end calculateFastStochastic

   // days are 0-based:
   protected void calculateFastStochastic2( int days, double ema )
   {

      double stoch = 0;
      double stochEMA = 0;
      double close = 0;
      double largest = -20000;
      double smallest = 20000;
      double theDiff = 0;

      _theStochastic = new ArrayList<Double>( _Close.size() );
      _theStochEMA   = new ArrayList<Double>( _Close.size() );
      _theStochDiff  = new ArrayList<Double>( _Close.size() );

      // pre-fill unused areas so that we match up with other arrays:
      for( int q=0; q<days; q++ )
      {
         _theStochastic.add( new Double( 0 ) );
         _theStochEMA.add( new Double( 0 ) );
         _theStochDiff.add( new Double( 0 ) );
      } // end for

      // days are 0-based:
      for( int l=days; l<_Close.size(); l++ )
      {

         largest = -20000;
         smallest = 20000;

         // Find the largest and smallest in the days window:
         for( int p=(l-days); p<=l; p++ )
         {
//            close = ((Double)_High.get( p )).doubleValue() -
//                    ((Double)_Open.get( p )).doubleValue();
            close = ((Double)_Close.get( p )).doubleValue();
            if( close > largest )
            {
               largest = close;
            }
            if( close < smallest )
            {
               smallest = close;
            }
         } // for search loop

//       close = ((Double)_High.get( p )).doubleValue() -
//                    ((Double)_Open.get( p )).doubleValue();
         close = ((Double)_Close.get( l )).doubleValue();

         // Calculate the fast stochastic value:
         stoch = 100 * ( (close - smallest) / ( largest - smallest ) );
         _StochasticSum[l] += 0.5 * ( stoch - _StochasticSum[l] );
         _theStochastic.add( new Double( stoch ) );
         
         // Now calculate the EMA of the stochastic:
         stochEMA += ema * ( stoch - stochEMA );
         _theStochEMA.add( new Double( stochEMA ) );

         // Finally, calc the difference:
         theDiff = stoch - stochEMA;
         _theStochDiff.add( new Double( theDiff ) );
         _StochasticDiffSum[l] += theDiff;
      } // end for loop

   } // end calculateFastStochastic

   protected boolean isFastStochastic( int theDay )
   {
      if( (theDay - 3) < 0 )
      {
         return false;
      }

      // check for current Fast Stochastic indicator:
      double current =  ((Double)_theStochDiff.get( theDay - 1 )).doubleValue();
      double previous = ((Double)_theStochDiff.get( theDay - 2 )).doubleValue();
      double stochastic = ((Double)_theStochastic.get( theDay - 1 )).doubleValue();

      if( (current > 0) && (previous < 0) && (stochastic < 30) )
      {
         return true;
      }

      return false;
   }

/*
   public void calculateMACDMass()
   {
      for( int i=0; i<_theSignalDiff.size(); i++ )
      {
         _MACDMass += ((Double)_theSignalDiff.get( i )).doubleValue();
      } // end for
   }

   protected void calculateMACDValues()
   {
      _Worth            = -2000000;
      _RealWorth        = 0;
      double theWorth   = 0;
      double prev       = 0;
      double curr       = 0;
      double macd       = 0;
      double stoch      = 0;
      double currStoch  = 0;
      double prevStoch  = 0;
      double amountBought = 0;
      int    sharesBought = 0;
      int    numberOfBuys = 0;
      int    lastBuy      = 0;
       
      // parameter data:
      double fast = 0;
      double slow = 0;
      double signal = 0;

      int numberRight = 0;
      int numberWrong = 0;

      for( double f=0.1; f<1; f += 0.1 )
      {
         for( double s=0.1; s<f; s += 0.1 )
         {
            for( double ss=0.1; ss<1; ss += 0.1 )
            {

               // Calculate the MACD for these values:
               calculateMACD( f, s, ss );

               for( int d=10; d<15; d++ )
               {

                  for( double ema=0.1; ema<1; ema+=0.1 )
                  {
                     // cleanup local variables:
                     theWorth = 0;
                     prev = 0;
                     curr = 0;
                     prevStoch = 0;
                     currStoch = 0;
                     stoch = 0;
                     amountBought = 0;
                     sharesBought = 0;
                     numberRight = 0;
                     numberWrong = 0;
                     numberOfBuys = 0;
                     lastBuy = 0;
                     boolean haveBought = false;
                     boolean isActive = false;
      
                     // Calculate the Fast Stochastic for these values:
                     calculateFastStochastic( d, ema );

                     // Now we're ready to buy and sell:
                     for( int i=1; i<_theSignalDiff.size(); i++ )
                     {
                        prevStoch = ((Double)_theStochDiff.get( i-1 )).doubleValue();
                        currStoch = ((Double)_theStochDiff.get( i )).doubleValue();
                        stoch = ((Double)_theStochastic.get( i )).doubleValue();
                        prev = ((Double)_theSignalDiff.get( i-1 )).doubleValue();
                        curr = ((Double)_theSignalDiff.get( i )).doubleValue();
                        macd = ((Double)_theMACD.get( i )).doubleValue();
      
                        // buy situation:
                        if( (curr > 0) &&
                            (prev < 0) && 
                            !haveBought &&
                            (macd > 0) /*&&
                            (currStoch > 0) &&
                            (prevStoch < 0) &&
                            (stoch >= 0) &&
                            (stoch < 80 ) &&
                            isQQQGood( i ) )
                        {
                           haveBought = true;
                           numberOfBuys++;
                           lastBuy = i;
                           // This signal is generated on the close, so we can't
                           // buy until the next morning:
                           if( i == (_Open.size() - 1) )
                           {
                              //isActive = true;
                              _isActive = true;
                              _NumberOfActives++;
                              continue;
                           }
                           else
                           {
                              // get the next day's opening value:
                              curr = ((Double)_Open.get( i+1 )).doubleValue();
                              prev = ((Double)_Close.get( i )).doubleValue();

                              // If the opening price is lower than the indicator
                              // price, then we don't want to buy:
                              if( curr < prev )
                              {
                                 haveBought = false;
                                 continue;
                              } // end if lower price
      
                              // find number of shares to buy:
                              sharesBought = (int)(_Cash / curr);

                              // find amount bought:
                              amountBought = (sharesBought * curr) + 5;
                           }
                        }

                        // now the sell situation:
                        else if( ( ((curr < 0) &&
                                 (prev > 0) ) || // think about ORing these
                                 ((currStoch < 0) &&
                                 (prevStoch > 0) ) ||
                                 stoch >= 90 ) &&
                                 haveBought )
                        {
                           haveBought = false;
                           // get today's close price:
                           prev = ((Double)_Close.get( i )).doubleValue();

                           // calclulate the sell amount:
                           curr = (sharesBought * prev) - 5;
                           prev = (curr - amountBought);

                           if( prev <= 0 )
                           {
                              numberWrong++;
                           }
                           else
                           {
                              numberRight++;
                           }
      
                           // now update the total worth:
                           theWorth += prev;
                        } // end else
      
                     } // end buy and sell for
      
                     // now we're done trading for this dataset, 
                     // we save data if we need to:
                     if( theWorth > _Worth )
                     {
                        _Worth     = theWorth;
                        //_RealWorth = theWorth;
                        _FastEMA   = f;
                        _SlowEMA   = s;
                        _SignalEMA = ss;
                        _SlowEMA   = s;
                        _StochasticDays = d;
                        _StochasticEMA = ema;
                        _DaysAbove = numberOfBuys;
                        _LastBuy   = lastBuy;
                        _FastStochastic = stoch;
                        _PrevStochastic = prevStoch;
                        _CurrStochastic = currStoch;
                        //_isActive = isActive;
                        if( ( numberRight + numberWrong ) != 0 )
                        {
                           _SMAvalue = (double)numberRight / ((double)( numberRight + numberWrong ));
                        }
                        else
                        {
                           _SMAvalue = 0;
                        }
                     } // end if best worth

                     _RealWorth += theWorth;

                  } // end stochEMA for
               } // end day for
            } // end fast for
         } // end fast for
      } // end fast for

   } // end calculateMACDValues

   protected void calculateMACDDiffValues()
   {

      for( double f=0.1; f<1; f += 0.1 )
      {
         for( double s=0.1; s<f; s += 0.1 )
         {
            for( double ss=0.1; ss<1; ss += 0.1 )
            {

               // Calculate the MACD for these values:
               calculateMACD2( f, s, ss );

               for( int d=10; d<15; d++ )
               {
                  for( double ema=0.1; ema<1; ema+=0.1 )
                  {
                     // Calculate the Fast Stochastic for these values:
                     calculateFastStochastic2( d, ema );
                  } // end stochEMA for
               } // end day for
            } // end fast for
         } // end fast for
      } // end fast for

      _theParameters = new StockParameterData( _MACDSum,
                                               _MACDDiffSum,
                                               _StochasticSum,
                                               _StochasticDiffSum );
   } // end calculateMACDDiffValues
*/

   private boolean isQQQGood( int day )
   {
      boolean isGood = false;

      if(   _QQQMACDSum != null
         && _QQQMACDSum[ day ] > 0
         && _QQQMACDDiffSum[ day ] > 0
/*
         && _QQQStochasticSum[ day ] > 60
         && _QQQStochasticDiffSum[ day ] > 0 */ )
      {
         isGood = true;
      }
 
      return isGood;
   } // end isQQQGood

   protected void calculateTotalValues()
   {
//      calculateMACD( 0.79999999999999999, 0.7, 0.3 );
      //calculateFastStochastic( d, ema );

/*
      for( double f=0.1; f<1; f += 0.1 )
      {
         for( double s=0.1; s<f; s += 0.1 )
         {
            for( double ss=0.1; ss<1; ss += 0.1 )
            {

               // Calculate the MACD for these values:
               calculateMACD( f, s, ss );

               for( int d=10; d<15; d++ )
               {

                  for( double ema=0.1; ema<1; ema+=0.1 )
                  {
                     // Calculate the Fast Stochastic for these values:
                     calculateFastStochastic( d, ema );

                  } // end stochEMA for
               } // end day for
            } // end fast for
         } // end fast for
      } // end fast for

      double prevStoch = 0;
      double currStoch = 0;
      double stoch = 0;
      double prev = 0;
      double curr = 0;
      double macd = 0;
      double amountBought = 0;
      double theWorth = 0;
      boolean haveBought = false;
      int lastBuy = 0;
      int sharesBought = 0;
      int numberRight = 0;
      int numberWrong = 0;

      // Now we're ready to buy and sell:
      for( int i=1; i<_theSignalDiff.size(); i++ )
      {
                        prevStoch = _StochasticDiffSum[ i-1 ]; //((Double)_theStochDiff.get( i-1 )).doubleValue();
                        currStoch = _StochasticDiffSum[ i ]; //((Double)_theStochDiff.get( i )).doubleValue();
                        stoch = _StochasticSum[ i ]; //((Double)_theStochastic.get( i )).doubleValue();
                        prev = _MACDDiffSum[ i-1 ]; //((Double)_theSignalDiff.get( i-1 )).doubleValue();
                        curr = _MACDDiffSum[ i ]; //((Double)_theSignalDiff.get( i )).doubleValue();
                        macd = _MACDSum[ i ]; //((Double)_theMACD.get( i )).doubleValue();
      
                        // buy situation:
                        if( (curr > 0) &&
                            (prev < 0) && 
                            !haveBought &&
                            (macd > 0) &&
                            (currStoch > 0) &&
                            (prevStoch < 0) &&
                            (stoch >= 0) &&
                            (stoch < 75 ) &&
                            isQQQGood( i ) )
                        {
                           haveBought = true;
                           _DaysAbove++;
                           _LastBuy = i;
                           // This signal is generated on the close, so we can't
                           // buy until the next morning:
                           if( i == (_Open.size() - 1) )
                           {
                              _isActive = true;
                              continue;
                           }
                           else
                           {
                              // get the next day's opening value:
                              curr = ((Double)_Open.get( i+1 )).doubleValue();
                              prev = ((Double)_Close.get( i )).doubleValue();

                              // If the opening price is lower than the indicator
                              // price, then we don't want to buy:
                              if( curr < prev )
                              {
                                 haveBought = false;
                                 continue;
                              } // end if lower price
      
                              // find number of shares to buy:
                              sharesBought = (int)(_Cash / curr);

                              // find amount bought:
                              amountBought = (sharesBought * curr) + 5;
                           }
                        }

                        // now the sell situation:
                        /*else if( ( ((curr < 0) &&
                                 (prev > 0) ) || // think about ORing these
                                 ((currStoch < 0) &&
                                 (prevStoch > 0) ) ||
                                 stoch >= 80 ) &&
                                 haveBought )
                        else if( haveBought )
                        {
                           haveBought = false;
                           // get today's close price:
                           prev = ((Double)_Close.get( i )).doubleValue();

                           // calclulate the sell amount:
                           curr = (sharesBought * prev) - 5;
                           prev = (curr - amountBought);

                           if( prev <= 0 )
                           {
                              numberWrong++;
                           }
                           else
                           {
                              numberRight++;
                           }
      
                           // now update the total worth:
                           theWorth += prev;
                        } // end else
      
      } // end buy and sell for

      _Worth = theWorth;
      if( (numberRight + numberWrong) != 0 )
      {
         _SMAvalue = (double)numberRight/( (double)(numberRight + numberWrong) );
      }
      
*/
   } // end calculateTotalValues

   private double findLow( int start, int end, ArrayList theList )
   {
   
      double min = Double.POSITIVE_INFINITY;
      double current = min;

      if( end == theList.size() )
      {
         end--;
      }

      if( theList != null )
      {
         for( int i=start; i<(end+1); i++ )
         {
            current = ((Double)theList.get( i )).doubleValue();
            if( current < min )
            {
               min = current;
            }
         }
      }

      return min;
   }

   private double findHigh( int start, int end, ArrayList theList )
   {
   
      double max = Double.NEGATIVE_INFINITY;
      double current = max;

      if( end == theList.size() )
      {
         end--;
      }

      if( theList != null )
      {
         for( int i=start; i<(end+1); i++ )
         {
            current = ((Double)theList.get( i )).doubleValue();
            if( current > max )
            {
               max = current;
            }
         }
      }

      return max;
   }

   private double findMax( double[] theList )
   {
      double max = Double.NEGATIVE_INFINITY;
      double current = max;

      if( theList != null )
      {
         for( int i=0; i<theList.length; i++ )
         {
            //current = ((Double)theList.get( i )).doubleValue();
            current = theList[ i ];
            if( current > max )
            {
               max = current;
            }
         }
      }

      return max;
   }

   private double findMin( double[] theList )
   {
      double min = Double.POSITIVE_INFINITY;
      double current = min;

      if( theList != null )
      {
         for( int i=0; i<theList.length; i++ )
         {
            current = theList[ i ];
            if( current < min )
            {
               min = current;
            }
         }
      }

      return min;
   }

   private double findMin( ArrayList theList )
   {
      double min = Double.POSITIVE_INFINITY;
      double current = min;

      if( theList != null )
      {
         for( int i=0; i<theList.size(); i++ )
         {
            current = ((Double)theList.get( i )).doubleValue();
            if( current < min )
            {
               min = current;
            }
         }
      }

      return min;
   }

   protected double[] scaleIt( double[] theList )
   {
      double[] theScaledList = new double[ theList.length ];
      double min = 0;
      double max = 0;
      double current = 0;

      min = findMin( theList );
      for( int i=0; i<theList.length; i++ )
      {
         current = theList[ i ];
         theScaledList[ i ] = current - min;
      }

      max = findMax( theScaledList );
      for( int i=0; i<theScaledList.length; i++ )
      {
         theScaledList[ i ] /= max;
      }

      return theScaledList;
   }

   protected double[] scaleIt( ArrayList theList )
   {
      double[] theScaledList = new double[ theList.size() ];
      double min = 0;
      double max = 0;
      double current = 0;

      min = findMin( theList );
      for( int i=0; i<theList.size(); i++ )
      {
         current = ((Double)theList.get( i )).doubleValue();
         theScaledList[ i ] = current - min;
      }

      max = findMax( theScaledList );
      if( max == 0 )
      {
         return theScaledList;
      }

      for( int i=0; i<theScaledList.length; i++ )
      {
         theScaledList[ i ] /= max;
      }

      return theScaledList;
   }

   protected void calculateScales()
   {
      _ScaleClose = scaleIt( _Close );
      _ScaleOpen  = scaleIt( _Open );
      _Worth = 0;

      if( _ScaleClose.length != _QQQScaleClose.length )
      {
         System.out.println( "returning for: " + _StockName );
         return;
      }

      if( _ScaleOpen.length != _QQQScaleOpen.length )
      {
         System.out.println( "returning for: " + _StockName );
         return;
      }

      for( int i=1; i<_ScaleClose.length; i++ )
      {
         _Worth += Math.abs( _ScaleClose[ i ] - _QQQScaleClose[ i ] );
      }

      for( int i=1; i<_ScaleOpen.length; i++ )
      {
         _Worth += Math.abs( _ScaleOpen[ i ] - _QQQScaleOpen[ i ] );
      }

   }

   protected void calculateQQQScales()
   {
      _ScaleClose = scaleIt( _Close );
      _ScaleOpen  = scaleIt( _Open );
   }

   protected double[][] getProbabilityArray()
   {
      double[][] theArray = new double[ _theParametersArray.length ][];

      for( int i=0; i<_theParametersArray.length; i++ )
      {
         theArray[ i ] = _theParametersArray[ i ].getProbabilityArray();
      }

      return theArray;
   }

   protected void writePoint( double point, BufferedWriter theFile )
   {
      try
      {
         theFile.write( "" + point );
      } 
      catch( IOException e )
      {
      }
   }

   protected void simulate( double close,
                            int state, 
                            double[][] theArray, 
                            double[][] intervals )
   {
      Random theRandomGenerator = new Random();
      double random = 0;
      double thePoint = 0;
      double temp = 0;
      double initialClose = close;
      int initialState = state;

      _Simulations = new double[ _NumberOfPredictions ];

      for( int k=0; k<_NumberOfSamples; k++ )
      {
         thePoint = 0;
         temp = 0;
         state = initialState;
         close = initialClose;

         for( int i=0; i<_NumberOfPredictions; i++ )
         {
            random = theRandomGenerator.nextDouble();
           
            // find the state that this is in:
            for( int j=0; j<theArray.length; j++ )
            {
               if( random <= theArray[ state ][ j ] )
               {
                  state = j;
                  break;
               }
            }
   
            // now we can generate a point:
            thePoint = theRandomGenerator.nextDouble();
            temp = intervals[ state ][ 1 ] - intervals[ state ][ 0 ];
            if( temp < 0 )
            {
               temp *= -1;
            }
            thePoint *= temp;
            thePoint += intervals[ state ][ 0 ];
   
            close += thePoint;
   
            _Simulations[ i ] += close;

         }
      }

      for( int i=0; i<_NumberOfPredictions; i++ )
      {
         _Simulations[ i ] /= _NumberOfSamples;
         //System.out.println( _Simulations[ i ] );
      }

      //System.out.println( "" );

   }

   public void calculateProbabilities( double[][] intervals )
   {
      _theParametersArray = new StockParameterData[ intervals.length ];
      double open  = 0;
      double close = 0;
      double slope = 0;
      int state = (int)(intervals.length / 2);

      // initialize the stock parameter objects:
      for( int i=0; i<intervals.length; i++ )
      {
         StockParameterData theParam = new StockParameterData( i, intervals );
         _theParametersArray[ i ] = theParam;
      } // end for loop

      // now loop through all data and calc probs:
      for( int i=0; i<_Open.size(); i++ )
      {
         open = ((Double)_Open.get( i )).doubleValue();

         // if we had a previous close, measure gap:
/*
         if( i > 0 )
         {
            slope = open - close;
            state = _theParametersArray[ state ].addProbability( (i + 1), slope );
         }
*/
         close = ((Double)_Close.get( i )).doubleValue();
         slope = close - open;
         state = _theParametersArray[ state ].addProbability( ((i * i) + 1), slope );
      } // end for loop

      double[][] theCOMs = new double[_theParametersArray.length][];

      double left = 0;
      double center = 0;
      double right = 0;
      int l = 0;
      int c = 0;
      int r = 0;

      // finalize the numbers:
      for( int i=0; i<_theParametersArray.length; i++ )
      {
         _theParametersArray[ i ].normalize();
         theCOMs[ i ] = _theParametersArray[ i ].calculateCenterOfMass();
System.out.println( i + " left: " + theCOMs[i][0] + " center: " + theCOMs[i][1] + " right: " + theCOMs[i][2] );
         if( theCOMs[i][0] > 0 )
         {
            left += theCOMs[i][0];
            l++;
         }
         if( theCOMs[i][1] > 0 )
         {
            center += theCOMs[i][1];
            c++;
         }
         if( theCOMs[i][2] > 0 )
         {
            right += theCOMs[i][2];
            r++;
         }
      } // end for loop

      if( l > 0 )
      {
         left /= l;
      }
      if( c > 0 )
      {
         center /= c;
      }
      if( r > 0 )
      {
         right /= r;
      }

System.out.println( _StockName + " left: " + left + " center: " + center + " right: " + right );

      double[][] theArray = getProbabilityArray();

/*
      // print out for test:
      for( int i=0; i<theArray.length; i++ )
      {
         for( int j=0; j<theArray.length; j++ )
         {
            System.out.println( i + " " + j + " " + theArray[ i ][ j ] );
         }
      }
*/

      // start state, probabilities array, intervals array:
      simulate( close, state, theArray, intervals );
      _ScaleClose = scaleIt( _Simulations );

   }

   /**
    *
    *
    * CalculateCallStatistics
    *
    *
    */
   public int calculateCallStatistics()
   {
      int theReturn = 0;
      _HighMean = 0;
      double highMean = 0;
      double difference = 0;
      double smallestDifference = 2000;

      System.out.println( _StockName );

      //for( int i=(_Open.size()-2); i>(_Open.size()-7); i-- )
      for( int i=(_Open.size()-3); i>(_Open.size()-8); i-- )
      {
         difference = getOpen( i ) - getLow( i );

         if( difference < smallestDifference )
         {
            smallestDifference = difference;
         }

         _HighMean += difference;

         difference = getHigh( i ) - getClose( i );
         highMean += difference;
      }

      _HighMean /= 5;
      highMean /= 5;

      System.out.println( "Smallest Diff: " + smallestDifference );
      System.out.println( "LowMean: " + _HighMean );
      System.out.println( "HighMean: " + highMean );
      System.out.println( "Actual Low: " + (getOpen( _Open.size() - 2 ) - getLow( _Open.size() - 2 )) );
      System.out.println( "Actual High: " + (getHigh( _Open.size() - 2 ) - getOpen( _Open.size() - 2 )) );

/*
      _HighMean /= 5;
      _CloseMean = _HighMean / 2;

      //_Worth = trade( _Open.size() - 1, _CloseMean );
      //_Worth = trade( _Open.size() - 1, _HighMean );
      _Worth = trade( _Open.size() - 1, smallestDifference );

      //tradeBest( _Open.size() - 1, _HighMean );
      tradeBest( _Open.size() - 1, smallestDifference );
      //calculateInflation( _Open.size() - 1 );
*/

      // 6 days back is the day after the call:

      //double value = tradeGap( (_Open.size() - 7), smallestDifference );
      // 1 day back is the day after the call:
      double value = tradeGap( (_Open.size() - 2) );

      //double diff = getHigh( _Open.size() - 2 ) - getOpen( _Open.size() - 2 );
      //diff /= getClose( _Open.size() - 2 );

      //System.out.println( _StockName + " The Call: " + value + " next: " + tradeGap( _Open.size() - 6 ) );
      //System.out.println( _StockName + " The Call: " + value + " next: " + tradeGap( _Open.size() - 6, smallestDifference ) );
      //System.out.println( _StockName + " The Call: " + value + " next: " + tradeGap( _Open.size() - 1, _HighMean ) );
      System.out.println( _StockName + " The Call: " + value + 
                                       " next: " + tradeGap( _Open.size() - 1, highMean ) +
                                       " Volume: " + getVolume() );

/*
      //System.out.println( _StockName + " high diff: " + diff );
      tradeHighLow( (_Open.size() - 2), highMean, _HighMean );

      diff = getClose( _Open.size() - 7 ) - getOpen( _Open.size() - 7 );
      diff /= getClose( _Open.size() - 7 );
      System.out.println( _StockName + " close diff: " + diff );

      value = tradeGap( (_Open.size() - 6) );
      double total = value;
      boolean wonFirst = false;

      if( value > 0 )
      {
         wonFirst = true;
         System.out.println( _StockName + " First: " + value + " next: " + tradeGap( _Open.size() - 5 ) );
      }

      for( int i=(_Open.size() - 5); i<(_Open.size()); i++ )
      {
         value = tradeGap( i );
         total += value;

         if( value > 0 )
         {
            theReturn++;
         }
      }

      double probability = (double)theReturn / 5;
*/

      //System.out.println( _StockName + " wonFirst: " + wonFirst + " prob: " + probability + " total: " + total );

      //System.out.println( _StockName + "    Gap:    " + tradeGap( _Open.size() - 1 ) );

      /**
       *
       * Now, calculate the short:
       *

      _HighMean = 0;
      difference = 0;

      for( int i=(_Open.size()-2); i>(_Open.size()-7); i-- )
      {
         difference = ((Double)_Open.get( i )).doubleValue() - 
                      ((Double)_Low.get( i )).doubleValue();
         _HighMean += difference;
      }

      _HighMean /= 5;
      _CloseMean = _HighMean / 2;

      _Worth = tradeShort( _Open.size() - 1, _HighMean );

      //System.out.println( _StockName + " highest: " + tradeShortBest( _Open.size() - 1, _HighMean ) + "\n" );
      tradeShortBest( _Open.size() - 1, _HighMean );

      System.out.println( "\n" );

       */

      _isActive = true;

      return theReturn;
   } 

   public int calculateStatistics()
   {
      //return calculateActiveStatistics();
      //return calculateEventStatistics();
      //return calculateCallStatistics();
      //return calculateStatistics( 0, _Open.size() );
      //return calculateStatistics2( 0, _Open.size() );
      return -1;
   } 

   /**
    * 
    */
   public int calculateActiveStatistics()
   {
      int active = 1;
      int day = _Open.size() - 2;
      double firstOpen = getOpen( 0 );
      double open = getOpen( day );

      for( int i=0; i<day; i++ )
      {
         if( firstOpen < getOpen( i ) )
         {
            active = 0;
            break;
         }
      }

      if( active == 1 )
      {
         if( getOpen( day + 1 ) < firstOpen )
         {
            active = 0;
         }
      }

      return active;
   }

   /**
    * Here, we collect some data that, hopefully, will 
    * be amenable to statistical analysis.
    */
   public int calculateStatistics( int start, int end )
   {
      // If the volume isn't at least half a mill, then we don't bother:
      if( getVolume() < 700000 || end > _Open.size() )
      {
         _initialized = false;
         return 1;
      } // end if not large enough
 
      double open = 0;
      double low = 0;
      double high = 0;
      double close = 0;
      double OpenHighMean = 0;
      double highMean = 0;
      double lowMean = 0;
      double BearCloseMean = 0;
      double meanSBD = 0;
      double meanIntSBD = 0;
      int numberOfBullDays = 0;
      int numberOfBearDays = 0;

      // holds the high differences:
      //double[] closeDiffs = new double[ _Open.size() ];
      // holds the close differences:
      //double[] highDiffs = new double[ _Open.size() ];
      // holds the low differences:
      double[] lowDiffs = new double[ end - start ];
      int pos = 0;

      // calculate the differences:
      for( int i=start; i<end; i++ )
      {
         open = ((Double)_Open.get(i)).doubleValue();
         //high = ((Double)_High.get(i)).doubleValue();
         low  = ((Double)_Low.get(i)).doubleValue();
         //close  = ((Double)_Close.get(i)).doubleValue();

         //closeDiffs[i] = (close - open);
         //highDiffs[i] = (high - open);
         lowDiffs[pos++] = Math.abs(low - open);
      } // end for

      // calculate the means:
/*
      for( int i=0; i<closeDiffs.length; i++ )
      {
         // count it as a bull:
         if( closeDiffs[i] > 0 )
         {
            BullCloseMean += closeDiffs[i];
            //numberOfBullDays++;
         }
         else // count it as a bear:
         {
            BearCloseMean += closeDiffs[i];
            //numberOfBearDays++;
         }
      } // end for
     
      BullCloseMean /= (double)_Open.size();
      BearCloseMean /= (double)_Open.size();

      // calculate the means:
      for( int i=0; i<highDiffs.length; i++ )
      {
         BullHighMean += highDiffs[i];
      } // end for
*/
     
      /*
       * here, calculate the strong bull days:
       * we should also calculate the mean time for double SBDs.
       * 
       */
      _Threshold = 0;
      int LastBullDay = -1;
      int lastSBD = -1;
      int lastIntSBD = -1;
      int lastConSBD = -1;
      int ConsequtiveDays = 0;
      int highDays = 0;
      int totalConseqs = 0;
      int count = 0;
      int lowDays = 0;
      int countSBD = 0;
      int countDSBD = 0;
      int shares = 0;
      double threshold = 0.01;
      double difference = 0;
      double value = 0;
      ArrayList<Double> dataPoints = new ArrayList<Double>();

      for( int i=0; i<lowDiffs.length; i++ )
      {
         // count it as a bull:
         if( lowDiffs[i] <= threshold )
         {
            // this will measure the average distance between SBDs:
            // which may not be a very accurate measurement.
            if( LastBullDay != -1 )
            {
               OpenHighMean += (i - LastBullDay);
            }
            numberOfBullDays++;
            LastBullDay = i;

            // if this is the first day of a SBD:
            if(    ( (i-1) >= 0 )
                && ( lowDiffs[i-1] > threshold ) 
                && ( (i+1) < lowDiffs.length ) )
            {

               count++; // increment for this possible group of bulls.

               open = ((Double)_Open.get(i+1)).doubleValue();
               close = ((Double)_Close.get(i+1)).doubleValue();
               difference = (close - open);

               if( difference > 0 )
               {
                  highDays++;
                  highMean += difference;
               } // end if
               else
               {
                  lowDays++;
                  lowMean += difference;
               } // end else
               
               // just sell at close:
               shares = (int)Math.floor(_Cash / open);
               value = (shares * close) - 5;
               value -= ((shares * open) + 5);
               _Worth += value;

               // if this is a consequtive SBD:
               if( lowDiffs[i+1] <= threshold )
               {
                  ConsequtiveDays++;

                  /*
                   * calc the mean first SBD
                   */
                  if( lastSBD == -1 )
                  {
                     lastSBD = i;
                  } // end if
                  else
                  {
                     meanSBD += (i - lastSBD);
                     countSBD++;
                     lastSBD = i;
                  } // end else

                  if( lastIntSBD == -1 )
                  {
                     lastIntSBD = count;
                  } // end if
                  else
                  {
                     meanIntSBD += (count - lastIntSBD);
                     dataPoints.add( meanIntSBD );
                     lastIntSBD = count;
                  } // end else

                  if( lastConSBD != -1 )
                  {
                     if( lastConSBD == (count-1) )
                     {
                        countDSBD++;
                     } // end if
                  } // end if

                  lastConSBD = count;
               }

            } // end if

            // now, calc the total bulls:
            if(    ( (i+1) < lowDiffs.length )
                && ( lowDiffs[i+1] <= threshold ) )
            {
               totalConseqs++;
            } // end if total bulls
         }

/*
      if( count != 0 )
      {
         System.out.println( _StockName + " : " + (double)((double)highDays / (double)count) );
      }
*/

      } // end for
     
      //BullCloseMean /= (double)_Open.size();
      //BearCloseMean /= (double)_Open.size();
      //BullHighMean /= (double)_Open.size();
/*
      str.append( _RealWorth ).append( " " ); // hold the number of SBDs
      str.append( _TotalLoss ).append( " " );  // holds the total number of consequtive SBDs
      str.append( _SMAvalue ).append( " " ); // holds the consequtive SBD probability
*/

      _StabilityMass = new Double( ConsequtiveDays );

      if( highDays != 0 )
      {
         _RealWorth = (double)highMean / (double)highDays;
      }
      else
      {
         _RealWorth = 0;
      }

      if( lowDays != 0 )
      {
         _TotalLoss = (double)lowMean / (double)lowDays;
      }
      else
      {
         _TotalLoss = 0;
      }

      if( _TotalLoss != 0 )
      {
         _SMAvalue = _RealWorth / Math.abs( _TotalLoss );
      }
      else
      {
         _SMAvalue = _RealWorth * count;
      }

/*
      if( numberOfBullDays == 0 )
      {
         OpenHighMean = 0;
         _SMAvalue = 0;
      }
      else
      {
         OpenHighMean /= (double)numberOfBullDays;
         _SMAvalue = (double)((double)totalConseqs / (double)numberOfBullDays);
      }
*/

      if( count != 0 )
      {
         _Threshold = (double)((double)ConsequtiveDays / (double)count);
         _StochasticEMA = (double)((double)highDays / (double)count);
      }

      if( countSBD != 0 )
      {
         meanSBD /= (double)countSBD;
         meanIntSBD /= (double)countSBD;
      }
      else
      {
         meanSBD = 1000;
      }

      if( (ConsequtiveDays-1) != 0 )
      {
         _MACDMass = ((double)countDSBD) / ((double)ConsequtiveDays-1);
      }
      else
      {
         _MACDMass = 0;
      }

      double meanIntDeviation = 0;
      double temp = 0;
      for( int i=0; i<dataPoints.size(); i++ )
      {
         temp = ( ((Double)dataPoints.get(i)).doubleValue() - meanIntSBD );
         temp *= temp;
         meanIntDeviation += temp;
      } // end for

      if( (dataPoints.size() - 1) > 0 )
      {
         meanIntDeviation /= (dataPoints.size() - 1);
      }
      meanIntDeviation = Math.sqrt( meanIntDeviation );

      _SignalEMA = meanIntDeviation;
      _FastEMA = meanSBD;
      _SlowEMA = meanIntSBD;
      _StandardDeviation = count;

      /*
       * this counts only the really big loss freq: 
       */
/*
      LastBullDay = 0;
      int LastBearDay = 0;
      count = 0;
      numberOfBearDays = 0;
      double BearFrequencyMean = 0;

      // calculate the frequencies:
      for( int i=0; i<closeDiffs.length; i++ )
      {
      //System.out.println( _StockName + " The diff is: " + closeDiffs[i] + " " + BullCloseMean + "\n" );
      //System.out.println( _StockName + " The bearDays is: " + numberOfBearDays );

         if( Math.abs( closeDiffs[i] ) > BullCloseMean )
         {
            BearFrequencyMean += (i - LastBearDay);
            LastBearDay = i;
            numberOfBearDays++;
         } // end if

      } // end for

      if( Math.abs( numberOfBearDays ) != 0 )
      {
         BearFrequencyMean /= (double)numberOfBearDays;
      }
      else
      {
         BearFrequencyMean = 1000;
      }
      _StabilityMass = new Double( BearFrequencyMean );
*/

      //simulateBuy();

      return 0;
   } // calculateStatistics

   /**
    * Here, we collect some data that, hopefully, will 
    * be amenable to statistical analysis.
    */
   public int calculateStatistics2( int start, int end )
   {
      // If the volume isn't at least half a mill, then we don't bother:
      if( getVolume() < 700000 || end > _Open.size() )
      {
         _initialized = false;
         return 1;
      } // end if not large enough
 
      double open = 0;
      double low = 0;
      double high = 0;
      double close = 0;
      double OpenHighMean = 0;
      double BullCloseMean = 0;
      double BullHighMean = 0;
      double BearCloseMean = 0;
      double meanSBD = 0;
      double meanIntSBD = 0;
      int numberOfBullDays = 0;
      int numberOfBearDays = 0;

      double[] lowDiffs = new double[ end - start ];
      int pos = 0;

      // calculate the differences:
      for( int i=start; i<end; i++ )
      {
         //open = ((Double)_Open.get(i)).doubleValue();
         high = ((Double)_High.get(i)).doubleValue();
         //low  = ((Double)_Low.get(i)).doubleValue();
         close  = ((Double)_Close.get(i)).doubleValue();

         lowDiffs[pos++] = Math.abs(close - high);
      } // end for

      /*
       * here, calculate the strong bull days:
       * we should also calculate the mean time for double SBDs.
       * 
       */
      _Threshold = 0;
      int LastBullDay = -1;
      int lastSBD = -1;
      int lastIntSBD = -1;
      int lastConSBD = -1;
      int ConsequtiveDays = 0;
      int highDays = 0;
      int totalConseqs = 0;
      int count = 0;
      int countSBD = 0;
      int countDSBD = 0;
      double threshold = 0.01;
      ArrayList<Double> dataPoints = new ArrayList<Double>();

      for( int i=0; i<lowDiffs.length; i++ )
      {
         // count it as a bull:
         if( lowDiffs[i] <= threshold )
         {
            // this will measure the average distance between SBDs:
            // which may not be a very accurate measurement.
            if( LastBullDay != -1 )
            {
               OpenHighMean += (i - LastBullDay);
            }
            numberOfBullDays++;
            LastBullDay = i;

            // if this is the first day of a SBD:
            if(    ( (i-1) >= 0 )
                && ( lowDiffs[i-1] > threshold ) 
                && ( (i+1) < lowDiffs.length ) )
            {

               count++; // increment for this possible group of bulls.

               open = ((Double)_Open.get(i+1)).doubleValue();
               close = ((Double)_Close.get(i+1)).doubleValue();

               if( (close - open) > 0 )
               {
                  highDays++;
               } // end if
               
               // if this is a consequtive SBD:
               if( lowDiffs[i+1] <= threshold )
               {
                  ConsequtiveDays++;

                  /*
                   * calc the mean first SBD
                   */
                  if( lastSBD == -1 )
                  {
                     lastSBD = i;
                  } // end if
                  else
                  {
                     meanSBD += (i - lastSBD);
                     countSBD++;
                     lastSBD = i;
                  } // end else

                  if( lastIntSBD == -1 )
                  {
                     lastIntSBD = count;
                  } // end if
                  else
                  {
                     meanIntSBD += (count - lastIntSBD);
                     dataPoints.add( meanIntSBD );
                     lastIntSBD = count;
                  } // end else

                  if( lastConSBD != -1 )
                  {
                     if( lastConSBD == (count-1) )
                     {
                        countDSBD++;
                     } // end if
                  } // end if

                  lastConSBD = count;
               }

            } // end if

            // now, calc the total bulls:
            if(    ( (i+1) < lowDiffs.length )
                && ( lowDiffs[i+1] <= threshold ) )
            {
               totalConseqs++;
            } // end if total bulls
         }

      } // end for
     
      _StabilityMass = new Double( ConsequtiveDays );

      if( numberOfBullDays == 0 )
      {
         OpenHighMean = 0;
         _SMAvalue = 0;
      }
      else
      {
         OpenHighMean /= (double)numberOfBullDays;
         _SMAvalue = (double)((double)totalConseqs / (double)numberOfBullDays);
      }

      if( count != 0 )
      {
         _Threshold = (double)((double)ConsequtiveDays / (double)count);
         _StochasticEMA = (double)((double)highDays / (double)count);
      }

      if( countSBD != 0 )
      {
         meanSBD /= (double)countSBD;
         meanIntSBD /= (double)countSBD;
      }
      else
      {
         meanSBD = 1000;
      }

      if( (ConsequtiveDays-1) != 0 )
      {
         _MACDMass = ((double)countDSBD) / ((double)ConsequtiveDays-1);
      }
      else
      {
         _MACDMass = 0;
      }

      double meanIntDeviation = 0;
      double temp = 0;
      for( int i=0; i<dataPoints.size(); i++ )
      {
         temp = ( ((Double)dataPoints.get(i)).doubleValue() - meanIntSBD );
         temp *= temp;
         meanIntDeviation += temp;
      } // end for

      if( (dataPoints.size() - 1) > 0 )
      {
         meanIntDeviation /= (dataPoints.size() - 1);
      }
      meanIntDeviation = Math.sqrt( meanIntDeviation );

      _SignalEMA = meanIntDeviation;
      _FastEMA = meanSBD;
      _SlowEMA = meanIntSBD;
      _Worth = OpenHighMean;
      _RealWorth = numberOfBullDays;
      _TotalLoss = totalConseqs;
      _StandardDeviation = count;

      return 0;
   } // calculateStatistics

   protected void exploratoryStatistics( double mean, double deviation )
   {
      double min = findMin( _StochasticSum );
      double[] values = new double[ _StochasticSum.length ];

      for( int i=0; i<_StochasticSum.length; i++ )
      {
         values[i] = _StochasticSum[i] - min;
      }
      double max = findMax( values );

      int distance = (int)( 3 * (Math.abs(mean - deviation)) );
      values = new double[ distance ];
      int position = 0;

      for( int i=0; i<_StochasticSum.length; i++ )
      {
         position = (int)( ( ( _StochasticSum[i] - min ) / max ) * distance );
         if( position >= 0 && position < distance )
         {
            System.out.println( "real value: " + _StochasticSum[i] + " goes to: " + position );
            values[ position ] += 1;
         }
      } // end for

      System.out.println( "exploratory:" );
      for( int i=0; i<values.length; i++ )
      {
         System.out.println( values[i] );
      }

   } // end exploratoryStatistics

   public void tradeStatistics()
   {
      double temp = 0;
      double open = 0;
      int shares = 0;
      _Worth = 0;

      for( int i=0; i<_Open.size(); i++ )
      {
         temp = 0;

         // check for buy:
         if( _MACDSum[i] > 0 )
         {
            temp = ((Double)_Open.get(i)).doubleValue();
            shares = (int)(_Cash / temp);
            temp *= shares; // amount bought
            temp = (((Double)_Close.get(i)).doubleValue() * shares) - temp;
         }
         else
         {
            open = ((Double)_Open.get(i)).doubleValue();
            shares = (int)(_Cash / open);
            temp = shares * open; // amount bought
            temp = ((open - 0.01) * shares) - temp;
         }

         temp -= 10;
 
         _Worth += temp;

//System.out.println( _Worth );

         _Cash += temp;
      } // end for loop

      //System.out.println( "Worth is: " + _Worth );
      //System.out.println( "Cash is: " + _Cash );
   } // end tradeStatistics

   protected void calculateDiffStatistics()
   {
      double[] highs = new double[ _StochasticSum.length + 1 ];
      double[] closes = new double[ _StochasticSum.length + 1 ];
      int position = 0;

      for( int i=0; i<_MACDSum.length; i++ )
      {
         if( _MACDSum[i] > 0 )
         {
            highs[position] = (((Double)_High.get(i)).doubleValue()) -
                              (((Double)_Open.get(i)).doubleValue());
            _HighMean += highs[position];
            closes[position] = (((Double)_Close.get(i)).doubleValue()) -
                                 (((Double)_Open.get(i)).doubleValue());
            _CloseMean += closes[position++];
         }
      } // end for
      _HighMean /= highs.length;
      _CloseMean /= closes.length;

      //System.out.println( "High mean is: " + _HighMean );
      //System.out.println( "Close mean is: " + _CloseMean );

      double temp = 0;

      for( int i=0; i<highs.length; i++ )
      {
         temp = (highs[i] - _HighMean);
         _HighDeviation += (temp * temp);

         temp = (closes[i] - _CloseMean);
         _CloseDeviation += (temp * temp);
      } // end for
      _HighDeviation /= (highs.length - 1);
      _CloseDeviation /= (closes.length - 1);
      _HighDeviation = Math.sqrt( _HighDeviation );
      _CloseDeviation = Math.sqrt( _CloseDeviation );

      //System.out.println( "High deviation is: " + _HighDeviation );
      //System.out.println( "Close deviation is: " + _CloseDeviation );

   }

   private void simulateBuy()
   {
      double worth = _Cash;
      double totalWorth = 0;
      int shares = 0;
      //int days = 10;
      int days = _Open.size();

      if( _Open.size() < days )
      {
         return;
      }

      for( int i=(_Open.size() - days); i<_Open.size(); i++ )
      {
         shares = (int)(_Cash / ((Double)_Open.get(i)).doubleValue());
         worth = (shares * ((Double)_Close.get(i)).doubleValue()) - 5;
         worth -= ((shares * ((Double)_Open.get(i)).doubleValue()) + 5);
         totalWorth += (i * i * worth);
      } // end for
      _Threshold = totalWorth;
      //System.out.println( _StockName + " " + totalWorth );
   }

   private double[] getLowDiffs2()
   {
      if( _ParamRank == null )
      {
         _ParamRank = new double[ _Open.size() ];
         double open = 0;
         double low = 0;

         // calculate the differences:
         for( int i=0; i<_Open.size(); i++ )
         {
            //open = ((Double)_Open.get(i)).doubleValue();
            //low  = ((Double)_Low.get(i)).doubleValue();
            open = ((Double)_Close.get(i)).doubleValue();
            low  = ((Double)_High.get(i)).doubleValue();
            _ParamRank[ i ] = Math.abs(low - open);
         } // end for
      }

      return _ParamRank;
   }

   private double[] getLowDiffs()
   {
      if( _ParamRank == null )
      {
         _ParamRank = new double[ _Open.size() ];
         double open = 0;
         double low = 0;

         // calculate the differences:
         for( int i=0; i<_Open.size(); i++ )
         {
            open = ((Double)_Open.get(i)).doubleValue();
            low  = ((Double)_Low.get(i)).doubleValue();
            _ParamRank[ i ] = Math.abs(low - open);
         } // end for
      }

      return _ParamRank;
   }

   public boolean isActive2( int day, boolean dummy )
   {
      double[] theDiffs = getLowDiffs2();
      double threshold = 0.01;
      _isActive = false;

      if( ((day - 2) > 0) && (day <= theDiffs.length) )
      {
         if(    theDiffs[ day - 1 ] <= threshold
             && theDiffs[ day - 2 ]  > threshold )
         {
            _isActive = true;
         } // end if
      } // end if

      return _isActive;
   } // end isActive

   public boolean isActive( int day, boolean dummy )
   {
      double[] theDiffs = getLowDiffs();
      double threshold = 0.01;
      _isActive = false;

      if( ((day - 2) > 0) && (day <= theDiffs.length) )
      {
         if(    theDiffs[ day - 1 ] <= threshold
             && theDiffs[ day - 2 ]  > threshold )
         {
            _isActive = true;
         } // end if
      } // end if

      return _isActive;
   } // end isActive

   public boolean isActive2( int day )
   {
      double[] theDiffs = getLowDiffs2();
      double threshold = 0.01;
      _isActive = false;

      if( ((day - 2) > 0) && (day < theDiffs.length) )
      {
         if(    theDiffs[ day - 1 ] <= threshold
             && theDiffs[ day - 2 ]  > threshold )
         {
            _isActive = true;
         } // end if
      } // end if

      return _isActive;
   } // end isActive

   public boolean isActive( int day )
   {
      double[] theDiffs = getLowDiffs();
      double threshold = 0.01;
      _isActive = false;

      if( ((day - 2) > 0) && (day < theDiffs.length) )
      {
         if(    theDiffs[ day - 1 ] <= threshold
             && theDiffs[ day - 2 ]  > threshold )
         {
            _isActive = true;
         } // end if
      } // end if

      return _isActive;
   } // end isActive

   public double tradeBest( int day, double threshold )
   {
      double open = ((Double)_Open.get(day)).doubleValue();
      double high = 200;
      double close = ((Double)_Close.get(day)).doubleValue();
      double difference = high - open;
      double value = 0;
      int shares = 0;

      if( open == 0 )
      {
         open = close;
      }

      if( difference >= threshold )
      {
         shares = (int)Math.floor(_Cash / open);
         value = shares * (open + threshold);
         value -= 5;
         value -= ((shares * open) + 5);
      } // end if good
      else
      {
         shares = (int)Math.floor(_Cash / open);
         value = shares * (close);
         value -= 5;
         value -= ((shares * open) + 5);
      } // end else (bad)

      double diff = (open - ((Double)_Close.get(day-1)).doubleValue());

System.out.println( _StockName + " thresh: " + threshold );
System.out.println( _StockName + " Buying (BEST): " + shares + " value:         " + value );
//System.out.println( _StockName + " Stop limit: " + (-35 + (shares * open) + 10) / shares );
System.out.println( _StockName + " opening diff: " + diff + " percentage: " + (diff / open) );

      return value;
   } // end buy

   public double tradeGap( int day, double diff )
   {
      _Worth = 0;
      double open = getClose( day - 1 );
      //double low = getLow( day - 1 );
      //double high = getHigh( day );
      double close = getOpen( day );
      double value = 0;
      int shares = 0;

      if( close <= open )
      {
         close = getClose( day );
         _Worth = trade( day );
      }

/* uncomment for low entry logic on call day:
      if( (getOpen( day - 1 ) - diff) > low )
      {
         open = getOpen( day - 1 ) - diff;
      }

      if( close <= open )
      {
         close = getClose( day );
      }

      if( close + diff <= high )
      {
         close += diff;
      }
      else 
      {
         close = getClose( day );
      }
*/

      shares = (int)Math.floor(_Cash / open);
      value = shares * (close);
      value -= 5;
      value -= ((shares * open) + 5);

      _Worth += value;

      return _Worth;
      //return value;
   } // end buy

   public double tradeGap( int day )
   {
      double open = getClose( day - 1 );
      double high = getHigh( day );
      double close = getOpen( day );
      double value = 0;
      int shares = 0;

      if( open == 0 )
      {
         open = close;
      }

      if( close <= open )
      {
/*
         if( open <= high )
         {
            close = open;
         }
         else
*/
         {
            close = getClose( day );
         }
      }

      shares = (int)Math.floor(_Cash / open);
      value = shares * (close);
      value -= 5;
      value -= ((shares * open) + 5);

      _Worth = value;

      return value;
   } // end buy

   public double trade( int day, double threshold )
   {
      double open = getOpen( day );
      double high = getHigh( day );
      double low = getLow( day );
      double close = getClose( day );
      double difference = _HighDiff[ day ];
      double value = 0;
      int shares = 0;

      if( open == 0 )
      {
         open = close;
      }

      shares = (int)Math.floor( _Cash / open );

      if( difference >= threshold )
      {
         value = shares * (open + threshold);
         value -= 5;
         value -= ((shares * open) + 5);
      } // end if good
      else
      {
         value = shares * (close);
         value -= 5;
         value -= ((shares * open) + 5);
      } // end else (bad)

      /*
      */
      System.out.println( _StockName + " Buying: " + 
                            shares + " open: " + 
                            open + " low: " + 
                            low + " close: " + 
                            close + " value: " + 
                            close + " value: " + 
                            value + " thresh: " + threshold );

      return value;
   } // end buy

   public double trade( int day )
   {
      //double[] theDiffs = getLowDiffs();
      double open = ((Double)_Open.get(day)).doubleValue();
      double close = ((Double)_Close.get(day)).doubleValue();
      double value = 0;
      int shares = 0;

      if( open == 0 )
      {
         open = getClose( day - 1 );
      }

         // just sell at close:
         shares = (int)Math.floor(_Cash / open);
         value = shares * close;
         value -= 5;
         value -= ((shares * open) + 5);
/*
      if( theDiffs[day] <= 0.01 )
      {
         shares = (int)Math.floor(_Cash / open);
         value = shares * close;
         value -= 5;
         value -= ((shares * open) + 5);
      } // end if good
      else
      {
         shares = (int)Math.floor(_Cash / open);
         value = shares * (open - 0.03); // big assumption here.
         value -= 5;
         value -= ((shares * open) + 5);
      } // end else (bad)
*/

System.out.println( _StockName + " Buying: " + shares + " value: " + value );

      return value;
   } // end buy

   // trade based on values
   public double trade( double open, double close )
   {
      double value = 0;
      int shares = 0;

      shares = (int)Math.floor(_Cash / open);
      value = shares * close;
      value -= 5;
      value -= ((shares * open) + 5);

      return value;
   } // end buy

   public double tradeShort( int day, double threshold )
   {
      double open = ((Double)_Open.get(day)).doubleValue();
      double low  = ((Double)_Low.get(day)).doubleValue();
      double close = ((Double)_Close.get(day)).doubleValue();
      double difference = open - low;
      double value = 0;
      int shares = 0;

      if( open == 0 )
      {
         open = close;
      }

      // Regular sell:
      // [ num * (open + thresh - 5) ] - [ num * (open + 5) ]
      if( difference >= threshold )
      {
         shares = (int)Math.floor(_Cash / (open - threshold));
         value = ((shares * open) - 5);
         value -= (shares * (open - threshold)) + 5;
      } // end if good
      else
      {
         shares = (int)Math.floor(_Cash / (open - threshold));
         value = ((shares * open) - 5);
         value -= (shares * close) + 5;
      } // end else (bad)

System.out.println( _StockName + " (SHORT) Buying: " + shares + " value: " + value );
//System.out.println( _StockName + " Stop limit diff: " + (open - ((-35 + (shares * open) + 10) / shares)) );

      return value;
   } // end buy

   public double tradeShortBest( int day, double threshold )
   {
      double open = ((Double)_Open.get(day)).doubleValue();
      double low  = 0;
      double close = ((Double)_Close.get(day)).doubleValue();
      double difference = open - low;
      double value = 0;
      int shares = 0;

      if( open == 0 )
      {
         open = close;
      }

      // Regular sell:
      // [ num * (open + thresh - 5) ] - [ num * (open + 5) ]
      if( difference >= threshold )
      {
         shares = (int)Math.floor(_Cash / (open - threshold));
         value = ((shares * open) - 5);
         value -= (shares * (open - threshold)) + 5;
      } // end if good
      else
      {
         shares = (int)Math.floor(_Cash / (open - threshold));
         value = ((shares * open) - 5);
         value -= (shares * close) + 5;
      } // end else (bad)

System.out.println( _StockName + " (SHORT) thresh: " + threshold );
System.out.println( _StockName + " (SHORT BEST) Buying: " + shares + " value: " + value );

      return value;
   } // end buy

   public boolean isInitialized()
   {
      return _initialized;
   }

   public double[] getScaledOpen()
   {
      if( _ScaleOpen == null )
      {
         _ScaleOpen = scaleIt( _Open );
      }

      return _ScaleOpen;
   }

   public double[] getScaledClose()
   {
      if( _ScaleClose == null )
      {
         _ScaleClose = scaleIt( _Close );
      }

      return _ScaleClose;
   }

   public double[] getScaledHigh()
   {
      if( _ScaleHigh == null )
      {
         _ScaleHigh = scaleIt( _High );
      }

      return _ScaleHigh;
   }

   public double[] getScaledLow()
   {
      if( _ScaleLow == null )
      {
         _ScaleLow = scaleIt( _Low );
      }

      return _ScaleLow;
   }

   public double getOpen( int day )
   {
      return _Open.get( day );
   }

   public double getOpenP( int day )
   {
      return _OpenP.get( day );
   }

   public double getOpenSpreadP( int day )
   {
      return _OpenSpreadP.get( day );
   }

   public ArrayList<String> getDates()
   {
      return _Date;
   }

   public String getDate( int day )
   {
      return ((String)_Date.get( day ));
   }

   public double getHigh( int day )
   {
      return _High.get( day );
   }

   public double getHighP( int day )
   {
      return _HighP.get( day );
   }

   public double getHighSpreadP( int day )
   {
      return _HighSpreadP.get( day );
   }

   public double getLow( int day )
   {
      return _Low.get( day );
   }

   public double getLowP( int day )
   {
      return _LowP.get( day );
   }

   public double getLowSpreadP( int day )
   {
      return _LowSpreadP.get( day );
   }

   public double getDividend( int day )
   {
      return _dividends.get( day );
   }

   public double getClose( int day )
   {
      return _Close.get( day );
   }

   public double getCloseP( int day )
   {
      if( _CloseP == null || _CloseP.size() == 0 )
      {
         calculatePercentages();
      }
      return _CloseP.get( day );
   }

   public double getCloseSpreadP( int day )
   {
      return _CloseSpreadP.get( day );
   }

   public double getAdjClose( int day )
   {
      return _AdjClose.get( day );
   }

   public ArrayList<Double> getAllDividends()
   {
      return _dividends;
   }

   public ArrayList<String> getAllDividendDates()
   {
      return _dividendDate;
   }

   public ArrayList<Double> getAllCloses()
   {
      return _Close;
   }

   public ArrayList<Double> getAllClosesP()
   {
      if( _CloseP == null || _CloseP.size() == 0 )
      {
         calculatePercentages();
      }
      return _CloseP;
   }

   public ArrayList<Double> getAllAdjClosesP()
   {
      return _AdjCloseP;
   }

   protected void calculateInflation( int day )
   {
      double open = getOpen( day - 1 );
      double high = getHigh( day - 1 );
      double low = getLow( day - 1 );
      double close = getClose( day - 1 );

      double highDiff = high - open;
      double lowDiff = open - low;

      if( lowDiff == 0 )
      {
         lowDiff = 0.0001;
      }

      // calculate the total increase:
      _SMAvalue = highDiff / lowDiff;

      // compare it to stock price:
      _SMAvalue /= close;
   }

   public double tradeHighLow( int day, double highMean, double lowMean )
   {
      double open = getOpen( day ) - lowMean;
      double low = getLow( day );
      double high = getHigh( day );
      double close = getOpen( day ) + highMean;
      double value = 0;
      double most = 0;
      int shares = 0;

      if( open == 0 )
      {
         open = close;
      }

      shares = (int)Math.floor(_Cash / open);
      value = shares * (close);
      value -= 5;
      value -= ((shares * open) + 5);
 
      most = value;

      low = getLow( day );
      value = 0;
      shares = 0;

      // now the actual:

      if( open == 0 )
      {
         open = close;
      }

      if( (getOpen( day ) - lowMean) >= low )
      {
         open = getOpen( day ) - lowMean;
      }
      else
      {
         _Worth = 0;
         return 0;
      }

      high = getHigh( day + 1 );

      if( (getOpen( day + 1 ) + highMean) <= high )
      {
         close = getOpen( day + 1 ) + highMean;
      }
      else
      {
         close = getClose( day + 1 );
      }

      shares = (int)Math.floor(_Cash / open);
      value = shares * (close);
      value -= 5;
      value -= ((shares * open) + 5);

      System.out.println( _StockName + " most: " + most + " actual: " + value );

      _Worth = value;

      return value;
   } // end buy

   private void calculateDifferences()
   {
      _HighDiff = new double[ _Open.size() ];
      for( int i=0; i<_Open.size(); i++ )
      {
         _HighDiff[i] = getHigh( i ) - getOpen( i );
      }
   }

   private void calculateAverageDifferences()
   {
      _Simulations = new double[ _HighDiff.length - _NumberOfSamples ];
    
      for( int i=0; i<_Simulations.length; i++ )
      {
         double tempSum = 0;
         for( int j=i; j<_NumberOfSamples+i; j++ )
         {
            tempSum += _HighDiff[j];
         }
         _Simulations[i] = tempSum / _NumberOfSamples;
//System.out.println( _StockName + " " + _Simulations[i] );
      }
   }

   private boolean isYearHigh( int day )
   {
      int start = day - 253;
      boolean isHigh = true;
      double today = getClose( day );
      double current = 0;
      double previous = 0;

      for( int j=start; j<(day+1); j++ )
      {
         current = getClose( j );
         if( current > today )
         {
            isHigh = false;
            break;
         }
         if( current > previous )
         {
            _HighWorth++;
         }
         else
         {
            _HighWorth--;
         }
         previous = current;
      }
   
      return isHigh;
   }

   /**
    *  1) start with the entry point.
    *  2) see if next open is below the threshold.
    *  3) see if the low for the day is below thresh.
    *  4) goto next day.
    *
    *
    */
   private int findExit( int start )
   {
      int ret = 0;
      double price = getOpen( start - 1 );
      double current = 0;

      for( int j=start+1; j<_Open.size(); j++ )
      {
         current = getOpen( j );
         ret = j;
         if( current < price )
         {
            break;
         }
         price = current;
      }

      return ret;
   }

   /**
    * 1) If today is the year high.
    * 2) Get tomorrow's opening.
    * 3) Find highest value in next _DaysAbove days.
    * 4) Calc profit
    * 5) Set today 15 days later, goto 1.
    *
    *
    */
   private void performAnnualHighBuy()
   {
      double entry = 0;
      double exit = 0;
      double profit = 0;
      int exitDay = 0;

      for( int i=253; i<_Open.size(); i++ )
      {
         if(    isYearHigh( i ) == true
             && i <= ( _Open.size() - (_DaysAbove+2) ) )
         {
//System.out.println( _StockName + " entry " + i );
            entry = getOpen( i + 1 );
            //exit = findHigh( (i+2), (i+2+_DaysAbove), _High );
            exitDay = findExit( i + 3 );
//System.out.println( _StockName + " exit " + exitDay );
            exit = getOpen( exitDay );
            profit = trade( entry, exit );
            //System.out.println( _StockName + " " + i + " " + profit );

/*
            if( profit < 0 )
            {
               if( i + 2 + _DaysAbove < _Open.size() )
               {
                  exit = getOpen(i+2+_DaysAbove);
               }
               else
               {
                  exit = getOpen(i+1+_DaysAbove);
               }
               profit = trade( entry, exit );
            }

            exit = findLow( (i+2), (i+2+_DaysAbove), _Low );
            profit = trade( entry, exit );
*/
            System.out.println( _StockName + " " + i + " " + profit + " " + entry + " " + exit );

            i += _DaysAbove + 2;
         }
      }
   }

   private void performOpenGapBuy()
   {

      double prevLow = 0;
      double prevClose = 0;
      double open = 0;
      double high = 0;
      double close = 0;
      double profit = 0;

      for( int i=0; i<_Date.size(); i++ )
      {
         System.out.println( getDate( i ) );
      }

/*
      for( int i=1; i<_Open.size(); i++ )
      {
         prevLow = getLow( i - 1 );
         prevClose = getClose( i - 1 );
         open = getOpen( i );
         high = getHigh( i );

         if( open < prevLow )
         {
            if( high >= prevLow && prevLow != prevClose )
            {
/*
               close = getClose( i );
               profit = trade( prevLow, close );

               System.out.println( _StockName + " " + i + " " + profit );

               if( high > prevClose )
               {
                  profit = trade( prevLow, prevClose );
                  System.out.println( _StockName + " " + i + " " + profit );
               }
               else
               {
                  close = getClose( i );
                  profit = trade( prevLow, close );
                  System.out.println( _StockName + " " + i + " " + profit );
               }
            }
         }
      }
*/

   }

   private String getMonthName( String text )
   {
      int start = text.indexOf( "-" );
      int end = text.lastIndexOf( "-" );
      String month = text.substring( start + 1, end );
 
      return month;
   }

   private void calculateMonthlyValues()
   {

      String currentMonth = null;
      String previousMonth = null;
      int count = 0;
      //int times = 1;
      double largest = -1;
      double smallest = 10000;
      double close = -1;
      double open = -1;

      for( int i=0; i<_Date.size(); i++ )
      {
         currentMonth = getMonthName( getDate( i ) );

         // This is the first value of the new month:
         // You will need either more data or make the average count
         // (sample size) smaller if you want to use this ----> \/
         if( !currentMonth.equals( previousMonth ) /*&& (times++ == 2)*/ )
         {
            if( i == 0 )
            {
               addMonthOpen( open = getClose( i ) );

               if( open > largest )
               {
                  largest = open;
               }
               if( open < smallest )
               {
                  smallest = open;
               }
               previousMonth = currentMonth; // hell, could take this out...
               continue;
            }

            addMonthHigh( largest - open );
            addMonthLow( open - smallest );
//System.out.println( "A low is: " + (open - smallest) + " i " + i );
            largest = -1;
            smallest = 10000;
            addMonthOpen( open = getClose( i ) );
            addMonthClose( close );
            //times = 1;
         }

         close = getClose( i );

         if( close > largest )
         {
            largest = close;
         }
         if( close < smallest )
         {
            smallest = close;
         }

         previousMonth = currentMonth;
      }

      addMonthClose( close );
      addMonthHigh( largest - open );
//System.out.println( "A low is: " + (open - smallest) );
      addMonthLow( open - smallest );
   }

   private double getMonthHighAverage( int beginMonth )
   {
      int endMonth = beginMonth - _NumberOfPredictions;
      double sum = 0;

      for( int j=endMonth; j<beginMonth; j++ )
      {
         sum += getMonthHigh( j );
      }
      sum /= _NumberOfPredictions;

      return sum;
   }

   private double getMonthLowAverage( int beginMonth )
   {
      int endMonth = beginMonth - _NumberOfPredictions;
      double sum = 0;

      for( int j=endMonth; j<beginMonth; j++ )
      {
         sum += getMonthLow( j );
      }
      sum /= _NumberOfPredictions;

      return sum;
   }

   /**
    *  1) For each month, find open, high, low, close
    *  2) For the previous _NumberOfPredictions months, find average high difference
    *  3) For the current month
    *     4) Buy at the open
    *     5) Sell if high is above threshold
    *     6) Otherwise, sell at close
    * 
    *  For each year:
    *   1) Gather all positive stocks
    *   2) Stocks found in (1) move to the next year
    * 
    * 
    */
   private void performMonthlyBuy()
   {
      calculateMonthlyValues();
      double averageHigh = 0;
      double averageLow = 0;
      double thisHigh = 0;
      double thisLow = 0;
      double thisOpen = 0;
      double thisClose = 0;
      double value = 0;
      double scale = 0.3;

      ArrayList<Double> longBuys = new ArrayList<Double>();
      ArrayList<Double> shortBuys = new ArrayList<Double>();

      for( int i=_NumberOfPredictions; i<_MonthHigh.size(); i++ )
      {
         averageHigh = scale * getMonthHighAverage( i );
         averageLow = scale * getMonthLowAverage( i );
         thisOpen = getMonthOpen( i );
         thisHigh = getMonthHigh( i );
         thisLow = getMonthLow( i );
         if( thisHigh >= averageHigh )
         {
            thisClose = thisOpen + averageHigh;
         }
         else
         {
            thisClose = getMonthClose( i );
         }
         value = trade( thisOpen, thisClose );
         longBuys.add( value );
         _Worth += value;

/*
System.out.println( _StockName + " buying at: " + thisOpen + " selling at: " + thisClose + " value: " + value +
                               " high " + averageHigh + " low " + averageLow + " thislow: " + thisLow );
*/

         if( thisLow >= averageLow )
         {
            thisClose = thisOpen - averageLow;
         }
         else
         {
            thisClose = getMonthClose( i );
         }
         value = trade( thisClose, thisOpen );
         shortBuys.add( value );
         _Worth += value;

/*
System.out.println( _StockName + " shorting at: " + thisOpen + " buying at: " + thisClose + " value: " + value +
                               " high " + averageHigh + " low " + averageLow + " thislow: " + thisLow );
*/

      }

      _Worth = 0;

      for( int i=2; i<longBuys.size(); i++ )
      {

         if(    ((Double)longBuys.get( i - 2 )).doubleValue() > 0
             && ((Double)longBuys.get( i - 1 )).doubleValue() > 0 
             && ((Double)shortBuys.get( i - 2 )).doubleValue() < 0 
             && ((Double)shortBuys.get( i - 1 )).doubleValue() < 0 )
         {
            _Worth += ((Double)longBuys.get( i )).doubleValue();
         }
         else if(    ((Double)longBuys.get( i - 2 )).doubleValue() < 0
                  && ((Double)longBuys.get( i - 1 )).doubleValue() < 0 
                  && ((Double)shortBuys.get( i - 2 )).doubleValue() > 0 
                  && ((Double)shortBuys.get( i - 1 )).doubleValue() > 0 )
         {
            _Worth += ((Double)shortBuys.get( i )).doubleValue();
         }
      }

System.out.println( _StockName + " " + _Worth );
   }

   /**
    *  1) Calculation of best event threshold. [1-3, 0.1]
    *  2) Calculation of best window. [2-20]
    *  3) Calculation of mean next day low.
    *  4) Calculation of next day low variance.
    *  5) Calculation of mean window high.
    *  6) Calculation of window high variance.
    *
    */
   public void calculateEventStatistics( int size )
   {
      _eventValues = new double[ _Volumes.size() ];
      double average = 0;
      double volume = 0;

      for( int i=0; i<_Volumes.size(); i++ )
      {
         average = getVolumeAverage( i );
         volume = getVolume( i );
         _eventValues[i] = (volume / average);
      }

      double lowMean = 0;
      double lowVariance = 0;
      double temp = 0;
      double highMean = 0;
      double highVariance = 0;

      _bestHighVariance = 0;
      _highestMean = -2000000;
      _lowestMean = 2000000;

      //double bestHighVariance = 0;
      //double highestMean = -2000000;
      //double lowestMean = 2000000;

      int events = 0;

      //int bestEvents = 0;
      //double threshold = 0;
      //int window = 0;
      //int bestDayHighMean = 0;

      _bestEvents = 0;
      _Threshold = 0;
      _window = 0;
      _bestDayHighMean = 0;

      ArrayList<Double> means = null;
      ArrayList<Double> highMeans = null;

      for( double k=1.1; k<3; k+=0.1 )
      {
         for( int j=3; j<21; j++ ) // may want to increase this value
         {
            lowMean = 0;
            lowVariance = 0;
            highMean = 0;
            highVariance = 0;
            events = 0;
            means = new ArrayList<Double>();
            highMeans = new ArrayList<Double>();
            int dayHigh = 0;
            int dayHighMean = 0;

            for( int i=1; i<(size - j); i++ )
            {
               // Check for a significant event day:
               if( _eventValues[i] > k && (getClose(i) / getClose(i-1) > 1) )
               {
                  events++;
                  temp = (getLow( i + 1 ) / getClose( i ));
                  means.add( temp );
                  lowMean += temp;

                  // find the max within the window:
                  double max = -2000000;
                  for( int m=(i + 2); m<(i + j); m++ )
                  {
                     temp = getHigh( m );
                     if( temp > max )
                     {
                        dayHigh = (m - (i + 2));
                        max = temp;
                     }
                  }

                  temp = (max / getClose( i + 1 ));
                  highMeans.add( new Double( temp ) );
                  highMean += temp;
                  if( dayHigh == 0 )
                  {
                     dayHighMean += j;
                  }
                  else
                  {
                     dayHighMean += dayHigh;
                  }
               }
            }

            // now calculate the variances:
            if( events > 0 )
            {
               lowMean /= events;
               lowVariance = getVariance( means, lowMean );

               highMean /= events;
               highVariance = getVariance( highMeans, highMean );

               dayHighMean /= events;

               if( _highestMean < (highMean - highVariance) && events > 5 && events < 85 )
               {
                  _highestMean = (highMean - highVariance);
                  _lowestMean = lowMean;
                  _Threshold = k;
                  _window = j;
                  _bestDayHighMean = dayHighMean;
                  _bestHighVariance = highVariance;
                  _bestEvents = events;
               }
            }
         }
      }
/*
System.out.println( _StockName + "," + 
                    _bestEvents + "," + 
                    _highestMean + "," + 
                    _lowestMean + "," + 
                    _Threshold + "," + 
                    _window + "," + 
                    _bestDayHighMean + "," +
                    _bestHighVariance );
*/
   }

   public double getVariance( ArrayList means, double mean )
   {
      double variance = 0;
      double temp = 0;

      for( int i=0; i<means.size(); i++ )
      {
         temp = ((Double)means.get(i)).doubleValue();
         temp -= mean;
         variance += (temp * temp);
      }
      if( means.size() != 1 )
      {
         variance /= (means.size() - 1);
      }
      variance = Math.sqrt( variance );

      return variance;
   }

   public void addDividend( String date, String value )
   {
      _divExDates.add( 0, date );
      Double div = new Double( Double.parseDouble( value ) );
      _SMAvalue += 0.1 * ( div.doubleValue() - _SMAvalue );
      _dividends.add( 0, div );
   }

   public void calculateDividends()
   {
      // if the stock is not a tenured stock, then return:
      if( _divExDates.size() < 35 )
      {
         return;
      }

      String year = (String)_divExDates.get( 0 );
      if( year == null || year.trim().length() < 7 )
      {
         return;
      }

      int startYear = Integer.parseInt( year.trim().substring( year.trim().length() - 2 ) );
      int yearsInBusiness = 0;

      if( startYear < 10 )
      {
         yearsInBusiness = 4 - startYear;
      }
      else
      {
         yearsInBusiness = (100 - startYear) + 4;
      }

      int total = yearsInBusiness * 4; // 4 quarters per year
      total = (total == 0) ? total + 1 : total;
      _StandardDeviation = (double)_divExDates.size() / (double)total;

      // if they're not frequent in their payments:
      if( _StandardDeviation < 0.9 )
      {
         return;
      }

      double rateOfIncrease = ((Double)_dividends.get( _dividends.size() - 1 )).doubleValue();
      rateOfIncrease -= _SMAvalue;

      System.out.println( getName() + "," + 
                          _SMAvalue + "," + 
                          rateOfIncrease + "," + 
                          _StandardDeviation + "," + 
                          _divExDates.size() );
   }

   /**
    * The first element in the percent arrays will always be 1
    */
   public void calculatePercentages()
   {
      _closeHash = new HashMap< String, Double >();

      // initialize values
      double previous = _Open.get( 0 );
      double current = 0;
      double percent = 0;
      _OpenP.add( (double)1 );

      for( int i=1; i<_Open.size(); i++ )
      {
         current = _Open.get( i );
         percent = current / previous;
         _OpenP.add( percent );
         previous = current;
      }

      // initialize values
      current = 0;
      percent = 0;
      _OpenSpreadP.add( (double)1 );

      for( int i=1; i<_Open.size(); i++ )
      {
         current = _Open.get( i );
         previous = _Close.get( i - 1 );
         percent = current / previous;
         _OpenSpreadP.add( percent );
      }

      // initialize values
      previous = _High.get( 0 );
      current = 0;
      percent = 0;
      _HighP.add( (double)1 );

      for( int i=1; i<_High.size(); i++ )
      {
         current = _High.get( i );
         percent = current / previous;
         _HighP.add( percent );
         previous = current;
      }

      // initialize values
      current = 0;
      percent = 0;
      _HighSpreadP.add( (double)1 );

      for( int i=1; i<_High.size(); i++ )
      {
         current = _High.get( i );
         previous = _Open.get( i );
         percent = current / previous;
         _HighSpreadP.add( percent );
      }

      // initialize values
      previous = _Low.get( 0 );
      current = 0;
      percent = 0;
      _LowP.add( (double)1 );

      for( int i=1; i<_Low.size(); i++ )
      {
         current = _Low.get( i );
         percent = current / previous;
         _LowP.add( percent );
         previous = current;
      }

      // initialize values
      current = 0;
      percent = 0;
      _LowSpreadP.add( (double)1 );

      for( int i=1; i<_Low.size(); i++ )
      {
         current = _Low.get( i );
         previous = _Open.get( i );
         percent = current / previous;
         _LowSpreadP.add( percent );
      }

      // initialize values
      previous = _Close.get( 0 );
      current = 0;
      percent = 0;
      _CloseP.add( (double)1 );

      for( int i=1; i<_Close.size(); i++ )
      {
         current = _Close.get( i );
         percent = current / previous;
         _CloseP.add( percent );
         _closeHash.put( _Date.get( i ), percent );
         previous = current;
      }

      // initialize values
      current = 0;
      percent = 0;
      _CloseSpreadP.add( (double)1 );

      for( int i=1; i<_Close.size(); i++ )
      {
         current = _Close.get( i );
         previous = _Open.get( i );
         percent = current / previous;
         _CloseSpreadP.add( percent );
      }

      // initialize values
      previous = _AdjClose.get( 0 );
      current = 0;
      percent = 0;
      _AdjCloseP.add( (double)1 );

      for( int i=1; i<_AdjClose.size(); i++ )
      {
         current = _AdjClose.get( i );
         percent = current / previous;
         _AdjCloseP.add( percent );
         previous = current;
      }
   }

   /**
    * 
    */
   public double getGaussianMedian( int start, int end )
   {
      double median = 0;

      if( start < 0 )
      {
         return -1;
      }

      for( int i=start; i<end; i++ )
      {
         median += ( (double)_CloseP.get( i ) - 1 );
      }
      
      return median;
   }

   /**
    *  This method assumes that calculatePercentages has been called.
    */
   public void calculateAverages( int length )
   {
      _closeAverages = new ArrayList<Double>();
      double temp = 0;

      for( int i=(length - 1); i<_CloseP.size(); i++ )
      {
         temp = 0;
         for( int j=0; j<length; j++ )
         {
            temp += _CloseP.get( i - j );
         }
         temp /= (double)length;
         _closeAverages.add( temp );
      }
   }

   /**
    *  
    */
   public double performConstantBuySimulation( int start, int end, double buyAmount )
   {
      double previous = _Close.get( start - 1 );
      double current = 0;
      double cash = _Cash;
      int numberOfShares = 0;
      int size = (int)(cash / buyAmount );
      int position = -1;
      int[] shares = new int[ size ];

      for( int i=start; i<end; i+=1 )
      {
         current = _Close.get( i );

         // buy situation
         if( current < previous )
         {
            if( (position < (size-1)) && (buyAmount <= cash) )
            {
               shares[ ++position ] = (int)(buyAmount / current);
               cash -= (shares[ position ] * current);
            }
         }
         // sell situation
         else
         {
            if( (position > -1) )
            {
               cash += (shares[position--] * current);
            }
         }
 
         previous = current;
      }

      if( position != -1 )
      {
//         System.out.println( "Positions open, curent price is: " + current );
         while( position > 0 )
         {
//            System.out.println( shares[ position ] );
            cash += (shares[ position-- ] * current);
         }
      }

      return cash;
   }

   /**
    *  
    */
   public ArrayList<Double> getMovingAverages()
   {
      return _closeMA;
   }

   /**
    *  
    */
   public void calculateMovingAverages( int windowSize )
   {
      _window = windowSize;

      for( int i=_window; i<_Close.size(); i++ )
      {
         double average = 0;
         for( int j=i; j>(i - _window); j-- )
         {
            average += _Close.get( j );
         }

         average /= _window;
         _closeMA.add( average );
      }

   }

   /**
    *  
    */
   public ArrayList<Double> getMADifferenceSum()
   {
      return _differenceSum;
   }

   /**
    *  
    */
   public void calculateMADifferenceSum()
   {
      _differenceSum = new ArrayList<Double>();

      for( int i=(2 * _window); i<_Close.size(); i++ )
      {
         double sum = 0;
         for( int j=i; j>(i - _window); j-- )
         {
            sum += Math.abs( _Close.get( j ) - _closeMA.get( j - _window ) );
         }
         _differenceSum.add( sum );
      }
   }

   /**
    *  
    */
   public int getHigh( int start, int end )
   {
      int high = -1;
      double highValue = -1;

      for( int i=start; i<end; i++ )
      {
         if( getClose( i ) > highValue )
         {
            highValue = getClose( i );
            high = i;
         }
      }

      return high;
   }

   /**
    * Checks to see if the closes have been increasing for the last size days.
    */
   public boolean isIncreasing( int size )
   {
      boolean isInc = false;
      double previous = Double.MAX_VALUE;

      if( _Close != null && _Close.size() != 0 )
      {
         isInc = true;
         for( int i=_Close.size() - 1; i>_Close.size() - size; i-- )
         {
            double current = _Close.get( i );
            if( current > previous )
            {
               isInc = false;
               break;
            }
            previous = current;
         }
      }

      return isInc;
   }

   /**
    *
    */
   public double getStandardDeviation( int days )
   {
      StatUtilities stats = new StatUtilities();
      ArrayList< Double > data = new ArrayList< Double >();

      if( _Close == null || _Close.size() == 0 || days >= _Close.size() )
      {
         return -1;
      }

      for( int i=_Close.size() - days; i<_Close.size(); i++ )
      {
         data.add( _Close.get( i ) );
      }

      stats.calculateStats( data );
      return stats.getDeviation();
   }

   /**
    *
    */
   public double getRateOfChange( int days )
   {
      double diff = 0;

      if( _Close.size() > days )
      {
         diff = _Close.get( _Close.size() - days - 1 ) / getLastClose();
      }

      return diff;
   }

   /**
    *
    */
   public void setPreviousTrade( String value )
   {
      try
      {
         _previousTrade = Double.parseDouble( value );
      }
      catch( NumberFormatException e )
      {
         System.err.println( "Error parsing previous trade value: " + e );
      }
   }

   /**
    *
    */
   public double getPreviousTrade()
   {
      return _previousTrade;
   }

   /**
    *
    */
   public void setStrikePrice( String value )
   {
      try
      {
         _strikePrice = Double.parseDouble( value );
      }
      catch( NumberFormatException e )
      {
         System.err.println( "Error parsing strike price value: " + e );
      }
   }

   /**
    *
    */
   public double getStrikePrice()
   {
      return _strikePrice;
   }

   /**
    * Note that this calculation is for the maximum and minimum values that can be reached within
    * the passed-in window timeframe -- this is not just the value at the end of that window
    * timeframe. Thus, these values are the highs and lows the stock can statistically reach
    * with the window timeframe, not what the stock is likey to close at the end of the timeframe.
    * However, that being said, for large window timeframes, the high, low, and close are rather
    * close in proximity proportionally speaking.
    *
    * @param int Starting day offset.
    * @param int Ending day offset.
    * @param int Window timeframe -- Pass -1 as end to get data for all days.
    * @param boolean Indicator to reinitialize the data (recalculate).
    */
   public ArrayList< Double > getUnicon( int start, int end, int window )
   {
      return getUnicon( start, end, window, true );
   }

   /**
    * Note that this calculation is for the maximum and minimum values that can be reached within
    * the passed-in window timeframe -- this is not just the value at the end of that window
    * timeframe. Thus, these values are the highs and lows the stock can statistically reach
    * with the window timeframe, not what the stock is likey to close at the end of the timeframe.
    * However, that being said, for large window timeframes, the high, low, and close are rather
    * close in proximity proportionally speaking.
    *
    * @param int Starting day offset.
    * @param int Ending day offset.
    * @param int Window timeframe -- Pass -1 as end to get data for all days.
    * @param boolean Indicator to reinitialize the data (recalculate).
    */
   public ArrayList< Double > getUnicon( int start, int end, int window, boolean refresh )
   {
      if( refresh || ( _unicon == null ) )
      {
         _unicon = new ArrayList< Double >();

         if( end == -1 )
         {
            end = getSize();
         }

         for( int i=start; i<end; i+=window )
         {
            double open = getAdjClose( i );
            double maxHigh = open;
            double maxLow = open;
            double current = 0;

            for( int j=0; j<window; j++ )
            {
               if( i + j >= end )
               {
                  break;
               }
               current = getAdjClose( i + j );

               if( current > maxHigh )
               {
                  maxHigh = current;
               }
               else if( current < maxLow )
               {
                  maxLow = current;
               }
            }

            _unicon.add( ( maxHigh / open ) );
            _unicon.add( ( maxLow / open ) );
         }

         Collections.sort( _unicon );
      }

      return _unicon;
   }

   /**
    *
    */
   public int getDateIndex( String d )
   {
      int pos = -1;
      for( String dd : _Date )
      {
         pos++;
         if( d.equals( dd ) )
         {
            return pos;
         }
      }
      return -1;
   }

   /**
    * We assume that the caller has invoked calculatePercentages before calling this method.
    * 
    */
   public ArrayList< Double > getQuantile( String start, int length )
   {
      ArrayList< Double > q = new ArrayList< Double >();
      int                 s = getDateIndex( start );

      if( s != -1 )
      {
         for( int j=s; j<( s + length ); j++ )
         {
            if( j >= _CloseP.size() )
            {
               break;
            }
            q.add( _CloseP.get( j ) );
         }
         Collections.sort( q );
      }

      return q;
   }

} // end StockElement
