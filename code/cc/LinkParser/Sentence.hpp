//***********************************************************************************************
// File    : Sentence.hpp
// Purpose : Base class for SentencE classes
//         :
//         :
// Author  : Brandon Benham
// Date    : 5/12/05
//***********************************************************************************************

#ifndef __SENTENCE_HPP
#define __SENTENCE_HPP

#include <vector>
#include <fstream>

#include "Constituent.hpp"
#include "SemanticDifferential.hpp"
#include "DebugLogger.hpp"

//***********************************************************************************************
// Class   : SentencE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose :
//         :
//***********************************************************************************************
class SentencE
{
   public:

      SentencE();          // Default Constructor declaration
      virtual ~SentencE(); // Destructor declaration

      void AddConstituent( ConstituenT* );
      void AddLink( LinK* );
      void Reset();
      void CheckAndSetContext();
      void SetNullLinkCount( int c ) { _nullLinkCount = c; }
      void SetSemanticDifferential( SemanticDifferentiaL* sd ) { _semanticDifferential = sd; }
      void SetLinkValue( long double v ) { _linkValue = v; }
      void WriteToDisk();

      ConstituenT* GetContext() { return _context; }
      SemanticDifferentiaL* GetSemanticDifferential() { return _semanticDifferential; }
      int GetNullLinkCount() { return _nullLinkCount; }
      int GetNonNullLinkCount() { return _constituents.size(); }
      long double GetLinkValue() { return _linkValue; }
      ConstituenT* GetConstituent( int );
      vector< ConstituenT* > GetConstituents() { return _constituents; }
      vector< LinK* > GetLinks() { return _links; }

   protected:

      void Setup();

   private:

      int _nullLinkCount;
      ConstituenT* _context;
      SemanticDifferentiaL* _semanticDifferential;
      vector< ConstituenT* > _constituents;
      vector< LinK* > _links;
      long double _linkValue;
      string* _outputFileName;

}; // end SentencE declaration

#endif
