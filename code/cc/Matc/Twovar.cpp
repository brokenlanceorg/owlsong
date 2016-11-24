//***************************************************************************** 
// File     :  TwoVar.cpp
// Purpose  :  This file contains the object oriented paradigm for the 
//          :  mathematical functions to be used in various applications
// Notes    :  The TwovariablE class is derived from the more or less
//          :  abstract function class. Observe that with this formulation,
//          :  after an 'evaluation' the result is held in ldDependentVariable
//          :  thus, there is no need to re-evaluate the function to get
//          :  this value for a given set of variables; this will speed
//          :  computations in certain situations.
// Author   :  Brandon Benham
//*****************************************************************************
#include"Twovar.hpp"

#define BENITO_SANTIAGO catch(NumerR* e) { e->HandleError(); }

//***************************************************************************** 
// Class    :  TwovariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  Default Constructor
// Purpose  :  Declares a derived class of two-variable functions from the
//          :  FunctioN class
// Notes    :  Constructs a numericalerror object with checking for divide
//          :  by zero, and a machine epsilon
//*****************************************************************************
TwovariablE::TwovariablE() : FunctioN()
{
   vIndependentVars = new VectoR(2);   
} // end default constructor

//***************************************************************************** 
// Class    :  TwovariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  Destructor
// Purpose  :  Declares a derived class of two-variable functions from the
//          :  FunctioN class
// Notes    :  Constructs a numericalerror object with checking for divide
//          :  by zero, and a machine epsilon
//*****************************************************************************
TwovariablE::~TwovariablE()
{
   delete vIndependentVars;
} // end destructor

//***************************************************************************** 
// Class    :  TwovariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  Constructor
// Purpose  :  Declares a derived class of two-variable functions from the
//          :  FunctioN class
// Notes    :  Constructs a numericalerror object with checking for divide
//          :  by zero, and a machine epsilon
//*****************************************************************************
TwovariablE::TwovariablE(_ERROR_TYPE_ EtheErr) : FunctioN(EtheErr)
{
   vIndependentVars = new VectoR(2);
} // end constructor       

//***************************************************************************** 
// Class    :  TwovariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  Constructor
// Purpose  :  Declares a derived class of two-variable functions from the
//          :  FunctioN class
// Notes    :  Constructs a numericalerror object with checking for divide
//          :  by zero, and a machine epsilon
//*****************************************************************************
TwovariablE::TwovariablE(_ERROR_TYPE_ EtheErr, 
   long double (*UserFunc)(long double ldX)) : FunctioN(EtheErr, UserFunc)
{
   vIndependentVars = new VectoR(2);
} // end constructor

//***************************************************************************** 
// Class    :  TwovariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  Intialize
// Purpose  :  Clears out all of the variables and parameters
//*****************************************************************************
void TwovariablE::Initialize()
{
   ldParam1 = 0;
   ldParam2 = 0;
   ldParam3 = 0;
   ldFirst_Variable = 0;
   ldSecond_Variable = 0;
   ldDependent_Variable = 0;
   vIndependentVars->Zero_Out(); 
   NumericalError->SetGoodVal(ldDependent_Variable);
   NumericalError->SetLogging(2);
} // end Initialize

//***************************************************************************** 
// Class    :  TwovariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  SetParams
// Purpose  :  Sets the parameters that are used in the calculations.
//*****************************************************************************
void TwovariablE::SetParams(long double ldFirst, long double ldSecond,
   long double ldThird)
{
   ldParam1 = ldFirst;
   ldParam2 = ldSecond;
   ldParam3 = ldThird;
} // end SetParams        

//***************************************************************************** 
// Class    :  TwovariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  SetVariable
// Purpose  :  Sets the corresponding variable equal to the passed in arg.
//*****************************************************************************
void TwovariablE::SetVariable( long double ldArg, int iW )
{
   if(iW > vIndependentVars->cnRows)
      return;
   vIndependentVars->pVariables[iW] = ldArg;
} // end SetVariable

//***************************************************************************** 
// Class    :  TwovariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  EvalDiffEqOne
// Purpose  :  Return the DiffEqOne function evaluated at the instanstiated
//          :  variables.
// Notes    :  Upon entry, we assume that the independent and parameter
//          :  variables have in fact been correctly instantiated.
//*****************************************************************************
long double TwovariablE::EvalDiffEqOne()
{
long double ldEval = 0;

   NumericalError->sHandled = 0; // Re-set the error flag
   NumericalError->SetErrorType(_OVERFLOW_);
   ldEval = DiffEqOne();
   if(NumericalError->sHandled)
      ldEval = NumericalError->GetGoodVal();

   return ldEval;
} // end EvalDiffEqOne

//***************************************************************************** 
// Class    :  TwovariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  DiffEqOne
// Purpose  :  The actual function which defines the equation: 
//          :  y' = param1 + (t - y)^param2
// Notes    :  We define ldFirst.. to be t, and ldSecond.. to be y
//          :  and we take ldParam1 to be 1, and ldParam2 to be 2.
//*****************************************************************************
long double TwovariablE::DiffEqOne()
{
long double ldDiffOne = 0;
//int iGreaterThan = 0;

   NumericalError->SetErrorType(_OVERFLOW_);
   NumericalError->SetRoutineName("DiffEqOne");
   NumericalError->SetFunctionName("Decimal Power");
   ldFirst_Variable = vIndependentVars->pVariables[0];
   ldSecond_Variable = vIndependentVars->pVariables[1];
   try{                                                
      ldDiffOne = ldFirst_Variable - ldSecond_Variable;
      NumericalError->SetGoodVal(ldDiffOne);
      ldDiffOne = DecimalPower(ldDiffOne, ldParam2);
/*    switch(iGreaterThan)
      {
         case 0 : // Less than One
            if(ldDiffOne > Absolute(NumericalError->GetGoodVal()))
               throw NumericalError; // Had Underflow
         break;

         case 1 : // Greater than One
            if(ldDiffOne < Absolute(NumericalError->GetGoodVal()))
               throw NumericalError; // Had Overflow
         break;
      } // end switch */
      ldDiffOne += ldParam1;
      NumericalError->SetGoodVal(ldDiffOne);
   } BENITO_SANTIAGO;
   if(NumericalError->sHandled)
      ldDiffOne = NumericalError->GetGoodVal();
   ldDependent_Variable = ldDiffOne;

   return ldDiffOne;
} // end DiffEqOne

