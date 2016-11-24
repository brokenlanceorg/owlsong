//***********************************************************************************************
// File     : ValueEncodedMutationStrategy.cpp
// Purpose  : Defines an implementation of the MutationStrategY interface.
//          : 
// Author   : Brandon Benham 
// Date     : 9/16/01
//***********************************************************************************************

#include"ValueEncodedMutationStrategy.hpp"          

//***********************************************************************************************
// Class    : ValueEncodedMutationStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
ValueEncodedMutationStrategY::ValueEncodedMutationStrategY() : MutationStrategY()
{
   Setup();
} // end ValueEncodedMutationStrategY default constructor

//***********************************************************************************************
// Class    : ValueEncodedMutationStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
ValueEncodedMutationStrategY::~ValueEncodedMutationStrategY()
{
} // end ValueEncodedMutationStrategY destructor

//***********************************************************************************************
// Class    : ValueEncodedMutationStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void ValueEncodedMutationStrategY::Setup()
{
} // end Setup

//***********************************************************************************************
// Class    : ValueEncodedMutationStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Clone
// Purpose  : 
//***********************************************************************************************
MutationStrategY* ValueEncodedMutationStrategY::Clone()
{
   ValueEncodedMutationStrategY* clone = new ValueEncodedMutationStrategY();
   clone->SetMutationProbability( _MutationProbability );
   return clone;
} // end Clone

//***********************************************************************************************
// Class    : ValueEncodedMutationStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Mutate
// Purpose  : Performs the following process:
//          : 
//          :  Algorithm:
//          :  For this Chromosome, find the smallest and largest
//          :  genes (Smallest, Largest).
//          :  Find range = Largest - Smallest.
//          :  Pick a random number r in [0, range].
//          :  Pick a random number s in [0, 1].
//          :  If s < 0.5 then r *= -1.
//          :  This gene += r.
//          :
//          : I'm actually going to change this algorithm to the following:
//          :   For each gene:
//          :      Pick a random number r in [0, 1]
//          :      Pick a random number s in [0, 1]
//          :      Pick a random number t in [0, 1]
//          :      if t <= _mutation probability
//          :        adj = 1.r * gene
//          :      else
//          :        adj = r * gene                  
//          :      if s < 0.5 then adj *= -1
//          :      gene += adj
//          :
//          :                    
//***********************************************************************************************
void ValueEncodedMutationStrategY::Mutate( void* p1 )
{
//FPRINT( "Mutate" );
   ChromosomE* theChrome = (ChromosomE*)p1;
   VectoR* theGenes = theChrome->GetGenome();
   long double theAdjustment = 0;

//FPRINT << "mutation prob is: " << _MutationProbability;

   for( int i=0; i<theGenes->cnRows; i++ )
   {
      long double p = _theGenerator->GetRandomLngDouble();
//FPRINT << "p is: " << p;
      if( p <= _MutationProbability )
      {
//FPRINT << "if 1";
         //FPRINT( "Mutate" );
         if( _theGenerator->GetRandomLngDouble() <= _MutationProbability )
         {
            p = 1 + _theGenerator->GetRandomLngDouble();
//FPRINT << "p is now: " << p;
         } // end if
         else
         {
            p = _theGenerator->GetRandomLngDouble();
//FPRINT << "p is else: " << p;
         } // end else                    
         theAdjustment = p * theGenes->pVariables[ i ];
         if( _theGenerator->GetRandomLngDouble() < 0.5 )
            theAdjustment *= -1;
         // Mutate the gene:
         theGenes->pVariables[ i ] += theAdjustment;
//FPRINT << "the gene is now: " << theGenes->pVariables[ i ];
      } // end if we want to mutate
   } // end for loop
} // end Mutate



