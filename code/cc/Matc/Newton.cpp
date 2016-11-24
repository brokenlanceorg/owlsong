//**********************************************************************
// File     :  Newton.cpp
// Author   :  Brandon Benham
// Purpose  :  This file contains the implementations for the NewtoN
//          :  class, which solves functions, i.e., finds the zeros
//          :  of the mathematical functions.
//          :  We also encapsulate our Bisecant (the hybrid algorithm)
//          :  in this class because it is significantly easier to
//          :  implement due to the similarities between Newton and Bisecant
// Notes    :  This method should be used when the function is well-defined
//          :  and the first derivative of the function is known.
//          :  If the derivative is unknown, we may then call the Hybrid
//          :  algorithm to find the solution.
//**********************************************************************
#include"Newton.hpp"

//************************************************************************
// Class    :  NewtoN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member   :  Default Constructor
// Purpose  :  Constructs our basic objects.
//************************************************************************
NewtoN::NewtoN()
{
   Fct1TheRootEquation = NULL;
   Fct1TheDerivative = NULL;
   ldTolerance = 2e-16;
   Setup();
} // end Default Constructor

//************************************************************************
// Class    :  NewtoN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member   :  Destructor
// Purpose  :  Cleans up the heap
//************************************************************************
NewtoN::~NewtoN()
{
   if(Fct1TheRootEquation)
      delete Fct1TheRootEquation;
   if(Fct1TheDerivative)
      delete Fct1TheDerivative;
   if(TargOutput != NULL)
      delete TargOutput;
   if(DtheDomain != NULL)
      delete DtheDomain;
} // end destructor  
                         
//************************************************************************
// Class    :  NewtoN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member   :  Constructor
// Purpose  :  We construct a function object with equation number iWhichEq
//          :  and we set our tolerance level, if we have tol == 0, then
//          :  we use utmost precision.
//************************************************************************
NewtoN::NewtoN(int iWhichEq, int iWhichDer, long double ldTol)
{
   Fct1TheRootEquation = new OnevariablE(_UNDERFLOW_);
   Fct1TheRootEquation->SetEquation(iWhichEq);
   Fct1TheRootEquation->SetParams(.5, .25, -1);
   Fct1TheDerivative = new OnevariablE(_UNDERFLOW_);
   Fct1TheDerivative->SetEquation(iWhichDer);
   if(ldTol == 0)
      ldTolerance = Fct1TheRootEquation->GetEpsilon();
   else
      ldTolerance = ldTol;
   Setup();
} // end Constructor

//************************************************************************
// Class    :  NewtoN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member   :  Setup
// Purpose  :  
//************************************************************************
void NewtoN::Setup()
{
   lMax_Iterations = 70;
   lCurrent_Iteration = 0;
   lTotal_Iterations = 0;
   lMax_Total_Iterations = 200;
   ldLast_Guess = 0;
   iIsSolved = -1;
   TargOutput = NULL;
   DtheDomain = NULL;
} // end setup

//************************************************************************
// Class    :  NewtoN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member   :  SetErrorLog
// Purpose  :  This constructs (if null) a new file output source
//          :  for our NewtoN object.
//************************************************************************
void NewtoN::SetErrorLog(char* pcErrFileName)
{
} // end SetErrorLog

//************************************************************************
// Class    :  NewtoN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member   :  SolveIt
// Purpose  :  Provides the front-end to the FindRoot function.
//************************************************************************
long double NewtoN::SolveIt(long double ldGuess)
{
   if(Fct1TheRootEquation == NULL)
      return 0;
   Fct1TheRootEquation->SetVariable( ldGuess, 0 );
   if(Fct1TheDerivative->Absolute(Fct1TheRootEquation->EvaluateIt()) 
      <= ldTolerance) {
         iIsSolved = 1; // I think this is what we want!
         return ldGuess; } //end if
   lCurrent_Iteration = 0;
   if((iIsSolved == 1) || (iIsSolved == -1))
      lTotal_Iterations = 0;
   iIsSolved = 0;
   return FindRoot(ldGuess);
} // end SolveIt

//************************************************************************
// Class    :  NewtoN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member   :  FindRoot
// Purpose  :  This is the actual implementation of Newton's method
// Notes    :  Interestingly, we can't recurse more than 72 times in the
//          :  class structure model on this 486/SX packard bell; otherwise,
//          :  we overflow the stack.
//************************************************************************
long double NewtoN::FindRoot(long double ldRoot)
{
long double ldThis_Guess;

   ldLast_Guess = ldRoot;
      lCurrent_Iteration += 1;
      SetVariable();
      ldThis_Guess = ldLast_Guess - Eval();
   if(BelowTolerance(ldThis_Guess) || IsZero(ldThis_Guess))
      return ldThis_Guess;
   else if(lTotal_Iterations > lMax_Total_Iterations) {
      iIsSolved = 1;
      return ldThis_Guess;
   } // end if
   else if(lCurrent_Iteration > lMax_Iterations) {
      lTotal_Iterations += lCurrent_Iteration;
      iIsSolved = 0;
      return ldThis_Guess; }
   else return FindRoot(ldThis_Guess);
} // end FindRoot

