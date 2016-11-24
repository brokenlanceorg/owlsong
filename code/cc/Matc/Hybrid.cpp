//***************************************************************************** 
// File		:	Hybrid.cpp
// Purpose	:	Contains the main function for numerical analysis calls
// Author	:  Brandon Benham
//*****************************************************************************
#include"bisecant.hpp"
#include"onevar.hpp"
#include<conio.h>
#include<iostream.h>
unsigned _stklen = 64000;
FilesourcE* NumerR::FlogFile = new FilesourcE("c:\\hybrid.log", _BYTE, 0);
BisecanT* NtnSolver;
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

		cout<<"\nEnter tolerance: ";
		cin>>ldTemp;
	
		cout<<"\nEnter Equation Number: ";
		cin>>ione;
		cout<<"\nEnter Left Endpoint: ";
		cin>>ldone;
		cout<<"\nEnter Right Endpoint: ";
		cin>>ldtwo;
		NtnSolver = new BisecanT(ldone, ldtwo, ione, ione, ldTemp);

		cout<<"\nEnter Param 1: ";
		cin>>ldone;
		cout<<"\nEnter Param 2: ";
		cin>>ldtwo;
		cout<<"\nEnter Param 3: ";
		cin>>ldthree;
				  
		NtnSolver->SetEquationParams(ldone, ldtwo, ldthree);
		NtnSolver->SetLogging(2, 2);
		NtnSolver->CheckEquation();
		NtnSolver->SetFileName("bisecant.dat");

		cout<<"\nEnter guess: ";
		cin>>ldTemp;

		NtnSolver->SendToFile("\nInitial Guess: ");
		acBuffer[0] = '\0';
		gcvt(ldTemp, 18, acBuffer);

		NtnSolver->SendToFile(acBuffer);
		while(NtnSolver->GetStatus() != 1)
			ldTemp = NtnSolver->SolveIt(ldTemp);

		cout<<"Result: "<<ldTemp<<"\n";
		NtnSolver->SendToFile("\nFinal Result: ");

		acBuffer[0] = '\0';
		gcvt(ldTemp, 18, acBuffer);
		NtnSolver->SendToFile(acBuffer);

		cout<<"It took "<<NtnSolver->GetTotalIterations()<<" iterations.";
		NtnSolver->SendToFile(" Number of Iterations: ");
		acBuffer[0] = '\0';
		ltoa(NtnSolver->GetTotalIterations(), acBuffer, 10);
		NtnSolver->SendToFile(acBuffer);

		delete NtnSolver;
} // end main