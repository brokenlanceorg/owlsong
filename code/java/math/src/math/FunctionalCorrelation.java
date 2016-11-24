package math;

import common.*;
import java.util.*;

/**
 *  The idea behind this class is the notion of correlation. So, for example if
 * two data series are correlated in some way, then the correlation coefficient will
 * be high or low. The data streams will be composed in various functions to produce
 * or correlate with a result dataset. The problem is how to represent an aribitrary
 * functional of all input datasets to represent the output dataset.
 *
 * Some of the data are:
 * Math.E
 * Math.PI
 * Miscellaneous data streams
 * 
 * Math unary methods: 
 * sqrt( double )
 * cbrt( double)
 * cos( double )
 * cosh( double )
 * sin( double )
 * sinh( double )
 * tan( double )
 * tanh( double )
 * exp( double )
 * log( double )
 * log10( double )
 * 
 * Math binary methods: 
 * hypot( double , double )
 * pow( double , double )
 * *
 * /
 * +
 *    2^n - 1
 * To create a fully balanced binary tree, you'll need this many nodes for the respective levels:
 * 1  1
 * 2  3
 * 3  7
 * 4  15
 * 5  31
 * 6  63
 * 7  127
 * 8  255
 * 9  511
 * 10 1023
 * 11 2047
 * 12 4095
 * 13 8191
 * 14 16383
 * 15 32767
 * 16 65535
 * 17 131071
 * 18 262143
 * 19 524287
 * 20 1048575
 * The genome size should be 2 * x - 1 where x is the number of leaves in the tree.
 */
public class FunctionalCorrelation implements Persistable
{

   private FunctionalCorrelation _root;
   private FunctionalCorrelation _left;
   private FunctionalCorrelation _right;
   private double                _dataValue = 0;
   private int                   _numberOfNodes;
   private int                   _nodeID;
   private int                   _numberOfStreams;
   private double[][]            _dataStreams;
   private double[]              _weights;
   private HashSet< Integer >    _variables;
   private HashSet< Integer >    _dependentVariables;

   /**
    *
    */
   public FunctionalCorrelation()
   {
   }

   /**
    *
    */
   public FunctionalCorrelation( ArrayList<Double> list, int node, double[][] streams )
   {
      // These must be set first
      _numberOfNodes = list.size();
      _nodeID = node;
      setDataStreams( streams );
      if( node < list.size() )
      {
         _dataValue = list.get( node - 1 );
      }

      if( (2 * node) < list.size() )
      {
         _left = new FunctionalCorrelation( list, (2 * node), _dataStreams );
      }

      if( ((2 * node) + 1) < list.size() )
      {
         _right = new FunctionalCorrelation( list, ((2 * node) + 1), _dataStreams );
      }
   }

