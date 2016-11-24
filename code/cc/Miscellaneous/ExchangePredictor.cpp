//***********************************************************************************************
// File     : ExchangePredictor.cpp
// Purpose  : 
//          : 
// Author   : Brandon Benham 
// Date     : 6/14/00
//***********************************************************************************************

#include"ExchangePredictor.hpp"          

//***********************************************************************************************
// Class    : ExchangepredictoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
ExchangepredictoR::ExchangepredictoR() : StockpredictoR( new StockpointmanageR() )
{
   Setup();
} // end ExchangepredictoR default constructor

//***********************************************************************************************
// Class    : ExchangepredictoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
ExchangepredictoR::ExchangepredictoR( StockpointmanageR* theMan ) : StockpredictoR( theMan )
{
   Setup();
} // end ExchangepredictoR constructor

//***********************************************************************************************
// Class    : ExchangepredictoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
ExchangepredictoR::~ExchangepredictoR()
{
} // end ExchangepredictoR destructor

//***********************************************************************************************
// Class    : ExchangepredictoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void ExchangepredictoR::Setup()
{
} // end Setup

//***********************************************************************************************
// Class    : ExchangepredictoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Predict
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void ExchangepredictoR::Predict()
{
NeuromanceR* theSequentialMancer  = new NeuromanceR();
NeuromanceR* theParallelMancer    = new NeuromanceR();
StockpoinT*  thePoint     = 0; // contins the bestPoint
BackproP* paraNet         = 0; // This is the parallel net from bestPoint
VectoR* theSeqResult      = 0; // Holds the seq first prediction and para result
int iSize                 = 4;

   theSeqResult = GetSequentialNetworkOutput( theSequentialMancer );
   if( theSeqResult == 0 )
      return; // how should we indicate that this code was hit?
   theSequentialMancer->InvertVector( theSeqResult );
   thePoint = theManager->FindBestPoint( theSeqResult );
   paraNet = theParallelMancer->MakeNeuralNetwork( thePoint );
   theSequentialMancer->MapVector( theSeqResult );
   paraNet->Recall( theSeqResult );

   theSeqResult->pVariables[ 0 ] = ( paraNet->GetOutput() ).pVariables[ 0 ];
   theSeqResult->pVariables[ 1 ] = ( paraNet->GetOutput() ).pVariables[ 1 ];
   theSeqResult->pVariables[ 2 ] = ( paraNet->GetOutput() ).pVariables[ 2 ];
   theSeqResult->pVariables[ 3 ] = ( paraNet->GetOutput() ).pVariables[ 3 ];

   theSequentialMancer->InvertVector( theSeqResult );

   for( int i = 0; i < theSeqResult->cnRows; i++ )
   {
      theManager->AddPredicted( theSeqResult->pVariables[ i ] );
   } // end for

   if( theSeqResult != 0 )
      delete theSeqResult;
   delete theSequentialMancer;
   delete theParallelMancer;
   delete paraNet;
} // end Predict

//***********************************************************************************************
// Class    : ExchangepredictoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : GetSequentialNetworkOutput
// Purpose  : 
//***********************************************************************************************
VectoR* ExchangepredictoR::GetSequentialNetworkOutput( NeuromanceR* theMancer )
{
StockserieS* theSeries    = 0;
SequencenetworK* seqNet   = 0;
VectoR* theVec            = 0;

   theSeries = GetLastSeries();
   seqNet = theMancer->MakeNeuralNetwork( theSeries );
   if( seqNet == 0 )
      return 0;
   theVec = new VectoR( seqNet->GetNumberOfInputs() );
   VectoR* theOutput = new VectoR( iPeel_Size + 1 );
   
   if( seqNet->GetNumberOfInputs() == 1 )
   {
      theVec->pVariables[ 0 ] = 0.625;
      seqNet->Recall( theVec );
      theOutput->pVariables[ 0 ] = ( seqNet->GetOutput() ).pVariables[ 0 ];
      
      theVec->pVariables[ 0 ] = 0.75;
      seqNet->Recall( theVec );
      theOutput->pVariables[ 1 ] = ( seqNet->GetOutput() ).pVariables[ 0 ];
      
      theVec->pVariables[ 0 ] = 0.875;
      seqNet->Recall( theVec );
      theOutput->pVariables[ 2 ] = ( seqNet->GetOutput() ).pVariables[ 0 ];
      
      theVec->pVariables[ 0 ] = 1;
      seqNet->Recall( theVec );
      theOutput->pVariables[ 3 ] = ( seqNet->GetOutput() ).pVariables[ 0 ];
      
   } else              
   {
      theVec->pVariables[ 1 ] = 0;
      
      theVec->pVariables[ 0 ] = 0.625;
      seqNet->Recall( theVec );
      theOutput->pVariables[ 0 ] = ( seqNet->GetOutput() ).pVariables[ 0 ];
                       
      theVec->pVariables[ 0 ] = 0.75;
      seqNet->Recall( theVec );
      theOutput->pVariables[ 1 ] = ( seqNet->GetOutput() ).pVariables[ 0 ];
                       
      theVec->pVariables[ 0 ] = 0.875;
      seqNet->Recall( theVec );
      theOutput->pVariables[ 2 ] = ( seqNet->GetOutput() ).pVariables[ 0 ];
                       
      theVec->pVariables[ 0 ] = 1;   
      seqNet->Recall( theVec );
      theOutput->pVariables[ 3 ] = ( seqNet->GetOutput() ).pVariables[ 0 ];
                       
   } // end else
   
   delete theVec;

   return theOutput;
} // end GetSequentialNetworkOutput

