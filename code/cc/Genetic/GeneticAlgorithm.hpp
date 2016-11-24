//***********************************************************************************************
// File     : GeneticAlgorithm.hpp
// Purpose  : 
//          : 
//          : 
// Author   : Brandon Benham 
// Date     : 9/16/01
//***********************************************************************************************
#ifndef __GENETICALGORITHM_HPP
#define __GENETICALGORITHM_HPP
            
#include"Individual.hpp"
#include"SelectionStrategy.hpp"
#include"DebugLogger.hpp"

//***********************************************************************************************
// Class    : GeneticAlgorithM
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : 
//          : 
//***********************************************************************************************
class GeneticAlgorithM
{
   public:
      GeneticAlgorithM(); // Default Constructor declaration
      virtual ~GeneticAlgorithM(); // Destructor declaration

      void AddIndividual( IndividuaL* );
      void SetSelectionStrategy( SelectionStrategY* );
      virtual IndividuaL* Evolve();
      void SetMaximumFitness( long double maxFit ) { _MaximumFitness = maxFit; }
      void SetMaxNumberOfGenerations( int i ) { _MaxNumberOfGenerations = i; }
      void SetPopulationSize( int s ) { _PopulationSize = s; }
      void SetBestIndividual( IndividuaL* i ) { _theBestIndividual = i; }
      long double GetBestFitnessValue() { return _theBestFitness; }

   protected:
      void Setup();
      void CloneBestIndividual();
      vector< IndividuaL* > GetNewGeneration();

      SelectionStrategY*     _theSelector;
      vector< IndividuaL* >  _thePopulation;
      IndividuaL*            _theBestIndividual;
      long double            _theBestFitness;
      int _MaxNumberOfGenerations;  // the max number of gens that we'll do
      int _PopulationSize;          // the population size for each generation
      long double _MaximumFitness;  // if we get this fitness, then we'll quit
      //      bool _FoundNewBestIndividual; // if true, then save best individual

   private:
   
}; // end GeneticAlgorithM declaration

#endif

