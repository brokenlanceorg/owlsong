//***********************************************************************************************
// File     : FuzzySet.cpp
// Purpose  : Defines our base class FuzzyseT. This class contians a MembershipFunctioN
//          : object to evaluate the truth value for a given input. This class also
//          : handles the alpha and omega cut routines. This base class also defines
//          : the overloaded operator behavior. Thus, all class that derive from this
//          : class will inherit this behavior or they can override it if necessary.
// Author   : Brandon Benham 
// Date     : 1/6/00
//***********************************************************************************************

#include "FuzzySet.hpp"
#include <iostream>

//***********************************************************************************************
// Class    : FuzzyseT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Declares the FuzzyseT class.
//***********************************************************************************************
FuzzyseT::FuzzyseT()      
{
   Setup();
   MemThe_Function = new MembershipFunctioN();
} // end FuzzyseT default constructor

//***********************************************************************************************
// Class    : FuzzyseT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Destoys stuff.
//***********************************************************************************************
FuzzyseT::~FuzzyseT()      
{
//cout << "In fuzzy set destoructor!!!" << endl;
   if( MemThe_Function != 0 )
   {
      delete MemThe_Function;
      MemThe_Function = 0;
   } // end if not null
} // end FuzzyseT default constructor

//***********************************************************************************************
// Class    : FuzzyseT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Constructor
// Purpose  : Declares a Bell-shaped curve instance of the FuzzyseT class.
// Notes    : The first arg denotes the width of the base, while the second
//          : arg indicated the midpoint, i.e., where m(x) = 1.
//***********************************************************************************************
FuzzyseT::FuzzyseT( long double ldP1, long double ldP2, bool bUseCuts )
{
//FuzzFPRINT("FuzzyseT::FuzzyseT(long double ldP1, long double ldP2 )");
long double ldVal = 0;

   Setup();
   MemThe_Function = new MembershipFunctioN( ldP1, ldP2 );
   if( bUseCuts )
   {
      ldVal = MemThe_Function->GetLeftEndpoint();
      //FuzzFPRINT << "Setting alpha cut for point:" << ldVal;
      SetAlphaCut( ldVal, 0 );
      ldVal = MemThe_Function->GetRightEndpoint();
      //FuzzFPRINT << "Setting omega cut for point:" << ldVal;
      SetOmegaCut( MemThe_Function->GetRightEndpoint(), 0 );
   } // end if useCuts
} // end FuzzyseT Bell-shaped constructor

//***********************************************************************************************
// Class    : FuzzyseT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Constructor
// Purpose  : Declares a Sigmoid-shaped curve instance of the FuzzyseT class.
// Notes    : The first arg denotes where the memFunc is 0|1, the second where
//          : the memFunc is 1/2, and the third where the memFunc is 0|1, while
//          : the boolean tells us whether it's increasing or decreasing.
//***********************************************************************************************
FuzzyseT::FuzzyseT( long double ld1, long double ld2, long double ld3, bool bI )
{
//FuzzFPRINT("FuzzyseT::FuzzyseT(ld1, ld2, ld3, bI)");

   Setup();
   MemThe_Function = new MembershipFunctioN( ld1, ld2, ld3, bI );
   _isIncreasing = bI;
   if( _isIncreasing )
   {
      //FuzzFPRINT << "Setting Cuts for INCREASING case";
      SetAlphaCut( ld1, 0 );
      SetOmegaCut( ld3, 1 );
   } else
   {
      //FuzzFPRINT << "Setting Cuts for DECREASING case";
      SetAlphaCut( ld1, 1 );                       
      SetOmegaCut( ld3, 0 );
   } // end else if increasing
} // end FuzzyseT Bell-shaped constructor

