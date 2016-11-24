//***************************************************************************** 
// File    : NewtonNet.cpp
// Purpose : Contains the implementations for the NewtonNeT class
// Author  : Brandon Benham
//*****************************************************************************
#include"NewtonNet.hpp"

//*****************************************************************************
// Class    : NewtonNeT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function : Default Constructor
// Purpose  : This class defines a NewtonNetagation network object
//          : which is derived from the NeuralneT abstract class.
// Notes    : This class is basically a backprop class with a modified 
//          : training algorithm that uses Newton's method in the multivariable
//          : case to update the connection weights. We form the negative
//          : gradient vector as our vector F(x_bar). Then, we form the Jacobian
//          : of this vector which is, of course, a matrix J(x_bar). To find
//          : the update to the connection weights y, we solve the linear system
//          : J(x_bar) * y = F(x_bar).
//*****************************************************************************
NewtonNeT::NewtonNeT( char* pcFile, unsigned short usAlive ) : BackproP( pcFile, usAlive )
{
   Setup();
} // end constructor

//*****************************************************************************
// Class    : NewtonNeT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function : Destructor
// Purpose  : This class defines a NewtonNetagation network object
//          : which is derived from the NeuralneT abstract class.
//*****************************************************************************
NewtonNeT::~NewtonNeT()
{
   if( _hiddenSecondError != 0 )
   {
      delete _hiddenSecondError; 
      _hiddenSecondError = 0;
   }
   if( _outputSecondError != 0 )
   {
      delete _outputSecondError; 
      _outputSecondError = 0;
   }
   if( _outputErrorSquared != 0 )
   {
      delete _outputErrorSquared; 
      _outputErrorSquared = 0;
   }
   if( _inputHiddenError != 0 )
   {
      delete _inputHiddenError; 
      _inputHiddenError = 0;
   }
   if( _inputHiddenSecondError != 0 )
   {
      delete _inputHiddenSecondError; 
      _inputHiddenSecondError = 0;
   }
   if( _sumErrorSquaredWW != 0 )
   {
      delete _sumErrorSquaredWW; 
      _sumErrorSquaredWW = 0;
   }
   if( _sumDeltaOutputErrorW != 0 )
   {
      delete _sumDeltaOutputErrorW; 
      _sumDeltaOutputErrorW = 0;
   }
   if( _sumDeltaOutputSecondErrorW != 0 )
   {
      delete _sumDeltaOutputSecondErrorW; 
      _sumDeltaOutputSecondErrorW = 0;
   }
   if( _hiddenErrorSumDeltaOutputErrorW != 0 )
   {
      delete _hiddenErrorSumDeltaOutputErrorW; 
      _hiddenErrorSumDeltaOutputErrorW = 0;
   }
   if( _jacobianSynapaseOne != 0 )
   {
      delete _jacobianSynapaseOne; 
      _jacobianSynapaseOne = 0;
   }
   if( _jacobianSynapaseTwo != 0 )
   {
      delete _jacobianSynapaseTwo; 
      _jacobianSynapaseTwo = 0;
   }
} // end destructor 

//*****************************************************************************
// Class     : NewtonNeT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : Setup
// Purpose   : Just does setup stuff.
//*****************************************************************************
void NewtonNeT::Setup()
{
   int size = iNum_Hidden * iNum_Inputs;

   _hiddenSecondError = new VectoR( iNum_Hidden );
   _outputSecondError = new VectoR( iNum_Outputs );
   _outputErrorSquared = new VectoR( iNum_Outputs );
   _deltaOutputSecondError = new VectoR( iNum_Outputs );
   _sumDeltaOutputErrorW = new VectoR( iNum_Hidden );
   _sumDeltaOutputSecondErrorW = new VectoR( iNum_Hidden );
   _hiddenErrorSumDeltaOutputErrorW = new VectoR( iNum_Hidden );
   _inputHiddenError = new MatriX( iNum_Hidden, iNum_Inputs );
   _inputHiddenSecondError = new MatriX( iNum_Hidden, iNum_Inputs );
   _sumErrorSquaredWW = new MatriX( iNum_Hidden, iNum_Hidden );
   _jacobianSynapaseOne = new MatriX( size, size + 1 );
   size = iNum_Hidden * iNum_Outputs;
   _jacobianSynapaseTwo = new MatriX( size, size + 1 );
} // end setup

