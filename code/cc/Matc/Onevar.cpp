//***************************************************************************** 
// File     :  OneVar.cpp
// Purpose  :  This file contains the object oriented paradigm for the 
//          :  mathematical functions to be used in various applications
// Notes    :  The OnevariablE class is derived from the more or less
//          :  abstract function class.
//          :  For 16 bit, the 'too big' value is: 1e97
//*****************************************************************************
#include"Onevar.hpp"

#define CARLTON_FISK catch(NumerR* eEe) {eEe->HandleError();}


//*****************************************************************************
// Class    :  OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  Default Constructor
// Purpose  :  Declares a derived class of two-variable functions from the
//          :  FunctioN class
// Notes    :  Constructs a numericalerror object with checking for divide
//          :  by zero, and a machine epsilon
//*****************************************************************************
OnevariablE::OnevariablE() : FunctioN()
{
   Setup();
} // end default constructor

//***************************************************************************** 
// Class    :  OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  Default Constructor
// Purpose  :  Declares a derived class of two-variable functions from the
//          :  FunctioN class
// Notes    :  Constructs a numericalerror object with checking for divide
//          :  by zero, and a machine epsilon
//*****************************************************************************
OnevariablE::~OnevariablE()
{
   if( vIndependentVars != 0 )
   {
      delete vIndependentVars;
      vIndependentVars = 0;
   } // end if not null
   if( Fct1NumericalError != 0 )
   {
      delete Fct1NumericalError; 
      Fct1NumericalError = 0; 
   } // end if not null
   if( vCoefficients != 0 )
   {
      delete vCoefficients; 
      vCoefficients = 0;
   } // end if not null
   if( vGaussian != 0 )
   {
      delete vGaussian; 
      vGaussian = 0;
   } // end if not null
   if( vUniform != 0 )
   {
      delete vUniform; 
      vUniform = 0;
   } // end if not null
   /*
   if( _theNeuroFunction != 0 )
   {
      delete ((NeuralparallelfunctioN*)_theNeuroFunction);
      _theNeuroFunction = 0;
   } // end if

   if(PthePolynomial != NULL) I don't know where this definition file is....
      delete PthePolynomial; */
} // end destructor

//***************************************************************************** 
// Class    :  OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  Default Constructor
// Purpose  :  Declares a derived class of two-variable functions from the
//          :  FunctioN class
// Notes    :  Constructs a numericalerror object with checking for divide
//          :  by zero, and a machine epsilon
//*****************************************************************************
OnevariablE::OnevariablE( _ERROR_TYPE_ EtheErr, bool isNeural ) : FunctioN(EtheErr)
{
   Setup();
   if( isNeural )
   {
      char** ppcTheNames;
      ppcTheNames = new char*[1];
      ppcTheNames[0] = new char[13];
      ppcTheNames[0][0] = '\0';
      strcpy( ppcTheNames[0], "nfunc.def" );
//    _theNeuroFunction = new NeuralparallelfunctioN( 1, ppcTheNames );
      SetEquation( 35 );
   } // end if nueral function
} // end constructor

//***************************************************************************** 
// Class    :  OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  Default Constructor
// Purpose  :  Declares a derived class of two-variable functions from the
//          :  FunctioN class
// Notes    :  Constructs a numericalerror object with checking for divide
//          :  by zero, and a machine epsilon
//*****************************************************************************
OnevariablE::OnevariablE(_ERROR_TYPE_ EtheErr, 
   long double (*UserFunc)(long double ldX)) : FunctioN(EtheErr, UserFunc)
{
   Setup();
} // end constructor                     

//***************************************************************************** 
// Class    :  OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  Setup
// Purpose  :  Sets up the various data properties.
//*****************************************************************************
void OnevariablE::Setup()
{
   vCoefficients = 0;
   vGaussian     = 0;
   vUniform      = 0;
   vIndependentVars = new VectoR( 3 );
   Fct1NumericalError = new NumerR( _OVERFLOW_ );
   ldParam1 = 1;
   ldParam2 = 0;
   ldParam3 = 0;
   ldInverse_Value = 0;
   Fct1NumericalError->SetLogging( 0 ); // NO LOGGING by default
//   _theNeuroFunction = 0;
//   PthePolynomial = NULL; I don't know where this is defined...
} // end Setup

//***************************************************************************** 
// Class    :  OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  SetLogging
// Purpose  :  Sets the logging variable for our numerr class object.
//*****************************************************************************
void OnevariablE::SetLogging(int iWhat)
{
   Fct1NumericalError->SetLogging(iWhat);
} // end SetLogging

//***************************************************************************** 
// Class    :  OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  SetVariable
// Purpose  :  Sets the variable in the independVars object indicated
//          :  by the first argument.
//*****************************************************************************
void OnevariablE::SetVariable( long double ldValue, int iWhichOne )
{
   if(iWhichOne > vIndependentVars->cnRows)
      return;
   vIndependentVars->pVariables[iWhichOne] = ldValue;
} // end SetVariable

//***************************************************************************** 
// Class    :  OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  SetParams
// Purpose  :  Sets the parameters for various evaluations
//*****************************************************************************
void OnevariablE::SetParams(long double ldOne, long double ldTwo, 
   long double ldThree)
{
   ldParam1 = ldOne;
   ldParam2 = ldTwo;
   ldParam3 = ldThree;
} // end SetParams     

//***************************************************************************** 
// Class    :  OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  EvaluateIt
// Purpose  :  The generic routine to evaluate the function
// Notes    :  
//          :  
//*****************************************************************************
long double OnevariablE::EvaluateIt()
{
long double ldRes = 0;

   switch(iWhich_Equation)
   {
      case 1 :            
         ldRes = ExpInvSquared(vIndependentVars->pVariables[0]);
      break;

      case 2 :
         ldRes = Logistic(vIndependentVars->pVariables[0]);
      break;                     

      case 3 :
         ldRes = DxLogistic(vIndependentVars->pVariables[0]);
      break;                     

      case 4 :
         ldRes = PythagoreanHypotenuse(vIndependentVars->pVariables[0]);
      break;                     

      case 5 :
         ldRes = PythagoreanMinus(vIndependentVars->pVariables[0]);
      break;                     
      
      case 6 :
         ldRes = Xsquared(vIndependentVars->pVariables[0]);
      break;                     
      
      case 7 :
         ldRes = Xcubed(vIndependentVars->pVariables[0]);
      break;                     
      
      case 8 :
         ldRes = ExpXPlusCosine(vIndependentVars->pVariables[0]);
      break;                     

      case 9 :
         ldRes = DxExpXPlusCosine(vIndependentVars->pVariables[0]);
      break;                     

      case 10 :
         ldRes = LogCosine(vIndependentVars->pVariables[0]);
      break;                     

      case 11 :
         ldRes = DxLogCosine(vIndependentVars->pVariables[0]);
      break;                     
      
      case 12 :
         ldRes = XCosine(vIndependentVars->pVariables[0]);
      break;                     
      
      case 13 :
         ldRes = DxXCosine(vIndependentVars->pVariables[0]);
      break;                     
      
      case 14 :
         ldRes = X2Log(vIndependentVars->pVariables[0]);
      break;                     
      
      case 15 :
         ldRes = DxX2Log(vIndependentVars->pVariables[0]);
      break;                     
      
      case 16 :
         ldRes = ExpX2(vIndependentVars->pVariables[0]);
      break;                     
      
      case 17 :
         ldRes = DxExpX2(vIndependentVars->pVariables[0]);
      break;                     
      
      case 18 :
         ldRes = SinXExpX(vIndependentVars->pVariables[0]);
      break;                     
      
/*    case -18 :
         ldRes = InvSinXExpX(vIndependentVars->pVariables[0]);
      break;                     
      
      case 19 :
         ldRes = DxSinXExpX(vIndependentVars->pVariables[0]);
      break;                     
      
      case 20 :
         ldRes = X2XSinCos(vIndependentVars->pVariables[0]);
      break;                     
      
      case 21 :
         ldRes = DxX2XSinCos(vIndependentVars->pVariables[0]);
      break;                     
      
      case 22 :
         ldRes = X3rdRootExpX(vIndependentVars->pVariables[0]);
      break;                     
      
      case 23 :
         ldRes = Polynomial(vIndependentVars->pVariables[0]);
      break;                     */
      
      case 24 :
         ldRes = Cosine(vIndependentVars->pVariables[0]);
      break;                     
      
      case 25 :
         ldRes = Sine(vIndependentVars->pVariables[0]);
      break;                     
      
      case 26 :
         ldRes = Gaussian(vIndependentVars->pVariables[0]);
      break; 
                          
      case 27 :
         ldRes = Linear(vIndependentVars->pVariables[0]);
      break;                     
                   
      case 28 :
         ldRes = Sigmoid(vIndependentVars->pVariables[0]);
      break;                     
                   
      case 29 :
         ldRes = SineWave(vIndependentVars->pVariables[0]);
      break;                     
                   
      case 30 :
         ldRes = CosWave(vIndependentVars->pVariables[0]);
      break;                     
                   
      case 31 :
         ldRes = CosWave2(vIndependentVars->pVariables[0]);
      break;                     
                   
      case 32 :
         ldRes = CosCos(vIndependentVars->pVariables[0]);
      break;                     
                   
      case 33 :
         ldRes = Tangent( vIndependentVars->pVariables[0] );
      break;                     
                   
      case 34 :
         ldRes = Polynomial( vIndependentVars->pVariables[0] );
      break;                     

      case 35 :
//       ldRes = NeuralFunction( vIndependentVars->pVariables[0] );
      break;                     

      case 36 :
         ldRes = FractionalBrownian( vIndependentVars->pVariables[0] );
      break;                     
                          
      case 37 :
         ldRes = ChordFunction( vIndependentVars->pVariables[0] );
      break;

      case 38 :
         ldRes = DxChordFunction( vIndependentVars->pVariables[0] );
      break;

      case 39 :
         ldRes = InverseSigmoid( vIndependentVars->pVariables[0] );
      break;

/*    case 26 :                                            
         ldRes = CosInv(vIndependentVars->pVariables[0]);
      break; 
                          
      case 27 :
         ldRes = OneMinusCosDivSqrtX(vIndependentVars->pVariables[0]);
      break;                     
                   
      case 28 :
         ldRes = SineX2(vIndependentVars->pVariables[0]);
      break;                     
                   
      case 29 :
         ldRes = OneOverSqrtX(vIndependentVars->pVariables[0]);
      break;                     
                   
      case 30 :
         ldRes = ExpInvOverSqrtX(vIndependentVars->pVariables[0]);
      break;                     
                   
      case 31 :
         ldRes = ExpInvOverSqrtX_2(vIndependentVars->pVariables[0]);
      break;                     
                   
      case 32 :
         ldRes = CosOverCos2(vIndependentVars->pVariables[0]);
      break;                     

      case 33 :
         ldRes = ExpOverExpInv(vIndependentVars->pVariables[0]);
      break;                     
                   
      case 34 :
         ldRes = XOvrExpX(vIndependentVars->pVariables[0]);
      break;                     
                   
      case 35 :
         ldRes = ExpSin2Cos(vIndependentVars->pVariables[0]);
      break;                     
      
      case 36 :
         ldRes = SinLn2X(vIndependentVars->pVariables[0]);
      break;                     
      
      case 37 :
         ldRes = CosSin2X(vIndependentVars->pVariables[0]);
      break;                     
      
      case 38 :
         ldRes = SinOverX(vIndependentVars->pVariables[0]);
      break;                     
      
      case 39 :
         ldRes = SinOverSqrtX(vIndependentVars->pVariables[0]);
      break;   

      case 40 :
         ldRes = XInverse(vIndependentVars->pVariables[0]);
      break;                     */
               
      case -1 :         
         // ldRes = 
      break;                     
   } // end switch              
   return ldRes;
} // end EvaluateIt

