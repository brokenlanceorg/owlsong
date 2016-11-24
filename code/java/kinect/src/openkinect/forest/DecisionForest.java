package openkinect.forest;

import math.*;
import common.*;
import grafix.*;

import java.util.*;
import java.math.*;

/**
 * The idea here is that this class will aggregate decision trees backed by a binary functional classifier, which
 * is, in turn, backed by a functional correlation object.
 * Each tree will classify the same set of data or vectors, but by the sheer size of the phase space of the 
 * functional correlations, each will be classifying by slightly different parameters determined by the
 * genetic algorithm.
 *
 */
public class DecisionForest implements Persistable, Runnable
{

   private String                        _name;
   private int                           _forestSize;
   private int                           _splitSize;
   private int                           _treeGenomeLength = 37;
   private int                           _treeNumber       = 0;
   private double                        _vectorDifference;
   private boolean                       _isAsynchronous   = true;
   private ArrayList< DecisionTreeNode > _forest;
   private double[]                      _input;
   private String[]                      _addresses;
   private BigInteger[]                  _classes;

   /**
    *
    */
   public DecisionForest()
   {
   }

   /**
    *
    */
   public DecisionForest( String name )
   {
      _name = name;
   }

   /**
    * @param String -- name of the forest for the persistence file.
    * @param int    -- size of the forest, i.e., number of trees.
    * @param double -- the difference between distinct vectors in a class.
    * @param int    -- the number of vectors in a class to trigger a split.
    */
   public DecisionForest( String name, int n, double diff, int split )
   {
      _name             = name;
      _forestSize       = n;
      _vectorDifference = diff;
      _splitSize        = split;
      initializeForest();
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public DecisionForest clone( double[] vector, int tree )
   {
      DecisionForest clone = new DecisionForest( _name, _forestSize, _vectorDifference, _splitSize );
      clone.setForest( getForest() );
      clone.setInput( vector );
      clone.setTreeNumber( tree );
      clone.setClasses( _classes );
      clone.setAddresses( _addresses );

      return clone;
   }

   /**
    *
    */
   private void initializeForest()
   {
      _forest    = new ArrayList< DecisionTreeNode >();
      _addresses = new String[ _forestSize ];
      _classes   = new BigInteger[ _forestSize ];

      for( int i=0; i<_forestSize; i++ )
      {
         _forest.add( new DecisionTreeNode( ( _name + ".DT-" + i ), _vectorDifference, _splitSize ) );
      }
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void run()
   {
      DecisionTreeNode tree     = _forest.get( _treeNumber );
      _classes[ _treeNumber ]   = tree.classify( _input );
      _addresses[ _treeNumber ] = tree.getTreeAddress( _classes[ _treeNumber ] );
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public BigInteger[] classify( double[] vector )
   {
      Thread[] workers = new Thread[ _forestSize ];

      System.out.println( "forest size is: " + _forestSize );

      for( int i=0; i<_forestSize; i++ )
      {
         workers[ i ] = new Thread( clone( vector, i ) );
         workers[ i ].start();
         System.out.println( "kicked off thread: " + i );
      }

      try
      {
         for( int i=0; i<_forestSize; i++ )
         {
            workers[ i ].join();
            System.out.println( "joined with thread: " + i );
         }
      }
      catch( InterruptedException e )
      {
         System.err.println( "Caught InterruptedException: " + e );
      }

      return _classes;

//       BigInteger[]     v    = new BigInteger[ _forestSize ];
//       DecisionTreeNode tree = null;
// 
//       for( int i=0; i<_forestSize; i++ )
//       {
//          tree            = _forest.get( i );
//          v[ i ]          = tree.classify( vector );
//          _addresses[ i ] = tree.getTreeAddress( v[ i ] );
//       }
// 
//       return v;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public String[] getTreeAddress( BigInteger treeClass )
   {
      String[]         v    = new String[ _forestSize ];
      DecisionTreeNode tree = null;

      for( int i=0; i<_forestSize; i++ )
      {
         tree   = _forest.get( i );
         v[ i ] = tree.getTreeAddress( treeClass );
      }

      return v;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public String[] getTreeAddress( BigInteger[] treeClasses )
   {
      String[]         v    = new String[ _forestSize ];
      DecisionTreeNode tree = null;

      for( int i=0; i<_forestSize; i++ )
      {
         tree   = _forest.get( i );
         v[ i ] = tree.getTreeAddress( treeClasses[ i ] );
      }

      return v;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public String[] getTreeAddresses()
   {
      return _addresses;
   }

   /**
    *
    * @return FunctionalBinaryClassifierDAO -- The impl class of DAOBase type.
    */
   public DAOBase getDAO()
   {
      DAOBase dao = new DecisionForestDAO();

      if( getName() != null )
      {
         dao.setFileName( getName() + ".ser" );
      }

      return dao;
   }

   /**
    *
    */
   public class DecisionForestDAO extends DAOBase< DecisionForest >
   {
   }

   /**
    *
    * @param private String
    */
   public void setName( String name )
   {
      _name = name;
   }

   /**
    *
    * @return private String
    */
   public String getName()
   {
      return _name;
   }

   /**
    *
    * @param int
    */
   public void setForestSize( int forestSize )
   {
      _forestSize = forestSize;
   }

   /**
    *
    * @return int
    */
   public int getForestSize()
   {
      return _forestSize;
   }

   /**
    *
    * @param int
    */
   public void setTreeGenomeLength( int treeGenomeLength )
   {
      _treeGenomeLength = treeGenomeLength;
      for( DecisionTreeNode tree : _forest )
      {
         tree.setGenomeLength( _treeGenomeLength );
      }
   }

   /**
    *
    * @return int
    */
   public int getTreeGenomeLength()
   {
      return _treeGenomeLength;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void setCanSplit( boolean split )
   {
      for( DecisionTreeNode obj : _forest )
      {
         obj.canSplit( split );
      }
   }

   /**
    *
    * @param boolean
    */
   public void setIsAsynchronous( boolean synch )
   {
      for( DecisionTreeNode obj : _forest )
      {
         obj.setIsAsynchronous( synch );
      }
   }

   /**
    *
    * @param ArrayList< DecisionTreeNode >
    */
   public void setForest( ArrayList< DecisionTreeNode > forest )
   {
      _forest = forest;
   }

   /**
    *
    * @return ArrayList< DecisionTreeNode >
    */
   public ArrayList< DecisionTreeNode > getForest()
   {
      return _forest;
   }

   /**
    *
    * @param double[]
    */
   public void setInput( double[] input )
   {
      _input = input;
   }

   /**
    *
    * @return double[]
    */
   public double[] getInput()
   {
      return _input;
   }

   /**
    *
    * @param int
    */
   public void setTreeNumber( int treeNumber )
   {
      _treeNumber = treeNumber;
   }

   /**
    *
    * @return int
    */
   public int getTreeNumber()
   {
      return _treeNumber;
   }

   /**
    *
    * @param BigInteger[]
    */
   public void setClasses( BigInteger[] classes )
   {
      _classes = classes;
   }

   /**
    *
    * @return BigInteger[]
    */
   public BigInteger[] getClasses()
   {
      return _classes;
   }

   /**
    *
    * @param String[]
    */
   public void setAddresses( String[] addresses )
   {
      _addresses = addresses;
   }

   /**
    *
    * @return String[]
    */
   public String[] getAddresses()
   {
      return _addresses;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public String toString()
   {
      String value = _name + "\n";

      value += "   Forest Size:             " + _forestSize + "\n";
      value += "   Tree Node Split Size:    " + _splitSize + "\n";
      value += "   Tree Node Genome Length: " + _treeGenomeLength + "\n";
      value += "   Tree Node Vector Diff:   " + _vectorDifference + "\n";
      if( _forest != null && _forest.size() > 0 )
      {
         for( DecisionTreeNode tree : _forest )
         {
            value += "        Decision Tree:    " + tree + "\n";
         }
      }

      return value;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public static void main( String[] args )
   {
      MathUtilities  mu   = new MathUtilities();
      int            n    = 20;
      int            s    = 200;

      DecisionForest root = ( DecisionForest ) ( new DecisionForest( "test-forest" ) ).getDAO().deserialize();

      if( root == null )
      {
//          root = new DecisionForest( "test-forest", 1, 0.3, 3 );
         // If using the quantile representation of the data vectors, make the distance smaller
         // since we're computing distance component-wise (otherwise the distance will be larger).
//          root = new DecisionForest( "test-forest", 1, 0.005, 3 );
         // Set this to an error distance of 0 and then just set canSplit to false when no training is desired.
         root = new DecisionForest( "test-forest", 5, 0.05, 3 );
         root.setIsAsynchronous( false );
      }

      // setting to true in case this was persisted in the false state.
      root.setCanSplit( true );

      for( int j=0; j<n; j++ )
      {
         double[]            vector = mu.normalize( mu.getRandomArray( s ), 1.0 );
//          ArrayList< Double > list   = mu.toArrayList( vector );
//          Collections.sort( list );
//          vector = mu.toArray( list );
         System.out.println( "test vector is: " );
         for( int k=0; k<vector.length; k++ )
         {
            double v = vector[ k ];
            System.out.print( v + " : " );
         }
         BigInteger[] c      = root.classify( vector );
         String[]     a      = root.getTreeAddress( c );
         System.out.print( "\nTest vector (1)" + j + " --> " );
         for( int k=0; k<a.length; k++ )
         {
            System.out.print( a[ k ] + " " );
         }
         System.out.println( "" );
      }

//       System.out.println( "sleeping..." );
//       try { Thread.sleep( 5000 ); } catch( InterruptedException e ) { }

      root.setCanSplit( false );

      for( int j=0; j<n; j++ )
      {
         double[]            vector = mu.normalize( mu.getRandomArray( s ), 1.0 );
//          ArrayList< Double > list   = mu.toArrayList( vector );
//          Collections.sort( list );
//          vector = mu.toArray( list );
         BigInteger[] c      = root.classify( vector );
         String[]     a      = root.getTreeAddress( c );
         System.out.print( "\nTest vector (2)" + j + " --> " );
         for( int k=0; k<a.length; k++ )
         {
            System.out.print( a[ k ] + " " );
         }
         System.out.println( "" );
      }

//       System.out.println( "sleeping..." );
//       try { Thread.sleep( 5000 ); } catch( InterruptedException e ) { }
      System.out.println( "at end of tests..." );

      root.getDAO().serialize( root );
   }

}