//*****************************************************************************
// Class     : NewtonNeT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : EncodeDataPair
// Purpose   : 'Shows' the input to the network interface, then propagates
//           : it's pattern to the output layer of neurons.
// Notes     : In the computation of the dot product, the first vector's
//           :  data gets destroyed.
//*****************************************************************************
int NewtonNeT::EncodeDataPair()
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

      //mSynapseOne->Print();

      *vEncodeIn *= (*vInput); // computing dot product data gets destroyed
      ldActivation = vEncodeIn->GetSum();
      tan = tanhl( ldActivation );

//      FPRINT << "Hidden, activation: " << ldActivation;
//      FPRINT << "Hidden, tan: " << tan;

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
      
//      FPRINT << "Hidden, temp: " << temp;

      //vHiddenError->pVariables[iWhichNeuron] = (temp / 2);
      vHiddenError->pVariables[iWhichNeuron] = temp;
      _hiddenSecondError->pVariables[iWhichNeuron] = (-1 * temp * tan);

//      FPRINT << "HiddenError: " << vHiddenError->pVariables[iWhichNeuron];
//      FPRINT << "HiddenSecondError: " << _hiddenSecondError->pVariables[iWhichNeuron];

/*
      vHidden->CheckForZeros();
      vHidden->CheckForInfinity();
      vHiddenError->CheckForZeros();
      vHiddenError->CheckForInfinity();
      _hiddenSecondError->CheckForZeros();
      _hiddenSecondError->CheckForInfinity();
*/
   } // end for loop

   //Now for the output layer activations:
   for( iWhichNeuron=0; iWhichNeuron<iNum_Outputs; iWhichNeuron++ )
   {
      ldActivation = 0;
      *vEncodeOut = mSynapseTwo->pCol[iWhichNeuron];
      *vEncodeOut *= (*vHidden); // computing dot product
      ldActivation = vEncodeOut->GetSum();
      tan = tanhl( ldActivation );
//      FPRINT << "Output, activation: " << ldActivation;
//      FPRINT << "Output, tan: " << tan;
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
      vOutputError->pVariables[iWhichNeuron] = temp;
      _outputSecondError->pVariables[iWhichNeuron] = (-1 * temp * tan);
      _outputErrorSquared->pVariables[iWhichNeuron] = (vOutputError->pVariables[iWhichNeuron] *
                                                       vOutputError->pVariables[iWhichNeuron]);
      
/*
      FPRINT << "Output, temp: " << temp;
      FPRINT << "OutputError: " << vOutputError->pVariables[iWhichNeuron];
      FPRINT << "OutputSecondError: " << _outputSecondError->pVariables[iWhichNeuron];
      FPRINT << "OutputErrorSquared: " << _outputErrorSquared->pVariables[iWhichNeuron];
      vOutput->CheckForZeros();
      vOutput->CheckForInfinity();
      vOutputError->CheckForZeros();
      vOutputError->CheckForInfinity();
      _outputSecondError->CheckForZeros();
      _outputSecondError->CheckForInfinity();
*/
   } // end for loop

   return 0;
} // end EncodeDataPair

