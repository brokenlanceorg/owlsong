//**************************************************************************
//	File		: Screen.hpp
// Purpose	: Declares the ScreeN class
//**************************************************************************
#ifndef __SCREEN_HPP
#define __SCREEN_HPP

#include<math.h>
#include<owl\owlpch.h>
#include<owl\dc.h>
#include<owl\framewin.h>
#include<owl\applicat.h>
#include<owl\dialog.h>
#include<owl\static.h>
//#include"c:\bc45\graph\win\ptable.h"
#include"demobase.h"

//**************************************************************************
//	Class		: ScreeN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose	: Declares the ScreeN class, which is to be inherited by
//				: the other windows graphics classes.
//**************************************************************************
class ScreeN : public TBaseDemoWindow
{
	protected:
		LOGPALETTE* PLogicalPalette;
		TPaintDC*	DCpainter;
		TBrush*		DCtheBrush;
		TPalette*	DCthePalette;
		TStatic*    StaticControl;
						  
		int iNum_Dimensions;
		int iTotal_Dimensions;
		int iMaxX;
		int iMaxY;
		void SetupPalette();
		void DoPalette();
		void Setup();
		void D2Axes(TDC&, TColor = TColor::LtBlue);
		void D3Axes(TColor = TColor::LtBlue);

	public:
		ScreeN();
		~ScreeN();
		ScreeN(int, int);
		void Paint(TDC& DCtheDc, bool, TRect&);
		void EvSize(UINT, TSize&);

	DECLARE_RESPONSE_TABLE(ScreeN);							 
}; // end class declaration


#endif