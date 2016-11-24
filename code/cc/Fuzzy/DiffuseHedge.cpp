//***********************************************************************************************
// File     : DiffuseHedge.cpp
// Purpose  : Defines the derived DiffuseHedgE class. This class will perform the "intensifying"
//          : operations to an already existing fuzzy set.
//          :
//          : These hedges operate basically the same as any others except in the
//          : functionality of "Setup" and "EvaluateIt".
//          :
//          : 
// Author   : Brandon Benham 
// Date     : 1/14/00
//***********************************************************************************************
#include"DiffuseHedge.hpp"

//***********************************************************************************************
// Class    : DiffuseHedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Declares the DiffuseHedgE class. Calls Setup.
//***********************************************************************************************
DiffuseHedgE::DiffuseHedgE()
{
//DiffFPRINT( "DiffuseHedgE::DiffuseHedgE()" );

   Setup( GENERALLY );
} // end DiffuseHedgE constructor

//***********************************************************************************************
// Class    : DiffuseHedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Does the old destruct stuff.
//***********************************************************************************************
DiffuseHedgE::~DiffuseHedgE()
{
//DiffFPRINT( "DiffuseHedgE::~DiffuseHedgE()" );

} // end DiffuseHedgE destructor

//***********************************************************************************************
// Class    : DiffuseHedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Constructor
// Purpose  : Constructs the "real" object.
//***********************************************************************************************
DiffuseHedgE::DiffuseHedgE( DiffuseType ITType )
{
//DiffFPRINT( "DiffuseHedgE::DiffuseHedgE( DiffuseType )" );

   Setup( ITType );
} // end constructor

//***********************************************************************************************
// Class    : DiffuseHedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : We construct the power variable via the enum passed in.
//***********************************************************************************************
void DiffuseHedgE::Setup( DiffuseType DIFType )
{
//DiffFPRINT( "DiffuseHedgE::Setup(DiffuseType DIFType)" );

   ldPower_Variable = (long double)(DIFType);
   if( ldPower_Variable != 0 )
      ldModified_Power_Variable = 1 / ldPower_Variable;
   else
   {
      //DiffFPRINT << "Caught DIVIDE by ZERO!!!";
      //cout << "Caught DIVIDE by ZERO!!!";
   }
      
   Fct1The_Hedge_Function = new OnevariablE( _OVERFLOW_ );

   //DiffFPRINT << "Power var is: " << ldPower_Variable;
   //DiffFPRINT << "Modified Power var is: " << ldModified_Power_Variable;
} // end Setup

//***********************************************************************************************
// Class    : DiffuseHedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : EvaluateIt
// Purpose  : Overrides the FuzzySet class' EvaluateIt
// Notes    : If the result is greater than or equal to 1/2 do:
//          :   (m(x) - .5)^n + .5
//          : Else do:
//          :   (m(x)^n + .5)^1/n - .5
//          : Where n is the power variable and m(x) is the membership function.
//***********************************************************************************************
long double DiffuseHedgE::EvaluateIt( long double ldArg )
{
//DiffFPRINT( "DiffuseHedgE::EvaluateIt( long double ldArg )" );
long double ldRes = 0;

   (*FzsNext_Set << ldArg) >> ldRes;

   if( ldRes >= ONE_HALF )
   {
      ldRes -= ONE_HALF;
      ldRes = Fct1The_Hedge_Function->DecimalPower( ldRes, ldPower_Variable );
      ldRes += ONE_HALF;
   } else
   {
      ldRes += ONE_HALF;
      ldRes = Fct1The_Hedge_Function->DecimalPower( ldRes, ldModified_Power_Variable );
      ldRes -= ONE_HALF;
   } // end else
      
   return ldRes;
} // end EvaluateIt

//***********************************************************************************************
// Class    : DiffuseHedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Operator <<
// Purpose  : Overrides the FuzzySet class' << operator.
//***********************************************************************************************
DiffuseHedgE& DiffuseHedgE::operator << ( const long double& ldRhs )
{
//DiffFPRINT( "DiffuseHedgE::operator << ( const long double& ldRhs )" );
long double ld1 = 0;

   ld1 = EvaluateIt( ldRhs );
   //DiffFPRINT << "DiffuseHedgE value is:" << ld1;
   SetTruthValue( ld1 );
   return *this;
} // end DiffuseHedge
