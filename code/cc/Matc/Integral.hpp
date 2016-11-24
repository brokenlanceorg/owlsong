//***************************************************************************** 
// File		:	Integral.hpp
// Purpose	:	Contains the implementations for the IntegraL class
// Author	:  Brandon Benham
//*****************************************************************************
#ifndef __INTEGRAL_HPP
#define __INTEGRAL_HPP

#include<iostream.h>                                              
#include<conio.h>
#include"random.hpp"
#include"numerr.hpp"
#include"function.hpp"
//************************************************************************
// Class		:	IntegraL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function	:  Default Constructor
// Purpose	:	This class defines an integral object used for numerical
//				:  approximations of an arbitrary single-variable function.
//************************************************************************
class IntegraL
{
	protected:
		long double (*TheFunction)(long double );
		float fLeft_EndPoint;
		float fRight_EndPoint;
		float fLength_Of_Interval;
		RandoM* RandomNumber;
      NumerR* NumericalError;
															  
	public:
		IntegraL();
		IntegraL(long double (*TheFunc)(long double nX));
		~IntegraL();
		void SetEndPoints(float fL, float fR); 
		unsigned long ulQuit_Value;
		long double CalcIntegral(unsigned long ulX = 0);
		long double CalcIntegralValue(long double);
}; // end class Integral

#endif