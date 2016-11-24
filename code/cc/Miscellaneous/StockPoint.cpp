//***********************************************************************************************
// File     : StockPoint.cpp
// Purpose  : This class basically manages the mapping between a predicted StockserieS
//          : and its associated Real StockserieS. It may have a class that is
//          : responsible for creating the parallel neural net.
//          :                                                                 
// Author   : Brandon Benham 
// Date     : 6/24/00
//***********************************************************************************************

#include"StockPoint.hpp"          
#include"StockPointManager.hpp" // we add this here because the header knows nothing
                                // about the Manager, it is here that we do a runtime cast.
//***********************************************************************************************
// Class    : StockpoinT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
StockpoinT::StockpoinT()
{
   Setup();
} // end StockpoinT default constructor

//***********************************************************************************************
// Class    : StockpoinT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Constructor
// Purpose  : Performs the construction actions.
//***********************************************************************************************
StockpoinT::StockpoinT( int iOID, void* man, long double ldS )
{
   Setup();
   ldSMA = ldS;
   theManager = man;
   iObjectID = iOID;
   ConstructSeries();
   iActualID = iObjectID; // this should be zero-based
   LoadActuals();
} // end StockpoinT constructor
  
//***********************************************************************************************
// Class    : StockpoinT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Constructor
// Purpose  : Performs the construction actions.
//***********************************************************************************************
StockpoinT::StockpoinT( int iOID, char* pcSuffix, void* man, long double ldS )
{
   Setup();
   ldSMA = ldS;
   theManager = man;
   iObjectID = iOID;
   ConstructSeries( pcSuffix );
   iActualID = iObjectID; // this should be zero-based
   LoadActuals();
} // end StockpoinT constructor
  
//***********************************************************************************************
// Class    : StockpoinT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
StockpoinT::~StockpoinT()
{
   if( thePrediction != 0 )
      delete thePrediction;
} // end StockpoinT destructor

//***********************************************************************************************
// Class    : StockpoinT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void StockpoinT::Setup()
{
   thePrediction = 0;
   theActual     = 0;
   theManager    = 0;
   iObjectID     = 0;
   iActualID     = -1;
   ldSMA         = 0.1;
} // end Setup   

//***********************************************************************************************
// Class    : StockpoinT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : ConstructSeries
// Purpose  : Builds the file name for the construction of the StockserieS object
//***********************************************************************************************
void StockpoinT::ConstructSeries( char* pcSuffixName )
{
   if( pcSuffixName == 0 )
      thePrediction = new StockserieS( (int)iObjectID, ldSMA );
   else
      thePrediction = new StockserieS( (int)iObjectID, pcSuffixName, ldSMA );
} // end ConstructFileName

//***********************************************************************************************
// Class    : StockpoinT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : LoadActuals
// Purpose  : This method must have the iActualID set, along with the StockPointManager
//          : then it will load in the actual dataseries.
//***********************************************************************************************
void StockpoinT::LoadActuals()
{
   if( theManager == 0 || iActualID < 0 )
      return;
   theActual = ((StockpointmanageR*)theManager)->GetActual( iActualID );
} // end LoadActuals

//***********************************************************************************************
// Class    : StockpoinT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Compare
// Purpose  : This computes a distance metric on the argument and this Point's
//          : Data elements.
//***********************************************************************************************
long double StockpoinT::Compare( VectoR* vec )
{
   return thePrediction->Compare( vec );
} // end Compare

//***********************************************************************************************
// Class    : StockpoinT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Peel
// Purpose  : "Peels" off the last iHowMany elements to for a new StockPoint object
//          : 
//***********************************************************************************************
StockpoinT* StockpoinT::Peel( int iHowMany )
{
   if( iHowMany > thePrediction->GetNumberOfElements() )
      return 0;
      
   StockserieS* newSeries = thePrediction->Peel( iHowMany );
   long id = newSeries->GetID();
   String suffix( newSeries->GetSuffix() );
   String match( "_StockPrediction.dat" );
   delete newSeries;

   // Not good, not good at all.....
   StockpoinT* aNewPoint = 0;
   if( suffix == match )
      aNewPoint = new StockpoinT( id, match.c_str(), theManager );
   else
      aNewPoint = new StockpoinT( id, theManager );
      
   return aNewPoint;
} // end Peel

//***********************************************************************************************
// Class    : StockpoinT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : GetActuals
// Purpose  : 
//          : 
//***********************************************************************************************
StockserieS* StockpoinT::GetActuals()
{
   if( theActual == 0 )
      LoadActuals();

   return theActual;
} // end GetActuals

