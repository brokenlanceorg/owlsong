//***********************************************************************************************
// File		: FuzzyComparator.cpp
// Purpose	: Defines the fuzzy logic operations
//	        :
// Author	: Brandon Benham
// Date		: 7/20/05
//***********************************************************************************************

#include "FuzzyComparator.hpp"

//***********************************************************************************************
// Class        : FuzzyComparatoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Default Constructor
// Purpose	: Performs the basic construction actions.
// Notes        : There will eight number in the genome to specify all the sets.
//***********************************************************************************************
FuzzyComparatoR::FuzzyComparatoR() : FuzzybasE()
{
   LARGE_NULL_LINK_COUNT = CreateFuzzySet( 0, 2, 4, false );
   LARGE_SEMANTIC_DIFFERENCE = CreateFuzzySet( 0, 4, 8, false );
   LARGE_LINKAGE_DIFFERENCE = CreateFuzzySet( 0, 125, 250, false );
   // for most sets:
   LARGE_GENOMIC_POINTS = CreateFuzzySet( 0, 100, 200, true );
   _LARGE_GENOMIC_POINTS_ = CreateConsequent( LARGE_GENOMIC_POINTS );
   // for the SD sets:
   LARGE_SD_GENOMIC_POINTS = CreateFuzzySet( 0, 400, 800, true );
   _LARGE_SD_GENOMIC_POINTS_ = CreateConsequent( LARGE_SD_GENOMIC_POINTS );
} // end FuzzyComparatoR default constructor

//***********************************************************************************************
// Class        : FuzzyComparatoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Destructor
// Purpose	: Performs the destruction actions.
//***********************************************************************************************
FuzzyComparatoR::~FuzzyComparatoR()
{
} // end FuzzyComparatoR destructor

//***********************************************************************************************
// Class        : FuzzyComparatoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Setup
// Purpose	: Performs the basic setup actions.
//***********************************************************************************************
void FuzzyComparatoR::Setup()
{
} // end Setup

//***********************************************************************************************
// Class        : FuzzyComparatoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: FireRules
// Purpose	:
//***********************************************************************************************
void FuzzyComparatoR::FireRules()
{
} // end FireRules

//***********************************************************************************************
// Class        : FuzzyComparatoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Defuzzify
// Purpose	:
//***********************************************************************************************
long double FuzzyComparatoR::Defuzzify()
{
   return 0;
} // end Defuzzify

//***********************************************************************************************
// Class        : FuzzyComparatoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: IsLargeLinkageDifference
// Purpose	:
//***********************************************************************************************
long double FuzzyComparatoR::IsLargeLinkageDifference( long double first, long double second )
{
   long double result = (first - second);
   long double temp = 0;

   if( result < 0 )
   {
      result *= -1;
   }

   (*LARGE_LINKAGE_DIFFERENCE << result) >> temp;
   (*_LARGE_GENOMIC_POINTS_ << temp) >> result;

   return result;
}

//***********************************************************************************************
// Class        : FuzzyComparatoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: IsLargeSemanticDifference
// Purpose	: Measures the difference in genetic points between two semantic vectors.
//***********************************************************************************************
long double FuzzyComparatoR::IsLargeSemanticDifference( VectoR* first, VectoR* second )
{
   long double result = 0;
   long double temp = 0;

   for( int i=0; i<first->cnRows; i++ )
   {
      temp = first->pVariables[i] - second->pVariables[i];
      if( temp < 0 )
      {
         temp *= -1;
      }
      result += temp;
   }

   (*LARGE_SEMANTIC_DIFFERENCE << result) >> temp;
   (*_LARGE_SD_GENOMIC_POINTS_ << temp) >> result;

   return result;
}

//***********************************************************************************************
// Class        : FuzzyComparatoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: IsLargeNullLinkCount
// Purpose	:
//***********************************************************************************************
long double FuzzyComparatoR::IsLargeNullLinkCount( int count )
{
   long double result = (long double)count;
   long double temp = 0;

   (*LARGE_NULL_LINK_COUNT << result) >> temp;
   (*_LARGE_GENOMIC_POINTS_ << temp) >> result;

   return result;
}
