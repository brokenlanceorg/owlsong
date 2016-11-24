//***********************************************************************************************
// File     : LargeNetwork.cpp
// Purpose  : This class basically just aggregates rolling networks into one big network.
//          :
//          :
//          :
//          :
//          :
//          :
//          :
// Author   : Brandon Benham
// Date     : 5/5/05
//***********************************************************************************************

#include "LargeNetwork.hpp"
#include <iostream>

// This will be used for all random numbers:
VectoR LargeNetworK::_random(1);

//***********************************************************************************************
// Class    : LargeNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions. Should never be used.
//          : Should never be used; i don't know why i put this here.
//***********************************************************************************************
LargeNetworK::LargeNetworK()
{
   Setup( 0 );
} // end LargeNetworK default constructor

//***********************************************************************************************
// Class    : LargeNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Constructor
// Purpose  : The parameters are: the base name of the networks,
//          : The number of aggregated rolling networks,
//          : The depth of the rolling networks,
//          : And the size of the networks.
//          :
//***********************************************************************************************
LargeNetworK::LargeNetworK( char* baseName, int numberOfNetworks, 
                            int depth, int inputs, int outputs, int hidden )
{
   Initialize( baseName, numberOfNetworks, 0, depth, inputs, outputs, hidden );
}

//***********************************************************************************************
// Class    : LargeNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Constructor
// Purpose  : The parameters are: the base name of the networks,
//          : The number of aggregated rolling networks,
//          : The depth of the rolling networks,
//          : And the size of the networks.
//          :
//***********************************************************************************************
LargeNetworK::LargeNetworK( char* baseName, int numberOfNetworks, int max,
                            int depth, int inputs, int outputs, int hidden )
{
   Initialize( baseName, numberOfNetworks, max, depth, inputs, outputs, hidden );
} // end LargeNetworK constructor

//***********************************************************************************************
// Class    : LargeNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
LargeNetworK::~LargeNetworK()
{
   for( int i=0; i<_theNetworks.size(); i++ )
   {
      delete _theNetworks[i];
   }
} // end LargeNetworK destructor

//***********************************************************************************************
// Class    : LargeNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Initialize
// Purpose  : 
//          : 
//          : 
//          : 
//***********************************************************************************************
void LargeNetworK::Initialize( char* baseName, int numberOfNetworks, int max,
                               int depth, int inputs, int outputs, int hidden )
{
   Setup( numberOfNetworks );
   char fileName[30];
   _maxNumberOfNetworks = max;
   _depth = depth;
   _inputs = inputs;
   _outputs = outputs;
   _hidden = hidden;

   strcpy( _baseName, baseName );

   for( int i=0; i<_numberOfNetworks; i++ )
   {
      fileName[0] = '\0';
      ostringstream streamer;
      strcpy( fileName, baseName );
      strcat( fileName, "_" );
      streamer << i;
      strcat( fileName, streamer.str().c_str() );
      _theNetworks.push_back( new RollingNetworK( fileName, depth, inputs, outputs, hidden ) );
   }

}

//***********************************************************************************************
// Class    : LargeNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//          : Instead of persisting the current network information, i've decided
//          : to simply randomize the variable.
//          : 
//***********************************************************************************************
void LargeNetworK::Setup( int num )
{
   _numberOfNetworks = num;
   _baseName[0] = '\0';

   _random.Fill_Rand();

   _currentNetwork = (int)(_random.pVariables[0] * _numberOfNetworks);
} // end Setup

//***********************************************************************************************
// Class    : LargeNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : FindBestMatch
// Purpose  : Finds the network which most closely matches the input and output.
//          : 
//          : 
//***********************************************************************************************
int LargeNetworK::FindBestMatch( VectoR* input, VectoR* output )
{
   VectoR* theResult;
   long double best = 2000000;
   long double current = 0;
   int which = -1;

   for( int i=0; i<_theNetworks.size(); i++ )
   {
      theResult = _theNetworks[i]->Recall( input );
      *theResult -= *output;
      current = theResult->GetSumSquared();
      if( current < best )
      {
         best = current;
         which = i;
      }
   }

   return which;
}

//***********************************************************************************************
// Class    : LargeNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : AddTrainingPair
// Purpose  : This is a greedy operation in that the network is trained as soon as the pair
//          : is added.
//          : 
//***********************************************************************************************
void LargeNetworK::AddTrainingPair( VectoR* input, VectoR* output )
{

   int which = FindBestMatch( input, output );
   _theNetworks[ which ]->AddTrainingPair( input, output );
   _theNetworks[ which ]->AddMaximumEpoch( 50000 );
   int ret = _theNetworks[ which ]->Run();

//Temporary:
//_theNetworks[ which ]->TestNet();

}

