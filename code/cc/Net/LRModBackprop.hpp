//***************************************************************************** 
// File	    : ModBackprop.hpp
// Purpose  : Contains the implementations for the ModBackproP class
// Author   : Brandon Benham
//*****************************************************************************
#ifndef __MODBACKPROP_HPP
#define __MODBACKPROP_HPP

#include"neural.hpp"
#include"Backprop.hpp"
#include"numerr.hpp"
#include"onevar.hpp"  
#include"mathmatc.hpp"
#include"DebugLogger.hpp"

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

      long double _hiddenLearningRate;
      long double _outputLearningRate;

      VectoR* _sumDeltaOutputErrorW;
      VectoR* _hiddenErrorSumDeltaOutputErrorW;
      VectoR* _sumDeltaOutputError;

      MatriX* _hiddenGradientDifference;
      MatriX* _outputGradientDifference;
      MatriX* _hiddenWeightDifference;
      MatriX* _outputWeightDifference;
}; // end class declaration

#endif
