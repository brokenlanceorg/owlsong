//***********************************************************************************************
// File     : Individual.cpp
// Purpose  : This is the interface that any solution must implement in order to 
//          : be used by the GeneticAlgorithm. The primary methods to be implemented
//          : are EvaluateFitness() and clone().
//          : 
// Author   : Brandon Benham 
// Date     : 9/16/01
//***********************************************************************************************

#include"Individual.hpp"          

//***********************************************************************************************
// Class    : IndividuaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
IndividuaL::IndividuaL()
{
   Setup();
} // end IndividuaL default constructor

//***********************************************************************************************
// Class    : IndividuaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
IndividuaL::~IndividuaL()
{      
   if( _myChromosome != 0 )
   {
      delete _myChromosome;
      _myChromosome = 0;
   } // end if not null
} // end IndividuaL destructor

//***********************************************************************************************
// Class    : IndividuaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void IndividuaL::Setup()
{
   _myChromosome = 0;
} // end Setup

//***********************************************************************************************
// Class    : IndividuaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : SetChromosome
// Purpose  : 
//***********************************************************************************************
void IndividuaL::SetChromosome( ChromosomE* theChrome )
{
   if( _myChromosome != 0 )
   {
      delete _myChromosome;
   } // end if not null

   _myChromosome = theChrome;
} // end SetChromosome

//***********************************************************************************************
// Class    : IndividuaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : SetCrossoverStrategy
// Purpose  : 
//***********************************************************************************************
void IndividuaL::SetCrossoverStrategy( CrossoverStrategY* theStrat )
{
   if( _myChromosome != 0 )
   {
      _myChromosome->SetCrossoverStrategy( theStrat );
   } // end if not null
} // end SetCrossoverStrategy

//***********************************************************************************************
// Class    : IndividuaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : GetGenome
// Purpose  : 
//***********************************************************************************************
VectoR* IndividuaL::GetGenome()
{
   VectoR* theGenome = 0;
   if( _myChromosome != 0 )
   {
      theGenome = _myChromosome->GetGenome();
   } // end if chromosome not null

   return theGenome;
} // end GetGenome

//***********************************************************************************************
// Class    : IndividuaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Operator '+' overload, which is crossover or mate.
// Purpose  : Performs the crossover operation between two individuals.
//***********************************************************************************************
IndividuaL& IndividuaL::operator + (IndividuaL& b)
{
   IndividuaL* theChild = (IndividuaL*)this->Clone();
   ChromosomE* chrome1 = (ChromosomE*)b.GetChromosome();
   ChromosomE* chrome2 = _myChromosome->Crossover( chrome1 );
   theChild->SetChromosome( chrome2 );
   
   return *theChild;
} // end operator + overload


