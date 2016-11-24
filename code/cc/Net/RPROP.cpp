//***************************************************************************** 
// File    : RPROP.cpp
// Purpose : Contains the implementations for the RPROP class
// Author  : Brandon Benham
//*****************************************************************************
#include"RPROP.hpp"

//*****************************************************************************
// Class    : RPROP
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
RPROP::RPROP( char* pcFile, unsigned short usAlive ) : BackproP( pcFile, usAlive )
{
   Setup();
} // end constructor

//*****************************************************************************
// Class    : RPROP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function : Destructor
// Purpose  : This class defines a ModBackpropagation network object
//          : which is derived from the NeuralneT abstract class.
//*****************************************************************************
RPROP::~RPROP()
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
   if( _hiddenWeightRates != 0 )
   {
      delete _hiddenWeightRates; 
      _hiddenWeightRates = 0;
   }
   if( _outputWeightRates != 0 )
   {
      delete _outputWeightRates; 
      _outputWeightRates = 0;
   }
   if( _previousHiddenGradient != 0 )
   {
      delete _previousHiddenGradient; 
      _previousHiddenGradient = 0;
   }
   if( _previousOutputGradient != 0 )
   {
      delete _previousOutputGradient; 
      _previousOutputGradient = 0;
   }
   if( _prevPreviousHiddenWeights != 0 )
   {
      delete _prevPreviousHiddenWeights; 
      _prevPreviousHiddenWeights = 0;
   }
   if( _prevPreviousOutputWeights != 0 )
   {
      delete _prevPreviousOutputWeights; 
      _prevPreviousOutputWeights = 0;
   }
} // end destructor 

//*****************************************************************************
// Class     : RPROP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : Setup
// Purpose   : Just does setup stuff.
//*****************************************************************************
void RPROP::Setup()
{
   _sumDeltaOutputErrorW = new VectoR( iNum_Hidden );
   _hiddenErrorSumDeltaOutputErrorW = new VectoR( iNum_Hidden );
   _previousHiddenWeights = new MatriX( iNum_Hidden, iNum_Inputs );
   _previousOutputWeights = new MatriX( iNum_Outputs, iNum_Hidden );
   _previousHiddenGradient = new MatriX( iNum_Hidden, iNum_Inputs );
   _previousOutputGradient = new MatriX( iNum_Outputs, iNum_Hidden );
   _hiddenWeightRates = new MatriX( iNum_Hidden, iNum_Inputs );
   _outputWeightRates = new MatriX( iNum_Outputs, iNum_Hidden );
   _hiddenWeightRates->Fill_Ones();
   _outputWeightRates->Fill_Ones();
   _previousHiddenGradient->Fill_Ones();
   _previousOutputGradient->Fill_Ones();
   _prevPreviousHiddenWeights = new MatriX( iNum_Hidden, iNum_Inputs );
   _prevPreviousOutputWeights = new MatriX( iNum_Outputs, iNum_Hidden );
   _prevPreviousHiddenWeights->Zero_Out();
   _prevPreviousOutputWeights->Zero_Out();
   *_previousHiddenWeights = *mSynapseOne;
   *_previousOutputWeights = *mSynapseTwo;
} // end setup

//*****************************************************************************
// Class     : RPROP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : EncodeDataPair
// Purpose   : 'Shows' the input to the network interface, then propagates
//           : it's pattern to the output layer of neurons.
// Notes     : In the computation of the dot product, the first vector's
//           :  data gets destroyed.
//*****************************************************************************
int RPROP::EncodeDataPair()
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
      
      vHiddenError->pVariables[iWhichNeuron] = (2 * temp);
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
// Class     : RPROP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : ComputeIntermediateValues
// Purpose   : Calculates some values that will be used inside the big loops;
//           : this serves to save valuable processor time.
// Notes     : 
//           :  
//*****************************************************************************
void RPROP::ComputeIntermediateValues()
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

