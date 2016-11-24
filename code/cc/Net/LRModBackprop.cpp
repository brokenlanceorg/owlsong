//***************************************************************************** 
// File    : ModBackprop.cpp
// Purpose : Contains the implementations for the ModBackproP class
// Author  : Brandon Benham
//*****************************************************************************
#include"ModBackprop.hpp"

//*****************************************************************************
// Class    : ModBackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function : Default Constructor
// Purpose  : This class defines a ModBackpropagation network object
//          : which is derived from the NeuralneT abstract class.
// Notes    : 
//          : 
//          : 
//          : 
//          : 
//          :
//          : 
//*****************************************************************************
ModBackproP::ModBackproP( char* pcFile, unsigned short usAlive ) : BackproP( pcFile, usAlive )
{
   Setup();
} // end constructor

//*****************************************************************************
// Class    : ModBackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function : Destructor
// Purpose  : This class defines a ModBackpropagation network object
//          : which is derived from the NeuralneT abstract class.
//*****************************************************************************
ModBackproP::~ModBackproP()
{
   if( _sumDeltaOutputErrorW != 0 )
   {
      delete _sumDeltaOutputErrorW; 
      _sumDeltaOutputErrorW = 0;
   }
   if( _hiddenErrorSumDeltaOutputErrorW != 0 )
   {
      delete _hiddenErrorSumDeltaOutputErrorW; 
      _hiddenErrorSumDeltaOutputErrorW = 0;
   }
   if( _hiddenGradientDifference != 0 )
   {
      delete _hiddenGradientDifference; 
      _hiddenGradientDifference = 0;
   }
   if( _outputGradientDifference != 0 )
   {
      delete _outputGradientDifference; 
      _outputGradientDifference = 0;
   }
   if( _hiddenWeightDifference != 0 )
   {
      delete _hiddenWeightDifference; 
      _hiddenWeightDifference = 0;
   }
   if( _outputWeightDifference != 0 )
   {
      delete _outputWeightDifference; 
      _outputWeightDifference = 0;
   }
} // end destructor 

//*****************************************************************************
// Class     : ModBackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : Setup
// Purpose   : Just does setup stuff.
//*****************************************************************************
void ModBackproP::Setup()
{
   _sumDeltaOutputErrorW = new VectoR( iNum_Hidden );
   _hiddenErrorSumDeltaOutputErrorW = new VectoR( iNum_Hidden );

   _hiddenGradientDifference = new MatriX( iNum_Hidden, iNum_Inputs );
   _outputGradientDifference = new MatriX( iNum_Outputs, iNum_Hidden );
   
   _hiddenWeightDifference = new MatriX( iNum_Hidden, iNum_Inputs );
   _outputWeightDifference = new MatriX( iNum_Outputs, iNum_Hidden );

   _hiddenGradientDifference->Zero_Out();
   _outputGradientDifference->Zero_Out();
   _hiddenWeightDifference->Zero_Out();
   _outputWeightDifference->Zero_Out();

   _hiddenLearningRate = _learningRate;
   _outputLearningRate = _learningRate;

} // end setup

