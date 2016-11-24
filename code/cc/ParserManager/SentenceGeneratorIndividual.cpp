//***********************************************************************************************
// File     : SentenceGeneratorIndividual.cpp
// Purpose  :
//          :
// Author   : Brandon Benham
// Date     : 7/14/05
//***********************************************************************************************

#include "SentenceGeneratorIndividual.hpp"

//***********************************************************************************************
// Class    : SentenceGeneratorIndividuaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//          : The default constructor has the responsibility to do the following:
//          :    (1) Create the necessary chromosome class
//          :    (2) Give the chromosome a reasonable random genome
//          :    (3) Set the proper mutation strategy on the chromosome
//          :    (4) Set the crossover strategy
// Notes    : In this case, we are going to forego requirement (2).
//          : Rather, we will leave that up to the calling class because for this 
//          : individual, those operations will be rather expensive.
//          : 
//          : In fact, all implementations could work this way since the initial random
//          : genome is used only once.
//          : 
//***********************************************************************************************
SentenceGeneratorIndividuaL::SentenceGeneratorIndividuaL()
{
   Setup();
   // (1):
   SetChromosome( (ChromosomE*)(new ValueEncodedChromosomE()) );

   // (2):
   // The chromosome will be 2 terms:
   //   the learning rate which can range from very small to 10 or higher, 
   //   and the momentum which can vary from 0.1 to unknown.
/** This will be done by the calling class
   VectoR* theRandomGenome = new VectoR( 2 );

   // We will just put the random numbers in [0, 1]:
   //theRandomGenome->Fill_Rand();
   theRandomGenome->pVariables[ 0 ] = 0.04;
   theRandomGenome->pVariables[ 1 ] = 0.4;

   GetChromosome()->SetGenome( theRandomGenome );
*/

   // (3):
   ValueEncodedMutationStrategY* strat = new ValueEncodedMutationStrategY();
   strat->SetMutationProbability( 0.5 );
   GetChromosome()->SetMutationStrategy( strat );

   // (4):
   CrossoverStrategY* theCrossover = (CrossoverStrategY*)new MultiplePointCrossoverStrategY();
   SetCrossoverStrategy( theCrossover );
} // end SentenceGeneratorIndividuaL default constructor

//***********************************************************************************************
// Class    : SentenceGeneratorIndividuaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
SentenceGeneratorIndividuaL::~SentenceGeneratorIndividuaL()
{
} // end SentenceGeneratorIndividuaL destructor

//***********************************************************************************************
// Class    : SentenceGeneratorIndividuaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void SentenceGeneratorIndividuaL::Setup()
{
   _myFitness = -1;
} // end Setup

//***********************************************************************************************
// Class    : SentenceGeneratorIndividuaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Clone
// Purpose  : 
//***********************************************************************************************
IndividuaL* SentenceGeneratorIndividuaL::Clone()
{
   return (new SentenceGeneratorIndividuaL());
} // end Clone

//***********************************************************************************************
// Class    : SentenceGeneratorIndividuaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : GetSentence
// Purpose  : 
//***********************************************************************************************
string SentenceGeneratorIndividuaL::GetSentence()
{
   VectoR* theGenome = GetChromosome()->GetGenome();
   return GetCharSentence( theGenome );
}

