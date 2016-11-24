package stock;

import java.util.*;
import java.io.*;
import common.*;

public class StockParameterData
{
 
   int    _consequtive = -1;
   int    _state       = -1;
   int    _depth       = -1;
   double _threshold   = -1;

   int     _wins          = 0;
   int     _losses        = 0;
   int     _count         = 0;
   double  _ratio         = -2000000;
   double  _area          = -1;
   double  _mass          = -1;
   double  _curvature     = -1;
   double  _worth         = 0;
   double[] _MACDSum      = null;
   double[] _MACDDiffSum  = null;
   double[] _StochasticSum  = null;
   double[] _StochasticDiffSum  = null;
   double[][] _theIntervals       = null;
   boolean _isExponential = false;

   public StockParameterData()
   {
   } // end default constructor

   public StockParameterData( int cons, int depth, double thresh )
   {
      _consequtive = cons;
      _depth = depth;
      _threshold = thresh;
   } // end constructor

   public StockParameterData( double[] macd, double[] macdSum, double[] stochSum, double[] stochDSum )
   {
      _MACDSum = macd;
      _MACDDiffSum = macdSum;
      _StochasticSum = stochSum;
      _StochasticDiffSum = stochDSum;
   } // end constructor

   public StockParameterData( int state, double[][] intervals )
   {
      _state        = state;
      _theIntervals = intervals;
      _StochasticDiffSum  = new double[ _theIntervals.length ];
      _StochasticSum      = new double[ _theIntervals.length ];
   } // end constructor

   public double[] getMACDSum()
   {
      return _MACDSum;
   }

   public double[] getMACDDiffSum()
   {
      return _MACDDiffSum;
   }

   public double[] getStochasticSum()
   {
      return _StochasticSum;
   }

   public double[] getStochasticDiffSum()
   {
      return _StochasticDiffSum;
   }

   public int getWins()
   {
      return _wins;
   }

   public int getLosses()
   {
      return _losses;
   }

   public double getMass()
   {
      return _mass;
   }

   public double getArea()
   {
      return _area;
   }

   public double getRatio()
   {
      if( _count > 0 )
      {
         _ratio = _wins / _count;
      }

      return _ratio;
   }

   public double getWorth()
   {
      return _worth;
   }

   // Check to see if it is an increasing sequence of length, len.
   // While we're here, let's compute a few values
   private boolean isLength( ArrayList theList, int len )
   {
      boolean isLength = false;

      if( theList == null || theList.size() < (len + 2) )
      {
         return false;
      }

      double previous = 0;
      double current = 0;
      double curvature = 0;
      double temp = 0;
      double mass = 0;
      boolean isFirst = true;
      int count = 0;
      int points = 0;

      for( int i=(theList.size() - 2); i>theList.size() - (len + 2); i-- )
      {
         previous = ((Double)theList.get( i - 1 )).doubleValue();
         current = ((Double)theList.get( i )).doubleValue();

//System.out.println( "current: " + current );
//System.out.println( "previous: " + previous );

         if( current > previous )
         {
            count++;
//System.out.println( "count: " + count );
         }
         else
         {
            break;
         }

         mass += current;
         points++;

         if( !isFirst )
         {
//System.out.println( "computing curve: " );
            // Compute Curvature:
            // y_3 - 2 * y_1 + y_2
            // First, get y_2:
            temp = ((Double)theList.get( i + 1 )).doubleValue();
            temp += previous;
            temp -= 2 * current;
            curvature += temp;
            /*
            if( temp < 0 )
            {
               isExp = false;
            }
            */
         } // end if not first one
         else
         {
            isFirst = false;
         } // end if not first one

      } // end backwards for loop

      if( count >= len )
      {
//System.out.println( "found conseq!" );
         isLength = true;
         // set attributes here:
         _mass += 0.5 * ( mass - _mass );
         _area += 0.5 * ( ( mass / points ) - _area );
         _curvature += 0.5 * ( curvature - _curvature );
      }
  
      return isLength;
   }

   public void addDataPoint( ArrayList theData )
   {

      if( isLength( theData, 1 ) == false )
      {
//System.out.println( "returning" );
         return;
      }

//System.out.println( "here" );

      // If we get here, we have computed mass, area, curvature as well.

      double amount = ((Double)theData.get( theData.size() - 1 )).doubleValue() -
                      ((Double)theData.get( theData.size() - 2 )).doubleValue();

      _worth += amount;
      _count++;

      if( amount > 0 )
      {
         _wins++;
      }
      else
      {
         _losses++;
      }

   } // end addDataPoint

