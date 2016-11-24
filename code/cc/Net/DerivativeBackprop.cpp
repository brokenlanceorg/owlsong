//***********************************************************************************************
// File		: DerivativeBackprop.cpp
// Purpose	:
//	        :
// Author	: Brandon Benham
// Date		: 8/25/02
//***********************************************************************************************

#include"DerivativeBackprop.hpp"

//***********************************************************************************************
// Class        : AutomatA
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Default Constructor
// Purpose	: Performs the basic construction actions.
//***********************************************************************************************
DerivativeBackproP::DerivativeBackproP( char* name, unsigned short alive ) : BackproP( name, alive )
{
   Setup();
} // end DerivativeBackproP default constructor

//***********************************************************************************************
// Class        : DerivativeBackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Destructor
// Purpose	: Performs the destruction actions.
//***********************************************************************************************
DerivativeBackproP::~DerivativeBackproP()
{
} // end DerivativeBackproP destructor

//***********************************************************************************************
// Class        : DerivativeBackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Setup
// Purpose	: Performs the basic setup actions.
//***********************************************************************************************
void DerivativeBackproP::Setup()
{
} // end Setup

//***********************************************************************************************
// Class        : DerivativeBackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: GetDerivative
// Purpose	: Returns the derivative with respect to the input dimension.
//***********************************************************************************************
long double DerivativeBackproP::GetDerivative()
{
long double ldResult = 0;
long double ldInnerSum = 0;

   for( int k=0; k<vOutputError->cnRows; k++ )
   {
      for( int j=0; j<vHiddenError->cnRows; j++ )
      {
         for( int i=0; i<vInput->cnRows; i++ )
         {
            ldInnerSum += mSynapseOne->pCol[ j ][ i ];
         } // end inner sum
         ldResult += vHiddenError->pVariables[ j ] * mSynapseTwo->pCol[ k ][ j ] * ldInnerSum;
         ldInnerSum = 0;
      } // end hidden loop
      vOutputError->pVariables[ k ] *= ldResult;
      ldResult = 0;
   } // end outer loop

   return vOutputError->GetSum();
} // end GetDerivative

