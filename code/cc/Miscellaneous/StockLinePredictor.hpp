//***********************************************************************************************
// File          : StockLinePredictor.hpp
// Purpose      : 
//              : 
//              :
// Author   : Brandon Benham 
// Date     : 7/11/00
//***********************************************************************************************
#ifndef __STOCKLINEPREDICTOR_HPP
#define __STOCKLINEPREDICTOR_HPP

#include "StockPredictor.hpp"
#include "ExchangePredictor.hpp"
#include "SmoothedMovingAverageInverse.hpp"
#include "NFunc.hpp"

//***********************************************************************************************
// Class        : StocklinepredictoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose      : 
//              : 
//***********************************************************************************************
class StocklinepredictoR : public StockpredictoR
{
   public:
      StocklinepredictoR( int = 1, int = 3 );            // Default Constructor declaration
      StocklinepredictoR( StockpointmanageR*, int = 3 ); // Constructor declaration
      ~StocklinepredictoR();                             // Destructor declaration

      virtual void Predict();

      void SetExchangePredictor( ExchangepredictoR* thePred ) { NASDAQPredictor = thePred; }
                     
   protected:
      void        Setup();
      long double GetSequentialNetworkOutput( NeuromanceR* ); // Actually predicts the next point.
      VectoR*     GetDifferences();
      
   private:
      int                iNumber_Of_Points_To_Predict;
      int                iPeel_Size;
      ExchangepredictoR* NASDAQPredictor;
   
}; // end StocklinepredictoR declaration

#endif
