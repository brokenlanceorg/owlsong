//***********************************************************************************************
// File     : GeneticAlgorithm.cpp
// Purpose  : 
//          : 
// Author   : Brandon Benham 
// Date     : 9/20/01
//***********************************************************************************************

#include"GeneticAlgorithm.hpp"          

//***********************************************************************************************
// Class    : GeneticAlgorithM
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
GeneticAlgorithM::GeneticAlgorithM()
{
   Setup();
} // end GeneticAlgorithM default constructor

//***********************************************************************************************
// Class    : GeneticAlgorithM
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
GeneticAlgorithM::~GeneticAlgorithM()
{
   if( _theSelector != 0 )
   {
      delete _theSelector;
      _theSelector = 0;
   } // end if not null

   for( int i=0; i<_thePopulation.size(); i++ )
   {
      delete _thePopulation.at( i );
   } // end if not null
   _thePopulation.clear();

} // end GeneticAlgorithM destructor

//***********************************************************************************************
// Class    : GeneticAlgorithM
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void GeneticAlgorithM::Setup()
{
   _MaxNumberOfGenerations = 3;  // Just to test with...
   _MaximumFitness         = 1000;
   _PopulationSize         = 3;  // Just to test with....
   _theSelector            = 0;
   _theBestIndividual      = 0;
   _theBestFitness         = 0;
} // end Setup

//***********************************************************************************************
// Class    : GeneticAlgorithM
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : AddIndividual
// Purpose  : 
//***********************************************************************************************
void GeneticAlgorithM::AddIndividual( IndividuaL* theInd )
{
   _thePopulation.push_back( theInd );
} // end AddIndividual

//***********************************************************************************************
// Class    : GeneticAlgorithM
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : SetSelectionStrategy
// Purpose  : 
//***********************************************************************************************
void GeneticAlgorithM::SetSelectionStrategy( SelectionStrategY* theStrat )
{
   if( _theSelector != 0 )
   {
      delete _theSelector;
   } // end if not null

   _theSelector = theStrat;
} // end SetSelectionStrategy

//***********************************************************************************************
// Class    : GeneticAlgorithM
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : CloneBestIndividual
// Purpose  : 
//***********************************************************************************************
void GeneticAlgorithM::CloneBestIndividual()
{
   IndividuaL* theBest = _theBestIndividual->Clone();
   ChromosomE* theChrom = theBest->GetChromosome()->Clone();
   VectoR* theGenome = new VectoR( _theBestIndividual->GetChromosome()->GetGenome() );
   theChrom->SetMutationStrategy( theBest->GetChromosome()->CloneMutationStrategy() );
   theChrom->SetCrossoverStrategy( theBest->GetChromosome()->CloneCrossoverStrategy() );
   theChrom->SetGenome( theGenome );
   theBest->SetChromosome( theChrom );
   theBest->SetFitness( _theBestIndividual->EvaluateFitness() );
   _theBestIndividual = theBest;
} // end CloneBestIndividual

//***********************************************************************************************
// Class    : GeneticAlgorithM
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : GetNewGeneration
// Purpose  : 
//***********************************************************************************************
vector< IndividuaL* > GeneticAlgorithM::GetNewGeneration()
{
   //FPRINT( "GetNewGeneration" );
   vector< IndividuaL* > myGeneration;
   long double theBestEval    = -20000;
   int CurrPopulationSize     = 0;
   IndividuaL* ParentOne      = 0;
   IndividuaL* ParentTwo      = 0;
   IndividuaL* theChild       = 0;

   while( CurrPopulationSize++ < _PopulationSize )
   {
      // Elitism:
      if( CurrPopulationSize == 1 && _theBestIndividual != 0 )
      {
         myGeneration.push_back( _theBestIndividual );
         theBestEval = _theBestIndividual->EvaluateFitness();
         CurrPopulationSize++;
      } // end if we have a best individual
//FPRINT << "About to get parents...";
      ParentOne = _theSelector->GetParent();
      ParentTwo = _theSelector->GetParent();
//FPRINT << "got parents...";
        
      theChild = &(*ParentOne + *ParentTwo);
//FPRINT << "About to mutate...";
      theChild->Mutate();  
//FPRINT << "Adding object.....";
      myGeneration.push_back( theChild );
//FPRINT << "Added object.....";
      // Check for Elitism: 
      if( theChild->EvaluateFitness() > theBestEval )
      {
//FPRINT << "Found a best ind.....";
         _theBestIndividual = theChild;
         theBestEval = _theBestIndividual->EvaluateFitness();
      } // end if best individual 
   } // end while
//FPRINT << "Returning new popualation.....";

   return myGeneration;
} // end GetNewGeneration

//***********************************************************************************************
// Class    : GeneticAlgorithM
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Evolve
// Purpose  : This will return the best evolved individual at the end of the run.
//          : Note that the pointer returned will be valid only as long as the
//          : GeneticAlgorithm object lasts.
//***********************************************************************************************
IndividuaL* GeneticAlgorithM::Evolve()
{
   //FPRINT( "E" );
   int                   CurrentGeneration = 0;
   vector< IndividuaL* > currentPopulation;

   while( CurrentGeneration++ < _MaxNumberOfGenerations &&
               _theBestFitness < _MaximumFitness )
   {
      // This method will delete the old population:
      _theSelector->SetPopulation( _thePopulation );
      //FPRINT << CurrentGeneration;
      currentPopulation = GetNewGeneration();
      CloneBestIndividual(); // Save the best for the next generation
      _theBestFitness = _theBestIndividual->EvaluateFitness();
      // is this a MEMORY LEAK!!!!!????:
      _thePopulation = currentPopulation;
   } // end generation loop

   return _theBestIndividual;
} // end Evolve
