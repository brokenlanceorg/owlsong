//***********************************************************************************************
// File     : FuzzyStockConfidence.cpp
// Purpose  : 
//          : 
// Author   : Brandon Benham 
// Date     : 2/24/01
//***********************************************************************************************

#include"FuzzyStockConfidence.hpp"          
#include"FileReader.hpp"          

//***********************************************************************************************
// Class    : FuzzyStockConfidencE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
FuzzyStockConfidencE::FuzzyStockConfidencE() : FuzzybasE()
{
   Setup();
} // end FuzzyStockConfidencE default constructor

//***********************************************************************************************
// Class    : FuzzyStockConfidencE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
FuzzyStockConfidencE::~FuzzyStockConfidencE()
{
} // end FuzzyStockConfidencE destructor

//***********************************************************************************************
// Class    : FuzzyStockConfidencE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void FuzzyStockConfidencE::Setup()
{
   SMALL_VARIANCE_RANK  = CreateFuzzySet( 0, 100, 200, false );
   SMALL_STABILITY_RANK = CreateFuzzySet( 0, 70, 140, false );
//   VERY_SMALL_STABILITY_RANK =  CreateConcentrateHedge( ConcentrateHedgE::VERY );
   
//   *VERY_SMALL_STABILITY_RANK + *SMALL_STABILITY_RANK;
//   SMALL_PREVIOUS_RANK = CreateFuzzySet(  -10, 110, 220, false );
} // end Setup

//***********************************************************************************************
// Class    : FuzzyStockConfidencE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : FireRules
// Purpose  : Fires our one and only rule:
//          :   If VARIANCE RANK is small AND STABILITY RANK is small, then confidence is high
//          :   New Rule:
//          :   If VARIANCE RANK is small AND STABILITY RANK is small AND PREVIOUS RANK is small,
//          :     then confidence is high
//          :   New Rule:
//          :   If VARIANCE RANK is small AND STABILITY RANK is VERY small, then confidence is high
//***********************************************************************************************
void FuzzyStockConfidencE::FireRules()
{
//   ( (*SMALL_VARIANCE_RANK << ldVariance) & (*SMALL_STABILITY_RANK << ldStability) & 
//     (*SMALL_PREVIOUS_RANK << ldPrevious) ) >> ldConfidence;

//   ( (*SMALL_VARIANCE_RANK << ldVariance) & (*VERY_SMALL_STABILITY_RANK << ldStability) ) >> ldConfidence;
             
   // Use old rule:   
   ( (*SMALL_VARIANCE_RANK << ldVariance) & (*SMALL_STABILITY_RANK << ldStability) ) >> ldConfidence;
} // end FileRules

//***********************************************************************************************
// Class    : FuzzyStockConfidencE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : OutputConfidences
// Purpose  : 
//          : 
//***********************************************************************************************
void FuzzyStockConfidencE::OutputConfidences()
{
   FilereadeR* theFile = new FilereadeR( "output.rank" );
   ofstream theOutputFile( "confidence.dat" );
   char* theWord = '\0';

   while( (theWord = theFile->GetNextWord()) != '\0' )
   {
      ldVariance = _atold( theFile->GetNextWord() );
      ldStability = _atold( theFile->GetNextWord() );
      //ldPrevious = _atold( theFile->GetNextWord() );

      FireRules();
      theOutputFile << theWord;
      theOutputFile << " ";
      theOutputFile << ldConfidence;
      theOutputFile << "\n";
   } // end while

   theOutputFile << flush;
   theOutputFile.close();
   
   delete theFile;   
} // end OutputConfidences
