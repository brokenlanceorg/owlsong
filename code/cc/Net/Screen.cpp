//**************************************************************************
//	File		: Screen.cpp
// Purpose	: Implements the ScreeN class
// Author	: Brandon Benham
//**************************************************************************
#include"screen.hpp"

DEFINE_RESPONSE_TABLE1(ScreeN, TBaseDemoWindow)
  EV_WM_SIZE,
END_RESPONSE_TABLE;

IMPLEMENT_CASTABLE1(ScreeN, TBaseDemoWindow);

const NumColors = 48;
const TColor aiTheColors[NumColors] = {
  0x00FFFFFFl,0x00E0E0E0l,0x00C0C0FFl,0x00C0E0FFl,0x00E0FFFFl,0x00C0FFC0l,
  0x00FFFFC0l,0x00FFC0C0l,0x00FFC0FFl,0x000000C0l,0x000040C0l,0x0000C0C0l,
  0x0000C000l,0x00C0C000l,0x00C00000l,0x00C000C0l,0x00C0C0C0l,0x00404040l,
  0x008080FFl,0x0080C0FFl,0x0080FFFFl,0x0080FF80l,0x00FFFF80l,0x00FF8080l,
  0x00FF80FFl,0x00000080l,0x00004080l,0x00008080l,0x00008000l,0x00808000l,
  0x00800000l,0x00800080l,0x00808080l,0x00000000l,0x000000FFl,0x000080FFl,
  0x0000FFFFl,0x0000FF00l,0x00FFFF00l,0x00FF0000l,0x00FF00FFl,0x00000040l,
  0x00404080l,0x00004040l,0x00004000l,0x00404000l,0x00400000l,0x00400040l
};

//**************************************************************************
//	Class		: ScreeN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Default Constructor
// Purpose	: Declares the ScreeN class, which is to be inherited by
//				: the other windows graphics classes.
//**************************************************************************
ScreeN::ScreeN() : TBaseDemoWindow()
{
	iNum_Dimensions = 0;
} // end default constructor

//**************************************************************************
//	Class		: ScreeN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Destructor
// Purpose	: Destructs
//**************************************************************************
ScreeN::~ScreeN()
{
/*	if(PLogicalPalette != NULL)
		delete PLogicalPalette; 
		delete StaticControl; */
} // end destructor

//**************************************************************************
//	Class		: ScreeN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Constructor
// Purpose	: Declares the ScreeN class, which is to be inherited by
//				: the other windows graphics classes.
// Notes		: The first argument will be the number at which this window's
//				: dimensions begin, and the second argument tell the window
//				: just how many dimensions there are in the whole system.
//**************************************************************************
ScreeN::ScreeN(int iDim, int iTotalDims) : TBaseDemoWindow()
{
	iNum_Dimensions = iDim; // if iTotalDims is > 2, this is first dimension
	iTotal_Dimensions = iTotalDims;
	Setup();
} // end constructor

//**************************************************************************
//	Class		: ScreeN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Setup
// Purpose	: Does the setup of the data properties
//**************************************************************************
void ScreeN::Setup()
{
char acStatic[64];
char acTemp[15];

	acStatic[0] = acTemp[0] = '\0';
	itoa(iNum_Dimensions + 1, acTemp, 10);
	strcpy(acStatic, "Dimensions: ");
	strcat(acStatic, acTemp);
	strcat(acStatic, ", ");
	itoa(iNum_Dimensions + 2, acTemp, 10);
	strcat(acStatic, acTemp);
	strcat(acStatic, ", ");
	itoa(iNum_Dimensions + 3, acTemp, 10);
	strcat(acStatic, acTemp);
/*	StaticControl = new TStatic(this, 100,
		 acStatic,10,10,10,10,0); */
	SetBkgndColor(TColor::Black);
	iMaxX = 0;
	iMaxY = 0;
/*	SetupPalette();
	DoPalette(); */
} // end Setup

