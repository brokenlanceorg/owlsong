//**************************************************************************
//	File		: Space.hpp
// Purpose	: Declares the SpacE class
//**************************************************************************
#ifndef __SPACE_HPP
#define __SPACE_HPP

#include"screen.hpp"
#include"mathmatc.hpp"
#include"domain.hpp"
#include"onevar.hpp"      

//**************************************************************************
//	Class		: SpacE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose	: Declares the SpacE class, which is to be inherited by
//				: the other windows graphics classes.
//**************************************************************************
class SpacE : public ScreeN
{
	protected:
		DomaiN* DtheDomains;
		static OnevariablE* Fct1TheFunction;
      static VectoR* vTheVars;

	public:
		SpacE();
		~SpacE();
		SpacE(int, int);
		SpacE(int, int, long double, long double);
		SpacE(int, int, long double, long double, long double, long double);
		void Setup();
		// Windows funcs:
		void Paint(TDC& DCtheDc, bool, TRect&);
		void EvSize(UINT, TSize&);
		// Misc. Public:
		void SetIntervalX(long double ld1, long double ld2) 
			{DtheDomains->SetXInterval(ld1, ld2);}
		void SetIntervalY(long double ld1, long double ld2)
			{DtheDomains->SetYInterval(ld1, ld2);}
		long double GetXLength() {return DtheDomains->GetLengthX();}
		long double GetYLength() {return DtheDomains->GetLengthY();}
		long double GetXFirst() {return DtheDomains->GetFirstX();}
		long double GetYFirst() {return DtheDomains->GetFirstY();}
		void SetVariable(int, long double);
		long double EvaluateTheFunction();
		static long double EvaluateIt();
		void GraphIt(TDC& DCtheDevice, bool, TRect&);
															 
	DECLARE_RESPONSE_TABLE(SpacE);							 
}; // end class declaration

#endif