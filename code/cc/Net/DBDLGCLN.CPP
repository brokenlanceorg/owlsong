//----------------------------------------------------------------------------
//  Project Db
//  
//  Copyright © 1996. All Rights Reserved.
//
//  SUBSYSTEM:    Db Application
//  FILE:         dbdlgcln.cpp
//  AUTHOR:       
//
//  OVERVIEW
//  ~~~~~~~~
//  Source file for implementation of TDbDlgClient (TDialog).
//
//----------------------------------------------------------------------------

#include <owl/pch.h>

#include<stdio.h>
#include"dbapp.h"
#include"dbdlgcln.h"


//
// Build a response table for all messages/commands handled
// by the application.
//
DEFINE_RESPONSE_TABLE1(TDbDlgClient, TDialog)
  EV_COMMAND(IDCANCEL, CmRead830),
//{{TDbDlgClientRSP_TBL_BEGIN}}
//{{TDbDlgClientRSP_TBL_END}}
END_RESPONSE_TABLE;


//--------------------------------------------------------
// TDbDlgClient
// ~~~~~~~~~~
// Construction/Destruction handling.
//
TDbDlgClient::TDbDlgClient(TDocument& doc, TWindow* parent)
:
  TView(doc), TDialog(parent, IDD_CLIENT)
{
int iAlive = 0;
char cFileName[] = "net12.def";

	TstatusWin = new TEdit(this, IDC_EDIT1);
	TstatusWin->SetText("Initializing...\r\n");
	TestNet = new BackproP(cFileName, iAlive);
} // end constructor

TDbDlgClient::~TDbDlgClient()
{
	delete TstatusWin;
	delete TestNet;
} // end destructor --------------------------------------------------------

//--------------------------------------------------------
// TDbDlgClient
// ~~~~~~~~~~
// Testing stuff
//
void 
TDbDlgClient::CmRead830()
{
} // end CmRead

bool TDbDlgClient::CanClose()
{
  return TWindow::CanClose() && Doc->CanClose();
}
/*
char cFileName[13];
int iAlive = 0;

	cout<<"Enter Definition File Name: \n";
	cin>>cFileName;
	cout.precision(18);
	cout<<"Is the network alive?\n";
	cin>>iAlive;
	TestNet = new BackproP(cFileName, iAlive);
	cout<<"Is the network mature?\n";
	cin>>iAlive;
	TestNet->SetMature(iAlive);
	TestNet->SetActivationFunc(Logistic);
	TestNet->SetActivationDerivative(DxLogistic);
	if(TestNet->Run())
		cout<<"Damn. Oh Well.";
	delete TestNet; */