//***********************************************************************************************
// Class    : LargeNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//          : 
//          : 
//***********************************************************************************************
int LargeNetworK::TrainWithRollback( VectoR* input, VectoR* output, int begin, int end )
{
   int trained = 0;

   for( int i=begin; i<end; i++ )
   {
//cout << "attempting a training .... i: " << i << endl;
      RollingNetworK* theNetwork = _theNetworks.at( i );

      MatriX* synOne = new MatriX( theNetwork->GetSynapseOne() );
      MatriX* synTwo = new MatriX( theNetwork->GetSynapseTwo() );

      MatriX* trainInput = new MatriX( theNetwork->GetTrainingInputMatrix() );
      MatriX* trainOutput = new MatriX( theNetwork->GetTrainingOutputMatrix() );

      theNetwork->AddTrainingPair( input, output );
      theNetwork->AddMaximumEpoch( 50000 );

//cout << "number of instances: " << trainInput->cnRows << endl;

      if( (theNetwork->Run()) )
      {
         theNetwork->WriteFactFile();
         theNetwork->TestNet();
         delete synOne;
         delete synTwo;
         delete trainInput;
         delete trainOutput;
         // Everything trained fine, so exit
         trained = 1;
         break;
      }
      else // didn't train so rollback:
      {
         if( trainInput->cnRows == _depth )
         {
//cout << "rolling back on a depth network" << endl;
            theNetwork->SetSynapseOne( synOne );
            theNetwork->SetSynapseTwo( synTwo );
            theNetwork->SetTrainingInputMatrix( trainInput );
            theNetwork->SetTrainingOutputMatrix( trainOutput );
            theNetwork->SetNumberOfInstances( trainInput->cnRows );
         }
         else // couldn't even make it to depth, so get new brain
         {
//cout << "recreating a depth network" << endl;
            synOne->Fill_Rand();
            synTwo->Fill_Rand();
            synOne->Scale( 0.5 );
            synTwo->Scale( 0.5 );

            theNetwork->SetSynapseOne( synOne );
            theNetwork->SetSynapseTwo( synTwo );

            theNetwork->WriteDefaultFactFile();

            trainInput = new MatriX( 0, input->cnRows );
            trainOutput = new MatriX( 0, output->cnRows );
            theNetwork->SetTrainingInputMatrix( trainInput );
            theNetwork->SetTrainingOutputMatrix( trainOutput );
            theNetwork->SetNumberOfInstances( trainInput->cnRows );

            theNetwork->SaveWeights();
         }
      }
   }

   return trained;
}

//***********************************************************************************************
// Class    : LargeNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : AddTrainingPairWithRollback
// Purpose  : This is a greedy operation in that the network is trained as soon as the pair
//          : is added.
// Notes    : Take caution when using the standard lib random number generator.
//          : Rember that these are psudo-random numbers, and as such, they will sometimes
//          : give values that really aren't all that random or different from each other.
//          : Thus, for some initial networks (since the synapses are random), the synapses
//          : *seem* to be very well suited for learning while other configurations are 
//          : downright terrible at it.
//          : 
//          : With this being said, it might be advantageous to create a large network with
//          : a small initial network size, and a much larger max size. In this manner,
//          : we don't initialize all networks at once (could be a "bad" random sequence),
//          : but rather over time on an as needed or lazy initialization basis, which would
//          : further increase the chances of getting an optimal random sequence.
//          : 
//          : 
//***********************************************************************************************
void LargeNetworK::AddTrainingPairWithRollback( VectoR* input, VectoR* output )
{
   int begin = (_currentNetwork % _numberOfNetworks);
   int end = _theNetworks.size();

cout << "training to network...." << begin << endl;

   if( TrainWithRollback( input, output, begin, end ) )
   {
//cout << "trained fine...." << endl;
   }
   else
   {
//cout << "couldn't train trying first half...." << endl;
      TrainWithRollback( input, output, 0, begin );
   }

   _currentNetwork++;

/**   This really won't work unless we persist the LargeNetwork data.....
   char fileName[30];
   fileName[0] = '\0';;

   // If we got here, nothing trained
   for( int i=0; i<_maxNumberOfNetworks; i++ )
   {
      ostringstream streamer;
      strcpy( fileName, _baseName );
      strcat( fileName, "_" );
      streamer << (_theNetworks.size() + i);
      strcat( fileName, streamer.str().c_str() );

      RollingNetworK* theNetwork = new RollingNetworK( fileName, _depth, _inputs, _outputs, _hidden, 0 );

      theNetwork->AddTrainingPair( input, output );
      theNetwork->AddMaximumEpoch( 50000 );
      if( _theNetworks[ i ]->Run() )
      {
cout << "added new network..." << endl;
         theNetwork->WriteFactFile();
         // Everything trained fine, so exit
         _theNetworks.push_back( theNetwork );
         return;
      }
      else // didn't train:
      {
cout << "couldn't train at all ..." << endl;
         delete theNetwork;
      }
   }
*/

} // end AddTrainingPair

//***********************************************************************************************
// Class    : LargeNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Recall
// Purpose  : 
//          : 
//          : 
//***********************************************************************************************
vector< VectoR* > LargeNetworK::Recall( VectoR* data )
{
   vector< VectoR* > theVector;

   for( int i=0; i<_theNetworks.size(); i++ )
   {
      VectoR* theResult = _theNetworks[i]->Recall( data );
      theVector.push_back( theResult );
   }

   return theVector;
}

//***********************************************************************************************
// Class    : LargeNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : IdempotentRecall
// Purpose  : 
//          : 
//          : 
//***********************************************************************************************
VectoR* LargeNetworK::IdempotentRecall( VectoR* data )
{
   VectoR* theVector;

   int which = FindBestMatch( data, data );
   vector< VectoR* > aVector = Recall( data );
   theVector = new VectoR( aVector.at( which ) );

/*
   for( int i=0; i<aVector.size(); i++ )
   {
      delete aVector[i];
   }
*/

   return theVector;
}

//***********************************************************************************************
// Class    : LargeNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : GetLargestNumberOfInstances()
// Purpose  : Returns the largest number of training instances amongst all the RollingNetworks.
//          : 
//          : 
//***********************************************************************************************
int LargeNetworK::GetLargestNumberOfInstances()
{
   int number = 0;

   for( int i=0; i<_theNetworks.size(); i++ )
   {
      RollingNetworK* aNetwork = _theNetworks.at( i );
      if( aNetwork->GetNumberOfInstances() > number )
      {
         number = aNetwork->GetNumberOfInstances();
      }
   }

   return number;
}
