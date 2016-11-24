//***************************************************************************** 
// File		:	Bisect.cpp                                                    
// Purpose	:	This file contains the implementations for the member
//				:  Functions of the QuadratiC class for 
//				:  Numerical Analysis
// Author	:  Brandon Benham
//*****************************************************************************
#include"bisect.hpp"

//************************************************************************
// Class		:	QuadratiC
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	:  Default Constructor
// Purpose	:	We instantiate our various concomitant objects that our
//				:	necessary for normal execution, such as matrices, vectors,
//				:  and function objects.
//************************************************************************
QuadratiC::QuadratiC()
{
	mCoeffs = new MatriX(3, 3);
	vCoeffs = new VectoR(3);
	Setup();
} // end default class constructor

//************************************************************************
// Class		:	QuadratiC
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	:  Constructor
// Purpose	:	We instantiate our various concomitant objects that our
//				:	necessary for normal execution, such as matrices, vectors,
//				:  and function objects.
//************************************************************************
QuadratiC::QuadratiC(int iR, int iC, int iWhich, long double ldT)
{
	mCoeffs = new MatriX(iR, iC);
	vCoeffs = new VectoR(iC);
	ldTau = ldT;
	Setup(iWhich);
} // end constructor

//************************************************************************
// Class		:	QuadratiC
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	:  Destructor
// Purpose	:	We destruct anything that needs to be destroyed
//************************************************************************
QuadratiC::~QuadratiC()
{
	delete mCoeffs;
	delete vCoeffs;
	delete Fct1TheRootFunction;
   if(RtheRandom != NULL)
   	delete RtheRandom;
} // end destructor
			 
//************************************************************************
// Class		:	QuadratiC
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	:  Setup
// Purpose	:	We initialize any member objects that need to be such as
//				:	our function object. The parameter tells the function
//				:  object which equation he is to be.
//************************************************************************
void QuadratiC::Setup(int iWhich)
{
	Fct1TheRootFunction = new OnevariablE(_UNDERFLOW);
	Fct1TheRootFunction->SetEquation(iWhich);
	iNumber_Of_Iterations = 0;
	lMaximum_Iterations = 0;
	iThe_Slope = 0;
   RtheRandom = NULL;
   ldPeturb_Value = 0;
} // end Setup

//************************************************************************
// Class		:	QuadratiC
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	:  Bisect
// Purpose	:	This functions as a front end to the BisectIt member function.
//				:	We have this only to fascilitate the possibility of given
//				:  the function values before the initial computation.
//************************************************************************
long double QuadratiC::Bisect(long double ldLeft, long double ldRight, 
	long double ldFleft, long double ldFright)
{
	if((ldFleft == 0) && (ldFright == 0))
	{
		iThe_Slope = CheckEndpoints(ldLeft, ldRight);
		if(iThe_Slope == 0)
		{
			cout<<"This interval contains no roots!\n";
			return 0;
		} // end if slope == 0
		return BisectIt(ldLeft, ldRight);
	} // end if
	
} // end Bisect

//************************************************************************
// Class		:	QuadratiC
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	:  BisectIt
// Purpose	:	This function actually computes the root of the function
//				:	object by the bisection algorithm.
//				:  
//************************************************************************
long double QuadratiC::BisectIt(long double ldLeft, long double ldRight)
{
long double ldBisResult = 0;
long double ldEval;
long lLoop = 0;
char acBuffer[64];
FilesourcE* FtempFile = new FilesourcE("c:\\output.bis", _BYTE, 2);

	lMaximum_Iterations = CalcIterations(ldLeft, ldRight);
	acBuffer[0] = '\0';
	if(!FtempFile->OpenFileWrite())
		cout<<"Error opening output file!\n";
	else
	{
		FtempFile->SetBuffer("Output, Number of Iterations:");
		FtempFile->WriteWord();
		ltoa(lMaximum_Iterations, acBuffer, 10);
		FtempFile->SetBuffer(acBuffer);
		FtempFile->WriteWord();
	} // end else                              
	acBuffer[0] = '\0';
	for(lLoop=0;lLoop<lMaximum_Iterations;lLoop++)
	{
		FtempFile->SetBuffer(" approx: ");
		FtempFile->WriteWord();
		ldBisResult = (ldLeft + ldRight) / 2;
		Fct1TheRootFunction->SetVariable(0, ldBisResult);
		ldEval = Fct1TheRootFunction->EvaluateIt();
		if(iThe_Slope >= 1)
		{
			if(ldEval > Fct1TheRootFunction->GetEpsilon())
				ldRight = ldBisResult;
			else
				ldLeft = ldBisResult;
		} else // end if positive slope
		{
			if(ldEval < Fct1TheRootFunction->GetEpsilon())
				ldRight = ldBisResult;
			else
				ldLeft = ldBisResult;
		} // end else 
		gcvt(ldBisResult, 20, acBuffer);
		FtempFile->SetBuffer(acBuffer);
		FtempFile->WriteWord();
	} // end for loop
	FtempFile->CloseFile();
	delete FtempFile;

	return ldBisResult;
} // end BisectIt

