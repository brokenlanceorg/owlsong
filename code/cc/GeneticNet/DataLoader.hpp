//***********************************************************************************************
// File	    : DataLoader.hpp
// Purpose  : This class will load data for a particular instance of the Genetic Algorithm
//          : 
//          : 
// Author   : Brandon Benham 
// Date	    : 4/9/05
//***********************************************************************************************
#ifndef __DATALOADER_HPP
#define __DATALOADER_HPP
            
#include "ModBackprop.hpp"

//***********************************************************************************************
// Class   : DataLoadeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose : Holds values for the genetic population
//         :
//***********************************************************************************************
class DataLoadeR
{
   public:
      virtual ~DataLoadeR(); // Destructor declaration
      inline ModBackproP* GetNeuralNet() { return _theNetwork; }
      inline void SetNeuralNet( ModBackproP* n ) { _theNetwork = n; }
      static DataLoadeR* GetInstance();

   protected:
      void Setup();

   private:
      DataLoadeR();  // Default Constructor declaration
      static DataLoadeR* _myInstance;
      static ModBackproP* _theNetwork;

}; // end DataLoadeR declaration

#endif
