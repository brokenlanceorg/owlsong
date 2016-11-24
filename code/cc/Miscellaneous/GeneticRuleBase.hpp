//***********************************************************************************************
// File     : GeneticRuleBase.hpp
// Purpose  : 
//          : 
//          : 
// Author   : Brandon Benham 
// Date     : 10/4/01
//***********************************************************************************************
#ifndef __GENETICRULEBASE_HPP
#define __GENETICRULEBASE_HPP
            
#include"FuzzyBase.hpp"

//***********************************************************************************************
// Class    : GeneticRuleBasE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : 
//          : 
//***********************************************************************************************
class GeneticRuleBasE : protected FuzzybasE
{
   public:
      GeneticRuleBasE(); // Default Constructor declaration
      GeneticRuleBasE( VectoR*, int, int );
      ~GeneticRuleBasE(); // Destructor declaration

      long double Extrapolate( VectoR*, VectoR* );
      long double Extrapolate( long double );
      long double GetErrorTerm() { return _theErrorTerm; }

   protected:                           
      void Setup();
      long double Defuzzify();
      FuzzyseT* CreateHedge( long double );
      void FireRules( VectoR* input, VectoR* output );
      void FireRules( long double );
      void FireRules() {}

      int _NumberAntecedents;
      int _NumberOfRules;
      int _DataWindowSize;
      long double _theErrorTerm;

      FuzzyseT**           _theFuzzySets;
      ConsequenT**         _theConsequents;
      CompoundconsequenT*  _theResultSet;

   private:               
   
}; // end GeneticRuleBasE declaration

#endif

