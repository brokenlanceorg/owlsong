//***************************************************************************** 
// File     :  Backprop.cpp
// Purpose  :  Contains the implementations for the BackproP class
// Author   :  Brandon Benham
//*****************************************************************************

#include"Backprop.hpp"
#include"DebugLogger.hpp"

//************************************************************************
// Class    :  BackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  Default Constructor
// Purpose  :  This class defines a Backpropagation network object
//          :  which is derived from the NeuralneT abstract class
// Notes    :  Yea right, we'll never use this one.
//************************************************************************
BackproP::BackproP() : NeuralneT(NULL)
{
   usIsAlive = 0; // the neural net hasn't been born yet
   vInput          = NULL;
   vOutput         = NULL;
   vHidden         = NULL;
   vEncodeIn       = NULL;
   vEncodeOut      = NULL;
   mSynapseOne     = 0;
   mSynapseTwo     = 0;
   mTrainingInput  = NULL;
   mTrainingOutput = NULL;
   pcBase_Name     = NULL;
   NumericalError  = NULL;
   Fct1Activation  = NULL;
   Fct1Derivative  = NULL;
} // end constructor

//************************************************************************
// Class    :  BackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  Destructor
// Purpose  :  This class defines a Backpropagation network object
//          :  which is derived from the NeuralneT abstract class
//************************************************************************
BackproP::~BackproP()
{
   SaveWeights();

   delete vInput;
   delete vOutput;
   delete vHidden;
   delete vEncodeIn;
   delete vEncodeOut;
   delete vOutputError;
   delete vHiddenError;
   delete vErrorTemp;
   delete mSynapseOne;
   delete mSynapseTwo;
   if(mTrainingInput != NULL)
   {
      delete mTrainingInput;
   }
   if(mTrainingOutput != NULL)
   {
      delete mTrainingOutput;
   }
   if(Fct1Activation != NULL)
   {
      delete Fct1Activation;
   }
   if(Fct1Derivative != NULL)
   {
      delete Fct1Derivative;
   }
   if( _synapseOneBackup != 0 )
   {
      delete _synapseOneBackup;
      _synapseOneBackup = 0;
   }
   if( _synapseTwoBackup != 0 )
   {
      delete _synapseTwoBackup;
      _synapseTwoBackup = 0;
   }

   delete[] pcBase_Name;
   delete NumericalError;
} // end destructor 

//************************************************************************
// Class    :  BackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  Constructor
// Purpose  :  This class defines a Backpropagation network object
//          :  which is derived from the NeuralneT abstract class
//************************************************************************
BackproP::BackproP(char* pcFileName, unsigned short usFirst) : NeuralneT(pcFileName)
{
   //FPRINT( "BackproP Constructor" );
   char* pcDot;
   char cDot = '.';

   usIsAlive = usFirst;
   pcBase_Name = new char[strlen(pcFileName) + 1];
   strcpy(pcBase_Name, pcFile_Name);
   pcDot = strchr(pcBase_Name, cDot);
   if(pcDot)
      pcBase_Name[strlen(pcBase_Name) - 3] = '\0';
   /*cout<<"input layer: "<<iNum_Inputs<<endl;
   cout<<"hidden layer: "<<iNum_Hidden<<endl;
   cout<<"output layer: "<<iNum_Outputs<<endl;
   cout<<"learning rate: "<<fLearning_Rate<<endl;
   cout<<"decay rate: "<<fDecay_Rate<<endl;
   cout<<"momentum: "<<fMomentum<<endl;
   cout<<"tolerance: "<<fTolerance<<endl; */

   //FPRINT << "iNUm_Inputs: " << iNum_Inputs;
   //FPRINT << "iNUm_Outputs: " << iNum_Outputs;
   //FPRINT << "iNUm_Hidden: " << iNum_Hidden;

   vInput  = new VectoR(iNum_Inputs);
   vHidden = new VectoR(iNum_Hidden);
   vOutput = new VectoR(iNum_Outputs);
   vEncodeIn  = new VectoR(iNum_Inputs); // same colunns as SynapseOne
   vEncodeOut = new VectoR(iNum_Hidden); // same columns as SynapseTwo
   vOutputError = new VectoR(iNum_Outputs);
   vHiddenError = new VectoR(iNum_Hidden);
   vErrorTemp = new VectoR(iNum_Outputs);
   // The matrices are considered: (row, column) such that row signifies
   // a single neuron in the layer which receives the synapse, and the
   // column signifies the input connection to this neuron from the neurons
   // below it
   mSynapseOne = new MatriX(iNum_Hidden, iNum_Inputs);
   mSynapseTwo = new MatriX(iNum_Outputs, iNum_Hidden);
   //mSynapseOne = 0;
   //mSynapseTwo = 0;
   //SetSynapseOne( new MatriX(iNum_Hidden, iNum_Inputs) );
   //SetSynapseTwo( new MatriX(iNum_Outputs, iNum_Hidden) );
   NumericalError = new NumerR(_UNDERFLOW_);
   Fct1Activation = new OnevariablE(_UNDERFLOW_);
   Fct1Derivative = new OnevariablE(_UNDERFLOW_);
   Setup();

   //FPRINT << "some synapses are: " << mSynapseOne->pCol[0][0];
   //FPRINT << "some synapses are: " << mSynapseOne->pCol[0][1];
   //FPRINT << "some synapses are: " << mSynapseOne->pCol[0][2];

} // end constructor

