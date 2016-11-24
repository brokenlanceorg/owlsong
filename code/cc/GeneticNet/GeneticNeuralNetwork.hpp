//***********************************************************************************************
// File	   : GeneticNeuralNetwork.hpp
// Purpose : 
//         : 
// Author  : Brandon Benham 
// Date	   : 4/9/05
//***********************************************************************************************
#ifndef __GENETICNET_HPP
#define __GENETICNET_HPP
            
#include "DataLoader.hpp"
#include "ModBackprop.hpp"
#include "GeneticAlgorithm.hpp"
#include "GeneticNetIndividual.hpp"
#include "RouletteWheelSelectionStrategy.hpp"

//***********************************************************************************************
// Class   : GeneticNeuralNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose : Encapsulates the neural network and the Genetic impl.
//         : 
//***********************************************************************************************
class GeneticNeuralNetworK
{
   public:
      GeneticNeuralNetworK( char*, unsigned short ); // Default Constructor declaration
      virtual ~GeneticNeuralNetworK(); // Destructor declaration

      // methods from neural net:
      VectoR* Recall( VectoR* v ) { _theNeuralNetwork->Recall( v ); 
                                    return &(_theNeuralNetwork->GetOutput()); }
      void SetMaximumEpoch( unsigned long max ) { _theNeuralNetwork->SetMaximumEpoch( max ); }
      void AddMaximumEpoch( unsigned long max ) 
              { _theNeuralNetwork->SetMaximumEpoch( _theNeuralNetwork->GetEpochs() + max ); }
      void SetNumberOfInstances( int n ) { _theNeuralNetwork->SetNumberOfInstances( n ); }
      void SetTrainingInputMatrix( MatriX* mat ) { _theNeuralNetwork->SetTrainingInputMatrix( mat ); }
      void SetTrainingOutputMatrix( MatriX* mat ) { _theNeuralNetwork->SetTrainingOutputMatrix( mat ); }
      void SetMature( unsigned short m ) { _theNeuralNetwork->SetMature( m ); }
      void SetRollbackPoint() { _theNeuralNetwork->SetRollbackPoint(); }
      MatriX* GetInputMatrix() { return _theNeuralNetwork->GetInputMatrix(); }
      MatriX* GetOutputMatrix() { return _theNeuralNetwork->GetOutputMatrix(); }
      MatriX* GetSynapseOne() { return _theNeuralNetwork->GetSynapseOne(); }
      MatriX* GetSynapseTwo() { return _theNeuralNetwork->GetSynapseTwo(); }
      void SetSynapseOne( MatriX* mat ) { _theNeuralNetwork->SetSynapseOne( mat ); }
      void SetSynapseTwo( MatriX* mat ) { _theNeuralNetwork->SetSynapseTwo( mat ); }
      void TestNet() { _theNeuralNetwork->TestNet(); }
      void SaveWeights() { _theNeuralNetwork->SaveWeights(); }
      int Run();
      int GetNumberOfInstances() { return _theNeuralNetwork->GetNumberOfInstances(); }

   protected:
      void Setup();
   
   private:
      ModBackproP* _theNeuralNetwork;
}; // end GeneticNeuralNetworK declaration

#endif