//***********************************************************************************************
// Class    : FuzzyseT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Constructor
// Purpose  : Declares a Linear curve instance of the FuzzyseT class.
// Notes    : Constructs a linear fuzzySet such that the linear manifold passes through
//          : the points (ldX1, ldY1) and (ldX2, ldY2)
//          : We always use Cuts in this scenario.
//***********************************************************************************************
FuzzyseT::FuzzyseT( long double ldX1, long double ldY1, long double ldX2, long double ldY2 )
{
//FuzzFPRINT("FuzzyseT::FuzzyseT(ldX1, ldY1, ldX2, ldY2)");

   Setup();                
   MemThe_Function = new MembershipFunctioN( ldX1, ldY1, ldX2, ldY2 );
   if( MemThe_Function->IsIncreasing() )
   {
      //FuzzFPRINT << "In FuzzySet, the line is INCREASING";
      SetAlphaCut( ldX1, 0 );
      SetOmegaCut( ldX2, 1 );
   } else // end if
   {
      //FuzzFPRINT << "In FuzzySet, the line is DECREASING";
      SetAlphaCut( ldX1, 1 );
      SetOmegaCut( ldX2, 0 );
   } // end else
} // end constructor

//***********************************************************************************************
// Class    : FuzzyseT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup stuff.
//***********************************************************************************************
void FuzzyseT::Setup()
{
//FuzzFPRINT("FuzzyseT::Setup()");

   MemThe_Function   = 0; // Null the pointer out
   ldAlpha           = 0;
   ldAlpha_Value     = 0;
   bHas_Alpha        = false;
   // Omega-cut threshold:
   ldOmega           = 0;
   ldOmega_Value     = 0;
   bHas_Omega        = false;
   // The truth value:
   ldThe_Truth_Value = 0;
   iWhich_Variable   = 0;
   ldThe_AND_Value   = 0;
   ldThe_OR_Value    = 0;
} // end Setup

//***********************************************************************************************
// Class    : FuzzyseT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : EvaluateIt
// Purpose  : This method actually evaluates the membershipFunction. Before we 
// Notes    : call evaluateIt on the membershipFunction, we check the Cuts
//          : in the event that they are set.
//          : 
//***********************************************************************************************
long double FuzzyseT::EvaluateIt( long double ldTheArg )
{
//FuzzFPRINT("FuzzyseT::EvaluateIt( ldTheArg )");
long double ldTheRet = 0;

//DebugloggeR FuzzFPRINT1( "Test", __FILE__, __LINE__  );

   if( bHas_Alpha )
   {
      if( ldTheArg < ldAlpha ) {
//       FuzzFPRINT1 << "Using the Alpha-Cut...";
         ldTheRet = ldAlpha_Value; 
         return ldTheRet;
      } // end if less than
   } // end if Alpha-Cut
   if( bHas_Omega )
   {
      if( ldTheArg > ldOmega ) {
//       FuzzFPRINT1 << "Using the Omega-Cut...";
         ldTheRet = ldOmega_Value; 
         return ldTheRet;
      } // end if less than
   } // end if Omega-Cut 

   ldThe_Truth_Value = MemThe_Function->EvaluateIt( ldTheArg );
   
//   FuzzFPRINT1 << "The result is:" << ldThe_Truth_Value;

   return ldThe_Truth_Value;
} // end EvaluateIt

//***********************************************************************************************
// Class    : FuzzyseT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : GetResultVariable
// Purpose  : Returns the current result variable.
// Notes    : The result variables are the_truth_value, the_AND_value and
//          : the_OR_value
//          : 
//***********************************************************************************************
long double FuzzyseT::GetResultVariable() const
{
//FuzzFPRINT("FuzzyseT::GetResultVariable()");
long double ldArg = 0;

   switch( iWhich_Variable )
   {
      case 0 :
         //FuzzFPRINT << "In the Truth value case.";
         ldArg = ldThe_Truth_Value;
      break;
      
      case 1 :
         //FuzzFPRINT << "In the AND value case.";
         ldArg = ldThe_AND_Value;           
      break;
      
      case 2 :
         //FuzzFPRINT << "In the OR value case.";
         ldArg = ldThe_OR_Value;
      break;

      default :
         //FuzzFPRINT << "In the default value case.";
         ldArg = ldThe_Truth_Value;
      break;
   } // end switch
   return ldArg;
} // end GetResultVariable

