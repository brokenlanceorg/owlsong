//***********************************************************************************************
// File     : Holder.hpp
// Purpose  : Declares the Holder classes. In all actuality, we can 
//          : probably do away with these in favor of template classes.
//          : 
// Author   : Brandon Benham 
// Date     : 5/30/00
//***********************************************************************************************
#ifndef __HOLDER_HPP
#define __HOLDER_HPP

#include "FuzzySet.hpp"
#include "Consequent.hpp"
#include "Hedge.hpp"
#include "RestrictHedge.hpp"
#include "ConcentrateHedge.hpp"
#include "DiffuseHedge.hpp"
#include "DiluteHedge.hpp"
#include "IntenseHedge.hpp"
//***********************************************************************************************
// Class    : FuzzyholdeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : These classes are wrappers for Fuzzy Sets, Hedges
//          : and Consequents that are produced from the Fuzzy
//          : Factory.
//          :
//          :
//***********************************************************************************************
class FuzzyholdeR
{
   public:
      FuzzyholdeR(); // Default Constructor declaration
      ~FuzzyholdeR(); // Destructor declaration

      FuzzyholdeR( FuzzyseT* ); // Constructor declaration
      inline void SetObject( FuzzyseT* );
      inline FuzzyholdeR* GetNext() { return NextHolder; }
      inline void SetNext( FuzzyholdeR* next ) { NextHolder = next; }
                              
   protected:
      void Setup(); // Initialize pointers and such

   private:
      FuzzyseT* TheObject;
      FuzzyholdeR* NextHolder;
   
}; // end FuzzyholdeR declaration

//***********************************************************************************************
// Class    : HedgeholdeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : These classes are wrappers for Fuzzy Sets, Hedges
//          : and Consequents that are produced from the Fuzzy
//          : Factory.
//          :
//          :
//***********************************************************************************************
class HedgeholdeR
{
   public:
      HedgeholdeR(); // Default Constructor declaration
      ~HedgeholdeR(); // Destructor declaration

      HedgeholdeR( HedgE* ); // Default Constructor declaration
      void SetObject( HedgE* );
      inline HedgeholdeR* GetNext() { return NextHolder; }
      inline void SetNext( HedgeholdeR* next ) { NextHolder = next; }
      
   protected:
      inline void Setup(); // Initialize pointers and such
             
   private:
      HedgE* TheObject;
      HedgeholdeR* NextHolder;
                                 
}; // end HedgeholdeR declaration

//***********************************************************************************************
// Class    : ConsequentHoldeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : These classes are wrappers for Fuzzy Sets, Hedges
//          : and Consequents that are produced from the Fuzzy
//          : Factory.
//          :
//          :
//***********************************************************************************************
class ConsequentHoldeR
{
   public:
      ConsequentHoldeR(); // Default Constructor declaration
      ~ConsequentHoldeR(); // Destructor declaration

      ConsequentHoldeR( ConsequenT* ); // Default Constructor declaration
      inline void SetObject( ConsequenT* );
      inline void SetNext( ConsequentHoldeR* next ) { NextHolder = next; }
      inline ConsequentHoldeR* GetNext() { return NextHolder; }
      
   protected:
      void Setup(); // Initialize pointers and such
   
   private:
      ConsequenT* TheObject;
      ConsequentHoldeR* NextHolder;
   
}; // end ConsequentHoldeR declaration

#endif

