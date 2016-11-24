//***********************************************************************************************
// File	    : DataLoader.cpp
// Purpose  : 
//          : 
// Author   : Brandon Benham 
// Date	    : 4/9/05
//***********************************************************************************************

#include"DataLoader.hpp"          

DataLoadeR* DataLoadeR::_myInstance = 0;
ModBackproP* DataLoadeR::_theNetwork = 0;

//***********************************************************************************************
// Class   : DataLoadeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method  : Constructor
// Purpose : Performs the construction actions.
//***********************************************************************************************
DataLoadeR::DataLoadeR()
{
   Setup();
} // end DataLoadeR constructor

//***********************************************************************************************
// Class   : DataLoadeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Destructor
// Purpose	: Performs the destruction actions.
//***********************************************************************************************
DataLoadeR::~DataLoadeR()
{
   if( _theNetwork != 0 )
   {
      delete _theNetwork;
      _theNetwork = 0;
   } // end if not null
} // end DataLoadeR destructor

//***********************************************************************************************
// Class	   : DataLoadeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Setup
// Purpose	: Performs the basic setup actions.
//***********************************************************************************************
DataLoadeR* DataLoadeR::GetInstance()
{
   if( _myInstance == 0 )
   {
      _myInstance = new DataLoadeR();
   }

   return _myInstance;
}

//***********************************************************************************************
// Class	   : DataLoadeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Setup
// Purpose	: Performs the basic setup actions.
//***********************************************************************************************
void DataLoadeR::Setup()
{
   _theNetwork = 0;
} // end Setup
