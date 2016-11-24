//**************************************************************
// File	    : Random.hpp
// Purpose  : Declarations of the RandoM class
// Author   : Brandon Benham
//**************************************************************
#ifndef __RANDOM_HPP
#define __RANDOM_HPP

#include<stdlib.h>
#include<time.h>
#include"DebugLogger.hpp"

//************************************************************************
// Class   : RandoM
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose : This class defines a RandoM object used to obtain random numbers
//************************************************************************
class RandoM
{
   protected:
      long double ldRandom_Long_Double;
      float fThe_Seed;
      float iLeft;
      float iRight;

   public:
      RandoM();
      RandoM(float iLft, float iRght);
      ~RandoM();
      virtual void CalcSeed();
      long int GetRandomInt(){RandomizeAll(); return (long int)ldRandom_Long_Double;}
      float GetRandomFloat(){RandomizeAll(); return (float)ldRandom_Long_Double;}
      double GetRandomDouble(){RandomizeAll(); return (double)ldRandom_Long_Double;}
      long double GetRandomLngDouble(){RandomizeAll(); return ldRandom_Long_Double;}
      float GetSeed(){return fThe_Seed;}
      float SetSeed(float fS);
      void SetEndPoints(float, float);
      void RandomizeAll();
		
}; // end RandoM class declarations

#endif
