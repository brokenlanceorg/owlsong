//***********************************************************************************************
// File     : FuzzyFactory.cpp
// Purpose  : Declares the FuzzyFactory interface which is used by the 
//          : FuzzyBaseClass to effortlessly and blithely create fuzzy
//          : objects such as Fuzzy Sets, Hedges, and Consequents.
// Author   : Brandon Benham 
// Date     : 1/30/00
//***********************************************************************************************

#include "FuzzyFactory.hpp"          

#include <iostream>

//***********************************************************************************************
// Class    : FuzzyfactorY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
FuzzyfactorY::FuzzyfactorY()
{
   Setup();
} // end FuzzyfactorY default constructor

//***********************************************************************************************
// Class    : FuzzyfactorY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
FuzzyfactorY::~FuzzyfactorY()
{
//cout << "in fuzzy factory dest...." << endl;
//cout << " the size is: " << _theFuzzySets.size() << endl;
//system( "PAUSE" );

   for( int i=0; i<_theConsequentSets.size(); i++ )
   {
//cout << "in fuzzy factory dest consqeuent i: " << i << endl;
//system( "PAUSE" );
      ConsequenT* aSet = _theConsequentSets.at( i );
      delete aSet;
   }

   _theConsequentSets.clear();

   for( int i=0; i<_theFuzzySets.size(); i++ )
   {
//cout << "in fuzzy factory dest i: " << i << endl;
      FuzzyseT* aSet = _theFuzzySets.at( i );
//cout << "in fuzzy factory dest the set is: " << aSet << endl;
//system( "PAUSE" );
      delete aSet;
   }

   _theFuzzySets.clear();

} // end FuzzyfactorY destructor

//***********************************************************************************************
// Class    : FuzzyfactorY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void FuzzyfactorY::Setup()
{
} // end Setup

//***********************************************************************************************
// Class    : FuzzyfactorY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : CreateFuzzySet
// Purpose  : Creates a Gaussian distribution set, the first arg is the width of the base
//          : while the second is the midpoint, the boolean indicates the use of cuts.
//***********************************************************************************************
FuzzyseT* FuzzyfactorY::CreateFuzzySet( long double ldBase, long double ldCenter, bool bCuts )
{
FuzzyseT* fzz_A_Fuzzy_Set = 0;

   fzz_A_Fuzzy_Set = new FuzzyseT( ldBase, ldCenter, bCuts );
   _theFuzzySets.push_back( fzz_A_Fuzzy_Set );
   
   return fzz_A_Fuzzy_Set;
} // end CreateFuzzySet

//***********************************************************************************************
// Class    : FuzzyfactorY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : CreateFuzzySet
// Purpose  : Creates a Sigmoid fuzzy set, the first arg is where the function is zero or one, while
//          : the second is where the function is 1/2, and the third tells us where the the
//          : function is zero or one, the boolean indicates if the function is increasing.
//***********************************************************************************************
FuzzyseT* FuzzyfactorY::CreateFuzzySet( long double ldX1, long double ldM, long double ldX2, bool bInc )
{
FuzzyseT* fzz_A_Fuzzy_Set = 0;

   fzz_A_Fuzzy_Set = new FuzzyseT( ldX1, ldM, ldX2, bInc );
   _theFuzzySets.push_back( fzz_A_Fuzzy_Set );
   
   return fzz_A_Fuzzy_Set;
} // end CreateFuzzySet

//***********************************************************************************************
// Class    : FuzzyfactorY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : CreateFuzzySet
// Purpose  : Creates a Linear fuzzy set, the arguments are self-explanatory; they denote
//          : a line from point (x1, y1) to (x2, y2).
//***********************************************************************************************
FuzzyseT* FuzzyfactorY::CreateFuzzySet( long double ldX1, long double ldY1, long double ldX2, long double ldY2 )
{
FuzzyseT* fzz_A_Fuzzy_Set = 0;

   fzz_A_Fuzzy_Set = new FuzzyseT( ldX1, ldY1, ldX2, ldY2 );
   _theFuzzySets.push_back( fzz_A_Fuzzy_Set );
   
   return fzz_A_Fuzzy_Set;
} // end CreateFuzzySet

