//----------------------------------------------------------------------------
//  Project Oletest
//  
//  Copyright © 1996. All Rights Reserved.
//
//  SUBSYSTEM:    Oletest Application
//  FILE:         oltstapp.cpp
//  AUTHOR:       
//
//  OVERVIEW
//  ~~~~~~~~
//  Source file for implementation of TOletestApp (TApplication).
//
//----------------------------------------------------------------------------

#include <owl/pch.h>

#include <owl/buttonga.h>
#include <owl/docmanag.h>
#include <owl/statusba.h>
#include <owl/filedoc.h>

#include <classlib/cmdline.h>
#include <classlib/filename.h>

#include "oltstapp.h"
#include "oltstdlc.h"                        // Definition of client class.


//{{TOletestApp Implementation}}



DEFINE_DOC_TEMPLATE_CLASS(TFileDocument, TOletestDlgClient, DlgDTPL);
DlgDTPL __dlogClnt("All Files", "*.*", 0, "TXT", dtAutoDelete | dtUpdateDir);

//{{DOC_VIEW}}
//{{DOC_VIEW_END}}

//{{DOC_MANAGER}}
//{{DOC_MANAGER_END}}


//
// Build a response table for all messages/commands handled by the application.
//
DEFINE_RESPONSE_TABLE2(TOletestApp, TRecentFiles, TApplication)
//{{TOletestAppRSP_TBL_BEGIN}}
  EV_OWLVIEW(dnCreate, EvNewView),
  EV_OWLVIEW(dnClose,  EvCloseView),
  EV_COMMAND(CM_FILESEND, CmFileSend),
  EV_COMMAND_ENABLE(CM_FILESEND, CeFileSend),
  EV_COMMAND(CM_HELPABOUT, CmHelpAbout),
  EV_OWLDOCUMENT(dnCreate, EvOwlDocument),
  EV_OWLDOCUMENT(dnRename, EvOwlDocument),
  EV_REGISTERED(MruFileMessage, CmFileSelected),
//{{TOletestAppRSP_TBL_END}}
END_RESPONSE_TABLE;


//--------------------------------------------------------
// TOletestApp
// ~~~~~
//
TOletestApp::TOletestApp() : TApplication("Oletest"), TRecentFiles(".\\Oletest.ini", 4)
{
  SetDocManager(new TDocManager(dmSDI, this));

  ApxMail = new TMailer();

  // INSERT>> Your constructor code here.
}


TOletestApp::~TOletestApp()
{
  delete ApxMail;

  // INSERT>> Your destructor code here.
}


//--------------------------------------------------------
// TOletestApp
// ~~~~~
// Application main window construction & intialization.
//
void TOletestApp::InitMainWindow()
{
  if (nCmdShow != SW_HIDE)
    nCmdShow = (nCmdShow != SW_SHOWMINNOACTIVE) ? SW_SHOWNORMAL : nCmdShow;

  TSDIDecFrame* frame = new TSDIDecFrame(0, GetName(), 0, true, this);

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
// TOletestApp
// ~~~~~
// Application instance initialization.
//
void TOletestApp::InitInstance()
{
  TApplication::InitInstance();
  GetDocManager()->CreateAnyDoc(0, dtNewDoc);
  ProcessCmdLine(lpCmdLine);
}


//--------------------------------------------------------
// TOletestApp
// ~~~~~
// Process command line parameters.
//
void TOletestApp::ProcessCmdLine(char * CmdLine)
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
// TOletestApp
// ~~~~~
// Register application info.
//
void TOletestApp::RegisterInfo()
{
  TAPointer<char> buffer = new char[_MAX_PATH];

  GetModuleFileName(buffer, _MAX_PATH);

  TRegKey(TRegKey::ClassesRoot, "Oletest.Application\\DefaultIcon").SetDefValue(0, REG_SZ, buffer, strlen(buffer));
  strcat(buffer, ",1");
}


//--------------------------------------------------------
// TOletestApp
// ~~~~~
// Unregister application info.
//
void TOletestApp::UnRegisterInfo()
{
  TAPointer<char> buffer = new char[_MAX_PATH];

  GetModuleFileName(buffer, _MAX_PATH);

  TRegKey(TRegKey::ClassesRoot, "Oletest.Application").DeleteKey("DefaultIcon");
  TRegKey(TRegKey::ClassesRoot, "Oletest.Document.1").DeleteKey("DefaultIcon");

  TRegKey::ClassesRoot.DeleteKey("Oletest.Application");
}



//--------------------------------------------------------
// TOletestApp
// ~~~~~
// Response Table handlers:
//
void TOletestApp::EvNewView(TView& view)
{
  GetMainWindow()->SetClientWindow(view.GetWindow());
  if (!view.IsOK())
    GetMainWindow()->SetClientWindow(0);
  else if (view.GetViewMenu())
    GetMainWindow()->MergeMenu(*view.GetViewMenu());
}


void TOletestApp::EvCloseView(TView&)
{
  GetMainWindow()->SetClientWindow(0);
  GetMainWindow()->SetCaption("Oletest");
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


void TOletestApp::CeFileSend(TCommandEnabler& ce)
{
  ce.Enable((GetDocManager()->GetCurrentDoc() != 0)
            && ApxMail->IsMAPIAvailable());
}


void TOletestApp::CmFileSend ()
{
  // Check to see if a document exists
  //
  TDocument* currentDoc = GetDocManager()->GetCurrentDoc();

  if (currentDoc) {
    TAPointer<char> savedPath = new char[_MAX_PATH];
    TAPointer<char> docName = new char[_MAX_PATH];

    bool dirtyState = currentDoc->IsDirty();

    if (currentDoc->GetDocPath())
      strcpy(savedPath, currentDoc->GetDocPath());
    else
      strcpy(savedPath, "");

    if (currentDoc->GetTitle())
      strcpy(docName, currentDoc->GetTitle());
    else
      strcpy(docName, "Document");

    TFileName tempFile(TFileName::TempFile);

    currentDoc->SetDocPath(tempFile.Canonical().c_str());
    currentDoc->Commit(true);

    currentDoc->SetDocPath(savedPath);
    currentDoc->SetDirty(dirtyState);

    ApxMail->SendDocuments(GetMainWindow(), tempFile.Canonical().c_str(), docName, false);

    tempFile.Remove();
  }
}


//--------------------------------------------------------
// TOletestApp
// ~~~~~~~~~~~
//
// Menu Help About Oletest command
void TOletestApp::CmHelpAbout()
{
}


void TOletestApp::EvOwlDocument(TDocument& doc)
{
  if (doc.GetDocPath())
    SaveMenuChoice(doc.GetDocPath());
}


int32 TOletestApp::CmFileSelected(uint wp, int32)
{
  TAPointer<char> text = new char[_MAX_PATH];

  GetMenuText(wp, text, _MAX_PATH);
  TDocTemplate* tpl = GetDocManager()->MatchTemplate(text);
  if (tpl)
    GetDocManager()->CreateDoc(tpl, text);
  return 0;
}


int OwlMain(int , char* [])
{
  TOletestApp   app;
  return app.Run();
}
