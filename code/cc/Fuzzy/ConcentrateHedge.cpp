//***********************************************************************************************
// File     : ConcentrateHedge.cpp
// Purpose  : Defines the derived ConcentrateHedgE class. This class will perform the "concentrating"
//          : operations to an already existing fuzzy set.
//          : These hedges operate basically the same as any others except in the
//          : functionality of "Setup" and "EvaluateIt".
//          : 
// Author   : Brandon Benham 
// Date     : 1/16/00
//***********************************************************************************************
#include"ConcentrateHedge.hpp"

//***********************************************************************************************
// Class    : ConcentrateHedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Declares the ConcentrateHedgE class. Calls Setup.
//***********************************************************************************************
ConcentrateHedgE::ConcentrateHedgE()
{
//CHFPRINT( "ConcentrateHedgE::ConcentrateHedgE()" );

   Setup( VERY );
} // end ConcentrateHedgE constructor


//***********************************************************************************************
// Class    : ConcentrateHedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Does the old destruct stuff.
//***********************************************************************************************
ConcentrateHedgE::~ConcentrateHedgE()
{
//CHFPRINT( "ConcentrateHedgE::~ConcentrateHedgE()" );

} // end ConcentrateHedgE destructor

//***********************************************************************************************
// Class    : ConcentrateHedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Constructor
// Purpose  : Constructs the "real" object.
//***********************************************************************************************
ConcentrateHedgE::ConcentrateHedgE( ConcentrateType CONType )
{
//CHFPRINT( "ConcentrateHedgE::ConcentrateHedgE( ConcentrateType )" );

   Setup( CONType );
} // end constructor

//***********************************************************************************************
// Class    : ConcentrateHedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : We construct the power variable via the enum passed in.
//***********************************************************************************************
void ConcentrateHedgE::Setup( ConcentrateType CONType )
{
//CHFPRINT( "ConcentrateHedgE::Setup(ConcentrateType CONType)" );

   ldPower_Variable = (long double)(CONType);
   Fct1The_Hedge_Function = new OnevariablE( _OVERFLOW_ );

   //CHFPRINT << "Power var is: " << ldPower_Variable;
} // end Setup

//***********************************************************************************************
// Class    : ConcentrateHedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : EvaluateIt
// Purpose  : Overrides the FuzzySet class' EvaluateIt
// Notes    : Performs:
//          :   m(x)^n
//          : Where n is the power variable and m(x) is the membership function.
//***********************************************************************************************
long double ConcentrateHedgE::EvaluateIt( long double ldArg )
{
//CHFPRINT( "ConcentrateHedgE::EvaluateIt( long double ldArg )" );
long double ldRes = 0;

   (*FzsNext_Set << ldArg) >> ldRes;

   ldRes = Fct1The_Hedge_Function->DecimalPower( ldRes, ldPower_Variable );
      
   return ldRes;
} // end EvaluateIt

//***********************************************************************************************
// Class    : ConcentrateHedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Operator <<
// Purpose  : Overrides the FuzzySet class' << operator.
//***********************************************************************************************
ConcentrateHedgE& ConcentrateHedgE::operator << ( const long double& ldRhs )
{
//CHFPRINT( "ConcentrateHedgE::operator << ( const long double& ldRhs )" );
long double ld1 = 0;

   ld1 = EvaluateIt( ldRhs );
   //CHFPRINT << "ConcentrateHedgE value is:" << ld1;
   SetTruthValue( ld1 );
   return *this;
} // end ConcentrateHedgE
