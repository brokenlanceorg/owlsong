//**************************************************************************
//	File		: Space.cpp
// Purpose	: Implements the SpacE class
// Author	: Brandon Benham
//**************************************************************************
#include"space.hpp"
#include<fstream.h>

DEFINE_RESPONSE_TABLE1(SpacE, ScreeN)
  EV_WM_SIZE,
END_RESPONSE_TABLE;

IMPLEMENT_CASTABLE1(SpacE, ScreeN);
//**************************************************************************
//	Class		: SpacE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Default Constructor
// Purpose	: Declares the SpacE class, which is to be inherited by
//				: the other windows graphics classes.
//**************************************************************************
SpacE::SpacE() : ScreeN()
{
	DtheDomains = new DomaiN();
   Setup();
} // end default constructor

//**************************************************************************
//	Class		: SpacE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Destructor
// Purpose	: Destructs
//**************************************************************************
SpacE::~SpacE()
{
	delete DtheDomains;
	delete Fct1TheFunction;
	delete Fct2TheFunction;
} // end destructor

//**************************************************************************
//	Class		: SpacE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Constructor
// Purpose	: Declares the SpacE class, which is to be inherited by
//				: the other windows graphics classes.
// Notes		: The first argument will be the number at which this window's
//				: dimensions begin, and the second argument tell the window
//				: just how many dimensions there are in the whole system.
//**************************************************************************
SpacE::SpacE(int iDim, int iTotalDims) : ScreeN(iDim, iTotalDims)
{
	DtheDomains = new DomaiN();
   Setup();
} // end first constructor

//**************************************************************************
//	Class		: SpacE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Constructor
// Purpose	: Declares the SpacE class, which is to be inherited by
//				: the other windows graphics classes.
// Notes		: The first argument will be the number at which this window's
//				: dimensions begin, and the second argument tell the window
//				: just how many dimensions there are in the whole system.
//**************************************************************************
SpacE::SpacE(int iDim, int iTotalDims, long double ldFirst, 
	long double ldSec) : ScreeN(iDim, iTotalDims)
{
	DtheDomains = new DomaiN(ldFirst, ldSec);
	Setup();
} // end second constructor

//**************************************************************************
//	Class		: SpacE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Constructor
// Purpose	: Declares the SpacE class, which is to be inherited by
//				: the other windows graphics classes.
// Notes		: The first argument will be the number at which this window's
//				: dimensions begin, and the second argument tell the window
//				: just how many dimensions there are in the whole system.
//**************************************************************************
SpacE::SpacE(int iDim, int iTotalDims, long double ldFirst, 
	long double ldSec, long double ldThird, long double ldFourth)
	: ScreeN(iDim, iTotalDims)
{
	DtheDomains = new DomaiN(ldFirst, ldSec, ldThird, ldFourth);
	Setup();
} // end third constructor

//**************************************************************************
//	Class		: SpacE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Setup
// Purpose	: Does the setup of the data properties
//**************************************************************************
void SpacE::Setup()
{
	Fct1TheFunction = new OnevariablE();
	Fct1TheFunction->SetEquation(2); // Logistic equation
	Fct2TheFunction = new TwovariablE();
	Fct2TheFunction->SetEquation(1); // DiffEqOne equation
//	DtheDomains->SetXInterval(-2, 2);
	ldMiddle_Of_Screen_X = 0;
	ldMiddle_Of_Screen_Y = 0;
	ldThe_Coordinate_Angle = 0;
} // end Setup

//**************************************************************************
//	Class		: SpacE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: SetVariable
// Purpose	: An interface to the OnevariablE class' member function to
//				: set the independent variables.
//**************************************************************************
void SpacE::SetVariable(int iWhichOne, long double ldArgument)
{
	switch(iTotal_Dimensions)
	{
		case 0 :
			Fct1TheFunction->SetVariable(iWhichOne, ldArgument);
		break;

		case 1 :
			Fct1TheFunction->SetVariable(iWhichOne, ldArgument);
		break;

		case 2 :
			Fct1TheFunction->SetVariable(iWhichOne, ldArgument);
		break;

		case 3 :
			Fct2TheFunction->SetVariable(iWhichOne, ldArgument);
		break;

		case 4 :
		break;

		case 5 :
		break;

		case 6 :
		break;

		default :
		break;
	} // end switch
} // end SetVariable

