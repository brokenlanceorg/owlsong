//***************************************************************************** 
// File		:	Paramtrc.hpp                                                    
// Purpose	:	This file contains the class declarations for the class
//				:  parametric - these will be used in cubic splines and parametric
//				:  spline classes later to be developed.
// Author	:  Brandon Benham
//*****************************************************************************

#ifndef __PARAMTRC_HPP
#define __PARAMTRC_HPP

#include"c:\bc45\matc\mathmatc.hpp"
		
//************************************************************************
// Class		:	ParametriC
// Purpose	:	This class defines a parametric object used to approximate
//				:	smooth differentiable functions. Note that this is for
//				:  a two-dimensional, one variable functional representation.
//************************************************************************
class ParametriC
{
	protected:
		VectoR* vTvar; // This is the vector contains the independent variable
		VectoR* vXvar; // The dependent variables
		VectoR* vCoeffs; // Contains our coefficients
		int nDegree; // degree of our equation
		long double nXvar; // The scalar of the dependant variables

	public:
		ParametriC(); // Default constructor, defaults to deg(3)
		ParametriC(int nDeg); // Degree of the polynomial, usually 3
		~ParametriC();
		void Calc_Vars(long double nX_);
		void Calc_xVars();
		VectoR Get_Var(long double nX_);
		friend void main(); // for testing purposes

}; // end class definition ParametriC

#endif
