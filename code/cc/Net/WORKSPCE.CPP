//*************************************************************************
//	File		: Workspce.cpp
//	Purpose	: Defines the workspace object which is a mdiclient decendent
//				: that will house our graphical interface views.
//*************************************************************************
#include"space.hpp"
#include"workspce.hpp"
// Menu bar constants
const int BackgroundId      =  100;	// Bitmap ID of background bitmap 
const int MenuId            =  100;	// Resource Id of the menubar
const int ArtyDemoId        =  204;	// Demo->Arty Demo Id
const int IconId            =  100;	// Resource Id of the program icon
FilesourcE* NumerR::FlogFile = new FilesourcE("c:\\bb\\code\\temp\\mathfunc.log", _BYTE, 0);
VectoR* SpacE::vTheVars = new VectoR(4);
OnevariablE* SpacE::Fct1TheFunction = new OnevariablE();

//*************************************************************************
// Response Tables:
//*************************************************************************
IMPLEMENT_CASTABLE1(TBaseDemoWindow, TWindow);

DEFINE_RESPONSE_TABLE1(TMDIWorkspacE, TMDIClient)
  EV_COMMAND(ArtyDemoId, CmDispatchViews),
  EV_WM_SIZE,
  EV_WM_TIMER,
  EV_WM_DESTROY,
END_RESPONSE_TABLE;
//*************************************************************************
// Class		: TMDIWorkspacE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Constructor
//	Purpose	: Does the basic setup calls the parents' Setup func, and 
//				: then creates the TBitmap objects.
//*************************************************************************
TMDIWorkspacE::TMDIWorkspacE(int iNumDim) : TMDIClient()
{
	iNumber_Dimensions = iNumDim;
	if(iNumber_Dimensions == 2)
		iNumber_Windows = 1;
	else
		iNumber_Windows = iNumber_Dimensions - 2;
} // end constructor
								  
//*************************************************************************
// Class		: TMDIWorkspacE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Destructor
//	Purpose	: Destruction
//*************************************************************************
TMDIWorkspacE::~TMDIWorkspacE()
{
	delete BackgroundBMP;
	delete ScratchBitmapBMP;
	delete StretchedBackBMP;
	delete NumerR::FlogFile;
	delete[] TMDItheChildren;
   /*if(TMDItheStatusWindow != NULL)
		delete TMDItheStatusWindow; */
} // end destructor
								  
//*************************************************************************
// Class		: TMDIWorkspacE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: SetupWindow
//	Purpose	: Does the basic setup calls the parents' Setup func, and 
//				: then creates the TBitmap objects.
//*************************************************************************
void
TMDIWorkspacE::SetupWindow()
{
	TMDIClient::SetupWindow();
	
	int result = IDRETRY;
	while (SetTimer(0, 50, 0) == 0 && result == IDRETRY)
		result = MessageBox("Could not Create Timer", "GDIDemo", MB_RETRYCANCEL);
	if (result == IDCANCEL)
		PostQuitMessage(0);
	BackgroundBMP = new TBitmap(*GetApplication(), BackgroundId);
	TClientDC mainwinDC(*this);
	ScratchBitmapBMP = new TBitmap(mainwinDC, 80, 80);
	StretchedBackBMP = new TBitmap(mainwinDC, 1000, 1000);
	TMDItheChildren = new TMDIChild*[iNumber_Windows];
} // end SetupWindow

