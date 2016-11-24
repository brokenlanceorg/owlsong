//***********************************************************************************************
// File		: FuzzyNeural.cpp
// Purpose	: This class is a neural network whose training algorithm has been
//          : overriden with a method that uses fuzzy logic to adjust the
//          : learning rate to quicken the training process.
//          :
// Author	: Brandon Benham 
// Date		: 6/03/00
//***********************************************************************************************

#include"FuzzyNeural.hpp"          

//***********************************************************************************************
// Class	   : FuzzyneuraL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Constructor
// Purpose	: Performs the construction actions.
// Notes        : lInt is just the number of epochs to check the progress for an adjustment.
//***********************************************************************************************
FuzzyneuraL::FuzzyneuraL( char* pcFile, unsigned short usAlive, long lInt ) : BackproP( pcFile, usAlive )
{
   Setup();
   lInterval = lInt;
   FuncHelper = new FunctioN( _OVERFLOW );
   Fuzzy_Learning_Rate = new LearningrateAdjusteR( 0, 1.65898, 0.1, 2.73971 );
} // end FuzzyneuraL constructor

//***********************************************************************************************
// Class	   : FuzzyneuraL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Destructor
// Purpose	: Performs the destruction actions.
//***********************************************************************************************
FuzzyneuraL::~FuzzyneuraL()
{
   if( FuncHelper != 0 )
   {
      delete FuncHelper;
   }
   if( Fuzzy_Learning_Rate != 0 )
   {
      delete Fuzzy_Learning_Rate;
   }
} // end FuzzyneuraL destructor

//***********************************************************************************************
// Class	   : FuzzyneuraL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Setup
// Purpose	: Performs the basic setup actions.
//***********************************************************************************************
void FuzzyneuraL::Setup()
{
   ldLast_Error        = 0;
   ldDifference        = 0;
   lInterval           = 0;
   FuncHelper          = 0;
   Fuzzy_Learning_Rate = 0;
} // end Setup

//***********************************************************************************************
// Class	   : FuzzyneuraL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: TrainNet
// Purpose	: This method overrides the method in the BackproP class.
//	Notes		:  At this point, we assume all matrices and vectors contain
//				:  the necessary data to begin the training process.
//          : We pass the current error, the current epoch, and the 
//          :  difference from the last error to the CheckProgress method.
//***********************************************************************************************
void FuzzyneuraL::TrainNet()
{
FPRINT( "Error is: " );
float fCycResult = 0;

   iCurrent_Instance = 0; // on first training instance
//   FPRINT << "fTolerance: " << (long double)fTolerance;

   while( 1 )
   {
      fCycResult = CycleThruNet(); // cycle all test patterns thru net one time
      ulEpoch += 1; // One time thru net for all patterns is one epoch
      if( ulEpoch % 1000 == 0 )
      {
         FPRINT << (long double)fCycResult;
      }
      if( CheckProgress( fCycResult ) )
      {
         usIsMature = 0;
         FPRINT << "Unable to train this network" << _learningRate;
         return; // we may need to throw an error here so that the run method will know
                 // or the client can check the usIsMature variable at the end of training.
      } // end if
      if(fCycResult <= fTolerance)
      {
         usIsMature = 1; // network is trained
         break;
      } // end if
   } // end while loop

} // end TrainNet

//***********************************************************************************************
// Class	   : FuzzyneuraL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: CheckProgress
// Purpose	: This method checks the training process via a fuzzy logic inference
//   		: based on this evaluation, we will either modify the learning rate
//		: or we will return an error stating to quit;
//          :
//          :
//***********************************************************************************************
int FuzzyneuraL::CheckProgress( long double ldError )
{
int iRet = 0;

   // First, we check to see if we even need to evaluate things just yet:
   if( (ulEpoch % lInterval) != 0 )
   {
      return iRet;
   }

   // We can proceed because this is the lInterval^th epoch
//   ldDifference = FuncHelper->Absolute( ldLast_Error - ldError ); // get absolute!!!!!

//   FPRINT( "CheckProgress: " );
//   FPRINT << "Difference is: " << ldDifference;
//   FPRINT << "Epoch is: " << (long)ulEpoch;

   // Here, we will pass the Error, Epoch, and Difference to the fuzzy logic
   //    engine to get the learning rate.
   /*
   fLearning_Rate = Fuzzy_Learning_Rate->GetLearningRate( (long double)(ulEpoch),
                                                          ldError, ldDifference);
                                                          */
   _learningRate = Fuzzy_Learning_Rate->GetLearningRate( ldError );
//   ldLast_Error = ldError;
   if( _learningRate < 0 )
   {
      _learningRate *= -1;
   }

//   FPRINT << "Learning Rate:" << (long double)(fLearning_Rate);

   // We need a mechanism to check for cancellation to go here....
   /*
   long double ldQuit = Fuzzy_Learning_Rate->GetQuitValue();
   FPRINT << "Quit: " << ldQuit;
   if( ulEpoch >= 30000 ) // This may be more appropriate....
   {
      iRet = 1;
   }
   */

   return iRet;
} // end CheckProgress

void FuzzyneuraL::SetLearningRateObject( LearningrateAdjusteR* r )
{
   if( Fuzzy_Learning_Rate != 0 )
   {
      delete Fuzzy_Learning_Rate;
   }
   Fuzzy_Learning_Rate = r;
}

