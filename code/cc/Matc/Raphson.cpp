//***************************************************************************** 
// File		:	Raphson.cpp
// Purpose	:	Contains the main function for numerical analysis calls
// Author	:  Brandon Benham
//*****************************************************************************
#include"newton.hpp"
#include"onevar.hpp"
#include<conio.h>
#include<iostream.h>
FilesourcE* NumerR::FlogFile = new FilesourcE("c:\\newton.log", _BYTE, 0);
//************************************************************************
// MAIN ~~~~~~~~~~~~~~~~~~~~~~~~
//************************************************************************
void main()  
{
char cC = '\0';
long double ldTemp;
long double ldTemp2;
int ione, itwo, ithree;
long double ldone, ldtwo, ldthree;
char acBuffer[15];

	cout<<"Enter tolerance: ";
	cin>>ldTemp;

	NewtoN NtnSolver(20, 21, ldTemp);
	NtnSolver.SetEquationParams(.5,.25,-1);
	NtnSolver.SetDerivativeParams(.5,-1);
/*	OnevariablE Fct1Tester(_UNDERFLOW);
	Fct1Tester.SetEquation(20);
	Fct1Tester.SetParams(.5,.25,-1);

	OnevariablE Fct1Tester2(_UNDERFLOW);
	Fct1Tester2.SetEquation(21);
	Fct1Tester2.SetParams(.5,-1,0); */

	while(cC != 0x1b)
	{
		cout<<"\nEnter guess: ";
		cin>>ldTemp;
/*		Fct1Tester.SetVariable(0, ldTemp);
		Fct1Tester2.SetVariable(0, ldTemp);
		cout<<"\nResult 1: "<<Fct1Tester.EvaluateIt();
		cout<<"\nResult 2: "<<Fct1Tester2.EvaluateIt();
		cC = getch(); */
		NtnSolver.WriteToOutput("\nInitial Guess: ");
		acBuffer[0] = '\0';
		gcvt(ldTemp, 18, acBuffer);

		NtnSolver.WriteToOutput(acBuffer);
		while(NtnSolver.GetStatus() != 1)
			ldTemp = NtnSolver.SolveIt(ldTemp);

		cout<<"Result: "<<ldTemp<<"\n";
		NtnSolver.WriteToOutput("\nFinal Result: ");

		acBuffer[0] = '\0';
		gcvt(ldTemp, 18, acBuffer);
		NtnSolver.WriteToOutput(acBuffer);

		cout<<"It took "<<NtnSolver.GetTotalIterations()<<" iterations.";
		NtnSolver.WriteToOutput(" Number of Iterations: ");
		acBuffer[0] = '\0';
		ltoa(NtnSolver.GetTotalIterations(), acBuffer, 10);
		NtnSolver.WriteToOutput(acBuffer);
		cC = getch();           

		cout<<"\nEnter Equation Number: ";
		cin>>ione;
		cout<<"\nEnter Derivative Number: ";
		cin>>itwo;
		NtnSolver.SetEquation(ione);
		NtnSolver.SetDerivative(itwo);
		cout<<"\nEnter Equation param1: ";
		cin>>ldone;
		cout<<"\nEnter Equation param2: ";
		cin>>ldtwo;
		cout<<"\nEnter Equation param3: ";
		cin>>ldthree;
		NtnSolver.SetEquationParams(ldone, ldtwo, ldthree);
		
		cout<<"\nEnter Derivative param1: ";
		cin>>ldone;
		cout<<"\nEnter Derivative param2: ";
		cin>>ldtwo;
		cout<<"\nEnter Derivative param3: ";
		cin>>ldthree;
		NtnSolver.SetDerivativeParams(ldone, ldtwo, ldthree);
	} // end while

} // end main