//***********************************************************************************************
// Class    : FuzzyseT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Overloaded operator <<
// Purpose  : This operator evaluates the fuzzySet at the given point.
// Notes    : We are actually using the EvaluateIt method, and then
//          : storing the value in ldThe_Truth_Value.
//          : 
//***********************************************************************************************
FuzzyseT& FuzzyseT::operator << ( const long double& ldArg )
{
//FuzzFPRINT("FuzzyseT::operator << ()");

   SetTruthValue( EvaluateIt( ldArg ) );

   return *this;
} // end operator <<

//***********************************************************************************************
// Class    : FuzzyseT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Overloaded operator >>
// Purpose  : This operator outputs the truth value to the right hand side arg.
// Notes    : 
//          : 
//          : 
//***********************************************************************************************
FuzzyseT& FuzzyseT::operator >> ( long double& ldArg )
{
//FuzzFPRINT("FuzzyseT::operator >> (long double)");

   ldArg = GetResultVariable();

   return *this;
} // end operator <<

//***********************************************************************************************
// Class    : FuzzyseT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Overloaded operator &
// Purpose  : This operator performs the AND operation on two FuzzyseTs
// Notes    : We can inherit this class and modify this functionality in another
//          : class that handles the other forms of the operators.
//          : 
//***********************************************************************************************
FuzzyseT& FuzzyseT::operator & ( const FuzzyseT& FzsRHS )
{
   long double ldTmp1 = 0;
   long double ldTmp2 = 0;

   ldTmp1 = FzsRHS.GetResultVariable();
   ldTmp2 = GetResultVariable();
   ldTmp1 = MemThe_Function->PerformAND( ldTmp1, ldTmp2 );
   SetANDValue( ldTmp1 );
   
   return *this;
} // end operator &

//***********************************************************************************************
// Class    : FuzzyseT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Overloaded operator |
// Purpose  : This operator performs the OR operation on two FuzzyseTs
// Notes    : We can inherit this class and modify this functionality in another
//          : class that handles the other forms of the operators.
//          : 
//***********************************************************************************************
FuzzyseT& FuzzyseT::operator | ( const FuzzyseT& FzsRHS )
{
   //FuzzFPRINT("FuzzyseT::operator | FuzzyseT&");
   long double ldTmp1 = 0;
   long double ldTmp2 = 0;

   ldTmp1 = FzsRHS.GetResultVariable();
   ldTmp2 = GetResultVariable();
   //FuzzFPRINT << "The OR variables are: " << ldTmp1 << ldTmp2;
   ldTmp1 = MemThe_Function->PerformOR( ldTmp1, ldTmp2 );
   //FuzzFPRINT << "The OR result is: " << ldTmp1;
   SetORValue( ldTmp1 );
                                                                     
   return *this;
} // end operator |

//***********************************************************************************************
// Class    : FuzzyseT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : InverseIt
// Purpose  : This method actually evaluates the membershipFunction. Before we
// Notes    : call evaluateIt on the membershipFunction, we check the Cuts
//          : in the event that they are set.
//          :
//***********************************************************************************************
long double FuzzyseT::InverseIt( long double ldTheArg )
{
//FPRINT( "FuzzyseT::Inverse" );

long double ldTheRet = 0;

   if( _isIncreasing )
   {
      if( bHas_Alpha )
      {
         if( ldTheArg < ldAlpha_Value )
         {
            ldTheRet = ldAlpha;
            return ldTheRet;
         } // end if less than
      } // end if Alpha-Cut
   
      if( bHas_Omega )
      {
         if( ldTheArg > ldOmega_Value )
         {
            ldTheRet = ldOmega;
            return ldTheRet;
         } // end if less than
      } // end if Omega-Cut
   }
   else
   {
      if( bHas_Alpha )
      {
         if( ldTheArg > ldAlpha_Value )
         {
            ldTheRet = ldAlpha;
            return ldTheRet;
         } // end if less than
      } // end if Alpha-Cut

      if( bHas_Omega )
      {
         if( ldTheArg < ldOmega_Value )
         {
            ldTheRet = ldOmega;
            return ldTheRet;
         } // end if less than
      } // end if Omega-Cut
   }

   // otherwise, we actually evaluate it:
   return MemThe_Function->InverseIt( ldTheArg );
} // end InverseIt
