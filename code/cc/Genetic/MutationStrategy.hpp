//***********************************************************************************************
// File     : MutationStrategy.hpp
// Purpose  : 
//          : 
//          : 
// Author   : Brandon Benham 
// Date     : 9/16/01
//***********************************************************************************************
#ifndef __MUTATIONSTRATEGY_HPP
#define __MUTATIONSTRATEGY_HPP
            
#include"Chromosome.hpp"
#include"Random.hpp"

//***********************************************************************************************
// Class    : MutationStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : 
//          : 
//***********************************************************************************************
class MutationStrategY
{
   public:
      MutationStrategY(); // Default Constructor declaration
      virtual ~MutationStrategY(); // Destructor declaration

      virtual void Mutate( void* ) = 0;
      virtual MutationStrategY* Clone() = 0;

      virtual void SetMutationProbability( long double v ) { _MutationProbability = v; }

   protected:
      void Setup();
      long double _MutationProbability;
      RandoM*     _theGenerator;

   private:
   
}; // end MutationStrategY declaration

#endif