//************************************************************************
// Class    :  BackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  Setup
// Purpose  :  Just does setup stuff
//          :  
//************************************************************************
void BackproP::Setup()
{
   Fct1Derivative->SetEquation(3);
   Fct1Activation->SetEquation(2);
   Fct1Derivative->SetParams(1, 1, 1);
   Fct1Activation->SetParams(0, 1, 1);
   Fct1Derivative->SetLogging(0);
   Fct1Activation->SetLogging(0);
   ldEpsilon = Fct1Activation->GetEpsilon();
   vInput->Zero_Out();            
   vHidden->Zero_Out();
   vOutput->Zero_Out();
   vEncodeIn->Zero_Out();
   vEncodeOut->Zero_Out();
   mSynapseOne->Zero_Out();
   mSynapseTwo->Zero_Out();
   ReadEmperica();
   if(usIsAlive == 0)
   {
      mSynapseOne->Fill_Rand();
      mSynapseTwo->Fill_Rand();
      mSynapseOne->Scale( 0.5 );
      mSynapseTwo->Scale( 0.5 );
      mSynapseOne->OutputToFile( "synapseOne.txt" );
      mSynapseTwo->OutputToFile( "synapseTwo.txt" );
   } // end if not alive
   else
   {
      LoadWeights();
   }

   _performRollback = 0;
   _synapseOneBackup = 0;
   _synapseTwoBackup = 0;

} // end setup

//************************************************************************
// Class    :  BackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  SetActivationFunc
// Purpose  :  Sets the attribute function pointer to the parameter
//************************************************************************
void BackproP::SetActivationFunc(long double (*TheFunc)(long double ))
{
   TheActivationFunc = TheFunc;
} // end SetActivationFunc

//************************************************************************
// Class    :  BackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  SetActivationDerivative
// Purpose  :  Sets the attribute function pointer to the parameter
//************************************************************************
void BackproP::SetActivationDerivative(long double (*TheDxFunc)(long double ))
{
   TheDerivativeFunc = TheDxFunc;
} // end SetActivationFunc

//************************************************************************
// Class    :  BackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  ReadEmperica
// Purpose  :  Reads in from the disk the training instances to train
//          :  the network to
//************************************************************************
void BackproP::ReadEmperica()
{
//   FPRINT( "BackproP::ReadEmperica" );
   char pcTheDataFile[13];
   char* theWord = 0;
   int iFillRes = 0;

   // generate the file name:
   pcTheDataFile[0] = '\0';
   strcpy(pcTheDataFile, pcBase_Name);
   strcat(pcTheDataFile, "fac");

   FilereadeR* theFile = new FilereadeR( pcTheDataFile );

   theWord = theFile->GetNextWord();

   iNumber_Instances = atoi( theWord );

   mTrainingInput = new MatriX( iNumber_Instances, iNum_Inputs );
   mTrainingOutput = new MatriX( iNumber_Instances, iNum_Outputs );
   mTrainingInput->Zero_Out();
   mTrainingOutput->Zero_Out();

   iFillRes = FillTrainMatrices( theFile ); 

   delete theFile;
} // end ReadEmperica

