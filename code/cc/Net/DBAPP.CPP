//----------------------------------------------------------------------------
//  Project Db
//  
//  Copyright © 1996. All Rights Reserved.
//
//  SUBSYSTEM:    Db Application
//  FILE:         dbapp.cpp
//  AUTHOR:       
//
//  OVERVIEW
//  ~~~~~~~~
//  Source file for implementation of TDbApp (TApplication).
//
//----------------------------------------------------------------------------

#include <owl/pch.h>

#include <owl/buttonga.h>
#include <owl/docmanag.h>
#include <owl/statusba.h>
#include <owl/filedoc.h>

#include "dbapp.h"
#include "dbdlgcln.h"                        // Definition of client class.


//{{TDbApp Implementation}}



DEFINE_DOC_TEMPLATE_CLASS(TFileDocument, TDbDlgClient, DlgDTPL);
DlgDTPL __dlogClnt("All Files", "*.*", 0, "TXT", dtAutoDelete | dtUpdateDir);

//{{DOC_VIEW}}
//{{DOC_VIEW_END}}

//{{DOC_MANAGER}}
//{{DOC_MANAGER_END}}


//
// Build a response table for all messages/commands handled by the application.
//
DEFINE_RESPONSE_TABLE1(TDbApp, TApplication)
//{{TDbAppRSP_TBL_BEGIN}}
  EV_OWLVIEW(dnCreate, EvNewView),
  EV_OWLVIEW(dnClose,  EvCloseView),
  EV_COMMAND(CM_HELPABOUT, CmHelpAbout),
//{{TDbAppRSP_TBL_END}}
END_RESPONSE_TABLE;


//--------------------------------------------------------
// TDbApp
// ~~~~~
//
TDbApp::TDbApp() : TApplication("Net")
{
  SetDocManager(new TDocManager(dmSDI, this));

  // INSERT>> Your constructor code here.
}


TDbApp::~TDbApp()
{
  // INSERT>> Your destructor code here.
}


//--------------------------------------------------------
// TDbApp
// ~~~~~
// Application main window construction & intialization.
//
void TDbApp::InitMainWindow()
{
  if (nCmdShow != SW_HIDE)
    nCmdShow = (nCmdShow != SW_SHOWMINNOACTIVE) ? SW_SHOWNORMAL : nCmdShow;

  TSDIDecFrame* frame = new TSDIDecFrame(0, GetName(), 0, false, this);

  // Assign icons for this application.
  //
  frame->SetIcon(this, IDI_SDIAPPLICATION);
  frame->SetIconSm(this, IDI_SDIAPPLICATION);

  SetMainWindow(frame);

  // Borland Windows custom controls.
  //
  EnableBWCC();
}


//--------------------------------------------------------
// TDbApp
// ~~~~~
// Application instance initialization.
//
void TDbApp::InitInstance()
{
  TApplication::InitInstance();
  GetDocManager()->CreateAnyDoc(0, dtNewDoc);
}



//--------------------------------------------------------
// TDbApp
// ~~~~~
// Response Table handlers:
//
void TDbApp::EvNewView(TView& view)
{
  GetMainWindow()->SetClientWindow(view.GetWindow());
  if (!view.IsOK())
    GetMainWindow()->SetClientWindow(0);
  else if (view.GetViewMenu())
    GetMainWindow()->MergeMenu(*view.GetViewMenu());
}


void TDbApp::EvCloseView(TView&)
{
  GetMainWindow()->SetClientWindow(0);
  GetMainWindow()->SetCaption("Net");
}


//{{TSDIDecFrame Implementation}}


TSDIDecFrame::TSDIDecFrame(TWindow* parent, const char far* title, TWindow* clientWnd, bool trackMenuSelection, TModule* module)
:
  TDecoratedFrame(parent, title, clientWnd, trackMenuSelection, module)
{
  // INSERT>> Your constructor code here.

}


TSDIDecFrame::~TSDIDecFrame()
{
  // INSERT>> Your destructor code here.

}


//--------------------------------------------------------
// TDbApp
// ~~~~~~~~~~~
//
// Menu Help About Db command
void TDbApp::CmHelpAbout()
{
}


int OwlMain(int , char* [])
{
  TDbApp   app;
  return app.Run();
}
