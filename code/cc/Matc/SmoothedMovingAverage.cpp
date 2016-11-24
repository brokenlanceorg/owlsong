//***********************************************************************************************
// File		: SmoothedMovingAverage.cpp
// Purpose	: 
//				: 
// Author	: Brandon Benham 
// Date		: 6/15/00
//***********************************************************************************************

#include"SmoothedMovingAverage.hpp"          

//***********************************************************************************************
// Class	   : SmoothedMovingAveragE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Default Constructor
// Purpose	: Performs the basic construction actions.
//***********************************************************************************************
SmoothedMovingAveragE::SmoothedMovingAveragE()
{
	Setup();
} // end SmoothedMovingAveragE default constructor

//***********************************************************************************************
// Class	   : SmoothedMovingAveragE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Default Constructor
// Purpose	: Performs the basic construction actions.
//***********************************************************************************************
SmoothedMovingAveragE::SmoothedMovingAveragE( long double ldFact )
{
	Setup();
	SetSmoothingFactor( ldFact );
} // end SmoothedMovingAveragE default constructor

//***********************************************************************************************
// Class	   : SmoothedMovingAveragE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Constructor
// Purpose	: Performs the construction actions.
//***********************************************************************************************
SmoothedMovingAveragE::SmoothedMovingAveragE( long double ldFactor, long double ldPrevious )
{
	Setup();
	ldSmoothing_Factor  = 1 - ldFactor;
	ldPrevious_Average  = ldPrevious;
	Fct2_the_Function->SetParams( ldSmoothing_Factor );
} // end SmoothedMovingAveragE constructor

//***********************************************************************************************
// Class	   : SmoothedMovingAveragE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Destructor
// Purpose	: Performs the destruction actions.
//***********************************************************************************************
SmoothedMovingAveragE::~SmoothedMovingAveragE()
{
	if( Fct2_the_Function != 0 )
   	delete Fct2_the_Function;
} // end SmoothedMovingAveragE destructor

//***********************************************************************************************
// Class	   : SmoothedMovingAveragE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Setup
// Purpose	: Performs the basic setup actions.
//***********************************************************************************************
void SmoothedMovingAveragE::Setup()
{
	Fct2_the_Function   = new TwovariablE( _OVERFLOW );
	ldSmoothing_Factor  = 0;
	ldPrevious_Average  = 0;
	Fct2_the_Function->SetEquation( 3 );
} // end Setup     

//***********************************************************************************************
// Class	   : SmoothedMovingAveragE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: SetSmoothingFactor
// Purpose	: Computes and sets the smoothing factor
//***********************************************************************************************
void SmoothedMovingAveragE::SetSmoothingFactor( long double ldF )
{
	ldSmoothing_Factor = 1 - ldF; 
   Fct2_the_Function->SetParams( ldSmoothing_Factor );
} // end SetSmoothingFactor
         
//***********************************************************************************************
// Class	   : SmoothedMovingAveragE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: GetAverage
// Purpose	: 
//***********************************************************************************************
long double SmoothedMovingAveragE::GetAverage( long double ldLast )
{
	Fct2_the_Function->SetVariable( ldLast );
	Fct2_the_Function->SetVariable( ldPrevious_Average, 1 );
   ldPrevious_Average = Fct2_the_Function->EvaluateIt();

   return ldPrevious_Average;
} // end GetAverage

//***********************************************************************************************
// Class	   : SmoothedMovingAveragE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: GetAverage
// Purpose	: 
//***********************************************************************************************
long double SmoothedMovingAveragE::GetAverage( long double ldPrevious, long double ldLast )
{
	ldPrevious_Average = ldPrevious;
	Fct2_the_Function->SetVariable( ldLast );
	Fct2_the_Function->SetVariable( ldPrevious_Average, 1 );
   ldPrevious_Average = Fct2_the_Function->EvaluateIt();

   return ldPrevious_Average;
} // end GetAverage

