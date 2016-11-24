//***************************************************************************** 
// File    : BFGSNetwork.cpp
// Purpose : Contains the implementations for the BFGSNetworK class
// Author  : Brandon Benham
//*****************************************************************************
#include"BFGSNetwork.hpp"

//*****************************************************************************
// Class    : BFGSNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function : Default Constructor
// Purpose  : This class defines a BFGSNetworkagation network object
//          : which is derived from the NeuralneT abstract class.
// Notes    : 
//          : 
//          : 
//          : 
//          : 
//          :
//          : 
//*****************************************************************************
BFGSNetworK::BFGSNetworK( char* pcFile, unsigned short usAlive ) : BackproP( pcFile, usAlive )
{
   Setup();
} // end constructor

//*****************************************************************************
// Class    : BFGSNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function : Destructor
// Purpose  : This class defines a BFGSNetworkagation network object
//          : which is derived from the NeuralneT abstract class.
//*****************************************************************************
BFGSNetworK::~BFGSNetworK()
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
   if( _hiddenHessian != 0 )
   {
      delete _hiddenHessian; 
      _hiddenHessian = 0;
   }
   if( _outputHessian != 0 )
   {
      delete _outputHessian; 
      _outputHessian = 0;
   }
   if( _hiddenDirection != 0 )
   {
      delete _hiddenDirection; 
      _hiddenDirection = 0;
   }
   if( _outputDirection != 0 )
   {
      delete _outputDirection; 
      _outputDirection = 0;
   }
   if( _previousHiddenP != 0 )
   {
      delete _previousHiddenP; 
      _previousHiddenP = 0;
   }
   if( _previousOutputP != 0 )
   {
      delete _previousOutputP; 
      _previousOutputP = 0;
   }
} // end destructor 

//*****************************************************************************
// Class     : BFGSNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : Setup
// Purpose   : Just does setup stuff.
//*****************************************************************************
void BFGSNetworK::Setup()
{
   _outputSize = (iNum_Hidden * iNum_Outputs);
   _hiddenSize = (iNum_Hidden * iNum_Inputs);
   _sumDeltaOutputErrorW = new VectoR( iNum_Hidden );
   _hiddenErrorSumDeltaOutputErrorW = new VectoR( iNum_Hidden );
   _hiddenGradient = new VectoR( _hiddenSize );
   _outputGradient = new VectoR( _outputSize );
   _previousHiddenGradient = new VectoR( _hiddenSize );
   _previousOutputGradient = new VectoR( _outputSize );
   _hiddenDirection = new VectoR( _hiddenSize );
   _outputDirection = new VectoR( _outputSize );
   _previousHiddenP = new VectoR( _hiddenSize );
   _previousOutputP = new VectoR( _outputSize );
   _hiddenHessian = new MatriX( _hiddenSize, _hiddenSize );
   _outputHessian = new MatriX( _outputSize, _outputSize );
   _previousHiddenGradient->Zero_Out();
   _previousOutputGradient->Zero_Out();
   _hiddenIterations = 0;
   _outputIterations = 0;
} // end setup

//*****************************************************************************
// Class     : BFGSNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : EncodeDataPair
// Purpose   : 'Shows' the input to the network interface, then propagates
//           : it's pattern to the output layer of neurons.
// Notes     : In the computation of the dot product, the first vector's
//           :  data gets destroyed.
//*****************************************************************************
int BFGSNetworK::EncodeDataPair()
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
// Class     : BFGSNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : ComputeIntermediateValues
// Purpose   : Calculates some values that will be used inside the big loops;
//           : this serves to save valuable processor time.
// Notes     : 
//           :  
//*****************************************************************************
void BFGSNetworK::ComputeIntermediateValues()
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

   int position = 0;

   // Compute hidden gradient vector:
   for( int j=0; j<iNum_Hidden; j++ )
   {
      for( int i=0; i<iNum_Inputs; i++ )
      {  // changing from += to +
         _hiddenGradient->pVariables[position++] = (vInput->pVariables[i] *
                                                     _hiddenErrorSumDeltaOutputErrorW->pVariables[j]);
      } // end i for
   } // end j for

   position = 0;

   // Now, for the output layer:
   for( int k=0; k<iNum_Outputs; k++ )
   {
      for( int j=0; j<iNum_Hidden; j++ )
      {  // changing from += to +
         _outputGradient->pVariables[position++] = (vHidden->pVariables[j] *
                                                     vErrorTemp->pVariables[k]);
      } // end j for
   } // end k for
} // end ComputeIntermediateValues

