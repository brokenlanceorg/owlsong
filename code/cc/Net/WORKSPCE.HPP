//*************************************************************************
// File		: Workspce.hpp
//	Purpose	: Declares the TWorkspacE class
//*************************************************************************
#ifndef __WORKSPCE_HPP
#define __WORKSPCE_HPP

#include <owl\owlpch.h>
#include <owl\applicat.h>
#include <owl\mdi.h>
#include <string.h>
#include "infowin.hpp"
#include "screen.hpp"
#include "demobase.h"
//*************************************************************************
// Class		: TMDIWorkspacE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//	Purpose	: Defines the 'workspace' in which we will construct our
//				: graphical views of the mathematical functions.
//*************************************************************************
class TMDIWorkspacE : public TMDIClient 
{
	public:
		TMDIWorkspacE() : TMDIClient() { Attr.Style |= WS_TABSTOP; }
		~TMDIWorkspacE();
		TMDIWorkspacE(int);
		void Paint(TDC&, bool, TRect&);
	
	protected:
		void SetupWindow();
		// Commands:
		void CmDispatchViews();
		// Events:
		void EvTimer(UINT);
		void EvSize(UINT, TSize&);
		void EvDestroy();
		// Data properties:
		int iNumber_Dimensions;
		int iNumber_Windows;
		TMDIChild** TMDItheChildren;
		TMDIChild* TMDItheStatusWindow;
	private:
		TPoint      WindowSize;
		TBitmap*    ScratchBitmapBMP;
		TBitmap*    StretchedBackBMP;
		TBitmap*    BackgroundBMP;

	DECLARE_RESPONSE_TABLE(TMDIWorkspacE);
};

#endif