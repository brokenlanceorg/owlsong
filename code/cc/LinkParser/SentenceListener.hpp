//***********************************************************************************************
// File    : SentenceListener.hpp
// Purpose : Base class for SentenceListener classes
//         :
//         :
// Author  : Brandon Benham
// Date    : 5/12/05
//***********************************************************************************************

#ifndef __SENTENCELISTENER_HPP
#define __SENTENCELISTENER_HPP

#include "Constituent.hpp"
#include "SemanticDifferential.hpp"
#include "Link.hpp"
#include "DebugLogger.hpp"

#include <iostream>

//***********************************************************************************************
// Class   : SentenceListeneR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose :
//         :
//***********************************************************************************************
class SentenceListeneR
{
   public:

      SentenceListeneR();          // Default Constructor declaration
      virtual ~SentenceListeneR(); // Destructor declaration

      virtual void EncounteredConstituent( ConstituenT*, int ) = 0;
      virtual void EncounteredLink( LinK*, int ) = 0;
      virtual SemanticDifferentiaL* GetSemanticDifferential() { cout << "########" << endl; return 0; }
      virtual long double GetLinkValue() { cout << "########" << endl; return 0; }

   protected:

   private:

}; // end SentenceListeneR declaration

#endif