//************************************************************************
// Class    :  BackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  FillTrainMatrices
// Purpose  :  Fills the mTrainingInput/output matrices from the data in
//          :  the fact file. Called by ReadEmperica.
//************************************************************************
int BackproP::FillTrainMatrices( FilereadeR* theFile )
{
//   FPRINT( "BackproP::FillTrainMatrices" );
   int iFillRes = 0;
   int iFillLoop = 0;
   int iIn = 0;
   int iOut = 0;
   long double ldTemp = 0;
   char* theWord = 0;

   for( iFillLoop=0; iFillLoop<iNumber_Instances; iFillLoop++ )
   {
      for( iIn=0; iIn<iNum_Inputs; iIn++ )
      {
         theWord = theFile->GetNextWord();
         ldTemp = strtold( theWord, (char**)NULL );
//         FPRINT << "an input:" << ldTemp;
         mTrainingInput->pCol[iFillLoop][iIn] = ldTemp;
      } // end for loop inputs
      ldTemp = 0;
      for( iOut=0; iOut<iNum_Outputs; iOut++ )
      {
         theWord = theFile->GetNextWord();
         ldTemp = strtold( theWord, (char**)NULL );
//         FPRINT << "an output:" << ldTemp;
         mTrainingOutput->pCol[iFillLoop][iOut] = ldTemp;
      } // end for loop outputs
   } // end outer for loop
   return iFillRes;
} // end FillTrainMatrices

//************************************************************************
// Class    :  BackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  SaveWeights
// Purpose  :  Saves our weight matrices to disk so that we may resume
//          :  training or running at a later date.
// Notes    :  Since it is the case that we already know the size of
//          :  these matrices, there is no need to write or read this
//          :  data from the disk. Thus, we use the iNum_Inputs and 
//          :  iNum_Outputs which are garnered from the def file.
//************************************************************************
int BackproP::SaveWeights()
{
   char acWghtName[13];
   int iOuter = 0;
   int iInner = 0;

   strcpy(acWghtName, pcBase_Name);
   strcat(acWghtName, "wts");

   fstream theFile( acWghtName, ios::out | ios::binary );
   // The matrices are considered: (row, column) such that row signifies
   // a single neuron in the layer which receives the synapse, and the
   // column signifies the input connection to this neuron from the neurons
   // below it
   // mSynapseOne = new MatriX(iNum_Hidden, iNum_Inputs);
   // mSynapseTwo = new MatriX(iNum_Outputs, iNum_Hidden);
   for( iOuter=0; iOuter<iNum_Hidden; iOuter++ )
      for( iInner=0; iInner<iNum_Inputs; iInner++ )
      {
         theFile.write( (char*)&mSynapseOne->pCol[iOuter][iInner], sizeof( long double ) );
      }

   for( iOuter=0; iOuter<iNum_Outputs; iOuter++ )
      for( iInner=0; iInner<iNum_Hidden; iInner++ )
      {
         theFile.write( (char*)&mSynapseTwo->pCol[iOuter][iInner], sizeof( long double ) );
      } // end inner for loop
   theFile.close();

   return 0;
} // end SaveWeights

//************************************************************************
// Class    :  BackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  LoadWeights
// Purpose  :  Pulls the weight data into memory so that we can resume
//          :  training or running of the network
// Notes    :  Since it is the case that we already know the size of
//          :  these matrices, there is no need to write or read this
//          :  data from the disk. Thus, we use the iNum_Inputs and 
//          :  iNum_Outputs which are garnered from the def file.
//************************************************************************
int BackproP::LoadWeights()
{
   char acWeightName[13];
   int iOuty = 0;
   int iInny = 0;
                   
   strcpy(acWeightName, pcBase_Name);
   strcat(acWeightName, "wts");
   fstream theFile( acWeightName, ios::in | ios::binary );

   for( iOuty=0; iOuty<iNum_Hidden; iOuty++ )
      for( iInny=0; iInny<iNum_Inputs; iInny++ )
      {
         // mSynapseOne(iNum_Hidden, iNum_Inputs); 
         theFile.read( (char*)&mSynapseOne->pCol[iOuty][iInny], sizeof( long double ) );
      } // end inner for loop

   for( iOuty=0; iOuty<iNum_Outputs; iOuty++ )
      for( iInny=0; iInny<iNum_Hidden; iInny++ )
      {
         theFile.read( (char*)&mSynapseTwo->pCol[iOuty][iInny], sizeof( long double ) );
      } // end inner for loop
   theFile.close();

   return 0;
} // end LoadWeights

