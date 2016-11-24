//***********************************************************************************************
// File     : FuzzyFactory.hpp
// Purpose  : Declares the FuzzyFactory interface which is used by the 
//          : FuzzyBaseClass to effortlessly and blithely create fuzzy
//          : objects such as Fuzzy Sets, Hedges, and Consequents.
//          :                                                         
// Author   : Brandon Benham 
// Date     : 5/30/00
//***********************************************************************************************
#ifndef __FUZZYFACTORY_HPP
#define __FUZZYFACTORY_HPP

#include "FuzzySet.hpp"
#include "RestrictHedge.hpp"
#include "ConcentrateHedge.hpp"
#include "IntenseHedge.hpp"
#include "DiluteHedge.hpp"
#include "DiffuseHedge.hpp"
#include "Consequent.hpp"
#include "ObjectVector.hpp"

#include <vector>

//***********************************************************************************************
// Class    : FuzzyfactorY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : This class will create and manage resources such as FuzzySets,
//          : Hedges, and Consequents throughout their lifetimes.
//***********************************************************************************************
class FuzzyfactorY
{
   public:
   
      FuzzyfactorY(); // Default Constructor declaration
      ~FuzzyfactorY(); // Destructor declaration

      // Gaussian distribution function, boolean tells us to use cuts
      FuzzyseT* CreateFuzzySet( long double, long double, bool = true );
      // The 'ol sigmoid, boolean tells us if it is increasing or not.
      FuzzyseT* CreateFuzzySet( long double, long double, long double, bool = false );
      // A linear fuzzy set (x1, y1) to (x2, y2)
      FuzzyseT* CreateFuzzySet( long double, long double, long double, long double );
      RestrictHedgE* CreateRestrictHedge( RestrictHedgE::RestrictType );
      ConcentrateHedgE* CreateConcentrateHedge( ConcentrateHedgE::ConcentrateType );
      DiluteHedgE* CreateDiluteHedge( DiluteHedgE::DiluteType );
      IntenseHedgE* CreateIntenseHedge( IntenseHedgE::IntenseType );
      DiffuseHedgE* CreateDiffuseHedge( DiffuseHedgE::DiffuseType );
      ConsequenT* CreateConsequent( FuzzyseT* );

   protected:
   
      void Setup();

   private:
   
      vector< FuzzyseT* > _theFuzzySets;
      vector< ConsequenT* > _theConsequentSets;

}; // end FuzzyfactorY declaration

#endif

