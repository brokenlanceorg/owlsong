//***********************************************************************************************
// File    : RollingNetwork.hpp
// Purpose : Base class for RollingNetworK classes
//         :
//         :
// Author  : Brandon Benham
// Date    : 5/3/05
//***********************************************************************************************

#ifndef __ROLLINGNET_HPP
#define __ROLLINGNET_HPP

#include "GeneticNeuralNetwork.hpp"

//***********************************************************************************************
// Class   : RollingNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose :
//         :
//***********************************************************************************************
class RollingNetworK
{
   public:
      RollingNetworK();          // Default Constructor declaration
      RollingNetworK( char*, int, int, int, int, unsigned short = 1 ); // Constructor declaration
      virtual ~RollingNetworK(); // Destructor declaration

      // Delegated calls to the base network:
      int Run() { return _geneticNetwork->Run(); }
      VectoR* Recall( VectoR* v ) { return _geneticNetwork->Recall( v ); }
      void TestNet() { _geneticNetwork->TestNet(); }
      void SetMaximumEpoch( unsigned long m ) { _geneticNetwork->SetMaximumEpoch( m ); }
      void AddMaximumEpoch( unsigned long m ) { _geneticNetwork->AddMaximumEpoch( m ); }
      void SetMature( unsigned short m ) { _geneticNetwork->SetMature( m ); }
      void SetRollbackPoint() { _geneticNetwork->SetRollbackPoint(); }
      void RollbackTrainingInstance();
      MatriX* GetTrainingInputMatrix() { return _geneticNetwork->GetInputMatrix(); }
      MatriX* GetTrainingOutputMatrix() { return _geneticNetwork->GetOutputMatrix(); }
      MatriX* GetSynapseOne() { return _geneticNetwork->GetSynapseOne(); }
      MatriX* GetSynapseTwo() { return _geneticNetwork->GetSynapseTwo(); }
      void SetSynapseOne( MatriX* mat ) { _geneticNetwork->SetSynapseOne( mat ); }
      void SetSynapseTwo( MatriX* mat ) { _geneticNetwork->SetSynapseTwo( mat ); }
      void SetTrainingInputMatrix( MatriX* mat ) { _geneticNetwork->SetTrainingInputMatrix( mat ); }
      void SetTrainingOutputMatrix( MatriX* mat ) { _geneticNetwork->SetTrainingOutputMatrix( mat ); }
      void SetNumberOfInstances( int n ) { _geneticNetwork->SetNumberOfInstances( n ); }
      void SaveWeights() { _geneticNetwork->SaveWeights(); }
      void WriteFactFile();
      void WriteDefaultFactFile();

      // Methods for RollingNetwork:
      void AddTrainingPair( VectoR*, VectoR* );
      int GetNumberOfInstances();

   protected:
      void Setup();
      bool DoesFileExist( char* );
      void CreateDefaultFiles( char*, int, int, int );
      void PopulateSynapses();

   private:
      int _depth;
      GeneticNeuralNetworK* _geneticNetwork;
      char _outputFileName[30];

}; // end RollingNetworK declaration

#endif
