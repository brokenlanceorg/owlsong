//***********************************************************************************************
// File     : FuzzyStockConfidence.hpp
// Purpose  : 
//          : 
//          : 
// Author   : Brandon Benham 
// Date     : 2/24/01
//***********************************************************************************************
#ifndef __FUZZYSTOCKCONF_HPP
#define __FUZZYSTOCKCONF_HPP

#include "FuzzyBase.hpp"
//***********************************************************************************************
// Class    : FuzzyStockConfidencE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : 
//          : 
//***********************************************************************************************
class FuzzyStockConfidencE : protected FuzzybasE
{
   public:
      FuzzyStockConfidencE(); // Default Constructor declaration
      ~FuzzyStockConfidencE(); // Destructor declaration

      void OutputConfidences();

   protected:
      void Setup();
      void FireRules();
      inline long double Defuzzify() { return 0; };
                    
   private:
      long double ldVariance;
      long double ldStability;
      long double ldPrevious;
      long double ldConfidence;
      
      FuzzyseT*         SMALL_VARIANCE_RANK;
      FuzzyseT*         SMALL_STABILITY_RANK;
      FuzzyseT*         SMALL_PREVIOUS_RANK;
      ConcentrateHedgE* VERY_SMALL_STABILITY_RANK;
}; // end FuzzyStockConfidencE declaration

#endif

