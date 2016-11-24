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
SentenceIteratorListeneR::SentenceIteratorListeneR() //: SentenceListeneR()
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
   //FPRINT( "~SentenceIteratorListeneR" );

   if( _fuzzyFactory != 0 )
   {
      //FPRINT << "2";
      delete _fuzzyFactory;
      _fuzzyFactory = 0;
   }
   if( _agentFuzzySystem != 0 )
   {
      //FPRINT << "3";
      delete _agentFuzzySystem;
      _agentFuzzySystem = 0;
   }
   if( _klineFuzzySystem != 0 )
   {
      //FPRINT << "4";
      delete _klineFuzzySystem;
      _klineFuzzySystem = 0;
   }
   if( _thingFuzzySystem != 0 )
   {
      //FPRINT << "5";
      delete _thingFuzzySystem;
      _thingFuzzySystem = 0;
   }
   if( _spatialFuzzySystem != 0 )
   {
      //FPRINT << "6";
      delete _spatialFuzzySystem;
      _spatialFuzzySystem = 0;
   }
   if( _eventFuzzySystem != 0 )
   {
      //FPRINT << "7";
      delete _eventFuzzySystem;
      _eventFuzzySystem = 0;
   }
   if( _causalFuzzySystem != 0 )
   {
      //FPRINT << "8";
      delete _causalFuzzySystem;
      _causalFuzzySystem = 0;
   }
   if( _functionalFuzzySystem != 0 )
   {
      //FPRINT << "9";
      delete _functionalFuzzySystem;
      _functionalFuzzySystem = 0;
   }
   if( _affectiveFuzzySystem != 0 )
   {
      //FPRINT << "10";
      delete _affectiveFuzzySystem;
      _affectiveFuzzySystem = 0;
   }
   if( _linkValueFuzzySystem != 0 )
   {
      //FPRINT << "11";
      delete _linkValueFuzzySystem;
      _linkValueFuzzySystem = 0;
   }
      //FPRINT << "12";
} // end SentenceListeneR destructor

//***********************************************************************************************
// Class    : SentenceIteratorListeneR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//***********************************************************************************************
void SentenceIteratorListeneR::Setup()
{
   _agentFuzzySystem = 0;
   _klineFuzzySystem = 0;
   _thingFuzzySystem = 0;
   _spatialFuzzySystem = 0;
   _eventFuzzySystem = 0;
   _causalFuzzySystem = 0;
   _functionalFuzzySystem = 0;
   _affectiveFuzzySystem = 0;
   _linkValueFuzzySystem = 0;

   _fuzzyFactory = new FuzzyfactorY();
}

//***********************************************************************************************
// Class    : SentenceIteratorListeneR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//***********************************************************************************************
SemanticDifferentiaL* SentenceIteratorListeneR::GetSemanticDifferential()
{
   SemanticDifferentiaL* sd = new SemanticDifferentiaL();
   long double result = 0;

   if( _agentFuzzySystem != 0 )
   {
      *_agentFuzzySystem >> result;
      sd->SetAgentDimension( Absolute( result ) );

      *_klineFuzzySystem >> result;
      sd->SetKLineDimension( Absolute( result ) );

      *_thingFuzzySystem >> result;
      sd->SetThingDimension( Absolute( result ) );

      *_spatialFuzzySystem >> result;
      sd->SetSpatialDimension( Absolute( result ) );

      *_eventFuzzySystem >> result;
      sd->SetEventDimension( Absolute( result ) );

      *_causalFuzzySystem >> result;
      sd->SetCausalDimension( Absolute( result ) );

      *_functionalFuzzySystem >> result;
      sd->SetFunctionalDimension( Absolute( result ) );

      *_affectiveFuzzySystem >> result;
      sd->SetAffectiveDimension( Absolute( result ) );
   }

   return sd;
}

//***********************************************************************************************
// Class    : SentenceIteratorListeneR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//***********************************************************************************************
void SentenceIteratorListeneR::PushSemanticDifferential( SemanticDifferentiaL* sd, int level )
{
   long double scale;

   FuzzyseT* aFuzzySet = _fuzzyFactory->CreateFuzzySet( 1, sd->GetAgentDimension(), true );
   ConsequenT* aConsequent = _fuzzyFactory->CreateConsequent( aFuzzySet );
   scale = ((long double)1 / (long double)(level + 1));

   *_agentFuzzySystem + *aConsequent;
   *aConsequent << scale;

   aFuzzySet = _fuzzyFactory->CreateFuzzySet( 1, sd->GetKLineDimension(), true );
   aConsequent = _fuzzyFactory->CreateConsequent( aFuzzySet );
   *aConsequent << scale;
   *_klineFuzzySystem + *aConsequent;

   aFuzzySet = _fuzzyFactory->CreateFuzzySet( 1, sd->GetThingDimension(), true );
   aConsequent = _fuzzyFactory->CreateConsequent( aFuzzySet );
   *aConsequent << scale;
   *_thingFuzzySystem + *aConsequent;

   aFuzzySet = _fuzzyFactory->CreateFuzzySet( 1, sd->GetSpatialDimension(), true );
   aConsequent = _fuzzyFactory->CreateConsequent( aFuzzySet );
   *aConsequent << scale;
   *_spatialFuzzySystem + *aConsequent;

   aFuzzySet = _fuzzyFactory->CreateFuzzySet( 1, sd->GetEventDimension(), true );
   aConsequent = _fuzzyFactory->CreateConsequent( aFuzzySet );
   *aConsequent << scale;
   *_eventFuzzySystem + *aConsequent;

   aFuzzySet = _fuzzyFactory->CreateFuzzySet( 1, sd->GetCausalDimension(), true );
   aConsequent = _fuzzyFactory->CreateConsequent( aFuzzySet );
   *aConsequent << scale;
   *_causalFuzzySystem + *aConsequent;

   aFuzzySet = _fuzzyFactory->CreateFuzzySet( 1, sd->GetFunctionalDimension(), true );
   aConsequent = _fuzzyFactory->CreateConsequent( aFuzzySet );
   *aConsequent << scale;
   *_functionalFuzzySystem + *aConsequent;

   aFuzzySet = _fuzzyFactory->CreateFuzzySet( 1, sd->GetAffectiveDimension(), true );
   aConsequent = _fuzzyFactory->CreateConsequent( aFuzzySet );
   *aConsequent << scale;
   *_affectiveFuzzySystem + *aConsequent;
}

