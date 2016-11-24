//***********************************************************************************************
// File     : FuzzyBase.hpp
// Purpose  : This class provides an interface for all fuzzy logic engines to
//          : implement. This class derives from FuzzyfactorY which allows
//          : for easy creation and management of fuzzy objects. The classes
//          : FireRules and Defuzzify must be overridden to use.
//          :
// Author   : Brandon Benham 
// Date     : 5/30/00
//***********************************************************************************************
#ifndef __FUZZYBASE_HPP
#define __FUZZYBASE_HPP

#include "FuzzyFactory.hpp"
#include "CompoundConsequent.hpp"
#include "FuzzySet.hpp"
//***********************************************************************************************
// Class    : FuzzybasE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : Provides a foundation for Fuzzy logic engines including a
//          : FuzzyFactory which allows easy fuzzy object creation and management.
//***********************************************************************************************
class FuzzybasE : protected FuzzyfactorY
{
   public:
      FuzzybasE(); // Default Constructor declaration
      virtual ~FuzzybasE(); // Destructor declaration

   protected:
      virtual void Setup();
      virtual void FireRules() = 0;
      virtual long double Defuzzify() = 0;
      

   private:
   
}; // end FuzzybasE declaration

#endif

