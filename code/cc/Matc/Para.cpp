//***************************************************************************** 
// File		:	Para.cpp                                                    
// Purpose	:	This file contains the test calls to the class member
//				:  Functions of the various mathematical functions for 
//				:  Numerical Analysis
//*****************************************************************************
#include<iostream.h>
#include<conio.h>
#include"c:\bc45\matc\paramtrc.hpp"

//***************************************************************
// Function	:  Main!
//***************************************************************
void main()
{
ParametriC* Para_Test;
int nNum_Vars = 0;
long double nTemp1 = 0;

	cout<<"Enter number of variables: ";
	cin>>nNum_Vars;
	Para_Test = new ParametriC(nNum_Vars);
	Para_Test->vTvar->Display(TRUE);
	Para_Test->vCoeffs->pVariables[0] = 5;
	Para_Test->vCoeffs->pVariables[1] = 2;
	Para_Test->vCoeffs->pVariables[2] = 3;
	Para_Test->vCoeffs->pVariables[3] = 4;
	cout<<"Enter an X: ";
	cin>>nTemp1;
	Para_Test->Calc_Vars(nTemp1);
	Para_Test->vTvar->Display(TRUE);
	Para_Test->Calc_xVars();
	Para_Test->vXvar->Display(TRUE);
	cout<<'\n'<<"result is: "<<Para_Test->nXvar;
											  
	getch();
	delete Para_Test;

} // end main