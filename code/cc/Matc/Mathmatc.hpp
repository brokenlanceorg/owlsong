//**********************************************************************
// File    : Mathmatc.hpp
// Purpose : This file contains the declaration for the MatriX, 
//         : VectoR, and SysteM classes, which will ease programming 
//         : of calculated mathematical functions.
//**********************************************************************

#ifndef __MATHMATC_HPP
#define __MATHMATC_HPP

#include <cmath>
#include <iomanip>
#include "Random.hpp"
#include "Func.hpp"

class VectoR;

//**********************************************************************
// Class   : MatriX
// Purpose : This class initializes two-dimensional array for storage 
//         :  of calculated mathematical functions.
//**********************************************************************
class MatriX        
{
   protected:

      long double cnDeterminant;
      long double* cnMagnitudes;
      RandoM* RandomVariable;
      FunctioN* FunctionObject;
      void Setup();
                int FindFirstNonZeroRow( int );
                long double Absolute( long double );
                void Swap( int, int );
                MatriX* _Q;
                MatriX* _R;

   public:

      MatriX(int nRows, int nColumns);
         MatriX(MatriX*);
      MatriX();
      ~MatriX();
      //MatriX& operator +  (const MatriX& b);
      //MatriX& operator *  (const MatriX& b);
      //VectoR& operator *  (const VectoR& b);
      MatriX& operator =  (const MatriX& b);
      MatriX& operator += (const MatriX& rhs);
      MatriX& operator -= (const MatriX& rhs);
      int cnColumns;
      int cnRows;
      long double** pCol; // Our data item!!!!
      friend ostream& operator << (ostream& o, const MatriX& rhs);
      void Equate(MatriX* mFirst); // cause the overload is screwed up
      MatriX Get_Cofactor(int nRow, int nColumn);
      MatriX Transpose();
      long double Determinant();
      void Zero_Out();
      void Fill_Ones();
      void cDet(); 
      void Normalize();
      void Fill_Rand();
      void Fill_Rand_Integer(int nMax);
      //void Display(BOOL lSpace, BOOL lMag); // spaces between elements
         // lRows: if we want to print out the row vectors
      void Calc_Magnitudes();
      void Make_Identity();
      void Make_Translation(long double nX, long double nY, long double nZ);
      void Make_Scale(long double nX, long double nY, long double nZ);
      void Make_Rotation_X(long double nTheta);
      void Make_Rotation_Y(long double nTheta);
      void Make_Rotation_Z(long double nTheta);
      int Find_Pivot(int nRows);
      void Add_vector(int nRow, VectoR* nVec);
      void Eschalon();
      void Reduced_Eschalon();
      void Switch_Rows(int nDest, int nSource);
      void Scale(long double nAlpha);
      void OutputToFile( char* );
      MatriX Inverse();
                VectoR* Solve();
                VectoR* SolveMGS( VectoR*, bool = true );
                MatriX* Add( MatriX* );
                MatriX* Product( MatriX* );
                VectoR* Product( VectoR* );
      friend class VectoR;
      //friend class SysteM;
      //friend class SpacE;
      //friend class QuadratiC;
                void Print();
                int ComputeMGS();
                long double ComputeColumnNorm( int );
                MatriX* GetQ() { return _Q; }
                MatriX* GetR() { return _R; }

}; //end class definition matrix

//**********************************************************************
// Class    :  VectoR
// Purpose  :  This class initializes one-dimensional array for storage 
//          :  of calculated mathematical functions.
//**********************************************************************
class VectoR   
{
   protected:
      RandoM* RandomVariable;
      FunctioN* FunctionObject;
      void Setup();
              
   public:
      VectoR();
      VectoR(int nVariables);
      VectoR( VectoR* );
      ~VectoR();
      long double* pVariables;
      long double cnMagnitude;
      int cnRows;

      //VectoR& operator + (const VectoR& b);
      //VectoR& operator * (const VectoR& b);
      VectoR& operator *= (const VectoR& b);
      //MatriX& operator % (const VectoR& b);
      VectoR& operator = (const VectoR& b);
      VectoR& operator = (const long double* rhs);
      long double operator == (const VectoR& rhs);
      VectoR& operator += (const VectoR& rhs);
      VectoR& operator -= (const VectoR& rhs);
      //VectoR& operator - (const VectoR& rhs);
      friend ostream& operator << (ostream& o, const VectoR& rhs);
      void Zero_Out();
      void CalcMagnitude();
      void Normalize();
      void Fill_Rand();
      void FillGaussianRandom();
      void FillUniformRandom();
      void Fill_Rand_Integer(int nMax);
      void Fill_Ones();
      void Scale(long double);
      void Add(long double);
      void Print();
      VectoR* Product( VectoR* );
                MatriX* OuterProduct( VectoR* );
                VectoR* Add( VectoR* );
                VectoR* Subtract( VectoR* );
      //void Display(BOOL lSpace);
      long double GetSum();
      long double GetSumSquared();
      void CheckForZeros();
      void CheckForInfinity();
                VectoR* GetAbsoluteVector();
                long double GetSmallestNumber();
                long double GetLargestNumber();
                long double GetMaxElement( int, int );
                long double GetMinElement( int, int );
      
      //friend class SysteM;          // One of these days
      //friend class SpacE;           // I'll make some
      friend class MatriX;          // public functions
      //friend class QuadratiC;       // to get rid of this
      //friend class ParametriC;      // friend stuff
      //friend class SpacE;
      //friend class InpuT;
      //friend class Data_seT;
      //friend class Plot_daT;
      //friend class LagrangE;

}; //end class definition vector

#endif
