//***************************************************************************** 
// File		:	DataSet.cpp
// Purpose	:	Contains the implementations for the Data_seT class
// Author	:  Brandon Benham
//*****************************************************************************
#include<iostream.h>                                              
#include<conio.h>
#include<stdlib.h>
#include"dataset.hpp"

//************************************************************************
// Class		:	Data_seT
// Function	:  Default Constructor
// Purpose	:	This class defines a data_set object used for scientific
//				:  investigations, utilizes the input class
//************************************************************************
Data_seT::Data_seT()
{
	nNum_Data_Points = 1;
   xVariables = new VectoR(nNum_Data_Points);
	yVariables = new VectoR(nNum_Data_Points);
															
} // end default constructor data_set

//************************************************************************
// Class		:	Data_seT
// Function	:  Constructor
// Purpose	:	This class defines a data_set object used for scientific
//				:  investigations, utilizes the input class
//************************************************************************
Data_seT::Data_seT(int nNum_Pts)
{
	nNum_Data_Points = nNum_Pts;
	xVariables = new VectoR(nNum_Data_Points);
	yVariables = new VectoR(nNum_Data_Points);
	 
} // end constructor data_set

//************************************************************************
// Class		:	Data_seT
// Function	:  Constructor
// Purpose	:	This class defines a data_set object used for scientific
//				:  investigations, utilizes the input class
//************************************************************************
Data_seT::Data_seT(char* cF_name, _SIZE dTyp)
{
	FInData = new FilesourcE(cF_name, dTyp, 1);
	if(FInData->OpenFileRead())
	{
		FInData->GetWord();
		nNum_Data_Points = atoi(FInData->RetWord());
		xVariables = new VectoR(nNum_Data_Points);
		yVariables = new VectoR(nNum_Data_Points);
	} // end if
} // end constructor data_set

//************************************************************************
// Class		:	Data_seT
// Function	:  Destructor
// Purpose	:	This class defines a data_set object used for scientific
//				:  investigations, utilizes the input class
//************************************************************************
Data_seT::~Data_seT()
{
	 delete xVariables;
	 delete yVariables;
} // end default destructor data_set

//************************************************************************
// Class		:	Data_seT
// Function	:  Get_Data_Input
// Purpose	:	Gets the data input from the input object, such as a file
//				:  or maybe the user.
//************************************************************************
void Data_seT::Get_Data_Input()
{
const char _END = '*';
char acTemp[35];
int iWhichVar = 0;
long double nRes = 0;

	acTemp[0] = '\0';
	do // we operate with the caveat that cThe_Data is '*' when we're done
	{  // first we get the xvariable
		FInData->GetWord();
		strcpy(acTemp, FInData->RetWord());
		nRes = _atold(acTemp);
		if(iWhichVar < nNum_Data_Points)
			xVariables->pVariables[iWhichVar] = nRes;
		acTemp[0] = '\0';

		// next, we get the corresponding yvariable
		FInData->GetWord();
		strcpy(acTemp, FInData->RetWord());
		nRes = _atold(acTemp);
		acTemp[0] = '\0';
		if(iWhichVar < nNum_Data_Points)
			yVariables->pVariables[iWhichVar] = nRes;
		iWhichVar += 1;
	} while((FInData->RetWord())[0] != _END);
	FInData->CloseFile();
} // end Get_Data_Input

//************************************************************************
// Class		:	Data_seT
// Function	:  Get_XDat
// Purpose	:	Returns the X-vector of the dataset
//************************************************************************
VectoR* Data_seT::Get_XDat()
{
	return xVariables;
} // end Get_XDat

//************************************************************************
// Class		:	Data_seT
// Function	:  Get_YDat
// Purpose	:	Returns the Y-vector of the dataset
//************************************************************************
VectoR* Data_seT::Get_YDat()
{
	return yVariables;
} // end Get_YDat

