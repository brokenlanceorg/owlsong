//***********************************************************************************************
// File     : FuzzySet.hpp
// Purpose  : Defines our base class FuzzyseT, which incorporates the basic operations of
//          : fuzzy sets including virtual overloaded operators.
// Author   : Brandon Benham 
// Date     : 1/4/00
//***********************************************************************************************

#include"MembershipFunc.hpp"
#include"DebugLogger.hpp"

#ifndef __FUZZYSET_HPP
#define __FUZZYSET_HPP
//***********************************************************************************************
// Class : FuzzyseT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : Declares the FuzzyseT class.
//***********************************************************************************************
class FuzzyseT
{
   private:

   protected:

      // The Set Membership Function:
      MembershipFunctioN* MemThe_Function;

      // A holder for the current truth value:
      long double ldThe_Truth_Value;
      long double ldThe_AND_Value;
      long double ldThe_OR_Value;

      // To keep track of which variable is currently valid:
      int iWhich_Variable;

      // The overloadable Eval method:
      virtual long double EvaluateIt( long double );

      bool _isIncreasing;

      // Alpha-cut threshold:
      long double ldAlpha;
      long double ldAlpha_Value;
      bool bHas_Alpha;

      // Omega-cut threshold:
      long double ldOmega;
      long double ldOmega_Value;
      bool bHas_Omega;

      // The ol' Setup method:
      void Setup();


   public:

      // Default constructor: use equation zero
      FuzzyseT();

      // Constructor: Bell curve set: 
      // For now, we'll just use Gaussian distributions
      FuzzyseT( long double, long double, bool = true );
      
      // Constructor: Sigmoid curve set:
      FuzzyseT( long double, long double, long double, bool = false );

      // Constructor: Linear set
      FuzzyseT( long double, long double, long double, long double );

      // Destructor:
      virtual ~FuzzyseT();

      // Overloaded operators:
      FuzzyseT& operator & ( const FuzzyseT& );
      FuzzyseT& operator | ( const FuzzyseT& );
      virtual FuzzyseT& operator << ( const long double& );
      FuzzyseT& operator >> ( long double& );

      // Inline cut-setters:
      inline void SetAlphaCut( long double ldA1, long double ldA2 ) {
         ldAlpha = ldA1, ldAlpha_Value = ldA2; bHas_Alpha = true; }

      inline void SetOmegaCut( long double ldO1, long double ldO2 ) {
         ldOmega = ldO1, ldOmega_Value = ldO2; bHas_Omega = true; }

      inline void SetTruthValue( long double ldT1 ) {ldThe_Truth_Value = ldT1;
         iWhich_Variable = 0;}
         
      inline void SetANDValue( long double ldA1 ) {ldThe_AND_Value = ldA1;
         iWhich_Variable = 1;}
         
      inline void SetORValue( long double ld1 ) {ldThe_OR_Value = ld1;
         iWhich_Variable = 2;}
         
      // For obtaining the result variable:
      long double GetResultVariable() const;

      // To inverse the Membership function:
      long double InverseIt( long double ld1 );

      // To get domain values:           
      virtual inline long double GetLeftEndpoint() {return MemThe_Function->GetLeftEndpoint();}
      virtual inline long double GetRightEndpoint() {return MemThe_Function->GetRightEndpoint();}
      virtual inline long double GetMidPoint() {return MemThe_Function->GetMidPoint();}
}; // end FuzzyseT declaration

#ifdef __MYDEBUG1__  
   #define FuzzFPRINT( msg ) DebugloggeR FuzzFPRINT( msg, __FILE__, __LINE__ );
#else
   #define FuzzFPRINT( msg );
#endif
      
#endif
