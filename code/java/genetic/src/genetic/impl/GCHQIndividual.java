package genetic.impl;

import genetic.*;
import grafix.*;
import math.*;
import ifs.*;

import java.math.*;
import java.util.*;

/**
 * 
 */
public class GCHQIndividual extends Individual
{
   private int                  _i        = 0;
   private ArrayList< Double >  _genotype = null;
   private ArrayList< Integer > _row      = null;

   /**
    * Default constructor.
    * Genome: 
    */
   public GCHQIndividual()
   {
      setImplementationClass( "genetic.impl.GCHQIndividual" );
      setGenomeLength( 155 );
   }

   /**
    * Row is zero-based.
    */
   private String getRow( int r )
   {
      StringBuffer s = new StringBuffer();

      for( int i=0; i<25; i++ )
      {
         if( _genotype.get( ( r * 25 ) + i ) < 0.5 )
         {
            s.append( "a" );
         }
         else
         {
            s.append( "b" );
         }
      }

      return s.toString();
   }

   /**
    *
    */
   private String getColumn( int c )
   {
      StringBuffer s = new StringBuffer();

      for( int j=0; j<25; j++ )
      {
         if( _genotype.get( ( j * 25 ) + c ) < 0.5 )
         {
            s.append( "a" );
         }
         else
         {
            s.append( "b" );
         }
      }

      return s.toString();
   }

   /**
    * 
    */
   private void addPadding()
   {
      int s = 0;
      for( Integer i : _row )
      {
         s += i;
      }
      s += ( _row.size() - 1 );
      // the number of spaces we need to add:
      int   x = 25 - s;
      int[] X = new int[ _row.size() + 1 ];

      for( int i=0; i<x; i++ )
      {
         X[ (int) Math.round( ( X.length - 1 ) *  _myGenotype.get( _i++ ) ) ] += 1;
      }

      for( int j=0; j<X.length; j++ )
      {
         for( int k=0; k<X[ j ]; k++ )
         {
            _genotype.add( 0.0 );
         }
         if( j < _row.size() )
         {
            for( int r=0; r<_row.get( j ); r++ )
            {
               _genotype.add( 1.0 );
            }
            if( j < ( _row.size() - 1 )  )
            {
               _genotype.add( 0.0 );
            }
         }
      }

      _row = new ArrayList< Integer >();
   }

   /**
    *
    */
   private void setValue( int r, int c )
   {
      _genotype.set( ( ( r * 25 ) + c ), 1.0 );
   }

