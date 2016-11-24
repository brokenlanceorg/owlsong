//***********************************************************************************************
// File    : DerivativeBackprop.hpp
// Purpose : Base class for DerivativeBackprop classes
//         :
//         :
// Author  : Brandon Benham
// Date    : 7/26/02
//***********************************************************************************************
#ifndef __DerivativeBackprop_HPP
#define __DerivativeBackprop_HPP

#include "Backprop.hpp"
//***********************************************************************************************
// Class   : DerivativeBackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose :
//         :
//***********************************************************************************************
class DerivativeBackproP : public BackproP
{
   public:
      DerivativeBackproP( char*, unsigned short );  // Default Constructor declaration
      ~DerivativeBackproP(); // Destructor declaration

      long double GetDerivative();

   protected:
      void Setup();

   private:

}; // end DerivativeBackprop declaration

#endif

