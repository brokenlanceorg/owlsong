//***********************************************************************************************
// File     : SinglePointCrossoverStrategy.hpp
// Purpose  : 
//          : 
//          : 
// Author   : Brandon Benham 
// Date     : 9/16/01
//***********************************************************************************************
#ifndef __SINGLEPOINTCROSSOVER_HPP
#define __SINGLEPOINTCROSSOVER_HPP
            
#include"CrossoverStrategy.hpp"
#include"Chromosome.hpp"

//***********************************************************************************************
// Class    : SinglePointCrossoverStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : 
//          : 
//***********************************************************************************************
class SinglePointCrossoverStrategY : protected CrossoverStrategY
{
   public:
      SinglePointCrossoverStrategY(); // Default Constructor declaration
      virtual ~SinglePointCrossoverStrategY(); // Destructor declaration
      
      virtual void Crossover( void*, void*, void* );
      virtual CrossoverStrategY* Clone();

   protected:
      void Setup();

   private:
   
}; // end SinglePointCrossoverStrategY declaration

#endif