//*****************************************************************************
// Class     : NewtonNeT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : ComputeIntermediateValues
// Purpose   : Calculates some values that will be used inside the big loops;
//           : this serves to save valuable processor time.
// Notes     : 
//           :  
//*****************************************************************************
void NewtonNeT::ComputeIntermediateValues()
{
//   FPRINT( "Intermediate" );

   // vErrorTemp holds the training output instance
   *vErrorTemp -= *vOutput; // This is delta_k
   ldThisError += vErrorTemp->GetSum(); // This is E_0
   for( int k=0; k<iNum_Outputs; k++ )
   { 
      _deltaOutputSecondError->pVariables[k] = _outputSecondError->pVariables[k] * vErrorTemp->pVariables[k];
   }
   *vErrorTemp *= (*vOutputError); // This is now delta_k * outputError
   //vErrorTemp->CheckForZeros();
   //_deltaOutputSecondError->CheckForZeros();

   for( int j=0; j<iNum_Hidden; j++ )
   {
      for( int i=0; i<iNum_Inputs; i++ )
      {
         _inputHiddenError->pCol[j][i] = vInput->pVariables[i] * vHiddenError->pVariables[j];
         _inputHiddenSecondError->pCol[j][i] = vInput->pVariables[i] * vHiddenError->pVariables[j];
      } // end i for
      for( int JJ=0; JJ<iNum_Hidden; JJ++ )
      {
         _sumErrorSquaredWW->pCol[JJ][j] = 0;
         for( int k=0; k<iNum_Outputs; k++ )
         {
            _sumErrorSquaredWW->pCol[JJ][j] += ( _outputErrorSquared->pVariables[k] *
                                                 mSynapseTwo->pCol[k][j] *
                                                 mSynapseTwo->pCol[k][JJ] );
         } // end k for
      } // end JJ for
      _sumDeltaOutputErrorW->pVariables[j] = 0;
      _sumDeltaOutputSecondErrorW->pVariables[j] = 0;
      for( int k=0; k<iNum_Outputs; k++ )
      {
         _sumDeltaOutputErrorW->pVariables[j] += (vErrorTemp->pVariables[k] * mSynapseTwo->pCol[k][j]);
         _sumDeltaOutputSecondErrorW->pVariables[j] += (_deltaOutputSecondError->pVariables[k] * mSynapseTwo->pCol[k][j]);
      } // end k for
      _hiddenErrorSumDeltaOutputErrorW->pVariables[j] = vHiddenError->pVariables[j] * _sumDeltaOutputErrorW->pVariables[j];
   } // end j for

} // end ComputeIntermediateValues

//*****************************************************************************
// Class     : NewtonNeT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : ConstructJacobian
// Purpose   : 
//           : 
// Notes     : 
//           :  
//*****************************************************************************
void NewtonNeT::ConstructJacobian()
{
   int position = 0;
   int rowPos = 0;
   int colPos = 0;

   // First, let's do the first synapse layer:
   for( int j=0; j<iNum_Hidden; j++ )
   {
      for( int i=0; i<iNum_Inputs; i++ )
      {
         _jacobianSynapaseOne->pCol[ position++ ][ _jacobianSynapaseOne->cnColumns-1 ] = 
           ( -1 * vInput->pVariables[i] * _hiddenErrorSumDeltaOutputErrorW->pVariables[j] );

         for( int JJ=0; JJ<iNum_Hidden; JJ++ )
         {
            for( int II=0; II<iNum_Inputs; II++ )
            {
               if( j == JJ )
               {
                  _jacobianSynapaseOne->pCol[ rowPos ][ colPos ] = (_inputHiddenSecondError->pCol[j][i] *
                                                                    vInput->pVariables[II] * 
                                                                    _sumDeltaOutputErrorW->pVariables[j]);
               }
               else
               {
                  _jacobianSynapaseOne->pCol[ rowPos ][ colPos ] = 0;
               }

               _jacobianSynapaseOne->pCol[ rowPos ][ colPos ] += (_inputHiddenError->pCol[j][i] *
                                                                  _inputHiddenError->pCol[JJ][II] *
                                                                  _sumErrorSquaredWW->pCol[JJ][j]);


               _jacobianSynapaseOne->pCol[ rowPos ][ colPos ] += (_inputHiddenError->pCol[j][i] *
                                                                  _inputHiddenError->pCol[JJ][II] *
                                                                  _sumDeltaOutputSecondErrorW->pVariables[JJ]);

               if( ++colPos == (_jacobianSynapaseOne->cnColumns - 1) )
               {
                  colPos = 0;
                  rowPos++;
               }
            } // end II for
         } // end JJ for
      } // end i for
   } // end j for

   position = 0;
   rowPos = 0;
   colPos = 0;

   // Next, let's do the second synapse layer:
   for( int k=0; k<iNum_Outputs; k++ )
   {
      for( int j=0; j<iNum_Hidden; j++ )
      {
         _jacobianSynapaseTwo->pCol[ position++ ][ _jacobianSynapaseTwo->cnColumns-1 ] = 
            ( -1 * vHidden->pVariables[j] * vErrorTemp->pVariables[k] );

         for( int KK=0; KK<iNum_Outputs; KK++ )
         {
            for( int JJ=0; JJ<iNum_Hidden; JJ++ )
            {
               if( KK == k )
               {
                  _jacobianSynapaseTwo->pCol[ rowPos ][ colPos ] = (vHidden->pVariables[j] *
                                                                    vHidden->pVariables[JJ] *
                                                                    _outputErrorSquared->pVariables[k]);
                  _jacobianSynapaseTwo->pCol[ rowPos ][ colPos ] += (vHidden->pVariables[j] *
                                                                    vHidden->pVariables[JJ] *
                                                                    _deltaOutputSecondError->pVariables[k]);
               } // end if
               else
               {
                  _jacobianSynapaseTwo->pCol[ rowPos ][ colPos ] = 0;
               } // end else

               if( ++colPos == (_jacobianSynapaseTwo->cnColumns - 1) )
               {
                  colPos = 0;
                  rowPos++;
               }
            } // end JJ for
         } // end KK for
      } // end j for
   } // end k for

} // end ConstructJacobian

