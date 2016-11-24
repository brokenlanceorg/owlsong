//***********************************************************************************************
// File     : SentenceIteratorListener.cpp
// Purpose  :
//          :
// Author   : Brandon Benham
// Date     : 5/12/05
//***********************************************************************************************

#include "SentenceIteratorListener.hpp"

//***********************************************************************************************
// Class    : SentenceIteratorListeneR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
SentenceIteratorListeneR::SentenceIteratorListeneR() : SentenceListeneR()
{
   Setup();
} // end SentenceListeneR default constructor

//***********************************************************************************************
// Class    : SentenceIteratorListeneR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
SentenceIteratorListeneR::~SentenceIteratorListeneR()
{
   if( _semanticDifferential != 0 )
   {
      delete _semanticDifferential;
      _semanticDifferential = 0;
   }
} // end SentenceListeneR destructor

//***********************************************************************************************
// Class    : SentenceIteratorListeneR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//***********************************************************************************************
void SentenceIteratorListeneR::Setup()
{
   _semanticDifferential = 0;
}

//***********************************************************************************************
// Class    : SentenceIteratorListeneR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//***********************************************************************************************
void SentenceIteratorListeneR::EncounteredConstituent( ConstituenT* aConstituent, int level )
{
   string* theWord = aConstituent->GetForm();
   string* theInflectedWord = aConstituent->GetInflectedForm();

   cout << "The word is: " << theWord->c_str() << endl;
   cout << "The inflected word is: " << theInflectedWord->c_str() << endl;
   cout << "The level is: " << level << endl;

   SemanticDifferentiaL* sd = aConstituent->GetSemanticDifferential();

   if( sd != 0 )
   {
      cout << "The agent dimension is: " << sd->GetAgentDimension() << endl;
      cout << "The kline dimension is: " << sd->GetKLineDimension() << endl;
      cout << "The thing dimension is: " << sd->GetThingDimension() << endl;
      cout << "The spatial dimension is: " << sd->GetSpatialDimension() << endl;
      cout << "The event dimension is: " << sd->GetEventDimension() << endl;
      cout << "The causal dimension is: " << sd->GetCausalDimension() << endl;
      cout << "The functional dimension is: " << sd->GetFunctionalDimension() << endl;
      cout << "The affective dimension is: " << sd->GetAffectiveDimension() << endl;
   }
} // end EncounteredConstituent

//***********************************************************************************************
// Class    : SentenceIteratorListeneR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//***********************************************************************************************
void SentenceIteratorListeneR::EncounteredLink( LinK* aLink, int level )
{
   cout << "The link is: " << aLink->GetLabel()->c_str() << endl;
   cout << "The level is: " << level << endl;

   vector< string* > domains = aLink->GetDomains();

   for( int j=0; j<domains.size(); j++ )
   {
      string* aDomain = domains.at( j );
      if( j == 0 )
      {
         cout << "Domains are: " << aDomain->c_str();
      }
      cout << " " << aDomain->c_str();
   }

   cout << endl;
} // end EncounteredLink
