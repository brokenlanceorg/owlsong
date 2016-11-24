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
   if( _previousHiddenWeights != 0 )
   {
      delete _previousHiddenWeights; 
      _previousHiddenWeights = 0;
   }
   if( _previousOutputWeights != 0 )
   {
      delete _previousOutputWeights; 
      _previousOutputWeights = 0;
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
   _previousHiddenWeights = new MatriX( iNum_Hidden, iNum_Inputs );
   _previousOutputWeights = new MatriX( iNum_Outputs, iNum_Hidden );
   *_previousHiddenWeights = *mSynapseOne;
   *_previousOutputWeights = *mSynapseTwo;
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
      *vEncodeIn *= (*vInput); // computing dot product data gets destroyed
      ldActivation = vEncodeIn->GetSum();

//      FPRINT << "The sum is: " << ldActivation;

      tan = tanhl( ldActivation );

//      FPRINT << "The tan is: " << tan;

      vHidden->pVariables[iWhichNeuron] = ((tan + 1 ) / 2); // map into [0,1]
      
//      FPRINT << "The hidden is: " << vHidden->pVariables[iWhichNeuron];

      if( ldActivation > 11000 )
      {
//         FPRINT << "Reseting the activation...";
         ldActivation = 11000;
      }

      temp = coshl( ldActivation );

//      FPRINT << "The temp is: " << temp;

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
      
      vHiddenError->pVariables[iWhichNeuron] = (2 * temp);
//      FPRINT << "The hidden error is: " << vHiddenError->pVariables[iWhichNeuron];

      //vHiddenError->pVariables[iWhichNeuron] = _momentum * temp;

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

//      FPRINT << "The result for: " << iWhichNeuron << vOutput->pVariables[iWhichNeuron];

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
      
      vOutputError->pVariables[iWhichNeuron] = (2 * temp);
      //vOutputError->pVariables[iWhichNeuron] = _momentum * temp;

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
   ldThisError = vErrorTemp->GetSumSquared(); // This is E_0
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

   // First, update the hidden layer:
   for( int j=0; j<iNum_Hidden; j++ )
   {
      for( int i=0; i<iNum_Inputs; i++ )
      {
//         FPRINT << "before: " << mSynapseOne->pCol[j][i];
         mSynapseOne->pCol[j][i] += (_learningRate *
                                     vInput->pVariables[i] *
                                     _hiddenErrorSumDeltaOutputErrorW->pVariables[j]);
         mSynapseOne->pCol[j][i] += ( _momentum * (mSynapseOne->pCol[j][i] - _previousHiddenWeights->pCol[j][i]) );
//         FPRINT << "after: " << mSynapseOne->pCol[j][i];

         _previousHiddenWeights->pCol[j][i] = mSynapseOne->pCol[j][i];
      } // end i for
   } // end j for

   // Now, for the output layer:
   for( int k=0; k<iNum_Outputs; k++ )
   {
      for( int j=0; j<iNum_Hidden; j++ )
      {
//         FPRINT << "before: " << mSynapseTwo->pCol[k][j];
         mSynapseTwo->pCol[k][j] += (_learningRate *
                                     vHidden->pVariables[j] *
                                     vErrorTemp->pVariables[k]);
         mSynapseTwo->pCol[k][j] += ( _momentum * (mSynapseTwo->pCol[k][j] - _previousOutputWeights->pCol[k][j]) );
//         FPRINT << "after: " << mSynapseTwo->pCol[k][j];

         _previousOutputWeights->pCol[k][j] = mSynapseTwo->pCol[k][j];
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
//   FPRINT( "ModBackproP::CycleThruNet()" );
   long double ldSumSquared = 0;

   //FPRINT << "base name: " << pcBase_Name;
   //FPRINT << "number of instance: " << iNumber_Instances;

   // this will all be a loop on the number of training instances
   for(int iCurrent_Instance=0;iCurrent_Instance<iNumber_Instances;iCurrent_Instance++)
   {
//      FPRINT << "cycling through instance: " << iCurrent_Instance;
      *vEncodeIn = mTrainingInput->pCol[iCurrent_Instance]; // get the input instance
      EncodeDataPair();
//      FPRINT << "about to get output data and compute intermediate......";
      *vErrorTemp = mTrainingOutput->pCol[iCurrent_Instance]; // get the output instance
      ComputeIntermediateValues();
//      FPRINT << "about to update connection weights....";
      UpdateConnectionWeights();
      //ldSumSquared += Fct1Activation->Absolute(ldThisError);// * ldThisError;
      ldSumSquared += (ldThisError);// * ldThisError;
   } // end for loop           
   //ldThisError = 0;

   return ldSumSquared;
} // end CycleThruNet
