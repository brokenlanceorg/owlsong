//***********************************************************************************************
// File     : SelectionStrategy.hpp
// Purpose  : 
//          : 
//          : 
// Author   : Brandon Benham 
// Date     : 9/16/01
//***********************************************************************************************
#ifndef __SELECTIONSTRATEGY_HPP
#define __SELECTIONSTRATEGY_HPP
            
#include <vector>

#include"Individual.hpp"
#include"Random.hpp"

//***********************************************************************************************
// Class    : SelectionStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : 
//          : 
//***********************************************************************************************
class SelectionStrategY
{
   public:
      SelectionStrategY(); // Default Constructor declaration
      virtual ~SelectionStrategY(); // Destructor declaration

      virtual IndividuaL* GetParent() = 0;
      void SetPopulation( vector< IndividuaL* > );

   protected:
      void Setup();
      void CalculatePopulationFitness();

      long double           _PopulationFitness;
      RandoM*               _theGenerator;
      vector< IndividuaL* > _thePopulation;

   private:
   
}; // end SelectionStrategY declaration

#endif

