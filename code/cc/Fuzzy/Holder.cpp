//***********************************************************************************************
// File     : Holder.cpp
// Purpose  : Defines the XHolder classes
//          : 
// Author   : Brandon Benham 
// Date     : 5/30/00
//***********************************************************************************************

#include"Holder.hpp"          

//***********************************************************************************************
// Class : FuzzyholdeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
FuzzyholdeR::FuzzyholdeR()
{
   Setup();
} // end class default constructor

//***********************************************************************************************
// Class : FuzzyholdeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Constructor
// Purpose  : Performs the construction actions.
//***********************************************************************************************
FuzzyholdeR::FuzzyholdeR( FuzzyseT* theSet )
{
   Setup();
   TheObject = theSet;
} // end class constructor

//***********************************************************************************************
// Class : FuzzyholdeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
FuzzyholdeR::~FuzzyholdeR()
{
   if( TheObject != 0 )
      delete TheObject;
} // end class destructor

//***********************************************************************************************
// Class : FuzzyholdeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void FuzzyholdeR::Setup()
{
   NextHolder = 0;
   TheObject  = 0;
} // end Setup

//***********************************************************************************************
// Class : FuzzyholdeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : SetObject
// Purpose  : Sets the member pointer to passed in param.
//***********************************************************************************************
void FuzzyholdeR::SetObject( FuzzyseT* thePointer )
{
   TheObject = thePointer;
} // end Setup

//***********************************************************************************************
// Class : HedgeholdeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
HedgeholdeR::HedgeholdeR()
{
   Setup();
} // end class default constructor

//***********************************************************************************************
// Class : HedgeholdeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Constructor
// Purpose  : Performs the construction actions.
//***********************************************************************************************
HedgeholdeR::HedgeholdeR( HedgE* theHedge )
{
   Setup();
   TheObject = theHedge;
} // end class constructor

//***********************************************************************************************
// Class : HedgeholdeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
HedgeholdeR::~HedgeholdeR()
{
   if( TheObject != 0 )
      delete TheObject;
} // end class destructor

//***********************************************************************************************
// Class : HedgeholdeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void HedgeholdeR::Setup()
{
   NextHolder = 0;
   TheObject  = 0;
} // end Setup

//***********************************************************************************************
// Class : HedgeholdeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : SetObject
// Purpose  : Sets the member pointer to passed in param.
//***********************************************************************************************
void HedgeholdeR::SetObject( HedgE* thePointer )
{
   TheObject = thePointer;
} // end Setup

//***********************************************************************************************
// Class : ConsequentHoldeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
ConsequentHoldeR::ConsequentHoldeR()
{
   Setup();
} // end class default constructor

//***********************************************************************************************
// Class : ConsequentHoldeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Constructor
// Purpose  : Performs the construction actions.
//***********************************************************************************************
ConsequentHoldeR::ConsequentHoldeR( ConsequenT* theCon )
{
   Setup();
   TheObject = theCon;
} // end class constructor

//***********************************************************************************************
// Class : ConsequentHoldeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
ConsequentHoldeR::~ConsequentHoldeR()
{
   if( TheObject != 0 )
      delete TheObject;
} // end class destructor

//***********************************************************************************************
// Class : ConsequentHoldeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void ConsequentHoldeR::Setup()
{
   NextHolder = 0;
   TheObject  = 0;
} // end Setup

//***********************************************************************************************
// Class : ConsequentHoldeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : SetObject
// Purpose  : Sets the member pointer to passed in param.
//***********************************************************************************************
void ConsequentHoldeR::SetObject( ConsequenT* thePointer )
{
   TheObject = thePointer;
} // end Setup

