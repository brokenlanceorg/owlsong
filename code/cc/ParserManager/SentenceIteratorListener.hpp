//***********************************************************************************************
// File    : SentenceIteratorListener.hpp
// Purpose : Base class for SentenceIteratorListener classes
//         :
//         :
// Author  : Brandon Benham
// Date    : 5/12/05
//***********************************************************************************************

#ifndef __SENTENCEITERLISTENER_HPP
#define __SENTENCEITERLISTENER_HPP

#include "SentenceListener.hpp"

//***********************************************************************************************
// Class   : SentenceIteratorListeneR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose :
//         :
//***********************************************************************************************
class SentenceIteratorListeneR : public SentenceListeneR
{
   public:

      SentenceIteratorListeneR();          // Default Constructor declaration
      virtual ~SentenceIteratorListeneR(); // Destructor declaration

      virtual void EncounteredConstituent( ConstituenT*, int );
      virtual void EncounteredLink( LinK*, int );

   protected:

      virtual void Setup();

   private:

      // The semantic differential for this entire sentence:
      SemanticDifferentiaL* _semanticDifferential;

}; // end SentenceIteratorListeneR declaration

#endif
