//----------------------------------------------------------------------------
// ObjectWindows - (C) Copyright 1991, 1993 by Borland International
//   A GDI demo program
//----------------------------------------------------------------------------
#include <owl\owlpch.h>
#include <owl\applicat.h>
#include <owl\mdi.h>
#include <string.h>
#include "demobase.h"
#include "bitblt.h"
#include "arty.h"

// Menu bar constants
const int MenuId            =  100;  // Resource Id of the menubar
const int MoveToLineToDemoId=  201;  // Demo->MoveToDemo Id
const int FontDemoId        =  202;  // Demo->Font Demo Id
const int BitBltDemoId      =  203;  // Demo->BitBlt Demo Id
const int ArtyDemoId        =  204;  // Demo->Arty Demo Id

const int IconId            =  100;  // Resource Id of the program icon

IMPLEMENT_CASTABLE1(TBaseDemoWindow, TWindow);

//----------------------------------------------------------------------------

class TGdiDemoWindow : public TMDIClient {
  public:
	 TGdiDemoWindow() : TMDIClient() { Attr.Style |= WS_TABSTOP; }
	 void Paint(TDC&, BOOL, TRect&);

  protected:
	 void SetupWindow();

	 void CmMoveToLineToDemo();
	 void CmFontDemo();
	 void CmBitBltDemo();
	 void CmArtyDemo();

	 void EvTimer(UINT);
	 void EvSize(UINT, TSize&);
	 void EvDestroy();
	 // Data properties:
  private:
	 TPoint      WindowSize;
	 TBitmap*    ScratchBitmapBMP;
	 TBitmap*    StretchedBackBMP;
	 TBitmap*    BackgroundBMP;

  DECLARE_RESPONSE_TABLE(TGdiDemoWindow);
};

DEFINE_RESPONSE_TABLE1(TGdiDemoWindow, TMDIClient)
  EV_COMMAND(BitBltDemoId, CmBitBltDemo),
  EV_COMMAND(ArtyDemoId, CmArtyDemo),
  EV_WM_SIZE,
  EV_WM_TIMER,
  EV_WM_DESTROY,
END_RESPONSE_TABLE;


//
// Setup the main demo window, and try to allocate its timer
//
void
TGdiDemoWindow::SetupWindow()
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
} // end SetupWindow

//
// In response to a demo command, create one of the demo windows as the client
// of an mdi child frame. Turn of the icon for the mdi child to allow the
// client to paint itself when iconic.
//
void
TGdiDemoWindow::CmBitBltDemo()
{
  TMDIChild* child = new TMDIChild(*this, "BitBlt Window", new TBitBltWindow);
  child->SetIcon(0, 0);
  child->Create();
}

void
TGdiDemoWindow::CmArtyDemo()
{
  TMDIChild* child = new TMDIChild(*this, "Arty Window", new TArtyWindow);
  child->SetIcon(0, 0);
  child->Create();
}

//
// Get client demo window from mdi child frame using typesafe downcasting
//
void
ChildTimers(TWindow* p, void*)
{
  TFrameWindow* frame = TYPESAFE_DOWNCAST(p, TFrameWindow);
  CHECK(frame);
  TBaseDemoWindow* demoWin = TYPESAFE_DOWNCAST(frame->GetClientWindow(), TBaseDemoWindow);
  CHECK(demoWin);
  demoWin->TimerTick();
}

//
// In response to WMTimer messages, each MDI child window's TimerTick
// Method is called.
//
void
TGdiDemoWindow::EvTimer(UINT)
{
  //ForEach(ChildTimers, 0);
}

void
TGdiDemoWindow::EvSize(UINT sizeType, TSize& TSsize)
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

void
TGdiDemoWindow::EvDestroy()
{
  KillTimer(0);
  TMDIClient::EvDestroy();
}

void
TGdiDemoWindow::Paint(TDC& DCtheDc, BOOL, TRect&)
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
  MainWindow = new TMDIFrame("GDI Demo", MenuId, *new TGdiDemoWindow);
  MainWindow->SetIcon(this, IconId);
}

int
OwlMain(int /*argc*/, char* /*argv*/ [])
{
  return TGdiDemoApp().Run();
}
