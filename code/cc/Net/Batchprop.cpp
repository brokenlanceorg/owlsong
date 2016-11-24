//***************************************************************************** 
// File    : Batchprop.cpp
// Purpose : Contains the implementations for the BatchproP class
// Author  : Brandon Benham
//*****************************************************************************
#include"Batchprop.hpp"

//*****************************************************************************
// Class    : BatchproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function : Default Constructor
// Purpose  : This class defines a Batchpropagation network object
//          : which is derived from the NeuralneT abstract class.
// Notes    : 
//          : 
//          : 
//          : 
//          : 
//          :
//          : 
//*****************************************************************************
BatchproP::BatchproP( char* pcFile, unsigned short usAlive ) : BackproP( pcFile, usAlive )
{
   Setup();
} // end constructor

//*****************************************************************************
// Class    : BatchproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function : Destructor
// Purpose  : This class defines a Batchpropagation network object
//          : which is derived from the NeuralneT abstract class.
//*****************************************************************************
BatchproP::~BatchproP()
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
   if( _hiddenGradient != 0 )
   {
      delete _hiddenGradient; 
      _hiddenGradient = 0;
   }
   if( _outputGradient != 0 )
   {
      delete _outputGradient; 
      _outputGradient = 0;
   }
} // end destructor 

//*****************************************************************************
// Class     : BatchproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : Setup
// Purpose   : Just does setup stuff.
//*****************************************************************************
void BatchproP::Setup()
{
   _sumDeltaOutputErrorW = new VectoR( iNum_Hidden );
   _hiddenErrorSumDeltaOutputErrorW = new VectoR( iNum_Hidden );
   _hiddenGradient = new VectoR( iNum_Hidden * iNum_Inputs );
   _outputGradient = new VectoR( iNum_Hidden * iNum_Outputs );
} // end setup

//*****************************************************************************
// Class     : BatchproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : EncodeDataPair
// Purpose   : 'Shows' the input to the network interface, then propagates
//           : it's pattern to the output layer of neurons.
// Notes     : In the computation of the dot product, the first vector's
//           :  data gets destroyed.
//*****************************************************************************
int BatchproP::EncodeDataPair()
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
      *vEncodeIn *= (*vInput); // computing dot product data gets destroyed
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
      *vEncodeOut *= (*vHidden); // computing dot product
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
// Class     : BatchproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : ComputeIntermediateValues
// Purpose   : Calculates some values that will be used inside the big loops;
//           : this serves to save valuable processor time.
// Notes     : 
//           :  
//*****************************************************************************
void BatchproP::ComputeIntermediateValues()
{
//   FPRINT( "Intermediate" );

   // vErrorTemp holds the training output instance
   *vErrorTemp -= *vOutput; // This is delta_k
//   *vErrorTemp *= *vErrorTemp;
   ldThisError += vErrorTemp->GetSum(); // This is E_0
   *vErrorTemp *= (*vOutputError); // This is now delta_k * outputError

   for( int j=0; j<iNum_Hidden; j++ )
   {
      _sumDeltaOutputErrorW->pVariables[j] = 0;
      for( int k=0; k<iNum_Outputs; k++ )
      {
         _sumDeltaOutputErrorW->pVariables[j] += (vErrorTemp->pVariables[k] * mSynapseTwo->pCol[k][j]);
      } // end k for
      _hiddenErrorSumDeltaOutputErrorW->pVariables[j] = vHiddenError->pVariables[j] * _sumDeltaOutputErrorW->pVariables[j];
   } // end j for

   int position = 0;
   // First, update the hidden layer:
   for( int j=0; j<iNum_Hidden; j++ )
   {
      for( int i=0; i<iNum_Inputs; i++ )
      {
         _hiddenGradient->pVariables[position++] += (_learningRate *
                                                     vInput->pVariables[i] *
                                                     _hiddenErrorSumDeltaOutputErrorW->pVariables[j]);
      } // end i for
   } // end j for

   position = 0;
   // Now, for the output layer:
   for( int k=0; k<iNum_Outputs; k++ )
   {
      for( int j=0; j<iNum_Hidden; j++ )
      {
         _outputGradient->pVariables[position++] += (_learningRate *
                                                     vHidden->pVariables[j] *
                                                     vErrorTemp->pVariables[k]);
      } // end j for
   } // end k for

} // end ComputeIntermediateValues

//*****************************************************************************
// Class     : BatchproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : UpdateConnectionWeights
// Purpose   : Here, we solve the linear jacobian system and then update the
//           : connection weights appropriately.
// Notes     : 
//           :  
//*****************************************************************************
void BatchproP::UpdateConnectionWeights()
{
//   FPRINT( "Update" );

   int position = 0;
   // First, update the hidden layer:
   for( int j=0; j<iNum_Hidden; j++ )
   {
      for( int i=0; i<iNum_Inputs; i++ )
      {
         mSynapseOne->pCol[j][i] += _hiddenGradient->pVariables[position++];
      } // end i for
   } // end j for

   position = 0;
   // Now, for the output layer:
   for( int k=0; k<iNum_Outputs; k++ )
   {
      for( int j=0; j<iNum_Hidden; j++ )
      {
         mSynapseTwo->pCol[k][j] += _outputGradient->pVariables[position++];
      } // end j for
   } // end k for

} // end UpdateConnectionWeights

//*****************************************************************************
// Class     : BatchproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : CycleThruNet
// Purpose   : Runs the EncodeDataPair on the network for each datapair
//           : in the data file.
//*****************************************************************************
long double BatchproP::CycleThruNet()
{
   //FPRINT( "BatchproP::CycleThruNet()" );
   long double ldSumSquared = 0;

   _hiddenGradient->Zero_Out();
   _outputGradient->Zero_Out();

   // this will all be a loop on the number of training instances
   for(int iCurrent_Instance=0;iCurrent_Instance<iNumber_Instances;iCurrent_Instance++)
   {
      *vEncodeIn = mTrainingInput->pCol[iCurrent_Instance]; // get the input instance
      EncodeDataPair();
      *vErrorTemp = mTrainingOutput->pCol[iCurrent_Instance]; // get the output instance
      ComputeIntermediateValues();
      ldSumSquared += Fct1Activation->Absolute(ldThisError);// * ldThisError;
   } // end for loop           
   ldThisError = 0;

   UpdateConnectionWeights();

   return ldSumSquared;
} // end CycleThruNet
