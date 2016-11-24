//***************************************************************************** 
// File     :  Diff.cpp
// Purpose  :  This file contains the implementations for the DifferentiaL
//          :  mathematical object.
// Notes    :  
//          :  
// Author   :  Brandon Benham
//*****************************************************************************
#include"diff.hpp"
#include<conio.h>
#define CARLTON_FISK catch(NumerR* EE) {EE->HandleError();}
//***************************************************************************** 
// Class    :  DifferentiaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  Default Constructor
// Purpose  :  Declares a DifferentiaL object to be used in numerical
//          :  computations.
// Notes    :  
//          :  
//*****************************************************************************
DifferentiaL::DifferentiaL()
{
   Setup();
} // end DifferentiaL constructor

//***************************************************************************** 
// Class    :  DifferentiaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  Destructor
// Purpose  :  Free up the memory
//*****************************************************************************
DifferentiaL::~DifferentiaL()
{
   if(DifferentialError != NULL)
      delete DifferentialError;
   if(vDependentVars != NULL)
      delete vDependentVars;
   if(vConditions != NULL)
      delete vConditions;
   if(mCoeffs != NULL)
      delete mCoeffs;
   if(Fct1TheDiffEq != NULL)
      delete Fct1TheDiffEq;
   if(Fct2TheDiffEq != NULL)
      delete Fct2TheDiffEq;
   if(Fct3TheDiffEq != NULL)
      delete Fct3TheDiffEq;
   if(DtheDomains != NULL)
      delete DtheDomains;
   if(FOutput != NULL) {
      FOutput->CloseFile();
      delete FOutput; }
} // end DifferentiaL destructor
               
//***************************************************************************** 
// Class    :  DifferentiaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  Constructor
// Purpose  :  We instantiate a differential object with the passed in
//          :  number of variables.
//*****************************************************************************
DifferentiaL::DifferentiaL(int iNum)
{
   Setup();
   iNum_Variables = iNum;
   ldStepSize = .5;
   ulDone = 0;
   vDependentVars = new VectoR(iNum_Variables); 
   vConditions = new VectoR(iNum_Variables);
   mCoeffs = new MatriX(iNum_Variables, 4);
   DifferentialError = new NumerR(_OVERFLOW);
} // end DifferentiaL constructor              
                           
//***************************************************************************** 
// Class    :  DifferentiaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  Constructor
// Purpose  :  Set up number of variables, and the two-variable function
// Notes    :  Two Variable version
//*****************************************************************************
DifferentiaL::DifferentiaL(int iNum, int iEq_Num, char* pcFileName)
{
   Setup();
   iNum_Variables = iNum;
   CreateFunctions();
   ldStepSize = .5;                               
   ulDone = 0;
   FOutput = new FilesourcE(pcFileName, _BYTE, 0);
   vDependentVars = new VectoR(iNum_Variables);
   vConditions = new VectoR(iNum_Variables);
   mCoeffs = new MatriX(iNum_Variables, 4);
   DifferentialError = new NumerR(_OVERFLOW);
   CreateHeader();
} // end DifferentiaL constructor              

//***************************************************************************** 
// Class    :  DifferentiaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  Setup
// Purpose  :  Basic Setup stuff
//*****************************************************************************
void DifferentiaL::Setup()
{
   Fct1TheDiffEq                 = NULL;
   Fct2TheDiffEq                 = NULL;
   vDependentVars                = NULL;        
   vConditions                   = NULL;
   mCoeffs                       = NULL;
   DtheDomains                   = NULL;
   DifferentialError             = NULL;
   FOutput                       = NULL;
   iNum_Variables                = 0;
   ldStepSize                    = 0;
   ldHalfStep                    = 0;     
   ldTimeVariable                = 0;  
   ulDone                        = 0;
   FOutput = new FilesourcE("DiffEq.out", _BYTE, 0);
} // end setup

//***************************************************************************** 
// Class    :  DifferentiaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  CreateFunctions
// Purpose  :  yas;dlkf
//*****************************************************************************
void DifferentiaL::CreateFunctions()
{
   switch(iNum_Variables + 1)
   {
      case 1 :
         Fct1TheDiffEq = new OnevariablE(_UNDERFLOW);
      break;
      
      case 2 :
         Fct2TheDiffEq = new TwovariablE(_UNDERFLOW);
      break;                            
      
      case 3 :
         Fct3TheDiffEq = new ThreevariablE(_UNDERFLOW);
      break;                  
      
   } //end switch
} // end CreateFunctions

