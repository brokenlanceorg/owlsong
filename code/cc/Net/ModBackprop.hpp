//***************************************************************************** 
// File      : ModBackprop.hpp
// Purpose  : Contains the implementations for the ModBackproP class
// Author   : Brandon Benham
//*****************************************************************************
#ifndef __MODBACKPROP_HPP
#define __MODBACKPROP_HPP

#include"Backprop.hpp"
#include"Mathmatc.hpp"

//*****************************************************************************
// Class     : ModBackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : Class Declaration
// Purpose   : This class defines a ModBackprop network object
//           : which is derived from the BackproP class.
//*****************************************************************************
class ModBackproP : public BackproP
{
   public:
      ModBackproP();
      ModBackproP( char*, unsigned short usFirst = 0 );
      ~ModBackproP();
   
   protected:
      void Setup();
      long double CycleThruNet();
      int EncodeDataPair();
      void ComputeIntermediateValues();
      void UpdateConnectionWeights();

      VectoR* _sumDeltaOutputErrorW;
      VectoR* _hiddenErrorSumDeltaOutputErrorW;
      VectoR* _sumDeltaOutputError;

      MatriX* _previousHiddenWeights;
      MatriX* _previousOutputWeights;
}; // end class declaration

#endif