//***********************************************************************************************
// Class    : FuzzyfactorY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : CreateRestrictHedge
// Purpose  : Creates a Restrict Hedge based on the type passed in. The types for a Restrict
//          : Hedge are as follows:
//          :    NOT   - The negation of a fuzzy set.
//          :    ABOVE - The upper half of the fuzzy set.
//          :    BELOW - True if in the lower half of the fuzzy set.
//***********************************************************************************************
RestrictHedgE* FuzzyfactorY::CreateRestrictHedge( RestrictHedgE::RestrictType theType )
{
RestrictHedgE* hdg_A_Hedge = 0;

   hdg_A_Hedge = new RestrictHedgE( theType );
   _theFuzzySets.push_back( hdg_A_Hedge );

   return hdg_A_Hedge;
} // end CreateRestrictHedge

//***********************************************************************************************
// Class    : FuzzyfactorY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : CreateConcentrateHedgE
// Purpose  : The concentrate types are as follows:
//          :     VERY      - the squared fuzzy set.
//          :     EXTREMELY - the cubed fuzzy set.
//***********************************************************************************************
ConcentrateHedgE* FuzzyfactorY::CreateConcentrateHedge( ConcentrateHedgE::ConcentrateType theType )
{
ConcentrateHedgE* hdg_A_Hedge = 0;

   hdg_A_Hedge = new ConcentrateHedgE( theType );
   _theFuzzySets.push_back( hdg_A_Hedge );

   return hdg_A_Hedge;
} // end CreateConcentrateHedgE

//***********************************************************************************************
// Class    : FuzzyfactorY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : CreateDiffuseHedgE
// Purpose  : The types for Diffuse hedge are as follows:
//          :     GENERALLY - Makes more fuzzy by factor of 2.
//          :     USUALLY   - Makes more fuzzy by factor of 3.
//          :     MOSTLY    - Makes more fuzzy by factor of 4.
//***********************************************************************************************
DiffuseHedgE* FuzzyfactorY::CreateDiffuseHedge( DiffuseHedgE::DiffuseType theType )
{
DiffuseHedgE* hdg_A_Hedge = 0;

   hdg_A_Hedge = new DiffuseHedgE( theType );
   _theFuzzySets.push_back( hdg_A_Hedge );

   return hdg_A_Hedge;
} // end CreateDiffuseHedgE

//***********************************************************************************************
// Class    : FuzzyfactorY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : CreateDiluteHedgE
// Purpose  : The types for the Dilute hedge are as follows:
//          :     SOMEWHAT - Opposite of concentrate by factor of 2.
//          :     RATHER   - Opposite of concentrate by factor of 3.
//          :     QUITE    - Opposite of concentrate by factor of 4.
//***********************************************************************************************
DiluteHedgE* FuzzyfactorY::CreateDiluteHedge( DiluteHedgE::DiluteType theType )
{
DiluteHedgE* hdg_A_Hedge = 0;

   hdg_A_Hedge = new DiluteHedgE( theType );
   _theFuzzySets.push_back( hdg_A_Hedge );

   return hdg_A_Hedge;
} // end CreateDiluteHedgE

//***********************************************************************************************
// Class    : FuzzyfactorY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : CreateIntenseHedgE
// Purpose  : The type for the Intense hedge are as follows:
//          :      POSITIVELY - Make less fuzzy by a factor of 2.
//          :      DEFINITELY - Make less fuzzy by a factor of 3. 
//          :      ABSOUTELY  - Make less fuzzy by a factor of 4.
//***********************************************************************************************
IntenseHedgE* FuzzyfactorY::CreateIntenseHedge( IntenseHedgE::IntenseType theType )
{
IntenseHedgE* hdg_A_Hedge = 0;

   hdg_A_Hedge = new IntenseHedgE( theType );
   _theFuzzySets.push_back( hdg_A_Hedge );

   return hdg_A_Hedge;
} // end CreateIntenseHedgE

//***********************************************************************************************
// Class    : FuzzyfactorY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : CreateConsequenT
// Purpose  : Creates a consequent based on the Fuzzy set pased in.
//          : 
//***********************************************************************************************
ConsequenT* FuzzyfactorY::CreateConsequent( FuzzyseT* theSet )
{
ConsequenT* con_A_Cons = 0;

   con_A_Cons = new ConsequenT( theSet );
   _theConsequentSets.push_back( con_A_Cons );

   return con_A_Cons;
} // end CreateConsequenT
