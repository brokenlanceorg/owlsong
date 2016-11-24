//**************************************************************
// File	    : Random.cpp
// Purpose  : Implementations of the RandoM class
// Author   : Brandon Benham
// Notes    : General process: Construct->CalcSeed[->SetSeed]->GetRandom[]
//**************************************************************
#include "Random.hpp"
#include <iostream>

// Prototypes:********************************
long Get_Time_Secs();
long double AbsoluteRand( long double );

//************************************************************************
// Class    : RandoM
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  Default Constructor
// Purpose  :  This class defines a RandoM object used to obtain random numbers
//************************************************************************
RandoM::RandoM()
{
   ldRandom_Long_Double = 0;
   fThe_Seed = 0;
} // end default constructor

//************************************************************************
// Class    : RandoM
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  Constructor
// Purpose  :  This class defines a RandoM object used to obtain random numbers
//************************************************************************
RandoM::RandoM( float iLft, float iRght )
{
   iLeft = iLft;
   iRight = iRght;
   ldRandom_Long_Double = 0;
   fThe_Seed = 0;
} // end constructor

//************************************************************************
// Class    : RandoM
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function : Destructor
// Purpose  : This class defines a RandoM object used to obtain random numbers
//************************************************************************
RandoM::~RandoM()
{
} // end default destructor

//************************************************************************
// Class    : RandoM
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function : SetSeed
// Purpose  : Sets the current seed value, and then returns the old one
// Notes    : This is mainly functionality for the derived classes.
//************************************************************************
float RandoM::SetSeed( float fS )
{
   float fOld_Seed = fThe_Seed;
   fThe_Seed = fS;
   return fOld_Seed;
} // end SetSeed

//************************************************************************
// Class    : RandoM
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function : CalcSeed
// Purpose  : Calculates the current seed value; this function is virtual
//          :  so that an ancestor class may modify this procedure as it
//          :  is the case that this is one of the most important parts to
//          :  a random number generator.
//************************************************************************
void RandoM::CalcSeed()
{
   long lThe_Time = Get_Time_Secs();

   fThe_Seed = (float)lThe_Time;
   srand(lThe_Time);
} // end CalcSeed

//************************************************************************
// Class    : RandoM
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function : SetEndPoints
// Purpose  : Sets the interval for the random number; if the endpoints
//          : aren't set, we use the interval 0 to RAND_MAX defined in
//          :  stdlib.h
//************************************************************************
void RandoM::SetEndPoints( float iL, float iR )
{
   iLeft = iL;
   iRight = iR;
} // end SetEndPoints

//************************************************************************
// Class    : RandoM
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function : RandomizeAll
// Purpose  : Call this function to actually fill all of the random
//          :  member variables before using them.
//************************************************************************
void RandoM::RandomizeAll()
{
   float fLength = (float)AbsoluteRand(iRight - iLeft);
   int iThe_Random = rand();

   ldRandom_Long_Double = (long double)(iThe_Random) / RAND_MAX;
												  
   ldRandom_Long_Double *= fLength; // scale the random into the range
   ldRandom_Long_Double += iLeft;
} // end RandomizeAll

//********************************************************
// Function : Get_time_Secs
// Purpose  : Returns the current time in seconds passed
//          : since 00:00:00 hours 1970
//********************************************************
long Get_Time_Secs()
{
   time_t T_time;
   long _testint;
   T_time = time(NULL);
   _testint = (long int)T_time;
   return _testint;		
} // end Get_time_Secs

//***********************************************************************
// Function  : AbsoluteRand
// Purpose   : Returns the absolute value of a long double number
//           :  We have to write this func because C++ doesn't have one
//           :  defined for the long double type, only for int data type
//***********************************************************************
long double AbsoluteRand( long double nA )
{
   if(nA < 0)
      nA *= (-1);
   return nA;
   //return nA < 0 ? nA * -1 : nA;
} // end Absolute