//*****************************************************************************
// Class     : ModBackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : EncodeDataPair
// Purpose   : 'Shows' the input to the network interface, then propagates
//           : it's pattern to the output layer of neurons.
// Notes     : In the computation of the dot product, the first vector's
//           :  data gets destroyed.
//*****************************************************************************
int ModBackproP::EncodeDataPair()
{
int iWhichNeuron = 0;
long double ldActivation = 0;
long double temp = 0;
long double tan = 0;

//   FPRINT( "Encode" );

   *vInput = *vEncodeIn; // EncodeIn contains the training instance
   for( iWhichNeuron=0; iWhichNeuron<iNum_Hidden; iWhichNeuron++ )
   {
      ldActivation = 0;
      *vEncodeIn = mSynapseOne->pCol[iWhichNeuron]; // get the weights
      *vEncodeIn = *vEncodeIn * (*vInput); // computing dot product data gets destroyed
      ldActivation = vEncodeIn->GetSum();
      tan = tanhl( ldActivation );

      vHidden->pVariables[iWhichNeuron] = ((tan + 1 ) / 2); // map into [0,1]
      
      if( ldActivation > 11000 )
      {
         ldActivation = 11000;
      }
      temp = coshl( ldActivation );
      if( temp < 1e100 )
      {
         temp *= temp;
      }
      // temp = sech^2
      if( temp <= ldEpsilon )
      {
         temp = 1 / ldEpsilon;
      }
      else
      {
         temp = 1 / temp;
      }
      
      //vHiddenError->pVariables[iWhichNeuron] = (temp / 2);
      vHiddenError->pVariables[iWhichNeuron] = _momentum * temp;

   } // end for loop

   //Now for the output layer activations:
   for( iWhichNeuron=0; iWhichNeuron<iNum_Outputs; iWhichNeuron++ )
   {
      ldActivation = 0;
      *vEncodeOut = mSynapseTwo->pCol[iWhichNeuron];
      *vEncodeOut = *vEncodeOut * (*vHidden); // computing dot product
      ldActivation = vEncodeOut->GetSum();
      tan = tanhl( ldActivation );
      vOutput->pVariables[iWhichNeuron] = ((tan + 1 ) / 2);

      if( ldActivation > 11000 )
      {
         ldActivation = 11000;
      }
      temp = coshl( ldActivation );
      if( temp < 1e100 )
      {
         temp *= temp;
      }
      // temp = sech^2
      if( temp <= ldEpsilon )
      {
         temp = 1 / ldEpsilon;
      }
      else
      {
         temp = 1 / temp;
      }
      
      //vOutputError->pVariables[iWhichNeuron] = (temp / 2);
      vOutputError->pVariables[iWhichNeuron] = _momentum * temp;

   } // end for loop

   return 0;
} // end EncodeDataPair

//*****************************************************************************
// Class     : ModBackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : ComputeIntermediateValues
// Purpose   : Calculates some values that will be used inside the big loops;
//           : this serves to save valuable processor time.
// Notes     : 
//           :  
//*****************************************************************************
void ModBackproP::ComputeIntermediateValues()
{
//   FPRINT( "Intermediate" );

   // vErrorTemp holds the training output instance
   *vErrorTemp -= *vOutput; // This is delta_k
   ldThisError += vErrorTemp->GetSum(); // This is E_0
   *vErrorTemp = *vErrorTemp * (*vOutputError); // This is now delta_k * outputError

   for( int j=0; j<iNum_Hidden; j++ )
   {
      _sumDeltaOutputErrorW->pVariables[j] = 0;
      for( int k=0; k<iNum_Outputs; k++ )
      {
         _sumDeltaOutputErrorW->pVariables[j] += (vErrorTemp->pVariables[k] * mSynapseTwo->pCol[k][j]);
      } // end k for
      _hiddenErrorSumDeltaOutputErrorW->pVariables[j] = vHiddenError->pVariables[j] * _sumDeltaOutputErrorW->pVariables[j];
   } // end j for

} // end ComputeIntermediateValues

//*****************************************************************************
// Class     : ModBackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : UpdateConnectionWeights
// Purpose   : Here, we solve the linear jacobian system and then update the
//           : connection weights appropriately.
// Notes     : 
//           :  
//*****************************************************************************
void ModBackproP::UpdateConnectionWeights()
{
//   FPRINT( "Update" );
   long double grad = 0;

   // First, update the hidden layer:
   for( int j=0; j<iNum_Hidden; j++ )
   {
      for( int i=0; i<iNum_Inputs; i++ )
      {
         grad = (vInput->pVariables[i] * _hiddenErrorSumDeltaOutputErrorW->pVariables[j]);
         _hiddenGradientDifference->pCol[j][i] += (grad);

         mSynapseOne->pCol[j][i] += (_hiddenLearningRate * grad);

         _hiddenWeightDifference->pCol[j][i] += mSynapseOne->pCol[j][i];
      } // end i for
   } // end j for

   // Now, for the output layer:
   for( int k=0; k<iNum_Outputs; k++ )
   {
      for( int j=0; j<iNum_Hidden; j++ )
      {
         grad = (vHidden->pVariables[j] * vErrorTemp->pVariables[k]);

         _outputGradientDifference->pCol[k][j] += grad;

         mSynapseTwo->pCol[k][j] += (_outputLearningRate * grad);

         _outputWeightDifference->pCol[k][j] += mSynapseTwo->pCol[k][j];
      } // end j for
   } // end k for

} // end UpdateConnectionWeights

