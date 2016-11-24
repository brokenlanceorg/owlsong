//***********************************************************************************************
// File     : SentenceIterator.cpp
// Purpose  :
//          :
// Author   : Brandon Benham
// Date     : 5/12/05
//***********************************************************************************************

#include "SentenceIterator.hpp"

//***********************************************************************************************
// Class    : SentenceIteratoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
SentenceIteratoR::SentenceIteratoR()
{
   Setup();
} // end SentenceIteratoR default constructor

//***********************************************************************************************
// Class    : SentenceIteratoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//***********************************************************************************************
SentenceIteratoR::SentenceIteratoR( SentencE* aSentence )
{
   Setup();
   _sentence = aSentence;
} // end SentenceIteratoR default constructor

//***********************************************************************************************
// Class    : SentenceIteratoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
SentenceIteratoR::~SentenceIteratoR()
{
   //FPRINT( "~SentenceIteratoR" );

   if( _sentenceListener != 0 )
   {
      //FPRINT << "deleteing listener...";
      delete _sentenceListener;
      _sentenceListener = 0;
   }
} // end SentenceIteratoR destructor

//***********************************************************************************************
// Class    : SentenceIteratoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void SentenceIteratoR::Setup()
{
   _sentence = 0;
   _sentenceListener = 0;
} // end Setup

//***********************************************************************************************
// Class    : SentenceIteratoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
// Notes    : We will use an iterface to call back on an object as we iterate the sentence.
//          : 
//          : 
//          : 
//          : 
//***********************************************************************************************
void SentenceIteratoR::Iterate()
{
   ConstituenT* theContext = _sentence->GetContext();

   if( theContext != 0 )
   {
      _sentence->Reset();
      Iterate( theContext, 0 );
      _sentence->SetSemanticDifferential( _sentenceListener->GetSemanticDifferential() );
      _sentence->SetLinkValue( _sentenceListener->GetLinkValue() );
   }

} // end Iterate

//***********************************************************************************************
// Class    : SentenceIteratoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
// Notes    : We will use an iterface to call back on an object as we iterate the sentence.
//          : 
//          : 
//          : 
//          : 
//***********************************************************************************************
void SentenceIteratoR::Iterate( ConstituenT* aConstituent, int level )
{
//FPRINT( "Iterate" );

   if( aConstituent == 0 )
   {
      return;
   }

   if( _sentenceListener != 0 )
   {
      _sentenceListener->EncounteredConstituent( aConstituent, level );
   }

   vector< LinkIteM* > leftLinks = aConstituent->GetLeftLinks();

   for( int i=0; i<leftLinks.size(); i++ )
   {
      LinK* aLink = (LinK*)leftLinks.at( i );

      if( aLink->HasBeenVisited() )
      {
         continue;
      }

      aLink->SetVisited();

      if( _sentenceListener != 0 )
      {
         _sentenceListener->EncounteredLink( aLink, level );
      }

      ConstituenT* rightConstituent = (ConstituenT*)aLink->GetRightConstituent();

      Iterate( rightConstituent, level + 1 );
   }

   vector< LinkIteM* > rightLinks = aConstituent->GetRightLinks();

   for( int i=0; i<rightLinks.size(); i++ )
   {
      LinK* aLink = (LinK*)rightLinks.at( i );

      if( aLink->HasBeenVisited() )
      {
         continue;
      }

      aLink->SetVisited();

      if( _sentenceListener != 0 )
      {
         _sentenceListener->EncounteredLink( aLink, level );
      }

      ConstituenT* leftConstituent = (ConstituenT*)aLink->GetLeftConstituent();

      Iterate( leftConstituent, level + 1 );
   }

}
