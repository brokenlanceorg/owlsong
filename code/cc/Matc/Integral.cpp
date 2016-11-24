//***************************************************************************** 
// File		:	Integral.cpp
// Purpose	:	Contains the implementations for the IntegraL class
// Author	:  Brandon Benham
//*****************************************************************************
#include"integral.hpp"
#define CARLTON_FISK catch(NumerR* eEe){eEe->HandleError();}
//************************************************************************
// Class		:	IntegraL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function	:  Default Constructor
// Purpose	:	This class defines an integral object used for numerical
//				:  approximations of an arbitrary single-variable function.
//************************************************************************
IntegraL::IntegraL()
{
	ulQuit_Value = 1000;
   fLength_Of_Interval = 0;
	NumericalError = new NumerR(_OVERFLOW);
} // end Integral Constructor

//************************************************************************
// Class		:	IntegraL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function	:  Default Constructor
// Purpose	:	This class defines an integral object used for numerical
//				:  approximations of an arbitrary single-variable function.
//************************************************************************
IntegraL::~IntegraL()
{
	if(RandomNumber != NULL)
		delete RandomNumber;
	if(NumericalError != NULL)
		delete NumericalError;
} // end Integral Destructor

//************************************************************************
// Class		:	IntegraL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function	:  Constructor
// Purpose	:	This class defines an integral object used for numerical
//				:  approximations of an arbitrary single-variable function.
//************************************************************************
IntegraL::IntegraL(long double (*TheFunc)(long double nX))
{
	ulQuit_Value = 1000;
   fLength_Of_Interval = 0;
	TheFunction = TheFunc;
	NumericalError = new NumerR(_OVERFLOW);
} // end Integral Constructor

//************************************************************************
// Class		:	IntegraL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function	:  SetEndPoints
// Purpose	:	Sets the enpoints of the domain of the integral
//************************************************************************
void IntegraL::SetEndPoints(float fL, float fR)
{
	fLeft_EndPoint = fL;
	fRight_EndPoint = fR; 
	fLength_Of_Interval = Absolute(fRight_EndPoint - fLeft_EndPoint);
//	if(RandomNumber != NULL)
//		delete RandomNumber;
	RandomNumber = new RandoM(fLeft_EndPoint, fRight_EndPoint);
	RandomNumber->CalcSeed();	
} // end SetEndPoints

//************************************************************************
// Class		:	IntegraL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function	:  CalcIntegral
// Purpose	:	Serves as a front end to the CalcIntegralValue funtion
//				:  together, these functions compute the integral of a given function
//************************************************************************
long double IntegraL::CalcIntegral(unsigned long ulX)
{
long double ldResult = 0;
long double ldPrimerVal = 0;

	if(ulX != 0)
		ulQuit_Value = ulX;
	ldPrimerVal = TheFunction(RandomNumber->GetRandomLngDouble());
	ldPrimerVal = Absolute(ldPrimerVal);
	NumericalError->SetGoodVal(ldPrimerVal);
	ldResult = CalcIntegralValue(ldPrimerVal);
	ldPrimerVal = fLength_Of_Interval / ulQuit_Value;
	ldResult *= ldPrimerVal;

	return ldResult;
} // end CalcIntegral

//************************************************************************
// Class		:	IntegraL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function	:  CalcIntegralValue
// Purpose	:	A function which uses the Monte-Carlo method for apporximating
//				:  an integral. We catch an overflow by observing if the
//				:  result ever turns negative; - since we're interpreting the
//				:  integral as an area, we can do this.
//************************************************************************
long double IntegraL::CalcIntegralValue(long double ldX)
{
long double ldFuncVal = 0;
unsigned long ulLoop = 0;

	ldFuncVal = ldX;
	try{
		for(ulLoop=0;ulLoop<ulQuit_Value;ulLoop++)
		{
			ldFuncVal += TheFunction(RandomNumber->GetRandomLngDouble());
			if(ldFuncVal > 1.1e4932)
				throw NumericalError;
			else
				NumericalError->SetGoodVal(ldFuncVal);
		} // end for loop
	} CARLTON_FISK;
	if(NumericalError->sHandled)
		ldFuncVal = NumericalError->GetGoodVal();

	return ldFuncVal;
} // end CalcIntegralValue