//*****************************************************************************
// Class     : ModBackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : CycleThruNet
// Purpose   : Runs the EncodeDataPair on the network for each datapair
//           : in the data file.
//*****************************************************************************
long double ModBackproP::CycleThruNet()
{
   FPRINT( "ModBackproP::CycleThruNet()" );
   long double ldSumSquared = 0;

   MatriX* previousHiddenGradient = new MatriX( _hiddenGradientDifference );
   MatriX* previousOutputGradient = new MatriX( _outputGradientDifference );
   MatriX* previousHiddenWeight = new MatriX( _hiddenWeightDifference );
   MatriX* previousOutputWeight = new MatriX( _outputWeightDifference );

   _hiddenGradientDifference->Zero_Out();
   _outputGradientDifference->Zero_Out();
   _hiddenWeightDifference->Zero_Out();
   _outputWeightDifference->Zero_Out();

   // this will all be a loop on the number of training instances
   for(int iCurrent_Instance=0;iCurrent_Instance<iNumber_Instances;iCurrent_Instance++)
   {
      *vEncodeIn = mTrainingInput->pCol[iCurrent_Instance]; // get the input instance
      EncodeDataPair();
      *vErrorTemp = mTrainingOutput->pCol[iCurrent_Instance]; // get the output instance
      ComputeIntermediateValues();
      UpdateConnectionWeights();
      ldSumSquared += Fct1Activation->Absolute(ldThisError);// * ldThisError;
   } // end for loop           
   ldThisError = 0;

   long double top = 0;
   long double bottom = 0;
   long double weightDiff = 0;

   // calculate hidden learning rate:
   for( int j=0; j<iNum_Hidden; j++ )
   {
      for( int i=0; i<iNum_Inputs; i++ )
      { 
         weightDiff = (_hiddenWeightDifference->pCol[j][i] - previousHiddenWeight->pCol[j][i]);
         top += (weightDiff * weightDiff);
         bottom += ( weightDiff * (_hiddenGradientDifference->pCol[j][i] - previousHiddenGradient->pCol[j][i]) );
      } // end i for
   } // end j for

   if( bottom != 0 )
   {
      _hiddenLearningRate = top / bottom;
      _hiddenLearningRate = Fct1Activation->Absolute( _hiddenLearningRate );
      if( _hiddenLearningRate > 1 )
      {
         _hiddenLearningRate = 1;
      }
   }

   FPRINT << "Hidden Rate: " << _hiddenLearningRate;

   top = 0;
   bottom = 0;
   weightDiff = 0;

   // calculate output learning rate:
   for( int k=0; k<iNum_Outputs; k++ )
   {
      for( int j=0; j<iNum_Hidden; j++ )
      { 
         weightDiff = (_outputWeightDifference->pCol[k][j] - previousOutputWeight->pCol[k][j]);
         top += (weightDiff * weightDiff);
         bottom += ( weightDiff * (_outputGradientDifference->pCol[k][j] - previousOutputGradient->pCol[k][j]) );
      } // end i for
   } // end j for

   if( bottom != 0 )
   {
      _outputLearningRate = top / bottom;
      _outputLearningRate = Fct1Activation->Absolute( _outputLearningRate );
      if( _outputLearningRate > 1 )
      {
         _outputLearningRate = 1;
      }
   }

   FPRINT << "Output Rate: " << _outputLearningRate;

   delete previousHiddenGradient;
   delete previousOutputGradient;
   delete previousHiddenWeight;
   delete previousOutputWeight;

   return ldSumSquared;
} // end CycleThruNet
