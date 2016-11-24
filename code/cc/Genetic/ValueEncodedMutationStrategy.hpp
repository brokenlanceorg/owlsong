//***********************************************************************************************
// File     : ValueEncodedMutationStrategy.hpp
// Purpose  : Defines an implementation of the MutationStrategY interface.
//          : 
//          : 
// Author   : Brandon Benham 
// Date     : 9/19/01
//***********************************************************************************************
#ifndef __VALUEENCODEDMUTATION_HPP
#define __VALUEENCODEDMUTATION_HPP

#include"MutationStrategy.hpp"           

//***********************************************************************************************
// Class    : ValueEncodedMutationStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : 
//          : 
//***********************************************************************************************
class ValueEncodedMutationStrategY : public MutationStrategY
{
   public:
      ValueEncodedMutationStrategY(); // Default Constructor declaration
      virtual ~ValueEncodedMutationStrategY(); // Destructor declaration

      virtual void Mutate( void* );
      virtual MutationStrategY* Clone();
      
   protected:                 
      void Setup();

   private:
   
}; // end ValueEncodedMutationStrategY declaration

#endif

