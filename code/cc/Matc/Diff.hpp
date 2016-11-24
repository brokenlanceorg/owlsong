//***************************************************************************** 
// File		:	Diff.hpp
// Purpose	:	This file declares the DifferentiaL mathematical object
//	Author	:  Brandon Benham
//*****************************************************************************
#ifndef __DIFF_HPP
#define __DIFF_HPP
#include<stdlib.h>
#include"threevar.hpp"
#include"twovar.hpp"
#include"onevar.hpp"
#include"filesrc.hpp"
#include"mathmatc.hpp"
#include"domain.hpp"
//***************************************************************************** 
// Class		:	DifferentiaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose	:	This class will be used to numerically solve differential
//				:  systems of equations.
// Notes		:  We implement the Runge-Kutta method of order four.
//				:  Our naming conventions for the function objects will be:
//				:  Fct1[...] for one-variable functions; Fct2[...] for two
//				:  variable objects, and so on.
//*****************************************************************************
class DifferentiaL
{
	protected:
		OnevariablE* Fct1TheDiffEq;	// A one-variable differential equation
		TwovariablE* Fct2TheDiffEq;	// A two-variable differential equation
		ThreevariablE* Fct3TheDiffEq;	// A two-variable differential equation
		int iNum_Variables;				// The number of dependent variables
		VectoR* vDependentVars;			// A vector containing these variables
		VectoR* vConditions;				// the initial conditions (given).
		MatriX* mCoeffs;					// the coefficients used in Runge-Kutta
      DomaiN* DtheDomains;				// Contains our domain information
		long double ldStepSize;
		long double ldHalfStep;     
		long double ldTimeVariable;	// Our time value
		unsigned long ulDone;			// Holds the number of iterations
		NumerR* DifferentialError;  	// error handling
		FilesourcE* FOutput;				// for a hard copy of the data
		// Protected Functions:
		inline long double Abs(long double);
		inline long double Eps();
		void FillVariables(int);
		void CalcCoeffs();
		void Approximate();
		long double TheFunction();		// Returns our Function Evaluation
		void CreateHeader();
		void CreateFunctions();     
		void WriteToDisk();
      void Setup();

	public:
		DifferentiaL();
		~DifferentiaL();
		DifferentiaL(int);
		DifferentiaL(int, int, char* pcFileName = "DiffEq.OUT");
      void SetEquation(int);
      void SetParams(long double, long double = 0, long double = 0);
		inline void SetConditions(VectoR* vVec) {*vConditions = *vVec;}
		void SetStepSize(float);
		void SetVariable(int , long double);
		void SetEndPoints(long double=0, long double=0, long double=0, 
      	long double=0, long double=0);
		int Solve();
		inline void SetOneVarEq(int iWhich) {Fct3TheDiffEq->SetOneVarEq(iWhich);}
		inline void SetTwoVarEq(int iWhich) {Fct3TheDiffEq->SetTwoVarEq(iWhich); }
		inline void SetOneVarParams(long double ld1, long double ld2, long double ld3) {
      	Fct3TheDiffEq->SetOneVarParams(ld1, ld2, ld3); }
		inline void SetTwoVarParams(long double ld1, long double ld2, long double ld3) {
      	Fct3TheDiffEq->SetTwoVarParams(ld1, ld2, ld3); }
		friend void main();

}; // end DifferentiaL declaration

#endif