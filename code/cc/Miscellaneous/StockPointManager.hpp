//***********************************************************************************************
// File     : StockPointManager.hpp
// Purpose  : This class manages all references to StockpoinTs. When a NASDAQ point (real or not)
//          : is added to the model, it is first done through this interface.
//          : It is the responsiblility of this class to manage the Real StockpoinTs
// Author   : Brandon Benham 
// Date     : 6/18/00
//***********************************************************************************************
#ifndef __STOCKPOINTMANAGER_HPP
#define __STOCKPOINTMANAGER_HPP

#include "StockPoint.hpp"
#include "StockSeries.hpp"
#include "Serializable.hpp"
#include "Func.hpp"
//***********************************************************************************************
// Class    : StockpointmanageR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : This class manages all references to StockpoinTs. When a NASDAQ point (real or not)
//          : is added to the model, it is first done through this interface.
//          : It is the responsiblility of this class to manage the Real StockpoinTs
//          : 
//***********************************************************************************************
class StockpointmanageR : public SerializablE
{
   public:
      StockpointmanageR(); // Default Constructor declaration
      StockpointmanageR( char*, char* ); // Constructor declaration
      StockpointmanageR( char* ); // Constructor declaration - may make this int
      ~StockpointmanageR(); // Destructor declaration

      // Declared in Serializable:
      int Load();
      int Store();

      void AddActual( long double );
      void AddPredicted( long double );
      bool IsActualLast();
      bool IsLastIncreasing();
      StockserieS* GetActual( int );
      StockserieS* GetLastActual() { return theActualSeries[ iNumber_Of_Actuals - 1 ]; }
      StockserieS* GetLastSeries(); // returns the last series be it Actual or Predicted
      StockpoinT*  GetLastPoint()  { return theStockPoints[ iNumber_Of_Elements - 1 ]; }
      StockpoinT*  GetSecondToLastPoint()  { return theStockPoints[ iNumber_Of_Elements - 2 ]; }
      StockpoinT*  FindBestPoint( VectoR* );
      VectoR*      GetLastDifferences();

   protected:
      void Setup();
      StockpoinT**  theStockPoints;
      StockserieS** theActualSeries;
      char*         pcAlternate_File_Name;
      char*         pcSuffix;      // used by stockPoint class
      char*         pcPrediction_Suffix;      // used by stockPoint class
      int           iPeel_Size;    // how to peel off for peels
      int           iStep_Size;    // how many points to add when we resize
      int           iActual_Size;  // keeps track of the capacity of
      int           iPredict_Size; // the arrays of the objects.
      int           iNumber_Of_Elements;
      int           iNumber_Of_Actuals;
      void          ResizePredictions();
      void          ResizeActuals();
      long double   ldSMA;

   private:
      FunctioN*  funcHelper;
   
}; // end StockpointmanageR declaration

#endif

