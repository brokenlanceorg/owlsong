//***********************************************************************************************
// File     : SentenceGenerator.cpp
// Purpose  :
//          :
// Author   : Brandon Benham
// Date     : 7/11/05
//***********************************************************************************************

#include "SentenceGenerator.hpp"

//***********************************************************************************************
// Class    : SentenceGeneratoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
SentenceGeneratoR::SentenceGeneratoR()
{
   Setup();
} // end SentenceGeneratoR default constructor

//***********************************************************************************************
// Class    : SentenceGeneratoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
SentenceGeneratoR::~SentenceGeneratoR()
{
   if( _kernelSentence != 0 )
   {
      delete _kernelSentence;
   }

   _contexts.clear();
} // end SentenceGeneratoR destructor

//***********************************************************************************************
// Class    : SentenceGeneratoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void SentenceGeneratoR::Setup()
{
   _kernelSentence = 0;
   _genePoolSize = 0;
} // end Setup

//***********************************************************************************************
// Class    : SentenceGeneratoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : GetGeneratedSentence
// Purpose  : 
//***********************************************************************************************
string* SentenceGeneratoR::GetGeneratedSentence( vector< string* > kernel )
{
   string* theSentence = 0;
   vector< ConstituenT* > vec = SemanticDifferentialDataSourcE::GetInstance()->GetConstituents();
   _genePoolSize = vec.size();
   //cout << "the size of constituents is: " << _genePoolSize << endl;
   
   for( int i=0; i<_genePoolSize; i++ )
   {
      ConstituenT* aConstituent = vec.at( i );
      bool added = false;
      
      //cout << "A constituent form is: " << *(aConstituent->GetForm()) << endl;
      
      vector< ConstituentIteM* > items = aConstituent->GetConstituentItems();
      for( int j=0; j<items.size(); j++ )
      {
         ConstituentIteM* anItem = items.at( j );
         //cout << "Item link type: " << *(anItem->GetLinkType()) << endl;
         //cout << "Item context: " << *(anItem->GetContext()) << endl;
         //cout << "Item linked constituent: " << *(anItem->GetLinkedConstituent()) << endl;
         //cout << "Item constituent: " << *(anItem->GetConstituent()) << endl;
         //cout << "Item inflected form: " << *(anItem->GetInflectedForm()) << endl;
         //cout << "Item linkage: " << anItem->GetLinkage() << endl;                                            
         //cout << "Item serial number: " << anItem->GetSerialNumber() << endl;                                            

         if( !added && aConstituent->GetForm() != 0 &&
             anItem->GetContext() != 0 &&
             (*(aConstituent->GetForm()) == *(anItem->GetContext())) )
         {
            _contexts.push_back( aConstituent );
            added = true;
         }

/** for display purposes
         vector< string* > domains = anItem->GetDomains();
         for( int k=0; k<domains.size(); k++ )
         {
            cout << "Item domain: " << *(domains.at( k )) << " ";
         }
         cout << endl;
*/
      }
   }

   // temporary:
/**
   for( int i=0; i<_contexts.size(); i++ )
   {
      ConstituenT* aConstituent = _contexts.at( i );
      cout << "A context: " << *(aConstituent->GetForm()) << endl;

      vector< ConstituentIteM* > items = aConstituent->GetConstituentItems();
      for( int j=0; j<items.size(); j++ )
      {
         ConstituentIteM* anItem = items.at( j );
         cout << "serial number: " << anItem->GetSerialNumber() << endl;                                            
      }
   }
*/

   // find the kernel sentence, right now, only using the last
   char* kernelSentence = 0;
   for( int i=0; i<kernel.size(); i++ )
   {
      kernelSentence = (char*)(kernel.at( i ))->c_str();
   }

   // Get the sentence object for the kernel sentence:
   LinkParseR* aParser = new LinkParseR();
   _kernelSentence = aParser->ParseSentence( kernelSentence );
   SentenceIteratoR* anIterator = new SentenceIteratoR( _kernelSentence );
   anIterator->SetListener( new SentenceIteratorListeneR() );
   anIterator->Iterate();
   SemanticDifferentialDataSourcE::GetInstance()->SetLinkParser( (void*)aParser );
   SemanticDifferentialDataSourcE::GetInstance()->SetKernelSentence( _kernelSentence );
   SemanticDifferentialDataSourcE::GetInstance()->SetContexts( _contexts );

   theSentence = PerformGeneticAlgorithm();

   delete anIterator;
   delete aParser;

   return theSentence;
} // end GetGeneratedSentence

//***********************************************************************************************
// Class    : SentenceGeneratoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : PerformGeneticAlgorithm
// Purpose  : 
//***********************************************************************************************
string* SentenceGeneratoR::PerformGeneticAlgorithm()
{
   FPRINT( "PerformGA" );

   string* sentence = 0;
   int populationSize = 30;
   int numberOfGenerations = 30;
   int genes = 41;

   // Setup the GeneticAlgorithm
   GeneticAlgorithM* theGA = new GeneticAlgorithM();
   SentenceGeneratorIndividuaL* theIndividual = 0;
   SelectionStrategY* theStrategy = (SelectionStrategY*)(new RouletteWheelSelectionStrategY());
   theGA->SetSelectionStrategy( theStrategy );
   theGA->SetMaximumFitness( 90000000 );
   theGA->SetMaxNumberOfGenerations( numberOfGenerations );
   theGA->SetPopulationSize( populationSize );

   // The random number generator is a tricky object. If you construct a VectoR
   // or MatriX object (or any other that uses the RandoM class), then you should
   // store up a collection of random numbers, otherwise the c-style (and hence static
   // from our objects perspective) function srand gets called again and again quite quickly.
   RandoM* randomVariable = new RandoM( 0, 1 );
   randomVariable->CalcSeed();
   VectoR* aRandomVector = new VectoR( populationSize * genes );
   int pos = 0;

   for( int i=0; i<(populationSize * genes); i++ )
   {
      aRandomVector->pVariables[i] = randomVariable->GetRandomLngDouble();
   }

   for( int i=0; i<populationSize; i++ )
   {
      theIndividual = new SentenceGeneratorIndividuaL();
      VectoR* aRandomGenome = new VectoR( genes );

      // get a random genome
      for( int j=0; j<(genes - 1); j+=2 )
      {
         aRandomGenome->pVariables[j] = aRandomVector->pVariables[ pos++ ];
         aRandomGenome->pVariables[j + 1] = ((_genePoolSize - 1) * aRandomVector->pVariables[ pos++ ] );
      }

      aRandomGenome->pVariables[ genes - 1 ] = aRandomVector->pVariables[ pos++ ];

      theIndividual->GetChromosome()->SetGenome( aRandomGenome );

      theGA->AddIndividual( (IndividuaL*)theIndividual );
   }

   FPRINT << "About to evolve the GA...";

   theIndividual = (SentenceGeneratorIndividuaL*)theGA->Evolve();

   if( theIndividual != 0 )
   {
      FPRINT << "The final sentence is: ";
      FPRINT << theIndividual->GetSentence().c_str();
   }
   else
   {
      FPRINT << "The final sentence or individual is NULL!!!!!";
   }

   cout << "The sentence is: " << theIndividual->GetSentence() << endl;

   delete theGA;

   return sentence;
} // end Setup