//**************************************************************************
//	Class		: SpacE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: EvaluateIt
// Purpose	: An interface to the OnevariablE class' member function to
//				: evaluate an equation.
//**************************************************************************
long double SpacE::EvaluateIt()
{
long double ldEvalRes;

	switch(iTotal_Dimensions)
	{
		case 0 :
			ldEvalRes = Fct1TheFunction->EvaluateIt();
		break;

		case 1 :
			ldEvalRes = Fct1TheFunction->EvaluateIt();
		break;

		case 2 :
			ldEvalRes = Fct1TheFunction->EvaluateIt();
		break;

		case 3 :
			ldEvalRes = Fct2TheFunction->EvaluateIt();
		break;

		case 4 :
		break;

		case 5 :
		break;

		case 6 :
		break;

		default :
		break;
	} // end switch

	return ldEvalRes;
} // end EvaluateIt


//-------------------------------------------------------------
//	Windows response member functions:
//-------------------------------------------------------------
												 
//**************************************************************************
//	Class		: SpacE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Paint
// Purpose	: Responds to windows' paint message.
//**************************************************************************
void SpacE::Paint(TDC& DCtheDc, BOOL, TRect&)
{
TRect r;

	ScreeN::Paint(DCtheDc, 1, r);
	GraphIt(DCtheDc, 1, r);
} // end Paint

//**************************************************************************
//	Class		: SpacE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Paint
// Purpose	: Responds to windows' paint message.
//**************************************************************************
void SpacE::EvSize(UINT sizeType, TSize& size)
{
  ScreeN::EvSize(sizeType, size);
  // Force Windows to repaint the entire window region
  Invalidate(TRUE);
						  
} // end EvSize

//**************************************************************************
//	Class		: SpacE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: SetScreenVariables
// Purpose	: This function sets up the member data properties such as
//				: iMax's and mid-screen variables.
//**************************************************************************
void SpacE::SetScreenVariables()
{
TRect TRregion;
int iOld;
long double ldTempor1;
long double ldTempor2;

	GetClientRect(TRregion);
	iMaxX = TRregion.right;
	iMaxY = TRregion.bottom;
	ldMiddle_Of_Screen_Y = (long double)(iMaxY) / 2;
	ldMiddle_Of_Screen_X = (long double)(iMaxX) / 2;
	if(Fct1TheFunction->Absolute(ldMiddle_Of_Screen_X) < 
		Fct1TheFunction->GetEpsilon())
			return;
	iOld = Fct1TheFunction->SetEquation(4); // set equation to pythagoreanHy
	ldTempor1 = (long double)(iMaxX);
	ldTempor2 = (long double)(iMaxY);
	Fct1TheFunction->SetParams(ldTempor1);
	Fct1TheFunction->SetVariable(0, ldTempor2);
	ldTempor1 = Fct1TheFunction->EvaluateIt();
	if(Fct1TheFunction->Absolute(ldTempor1) < 
		Fct1TheFunction->GetEpsilon())
			return;
	// Observe that this really isn't the angle between the x and y
	// axes, but since we need to compute the sine of the result, a
	// call to arcsine is superfluous.
	ldThe_Coordinate_Angle = ldTempor2 / ldTempor1;

	Fct1TheFunction->SetEquation(iOld); // set equation back
} // end SetScreenVariables

//**************************************************************************
//	Class		: SpacE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: MathmaticalPlot
// Purpose	: Plots a two-dimensional point (x, y) into the two-dimensional
//				: computer screen.
// Notes		: We tacitly assume that the user has properly set the iMaxX and
//				: iMaxY data properties, because these are used extensively in
//				: this routine; we assume this because it would be too costly
//				: set those properties every time this function is called.
//				: We also assume that the DeviceContext object is good to go.
//				: There is indeed an abundance of assumptions, but this will
//				: significantly increase execution speed.
//**************************************************************************
void SpacE::MathematicalPlot(TClientDC& DCthePixelPen, int iXVal, 
	long double ldYVal, TColor TCc)
{
long double ldTempY;

	ldTempY = ldMiddle_Of_Screen_Y - ldYVal;
	iXVal += ldMiddle_Of_Screen_X;
	DCthePixelPen.SetPixel(iXVal, (int)(ldTempY), TCc);
} // end MathmaticalPlot

