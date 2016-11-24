//***********************************************************************************************
// File     : ValueEncodedChromosome.cpp
// Purpose  : 
//          : 
// Author   : Brandon Benham 
// Date     : 9/20/01
//***********************************************************************************************

#include"ValueEncodedChromosome.hpp"          
#include"ValueEncodedMutationStrategy.hpp"          

//***********************************************************************************************
// Class    : ValueEncodedChromosomE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
ValueEncodedChromosomE::ValueEncodedChromosomE() : ChromosomE()
{
   Setup();
} // end ValueEncodedChromosomE default constructor

//***********************************************************************************************
// Class    : ValueEncodedChromosomE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
ValueEncodedChromosomE::~ValueEncodedChromosomE()
{
} // end ValueEncodedChromosomE destructor

//***********************************************************************************************
// Class    : ValueEncodedChromosomE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void ValueEncodedChromosomE::Setup()
{
} // end Setup

//***********************************************************************************************
// Class    : ValueEncodedChromosomE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Clone
// Purpose  : Returns an object of this type.
//***********************************************************************************************
ChromosomE* ValueEncodedChromosomE::Clone()
{
   return new ValueEncodedChromosomE();
} // end Setup
