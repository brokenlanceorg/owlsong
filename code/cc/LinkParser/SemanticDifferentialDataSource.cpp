//***********************************************************************************************
// File     : SemanticDifferentialDataSource.cpp
// Purpose  : This class will also contain the linkage data (mappings from links to numerals).
//          :
// Author   : Brandon Benham
// Date     : 5/12/05
//***********************************************************************************************

#include "SemanticDifferentialDataSource.hpp"

SemanticDifferentialDataSourcE* SemanticDifferentialDataSourcE::_instance = 0;

//***********************************************************************************************
// Class    : SemanticDifferentialDataSourcE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
SemanticDifferentialDataSourcE::SemanticDifferentialDataSourcE()
{
   Setup();
} // end SemanticDifferentialDataSourcE default constructor

//***********************************************************************************************
// Class    : SemanticDifferentialDataSourcE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
SemanticDifferentialDataSourcE::~SemanticDifferentialDataSourcE()
{
   //FPRINT( "~SemanticDifferentialDataSourcE" );
   StoreLinkValues();

   if( _dataSource != 0 )
   {
      delete _dataSource;
      _dataSource = 0;
   }

   if( _linkDataSource != 0 )
   {
      delete _linkDataSource;
      _linkDataSource = 0;
   }

   for( int i=0; i<_constituents.size(); i++ )
   {
      delete _constituents.at( i );
   }
   _constituents.clear();

   for( int i=0; i<_links.size(); i++ )
   {
      delete _links.at( i );
   }
   _links.clear();

   for( int i=0; i<_semanticDifferentials.size(); i++ )
   {
      delete _semanticDifferentials.at( i );
   }
   _semanticDifferentials.clear();

   for( int i=0; i<_constituentObjects.size(); i++ )
   {
      delete _constituentObjects.at( i );
   }
   _constituentObjects.clear();

} // end SemanticDifferentialDataSourcE destructor

//***********************************************************************************************
// Class    : SemanticDifferentialDataSourcE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void SemanticDifferentialDataSourcE::Setup()
{
   _dataSource = new string( getenv( "HOME" ) );
   *_dataSource += "/SemanticDifferential.txt";
   ReadDataSource();

   _linkDataSource = new string( getenv( "HOME" ) );
   *_linkDataSource += "/LinkData.txt";
   ReadLinkDataSource();
} // end Setup

//***********************************************************************************************
// Class    : SemanticDifferentialDataSourcE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//***********************************************************************************************
SemanticDifferentialDataSourcE* SemanticDifferentialDataSourcE::GetInstance()
{
   if( _instance == 0 )
   {
      _instance = new SemanticDifferentialDataSourcE();
   }

   return _instance;

} // end GetInstance

//***********************************************************************************************
// Class    : SemanticDifferentialDataSourcE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//***********************************************************************************************
int SemanticDifferentialDataSourcE::FindConstituent( char* constituent )
{
   string theConstituent( constituent );
   int position = -1;

   for( int i=0; i<_constituents.size(); i++ )
   {
      if( theConstituent == *_constituents.at( i ) )
      {
         position = i;
         break;
      }
   }

   return position;
}

//***********************************************************************************************
// Class    : SemanticDifferentialDataSourcE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//***********************************************************************************************
void SemanticDifferentialDataSourcE::ReadLinkDataSource()
{
   FilereadeR* theFile = new FilereadeR( (char*)_linkDataSource->c_str() );
   char* theWord = 0;

   theFile->Rewind();

   while( (theWord = theFile->GetNextWord()) != 0 )
   {
      _links.push_back( new string( theWord ) );
      theWord = theFile->GetNextWord();
      _linkValues.push_back( atoi( theWord ) );
   }

   delete theFile;
}

//***********************************************************************************************
// Class    : SemanticDifferentialDataSourcE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//***********************************************************************************************
void SemanticDifferentialDataSourcE::ReadDataSource()
{
   //FPRINT( "ReadDataSource" );

   FilereadeR* theFile = new FilereadeR( (char*)_dataSource->c_str() );
   char* theWord = 0;
   int position = 0;
   int dimension = 0;
   long double dimensionValue = 0;
   SemanticDifferentiaL* sd = 0;


   theFile->Rewind();

   while( (theWord = theFile->GetNextWord()) != 0 )
   {
      if( (position++ % 9) == 0 )
      {
         _constituents.push_back( new string( theWord ) );
         sd = new SemanticDifferentiaL();
         _semanticDifferentials.push_back( sd );
         dimension = 0;
      }
      else
      {
         dimensionValue = strtold( theWord, (char**)0 );
         switch( dimension++ )
         {
            case 0 :
               sd->SetAgentDimension( dimensionValue );
            break;

            case 1 :
               sd->SetKLineDimension( dimensionValue );
            break;

            case 2 :
               sd->SetThingDimension( dimensionValue );
            break;

            case 3 :
               sd->SetSpatialDimension( dimensionValue );
            break;

            case 4 :
               sd->SetEventDimension( dimensionValue );
            break;

            case 5 :
               sd->SetCausalDimension( dimensionValue );
            break;

            case 6 :
               sd->SetFunctionalDimension( dimensionValue );
            break;

            case 7 :
               sd->SetAffectiveDimension( dimensionValue );
            break;
         }
      }
   }

   delete theFile;
}

