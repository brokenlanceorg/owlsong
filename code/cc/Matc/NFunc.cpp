//***************************************************************************** 
// File		:	Nfunc.cpp
// Purpose	:	This file contains the object oriented paradigm for the 
//				:  neural network parallel function interface class.
// Notes		:  This is a base class which has several outlier classes
//				:  necessary for the mathematical bookeeping.
// Author	:  Brandon Benham
//*****************************************************************************
#include"Nfunc.hpp"

//***************************************************************************** 
// Class		:	NeuralparallelfunctioN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	:  Constructor
// Purpose	:	This class furnishes an interface to a trained neural network
//				:  that will calculate the value of some arbitrary function
// 			:  at non-interpolating points in the function's domain.
//*****************************************************************************
NeuralparallelfunctioN::NeuralparallelfunctioN()
{
	BPNtheNetworks = NULL;
	ppcNetwork_Names = NULL;
} // end constructor

//***************************************************************************** 
// Class		:	NeuralparallelfunctioN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	:  Destructor
// Purpose	:	This class furnishes an interface to a trained neural network
//				:  that will calculate the value of some arbitrary function
// 			:  at non-interpolating points in the function's domain.
//*****************************************************************************
NeuralparallelfunctioN::~NeuralparallelfunctioN()
{
int iLoop3;

	for(iLoop3=0;iLoop3<iNumber_Of_Intervals;iLoop3++)
	{
		delete[] ppcNetwork_Names[iLoop3];
	} // end for
	delete[] ppcNetwork_Names;
	delete[] BPNtheNetworks;
	delete[] ppItheIntervals;
} // end destructor                     

//***************************************************************************** 
// Class		:	NeuralparallelfunctioN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	:  Constructor (not recomended)
// Purpose	:	This class furnishes an interface to a trained neural network
//				:  that will calculate the value of some arbitrary function
// 			:  at non-interpolating points in the function's domain.
//*****************************************************************************
NeuralparallelfunctioN::NeuralparallelfunctioN(char* pcName)
{
	iNumber_Of_Intervals = 1;
	ppcNetwork_Names = new char*[1];
	ppcNetwork_Names[0] = new char[strlen(pcName) + 1];
	ppcNetwork_Names[0][0] = '\0';
	strcpy(ppcNetwork_Names[0], pcName);
	BPNtheNetworks = new BackproP*[1];
	BPNtheNetworks[0] = new BackproP(ppcNetwork_Names[0], 1);
	Setup();
} // end constructor

//***************************************************************************** 
// Class		:	NeuralparallelfunctioN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	:  Big Constructor
// Purpose	:	This is the 'big' constructor, i.e., if we have a general
//				:  function composed of several BPNs, then we use this constructor.
// 			:  We construct a BPN object for each name given.
//*****************************************************************************
NeuralparallelfunctioN::NeuralparallelfunctioN(int iHowMany, char** ppcNames)
{
int iLoop2;

	iNumber_Of_Intervals = iHowMany;
	ConstructArray(iHowMany);
	
	BPNtheNetworks = new BackproP*[iNumber_Of_Intervals];
	for(iLoop2=0;iLoop2<iNumber_Of_Intervals;iLoop2++)
	{
		strcpy(ppcNetwork_Names[iLoop2], ppcNames[iLoop2]);
		BPNtheNetworks[iLoop2] = new BackproP(ppcNetwork_Names[iLoop2], 1);
		BPNtheNetworks[iLoop2]->SetMature(1);
	} // end for loop
	Setup();
} // end big constructor

