//***********************************************************************************************
// File     : GeneticRuleBase.cpp
// Purpose  : 
//          : 
// Author   : Brandon Benham 
// Date     : 10/4/01
//***********************************************************************************************

#include"GeneticRuleBase.hpp"          

//***********************************************************************************************
// Class    : GeneticRuleBasE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
GeneticRuleBasE::GeneticRuleBasE() : FuzzybasE()
{
   Setup();
} // end GeneticRuleBasE default constructor

//***********************************************************************************************
// Class    : GeneticRuleBasE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Constructor
// Purpose  : Performs the construction actions.
//***********************************************************************************************
GeneticRuleBasE::GeneticRuleBasE( VectoR* theGenome, int iWindow, int iAntecedents ) : FuzzybasE()
{
   _DataWindowSize    = iWindow;
   _NumberAntecedents = iAntecedents;
   _NumberOfRules     = iAntecedents;

   _theFuzzySets = new FuzzyseT*[ _NumberAntecedents ];
   _theConsequents = new ConsequenT*[ _NumberOfRules ];

   // First, create all the antecedents:
   int iCounter  = 0;
   int iCounter2 = 0;
   int i = 0;
   int j = 0;
   HedgE* temporarySet = 0;
   for( ; i<theGenome->cnRows; i+=4 ) // there are four genes per fuzzy set!
   {
   /*
      // We've created all the required antecedents:
      if( iCounter >= _NumberAntecedents )
         break;
   */
      if( (j++ % 2) == 0 )
      {
         // check to see if a hedge is turned on:
         if( theGenome->pVariables[ i + 2 ] > 0.5 )
         {
            temporarySet = (HedgE*)CreateHedge( theGenome->pVariables[ i + 3 ] );
            _theFuzzySets[ iCounter++ ] = &(*temporarySet + 
                *(CreateFuzzySet( theGenome->pVariables[ i ], theGenome->pVariables[ i + 1 ] )) );
         } // end if create a hedge
         else
         {
            _theFuzzySets[ iCounter++ ] = CreateFuzzySet( theGenome->pVariables[ i ], theGenome->pVariables[ i + 1 ] );
         } // end else
      } // end if mod 2
      else
      {
         // Next, create all the consequents:
         if( theGenome->pVariables[ i + 2 ] > 0.5 )
         {
            temporarySet = (HedgE*)CreateHedge( theGenome->pVariables[ i + 3 ] );
            _theConsequents[ iCounter2++ ] = CreateConsequent( &(*temporarySet + 
                *(CreateFuzzySet( theGenome->pVariables[ i ], theGenome->pVariables[ i + 1 ] )) ) );
         } // end if create a hedge
         else
         {
            _theConsequents[ iCounter2++ ] = CreateConsequent( 
                    CreateFuzzySet( theGenome->pVariables[ i ], theGenome->pVariables[ i + 1 ] ) );
         } // end else
      } // end else if mod 2
   } // end for

   // !!! Make sure you delete this one:
   // !!! Make sure to add the consequents to this set!
   _theResultSet = new CompoundconsequenT(); 
   for( int i=0; i<_NumberOfRules; i++ )
   {
      *_theResultSet + *_theConsequents[ i ];
   } // end for loop

} // end GeneticRuleBasE constructor

//***********************************************************************************************
// Class    : GeneticRuleBasE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
GeneticRuleBasE::~GeneticRuleBasE()
{
   if( _theResultSet != 0 )
   {
      delete _theResultSet;
      _theResultSet = 0;
   } // end if not null
   if( _theFuzzySets != 0 )
   {
      delete[] _theFuzzySets;
      _theFuzzySets = 0;
   } // end if not null
   if( _theConsequents != 0 )
   {
      delete[] _theConsequents;
      _theConsequents = 0;
   } // end if not null
} // end GeneticRuleBasE destructor

//***********************************************************************************************
// Class    : GeneticRuleBasE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void GeneticRuleBasE::Setup()
{
   _NumberAntecedents = 0;
   _NumberOfRules     = 0;
   _DataWindowSize    = 0;
   _theErrorTerm      = 0;

   _theFuzzySets   = 0;
   _theConsequents = 0;
   _theResultSet   = 0;
} // end Setup

//***********************************************************************************************
// Class    : GeneticRuleBasE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Extrapolate
// Purpose  : 
//***********************************************************************************************
long double GeneticRuleBasE::Extrapolate( VectoR* input, VectoR* output )
{
   FireRules( input, output );
   return Defuzzify();
} // end Extrapolate

//***********************************************************************************************
// Class    : GeneticRuleBasE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Extrapolate
// Purpose  : 
//***********************************************************************************************
long double GeneticRuleBasE::Extrapolate( long double theVar )
{
   FireRules( theVar );
   return Defuzzify();
} // end Extrapolate

