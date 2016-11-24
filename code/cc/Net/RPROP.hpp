//***************************************************************************** 
// File	    : RPROP.hpp
// Purpose  : Contains the implementations for the RPROP class
// Author   : Brandon Benham
//*****************************************************************************
#ifndef __RPROP_HPP
#define __RPROP_HPP

#include"neural.hpp"
#include"Backprop.hpp"
#include"numerr.hpp"
#include"onevar.hpp"  
#include"mathmatc.hpp"
#include"DebugLogger.hpp"

//*****************************************************************************
// Class     : RPROP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : Class Declaration
// Purpose   : This class defines a ModBackprop network object
//           : which is derived from the BackproP class.
//*****************************************************************************
class RPROP : public BackproP
{
   public:
      RPROP();
      RPROP( char*, unsigned short usFirst = 0 );
      ~RPROP();
   
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
      MatriX* _hiddenWeightRates;
      MatriX* _outputWeightRates;
      MatriX* _previousHiddenGradient;
      MatriX* _previousOutputGradient;
      MatriX* _prevPreviousHiddenWeights;
      MatriX* _prevPreviousOutputWeights;
}; // end class declaration

#endif
