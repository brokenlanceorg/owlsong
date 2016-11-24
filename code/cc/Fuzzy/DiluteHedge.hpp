//***********************************************************************************************
// File     : DiluteHedge.hpp
// Purpose  : Defines our derived class DiluteHedge, which incorporates the basic operations of
//          : fuzzy and hedge sets and overloads the EvaluateIt method.
// Author   : Brandon Benham 
// Date     : 1/5/00
//***********************************************************************************************

#include"Hedge.hpp"

#ifndef __DILUTE_HPP
#define __DILUTE_HPP

//***********************************************************************************************
// Class : DiluteHedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : Declares the DiluteHedgE class.
//***********************************************************************************************
class DiluteHedgE : public HedgE
{
   public:

      // Dilute Hedge Type:
      enum DiluteType
      {
         SOMEWHAT = 2,
         RATHER   = 3,
         QUITE    = 4
      }; // end enum

      // Default constructor:
      DiluteHedgE();

      // Destructor:
      virtual ~DiluteHedgE();

      // Constructor:
      DiluteHedgE( DiluteType );

      DiluteHedgE& operator << ( const long double& );
      
   protected:

      // The overloaded eval function:
      long double EvaluateIt( long double );

      // The ol' Setup method:
      void Setup( DiluteType );

}; // end class DiluteHedgE declaration

#ifdef __MYDEBUG__
   #define DilFPRINT( msg ) DebugloggeR DilFPRINT( msg, __FILE__, __LINE__ );
#else
   #define DilFPRINT( msg );
#endif
      
#endif