//***********************************************************************************************
// Class    : GeneticRuleBasE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : CreateHedge
// Purpose  : 
//***********************************************************************************************
FuzzyseT* GeneticRuleBasE::CreateHedge( long double whatKind )
{
   FuzzyseT* theHedge = 0;

   if( whatKind >= 0 && whatKind < 0.071428 )
   {
      theHedge = CreateRestrictHedge( RestrictHedgE::NOT );
   }
   else if( whatKind >= 0.071428 && whatKind < 0.142856 )
   {
      theHedge = CreateRestrictHedge( RestrictHedgE::ABOVE );
   }
   else if( whatKind >= 0.142856 && whatKind < 0.214284 )
   {
      theHedge = CreateRestrictHedge( RestrictHedgE::BELOW );
   }
   else if( whatKind >= 0.214284 && whatKind < 0.285712 )
   {
      theHedge = CreateConcentrateHedge( ConcentrateHedgE::VERY );
   }
   else if( whatKind >= 0.285712 && whatKind < 0.35714 )
   {
      theHedge = CreateConcentrateHedge( ConcentrateHedgE::EXTREMELY );
   }
   else if( whatKind >= 0.35714 && whatKind < 0.428568 )
   {
      theHedge = CreateDiluteHedge( DiluteHedgE::SOMEWHAT );
   }
   else if( whatKind >= 0.428568 && whatKind < 0.499996 )
   {
      theHedge = CreateDiluteHedge( DiluteHedgE::RATHER );
   }
   else if( whatKind >= 0.499996 && whatKind < 0.571424 )
   {
      theHedge = CreateDiluteHedge( DiluteHedgE::QUITE );
   }
   else if( whatKind >= 0.571424 && whatKind < 0.642852 )
   {
      theHedge = CreateDiffuseHedge( DiffuseHedgE::GENERALLY );
   }
   else if( whatKind >= 0.642852 && whatKind < 0.71428 )
   {
      theHedge = CreateDiffuseHedge( DiffuseHedgE::USUALLY );
   }
   else if( whatKind >= 0.71428 && whatKind < 0.785708 )
   {
      theHedge = CreateDiffuseHedge( DiffuseHedgE::MOSTLY );
   }
   else if( whatKind >= 0.785708 && whatKind < 0.857136 )
   {
      theHedge = CreateIntenseHedge( IntenseHedgE::POSITIVELY );
   }
   else if( whatKind >= 0.857136 && whatKind < 0.928564 )
   {
      theHedge = CreateIntenseHedge( IntenseHedgE::DEFINITELY );
   }
   else if( whatKind >= 0.928564 && whatKind <= 1 )
   {
      theHedge = CreateIntenseHedge( IntenseHedgE::ABSOLUTELY );
   }
   else // make sure we don't return a null pointer:
   {
      theHedge = CreateIntenseHedge( IntenseHedgE::ABSOLUTELY );
   } // end else
 
   return theHedge;
} // end createHedge

//***********************************************************************************************
// Class    : GeneticRuleBasE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Defuzzify
// Purpose  : Finds the inverse of the collection of consequents.
//***********************************************************************************************
long double GeneticRuleBasE::Defuzzify()
{
//   FPRINT( "Defuzzify" );
   long double ldResult = 0;

   *_theResultSet >> ldResult;

   return ldResult;
} // end Defuzzify

//***********************************************************************************************
// Class    : GeneticRuleBasE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : CreateHedge
// Purpose  : 
// * This should fire the rule set for a rule base such as:
// *
// * if x1 is Y1 then z is W1
// * if x1 is Y2 then z is W2
// * if x1 is Y3 then z is W3
// * if x2 is Y1 then z is W4
// * if x2 is Y2 then z is W5
// * if x2 is Y3 then z is W6
// * if x3 is Y1 then z is W7
// * if x3 is Y2 then z is W8
// * if x3 is Y3 then z is W9
// * if y1 is Y4 then z is W10
// * if y1 is Y5 then z is W11
// * if y1 is Y6 then z is W12
// * if y2 is Y4 then z is W13
// * if y2 is Y5 then z is W14
// * if y2 is Y6 then z is W15
// *
// * In the above, the number of antecedents is 6.
// *
// * if x1 is Y1 then z is W1
// * if x1 is Y2 then z is W2
// * if x2 is Y1 then z is W3
// * if x2 is Y2 then z is W4
// * if x3 is Y1 then z is W5
// * if x3 is Y2 then z is W6
// * if y1 is Y3 then z is W7
// * if y1 is Y4 then z is W8
// * if y2 is Y3 then z is W9
// * if y2 is Y4 then z is W10
// *
// * In the above, the number of antecedents is 4.
//***********************************************************************************************
void GeneticRuleBasE::FireRules( VectoR* input, VectoR* output )
{
//   FPRINT( "FireRules" );
   int firstSet = _NumberAntecedents / 2 * _DataWindowSize;
   int iCounter = 0;
   int iCounter2 = 0;
   long double ldTempValue = 0;

   for( int i=0; i<_NumberOfRules; i+=_NumberAntecedents/2 )
   {
      if( i < firstSet ) // handle input variables
      {
         for( int j=0; j<_NumberAntecedents/2; j++ )
         {
            (*_theFuzzySets[ j ] << input->pVariables[ iCounter ] ) >> ldTempValue;
            *_theConsequents[ i + j ] << ldTempValue;
         } // end for loop
         iCounter++;
      } // end if
      else               // handle output variables
      {
         for( int j=_NumberAntecedents/2; j<_NumberAntecedents; j++ )
         {
            (*_theFuzzySets[ j ] << output->pVariables[ iCounter2 ] ) >> ldTempValue;
            *_theConsequents[ i + j - _NumberAntecedents/2 ] << ldTempValue;
         } // end for loop
         iCounter2++;
      } // end else
   } // end for loop

} // end FireRules

void GeneticRuleBasE::FireRules( long double theVariable )
{
   long double ldTempValue = 0;
   _theErrorTerm = 0;

   for( int i=0; i<_NumberOfRules; i++ )
   {
      (*_theFuzzySets[ i ] << theVariable) >> ldTempValue;
      if( ldTempValue == 0 )
      {
         _theErrorTerm += 1000;
      } // end if zero
      *_theConsequents[ i ] << ldTempValue;
   } // end for loop

} // end FireRules

