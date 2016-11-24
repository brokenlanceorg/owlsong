//***********************************************************************************************
// File     : LinkItem.cpp
// Purpose  :
//          :
// Author   : Brandon Benham
// Date     : 5/12/05
//***********************************************************************************************

#include "LinkItem.hpp"

//***********************************************************************************************
// Class    : LinkIteM
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
LinkIteM::LinkIteM()
{
   Setup();
} // end LinkIteM default constructor

//***********************************************************************************************
// Class    : LinkIteM
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
LinkIteM::~LinkIteM()
{
   //FPRINT( "~LinkIteM" );
} // end LinkIteM destructor

//***********************************************************************************************
// Class    : LinkIteM
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void LinkIteM::Setup()
{
   _leftConstituent = 0;
   _rightConstituent = 0;
} // end Setup

//***********************************************************************************************
// Class    : LinkIteM
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//***********************************************************************************************
void LinkIteM::SetLeftConstituent( LinkIteM* anItem )
{
   if( _leftConstituent != 0 )
   {
      delete _leftConstituent;
   }
   _leftConstituent = anItem;
} // end SetLeftConstituent

//***********************************************************************************************
// Class    : LinkIteM
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//***********************************************************************************************
void LinkIteM::SetRightConstituent( LinkIteM* anItem )
{
   if( _rightConstituent != 0 )
   {
      delete _rightConstituent;
   }
   _rightConstituent = anItem;
} // end SetLeftConstituent
