//***************************************************************************** 
// File		:	Nfunc.hpp
// Purpose	:	This file contains the object oriented paradigm for the 
//				:  neural network parallel function interface class.
// Notes		:  This is a base class which has several outlier classes
//				:  necessary for the mathematical bookeeping.
// Author	:  Brandon Benham
//*****************************************************************************
#ifndef __NFUNC_HPP
#define __NFUNC_HPP

#include"backprop.hpp"
#include"interval.hpp"

//***************************************************************************** 
// Class		:	NeuralparallelfunctioN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose	:	This class furnishes an interface to a trained neural network
//				:  that will calculate the value of some arbitrary function
// 			:  at non-interpolating points in the function's domain.
//*****************************************************************************
class NeuralparallelfunctioN
{
	protected:
		BackproP** BPNtheNetworks;
		char** ppcNetwork_Names;
		IntervaL** ppItheIntervals;
		int iNumber_Of_Intervals;
		void ConstructArray(int);
      long double ldThe_Variable;

	public:
		NeuralparallelfunctioN(); // default constructor
		~NeuralparallelfunctioN(); // destructor
		NeuralparallelfunctioN(char*); // constructor
		NeuralparallelfunctioN(int, char**); // big constructor
		void Setup();
		int FindDomain(int, long double);
		long double EvaluateIt();
		inline void SetVariable(long double ld1) {ldThe_Variable = ld1;}
														 
}; // end NeuralparallelfunctioN



#endif
