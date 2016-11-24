//----------------------------------------------------------------------------
//  Project Db
//  
//  Copyright © 1996. All Rights Reserved.
//
//  SUBSYSTEM:    Db Application
//  FILE:         dbapp.h
//  AUTHOR:       
//
//  OVERVIEW
//  ~~~~~~~~
//  Class definition for TDbApp (TApplication).
//
//----------------------------------------------------------------------------
#if !defined(dbapp_h)              // Sentry, use file only if it's not already included.
#define dbapp_h

#include <owl/controlb.h>
#include <owl/docking.h>


#include "dbapp.rh"            // Definition of all resources.

//
// FrameWindow must be derived to override Paint for Preview and Print.
//
//{{TDecoratedFrame = TSDIDecFrame}}
class TSDIDecFrame : public TDecoratedFrame {
  public:
    TSDIDecFrame(TWindow* parent, const char far* title, TWindow* clientWnd, bool trackMenuSelection = false, TModule* module = 0);
    ~TSDIDecFrame();
};    //{{TSDIDecFrame}}

//{{TApplication = TDbApp}}
class TDbApp : public TApplication {
  private:

  public:
    TDbApp();
    virtual ~TDbApp();

    void CreateGadgets(TDockableControlBar* cb, bool server = false);
    THarbor*        ApxHarbor;

//{{TDbAppVIRTUAL_BEGIN}}
  public:
    virtual void InitMainWindow();
    virtual void InitInstance();
//{{TDbAppVIRTUAL_END}}

//{{TDbAppRSP_TBL_BEGIN}}
  protected:
    void EvNewView(TView& view);
    void EvCloseView(TView& view);
    void CmHelpAbout();
//{{TDbAppRSP_TBL_END}}
DECLARE_RESPONSE_TABLE(TDbApp);
};    //{{TDbApp}}


#endif  // dbapp_h sentry.
