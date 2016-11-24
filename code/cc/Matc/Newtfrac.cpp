//***************************************************************************** 
// File		:	NewtFrac.cpp
// Purpose	:	Contains the main function for numerical analysis calls
// Author	:  Brandon Benham
//*****************************************************************************
#include"newton.hpp"
#include"onevar.hpp"
#include<conio.h>
#include<iostream.h>
#include<dos.h>
unsigned _stklen = 64000;
FilesourcE* NumerR::FlogFile = new FilesourcE("c:\\Fracton.log", _BYTE, 0);
//************************************************************************
// MAIN ~~~~~~~~~~~~~~~~~~~~~~~~
//************************************************************************
void main()  
{
long double ldTemp;
int ione, itwo, ithree;
long double ldone, ldtwo, ldthree;

	cout<<"Enter tolerance: ";
	cin>>ldTemp;

	cout<<"Enter Equation Number: ";
	cin>>ione;

	cout<<"Enter Derivative Equation Number: ";
	cin>>itwo;

	NewtoN NtnSolver(ione, itwo, ldTemp);
	NtnSolver.SetLogging(0);

	cout<<"Enter Equation param1: ";
	cin>>ldone;

	cout<<"Enter Equation param2: ";
	cin>>ldtwo;

	cout<<"Enter Equation param3: ";
	cin>>ldthree;
	// .5,.25,-1
	NtnSolver.SetEquationParams(ldone, ldtwo, ldthree);

	cout<<"Enter Derivative Equation param1: ";
	cin>>ldone;

	cout<<"Enter Derivative Equation param2: ";
	cin>>ldtwo;

	cout<<"Enter Derivative Equation param3: ";
	cin>>ldthree;
	// .5,-1
	NtnSolver.SetDerivativeParams(ldone, ldtwo, ldthree);

	cout<<"Enter Image Width: ";
	cin>>ione;
	cout<<"Enter Image Height: ";
	cin>>itwo;
	NtnSolver.SetFileLimits(ione, itwo);

	cout<<"(Domain Values) Enter a: ";
	cin>>ldone;
	cout<<"(Domain Values) Enter b: ";
	cin>>ldtwo;
	NtnSolver.SetDomains(ldone, ldtwo, ldone, ldtwo);
	NtnSolver.FractalizeIt();
	cout<<"*************************************************";
	cout<<"*************************************************";
	cout<<"*************************************************";
	cout<<"*************************************************";
	cout<<"*************************************************";
	cout<<"***************It Is Finished********************";
	cout<<"*************************************************";
	cout<<"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!";
	cout<<'\b';
	cout<<'\b';
} // end main                          
