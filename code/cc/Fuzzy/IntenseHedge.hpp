//***********************************************************************************************
// File     : IntenseHedge.hpp
// Purpose  : Defines our derived class IntenseHedge, which incorporates the basic operations of
//          : fuzzy and hedge sets and overloads the EvaluateIt method.
// Author   : Brandon Benham 
// Date     : 1/5/00
//***********************************************************************************************
#ifndef __INTENSE_HPP
#define __INTENSE_HPP

#include"Hedge.hpp"

//***********************************************************************************************
// Class : IntenseHedgE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : Declares the IntenseHedgE class.
//***********************************************************************************************
class IntenseHedgE : public HedgE
{
   public:

      // Intense Hedge Type:
      enum IntenseType
      {
         POSITIVELY = 2,
         DEFINITELY = 3,
         ABSOLUTELY = 4
      }; // end enum

      // Default constructor:
      IntenseHedgE();

      // Destructor:
      virtual ~IntenseHedgE();

      // Constructor:
      IntenseHedgE( IntenseType );

      IntenseHedgE& operator << ( const long double& );

   protected:

      // The overloaded eval function:
      long double EvaluateIt( long double );

      // The ol' Setup method:
      void Setup( IntenseType );

}; // end class IntenseHedgE declaration

#ifdef __MYDEBUG__
   #define IntFPRINT( msg ) DebugloggeR IntFPRINT( msg, __FILE__, __LINE__ );
#else
   #define IntFPRINT( msg );
#endif  

#endif
