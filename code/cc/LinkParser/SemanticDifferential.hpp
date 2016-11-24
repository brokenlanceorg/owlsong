//***********************************************************************************************
// File    : SemanticDifferential.hpp
// Purpose : Base class for SemanticDifferential classes
//         :
//         :
// Author  : Brandon Benham
// Date    : 5/12/05
//***********************************************************************************************

#ifndef __SEMANTICDIFF_HPP
#define __SEMANTICDIFF_HPP

#include "DebugLogger.hpp"

//***********************************************************************************************
// Class   : SemanticDifferentiaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose :
//         :
//***********************************************************************************************
class SemanticDifferentiaL
{
   public:

      SemanticDifferentiaL();          // Default Constructor declaration
      SemanticDifferentiaL( SemanticDifferentiaL* );
      virtual ~SemanticDifferentiaL(); // Destructor declaration
 
      void SetAgentDimension( long double a ) { _agent = a; }
      void SetKLineDimension( long double a ) { _Kline = a; }
      void SetThingDimension( long double a ) { _thing = a; }
      void SetSpatialDimension( long double a ) { _spatial = a; }
      void SetEventDimension( long double a ) { _event = a; }
      void SetCausalDimension( long double a ) { _causal = a; }
      void SetFunctionalDimension( long double a ) { _functional = a; }
      void SetAffectiveDimension( long double a ) { _affective = a; }

      long double GetAgentDimension() { return _agent; }
      long double GetKLineDimension() { return _Kline; }
      long double GetThingDimension() { return _thing; }
      long double GetSpatialDimension() { return _spatial; }
      long double GetEventDimension() { return _event; }
      long double GetCausalDimension() { return _causal; }
      long double GetFunctionalDimension() { return _functional; }
      long double GetAffectiveDimension() { return _affective; }

   protected:

      void Setup();

   private:

      long double _agent;
      long double _Kline;
      long double _thing;
      long double _spatial;
      long double _event;
      long double _causal;
      long double _functional;
      long double _affective;

}; // end SemanticDifferentiaL declaration

#endif
