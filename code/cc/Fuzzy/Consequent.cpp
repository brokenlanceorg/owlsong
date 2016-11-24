//***********************************************************************************************
// File     : Consequent.cpp
// Purpose  : Declares the ConsequenT class. This class:
//          : wraps a FuzzyseT or HedgE derivation.
//          : scales a truth value based on antecedent.
//          : performs method "inverse" on itself when it is the only consequent.
//          : provides accessor methods to callers.
//          :
// Author   : Brandon Benham 
// Date     : 1/20/00
//***********************************************************************************************
#include"Consequent.hpp"

//***********************************************************************************************
// Class    : ConsequenT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Declares the ConsequenT class.
//***********************************************************************************************
ConsequenT::ConsequenT()      
{
//ConFPRINT("ConsequenT::ConsequenT()");

   Setup();
} // end ConsequenT default constructor

//***********************************************************************************************
// Class    : ConsequenT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Destroys stuff.
//***********************************************************************************************
ConsequenT::~ConsequenT()      
{
//ConFPRINT("ConsequenT::~ConsequenT()");
} // end ConsequenT destructor

//***********************************************************************************************
// Class    : ConsequenT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Constructor
// Purpose  : Declares the ConsequenT class.
//***********************************************************************************************
ConsequenT::ConsequenT( FuzzyseT* pFzs )      
{
//ConFPRINT("ConsequenT::ConsequenT( FuzzyseT* )");

   Setup();
   FzsThe_Set = pFzs;
} // end ConsequenT constructor

//***********************************************************************************************
// Class    : ConsequenT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Does the usual setup routine
//***********************************************************************************************
void ConsequenT::Setup()      
{
//ConFPRINT("ConsequenT::Setup()");

   FzsThe_Set        = 0;
   ldScale_Variable  = 1;

} // end Setup

//***********************************************************************************************
// Class    : ConsequenT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Overloaded operator <<
// Purpose  : Used to set the FzsThe_set member pointer.
//***********************************************************************************************
ConsequenT& ConsequenT::operator << ( FuzzyseT& FzsRHS )      
{
//ConFPRINT("ConsequenT::operator << ( const FuzzyseT& FzsRHS )");

   if( FzsThe_Set != 0 )
   {
      //ConFPRINT << "FzsThe_Set is already set!";
      //cout << "FzsThe_Set is already set!";
   }
   else
   {
      FzsThe_Set = &FzsRHS;
   }
      
   return *this;
} // end operator <<

//***********************************************************************************************
// Class    : ConsequenT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Overloaded operator <<
// Purpose  : Used to set the ldScale_Variable member.
//***********************************************************************************************
ConsequenT& ConsequenT::operator << ( const long double& ldRHS )      
{
//ConFPRINT("ConsequenT::operator << ( const long double& ldRHS )");

   ldScale_Variable = ldRHS;
   return *this;
} // end operator <<

//***********************************************************************************************
// Class    : ConsequenT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Overloaded operator >>
// Purpose  : Used to call the Inverse method. This is only used whenever the CompoundConsquent
//          : has only one Consequent set.
//***********************************************************************************************
ConsequenT& ConsequenT::operator >> ( long double& ldRHS )      
{
//FPRINT("ConsequenT::operator >> ( long double& ldRHS )");

   ldRHS = FzsThe_Set->InverseIt( ldScale_Variable );
   //FPRINT << "The Inverse value is:" << ldRHS;
   return *this;
} // end operator >>

//***********************************************************************************************
// Class    : ConsequenT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : EvaluateIt
// Purpose  : Evaluates the FuzzySet at this given point, and then scales it according to 
//          : the Scale Variable that the antecedent set.
//***********************************************************************************************
long double ConsequenT::EvaluateIt( const long double ldArg )
{
//ConFPRINT("ConsequenT::EvaluateIt( long double ldArgument )");
long double ldE = 0;

   //ConFPRINT << "the ARG is: " << ldArgument;
   (*FzsThe_Set << ldArg) >> ldE;
   //ConFPRINT << "the ldE is: " << ldE;
        ldE *= ldScale_Variable;
   //ConFPRINT << "the ldE finally is: " << ldE;

   return ldE;
} // end EvaluateIt
