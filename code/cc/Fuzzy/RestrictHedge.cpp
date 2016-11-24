//***********************************************************************************************
// File     : RestrictHedge.cpp
// Purpose  : Defines the derived RestrictHedgE class. This class will perform the "intensifying"
//          : operations to an already existing fuzzy set.
//          :
//          : These hedges operate basically the same as any others except in the
//          : functionality of "Setup" and "EvaluateIt".
//          :
//          : 
// Author   : Brandon Benham 
// Date     : 1/14/00
//***********************************************************************************************
#include"RestrictHedge.hpp"

//***********************************************************************************************
// Class    : RestrictHedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Declares the RestrictHedgE class. Calls Setup.
//***********************************************************************************************
RestrictHedgE::RestrictHedgE()
{
//ResFPRINT( "RestrictHedgE::RestrictHedgE()" );

   Setup( NOT );
} // end RestrictHedgE constructor

//***********************************************************************************************
// Class    : RestrictHedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Does the old destruct stuff.
//***********************************************************************************************
RestrictHedgE::~RestrictHedgE()
{
//ResFPRINT( "RestrictHedgE::~RestrictHedgE()" );

} // end RestrictHedgE destructor

//***********************************************************************************************
// Class    : RestrictHedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Constructor
// Purpose  : Constructs the "real" object.
//***********************************************************************************************
RestrictHedgE::RestrictHedgE( RestrictType RType )
{
//ResFPRINT( "RestrictHedgE::RestrictHedgE( RestrictType )" );

   Setup( RType );
} // end constructor

//***********************************************************************************************
// Class    : RestrictHedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : We construct the power variable via the enum passed in.
//***********************************************************************************************
void RestrictHedgE::Setup( RestrictType RType )
{
//ResFPRINT( "RestrictHedgE::Setup(RestrictType RType)" );

   Restrict_Type = RType;
   Fct1The_Hedge_Function = new OnevariablE( _OVERFLOW_ );
} // end Setup

//***********************************************************************************************
// Class    : RestrictHedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : EvaluateIt
// Purpose  : Overrides the FuzzySet class' EvaluateIt
// Notes    : 
//          :
//          :
//          :
//          : Where n is the power variable and m(x) is the membership function.
//***********************************************************************************************
long double RestrictHedgE::EvaluateIt( long double ldArg )
{
//ResFPRINT( "RestrictHedgE::EvaluateIt( long double ldArg )" );
long double ldRes = 0;

   //ResFPRINT << "Midpoint is:" << FzsNext_Set->GetMidPoint();

   switch( Restrict_Type )
   {
      case NOT :
         (*FzsNext_Set << ldArg) >> ldRes;
         ldRes = 1 - ldRes;
      break;
      
      case ABOVE :
         if( ldArg >= FzsNext_Set->GetMidPoint() )
         {
            (*FzsNext_Set << ldArg) >> ldRes;
            ldRes = 1 - ldRes;
         } else
            ldRes = 0;
      break;
      
      case BELOW :
         if( ldArg <= FzsNext_Set->GetMidPoint() )
         {
            (*FzsNext_Set << ldArg) >> ldRes;
            ldRes = 1 - ldRes;
         } else
            ldRes = 0;
      break;
      
      default :
         (*FzsNext_Set << ldArg) >> ldRes;
      break;
   } // end switch

   return ldRes;
} // end EvaluateIt

//***********************************************************************************************
// Class    : RestrictHedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Operator <<
// Purpose  : Overrides the FuzzySet class' << operator.
//***********************************************************************************************
RestrictHedgE& RestrictHedgE::operator << ( const long double& ldRhs )
{
//ResFPRINT( "RestrictHedgE::operator << ( const long double& ldRhs )" );
long double ld1 = 0;

   ld1 = EvaluateIt( ldRhs );
   //ResFPRINT << "RestrictHedgE value is:" << ld1;
   SetTruthValue( ld1 );
   return *this;
} // end RestrictHedgE
