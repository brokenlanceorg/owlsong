//***********************************************************************************************
// File	   : GeneticNeuralNetwork.cpp
// Purpose : 
//         : 
// Author  : Brandon Benham 
// Date	   : 4/9/05
//***********************************************************************************************

#include "GeneticNeuralNetwork.hpp"          

//***********************************************************************************************
// Class    : GeneticNeuralNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : 
//          : 
//          :    
//          :   
//          : 
//          :
// Notes    : 
//***********************************************************************************************
GeneticNeuralNetworK::GeneticNeuralNetworK( char* name, unsigned short alive )
{
   Setup();
   _theNeuralNetwork = new ModBackproP( name, alive );
} // end GeneticNeuralNetworK default constructor

//***********************************************************************************************
// Class   : GeneticNeuralNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method  : Destructor
// Purpose : Performs the destruction actions.
//***********************************************************************************************
GeneticNeuralNetworK::~GeneticNeuralNetworK()
{
   if( _theNeuralNetwork != 0 )
   {
      delete _theNeuralNetwork;
      _theNeuralNetwork = 0;
   }
} // end GeneticNeuralNetworK destructor

//***********************************************************************************************
// Class   : GeneticNeuralNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method  : Setup
// Purpose : Performs the basic setup actions.
//***********************************************************************************************
void GeneticNeuralNetworK::Setup()
{
   _theNeuralNetwork = 0;
} // end Setup

//***********************************************************************************************
// Class   : GeneticNeuralNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method  : Run
// Purpose : Here we kick off the GA and monitor the number of epochs.
//         : 
//         : 
//         : 
//         : 
//         : 
//***********************************************************************************************
int GeneticNeuralNetworK::Run()
{
   FPRINT( "GeneticNeuralNetworK::Run" );

   // Set this just before we run:
   _theNeuralNetwork->SetMature( 0 );
   DataLoadeR::GetInstance()->SetNeuralNet( _theNeuralNetwork );
   int returnValue = 0;

   // Setup the GeneticAlgorithm
   GeneticAlgorithM* theGA = new GeneticAlgorithM();
   GeneticNetIndividuaL* theIndividual = 0;
   SelectionStrategY* theStrategy = (SelectionStrategY*)new RouletteWheelSelectionStrategY();
   theGA->SetSelectionStrategy( theStrategy );
   theGA->SetMaximumFitness( 90000000 );
   theGA->SetMaxNumberOfGenerations( 50 );
   theGA->SetPopulationSize( 10 );

   // we'll optimize every period epochs:
   int period = 15000;

   for( int i=0; i<10; i++ )
   {
      theIndividual = new GeneticNetIndividuaL();
      theGA->AddIndividual( (IndividuaL*)theIndividual );
   }

   unsigned long maxEpochs = _theNeuralNetwork->GetMaximumEpoch();

   while( 1 )
   {
      unsigned long numberEpochs = _theNeuralNetwork->GetEpochs();

      FPRINT << "About to evolve";
      theIndividual = (GeneticNetIndividuaL*)theGA->Evolve();
      _theNeuralNetwork->SetLearningRate( (theIndividual->GetGenome())->pVariables[ 0 ] );
      _theNeuralNetwork->SetMomentum( (theIndividual->GetGenome())->pVariables[ 1 ] );
      FPRINT << "Finished evolving";

      // now run the network on the values:
      _theNeuralNetwork->SetMaximumEpoch( _theNeuralNetwork->GetEpochs() + period );

      FPRINT << "About to run on evolved params";
      // check too see if the network is trained
      if( _theNeuralNetwork->Run() )
      {
         FPRINT << "exiting from run";
         returnValue = 1;
         break;
      }

      // break this if out from the previous one
      // so that we will only return 1 when the 
      // network has been fully trained.
      numberEpochs = _theNeuralNetwork->GetEpochs();
      if( maxEpochs != 0 && numberEpochs >= maxEpochs )
      {
         FPRINT << "exiting from epochs";
         break;
      }
   }

   delete theGA;

   // return zero until we can find something meaningful to return
   return returnValue;
} // end Setup
