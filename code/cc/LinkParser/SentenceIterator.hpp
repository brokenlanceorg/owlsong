//***********************************************************************************************
// File    : SentenceIterator.hpp
// Purpose : Base class for SentenceIterator classes
//         :
//         :
// Author  : Brandon Benham
// Date    : 5/12/05
//***********************************************************************************************

#ifndef __SENTENCEITER_HPP
#define __SENTENCEITER_HPP

#include "Sentence.hpp"
//#include "SentenceListener.hpp"
#include "SentenceIteratorListener.hpp"
#include "DebugLogger.hpp"

#include <iostream>

using namespace std;

//***********************************************************************************************
// Class   : SentenceIteratoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose :
//         :
//***********************************************************************************************
class SentenceIteratoR
{
   public:

      SentenceIteratoR( SentencE* );          // Default Constructor declaration
      SentenceIteratoR();          // Default Constructor declaration
      virtual ~SentenceIteratoR(); // Destructor declaration

      void Iterate();

      //void SetListener( SentenceListeneR* l ) { _sentenceListener = l; }
      void SetListener( SentenceIteratorListeneR* l ) { _sentenceListener = l; }

   protected:

      void Setup();
      void Iterate( ConstituenT*, int );

   private:

      SentencE* _sentence;
      //SentenceListeneR* _sentenceListener;
      SentenceIteratorListeneR* _sentenceListener;

}; // end SentenceIteratoR declaration

#endif
