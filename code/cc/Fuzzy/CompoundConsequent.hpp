//***********************************************************************************************
// File     : CompoundConsequent.hpp
// Purpose  : Defines our derived class CompoundConsequent, which incorporates the basic operations of
//          : the membership function class and overloades the EvaluateIt method.
// Author   : Brandon Benham 
// Date     : 1/20/00
//***********************************************************************************************

#ifndef __COMPCONSEQUENT_HPP
#define __COMPCONSEQUENT_HPP
                     
#include "MembershipFunc.hpp"
#include "Consequent.hpp"
#include <vector>

using namespace std;

//***********************************************************************************************
// Class    : CompoundconsequenT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : Declares the CompoundconsequenT class. This class performs the following:
//          : Contains an array of consequents, initially size 10.
//          : Maintains the correct left and right endpoints for the entire set.
//          : Derived from membershipFunction so that the "|" operation is easily accessible.
//          : Keeps track of the number of consequent sets.
//          : Overloades "+" to add consequent sets.
//          : Implements the logic for the Center Of Mass (COM) algorithm.
//          : Contains a variable that defines the granularity of the COM algorithm.
//          :
//***********************************************************************************************
class CompoundconsequenT : public MembershipFunctioN
{
   private:

   protected:

      // The pointer the the array of Consequents:
      vector< ConsequenT* > _consequents;

      // Granularity of COM:
      int iGranularity;

      // Left EndPoint:
      long double ldLeft_Endpoint;

      // Right EndPoint:
      long double ldRight_Endpoint;

      // Step Value for COM algorithm:
      long double ldStep_Value;

      // Variables for the COM alg:
      long double ldTop;
      long double ldBot;

      // The ol' Setup method:
      void Setup();

      // Method to resize the array: 
      void ResizeArray();

      // CenterOfMass implementation
      long double CenterOfMass();

      // Evaluate all of the consequents:
      long double EvaluateEm( long double );

   public:

      // Default constructor:
      CompoundconsequenT();

      // Real Constructor:
      CompoundconsequenT( ConsequenT* );

      // Destructor:
      virtual ~CompoundconsequenT();

      // Overloaded operators:
      CompoundconsequenT& operator + ( ConsequenT& );

      // Compute front end:
      CompoundconsequenT& operator >> ( long double& );

      // Accessor to the Granularity:
      inline void SetGranularity( int iG ) { iGranularity = iG; }

}; // end class CompoundconsequenT declaration

#ifdef __MYDEBUG__
   #define CCFPRINT( msg ) DebugloggeR CCFPRINT( msg, __FILE__, __LINE__ );
#else
   #define CCFPRINT( msg );
#endif
#endif

