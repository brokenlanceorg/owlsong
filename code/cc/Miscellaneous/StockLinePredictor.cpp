//***********************************************************************************************
// File     : StockLinePredictor.cpp
// Purpose  : 
//          : 
// Author   : Brandon Benham 
// Date  : 7/14/00
//***********************************************************************************************

#include"StockLinePredictor.hpp"          

//***********************************************************************************************
// Class    : StocklinepredictoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
StocklinepredictoR::StocklinepredictoR( int iNum, int iPeel ) : 
    StockpredictoR( new StockpointmanageR( "StockPredictions.dat", "StockActuals.dat" ), iPeel )
{
   Setup();
   iPeel_Size = iPeel;
   iNumber_Of_Points_To_Predict = iNum;
} // end StocklinepredictoR default constructor

//***********************************************************************************************
// Class : StocklinepredictoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
StocklinepredictoR::StocklinepredictoR( StockpointmanageR* theMan, int iPeel ) : StockpredictoR( theMan, iPeel )
{
   Setup();
   iPeel_Size = iPeel;
} // end StocklinepredictoR default constructor

//***********************************************************************************************
// Class    : StocklinepredictoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
StocklinepredictoR::~StocklinepredictoR()
{
} // end StocklinepredictoR destructor

//***********************************************************************************************
// Class    : StocklinepredictoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void StocklinepredictoR::Setup()
{
   iNumber_Of_Points_To_Predict = 1;
   iPeel_Size                   = 3;
   NASDAQPredictor              = 0;
} // end Setup

//***********************************************************************************************
// Class    : StocklinepredictoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : GetDifferences
// Purpose  : Returns the differences as vector that will be then added to the prediction.
// Notes    : How do we map the difference vector into [0, 1], and then invert back?
//          : We could write a new MapXXX method in NeuromanceR that basically calls
//          : GetMappedSeries that mapps into [0, 1], but keeps track of signs
//          : so that InvertXXX can incorporate this info....
//          : 
//***********************************************************************************************
VectoR* StocklinepredictoR::GetDifferences()
{
VectoR*        theVec    = NASDAQPredictor->GetLastDifferences(); // this needs to be deleted.
VectoR*        theRetVec = new VectoR( theVec->cnRows );
VectoR*        inputVec  = 0;
NeuralparallelfunctioN* theFunc   = 0;
NeuromanceR*   theMancer = new NeuromanceR(); // this version maps into [0, 1]

   theMancer->SetCeilingAndFloor(); // this will map vecs into [0, 1]

   inputVec = theVec->GetAbsoluteVector(); // this will be easier if VectoR has access to FunctioN

   StockserieS* theSeries = new StockserieS( 0.1 );
   theSeries->SetTransient();
   for( int i = 0; i < theVec->cnRows; i++ )
      theSeries->AddElement( theVec->pVariables[ i ] );
   StockserieS* theRetSeries = theMancer->GetMappedSeries( theSeries );

   theMancer->MapVector( inputVec );

   if( NASDAQPredictor->IsLastIncreasing() ) // this is defined only for StockpointmanageR
   {
      theFunc = new NeuralparallelfunctioN( "Increasing.def" );
   } else
   {
      theFunc = new NeuralparallelfunctioN( "Decreasing.def" );
   } // end else

   for( int i = 0; i < theRetVec->cnRows; i++ )
   {
      theFunc->SetVariable( inputVec->pVariables[ i ] );
      theRetVec->pVariables[ i ] = theFunc->EvaluateIt();
   } // end for loop

   theMancer->InvertVector( theRetVec ); 

   // must account for sign:
   for( int i = 0; i < theRetVec->cnRows; i++ )
   {
      if( theVec->pVariables[ i ] < 0 )
         theRetVec->pVariables[ i ] *= -1;
   } // end for

   delete theMancer;
   delete inputVec;
   delete theFunc;
   delete theSeries;
   delete theRetSeries;
   delete theVec;

   return theRetVec;
} // end GetDifferences

//***********************************************************************************************
// Class    : StocklinepredictoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Predict
// Purpose  : 
// Notes    : The for loop should be controlled by what attribute? At first, this may
//          : need to be a separate member set to 1, because, we want to see how far
//          : off the mark we are to correctly calibrate the NeurofunctioN object.
//***********************************************************************************************
void StocklinepredictoR::Predict()
{
SmoothedMovingAverageInversE* SMAInverser = new SmoothedMovingAverageInversE( 0.1 ); // this may be default
NeuromanceR* theMancer  = new NeuromanceR();
long double  ldThePoint = 0;
// Get the very last average:
//long double  ldPrevious = theManager->GetLastPoint()->GetPredictions()->GetAverages()->pVariables[ 3 ];
// This may be the better way:
long double  ldPrevious = ((theManager->GetLastSeries())->GetAverages())->pVariables[ 3 ];
//VectoR*      vDiffs     = GetDifferences(); // call, but don't use right now...
ofstream statusFile( "Status.dat", ios::app );

   statusFile << "Created Prediction objects, will now begin prediction loop\n";

   for( int i = 0; i < iNumber_Of_Points_To_Predict; i++ )
   {
      statusFile << "Beginning loop number: " << i << "\n";
      
      ldThePoint = GetSequentialNetworkOutput( theMancer );

      statusFile << "Received sequential network output, value: " << ldThePoint << "\n";

      ldThePoint = theMancer->InvertPoint( ldThePoint );
      
      statusFile << "Inverted value via NeuromanceR::InvertPoint: " << ldThePoint << "\n";

      // Invert the Smoothed Moving Average:
      if( i == 0 )
         ldThePoint = SMAInverser->GetInverse( ldThePoint, ldPrevious );
      else
         ldThePoint = SMAInverser->GetInverse( ldThePoint );

      statusFile << "Inverted value via SMA::GetInverse: " << ldThePoint << "\n";
      
      // Adding of the difference (vDiffs) will go here.....
      // Do we want to store the before *and* after values?
      // Or do we simply store the final result?
      // And we may want to save each value at each step.

      theManager->AddPredicted( ldThePoint ); // when we add, we may not be doing everything necessary...
   } // end for loop

   statusFile << flush;
   statusFile.close();

   delete theMancer;
   delete SMAInverser;
} // end Predict

//***********************************************************************************************
// Class    : StocklinepredictoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : GetSequentialNetworkOutput
// Purpose  : 
//***********************************************************************************************
long double StocklinepredictoR::GetSequentialNetworkOutput( NeuromanceR* theMancer )
{
SequencenetworK* theSeqNet = 0;
VectoR* theSMA             = 0;
VectoR* theSeqInput        = 0;
long double ldRet          = 0;

   theSMA      = (GetLastSeries())->GetAverages();
   theSeqNet   = theMancer->MakeNeuralNetwork( theSMA );
   if( theSeqNet == 0 )
      return ldRet;
   theSeqInput = new VectoR( theSeqNet->GetNumberOfInputs() ); // make sure this is defined properly!!!

   if( theSeqInput->cnRows == 2 )
   {
      theSeqInput->pVariables[ 0 ] = 1;
      theSeqInput->pVariables[ 1 ] = 0;
   }
   else
      theSeqInput->pVariables[ 0 ] = 1;

   theSeqNet->Recall( theSeqInput );
   ldRet = (theSeqNet->GetOutput()).pVariables[ 0 ];

   delete theSeqNet;
   delete theSeqInput;

   return ldRet;
} // end GetSequentialNetworkOutput
