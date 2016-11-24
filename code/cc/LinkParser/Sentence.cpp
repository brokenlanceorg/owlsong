//***********************************************************************************************
// File     : Sentence.cpp
// Purpose  :
//          :
// Author   : Brandon Benham
// Date     : 5/12/05
//***********************************************************************************************

#include "Sentence.hpp"

using namespace std;

//***********************************************************************************************
// Class    : SentencE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
SentencE::SentencE()
{
   Setup();
   _outputFileName = new string( getenv( "HOME" ) );
   *_outputFileName += "/Sentence.txt";
} // end SentencE default constructor

//***********************************************************************************************
// Class    : SentencE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
SentencE::~SentencE()
{
   //FPRINT( "~SentencE" );
   for( int i=0; i<_constituents.size(); i++ )
   {
      //FPRINT << "1";
      delete _constituents.at( i );
   }
      //FPRINT << "2";
   _constituents.clear();

   for( int i=0; i<_links.size(); i++ )
   {
      //FPRINT << "3";
      delete _links.at( i );
   }
      //FPRINT << "4";
   _links.clear();

   if( _outputFileName != 0 )
   {
      //FPRINT << "5";
      delete _outputFileName;
      _outputFileName = 0;
   }

   if( _semanticDifferential != 0 )
   {
      //FPRINT << "6";
      delete _semanticDifferential;
      _semanticDifferential = 0;
   }

} // end SentencE destructor

//***********************************************************************************************
// Class    : SentencE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void SentencE::Setup()
{
   _nullLinkCount = 0;
   _context = 0;
   _linkValue = 0;
   _semanticDifferential = 0;
   _outputFileName = 0;
} // end Setup

//***********************************************************************************************
// Class    : SentencE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//***********************************************************************************************
void SentencE::Reset()
{
   for( int i=0; i<_links.size(); i++ )
   {
      LinK* aLink = _links.at( i );
      aLink->ClearVisited();
   }
}

//***********************************************************************************************
// Class    : SentencE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//***********************************************************************************************
void SentencE::AddConstituent( ConstituenT* aConstituent )
{
   _constituents.push_back( aConstituent );
} // end AddConstituent

//***********************************************************************************************
// Class    : SentencE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
// Notes    : The algorithm should be as follows:
//          :
//          :  1) Iterate through all constituents
//          :  2) If the constituent functioned as a verb then:
//          :     3) Iterate through all the links and:
//          :     4) find the max domain count for this constituent across links
//          :  5) If the max count for this constituent is the least so far then
//          :     6) Set this constituent as the context
//          :  7) If none of these match, then use the first gerund
//          :  8) Else use the first noun
//          :  
//          :  
//***********************************************************************************************
void SentencE::CheckAndSetContext()
{
   int leastContext = 2000;

   for( int i=0; i<_constituents.size(); i++ )
   {
      ConstituenT* aConstituent = _constituents.at( i );
      string* inflected = aConstituent->GetInflectedForm();

      if( inflected->find( ".v" ) != string::npos )
      {
         int maxDomain = -2000;
         vector< LinkIteM* > leftLinks = aConstituent->GetLeftLinks();

         for( int j=0; j<leftLinks.size(); j++ )
         {
            LinK* aLink = (LinK*)leftLinks.at( j );

            if( aLink->GetNumberOfDomains() > maxDomain )
            {
               maxDomain = aLink->GetNumberOfDomains();
            }
         }
   
         vector< LinkIteM* > rightLinks = aConstituent->GetRightLinks();

         for( int j=0; j<rightLinks.size(); j++ )
         {
            LinK* aLink = (LinK*)rightLinks.at( j );

            if( aLink->GetNumberOfDomains() > maxDomain )
            {
               maxDomain = aLink->GetNumberOfDomains();
            }
         }

         if( maxDomain < leastContext )
         {
            leastContext = maxDomain;
            _context = aConstituent;
         }

      } // end if verb 

   } // end constituent for

   if( _context == 0 )
   {
      for( int i=0; i<_constituents.size(); i++ )
      {
         ConstituenT* aConstituent = _constituents.at( i );
         string* inflected = aConstituent->GetInflectedForm();
   
         if( inflected->find( ".g" ) != string::npos )
         {
            _context = aConstituent;
            break;
         }
      }
   }

   if( _context == 0 )
   {
      for( int i=0; i<_constituents.size(); i++ )
      {
         ConstituenT* aConstituent = _constituents.at( i );
         string* inflected = aConstituent->GetInflectedForm();
   
         if( inflected->find( ".n" ) != string::npos )
         {
            _context = aConstituent;
            break;
         }
      }
   }

}

