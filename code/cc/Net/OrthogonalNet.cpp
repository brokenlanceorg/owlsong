//***************************************************************************** 
// File    : OrthogonalNet.cpp
// Purpose : Contains the implementations for the OrthogonalNeT class
// Author  : Brandon Benham
// Notes   : The algorithm for the training of these types of nets is as follows:
//         :
//         :  1) Create a random iNum_Instances x iNum_Hidden matrix.
//         :  2) Compute the QR factorization for this matrix.
//         :  3) Use Q as the basis vectors for the target h_j's
//         :  4) Create matrix hidden_targets which is tanh^-1( Q ).
//         :  5) For each j do:
//         :     6) Solve mInputs x W_j = hidden_targets_j
//         :        To speed calculations, just compute QR one time, then use
//         :        R back sub for each change in hidden_targets_j.
//         :     7) Set synapse_one_j = W_j.
//         :  8) Create matrix output_targets which is tanh^-1( mOutputs ).
//         :  9) For each k do:
//         :     10) Solve Q x W_k = output_targets_k
//         :        To speed calculations, just compute QR one time, then use
//         :        R back sub for each change in output_targets_k.
//         :     11) Set synapse_two_k = W_k
//         :  12) Pass data through to calculate delta. Hidden layer has modified
//         :      neurons that don't map into [0, 1], but map into [-1, 1]
//         :
//         : 
//*****************************************************************************
#include"OrthogonalNet.hpp"

//*****************************************************************************
// Class    : OrthogonalNeT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function : Default Constructor
// Purpose  : This class defines a OrthogonalNetagation network object
//          : which is derived from the NeuralneT abstract class.
// Notes    : 
//          : 
//          : 
//          : 
//          : 
//          :
//          : 
//*****************************************************************************
OrthogonalNeT::OrthogonalNeT( char* pcFile, unsigned short usAlive ) : BackproP( pcFile, usAlive )
{
   Setup();
} // end constructor

//*****************************************************************************
// Class    : OrthogonalNeT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function : Destructor
// Purpose  : This class defines a OrthogonalNetagation network object
//          : which is derived from the NeuralneT abstract class.
//*****************************************************************************
OrthogonalNeT::~OrthogonalNeT()
{
} // end destructor 

//*****************************************************************************
// Class     : OrthogonalNeT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : Setup
// Purpose   : Just does setup stuff.
//*****************************************************************************
void OrthogonalNeT::Setup()
{
} // end setup

//*****************************************************************************
// Class     : OrthogonalNeT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : EncodeDataPair
// Purpose   : 'Shows' the input to the network interface, then propagates
//           : it's pattern to the output layer of neurons.
// Notes     : In the computation of the dot product, the first vector's
//           :  data gets destroyed.
//*****************************************************************************
int OrthogonalNeT::EncodeDataPair()
{
int iWhichNeuron = 0;
long double ldActivation = 0;
long double temp = 0;
long double tan = 0;

   FPRINT( "Encode" );

   *vInput = *vEncodeIn; // EncodeIn contains the training instance
   for( iWhichNeuron=0; iWhichNeuron<iNum_Hidden; iWhichNeuron++ )
   {
      ldActivation = 0;
/*
      *vEncodeIn = mSynapseOne->pCol[iWhichNeuron]; // get the weights
      *vEncodeIn = *vEncodeIn * (*vInput); // computing dot product data gets destroyed
      ldActivation = vEncodeIn->GetSum();
*/
      for( int i=0; i<iNum_Inputs; i++ )
      {
         vEncodeIn->pVariables[i] *= mSynapseOne->pCol[iWhichNeuron][i];
      }
      ldActivation = vEncodeIn->GetSum();

      tan = tanhl( ldActivation );

//if( iWhichNeuron == 0 )
//{
   FPRINT << "activation: " << ldActivation;
   FPRINT << "tan:" << tan;
//}

      //vHidden->pVariables[iWhichNeuron] = ((tan + 1 ) / 2); // map into [0,1]
      vHidden->pVariables[iWhichNeuron] = tan; // map into [-1, 1]
      
/*
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
*/

   } // end for loop

   //Now for the output layer activations:
   for( iWhichNeuron=0; iWhichNeuron<iNum_Outputs; iWhichNeuron++ )
   {
      ldActivation = 0;
      *vEncodeOut = mSynapseTwo->pCol[iWhichNeuron];
      *vEncodeOut *= (*vHidden); // computing dot product
      ldActivation = vEncodeOut->GetSum();
//FPRINT << "activiation: " << ldActivation;
      tan = tanhl( ldActivation );
      vOutput->pVariables[iWhichNeuron] = ((tan + 1 ) / 2);

/*
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
*/

   } // end for loop

   return 0;
} // end EncodeDataPair

