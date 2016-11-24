//***********************************************************************************************
// File     : RouletteWheelSelectionStrategy.hpp
// Purpose  : 
//          : 
//          : 
// Author   : Brandon Benham 
// Date     : 9/20/01
//***********************************************************************************************
#ifndef __ROULETTEWHEEL_HPP
#define __ROULETTEWHEEL_HPP
            
#include"SelectionStrategy.hpp"
#include"Individual.hpp"

//***********************************************************************************************
// Class    : RouletteWheelSelectionStrategY
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : 
//          : 
//***********************************************************************************************
class RouletteWheelSelectionStrategY : protected SelectionStrategY
{
   public:
      RouletteWheelSelectionStrategY(); // Default Constructor declaration
      virtual ~RouletteWheelSelectionStrategY(); // Destructor declaration

      virtual IndividuaL* GetParent();
      
   protected:
      void Setup();

   private:
   
}; // end RouletteWheelSelectionStrategY declaration

#endif

