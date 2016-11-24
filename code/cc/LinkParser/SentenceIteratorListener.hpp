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
#include "FuzzyFactory.hpp"
#include "CompoundConsequent.hpp"
#include "DebugLogger.hpp"

#include <iomanip>

//***********************************************************************************************
// Class   : SentenceIteratorListeneR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose :
//         :
//***********************************************************************************************
class SentenceIteratorListeneR //: public SentenceListeneR
{
   public:

      SentenceIteratorListeneR();          // Default Constructor declaration
      virtual ~SentenceIteratorListeneR(); // Destructor declaration

      virtual void EncounteredConstituent( ConstituenT*, int );
      virtual void EncounteredLink( LinK*, int );
      virtual SemanticDifferentiaL* GetSemanticDifferential();
      virtual long double GetLinkValue();

   protected:

      virtual void Setup();
      void PushSemanticDifferential( SemanticDifferentiaL*, int );
      void PushLinkValue( LinK*, int );
      long double Absolute( long double );

   private:

      FuzzyfactorY* _fuzzyFactory;
      CompoundconsequenT* _agentFuzzySystem;
      CompoundconsequenT* _klineFuzzySystem;
      CompoundconsequenT* _thingFuzzySystem;
      CompoundconsequenT* _spatialFuzzySystem;
      CompoundconsequenT* _eventFuzzySystem;
      CompoundconsequenT* _causalFuzzySystem;
      CompoundconsequenT* _functionalFuzzySystem;
      CompoundconsequenT* _affectiveFuzzySystem;

      CompoundconsequenT* _linkValueFuzzySystem;

}; // end SentenceIteratorListeneR declaration

#endif
