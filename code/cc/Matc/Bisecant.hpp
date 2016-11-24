//**********************************************************************
// File		:  Bisecant.hpp
// Author	:  Brandon Benham
// Purpose	:  We also declare our Bisecant (the hybrid algorithm)
//				:	in this class. Due to the similarities between Newton 
//				:  and Bisecant, we derive this class from the NewtoN class.
//				:  
// Notes		:  This method should be used when the function is well-defined
//				:  and a closed interval containing the root is known.
//**********************************************************************
#ifndef __BISECANT_HPP
#define __BISECANT_HPP

#include"newton.hpp"

//************************************************************************
// Class		:	BisecanT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose	:	Calculates the zero of a known function by using a hybrid
//				:	algorithm which combines both the secant and bisection
//				:  algorithms.
//************************************************************************
class BisecanT : protected NewtoN
{
	protected:
		FilesourcE* FsecantOutput;
		long double ldLeft_Endpoint;				// a; [a, b] contains root
		long double ldRight_Endpoint;				// b; [a, b] contains root
		long double ldMidpoint;						// (a + b) / 2
		long double ldThis_Guess;					// p_n
		long double ldNext_Guess;					// p_n+1
		long double ldThis_Eval;					// f(p_n)
		long double ldLast_Eval;					// f(p_n-1)
		int iIncreasing;	// classification of function
		// Methods:
		void Setup(int = 1);			// does normal setup functions
		long double SecantMethod(int);// evaluates the secant formula
		long double BisecantIt(int);	// the method that implements algorithm
		void BisectIt();		// does the bisect routine
		void SendDataToFile(int);		// output function

	public:
		BisecanT(); // default constructor
		~BisecanT(); // destructor
		BisecanT(int, int = -1, long double = 0); // Constructor, with tolerance
		// Constructor, same as above except we have the interval
		BisecanT(long double, long double, int, int = -1, long double = 0);
		long double SolveIt(long double = 0);	// front-end to method
		// Public functions:
      void CheckEquation();
		inline void SetMaxIterations(long lM) {NewtoN::SetMaxIterations(lM);}
		inline void SetTolerance(long double ldT) {NewtoN::SetTolerance(ldT);}
		inline void SetEquation(int iW) {NewtoN::SetEquation(iW);}
		inline void SetDerivative(int iD) {NewtoN::SetDerivative(iD);}
		inline void SetEquationParams(long double ld1, long double ld2=0,
			long double ld3=0) {NewtoN::SetEquationParams(ld1, ld2, ld3);}
		inline void SetDerivativeParams(long double ld1, long double ld2=0,
			long double ld3=0) {NewtoN::SetDerivativeParams(ld1, ld2, ld3);}
      void SetInterval(long double, long double);
		inline long GetIterations() {return NewtoN::GetIterations();}
		inline long GetTotalIterations() {return NewtoN::GetTotalIterations();}
		inline int GetStatus() {return NewtoN::GetStatus();}
		inline void SetLogging(unsigned short usOn, int iErr = 0) {
			NewtoN::SetLogging(usOn, iErr);} // end SetLogging
		void SendToFile(char*);  // interface to the output file
		void SetFileName(char*);
										  
}; // end BisecanT declaration
		
#endif