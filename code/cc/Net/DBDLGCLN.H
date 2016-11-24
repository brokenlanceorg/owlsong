//----------------------------------------------------------------------------
//  Project Db
//  
//  Copyright © 1996. All Rights Reserved.
//
//  SUBSYSTEM:    Db Application
//  FILE:         dbdlgcln.h
//  AUTHOR:       
//
//  OVERVIEW
//  ~~~~~~~~
//  Class definition for TDbDlgClient (TDialog).
//
//----------------------------------------------------------------------------
#if !defined(dbdlgcln_h)              // Sentry, use file only if it's not already included.
#define dbdlgcln_h

#include <owl/docview.h>
#include <owl/edit.h>

#include "dbapp.rh"                  // Definition of all resources.
#include "backprop.hpp"
#include "neural.hpp"

//{{TDialog = TDbDlgClient}}
class TDbDlgClient : public TDialog, public TView {
  public:
//  TDbDlgClient(TWindow* parent, TResId resId = IDD_CLIENT, TModule* module = 0);  //{{DIALOG_ASSOCIATION}}

    TDbDlgClient(TDocument& doc, TWindow* parent = 0);
    virtual ~TDbDlgClient();

    static const char far* StaticName() {return "Dialog View";}  // put in resource
   
    // Inherited virtuals from TView
    //
    const char far* GetViewName() {return StaticName();}
    TWindow* GetWindow() {return(TWindow*)this;}
    bool     SetDocTitle(const char far* docname, int index)
             { return TWindow::SetDocTitle(docname, index); }
  private:
    // Event handlers
    //
    bool     VnIsWindow(HWND hWnd)
             { return ((HWindow == hWnd) || IsChild(hWnd)); }

//{{TDbDlgClientVIRTUAL_BEGIN}}
  public:
    virtual bool CanClose();
	void CmRead830();
//{{TDbDlgClientVIRTUAL_END}}

//{{TDbDlgClientRSP_TBL_BEGIN}}
  protected:
	BackproP* TestNet;
  	TEdit* TstatusWin;
//{{TDbDlgClientRSP_TBL_END}}
DECLARE_RESPONSE_TABLE(TDbDlgClient);
};    //{{TDbDlgClient}}


#endif  // dbdlgcln_h sentry.