//***********************************************************************************************
// Class    : SemanticDifferentialDataSourcE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//***********************************************************************************************
SemanticDifferentiaL* SemanticDifferentialDataSourcE::FindSemanticDifferential( char* constituent )
{
   SemanticDifferentiaL* semanticDifferential = 0;

   int position = FindConstituent( constituent );

   if( position != -1 )
   {
      semanticDifferential = _semanticDifferentials.at( position );
   }

   return semanticDifferential;
}

//***********************************************************************************************
// Class    : SemanticDifferentialDataSourcE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//***********************************************************************************************
SemanticDifferentiaL* SemanticDifferentialDataSourcE::GetSemanticDifferential( char* constituent )
{
   SemanticDifferentiaL* semanticDifferential = FindSemanticDifferential( constituent );

   return semanticDifferential;
}

//***********************************************************************************************
// Class    : SemanticDifferentialDataSourcE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//***********************************************************************************************
int SemanticDifferentialDataSourcE::GetLinkValue( char* aLink )
{
   int value = -1;
   int last = -1;
   string* theLink = new string( aLink );

   for( int i=0; i<_links.size(); i++ )
   {
      if( *(_links.at( i )) == *theLink )
      {
         value = _linkValues.at( i );
      }
      last = _linkValues.at( i );
   }

   // not found then
   if( value == -1 )
   {
      value = last + 1;
      _links.push_back( theLink );
      _linkValues.push_back( value );
   }

   return value;
}

//***********************************************************************************************
// Class    : SemanticDifferentialDataSourcE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//***********************************************************************************************
void SemanticDifferentialDataSourcE::StoreLinkValues()
{
   fstream outputFile( _linkDataSource->c_str(), ios::out | ios::trunc );

   for( int i=0; i<_links.size(); i++ )
   {
      outputFile << (_links.at( i ))->c_str() << " " << _linkValues.at( i ) << endl;
   }

   outputFile << endl;

   outputFile.close();
}

//***********************************************************************************************
// Class    : SemanticDifferentialDataSourcE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//***********************************************************************************************
void SemanticDifferentialDataSourcE::ReadConstituentData( int size )
{
   string* baseName = new string( getenv( "HOME" ) );
   *baseName += "/constituent_";

   // Each iteration gives a new initial chromosome or entry into the gene pool
   for( int i=0; i<size; i++ )
   {
      string* fileName = new string( *baseName );
      ostringstream streamer;
      streamer << i;
      *fileName += streamer.str();
      *fileName += ".txt";

      FilereadeR* theFile = new FilereadeR( (char*)fileName->c_str() );
      char* theWord = 0;
   
      theFile->Rewind();

      // First word is the form of the constituent:
      theWord = theFile->GetNextWord();

      ConstituenT* aConstituent = new ConstituenT( theWord );
      ConstituentIteM* anItem = 0;

      while( (theWord = theFile->GetNextWord()) != 0 )
      {
         if( strcmp( theWord, "LINK_TYPE" ) == 0 )
         {
            theWord = theFile->GetNextWord();
            anItem = new ConstituentIteM();
            anItem->SetLinkType( theWord );
            aConstituent->AddConstituentItem( anItem );
         }
         else if( strcmp( theWord, "DOMAINS" ) == 0 )
         {
            theWord = theFile->GetNextWord();
            anItem->SetDomains( theWord );
         }
         else if( strcmp( theWord, "CONTEXT" ) == 0 )
         {
            theWord = theFile->GetNextWord();
            anItem->SetContext( theWord );
         }
         else if( strcmp( theWord, "LINKED_CONSTITUENT" ) == 0 )
         {
            theWord = theFile->GetNextWord();
            anItem->SetLinkedConstituent( theWord );
         }
         else if( strcmp( theWord, "INFLECTED" ) == 0 )
         {
            theWord = theFile->GetNextWord();
            anItem->SetInflectedForm( theWord );
         }
         else if( strcmp( theWord, "LINKAGE" ) == 0 )
         {
            theWord = theFile->GetNextWord();
            anItem->SetLinkage( strtold( theWord, (char**)0 ) );
         }
         else if( strcmp( theWord, "CONSTITUENT" ) == 0 )
         {
            theWord = theFile->GetNextWord();
            anItem->SetConstituent( theWord );
         }
         else if( strcmp( theWord, "SERIAL_NUMBER" ) == 0 )
         {
            theWord = theFile->GetNextWord();
            anItem->SetSerialNumber( atol( theWord ) );
         }
         else 
         {
         }
      }

      _constituentObjects.push_back( aConstituent );

      delete theFile;
      delete fileName;
   }

   delete baseName;
}
