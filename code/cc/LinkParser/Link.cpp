//***********************************************************************************************
// File     : Link.cpp
// Purpose  :
//          :
// Author   : Brandon Benham
// Date     : 5/12/05
//***********************************************************************************************

#include "Link.hpp"

using namespace std;

//***********************************************************************************************
// Class    : LinK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
LinK::LinK() : LinkIteM()
{
   Setup();
} // end LinK default constructor

//***********************************************************************************************
// Class    : LinK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
LinK::LinK( string* label, int left, int right, vector< string* > domains ) : LinkIteM()
{
   Setup();

   _label = label;
   _left = left;
   _right = right;

   for( int i=0; i<domains.size(); i++ )
   {
      _domains.push_back( domains.at(i) );
   }

} // end LinK default constructor

//***********************************************************************************************
// Class    : LinK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
LinK::~LinK()
{
   //FPRINT( "~LinK" );
   if( _label != 0 )
   {
      //FPRINT << "deleteing label...";
      //FPRINT << _label->c_str();
      delete _label;
      _label = 0;
   }

   for( int i=0; i<_domains.size(); i++ )
   {
      //FPRINT << "deleteing a domain...";
      //FPRINT << _domains.at(i)->c_str();
      delete _domains.at( i );
   }

      //FPRINT << "clear a domain...";
   _domains.clear();

} // end LinK destructor

//***********************************************************************************************
// Class    : LinK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void LinK::Setup()
{
   _leftConstituent = 0;
   _rightConstituent = 0;
   _label = 0;
   _left = 0;
   _right = 0;
   _visited = false;
} // end Setup
