//***************************************************************************** 
// File	    : OrthogonalNet.hpp
// Purpose  : Contains the implementations for the OrthogonalNeT class
// Author   : Brandon Benham
//*****************************************************************************
#ifndef __ORTHOGNET_HPP
#define __ORTHOGNET_HPP

#include"neural.hpp"
#include"Backprop.hpp"
#include"numerr.hpp"
#include"onevar.hpp"  
#include"mathmatc.hpp"
#include"DebugLogger.hpp"

//*****************************************************************************
// Class     : OrthogonalNeT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : Class Declaration
// Purpose   : This class defines a OrthogonalNet network object
//           : which is derived from the BackproP class.
//*****************************************************************************
class OrthogonalNeT : public BackproP
{
   public:
      OrthogonalNeT();
      OrthogonalNeT( char*, unsigned short usFirst = 0 );
      ~OrthogonalNeT();
      void TrainNet();
   
   protected:
      void Setup();
      long double CycleThruNet();
      int EncodeDataPair();
      void ComputeIntermediateValues();
      int SolveConnectionWeights();

}; // end class declaration

#endif
