//***********************************************************************************************
// File     : ConcentrateHedge.hpp
// Purpose  : Defines our derived class ConcentrateHedge, which incorporates the basic operations of
//          : fuzzy and hedge sets and overloads the EvaluateIt method.
// Author   : Brandon Benham 
// Date     : 1/5/00
//***********************************************************************************************

#include"Hedge.hpp"

#ifndef __CONCENTRATE_HPP
#define __CONCENTRATE_HPP

//***********************************************************************************************
// Class : ConcentrateHedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : Declares the ConcentrateHedgE class.
//***********************************************************************************************
class ConcentrateHedgE : public HedgE
{
   public:

      // Concentrator Hedge Type:
      enum ConcentrateType
      {
         VERY        = 2,
         EXTREMELY   = 3
      }; // end enum

      // Default constructor:
      ConcentrateHedgE();

      // Destructor:
      virtual ~ConcentrateHedgE();

      // Constructor:
      ConcentrateHedgE( ConcentrateType );

      ConcentrateHedgE& operator << ( const long double& );
      
   protected:

      // The overloaded eval function:
      long double EvaluateIt( long double );

      // The ol' Setup method:
      void Setup( ConcentrateType );

}; // end class ConcentrateHedgE declaration

#ifdef __MYDEBUG__
   #define CHFPRINT( msg ) DebugloggeR CHFPRINT( msg, __FILE__, __LINE__ );
#else
   #define CHFPRINT( msg );
#endif     

#endif
