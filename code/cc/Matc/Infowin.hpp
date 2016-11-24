//**************************************************************************
//	File		: InfoWin.hpp
// Purpose	: Declares the InformationwiN class
//**************************************************************************
#ifndef __INFOWIN_HPP
#define __INFOWIN_HPP

#include"screen.hpp"
//*************************************************************************
// Class		: InformationwiN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//	Purpose	: This class functions as an information giver in the
//				: MDI context.
//*************************************************************************
class InformationwiN : protected ScreeN
{
	protected:
   	void Setup(int = 0);
      int iNumber_Of_Lines;
      int iHeight;
      char** ppcTextStrings;
      char* pcFont_Name;

   public:
   	InformationwiN();
   	~InformationwiN();
   	InformationwiN(int);
		int SetTextInformation(int, char*);
      void SetFontName(char*);
      void SetNumberOfLines(int);
		// Windows funcs:
		void Paint(TDC& DCtheDc, bool, TRect&);
		void EvSize(UINT, TSize&);
      // Misc:
      void Tester();
      inline void SetTextSpacing(int iI) {iHeight = iI;}
      
	DECLARE_RESPONSE_TABLE(InformationwiN);
}; // end InformationwiN
      
#endif