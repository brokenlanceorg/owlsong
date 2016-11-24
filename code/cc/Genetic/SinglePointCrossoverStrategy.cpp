//***********************************************************************************************
// File     : SinglePointCrossoverStrategy.cpp
// Purpose  : 
//          : 
// Author   : Brandon Benham 
// Date     : 9/16/01
//***********************************************************************************************

#include"SinglePointCrossoverStrategy.hpp"          

//***********************************************************************************************
// Class    : SinglePointCrossoverStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
SinglePointCrossoverStrategY::SinglePointCrossoverStrategY() : CrossoverStrategY()
{
   Setup();
} // end SinglePointCrossoverStrategY default constructor

//***********************************************************************************************
// Class    : SinglePointCrossoverStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
SinglePointCrossoverStrategY::~SinglePointCrossoverStrategY()
{
} // end SinglePointCrossoverStrategY destructor

//***********************************************************************************************
// Class    : SinglePointCrossoverStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void SinglePointCrossoverStrategY::Setup()
{
} // end Setup

//***********************************************************************************************
// Class    : SinglePointCrossoverStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Crossover
// Purpose  : Mates the two parents to produce the child chromosome.
//***********************************************************************************************
void SinglePointCrossoverStrategY::Crossover( void* p1, void* p2, void* p3 )
{
   long double r = _theGenerator->GetRandomLngDouble();
   
   if( r > _CrossoverProbability )
   {
      VectoR* theNew = new VectoR( ((ChromosomE*)p1)->GetGenome() );
      ((ChromosomE*)p3)->SetGenome( theNew );
      return;
   } // end if
   
   ChromosomE* theParentOne = (ChromosomE*)p1;
   ChromosomE* theParentTwo = (ChromosomE*)p2;
   ChromosomE* theChild     = (ChromosomE*)p3;

   int iPoint = (int)(_theGenerator->GetRandomLngDouble() * (theParentOne->GetGenome()->cnRows - 1) );
   // We must have a least a little crossover:
   if( iPoint == 0 )
   {
      iPoint += 1;
   }
   else if( iPoint == (theParentOne->GetGenome()->cnRows - 1) )
   {
      iPoint -= 1;
   }
                  
   theChild->AddGenes( theParentOne->GetGenes( 0, iPoint - 1 ) );
   theChild->AddGenes( theParentTwo->GetGenes( iPoint, theParentTwo->GetGenome()->cnRows - 1 ) );
   
} // end Crossover

//***********************************************************************************************
// Class    : SinglePointCrossoverStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Clone
// Purpose  : 
//***********************************************************************************************
CrossoverStrategY* SinglePointCrossoverStrategY::Clone()
{
   return new SinglePointCrossoverStrategY();
} // end Clone