//************************************************************************
// Class    :  BackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  EncodeDataPair
// Purpose  :  'Shows' the input to the network interface, then propagates
//          :  it's pattern to the output layer of neurons.
// Notes    :  In the computation of the dot product, the first vector's
//          :  data gets destroyed.
//************************************************************************
int BackproP::EncodeDataPair()
{
int iEncodeRes = 0;
int iWhichNeuron = 0;
long double ldActivation = 0;
long double temp = 0;

   // vInput(iNum_Inputs); vEncodeIn(iNum_Inputs);
   //
   *vInput = *vEncodeIn; // EncodeIn contains the training instance
   for(iWhichNeuron=0;iWhichNeuron<iNum_Hidden;iWhichNeuron++)
   {
      ldActivation = 0;
      // vEncodeIn(iNum_Inputs); mSynapseOne(iNum_Hidden, iNum_Inputs);
/*    cout<<"the weight matrix is: \n"<<*mSynapseOne<<endl;
      getch(); */
      *vEncodeIn = mSynapseOne->pCol[iWhichNeuron]; // get the weights
//    cout<<"the weights are: \n"<<*vEncodeIn<<endl;
      // vInput(iNum_Inputs); vEncodeIn(iNum_Inputs);
      *vEncodeIn *= (*vInput); // computing dot product
//    cout<<"Activation vec is: \n"<<*vEncodeIn<<endl;

      ldActivation = vEncodeIn->GetSum();
      // compute the actual neural result:
      /*vHidden->pVariables[iWhichNeuron] = TheActivationFunc(ldActivation);
      vHiddenError->pVariables[iWhichNeuron] = TheDerivativeFunc(ldActivation);*/

                /*
      Fct1Activation->SetVariable( ldActivation );
      Fct1Derivative->SetVariable( ldActivation );
                */

//    cout<<"i(20): "<<iWhichNeuron<<" Activation is: "<<ldActivation<<endl;
      // vHidden(iNum_Hidden); iWhichNeuron <= iNum_Hidden

//    vHidden->pVariables[iWhichNeuron] = Fct1Activation->EvaluateIt();
                vHidden->pVariables[iWhichNeuron] = ((tanhl( ldActivation ) + 1 ) / 2);

      // vHiddenError(iNum_Hidden); iWhichNeuron <= iNum_Hidden

//    vHiddenError->pVariables[iWhichNeuron] = Fct1Derivative->EvaluateIt();
                if( ldActivation > 11000 )
                {
                   ldActivation = 11000;
                }
                temp = coshl( ldActivation );
                if( temp < 1e100 )
                {
                   temp *= temp;
                }
                if( temp <= ldEpsilon )
                {
                   temp = 1 / ldEpsilon;
                }
                else
                {
                   temp = 1 / temp;
                }

                vHiddenError->pVariables[iWhichNeuron] = temp;

      vHidden->CheckForZeros();
      vHidden->CheckForInfinity();
      vHiddenError->CheckForZeros();
      vHiddenError->CheckForInfinity();
      // vEncodeIn still contains the activations
   } // end for loop
   iWhichNeuron = 0;
   //Now for the output layer activations:
   for(iWhichNeuron=0;iWhichNeuron<iNum_Outputs;iWhichNeuron++)
   {
      ldActivation = 0;
      // vEncodeOut(iNum_Hidden);
      // mSynapseTwo(iNum_Outputs, iNum_Hidden);
      *vEncodeOut = mSynapseTwo->pCol[iWhichNeuron];
//    cout<<"Weights: "<<*vEncodeOut;
      // vEncodeOut(iNum_Hidden); vHidden(iNum_Hidden); 
      *vEncodeOut *= (*vHidden); // computing dot product
//    cout<<"Input * Weights: "<<*vEncodeOut;
      // vEncodeOut(iNum_Hidden); iTmp2<=iNum_Hidden
/*    cout<<"EncodeOut is: \n"<<*vEncodeOut<<endl;
      getch(); */
      ldActivation = vEncodeOut->GetSum();
      // compute the actual neural result:
      /*vOutput->pVariables[iWhichNeuron] = TheActivationFunc(ldActivation);
      vOutputError->pVariables[iWhichNeuron] = TheDerivativeFunc(ldActivation);*/

                /*
      Fct1Activation->SetVariable( ldActivation );
      Fct1Derivative->SetVariable( ldActivation );
                */

      // vOutput(iNum_Outputs); iWhichNeuron <= iNum_Outputs

//    vOutput->pVariables[iWhichNeuron] = Fct1Activation->EvaluateIt();
                vOutput->pVariables[iWhichNeuron] = ((tanhl( ldActivation ) + 1 ) / 2);

      // vOutputError(iNum_Outputs); iWhichNeuron <= iNum_Outputs

//    vOutputError->pVariables[iWhichNeuron] = Fct1Derivative->EvaluateIt();
                if( ldActivation > 11000 )
                {
                   ldActivation = 11000;
                }
                temp = coshl( ldActivation );
                if( temp < 1e100 )
                {
                   temp *= temp;
                }
                if( temp <= ldEpsilon )
                {
                   temp = 1 / ldEpsilon;
                }
                else
                {
                   temp = 1 / temp;
                }

                vOutputError->pVariables[iWhichNeuron] = temp;

      vOutput->CheckForZeros();
      vOutput->CheckForInfinity();
      vOutputError->CheckForZeros();
      vOutputError->CheckForInfinity();
      // vEncodeOut still contains the activations
   } // end for loop

   return iEncodeRes;
} // end EncodeDataPair

