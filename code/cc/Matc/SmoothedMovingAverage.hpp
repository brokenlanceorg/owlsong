//***********************************************************************************************
// File		: SmoothedMovingAverage.hpp
// Purpose	: This class provides the calculation methods for a smoothed
//          : moving average. Basically, it wraps a TwoVar object.
//				: 
// Author	: Brandon Benham 
// Date		: 6/15/00
//***********************************************************************************************
#ifndef __SMOOTHED_HPP
#define __SMOOTHED_HPP

#include "Twovar.hpp"
//***********************************************************************************************
// Class	   : SmoothedMovingAveragE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose	: This class provides the calculation methods for a smoothed
//          : moving average. Basically, it wraps a TwoVar object.
//***********************************************************************************************
class SmoothedMovingAveragE
{
	public:
   	SmoothedMovingAveragE( ); // Default Constructor declaration
   	SmoothedMovingAveragE( long double ); // Default Constructor declaration
   	SmoothedMovingAveragE( long double, long double ); // Constructor declaration
   	~SmoothedMovingAveragE(); // Destructor declaration
      long double GetAverage( long double ); // Used in conjuction with real constructor
      long double GetAverage( long double, long double );
      void SetSmoothingFactor( long double );

   protected:
   	void Setup();

   private:
   	TwovariablE* Fct2_the_Function;
      long double ldSmoothing_Factor;
      long double ldPrevious_Average;
   
}; // end SmoothedMovingAveragE declaration

#endif

