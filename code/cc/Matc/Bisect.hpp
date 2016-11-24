//***************************************************************************** 
// File		:	Bisect.hpp
// Purpose	:	This file contains the declarations for the QuadratiC class 
// Author	:  Brandon Benham
//*****************************************************************************
#ifndef __BISECT_HPP
#define __BISECT_HPP

#include<iostream.h>
#include<stdlib.h>
#include"mathmatc.hpp"
#include"onevar.hpp"
#include"numerr.hpp"

//************************************************************************
// Class		:	QuadratiC
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose	:	This class sets up a buffer to keep track of the coefficients
//				:	of a quadratic equation. We also have member functions
//				:  which compute the solution.
//************************************************************************
class QuadratiC
{
	protected:
		MatriX* mCoeffs;
		VectoR* vCoeffs;
		OnevariablE* Fct1TheRootFunction;
      RandoM* RtheRandom;
		int iNumber_Of_Iterations;
		int iThe_Slope;
		long lMaximum_Iterations;
		long double ldLeft_Endpoint;
      long double ldRight_Endpoint;
		long double ldTau;
      long double ldPeturb_Value;
		void Setup(int = 0);
		long CalcIterations(long double, long double);
		int CheckEndpoints(long double, long double);
										 
	public:
		QuadratiC(); // default constructor
		QuadratiC(int, int, int = -1, long double = 2e-16); // real constructor
		~QuadratiC(); // destructor
		long double Bisect(long double, long double, long double = 0, 
			long double = 0);
		long double BisectIt(long double, long double);
		long double Secant_Root(long double (*Function1)(long double nX),
			long double nApprox1);
		void inline SetTolerance(long double ldT) {ldTau = ldT;}
		void inline SetRootEquation(int iW) 
			{Fct1TheRootFunction->SetEquation(iW);}
		void SetFunctionParams(long double ld1, long double ld2 = 0,
			long double ld3 = 0) {Fct1TheRootFunction->SetParams(ld1, ld2, ld3);}
      void SetRandomInterval(float, float);
      void SetInterval(long double, long double);
		void SetPolyCoeffs(VectoR*, VectoR*);
		void SetPolyCoeffs(VectoR*);
      long double PeturbBisect();
		void CalcPeturbValue();
		friend void main(); // for testing purposes
}; // end class definition


#endif