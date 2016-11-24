//***************************************************************************** 
// File     :  TwoVar.hpp
// Purpose  :  This file contains the object oriented paradigm for the 
//          :  mathematical functions to be used in various applications
// Notes    :  The TwovariablE class is derived from the more or less
//          :  abstract function class.
//*****************************************************************************
#ifndef __TWOVAR_HPP
#define __TWOVAR_HPP

#include"Func.hpp"
#include"Mathmatc.hpp"

//***************************************************************************** 
// Class    :  TwovariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  :  Declares a derived class from FunctioN
// Notes    :  This class defines a set of Two variable functions
//          :  
//*****************************************************************************
class TwovariablE : public FunctioN
{
   protected:
      int iWhich_Equation;    // This tells the object which func the user wants
      void Initialize();
      long double DiffEqOne(); // Equation number ONE
      long double DiffEqTwo(); // Equation number TWO
      long double SmoothedMovingAverage(); // Equation number THREE
      long double SmoothedMovingAverageInverse(); // Equation number FOUR
      // Variables used in computations:
      // Variables used in computations:
      long double ldFirst_Variable;
      long double ldSecond_Variable;
      long double ldDependent_Variable;
      long double ldParam1;   // These variables are for the
      long double ldParam2;   // various 'parameters' encountered
      long double ldParam3;   // in the evaluation of a function
      VectoR* vIndependentVars;

   public:
      TwovariablE();
      ~TwovariablE();
      TwovariablE(_ERROR_TYPE_); // to change the error checking type
      TwovariablE(_ERROR_TYPE_,  // to change the error checking type
         long double (*UserFunc)(long double ldX)); // to add error
         // checking function to numericalerror object

      // Functions:
      inline void SetLogging(int);
      inline void SetEquation(int iW) {iWhich_Equation = iW;}
      void SetParams( long double, long double = 0, long double = 0 );
      void SetVariable( long double, int = 0 );
      inline long double GetResult() { return ldDependent_Variable; }
      long double EvaluateIt();  // Calls to this assume iWhich has been set
      long double EvalDiffEqOne(); // Evaluates the DiffEqOne function
         
}; // end class declaration

#endif
