//***********************************************************************************************
// File     : Constituent.cpp
// Purpose  :
//          :
// Author   : Brandon Benham
// Date     : 5/12/05
//***********************************************************************************************

#include "Constituent.hpp"

using namespace std;

//***********************************************************************************************
// Class    : ConstituenT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
ConstituenT::ConstituenT() : LinkIteM()
{
   Setup();
} // end ConstituenT default constructor

//***********************************************************************************************
// Class    : ConstituenT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
ConstituenT::ConstituenT( char* form ) : LinkIteM()
{
   Setup();
   SetForm( new string( form ) );
} // end ConstituenT default constructor

//***********************************************************************************************
// Class    : ConstituenT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
ConstituenT::ConstituenT( int ord, string* form, string* inflected ) : LinkIteM()
{
   Setup();
   SetOrdinal( ord );
   SetForm( form );
   SetInflectedForm( inflected );
} // end ConstituenT default constructor

//***********************************************************************************************
// Class    : ConstituenT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
ConstituenT::~ConstituenT()
{
   //FPRINT( "~ConstituenT" );

   if( _form != 0 )
   {
      //FPRINT << "deleting form...";
      //FPRINT << _form->c_str();
      delete _form;
      _form = 0;
   }

   if( _inflectedForm != 0 )
   {
      //FPRINT << "deleting inflected form...";
      //FPRINT << _inflectedForm->c_str();
      delete _inflectedForm;
      _inflectedForm = 0;
   }

   for( int i=0; i<_constituentItems.size(); i++ )
   {
      delete _constituentItems.at( i );
   }
   _constituentItems.clear();

} // end ConstituenT destructor

//***********************************************************************************************
// Class    : ConstituenT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void ConstituenT::Setup()
{
   _ordinal = -1;
   _semanticDifferential = 0;
   _form = 0;
   _inflectedForm = 0;
} // end Setup