//*****************************************************************************
// Class     : OrthogonalNeT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : ComputeIntermediateValues
// Purpose   : Calculates some values that will be used inside the big loops;
//           : this serves to save valuable processor time.
// Notes     : 
//           :  
//*****************************************************************************
void OrthogonalNeT::ComputeIntermediateValues()
{
//   FPRINT( "Intermediate" );

   // vErrorTemp holds the training output instance
   *vErrorTemp -= *vOutput; // This is delta_k
   ldThisError += vErrorTemp->GetSum(); // This is E_0

} // end ComputeIntermediateValues

//*****************************************************************************
// Class     : OrthogonalNeT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function: SolveConnectionWeights
// Purpose : Here, we solve the linear jacobian system and then update the
//         : connection weights appropriately.
// Notes   : The algorithm for the training of these types of nets is as follows:
//         :
//         :  1) Create a random iNum_Instances x iNum_Hidden matrix.
//         :  2) Compute the QR factorization for this matrix.
//         :  3) Use Q as the basis vectors for the target h_j's
//         :  4) Create matrix hidden_targets which is tanh^-1( Q ).
//         :  5) For each j do:
//         :     6) Solve mInputs x W_j = hidden_targets_j
//         :        To speed calculations, just compute QR one time, then use
//         :        R back sub for each change in hidden_targets_j.
//         :     7) Set synapse_one_j = W_j.
//         :  8) Create matrix output_targets which is tanh^-1( mOutputs ).
//         :  9) For each k do:
//         :     10) Solve Q x W_k = output_targets_k
//         :        To speed calculations, just compute QR one time, then use
//         :        R back sub for each change in output_targets_k.
//         :     11) Set synapse_two_k = W_k
//         :  12) Pass data through to calculate delta. Hidden layer has modified
//         :      neurons that don't map into [0, 1], but map into [-1, 1]
//         :
//         : 
//*****************************************************************************
int OrthogonalNeT::SolveConnectionWeights()
{
   FPRINT( "SolveConnectionWeights" );

   // Compute QR factorization of a random matrix:
   MatriX* randomMatrix = new MatriX( iNumber_Instances, iNum_Hidden );
   randomMatrix->Fill_Rand();
   if( randomMatrix->ComputeMGS() == 0 )
   {
      FPRINT << "Couldn't solve random matrix, try again.";
      delete randomMatrix;
      return 1;
   }

   // Compute hidden_targets:
   MatriX* hiddenTargets = new MatriX( iNumber_Instances, iNum_Hidden );
   MatriX* hiddenTargetsQFactor = randomMatrix->GetQ();
   long double top = 0;
   long double bottom = 0;
   for( int i=0; i<iNumber_Instances; i++ )
   {
      for( int j=0; j<iNum_Hidden; j++ )
      {
         top = 1 + hiddenTargetsQFactor->pCol[i][j];
         bottom = 1 - hiddenTargetsQFactor->pCol[i][j];
         hiddenTargets->pCol[i][j] = logl( top / bottom );  // *might* get a div_by_zero
         hiddenTargets->pCol[i][j] /= 2;
//if( j == 0 )
//{
// This should equal values in encodeDataPair
FPRINT << "hidden target result: " << hiddenTargetsQFactor->pCol[i][j];
FPRINT << "hidden target activation: " << hiddenTargets->pCol[i][j];
//}
      }
   }

   // Solve mInputs:
   if( mTrainingInput->ComputeMGS() == 0 )
   {
      FPRINT << "Couldn't solve input matrix.";
      delete randomMatrix;
      delete hiddenTargets;
      return 1;
   }

   VectoR* solution = 0;
   VectoR* b = new VectoR( iNumber_Instances );
   for( int j=0; j<iNum_Hidden; j++ )
   {
      // build the b vector:
      for( int p=0; p<iNumber_Instances; p++ )
      {
         b->pVariables[p] = hiddenTargets->pCol[p][j];
      }
      solution = mTrainingInput->SolveMGS( b, false );
      if( solution == 0 )
      {
         FPRINT << "Couldn't solve input system j:" << j;
         delete b;
         delete randomMatrix;
         delete hiddenTargets;
         return 1;
      }


      // Set the connection weights:
      for( int i=0; i<iNum_Inputs; i++ )
      {
         mSynapseOne->pCol[j][i] = solution->pVariables[i];

      }

long double temp2 = 0;
for( int p=0; p<iNumber_Instances; p++ )
{
   temp2 = 0;
   for( int i=0; i<iNum_Inputs; i++ )
   {
      temp2 += (mTrainingInput->pCol[p][i] * solution->pVariables[i]);
   }
   FPRINT << "temp2: " << temp2;
   FPRINT << "should equal: " << b->pVariables[p];
}

      delete solution;
   }

//         :  8) Create matrix output_targets which is tanh^-1( mOutputs ).

   // Compute output_targets:
   MatriX* outputTargets = new MatriX( iNumber_Instances, iNum_Outputs );
   top = 0;
   bottom = 0;
   long double temp = 0;
   for( int i=0; i<outputTargets->cnRows; i++ )
   {
      for( int j=0; j<outputTargets->cnColumns; j++ )
      {
         temp = ((2 * mTrainingOutput->pCol[i][j]) - 1);
         top = 1 + temp;
         bottom = 1 - temp;
         outputTargets->pCol[i][j] = logl( top / bottom );  // *might* get a div_by_zero
         outputTargets->pCol[i][j] /= 2;
//FPRINT << "output target: " << outputTargets->pCol[i][j];
      }
   }

   // Solve hiddenTargets:
   if( hiddenTargetsQFactor->ComputeMGS() == 0 )
   {
      FPRINT << "Couldn't solve hidden targets matrix.";
      delete outputTargets;
      delete randomMatrix;
      delete hiddenTargets;
      return 1;
   }

//         :  9) For each k do:
//         :     10) Solve Q x W_k = output_targets_k
//         :        To speed calculations, just compute QR one time, then use
//         :        R back sub for each change in output_targets_k.
//         :     11) Set synapse_two_k = W_k

   solution = 0;
   for( int k=0; k<iNum_Outputs; k++ )
   {
      // build the b vector:
      for( int p=0; p<iNumber_Instances; p++ )
      {
         b->pVariables[p] = outputTargets->pCol[p][k];
      }
      solution = hiddenTargetsQFactor->SolveMGS( b, false );
if( k == 0 )
{
FPRINT << "hiddenTargetsQFactor" << hiddenTargetsQFactor->pCol[0][k];
}
      if( solution == 0 )
      {
         FPRINT << "Couldn't solve output system k:" << k;
         delete outputTargets;
         delete randomMatrix;
         delete hiddenTargets;
         return 1;
      }
      // Set the connection weights:
      for( int j=0; j<iNum_Hidden; j++ )
      {
         mSynapseTwo->pCol[k][j] = solution->pVariables[j];
      }
      delete solution;
   }

   delete b;
   delete hiddenTargets;
   delete outputTargets;
   delete randomMatrix;

   return 0;
} // end SolveConnectionWeights

//*****************************************************************************
// Class     : OrthogonalNeT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : CycleThruNet
// Purpose   : Runs the EncodeDataPair on the network for each datapair
//           : in the data file.
//*****************************************************************************
long double OrthogonalNeT::CycleThruNet()
{
   FPRINT( "OrthogonalNeT::CycleThruNet()" );
   long double ldSumSquared = 0;

   if( SolveConnectionWeights() )
   {
      // couldn't solve it.
      return 1000;
   }

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

   FPRINT << ldSumSquared;

   return ldSumSquared;
} // end CycleThruNet

//*****************************************************************************
// Class     : OrthogonalNeT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function  : CycleThruNet
// Purpose   : Runs the EncodeDataPair on the network for each datapair
//           : in the data file.
//*****************************************************************************
void OrthogonalNeT::TrainNet()
{
   FPRINT( "TrainNet" );

   long double error = CycleThruNet();

   FPRINT << error;
   usIsMature = 1;
}
