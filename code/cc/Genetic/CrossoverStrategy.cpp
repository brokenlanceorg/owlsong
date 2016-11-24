//***********************************************************************************************
// File     : CrossoverStrategy.cpp
// Purpose  : 
//          : 
// Author   : Brandon Benham 
// Date     : 9/16/01
//***********************************************************************************************

#include"CrossoverStrategy.hpp"          

//***********************************************************************************************
// Class    : CrossoverStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
CrossoverStrategY::CrossoverStrategY()
{
   Setup();
} // end CrossoverStrategY default constructor

//***********************************************************************************************
// Class    : CrossoverStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
CrossoverStrategY::~CrossoverStrategY()
{
   if( _theGenerator != 0 )
   {
      delete _theGenerator;
      _theGenerator = 0;
   } // end if not null
} // end CrossoverStrategY destructor

//***********************************************************************************************
// Class    : CrossoverStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void CrossoverStrategY::Setup()
{
   _GeneSize              = 0;
   _CrossoverProbability  = 0.9;
   _theGenerator          = new RandoM( 0, 1 );
   _theGenerator->CalcSeed();
} // end Setup

