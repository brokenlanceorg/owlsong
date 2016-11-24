//***********************************************************************************************
// File    : GeneticNetIndividual.cpp
// Purpose : 
//         : 
// Author  : Brandon Benham 
// Date    : 4/9/05
//***********************************************************************************************

#include "GeneticNetIndividual.hpp"          

//***********************************************************************************************
// Class    : GeneticNetIndividuaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//          : The default constructor has the responsibility to do the following:
//          :    (1) Create the necessary chromosome class
//          :    (2) Give the chromosome a reasonable random genome
//          :    (3) Set the proper mutation strategy on the chromosome
//          :    (4) Set the crossover strategy
// Notes    : 
//***********************************************************************************************
GeneticNetIndividuaL::GeneticNetIndividuaL() : IndividuaL()
{
   Setup();
   // (1):
   SetChromosome( (ChromosomE*)(new ValueEncodedChromosomE()) );

   // (2):
   // The chromosome will be 2 terms:
   //   the learning rate which can range from very small to 10 or higher, 
   //   and the momentum which can vary from 0.1 to unknown.
   VectoR* theRandomGenome = new VectoR( 2 );

   // We will just put the random numbers in [0, 1]:
   //theRandomGenome->Fill_Rand();
   theRandomGenome->pVariables[ 0 ] = 0.04;
   theRandomGenome->pVariables[ 1 ] = 0.4;

   GetChromosome()->SetGenome( theRandomGenome );

   // (3):
   ValueEncodedMutationStrategY* strat = new ValueEncodedMutationStrategY();
   strat->SetMutationProbability( 0.5 );
   GetChromosome()->SetMutationStrategy( strat );

   // (4):
   CrossoverStrategY* theCrossover = (CrossoverStrategY*)new SinglePointCrossoverStrategY();
   SetCrossoverStrategy( theCrossover );
} // end GeneticNetIndividuaL default constructor

//***********************************************************************************************
// Class   : GeneticNetIndividuaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method  : Destructor
// Purpose : Performs the destruction actions.
//***********************************************************************************************
GeneticNetIndividuaL::~GeneticNetIndividuaL()
{
} // end GeneticNetIndividuaL destructor

//***********************************************************************************************
// Class   : GeneticNetIndividuaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method  : Setup
// Purpose : Performs the basic setup actions.
//***********************************************************************************************
void GeneticNetIndividuaL::Setup()
{
   _myFitness = -1;
} // end Setup

//***********************************************************************************************
// Class   : GeneticNetIndividuaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method  : Clone
// Purpose :
//***********************************************************************************************
IndividuaL* GeneticNetIndividuaL::Clone()
{
   return ( new GeneticNetIndividuaL() );
} // end Clone

//***********************************************************************************************
// Class   : GeneticNetIndividuaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method  : EvaluateFitness
// Purpose : Here, we're tyring to evolve good neural networks.
//         : If we have a population size of say 10 and 100 generations,
//         : then we can really only afford to have each go through about
//         : 100 iterations on the training algorithm. That being true, it
//         : would give us 10 x 100 x 100 = 100,000 every optimization cycle.
//         : This implies that we shouldn't optimize less than every 100,000 epochs.
//         : Instead, let's go for 50 epochs and optimize every 250,000 epochs.
//***********************************************************************************************
long double GeneticNetIndividuaL::EvaluateFitness()
{
//FPRINT( "EvalFitness" );

   // Already calculated fitness:
   if( _myFitness > -1 )
   {
      return _myFitness;
   } // end if

   _myFitness = 0;
   VectoR* theGenome = GetChromosome()->GetGenome();
   ModBackproP* theNetwork = (DataLoadeR::GetInstance())->GetNeuralNet();

   long double initialError = theNetwork->GetError();
   long double temp = 0;
//FPRINT << "learning rate: " << theGenome->pVariables[ 0 ];
//FPRINT << "momentum: " << theGenome->pVariables[ 1 ];
   theNetwork->SetLearningRate( theGenome->pVariables[ 0 ] );
   theNetwork->SetMomentum( theGenome->pVariables[ 1 ] );

   /* not sure which is better:
   */
      theNetwork->SetMaximumEpoch( theNetwork->GetEpochs() + 100 );
      theNetwork->Run();
      temp = (initialError - theNetwork->GetError());
      _myFitness += temp;
      if( temp < 1 )
      {
         _myFitness *= 2;
      }
      if( initialError != 0 )
      {
         _myFitness /= initialError;
      }

/*
   for( int i=0; i<10; i++ )
   {
      theNetwork->SetMaximumEpoch( theNetwork->GetEpochs() + 5 );
      theNetwork->Run();
      _myFitness += (initialError - theNetwork->GetError());
      initialError = theNetwork->GetError();
   }
*/

   return _myFitness;
} // end EvaluateFitness
