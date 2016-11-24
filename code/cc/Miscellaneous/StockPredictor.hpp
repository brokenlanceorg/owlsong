//***********************************************************************************************
// File     : StockPredictor.hpp
// Purpose  : 
//          : 
//          : 
// Author   : Brandon Benham 
// Date     : 7/11/00
//***********************************************************************************************
#ifndef __STOCKPREDICTOR_HPP
#define __STOCKPREDICTOR_HPP

#include "StockPointManager.hpp"
#include "NeuroMancer.hpp"
#include "StockSeries.hpp"
#include "Filesrc.hpp"
//***********************************************************************************************
// Class    : StockpredictoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : Base Abstract class for the Stock predictor classes
//          : 
//***********************************************************************************************
class StockpredictoR
{
   public:
      StockpredictoR( StockpointmanageR*, int = 3 ); // Default Constructor declaration
      ~StockpredictoR(); // Destructor declaration

      virtual void Predict() = 0;
      virtual StockserieS* GetLastPredictions() { 
                return (theManager->GetLastPoint())->GetPredictions(); }
      virtual StockserieS* GetLastActuals() { theManager->GetLastActual(); }
                     
   protected:
      int iExtrapolate_Size;   // The number of prediction blocks
      int iPeel_Size;          // The number of elements to predict per block
      FilesourcE* fThe_Output_File; // Should use ofstream instead.
      StockpointmanageR* theManager;
      
      void Setup();
      virtual StockserieS* GetLastSeries();
      
   private:
   
}; // end StockpredictoR declaration

#endif

