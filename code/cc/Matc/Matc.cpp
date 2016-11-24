//**********************************************************************
// Class		:	MatriX
// Purpose	:  This class initializes two-dimensional array for storage 
//				:  of calculated mathematical functions.
//**********************************************************************
#include<iostream.h>
#include<conio.h>
#include"function.hpp"
#include"mathmatc.hpp"
//#include"grphclss.hpp"

//extern long Get_time_Secs();

void main()
{
MatriX beta(2,4);
/*
MatriX scale(1,1);            
long nBeg = 0;
long nEnd = 0;
VectoR vel(4);
MatriX translate(3,3);
VectoR accel(3);
VectoR res(3);
*/
//BOOL ldisp_space = FALSE;
BOOL ldisp_space = TRUE;

	cout.precision(20);
	beta.Zero_Out();
	for(int i=0;i<beta.cnRows;i++) //row 
		for(int j=0;j<beta.cnColumns;j++) // col
		{
			beta.pCol[i][j] = i + j * 4;
			cout<<beta.pCol[i][j]<<endl;
		} // end for 

	beta.Display(FALSE, ldisp_space);
	getch();
	//beta.Reduced_Eschalon();
	beta.Display(TRUE, ldisp_space);
} // end main        