//***********************************************************************
// Class    :  OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  ExpInvSquared
// Purpose  :  Computes the exponential function raised to the negative
//          :  argument squared
// Equation :  1
//***********************************************************************
long double OnevariablE::ExpInvSquared(long double ldArgX)
{
long double ExpResult = 0;
long double ldTmp = 0;

   ldTmp = ldArgX * ldArgX;
   ExpResult = expl(ldTmp);
   if(Absolute(ExpResult) > ldEpsilon)
      ExpResult = 1 / ExpResult;

   return ExpResult;
} // end ExpInvSquared

//***********************************************************************
// Class    :  OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  Logistic
// Purpose  :  The famous logistic equation. 
//          :  Computes: Param3 \ (Param2 + Param3 \ e^x) + Param1
// Notes    :  The c++ expl function overflows on an argument of 1.2e4,
//          :  but not on 1.1e4
// Equation :  2
//***********************************************************************
long double OnevariablE::Logistic(long double ldArg)
{
long double ldLogRes = 0;
long double ldTemp = 0;

/*
   Fct1NumericalError->SetErrorType(_UNDERFLOW_);
   Fct1NumericalError->SetRoutineName("Logistic");
   Fct1NumericalError->SetFunctionName("expl");
   try {
      Fct1NumericalError->SetGoodVal(ldArg);
      if(ldArg > 1.1e4)
         ldArg = 1.1e4;
        */
      ldTemp = Exponential(ldArg);        // e^x
      if(Absolute(ldTemp) <= _EPSILON)
      {
                   return 1;
                /*
         Fct1NumericalError->SetGoodVal(1);
         throw Fct1NumericalError;
                        */
      } // end if ldTemp is zero
      ldLogRes = ldParam3 / ldTemp;       // param3 / e^x
      ldLogRes += ldParam2;               // param2 + (param3 / e^x)
      if(Absolute(ldLogRes) <= _EPSILON)
      {
                   return 1e97;
                /*
         Fct1NumericalError->SetGoodVal(1e97);
         throw Fct1NumericalError;
                        */
      } // end if LogRes is zero
      ldTemp = ldParam3 / ldLogRes; // param3 / (param2 + (param3 / e^x))
      if(Absolute(ldTemp) <= _EPSILON)
      {
                   return 0;
                /*
         Fct1NumericalError->SetGoodVal(0);
         throw Fct1NumericalError;
                        */
      } // end if ldTemp is zero
      ldLogRes = ldTemp + ldParam1; // [p3 / (p2 + (p3 / e^x))] + p1
      if(Absolute(ldLogRes) <= _EPSILON)
      {
                   return 0;
                /*
         Fct1NumericalError->SetGoodVal(0);
         throw Fct1NumericalError;
                        */
      } // end if ldTemp is zero
                /*
   } CARLTON_FISK;
   if(Fct1NumericalError->sHandled)
      ldLogRes = Fct1NumericalError->GetGoodVal();
                */

   return ldLogRes;

} // end Logistic equation

