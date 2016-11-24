//**************************************************************************
//	File		: InfoWin.cpp
// Purpose	: Defines the InformationwiN class
//**************************************************************************
#include"infowin.hpp"

//**************************************************************************
//	Response Table:
//**************************************************************************
DEFINE_RESPONSE_TABLE1(InformationwiN, ScreeN)
  EV_WM_SIZE,
END_RESPONSE_TABLE;

IMPLEMENT_CASTABLE1(InformationwiN, ScreeN);
//*************************************************************************
// Class		: InformationwiN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Default Constructor
//	Purpose	: This class functions as an information giver in the
//				: MDI context.
//*************************************************************************
InformationwiN::InformationwiN() : ScreeN()
{
	Setup();
} // end default constructor

//*************************************************************************
// Class		: InformationwiN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Constructor
//	Purpose	: This class functions as an information giver in the
//				: MDI context.
//*************************************************************************
InformationwiN::InformationwiN(int iNum) : ScreeN()
{
	Setup(iNum);
} // end constructor

//*************************************************************************
// Class		: InformationwiN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Destructor
//	Purpose	: This class functions as an information giver in the
//				: MDI context.
//*************************************************************************
InformationwiN::~InformationwiN()
{
int iIter;

	if(iNumber_Of_Lines != 0)
   {
		for(iIter=0;iIter<iNumber_Of_Lines;iIter++)
	   	delete[] ppcTextStrings[iIter];
	   delete[] ppcTextStrings;
   } // end if
   if(pcFont_Name != NULL)
   	delete[] pcFont_Name;
} // end destructor

//*************************************************************************
// Class		: InformationwiN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Setup
//	Purpose	: Sets up our text string array
//*************************************************************************
void InformationwiN::Setup(int iNumLines)
{
	SetBkgndColor(TColor::Gray);
   SetTextSpacing(25);
   pcFont_Name = NULL;
   iNumber_Of_Lines = 0;
   SetNumberOfLines(iNumLines);
   SetFontName("Pompeii Capitals");
   Tester();
} // end Setup

//*************************************************************************
// Class		: InformationwiN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: SetNumberOfLines
//	Purpose	: Sets the number of lines that the string buffer contains
// Notes		: If the old string buffer exists, it gets deleted, so the user
//				: must get all necessary info out first.
//*************************************************************************
void InformationwiN::SetNumberOfLines(int iNLines)
{
int iI;

	if(iNumber_Of_Lines != 0)
   {
		for(iI=0;iI<iNumber_Of_Lines;iI++)
	   	delete[] ppcTextStrings[iI];
	   delete[] ppcTextStrings;
   } // end if
   iNumber_Of_Lines = iNLines;
   ppcTextStrings = new char*[iNumber_Of_Lines];
   for(iI=0;iI<iNumber_Of_Lines;iI++)
   {
   	ppcTextStrings[iI] = new char[164];
      ppcTextStrings[iI][0] = '\0';
   } // end for
} // end SetNumberOfLines

//*************************************************************************
// Class		: InformationwiN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: SetFontName
//	Purpose	: Sets the current font name, and then returns the old one.
// Notes		: The caller must take care of the return argument or else
//				: we have ourselves a memory leak.
//*************************************************************************
void InformationwiN::SetFontName(char* pcTheName)
{
	if(pcTheName == NULL)
   	return;
   if(pcFont_Name != NULL)
   	delete[] pcFont_Name;
   pcFont_Name = new char[64];
   pcFont_Name[0] = '\0';
   strcpy(pcFont_Name, pcTheName);
} // end SetFontName

//*************************************************************************
// Class		: InformationwiN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: SetTextInformation
//	Purpose	: Sets the corresponding entry in the buffer to the associated
//				: text string.
//*************************************************************************
int InformationwiN::SetTextInformation(int iLine, char* pcTheText)
{
int iSet;

	if(iLine >= iNumber_Of_Lines)
   	return -1;
   if(pcTheText == NULL)
   	return -1;
   else if(strlen(pcTheText) > 164)
   	return -1;
   ppcTextStrings[iLine][0] = '\0';
   strcpy(ppcTextStrings[iLine], pcTheText);
   iSet = iLine;
   
   return iSet;
} // end SetTextInformation

//*************************************************************************
// Class		: InformationwiN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Tester
//	Purpose	: Just 'fer testing
//				: 
//*************************************************************************
void InformationwiN::Tester()
{
	SetTextInformation(0, "Purple Haze All in my Brain");
	SetTextInformation(1, "Lately things just don't seem the same");
	SetTextInformation(2, "Actin' funny, but I don't know why");
	SetTextInformation(3, "'Scuse my while I kiss the sky.");
	SetTextInformation(4, "Purple Haze All around");
} // end tester


//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Window's Response Functions:
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

//*************************************************************************
// Class		: InformationwiN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Paint
//	Purpose	: This is the function which is called when Windows wants to 
//				: re-paint the screen.
//*************************************************************************
void InformationwiN::Paint(TDC& DCtheDc, bool, TRect&)
{
TClientDC DCpen(*this);
int iW;
int iY = 10;

	if(pcFont_Name == NULL)
   	return;
      
	TFont TFtheFont(pcFont_Name);
   DCpen.SelectObject(TFtheFont);
   DCpen.SetBkColor(TColor::Gray);
   DCpen.SetTextColor(TColor::Black);

   for(iW=0;iW<iNumber_Of_Lines;iW++)
   {
	   DCpen.TextOut(10, iY, ppcTextStrings[iW], strlen(ppcTextStrings[iW]));
      iY += iHeight;
   } // end for loop

} // end Paint

//*************************************************************************
// Class		: InformationwiN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: EvSize
//	Purpose	: Responds to Windows' WM_SIZE system message, and then tells
//				: windows to re-paint (invalidate)
//*************************************************************************
void InformationwiN::EvSize(UINT, TSize&)
{
TRect TRSize;

	GetClientRect(TRSize);
   iMaxX = TRSize.right;
   iMaxY = TRSize.bottom;
	Invalidate();
} // end EvSize