/*
void RPROP::UpdateConnectionWeights()
{
//   FPRINT( "Update" );

   // Save previous weights there's a better way than this:
   *_previousHiddenWeights = *mSynapseOne;
   *_previousOutputWeights = *mSynapseTwo;

   // First, update the hidden layer:
   for( int j=0; j<iNum_Hidden; j++ )
   {
      for( int i=0; i<iNum_Inputs; i++ )
      {
         mSynapseOne->pCol[j][i] += (_learningRate *
                                     vInput->pVariables[i] *
                                     _hiddenErrorSumDeltaOutputErrorW->pVariables[j]);
         mSynapseOne->pCol[j][i] += ( _momentum * (mSynapseOne->pCol[j][i] - _previousHiddenWeights->pCol[j][i]) );
      } // end i for
   } // end j for

   // Now, for the output layer:
   for( int k=0; k<iNum_Outputs; k++ )
   {
      for( int j=0; j<iNum_Hidden; j++ )
      {
         mSynapseTwo->pCol[k][j] += (_learningRate *
                                     vHidden->pVariables[j] *
                                     vErrorTemp->pVariables[k]);
         mSynapseTwo->pCol[k][j] += ( _momentum * (mSynapseTwo->pCol[k][j] - _previousOutputWeights->pCol[k][j]) );
      } // end j for
   } // end k for

} // end UpdateConnectionWeights
*/