//***********************************************************************************************
// Class    : SentencE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
// Notes    :  We assume that all the constituents have already been added.
//***********************************************************************************************
void SentencE::AddLink( LinK* aLink )
{
   _links.push_back( aLink );

   for( int i=0; i<_constituents.size(); i++ )
   {
      ConstituenT* aConstituent = _constituents.at( i );

      if( aConstituent->GetOrdinal() == aLink->GetLeftLink() )
      {
         aConstituent->AddLeftLink( aLink );
         aLink->SetLeftConstituent( aConstituent );
      }

      if( aConstituent->GetOrdinal() == aLink->GetRightLink() )
      {
         aConstituent->AddRightLink( aLink );
         aLink->SetRightConstituent( aConstituent );
      }
   }

} // end AddLink

//***********************************************************************************************
// Class    : SentencE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
// Notes    :  
//***********************************************************************************************
void SentencE::WriteToDisk()
{
   // if the context is null, then this sentence hasn't been parsed.
   if( _context == 0 )
   {
      return;
   }

   fstream* output = new fstream( _outputFileName->c_str(), ios::out | ios::trunc );

   *output << _context->GetForm()->c_str() << " " << GetLinkValue();

   ConstituenT* aConstituent = 0;

   for( int i=0; i<_constituents.size(); i++ )
   {
      aConstituent = _constituents.at( i );
      *output << "\n" << aConstituent->GetForm()->c_str() << " " << aConstituent->GetInflectedForm()->c_str();

      vector< LinkIteM* > leftLinks = aConstituent->GetLeftLinks();

      for( int j=0; j<leftLinks.size(); j++ )
      {
         //if( j == 0 )
         //{
            *output << " LEFT_LINKS";
         //}
         LinK* aLink = (LinK*)leftLinks.at( j );
         *output << " " << aLink->GetLabel()->c_str();

         vector< string* > domains = aLink->GetDomains();
         for( int k=0; k<domains.size(); k++ )
         {
            string* domain = domains.at( k );
            if( k == 0 )
            {
               *output << " DOMAIN_LINKS ";
               *output << domain->c_str();
            }
            else
            {
               *output << "," << domain->c_str();
            }
         }

         ConstituenT* anotherConstituent = (ConstituenT*)aLink->GetRightConstituent();
         *output << " " << anotherConstituent->GetForm()->c_str();
      }

/** don't really need the duplicate data
      vector< LinkIteM* > rightLinks = aConstituent->GetRightLinks();

      for( int j=0; j<rightLinks.size(); j++ )
      {
         //if( j == 0 )
         //{
            *output << " RIGHT_LINKS";
         //}
         LinK* aLink = (LinK*)rightLinks.at( j );
         *output << " " << aLink->GetLabel()->c_str();

         vector< string* > domains = aLink->GetDomains();
         for( int k=0; k<domains.size(); k++ )
         {
            string* domain = domains.at( k );
            if( k == 0 )
            {
               *output << " DOMAIN_LINKS ";
               *output << domain->c_str();
            }
            else
            {
               *output << "," << domain->c_str();
            }
         }

         ConstituenT* anotherConstituent = (ConstituenT*)aLink->GetLeftConstituent();
         *output << " " << anotherConstituent->GetForm()->c_str();
      }
*/
   }

   output->close();

   delete output;
}

//***********************************************************************************************
// Class    : SentencE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
// Notes    :  
//***********************************************************************************************
ConstituenT* SentencE::GetConstituent( int ord )
{
   ConstituenT* constituent = 0;

   for( int i=0; i<_constituents.size(); i++ )
   {
      constituent = _constituents.at( i );
      if( constituent->GetOrdinal() == ord )
      {
         break;
      }
   }

   return constituent;
}
