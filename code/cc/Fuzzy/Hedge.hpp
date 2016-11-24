//***********************************************************************************************
// File     : Hedge.hpp
// Purpose  : Defines our derived class Hedge, which incorporates the basic operations of
//       : fuzzy sets and overloads the EvaluateIt method.
// Author   : Brandon Benham 
// Date     : 1/4/00
//***********************************************************************************************

#include"FuzzySet.hpp"

#ifndef __HEDGE_HPP
#define __HEDGE_HPP
                     
//***********************************************************************************************
// Class : HedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : Declares the HedgE class.
//***********************************************************************************************
class HedgE : public FuzzyseT
{
   private:

   protected:

      // The pointer the the next FuzzyseT or HedgE:
      FuzzyseT* FzsNext_Set;

      // The ability to perform the hedge mathematics:
      OnevariablE* Fct1The_Hedge_Function;

      // A holder for the exponent of the transformation:
      long double ldPower_Variable;
      long double ldModified_Power_Variable;

      // The ol' Setup method:
      void Setup();

   public:

      // Default constructor:
      HedgE();

      // Destructor:
      virtual ~HedgE();

      // Overloaded operators:
      HedgE& operator + ( FuzzyseT& );

      // To get domain values:           
      inline long double GetLeftEndpoint() {return FzsNext_Set->GetLeftEndpoint();}
      inline long double GetRightEndpoint() {return FzsNext_Set->GetRightEndpoint();}
      inline long double GetMidPoint() {return FzsNext_Set->GetMidPoint();}
}; // end class HedgE declaration

#ifdef __MYDEBUG__
   #define HFPRINT( msg ) DebugloggeR HFPRINT( msg, __FILE__, __LINE__ );
#else
   #define HFPRINT( msg );
#endif  

#endif  

