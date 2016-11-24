//***********************************************************************************************
// File		: SmoothedMovingAverageInverse.hpp
// Purpose	: 
//          : 
//				: 
// Author	: Brandon Benham 
// Date		: 7/18/00
//***********************************************************************************************
#ifndef __SMAINVERSE_HPP
#define __SMAINVERSE_HPP

#include "Twovar.hpp"
//***********************************************************************************************
// Class	   : SmoothedMovingAverageInversE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose	: Computes the Smoothed moving average calculation
//          : 
//***********************************************************************************************
class SmoothedMovingAverageInversE
{
	public:
   	SmoothedMovingAverageInversE( long double = 0.1 ); // Default Constructor declaration
   	~SmoothedMovingAverageInversE(); // Destructor declaration

      long double GetInverse( long double );
      long double GetInverse( long double, long double );

   protected:
   	void Setup();

   private:
      TwovariablE* theFunction;
      long double  ldSMA_Factor;
      long double  ldPrevious;
   
}; // end SmoothedMovingAverageInversE declaration

#endif