//************************************************************************
// Class    :  NewtoN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member   :  Eval
// Purpose  :  We perform the mathematical operation: f(x_k)/f'(x_k)
// Notes    :  It may prove to be advantageous to use a user-defined
//************************************************************************
long double NewtoN::Eval()
{
long double ldRes;
long double ldTem;

   ldTem = Fct1TheDerivative->EvaluateIt();
   // Since we're already setting Tol, let's just do this instead:
   if(Fct1TheRootEquation->Absolute(ldTem) <= ldTolerance) {
/*    cout<<"\n*****************************Caught divide by zero!\n";
      Fct1TheRootEquation->GetEpsilon()) // the deriv is zero */
         return Fct1TheRootEquation->EvaluateIt();}  // so return the func
   // otherwise, we're okay
   else
      ldRes = Fct1TheRootEquation->EvaluateIt() / ldTem;

   return ldRes;
} // end Eval

//************************************************************************
// Class    :  NewtoN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member   :  BelowTolerance
// Purpose  :  Checks to see if we've met our criteria.
// Notes    :  We're using the formula: abs(x_k+1 - x_k)<tol*abs(x_k+1)
//          :  if it is good, (equal to or below tol), we return one;
//          :  otherwise, we return a zero.
//************************************************************************
int NewtoN::BelowTolerance(long double ldThisG)
{
int iIsGood = 0;
long double ldTemp1;

   ldTemp1 = Fct1TheRootEquation->Absolute(ldThisG - ldLast_Guess);
   if(Fct1TheRootEquation->Absolute(ldTemp1) <= 
      Fct1TheRootEquation->GetEpsilon())
         ldTemp1 = 0;
   if(ldTemp1 < ldTolerance * Fct1TheRootEquation->Absolute(ldThisG)) {
      iIsSolved = 1;
      lTotal_Iterations += lCurrent_Iteration;
      iIsGood = 1; }
   
   return iIsGood;
} // end BelowTolerance

//************************************************************************
// Class    :  NewtoN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member   :  IsZero
// Purpose  :  Determines whether the value gives a zero for the function.
// Notes    :  This was the only way to implement this check, I wish there
//          :  were a more elegant way, but there isn't.
//************************************************************************
int NewtoN::IsZero(long double ldX1)
{
int iIsZ = 0;

   Fct1TheRootEquation->SetVariable( ldX1, 0 );
   if(Fct1TheDerivative->Absolute(Fct1TheRootEquation->EvaluateIt())
      <= Fct1TheRootEquation->GetEpsilon()) {
         iIsSolved = 1;
         lTotal_Iterations += lCurrent_Iteration;
         iIsZ = 1; }

   return iIsZ;
} // end IsZero

//************************************************************************
// Class    :  NewtoN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member   :  WriteToOutput
// Purpose  :  Writes the character buffer out to our data file.
//************************************************************************
void NewtoN::WriteToOutput(char* pcBuff)
{
} // end WriteToOutput

//************************************************************************
// Class    :  NewtoN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member   :  SetFileLimits
// Purpose  :  Sets our targa file size, then constructs it.
//************************************************************************
void NewtoN::SetFileLimits(int iWidth, int iHeight, char* pcFileName)
{
   if(TargOutput != NULL)
      delete TargOutput;
   if(pcFileName)
      TargOutput = new TargA(pcFileName, iWidth, iHeight);
   else
      TargOutput = new TargA("NewFrac.tga", iWidth, iHeight);
   TargOutput->Make_Header( 24 );
   iTargWidth = iWidth;
   iTargHeight = iHeight;
   //TargOutput->Change_Pallette(1, 250, 200);
   TargOutput->Change_Pallette( 10, 0, 0, 0, 768 );
} // end SetFileLimits

//************************************************************************
// Class    :  NewtoN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member   :  SetDomains
// Purpose  :  Sets up the domain to be used in the complex plane.
//************************************************************************
void NewtoN::SetDomains(long double ldX1, long double ldX2, long double ldY1,
   long double ldY2)
{
   if(DtheDomain != NULL)
      delete DtheDomain;
   DtheDomain = new DomaiN(ldX1, ldX2, ldY1, ldY2);
} // end SetDomains

//************************************************************************
// Class    :  NewtoN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member   :  FractalizeIt
// Purpose  :  Implements the Newton fractal method.
// Notes    :  The outside loop runs with the y-axis, 
//************************************************************************
void NewtoN::FractalizeIt()
{
//FPRINT( "Frac" );
int iLoopVar1;
int iLoopVar2;
long double ldTheVariableX;
long double ldTheVariableY;
long double ldTheRealVariable;

   for(iLoopVar1=0;iLoopVar1<iTargHeight;iLoopVar1++) {
      for(iLoopVar2=0;iLoopVar2<iTargWidth;iLoopVar2++)
      {
         ldTheVariableX = (long double)(iLoopVar2)/(long double)(iTargWidth);
         ldTheVariableY = (long double)(iLoopVar1)/(long double)(iTargHeight);
         ldTheVariableX *= DtheDomain->GetLengthX();
         ldTheVariableY *= DtheDomain->GetLengthY();
         ldTheVariableX += DtheDomain->GetFirstX();
         ldTheVariableY += DtheDomain->GetFirstY();
         ldTheVariableX *= ldTheVariableX; // x^2
         ldTheVariableY *= ldTheVariableY; // y^2
         ldTheRealVariable = ldTheVariableX + ldTheVariableY;
         while(GetStatus() != 1)
            ldTheRealVariable = SolveIt(ldTheRealVariable);
         TargOutput->Do_Targa_File(lTotal_Iterations);
                        //FPRINT << lTotal_Iterations;
         iIsSolved = -1;
      } // end inside loop
   } // end outside loop
} // end FractalizeIt                                 