//***********************************************************************************************
// Class    : SentenceGeneratorIndividuaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : GetCharSentence
// Purpose  : 
//***********************************************************************************************
string SentenceGeneratorIndividuaL::GetCharSentence( VectoR* genome )
{
   //FPRINT( "GetCharSentence" );

   SemanticDifferentialDataSourcE* semanticDataSource = SemanticDifferentialDataSourcE::GetInstance();
   vector< ConstituenT* > constituents = SemanticDifferentialDataSourcE::GetInstance()->GetConstituents();
   string sentence;
   bool addSpace = false;

   //FPRINT << "Got static objects, about to loop..";

   for( int i=0; i<(genome->cnRows - 1); i+=2 )
   {
      //FPRINT << "i is: ";
      //FPRINT << i;

      int word = 0;
      if( genome->pVariables[i] >= 0.5 )
      {
         word = (int)genome->pVariables[i + 1];
         if( word < 0 )
         {
            word *= -1;
         }
         word %= (constituents.size() - 1);

      //FPRINT << "word is:";
      //FPRINT << word;

         if( addSpace )
         {
            sentence += " ";
         }
         else
         {
            addSpace = true;
         }
         sentence += *(constituents.at( word )->GetForm());
      //FPRINT << "sentence is now:";
      //FPRINT << (char*)sentence.c_str();
      }
   }

   long double v = genome->pVariables[ genome->cnRows - 1 ];
      //FPRINT << "v is:";
      //FPRINT << v;

   // consider this a ?
   if( v <= 0.2 )
   {
      sentence += "?";
   }
   // consider this a !
   else if( (v > 0.2) && (v <= 0.4) )
   {
      sentence += "!";
   }
   // consider this a .
   else
   {
      sentence += ".";
   }

      //FPRINT << "sentence is:";
      //FPRINT << (char*)sentence.c_str();

   return sentence;
} // end GetCharSentence

//***********************************************************************************************
// Class    : SentenceGeneratorIndividuaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : GetConstituent
// Purpose  : 
//          : 
//***********************************************************************************************
ConstituenT* SentenceGeneratorIndividuaL::GetConstituent( string* word )
{
   ConstituenT* constituent = 0;
   ConstituenT* current = 0;

   vector< ConstituenT* > constituents = SemanticDifferentialDataSourcE::GetInstance()->GetConstituents();
   for( int i=0; i<constituents.size(); i++ )
   {
      current = constituents.at( i );
      if( *(current->GetForm()) == *word )
      {
         constituent = current;
         break;
      }
   }

   return constituent;
}

//***********************************************************************************************
// Class    : SentenceGeneratorIndividuaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : GetContext
// Purpose  : 
//          : 
//***********************************************************************************************
ConstituenT* SentenceGeneratorIndividuaL::GetContext( string* word )
{
   ConstituenT* constituent = 0;
   ConstituenT* current = 0;

   vector< ConstituenT* > contexts = SemanticDifferentialDataSourcE::GetInstance()->GetContexts();
   for( int i=0; i<contexts.size(); i++ )
   {
      current = contexts.at( i );

      if( *(current->GetForm()) == *word )
      {
         constituent = current;
         break;
      }
   }

   return constituent;
}

//***********************************************************************************************
// Class    : SentenceGeneratorIndividuaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : GetContext
// Purpose  : 
//          : 
//***********************************************************************************************
string* SentenceGeneratorIndividuaL::GetContext( long serial )
{
   string* context = 0;
   ConstituenT* current = 0;

   vector< ConstituenT* > constituents = SemanticDifferentialDataSourcE::GetInstance()->GetConstituents();
   for( int i=0; i<constituents.size(); i++ )
   {
      current = constituents.at( i );
      vector< ConstituentIteM* > items = current->GetConstituentItems();

      for( int j=0; j<items.size(); j++ )
      {
         ConstituentIteM* item = items.at( j );
         if( item->GetSerialNumber() == serial )
         {
            context = item->GetContext();
            break;
         }
      }
   }

   return context;
}

//***********************************************************************************************
// Class    : SentenceGeneratorIndividuaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : GetContextSerialNumbers
// Purpose  : 
//          : 
//***********************************************************************************************
vector< long > SentenceGeneratorIndividuaL::GetContextSerialNumbers( ConstituenT* constituent )
{
   vector< long > serials;

   vector< ConstituentIteM* > constituents = constituent->GetConstituentItems();
   string* form = constituent->GetForm();

   for( int i=0; i<constituents.size(); i++ )
   {
      ConstituentIteM* item = constituents.at( i );
      if( *form == *(item->GetContext()) )
      {
         serials.push_back( item->GetSerialNumber() );
      }
   }

   return serials;
}

