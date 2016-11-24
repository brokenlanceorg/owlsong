//***********************************************************************************************
// File     : Individual.hpp
// Purpose  : This is the interface that any solution must implement in order to 
//          : be used by the GeneticAlgorithm. The primary methods to be implemented
//          : are EvaluateFitness() and clone().
// Author   : Brandon Benham 
// Date     : 9/16/01
//***********************************************************************************************
#ifndef __INDIVIDUAL_HPP
#define __INDIVIDUAL_HPP
            
#include"Chromosome.hpp"

//***********************************************************************************************
// Class    : IndividuaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : This is the interface that any complete solutions must implement
//          : in order to be solved by the GeneticAlgorithm.
//***********************************************************************************************
class IndividuaL
{
   public:
      IndividuaL(); // Default Constructor declaration
      virtual ~IndividuaL(); // Destructor declaration

      virtual long double EvaluateFitness() = 0;
      virtual IndividuaL* Clone() = 0;
      ChromosomE* GetChromosome() { return _myChromosome; }
      VectoR* GetGenome();
      void SetChromosome( ChromosomE* );
      void SetCrossoverStrategy( CrossoverStrategY* );
      IndividuaL& operator + (IndividuaL& b);
      inline void Mutate() { _myChromosome->Mutate(); }
      virtual void SetFitness( long double f ) { _myFitness = f; }
      
   protected:
      void Setup();

      long double _myFitness;
      ChromosomE* _myChromosome; // Holds this instance's genome

   private:
   
}; // end IndividuaL declaration

#endif