//**************************************************************************
//	Class		: ScreeN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: SetupPalette
// Purpose	: Changes the palette to a custom set of RGB color values
//**************************************************************************
void ScreeN::SetupPalette()
{
int iVal;

	PLogicalPalette = (LOGPALETTE*)new char[sizeof(LOGPALETTE) +
		sizeof(PALETTEENTRY) * NumColors];
	PLogicalPalette->palVersion = 0x300;
	PLogicalPalette->palNumEntries = NumColors;
	for(iVal=0;iVal<NumColors;iVal++)
	{
		PLogicalPalette->palPalEntry[iVal].peRed = aiTheColors[iVal].Red();
		PLogicalPalette->palPalEntry[iVal].peGreen = aiTheColors[iVal].Green();
		PLogicalPalette->palPalEntry[iVal].peBlue = aiTheColors[iVal].Blue();
		PLogicalPalette->palPalEntry[iVal].peFlags = PC_RESERVED;
	} // end for loop
	DCthePalette = new TPalette(PLogicalPalette);
} // end SetupPalette                             

//**************************************************************************
//	Class		: ScreeN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: DoPalette
// Purpose	: Changes the palette to a custom set of RGB color values
//**************************************************************************
void ScreeN::DoPalette()
{
TRect TtheRect;

	DCpainter->SelectObject(*DCthePalette);
	DCpainter->RealizePalette();
	GetClientRect(TtheRect);
	TColor palColor(0);
	DCtheBrush = new TBrush(palColor);
	DCpainter->SelectObject(*DCtheBrush);
	DCpainter->FillRect(TtheRect, *DCtheBrush);
		
} // end DoPalette

//**************************************************************************
//	Class		: ScreeN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: D2Axes
// Purpose	: Draws two-dimensional axes in the drawing window.
// Notes		: We utilize this object's windowsize, thus this function
//				: can be called only after a size event.
//**************************************************************************
void ScreeN::D2Axes(TDC& Dc, TColor TCtheColor)
{
TRect TRtheRect;
TClientDC DCclient(*this);

	GetClientRect(TRtheRect);
	TPen TPthePen(TCtheColor);
	DCclient.SelectObject(TPthePen);
	// y-axis:
	DCclient.MoveTo(TRtheRect.right / 2, 0);
	DCclient.LineTo(TRtheRect.right / 2, TRtheRect.bottom);
	// x-axis:
	DCclient.MoveTo(0, TRtheRect.bottom / 2);
	DCclient.LineTo(TRtheRect.right, TRtheRect.bottom / 2);
			
} // end Two_D_Axes

//**************************************************************************
//	Class		: ScreeN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: D3Axes
// Purpose	: Draws three-dimensional axes in the drawing window.
//**************************************************************************
void ScreeN::D3Axes(TColor TCtheColor)
{
TRect TRtheRect;
TClientDC DCclient(*this);

	GetClientRect(TRtheRect);
	TPen TPthePen(TCtheColor);
	DCclient.SelectObject(TPthePen);
	// x-axis:
	DCclient.MoveTo(0, TRtheRect.bottom);
	DCclient.LineTo(TRtheRect.right, 0);
	// y-axis:                
	DCclient.MoveTo(0, TRtheRect.bottom / 2);
	DCclient.LineTo(TRtheRect.right, TRtheRect.bottom / 2);
	// z-axis:
	DCclient.MoveTo(TRtheRect.right / 2, TRtheRect.bottom / 2);
	DCclient.LineTo(TRtheRect.right / 2, 0);
											  
} // end D3Axes

//-------------------------------------------------------------
//	Windows response member functions:
//-------------------------------------------------------------
												 
//**************************************************************************
//	Class		: ScreeN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Paint
// Purpose	: Responds to windows' paint message.
//**************************************************************************
void ScreeN::Paint(TDC& DCtheDc, bool, TRect&)
{
TRect TRtheRect;
TColor AxColor(TColor::LtBlue);

	TClientDC DCclient(*this);	
	SetBkgndColor(TColor::Black);
   if(iTotal_Dimensions == 2)
		D2Axes(DCtheDc, AxColor);
   else
		D3Axes();
} // end Paint

//**************************************************************************
//	Class		: ScreeN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Paint
// Purpose	: Responds to windows' paint message.
//**************************************************************************
void ScreeN::EvSize(UINT sizeType, TSize& size)
{
	TWindow::EvSize(sizeType, size);
	// Force Windows to repaint the entire window region
	Invalidate(TRUE);
	/*if (StaticControl)
		StaticControl->MoveWindow(0, size.cy - 20, size.cx, 20, TRUE); */
	iMaxX = size.cx;
	iMaxY = size.cy;
						  
} // end EvSize

