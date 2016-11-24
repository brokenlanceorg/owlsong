//***********************************************************************************************
// File    : SentenceGenerator.hpp
// Purpose : Base class for SentenceGenerator classes
//         :
//         :
// Author  : Brandon Benham
// Date    : 7/11/05
//***********************************************************************************************

#ifndef __SENTENCEGENERATOR_HPP
#define __SENTENCEGENERATOR_HPP

#include <string>
#include <vector>

#include "SentenceGeneratorIndividual.hpp"
#include "SemanticDifferentialDataSource.hpp"
#include "LinkParser.hpp"
#include "SentenceIterator.hpp"
#include "SentenceIteratorListener.hpp"
#include "Sentence.hpp"
#include "Constituent.hpp"
#include "GeneticAlgorithm.hpp"
#include "Mathmatc.hpp"
#include "Individual.hpp"
#include "Random.hpp"
#include "ValueEncodedChromosome.hpp"
#include "ValueEncodedMutationStrategy.hpp"
#include "MultiplePointCrossoverStrategy.hpp"
#include "SelectionStrategy.hpp"
#include "RouletteWheelSelectionStrategy.hpp"

//***********************************************************************************************
// Class   : SentenceGeneratoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose :
//         :
//***********************************************************************************************
class SentenceGeneratoR
{
   public:

      SentenceGeneratoR();          // Default Constructor declaration
      virtual ~SentenceGeneratoR(); // Destructor declaration

      string* GetGeneratedSentence( vector< string* > );

   protected:

      void Setup();

   private:

      vector< string* > _kernel; // this is cleaned up by the DLL entry code
      vector< ConstituenT* > _contexts;
      SentencE* _kernelSentence;
      string* PerformGeneticAlgorithm();
      int _genePoolSize;

}; // end SentenceGeneratoR declaration

#endif