//***********************************************************************
// Class    :  OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  DxLogistic
// Purpose  :  The Derivative of the Logistic equation.
//          :  Computes: (Param1^2 / e^x) / (Param2 + Param1 / e^x)^2
// Notes    :  The c++ expl function overflows on an argument of 1.2e4,
//          :  but not on 1.1e4
// Equation :  3
//***********************************************************************
long double OnevariablE::DxLogistic(long double ldArg)
{
long double ldLogRes = 0;
long double ldLogTmp = 0;
long double ldTooBig = 1e2300;

/*
   Fct1NumericalError->SetErrorType(_UNDERFLOW_);
   Fct1NumericalError->SetRoutineName("DxLogistic");
   Fct1NumericalError->SetFunctionName("expl");
   try {
      if(ldArg > 1.1e4)
         ldArg = 1.1e4;
        */
//    Fct1NumericalError->SetGoodVal(ldArg);
/*    if(ldArg < (-1)*1e3)
      {
         Fct1NumericalError->SetGoodVal(1); do we really need this?
         throw Fct1NumericalError;
      } // end if ldLogTmp is zero
      */
      ldLogTmp = Exponential(ldArg);   // e^x
      if(Absolute(ldLogTmp) <= _EPSILON)
      {
                   return 0;
                /*
         Fct1NumericalError->SetGoodVal(0);
         throw Fct1NumericalError;
                        */
      } // end if ldLogTmp is zero
      ldLogRes = (ldParam1 * ldParam1) / ldLogTmp;
      ldLogTmp = ldLogRes;   // e^-x
      ldLogRes += ldParam2;  // e^-x + p2
      if(Absolute(ldLogRes) <= _EPSILON)
      {
                   return 0;
                /*
         Fct1NumericalError->SetGoodVal(0);
         throw Fct1NumericalError;
                        */
      } // end if LogRes is zero
      if(ldLogRes >= ldTooBig)
         ldLogRes = 1e2000;
      ldLogRes *= ldLogRes; // (e^-x + p2)^2
/*    if(ldLogRes >= ldTooBig)
      {
         Fct1NumericalError->SetGoodVal(0); Not sure if we really need this
         throw Fct1NumericalError;
      } // end if LogRes is too big
      */
      if(Absolute(ldLogRes) <= _EPSILON)
      {
                   return 1e97;
                /*
         Fct1NumericalError->SetGoodVal(1e97);
         throw Fct1NumericalError;
                        */
      } // end if LogRes is zero
      ldLogRes = ldLogTmp / ldLogRes; // e^-x / (e^-x + p2)^2
      if(Absolute(ldLogRes) <= _EPSILON)
      {
                   return 0;
                /*
         Fct1NumericalError->SetGoodVal(0);
         throw Fct1NumericalError;
                        */
      } // end if LogRes is zero
                /*
   } CARLTON_FISK;
   if(Fct1NumericalError->sHandled)
      ldLogRes = Fct1NumericalError->GetGoodVal();
                */

   return ldLogRes;
} // end DxLogistic equation

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : PythagoreanHypotenuse
// Purpose  : This member function computes the pythagorean
//          : relationship for a hypotenuse side, i.e., (ldA^2 + ldParam1^2)^1/2
// Notes    : This will be equation 4, and we assume that the user
//          : has set ldParam1 to the correct value. At present, we don't
//          : handle complex results. (we will in the future though.)
//****************************************************************************
long double OnevariablE::PythagoreanHypotenuse(long double ldA)
{
long double ldPyth;
long double ldTemp;

   Fct1NumericalError->SetErrorType(_UNDERFLOW_);
   Fct1NumericalError->SetRoutineName("Pythagorean Hypotenuse");
   Fct1NumericalError->SetFunctionName("sqrtl");
   try {
      if(Absolute(ldA) <= GetEpsilon()) {
         Fct1NumericalError->SetGoodVal(0);
         throw Fct1NumericalError; }
      ldPyth = ldA * ldA; // square it
      if(Absolute(ldPyth) <= GetEpsilon()) {
         Fct1NumericalError->SetGoodVal(0);
         throw Fct1NumericalError; }
      ldTemp = ldParam1 * ldParam1;
      if(Absolute(ldTemp) <= GetEpsilon())
         ldTemp = 0;
      ldPyth += ldTemp;
      if(Absolute(ldPyth) <= GetEpsilon()) {
         Fct1NumericalError->SetGoodVal(0);
         throw Fct1NumericalError; }
      // now for the big show...
      ldPyth = sqrtl(ldPyth);
   } CARLTON_FISK;
   if(Fct1NumericalError->sHandled)
      ldPyth = Fct1NumericalError->GetGoodVal();

   return ldPyth;
} // end PythagoreanHypotenuse

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : PythagoreanMinus
// Purpose  : This member function computes the pythagorean
//          : relationship for a non-hypotenuse side, i.e., 
//          : (ldA^2 - ldParam1^2)^1/2
// Notes    : This will be equation 5, and we assume that the user
//          : has set ldParam1 to the correct value. At present, we don't
//          : handle complex results. (we will in the future though.)
//****************************************************************************
long double OnevariablE::PythagoreanMinus(long double ldA)
{
long double ldPyth;
long double ldTemp;

   Fct1NumericalError->SetErrorType(_UNDERFLOW_);
   Fct1NumericalError->SetRoutineName("Pythagorean Minus");
   Fct1NumericalError->SetFunctionName("sqrtl");
   try {
      if(Absolute(ldA) <= GetEpsilon()) {
         Fct1NumericalError->SetGoodVal(0);
         throw Fct1NumericalError; }
      ldPyth = ldA * ldA; // square it
      if(Absolute(ldPyth) <= GetEpsilon()) {
         Fct1NumericalError->SetGoodVal(0);
         throw Fct1NumericalError; }
      ldTemp = ldParam1 * ldParam1;
      if(Absolute(ldTemp) <= GetEpsilon())
         ldTemp = 0;
      if(ldTemp >= ldPyth) {
         Fct1NumericalError->SetGoodVal(0);
         throw Fct1NumericalError; }
      ldPyth = ldPyth - ldTemp;
      if(Absolute(ldPyth) <= GetEpsilon()) {
         Fct1NumericalError->SetGoodVal(0);
         throw Fct1NumericalError; }
      // now for the big show...
      ldPyth = sqrtl(ldPyth);
   } CARLTON_FISK;
   if(Fct1NumericalError->sHandled)
      ldPyth = Fct1NumericalError->GetGoodVal();

   return ldPyth;
} // end PythagoreanMinus

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Xsquared
// Purpose  : Implements the following function: 
//          : f(x) = param1 * X * X + param3 * X + param2
// Notes    : This will be considered equation number 6. We assume upon
//          : execution that the user has properly set the parameters.
//          : 
//****************************************************************************
long double OnevariablE::Xsquared(long double ldX)
{
long double ldX2 = ldX;

   Fct1NumericalError->SetErrorType(_OVERFLOW_);
   Fct1NumericalError->SetRoutineName("XSquared");
   Fct1NumericalError->SetFunctionName("Multiplication (*)");
   try {
      Fct1NumericalError->SetGoodVal(ldX);
      ldX *= ldX; // x^2
      ldX *= ldParam1; // param1 * x^2
      if(ldX < ldX2)
         throw Fct1NumericalError; // had an overflow
      ldX += ldX2 * ldParam3; // P1 * x^2 + P3 * x
      ldX += ldParam2; // P1 * x^2 + P3 * x + P2
      ldX2 = ldX;
   } CARLTON_FISK;
   if(Fct1NumericalError->sHandled)
      ldX2 = Fct1NumericalError->GetGoodVal();

   return ldX2;
} // end Xsquared

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Xcubed
// Purpose  : Implements the following function: 
//          : f(x) = param1 * X^3 + param2
// Notes    : This will be considered equation number 7. We assume upon
//          : execution that the user has properly set the parameters.
//****************************************************************************
long double OnevariablE::Xcubed(long double ldX)
{
long double ldX3 = ldX;

   Fct1NumericalError->SetErrorType(_OVERFLOW_);
   Fct1NumericalError->SetRoutineName("XCubed");
   Fct1NumericalError->SetFunctionName("Multiplication (*)");
   try {
      Fct1NumericalError->SetGoodVal(ldX);
      ldX *= ldX; // x^2
      ldX *= ldX3; // x^3
      ldX *= ldParam1; // x^3 * P1
      if(ldX < ldX3)
         throw Fct1NumericalError; // had an overflow
      ldX += ldParam3 * ldX3; // P1 * x^3 + P3 * x
      ldX3 = ldX + ldParam2; // P1 * x^3 + P3 * x + P2
   } CARLTON_FISK;
   if(Fct1NumericalError->sHandled)
      ldX3 = Fct1NumericalError->GetGoodVal();

   return ldX3;
} // end Xcubed

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : ExpXPlusCosine
// Purpose  : Implements the following function: 
//          : f(x) = e^x + param1^-x + param2*cosx + param3
// Notes    : This will be considered equation number 8. We assume upon
//          : execution that the user has properly set the parameters.
//****************************************************************************
long double OnevariablE::ExpXPlusCosine(long double ldX)
{
long double ldEx;
long double ldTemp1;

   Fct1NumericalError->SetErrorType(_OVERFLOW_);
   Fct1NumericalError->SetRoutineName("ExpXPlusCosine");
   Fct1NumericalError->SetFunctionName("Cosine/Exp");
   try {
      Fct1NumericalError->SetGoodVal(ldX);
      ldTemp1 = DecimalPower(ldParam1, ldX); // 2^x
      if(Absolute(ldTemp1) > GetEpsilon())
         ldTemp1 = 1 / ldTemp1; // 2^-x
      else
         ldTemp1 = 0;
      ldEx = Exponential(ldX) + ldTemp1; // e^x + 2^-x
      if(Absolute(ldEx) <= GetEpsilon())
         ldEx = 0;
      ldEx += ldParam2 * cosl(ldX); // e^x + 2^-x + 2cos(x)
      ldEx += ldParam3; // e^x + 2^-x + 2cos(x) - 6
   } CARLTON_FISK;
   if(Fct1NumericalError->sHandled)
      ldEx = Fct1NumericalError->GetGoodVal();

   return ldEx;                        
} // end ExpXPlusCosine

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : DxExpXPlusCosine
// Purpose  : Implements the first derivative of ExpXPlusCosine: 
//          : f(x) = e^x - ln(2)*param1^-x - param2*sinx
// Notes    : This will be considered equation number 9. We assume upon
//          : execution that the user has properly set the parameters.
//****************************************************************************
long double OnevariablE::DxExpXPlusCosine(long double ldX)
{
long double ldDxEx;
long double ldTemp1;
const long double ldTwo = 2;

   Fct1NumericalError->SetErrorType(_OVERFLOW_);
   Fct1NumericalError->SetRoutineName("Dx(ExpXPlusCosine)");
   Fct1NumericalError->SetFunctionName("Sine/Exp/Log");
   try {
      Fct1NumericalError->SetGoodVal(ldX);
      ldTemp1 = DecimalPower(ldParam1, ldX); // 2^x
      if(Absolute(ldTemp1) > GetEpsilon())
         ldTemp1 = 1 / ldTemp1; // 2^-x
      else
         ldTemp1 = 0;
      ldTemp1 *= logl(ldTwo);
      ldDxEx = Exponential(ldX) - ldTemp1; // e^x - ln(2)*2^-x
      if(Absolute(ldDxEx) <= GetEpsilon())
         ldDxEx = 0;
      ldDxEx -= ldParam2 * sinl(ldX); // e^x + 2^-x + 2cos(x)
   } CARLTON_FISK;
   if(Fct1NumericalError->sHandled)
      ldDxEx = Fct1NumericalError->GetGoodVal();

   return ldDxEx;
} // end DxExpXPlusCosine

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : LogCosine
// Purpose  : Implements the function:
//          : f(x) = log(x + param1) + param2 * cos(x + param3)
// Notes    : This will be considered equation number 10. We assume upon
//          : execution that the user has properly set the parameters.
//****************************************************************************
long double OnevariablE::LogCosine(long double ldX)
{
long double ldLog;
long double ldTmp;

   Fct1NumericalError->SetErrorType(_OVERFLOW_);
   Fct1NumericalError->SetRoutineName("LogCosine");
   Fct1NumericalError->SetFunctionName("Cosine/Log");
   try {
      Fct1NumericalError->SetGoodVal(ldX);
      ldTmp = ldX + ldParam1;
      //ldTmp = Absolute(ldTmp); // x + param1
      if(ldTmp <= GetEpsilon()) {
         ldLog = ldParam2 * cosl(ldParam3);
         Fct1NumericalError->SetGoodVal(ldLog);
         throw Fct1NumericalError;
      } // end if zero
      ldLog = NaturalLog(ldTmp); // ln(x+param1)
      if(Absolute(ldLog) <= GetEpsilon())
         ldLog = 0;
      ldTmp = ldParam2 * cosl(ldParam3 + ldX); // param2 * cos(param3+x)
      if(Absolute(ldTmp) <= GetEpsilon())
         ldTmp = 0;
      ldLog += ldTmp;
   } CARLTON_FISK;
   if(Fct1NumericalError->sHandled)
      ldLog = Fct1NumericalError->GetGoodVal();

   return ldLog;
} // end LogCosine

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : DxLogCosine
// Purpose  : Implements the derivative of the LogCosine function:
//          : f(x) = (x+param1)^-1 - param2 * sin(x + param3)
// Notes    : This will be considered equation number 11. We assume upon
//          : execution that the user has properly set the parameters.
//****************************************************************************
long double OnevariablE::DxLogCosine(long double ldX)
{
long double ldDxLog;
long double ldTmp;

   Fct1NumericalError->SetErrorType(_OVERFLOW_);
   Fct1NumericalError->SetRoutineName("DxLogCosine");
   Fct1NumericalError->SetFunctionName("Sine/x^-1");
   try {
      Fct1NumericalError->SetGoodVal(ldX);
      ldTmp = ldX + ldParam1;
      if(Absolute(ldTmp) <= GetEpsilon()) { // divide by zero!
         ldDxLog = ldParam2 * sinl(ldX + ldParam3);
         ldDxLog *= -1;
         Fct1NumericalError->SetGoodVal(ldDxLog);
         throw Fct1NumericalError;
      } // end if zero                                    
      ldDxLog = 1 / ldTmp;                   // (x + param1)^-1
      if(Absolute(ldDxLog) <= GetEpsilon())
         ldDxLog = 0;
      ldTmp = sinl(ldX + ldParam3);  // don't know how sensitive sinl is.
      if(Absolute(ldTmp) <= GetEpsilon())
         ldTmp = 0;
      ldDxLog -= ldParam2 * ldTmp; // param2*sin(param3+x)
      if(Absolute(ldDxLog) <= GetEpsilon())
         ldDxLog = 0;
   } CARLTON_FISK;
   if(Fct1NumericalError->sHandled)
      ldDxLog = Fct1NumericalError->GetGoodVal();

   return ldDxLog;
} // end DxLogCosine


