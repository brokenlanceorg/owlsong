//***********************************************************************************************
// File    : QuickneticNetwork.hpp
// Purpose : Base class for QuickneticNetwork classes
//         :
//         :
// Author  : Brandon Benham
// Date    : 8/12/05
//***********************************************************************************************

#ifndef __QUICKNETICNETWORK_HPP
#define __QUICKNETICNETWORK_HPP

#include "GeneticNeuralNetwork.hpp"
#include "FileReader.hpp"
#include "Mathmatc.hpp"
#include <iostream>
#include <vector>

using namespace std;

//***********************************************************************************************
// Class   : QuickneticNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose :
//         :
//***********************************************************************************************
class QuickneticNetworK
{
   public:

      QuickneticNetworK( char*, unsigned short ); // Default Constructor declaration
      virtual ~QuickneticNetworK();               // Destructor declaration
   
      void SetMature( unsigned short m ) { _isMature = m; _geneticNetwork->SetMature( m ); }
      int Run();
      MatriX* RecurseRecall( int );
      MatriX* RecurseRecall( VectoR*, int );
      VectoR* Recall( VectoR* );

   protected:

      void Setup();

   private:

      GeneticNeuralNetworK* _geneticNetwork;
      string* _dataFileName;
      int _numberInputs;
      int _numberOutputs;
      vector< long double > _trainingData;
      unsigned short _isMature;

}; // end QuickneticNetworK declaration

#endif
