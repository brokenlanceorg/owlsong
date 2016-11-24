//***********************************************************************************************
// File     : LinkParser.cpp
// Purpose  :
//          :
// Author   : Brandon Benham
// Date     : 5/12/05
//***********************************************************************************************

#include "LinkParser.hpp"

//***********************************************************************************************
// Class    : LinkParseR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
LinkParseR::LinkParseR()
{
   Setup();

   _parsingOptions = parse_options_create();
   _parsingOptions->panic_mode = 1;
   _parsingOptions->islands_ok = 1;
   _parsingOptions->min_null_count = 0;
   _parsingOptions->max_null_count = 50;
   _dictionary = dictionary_create( "4.0.dict", "4.0.knowledge", "4.0.constituent-knowledge", "4.0.affix" );

} // end LinkParseR default constructor

//***********************************************************************************************
// Class    : LinkParseR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
LinkParseR::~LinkParseR()
{
   //FPRINT( "~LinkParseR" );

   if( _dictionary != 0 )
   {
      //FPRINT << "freeing dictionary...";
      dictionary_delete( _dictionary );
      _dictionary = 0;
   }

   if( _parsingOptions !=0 )
   {
      //FPRINT << "freeing parsing options...";
      parse_options_delete( _parsingOptions );
      _parsingOptions = 0;
   }
} // end LinkParseR destructor

//***********************************************************************************************
// Class    : LinkParseR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void LinkParseR::Setup()
{
   _dictionary = 0;
   _parsingOptions = 0;
   _linkSentence = 0;
   _linkage = 0;
}   

//***********************************************************************************************
// Class    : LinkParseR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//***********************************************************************************************
SentencE* LinkParseR::ParseSentence( char* aSentence )
{
   SentencE* theSentence = new SentencE();
   char* diagram;
   int numberOfLinkages;
   int nullCount;

   _linkSentence = 0;
   _linkSentence = sentence_create( aSentence, _dictionary );

   numberOfLinkages = sentence_parse( _linkSentence, _parsingOptions );
   nullCount = sentence_null_count( _linkSentence );

   theSentence->SetNullLinkCount( nullCount );

   if( numberOfLinkages > 0 ) 
   {
      _linkage = linkage_create( 0, _linkSentence, _parsingOptions );

      linkage_compute_union( _linkage );

      diagram = linkage_print_diagram( _linkage );

      //cout << diagram << endl;

      BuildSentence( theSentence );

      string_delete( diagram );
      linkage_delete( _linkage );
   }

   if( _linkSentence != 0 )
   {
      sentence_delete( _linkSentence );
   }
   
   theSentence->CheckAndSetContext();

   return theSentence;
}   

//***********************************************************************************************
// Class    : LinkParseR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//***********************************************************************************************
SentencE* LinkParseR::ParseSentence( string* aString )
{
   return ParseSentence( (char*)aString->c_str() );
}

//***********************************************************************************************
// Class    : LinkParseR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//***********************************************************************************************
void LinkParseR::BuildSentence( SentencE* aSentence )
{
   int numberOfWords = linkage_get_num_words( _linkage );
   string* theInflectedWord;
   string* theWord;
   ConstituenT* aConstituent = 0;
   SemanticDifferentialDataSourcE* dataSource = SemanticDifferentialDataSourcE::GetInstance();

   for( int i=0; i<numberOfWords; i++ )
   {
      theInflectedWord = new string( linkage_get_word( _linkage, i ) );
      theWord = new string( sentence_get_word( _linkSentence, i ) );

      aConstituent = new ConstituenT( i, theWord, theInflectedWord );
      aSentence->AddConstituent( aConstituent );
      aConstituent->SetSemanticDifferential( dataSource->GetSemanticDifferential( theWord ) );
   }

   int numberOfLinks = linkage_get_num_links( _linkage );

   for( int i=0; i<numberOfLinks; i++ )
   {
      string* label = new string( linkage_get_link_label( _linkage, i ) );
      // Not sure if we can really use this piece of data:
      // int length = linkage_get_link_length( _linkage, i );
      int left = linkage_get_link_lword( _linkage, i );
      int right = linkage_get_link_rword( _linkage, i );

      int numberOfDomains = linkage_get_link_num_domains( _linkage, i );
      char** domainArray = linkage_get_link_domain_names( _linkage, i );
      vector< string* > domains;

      for( int j=0; j<numberOfDomains; j++ )
      {
         domains.push_back( new string( domainArray[j] ) );
      }

      LinK* aLink = new LinK( label, left, right, domains );
      aLink->SetLinkValue( dataSource->GetLinkValue( label ) );

      aSentence->AddLink( aLink );
   }
}
