//***********************************************************************************************
// File     : MultiplePointCrossoverStrategy.hpp
// Purpose  : 
//          : 
//          : 
// Author   : Brandon Benham 
// Date     : 7/9/05
//***********************************************************************************************
#ifndef __MULTIPLEPOINTCROSSOVER_HPP
#define __MULTIPLEPOINTCROSSOVER_HPP
            
#include"CrossoverStrategy.hpp"
#include"Chromosome.hpp"

//***********************************************************************************************
// Class    : MultiplePointCrossoverStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : 
//          : 
//***********************************************************************************************
class MultiplePointCrossoverStrategY : protected CrossoverStrategY
{
   public:

      MultiplePointCrossoverStrategY();  // Default Constructor declaration
      virtual ~MultiplePointCrossoverStrategY(); // Destructor declaration
      
      virtual void Crossover( void*, void*, void* );
      virtual CrossoverStrategY* Clone();

   protected:

      void Setup();

   private:
   
}; // end MultiplePointCrossoverStrategY declaration

#endif