//************************************************************************
// Class		:	QuadratiC
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	:  CalcIterations
// Purpose	:	We base how many loops we must go through by first specifying
//				:	how much relative error we want, (the most accurate way to
//				:  specify this), and then we compute how many iterations needed
//				:  based on the endpoints.
// Notes		:  Since we want to be accurate within some specified tolerance,
//				:  we can use this fact in determining the number of iterations
//				:  needed to meet this criterion - tau = abs(r - l) / 2^iters
//************************************************************************
long QuadratiC::CalcIterations(long double ldL, long double ldR)
{
long double ldTemp1 = 0;
long lIterations = 0;

	ldTemp1 = (Fct1TheRootFunction->Absolute(ldR) - 
		Fct1TheRootFunction->Absolute(ldL));
   ldTemp1 /= ldTau;
	ldTemp1 = logl(ldTemp1);
	ldTemp1 /= logl(2);
	ldTemp1 += 0.5;
	lIterations = (int)(ldTemp1);

	return lIterations;
} // end Calc_Iters

//************************************************************************
// Class		:	QuadratiC
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	:  CheckEndpoints
// Purpose	:	Checks the endpoints of our interval to make sure they
//				:	do in fact contain a root.
//	Notes		:  A zero indicates there is no root, a negative that the
//				:  slope is up to the left, and a positive that the slope
//				:  is up to the right.
//************************************************************************
int QuadratiC::CheckEndpoints(long double ldL, long double ldR)
{
int iRet = -1;
long double ldTemp = 0;

	Fct1TheRootFunction->SetVariable(0, ldL);
	ldTemp = Fct1TheRootFunction->EvaluateIt(); // sample function at left
	if(ldTemp <= Fct1TheRootFunction->GetEpsilon())
	{ // our function should have positive slope
		Fct1TheRootFunction->SetVariable(0, ldR);
		ldTemp = Fct1TheRootFunction->EvaluateIt();
		iRet = 1;
		if(ldTemp <= Fct1TheRootFunction->GetEpsilon())
			iRet = 0; // no roots in interval
	} else // end if
	{ // our function should have negative slope
		Fct1TheRootFunction->SetVariable(0, ldR);
		ldTemp = Fct1TheRootFunction->EvaluateIt();
		if(ldTemp >= Fct1TheRootFunction->GetEpsilon())
			iRet = 0; // no roots in interval
	} // end else

	return iRet;
} // end CheckEndpoints

//************************************************************************
// Class		:	QuadratiC
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	:  SetRandomInterval
// Purpose	:	We construct our random interval object from
//				:	which we will peturbate
//				:  
//************************************************************************
void QuadratiC::SetRandomInterval(float fL, float fR)
{
	RtheRandom = new RandoM(fL, fR);
   RtheRandom->CalcSeed();
   ldPeturb_Value = 2 * RtheRandom->GetRandomLngDouble();
   ldPeturb_Value = 1 - ldPeturb_Value;
   ldPeturb_Value *= 10e-5;
} // end SetRandomInterval

//************************************************************************
// Class		:	QuadratiC
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	:  PeturbBisect
// Purpose	:	This is the interface into the Peturbation algorithm
//	Notes		:	
//				:  
//************************************************************************
long double QuadratiC::PeturbBisect()
{
	return Bisect(ldLeft_Endpoint, ldRight_Endpoint);
} // end PeturbBisect

//************************************************************************
// Class		:	QuadratiC
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	:  SetInterval
// Purpose	:	We just set our interval for the bisection method.
// Notes		:  Normally, we want to set our random interval before this
//				:  member call.
//************************************************************************
void QuadratiC::SetInterval(long double ldL, long double ldR)
{
	ldLeft_Endpoint = ldL;
   ldRight_Endpoint = ldR;
   if(ldPeturb_Value != 0) {
   	ldLeft_Endpoint += ldPeturb_Value;
   	ldRight_Endpoint += ldPeturb_Value;
   } // end if we have set our peturb_value
} // end SetInterval

//************************************************************************
// Class		:	QuadratiC
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	:  SetPolyCoeffs
// Purpose	:	This Sets the function object, if we're using the 
//				:	polynomial function method.
//				:  
//************************************************************************
void QuadratiC::SetPolyCoeffs(VectoR* vTheCoeffs)
{
	Fct1TheRootFunction->SetPolynomialCoeffs(vTheCoeffs);
	Fct1TheRootFunction->SetEquation(23);
} // end SetPolyCoeff
                                          
//************************************************************************
// Class		:	QuadratiC
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	:  SetPolyCoeffs
// Purpose	:	
//				:	
//				:  
//************************************************************************
void QuadratiC::SetPolyCoeffs(VectoR* vTheCoeffs1, VectoR* vTheCoeffs2)
{
	Fct1TheRootFunction->SetPolynomialRoots(vTheCoeffs1, vTheCoeffs2);
	Fct1TheRootFunction->SetEquation(23);
} // end SetPolyCoeff                   

//************************************************************************
// Class		:	QuadratiC
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	:  CalcPeturbValue
// Purpose	:	Call this function before peturbating the endpoints
//************************************************************************
void QuadratiC::CalcPeturbValue()
{
   ldPeturb_Value = 2 * RtheRandom->GetRandomLngDouble();
   ldPeturb_Value = 1 - ldPeturb_Value;
   ldPeturb_Value *= 10e-5;
} // end SetRandomInterval

