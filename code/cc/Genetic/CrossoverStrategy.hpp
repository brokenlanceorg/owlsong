//***********************************************************************************************
// File     : CrossoverStrategy.hpp
// Purpose  : 
//          : 
//          : 
// Author   : Brandon Benham 
// Date     : 9/16/01
//***********************************************************************************************
#ifndef __CROSSOVERSTRATEGY_HPP
#define __CROSSOVERSTRATEGY_HPP
            
#include"Chromosome.hpp"
#include"Random.hpp"

//***********************************************************************************************
// Class    : CrossoverStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : 
//          : 
//***********************************************************************************************
class CrossoverStrategY
{
   public:
      CrossoverStrategY(); // Default Constructor declaration
      virtual ~CrossoverStrategY(); // Destructor declaration

      virtual void Crossover( void*, void*, void* ) = 0;
      virtual CrossoverStrategY* Clone() = 0;
      void SetGeneSize( int i ) { _GeneSize = i; }
                                 
   protected:
      void Setup();

      int         _GeneSize;
      long double _CrossoverProbability;
      RandoM*     _theGenerator;

   private:
   
}; // end CrossoverStrategY declaration

#endif

