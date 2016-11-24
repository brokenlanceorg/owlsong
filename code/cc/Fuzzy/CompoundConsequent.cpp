//***********************************************************************************************
// File     : CompoundConsequent.cpp
// Purpose  : Declares the CompoundconsequenT class. This class:
//          : Contains an array of consequents, initial size: 20.
//          : Maintains the correct left and right endpoints for the entire set.
//          : Derived from membershipFunction so that the "|" operation is easily accessible.
//          : Keeps track of the number of consequent sets.
//          : Overloades "+" to add consequent sets.
//          : Implements the logic for the Center Of Mass (COM) algorithm.
//          : Contains a variable that defines the granularity of the COM algorithm initially 64.
//          :
// Author   : Brandon Benham 
// Date     : 1/20/00
//***********************************************************************************************

#include "CompoundConsequent.hpp"
#include <iostream>

//***********************************************************************************************
// Class    : CompoundconsequenT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Declares the CompoundconsequenT class.
//***********************************************************************************************
CompoundconsequenT::CompoundconsequenT() : MembershipFunctioN()
{
   Setup();
} // end CompoundconsequenT default constructor

//***********************************************************************************************
// Class    : CompoundconsequenT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Destroys stuff.
//***********************************************************************************************
CompoundconsequenT::~CompoundconsequenT()      
{
/** If using the FuzzyFactory, and you should be, then you don't 
    need to delete here....
   if( ConThe_Consequents != 0 )
   {
      delete[] ConThe_Consequents;
      ConThe_Consequents = 0;
   } // end if not null
*/
   _consequents.clear();
} // end CompoundconsequenT destructor

//***********************************************************************************************
// Class    : CompoundconsequenT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Constructor
// Purpose  : Declares the CompoundconsequenT class.
//***********************************************************************************************
CompoundconsequenT::CompoundconsequenT( ConsequenT* pCon ) : MembershipFunctioN()
{
   Setup();

   _consequents.push_back( pCon );
   ldLeft_Endpoint = pCon->GetLeftEndPoint();
   ldRight_Endpoint = pCon->GetRightEndPoint();
} // end CompoundconsequenT constructor

//***********************************************************************************************
// Class    : CompoundconsequenT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Does the usual setup routine
//***********************************************************************************************
void CompoundconsequenT::Setup()      
{
   ldLeft_Endpoint   = 0;
   ldRight_Endpoint  = 0;
   ldStep_Value      = 0;
   ldTop             = 0;
   ldBot             = 0;
   iGranularity      = 64;
} // end Setup

//***********************************************************************************************
// Class    : CompoundconsequenT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Overloaded operator +
// Purpose  : To add ConsequenTs to the array and to set the endpoints properly.
// Notes    : The Find_Max(a, b) routine returns true if a > b.
//***********************************************************************************************
CompoundconsequenT& CompoundconsequenT::operator + ( ConsequenT& ConRHS )      
{
//CCFPRINT("CompoundconsequenT::operator + ( ConsequenT& ConRHS )");

   _consequents.push_back( &ConRHS );

//cout << "in + operator, size is: " << _consequents.size() << endl;
   if( _consequents.size() == 1 )
   {
      ldLeft_Endpoint = ConRHS.GetLeftEndPoint();
      ldRight_Endpoint = ConRHS.GetRightEndPoint();
//cout << "in + operator, left is: " << ldLeft_Endpoint << endl;
//cout << "in + operator, right is: " << ldRight_Endpoint << endl;
      return *this;
   }

   // Everything's normal:
   if( Find_Max( ldLeft_Endpoint, ConRHS.GetLeftEndPoint() ) )
   {
      ldLeft_Endpoint = ConRHS.GetLeftEndPoint();
   } // end if
   
   if( Find_Max( ConRHS.GetRightEndPoint(), ldRight_Endpoint ) )
   { 
      ldRight_Endpoint = ConRHS.GetRightEndPoint();
   }

//cout << "in + operator, left is: " << ldLeft_Endpoint << endl;
//cout << "in + operator, right is: " << ldRight_Endpoint << endl;
   return *this;
} // end operator +

//***********************************************************************************************
// Class    : CompoundconsequenT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Overloaded operator >>
// Purpose  : Used publicly to execute the COM algorithm and get the value.
// Notes    : We check the number of consequents in the array. If we have only one, then we
//          : know that we are in a case where we are using Monotonic reasoning, if we have
//          : more, then we are doing more complex non-monotonic reasoning.
//          :
//***********************************************************************************************
CompoundconsequenT& CompoundconsequenT::operator >> ( long double& ldRHS )      
{
//CCFPRINT("CompoundconsequenT::operator >> ( long double& ldRHS )");

   if( _consequents.size() == 1 ) // Monotonic Reasoning
   {
      *_consequents[0] >> ldRHS;
   }
   else
   {
      ldRHS = CenterOfMass();    // Non-Monotonic Reasoning
   }

   return *this;
} // end operator >>

//***********************************************************************************************
// Class    : CompoundconsequenT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : CenterOfMass
// Purpose  : Computes the Center of Mass for the respective set of Consequents. We perform the
//          : Algorithm:
//          :  COM = sum(0, n){ d_i * m(d_i) } / sum(0,n){ m(d_i) }
//          : where sum(x, y){ z } is the summation of z from x to y, d_i is the ith domain
//          : value and m(d_i) is the truth value of that point.
//          :     
// Notes    : We assume that all of the Consequent sets have been added properly when this
//          : method is called.
//***********************************************************************************************
long double CompoundconsequenT::CenterOfMass()
{
//FPRINT("CompoundconsequenT::CenterOfMass()");
long double ldE = 0;
long double ldTheArgument = 0;

/*
   if( Absolute( iGranularity ) <= GetEpsilon() ) { // Should never get here...
      //CCFPRINT << "Caught divide by zero! iGranularity is zero!";
      return 0; } // end if zero
                */

   // Prime the pumps:
   ldStep_Value = ( ldRight_Endpoint - ldLeft_Endpoint ) / (long double)iGranularity;
   ldTheArgument = ldLeft_Endpoint;
   ldTop = ldBot = 0;

   //CCFPRINT << "The step size is:" << ldStep_Value;

   for( int i=0; i<(iGranularity+1); i++ )
   {
      ldE = EvaluateEm( ldTheArgument );
      ldTop += ldTheArgument * ldE;
      ldBot += ldE;
      ldTheArgument += ldStep_Value;
   } // end for loop

        /*
   if( Absolute( ldBot ) <= GetEpsilon() ) { // Should never get here...
      //CCFPRINT << "Caught divide by zero!! ldBot is zero!!";
      return ldTop; } // end if zero
                */

   ldE = ldTop / ldBot;

   return ldE;
} // end EvaluateIt

//***********************************************************************************************
// Class    : CompoundconsequenT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : EvaluateEm
// Purpose  : This computes the "|" on the domain value across all the Consequents.
// Notes    : For now, we'll just compute the or in the following manner:
//          : (...(((val1 | val2) | val3) | val4) | ...)
//          :
//***********************************************************************************************
long double CompoundconsequenT::EvaluateEm( long double ldArg )
{
long double ldEvalResult = 0;

// This method needs rework before use!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
   
   if( _consequents.size() > 0 )
   {
      ldEvalResult = _consequents[0]->EvaluateIt( ldArg );

      for( int i=1; i<_consequents.size(); i++ )
      {
         ldEvalResult = PerformOR( ldEvalResult, _consequents[i]->EvaluateIt( ldArg ) );  
      }
   }

   return ldEvalResult;
} // end EvaluateEm
