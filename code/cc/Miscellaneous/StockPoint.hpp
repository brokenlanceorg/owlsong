//***********************************************************************************************
// File     : StockPoint.hpp
// Purpose  : This class basically manages the mapping between a predicted StockserieS
//          : and its associated Real StockserieS. It may have a class that is
//          : responsible for creating the parallel neural net.
//          :                                                                 
// Author   : Brandon Benham 
// Date     : 6/18/00
//***********************************************************************************************
#ifndef __STOCKPOINT_HPP
#define __STOCKPOINT_HPP

#include "StockSeries.hpp"
//***********************************************************************************************
// Class    : StockpoinT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : This class basically manages the mapping between a predicted StockserieS
//          : and its associated Real StockserieS. It may have a class that is
//          : responsible for creating the parallel neural net.
//          : 
//***********************************************************************************************
class StockpoinT
{
   public:
      StockpoinT(); // Default Constructor declaration
      StockpoinT( int, char*, void*, long double = 0.1 ); // Constructor declaration
      StockpoinT( int, void*, long double = 0.1 ); // Constructor declaration
      StockpoinT( int, long double = 0.1 ); // Constructor declaration
      ~StockpoinT(); // Destructor declaration

      long double Compare( VectoR* );
      virtual StockpoinT* Peel( int );
      StockserieS* GetPredictions() { return thePrediction; }
      StockserieS* GetActuals();
      int GetObjectID() { return iObjectID; }
      int GetActualID() { return iActualID; }
      void AddPredictionElement( long double x ) { GetPredictions()->AddElement( x ); }
      long double GetLastAverage() { return thePrediction->GetLastAverage(); }
      long double GetFirstAverage() { return thePrediction->GetFirstAverage(); }
      void SetFirstAverage( long double ldF ) { thePrediction->SetFirstAverage( ldF ); }

   protected:
      void Setup();
      void ConstructSeries( char* = 0 );
      void LoadActuals();
      // members:
      StockserieS* thePrediction; // This object is responsible for this member
      StockserieS* theActual;     // The StockpointmagageR is responsible for this
      void*        theManager;    // This is actually a StockPointManager with which to get the actuals
      int          iObjectID;     // Identifies this instance for the StockserieS
      int          iActualID;     // Identifies the actual stock values (StockserieS)
      long double  ldSMA;         // The smoothed moving average value

   private:
   
}; // end StockpoinT declaration

#endif

