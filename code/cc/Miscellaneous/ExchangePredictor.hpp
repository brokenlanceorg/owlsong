//***********************************************************************************************
// File     : ExchangePredictor.hpp
// Purpose  : 
//          : 
//          : 
// Author   : Brandon Benham 
// Date     : 7/13/00
//***********************************************************************************************
#ifndef __EXCHANGEPRED_HPP
#define __EXCHANGEPRED_HPP

#include "StockPointManager.hpp"
#include "StockPredictor.hpp"
//***********************************************************************************************
// Class    : ExchangepredictoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : 
// Notes    : Should we have a def file that delineates attributes?
//***********************************************************************************************
class ExchangepredictoR : StockpredictoR
{
   public:
      ExchangepredictoR();                     // Default Constructor declaration
      ExchangepredictoR( StockpointmanageR* ); // Default Constructor declaration
      ~ExchangepredictoR();                    // Destructor declaration

      virtual void Predict();
      VectoR* GetLastDifferences() { return  theManager->GetLastDifferences(); }
      bool    IsLastIncreasing()   { return theManager->IsLastIncreasing(); }

   protected:
      void    Setup();
      VectoR* GetSequentialNetworkOutput( NeuromanceR* );

   private:
   
}; // end ExchangepredictoR declaration

#endif