//************************************************************************
// Class    :  BackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  Recall
// Purpose  :  'Shows' an input pattern to the network, and then generates
//          :  a corresponding output based on the current state of the
//          :  weight matrices.
//          :  After execution, the vector vOutput will contain the output
//          :  activations.
//************************************************************************
int BackproP::Recall(VectoR* vInVector)
{
int iRecallRes = 0;

   *vEncodeIn = *vInVector;         // EncodeDataPair used vEncodeIn
   iRecallRes = EncodeDataPair();   // vOutput now contains the outputs

   return iRecallRes;
} // end Recal

//************************************************************************
// Class    :  BackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  ComputeOutputError
// Purpose  :  Calculates the error vector for the output layer.
//          :  
// Notes    :  The vectors vHiddenError and vOutputError will contain
//          :  the activations evaluated by the derivative function
//          :  upon entry, and vErrorTemp contains the 'real' output
//          :  instance.
//          :  Upon exit, vErrorTemp contains error for output layer
//************************************************************************
void BackproP::ComputeOutputError()
{
        // vErrorTemp holds the training output instance
   *vErrorTemp -= *vOutput;
   ldThisError += vErrorTemp->GetSum();
   *vErrorTemp *= (*vOutputError);
   vErrorTemp->CheckForZeros();
   /*
   for(iNerr=0;iNerr<iNum_Outputs;iNerr++)
   {
      if(Fct1Activation->Absolute(vErrorTemp->pVariables[iNerr]) < ldEpsilon)
      {
         cout<<"Setting to zero...."<<endl;
         vErrorTemp->pVariables[iNerr] = 0; // error checking
      }
      ldThisError += vErrorTemp->pVariables[iNerr];
   } // end for loop */
} // end ComputeOutputError

