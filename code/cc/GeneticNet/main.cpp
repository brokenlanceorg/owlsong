#include <cstdlib>
#include <iostream>
#include "GeneticNeuralNetwork.hpp"
#include "RollingNetwork.hpp"
#include "LargeNetwork.hpp"
#include "QuickneticNetwork.hpp"

using namespace std;

void OutputVectorToConsole( VectoR* vector )
{
   for( int i=0; i<vector->cnRows; i++ )
   {
      cout << vector->pVariables[i] << " ";
   }
   cout << "\n";
}

void OutputMatrixToConsole( MatriX* matrix )
{
   for( int i=0; i<matrix->cnRows; i++ )
   {
      for( int j=0; j<matrix->cnColumns; j++ )
      {
         cout << matrix->pCol[i][j] << " ";
      }
      cout << "\n";
   }
}

int main(int argc, char *argv[])
{
   cout << "Initializing the neural network..." << endl;

/** Code for GeneticNetwork */
   GeneticNeuralNetworK* BPNTestNet = 0;
   char fileName[30];
   fileName[0] = '\0';
   unsigned short isAlive = 0;
   unsigned short isMature = 0;

   if( argc > 1 )
   {
      strcpy( fileName, argv[ 1 ] );
      isAlive = atoi( argv[2] );
      isMature = atoi( argv[3] );
   }
   else
   {
      strcpy( fileName, "idempotent.def" );
   }

   BPNTestNet = new GeneticNeuralNetworK( fileName, isAlive );
   BPNTestNet->SetMature( isMature );

   if( argc == 5 )
   {
      BPNTestNet->SetMaximumEpoch( atoi( argv[4] ) );
   }

   cout << "Instantiating network: " << fileName << endl;
   cout << "isAlive: " << isAlive << endl;
   cout << "isMature: " << isMature << endl;

   cout << endl << "running network...." << endl;

   BPNTestNet->Run();

   cout << "\nThe network trained successfully!\n";

   BPNTestNet->TestNet();

   //VectoR* inputVector = new VectoR( 5 );
   //inputVector->Fill_Rand();
   //VectoR* output = BPNTestNet->Recall( inputVector );
   //OutputVectorToConsole( output );

   delete BPNTestNet;

/** Code for QuickneticNetwork
   QuickneticNetworK* BPNTestNet = 0;
   //GeneticNeuralNetworK* BPNTestNet = 0;
   char fileName[30];
   fileName[0] = '\0';
   unsigned short isAlive = 0;
   unsigned short isMature = 0;

   if( argc > 1 )
   {
      strcpy( fileName, argv[ 1 ] );
      isAlive = atoi( argv[2] );
      isMature = atoi( argv[3] );
   }
   else
   {
      strcpy( fileName, "idempotent.def" );
   }

   //BPNTestNet = new GeneticNeuralNetworK( fileName, isAlive );
   BPNTestNet = new QuickneticNetworK( fileName, isAlive );
   BPNTestNet->SetMature( isMature );

   cout << "Instantiating network: " << fileName << endl;
   cout << "isAlive: " << isAlive << endl;
   cout << "isMature: " << isMature << endl;

   cout << endl << "running network...." << endl;

   BPNTestNet->Run();

   cout << "\nThe network trained successfully!\n";

   //BPNTestNet->TestNet();

   //VectoR* inputVector = new VectoR( 5 );
   //inputVector->Fill_Rand();
   //VectoR* output = BPNTestNet->Recall( inputVector );
   //OutputVectorToConsole( output );

   MatriX* matrix = BPNTestNet->RecurseRecall( 30 );
   OutputMatrixToConsole( matrix );

   delete matrix;
   delete BPNTestNet;
*/
   
/*
   VectoR* inputVector = new VectoR( 8 );
   inputVector->Fill_Rand();
   VectoR* outputVector = new VectoR( 8 );
   outputVector->Fill_Rand();
*/

   //RollingNetworK* rollingNet = new RollingNetworK( "roll", 5, 8, 8, 60 );
/*
   RollingNetworK* rollingNet = new RollingNetworK( "testroll", 5, 8, 8, 60 );

   rollingNet->AddTrainingPair( inputVector, outputVector );
   rollingNet->Run();
   rollingNet->TestNet();
   cout << "\nThe rolling network trained successfully!\n";
   delete rollingNet;
   //system( "PAUSE" );
   //inputVector->Fill_Rand();
   //outputVector->Fill_Rand();

   LargeNetworK* largeNetwork = new LargeNetworK( "testroll", 10, 25, 8, 8, 60 );
   //LargeNetworK* largeNetwork = new LargeNetworK( "testroll", 1, 5, 130, 8, 8, 60 );

   cout << "constructed the large network" << endl;

   for( int i=0; i<300; i++ )
   {
      cout << "the input vector is:" << endl;
      OutputVectorToConsole( inputVector );

      largeNetwork->AddTrainingPairWithRollback( inputVector, inputVector );
      VectoR* result = largeNetwork->IdempotentRecall( inputVector );

      cout << "the output vector is:" << endl << endl;
      OutputVectorToConsole( result );

      delete result;

      //system( "PAUSE" );

      inputVector->Fill_Rand();
      outputVector->Fill_Rand();
   }


   delete largeNetwork;
*/

}