//***************************************************************************** 
// Class    :  DifferentiaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  Abs
// Purpose  :  Shortcuts the Absolute
//*****************************************************************************
long double DifferentiaL::Abs(long double ldIt)
{
long double ldAbs;

   switch(iNum_Variables + 1)
   {
      case 1 :
         ldAbs = Fct1TheDiffEq->Absolute(ldIt);
      break;
      
      case 2 :
         ldAbs = Fct2TheDiffEq->Absolute(ldIt);
      break;                                 
      
      case 3 :
         ldAbs = Fct3TheDiffEq->Absolute(ldIt);
      break;                                 
      
   } // end switch
   
   return ldAbs;
} // end Abs

//***************************************************************************** 
// Class    :  DifferentiaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  Abs
// Purpose  :  Shortcuts the Absolute
//*****************************************************************************
long double DifferentiaL::Eps()
{
long double ldEps;

   switch(iNum_Variables + 1)
   {
      case 1 :
         ldEps = Fct1TheDiffEq->GetEpsilon();
      break;
      
      case 2 :
         ldEps = Fct2TheDiffEq->GetEpsilon();
      break;                                 
      
      case 3 :
         ldEps = Fct3TheDiffEq->GetEpsilon();
      break;                                 
      
   } // end switch
   
   return ldEps;
} // end Eps

//***************************************************************************** 
// Class    :  DifferentiaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  SetEquation
// Purpose  :  Sets up the Function object's equation number.
//*****************************************************************************
void DifferentiaL::SetEquation(int iWhich)
{
   switch(iNum_Variables + 1)
   {
      case 1 :
         Fct1TheDiffEq->SetEquation(iWhich);
      break;
      
      case 2 :
         Fct2TheDiffEq->SetEquation(iWhich);
      break;                       

      case 3 :
         Fct3TheDiffEq->SetEquation(iWhich);
      break;                       
      
   } // end switch
} // end SetEquation

//***************************************************************************** 
// Class    :  DifferentiaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  SetParams
// Purpose  :  Sets up the Function object's equation number.
//*****************************************************************************
void DifferentiaL::SetParams(long double ld1, long double ld2, long double ld3)
{
   switch(iNum_Variables + 1)
   {
      case 1 :
         Fct1TheDiffEq->SetParams(ld1, ld2, ld3);
      break;
      
      case 2 :
         Fct2TheDiffEq->SetParams(ld1, ld2, ld3);
      break; 

      case 3 :
         Fct3TheDiffEq->SetParams(ld1, ld2, ld3);
      break;                       
                            
   } // end switch
} // end SetParams

//***************************************************************************** 
// Class    :  DifferentiaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  SetEndPoints
// Purpose  :  To set up our endpoints
//*****************************************************************************
void DifferentiaL::SetEndPoints(long double fOne, long double fTwo, long double fThree, 
   long double fFour, long double fFive)
{
   if(DtheDomains) {
      DtheDomains->SetXInterval(fOne, fTwo);
      DtheDomains->SetYInterval(fThree, fFour); }
   else
      DtheDomains = new DomaiN(fOne, fTwo, fThree, fFour);
   if(Abs(ldStepSize) >= Eps())
      ulDone = DtheDomains->GetLengthX() / ldStepSize;
} // end SetEndPoints

//***************************************************************************** 
// Class    :  DifferentiaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  SetStepSize
// Purpose  :  To set up our h value
//*****************************************************************************
void DifferentiaL::SetStepSize(float fNewStep)
{
   ldStepSize = fNewStep;
   ldHalfStep = ldStepSize / 2;
   if(Abs(ldStepSize) >= Eps())
      ulDone = DtheDomains->GetLengthX() / ldStepSize;
} // end SetStepSize

//***************************************************************************** 
// Class    :  DifferentiaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  Solve
// Purpose  :  Solves the systems of differential equations
// Notes    :  Return non-zero on error
//*****************************************************************************
int DifferentiaL::Solve()
{
int iSolveRes = 0;
int iCount = 0;

   *vDependentVars = *vConditions;  // Our solution is correct at the begining
   ldTimeVariable = DtheDomains->GetFirstX();      // Set our starting time value
   for(iCount=0;iCount<ulDone;iCount++)
   {
      CalcCoeffs();
      Approximate();
      cout<<"Time variable: "<<ldTimeVariable<<" Approximation: "
      <<*vDependentVars;
      WriteToDisk();
      cout<<"Pres any key to continue..."<<endl;
      getch();
   } // end for loop
   if(!FOutput->CloseFile())
      cout<<"Error closing DiffEq.OUT file!"<<endl;
      
   return iSolveRes;
} // end Solve method