//************************************************************************
// Class    :  BackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  ComputeHiddenError
// Purpose  :  Calculates the error vector for the hidden layer
//          :  
// Notes    :  The vector vErrorTemp will contain the output layer's error
//          :  Upon exit, vHiddenError contains error for hidden layer
//************************************************************************
void BackproP::ComputeHiddenError()
{
int iLoop1 = 0;
int iLoop2 = 0;
long double ldtheError = 0;

   for(iLoop2=0;iLoop2<iNum_Hidden;iLoop2++)
   {
      for(iLoop1=0;iLoop1<iNum_Outputs;iLoop1++)
      {
         vOutputError->pVariables[iLoop1] = vErrorTemp->pVariables[iLoop1] * 
            mSynapseTwo->pCol[iLoop1][iLoop2];
         ldtheError += vOutputError->pVariables[iLoop1];
      } // end for loop1
      vHiddenError->pVariables[iLoop2] *= ldtheError;
      ldtheError = 0;
   } // end for loop2
} // end ComputeHiddenError

//************************************************************************
// Class    :  BackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  UpdateOutputWeights
// Purpose  :  Updates the connection weights for the synaptic layer between
//          :  the hidden layer and the output layer
// Notes    :  Upon Entry, vErrorTemp contains error info for outputs
//          :  
//************************************************************************
void BackproP::UpdateOutputWeights()
{
int iOuter1 = 0;
int iInner1 = 0;

   // vErrorTemp->Scale(ldLearning);
   // vHidden(iNum_Hidden); vErrorTemp(iNum_Outputs);
   for(iOuter1=0;iOuter1<iNum_Outputs;iOuter1++)
      for(iInner1=0;iInner1<iNum_Hidden;iInner1++)
      {
         mSynapseTwo->pCol[iOuter1][iInner1] += _learningRate *
            vErrorTemp->pVariables[iOuter1] * vHidden->pVariables[iInner1];
      } // end inner for loop
} // end UpdateOutputWeights

//************************************************************************
// Class    :  BackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  UpdateHiddenWeights
// Purpose  :  Updates the connection weights for the synaptic layer between
//          :  the input layer and the hidden layer
// Notes    :  Upon Entry, the vec vHiddenError contains the necessary
//          :  error change info.
//************************************************************************
void BackproP::UpdateHiddenWeights()
{
int iOuter = 0;
int iInner = 0;

   // mSynapseOne(iNum_Hidden, iNum_Inputs);
   // vHiddenError(iNum_Hidden); VectoR(iNum_Inputs);
   for(iOuter=0;iOuter<iNum_Hidden;iOuter++)
      for(iInner=0;iInner<iNum_Inputs;iInner++)
      {
         mSynapseOne->pCol[iOuter][iInner] += _learningRate *
            vHiddenError->pVariables[iOuter] * vInput->pVariables[iInner];
      } // end inner for loop
} // end UpdateHiddenWeights

//************************************************************************
// Class    :  BackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  CycleThruNet
// Purpose  :  Runs the EncodeDataPair on the network for each datapair
//          :  in the data file.
//************************************************************************
long double BackproP::CycleThruNet()
{
//FPRINT( "BackproP::CycleThruNet()" );
long double ldSumSquared = 0;

   // this will all be a loop on the number of training instances
   for(int iCurrent_Instance=0;iCurrent_Instance<iNumber_Instances;iCurrent_Instance++)
   {
//      FPRINT << "iCurrent_Instance is: " << iCurrent_Instance;
      *vEncodeIn = mTrainingInput->pCol[iCurrent_Instance]; // get the input instance
//      FPRINT << "Got the input instance......";
      EncodeDataPair();
//      FPRINT << "Did the EncodeDataPair......";
      *vErrorTemp = mTrainingOutput->pCol[iCurrent_Instance]; // get the output instance
//      FPRINT << "Got the output instance......";
      ComputeOutputError(); // vOutputError contains error
//      FPRINT << "Computed the Output Error......";
      ComputeHiddenError(); // vHiddenError contains error
//      FPRINT << "Computed the Hidden Error......";
      UpdateHiddenWeights(); // later, add momentum adjustment
//      FPRINT << "Updated the Hidden weights......";
      UpdateOutputWeights(); // later, add momentum adjustment
//      FPRINT << "Updated the Output weights......";
      ldSumSquared += Fct1Activation->Absolute(ldThisError);// * ldThisError;
//      FPRINT << "The sumSquared is:" << ldSumSquared;
   } // end for loop           
   ldThisError = 0;

   return ldSumSquared;
} // end CycleThruNet

