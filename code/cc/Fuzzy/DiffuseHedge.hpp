//***********************************************************************************************
// File     : DiffuseHedge.hpp
// Purpose  : Defines our derived class DiffuseHedge, which incorporates the basic operations of
//          : fuzzy and hedge sets and overloads the EvaluateIt method.
// Author   : Brandon Benham 
// Date     : 1/5/00
//***********************************************************************************************

#include"Hedge.hpp"

#ifndef __DIFFUSE_HPP
#define __DIFFUSE_HPP

//***********************************************************************************************
// Class : DiffuseHedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : Declares the DiffuseHedgE class.
//***********************************************************************************************
class DiffuseHedgE : public HedgE
{
   public:

      // Diffuse Hedge Type:
      enum DiffuseType
      {
         GENERALLY   = 2,
         USUALLY     = 3,
         MOSTLY      = 4
      }; // end enum

      // Default constructor:
      DiffuseHedgE();

      // Destructor:
      virtual ~DiffuseHedgE();

      // Constructor:
      DiffuseHedgE( DiffuseType );

      DiffuseHedgE& operator << ( const long double& );
      
   protected:

      // The overloaded eval function:
      long double EvaluateIt( long double );

      // The ol' Setup method:
      void Setup( DiffuseType );

}; // end class DiffuseHedgE declaration

#ifdef __MYDEBUG__
   #define DiffFPRINT( msg ) DebugloggeR DiffFPRINT( msg, __FILE__, __LINE__ );
#else
   #define DiffFPRINT( msg );
#endif
      
#endif
