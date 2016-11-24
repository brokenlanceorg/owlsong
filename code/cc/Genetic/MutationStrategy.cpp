//***********************************************************************************************
// File     : MutationStrategy.cpp
// Purpose  : 
//          : 
// Author   : Brandon Benham 
// Date     : 9/16/01
//***********************************************************************************************

#include"MutationStrategy.hpp"          

//***********************************************************************************************
// Class    : MutationStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
MutationStrategY::MutationStrategY()
{
   Setup();
} // end MutationStrategY default constructor

//***********************************************************************************************
// Class    : MutationStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
MutationStrategY::~MutationStrategY()
{
   if( _theGenerator != 0 )
   {
      delete _theGenerator;
      _theGenerator = 0;
   } // end if not null
} // end MutationStrategY destructor

//***********************************************************************************************
// Class    : MutationStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void MutationStrategY::Setup()
{
   _theGenerator = new RandoM( 0, 1 );
   _theGenerator->CalcSeed();
   //_MutationProbability = 0.01;
   _MutationProbability = 0.08;
} // end Setup