//***********************************************************************************************
// Class    : SentenceIteratorListeneR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//***********************************************************************************************
void SentenceIteratorListeneR::PushLinkValue( LinK* aLink, int level )
{
   long double scale;
   FuzzyseT* aFuzzySet = _fuzzyFactory->CreateFuzzySet( 1, aLink->GetLinkValue(), true );
   ConsequenT* aConsequent = _fuzzyFactory->CreateConsequent( aFuzzySet );
   scale = ((long double)1 / (long double)(level + 1));

   *_linkValueFuzzySystem + *aConsequent;
   *aConsequent << scale;
}

//***********************************************************************************************
// Class    : SentenceIteratorListeneR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//***********************************************************************************************
long double SentenceIteratorListeneR::GetLinkValue()
{
   long double value = 0;
   
   if( _linkValueFuzzySystem != 0 )
   {
      *_linkValueFuzzySystem >> value;
   }

   return Absolute( value );
}

//***********************************************************************************************
// Class    : SentenceIteratorListeneR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//***********************************************************************************************
void SentenceIteratorListeneR::EncounteredConstituent( ConstituenT* aConstituent, int level )
{
   if( aConstituent == 0 )
   {
      return;
   }

   string* theWord = aConstituent->GetForm();
   string* theInflectedWord = aConstituent->GetInflectedForm();

/*
   if( theWord != 0 )
   {
      cout << "The word is: " << theWord->c_str() << endl;
   }

   if( theInflectedWord != 0 )
   {
      cout << "The inflected word is: " << theInflectedWord->c_str() << endl;
   }

   cout << "The level is: " << level << endl;
*/

   SemanticDifferentiaL* sd = aConstituent->GetSemanticDifferential();

   if( sd != 0 )
   {
      if( _agentFuzzySystem == 0 )
      {
         _agentFuzzySystem = new CompoundconsequenT();
         _klineFuzzySystem = new CompoundconsequenT();
         _thingFuzzySystem = new CompoundconsequenT();
         _spatialFuzzySystem = new CompoundconsequenT();
         _eventFuzzySystem = new CompoundconsequenT();
         _causalFuzzySystem = new CompoundconsequenT();
         _functionalFuzzySystem = new CompoundconsequenT();
         _affectiveFuzzySystem = new CompoundconsequenT();
      }

      PushSemanticDifferential( sd, level );

/*
      cout << "The agent dimension is: " << sd->GetAgentDimension() << endl;
      cout << "The kline dimension is: " << sd->GetKLineDimension() << endl;
      cout << "The thing dimension is: " << sd->GetThingDimension() << endl;
      cout << "The spatial dimension is: " << sd->GetSpatialDimension() << endl;
      cout << "The event dimension is: " << sd->GetEventDimension() << endl;
      cout << "The causal dimension is: " << sd->GetCausalDimension() << endl;
      cout << "The functional dimension is: " << sd->GetFunctionalDimension() << endl;
      cout << "The affective dimension is: " << sd->GetAffectiveDimension() << endl;
*/
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
   if( aLink == 0 )
   {
      return;
   }

/*
   if( aLink->GetLabel() != 0 )
   {
      cout << "The link is: " << aLink->GetLabel()->c_str() << endl;
   }

   cout << "The link value is: " << aLink->GetLinkValue() << endl;
   cout << "The level is: " << level << endl;

   vector< string* > domains = aLink->GetDomains();

   for( int j=0; j<domains.size(); j++ )
   {
      string* aDomain = domains.at( j );
      if( j == 0 )
      {
         if( aDomain != 0 )
         {
            cout << "Domains are: " << aDomain->c_str();
         }
      }
      else
      {
         if( aDomain != 0 )
         {
            cout << " " << aDomain->c_str();
         }
      }
   }

   cout << endl;
*/

   if( aLink != 0 )
   {
      if( _linkValueFuzzySystem == 0 )
      {
         _linkValueFuzzySystem = new CompoundconsequenT();
      }
      PushLinkValue( aLink, level );
   }

} // end EncounteredLink

//***********************************************************************************************
// Class    : SentenceIteratorListeneR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//***********************************************************************************************
long double SentenceIteratorListeneR::Absolute( long double n )
{
   if( n < 0 )
   {
      return (-1 * n);
   }

   return n;
}
