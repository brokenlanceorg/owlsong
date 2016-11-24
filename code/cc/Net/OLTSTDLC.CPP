//----------------------------------------------------------------------------
//  Project Oletest
//  
//  Copyright © 1996. All Rights Reserved.
//
//  SUBSYSTEM:    Oletest Application
//  FILE:         oltstdlc.cpp
//  AUTHOR:       
//
//  OVERVIEW
//  ~~~~~~~~
//  Source file for implementation of TOletestDlgClient (TDialog).
//
//----------------------------------------------------------------------------

#include <owl/pch.h>

#include "oltstapp.h"
#include "oltstdlc.h"


//{{TOletestDlgClient Implementation}}


//
// Build a response table for all messages/commands handled
// by the application.
//
DEFINE_RESPONSE_TABLE1(TOletestDlgClient, TDialog)
//{{TOletestDlgClientRSP_TBL_BEGIN}}
//{{TOletestDlgClientRSP_TBL_END}}
END_RESPONSE_TABLE;


//--------------------------------------------------------
// TOletestDlgClient
// ~~~~~~~~~~
// Construction/Destruction handling.
//
TOletestDlgClient::TOletestDlgClient(TDocument& doc, TWindow* parent)
:
  TView(doc), TDialog(parent, IDD_CLIENT)
{
  // INSERT>> Your constructor code here.

}


TOletestDlgClient::~TOletestDlgClient()
{
  // INSERT>> Your destructor code here.

}


bool TOletestDlgClient::CanClose()
{
  return TWindow::CanClose() && Doc->CanClose();
}
