//***********************************************************************************************
// File     : Enumeration.cpp
// Purpose  : Provides an interface for an enumeration-type data set.
//          : Derived classes must furnish implementations for the operations
//          : such as GetNextElement and HasMoreElements.
//          : 
// Author   : Brandon Benham 
// Date     : 6/11/00
//***********************************************************************************************

#include"Enumeration.hpp"          

//***********************************************************************************************
// Class    : EnumeratioN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
EnumeratioN::EnumeratioN()
{
   Setup();
} // end EnumeratioN default constructor

//***********************************************************************************************
// Class    : EnumeratioN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
EnumeratioN::~EnumeratioN()
{
} // end EnumeratioN destructor

//***********************************************************************************************
// Class    : EnumeratioN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void EnumeratioN::Setup()
{
   iNumber_Of_Elements = 0;
   iCurrent_Number     = -1;
} // end Setup

