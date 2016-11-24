//***********************************************************************************************
// File    : SentenceGeneratorIndividual.hpp
// Purpose : Base class for SentenceGeneratorIndividual classes
//         :
//         :
// Author  : Brandon Benham
// Date    : 7/14/05
//***********************************************************************************************

#ifndef __SENTENCEGENIND_HPP
#define __SENTENCEGENIND_HPP

#include "Individual.hpp"
#include "Random.hpp"
#include "ValueEncodedChromosome.hpp"
#include "ValueEncodedMutationStrategy.hpp"
#include "MultiplePointCrossoverStrategy.hpp"
#include "SemanticDifferentialDataSource.hpp"
#include "LinkParser.hpp"
#include "Sentence.hpp"
#include "SentenceIterator.hpp"
#include "SentenceIteratorListener.hpp"
#include "FuzzyComparator.hpp"
#include "LargeNetwork.hpp"

//***********************************************************************************************
// Class   : SentenceGeneratorIndividuaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose :
//         :
//***********************************************************************************************
class SentenceGeneratorIndividuaL : public IndividuaL
{
   public:

      SentenceGeneratorIndividuaL();          // Default Constructor declaration
      virtual ~SentenceGeneratorIndividuaL(); // Destructor declaration

      // Methods from IndividuaL:
      virtual long double EvaluateFitness();
      virtual IndividuaL* Clone();
      virtual void SetFitness( long double n ) { IndividuaL::SetFitness( n ); }
      virtual VectoR* GetGenome() { return GetChromosome()->GetGenome(); }
 
      string GetSentence();

   protected:

      void Setup();

   private:

      string GetCharSentence( VectoR* );
      ConstituenT* GetConstituent( string* );
      ConstituenT* GetContext( string* );
      string* GetContext( long );
      vector< long > GetContextSerialNumbers( ConstituenT* );
      LargeNetworK* GetNeuralNetwork( string* );

}; // end SentenceGeneratorIndividuaL declaration

#endif
