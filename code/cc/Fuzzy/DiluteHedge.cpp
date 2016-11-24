//***********************************************************************************************
// File     : DiluteHedge.cpp
// Purpose  : Defines the derived DiluteHedgE class. This class will perform the "diluting"
//          : operations to an already existing fuzzy set.
//          : These hedges operate basically the same as any others except in the
//          : functionality of "Setup" and "EvaluateIt".
//          : 
// Author   : Brandon Benham 
// Date     : 1/16/00
//***********************************************************************************************
#include"DiluteHedge.hpp"

//***********************************************************************************************
// Class    : DiluteHedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Declares the DiluteHedgE class. Calls Setup.
//***********************************************************************************************
DiluteHedgE::DiluteHedgE()
{
//FPRINT( "DiluteHedgE::DiluteHedgE()" );

   Setup( SOMEWHAT );
} // end DiluteHedgE constructor


//***********************************************************************************************
// Class    : DiluteHedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Does the old destruct stuff.
//***********************************************************************************************
DiluteHedgE::~DiluteHedgE()
{
//FPRINT( "DiluteHedgE::~DiluteHedgE()" );

} // end DiluteHedgE destructor

//***********************************************************************************************
// Class    : DiluteHedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Constructor
// Purpose  : Constructs the "real" object.
//***********************************************************************************************
DiluteHedgE::DiluteHedgE( DiluteType DType )
{
//FPRINT( "DiluteHedgE::DiluteHedgE( DiluteType )" );

   Setup( DType );
} // end constructor

//***********************************************************************************************
// Class    : DiluteHedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : We construct the power variable via the enum passed in.
//***********************************************************************************************
void DiluteHedgE::Setup( DiluteType DType )
{
//FPRINT( "DiluteHedgE::Setup(DiluteType DType)" );

   if( DType != 0 )
      ldPower_Variable = 1 / (long double)(DType);
   else 
   {
      ldPower_Variable = 1;
//      FPRINT << "Caught divide by zero!"; 
   } // end else
                      
   Fct1The_Hedge_Function = new OnevariablE( _OVERFLOW_ );

//   FPRINT << "Power var is: " << ldPower_Variable;
} // end Setup

//***********************************************************************************************
// Class    : DiluteHedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : EvaluateIt
// Purpose  : Overrides the FuzzySet class' EvaluateIt
// Notes    : Performs:
//          :   m(x)^n
//          : Where n is the power variable and m(x) is the membership function.
//***********************************************************************************************
long double DiluteHedgE::EvaluateIt( long double ldArg )
{
//FPRINT( "DiluteHedgE::EvaluateIt( long double ldArg )" );
long double ldRes = 0;

   (*FzsNext_Set << ldArg) >> ldRes;

   ldRes = Fct1The_Hedge_Function->DecimalPower( ldRes, ldPower_Variable );
      
   return ldRes;
} // end EvaluateIt

//***********************************************************************************************
// Class    : DiluteHedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Operator <<
// Purpose  : Overrides the FuzzySet class' << operator.
//***********************************************************************************************
DiluteHedgE& DiluteHedgE::operator << ( const long double& ldRhs )
{
//FPRINT( "DiluteHedgE::operator << ( const long double& ldRhs )" );
long double ld1 = 0;

   ld1 = EvaluateIt( ldRhs );
// FPRINT << "DiluteHedgE value is:" << ld1;
   SetTruthValue( ld1 );
   return *this;
} // end DiluteHedgE