//*****************************************************************************
// Class     : RPROP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : UpdateConnectionWeights
// Purpose   : Here, we solve the linear jacobian system and then update the
//           : connection weights appropriately.
// Notes     : 
//           :  
//*****************************************************************************
void RPROP::UpdateConnectionWeights()
{
   //FPRINT( "Update" );

   //FPRINT << "ldThis Error: " << ldThisError;

   // Save previous weights there's a better way than this:
   *_prevPreviousHiddenWeights = *_previousHiddenWeights;
   *_prevPreviousOutputWeights = *_previousOutputWeights;
   *_previousHiddenWeights = *mSynapseOne;
   *_previousOutputWeights = *mSynapseTwo;
   long double currentGradient = 0;
   long double gradientProduct = 0;
   long double weightUpdate = 0;

   // First, update the hidden layer:
   for( int j=0; j<iNum_Hidden; j++ )
   {
      for( int i=0; i<iNum_Inputs; i++ )
      {
         currentGradient = ( vInput->pVariables[i] * _hiddenErrorSumDeltaOutputErrorW->pVariables[j] );

         gradientProduct = ( currentGradient * _previousHiddenGradient->pCol[j][i] );

//if( i==0 && j==0 )
//{
//FPRINT << "current Grad: " << currentGradient;
//FPRINT << "Grad prod: " << gradientProduct;
//}

         if( gradientProduct > 0 )
         {
            _hiddenWeightRates->pCol[j][i] *= 1.2;
            if( _hiddenWeightRates->pCol[j][i] >= 50 )
            {
               _hiddenWeightRates->pCol[j][i] = 50;
            }
            weightUpdate = _hiddenWeightRates->pCol[j][i];
            if( currentGradient < 0 )
            {
               weightUpdate *= -1;
            }
            mSynapseOne->pCol[j][i] += weightUpdate;
//if( i==0 && j==0 )
//FPRINT << "case 1" << _hiddenWeightRates->pCol[j][i];
//FPRINT << "update" << weightUpdate;
         }
         else if( gradientProduct < 0 )
         {
            _hiddenWeightRates->pCol[j][i] *= 0.5;
            if( _hiddenWeightRates->pCol[j][i] <= 0.0001 )
            {
               _hiddenWeightRates->pCol[j][i] = 0.0001;
            }
            mSynapseOne->pCol[j][i] = _prevPreviousHiddenWeights->pCol[j][i];
            currentGradient = 0;
//if( i==0 && j==0 )
//FPRINT << "case 2" << _hiddenWeightRates->pCol[j][i];
//FPRINT << "update" << weightUpdate;
         }
         else if( gradientProduct == 0 )
         {
            weightUpdate = _hiddenWeightRates->pCol[j][i];
            if( currentGradient < 0 )
            {
               weightUpdate *= -1;
            }
            mSynapseOne->pCol[j][i] += weightUpdate;
//if( i==0 && j==0 )
//FPRINT << "case 3" << _hiddenWeightRates->pCol[j][i];
//FPRINT << "update" << weightUpdate;
         }
         _previousHiddenGradient->pCol[j][i] = currentGradient;
         mSynapseOne->pCol[j][i] += ( _momentum * (mSynapseOne->pCol[j][i] - _previousHiddenWeights->pCol[j][i]) );
      } // end i for
   } // end j for

   // Now, for the output layer:
   for( int k=0; k<iNum_Outputs; k++ )
   {
      for( int j=0; j<iNum_Hidden; j++ )
      {
/*
         mSynapseTwo->pCol[k][j] += (_learningRate *
                                     vHidden->pVariables[j] *
                                     vErrorTemp->pVariables[k]);
         mSynapseTwo->pCol[k][j] += ( _momentum * (mSynapseTwo->pCol[k][j] - _previousOutputWeights->pCol[k][j]) );
*/
         currentGradient = ( vHidden->pVariables[j] * vErrorTemp->pVariables[k]);

         gradientProduct = ( currentGradient * _previousOutputGradient->pCol[k][j] );

//if( k==0 && j==0 )
//FPRINT << "current grad: " << currentGradient;

         if( gradientProduct > 0 )
         {
            _outputWeightRates->pCol[k][j] *= 1.2;
            if( _outputWeightRates->pCol[k][j] >= 50 )
            {
               _outputWeightRates->pCol[k][j] = 50;
            }
            weightUpdate = _outputWeightRates->pCol[k][j];
            if( currentGradient < 0 )
            {
               weightUpdate *= -1;
            }
            mSynapseTwo->pCol[k][j] += weightUpdate;

//if( k==0 && j==0 )
//FPRINT << "case 1" << weightUpdate;


         }
         else if( gradientProduct < 0 )
         {
            _outputWeightRates->pCol[k][j] *= 0.5;
            if( _outputWeightRates->pCol[k][j] <= 0.0001 )
            {
               _outputWeightRates->pCol[k][j] = 0.0001;
            }
            mSynapseTwo->pCol[k][j] = _prevPreviousOutputWeights->pCol[k][j]; 
            currentGradient = 0;

//if( k==0 && j==0 )
//FPRINT << "case 2" << weightUpdate;

         }
         else if( gradientProduct == 0 )
         {
            weightUpdate = _outputWeightRates->pCol[k][j];
            if( currentGradient < 0 )
            {
               weightUpdate *= -1;
            }
            mSynapseTwo->pCol[k][j] += weightUpdate;

//if( k==0 && j==0 )
//FPRINT << "case 3" << weightUpdate;

         }
         _previousOutputGradient->pCol[k][j] = currentGradient;
         mSynapseTwo->pCol[k][j] += ( _momentum * (mSynapseTwo->pCol[k][j] - _previousOutputWeights->pCol[k][j]) );
      } // end j for
   } // end k for

} // end UpdateConnectionWeights

//*****************************************************************************
// Class     : RPROP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : CycleThruNet
// Purpose   : Runs the EncodeDataPair on the network for each datapair
//           : in the data file.
//*****************************************************************************
long double RPROP::CycleThruNet()
{
   //FPRINT( "RPROP::CycleThruNet()" );
   long double ldSumSquared = 0;

   // this will all be a loop on the number of training instances
   for(int iCurrent_Instance=0;iCurrent_Instance<iNumber_Instances;iCurrent_Instance++)
   {
      *vEncodeIn = mTrainingInput->pCol[iCurrent_Instance]; // get the input instance
      EncodeDataPair();
      *vErrorTemp = mTrainingOutput->pCol[iCurrent_Instance]; // get the output instance
      ComputeIntermediateValues();
      UpdateConnectionWeights();
      //ldSumSquared += Fct1Activation->Absolute(ldThisError);// * ldThisError;
      ldSumSquared += (ldThisError);// * ldThisError;
   } // end for loop           
   //ldThisError = 0;

   return ldSumSquared;
} // end CycleThruNet
