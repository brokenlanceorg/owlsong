//***********************************************************************************************
// File     : MembershipFunc.cpp
// Purpose  : Defines our derived class MembershipFunc, which incorporates the operations of the
//          : OnevariablE class and the DomaiN class.
// Author   : Brandon Benham 
// Date     : 1/6/00
//***********************************************************************************************
#include"MembershipFunc.hpp"

//***********************************************************************************************
// Class    : MembershipFunctioN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Declares the MembershipFunctioN class.
//***********************************************************************************************
MembershipFunctioN::MembershipFunctioN() : OnevariablE(_OVERFLOW_)
{
//MemFPRINT( "MembershipFunctioN::MembershipFunctioN( )" );
   Setup();
} // end MembershipFunctioN default constructor

//***********************************************************************************************
// Class    : MembershipFunctioN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Constructor
// Purpose  : This one constructs a Bell-curve function
// Notes    : ld2 is the middle point of the curve, while ld1 is the width
//          : of the base of the curve.
//***********************************************************************************************
MembershipFunctioN::MembershipFunctioN( long double ld1, long double ld2 ) : OnevariablE(_OVERFLOW_)
{
//MemFPRINT( "MembershipFunctioN::MembershipFunctioN( long double ld1, long double ld2 )" );
long double ldLength = 0;

   Setup();
   ldLength = ld1 / 2;
   //MemFPRINT << "ldLength is:" << ldLength;
   Dthe_Domain = new DomaiN( ld2 - ldLength, ld2 + ldLength );

   // Our Gaussian equation number is 26
   SetEquation( 26 );
   SetParams( ld1, ld2 );
} // end MembershipFunctioN constructor

//***********************************************************************************************
// Class    : MembershipFunctioN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Constructor
// Purpose  : This one constructs a Sigmoid-curve function
// Notes    : The first and third params tells us where m(x) = 0|1, while
//          : the second param tells us where m(x) = 1/2, and bool tells
//          : us whether we have a monotonically increasing or decreasing function.
//***********************************************************************************************
MembershipFunctioN::MembershipFunctioN(   long double ld1, 
                                          long double ld2,
                                          long double ld3,
                                          bool bIncreasing ) : OnevariablE(_OVERFLOW_)
{
//MemFPRINT( "MembershipFunctioN::MembershipFunctioN( Sigmoid )" );

   Setup();
   bIs_Increasing = bIncreasing;
   bIs_Sigmoid = true;
   Dthe_Domain = new DomaiN( ld1, ld3 );
   // Our Sigmoid equation number is 28
   SetEquation( 28 );
   SetParams( ld1, ld2, ld3 );
   // The InverseSigmoid is eq 39
   _theInverse = new OnevariablE( _OVERFLOW_ );
   _theInverse->SetEquation( 39 );
   _theInverse->SetParams( ld1, ld2, ld3 );
} // end MembershipFunctioN constructor

//***********************************************************************************************
// Class    : MembershipFunctioN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Constructor
// Purpose  : This one constructs a Linear function
// Notes    : Respectively, we have (x1, y1) and (x2, y2) from which we can
//          : compute y(x) = m * x + b
//***********************************************************************************************
MembershipFunctioN::MembershipFunctioN(   long double ldX1, 
                                          long double ldY1,
                                          long double ldX2,
                                          long double ldY2 ) : OnevariablE(_OVERFLOW_)
{
//MemFPRINT( "MembershipFunctioN::MembershipFunctioN( Linear )" );
long double ldTemp = 0;
long double ldSlope = 0;

   Setup();
   Dthe_Domain = new DomaiN( ldX1, ldX2 );
   ldTemp = ldX2 - ldX1;
   if( Absolute( ldTemp ) <= GetEpsilon() )
   {
      // throw an error
      return; // for now
   } // end if
   ldSlope = ( ldY2 - ldY1 ) / ldTemp;
   if ( ldSlope < 0 ) {
      //MemFPRINT << "The line is DECREASING";
      bIs_Increasing = false; }
   else {
      //MemFPRINT << "The line is INCREASING";
      bIs_Increasing = true; }
   ldTemp = ldY1 - (ldSlope * ldX1);
   SetParams( ldSlope, ldTemp );
   // Our Linear equation number is 27
   SetEquation( 27 );
} // end MembershipFunctioN constructor

