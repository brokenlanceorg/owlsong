//***************************************************************************** 
// File	    : BFGSNetwork.hpp
// Purpose  : Contains the implementations for the BFGSNetworK class
// Author   : Brandon Benham
//*****************************************************************************
#ifndef __BFGSNETWORK_HPP
#define __BFGSNETWORK_HPP

#include"neural.hpp"
#include"Backprop.hpp"
#include"numerr.hpp"
#include"onevar.hpp"  
#include"mathmatc.hpp"
#include"DebugLogger.hpp"

//*****************************************************************************
// Class     : BFGSNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : Class Declaration
// Purpose   : This class defines a BFGSNetwork network object
//           : which is derived from the BackproP class.
//*****************************************************************************
class BFGSNetworK : public BackproP
{
   public:
      BFGSNetworK();
      BFGSNetworK( char*, unsigned short usFirst = 0 );
      ~BFGSNetworK();
   
   protected:
      void Setup();
      long double CycleThruNet();
      int EncodeDataPair();
      void ComputeIntermediateValues();
      void UpdateConnectionWeights();
      void PerformBFGSMethod();

      VectoR* _sumDeltaOutputErrorW;
      VectoR* _hiddenErrorSumDeltaOutputErrorW;
      VectoR* _sumDeltaOutputError;
      VectoR* _hiddenGradient;
      VectoR* _outputGradient;
      VectoR* _previousHiddenGradient;
      VectoR* _previousOutputGradient;
      VectoR* _hiddenDirection;
      VectoR* _outputDirection;
      VectoR* _previousHiddenP;
      VectoR* _previousOutputP;

      MatriX* _hiddenHessian;
      MatriX* _outputHessian;

      int _hiddenSize;
      int _outputSize;

      unsigned long _hiddenIterations;
      unsigned long _outputIterations;
}; // end class declaration

#endif
