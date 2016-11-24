//***********************************************************************************************
// File     : MembershipFunc.hpp
// Purpose  : Defines our derived class MembershipFunc, which incorporates the operations of the
//          : OnevariablE class and the DomaiN class.
// Author   : Brandon Benham 
// Date     : 1/6/00
//***********************************************************************************************

#include"Onevar.hpp"
#include"Domain.hpp"
#include"DebugLogger.hpp"          

#ifndef __MEMBERSHIP_HPP
#define __MEMBERSHIP_HPP

//***********************************************************************************************
// Class : MembershipFunctioN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : Declares the MembershipFunctioN class.
//***********************************************************************************************
class MembershipFunctioN : public OnevariablE
{
   private:
           OnevariablE* _theInverse;

   protected:

      // The Domain object:
      DomaiN* Dthe_Domain;

      // For the Sigmoid and Linear cases:
      bool bIs_Sigmoid;
      bool bIs_Increasing;

      // The ol' Setup method:
      void Setup();

   public:

      // Default constructor:
      MembershipFunctioN();

      // Constructor for Bell-shaped curve:
      MembershipFunctioN( long double, long double );

      // Constructor for Sigmoid-shaped curve:
      MembershipFunctioN( long double, long double, long double, bool );

      // Constructor for Linear curve:
      MembershipFunctioN( long double, long double, long double, long double );

      // Destructor:
      virtual ~MembershipFunctioN();

      // To test increasing:
      bool IsIncreasing() {return bIs_Increasing;}

      // To get domain values:
      long double GetLeftEndpoint() {return Dthe_Domain->GetFirstX();}
      long double GetRightEndpoint() {return Dthe_Domain->GetLastX();}
      long double GetMidPoint() {return Dthe_Domain->GetMidPointX();}

      // To perform the operations:
      // We may want to make these virtual and derive this class....
      long double PerformAND( long double, long double );
      long double PerformOR( long double, long double );
      
      // The Evaluate method:
      // Make sure that this is virtual in OnevariablE!!!
      virtual long double EvaluateIt( long double );
      virtual long double InverseIt( long double );

}; // end class MembershipFunctioN declaration

#ifdef __MYDEBUG__
   #define MemFPRINT( msg ) DebugloggeR MemFPRINT( msg, __FILE__, __LINE__ );
#else
   #define MemFPRINT( msg );
#endif     

#endif
