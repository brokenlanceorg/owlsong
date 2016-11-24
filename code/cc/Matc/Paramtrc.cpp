//***************************************************************************** 
// File		:	Paramtrc.cpp                                                    
// Purpose	:	This file contains the class implementations for the class
//				:  parametric - these will be used in cubic splines and parametric
//				:  spline classes later to be developed.
// Author	:  Brandon Benham
//*****************************************************************************
#include<iostream.h>
#include<stdlib.h>
#include<math.h>
#include<conio.h>
#include"c:\bc45\matc\paramtrc.hpp"
#include"c:\bc45\matc\mathmatc.hpp"

//************************************************************************
// Class		:	ParametriC
// Function	:  Default Constructor
// Purpose	:	Initializes the vector members and instantiates the degree
//				:	of the polynomial.
//************************************************************************
ParametriC::ParametriC()
{
	nDegree = 3;
	vTvar = new VectoR(nDegree);
	vXvar = new VectoR(nDegree);
	vCoeffs = new VectoR(nDegree);
	vXvar->Fill_Ones(); // make all entries equal to one
	vTvar->Fill_Ones(); // make all entries equal to one
	vCoeffs->Fill_Ones(); // make all entries equal to one
} // end default constructor

//************************************************************************
// Class		:	ParametriC
// Function	:  Constructor
// Purpose	:	Initializes the vector members and instantiates the degree
//				:	of the polynomial.
//************************************************************************
ParametriC::ParametriC(int nDeg)
{
	nDegree = nDeg;
	vTvar = new VectoR(nDegree);
	vXvar = new VectoR(nDegree);
	vCoeffs = new VectoR(nDegree);
	vTvar->Fill_Ones(); // make all entries equal to one
	vXvar->Fill_Ones(); // make all entries equal to one
	vCoeffs->Fill_Ones(); // make all entries equal to one
} // end constructor

//************************************************************************
// Class		:	ParametriC
// Function	:  Destructor
// Purpose	:	Kills the object
//************************************************************************
ParametriC::~ParametriC()
{
	delete vTvar;
	delete vCoeffs;
} // end destructor

//************************************************************************
// Class		:	ParametriC
// Function	:  Calc_Vars
// Purpose	:	Returns the independent variable vector given some value
//				:	of the independent variable.
//************************************************************************
void ParametriC::Calc_Vars(long double nX_)
{
int iLoop = 0;

	for(iLoop=0;iLoop<nDegree;iLoop++)
		vTvar->pVariables[iLoop] = powl(nX_, (nDegree - iLoop - 1));
		// facilitate vector and matrix multiplication
} // end Get_Var

//************************************************************************
// Class		:	ParametriC
// Function	:  Calc_xVars
// Purpose	:	Calculates the dependent variable
//************************************************************************
void ParametriC::Calc_xVars()
{
int iL = 0;

	*(vXvar) = *(vCoeffs) * *(vTvar);
	nXvar = 0;
	for(iL=0;iL<nDegree;iL++)
		nXvar += vXvar->pVariables[iL];
} // end Calc_xVars