//*****************************************************************************
// Class     : BFGSNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : UpdateConnectionWeights
// Purpose   : Here, we solve the linear jacobian system and then update the
//           : connection weights appropriately.
// Notes     : 
//           :  
//*****************************************************************************
void BFGSNetworK::UpdateConnectionWeights()
{
//   FPRINT( "Update" );

   int position = 0;
   // First, update the hidden layer:
   for( int j=0; j<iNum_Hidden; j++ )
   {
      for( int i=0; i<iNum_Inputs; i++ )
      {
         mSynapseOne->pCol[j][i] += (_learningRate *
                                     _hiddenDirection->pVariables[position++]);
      } // end i for
   } // end j for

   position = 0;
   // Now, for the output layer:
   for( int k=0; k<iNum_Outputs; k++ )
   {
      for( int j=0; j<iNum_Hidden; j++ )
      {
         mSynapseTwo->pCol[k][j] += (_learningRate *
                                     _outputDirection->pVariables[position++]);
      } // end j for
   } // end k for

} // end UpdateConnectionWeights

//*****************************************************************************
// Class     : BFGSNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : PerformBFGSMethod
// Purpose   : 
//           : 
// Notes     :  Later, we can break up the learning rate into two, one for
//           :  each layer when we optimize the learning rate.
//*****************************************************************************
void BFGSNetworK::PerformBFGSMethod()
{
   //FPRINT( "PerformBFGS" );

   // First, handle hidden layer:
   //if( (ulEpoch % _hiddenSize) == 0 )  // when do we do this without batching?
   if( (_hiddenIterations++ % _hiddenSize) == 0 )  // when do we do this without batching?
   {
      _hiddenHessian->Make_Identity();
      *_hiddenDirection = *_hiddenGradient;
   }
   else
   {
      VectoR* temp = 0;
      temp = _hiddenHessian->Product( _hiddenGradient );
      *_hiddenDirection = *temp;
      delete temp;
      _hiddenDirection->Normalize();
   }

   //_hiddenDirection->Print();
   
   VectoR* hiddenQ = 0;
   hiddenQ = _hiddenGradient->Subtract( _previousHiddenGradient );
   hiddenQ->Normalize();

   VectoR* hiddenP = new VectoR( _hiddenDirection );
   hiddenP->Scale( _learningRate );

   // not sure exactly what this is for
   VectoR* temp = 0;
/*
   temp = hiddenQ->Product( hiddenP );
   if( temp->GetSum() <= 0 )
   {
      *hiddenP = *_previousHiddenP;
      //FPRINT << "Setting hiddenP back to previous";
   }
   delete temp;
*/

   long double denom = 0;
   long double scalar = 0;

   // now, calculate the hessian:
   temp = hiddenP->Product( hiddenQ );
   denom = temp->GetSum();
   if( Fct1Activation->Absolute( denom ) <= Fct1Activation->GetEpsilon() )
   {
      FPRINT( "Caught a div by zero" );
   }
   else
   {
      denom = 1 / denom;
   }

   delete temp;
   temp = _hiddenHessian->Product( hiddenQ );

   MatriX* outerP = 0;
   outerP = hiddenP->OuterProduct( hiddenP );
   outerP->Scale( denom );

   MatriX* outerPQ = 0;
   outerPQ = hiddenP->OuterProduct( hiddenQ );

   VectoR* hessianQ = 0;
   hessianQ = _hiddenHessian->Product( hiddenQ );

   VectoR* QHessianQ = 0;
   QHessianQ = hiddenQ->Product( hessianQ );
   scalar = QHessianQ->GetSum();
   scalar *= denom;
   scalar += 1;
   outerP->Scale( scalar );

   MatriX* hessianQPOuter = 0;
   hessianQPOuter = hessianQ->OuterProduct( hiddenP );

   MatriX* PQOuterHessian = 0;
   PQOuterHessian = outerPQ->Product( _hiddenHessian );

   *hessianQPOuter += *PQOuterHessian;
   hessianQPOuter->Scale( denom );

   *outerP -= *hessianQPOuter;
   *_hiddenHessian += outerP;

   // capture previous data:
   *_previousHiddenGradient = *_hiddenGradient;
   *_previousHiddenP = *hiddenP;

   delete hiddenQ;
   delete hiddenP;
   delete temp;
   delete outerP;
   delete outerPQ;
   delete hessianQ;
   delete QHessianQ;
   delete hessianQPOuter;
   delete PQOuterHessian;


   // Next, handle output layer:
   //if( (ulEpoch % _outputSize) == 0 )
   if( (_outputIterations++ % _outputSize) == 0 )
   {
      _outputHessian->Make_Identity();
      *_outputDirection = *_outputGradient;
   }
   else
   {
      VectoR* temp = 0;
      temp = _outputHessian->Product( _outputGradient );
      *_outputDirection = *temp;
      delete temp;
      _outputDirection->Normalize();
   }

   VectoR* outputQ = 0; 
   outputQ = _outputGradient->Subtract( _previousOutputGradient );
   outputQ->Normalize();

   VectoR* outputP = new VectoR( _outputDirection );
   outputP->Scale( _learningRate );

   // not sure exactly what this is for
   temp = 0;
/*
   temp = outputQ->Product( outputP );
   if( temp->GetSum() <= 0 )
   {
      *outputP = *_previousOutputP;
      //FPRINT << "Setting outputP back to previous";
   }
   delete temp;
*/

   // now, calculate the hessian:
   temp = outputP->Product( outputQ );
   denom = temp->GetSum();
   if( Fct1Activation->Absolute( denom ) <= Fct1Activation->GetEpsilon() )
   {
      FPRINT( "Caught a div by zero" );
   }
   else
   {
      denom = 1 / denom;
   }

   delete temp;
   temp = _outputHessian->Product( outputQ );

   outerP = 0;
   outerP = outputP->OuterProduct( outputP );
   outerP->Scale( denom );

   outerPQ = 0;
   outerPQ = outputP->OuterProduct( outputQ );

   hessianQ = 0;
   hessianQ = _outputHessian->Product( outputQ );

   QHessianQ = 0;
   QHessianQ = outputQ->Product( hessianQ );
   scalar = QHessianQ->GetSum();
   scalar *= denom;
   scalar += 1;
   outerP->Scale( scalar );

   hessianQPOuter = 0;
   hessianQPOuter = hessianQ->OuterProduct( outputP );

   PQOuterHessian = 0;
   PQOuterHessian = outerPQ->Product( _outputHessian );

   *hessianQPOuter += *PQOuterHessian;
   hessianQPOuter->Scale( denom );

   *outerP -= *hessianQPOuter;
   *_outputHessian += outerP;

   // capture previous data:
   *_previousOutputGradient = *_outputGradient;
   *_previousOutputP = *outputP;

   delete outputQ;
   delete outputP;
   delete temp;
   delete outerP;
   delete outerPQ;
   delete hessianQ;
   delete QHessianQ;
   delete hessianQPOuter;
   delete PQOuterHessian;

   UpdateConnectionWeights();
}

//*****************************************************************************
// Class     : BFGSNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : CycleThruNet
// Purpose   : Runs the EncodeDataPair on the network for each datapair
//           : in the data file.
//*****************************************************************************
long double BFGSNetworK::CycleThruNet()
{
   //FPRINT( "BFGSNetworK::CycleThruNet()" );
   long double ldSumSquared = 0;

   // We're gonna use batching, so clear out the gradient vectors:
   //_hiddenGradient->Zero_Out();
   //_outputGradient->Zero_Out();

   // this will all be a loop on the number of training instances
   for(int iCurrent_Instance=0;iCurrent_Instance<iNumber_Instances;iCurrent_Instance++)
   {
      *vEncodeIn = mTrainingInput->pCol[iCurrent_Instance]; // get the input instance
      EncodeDataPair();
      *vErrorTemp = mTrainingOutput->pCol[iCurrent_Instance]; // get the output instance
      ComputeIntermediateValues();
      PerformBFGSMethod();
      //ldSumSquared += Fct1Activation->Absolute(ldThisError);// * ldThisError;
      ldSumSquared += (ldThisError);// * ldThisError;
   } // end for loop           
   ldThisError = 0;

   //FPRINT << ldSumSquared;

   return ldSumSquared;
} // end CycleThruNet
