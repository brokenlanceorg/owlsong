//***********************************************************************************************
// File    : FuzzyComparator.hpp
// Author  : Brandon Benham
// Date    : 7/20/05
//***********************************************************************************************

#ifndef __FUZZYCOMPARATOR_HPP
#define __FUZZYCOMPARATOR_HPP

#include "FuzzyBase.hpp"
#include "Mathmatc.hpp"     

//***********************************************************************************************
// Class   : FuzzyComparatoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose : Performs the fuzzy logic actions 
//         :
//***********************************************************************************************
class FuzzyComparatoR : protected FuzzybasE
{
   public:

      FuzzyComparatoR(); // Constructor declaration
      virtual ~FuzzyComparatoR(); // Destructor declaration

      // linkage fuzzy classes
      FuzzyseT* LARGE_LINKAGE_DIFFERENCE;
      FuzzyseT* LARGE_GENOMIC_POINTS;
      FuzzyseT* LARGE_NULL_LINK_COUNT;
      ConsequenT* _LARGE_GENOMIC_POINTS_;
      FuzzyseT* LARGE_SD_GENOMIC_POINTS;
      ConsequenT* _LARGE_SD_GENOMIC_POINTS_;

      // semantic fuzzy classes
      FuzzyseT* LARGE_SEMANTIC_DIFFERENCE;

      long double IsLargeLinkageDifference( long double, long double );
      long double IsLargeSemanticDifference( VectoR*, VectoR* );
      long double IsLargeNullLinkCount( int );

   protected:

      void Setup();
      void FireRules();
      long double Defuzzify();

   private:

}; // end FuzzyComparatoR declaration

#endif
