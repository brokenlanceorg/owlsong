//***********************************************************************************************
// File	   : GeneticNetIndividual.hpp
// Purpose : 
//         : 
// Author  : Brandon Benham 
// Date	   : 4/9/05
//***********************************************************************************************
#ifndef __GENETICNETIND_HPP
#define __GENETICNETIND_HPP
            
#include "Individual.hpp"
#include "Random.hpp"
#include "OneVar.hpp"
#include "ValueEncodedChromosome.hpp"
#include "ValueEncodedMutationStrategy.hpp"
#include "SinglePointCrossoverStrategy.hpp"
#include "DataLoader.hpp"

//***********************************************************************************************
// Class   : GeneticNetIndividuaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose : 
//         : 
//***********************************************************************************************
class GeneticNetIndividuaL : protected IndividuaL
{
   public:
      GeneticNetIndividuaL(); // Default Constructor declaration
      virtual ~GeneticNetIndividuaL(); // Destructor declaration

      // Methods from IndividuaL:
      long double EvaluateFitness();
      IndividuaL* Clone();
      void SetFitness( long double n ) { IndividuaL::SetFitness( n ); }
      VectoR* GetGenome() { return GetChromosome()->GetGenome(); }

   protected:
      void Setup();
   
   private:
}; // end GeneticNetIndividuaL declaration

#endif
