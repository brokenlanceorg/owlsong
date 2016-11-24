//***********************************************************************************************
// File     : SemanticDifferential.cpp
// Purpose  :
//          :
// Author   : Brandon Benham
// Date     : 5/12/05
//***********************************************************************************************

#include "SemanticDifferential.hpp"

//***********************************************************************************************
// Class    : SemanticDifferentiaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
SemanticDifferentiaL::SemanticDifferentiaL()
{
   Setup();
} // end SemanticDifferentiaL default constructor

//***********************************************************************************************
// Class    : SemanticDifferentiaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
SemanticDifferentiaL::SemanticDifferentiaL( SemanticDifferentiaL* sd )
{
   Setup();
   
   SetAgentDimension( sd->GetAgentDimension() );
   SetKLineDimension( sd->GetKLineDimension() );
   SetThingDimension( sd->GetThingDimension() );
   SetSpatialDimension( sd->GetSpatialDimension() );
   SetEventDimension( sd->GetEventDimension() );
   SetCausalDimension( sd->GetCausalDimension() );
   SetFunctionalDimension( sd->GetFunctionalDimension() );
   SetAffectiveDimension( sd->GetAffectiveDimension() );
} // end SemanticDifferentiaL default constructor

//***********************************************************************************************
// Class    : SemanticDifferentiaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
SemanticDifferentiaL::~SemanticDifferentiaL()
{
   //FPRINT( "~SemanticDifferentiaL" );
} // end SemanticDifferentiaL destructor

//***********************************************************************************************
// Class    : SemanticDifferentiaL
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void SemanticDifferentiaL::Setup()
{
   _agent      = -1;
   _Kline      = -1;
   _thing      = -1;
   _spatial    = -1;
   _event      = -1;
   _causal     = -1;
   _functional = -1;
   _affective  = -1;
} // end Setup