   // ( left, right ]
   public int addProbability( int value, double slope )
   {
      int state = 0;

      for( int i=0; i<_theIntervals.length; i++ )
      {
         if(    slope >  _theIntervals[i][0] 
             && slope <= _theIntervals[i][1] )
         {
             _StochasticDiffSum[ i ] += value;
             state = i;
             break;
         }
         else if(    (    i == (_theIntervals.length - 1)
                       && slope >  _theIntervals[ i ][ 1 ] )
                  || (    i == 0
                       && slope < _theIntervals[ i ][ 0 ] )
                 )
         {
            _StochasticDiffSum[ i ] += value;
            state = i;
            break;
         }
      }

      return state;
   }

   public void normalize()
   {
      double sum = 0;

      //double[][][] state = new double[3][4][5];

      // first, find total:
      for( int i=0; i<_StochasticDiffSum.length; i++ )
      {
         sum += _StochasticDiffSum[ i ];
      }

      // then, normalize total:
      if( sum != 0 )
      {
         for( int i=0; i<_StochasticDiffSum.length; i++ )
         {
            _StochasticDiffSum[ i ] /= sum;
         }
      }

      for( int i=0; i<_StochasticDiffSum.length; i++ )
      {
         System.out.println( _state + " " + i + " " + _StochasticDiffSum[ i ] );
      }
   }

   public double[] getProbabilityArray()
   {
      double[] theArray = new double[ _StochasticDiffSum.length ];
      double probability = 0;

      for( int i=0; i<_StochasticDiffSum.length; i++ )
      {
         if( _StochasticDiffSum[ i ] != 0 )
         {
            probability += _StochasticDiffSum[ i ];
            theArray[ i ] = probability;
         }
      }

      return theArray;
   } // end getProbabilityArray

   public double[] calculateCenterOfMass()
   {
      double sum = 0;
      _MACDSum = new double[ 3 ];
      // _MACDSum[0] contains left-most COM
      // _MACDSum[1] contains COM
      // _MACDSum[2] contains right-most COM

      // First, compute COM:
      for( int i=0; i<_StochasticDiffSum.length; i++ )
      {
         _StochasticSum[ i ] = (i + 1) * _StochasticDiffSum[ i ];
         _MACDSum[ 1 ] += _StochasticSum[ i ];
      }

      int com = (int)Math.round( _MACDSum[1] );
      int left = com + 1;
      int right = com;
      
      if( com == 0 )
      {
         left = 0;
      }
      else if( com == _StochasticDiffSum.length )
      {
         right = _StochasticDiffSum.length;
         left = com;
      }

      // Next, compute left COM:
      for( int i=0; i<left; i++ )
      {
         _MACDSum[ 0 ] += _StochasticSum[ i ];
         sum += _StochasticDiffSum[ i ];
      }

      if( sum > 0 )
      {
         _MACDSum[ 0 ] /= sum;
      } // end if
      else
      {
         _MACDSum[ 0 ] = _MACDSum[1];
      } // end if

      sum = 0;

      // Next, compute right COM:
      for( int i=right; i<_StochasticDiffSum.length; i++ )
      {
         _MACDSum[ 2 ] += _StochasticSum[ i ];
         sum += _StochasticDiffSum[ i ];
      }

      if( sum > 0 )
      {
         _MACDSum[ 2 ] /= sum;
      } // end if
      else
      {
         _MACDSum[ 2 ] = _MACDSum[1];
      } // end if

      _MACDSum[ 0 ] -= 1;
      _MACDSum[ 1 ] -= 1;
      _MACDSum[ 2 ] -= 1;

      return _MACDSum;
   } // end calculateCenterOfMass

   // Worth, Ratio, Curvature, Area, Mass, Cons, Depth, Threashold
   public String toString()
   {
      StringBuffer str = new StringBuffer( "State: " + _state );

      for( int i=0; i<_StochasticDiffSum.length; i++ )
      {
         str.append( "\n state: " + i + " " + _StochasticDiffSum[i] );
      }

      return str.toString();
   }

} // end StockParameterData
