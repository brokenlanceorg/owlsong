//***************************************************************************** 
// File     :  Backprop.hpp
// Purpose  :  Contains the implementations for the BackproP class
// Author   :  Brandon Benham
//*****************************************************************************
#ifndef __BACKPROP_HPP
#define __BACKPROP_HPP

#include"Neural.hpp"
#include"Numerr.hpp"
#include"Onevar.hpp"  
#include"Mathmatc.hpp"
#include"FileReader.hpp"
#include<fstream>

using namespace std;

//************************************************************************
// Class    :  BackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  Class Declaration
// Purpose  :  This class defines a Backpropagation network object
//          :  which is derived from the NeuralneT abstract class
//************************************************************************
class BackproP : public NeuralneT
{
   protected:
      unsigned short usIsAlive;
      unsigned short _performRollback;
      int iNumber_Instances; // the number of training instances
      int iCurrent_Instance;
      char* pcBase_Name;// pointer to base file name string
      VectoR* vInput;   // input neurons
      VectoR* vOutput;  // output neurons
      VectoR* vHidden;  // hidden layer neurons
      VectoR* vEncodeIn;
      VectoR* vEncodeOut;
      VectoR* vOutputError;
      VectoR* vHiddenError;
      VectoR* vErrorTemp;
      MatriX* mSynapseOne; // first layer synapses
      MatriX* mSynapseTwo; // second layer synapses
      MatriX* mTrainingInput;    // used to store input facts
      MatriX* mTrainingOutput;   // and the corresponding output facts
      MatriX* _synapseOneBackup; // first layer synapses
      MatriX* _synapseTwoBackup; // second layer synapses
      // this function's params may have to be modified to obtain generality
      long double (*TheActivationFunc)(long double);
      long double (*TheDerivativeFunc)(long double);
      NumerR* NumericalError; // to catch numerical errors
      OnevariablE* Fct1Activation; // these are to replace the activation
      OnevariablE* Fct1Derivative; // and derivative functions respectively.
      long double ldThisError; 
      // Later, we can add a magnitude catcher for momentum

      virtual void CheckProgress( float );

      // inherited virtual functions:
      int LoadWeights();
      void Setup();

   public:
      BackproP();
      BackproP(char*, unsigned short usFirst = 0);
      virtual ~BackproP();
      inline void SetMature(unsigned short usN) {usIsMature = usN;}
      inline unsigned short IsMature() { return usIsMature; }
      void SetActivationFunc(long double (*TheFunc)(long double));
      void SetActivationDerivative(long double (*TheDxFunc)(long double));
      void SetNumberOfInstances( int n ) { iNumber_Instances = n; };
      void SetTrainingInputMatrix( MatriX* );
      void SetTrainingOutputMatrix( MatriX* );
      void SetSynapseOne( MatriX* );
      void SetSynapseTwo( MatriX* );
      void ReadEmperica(); // read in training data
      int FillTrainMatrices( FilereadeR* );
      void ComputeOutputError();
      void ComputeHiddenError();
      void UpdateOutputWeights();
      void UpdateHiddenWeights();
      void QuitForNow();
      void SetRollbackPoint();
      void Rollback();
      
      // inherited virtual functions:
      int EncodeDataPair();
      int Recall(VectoR*);
      long double CycleThruNet();
      virtual void TrainNet();
      float TestNet();
      int Run();
      int SaveWeights();

      // Accessor Functions:
      inline char* GetBaseName()       { return pcBase_Name;    }
      inline int GetNumberOfInputs()   { return iNum_Inputs;    }
      inline int GetNumberOfHidden()   { return iNum_Hidden;    }
      inline int GetNumberOfOutputs()  { return iNum_Outputs;   }
      inline int GetNumberOfInstances(){ return iNumber_Instances;}
      inline float GetTolerance()      { return fTolerance;     }
      inline unsigned long GetEpochs() { return ulEpoch;        }
      VectoR& GetTrainingInput(int);
      MatriX* GetInputMatrix() { return mTrainingInput; }
      MatriX* GetOutputMatrix() { return mTrainingOutput; }
      MatriX* GetSynapseOne() { return mSynapseOne; }
      MatriX* GetSynapseTwo() { return mSynapseTwo; }
      VectoR& GetOutput();
}; // end class declaration

#endif
