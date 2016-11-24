//***********************************************************************************************
// File     : MultiplePointCrossoverStrategy.cpp
// Purpose  : 
//          : 
// Author   : Brandon Benham 
// Date     : 7/9/05
//***********************************************************************************************

#include"MultiplePointCrossoverStrategy.hpp"          

//***********************************************************************************************
// Class    : MultiplePointCrossoverStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
MultiplePointCrossoverStrategY::MultiplePointCrossoverStrategY() : CrossoverStrategY()
{
   Setup();
} // end MultiplePointCrossoverStrategY default constructor

//***********************************************************************************************
// Class    : MultiplePointCrossoverStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
MultiplePointCrossoverStrategY::~MultiplePointCrossoverStrategY()
{
} // end MultiplePointCrossoverStrategY destructor

//***********************************************************************************************
// Class    : MultiplePointCrossoverStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void MultiplePointCrossoverStrategY::Setup()
{
} // end Setup

//***********************************************************************************************
// Class    : MultiplePointCrossoverStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Crossover
// Purpose  : Mates the two parents to produce the child chromosome.
//***********************************************************************************************
void MultiplePointCrossoverStrategY::Crossover( void* p1, void* p2, void* p3 )
{
   //FPRINT( "Crossover" );

   long double r = _theGenerator->GetRandomLngDouble();
   
   // decide if we even do a crossover:
   if( r > _CrossoverProbability )
   {
      VectoR* theNew = new VectoR( ((ChromosomE*)p1)->GetGenome() );
      ((ChromosomE*)p3)->SetGenome( theNew );
      return;
   } // end if
   
   ChromosomE* theParentOne = (ChromosomE*)p1;
   ChromosomE* theParentTwo = (ChromosomE*)p2;
   ChromosomE* theChild     = (ChromosomE*)p3;

   // First, get the number of crossover points.
   int numberOfPoints = (int)(_theGenerator->GetRandomLngDouble() * (theParentOne->GetGenome()->cnRows) );
   int maxNumberOfPoints = (int)( ((long double)theParentOne->GetGenome()->cnRows) / (long double)2 );

   // We must have a least a little crossover:
   if( numberOfPoints == 0 )
   {
//FPRINT << "if 2...";
      numberOfPoints = 1;
   }
   else if( numberOfPoints > maxNumberOfPoints )
   {
//FPRINT << "else 2...";
      numberOfPoints = maxNumberOfPoints; 
   }
                  
//FPRINT << "numberOfPoints:";
//FPRINT << numberOfPoints;

   VectoR* randomVector = new VectoR( (2 * numberOfPoints) );
   randomVector->Fill_Rand();
   int position = 0;

   // Next, get the max size of each crossover interval.
   // For instance, if you have 4 crossover points, you have 5 intervals, and if your
   // chromosome is length 20, then the max interval is 4 genes wide.
   int maxInterval = (int)((double)theParentOne->GetGenome()->cnRows / (double)(numberOfPoints) );
   int offset = 0;
   int currentPoint = 0;
   int parentOne = 0;

//FPRINT << "max interval:";
//FPRINT << maxInterval;

   if( randomVector->pVariables[ position++ ] > 0.5 )
   {
//FPRINT << "if 3...";
      parentOne = 1;
   }

   for( int i=0; i<numberOfPoints; i++ )
   {
//FPRINT << "current";
//FPRINT << currentPoint;

      // calculate the offset from the current crossover point.
      long double tempRand = randomVector->pVariables[ position++ ];

//FPRINT << "tempRand";
//FPRINT << tempRand;

      offset = (int)(tempRand * (long double)(maxInterval) );

//FPRINT << "offset";
//FPRINT << offset;

      // at least a little bit of crossover for this point.
      if( offset <= 1 )
      {
//FPRINT << "if 4...";
         offset = 2;
      }

      // the GetGenes method is inclusive at endpoints.
      if( parentOne == 1 )
      {
         parentOne = 0;
         theChild->AddGenes( theParentOne->GetGenes( currentPoint, currentPoint + offset - 1 ) );
      }
      else
      {
         parentOne = 1;
         theChild->AddGenes( theParentTwo->GetGenes( currentPoint, currentPoint + offset - 1 ) );
      }

      currentPoint += offset;
   }

//FPRINT << "current";
//FPRINT << currentPoint;

   // now get the last block of genes.
   if( currentPoint < theParentOne->GetGenome()->cnRows )
   {
      if( parentOne == 1 )
      {
         theChild->AddGenes( theParentOne->GetGenes( currentPoint, theParentOne->GetGenome()->cnRows - 1 ) );
      }
      else
      {
         theChild->AddGenes( theParentTwo->GetGenes( currentPoint, theParentTwo->GetGenome()->cnRows - 1 ) );
      }
   }
   
} // end Crossover

//***********************************************************************************************
// Class    : MultiplePointCrossoverStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Clone
// Purpose  : 
//***********************************************************************************************
CrossoverStrategY* MultiplePointCrossoverStrategY::Clone()
{
   return new MultiplePointCrossoverStrategY();
} // end Clone
