//***********************************************************************************************
// File     : StockPredictor.cpp
// Purpose  : 
//          : 
// Author   : Brandon Benham 
// Date     : 7/14/00
//***********************************************************************************************

#include"StockPredictor.hpp"          

//***********************************************************************************************
// Class    : StockpredictoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
StockpredictoR::StockpredictoR( StockpointmanageR* theMan, int iPeel )
{
   Setup();
   theManager = theMan;
   iPeel_Size = iPeel;
} // end StockpredictoR default constructor

//***********************************************************************************************
// Class    : StockpredictoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
StockpredictoR::~StockpredictoR()
{
// if( fThe_Output_File != 0 )
//        delete fThe_Output_File;
    if( theManager != 0 )
        delete theManager;
} // end StockpredictoR destructor

//***********************************************************************************************
// Class    : StockpredictoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void StockpredictoR::Setup()
{
   iExtrapolate_Size = 1;
   iPeel_Size        = 3;
   fThe_Output_File  = 0;
   theManager        = 0;
} // end Setup

//***********************************************************************************************
// Class    : StockpredictoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : GetLastPredictions
// Purpose  : 
//***********************************************************************************************
StockserieS* StockpredictoR::GetLastSeries()
{
StockserieS* theSeries = 0;

   if( theManager->IsActualLast() )
      theSeries = theManager->GetLastActual();
   else
      theSeries = (theManager->GetLastPoint())->GetPredictions();

   return theSeries;
} // end GetLastPredictions