//*************************************************************************
// Class		: TMDIWorkspacE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: CmDispatchViews
//	Purpose	: 
//				: 
/* Create our children windows
  Children[0] = new TMDIChild(*this, "Rectangles Window",
										new RectsWindow);
  Children[1] = new TMDIChild(*this, "Shapes, Lines, & Fills Window",
										new ShapesAndFillsWindow);
  Children[2] = new TMDIChild(*this, "Ellipses & Arcs Window",
										new SmileyWindow);

Set the styles so the children windows are hidden, disabled,
and without title-bar controls. Then create the windows.
  for (int iLoop = 0; iLoop < 3; iLoop++) {
	  Children[iLoop]->Attr.Style &= ~(WS_VISIBLE     |
												  WS_SYSMENU     |
												  WS_MINIMIZEBOX |
												  WS_MAXIMIZEBOX);
	  Children[iLoop]->Attr.Style |= WS_DISABLED;
	  Children[iLoop]->Create();
  }

Post the message that will tell us to show and tile the
children windows
  PostMessage(WM_COMMAND, ShowChildrenId);
In Response Table->  EV_COMMAND(ShowChildrenId, CmShowChildren),*/
//*************************************************************************
void          
TMDIWorkspacE::CmDispatchViews()
{
	for(int i=0;i<iNumber_Windows;i++)
	{
		TMDItheChildren[i] = new TMDIChild(*this, "Function View", 
			new SpacE(i, iNumber_Dimensions));
		TMDItheChildren[i]->SetIcon(0, 0);
		TMDItheChildren[i]->Create();
	} // end for loop
   TMDItheStatusWindow = new TMDIChild(*this, "Information Window", 
		new InformationwiN(5));
   TMDItheStatusWindow->SetIcon(0, 0);
   TMDItheStatusWindow->Create();
}                      

//*************************************************************************
// Class		: TMDIWorkspacE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: ChildTimers
//	Purpose	: Not implemented
//				: 
//*************************************************************************
void          
ChildTimers(TWindow* p, void*)
{
  TFrameWindow* frame = TYPESAFE_DOWNCAST(p, TFrameWindow);
  CHECK(frame);
  TBaseDemoWindow* demoWin = TYPESAFE_DOWNCAST(frame->GetClientWindow(), TBaseDemoWindow);
  CHECK(demoWin);
  demoWin->TimerTick();
} // ChildTimers

//*************************************************************************
// Class		: TMDIWorkspacE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: EvTimer
//	Purpose	: Not installed
//*************************************************************************
void          
TMDIWorkspacE::EvTimer(UINT)
{
  //ForEach(ChildTimers, 0);
} // end EvTimer

//*************************************************************************
// Class		: TMDIWorkspacE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: EvSize
//	Purpose	: EventHandler which is dispatched upon a resize command
//*************************************************************************
void          
TMDIWorkspacE::EvSize(UINT sizeType, TSize& TSsize)
{
	TMDIClient::EvSize(sizeType, TSsize);
	WindowSize.x = TSsize.cx;
	WindowSize.y = TSsize.cy;
	TClientDC winDC(*this);
	TMemoryDC stretchedDC(winDC);
	TMemoryDC memDC(winDC);
	stretchedDC.SelectObject(*StretchedBackBMP);
	memDC.SelectObject(*BackgroundBMP);
	// Set cursur to hourglass:
	HCURSOR oldcur = ::SetCursor(::LoadCursor(0, IDC_WAIT));
	stretchedDC.StretchBlt(0, 0, WindowSize.x, WindowSize.y, memDC,
		0, 0, 640, 480, SRCCOPY);
	::SetCursor(oldcur);
	Invalidate(); // Tell windows to re-paint

} // end EvSize

//*************************************************************************
// Class		: TMDIWorkspacE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: EvDestroy
//	Purpose	: Called by windows when we destruct.
//*************************************************************************
void          
TMDIWorkspacE::EvDestroy()
{
  KillTimer(0);
  TMDIClient::EvDestroy();
} // end EvDestroy

//*************************************************************************
// Class		: TMDIWorkspacE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Paint
//	Purpose	: This function is called by windows whenever a re-painting
//				: of the app is necessary.
//*************************************************************************
void
TMDIWorkspacE::Paint(TDC& DCtheDc, bool, TRect&)
{
	TMemoryDC memorDC(DCtheDc);
	memorDC.SelectObject(*StretchedBackBMP);
	DCtheDc.BitBlt(0, 0, WindowSize.x, WindowSize.y, memorDC, 0, 0, SRCCOPY);
} // end Paint


//----------------------------------------------------------------------------

class TGdiDemoApp : public TApplication {
  public:
	 TGdiDemoApp() : TApplication() {}
	 void InitMainWindow();
};

void
TGdiDemoApp::InitMainWindow()
{
  MainWindow = new TMDIFrame("Graph Demo", MenuId, *new TMDIWorkspacE(4));
  MainWindow->SetIcon(this, IconId);
}

int
OwlMain(int /*argc*/, char* /*argv*/ [])
{
  return TGdiDemoApp().Run();
}
