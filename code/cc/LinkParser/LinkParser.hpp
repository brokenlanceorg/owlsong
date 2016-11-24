//***********************************************************************************************
// File    : LinkParser.hpp
// Purpose : Base class for LinkParser classes
//         :
//         :
// Author  : Brandon Benham
// Date    : 5/12/05
//***********************************************************************************************

#ifndef __LINKPARSER_HPP
#define __LINKPARSER_HPP

#include <string>
#include <iostream>

#include "link-includes.h"
#include "Sentence.hpp"
#include "SemanticDifferentialDataSource.hpp"
#include "DebugLogger.hpp"

using namespace std;

//***********************************************************************************************
// Class   : LinkParseR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose :
//         :
//***********************************************************************************************
class LinkParseR
{
   public:

      LinkParseR();          // Default Constructor declaration
      virtual ~LinkParseR(); // Destructor declaration

      SentencE* ParseSentence( char* );
      SentencE* ParseSentence( string* );

   protected:

      void Setup();
      void BuildSentence( SentencE* );

   private:

      Dictionary _dictionary;
      Parse_Options _parsingOptions;
      Sentence _linkSentence;
      Linkage _linkage;

}; // end LinkParseR declaration

#endif
