package openkinect.forest;

import java.util.*;
import java.math.*;

import math.*;
import common.*;
import functional.classifier.*;

/**
 *
 * In a random tree, the most classes (leaf nodes) possible is: 2^( L - 1 ) where L is the number of levels (root counts as level 1).
 * This implies that the number of decision (internal) nodes is: 2^( L - 1 ) - 1 which is suprisingly efficient! so long as the entropy is maximized.
 *                o         L = 1
 *               / \   
 *              /   \   
 *             /     \   
 *            o       o     L = 2
 *           / \     / \
 *          o   o   o   o   L = 3
 *         / \ / \ / \ / \
 * Nodes grow one at a time and if entropy is maximized, the tree should grow symmetrically and efficiently.
 * Each decision node must do the following:
 *   1) It must have a minimal set of N points to split or sort into two equally-sized groups.
 *   2) Train online new data points, yet train offline so that the system can continue to classify.
 *   3) Ancestor nodes must be trained first, only then can descendents be trained.
 *
 */
public class DecisionTreeNode implements Persistable, Runnable
{

   private String                      _name;
   private DecisionTreeNode            _parent;
   private BigInteger                  _nodeID           = BigInteger.valueOf( 1 );
   private BigInteger                  _maximumNodeID    = null;
   private BigDecimal                  _maximumClass     = BigDecimal.ONE;
   private int                         _splitSize        = 5;
   private int                         _genomeLength     = 37;
   private int                         _retrainInterval  = 10;
   private int                         _lastTrain        = 0;
   private double                      _vectorDifference = 0.5;
   private ArrayList< Double[] >       _vectors;
   private DecisionTreeNode            _leftNode;
   private DecisionTreeNode            _rightNode;
   private boolean                     _canSplit         = true;
   private boolean                     _isAsynchronous   = true;
//    private TreeMap< Integer, Integer > _nodeCounts;
//    private FunctionalBinaryClassifier  _classifier;
   private SurjectiveFunctionalBinaryClassifier  _classifier;

   /**
    *
    */
   public DecisionTreeNode()
   {
   }

   /**
    *
    */
   public DecisionTreeNode( String name )
   {
      _name       = new String( name );
      _vectors    = new ArrayList< Double[] >();
   }

