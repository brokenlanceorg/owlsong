//***********************************************************************************************
// File     : RouletteWheelSelectionStrategy.cpp
// Purpose  : 
//          : 
// Author   : Brandon Benham 
// Date     : 9/20/01
//***********************************************************************************************

#include"RouletteWheelSelectionStrategy.hpp"          

//***********************************************************************************************
// Class    : RouletteWheelSelectionStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
RouletteWheelSelectionStrategY::RouletteWheelSelectionStrategY() : SelectionStrategY()
{
   Setup();
} // end RouletteWheelSelectionStrategY default constructor

//***********************************************************************************************
// Class    : RouletteWheelSelectionStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
RouletteWheelSelectionStrategY::~RouletteWheelSelectionStrategY()
{
} // end RouletteWheelSelectionStrategY destructor

//***********************************************************************************************
// Class    : RouletteWheelSelectionStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void RouletteWheelSelectionStrategY::Setup()
{
} // end Setup

//***********************************************************************************************
// Class    : RouletteWheelSelectionStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : GetParent
// Purpose  : Returns a parent to the client based on RouletteWheel Selection.
//***********************************************************************************************
IndividuaL* RouletteWheelSelectionStrategY::GetParent()
{
   IndividuaL* theParent = 0;
   long double theSum    = 0;
   long double r         = 0;

   if( _PopulationFitness < 0 )
   {
      CalculatePopulationFitness();
   } // end if population fitness less than zero

   r = _theGenerator->GetRandomLngDouble() * _PopulationFitness;

   for( int i=0; i<_thePopulation.size(); i++ )
   {
      theParent = _thePopulation.at( i );
      theSum += theParent->EvaluateFitness();
         
      if( theSum >= r )
      {
         break;
      } // end if we found our parent
   } // end while
   
   if( theParent == 0 )
   {
      theParent = _thePopulation.at( _thePopulation.size() - 1 );
   } // end if null
      
   return theParent;
} // end GetParent