//***********************************************************************************************
// Class    : SentenceGeneratorIndividuaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : GetNeuralNetwork
// Purpose  : 
//          : 
//***********************************************************************************************
LargeNetworK* SentenceGeneratorIndividuaL::GetNeuralNetwork( string* context )
{
   LargeNetworK* network = 0;
   ifstream inputStream;

   inputStream.open( context->c_str(), ifstream::in );
   if( !inputStream.bad() )
   {
      network = new LargeNetworK( (char*)context->c_str(), 10, 25, 8, 8, 60 );
   }
   inputStream.close();

   return network;
}

//***********************************************************************************************
// Class    : SentenceGeneratorIndividuaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : EvaluateFitness
// Purpose  : 
// Notes    : The genome will be considered as the following:
//          : 41 genes long ==> 20 words, plus 20 switches, and ending punctuation.
//          : 
//          : This may seem like a huge monolithic method (because it is), but we can't
//          : afford to waste even a few micros on stack pushes for other method calls--
//          : speed is the essence here friend.
//          : 
//          : 
//***********************************************************************************************
long double SentenceGeneratorIndividuaL::EvaluateFitness()
{
   FPRINT( "EvaluateFitness" );

   // Already calculated fitness:
   if( _myFitness > -1 )
   {
      return _myFitness;
   } // end if

   _myFitness = 0;
   VectoR* theGenome = GetChromosome()->GetGenome();

   SemanticDifferentialDataSourcE* semanticDataSource = SemanticDifferentialDataSourcE::GetInstance();
   LinkParseR* linkParser = (LinkParseR*)semanticDataSource->GetLinkParser();
   string sentence = GetCharSentence( theGenome );
   SentencE* linkSentence = linkParser->ParseSentence( (char*)sentence.c_str() );
   SentenceIteratoR* iterator = new SentenceIteratoR( linkSentence );
   iterator->SetListener( new SentenceIteratorListeneR() );
   SentencE* kernelSentence = SemanticDifferentialDataSourcE::GetInstance()->GetKernelSentence();

   iterator->Iterate();

   FPRINT << "sentence is";
   FPRINT << (char*)sentence.c_str();

   // if the genome contains a main verb
   ConstituenT* theContext = linkSentence->GetContext();
   ConstituenT* kernelContext = kernelSentence->GetContext();
   if( theContext != 0 )
   {
      _myFitness += 50;
      FPRINT << "a context is";
      FPRINT << theContext->GetForm()->c_str();

      // if the genome's verb/context has a predecessor context of the kernel context
      if( kernelContext != 0 )
      {
         ConstituenT* genomeConstituent = GetContext( theContext->GetForm() );
         ConstituenT* kernelConstituent = GetContext( kernelContext->GetForm() );

         if( genomeConstituent != 0 && kernelConstituent != 0 )
         {
            vector< long > genomeSerials = GetContextSerialNumbers( genomeConstituent );
            vector< long > kernelSerials = GetContextSerialNumbers( kernelConstituent );
            for( int i=0; i<genomeSerials.size(); i++ )
            {
               long g = genomeSerials.at( i );
               for( int j=0; j<kernelSerials.size(); j++ )
               {
                  long k = kernelSerials.at( j );
                  if( (g - 1) == k )
                  {
                     _myFitness += 100;
                     FPRINT << "added points for eval 2";
                  }
               }
            }
         }
      }

FPRINT << "fitness is:";
FPRINT << _myFitness;

      // if the genome's linkage is similar to a linkage in this context
      ConstituenT* genomeConstituent = GetContext( theContext->GetForm() );
      FuzzyComparatoR* fuzzyComparator = new FuzzyComparatoR();
      long bestLinkageSerial = 0;
      long double best = -2000;
      if( genomeConstituent != 0 )
      {
         long double linkage = linkSentence->GetLinkValue();
         vector< ConstituentIteM* > items = genomeConstituent->GetConstituentItems();
         for( int i=0; i<items.size(); i++ )
         {
            ConstituentIteM* item = items.at( i );
            if( *(item->GetContext()) == *(theContext->GetForm()) )
            {
               long double temp = item->GetLinkage();
               long double points = fuzzyComparator->IsLargeLinkageDifference( linkage, temp );
               if( points > best )
               {
                  best = points;
                  bestLinkageSerial = item->GetSerialNumber();
               }
            }
         }
         FPRINT << "fitness linkage: ";
         FPRINT << best;
         _myFitness += best;

FPRINT << "fitness is:";
FPRINT << _myFitness;

      }

      // for the linkage this genome most closely matches in context table, if pred linkage
      // is similar to the kernel linkage
      long double kernelLinkage = kernelSentence->GetLinkValue();
      best = 0;
      vector< ConstituenT* > constituents = SemanticDifferentialDataSourcE::GetInstance()->GetConstituents();

      for( int i=0; i<constituents.size(); i++ )
      {
         ConstituenT* current = constituents.at( i );
         vector< ConstituentIteM* > items = current->GetConstituentItems();
         for( int j=0; j<items.size(); j++ )
         {
            ConstituentIteM* item = items.at( j );
            if( item->GetSerialNumber() == (bestLinkageSerial - 1) )
            {
               long double itemLinkage = item->GetLinkage();
               long double points = fuzzyComparator->IsLargeLinkageDifference( kernelLinkage, itemLinkage );
               if( points > best )
               {
                  best = points;
               }
            }
         }
      }
      FPRINT << "kernel fitness linkage: ";
      FPRINT << best;
      _myFitness += best;

FPRINT << "fitness is:";
FPRINT << _myFitness;


      // for the linkage this genome most closely matches in context table, 
      // if the SD is similar to the genome SD
      string* contextString = GetContext( bestLinkageSerial );
      if( contextString != 0 )
      {
         FPRINT << "the best serial context is:";
         FPRINT << contextString->c_str();
         LargeNetworK* neuralNet = GetNeuralNetwork( contextString );
         if( neuralNet != 0 && neuralNet->GetLargestNumberOfInstances() > 1 )
         {
            SemanticDifferentiaL* genomeSD = linkSentence->GetSemanticDifferential();

            VectoR* input = new VectoR( 8 );
            VectoR* output = 0;

            input->pVariables[0] = genomeSD->GetAgentDimension();
            input->pVariables[1] = genomeSD->GetKLineDimension();
            input->pVariables[2] = genomeSD->GetThingDimension();
            input->pVariables[3] = genomeSD->GetSpatialDimension();
            input->pVariables[4] = genomeSD->GetEventDimension();
            input->pVariables[5] = genomeSD->GetCausalDimension();
            input->pVariables[6] = genomeSD->GetFunctionalDimension();
            input->pVariables[7] = genomeSD->GetAffectiveDimension();
 
            output = neuralNet->IdempotentRecall( input );

            long double points = fuzzyComparator->IsLargeSemanticDifference( input, output );
            FPRINT << "the semantic diff is:";
            FPRINT << points;
            _myFitness += points;
            
            if( output != 0 )
            {
               delete output;
            }
            delete neuralNet;
            delete input;
         }
      }


FPRINT << "fitness is:";
FPRINT << _myFitness;

      // for the linkage this genome most closely matches in context table, 
      // if the pred SD is similar to the kernel SD
      contextString = GetContext( (bestLinkageSerial - 1) );
      if( contextString != 0 )
      {
         FPRINT << "the pred serial context is:";
         FPRINT << contextString->c_str();
         LargeNetworK* neuralNet = GetNeuralNetwork( contextString );
         if( neuralNet != 0 && neuralNet->GetLargestNumberOfInstances() > 1 )
         {
            SemanticDifferentiaL* kernelSD = kernelSentence->GetSemanticDifferential();

            VectoR* input = new VectoR( 8 );
            VectoR* output = 0;

            input->pVariables[0] = kernelSD->GetAgentDimension();
            input->pVariables[1] = kernelSD->GetKLineDimension();
            input->pVariables[2] = kernelSD->GetThingDimension();
            input->pVariables[3] = kernelSD->GetSpatialDimension();
            input->pVariables[4] = kernelSD->GetEventDimension();
            input->pVariables[5] = kernelSD->GetCausalDimension();
            input->pVariables[6] = kernelSD->GetFunctionalDimension();
            input->pVariables[7] = kernelSD->GetAffectiveDimension();
 
            output = neuralNet->IdempotentRecall( input );

            long double points = fuzzyComparator->IsLargeSemanticDifference( input, output );
            FPRINT << "the semantic diff is:";
            FPRINT << points;
            _myFitness += points;
            
            if( output != 0 )
            {
               delete output;
            }
            delete neuralNet;
            delete input;
         }
      }


FPRINT << "fitness is:";
FPRINT << _myFitness;


      delete fuzzyComparator;
   }

   // Now we evaluate the ***constituent*** level:
   vector< ConstituenT* > sentenceConstituents = linkSentence->GetConstituents();
   vector< ConstituenT* > constituents = SemanticDifferentialDataSourcE::GetInstance()->GetConstituents();

   // if the constituent has existed in this context before
   if( theContext != 0 )
   {
      for( int i=0; i<sentenceConstituents.size(); i++ )
      {
         ConstituenT* constituent = sentenceConstituents.at( i );
         bool check = false;
         for( int k=0; k<constituents.size(); k++ )
         {
            ConstituenT* constituentAggregate = constituents.at( k );
            vector< ConstituentIteM* > items = constituentAggregate->GetConstituentItems();
   
            for( int j=0; j<items.size(); j++ )
            {
               ConstituentIteM* item = items.at( j );
               if( *(theContext->GetForm()) == *(item->GetContext()) && 
                   ( *(item->GetConstituent()) == *(constituent->GetForm()) || 
                     *(item->GetLinkedConstituent()) == *(constituent->GetForm()) ) )
               {
                  if( !check )
                  {
                     FPRINT << "constituent has existed in context..";
                     _myFitness += 5;
                     check = true;
                  }
               }
            }
         }
      }
   }


FPRINT << "fitness is:";
FPRINT << _myFitness;

   // if the constituent has existed in this domain before AND
   // if the constituent has the same linkage as in the past in same context
   vector< LinK* > links = linkSentence->GetLinks();
   for( int i=0; i<links.size(); i++ )
   {
      bool checkd1 = false;
      bool checkd2 = false;
      LinK* link = links.at( i );
      string* linkLabel = link->GetLabel();
      ConstituenT* left = linkSentence->GetConstituent( link->GetLeftLink() );
      ConstituenT* right = linkSentence->GetConstituent( link->GetRightLink() );
      for( int k=0; k<constituents.size(); k++ )
      {
         ConstituenT* constituent = constituents.at( k );
         vector< ConstituentIteM* > items = constituent->GetConstituentItems();
         for( int w=0; w<items.size(); w++ )
         {
            ConstituentIteM* item = items.at( w );
            if( *(left->GetForm()) == *(item->GetConstituent()) ||
                *(left->GetForm()) == *(item->GetLinkedConstituent()) ||
                *(right->GetForm()) == *(item->GetConstituent()) ||
                *(right->GetForm()) == *(item->GetLinkedConstituent()) )
            {
               vector< string* > domains = link->GetDomains();
               vector< string* > domains2 = item->GetDomains();
               if( !checkd1 && domains.size() == domains2.size() )
               {
                  int matches = 0;
                  for( int j=0; j<domains.size(); j++ )
                  {
                     string* domain = domains.at( j );
                     for( int l=0; l<domains2.size(); l++ )
                     {
                        string* domain2 = domains2.at( l );
                        if( *domain == *domain2 )
                        {
                           matches += 1;
                        }
                     }
                  }
                  if( matches == domains.size() )
                  {
                     FPRINT << "constituent has existed in domain..";
                     _myFitness += 5;
                     checkd1 = true;
                  }
               }

               // if the constituent has the same linkage as in the past in same context
               if( !checkd2 && theContext != 0 && *(theContext->GetForm()) == *(item->GetContext()) &&
                   *(linkLabel) == *(item->GetLinkType()) )
               {
                  FPRINT << "constituent has the same linkage as in past..";
                  _myFitness += 10;
                  checkd2 = true;
               }
            }
         }
      }
   }

FPRINT << "fitness is:";
FPRINT << _myFitness;


   // if the constituent has a predecesor context of the kernel context AND 
   // if the constituent has functioned the same way in the same context
   if( kernelContext != 0 )
   {
      vector< ConstituenT* > genomeConstituents = linkSentence->GetConstituents();
      long serial = 0;
      for( int j=0; j<genomeConstituents.size(); j++ )
      {
         bool check1 = false;
         bool check2 = false;
         ConstituenT* theConstituent = genomeConstituents.at( j );
         for( int i=0; i<constituents.size(); i++ )
         {
            ConstituenT* aConstituent = constituents.at( i );
            vector< ConstituentIteM* > items = aConstituent->GetConstituentItems();
            for( int k=0; k<items.size(); k++ ) 
            {
               ConstituentIteM* item = items.at( k );
               if( theConstituent->GetForm() != 0 &&
                   (*(theConstituent->GetForm()) == *(item->GetConstituent()) ||
                    *(theConstituent->GetForm()) == *(item->GetLinkedConstituent()) ) )
               {
                  serial = item->GetSerialNumber();
               }
               // this is to speed up the calculations (i.e., this isn't the full impl)
               if( serial != 0 && !check1 &&
                   serial == item->GetSerialNumber() && item->GetContext() != 0 &&
                   *(item->GetContext()) == *(kernelContext->GetForm()) )
               {
                  FPRINT << "constituent has pred context of kernel context..";
                  _myFitness += 5;
                  check1 = true;
               }

               // if the constituent has functioned the same way in the same context
               if( theContext != 0 )
               {
                  if( *(theContext->GetForm()) == *(item->GetContext()) )
                  {
                     // breaking these "ifs" out because of compiler bug...
                     string* inflect1 = theConstituent->GetInflectedForm();
                     string* inflect2 = item->GetInflectedForm();
                     if( !check2 && *(inflect1) == *(inflect2) )
                     {
                        FPRINT << "constituent has functioned the same (inflected) in same context...";
                        _myFitness += 5;
                        check2 = true;
                     }
                  }
               }
            }
         }
      }
   }

FPRINT << "fitness is:";
FPRINT << _myFitness;


/**
   // increase fitness based on ratio of used to unused constituents
   FuzzyComparatoR* fuzzyComparator = new FuzzyComparatoR();
   fuzzyComparator->IsLargeNullLinkCount( linkSentence->GetNullLinkCount() );
   long double nullPoints = fuzzyComparator->IsLargeNullLinkCount( linkSentence->GetNullLinkCount() );
                                  
   FPRINT << "the null link count points is:";
   FPRINT << nullPoints;
   _myFitness += nullPoints;
*/

   // decrease fitness based on ratio of used to unused constituents
   int nullCount = linkSentence->GetNullLinkCount();
   for( int i=0; i<nullCount; i++ )
   {
      FPRINT << "decreasing fitness";
      _myFitness *= 0.5;
   }


FPRINT << "fitness is:";
FPRINT << _myFitness;

   // if the same constituent is used more than once
   vector< ConstituenT* > genomeConstituents = linkSentence->GetConstituents();
   for( int i=1; i<genomeConstituents.size(); i++ )
   {
      ConstituenT* theNextConstituent = genomeConstituents.at( i );
      ConstituenT* theConstituent = genomeConstituents.at( i - 1 );
      if( *(theConstituent->GetForm()) == *(theNextConstituent->GetForm()) )
      {
         _myFitness *= 0.5;
         FPRINT << "the constituent has been used more than once:";
      }
/*
      for( int j=(i+1); j<genomeConstituents.size(); j++ )
      {
         ConstituenT* anotherConstituent = genomeConstituents.at( j );
         if( *(theConstituent->GetForm()) == *(anotherConstituent->GetForm()) )
         {
            _myFitness *= 0.7;
FPRINT << "the constituent has been used more than once:";
         }
      }
*/
   }

FPRINT << "final fitness is:";
FPRINT << _myFitness;


   // perform the memory cleanup for this method
   //delete fuzzyComparator;
   delete linkSentence;
   delete iterator;

   return _myFitness;
} // end EvaluateFitness
