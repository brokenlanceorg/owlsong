//***********************************************************************************************
// File     : Consequent.hpp
// Purpose  : Defines our derived class Consequent, which incorporates the basic operations of
//          : the membership function class and overloades the EvaluateIt method.
// Author   : Brandon Benham 
// Date     : 1/20/00
//***********************************************************************************************

#include"FuzzySet.hpp"

#ifndef __CONSEQUENT_HPP
#define __CONSEQUENT_HPP
                     
//***********************************************************************************************
// Class : ConsequenT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : Declares the ConsequenT class. This class:
//          : wraps a FuzzyseT or HedgE derivation.
//          : scales a truth value based on antecedent.
//          : performs method "inverse" on itself when it is the only consequent.
//          : provides accessor methods to callers.
//          :
//***********************************************************************************************
class ConsequenT
{
   private:

   protected:

      // The pointer the the FuzzySet:
      FuzzyseT* FzsThe_Set;

      // A holder for the variable of the transformation:
      long double ldScale_Variable;

      // The ol' Setup method:
      void Setup();

   public:

      // Default constructor:
      ConsequenT();

      // Real Constructor:
      ConsequenT( FuzzyseT* );

      // Destructor:
      virtual ~ConsequenT();

      // Overloaded operators:
      ConsequenT& operator << ( FuzzyseT& );

      // (set scale var) Input:
      ConsequenT& operator << ( const long double& );

      // (Inverse) Output:
      ConsequenT& operator >> ( long double& );

      // The EvaluateIt method:
      long double EvaluateIt( long double );

      // Accessor methods:
      // We are assuming that the caller has correctly set the FuzzySet pointer member!!
      inline long double GetLeftEndPoint()   { return FzsThe_Set->GetLeftEndpoint(); }
      inline long double GetRightEndPoint()  { return FzsThe_Set->GetRightEndpoint(); }
      inline long double GetMidPoint()       { return FzsThe_Set->GetMidPoint(); }

}; // end class ConsequenT declaration
//#define __MYDEBUG__
#ifdef __MYDEBUG__
   #define ConFPRINT( msg ) DebugloggeR ConFPRINT( msg, __FILE__, __LINE__ );
#else
   #define ConFPRINT( msg );
#endif
                
#endif