   /**
    *
    */
   private void buildGenotype()
   {
      _i        = 0;
      _genotype = new ArrayList< Double >();
      _row      = new ArrayList< Integer >();

      _row.add( 7 );
      _row.add( 3 );
      _row.add( 1 );
      _row.add( 1 );
      _row.add( 7 );
      addPadding();
      _row.add( 1 );
      _row.add( 1 );
      _row.add( 2 );
      _row.add( 2 );
      _row.add( 1 );
      _row.add( 1 );
      addPadding();
      _row.add( 1 );
      _row.add( 3 );
      _row.add( 1 );
      _row.add( 3 );
      _row.add( 1 );
      _row.add( 1 );
      _row.add( 3 );
      _row.add( 1 );
      addPadding();
      _row.add( 1 );
      _row.add( 3 );
      _row.add( 1 );
      _row.add( 1 );
      _row.add( 6 );
      _row.add( 1 );
      _row.add( 3 );
      _row.add( 1 );
      addPadding();
      _row.add( 1 );
      _row.add( 3 );
      _row.add( 1 );
      _row.add( 5 );
      _row.add( 2 );
      _row.add( 1 );
      _row.add( 3 );
      _row.add( 1 );
      addPadding();
      _row.add( 1 );
      _row.add( 1 );
      _row.add( 2 );
      _row.add( 1 );
      _row.add( 1 );
      addPadding();
      _row.add( 7 );
      _row.add( 1 );
      _row.add( 1 );
      _row.add( 1 );
      _row.add( 1 );
      _row.add( 1 );
      _row.add( 7 );
      addPadding();
      _row.add( 3 );
      _row.add( 3 );
      addPadding();
      _row.add( 1 );
      _row.add( 2 );
      _row.add( 3 );
      _row.add( 1 );
      _row.add( 1 );
      _row.add( 3 );
      _row.add( 1 );
      _row.add( 1 );
      _row.add( 2 );
      addPadding();
      _row.add( 1 );
      _row.add( 1 );
      _row.add( 3 );
      _row.add( 2 );
      _row.add( 1 );
      _row.add( 1 );
      addPadding();
      _row.add( 4 );
      _row.add( 1 );
      _row.add( 4 );
      _row.add( 2 );
      _row.add( 1 );
      _row.add( 2 );
      addPadding();
      _row.add( 1 );
      _row.add( 1 );
      _row.add( 1 );
      _row.add( 1 );
      _row.add( 1 );
      _row.add( 4 );
      _row.add( 1 );
      _row.add( 3 );
      addPadding();
      _row.add( 2 );
      _row.add( 1 );
      _row.add( 1 );
      _row.add( 1 );
      _row.add( 2 );
      _row.add( 5 );
      addPadding();
      _row.add( 3 );
      _row.add( 2 );
      _row.add( 2 );
      _row.add( 6 );
      _row.add( 3 );
      _row.add( 1 );
      addPadding();
      _row.add( 1 );
      _row.add( 9 );
      _row.add( 1 );
      _row.add( 1 );
      _row.add( 2 );
      _row.add( 1 );
      addPadding();
      _row.add( 2 );
      _row.add( 1 );
      _row.add( 2 );
      _row.add( 2 );
      _row.add( 3 );
      _row.add( 1 );
      addPadding();
      _row.add( 3 );
      _row.add( 1 );
      _row.add( 1 );
      _row.add( 1 );
      _row.add( 1 );
      _row.add( 5 );
      _row.add( 1 );
      addPadding();
      _row.add( 1 );
      _row.add( 2 );
      _row.add( 2 );
      _row.add( 5 );
      addPadding();
      _row.add( 7 );
      _row.add( 1 );
      _row.add( 2 );
      _row.add( 1 );
      _row.add( 1 );
      _row.add( 1 );
      _row.add( 3 );
      addPadding();
      _row.add( 1 );
      _row.add( 1 );
      _row.add( 2 );
      _row.add( 1 );
      _row.add( 2 );
      _row.add( 2 );
      _row.add( 1 );
      addPadding();
      _row.add( 1 );
      _row.add( 3 );
      _row.add( 1 );
      _row.add( 4 );
      _row.add( 5 );
      _row.add( 1 );
      addPadding();
      _row.add( 1 );
      _row.add( 3 );
      _row.add( 1 );
      _row.add( 3 );
      _row.add( 10 );
      _row.add( 2 );
      addPadding();
      _row.add( 1 );
      _row.add( 3 );
      _row.add( 1 );
      _row.add( 1 );
      _row.add( 6 );
      _row.add( 6 );
      addPadding();
      _row.add( 1 );
      _row.add( 1 );
      _row.add( 2 );
      _row.add( 1 );
      _row.add( 1 );
      _row.add( 2 );
      addPadding();
      _row.add( 7 );
      _row.add( 2 );
      _row.add( 1 );
      _row.add( 2 );
      _row.add( 5 );
      addPadding();

      // setting known values:
      /*
      setValue( 0, 16 );
      setValue( 0, 18 );
      setValue( 0, 19 );
      setValue( 0, 20 );
      setValue( 0, 21 );
      setValue( 0, 22 );
      setValue( 0, 23 );
      setValue( 0, 24 );
      setValue( 1, 6 );
      setValue( 2, 6 );
      setValue( 3, 6 );
      setValue( 4, 6 );
      setValue( 5, 6 );
      setValue( 6, 6 );
      setValue( 8, 6 );
      setValue( 10, 6 );
      setValue( 12, 6 );
      setValue( 14, 6 );
      setValue( 16, 6 );
      setValue( 18, 6 );
      setValue( 19, 6 );
      setValue( 20, 6 );
      setValue( 21, 6 );
      setValue( 22, 6 );
      setValue( 23, 6 );
      setValue( 24, 6 );
      setValue( 0, 0 );
      setValue( 1, 0 );
      setValue( 2, 0 );
      setValue( 3, 0 );
      setValue( 4, 0 );
      setValue( 5, 0 );
      setValue( 6, 0 );
      setValue( 8, 0 );
      setValue( 10, 0 );
      setValue( 18, 0 );
      setValue( 19, 0 );
      setValue( 20, 0 );
      setValue( 21, 0 );
      setValue( 22, 0 );
      setValue( 23, 0 );
      setValue( 24, 0 );
      setValue( 0, 2 );
      setValue( 2, 2 );
      setValue( 3, 2 );
      setValue( 4, 2 );
      setValue( 6, 2 );
      setValue( 8, 2 );
      setValue( 9, 2 );
      setValue( 10, 2 );
      setValue( 12, 2 );
      setValue( 14, 2 );
      setValue( 15, 2 );
      setValue( 16, 2 );
      setValue( 18, 2 );
      setValue( 20, 2 );
      setValue( 21, 2 );
      setValue( 22, 2 );
      setValue( 24, 2 );
      setValue( 2, 3 );
      setValue( 3, 3 );
      setValue( 4, 3 );
      setValue( 8, 3 );
      setValue( 21, 3 );
      setValue( 2, 4 );
      setValue( 3, 4 );
      setValue( 4, 4 );
      setValue( 21, 4 );
      setValue( 8, 7 );
      setValue( 8, 10 );
      setValue( 21, 10 );
      setValue( 3, 11 );
      setValue( 16, 11 );
      setValue( 3, 12 );
      setValue( 8, 12 );
      setValue( 3, 13 );
      setValue( 3, 13 );
      setValue( 3, 14 );
      setValue( 8, 14 );
      setValue( 3, 15 );
      setValue( 8, 15 );
      setValue( 0, 16 );
      setValue( 2, 16 );
      setValue( 3, 16 );
      setValue( 4, 16 );
      setValue( 6, 16 );
      setValue( 7, 16 );
      setValue( 8, 16 );
      setValue( 10, 16 );
      setValue( 11, 16 );
      setValue( 13, 16 );
      setValue( 15, 16 );
      setValue( 16, 16 );
      setValue( 17, 16 );
      setValue( 18, 16 );
      setValue( 19, 16 );
      setValue( 20, 16 );
      setValue( 21, 16 );
      setValue( 22, 16 );
      setValue( 24, 16 );
      setValue( 0, 18 );
      setValue( 1, 18 );
      setValue( 2, 18 );
      setValue( 3, 18 );
      setValue( 4, 18 );
      setValue( 5, 18 );
      setValue( 6, 18 );
      setValue( 8, 18 );
      setValue( 16, 18 );
      setValue( 18, 18 );
      setValue( 16, 19 );
      setValue( 2, 20 );
      setValue( 3, 20 );
      setValue( 4, 20 );
      setValue( 16, 20 );
      setValue( 18, 20 );
      setValue( 2, 21 );
      setValue( 3, 21 );
      setValue( 4, 21 );
      setValue( 16, 21 );
      setValue( 2, 22 );
      setValue( 3, 22 );
      setValue( 4, 22 );
      setValue( 16, 22 );
      setValue( 18, 23 );
      setValue( 1, 24 );
      setValue( 2, 24 );
      setValue( 3, 24 );
      setValue( 4, 24 );
      setValue( 5, 24 );
      setValue( 6, 24 );
      setValue( 16, 24 );
      setValue( 18, 24 );
      */
   }

