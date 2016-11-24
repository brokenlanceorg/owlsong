//**********************************************************************
// File		:  Bisecant.cpp
// Author	:  Brandon Benham
// Purpose	:  We define our Bisecant (the hybrid algorithm)
//				:	in this class. Due to the similarities between Newton 
//				:  and Bisecant, we derive this class from the NewtoN class.
//				:  
// Notes		:  This method should be used when the function is well-defined
//				:  and a closed interval containing the root is known.
//**********************************************************************
#include"bisecant.hpp"

//************************************************************************
// Class		:	BisecanT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	:  Default Constructor
// Purpose	:	Performs the usual constructing functions.
//************************************************************************
BisecanT::BisecanT() : NewtoN()
{
	Setup(0);
} // end default constructor

//************************************************************************
// Class		:	BisecanT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	:  Destructor
// Purpose	:	Performs the usual destructing functions.
//************************************************************************
BisecanT::~BisecanT()
{
	if(FsecantOutput != NULL) {
   	FsecantOutput->CloseFile();
		delete FsecantOutput;}
} // end destructor

//************************************************************************
// Class		:	BisecanT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	:  Constructor
// Purpose	:	Performs the usual constructing functions.
//************************************************************************
BisecanT::BisecanT(int iEqOne, int iEqTwo, long double ldTol)
	: NewtoN(iEqOne, iEqTwo, ldTol)
{
   Setup();
} // end constructor

//************************************************************************
// Class		:	BisecanT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	:  Constructor
// Purpose	:	Performs the usual constructing functions.
//************************************************************************
BisecanT::BisecanT(long double ldLeft, long double ldRight, int iEqOne,
	int iEqTwo, long double ldTol) : NewtoN(iEqOne, iEqTwo, ldTol)
{
	ldLeft_Endpoint = ldLeft;
	ldRight_Endpoint = ldRight;
	ldLast_Guess = ldRight;
	ldThis_Guess = ldLeft;
	Setup();
} // end constructor

//************************************************************************
// Class		:	BisecanT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	:  Setup
// Purpose	:	Performs the usual setup procedures.
// Notes		:  We pass in a boolean parameter to classify the function as
//				:  either increasing or decreasing.
//************************************************************************
void BisecanT::Setup(int iCheckIt)
{
	ldMidpoint = 0;					// (a + b) / 2
	ldThis_Guess = 0;					// p_n
	ldNext_Guess = 0;					// p_n+1
	ldThis_Eval = 0;					// f(p_n)
	ldLast_Eval = 0;					// f(p_n-1)
	FsecantOutput = NULL;
	if(iCheckIt)
	{
		Fct1TheRootEquation->SetVariable(0, ldLeft_Endpoint);
		if(Fct1TheRootEquation->EvaluateIt() <=
			Fct1TheRootEquation->GetEpsilon())
				iIncreasing = 1;
		else
			iIncreasing = 0;
	} // end if
		
} // end setup

//************************************************************************
// Class		:	BisecanT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	:  CheckEquation
// Purpose	:	We use this to determine if the function is increasing
//				:  or decreasing for use in the bisection algorithm.
//************************************************************************
void BisecanT::CheckEquation()
{
	Fct1TheRootEquation->SetVariable(0, ldLeft_Endpoint);
	if(Fct1TheRootEquation->EvaluateIt() <=
		Fct1TheRootEquation->GetEpsilon())
			iIncreasing = 1;
	else
		iIncreasing = 0;
} // end CheckEquation

//************************************************************************
// Class		:	BisecanT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	:  SetFileName
// Purpose	:	We set the output file name and construct the FilesourcE
//				:  object.
//************************************************************************
void BisecanT::SetFileName(char* pcTheFile)
{
	FsecantOutput = new FilesourcE(pcTheFile, _BYTE, 2);
	if(!FsecantOutput->OpenFileWrite())
		cout<<"Error opening file!\n";
	else {
		FsecantOutput->SetBuffer("Output using Hybrid algorithm: ");
		FsecantOutput->WriteWord();
		if(iIncreasing)
			FsecantOutput->SetBuffer(" Function is considered to be increasing. ");
		else
			FsecantOutput->SetBuffer(" Function is not considered to be increasing. ");
		FsecantOutput->WriteWord();
	} //end else      
} // end SetFileName

//************************************************************************
// Class		:	BisecanT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	:  SolveIt
// Purpose	:	This is the overidden virtual member routine which functions
//				:  as a front-end to the recusive Bisecant method.
// Notes		:  Due to functions such as parabolas, or the like, (i.e., a
//				:  polynomial taken at sufficient scale), we can only assume
//				:  that the user has in fact given us an interval which contains
//				:  the root.
//************************************************************************
long double BisecanT::SolveIt(long double ldX)
{
	if(Fct1TheRootEquation == NULL)
		return 0;
	lCurrent_Iteration = 0;
	if((iIsSolved == 1) || (iIsSolved == -1))
		lTotal_Iterations = 0;
	iIsSolved = 0;
	if(ldX != 0)
		ldThis_Guess = ldX;
	Fct1TheRootEquation->SetVariable(0, ldLast_Guess);
	ldLast_Eval = Fct1TheRootEquation->EvaluateIt();

	return BisecantIt(1);
} // end SolveIt

