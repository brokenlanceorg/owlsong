//***********************************************************************************************
// File     : StockSeries.hpp
// Purpose  : This class derives from the DataserieS class to provide additional
//          : functionality such as a moving average line.
//          : 
// Author   : Brandon Benham 
// Date     : 6/18/00
//***********************************************************************************************
#ifndef __STOCKSERIES_HPP
#define __STOCKSERIES_HPP

#include "DataSeries.hpp"
#include "SmoothedMovingAverage.hpp"
#include "Mathmatc.hpp"
//***********************************************************************************************
// Class    : StockserieS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : This class derives from the DataserieS class to provide additional
//          : functionality such as a moving average line.
//***********************************************************************************************
class StockserieS : public DataserieS
{
   public:
      StockserieS( long double = 0.1 );        // Default Constructor declaration
      StockserieS( int, long double = 0.1 ); // Constructor for persistent dataseries
      StockserieS( int, char*, long double = 0.1 ); // Constructor for persistent dataseries
      StockserieS( char*, long double, long double = 0.1 ); // Constructor for persistent dataseries
      ~StockserieS(); // Destructor declaration
      
      virtual void AddElement( long double );
      virtual void SetSmoothingFactor( long double ldx ) { ldSmoothing_Factor = ldx; Recalculate(); }
      virtual void SetFirstAverage( long double ldF ) { ldFirst_Average = ldF; Recalculate(); }
      VectoR* GetAverages() { return vThe_Averages; }
      long double GetDataMaximum() { return ldData_Maximum; }
      long double GetDataMinimum() { return ldData_Minimum; }
      long double GetSMAMaximum() { return ldSMA_Maximum; }
      long double GetSMAMinimum() { return ldSMA_Minimum; }
      long double Compare( VectoR* );
      virtual long double GetLastAverage() { return vThe_Averages->pVariables[vThe_Averages->cnRows-1]; }
      virtual long double GetFirstAverage() { return vThe_Averages->pVariables[ 0 ]; }
      virtual StockserieS* Peel( int );
      virtual inline char* GetName() { return DataserieS::GetName(); }
      virtual inline void  SetName( char* pc ) { DataserieS::SetName( pc ); }
      virtual inline int   GetID()   { return iObjectID; }
      virtual inline void  SetID( int id ) { iObjectID = id; }
      virtual inline char* GetSuffix() { return pcSuffix; }
      virtual inline void  SetSuffix( char* );
      virtual inline void  GenerateFileName();
      virtual inline int   GetNumberOfElements();
      void SetTransient( bool bT = true ) { DataserieS::SetTransient( bT ); }
      
                                            
   protected:
      void Setup();
      void Recalculate();
      void CalculateMax();
      void CalculateMin();
      char* MakeFileName( int );
      char* MakeFileName( int, char* );

   private:
      SmoothedMovingAveragE* theMoving_Average_Calculator;
      VectoR*                vThe_Averages;
      FunctioN*              Fthe_Function;
      long double            ldFirst_Average;
      long double            ldSmoothing_Factor;
      long double            ldData_Minimum;
      long double            ldData_Maximum;
      long double            ldSMA_Minimum;
      long double            ldSMA_Maximum;
      int                    iObjectID;
      char*                  pcSuffix;
   
}; // end StockserieS declaration

#endif

