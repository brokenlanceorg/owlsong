//***********************************************************************************************
// File     : Chromosome.hpp
// Purpose  : 
//          : 
//          : 
// Author   : Brandon Benham 
// Date     : 9/16/01
//***********************************************************************************************
#ifndef __CHROMOSOME_HPP
#define __CHROMOSOME_HPP
            
#include"CrossoverStrategy.hpp"
#include"MutationStrategy.hpp"   
#include"Mathmatc.hpp"

//***********************************************************************************************
// Class    : ChromosomE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : 
//          : 
//***********************************************************************************************
class ChromosomE
{
   public:
      ChromosomE(); // Default Constructor declaration
      virtual ~ChromosomE(); // Destructor declaration

      void SetCrossoverStrategy( void* );
      ChromosomE* Crossover( ChromosomE* );
      void Mutate();
      VectoR* GetGenes( int, int);
      void AddGenes( VectoR* );
      virtual ChromosomE* Clone() = 0;
      void SetGenome( VectoR* );
      VectoR* GetGenome() { return _myGenome; }
      void SetMutationStrategy( void* );
      void* CloneMutationStrategy();
      void* CloneCrossoverStrategy();

   protected:
      void Setup();

      void*   _myCrosser; // Type: CrossoverStrategY*
      VectoR* _myGenome;
      void*   _myMutator; // Type: MutationStrategY*
      
      long double _LowEndpoint;
      long double _HighEndpoint;
      long double _Smallest; // to scale back to original values
      long double _Largest;  // to scale back to original values

   private:
   
}; // end ChromosomE declaration

#endif