//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : XCosine
// Purpose  : Implements the function:
//          : f(x) = param1*X*cos(param1*X) + param2*(X + param3)^2
// Notes    : This will be considered equation number 12. We assume upon
//          : execution that the user has properly set the parameters.
//****************************************************************************
long double OnevariablE::XCosine(long double ldX)
{
long double ldXC;
long double ldTemp1;

   Fct1NumericalError->SetErrorType(_OVERFLOW_);
   Fct1NumericalError->SetRoutineName("XCosine");
   Fct1NumericalError->SetFunctionName("Cosine/X^2");
   try {
      Fct1NumericalError->SetGoodVal(ldX);
      ldTemp1 = ldParam1 * ldX;           // param1 * X
      if(Absolute(ldTemp1) <= GetEpsilon())
         ldTemp1 = 0;
      ldXC = ldTemp1 * cosl(ldTemp1);
      if(Absolute(ldXC) <= GetEpsilon())
         ldXC = 0;
      ldTemp1 = ldX + ldParam3;
      if(Absolute(ldTemp1) <= GetEpsilon())
         ldTemp1 = 0;
      ldTemp1 = DecimalPower(ldTemp1, 2);
      if(Absolute(ldTemp1) <= GetEpsilon())
         ldTemp1 = 0;
      ldTemp1 *= ldParam2;
      if(Absolute(ldTemp1) <= GetEpsilon())
         ldTemp1 = 0;
      ldXC += ldTemp1;
      if(Absolute(ldXC) <= GetEpsilon())
         ldXC = 0;
   } CARLTON_FISK;          
   if(Fct1NumericalError->sHandled)
      ldXC = Fct1NumericalError->GetGoodVal();

   return ldXC;
} // end XCosine

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : DxXCosine
// Purpose  : Implements the derivative of the XCosine function:
//          : f(x) = -1*param1^2*x*sine(param1*x) + param1*cos(param1*x)
//          :  param2 * 2(x + param3)
// Notes    : This will be considered equation number 13. We assume upon
//          : execution that the user has properly set the parameters.
//****************************************************************************
long double OnevariablE::DxXCosine(long double ldX)
{
long double ldDxXC;
long double ldTemp0;
long double ldTemp1;

   Fct1NumericalError->SetErrorType(_OVERFLOW_);
   Fct1NumericalError->SetRoutineName("DxXCosine");
   Fct1NumericalError->SetFunctionName("Sine/Cosine/X");
   try {
      Fct1NumericalError->SetGoodVal(ldX);
      ldTemp0 = ldParam1 * ldX;              // param1 * x
      if(Absolute(ldTemp0) <= GetEpsilon())
         ldTemp0 = 0;
      ldDxXC = ldParam1 * ldTemp0 * sinl(ldTemp0); // p1^2*x*sin(p1*x)
      if(Absolute(ldDxXC) <= GetEpsilon())
         ldDxXC = 0;
      ldDxXC *= -1;              // -1 * p1^2*x*sin(p1*x)
      ldTemp1 = ldParam1 * cosl(ldTemp0);
      if(Absolute(ldTemp1) <= GetEpsilon())
         ldTemp1 = 0;
      ldDxXC += ldTemp1;
      ldTemp0 = ldX + ldParam3;
      if(Absolute(ldTemp0) <= GetEpsilon())
         ldTemp0 = 0;
      ldTemp0 *= 2 * ldParam2;         // param2 * 2(x + param3)
      if(Absolute(ldTemp0) <= GetEpsilon())
         ldTemp0 = 0;
      ldDxXC += ldTemp0;
   } CARLTON_FISK;                       
   if(Fct1NumericalError->sHandled)
      ldDxXC = Fct1NumericalError->GetGoodVal();

   return ldDxXC;
} // end DxXCosine

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : X2Log
// Purpose  : Implements the function:
//          : f(x) = (x + param1)^2 + param2 * ln(x)
// Notes    : This will be considered equation number 14. We assume upon
//          : execution that the user has properly set the parameters.
//****************************************************************************
long double OnevariablE::X2Log(long double ldX)
{
long double ldRes;
long double ldTemp0;

   Fct1NumericalError->SetErrorType(_OVERFLOW_);
   Fct1NumericalError->SetRoutineName("X2Log");
   Fct1NumericalError->SetFunctionName("Log/X^2");
   try {
      Fct1NumericalError->SetGoodVal(ldX);
      ldTemp0 = ldParam1 + ldX;
      if(Absolute(ldTemp0) <= GetEpsilon())
         ldTemp0 = 0;
      ldRes = DecimalPower(ldTemp0, 2);
      if(Absolute(ldRes) <= GetEpsilon())
         ldRes = 0;
      ldTemp0 = ldParam2 * NaturalLog(ldX);
      if(Absolute(ldTemp0) <= GetEpsilon())
         ldTemp0 = 0;
      ldRes += ldTemp0;
   } CARLTON_FISK;                         
   if(Fct1NumericalError->sHandled)
      ldRes = Fct1NumericalError->GetGoodVal();

   return ldRes;
} // end X2Log

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : DxX2Log
// Purpose  : Implements the derivative of the X2Log function:
//          : f(x) = 2(x + param1) + param2 / x
// Notes    : This will be considered equation number 15. We assume upon
//          : execution that the user has properly set the parameters.
//****************************************************************************
long double OnevariablE::DxX2Log(long double ldX)
{
long double ldRes;
long double ldTemp0;

   Fct1NumericalError->SetErrorType(_OVERFLOW_);
   Fct1NumericalError->SetRoutineName("DxX2Log");
   Fct1NumericalError->SetFunctionName("X^-1/X");
   try {
      if(Absolute(ldX) <= GetEpsilon()) {
         Fct1NumericalError->SetGoodVal(0);
         throw Fct1NumericalError;
      } // end if zero
      ldTemp0 = ldParam2 / ldX;
      if(Absolute(ldTemp0) <= GetEpsilon())
         ldTemp0 = 0;
      ldRes = 2 * (ldX + ldParam1);
      if(Absolute(ldRes) <= GetEpsilon())
         ldRes = 0;
      ldRes += ldTemp0;
   } CARLTON_FISK;                       
   if(Fct1NumericalError->sHandled)
      ldRes = Fct1NumericalError->GetGoodVal();

   return ldRes;
} // end DxX2Log

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : ExpX2
// Purpose  : Implements the function:
//          : f(x) = e^x + 3 * param1 * x^2
// Notes    : This will be considered equation number 16. We assume upon
//          : execution that the user has properly set the parameters.
//****************************************************************************
long double OnevariablE::ExpX2(long double ldX)
{
long double ldRes1;

   Fct1NumericalError->SetErrorType(_OVERFLOW_);
   Fct1NumericalError->SetRoutineName("ExpX2");
   Fct1NumericalError->SetFunctionName("Exponential/X^2");
   try {
      ldRes1 = 3 * ldParam1 * ldX;
      if(Absolute(ldRes1) <= GetEpsilon())
         ldRes1 = 0;
      ldRes1 *= ldX;
      ldRes1 += Exponential(ldX);
      if(Absolute(ldRes1) <= GetEpsilon())
         ldRes1 = 0;
   } CARLTON_FISK;                       
   if(Fct1NumericalError->sHandled)
      ldRes1 = Fct1NumericalError->GetGoodVal();

   return ldRes1;
} // end ExpX2

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : DxExpX2
// Purpose  : Implements the derivative of the ExpX2 function:
//          : f(x) = e^x + 6 * param1 * x
// Notes    : This will be considered equation number 17. We assume upon
//          : execution that the user has properly set the parameters.
//****************************************************************************
long double OnevariablE::DxExpX2(long double ldX)
{
long double ldRes;

   Fct1NumericalError->SetErrorType(_OVERFLOW_);
   Fct1NumericalError->SetRoutineName("DxExpX2");
   Fct1NumericalError->SetFunctionName("DxExpX2/X");
   try {
      ldRes = 6 * ldParam1 * ldX;
      if(Absolute(ldRes) <= GetEpsilon())
         ldRes = 0;
      ldRes += Exponential(ldX);
      if(Absolute(ldRes) <= GetEpsilon())
         ldRes = 0;
   } CARLTON_FISK;                       
   if(Fct1NumericalError->sHandled)
      ldRes = Fct1NumericalError->GetGoodVal();

   return ldRes;
} // end DxExpX2

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : SinXExpX
// Purpose  : Implements the function:
//          : f(x) = Sin(x) + param1 * e^(-x)
// Notes    : This will be considered equation number 18. We assume upon
//          : execution that the user has properly set the parameters.
//****************************************************************************
long double OnevariablE::SinXExpX(long double ldX)
{
long double ldRes;
long double ldTemp0;

   Fct1NumericalError->SetErrorType(_OVERFLOW_);
   Fct1NumericalError->SetRoutineName("SinXExpX");
   Fct1NumericalError->SetFunctionName("Sine/Exp");
   try {
      ldTemp0 = Exponential(ldX);
      if(Absolute(ldTemp0) <= GetEpsilon()) {
         Fct1NumericalError->SetGoodVal(0);
         throw Fct1NumericalError;
      } // end if zero
      ldTemp0 = ldParam1 / ldTemp0;
      if(Absolute(ldTemp0) <= GetEpsilon())
         ldTemp0 = 0;
      ldRes = sinl(ldX);
      if(Absolute(ldRes) <= GetEpsilon())
         ldRes = 0;
      ldRes += ldTemp0;
   } CARLTON_FISK;                       
   if(Fct1NumericalError->sHandled)
      ldRes = Fct1NumericalError->GetGoodVal();

   return ldRes;
} // end SinXExpX

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : DxSinXExpX
// Purpose  : Implements the derivative of the SinXExpX function:
//          : f(x) = Cos(x) + param1 * e^(-x)
// Notes    : This will be considered equation number 19. We assume upon
//          : execution that the user has properly set the parameters.
//****************************************************************************
long double OnevariablE::DxSinXExpX(long double ldX)
{
long double ldRes;
long double ldTemp0;

   Fct1NumericalError->SetErrorType(_OVERFLOW_);
   Fct1NumericalError->SetRoutineName("DxSinXExpX");
   Fct1NumericalError->SetFunctionName("Cosine/Exp");
   try {
      ldTemp0 = Exponential(ldX);
      if(Absolute(ldTemp0) <= GetEpsilon()) {
         Fct1NumericalError->SetGoodVal(0);
         throw Fct1NumericalError;
      } // end if zero
      ldTemp0 = ldParam1 / ldTemp0;
      if(Absolute(ldTemp0) <= GetEpsilon())
         ldTemp0 = 0;
      ldRes = cosl(ldX);
      if(Absolute(ldRes) <= GetEpsilon())
         ldRes = 0;
      ldRes += ldTemp0;
   } CARLTON_FISK;                       
   if(Fct1NumericalError->sHandled)
      ldRes = Fct1NumericalError->GetGoodVal();

   return ldRes;
} // end DxSinXExpX

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : X2XSinCos
// Purpose  : Implements the function:
//          : f(x) = param1 + param2 * x^2 + param3 * x * sin(x)
//          :  - param1 * cos(2x)
// Notes    : This will be considered equation number 20. We assume upon
//          : execution that the user has properly set the parameters.
//****************************************************************************
long double OnevariablE::X2XSinCos(long double ldX)
{
long double ldX2X;
long double ldTemp;

   Fct1NumericalError->SetErrorType(_OVERFLOW_);
   Fct1NumericalError->SetRoutineName("X2XSinCos");
   Fct1NumericalError->SetFunctionName("Sine/Cosine/X2");
   try {
      if(ldX >= 1e30) {
         Fct1NumericalError->SetGoodVal(1e30);
         throw Fct1NumericalError;
      } // end if too big
      ldTemp = ldX * ldX;
      if(ldTemp >= 1e30) {
         Fct1NumericalError->SetGoodVal(1e30);
         throw Fct1NumericalError;          
      } // end if too big
      if(Absolute(ldTemp) <= GetEpsilon())
         ldTemp = 0;
      ldTemp *= ldParam2;        // param2 * x^2
      ldTemp += ldParam1;        // param1 + param2 * x^2
      if(Absolute(ldTemp) <= GetEpsilon())
         ldTemp = 0;
      ldX2X = ldX * ldParam3;    // param3 * x
      if(Absolute(ldX2X) <= GetEpsilon())
         ldX2X = 0;
      ldX2X *= sinl(ldX);        // param3 * x * sin(x)
      if(Absolute(ldX2X) <= GetEpsilon())
         ldX2X = 0;
      ldX2X += ldTemp; // param1 + param2 * x^2 + param3 * x * sin(x)
      if(Absolute(ldX2X) <= GetEpsilon())
         ldX2X = 0;
      ldTemp = cosl(2 * ldX);       // cos(2x)
      if(Absolute(ldTemp) <= GetEpsilon())
         ldTemp = 0;
      ldTemp *= ldParam1;           // param1 * cos(2x)
      if(Absolute(ldTemp) <= GetEpsilon())
         ldTemp = 0;
      ldX2X -= ldTemp;
      if(Absolute(ldX2X) <= GetEpsilon())
         ldX2X = 0;
   } CARLTON_FISK;                            
   if(Fct1NumericalError->sHandled)
      ldX2X = Fct1NumericalError->GetGoodVal();

   return ldX2X;
} // end X2XSinCos

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : DxX2XSinCos
// Purpose  : Implements the derivative of the X2XSinCos function:
//          : f(x) = ldX * (param1 + param2 * cos(x)) + param2 * sin(x)
//          :  + sin(2x)
// Notes    : This will be considered equation number 21. We assume upon
//          : execution that the user has properly set the parameters.
//****************************************************************************
long double OnevariablE::DxX2XSinCos(long double ldX)
{
long double ldDxX2;
long double ldTemp;

   Fct1NumericalError->SetErrorType(_OVERFLOW_);
   Fct1NumericalError->SetRoutineName("Dx(X2XSinCos)");
   Fct1NumericalError->SetFunctionName("Sine/Cosine/X2");
   try {
      if(ldX >= 1e18) {
         Fct1NumericalError->SetGoodVal(3.97498e17);
         throw Fct1NumericalError;
      } // end if too big
      ldTemp = ldParam2 * cosl(ldX);         // param2 * cos(x)
      if(ldTemp >= 1e18) {
         Fct1NumericalError->SetGoodVal(3.97498e17);
         throw Fct1NumericalError;
      } // end if too big
      if(Absolute(ldTemp) <= GetEpsilon())
         ldTemp = 0;
      ldTemp += ldParam1;                    // param1 + param2 * cos(x)
      if(Absolute(ldTemp) <= GetEpsilon())
         ldTemp = 0;
      ldTemp *= ldX;             // ldX * (param1 + param2 * cos(x))
      if(ldTemp >= 1e18) {
         Fct1NumericalError->SetGoodVal(3.97498e17);
         throw Fct1NumericalError;
      } // end if too big
      if(Absolute(ldTemp) <= GetEpsilon())
         ldTemp = 0;
      ldDxX2 = ldParam2 * sinl(ldX);   // param2 * sin(x)
      if(Absolute(ldDxX2) <= GetEpsilon())
         ldDxX2 = 0;
      ldDxX2 += ldTemp; //ldX * (param1 + param2 * cos(x)) + param2 * sin(x)
      if(Absolute(ldDxX2) <= GetEpsilon())
         ldDxX2 = 0;
      ldDxX2 += sinl(2 * ldX);
      if(Absolute(ldDxX2) <= GetEpsilon())
         ldDxX2 = 0;
   } CARLTON_FISK;                       
   if(Fct1NumericalError->sHandled)
      ldDxX2 = Fct1NumericalError->GetGoodVal();

   return ldDxX2;
} // end DxX2XSinCos

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : X3rdRootExpX
// Purpose  : Implements the X3rdRootExpX function:
//          : f(x) = [X^(1/3)]/e^(x^2)
// Notes    : This will be considered equation number 22. We assume upon
//          : execution that the user has properly set the parameters.
//****************************************************************************
long double OnevariablE::X3rdRootExpX(long double ldArg)
{
long double ldX3;
long double ldT;
int iIsNeg = 0;

   Fct1NumericalError->SetErrorType(_OVERFLOW_);
   Fct1NumericalError->SetRoutineName("X3rdRootExpX");
   Fct1NumericalError->SetFunctionName("X^1/3 / Exponential");
   try {
      if(ldArg <= GetEpsilon())
         iIsNeg = 1;
      if(Absolute(ldArg) <= GetEpsilon()) {
         Fct1NumericalError->SetGoodVal(0);
         throw Fct1NumericalError;
      } // end if argument is zero
      ldX3 = DecimalPower(ldArg, ONE_THIRD); // X^(1/3)
      if(Absolute(ldX3) <= GetEpsilon()) {
         Fct1NumericalError->SetGoodVal(0);
         throw Fct1NumericalError;
      } // end if zero
      ldT = ldArg * ldArg;                     // x^2
      if(Absolute(ldT) <= GetEpsilon()) {
         Fct1NumericalError->SetGoodVal(ldX3);
         throw Fct1NumericalError;
      } // end denominator is one
      ldT = Exponential(ldT);                // e^(x^2)
      if(Absolute(ldT) <= GetEpsilon()) {
         Fct1NumericalError->SetGoodVal(ldX3);
         throw Fct1NumericalError;
      } // end denominator is zero
      ldX3 /= ldT;                        // [X^(1/3)]/e^(x^2)
      if(Absolute(ldX3) <= GetEpsilon()) {
         Fct1NumericalError->SetGoodVal(0);
         throw Fct1NumericalError;
      } // end if result is zero
   } CARLTON_FISK;
   if(Fct1NumericalError->sHandled)
      ldX3 = Fct1NumericalError->GetGoodVal();
   if(iIsNeg && (ldX3 >= GetEpsilon()))
      ldX3 *= -1;
      
   return ldX3;
} // end X3rdroot