   /**
    *
    */
   public double evaluateFitness()
   {
      buildGenotype();

      _myFitness = 0;

      /*
      if( getRow( 0 ).matches( "^b{7,7}a+b{3,3}a+b{1,1}a+b{1,1}a+b{7,7}$" ) ) // 19
      {
         _myFitness += 1;
         System.out.println( "row 0" );
      }
      if( getRow( 1 ).matches( "^b{1,1}a{5,5}b{1,1}a+b{2,2}a+b{2,2}a+b{1,1}a{5,5}b{1,1}$" ) ) // 8
      {
         _myFitness += 1;
         System.out.println( "row 1" );
      }
      if( getRow( 2 ).matches( "^b{1,1}a{1,1}b{3,3}a{1,1}b{1,1}a+b{3,3}a+b{1,1}a+b{1,1}a{1,1}b{3,3}a{1,1}b{1,1}$" ) ) // 14
      {
         _myFitness += 1;
         System.out.println( "row 2" );
      }
      if( getRow( 3 ).matches( "^b{1,1}a{1,1}b{3,3}a{1,1}b{1,1}a+b{1,1}a+b{6,6}a+b{1,1}a{1,1}b{3,3}a{1,1}b{1,1}$" ) ) // 17
      {
         _myFitness += 1;
         System.out.println( "row 3" );
      }
      if( getRow( 4 ).matches( "^b{1,1}a{1,1}b{3,3}a{1,1}b{1,1}a+b{5,5}a+b{2,2}a+b{1,1}a{1,1}b{3,3}a{1,1}b{1,1}$" ) ) // 17
      {
         _myFitness += 1;
         System.out.println( "row 4" );
      }
      if( getRow( 5 ).matches( "^b{1,1}a{5,5}b{1,1}a+b{2,2}a+b{1,1}a{5,5}b{1,1}$" ) ) // 6
      {
         _myFitness += 1;
         System.out.println( "row 5" );
      }
      if( getRow( 6 ).matches( "^b{7,7}a+b{1,1}a+b{1,1}a+b{1,1}a+b{1,1}a+b{1,1}a+b{7,7}$" ) ) // 19
      {
         _myFitness += 1;
         System.out.println( "row 6" );
      }
      if( getRow( 7 ).matches( "^a*b{3,3}a+b{3,3}a*$" ) ) // 6
      {
         _myFitness += 1;
         System.out.println( "row 7" );
      }
      if( getRow( 8 ).matches( "^a*b{1,1}a+b{2,2}a+b{3,3}a+b{1,1}a+b{1,1}a+b{3,3}a+b{1,1}a+b{1,1}a+b{2,2}a*$" ) ) // 15
      {
         _myFitness += 1;
         System.out.println( "row 8" );
      }
      if( getRow( 9 ).matches( "^a*b{1,1}a+b{1,1}a+b{3,3}a+b{2,2}a+b{1,1}a+b{1,1}a*$" ) ) // 9
      {
         _myFitness += 1;
         System.out.println( "row 9" );
      }
      if( getRow( 10 ).matches( "^a*b{4,4}a+b{1,1}a+b{4,4}a+b{2,2}a+b{1,1}a+b{2,2}a*$" ) ) // 14
      {
         _myFitness += 1;
         System.out.println( "row 10" );
      }
      if( getRow( 11 ).matches( "^a*b{1,1}a+b{1,1}a+b{1,1}a+b{1,1}a+b{1,1}a+b{4,4}a+b{1,1}a+b{3,3}a*$" ) ) // 13
      {
         _myFitness += 1;
      }
      if( getRow( 12 ).matches( "^a*b{2,2}a+b{1,1}a+b{1,1}a+b{1,1}a+b{2,2}a+b{5,5}a*$" ) ) // 12
      {
         _myFitness += 1;
      }
      if( getRow( 13 ).matches( "^a*b{3,3}a+b{2,2}a+b{2,2}a+b{6,6}a+b{3,3}a+b{1,1}a*$" ) ) // 17
      {
         _myFitness += 1;
      }
      if( getRow( 14 ).matches( "^a*b{1,1}a+b{9,9}a+b{1,1}a+b{1,1}a+b{2,2}a+b{1,1}a*$" ) ) // 15
      {
         _myFitness += 1;
      }
      if( getRow( 15 ).matches( "^a*b{2,2}a+b{1,1}a+b{2,2}a+b{2,2}a+b{3,3}a+b{1,1}a*$" ) ) // 11
      {
         _myFitness += 1;
      }
      if( getRow( 16 ).matches( "^a*b{3,3}a+b{1,1}a+b{1,1}a+b{1,1}a+b{1,1}a+b{5,5}a+b{1,1}a*$" ) ) // 13
      {
         _myFitness += 1;
      }
      if( getRow( 17 ).matches( "^a*b{1,1}a+b{2,2}a+b{2,2}a+b{5,5}a*$" ) ) // 10
      {
         _myFitness += 1;
      }
      if( getRow( 18 ).matches( "^a*b{7,7}a+b{1,1}a+b{2,2}a+b{1,1}a+b{1,1}a+b{1,1}a+b{3,3}a*$" ) ) // 16
      {
         _myFitness += 1;
      }
      if( getRow( 19 ).matches( "^b{1,1}a{5,5}b{1,1}a+b{2,2}a+b{1,1}a+b{2,2}a+b{2,2}a+b{1,1}a*$" ) ) // 10
      {
         _myFitness += 1;
      }
      if( getRow( 20 ).matches( "^b{1,1}a{1,1}b{3,3}a{1,1}b{1,1}a+b{4,4}a+b{5,5}a+b{1,1}a*$" ) ) // 14
      {
         _myFitness += 1;
      }
      if( getRow( 21 ).matches( "^b{1,1}a{1,1}b{3,3}a{1,1}b{1,1}a+b{3,3}a+b{10,10}a+b{2,2}a*$" ) ) // 20
      {
         _myFitness += 1;
      }
      if( getRow( 22 ).matches( "^b{1,1}a{1,1}b{3,3}a{1,1}b{1,1}a+b{1,1}a+b{6,6}a+b{6,6}a*$" ) ) // 18
      {
         _myFitness += 1;
      }
      if( getRow( 23 ).matches( "^b{1,1}a{5,5}b{1,1}a+b{2,2}a+b{1,1}a+b{1,1}a+b{2,2}a*$" ) ) // 8
      {
         _myFitness += 1;
      }
      if( getRow( 24 ).matches( "^b{7,7}a+b{2,2}a+b{1,1}a+b{2,2}a+b{5,5}a*$" ) ) // 17
      {
         _myFitness += 1;
      }
      */

      if( getColumn( 0 ).matches( "^b{7,7}a+b{2,2}a+b{1,1}a+b{1,1}a+b{7,7}$" ) )
      {
         _myFitness += 1;
//          _myFitness += 1000;
//          System.out.println( "column 0" );
      }
      if( getColumn( 1 ).matches( "^b{1,1}a+b{1,1}a+b{2,2}a+b{2,2}a+b{1,1}a+b{1,1}$" ) )
      {
         _myFitness += 1;
//          _myFitness += 1000;
//          System.out.println( "column 1" );
      }
      if( getColumn( 2 ).matches( "^b{1,1}a+b{3,3}a+b{1,1}a+b{3,3}a+b{1,1}a+b{3,3}a+b{1,1}a+b{3,3}a+b{1,1}$" ) )
      {
         _myFitness += 1;
//          _myFitness += 1000;
//          System.out.println( "column 2" );
      }
      if( getColumn( 3 ).matches( "^b{1,1}a+b{3,3}a+b{1,1}a+b{1,1}a+b{5,5}a+b{1,1}a+b{3,3}a+b{1,1}$" ) )
      {
         _myFitness += 1;
//          _myFitness += 1000;
//          System.out.println( "column 3" );
      }
      if( getColumn( 4 ).matches( "^b{1,1}a+b{3,3}a+b{1,1}a+b{1,1}a+b{4,4}a+b{1,1}a+b{3,3}a+b{1,1}$" ) )
      {
         _myFitness += 1;
//          _myFitness += 1000;
//          System.out.println( "column 4" );
      }
      if( getColumn( 5 ).matches( "^b{1,1}a+b{1,1}a+b{1,1}a+b{2,2}a+b{1,1}a+b{1,1}$" ) )
      {
         _myFitness += 1;
//          _myFitness += 1000;
//          System.out.println( "column 5" );
      }
      if( getColumn( 6 ).matches( "^b{7,7}a+b{1,1}a+b{1,1}a+b{1,1}a+b{1,1}a+b{1,1}a+b{7,7}$" ) )
      {
         _myFitness += 1;
//          _myFitness += 1000;
//          System.out.println( "column 6" );
      }
      if( getColumn( 7 ).matches( "^a*b{1,1}a+b{1,1}a+b{3,3}a*$" ) )
      {
         _myFitness += 1;
//          _myFitness += 1000;
//          System.out.println( "column 7" );
      }
      if( getColumn( 8 ).matches( "^a*b{2,2}a+b{1,1}a+b{2,2}a+b{1,1}a+b{8,8}a+b{2,2}a+b{1,1}a*$" ) )
      {
         _myFitness += 1;
//          _myFitness += 1000;
//          System.out.println( "column 8" );
      }
      if( getColumn( 9 ).matches( "^a*b{2,2}a+b{2,2}a+b{1,1}a+b{2,2}a+b{1,1}a+b{1,1}a+b{1,1}a+b{2,2}a*$" ) )
      {
         _myFitness += 1;
//          _myFitness += 1000;
//          System.out.println( "column 9" );
      }
      if( getColumn( 10 ).matches( "^a*b{1,1}a+b{7,7}a+b{3,3}a+b{2,2}a+b{1,1}a*$" ) )
      {
         _myFitness += 1;
//          _myFitness += 1000;
//          System.out.println( "column 10" );
      }
      if( getColumn( 11 ).matches( "^a*b{1,1}a+b{2,2}a+b{3,3}a+b{1,1}a+b{1,1}a+b{1,1}a+b{1,1}a+b{1,1}a*$" ) )
      {
         _myFitness += 1;
//          _myFitness += 1000;
//          System.out.println( "column 11" );
      }
      if( getColumn( 12 ).matches( "^a*b{4,4}a+b{1,1}a+b{1,1}a+b{2,2}a+b{6,6}a*$" ) )
      {
         _myFitness += 1;
//          _myFitness += 1000;
//          System.out.println( "column 12" );
      }
      if( getColumn( 13 ).matches( "^a*b{3,3}a+b{3,3}a+b{1,1}a+b{1,1}a+b{1,1}a+b{3,3}a+b{1,1}a*$" ) )
      {
         _myFitness += 1;
//          _myFitness += 1000;
      }
      if( getColumn( 14 ).matches( "^a*b{1,1}a+b{2,2}a+b{5,5}a+b{2,2}a+b{2,2}a*$" ) )
      {
         _myFitness += 1;
//          _myFitness += 1000;
      }
      if( getColumn( 15 ).matches( "^a*b{2,2}a+b{2,2}a+b{1,1}a+b{1,1}a+b{1,1}a+b{1,1}a+b{1,1}a+b{2,2}a+b{1,1}a*$" ) )
      {
         _myFitness += 1;
//          _myFitness += 1000;
      }
      if( getColumn( 16 ).matches( "^a*b{1,1}a+b{3,3}a+b{3,3}a+b{2,2}a+b{1,1}a+b{8,8}a+b{1,1}a*$" ) )
      {
         _myFitness += 1;
//          _myFitness += 1000;
      }
      if( getColumn( 17 ).matches( "^a*b{6,6}a+b{2,2}a+b{1,1}a*$" ) )
      {
         _myFitness += 1;
//          _myFitness += 1000;
      }
      if( getColumn( 18 ).matches( "^a*b{7,7}a+b{1,1}a+b{4,4}a+b{1,1}a+b{1,1}a+b{3,3}a*$" ) )
      {
         _myFitness += 1;
//          _myFitness += 1000;
      }
      if( getColumn( 19 ).matches( "^a*b{1,1}a+b{1,1}a+b{1,1}a+b{1,1}a+b{4,4}a*$" ) )
      {
         _myFitness += 1;
//          _myFitness += 1000;
      }
      if( getColumn( 20 ).matches( "^a*b{1,1}a+b{3,3}a+b{1,1}a+b{3,3}a+b{7,7}a+b{1,1}a*$" ) )
      {
         _myFitness += 1;
//          _myFitness += 1000;
      }
      if( getColumn( 21 ).matches( "^a*b{1,1}a+b{3,3}a+b{1,1}a+b{1,1}a+b{1,1}a+b{2,2}a+b{1,1}a+b{1,1}a+b{4,4}a*$" ) )
      {
         _myFitness += 1;
//          _myFitness += 1000;
      }
      if( getColumn( 22 ).matches( "^a*b{1,1}a+b{3,3}a+b{1,1}a+b{4,4}a+b{3,3}a+b{3,3}a*$" ) )
      {
         _myFitness += 1;
//          _myFitness += 1000;
      }
      if( getColumn( 23 ).matches( "^a*b{1,1}a+b{1,1}a+b{2,2}a+b{2,2}a+b{2,2}a+b{6,6}a+b{1,1}a*$" ) )
      {
         _myFitness += 1;
//          _myFitness += 1000;
      }
      if( getColumn( 24 ).matches( "^a*b{7,7}a+b{1,1}a+b{3,3}a+b{2,2}a+b{1,1}a+b{1,1}a*$" ) )
      {
         _myFitness += 1;
//          _myFitness += 1000;
      }

//       System.out.println( _myFitness );

      return _myFitness;
   }

