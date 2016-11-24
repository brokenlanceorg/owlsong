//***********************************************************************************************
// File     : RestrictHedge.hpp
// Purpose  : Defines our derived class RestrictHedge, which incorporates the basic operations of
//       : fuzzy and hedge sets and overloads the EvaluateIt method.
// Author   : Brandon Benham 
// Date     : 1/5/00
//***********************************************************************************************

#include"Hedge.hpp"

#ifndef __RESTRICT_HPP
#define __RESTRICT_HPP

//***********************************************************************************************
// Class : RestrictHedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : Declares the RestrictHedgE class.
//***********************************************************************************************
class RestrictHedgE : public HedgE
{
   public:

      enum RestrictType
      {
         NOT,
         ABOVE,
         BELOW
      }; // end enum

      // RestrictType:
      RestrictType Restrict_Type;

      // Default constructor:
      RestrictHedgE();

      // Destructor:
      virtual ~RestrictHedgE();

      // Constructor:
      RestrictHedgE( RestrictType );

      RestrictHedgE& operator << ( const long double& );
      
   protected:

      // The overloaded eval function:
      long double EvaluateIt( long double );

      // The ol' Setup method:
      void Setup( RestrictType );

}; // end class RestrictHedgE declaration

#ifdef __MYDEBUG__
   #define ResFPRINT( msg ) DebugloggeR ResFPRINT( msg, __FILE__, __LINE__ );
#else
   #define ResFPRINT( msg );
#endif
      
#endif