//************************************************************************
// Class		:	BisecanT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	:  Bisecant
// Purpose	:	Implements the actual hybrid algorithm. We compute the
//				:  root of an equation by using both the secant and bisection
//				:  algorithms.
//************************************************************************
long double BisecanT::BisecantIt(int iNeed_To_Compute)
{
int iNeed;

	ldNext_Guess = SecantMethod(iNeed_To_Compute);
	// check to see if our current guess is within the interval
	if(ldNext_Guess >= ldLeft_Endpoint && ldNext_Guess <= ldRight_Endpoint)
	{ // we can use the secant method!
		iNeed = 1;
		ldLast_Eval = ldThis_Eval;
	} // end if in interval
	else	// we must use the bisection method!
	{
		// calculate midpoint: m = (l + r) / 2
		BisectIt();
		iNeed = 0;
	} // end else of not in interval
	// update our variables:
	ldLast_Guess = ldThis_Guess;
	ldThis_Guess = ldNext_Guess;
	lCurrent_Iteration++;
	// output our results to file:
	SendDataToFile(iNeed);
	// check our stopping criteria:
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
	else
		return BisecantIt(iNeed);
} // end Bisecant

//************************************************************************
// Class		:	BisecanT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	:  SecantMethod
// Purpose	:	This memeber function performs the actual computation
//				:  of the secant method; that is, 
//				:  p_n+1 = p_n - [f(p_n)(p_n - p_n-1)]/[f(p_n) - f(p_n-1)]
//	Notes		:  We assume that the user has properly set the necessary
//				:  variables such as last_guess, this_guess, etc.
//************************************************************************
long double BisecanT::SecantMethod(int iComp)
{
long double ldTmp;
long double ldTmp2;

	if(iComp)
	{
		// set our variable:
		Fct1TheRootEquation->SetVariable(0, ldThis_Guess);
		// evaluate the function at that point:
		ldThis_Eval = Fct1TheRootEquation->EvaluateIt();
	} // end if comp
	ldTmp = ldThis_Eval - ldLast_Eval; // [f(p_n) - f(p_n-1)]
	// Check for a possible divide by zero, we could use the machine
	// epsilon here, but I decided to use the user-specified tolerance
	if(Fct1TheRootEquation->Absolute(ldTmp) <= ldTolerance)
		return ldThis_Guess;
	ldTmp2 = ldThis_Guess - ldLast_Guess;
	ldTmp2 *= ldThis_Eval; 								// [f(p_n)(p_n - p_n-1)]
	if(Fct1TheRootEquation->Absolute(ldTmp2) <= 	// check for zero
		Fct1TheRootEquation->GetEpsilon())
			return ldThis_Guess;
	ldTmp2 /= ldTmp;				//[f(p_n)(p_n - p_n-1)]/[f(p_n) - f(p_n-1)]
	if(Fct1TheRootEquation->Absolute(ldTmp2) <= 	// check for zero
		Fct1TheRootEquation->GetEpsilon())
			return ldThis_Guess;
	// otherwise, return the final result:
	return (ldThis_Guess - ldTmp2);
	
} // end SecantMethod

//************************************************************************
// Class		:	BisecanT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	:  Bisect
// Purpose	:	Performs the bisection routines.
//				:  
//				:  
//	Notes		:  
//				:  
//************************************************************************
void BisecanT::BisectIt()
{
	ldMidpoint = .5 * (ldLeft_Endpoint + ldRight_Endpoint);
	ldNext_Guess = ldMidpoint;
	Fct1TheRootEquation->SetVariable(0, ldNext_Guess);
	// need to set our variable before we overwrite it:
	ldLast_Eval = ldThis_Eval;
	ldThis_Eval = Fct1TheRootEquation->EvaluateIt();
	switch(iIncreasing)
	{
		case 0 : // Non-increasing function on interval
			if(ldThis_Eval < Fct1TheRootEquation->GetEpsilon())
				ldRight_Endpoint = ldNext_Guess;
			else
				ldLeft_Endpoint = ldNext_Guess;
		break; 

		case 1 : // Increasing function on interval
			if(ldThis_Eval < Fct1TheRootEquation->GetEpsilon())
				ldLeft_Endpoint = ldNext_Guess;
			else
				ldRight_Endpoint = ldNext_Guess;
		break;                             
	} // end switch
} // end Bisect

//************************************************************************
// Class		:	BisecanT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	:  SendDataToFile
// Purpose	:	Writes out to our FilesourcE object the data of our current
//				:  iterative results.
//************************************************************************
void BisecanT::SendDataToFile(int iUsedWhat)
{
	if(!usLog_It)
		return;
	char acTemp[64];
	if(iUsedWhat) {
		FsecantOutput->SetBuffer(" (used secant method) ");
		FsecantOutput->WriteWord(); }
	else {
		FsecantOutput->SetBuffer(" (used bisection method) ");
		FsecantOutput->WriteWord(); }
	FsecantOutput->SetBuffer(" approx: ");                           
	FsecantOutput->WriteWord();
	acTemp[0] = '\0';
	gcvt(ldThis_Guess, 18, acTemp);
	FsecantOutput->SetBuffer(acTemp);
	FsecantOutput->WriteWord();
} // end Output

//************************************************************************
// Class		:	BisecanT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	:  SendToFile
// Purpose	:	Provides an interface to owner objects.
//				:  
//************************************************************************
void BisecanT::SendToFile(char* pcData)
{
	if(FsecantOutput == NULL || pcData == NULL)
		return;
	FsecantOutput->SetBuffer(pcData);
	FsecantOutput->WriteWord();
} // end SendToFile

//************************************************************************
// Class		:	BisecanT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	:  SetInterval
// Purpose	:	Sets the interval again; used primarialy by the
//				:  InversE class.
//************************************************************************
void BisecanT::SetInterval(long double ldL, long double ldR)
{
	ldLeft_Endpoint = ldL;
	ldRight_Endpoint = ldR;
	ldLast_Guess = ldRight_Endpoint;
	ldThis_Guess = ldLeft_Endpoint;
	CheckEquation();   
} // end SetInterval