//***************************************************************************** 
// Class    :  TwovariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  DiffEqTwo
// Purpose  :  
// Notes    :  This will be considered equation #2
//          :  
//*****************************************************************************
long double TwovariablE::DiffEqTwo()
{
long double ldDiffTwo = 0;

   NumericalError->SetErrorType(_OVERFLOW_);
   NumericalError->SetRoutineName("DiffEqTwo");
   NumericalError->SetFunctionName("Decimal Power");
   ldFirst_Variable = vIndependentVars->pVariables[0];
   ldSecond_Variable = vIndependentVars->pVariables[1];
   try{                                                
      ldDiffTwo = ldFirst_Variable + ldSecond_Variable;
      NumericalError->SetGoodVal(ldDiffTwo);
   } BENITO_SANTIAGO;
   if(NumericalError->sHandled)
      ldDiffTwo = NumericalError->GetGoodVal();
   ldDependent_Variable = ldDiffTwo;

   return ldDiffTwo;
} // end EquationTwo

//***************************************************************************** 
// Class    :  TwovariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  EvaluateIt
// Purpose  :  This function is defined so that we can arbitrarily call this
//          :  to evaluate some pre-defined function - this is used by
//          :  algorithms such as integral and differential approximators.
// Notes    :  This member assumes the same values are set as the immediate
//          :  evaluation functions.
//*****************************************************************************
long double TwovariablE::EvaluateIt()
{
long double ldTheResult = 0;

   switch(iWhich_Equation)
   {
      case 1 :
         ldTheResult = EvalDiffEqOne();
      break;

      case 2 :
         ldTheResult = DiffEqTwo();
      break;       

      case 3 :
         ldTheResult = SmoothedMovingAverage();
      break;

      case 4 :
         ldTheResult = SmoothedMovingAverageInverse();
      break;

      case 5 :
      break;

      case 6 :
      break;

      case 7 :
      break;

      case 8 :
      break;

   } // end switch statement
   return ldTheResult;
} // end EvaluateIt

//***************************************************************************** 
// Class    :  TwovariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  SetLogging
// Purpose  :  Just sets our numerical logging value.
//*****************************************************************************
void TwovariablE::SetLogging(int iWhat)
{
   NumericalError->SetLogging(iWhat);
} // end SetLogging

//***************************************************************************** 
// Class    :  TwovariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  SmoothedMovingAverage
// Purpose  :  Computes the function: x2 + p1(x1 - x2)
//          :  
// Notes    :  This will be considered equation number 3
//          :  The FirstVar will contain x2
//          :  While SecondVar will contain x1
//          :  We assume that the client has properly computed the p1 param,
//          :  i.e., that this is p = 1 - s.
//*****************************************************************************
long double TwovariablE::SmoothedMovingAverage()
{
long double ldSMA = 0;

   NumericalError->SetErrorType(_OVERFLOW_);
   NumericalError->SetRoutineName( "SmoothedMovingAverage" );
   NumericalError->SetFunctionName( "Multiplication" );
   ldFirst_Variable = vIndependentVars->pVariables[0];
   ldSecond_Variable = vIndependentVars->pVariables[1];
   try {
      ldSMA = ldSecond_Variable - ldFirst_Variable;
      ldSMA *= ldParam1;
      ldSMA += ldFirst_Variable;
   } BENITO_SANTIAGO;
   if( NumericalError->sHandled )
      ldSMA = NumericalError->GetGoodVal();
   ldDependent_Variable = ldSMA; // what's this?

   return ldSMA;
} // end SmoothedMovingAverage

//***************************************************************************** 
// Class    :  TwovariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  SmoothedMovingAverageInverse
// Purpose  :  Computes the function: (x1 - x2) / p1 + x2
//          :  
// Notes    :  This will be considered equation number 4
//          :  The FirstVar will contain x1
//          :  While SecondVar will contain x2
//          :  We assume that the client has properly computed the p1 param,
//          :  i.e., that this is p = 1 - s.
//*****************************************************************************
long double TwovariablE::SmoothedMovingAverageInverse()
{
long double ldSMAinv = 0;

   NumericalError->SetErrorType( _OVERFLOW_ );
   NumericalError->SetRoutineName( "SmoothedMovingAverageInverse" );
   NumericalError->SetFunctionName( "Division" );
   ldFirst_Variable = vIndependentVars->pVariables[0];
   ldSecond_Variable = vIndependentVars->pVariables[1];
   try {
      ldSMAinv = ldFirst_Variable - ldSecond_Variable;
      NumericalError->SetGoodVal( ldSMAinv );
      if( ldParam1 == 0 )
         throw NumericalError;
      ldSMAinv /= ldParam1;
      ldSMAinv += ldSecond_Variable;
   } BENITO_SANTIAGO;
   if( NumericalError->sHandled )
      ldSMAinv = NumericalError->GetGoodVal();

   return ldSMAinv;
} // end SmoothedMovingAverageInverse

