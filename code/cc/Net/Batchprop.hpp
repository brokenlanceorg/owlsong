//***************************************************************************** 
// File	    : Batchprop.hpp
// Purpose  : Contains the implementations for the BatchproP class
// Author   : Brandon Benham
//*****************************************************************************
#ifndef __BATCHPROP_HPP
#define __BATCHPROP_HPP

#include"neural.hpp"
#include"Backprop.hpp"
#include"numerr.hpp"
#include"onevar.hpp"  
#include"mathmatc.hpp"
#include"DebugLogger.hpp"

//*****************************************************************************
// Class     : BatchproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : Class Declaration
// Purpose   : This class defines a Batchprop network object
//           : which is derived from the BackproP class.
//*****************************************************************************
class BatchproP : public BackproP
{
   public:
      BatchproP();
      BatchproP( char*, unsigned short usFirst = 0 );
      ~BatchproP();
   
   protected:
      void Setup();
      long double CycleThruNet();
      int EncodeDataPair();
      void ComputeIntermediateValues();
      void UpdateConnectionWeights();

      VectoR* _sumDeltaOutputErrorW;
      VectoR* _hiddenErrorSumDeltaOutputErrorW;
      VectoR* _sumDeltaOutputError;
      VectoR* _hiddenGradient;
      VectoR* _outputGradient;

}; // end class declaration

#endif
