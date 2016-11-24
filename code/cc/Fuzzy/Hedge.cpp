//***********************************************************************************************
// File     : HedgE.cpp
// Purpose  : Defines the derived HedgE class. This class will itself function
//          : as a base class for the other more specific hedges.
//          : Basically, this class manages the use of the pointers to ensure that
//          : they are deleted and nulled out accordingly.
//          : 
// Author   : Brandon Benham 
// Date     : 1/6/00
//***********************************************************************************************

#include"Hedge.hpp"

//***********************************************************************************************
// Class    : HedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Declares the HedgE class. Calls Setup.
//***********************************************************************************************
HedgE::HedgE()
{
//HFPRINT( "HedgE::HedgE()" );

   Setup();
} // end default constructor

//***********************************************************************************************
// Class    : HedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Defines the destructor, deletes the objects if they exist.
//***********************************************************************************************
HedgE::~HedgE()
{
//HFPRINT( "HedgE::~HedgE()" );

// if( FzsNext_Set != 0 ) I don't think we should delete the set
//    delete FzsNext_Set; we should leave that up to the creator
   if( Fct1The_Hedge_Function != 0 )
   {
      delete Fct1The_Hedge_Function;
      Fct1The_Hedge_Function = 0;
   } // end if not null
} // end destructor

//***********************************************************************************************
// Class    : HedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Does the setup stuff: we make sure that the pointers are nulled out. 
//***********************************************************************************************
void HedgE::Setup()
{
//HFPRINT( "HedgE::Setup()" );

   FzsNext_Set                = 0;
   Fct1The_Hedge_Function     = 0;
   ldPower_Variable           = 0;
   ldModified_Power_Variable  = 0;
} // end Setup

//***********************************************************************************************
// Class    : HedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Overloded operator +
// Purpose  : The "hook" up with a fuzzy set or a hedge set.
// Notes : We may need to get rid of the "const" later.
//***********************************************************************************************
HedgE& HedgE::operator + ( FuzzyseT& FzsRHS )
{
//HFPRINT( "HedgE::operator + (const FuzzyseT& FzsRHS)" );

   // Make sure that we don't point to ourselves, which could be fatal.
   if ( this == &FzsRHS )
   {
      return *this; 
   } // end if self
   FzsNext_Set = &FzsRHS;
   
   return *this;
} // end operator +