//***********************************************************************************************
// Class    : MembershipFunctioN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Destroys the object.
//***********************************************************************************************
MembershipFunctioN::~MembershipFunctioN()
{
//MemFPRINT( "MembershipFunctioN::~MembershipFunctioN( )" );

   if ( Dthe_Domain != 0 )
   {
      delete Dthe_Domain;
      Dthe_Domain = 0;
   } // end if not null

   if( _theInverse != 0 )
   {
      delete _theInverse;
   } // end if not null
} // end MembershipFunctioN destructor

//***********************************************************************************************
// Class    : MembershipFunctioN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setting up of memory etc...
//          : 
//***********************************************************************************************
void MembershipFunctioN::Setup()
{
//MemFPRINT( "MembershipFunctioN::Setup( )" );

   Dthe_Domain    = 0;
   bIs_Increasing = true;
   bIs_Sigmoid    = false;
   _theInverse = 0;
} // end Setup

//***********************************************************************************************
// Class    : MembershipFunctioN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : EvaluatIt
// Purpose  : Evaluates the function using the overridden EvaluateIt from 
//          : OnevariablE.
//***********************************************************************************************
long double MembershipFunctioN::EvaluateIt( long double ldThe_Var )
{
//MemFPRINT( "MembershipFunctioN::EvaluateIt( long double )" );
long double ldReturn = 0;           

   SetVariable( ldThe_Var );
   if( bIs_Sigmoid )
   {
      if( bIs_Increasing )
         ldReturn = OnevariablE::EvaluateIt();
      else
         ldReturn = 1 - OnevariablE::EvaluateIt();
   } else // end if
      ldReturn = OnevariablE::EvaluateIt();

   return ldReturn;
} // end EvaluateIt

//***********************************************************************************************
// Class    : MembershipFunctioN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : PerformAND
// Purpose  : Performs the Mean-AND operation:
//          : m = (A + B) / 2
//***********************************************************************************************
long double MembershipFunctioN::PerformAND( long double ldA, long double ldB )
{
long double ldANDRet = 0;

   ldANDRet = ( ldA + ldB ) / 2;

   return ldANDRet;
} // end PerformAND

//***********************************************************************************************
// Class    : MembershipFunctioN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : PerformOR
// Purpose  : Performs the Mean-OR operation:
//          : m = ( min(A,B) + 2*max(A,B) ) / 3
//***********************************************************************************************
long double MembershipFunctioN::PerformOR( long double ldA, long double ldB )
{
long double ldANDRet = 0;

   if( Find_Max(ldA, ldB) )
      ldANDRet = ( ldB + 2 * ldA ) / 3;
   else
      ldANDRet = ( ldA + 2 * ldB ) / 3;

   return ldANDRet;
} // end PerformOR

//***********************************************************************************************
// Class    : MembershipFunctioN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : InverseIt
// Purpose  : Evaluates the function using the overridden EvaluateIt from
//          : OnevariablE.
//***********************************************************************************************
long double MembershipFunctioN::InverseIt( long double ldThe_Var )
{
long double ldReturn = 0;


   if( bIs_Sigmoid )
   {
      if( bIs_Increasing )
      {
         _theInverse->SetVariable( ldThe_Var );
         ldReturn = _theInverse->EvaluateIt();
      }
      else
      {
         _theInverse->SetVariable( 1 - ldThe_Var );
         ldReturn = _theInverse->EvaluateIt();
      }
   }
   else // end if
      ldReturn = OnevariablE::EvaluateIt();

return ldReturn;
} // end InverseIt


