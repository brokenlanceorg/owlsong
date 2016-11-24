//***************************************************************************** 
// File     : NewtonNet.hpp
// Purpose  : Contains the implementations for the NewtonNeT class
// Author   : Brandon Benham
//*****************************************************************************
#ifndef __NEWTONNET_HPP
#define __NEWTONNET_HPP

#include"Neural.hpp"
#include"Backprop.hpp"
#include"Numerr.hpp"
#include"Onevar.hpp"  
#include"Mathmatc.hpp"
#include"DebugLogger.hpp"

//*****************************************************************************
// Class     : NewtonNeT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : Class Declaration
// Purpose   : This class defines a NewtonNet network object
//           : which is derived from the BackproP class.
//*****************************************************************************
class NewtonNeT : public BackproP
{
   public:
      NewtonNeT();
      NewtonNeT( char*, unsigned short usFirst = 0 );
      ~NewtonNeT();
   
   protected:
      void Setup();
      long double CycleThruNet();
      int EncodeDataPair();
      void ComputeIntermediateValues();
      void ConstructJacobian();
      void UpdateConnectionWeights();

      VectoR* _hiddenSecondError;
      VectoR* _outputSecondError;
      VectoR* _outputErrorSquared;
      VectoR* _deltaOutputSecondError;
      VectoR* _sumDeltaOutputErrorW;
      VectoR* _sumDeltaOutputSecondErrorW;
      VectoR* _hiddenErrorSumDeltaOutputErrorW;

      MatriX* _inputHiddenError;
      MatriX* _inputHiddenSecondError;
      MatriX* _sumErrorSquaredWW;
      MatriX* _jacobianSynapaseOne;
      MatriX* _jacobianSynapaseTwo;
   
}; // end class declaration

#endif
