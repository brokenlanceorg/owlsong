//***************************************************************************** 
// File     :  Interval.cpp
// Purpose  :  Defines the interval class which will house necessary
//          :  instantiations of the DomaiN class.
// Notes    :  
//          :  
// Author   :  Brandon Benham
//*****************************************************************************
#include"Interval.hpp"

//***************************************************************************** 
// Class    :  IntervaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  Default Constructor
// Purpose  :  Does basic Setup stuff.
//          :  
//*****************************************************************************
IntervaL::IntervaL()
{
   iNumber_Of_Domains = 0;
} // end Default Constructor

//***************************************************************************** 
// Class    :  IntervaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  Destructor
// Purpose  :  Does basic shutdown stuff.
//          :  
//*****************************************************************************
IntervaL::~IntervaL()
{
int iLoop1;

   /*for(iLoop1=0;iLoop1<iNumber_Of_Domains;iLoop1++)
   {
      delete ppDtheDomains[iLoop1];
   } // end for */
   delete[] ppDtheDomains;
} // end Destructor

//***************************************************************************** 
// Class    :  IntervaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  Constructor
// Purpose  :  Instantiates the number of Domains that we need.
//          :  
//*****************************************************************************
IntervaL::IntervaL(int iNum)
{
int iLoop;

   iNumber_Of_Domains = iNum;
   ppDtheDomains = new DomaiN*[iNumber_Of_Domains];
   for(iLoop=0;iLoop<iNumber_Of_Domains;iLoop++)
   {
      ppDtheDomains[iLoop] = new DomaiN(); // construct 1-dimensional domains
   } // end for loop

} // end Constructor

//***************************************************************************** 
// Class    :  IntervaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  SetDomain
// Purpose  :  Sets the respective domain in the DomaiN instance.
//          :  
//*****************************************************************************
void IntervaL::SetDomain(int iNum, long double ldFirst, long double ldSec)
{
   if(iNum > iNumber_Of_Domains) {
      //cout<<"Attempted to access non-existing domains\n";
      return; }
   ppDtheDomains[iNum]->SetXInterval(ldFirst, ldSec);
} // end SetDomain

