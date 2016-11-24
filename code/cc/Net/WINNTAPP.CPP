//----------------------------------------------------------------------------
//  Project Winnet
//  
//  Copyright © 1996. All Rights Reserved.
//
//  SUBSYSTEM:    Winnet Application
//  FILE:         winntapp.cpp
//  AUTHOR:       
//
//  OVERVIEW
//  ~~~~~~~~
//  Source file for implementation of TWinnetApp (TApplication).
//
//----------------------------------------------------------------------------

#include <owl/pch.h>

#include <classlib/cmdline.h>
#include <winsys/registry.h>
#include <stdio.h>

#include "winntapp.h"
#include "wnntdlgc.h"                        // Definition of client class.
#include "wnntabtd.h"                        // Definition of about dialog.


//{{TWinnetApp Implementation}}


//
// Build a response table for all messages/commands handled
// by the application.
//
DEFINE_RESPONSE_TABLE1(TWinnetApp, TApplication)
//{{TWinnetAppRSP_TBL_BEGIN}}
  EV_COMMAND(CM_HELPABOUT, CmHelpAbout),
//{{TWinnetAppRSP_TBL_END}}
END_RESPONSE_TABLE;


//--------------------------------------------------------
// TWinnetApp
//
TWinnetApp::TWinnetApp() : TApplication("Winnet")
{

  // INSERT>> Your constructor code here.
}


TWinnetApp::~TWinnetApp()
{
  // INSERT>> Your destructor code here.
}


//--------------------------------------------------------
// TWinnetApp
// ~~~~~
// Application intialization.
//
void TWinnetApp::InitMainWindow()
{
  if (nCmdShow != SW_HIDE)
    nCmdShow = (nCmdShow != SW_SHOWMINNOACTIVE) ? SW_SHOWNORMAL : nCmdShow;

  TSDIDecFrame* frame = new TSDIDecFrame(0, GetName(), 0, false);
  frame->SetFlag(wfShrinkToClient);

  // Override the default window style for the main window.
  //
      frame->Attr.Style |= WS_BORDER | WS_CAPTION | WS_CLIPCHILDREN | WS_SYSMENU | WS_THICKFRAME | WS_VISIBLE;
    frame->Attr.Style &= ~(WS_CHILD | WS_MAXIMIZEBOX | WS_MINIMIZEBOX);

  // Assign icons for this application.
  //
  frame->SetIcon(this, IDI_SDIAPPLICATION);
  frame->SetIconSm(this, IDI_SDIAPPLICATION);

  // Menu associated with window and accelerator table associated with table.
  //
  frame->AssignMenu(IDM_SDI);

  // Associate with the accelerator table.
  //
  frame->Attr.AccelTable = IDM_SDI;

  SetMainWindow(frame);

  frame->SetMenuDescr(TMenuDescr(IDM_SDI));
}


//--------------------------------------------------------
// TWinnetApp
// ~~~~~
// Application instance initialization.
//
void TWinnetApp::InitInstance()
{
  TApplication::InitInstance();

  ProcessCmdLine(lpCmdLine);
}


//--------------------------------------------------------
// TWinnetApp
// ~~~~~
// Process command line parameters.
//
void TWinnetApp::ProcessCmdLine(char * CmdLine)
{
  TCmdLine cmd(CmdLine);

  while (cmd.Kind != TCmdLine::Done) {
    if (cmd.Kind == TCmdLine::Option) {
      if (strnicmp(cmd.Token, "unregister", cmd.TokenLen) == 0) {
        UnRegisterInfo();
        return;
      }
    }
    cmd.NextToken();
  }

  RegisterInfo();
}


//--------------------------------------------------------
// TWinnetApp
// ~~~~~
// Register application info.
//
void TWinnetApp::RegisterInfo()
{
  TAPointer<char> buffer = new char[_MAX_PATH];

  GetModuleFileName(buffer, _MAX_PATH);

  TRegKey(TRegKey::ClassesRoot, "Winnet.Application\\DefaultIcon").SetDefValue(0, REG_SZ, buffer, strlen(buffer));
  strcat(buffer, ",1");
}


//--------------------------------------------------------
// TWinnetApp
// ~~~~~
// Unregister application info.
//
void TWinnetApp::UnRegisterInfo()
{
  TAPointer<char> buffer = new char[_MAX_PATH];

  GetModuleFileName(buffer, _MAX_PATH);

  TRegKey(TRegKey::ClassesRoot, "Winnet.Application").DeleteKey("DefaultIcon");
  TRegKey(TRegKey::ClassesRoot, "Winnet.Document.1").DeleteKey("DefaultIcon");

  TRegKey::ClassesRoot.DeleteKey("Winnet.Application");
}



//{{TSDIDecFrame Implementation}}


TSDIDecFrame::TSDIDecFrame(TWindow* parent, const char far* title, TWindow* clientWnd, bool trackMenuSelection, TModule* module)
:
  TDecoratedFrame(parent, title, !clientWnd ? new TWinnetDlgClient(0) : clientWnd, trackMenuSelection, module)
{
  // INSERT>> Your constructor code here.

}


TSDIDecFrame::~TSDIDecFrame()
{
  // INSERT>> Your destructor code here.

}


//--------------------------------------------------------
// TWinnetApp
// ~~~~~~~~~~~
// Menu Help About Winnet command
//
void TWinnetApp::CmHelpAbout()
{
  // Show the modal dialog.
  //
  TWinnetAboutDlg(MainWindow).Execute();
}



int OwlMain(int , char* [])
{
  TWinnetApp   app;
  return app.Run();
}