   /**
    *
    */
   public static FunctionalCorrelation buildBinaryTree( ArrayList<Double> list, double[][] streams )
   {
      return ( new FunctionalCorrelation( list, 1, streams ) );
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public HashSet< Integer > getDependentVariables()
   {
      if( _dependentVariables == null )
      {
         _dependentVariables = new HashSet< Integer >();
         determineDependentVariables( _dependentVariables );
      }
      return _dependentVariables;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   private void determineDependentVariables( HashSet< Integer > vars )
   {
      HashSet< Integer > leftVars = new HashSet< Integer >();
      HashSet< Integer > rightVars = new HashSet< Integer >();
      if( _left != null )
      {
         _left.determineDependentVariables( leftVars );
      }
      if( _right != null )
      {
         _right.determineDependentVariables( rightVars );
      }

      // If we're a leaf node and we're still an operator, let's become a data stream:
      if( _left == null && _right == null && _dataValue < 0.5 )
      {
         _dataValue += 0.5;
      }
      // otherwise, if we're an interior node and we're a data stream, become an operator:
      else if( _left != null && _right != null && _dataValue >= 0.5 )
      {
         _dataValue -= 0.5;
      }

      double interval = 0.016666667;

      // Operator
      if( _dataValue >= 0 && _dataValue < 0.5 )
      {
         if( _dataValue >= 0 && _dataValue < (1 * interval) )
         {
            vars.addAll( leftVars );
         }
         else if( _dataValue >= (1 * interval) && _dataValue < (2 * interval) )
         {
            vars.addAll( leftVars );
         }
         else if( _dataValue >= (2 * interval) && _dataValue < (3 * interval) )
         {
            vars.addAll( leftVars );
         }
         else if( _dataValue >= (3 * interval) && _dataValue < (4 * interval) )
         {
            vars.addAll( leftVars );
         }
         else if( _dataValue >= (4 * interval) && _dataValue < (5 * interval) )
         {
            vars.addAll( leftVars );
         }
         else if( _dataValue >= (5 * interval) && _dataValue < (6 * interval) )
         {
            vars.addAll( leftVars );
         }
         else if( _dataValue >= (6 * interval) && _dataValue < (7 * interval) )
         {
            vars.addAll( leftVars );
         }
         else if( _dataValue >= (7 * interval) && _dataValue < (8 * interval) )
         {
            vars.addAll( leftVars );
         }
         else if( _dataValue >= (8 * interval) && _dataValue < (9 * interval) )
         {
            vars.addAll( leftVars );
         }
         else if( _dataValue >= (9 * interval) && _dataValue < (10 * interval) )
         {
            vars.addAll( leftVars );
         }
         else if( _dataValue >= (10 * interval) && _dataValue < (11 * interval) )
         {
            vars.addAll( leftVars );
         }
         else if( _dataValue >= (11 * interval) && _dataValue < (12 * interval) )
         {
            vars.addAll( leftVars );
         }
         else if( _dataValue >= (12 * interval) && _dataValue < (13 * interval) )
         {
            vars.addAll( leftVars );
         }
         else if( _dataValue >= (13 * interval) && _dataValue < (14 * interval) )
         {
            vars.addAll( leftVars );
         }
         else if( _dataValue >= (14 * interval) && _dataValue < (15 * interval) )
         {
            vars.addAll( leftVars );
         }
         else if( _dataValue >= (15 * interval) && _dataValue < (16 * interval) )
         {
            vars.addAll( leftVars );
         }
         else if( _dataValue >= (16 * interval) && _dataValue < (17 * interval) )
         {                          
            vars.addAll( leftVars );
         }                          
         else if( _dataValue >= (17 * interval) && _dataValue < (18 * interval) )
         {                          
            vars.addAll( leftVars );
         }                          
         else if( _dataValue >= (18 * interval) && _dataValue < (19 * interval) )
         {                          
            vars.addAll( leftVars );
         }                          
         else if( _dataValue >= (19 * interval) && _dataValue < (20 * interval) )
         {                          
            vars.addAll( leftVars );
         }                          
         else if( _dataValue >= (20 * interval) && _dataValue < (21 * interval) )
         {                          
            vars.addAll( leftVars );
         }                          
         else if( _dataValue >= (21 * interval) && _dataValue < (22 * interval) )
         {                          
            vars.addAll( leftVars );
            vars.addAll( rightVars );
         }                          
         else if( _dataValue >= (22 * interval) && _dataValue < (23 * interval) )
         {                          
            vars.addAll( leftVars );
            vars.addAll( rightVars );
         }                          
         else if( _dataValue >= (23 * interval) && _dataValue < (24 * interval) )
         {                          
            vars.addAll( leftVars );
            vars.addAll( rightVars );
         }                          
         else if( _dataValue >= (24 * interval) && _dataValue < (25 * interval) )
         {                          
            vars.addAll( leftVars );
            vars.addAll( rightVars );
         }                          
         else if( _dataValue >= (25 * interval) && _dataValue < (26 * interval) )
         {                          
            vars.addAll( leftVars );
            vars.addAll( rightVars );
         }                          
         else if( _dataValue >= (26 * interval) && _dataValue < (27 * interval) )
         {                          
            vars.addAll( leftVars );
            vars.addAll( rightVars );
         }                          
         else if( _dataValue >= (27 * interval) && _dataValue < (28 * interval) )
         {                          
            vars.addAll( leftVars );
            vars.addAll( rightVars );
         }                          
         else if( _dataValue >= (28 * interval) && _dataValue < (29 * interval) )
         {                          
            vars.addAll( leftVars );
            vars.addAll( rightVars );
         }                          
         else if( _dataValue >= (29 * interval) && _dataValue < (30 * interval) )
         {                          
            vars.addAll( leftVars );
            vars.addAll( rightVars );
         }                          
      }
      // Data Line
      else
      {
         double leftEndpoint = 0.5;
         int numberOfSteps = _numberOfStreams + 2;
         double stepSize = 0.5 / (double)numberOfSteps;
         double rightEndpoint = leftEndpoint + stepSize;

         for( int i=0; i<numberOfSteps; i++ )
         {
            if( _dataValue >= leftEndpoint && _dataValue < rightEndpoint )
            {
               if( i == (numberOfSteps - 1) )
               {
//                   result = Math.E;
                  vars.add( new Integer( -1 ) );
               }
               else if( i == (numberOfSteps - 2) )
               {
//                   result = Math.PI;
                  vars.add( new Integer( -2 ) );
               }
               else
               {
//                   result = _dataStreams[ i ][ index ];
                  vars.add( new Integer( i ) );
               }
               break;
            }
            rightEndpoint += stepSize;
            leftEndpoint += stepSize;
         }
      }
   }

   /**
    *
    */
   public HashSet< Integer > getVariables()
   {
      if( _variables == null )
      {
         _variables = new HashSet< Integer >();
         determineVariables( _variables );
      }
      return _variables;
   }

   /**
    *
    */
   private void determineVariables( HashSet< Integer > vars )
   {
      if( _left != null ) 
      {
         _left.determineVariables( vars );
      }
      if ( _right != null ) 
      {
         _right.determineVariables( vars );
      }

      // If we're a leaf node and we're still an operator, let's become a data stream:
      if( _left == null && _right == null && _dataValue < 0.5 )
      {
         _dataValue += 0.5;
      }
      // otherwise, if we're an interior node and we're a data stream, become an operator:
      else if( _left != null && _right != null && _dataValue >= 0.5 )
      {
         _dataValue -= 0.5;
      }

      double interval = 0.016666667;

      // Operator
      if( _dataValue >= 0 && _dataValue < 0.5 )
      {
         // nop
      }
      // Data Line
      else
      {
         double leftEndpoint = 0.5;
         int numberOfSteps = _numberOfStreams + 2;
         double stepSize = 0.5 / (double)numberOfSteps;
         double rightEndpoint = leftEndpoint + stepSize;

         for( int i=0; i<numberOfSteps; i++ )
         {
            if( _dataValue >= leftEndpoint && _dataValue < rightEndpoint )
            {
               if( i == (numberOfSteps - 1) )
               {
                  // nop
               }
               else if( i == (numberOfSteps - 2) )
               {
                  // nop
               }
               else
               {
                  vars.add( i );
               }
               break;
            }
            rightEndpoint += stepSize;
            leftEndpoint += stepSize;
         }
      }
   }

   /**
    *
    */
   public double getDataValue()
   {
      return _dataValue;
   }

   /**
    *
    */
   public void setWeights( double[] weights )
   {
//       System.out.println( "Setting weights 2 size: " + weights.length );
      _weights = weights;
   }

   /**
    *
    */
   public void setWeights( ArrayList< Double > weights )
   {
//       System.out.println( "Setting weights 3 size: " + weights.size() );
//       _weights = new double[ weights.size() ];
//       for( int i=0; i<weights.size(); i++ )
//       {
//          _weights[ i ] = weights.get( i );
//       }
   }

   /**
    *
    */
   public double[] getWeights()
   {
      return _weights;
   }

   /**
    * The datastreams are setup as a matrix. However, the usual matrix orientation
    * is such that it is streams[ row ][ col ]. That would seem natural in that 
    * each row would be a training instance and the column respresent the variable.
    * In this case, it is the reverse because the evaluate method uses it as:
    *     result = _dataStreams[ i ][ index ];
    * which implies that, since index represents the training instance, the rows
    * represent the independent variables while the columns represent the different
    * instances of training data.
    */
   public void setDataStreams( double[][] streams )
   {
      _dataStreams = streams;
      _numberOfStreams = streams.length;
      if( _left != null )
      {
         _left.setDataStreams( streams );
      }
      if( _right != null )
      {
         _right.setDataStreams( streams );
      }
//       if( _weights == null )
//       {
// //          System.out.println( "Setting weights 1 size: " + _numberOfNodes );
//          _weights = new double[ _numberOfNodes ];
//          for( int i=0; i<_numberOfNodes; i++ )
//          {
//             _weights[ i ] = 1.0;
//          }
//       }
   }

   /**
    * The idea is that the data value will range from .00 to .99.
    * There are three possibilities: unused, data lines or operators, while
    * operators can be unary or binary.
    * There are 16 operators and at least 2 data streams
    *
    *
    * @param int -- The index position to pull from data streams.
    */
   public double evaluate( int index )
   {
//       if( _left != null )
//       {
//          _left.setWeights( _weights );
//       }
//       if( _right != null )
//       {
//          _right.setWeights( _weights );
//       }
      double result = Double.NaN;
      double left = (_left != null) ? _left.evaluate( index ) : Double.NaN;
      double right = (_right != null) ? _right.evaluate( index ) : Double.NaN;

      // If we're a leaf node and we're still an operator, let's become a data stream:
      if( _left == null && _right == null && _dataValue < 0.5 )
      {
         _dataValue += 0.5;
      }
      // otherwise, if we're an interior node and we're a data stream, become an operator:
      else if( _left != null && _right != null && _dataValue >= 0.5 )
      {
         _dataValue -= 0.5;
      }

      double interval = 0.016666667;

      // Operator
      if( _dataValue >= 0 && _dataValue < 0.5 )
      {
         if( _dataValue >= 0 && _dataValue < (1 * interval) )
         {
            result = Math.abs( left );
         }
         else if( _dataValue >= (1 * interval) && _dataValue < (2 * interval) )
         {
            result = Math.floor( left );
         }
         else if( _dataValue >= (2 * interval) && _dataValue < (3 * interval) )
         {
            result = Math.ceil( left );
         }
         else if( _dataValue >= (3 * interval) && _dataValue < (4 * interval) )
         {
            result = Math.cbrt( left );
         }
         else if( _dataValue >= (4 * interval) && _dataValue < (5 * interval) )
         {
            result = Math.sqrt( Math.abs( left ) );
         }
         else if( _dataValue >= (5 * interval) && _dataValue < (6 * interval) )
         {
            if( left == 0 )
            {
               left += 1;
            }
            result = Math.log( Math.abs( left ) );
         }
         else if( _dataValue >= (6 * interval) && _dataValue < (7 * interval) )
         {
            result = Math.log1p( left );
         }
         else if( _dataValue >= (7 * interval) && _dataValue < (8 * interval) )
         {
            if( left == 0 )
            {
               left += 1;
            }
            result = Math.log10( Math.abs( left ) );
         }
         else if( _dataValue >= (8 * interval) && _dataValue < (9 * interval) )
         {
            result = Math.atan( left );
         }
         else if( _dataValue >= (9 * interval) && _dataValue < (10 * interval) )
         {
            result = Math.tanh( left );
         }
         else if( _dataValue >= (10 * interval) && _dataValue < (11 * interval) )
         {
            result = Math.sin( left );
         }
         else if( _dataValue >= (11 * interval) && _dataValue < (12 * interval) )
         {
            result = Math.cos( left );
         }
         else if( _dataValue >= (12 * interval) && _dataValue < (13 * interval) )
         {
            result = Math.acos( left );
         }
         else if( _dataValue >= (13 * interval) && _dataValue < (14 * interval) )
         {
            result = Math.cosh( left );
         }
         else if( _dataValue >= (14 * interval) && _dataValue < (15 * interval) )
         {
            result = Math.sinh( left );
         }
         else if( _dataValue >= (15 * interval) && _dataValue < (16 * interval) )
         {
            result = Math.tan( left );
         }
         else if( _dataValue >= (16 * interval) && _dataValue < (17 * interval) )
         {                          
            result = Math.asin( left );
         }                          
         else if( _dataValue >= (17 * interval) && _dataValue < (18 * interval) )
         {                          
            result = Math.exp( left );
         }                          
         else if( _dataValue >= (18 * interval) && _dataValue < (19 * interval) )
         {                          
            result = Math.expm1( left );
         }                          
         else if( _dataValue >= (19 * interval) && _dataValue < (20 * interval) )
         {                          
            result = Math.toRadians( left );
         }                          
         else if( _dataValue >= (20 * interval) && _dataValue < (21 * interval) )
         {                          
            result = Math.toDegrees( left );
         }                          
         else if( _dataValue >= (21 * interval) && _dataValue < (22 * interval) )
         {                          
            result = left + right;
         }                          
         else if( _dataValue >= (22 * interval) && _dataValue < (23 * interval) )
         {                          
            result = left * right;
         }                          
         else if( _dataValue >= (23 * interval) && _dataValue < (24 * interval) )
         {                          
            if( right != 0 )
            {
               result = left / right;
            }
         }                          
         else if( _dataValue >= (24 * interval) && _dataValue < (25 * interval) )
         {                          
            result = Math.hypot( left, right );
         }                          
         else if( _dataValue >= (25 * interval) && _dataValue < (26 * interval) )
         {                          
            result = Math.atan2( left, right );
         }                          
         else if( _dataValue >= (26 * interval) && _dataValue < (27 * interval) )
         {                          
            result = Math.pow( left, right );
         }                          
         else if( _dataValue >= (27 * interval) && _dataValue < (28 * interval) )
         {                          
            result = Math.max( left, right );
         }                          
         else if( _dataValue >= (28 * interval) && _dataValue < (29 * interval) )
         {                          
            result = Math.min( left, right );
         }                          
         else if( _dataValue >= (29 * interval) && _dataValue < (30 * interval) )
         {                          
            result = Math.IEEEremainder( left, right );
         }                          
      }
      // Data Line
      else
      {
         double leftEndpoint = 0.5;
         int numberOfSteps = _numberOfStreams + 2;
         double stepSize = 0.5 / (double)numberOfSteps;
         double rightEndpoint = leftEndpoint + stepSize;

         for( int i=0; i<numberOfSteps; i++ )
         {
            if( _dataValue >= leftEndpoint && _dataValue < rightEndpoint )
            {
               if( i == (numberOfSteps - 1) )
               {
                  result = Math.E;
               }
               else if( i == (numberOfSteps - 2) )
               {
                  result = Math.PI;
               }
               else
               {
//                   result = ( _weights[ i ] * _dataStreams[ i ][ index ] );
                  result = _dataStreams[ i ][ index ];
               }
               break;
            }
            rightEndpoint += stepSize;
            leftEndpoint += stepSize;
         }
      }

//       return ( _weights[ _nodeID ] * result );
      return result;
   }

   /**
    * The idea is that the data value will range from .00 to .99.
    * There are three possibilities: unused, data lines or operators, while
    * operators can be unary or binary.
    * There are 16 operators and at least 2 data streams
    *
    *
    * @param int -- The index position to pull from data streams.
    */
   public double evaluate( double argument )
   {
      double result = 0;
      double left = (_left != null) ? _left.evaluate( argument ) : 0;
      double right = (_right != null) ? _right.evaluate( argument ) : 0;

      // If we're a leaf node and we're still an operator, let's become a data stream:
      if( _left == null && _right == null && _dataValue < 0.5 )
      {
         _dataValue += 0.5;
      }
      // otherwise, if we're an interior node and we're a data stream, become an operator:
      else if( _left != null && _right != null && _dataValue >= 0.5 )
      {
         _dataValue -= 0.5;
      }

      double interval = 0.016666667;
// System.out.println( "evaluating: " + argument + " the datavalue is: " + _dataValue );

      // Operator
      if( _dataValue >= 0 && _dataValue < 0.5 )
      {
         if( _dataValue >= 0 && _dataValue < (1 * interval) )
         {
            result = left * right;
         }
         else if( _dataValue >= (1 * interval) && _dataValue < (2 * interval) )
         {
            result = Math.hypot( left, right );
         }
         else if( _dataValue >= (2 * interval) && _dataValue < (3 * interval) )
         {
            result = Math.abs( left );
         }
         else if( _dataValue >= (3 * interval) && _dataValue < (4 * interval) )
         {
            result = Math.acos( left );
         }
         else if( _dataValue >= (4 * interval) && _dataValue < (5 * interval) )
         {
            result = Math.asin( left );
         }
         else if( _dataValue >= (5 * interval) && _dataValue < (6 * interval) )
         {
            result = Math.atan( left );
         }
         else if( _dataValue >= (6 * interval) && _dataValue < (7 * interval) )
         {
            result = Math.atan2( left, right );
         }
         else if( _dataValue >= (7 * interval) && _dataValue < (8 * interval) )
         {
            result = Math.ceil( left );
         }
         else if( _dataValue >= (8 * interval) && _dataValue < (9 * interval) )
         {
            result = Math.floor( left );
         }
         else if( _dataValue >= (9 * interval) && _dataValue < (10 * interval) )
         {
            result = Math.expm1( left );
         }
         else if( _dataValue >= (10 * interval) && _dataValue < (11 * interval) )
         {
            result = Math.IEEEremainder( left, right );
         }
         else if( _dataValue >= (11 * interval) && _dataValue < (12 * interval) )
         {
            result = Math.log1p( left );
         }
         else if( _dataValue >= (12 * interval) && _dataValue < (13 * interval) )
         {
            result = Math.max( left, right );
         }
         else if( _dataValue >= (13 * interval) && _dataValue < (14 * interval) )
         {
            result = Math.min( left, right );
         }
         else if( _dataValue >= (14 * interval) && _dataValue < (15 * interval) )
         {
            result = Math.toDegrees( left );
         }
         else if( _dataValue >= (15 * interval) && _dataValue < (16 * interval) )
         {
            result = Math.toRadians( left );
         }
         else if( _dataValue >= (16 * interval) && _dataValue < (17 * interval) )
         {                          
            result = Math.pow( left, right );
         }                          
         else if( _dataValue >= (17 * interval) && _dataValue < (18 * interval) )
         {                          
            result = Math.sqrt( Math.abs( left ) );
         }                          
         else if( _dataValue >= (18 * interval) && _dataValue < (19 * interval) )
         {                          
            result = Math.cbrt( left );
         }                          
         else if( _dataValue >= (19 * interval) && _dataValue < (20 * interval) )
         {                          
            result = Math.cos( left );
         }                          
         else if( _dataValue >= (20 * interval) && _dataValue < (21 * interval) )
         {                          
            result = Math.cosh( left );
         }                          
         else if( _dataValue >= (21 * interval) && _dataValue < (22 * interval) )
         {                          
            if( right != 0 )
            {
               result = left / right;
            }
         }                          
         else if( _dataValue >= (22 * interval) && _dataValue < (23 * interval) )
         {                          
            result = Math.sin( left );
         }                          
         else if( _dataValue >= (23 * interval) && _dataValue < (24 * interval) )
         {                          
            result = Math.sinh( left );
         }                          
         else if( _dataValue >= (24 * interval) && _dataValue < (25 * interval) )
         {                          
            result = Math.tan( left );
         }                          
         else if( _dataValue >= (25 * interval) && _dataValue < (26 * interval) )
         {                          
            result = Math.tanh( left );
         }                          
         else if( _dataValue >= (26 * interval) && _dataValue < (27 * interval) )
         {                          
            result = Math.exp( left );
         }                          
         else if( _dataValue >= (27 * interval) && _dataValue < (28 * interval) )
         {                          
            if( left == 0 )
            {
               left += 1;
            }
            result = Math.log( Math.abs( left ) );
         }                          
         else if( _dataValue >= (28 * interval) && _dataValue < (29 * interval) )
         {                          
            if( left == 0 )
            {
               left += 1;
            }
            result = Math.log10( Math.abs( left ) );
         }                          
         else if( _dataValue >= (29 * interval) && _dataValue < (30 * interval) )
         {                          
            result = left + right;
         }                          
      }
      // Data Line
      else 
      {
         double leftEndpoint = 0.5;
         int numberOfSteps = _numberOfStreams + 2;
         double stepSize = 0.5 / (double)numberOfSteps;
         double rightEndpoint = leftEndpoint + stepSize;

         for( int i=0; i<numberOfSteps; i++ )
         {
            if( _dataValue >= leftEndpoint && _dataValue < rightEndpoint )
            {
               if( i == (numberOfSteps - 1) )
               {
                  result = Math.E;
               }
               else if( i == (numberOfSteps - 2) )
               {
                  result = Math.PI;
               }
               else
               {
// System.out.println( "returning the arg: " + argument );
                  result = argument;
               }
               break;
            }
            rightEndpoint += stepSize;
            leftEndpoint += stepSize;
         }
      }

      return result;
   }

   /**
    *
    */
   public String toString( int depth )
   {
      String result = "\n";

//       if( depth == 0 )
//       {
//          result += "Independent Variables:";
//          for( Integer i : getVariables() )
//          {
//             result += ( "\n" + i );
//          }
//          result += "\n";
//       }

      String padding = "";
      for( int i=0; i<depth; i++ )
      {
         padding += " ";
      }

      // If we're a leaf node and we're still an operator, let's become a data stream:
      if( _left == null && _right == null && _dataValue < 0.5 )
      {
         _dataValue += 0.5;
      }
      // otherwise, if we're an interior node and we're a data stream, become an operator:
      else if( _left != null && _right != null && _dataValue >= 0.5 )
      {
         _dataValue -= 0.5;
      }

      result += padding + "DataValue is: " + getDataValue();
      double interval = 0.016666667;

      // Operator
      if( _dataValue >= 0 && _dataValue < 0.5 )
      {
         if( _dataValue >= 0 && _dataValue < (1 * interval) )
         {
            result += " operator: abs( double ) ";
         }
         else if( _dataValue >= (1 * interval) && _dataValue < (2 * interval) )
         {
            result += " operator: floor( double ) ";
         }
         else if( _dataValue >= (2 * interval) && _dataValue < (3 * interval) )
         {
            result += " operator: ceil( double ) ";
         }
         else if( _dataValue >= (3 * interval) && _dataValue < (4 * interval) )
         {
            result += " operator: cbrt( double ) ";
         }
         else if( _dataValue >= (4 * interval) && _dataValue < (5 * interval) )
         {
            result += " operator: sqrt( abs( double ) ) ";
         }
         else if( _dataValue >= (5 * interval) && _dataValue < (6 * interval) )
         {
            result += " operator: log( abs( double ) ) ";
         }
         else if( _dataValue >= (6 * interval) && _dataValue < (7 * interval) )
         {
            result += " operator: log1p( double ) ";
         }
         else if( _dataValue >= (7 * interval) && _dataValue < (8 * interval) )
         {
            result += " operator: log10( double ) ";
         }
         else if( _dataValue >= (8 * interval) && _dataValue < (9 * interval) )
         {
            result += " operator: atan( double ) ";
         }
         else if( _dataValue >= (9 * interval) && _dataValue < (10 * interval) )
         {
            result += " operator: tanh( double ) ";
         }
         else if( _dataValue >= (10 * interval) && _dataValue < (11 * interval) )
         {
            result += " operator: sin( double ) ";
         }
         else if( _dataValue >= (11 * interval) && _dataValue < (12 * interval) )
         {
            result += " operator: cos( double ) ";
         }
         else if( _dataValue >= (12 * interval) && _dataValue < (13 * interval) )
         {
            result += " operator: acos( double ) ";
         }
         else if( _dataValue >= (13 * interval) && _dataValue < (14 * interval) )
         {
            result += " operator: cosh( double ) ";
         }
         else if( _dataValue >= (14 * interval) && _dataValue < (15 * interval) )
         {
            result += " operator: sinh( double ) ";
         }
         else if( _dataValue >= (15 * interval) && _dataValue < (16 * interval) )
         {
            result += " operator: tan( double ) ";
         }
         else if( _dataValue >= (16 * interval) && _dataValue < (17 * interval) )
         {                          
            result += " operator: asin( double ) ";
         }                          
         else if( _dataValue >= (17 * interval) && _dataValue < (18 * interval) )
         {                          
            result += " operator: exp( double ) ";
         }                          
         else if( _dataValue >= (18 * interval) && _dataValue < (19 * interval) )
         {                          
            result += " operator: expm1( double ) ";
         }                          
         else if( _dataValue >= (19 * interval) && _dataValue < (20 * interval) )
         {                          
            result += " operator: toRadians( double ) ";
         }                          
         else if( _dataValue >= (20 * interval) && _dataValue < (21 * interval) )
         {                          
            result += " operator: toDegrees( double ) ";
         }                          
         else if( _dataValue >= (21 * interval) && _dataValue < (22 * interval) )
         {                          
            result += " operator: + ";
         }                          
         else if( _dataValue >= (22 * interval) && _dataValue < (23 * interval) )
         {                          
            result += " operator: * ";
         }                          
         else if( _dataValue >= (23 * interval) && _dataValue < (24 * interval) )
         {                          
            result += " operator: / ";
         }                          
         else if( _dataValue >= (24 * interval) && _dataValue < (25 * interval) )
         {                          
            result += " operator: hypot( double, double ) ";
         }                          
         else if( _dataValue >= (25 * interval) && _dataValue < (26 * interval) )
         {                          
            result += " operator: atan2( double, double ) ";
         }                          
         else if( _dataValue >= (26 * interval) && _dataValue < (27 * interval) )
         {                          
            result += " operator: pow( double, double ) ";
         }                          
         else if( _dataValue >= (27 * interval) && _dataValue < (28 * interval) )
         {                          
            result += " operator: max( double, double ) ";
         }                          
         else if( _dataValue >= (28 * interval) && _dataValue < (29 * interval) )
         {                          
            result += " operator: min( double, double ) ";
         }                          
         else if( _dataValue >= (29 * interval) && _dataValue < (30 * interval) )
         {                          
            result += " operator: IEEERemainder( double, double ) ";
         }                          
      }
      // Data Line
      else 
      {
         double leftEndpoint = 0.5;
         int numberOfSteps = _numberOfStreams + 2;
         double stepSize = 0.5 / (double)numberOfSteps;
         double rightEndpoint = leftEndpoint + stepSize;

         for( int i=0; i<numberOfSteps; i++ )
         {
            if( _dataValue >= leftEndpoint && _dataValue < rightEndpoint )
            {
               if( i == (numberOfSteps - 1) )
               {
                  result += " data: e ";
               }
               else if( i == (numberOfSteps - 2) )
               {
                  result += " data: pi ";
               }
               else
               {
                  result += " data: " + i;
               }
               break;
            }
            rightEndpoint += stepSize;
            leftEndpoint += stepSize;
         }
      }

//       result += " (" + _weights[ _nodeID ] + ")";
      result += "\n";

      if( _left != null )
      {
         result += padding + "left node ==> " + _left.toString( depth + 1 );
      }
      if( _right != null )
      {
         result += padding + "right node ==> " + _right.toString( depth + 1 );
      }

      return result;
   }

   /**
    *
    * @return int -- the total number of independent variables.
    */
   public int getNumberOfStreams()
   {
      return _numberOfStreams;
   }

   /**
    *
    * @return int -- the number of nodes in the functional tree.
    */
   public int getNumberOfNodes()
   {
      return _numberOfNodes;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public DAOBase getDAO()
   {
      return new FunctionalCorrelationDAO();
   }

   /**
    *
    */
   public class FunctionalCorrelationDAO extends DAOBase< FunctionalCorrelation >
   {
   }

   /**
    *
    */
   public static void main( String[] args )
   {
      // First, we read in the genome
      common.FileReader reader = new FileReader( "genome.dat", " " );
      String[] words = reader.getArrayOfWords();
      ArrayList<Double> temp = new ArrayList<Double>();

      for( int i=0; i<words.length; i++ )
      {
         try
         {
            temp.add( Double.parseDouble( words[i] ) );
         }
         catch( NumberFormatException e )
         {
            System.err.println( "Caught exception reading genome: " + e );
         }
      }

      // Next, we read in the data variables
      reader = new FileReader( "variables.dat", " " );
      words = reader.getArrayOfWords();
//       ArrayList< ArrayList < Double > > streams = new ArrayList< ArrayList< Double > >();
      int        numberOfVariables = Integer.parseInt( words[ 0 ] );
      int        numberOfDataPoints = (numberOfVariables == 1) ? (words.length - 1) : (words.length) / numberOfVariables;
      double[][] streams = new double[ numberOfVariables ][ numberOfDataPoints ];
      double[]   t = null;
      int        count = 1;

      System.out.println( "The number of variables is: " + numberOfVariables );
      System.out.println( "The number of data points is: " + numberOfDataPoints );

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
//          streams.add( (new MathUtilities()).normalize( temp, Math.PI ) );
         streams[ i ] = (new MathUtilities()).normalize( t, 1 );
      }
      
      FunctionalCorrelation root = FunctionalCorrelation.buildBinaryTree( temp, streams ); 
      System.out.println( "The root tree is: " + root );

//       root.setDataStreams( streams );

      System.out.println( "evaluate( i ) is: " );
      for( int i=0; i<numberOfDataPoints; i++ )
      {
         System.out.println( root.evaluate( i ) );
      }

      System.out.println( "" );
      System.out.println( "iterate evaluate( double ) is: " );
//       double value = root.evaluate( 0.5 );
      double value = root.evaluate( 1 );
      for( int i=0; i<numberOfDataPoints; i++ )
      {
         value = root.evaluate( value );
         System.out.println( value );
      }

      // Test of the Inteval class
//       ArrayList< Double > data = new ArrayList< Double >();
//       for( int i=0; i<10; i++ )
//       {
//          data.add( Math.random() );
//          System.out.println( "data item: " + data.get( i ) );
//       }
//       StatUtilities util = new StatUtilities();
//       util.calculateStats( data );
//       System.out.println( "stats are: " + util );
// 
//       Sequence seq = new Sequence( data );
//       ArrayList< ArrayList < Double > > sequences = seq.getTransposedSequence( 3 );
//       for( ArrayList< Double > list : sequences )
//       {
//          System.out.println( "\na list:" );
//          for( Double item : list )
//          {
//             System.out.print( item + " " );
//          }
//       }

   }
}
