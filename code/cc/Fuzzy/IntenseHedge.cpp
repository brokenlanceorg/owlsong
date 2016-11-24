//***********************************************************************************************
// File     : IntenseHedge.cpp
// Purpose  : Defines the derived IntenseHedgE class. This class will perform the "intensifying"
//          : operations to an already existing fuzzy set.
//          :
//          : These hedges operate basically the same as any others except in the
//          : functionality of "Setup" and "EvaluateIt".
//          :
//          : 
// Author   : Brandon Benham 
// Date     : 1/14/00
//***********************************************************************************************
#include"IntenseHedge.hpp"

//***********************************************************************************************
// Class    : IntenseHedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Declares the IntenseHedgE class. Calls Setup.
//***********************************************************************************************
IntenseHedgE::IntenseHedgE()
{
//IntFPRINT( "IntenseHedgE::IntenseHedgE()" );

   Setup( POSITIVELY );
} // end IntenseHedgE constructor

//***********************************************************************************************
// Class    : IntenseHedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Does the old destruct stuff.
//***********************************************************************************************
IntenseHedgE::~IntenseHedgE()
{
//IntFPRINT( "IntenseHedgE::~IntenseHedgE()" );

} // end IntenseHedgE destructor

//***********************************************************************************************
// Class    : IntenseHedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Constructor
// Purpose  : Constructs the "real" object.
//***********************************************************************************************
IntenseHedgE::IntenseHedgE( IntenseType ITType )
{
//IntFPRINT( "IntenseHedgE::IntenseHedgE( IntenseType )" );

   Setup( ITType );
} // end constructor

//***********************************************************************************************
// Class    : IntenseHedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : We construct the power variable via the enum passed in.
//***********************************************************************************************
void IntenseHedgE::Setup( IntenseType ITType )
{
//IntFPRINT( "IntenseHedgE::Setup(IntenseType ITType)" );

   ldPower_Variable = (long double)(ITType);
   if( ldPower_Variable != 0 )
      ldModified_Power_Variable = 1 / ldPower_Variable;
   else
   {
      //cout << "Caught DIVIDE by ZERO!!!";
   }
      
   Fct1The_Hedge_Function = new OnevariablE( _OVERFLOW_ );

   //IntFPRINT << "Power var is: " << ldPower_Variable;
   //IntFPRINT << "Modified Power var is: " << ldModified_Power_Variable;
} // end Setup

//***********************************************************************************************
// Class    : IntenseHedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : EvaluateIt
// Purpose  : Overrides the FuzzySet class' EvaluateIt
// Notes    : If the result is greater than or equal to 1/2 do:
//          :   m(x)^1/n
//          : Else do:
//          :   m(x)^n
//          : Where n is the power variable and m(x) is the membership function.
//***********************************************************************************************
long double IntenseHedgE::EvaluateIt( long double ldArg )
{
//IntFPRINT( "IntenseHedgE::EvaluateIt( long double ldArg )" );
long double ldRes = 0;

   (*FzsNext_Set << ldArg) >> ldRes;

   if( ldRes >= ONE_HALF )
      ldRes = Fct1The_Hedge_Function->DecimalPower( ldRes, ldModified_Power_Variable );
   else
      ldRes = Fct1The_Hedge_Function->DecimalPower( ldRes, ldPower_Variable );
      
   return ldRes;
} // end EvaluateIt

//***********************************************************************************************
// Class    : IntenseHedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Operator <<
// Purpose  : Overrides the FuzzySet class' << operator.
//***********************************************************************************************
IntenseHedgE& IntenseHedgE::operator << ( const long double& ldRhs )
{
//IntFPRINT( "IntenseHedgE::operator << ( const long double& ldRhs )" );
long double ld1 = 0;

   ld1 = EvaluateIt( ldRhs );
   //IntFPRINT << "IntenseHedgE value is:" << ld1;
   SetTruthValue( ld1 );
   return *this;
} // end IntenseHedgE