   /**
    *
    */
   public DecisionTreeNode( String name, double diff, int split )
   {
      _name             = new String( name );
      _vectors          = new ArrayList< Double[] >();
      _vectorDifference = diff;
      _splitSize        = split;
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
    */
   public void setVectors( ArrayList< Double[] > vectors )
   {
      _vectors = vectors;
   }

   /**
    *
    */
   public ArrayList< Double[] > getVectors()
   {
      if( _vectors == null )
      {
         _vectors = new ArrayList< Double[] >();
      }

      return _vectors;
   }

   /**
    *
    * @param DecisionTreeNode
    */
   public void setParent( DecisionTreeNode parent )
   {
      _parent = parent;
   }

   /**
    *
    * @return DecisionTreeNode
    */
   public DecisionTreeNode getParent()
   {
      return _parent;
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
    * @param DecisionTreeNode
    */
   public synchronized void setLeftNode( DecisionTreeNode leftNode )
   {
      _leftNode = leftNode;
   }

   /**
    *
    * @return DecisionTreeNode
    */
   public DecisionTreeNode getLeftNode()
   {
      return _leftNode;
   }

   /**
    *
    * @param DecisionTreeNode
    */
   public synchronized void setRightNode( DecisionTreeNode rightNode )
   {
      _rightNode = rightNode;
   }

   /**
    *
    * @return DecisionTreeNode
    */
   public DecisionTreeNode getRightNode()
   {
      return _rightNode;
   }

   /**
    *
    * @param int
    */
   public void setNodeID( BigInteger nodeID )
   {
      _nodeID = nodeID;
   }

   /**
    *
    * @return int
    */
   public BigInteger getNodeID()
   {
      return _nodeID;
   }

   /**
    *
    * @param int
    */
   public void setRetrainInterval( int retrainInterval )
   {
      _retrainInterval = retrainInterval;
   }

   /**
    *
    * @return int
    */
   public int getRetrainInterval()
   {
      return _retrainInterval;
   }

   /**
    *
    * @param boolean
    */
   public void canSplit( boolean canSplit )
   {
      _canSplit = canSplit;
      if( _leftNode != null )
      {
         _leftNode.canSplit( _canSplit );
      }
      if( _rightNode != null )
      {
         _rightNode.canSplit( _canSplit );
      }
   }

   /**
    *
    * @return boolean
    */
   public boolean canSplit()
   {
      return _canSplit;
   }

   /**
    *
    * @param boolean
    */
   public void setIsAsynchronous( boolean isAsynchronous )
   {
      _isAsynchronous = isAsynchronous;
      if( _leftNode != null )
      {
         _leftNode.setIsAsynchronous( _isAsynchronous );
      }
      if( _rightNode != null )
      {
         _rightNode.setIsAsynchronous( _isAsynchronous );
      }
   }

   /**
    *
    * @return boolean
    */
   public boolean getIsAsynchronous()
   {
      return _isAsynchronous;
   }

   /**
    *
    * @return FunctionalBinaryClassifierDAO -- The impl class of DAOBase type.
    */
   public DAOBase getDAO()
   {
      DAOBase dao = new DecisionTreeNodeDAO();

      if( getName() != null )
      {
         dao.setFileName( getName() + ".ser" );
      }

      return dao;
   }

   /**
    *
    */
   public class DecisionTreeNodeDAO extends DAOBase< DecisionTreeNode >
   {
   }

   /**
    *
    */
   private void storeVector( double[] v )
   {
      getVectors();

      double  d      = 0;
      boolean canAdd = true;
      int c = 0;

      for( Double[] vector : _vectors )
      {
         // DEBUG ONLY >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
         System.out.println( "   " + _name + " stored postion: " + c++ );
         System.out.println( "   " + _name + " stored vector mid pos: " + vector[ vector.length / 2 ] + " incoming vector mid pos: " + v[ ( v.length / 2 ) ] );
         System.out.println( "   " + _name + " stored vector last pos: " + vector[ vector.length - 1 ] + " incoming vector last pos: " + v[ v.length - 1 ] );
         // DEBUG ONLY <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
         d = 0;
         for( int i=0; i<Math.min( v.length, vector.length ); i++ )
         {
            d += Math.abs( vector[ i ] - v[ i ] );
         }
         d /= (double) vector.length;
         System.out.println( "d: " + d );
         if( d < _vectorDifference )
         {
            // DEBUG ONLY >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            System.out.println( "" + _name + " will not store vector." );
            // DEBUG ONLY <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
            canAdd = false;
            break;
         }
      }

      if( canAdd )
      {
         // DEBUG ONLY >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
         System.out.println( "" + _name + " storing a new vector..." );
//          System.out.println( "a vector length: " + v.length );
//          for( int i=0; i<v.length; i++ )
//          {
//             System.out.println( "a vector value: " + v[ i ] );
//          }
         // DEBUG ONLY <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
//          System.out.println( "Before storing a vector size: " + _vectors.size() );
         _vectors.add( MathUtilities.deepCopy( v ) );
//          System.out.println( "After storing a vector size: " + _vectors.size() );
//          System.out.println( "After storing a vector: " + toString() );
      }
   }

   /**
    *
    */
   private void handleSplit()
   {
      System.out.println( "In DecisionTreeNode::handleSplit()" );
      if( _isAsynchronous )
      {
         ( new Thread( this ) ).start();
         System.out.println( "kicked off thread..." );
      }
      else
      {
         System.out.println( "calling run synchronously..." );
         run();
      }
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void train()
   {
      System.out.println( "in train method for node: " + _nodeID );
      if( _canSplit == false )
      {
         System.out.println( "re-training node: " + _nodeID );
         long                  s            = System.currentTimeMillis();
         double[][]            targets      = new double[ _vectors.size() ][];
         ArrayList< Double[] > leftVectors  = new ArrayList< Double[] >();
         ArrayList< Double[] > rightVectors = new ArrayList< Double[] >();

         for( int i=0; i<_vectors.size(); i++ )
         {
            Double[] vec = _vectors.get( i );
            for( int j=0; j<vec.length; j++ )
            {
               targets[ i ][ j ] = vec[ j ];
            }
         }

         _classifier.setTargets( targets );
         _classifier.train();

         if(    _leftNode.canSplit()  == false 
             && _rightNode.canSplit() == false )
         {
            for( int i=0; i<_vectors.size(); i++ )
            {
               if( _classifier.classify( targets[ i ] ) )
               {
                  leftVectors.add( MathUtilities.deepCopy( targets[ i ] ) );
               }
               else
               {
                  rightVectors.add( MathUtilities.deepCopy( targets[ i ] ) );
               }
            }

            _leftNode.setVectors( leftVectors );
            _rightNode.setVectors( rightVectors );

            System.out.println( "kicking off thread for node " + _leftNode.getNodeID() );
            System.out.println( "kicking off thread for node " + _rightNode.getNodeID() );
            ( new Thread( _leftNode )  ).start();
            ( new Thread( _rightNode ) ).start();
         }

         System.out.println( "finished re-training node: " + _nodeID + " time: " + ( System.currentTimeMillis() - s ) );
      }
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void run()
   {
//       System.out.println( "In DecisionTreeNode::run()" );
//       if( _canSplit == false )
//       {
//          train();
//       }
//       else
      {
         _canSplit   = false;
//          _classifier = new FunctionalBinaryClassifier( _name + ".classifier" );
//       System.out.println( "Creating SurjectiveFunctionalBinaryClassifier" );
         _classifier = new SurjectiveFunctionalBinaryClassifier( _name + ".classifier" );
//       System.out.println( "Created SurjectiveFunctionalBinaryClassifier" );
         // The default halting criteria is 1 hour:
         // _classifier.setHaltingCriteria( criteria );

//       System.out.println( "_vectors.size: " + _vectors.size() );
         double[][] targets = new double[ _vectors.size() ][];

         for( int i=0; i<_vectors.size(); i++ )
         {
//       System.out.println( "i: " + i );
            Double[] vec = _vectors.get( i );
            targets[ i ] = new double[ vec.length ];
//       System.out.println( "vec.length(): " + vec.length );
            for( int j=0; j<vec.length; j++ )
            {
//       System.out.println( "vec[ j ]: " + vec[ j ] );
               targets[ i ][ j ] = vec[ j ];
            }
         }
//       System.out.println( "Setting targets" );

         _classifier.setTargets( targets );
         _classifier.setGenomeLength( _genomeLength );
         long s = System.currentTimeMillis();
//       System.out.println( "Starting at: " + s );
         System.out.println( "Calling train" );
         _classifier.train();

         // DEBUG ONLY >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
         System.out.println( _name + " finished training node: " + _nodeID + " time: " + ( System.currentTimeMillis() - s ) );
         // DEBUG ONLY <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

         BigInteger id = _nodeID.shiftLeft( 1 );
         DecisionTreeNode left  = new DecisionTreeNode( _name + "-" + id, _vectorDifference, _splitSize );
         left.setNodeID( id );
         left.setParent( this );
         left.setIsAsynchronous( _isAsynchronous );

         BigInteger ie = id.add( BigInteger.ONE );
         DecisionTreeNode right = new DecisionTreeNode( _name + "-" + ie, _vectorDifference, _splitSize );
         right.setNodeID( ie );
         right.setParent( this );
         right.setIsAsynchronous( _isAsynchronous );

         setLeftNode( left );
         setRightNode( right );
          
         _vectors = null;

//          if( _parent != null )
//          {
//             _parent.registerLeafNode( _nodeID, left.getNodeID(), right.getNodeID() );
//          }
//          else
//          {
//             registerLeafNode( _nodeID, left.getNodeID(), right.getNodeID() );
//          }
      }
   }

   /**
    *
   private TreeMap< Integer, Integer > getNodeCounts()
   {
      if( _nodeCounts == null )
      {
         _nodeCounts = new TreeMap< Integer, Integer >();
      }
      return _nodeCounts;
   }
    */

   /**
    *
    * @param TYPE
    * @return TYPE
   public void registerLeafNode( int parent, int left, int right )
   {
      if( _parent == null )
      {
         synchronized( this )
         {
            getNodeCounts().remove( new Integer( parent ) );
            getNodeCounts().put( new Integer( left ),  new Integer( 0 ) );
            getNodeCounts().put( new Integer( right ), new Integer( 0 ) );
            // DEBUG ONLY >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            System.out.println( _name + " node count map is now:" );
            Set< Integer > nodes = getNodeCounts().keySet();
            for( Integer node : nodes )
            {
               System.out.println( "  node: " + node + " count: " + getNodeCounts().get( node ) );
            }
            // DEBUG ONLY <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
         }
      }
      else
      {
         _parent.registerLeafNode( parent, left, right );
      }
   }
    */

   /**
    *
    */
   public double getMappedClass( BigInteger c )
   {
      BigDecimal n = new BigDecimal( c );
      BigDecimal d = n.divide( _maximumClass, MathContext.DECIMAL128 );

      return d.doubleValue();

//       Set< Integer > nodes    = getNodeCounts().keySet();
//       int            position = 0;
// 
//       for( Integer node : nodes )
//       {
//          position++;
//          if( node == c )
//          {
//             break;
//          }
//       }
// 
//       if( getNodeCounts().size() == 0 )
//       {
//          return 1.0;
//       }
// 
//       double m = (double) position / (double) getNodeCounts().size();
// 
//       return m;
   }

   /**
    *
    */
   private /*synchronized*/ void updateNodeCount( BigInteger c )
   {
//       Integer node  = new Integer( c );
//       Integer count = getNodeCounts().get( node );
//       count++;
//       getNodeCounts().put( node, count );
      
      BigInteger nodeID = new BigInteger( c.toByteArray() );

      if( _maximumNodeID == null || nodeID.compareTo( _maximumNodeID ) == 1 )
      {
         _maximumNodeID = nodeID;
         _maximumClass  = new BigDecimal( _maximumNodeID );
      }
   }

   /**
    *
   private void handleRetrain()
   {
      if( ( _vectors.size() - _lastTrain ) > _retrainInterval )
      {
         _lastTrain  = _vectors.size();
         _nodeCounts = null;
         ( new Thread( this ) ).start();
      }
   }
    */

   /**
    * When a class value is computed, it is shifted to the left by one bit, or equivalently
    * multiplying by two. So for instance, when an ID is computed, it could be something
    * like 2, then when it splits, left will be 2 * 2 and right will be 2 * 2 + 1.
    * Looking at the binary representation, the original will be 0010 while left will be:
    * 0100 and right will be 0101. Thus, the first highest-order bits will have the same
    * form while the lowest-order bits are the ones that change, but from the class level,
    * i.e., the BigInteger value, the entire number is growing without any noticeable 
    * pattern for a human readable value. Here, we convert each octet into a hex value and
    * shift it as the pattern grows. For instance, suppose the above value continued to grow
    * from 5 to 92 thus the binary grew from 0101 to 0101:1100. Then this tree address would
    * grow from 5 to 5:c -- the 5 pattern will always be present (as would the c as octets
    * are added).
    */
   public String getTreeAddress( BigInteger v )
   {
      BigInteger   v2      = v;
      StringBuffer address = new StringBuffer();
      int          l       = v.bitLength();
      int          a       = l / 4;
      int          b       = l % 4;

      for( int i=0; i<a; i++ )
      {
         v2      =  v.shiftRight( l - 4 );
         address.append( ":" + v2.toString( 16 ) );
         v2      =  v.clearBit( l - 4 ).clearBit( l - 3 ).clearBit( l - 2 ).clearBit( l - 1 );
         l       -= 4;
         v       =  v2;
      }

      if( b > 0 )
      {
         address.append( ":" + v2.toString( 16 ) );
      }

      return address.substring( 1 );
   }

   /**
    *
    */
   public BigInteger classify( double[] vector )
   {
      BigInteger c = null;
//       System.out.println( "canSplit 1: " + _canSplit );

      if( _canSplit )
      {
         storeVector( vector );
      }

//       synchronized( this )
//       {
//          System.out.println( "canSplit 2: " + _canSplit );
         if( _canSplit && _vectors.size() > _splitSize )
         {
            System.out.println( "Calling handleSplit" );
            handleSplit();
         }
//       }

      if( _leftNode != null && _rightNode != null )
      {
         if( _classifier.classify( vector ) )
         {
            c = _leftNode.classify( vector );
            if( _parent == null )
            {
               updateNodeCount( c );
//                handleRetrain();
            }
            return c;
         }
         else
         {
            c = _rightNode.classify( vector );
            if( _parent == null )
            {
               updateNodeCount( c );
//                handleRetrain();
            }
            return c;
         }
      }

      return _nodeID;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public String toString()
   {
      StringBuffer result = new StringBuffer();
      result.append( "split size: " + _splitSize + " genome length: " + _genomeLength + " canSplit: " + _canSplit + "\n" );

      if( _vectors != null && _vectors.size() > 0 )
      {
         for( Double[] obj : _vectors )
         {
            for( int i=0; i<obj.length; i++ )
            {
               result.append( obj[ i ] );
               result.append( " " );
            }
            result.append( "\n" );
         }
      }

      return result.toString();
   }


   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public static void main( String[] args )
   {
      DecisionTreeNode root = new DecisionTreeNode( "root", 0.05, 5 );
      MathUtilities    mu   = new MathUtilities();
      int              n    = 20;
      int              s    = 20;

//       root.setRetrainInterval( 15 );

      for( int j=0; j<n; j++ )
      {
         double[]    vector = mu.normalize( mu.getRandomArray( s ), 1.0 );
         BigInteger  c      = root.classify( vector );
         System.out.println( "Test vector " + j + " gives class: " + c );
         double      m      = root.getMappedClass( c );
         System.out.println( "Test vector " + j + " gives mapped value: " + m );
      }

      try { Thread.sleep( 5000 ); } catch( InterruptedException e ) { }

      for( int j=0; j<n; j++ )
      {
         double[]    vector = mu.normalize( mu.getRandomArray( s ), 1.0 );
         BigInteger  c      = root.classify( vector );
         System.out.println( "Test vector " + j + " gives class: " + c );
         double      m      = root.getMappedClass( c );
         System.out.println( "Test vector " + j + " gives mapped value: " + m );
      }

      try { Thread.sleep( 5000 ); } catch( InterruptedException e ) { }

      for( int j=0; j<n; j++ )
      {
         double[]    vector = mu.normalize( mu.getRandomArray( s ), 1.0 );
         BigInteger  c      = root.classify( vector );
         System.out.println( "Test vector " + j + " gives class: " + c );
         double      m      = root.getMappedClass( c );
         System.out.println( "Test vector " + j + " gives mapped value: " + m );
      }

      System.out.println( "at end of tests..." );
      try { Thread.sleep( 15000 ); } catch( InterruptedException e ) { }
   }

}
