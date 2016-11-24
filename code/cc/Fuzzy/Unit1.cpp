#include<iostream>
#include "FuzzySet.hpp"
#include "IntenseHedge.hpp"
#include "RestrictHedge.hpp"
#include "ConcentrateHedge.hpp"
#include "DiluteHedge.hpp"
#include "DiffuseHedge.hpp"
#include "Consequent.hpp"
#include "CompoundConsequent.hpp"
#include "DebugLogger.hpp"
#include "FuzzyFactory.hpp"

//***********************************************************************************************
// Class    : FuzzTest
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : This is our first fuzzy logic test. Based on a person's (male) waist size and
//          : height, we predict their weight.
//***********************************************************************************************
void FuncTest()
{
//FPRINT( "FuncTest" );
// Waist sizes:
FuzzyfactorY* theFuzFactory = new FuzzyfactorY();

FuzzyseT* WAIST_SMALL = theFuzFactory->CreateFuzzySet( 20, 27, 35, false );
FuzzyseT* WAIST_LARGE = theFuzFactory->CreateFuzzySet( 30, 37, 45, true  );

// Heights:
FuzzyseT* HEIGHT_SHORT = theFuzFactory->CreateFuzzySet( 57, 63, 69, false );
FuzzyseT* HEIGHT_TALL = theFuzFactory->CreateFuzzySet( 67, 73, 79, true );

// Weights:
FuzzyseT* WEIGHT_LIGHT = theFuzFactory->CreateFuzzySet( 1, 0, true );
FuzzyseT* WEIGHT_MEDIUM = theFuzFactory->CreateFuzzySet( 1, 1, true );
FuzzyseT* WEIGHT_HEAVY = theFuzFactory->CreateFuzzySet( 1, 8, true );
/*
FuzzyseT* WEIGHT_LIGHT = theFuzFactory->CreateFuzzySet( 1, 1, true );
FuzzyseT* WEIGHT_MEDIUM = theFuzFactory->CreateFuzzySet( 1, 15, true );
FuzzyseT* WEIGHT_HEAVY = theFuzFactory->CreateFuzzySet( 1, 8, true );
*/

// Hedges:
ConcentrateHedgE* VERY_HEAVY_WEIGHT = theFuzFactory->CreateConcentrateHedge( ConcentrateHedgE::VERY );
RestrictHedgE* NOT_SHORT_HEIGHT = theFuzFactory->CreateRestrictHedge( RestrictHedgE::NOT );
// This is in the consequents--maybe hedges are supposed to be for antecedents only?
*VERY_HEAVY_WEIGHT + *WEIGHT_HEAVY;
*NOT_SHORT_HEIGHT + *HEIGHT_SHORT;

// Consequents:
ConsequenT* WEIGHT_IS_LIGHT = theFuzFactory->CreateConsequent( WEIGHT_LIGHT );
ConsequenT* WEIGHT_IS_MEDIUM = theFuzFactory->CreateConsequent( WEIGHT_MEDIUM );
ConsequenT* WEIGHT_IS_HEAVY = theFuzFactory->CreateConsequent( WEIGHT_HEAVY );
ConsequenT* WEIGHT_IS_VERY_HEAVY = theFuzFactory->CreateConsequent( VERY_HEAVY_WEIGHT);

// Compound Consequents:
CompoundconsequenT* OUTPUT_WEIGHT = new CompoundconsequenT();
//((((*OUTPUT_WEIGHT + *WEIGHT_IS_LIGHT) + *WEIGHT_IS_MEDIUM) + *WEIGHT_IS_HEAVY) + *WEIGHT_IS_VERY_HEAVY);
//((((*OUTPUT_WEIGHT + *WEIGHT_IS_VERY_HEAVY) + *WEIGHT_IS_HEAVY) + *WEIGHT_IS_MEDIUM) + *WEIGHT_IS_LIGHT);
//(((*OUTPUT_WEIGHT + *WEIGHT_IS_HEAVY) + *WEIGHT_IS_MEDIUM) + *WEIGHT_IS_LIGHT);
//(((*OUTPUT_WEIGHT + *WEIGHT_IS_LIGHT) + *WEIGHT_IS_MEDIUM) + *WEIGHT_IS_HEAVY);
((*OUTPUT_WEIGHT + *WEIGHT_IS_MEDIUM) + *WEIGHT_IS_LIGHT);

long double ldWaist_Size = 8;
long double ldHeight = 27;
long double ldWeight = 0;
long double ldHolder = 100;

   cout << "System is initialized..." << endl;
   cout << "Ready" << endl;
   cout << "Waist Size is: " << ldWaist_Size << endl;
   cout << "Height is: " << ldHeight << endl;

   // Fire our rules*********************************************************************
   //             ############ We need to implement the ">>" operator on fuzzy sets!

   // If waist is small and height is short, then weight is light:
   //( (*WAIST_SMALL << ldWaist_Size) & (*HEIGHT_SHORT << ldHeight) ) >> *WEIGHT_IS_LIGHT; 
   ( ( (*WAIST_SMALL << ldWaist_Size) & (*HEIGHT_SHORT << ldHeight) ) >> ldHolder );

   // do something with ldHolder......
   ldHolder = 100;
   cout << "Weight is Light: " << ldHolder << endl;
   *WEIGHT_IS_LIGHT << ldHolder; 

   // If waist is small and height is tall, then weight is medium:
   //( (*WAIST_SMALL << ldWaist_Size) & (*HEIGHT_TALL << ldHeight) ) >> *WEIGHT_IS_MEDIUM; 
   ( ( (*WAIST_SMALL << ldWaist_Size) & (*HEIGHT_TALL << ldHeight) ) >> ldHolder );

   // do something with ldHolder
   ldHolder = 25;
   cout << "Weight is Medium: " << ldHolder << endl;
   *WEIGHT_IS_MEDIUM << ldHolder;         

   // If waist is large and height is not short, then weight is heavy:
   //( (*WAIST_LARGE << ldWaist_Size) & (*HEIGHT_SHORT << ldHeight) ) >> *WEIGHT_IS_HEAVY; 
   ( ( (*WAIST_LARGE << ldWaist_Size) & (*NOT_SHORT_HEIGHT << ldHeight) ) >> ldHolder );

   // do something with ldHolder
   ldHolder = 1;
   cout << "Weight is Heavy: " << ldHolder << endl;
   *WEIGHT_IS_HEAVY << ldHolder; 

   // If waist is large and height is tall, then weight is very heavy:
   //( (*WAIST_LARGE << ldWaist_Size) & (*HEIGHT_TALL << ldHeight) ) >> *WEIGHT_IS_VERY_HEAVY; 
   ( ( (*WAIST_LARGE << ldWaist_Size) & (*HEIGHT_TALL << ldHeight) ) >> ldHolder );

   // do something with ldHolder
   cout << "Weight is Very Heavy: " << ldHolder << endl;
   *WEIGHT_IS_VERY_HEAVY << ldHolder; 

   // Now, get the actual predicted weight:
   *OUTPUT_WEIGHT >> ldWeight;
   cout << "Weight is: " << ldWeight << endl;
   
   delete OUTPUT_WEIGHT;
   delete theFuzFactory;              

}                   
//---------------------------------------------------------------------------
//---------------------------------------------------------------------------
// Main function
//---------------------------------------------------------------------------
main( int argc, char *argv[] )
{
   cout << "Initializing the fuzzy engine..." << endl;
   FuncTest();
}
