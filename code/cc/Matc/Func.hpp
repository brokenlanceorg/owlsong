//***************************************************************************** 
// File    : Func.hpp
// Purpose : This file contains the object oriented paradigm for the 
//         : mathematical functions to be used in various applications
//Author   : Brandon Benham
//*****************************************************************************
#ifndef __FUNC_HPP
#define __FUNC_HPP

#include<math.h>
#include"Numerr.hpp"
#include"DebugLogger.hpp"

// Defines:*********************************
#define BASE_TWO 2
#define BASE_TEN 10
#define SINGL_PREC 32
#define DBLE_PREC 64
#define LNG_DBLE_PREC 80
const long double PI            = 3.14159265358979323846;
const long double PI_UNDER_180  = 57.29577951;
const long double PI_OVER_180   = 0.0174532925;
const long double SINE_45       = 0.707106781187;
const long double ONE_THIRD     = 0.33333333333333333;
const long double ONE_HALF      = 0.5;
//long double _EPSILON = 6.04463e-23;
const long double _EPSILON = 6.04463e-40;

//***************************************************************************** 
// Class    : FunctioN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : Declares an abstract class for other specialized classes to
//          : inherit.
// Notes    : Since all ancestors will be doing numerical computations, we
//          : will certainly have Machine Epsilon methods, and numerical
//          : error catchers.
//*****************************************************************************
class FunctioN
{
   protected:
      NumerR* NumericalError;// catching numerical errors
      long double ldEpsilon; // our machine precision
      long double Machine_Eps( int, int );

   public:
      FunctioN( int = 0 );
      ~FunctioN();
      FunctioN( _ERROR_TYPE_, int = 0 );
      FunctioN( _ERROR_TYPE_ TheKind, // to instantiate the numerr object
                         long double ( *UserFunc)(long double ldX), int = 0 );
      void SetPrecision( int, int );
      inline long double GetEpsilon() { return ldEpsilon; }
      long double Absolute( long double );
      long double DecimalPower( long double, long double );
      long double Find_Max( long double, long double, long double );
      int Find_Max( long double, long double );
      long double DegreesToRadians( long double );
      long double RadiansToDegrees( long double );
      long double Exponential( long double );
      long double NaturalLog( long double );
      double VectorRadianAngle( double*, double* );
}; //end class declaration

#ifdef __MYDEBUG__
   #define FuncFPRINT( msg ) DebugloggeR FuncFPRINT( msg, __FILE__, __LINE__ );
#else
   #define FuncFPRINT( msg );
#endif

#endif
