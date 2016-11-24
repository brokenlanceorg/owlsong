//***************************************************************************** 
// File		:	Nftest.cpp
// Purpose	:	Contains the main function
// Author	:  Brandon Benham
//*****************************************************************************
#include"nfunc.hpp"
#include"interval.hpp"
#include<iostream.h>
#include<conio.h>
FilesourcE* NumerR::FlogFile = new FilesourcE("c:\\nftest.log", _BYTE, 0);
//************************************************************************
// MAIN ~~~~~~~~~~~~~~~~~~~~~~~~
//************************************************************************
void main()  
{
char** ppcTheNames;
long double ldVariable = 0;
char acNet_Name[25];

	ppcTheNames = new char*[1];
	ppcTheNames[0] = new char[13];
	//ppcTheNames[1] = new char[13];
	ppcTheNames[0][0] = '\0';
   acNet_Name[0] = '\0';
	//ppcTheNames[1][0] = '\0';
   cout<<"Enter definition file name: ";
   cin>>acNet_Name;
	strcpy(ppcTheNames[0], acNet_Name);
	//strcpy(ppcTheNames[1], "net5.def");
					 
	NeuralparallelfunctioN* NPFtest = new NeuralparallelfunctioN(1, ppcTheNames);
	while(ldVariable != -1)
	{
		cout<<"Enter variable: ";
		cin>>ldVariable; // we had used: .5983459441, and got result: .509552
      NPFtest->SetVariable(ldVariable);
		cout<<NPFtest->EvaluateIt()<<"\n";
	} // end while
	cout<<"made it to here...";
	getch();

	delete NPFtest;
} // end main
