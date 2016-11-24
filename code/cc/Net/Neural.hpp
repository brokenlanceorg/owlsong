//*****************************************************************************
// File     :  Neural.hpp
// Purpose  :  Contains the Declarations for the Neural class
// Author   :  Brandon Benham
//*****************************************************************************
#ifndef __NEURAL_HPP
#define __NEURAL_HPP

#include"Mathmatc.hpp"
#include"FileReader.hpp"

//************************************************************************
// Class    :  NeuralneT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  Class declaration
// Purpose  :  This class defines a Neural Net object which is an abstract
//          :  classs to be inhereted by the other type of neural nets.
//************************************************************************
class NeuralneT
{
   protected:
      char* pcFile_Name;   // Base name for all of our files
      int iNum_Inputs;     // number of input nodes
      int iNum_Hidden;     // number of hidden nodes
      int iNum_Outputs;    // number of output nodes
      unsigned long _maxEpoch;
      unsigned long ulIteration;
      unsigned long ulEpoch;
      unsigned short usIsMature;
      long double _learningRate; // learning rate of weight matrix
      long double _error;        // total error sum squared
      float fDecay_Rate;         // decay rate of weight values
      float fTolerance;          // tolerance to match net error
      long double _momentum;     // degree to which changes affect weight matrix
      long double ldEpsilon;     // Error control
      virtual int SaveWeights() = 0;
      virtual int LoadWeights() = 0;
      bool _quit;

   public:
      NeuralneT();
      NeuralneT(char*);
      ~NeuralneT();
      int ReadDefinitionFile();
      virtual int EncodeDataPair() = 0;
      virtual int Recall(VectoR*) = 0;
      virtual long double CycleThruNet() = 0;
      virtual void TrainNet() = 0;
      virtual float TestNet() = 0;
      virtual int Run() = 0;

      // Accessor Methods:
      inline int GetNumberOfHidden() { return iNum_Hidden; }
      inline long double GetLearningRate() { return _learningRate; }
      inline void SetLearningRate( long double l ) { _learningRate = l; }
      inline float GetTolerance() { return fTolerance; }
      inline long double GetMomentum() { return _momentum; }
      inline void SetMomentum( long double m ) { _momentum = m; }
      inline long double GetError() { return _error; }
      void Quit() { _quit = true; }
      void SetMaximumEpoch( unsigned long m ) { _maxEpoch = m; }
      unsigned long GetMaximumEpoch() { return _maxEpoch; }

}; // end Neuralnet declaration

#endif
