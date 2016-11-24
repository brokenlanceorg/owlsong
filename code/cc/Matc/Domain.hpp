//**************************************************************************
// File     : Domain.hpp
// Purpose  : Declares the DomaiN class
//**************************************************************************
#ifndef __DOMAIN_HPP
#define __DOMAIN_HPP

#include"Mathmatc.hpp"

//**************************************************************************
// Class    : DomaiN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : Declares the DomaiN class, which encapsulates domain
//          : information pertinent to mathematical operations.
//**************************************************************************
class DomaiN
{
   protected:
      MatriX* mTheDomains;    // the matrix to store our domain info
      long double ldLengthX;  // contains the length in the x direction
      long double ldLengthY;  // contains the length in the y direction
      long double ldMidPointX;// holds the midpoint for the x direction
      long double ldMidPointY;// holds the midpoint for the y direction
      void Setup();
      int iNum_Axes;          // a quick reference to number of variables
      void CalcInterval();
      
   public:
      DomaiN();
      ~DomaiN();
      DomaiN(int);
      DomaiN(long double, long double);
      DomaiN(long double, long double, long double, long double);
      inline long double GetLengthX() {return ldLengthX;}
      inline long double GetLengthY() {return ldLengthY;}
      inline long double GetMidPointX() {return ldMidPointX;}
      inline long double GetMidPointY() {return ldMidPointY;}
      inline long double GetFirstX() {return mTheDomains->pCol[0][0];}
      inline long double GetFirstY() {return mTheDomains->pCol[1][0];}
      inline long double GetLastX() {return mTheDomains->pCol[0][1];}
      inline long double GetLastY() {return mTheDomains->pCol[1][1];}
      void SetXInterval(long double, long double);
      void SetYInterval(long double, long double);
      void SetSpecificInterval(int, long double, long double);
}; // end Domain declaration

#endif