//******************************************************************************
// Our Overwritten functions that should re-appear someday!
//-----------------------------------------------------------------------------
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//******************************************************************************

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Cosine
// Purpose  : Implements the Cosine function:
//          : f(x) = P1 * Cos(P2 * X + P3)
// Notes    : This will be considered equation number 24. We assume upon
//          : execution that the user has properly set the parameters.
//****************************************************************************
long double OnevariablE::Cosine(long double ldCosArg)
{
long double ldTmp = 0;
long double ldRet = 0;

   Fct1NumericalError->SetErrorType(_OVERFLOW_);
   Fct1NumericalError->SetRoutineName("Cosine");
   Fct1NumericalError->SetFunctionName("cosl");
   try {
      ldTmp = ldParam2 * ldCosArg;
      ldTmp += ldParam3;
      ldRet = cosl(ldTmp);
      ldRet *= ldParam1;
      if(Absolute(ldRet) <= GetEpsilon())
         ldRet = 0;
   } CARLTON_FISK;
   if(Fct1NumericalError->sHandled)
      ldRet = Fct1NumericalError->GetGoodVal();
      
   return ldRet;
} // end Cosine             

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Sine
// Purpose  : Implements the Sine function:
//          : f(x) = P1 * Sin(P2 * X + P3)
// Notes    : This will be considered equation number 25. We assume upon
//          : execution that the user has properly set the parameters.
//****************************************************************************
long double OnevariablE::Sine(long double ldSinArg)
{
long double ldTmp = 0;
long double ldRet = 0;

   Fct1NumericalError->SetErrorType(_OVERFLOW_);
   Fct1NumericalError->SetRoutineName("Sine");
   Fct1NumericalError->SetFunctionName("sinl");
   try {
      ldTmp = ldParam2 * ldSinArg;
      ldTmp += ldParam3;
      ldRet = sinl(ldTmp);
      ldRet *= ldParam1;
      if(Absolute(ldRet) <= GetEpsilon())
         ldRet = 0;
   } CARLTON_FISK;
   if(Fct1NumericalError->sHandled)
      ldRet = Fct1NumericalError->GetGoodVal();

   return ldRet;
} // end Sine             

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Gaussian
// Purpose  : Implements the Gaussian function:
//          : f(x) = e^-P1(P2 - x)^2
// Notes    : This will be considered equation number 26. We assume upon
//          : execution that the user has properly set the parameters.
//****************************************************************************
long double OnevariablE::Gaussian( long double ldGaussArg )
{
long double ldRet = 0;

   Fct1NumericalError->SetErrorType(_OVERFLOW_);
   Fct1NumericalError->SetRoutineName("Gaussian");
   Fct1NumericalError->SetFunctionName("expl");
   try {
      ldRet = ldParam2 - ldGaussArg; // (P2 - x)
      ldRet *= ldRet;               // (P2 - x)^2
      ldRet *= ldParam1;            // P1(P2 - x)^2
      ldRet *= -1;                  // -P1(P2 - x)^2
      ldRet = expl( ldRet );        // e^-P1(P2 - x)^2
   } CARLTON_FISK;
   if(Fct1NumericalError->sHandled)
      ldRet = Fct1NumericalError->GetGoodVal();

   return ldRet;
} // end Gaussian                   

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Linear
// Purpose  : Implements the Linear function:
//          : f(x) = P1 * x + P2
// Notes    : This will be considered equation number 27. We assume upon
//          : execution that the user has properly set the parameters.
//****************************************************************************
long double OnevariablE::Linear( long double ldLineArg )
{
long double ldRet = 0;

   Fct1NumericalError->SetErrorType(_OVERFLOW_);
   Fct1NumericalError->SetRoutineName("Linear");
   Fct1NumericalError->SetFunctionName("Multiplication (*)");
   try {
      ldRet = ldParam1 * ldLineArg + ldParam2;
   } CARLTON_FISK;
   if(Fct1NumericalError->sHandled)
      ldRet = Fct1NumericalError->GetGoodVal();

   return ldRet;
} // end Linear

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Sigmoid1
// Purpose  : Implements the Sigmoid1 function:
//          : f(x) = 2 * [(x - P1) / (P3 - P1) ]^2
// Notes    : This will be a worker function for the more general
//          : Sigmoid function
//****************************************************************************
long double OnevariablE::Sigmoid1( long double ldSig1Arg )
{
long double ldRet = 0;
long double ldTemp = 0;                       

// Fct1NumericalError->SetErrorType(_OVERFLOW);
// Fct1NumericalError->SetRoutineName("Sigmoid1");
// Fct1NumericalError->SetFunctionName("Multiplication (*)");
      ldTemp = ldParam3 - ldParam1;    // (P3 - P1)
        if( Absolute( ldTemp ) <= GetEpsilon() )
        {
           return 0;
        }
      ldRet = ldSig1Arg - ldParam1;    // (x - P1)
      ldRet /= ldTemp;                 // (x - P1) / (P3 - P1)
      ldRet *= ldRet;                  // [(x - P1) / (P3 - P1) ]^2
      ldRet *= 2;                      // 2 * [(x - P1) / (P3 - P1) ]^2

   return ldRet;
} // end Sigmoid1

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Sigmoid2
// Purpose  : Implements the Sigmoid2 function:
//          : f(x) = 1 - 2 * [(x - P3) / (P3 - P1) ]^2
// Notes    : This will be a worker function for the more general
//          : Sigmoid function
//****************************************************************************
long double OnevariablE::Sigmoid2( long double ldSig2Arg )
{
long double ldRet = 0;
long double ldTemp = 0;

// Fct1NumericalError->SetErrorType(_OVERFLOW);
// Fct1NumericalError->SetRoutineName("Sigmoid2");
// Fct1NumericalError->SetFunctionName("Multiplication (*)");
      ldTemp = ldParam3 - ldParam1;    // (P3 - P1)
        if( Absolute( ldTemp ) <= GetEpsilon() )
        {
           return 0;
        }
      ldRet = ldSig2Arg - ldParam3;    // (x - P3)
      ldRet /= ldTemp;                 // (x - P3) / (P3 - P1)
      ldRet *= ldRet;                  // [(x - P3) / (P3 - P1) ]^2
      ldRet *= 2;                      // 2 * [(x - P3) / (P3 - P1) ]^2

   return (1 - ldRet);                 // 1 - 2 * [(x - P3) / (P3 - P1) ]^2
} // end Sigmoid2

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Sigmoid
// Purpose  : Implements the Sigmoid function:
//          : If ( x < ldParam2 )
//          :     f(x) = Sigmoid1(x)
//          : else if ( x >= ldParam2 )
//          :     f(x) = Sigmoid1(x)
// Notes    : This will be considered equation number 28 We assume upon
//          : execution that the user has properly set the parameters.
//****************************************************************************
long double OnevariablE::Sigmoid( long double ldSigArg )
{
long double ldRet = 0;

   if( ldSigArg <= ldParam2 )
      ldRet = Sigmoid1( ldSigArg );
   else if( ldSigArg > ldParam2 )
      ldRet = Sigmoid2( ldSigArg );

   return ldRet;
} // end Sigmoid

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : SineWave
// Purpose  : Implements the Sine Wave function:
//          :   f(x) = e^(p1 * x)(sin(p2*x + p3))
//          : 
// Notes    : This will be considered equation number 29. We assume upon
//          : execution that the user has properly set the parameters.
//****************************************************************************
long double OnevariablE::SineWave( long double ldSineWavArg )
{
long double ldSineWavRet = 0;
long double ldOlTemp = 0;

   try {
      if( Absolute( ldSineWavArg ) <= GetEpsilon() )
         ldSineWavArg = 0;
      ldOlTemp = ldParam1 * ldSineWavArg; // p1 * x
      ldSineWavRet = Exponential( ldOlTemp ); // e^(p1 * x)
      ldOlTemp = ldParam2 * ldSineWavArg + ldParam3; // p2*x + p3
      ldOlTemp = sinl( ldOlTemp ); // sin(p2*x + p3)
      ldSineWavRet *= ldOlTemp; // e^(p1 * x)(sin(p2*x + p3))
      if( Absolute( ldSineWavRet ) <= GetEpsilon() )
         ldSineWavRet = 0;
   } CARLTON_FISK;                 
   if(Fct1NumericalError->sHandled)
      ldSineWavRet = Fct1NumericalError->GetGoodVal();

   return ldSineWavRet;
} // end SineWave

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : CosWave
// Purpose  : Implements the Cosine Wave function:
//          :   f(x) = p1 * e^(p2 * x)cos(p3*x)
//          : 
// Notes    : This will be considered equation number 30. We assume upon
//          : execution that the user has properly set the parameters.
//****************************************************************************
long double OnevariablE::CosWave( long double ldCosWavArg )
{
long double ldCosWavRet = 0;
long double ldOlTemp = 0;

   try {
      if( Absolute( ldCosWavArg ) <= GetEpsilon() )
         ldCosWavArg = 0;
      ldOlTemp = ldParam1 * ldCosWavArg; // p1 * x
      ldCosWavRet = Exponential( ldOlTemp ); // e^(p1 * x)
      ldOlTemp = ldParam2 * ldCosWavArg + ldParam3; // p2*x + p3
      ldOlTemp = cosl( ldOlTemp ); // cos(p2*x + p3)
      ldCosWavRet *= ldOlTemp; // e^(p1 * x)(cos(p2*x + p3))
      if( Absolute( ldCosWavRet ) <= GetEpsilon() )
         ldCosWavRet = 0;
   } CARLTON_FISK;                 
   if(Fct1NumericalError->sHandled)
      ldCosWavRet = Fct1NumericalError->GetGoodVal();

   return ldCosWavRet;
} // end CosWave

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : CosWave
// Purpose  : Implements the Cosine2 Wave function:
//          :   f(x) = p1 * e^-(p2 * x)cos(p3*x)
//          : 
// Notes    : This will be considered equation number 31. We assume upon
//          : execution that the user has properly set the parameters.
//****************************************************************************
long double OnevariablE::CosWave2( long double ldCosWavArg )
{
long double ldCosWavRet = 0;
long double ldOlTemp = 0;

   try {
      if( Absolute( ldCosWavArg ) <= GetEpsilon() )
         ldCosWavArg = 0;
      ldOlTemp = ldParam2 * ldCosWavArg; // p2 * x
      ldCosWavRet = Exponential( ldOlTemp ); // e^(p2 * x)
      if( Absolute( ldCosWavRet ) <= GetEpsilon() )
         return 1e9;
      ldCosWavRet = 1 / ldCosWavRet; // e^-(p2 * x)
      ldCosWavRet *= ldParam1; // p1 * e^-(p2 * x)
      ldOlTemp = ldParam3 * ldCosWavArg; // p3*x 
      ldOlTemp = cosl( ldOlTemp ); // cos(p3*x)
      ldCosWavRet *= ldOlTemp; // p1 * e^-(p2 * x)cos(p3*x)
      if( Absolute( ldCosWavRet ) <= GetEpsilon() )
         ldCosWavRet = 0;
   } CARLTON_FISK;                 
   if(Fct1NumericalError->sHandled)
      ldCosWavRet = Fct1NumericalError->GetGoodVal();

   return ldCosWavRet;
} // end CosWave2

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : CosCos
// Purpose  : Implements the Cosine2 Wave function:
//          :   f(x) = p1 cos(p2 * x)cos(p3 * x)
//          : 
// Notes    : This will be considered equation number 32. We assume upon
//          : execution that the user has properly set the parameters.
//****************************************************************************
long double OnevariablE::CosCos( long double ldCosWavArg )
{
long double ldCosRet = 0;
long double ldOlTemp = 0;

   try {
      if( Absolute( ldCosWavArg ) <= GetEpsilon() )
         ldCosWavArg = 0;
      ldOlTemp = ldParam2 * ldCosWavArg; // p2 * x
      ldCosRet = cosl( ldOlTemp ); // cos(p2 * x)
      ldCosRet *= ldParam1;        // p1 * cos(p2 * x)
      ldOlTemp = ldParam3 * ldCosWavArg; // p3 * x 
      ldOlTemp = cosl( ldOlTemp ); // cos(p3 * x)
      ldCosRet *= ldOlTemp; // p1 cos(p2 * x)cos(p3 * x)
      if( Absolute( ldCosRet ) <= GetEpsilon() )
         ldCosRet = 0;
   } CARLTON_FISK;                 
   if(Fct1NumericalError->sHandled)
      ldCosRet = Fct1NumericalError->GetGoodVal();

   return ldCosRet;
} // end CosCos

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Tangent
// Purpose  : Implements the Tangent function:
//          :   f(x) = p1 tan(p2 * x + p3)
//          : 
// Notes    : This will be considered equation number 33. We assume upon
//          : execution that the user has properly set the parameters.
//****************************************************************************
long double OnevariablE::Tangent( long double ldTanArg )
{
long double ldTanRet = 0;

   try {
      if( Absolute( ldTanArg ) <= GetEpsilon() )
         ldTanArg = 0;
      ldTanRet = ldParam2 * ldTanArg; // p2 * x
      ldTanRet += ldParam3;           // p2 * x + p3
      ldTanRet = tanl( ldTanRet );    // tan(p2 * x + p3)
      ldTanRet *= ldParam1;           // p1 * tan(p2 * x + p3)
      if( Absolute( ldTanRet ) <= GetEpsilon() )
         ldTanRet = 0;
   } CARLTON_FISK;                 
   if(Fct1NumericalError->sHandled)
      ldTanRet = Fct1NumericalError->GetGoodVal();

   return ldTanRet;
} // end Tangent

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Ploynomial
// Purpose  : Implements the Polynomial function:
//          :   f(x) = (c1 - x)(c2 - x)(c3 - x)....
//          : 
// Notes    : This will be considered equation number 34. We assume upon
//          : execution that the user has properly set the parameters.
//****************************************************************************
long double OnevariablE::Polynomial( long double ldPolyArg )
{
   long double ldTheResult = 1;

   if( Absolute( ldPolyArg ) <= GetEpsilon() )
   {
      ldPolyArg = 0;
   } // end if zero

   for( int i=0; i<vCoefficients->cnRows; i++ )
   {
      ldTheResult *= vCoefficients->pVariables[ i ] - ldPolyArg;
   } // end for loop

   return ldTheResult;
} // end Ploynomial

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : NeuralFunction
// Purpose  : Implements the Neural Network function whose form can be arbitary
//          : 
// Notes    : This will be considered equation number 35. We assume upon
//          : execution that the user has properly set the parameters.
//****************************************************************************
/*
long double OnevariablE::NeuralFunction( long double ldNeuroArg )
{
   NeuralparallelfunctioN* theFunc = (NeuralparallelfunctioN*)_theNeuroFunction;
   theFunc->SetVariable( ldNeuroArg );
   long double theRes = theFunc->EvaluateIt() - ldParam1;

   return theRes;
} // end NeuralFunction
*/

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : FractionalBrownian
// Purpose  : Implements the Fractional Brownian function:
//          : 
//          : w(t) = c1 * b^-H sin(b*t+d1) + c2 * b^-2H sin(b^2*t+d2) + ...
//          :                                             
// Notes    : This will be considered equation number 36. We assume upon
//          : execution that the user has properly set the parameters.
//          : ldParam1 holds the b param, while ldParam2 holds H.
//****************************************************************************
long double OnevariablE::FractionalBrownian( long double ldBrownArg )
{
   long double theRes = 0;
   long double ldTemp = 0;

   for( int i=0; i<vGaussian->cnRows; i++ )
   {
      ldTemp = DecimalPower( ldParam1, (i+1) ); // b^i
      ldTemp *= ldBrownArg + vUniform->pVariables[ i ]; // b^i * t + di
      ldTemp = sinl( ldTemp ); // sin( b^i * t + di )
      ldTemp *= DecimalPower( ldParam1, (ldParam2 * -1 * (i+1)) ); // b^-iH * sin( b^i * t + di )
      theRes += vGaussian->pVariables[ i ] * ldTemp; // ci * b^-iH * sin( b^i * t + di )
   } // end for loop

   return theRes;
} // end FractionalBrownian

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : FractionalBrownian
// Purpose  : Implements the ChordFunction function:
//          : D: 587.329535834815 ChordFunction
//          : A: 880
//          : C#: 1108.73052390749
//          : F#: 1479.97769084654
//          : f(x) = sin( 1/D x ) + sin( 1/A x ) + sin( 1/c# x ) + sin( 1/f# x )
//          :
//          :
// Notes    : This will be considered equation number 37. We assume upon
//          : execution that the user has properly set the parameters.
//          : vCoefficients will hold the coefficients.
//****************************************************************************
long double OnevariablE::ChordFunction( long double ldArg )
{
   long double theRes = 0;

   for( int i=0; i<vCoefficients->cnRows; i++ )
   {
      //theRes += sinl( vCoefficients->pVariables[i] * ldArg );
      theRes += cosl( vCoefficients->pVariables[i] * ldArg );
   } // end for loop

   return theRes;
} // end ChordFunction

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : FractionalBrownian
// Purpose  : Implements the Derivative of ChordFunction function:
//          : D: 587.329535834815 ChordFunction
//          : A: 880
//          : C#: 1108.73052390749
//          : F#: 1479.97769084654
//          : f(x) = sin( 1/D x ) + sin( 1/A x ) + sin( 1/c# x ) + sin( 1/f# x )
//          :
//          :
// Notes    : This will be considered equation number 38. We assume upon
//          : execution that the user has properly set the parameters.
//          : vCoefficients will hold the coefficients.
//****************************************************************************
long double OnevariablE::DxChordFunction( long double ldArg )
{
   long double theRes = 0;

   for( int i=0; i<vCoefficients->cnRows; i++ )
   {
      //theRes += (vCoefficients->pVariables[i] * cosl( vCoefficients->pVariables[i] * ldArg ));
      theRes += (vCoefficients->pVariables[i] * -1 * sinl( vCoefficients->pVariables[i] * ldArg ));
   } // end for loop

   return theRes;
} // end DxChordFunction

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Sigmoid1
// Purpose  : Implements the Sigmoid1 function:
//          : f(x) = sqrt( y/2 )*(p3-p1) + p1
// Notes    : This will be a worker function for the more general
//          : Sigmoid function
//****************************************************************************
long double OnevariablE::InverseSigmoid1( long double ldSig1Arg )
{
long double ldRet = 0;
long double ldTemp = 0;                       

   ldTemp = ldParam3 - ldParam1; // (P3 - P1)
   ldRet = sqrtl( ldSig1Arg / 2 );  // sqrt( y/2 )
   ldRet *= ldTemp;        // sqrt( y/2 ) * (P3 - P1)
   ldRet += ldParam1;         // sqrt( y/2 ) * (P3 - P1) + P1

   return ldRet;
} // end Sigmoid1

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Sigmoid2
// Purpose  : Implements the Sigmoid2 function:
//          : f(x) = sqrt( (1-y)/2)*-1*(p3-p1) + p3
// Notes    : This will be a worker function for the more general
//          : Sigmoid function
//****************************************************************************
long double OnevariablE::InverseSigmoid2( long double ldSig2Arg )
{
long double ldRet = 0;
long double ldTemp = 0;

   ldTemp = (1 - ldSig2Arg) / 2;
   if( ldTemp < 0 )
   {
      ldTemp = 0;
   }
   ldRet = sqrtl( ldTemp );                   // sqrt( (1-y)/2 )
   ldTemp = (-1) * (ldParam3 - ldParam1); // -(P3 - P1)
   ldRet *= ldTemp;                // sqrt( (1-y)/2 ) * -(P3 - P1)
   ldRet += ldParam3;            // sqrt( (1-y)/2 ) * -(P3 - P1) + P3

   return ldRet;
} // end Sigmoid2

//****************************************************************************
// Class    : OnevariablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Sigmoid
// Purpose  : Implements the InverseSigmoid function:
//          : If ( x < 0.5 )
//          :     f(x) = Sigmoid1(x)
//          : else if ( x >= 0.5 )
//          :     f(x) = Sigmoid1(x)
// Notes    : This will be considered equation number 39 We assume upon
//          : execution that the user has properly set the parameters.
//****************************************************************************
long double OnevariablE::InverseSigmoid( long double ldSigArg )
{
long double ldRet = 0;

   if( ldSigArg <= 0.5 )
      ldRet = InverseSigmoid1( ldSigArg );
   else if( ldSigArg > 0.5 )
      ldRet = InverseSigmoid2( ldSigArg );

   return ldRet;
} // end Sigmoid


