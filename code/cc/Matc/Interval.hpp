//***************************************************************************** 
// File     :  Interval.hpp
// Purpose  :  Declares the interval class which will house necessary
//          :  instantiations of the DomaiN class.
// Notes    :  
//          :  
// Author   :  Brandon Benham
//*****************************************************************************
#ifndef __INTERVAL_HPP
#define __INTERVAL_HPP

#include"Domain.hpp"
//***************************************************************************** 
// Class    :  IntervaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  :  Declares the interval class used by the NPF (neuralparallelfunc)
//          :  class; it will contain instances of the DomaiN class in order
//          :  to keep track of the NPF's domain information.
//*****************************************************************************
class IntervaL
{
   protected:
      int iNumber_Of_Domains;
      DomaiN** ppDtheDomains;

   public:
      IntervaL(); // constructor
      ~IntervaL(); // destructor
      IntervaL(int); // real constructor
      void SetDomain(int, long double, long double);
      // Get_First_X:***************************
      long double GetFirstX(int iI) { if(iI < (iNumber_Of_Domains) )
         return ppDtheDomains[iI]->GetFirstX(); 
         else return ppDtheDomains[iNumber_Of_Domains-1]->GetFirstX();}
      // Get_First_Y:***************************
      long double GetFirstY(int iI) { if(iI < (iNumber_Of_Domains) )
         return ppDtheDomains[iI]->GetFirstY();
         else return ppDtheDomains[iNumber_Of_Domains-1]->GetFirstX();}
      // Get_Last_X:***************************
      long double GetLastX(int iI) { if(iI < (iNumber_Of_Domains) )
         return ppDtheDomains[iI]->GetLastX();
         else return ppDtheDomains[iNumber_Of_Domains-1]->GetFirstX();}
      // Get_Last_Y:***************************
      long double GetLastY(int iI) { if(iI < (iNumber_Of_Domains) )
         return ppDtheDomains[iI]->GetLastY();
         else return ppDtheDomains[iNumber_Of_Domains-1]->GetFirstX();}
      // Get_Midpoint_X:***************************
      long double GetMidPointX(int iI) { if(iI < (iNumber_Of_Domains) )
         return ppDtheDomains[iI]->GetMidPointX();
         else return ppDtheDomains[iNumber_Of_Domains-1]->GetFirstX();}
      // Get_Midpoint_Y:***************************
      long double GetMidPointY(int iI) { if(iI < (iNumber_Of_Domains) )
         return ppDtheDomains[iI]->GetMidPointY();
         else return ppDtheDomains[iNumber_Of_Domains-1]->GetFirstX();}
      inline int GetNumberOfDomains() {return iNumber_Of_Domains;}

}; // end class declaration IntervaL

#endif
