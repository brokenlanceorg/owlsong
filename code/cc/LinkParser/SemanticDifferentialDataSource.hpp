//***********************************************************************************************
// File    : SemanticDifferentialDataSource.hpp
// Purpose : Base class for SemanticDifferentialDataSource classes
//         :
//         :
// Author  : Brandon Benham
// Date    : 5/12/05
//***********************************************************************************************

#ifndef __SEMANTICDIFFDS_HPP
#define __SEMANTICDIFFDS_HPP

#include <string>
#include <vector>
#include <algorithm>
#include <iostream>
#include <fstream>

#include "SemanticDifferential.hpp"
#include "Constituent.hpp"
#include "ConstituentItem.hpp"
#include "FileReader.hpp"
#include "DebugLogger.hpp"
#include "Sentence.hpp"

using namespace std;

//***********************************************************************************************
// Class   : SemanticDifferentialDataSourcE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose :
//         :
//***********************************************************************************************
class SemanticDifferentialDataSourcE
{
   public:

      virtual ~SemanticDifferentialDataSourcE(); // Destructor declaration
      static SemanticDifferentialDataSourcE* GetInstance();

      SemanticDifferentiaL* GetSemanticDifferential( string* s ) 
         { return GetSemanticDifferential( (char*)s->c_str() ); }

      SemanticDifferentiaL* GetSemanticDifferential( char* );
      int GetLinkValue( char* );
      int GetLinkValue( string* s ) { return GetLinkValue( (char*)s->c_str() ); }
      void ReadDataSource();
      void ReadConstituentData( int );
      void SetLinkParser( void* p ) { _theParser = p; }
      void SetContexts( vector< ConstituenT* > c ) { _contexts = c; }
      void SetKernelSentence( SentencE* s ) { _kernelSentence = s; }
      void* GetLinkParser() { return _theParser; }

      vector< ConstituenT* > GetConstituents() { return _constituentObjects; };
      vector< ConstituenT* > GetContexts() { return _contexts; };
      SentencE* GetKernelSentence() { return _kernelSentence; };

   protected:

      SemanticDifferentialDataSourcE();          // Default Constructor declaration
      void Setup();
      void ReadLinkDataSource();
      void StoreLinkValues();
      int FindConstituent( char* );
      SemanticDifferentiaL* FindSemanticDifferential( char* );

   private:

      static SemanticDifferentialDataSourcE* _instance;
      string* _dataSource;
      string* _linkDataSource;
      vector< string* > _constituents;
      vector< SemanticDifferentiaL* > _semanticDifferentials;
      vector< int > _linkValues;
      vector< string* > _links;
      vector< ConstituenT* > _constituentObjects;
      vector< ConstituenT* > _contexts;
      void* _theParser;
      SentencE* _kernelSentence;

}; // end SemanticDifferentialDataSourcE declaration

#endif
