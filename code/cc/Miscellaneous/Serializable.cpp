//***********************************************************************************************
// File     : Serializable.cpp
// Purpose  : Provides an interface for classes implementing persistence.
//          : Derived classes must implement methods such as Load and Store.
//          : 
// Author   : Brandon Benham 
// Date     : 6/11/00
//***********************************************************************************************

#include"Serializable.hpp"          

//***********************************************************************************************
// Class    : SerializablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
SerializablE::SerializablE()
{
   Setup();
} // end SerializablE default constructor

//***********************************************************************************************
// Class    : SerializablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Constructor
// Purpose  : Performs the construction actions.
//***********************************************************************************************
SerializablE::SerializablE( char* pcFileName )
{
   Setup();
   pcThe_File_Name = new char[ strlen(pcFileName) + 1 ];
   pcThe_File_Name[0] = '\0';
   strcpy( pcThe_File_Name, pcFileName );
} // end SerializablE constructor

//***********************************************************************************************
// Class    : SerializablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
SerializablE::~SerializablE()
{
   if( Fthe_Data_File != 0 )
   {
      delete Fthe_Data_File;
      Fthe_Data_File = 0;
   } // end if not null
   if( pcThe_File_Name != 0 )
   {
      delete[] pcThe_File_Name;
      pcThe_File_Name = 0;
   } // end if not null
   if( _theFileReader != 0 )
   {
      delete _theFileReader;
      _theFileReader = 0;
   } // end if not null
} // end SerializablE destructor

//***********************************************************************************************
// Class    : SerializablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void SerializablE::Setup()
{
   Fthe_Data_File    = 0;
   pcThe_File_Name   = 0;
   _theFileReader    = 0;
} // end Setup