//*****************************************************************************
// Class     : NewtonNeT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : UpdateConnectionWeights
// Purpose   : Here, we solve the linear jacobian system and then update the
//           : connection weights appropriately.
// Notes     : 
//           :  
//*****************************************************************************
void NewtonNeT::UpdateConnectionWeights()
{
//   FPRINT( "Update" );

   int position = 0;
   //_jacobianSynapaseOne->Scale( 1e25 );
   _jacobianSynapaseOne->Print();
   //_jacobianSynapaseTwo->Scale( 1e25 );
//   _jacobianSynapaseTwo->Print();
   VectoR* hiddenSolution = _jacobianSynapaseOne->Solve();
   VectoR* outputSolution = _jacobianSynapaseTwo->Solve();

//   FPRINT << hiddenSolution->GetSum();
//   FPRINT << outputSolution->GetSum();

   // First, update the hidden layer:
   for( int j=0; j<iNum_Hidden; j++ )
   {
      for( int i=0; i<iNum_Inputs; i++ )
      {
         mSynapseOne->pCol[j][i] += hiddenSolution->pVariables[position++];
      } // end i for
   } // end j for
   
   position = 0;
   // Now, for the output layer:
   for( int k=0; k<iNum_Outputs; k++ )
   {
      for( int j=0; j<iNum_Hidden; j++ )
      {
         mSynapseTwo->pCol[k][j] += outputSolution->pVariables[position++];
      } // end j for
   } // end k for

   delete hiddenSolution;
   delete outputSolution;
} // end UpdateConnectionWeights

//*****************************************************************************
// Class     : NewtonNeT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : CycleThruNet
// Purpose   : Runs the EncodeDataPair on the network for each datapair
//           : in the data file.
//*****************************************************************************
long double NewtonNeT::CycleThruNet()
{
   //FPRINT( "NewtonNeT::CycleThruNet()" );
   long double ldSumSquared = 0;

   // this will all be a loop on the number of training instances
   for(int iCurrent_Instance=0;iCurrent_Instance<iNumber_Instances;iCurrent_Instance++)
   {
      *vEncodeIn = mTrainingInput->pCol[iCurrent_Instance]; // get the input instance
      EncodeDataPair();
      *vErrorTemp = mTrainingOutput->pCol[iCurrent_Instance]; // get the output instance
      ComputeIntermediateValues();
      ConstructJacobian(); // vHiddenError contains error
      UpdateConnectionWeights();
      ldSumSquared += Fct1Activation->Absolute(ldThisError);// * ldThisError;
   } // end for loop           
   ldThisError = 0;

   return ldSumSquared;
} // end CycleThruNet
