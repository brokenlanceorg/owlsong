//***************************************************************************** 
// File		:	Plottest.cpp
// Purpose	:	Contains the implementations for the Plot_daT class
// Author	:  Brandon Benham
//*****************************************************************************
#include<conio.h>
#include"lagrange.hpp"
#include"dataset.hpp"
#include"plotdat.hpp"
#include"types.hpp"
#include"filesrc.hpp"

//***************************************************************************** 
// Function	:	main!
//*****************************************************************************
void main()
{
LagrangE* Test_Plot;
char acTemp_name[15];
char cC = '\0';
BYTE nClr = 9;
long double temp = 0;

	acTemp_name[0] = '\0';
	strcpy(acTemp_name, "sin1.dat");
	Test_Plot = new LagrangE(acTemp_name, _BYTE, 3, MODE_16);
	Test_Plot->Display(7, nClr, 1);
	cout.precision(18);
	while(cC != 0x1b)
	{
		cout<<"Enter value: ";
		cin>>temp;
		temp = Test_Plot->EvaluateIt(temp);
		cout<<"Result: "<<temp<<"\n";
		cC = getch();
	} // end while
/*	Test_Plot->Graph_It(7);
	getch();
	//Test_Plot->Graph_It(0); 
	Test_Plot->L_n_k->Display(TRUE); cout<<endl;
	Test_Plot->vX_Var->Display(TRUE);
	cout<<"L is: "<<Test_Plot->Calc_LPoly(1, 274.104)<<endl;
	cout<<"L is: "<<Test_Plot->Calc_LPoly(2, 274.104)<<endl;
	getch(); */
									  
	delete Test_Plot;

} // end main