//***************************************************************************** 
// Class		:	NeuralparallelfunctioN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	:  Setup
// Purpose	:	Provides setup operations for the NPF, such as instantiation
//				:  of the IntervaL objects.
// 			:  At this point, the BPN is fully constructed, so we can make
//				:  calls to its public memeber functions.
//*****************************************************************************
void NeuralparallelfunctioN::Setup()
{
int iL;
int iO1;
int iNumIns;
VectoR* vTempVec;

	ldThe_Variable = 0;
	ppItheIntervals = new IntervaL*[iNumber_Of_Intervals];
	for(iL=0;iL<iNumber_Of_Intervals;iL++)
	{
		iNumIns = BPNtheNetworks[iL]->GetNumberOfInputs();
		vTempVec = new VectoR(iNumIns);
		ppItheIntervals[iL] = new IntervaL(iNumIns - 1);
		*vTempVec = BPNtheNetworks[iL]->GetTrainingInput(0);
		for(iO1=0;iO1<(iNumIns-1);iO1++)
		{
/*			cout<<"a data point(L): "<<vTempVec->pVariables[iO1]<<" \n";
			cout<<"a data point(R): "<<vTempVec->pVariables[iO1 + 1]<<" \n"; */
			ppItheIntervals[iL]->SetDomain(iO1, vTempVec->pVariables[iO1], 
				vTempVec->pVariables[iO1 + 1]);
		} // end inner for loop
		delete vTempVec;
	} // end for 
} // end Setup

//***************************************************************************** 
// Class		:	NeuralparallelfunctioN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	:  ConstructArray
// Purpose	:	Builds the array to house the names of the network
//*****************************************************************************
void NeuralparallelfunctioN::ConstructArray(int iHowMany)
{
int iLoop4;

	ppcNetwork_Names = new char*[iHowMany];
	for(iLoop4=0;iLoop4<iNumber_Of_Intervals;iLoop4++)
	{
		ppcNetwork_Names[iLoop4] = new char[15];
		ppcNetwork_Names[iLoop4][0] = '\0';
	} // end for loop
} // end ConstructArray

//***************************************************************************** 
// Class		:	NeuralparallelfunctioN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	:  EvaluateIt
// Purpose	:	The function which acts as the interface into the NPF.
//*****************************************************************************
long double NeuralparallelfunctioN::EvaluateIt()
{
int iInters;
int iDomes;
int iTemp = 0;
long double ldResult;
long double ldX = ldThe_Variable;
VectoR* vInPut;

	for(iInters=0;iInters<iNumber_Of_Intervals;iInters++)
	{
		iDomes = FindDomain(iInters, ldX);
		if(iDomes == -1) {
      	iDomes = BPNtheNetworks[iNumber_Of_Intervals-1]->GetNumberOfInputs() - 2;
         iTemp = 1;
			continue; } // end if not found
		else {
      	iTemp = 0;
			break; }
	} // end outer for loop
   if(iTemp)
   	iInters--;
	if(ldX >= ppItheIntervals[iInters]->GetMidPointX(iDomes))
		iDomes += 1;
/*	cout<<"We're putting it in vec pos: "<<iDomes<<'\n';
	getch(); */
	vInPut = new VectoR(BPNtheNetworks[iInters]->GetNumberOfInputs());
	*vInPut = BPNtheNetworks[iInters]->GetTrainingInput(0);
//	cout<<"the training input is: "<<*vInPut<<"\n";
	vInPut->pVariables[iDomes] = ldX;
//	cout<<"the training input is now: "<<*vInPut<<"\n";
//	getch();
	BPNtheNetworks[iInters]->Recall(vInPut);       
	*vInPut = BPNtheNetworks[iInters]->GetOutput();
	ldResult = vInPut->pVariables[iDomes];
/*	cout<<"result is: "<<ldResult<<'\n';
	getch(); */

	delete vInPut;
	return ldResult;
} // end EvaluateIt

//***************************************************************************** 
// Class		:	NeuralparallelfunctioN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	:  FindDomain
// Purpose	:	Determines in which domain of the interval the number lies.
// Notes		:  We return a negative one on error.
//*****************************************************************************
int NeuralparallelfunctioN::FindDomain(int iWh, long double ldWhich)
{
int iWhere;
int iRes = -1;
int iNumInputs = BPNtheNetworks[iWh]->GetNumberOfInputs();

/*	cout<<"in find domain, num inputs is: "<<iNumInputs<<"...\n";
	getch(); */
	for(iWhere=0;iWhere<(iNumInputs-1);iWhere++)
	{
		if((ldWhich >= ppItheIntervals[iWh]->GetFirstX(iWhere)) &&
			(ldWhich <= ppItheIntervals[iWh]->GetLastX(iWhere)))
				return iRes = iWhere;
	} // end for 
	return iRes; // if we get here, we didn't find it
} // end FindDomain