//**************************************************************************
//	Class		: SpacE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: ThreeSpacePlot
// Purpose	: Plots the 3-space point into the computer screen; this function
//				: utilizes the protected member function mathematicalPlot.
// Notes		: The user must provide a valid DeviceContext for the particular
//				: TMDIChild object, and we assume that since we're dealing with
//				: mathematical functions of two variables, we can simply accept
//				: the independant variables as integers.
//				: As in the case of mathematicalPlot, we assume that SetScreenVars
//				: has in fact been called.
//				: 
//**************************************************************************
void SpacE::ThreeSpacePlot(TClientDC& DCPixPen, int iXVal, int iYVal,
	long double ldZVal, TColor TCc)
{
long double ldSideH;
long double ldSideB;
long double ldSideC;
long double ldTemp1;
int iXValue;
int iOldEq;

	iOldEq = Fct1TheFunction->SetEquation(5); // set equation to pythagoreanMinus
	ldSideC = ((long double)(iXVal) * ldThe_Coordinate_Angle);
	// Calculate y-axis:
	ldTemp1 = ldSideC + ldZVal;

	Fct1TheFunction->SetParams(ldSideC);
	Fct1TheFunction->SetVariable(0, (long double)(iXVal));
	ldSideB = Fct1TheFunction->EvaluateIt();
	// Calculate x-axis:
	ldSideH = iYVal + ldSideB;
	MathematicalPlot(DCPixPen, (int)(ldSideH), ldTemp1);
	Fct1TheFunction->SetEquation(iOldEq); // set equation back
											  
} // end ThreeSpacePlot

//**************************************************************************
//	Class		: SpacE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: GraphIt
// Purpose	: Actually plots the pixels into the ScreeN object, we use
//				: the OnevariablE and DomaiN classes to do this.
//**************************************************************************
void SpacE::GraphIt(TDC& DCtheDc, BOOL, TRect&)
{
int iXvar;
int iYvar;
int iZvar;
long double ldTemp1;
long double ldTemp2;
long double ldScaleFactor;
long double ldScaleFactor2;
const long double ldYScale = 17;
TPen TPthePen(TColor::LtRed);
TClientDC DCclient(*this);

	SetScreenVariables();
	DCclient.SelectObject(TPthePen);
	ldScaleFactor = DtheDomains->GetLengthX() / (long double)(iMaxX);
	ldScaleFactor2 = DtheDomains->GetLengthY() / (long double)(iMaxX);
	ThreeSpacePlot(DCclient, 0, 0, 0);

	if(iTotal_Dimensions > 2)
	{ // we have 3-dimensions
		for(iXvar=0;iXvar<iMaxX;iXvar++)
			for(iYvar=0;iYvar<iMaxY;iYvar++)
			{
				ldTemp1 = (long double)(iXvar);
				ldTemp1 *= ldScaleFactor;
				ldTemp1 += DtheDomains->GetFirstX();
	
				ldTemp2 = (long double)(iXvar);
				ldTemp2 *= ldScaleFactor2;
				ldTemp2 += DtheDomains->GetFirstY();
	
/*				SetVariable(0, ldTemp1);
				SetVariable(1, ldTemp2);
				ldTemp2 = ldYScale * EvaluateIt();
				ThreeSpacePlot(DCclient, (iXvar - ldMiddle_Of_Screen_X), 
					(iYvar - ldMiddle_Of_Screen_Y), ldTemp2); */
				ThreeSpacePlot(DCclient, (int)(ldTemp1 - ldMiddle_Of_Screen_X), 
					(int)(ldTemp2 - ldMiddle_Of_Screen_Y), 0); 
			} // end for inner loop                           
	} else // end if              
	{ // we have only 2 dimensions 
		for(iXvar=0;iXvar<iMaxX;iXvar++)
		{
			ldTemp1 = (long double)(iXvar);
			ldTemp1 *= ldScaleFactor;
			ldTemp1 += DtheDomains->GetFirstX();

			SetVariable(0, ldTemp1);
			ldTemp2 = ldYScale * EvaluateIt();
			MathematicalPlot(DCclient, (iXvar - ldMiddle_Of_Screen_X), ldTemp2);
		} // end for
	} // end else
} // end GraphIt