   /**
    *
    */
   public String toStringFinal()
   {
      buildGenotype();

      System.out.println( "************************************************ Final: getRow( 6 ):     " + getRow( 6 ) );
      System.out.println( "************************************************ Final: getColumn( 6 ):  " + getColumn( 6 ) );
      System.out.println( "************************************************ Final: getRow( 21 ):    " + getRow( 21 ) );
      System.out.println( "************************************************ Final: getColumn( 16 ): " + getColumn( 16 ) );
      System.out.println( "genotype size: " + _genotype.size() );

      String result = "\nFitness: " + _myFitness + "\n";

      RGBColorPoint[][] data = new RGBColorPoint[ 25 ][ 25 ];
      int               r    = -1;
      int               c    = 0;

      for( int k=0; k<_genotype.size(); k++ )
      {
         if( ( k % 25 ) == 0 )
         {
            r++;
            c = 0;
         }
         if( _genotype.get( k ) < 0.5 )
         {
            data[ r ][ c++ ] = new RGBColorPoint( 1.0, 1.0, 1.0 );
         }
         else
         {
            data[ r ][ c++ ] = new RGBColorPoint( 0.0, 0.0, 0.0 );
         }
      }

      GrafixUtilities grafix = new GrafixUtilities();
      grafix.putRGBData2( data, "gchq.bmp", "BMP" );

      return result;
   }
}
