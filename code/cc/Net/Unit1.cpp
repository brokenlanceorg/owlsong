#include<iostream>

#include "ModBackprop.hpp"

//---------------------------------------------------------------------------
// Main function
//---------------------------------------------------------------------------
main( int argc, char *argv[] )
{
   cout << "Initializing the neural network..." << endl;

   ModBackproP* BPNTestNet = 0;
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

   BPNTestNet = new ModBackproP( fileName, isAlive );
   BPNTestNet->SetMature( isMature );

   if( argc == 5 )
   {
      BPNTestNet->SetMaximumEpoch( atoi( argv[4] ) );
   }

   cout << "Instantiating network: " << fileName << endl;
   cout << "isAlive: " << isAlive << endl;
   cout << "isMature: " << isMature << endl;

   cout << endl << "running network...." << endl;

   if( isMature )
   {
      BPNTestNet->TestNet();
   }
   else if( BPNTestNet->Run() )
   {
      cout << "\nThe network couldn't train!";
      delete BPNTestNet;
      return 1;
   } //end if

   cout << "\nThe network trained successfully!\n";

   //VectoR* inputVector = new VectoR( 5 );
   //inputVector->pVariables[0] = .5327868;
   //BPNTestNet->Recall( inputVector );
   
   delete BPNTestNet;
}
