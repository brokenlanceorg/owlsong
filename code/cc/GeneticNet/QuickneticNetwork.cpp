//***********************************************************************************************
// File     : QuickneticNetwork.cpp
// Purpose  :
//          :
// Author   : Brandon Benham
// Date     : 8/12/05
//***********************************************************************************************

#include "QuickneticNetwork.hpp"

//***********************************************************************************************
// Class    : QuickneticNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
QuickneticNetworK::QuickneticNetworK( char* fileName, unsigned short mature )
{
   _dataFileName = new string( fileName );
   _dataFileName->append( ".dat" );

   string* definitionFile = new string( fileName );
   definitionFile->append( ".def" );

   _geneticNetwork = new GeneticNeuralNetworK( (char*)definitionFile->c_str(), mature );

   MatriX* matrix = _geneticNetwork->GetInputMatrix();
   _numberInputs = matrix->cnColumns;

   matrix = _geneticNetwork->GetOutputMatrix();
   _numberOutputs = matrix->cnColumns;

   Setup();
} // end QuickneticNetworK default constructor

//***********************************************************************************************
// Class    : QuickneticNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
QuickneticNetworK::~QuickneticNetworK()
{
   if( _dataFileName != 0 )
   {
      delete _dataFileName;
   }
   _dataFileName = 0;

   if( _geneticNetwork != 0 )
   {
      delete _geneticNetwork;
   }
   _geneticNetwork = 0;

   _trainingData.clear();

} // end QuickneticNetworK destructor

//***********************************************************************************************
// Class    : QuickneticNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void QuickneticNetworK::Setup()
{
   FilereadeR* theFile = new FilereadeR( (char*)_dataFileName->c_str() );

   char* theWord = 0;

   while( (theWord = theFile->GetNextWord()) != 0 )
   {
      _trainingData.push_back( strtold( theWord, (char**)0 ) );
   }

   delete theFile;
} // end Setup

//***********************************************************************************************
// Class    : QuickneticNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Run
// Purpose  : 
//***********************************************************************************************
int QuickneticNetworK::Run()
{
   int value = 0;
   int position = 0;

   if( _isMature )
   {
      _geneticNetwork->TestNet();
   }
   else
   {
      while( position < _trainingData.size() )
      {
         // first, create the new training matrices:
         MatriX* input = new MatriX( 1, _numberInputs );
         MatriX* output = new MatriX( 1, _numberOutputs );
   
         for( int i=0; i<_numberInputs; i++ )
         {
            input->pCol[0][i] = _trainingData.at( position++ );
         }
   
         for( int i=0; i<_numberOutputs; i++ )
         {
            output->pCol[0][i] = _trainingData.at( position++ );
         }
   
         // set the training matrices now they have data:
         _geneticNetwork->SetTrainingInputMatrix( input );
         _geneticNetwork->SetTrainingOutputMatrix( output );
   
         // finally, run the network for this data set:
         _geneticNetwork->Run();
         
         cout << ".";
      }
   }

   return value;
} // end Run

//***********************************************************************************************
// Class    : QuickneticNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Recall
// Notes    : The input vector size may not be the same size as the output...
//***********************************************************************************************
VectoR* QuickneticNetworK::Recall( VectoR* input )
{
   VectoR* result = 0;

   result = new VectoR( _geneticNetwork->Recall( input ) );

   return result;
}

//***********************************************************************************************
// Class    : QuickneticNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : RecurseRecall
// Notes    : Here, we assume the input and output sizes are the same....
//***********************************************************************************************
MatriX* QuickneticNetworK::RecurseRecall( VectoR* seed, int levels )
{
   MatriX* output = new MatriX( levels, seed->cnRows );

   for( int i=0; i<levels; i++ )
   {
      VectoR* result = Recall( seed );
      for( int j=0; j<output->cnColumns; j++ )
      {
         output->pCol[i][j] = result->pVariables[j];
         seed->pVariables[j] = output->pCol[i][j];
      }
      delete result;
   }

   return output;
}

//***********************************************************************************************
// Class    : QuickneticNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : RecurseRecall
// Notes    : Here, we assume the input and output sizes are the same....
//***********************************************************************************************
MatriX* QuickneticNetworK::RecurseRecall( int levels )
{
   MatriX* inputMatrix = _geneticNetwork->GetInputMatrix();
   VectoR* seed = new VectoR( inputMatrix->cnColumns );

   for( int i=0; i<seed->cnRows; i++ )
   {
      seed->pVariables[i] = inputMatrix->pCol[0][i];
   }

   MatriX* output = RecurseRecall( seed, levels );

   delete seed;

   return output;
}