//************************************************************************
// Class    :  BackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  CheckProgress
// Purpose  :  This method will check the current progress and adjust the
//          :  learning rate appropriately.
// Notes    :  
//          :  
//************************************************************************
void BackproP::CheckProgress( float fError )
{
   if( ulEpoch > 80000 && fError > .0001 )
   {
      if( _learningRate >= 7 )
        return;
      else
        _learningRate += 1;
   } /*else if( fError < .0001 && fError > fTolerance )
   {
      fLearning_Rate = .5;
   } // end if  */
} // end CheckProgress

//************************************************************************
// Class    :  BackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  TrainNet
// Purpose  :  The function we call to begin, or restart, the training
//          :  process for a given data set.
// Notes    :  At this point, we assume all matrices and vectors contain
//          :  the necessary data to begin the training process.
//************************************************************************
void BackproP::TrainNet()
{
FPRINT( "TrainNet" );

   iCurrent_Instance = 0; // on first training instance
   while( 1 ) // Not sure how to check for user-quit
   {
      _error = CycleThruNet(); // cycle all test patterns thru net one time
      FPRINT << _error;
      ulEpoch += 1; // One time thru net for all patterns is one epoch
      if( ulEpoch % 1000 == 0 )
      {
         if( _quit == true )
         {
            break;
         }
      }

      if( _maxEpoch != 0 &&
          ulEpoch >= _maxEpoch )
      {
         break;
      }

      if( _error <= fTolerance )
      {
         usIsMature = 1; // the only case in which network is trained
         break;
      } // end if
   } // end while loop
             
} // end TrainNet

//************************************************************************
// Class    :  BackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  TestNet
// Purpose  :  Tests the net on a given set of data and calculates the
//          :  sum-squared error.
//************************************************************************
float BackproP::TestNet()
{
   FPRINT( "TestNet" );
   float fTestResult = 0;
   int iLoop = 0;
   int iDo;
   char acTempBuff[164];
   MatriX* theMatrix = new MatriX( iNumber_Instances, iNum_Hidden );
   VectoR* theVector = new VectoR( iNumber_Instances );
   VectoR* theVec = 0;


   for(iLoop=0;iLoop<iNumber_Instances;iLoop++)
   {
      // mTrainingOuput(iNumber_Instances, iNum_Outputs);
      // vInput(iNum_Inputs); vHidden(iNum_Hidden);
      // vOutput(iNum_Outputs); vEncodeOut(iNum_Hidden);
      // vOutputError(iNum_Outputs); vHiddenError(iNum_Hidden);
      // vErrorTemp(iNum_Outputs);
      // mSynapseOne(iNum_Hidden, iNum_Inputs); 
      // mSynapseTwo(iNum_Outputs, iNum_Hidden);
      // vEncodeIn(iNum_Inputs); mTrainingInput(iNumber_Instances, iNum_Inputs);
      *vEncodeIn = mTrainingInput->pCol[iLoop]; // get the input instance
      
      FPRINT << " Input:";
   
      for(iDo=0;iDo<iNum_Inputs;iDo++)
      {
	 FPRINT << vEncodeIn->pVariables[iDo];
      } // end for
      
      //cout<<"Input: \n"<<*vEncodeIn<<endl;
      EncodeDataPair();
      FPRINT << " Output:";


      for( int j=0; j<iNum_Hidden; j++ )
      {
         theMatrix->pCol[iLoop][j] = vHidden->pVariables[j];
      }

      for(iDo=0;iDo<iNum_Outputs;iDo++)
      {
         FPRINT << vOutput->pVariables[iDo];
      } // end for

      //cout<<"Output: \n"<<*vOutput<<endl;
      //getch();
   } // end for loop

   //theVector->Zero_Out();

   //theVec = theMatrix->SolveMGS( theVector );
   //theMatrix->Print();
   //theVec = mTrainingInput->SolveMGS( theVector );
   //mTrainingInput->Print();

/*
   if( theVec != 0 )
   {
      theVec->Print();
      delete theVec;
   }
   else
   {
      FPRINT( "Unsolved" );
      FPRINT << "Couldn't solve it";
   }
*/

   //(theMatrix->GetQ())->Print();

   delete theMatrix;
   delete theVector;

   return fTestResult;
} // end TestNet