//***************************************************************************** 
// Class    :  DifferentiaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  CalcCoeffs
// Purpose  :  Calculates the coefficients for the Runge-Kutta method
//          :  of order four.
// Notes    :  
//          :  
//*****************************************************************************
void DifferentiaL::CalcCoeffs()
{
int iLoop = 0;
int iOuter = 0;

   for(iOuter=0;iOuter<4;iOuter++)
   {
      FillVariables(iOuter + 1);
      for(iLoop=0;iLoop<iNum_Variables;iLoop++)
      {
         if(iLoop == (iNum_Variables - 1)) 
         {
            mCoeffs->pCol[iLoop][iOuter] = ldStepSize * TheFunction();
         } else
         {
            mCoeffs->pCol[iLoop][iOuter] = ldStepSize * 
               vDependentVars->pVariables[iLoop + 1];
         } // end else
         if(Abs(mCoeffs->pCol[iLoop][iOuter]) <= Eps())
            mCoeffs->pCol[iLoop][iOuter] = 0;
      } // end for
   } // end outer for loop
} // end CalcCoeffs         

//***************************************************************************** 
// Class    :  DifferentiaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  FillVariables
// Purpose  :  Fills in the variables in the right-hand side equation
//*****************************************************************************
void DifferentiaL::FillVariables(int iWhichCoeff)
{
int iI = 1;
int iControl = 0;

   if(iNum_Variables == 1)
      iControl = 2;
   else
      iControl = iNum_Variables;

   switch(iWhichCoeff)
   {
      case 1 :
         SetVariable(0, ldTimeVariable); // set our time
         for(iI=1;iI<iControl;iI++)
         {  
            SetVariable(iI, vDependentVars->pVariables[(iI - 1)]);
         } // end for      
      break;

      case 2 :
         SetVariable(0, (ldTimeVariable + ldHalfStep)); // set our time
         for(iI=1;iI<iControl;iI++)
         {
            SetVariable(iI, vDependentVars->pVariables[(iI - 1)] + (.5 *
               mCoeffs->pCol[(iI - 1)][0]));
         } // end for      
      break;         

      case 3 :
         SetVariable(0, (ldTimeVariable + ldHalfStep)); // set our time
         for(iI=1;iI<iControl;iI++)
         {
            SetVariable(iI, vDependentVars->pVariables[(iI - 1)] + (.5 *
               mCoeffs->pCol[(iI - 1)][1]));
         } // end for      
      break;         

      case 4 :
         ldTimeVariable += ldStepSize;    // increment time for future calls
         SetVariable(0, ldTimeVariable); // set our time
         for(iI=1;iI<iControl;iI++)
         {
            SetVariable(iI, vDependentVars->pVariables[(iI - 1)] + 
               mCoeffs->pCol[(iI - 1)][2]);
         } // end for             
      break;
      default :
      break;
   } // end switch
} // end SetVariables

//***************************************************************************** 
// Class    :  DifferentiaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  SetVariable
// Purpose  :  Sets the variable indicated by the first argument
//*****************************************************************************
void DifferentiaL::SetVariable(int iWhich, long double ldValue)
{
   switch(iNum_Variables + 1)
   {
      case 0 : // Our system is one-variabled
         Fct1TheDiffEq->SetVariable(iWhich, ldValue);
      break;
      case 1 : // Our system is one-variabled
         Fct2TheDiffEq->SetVariable(iWhich, ldValue);
//       cout<<"Setting independent: "<<iWhich<<" to: "<<ldValue<<endl;
      break;
      case 2 : // Our system is two-variabled
         Fct2TheDiffEq->SetVariable(iWhich, ldValue);
      break;                       
      case 3 :
         Fct3TheDiffEq->SetVariable(iWhich, ldValue);
      break;
      case 4 :
      break;
      case 5 :
      break;
      case 6 :
      break;
   } // end 
} // end SetVariable

