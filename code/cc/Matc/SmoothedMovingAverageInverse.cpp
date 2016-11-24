//***********************************************************************************************
// File		: SmoothedMovingAverageInverse.cpp
// Purpose	: 
//				: 
// Author	: Brandon Benham 
// Date		: 6/04/00
//***********************************************************************************************

#include"SmoothedMovingAverageInverse.hpp"          

//***********************************************************************************************
// Class	   : SmoothedMovingAverageInversE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Default Constructor
// Purpose	: Performs the basic construction actions.
//***********************************************************************************************
SmoothedMovingAverageInversE::SmoothedMovingAverageInversE( long double ldSMA )
{
	Setup();
   ldSMA_Factor = ldSMA;
   theFunction  = new TwovariablE( _UNDERFLOW );
   theFunction->SetEquation( 4 );
   theFunction->SetParams( ldSMA_Factor );
} // end SmoothedMovingAverageInversE default constructor

//***********************************************************************************************
// Class	   : SmoothedMovingAverageInversE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Destructor
// Purpose	: Performs the destruction actions.
//***********************************************************************************************
SmoothedMovingAverageInversE::~SmoothedMovingAverageInversE()
{
	if( theFunction != 0 ) 
   	delete theFunction;
} // end SmoothedMovingAverageInversE destructor

//***********************************************************************************************
// Class	   : SmoothedMovingAverageInversE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Setup
// Purpose	: Performs the basic setup actions.
//***********************************************************************************************
void SmoothedMovingAverageInversE::Setup()
{
	theFunction  = 0;
   ldSMA_Factor = 0;
   ldPrevious   = 0;
} // end Setup

//***********************************************************************************************
// Class	   : SmoothedMovingAverageInversE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: GetInverse
// Purpose	: 
//***********************************************************************************************
long double SmoothedMovingAverageInversE::GetInverse( long double ldX )
{
	theFunction->SetVariable( ldX );
	theFunction->SetVariable( ldPrevious, 1 );
   ldPrevious = ldX;
   
   return theFunction->EvaluateIt();
} // end GetInverse

//***********************************************************************************************
// Class	   : SmoothedMovingAverageInversE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: GetInverse
// Purpose	: 
// Notes    : ldX1 is the current, while ldX2 is the previous
//***********************************************************************************************
long double SmoothedMovingAverageInversE::GetInverse( long double ldX1, long double ldX2 )
{
   ldPrevious = ldX2;

   return GetInverse( ldX1 );
} // end GetInverse

