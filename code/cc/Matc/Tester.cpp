//***************************************************************************** 
// File		:	Tester.cpp
// Purpose	:	Contains the main function for numerical analysis calls
// Author	:  Brandon Benham
//*****************************************************************************
#include"newton.hpp"
#include"onevar.hpp"
#include<conio.h>
#include<iostream.h>
FilesourcE* NumerR::FlogFile = new FilesourcE("c:\\tester.log", _BYTE, 0);
//************************************************************************
// MAIN ~~~~~~~~~~~~~~~~~~~~~~~~
//************************************************************************
void main()  
{
char cC = '\0';
long double ldTemp;
int ione;
long double ldone, ldtwo, ldthree;

	OnevariablE Fct1Tester(_UNDERFLOW);

	while(cC != 0x1b)
	{
		cout<<"\nEnter Equation Number: ";
		cin>>ione;
		Fct1Tester.SetEquation(ione);

		cout<<"\nEnter Param Number 1: ";
		cin>>ldone;
		cout<<"\nEnter Param Number 2: ";
		cin>>ldtwo;
		cout<<"\nEnter Param Number 3: ";
		cin>>ldthree;

		Fct1Tester.SetParams(ldone, ldtwo, ldthree);

		cout<<"\nEnter variable: ";
		cin>>ldTemp;
		Fct1Tester.SetVariable(0, ldTemp);

		cout<<"\nResult: "<<Fct1Tester.EvaluateIt();
		cout<<"\nPress any key to continue, Esc to exit.";
		cC = getch();
	} // end while

} // end main