//***************************************************************************** 
// Class    :  DifferentiaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  Approximate
// Purpose  :  Calculates the final approximation for each dependent variable
// Notes    :  We assume that the CalcCoeffs has been properly called.
//          :  For some damn reason, if we put a matrix inside a try block,
//          :  the program decides to screw it all up after so many iterations
//          :  it took several hair-pulling hours to track this one down,
//          :  so don't do this again.
//*****************************************************************************
void DifferentiaL::Approximate()
{
int iApproxCounter = 0;
long double fAsixth = .16666666666666666;
long double ldTemp1 = 0;

   for(iApproxCounter=0;iApproxCounter<iNum_Variables;iApproxCounter++)
   {
/*    DifferentialError->SetErrorType(_OVERFLOW);
      DifferentialError->SetGoodVal(vDependentVars->pVariables[iApproxCounter]);
      try { */
         ldTemp1 = (mCoeffs->pCol[iApproxCounter][0] +
               (2 * mCoeffs->pCol[iApproxCounter][1]) +
               (2 * mCoeffs->pCol[iApproxCounter][2]) +
               mCoeffs->pCol[iApproxCounter][3]);
//       DifferentialError->SetGoodVal(ldTemp1);
         ldTemp1 *= fAsixth;
         // Check for error
/*       if(Absolute(ldTemp1) > Absolute(DifferentialError->GetGoodVal()))
            throw DifferentialError; */
         vDependentVars->pVariables[iApproxCounter] += ldTemp1;
//    } CARLTON_FISK; // end try block
/*    if(DifferentialError->sHandled)
         vDependentVars->pVariables[iApproxCounter] +=
            DifferentialError->GetGoodVal(); */
   } // end for loop
} // Approximate

//***************************************************************************** 
// Class    :  DifferentiaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  TheFunction
// Purpose  :  Returns the differential function evaluation
// Notes    :  We assume everything the function objects assume, i.e.,
//          :  all independent variables are in fact instantiated.
//*****************************************************************************
long double DifferentiaL::TheFunction()
{
long double ldResult = 0;

   switch(iNum_Variables + 1)
   {
      case 0 : // Our system is one-variabled
         ldResult = Fct1TheDiffEq->EvaluateIt();
      break;
      case 1 : // Our system is one-variabled
         ldResult = Fct2TheDiffEq->EvaluateIt();
      break;
      case 2 : // Our system is two-variabled
         ldResult = Fct2TheDiffEq->EvaluateIt();
      break;                       
      case 3 :
         ldResult = Fct3TheDiffEq->EvaluateIt();
      break;
      case 4 :
      break;
      case 5 :
      break;
      case 6 :
      break;
   } // end switch
   return ldResult;
} // end TheFunction

//***************************************************************************** 
// Class    :  DifferentiaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  CreateHeader
// Purpose  :  Outputs the header information for the output file
// Notes    :  
//*****************************************************************************
void DifferentiaL::CreateHeader()
{
   if(!FOutput->OpenFileWrite())
   {
      cout<<"Couldn't create file!"<<endl;
      return;
   } // end if
   FOutput->SetBuffer("// The following are the results of the Runge-Kutta method");
   FOutput->WriteWord();
   FOutput->SetBuffer(" The first number indicates the time variable, and the");
   FOutput->WriteWord();
   FOutput->SetBuffer(" next n numbers indicate the approximation's solution at that");
   FOutput->WriteWord();
   FOutput->SetBuffer(" time value, where n is the number of dependent variables.\n");
   FOutput->WriteWord();
} // end createheader                

//***************************************************************************** 
// Class    :  DifferentiaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  WriteToDisk
// Purpose  :  Outputs the solution data to the disk
// Notes    :  
//*****************************************************************************
void DifferentiaL::WriteToDisk()
{
char acTempBuff[65];
int iNumDigits = 18;
int iloop = 0;

   acTempBuff[0] = '\0';
   gcvt(ldTimeVariable, iNumDigits, acTempBuff);
   FOutput->SetBuffer(" Time: ");
   FOutput->WriteWord();
   FOutput->SetBuffer(acTempBuff);
   FOutput->WriteWord();
   acTempBuff[0] = '\0';
   for(iloop=0;iloop<iNum_Variables;iloop++)
   {
      gcvt(vDependentVars->pVariables[iloop], iNumDigits, acTempBuff);
      FOutput->SetBuffer(" Approximation: ");
      FOutput->WriteWord();
      FOutput->SetBuffer(acTempBuff);
      FOutput->WriteWord();
   } // end for loop
} // end WriteToDisk    

