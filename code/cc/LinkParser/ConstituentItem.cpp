//***********************************************************************************************
// File     : ConstituentItem.cpp
// Purpose  :
//          :
// Author   : Brandon Benham
// Date     : 7/8/05
//***********************************************************************************************

#include "ConstituentItem.hpp"

//***********************************************************************************************
// Class    : ConstituentIteM
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
ConstituentIteM::ConstituentIteM()
{
   Setup();
} // end ConstituentIteM default constructor

//***********************************************************************************************
// Class    : ConstituentIteM
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
ConstituentIteM::~ConstituentIteM()
{
   for( int i=0; i<_domains.size(); i++ )
   {
      delete _domains.at( i );
   }
   _domains.clear();

   if( _linkType != 0 )
   {
      delete _linkType;
   }

   if( _context != 0 )
   {
      delete _context;
   }

   if( _linkedConstituent != 0 )
   {
      delete _linkedConstituent;
   }

   if( _constituent != 0 )
   {
      delete _constituent;
   }

   if( _inflectedForm != 0 )
   {
      delete _inflectedForm;
   }

} // end ConstituentIteM destructor

//***********************************************************************************************
// Class    : ConstituentIteM
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void ConstituentIteM::Setup()
{
   _linkType = 0;
   _context = 0;
   _linkedConstituent = 0;
   _constituent = 0;
   _inflectedForm = 0;
   _linkage = 0;
   _serialNumber = 0;
} // end Setup

//***********************************************************************************************
// Class    : ConstituentIteM
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : SetLinkType
// Purpose  : 
//***********************************************************************************************
void ConstituentIteM::SetLinkType( char* type )
{
   if( _linkType != 0 )
   {
      delete _linkType;
   }

   _linkType = new string( type );
}

//***********************************************************************************************
// Class    : ConstituentIteM
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : SetDomains
// Purpose  : 
//***********************************************************************************************
void ConstituentIteM::SetDomains( char* domains )
{
   char* theWord = strtok( domains, "," );

   while( (theWord = strtok( NULL, "," )) != NULL )
   {
      _domains.push_back( new string( theWord ) );
   }

}

//***********************************************************************************************
// Class    : ConstituentIteM
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : SetContext
// Purpose  : 
//***********************************************************************************************
void ConstituentIteM::SetContext( char* context )
{
   if( _context != 0 )
   {
      delete _context;
   }

   _context = new string( context );
}

//***********************************************************************************************
// Class    : ConstituentIteM
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : SetLinkedConstituent
// Purpose  : 
//***********************************************************************************************
void ConstituentIteM::SetLinkedConstituent( char* linkedConstituent )
{
   if( _linkedConstituent != 0 )
   {
      delete _linkedConstituent;
   }

   _linkedConstituent = new string( linkedConstituent );
}

//***********************************************************************************************
// Class    : ConstituentIteM
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : SetConstituent
// Purpose  : 
//***********************************************************************************************
void ConstituentIteM::SetConstituent( char* constituent )
{
   if( _constituent != 0 )
   {
      delete _constituent;
   }

   _constituent = new string( constituent );
}

//***********************************************************************************************
// Class    : ConstituentIteM
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : SetInflectedForm
// Purpose  : 
//***********************************************************************************************
void ConstituentIteM::SetInflectedForm( char* inflectedForm )
{
   if( _inflectedForm != 0 )
   {
      delete _inflectedForm;
   }

   _inflectedForm = new string( inflectedForm );
}

//***********************************************************************************************
// Class    : ConstituentIteM
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : SetLinkage
// Purpose  : 
//***********************************************************************************************
void ConstituentIteM::SetLinkage( long double linkage )
{
   _linkage = linkage;
}

//***********************************************************************************************
// Class    : ConstituentIteM
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : SetSerialNumber
// Purpose  : 
//***********************************************************************************************
void ConstituentIteM::SetSerialNumber( long serialNumber )
{
   _serialNumber = serialNumber;
}
