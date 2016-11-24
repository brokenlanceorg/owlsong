//***********************************************************************************************
// File     : ValueEncodedChromosome.hpp
// Purpose  : 
//          : 
//          : 
// Author   : Brandon Benham 
// Date     : 9/20/01
//***********************************************************************************************
#ifndef __VALUEENCODEDCHROMOSOME_HPP
#define __VALUEENCODEDCHROMOSOME_HPP
            
#include"Chromosome.hpp"

//***********************************************************************************************
// Class    : ValueEncodedChromosomE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : 
//          : 
//***********************************************************************************************
class ValueEncodedChromosomE : protected ChromosomE
{
   public:
      ValueEncodedChromosomE(); // Default Constructor declaration
      virtual ~ValueEncodedChromosomE(); // Destructor declaration

      virtual ChromosomE* Clone();
      
   protected:                                      
      void Setup();

   private:
   
}; // end ValueEncodedChromosomE declaration

#endif

