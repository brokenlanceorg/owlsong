//***********************************************************************************************
// File		: FuzzyNeural.hpp
// Purpose	: This class is a neural network whose training algorithm has been
//          : overriden with a method that uses fuzzy logic to adjust the
//          : learning rate to quicken the training process.
//          :
// Author	: Brandon Benham 
// Date		: 6/03/00
//***********************************************************************************************
#ifndef __FUZZYNEURAL_HPP
#define __FUZZYNEURAL_HPP

#include "FuzzyBase.hpp"
#include "Backprop.hpp"
#include "FuzzyLearningRate.hpp"
//***********************************************************************************************
// Class	   : FuzzyneuraL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose	: This class is a neural network whose training algorithm has been
//          : overriden with a method that uses fuzzy logic to adjust the
//          : learning rate to quicken the training process.
//          :
//          :
//          :
//***********************************************************************************************
class FuzzyneuraL : public BackproP
{
	public:
   	FuzzyneuraL( char*, unsigned short, long = 100 );  // Real Constructor declaration
   	~FuzzyneuraL(); // Destructor declaration

      inline void SetInterval( long lI ) { lInterval = lI;}
      int GetNumberOfInputs() { return iNum_Inputs; }
      void SetLearningRateObject( LearningrateAdjusteR* );
      long double GetLastError() { return ldLast_Error; }

   protected:
      void Setup();
      int CheckProgress( long double );
      void TrainNet();

   private:
      long double ldLast_Error;
      long double ldDifference;
      long lInterval;
      FunctioN* FuncHelper;
      // The fuzy logic engine:
      LearningrateAdjusteR* Fuzzy_Learning_Rate;
   
}; // end FuzzyneuraL declaration

#endif

