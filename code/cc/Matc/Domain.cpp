//**************************************************************************
// File     : Domain.cpp
// Purpose  : Declares the DomaiN class
//**************************************************************************
#include"Domain.hpp"

//**************************************************************************
// Class    : DomaiN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member   : Default Constructor
// Purpose  : Declares the DomaiN class, which encapsulates domain
//          : information pertinent to mathematical operations.
//**************************************************************************
DomaiN::DomaiN()
{
   mTheDomains = new MatriX(1, 2);
   iNum_Axes = 0;
} // end default constructor

//**************************************************************************
// Class    : DomaiN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member   : Constructor
// Purpose  : Declares the DomaiN class, which encapsulates domain
//          : information pertinent to mathematical operations.
//**************************************************************************
DomaiN::DomaiN(int iNum_Dims)
{
   mTheDomains = new MatriX(iNum_Dims, 2);
   iNum_Axes = iNum_Dims;
   Setup();
} // end constructor

//**************************************************************************
// Class    : DomaiN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member   : Destructor
//**************************************************************************
DomaiN::~DomaiN()
{
   if( mTheDomains != 0 )
   {
      delete mTheDomains;
      mTheDomains = 0;
   } // end if
} // end destructor

//**************************************************************************
// Class    : DomaiN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member   : Constructor
// Purpose  : Constructs a domain for the x-axis only
//**************************************************************************
DomaiN::DomaiN(long double ldArg1, long double ldArg2)
{
   mTheDomains = new MatriX(1, 2);
   mTheDomains->pCol[0][0] = ldArg1;
   mTheDomains->pCol[0][1] = ldArg2;
   iNum_Axes = 1;
   Setup();
} // end default constructor

//**************************************************************************
// Class    : DomaiN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member   : Constructor
// Purpose  : Constructs a domain for the x-axis and y-axis
//**************************************************************************
DomaiN::DomaiN(long double ldArg1, long double ldArg2, long double ldArg3, 
   long double ldArg4)
{
   mTheDomains = new MatriX(2, 2);
   mTheDomains->pCol[0][0] = ldArg1;
   mTheDomains->pCol[0][1] = ldArg2;
   mTheDomains->pCol[1][0] = ldArg3;
   mTheDomains->pCol[1][1] = ldArg4;
   iNum_Axes = 2;
   Setup();
} // end default constructor 

//**************************************************************************
// Class    : DomaiN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member   : Setup
// Purpose  : Does basic setup
//**************************************************************************
void DomaiN::Setup()
{
   CalcInterval();
} // end setup

//**************************************************************************
// Class    : DomaiN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member   : CalcInterval
// Purpose  : Calculates the length of the intervals
//**************************************************************************
void DomaiN::CalcInterval()
{
   switch(iNum_Axes)
   {
      case 0 :
      break;

      case 1 :
         ldLengthX = mTheDomains->pCol[0][1] - mTheDomains->pCol[0][0];
         if(ldLengthX < 0)
            ldLengthX *= -1;
         ldMidPointX = mTheDomains->pCol[0][0] + mTheDomains->pCol[0][1];
         ldMidPointX *= .5;
      break;

      case 2 :
         ldLengthX = mTheDomains->pCol[0][1] - mTheDomains->pCol[0][0];
         if(ldLengthX < 0)
            ldLengthX *= -1;
         ldLengthY = mTheDomains->pCol[1][1] - mTheDomains->pCol[1][0];
         if(ldLengthY < 0)
            ldLengthY *= -1;
         ldMidPointX = mTheDomains->pCol[0][0] + mTheDomains->pCol[0][1];
         ldMidPointX *= .5;
         ldMidPointY = mTheDomains->pCol[1][0] + mTheDomains->pCol[1][1];
         ldMidPointY *= .5;
      break;                

      default :
      break;
   } // end switch
} // end CalcInterval

//**************************************************************************
// Class    : DomaiN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member   : SetXInterval
// Purpose  : Sets the x-axis variables.
//**************************************************************************
void DomaiN::SetXInterval(long double ldArg1, long double ldArg2)
{
   mTheDomains->pCol[0][0] = ldArg1;
   mTheDomains->pCol[0][1] = ldArg2;
   iNum_Axes = 1;
   CalcInterval();
} // end SetXInterval

//**************************************************************************
// Class    : DomaiN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member   : SetYInterval
// Purpose  : Sets the y-axis variables.
//**************************************************************************
void DomaiN::SetYInterval(long double ldVal1, long double ldVal2)
{
   mTheDomains->pCol[1][0] = ldVal1;
   mTheDomains->pCol[1][1] = ldVal2;
   iNum_Axes = 2;
   CalcInterval();
} // end SetYInterval

//**************************************************************************
// Class    : DomaiN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member   : SetSpecificXInterval
// Purpose  : Sets the axis variables.
//**************************************************************************
void DomaiN::SetSpecificInterval(int iWhich, long double ldArg1, 
   long double ldArg2)
{
   if(iWhich > iNum_Axes)
      return;
   mTheDomains->pCol[iWhich][0] = ldArg1;
   mTheDomains->pCol[iWhich][1] = ldArg2;
   iNum_Axes = 1;
   CalcInterval();
} // end SetSpecificXInterval

