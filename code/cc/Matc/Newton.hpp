//**********************************************************************
// File     :  Newton.hpp
// Author   :  Brandon Benham
// Purpose  :  This file contains the declartions for the NewtoN
//          :  class, which solves functions, i.e., finds the zeros
//          :  of the mathematical functions.
//          :  We also encapsulate our Bisecant (the hybrid algorithm)
//          :  in this class because it is significantly easier to
//          :  implement due to the similarities between Newton and Bisecant
// Notes    :  This method should be used when the function is well-defined
//          :  and the first derivative of the function is known.
//**********************************************************************
#ifndef __NEWTON_HPP
#define __NEWTON_HPP

#include"Onevar.hpp"
#include"Domain.hpp"
#include"Targa.hpp"

//************************************************************************
// Class    :  NewtoN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  :  Calculates the zero of a known function by using the
//          :  Newton-Raphson Method. That is, for a given root guess x_k,
//          :  x_k+1 = x_k - f(x)/f`(x)
//************************************************************************
class NewtoN
{
   protected:
      OnevariablE* Fct1TheRootEquation;   // our function object
      OnevariablE* Fct1TheDerivative;     // our derivative object
      TargA* TargOutput;                  // output for frac funcs
      DomaiN* DtheDomain;
      long lMax_Iterations;               // maximum iterations
      long lCurrent_Iteration;
      long lMax_Total_Iterations;
      long lTotal_Iterations;             // had to implement 'cause of recursion stack
      long double ldTolerance;            // tolerance level
      long double ldLast_Guess;           // contains x_k
      void Setup();                       // usual setup routine
      long double FindRoot(long double);  // actual function that solves
      inline void SetVariable() {Fct1TheRootEquation->SetVariable( ldLast_Guess, 0 );
         Fct1TheDerivative->SetVariable( ldLast_Guess, 0 );}
      long double Eval();
      int BelowTolerance(long double);
      int IsZero(long double);
      int iIsSolved;                   // to handle the c++ recursion error
      int iTargWidth;
      int iTargHeight;
      unsigned short usLog_It;
      // Bisecant data member items:
                                       
   public:
      NewtoN();      // Default Constructor
      ~NewtoN();     // Destructor
      NewtoN(int, int, long double = 0);  // Constructor, with tolerance
      inline void SetMaxIterations(long lM) {lMax_Total_Iterations = lM;}
      inline void SetTolerance(long double ldT) {ldTolerance = ldT;}
      inline void SetEquation(int iW) {Fct1TheRootEquation->SetEquation(iW);}
      inline void SetDerivative(int iD) {Fct1TheDerivative->SetEquation(iD);}
      inline void SetEquationParams(long double ld1, long double ld2=0,
         long double ld3=0) {Fct1TheRootEquation->SetParams(ld1, ld2, ld3);}
      inline void SetDerivativeParams(long double ld1, long double ld2=0,
         long double ld3=0) {Fct1TheDerivative->SetParams(ld1, ld2, ld3);}
      inline long GetIterations() {return lCurrent_Iteration;}
      inline long GetTotalIterations() {return lTotal_Iterations;}
      // Domain accessor function:
      inline long double GetDomainLengthX() {return DtheDomain->GetLengthX();}
      inline long double GetDomainLengthY() {return DtheDomain->GetLengthY();}
      inline long double GetDomainFirstX() {return DtheDomain->GetFirstX();}
      inline long double GetDomainFirstY() {return DtheDomain->GetFirstY();}
      // Targa member access functions:
      inline short int GetTargaHeight() {return TargOutput->GetHeight();}
      inline short int GetTargaWidth() {return TargOutput->GetWidth();}
      void DoTargaFile(int iScale = 1)
         //{TargOutput->Do_Targa_File(iScale * lTotal_Iterations);}
         {TargOutput->Do_Targa_File( (int)(lTotal_Iterations % iScale) );}
      void DoTargaFile( long lValue )
         {TargOutput->Do_Targa_File( lValue );}
      void ChangePallete(int iBlue, int iGreen, int iRed, int iS = 0, int iE = 768)
         {TargOutput->Change_Pallette(iBlue, iGreen, iRed, iS, iE);}
      void WriteToOutput(char*);  // interface to the output file

      inline void SetCoefficients( VectoR* theCoeffs )
        {Fct1TheRootEquation->SetPolynomialCoeffs( theCoeffs );}
      inline void SetDxCoefficients( VectoR* theCoeffs )
         {Fct1TheDerivative->SetPolynomialCoeffs( theCoeffs );}
      inline void SetIsSolved(int iSolv) {iIsSolved = iSolv;}
      inline int GetStatus() {return iIsSolved;}
      void SetFileLimits(int, int, char* = 0);
      void FractalizeIt();
      void SetDomains(long double, long double, long double, long double);
      void SetErrorLog(char* );
      inline void SetLogging(unsigned short usOn, int iErr = 0) {
         usLog_It = usOn; Fct1TheDerivative->SetLogging(iErr);
         Fct1TheRootEquation->SetLogging(iErr);
         } // end SetLogging
      virtual long double SolveIt(long double); // front-end to method

}; // end class NewtoN declaration

#endif
