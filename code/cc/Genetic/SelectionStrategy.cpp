//***********************************************************************************************
// File     : SelectionStrategy.cpp
// Purpose  : 
//          : 
// Author   : Brandon Benham 
// Date     : 9/16/01
//***********************************************************************************************

#include"SelectionStrategy.hpp"          

//***********************************************************************************************
// Class    : SelectionStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
SelectionStrategY::SelectionStrategY()
{
   Setup();
} // end SelectionStrategY default constructor

//***********************************************************************************************
// Class    : SelectionStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
SelectionStrategY::~SelectionStrategY()
{
   if( _theGenerator != 0 )
   {
      delete _theGenerator;
      _theGenerator = 0;
   } // end if not null

   for( int i=0; i<_thePopulation.size(); i++ )
   {
      delete _thePopulation.at( i );
   } // end if not null
   _thePopulation.clear();

} // end SelectionStrategY destructor

//***********************************************************************************************
// Class    : SelectionStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void SelectionStrategY::Setup()
{
   _PopulationFitness = -1;
   _theGenerator      = new RandoM( 0, 1 );
   _theGenerator->CalcSeed();
} // end Setup

//***********************************************************************************************
// Class    : SelectionStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : CalculatePopulationFitness
// Purpose  : 
//***********************************************************************************************
void SelectionStrategY::CalculatePopulationFitness()
{
   //FPRINT( "CalcPopFitness" );

   _PopulationFitness = 0;
   IndividuaL* theIndividual = 0;

   for( int i=0; i<_thePopulation.size(); i++ )
   {
      //FPRINT << "Calculating pop fitness for idividual: ";
      //FPRINT << i;
      theIndividual = _thePopulation.at( i );
      _PopulationFitness += theIndividual->EvaluateFitness();
   } // end while
} // end CalculatePopulationFitness

//***********************************************************************************************
// Class    : SelectionStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : SetPopulation
// Purpose  : 
//***********************************************************************************************
void SelectionStrategY::SetPopulation( vector< IndividuaL* > thePop )
{
   for( int i=0; i<_thePopulation.size(); i++ )
   {
      delete _thePopulation.at( i );
   } // end if not null
   _thePopulation.clear();

   _PopulationFitness = -1;
   _thePopulation = thePop;
} // end SetPopulation
