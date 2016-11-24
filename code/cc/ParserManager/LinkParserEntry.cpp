#include <cstdlib>
#include <iostream>
#include <string>

#include "linkparser_SentenceParser.h"
#include "LinkParser.hpp"
#include "Sentence.hpp"
#include "LargeNetwork.hpp"
#include "Constituent.hpp"
#include "SentenceIterator.hpp"
#include "SentenceIteratorListener.hpp"
#include "SemanticDifferentialDataSource.hpp"
#include "Mathmatc.hpp"
#include "SentenceGenerator.hpp"

using namespace std;

JNIEXPORT void JNICALL Java_linkparser_SentenceParser_parseSentence( JNIEnv * env, jobject , 
                                                                     jstring javaSentence, jint deleteResources )
{
   FPRINT( "Main" );
   long performDelete = (long)deleteResources;
   SentencE* theSentence;
   LinkParseR* theParser = new LinkParseR();
   SemanticDifferentialDataSourcE::GetInstance()->ReadDataSource();

   const char* aString = env->GetStringUTFChars( javaSentence, 0 );
   cout << "The string is: " << aString << endl;

   theSentence = theParser->ParseSentence( (char*)aString );

   cout << "About to create iterator..." << endl;
   SentenceIteratoR* iterator = new SentenceIteratoR( theSentence );

   cout << "About to set listener" << endl;
//    iterator->SetListener( (SentenceListeneR*)new SentenceIteratorListeneR() );
   iterator->SetListener( (new SentenceIteratorListeneR()) );

   cout << "About to iterate" << endl;
   iterator->Iterate();

   cout << "About to write to disk" << endl;
   theSentence->WriteToDisk();

   cout << "About to train neural network" << endl;
   // number of nets, depth, inputs, outputs, hidden
   ConstituenT* context = theSentence->GetContext();

   LargeNetworK* network = 0;
   
   if( context != 0 )
   {
      string* form = context->GetForm();

      if( (form->find( "\\" ) == string::npos) &&
          (form->find( "\/" ) == string::npos) &&
          (form->find( ":" ) == string::npos) &&
          (form->find( "\*" ) == string::npos) &&
          (form->find( "\"" ) == string::npos) &&
          (form->find( "<" ) == string::npos) &&
          (form->find( ">" ) == string::npos) &&
          (form->find( "|" ) == string::npos) )
      {
         network = new LargeNetworK( (char*)context->GetForm()->c_str(), 10, 25, 8, 8, 60 );
         SemanticDifferentiaL* sd = theSentence->GetSemanticDifferential();
         VectoR* trainingVector = new VectoR( 8 );
   
         trainingVector->pVariables[0] = sd->GetAgentDimension();
         trainingVector->pVariables[1] = sd->GetKLineDimension();
         trainingVector->pVariables[2] = sd->GetThingDimension();
         trainingVector->pVariables[3] = sd->GetSpatialDimension();
         trainingVector->pVariables[4] = sd->GetEventDimension();
         trainingVector->pVariables[5] = sd->GetCausalDimension();
         trainingVector->pVariables[6] = sd->GetFunctionalDimension();
         trainingVector->pVariables[7] = sd->GetAffectiveDimension();
   
         network->AddTrainingPairWithRollback( trainingVector, trainingVector );
      }
   }


   /** Deletions */

   cout << "About to delete iterator" << endl;
   delete iterator;

   cout << "About to delete sentence" << endl;
   delete theSentence;

   cout << "About to delete network" << endl;
   if( network != 0 )
   {
      delete network;
   }

   // This must always be done!!! Need to add a param to check for deletion
   if( performDelete )
   {
      cout << "About to delete data source" << endl;
      delete SemanticDifferentialDataSourcE::GetInstance();
   }


   env->ReleaseStringUTFChars( javaSentence, aString );

   cout << "About to delete link parser" << endl;

   delete theParser;    
}

/**
 * Class:     linkparser_SentenceParser
 * Method:    parseConversation
 * Signature: ([Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_linkparser_SentenceParser_parseConversation( JNIEnv * env, jobject, 
                  jobjectArray sentences, jint poolSize )
{
   int size = env->GetArrayLength( sentences );
   int genePoolSize = (int)poolSize;
   const char* aSentence = 0;
   vector< string* > kernel;

   SemanticDifferentialDataSourcE::GetInstance()->ReadConstituentData( genePoolSize );

   for( int i=0; i<size; i++ )
   {
      jstring aString = (jstring)env->GetObjectArrayElement( sentences, i );
      aSentence = env->GetStringUTFChars( aString, 0 );
   
      kernel.push_back( new string( aSentence ) );

      env->ReleaseStringUTFChars( aString, aSentence );
   }
   
   SentenceGeneratoR* theGenerator = new SentenceGeneratoR();
   string* sentence = theGenerator->GetGeneratedSentence( kernel );
   
   if( sentence != 0 )
   {
      delete sentence;
   }

   for( int i=0; i<kernel.size(); i++ )
   {
      delete kernel.at( i );
   }
   kernel.clear();
      
   delete theGenerator;
   delete SemanticDifferentialDataSourcE::GetInstance();
}
