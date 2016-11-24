//*****************************************************************************
// File     :  OneVar.hpp
// Purpose  :  This file contains the object oriented paradigm for the
//          :  mathematical functions to be used in various applications
// Notes    :  The OnevariablE class is derived from the more or less
//          :  abstract function class.
//*****************************************************************************
#ifndef __ONEVAR_HPP
#define __ONEVAR_HPP

#include"Func.hpp"
#include"Mathmatc.hpp"
#include"Numerr.hpp"

//***************************************************************************** 
// Class    :  OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  :  Declares a derived class from FunctioN
// Notes    :  This class defines a set of one variable functions
//          :  
//*****************************************************************************
class OnevariablE : public FunctioN
{
   protected:
//    PolynomiaL* PthePolynomial;
      int iWhich_Equation;
      long double ldParam1;
      long double ldParam2;
      long double ldParam3;
      long double ldInverse_Value;
      VectoR* vIndependentVars;
      VectoR* vCoefficients;
      VectoR* vGaussian;
      VectoR* vUniform;
      NumerR* Fct1NumericalError;
//    void* _theNeuroFunction; // really of type NeuralparallelfunctioN
      // Some More Functions:
      long double ExpXPlusCosine(long double);
      long double DxExpXPlusCosine(long double);
      long double LogCosine(long double);
      long double DxLogCosine(long double);
      long double XCosine(long double);
      long double DxXCosine(long double);
      long double X2Log(long double);
      long double DxX2Log(long double);
      long double ExpX2(long double);
      long double DxExpX2(long double);
      long double SinXExpX(long double);
      long double DxSinXExpX(long double);
//    long double InvSinXExpX(long double); // eq # -18
      long double X2XSinCos(long double);
      long double DxX2XSinCos(long double);
      long double X3rdRootExpX(long double);
//      long double Polynomial(long double); We'll get these functions back someday!
      long double Cosine(long double);    // eq# 24
      long double Sine(long double);      // eq# 25
//    long double CosInv(long double);
//    long double OneMinusCosDivSqrtX(long double);
//      long double SineX2(long double);
//      long double OneOverSqrtX(long double);
//      long double ExpInvOverSqrtX(long double);
//      long double ExpInvOverSqrtX_2(long double);
//    long double CosOverCos2(long double);
//    long double ExpOverExpInv(long double);
//    long double ExpSin2Cos(long double);
//    long double XOvrExpX(long double);
//    long double SinLn2X(long double);
//    long double CosSin2X(long double);
//    long double SinOverX(long double);
//    long double SinOverSqrtX(long double);
//    long double XInverse(long double);
      long double Gaussian(long double);     // eq# 26
      long double Linear(long double);       // eq# 27
      long double Sigmoid(long double);      // eq# 28
      long double Sigmoid1(long double);     // worker function
      long double Sigmoid2(long double);     // worker function
      long double SineWave(long double);     // eq# 29
      long double CosWave(long double);      // eq# 30
      long double CosWave2(long double);     // eq# 31
      long double CosCos(long double);       // eq# 32
      long double Tangent(long double);      // eq# 33
      long double Polynomial(long double);   // eq# 34
//      long double NeuralFunction(long double);   // eq# 35
      long double FractionalBrownian(long double);   // eq# 36
      long double ChordFunction(long double);   // eq# 37
      long double DxChordFunction(long double);   // eq# 38
      long double InverseSigmoid(long double);   // eq# 39
      long double InverseSigmoid1(long double);   // worker function
      long double InverseSigmoid2(long double);   // worker function

   public:
      OnevariablE();
      ~OnevariablE();
      OnevariablE( _ERROR_TYPE_, bool = false ); // to change the error checking type
      OnevariablE( _ERROR_TYPE_,  // to change the error checking type
         long double (*UserFunc)(long double ldX)); // to add error
         // checking function to numericalerror object
      void SetPolynomialCoeffs( VectoR* v ) { vCoefficients = v; }
      void SetPolynomialRoots(VectoR*, VectoR*);

      void SetLogging(int);
      int SetEquation(int iW) { int iOld = iWhich_Equation; 
                                iWhich_Equation = iW; return iOld; }
      void SetVariable(long double, int = 0);
      void SetParams(long double, long double = 0, long double = 0);
      void SetInverseValue(long double ldT) {ldInverse_Value = ldT;}
      void Setup();
      void SetGaussianVector( VectoR* v ) { vGaussian = v; }
      void SetUniformVector( VectoR* v ) { vUniform = v; }
      
      virtual long double EvaluateIt();
      long double ExpInvSquared(long double);
      long double Logistic(long double);
      long double DxLogistic(long double);
      long double PythagoreanHypotenuse(long double);
      long double PythagoreanMinus(long double);
      long double Xsquared(long double);
      long double Xcubed(long double);
}; // end class declaration                          

#endif