//************************************************************************
// Class    :  BackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  QuitForNow
// Purpose  :  Quits everything for the time being, that is, the user
//          :  pressed the key.
//************************************************************************
void BackproP::QuitForNow()
{
   //cout<<"yo yo mama\n";
} // end QuitForNow

//************************************************************************
// Class    :  BackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  GetTrainingInput
// Purpose  :  Returns the first training instance.
//          :  
//************************************************************************
VectoR& BackproP::GetTrainingInput(int iWhichInst)
{
   *vInput = mTrainingInput->pCol[iWhichInst];
   return *vInput;
} // end GetTrainingInput

//************************************************************************
// Class    :  BackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  GetOutput
// Purpose  :  Returns the output vector.
//          :  
//************************************************************************
VectoR& BackproP::GetOutput()
{
   return *vOutput;
} // end GetTrainingOutput

//************************************************************************
// Class    :  BackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  SetTrainingInputMatrix
// Purpose  :  
//          :  
//************************************************************************
void BackproP::SetTrainingInputMatrix( MatriX* matrix )
{
   if( mTrainingInput != NULL )
   {
      delete mTrainingInput;
   }
   mTrainingInput = matrix;
}

//************************************************************************
// Class    :  BackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  SetTrainingOutputMatrix
// Purpose  :  
//          :  
//************************************************************************
void BackproP::SetTrainingOutputMatrix( MatriX* matrix )
{
   if( mTrainingOutput != NULL )
   {
      delete mTrainingOutput;
   }
   mTrainingOutput = matrix;
}

//************************************************************************
// Class    :  BackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  SetSynapseOne
// Purpose  :  
//          :  
//************************************************************************
void BackproP::SetSynapseOne( MatriX* matrix )
{
   if( mSynapseOne != 0 )
   {
      delete mSynapseOne;
   }
   mSynapseOne = matrix;
   mSynapseOne->OutputToFile( "synapseOne.txt" );
}

//************************************************************************
// Class    :  BackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  SetSynapseTwo
// Purpose  :  
//          :  
//************************************************************************
void BackproP::SetSynapseTwo( MatriX* matrix )
{
   if( mSynapseTwo != 0 )
   {
      delete mSynapseTwo;
   }
   mSynapseTwo = matrix;
   mSynapseTwo->OutputToFile( "synapseTwo.txt" );
}

//************************************************************************
// Class    :  BackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  
// Purpose  :  
//          :  
//************************************************************************
void BackproP::SetRollbackPoint()
{
   _performRollback = 1;

   if( _synapseOneBackup != 0 )
   {
      delete _synapseOneBackup;
   }

   if( _synapseTwoBackup != 0 )
   {
      delete _synapseTwoBackup;
   }

   _synapseOneBackup = new MatriX( mSynapseOne );
   _synapseTwoBackup = new MatriX( mSynapseTwo );
}

//************************************************************************
// Class    :  BackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  
// Purpose  :  
//          :  
//************************************************************************
void BackproP::Rollback()
{
//FPRINT( "Rollback" );

   if( mSynapseOne != 0 )
   {
      delete mSynapseOne;
   }
   if( mSynapseTwo != 0 )
   {
      delete mSynapseTwo;
   }

   mSynapseOne = _synapseOneBackup;
   mSynapseTwo = _synapseTwoBackup;
}

//************************************************************************
// Class    :  BackproP
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  Run
// Purpose  :  Runs the network, yet to be really defined
//          :  
//************************************************************************
int BackproP::Run()
{
//FPRINT( "Run" );
int value = 0;

   ldThisError = 0;
   if(!usIsMature)
   {
      //FPRINT << "About to train the network...";
      TrainNet();
   }

   if( usIsMature )
   {
      value = 1;
   }
   else if( _performRollback )
   {
      Rollback();
   }

   return value;
} // end Run
