//***********************************************************************************************
// File     : Chromosome.cpp
// Purpose  : 
//          : 
// Author   : Brandon Benham 
// Date     : 9/16/01
//***********************************************************************************************

#include"Chromosome.hpp"          

//***********************************************************************************************
// Class    : ChromosomE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
ChromosomE::ChromosomE()
{
   Setup();
} // end ChromosomE default constructor

//***********************************************************************************************
// Class    : ChromosomE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
ChromosomE::~ChromosomE()
{
   if( _myCrosser != 0 )
   {
      delete (CrossoverStrategY*)_myCrosser;
      _myCrosser = 0;
   } // end if not null
   if( _myGenome != 0 )
   {
      delete _myGenome;
      _myGenome = 0;
   } // end if not null
   if( _myMutator != 0 )
   {
      delete (MutationStrategY*)_myMutator;
      _myMutator = 0;
   } // end if not null
} // end ChromosomE destructor

//***********************************************************************************************
// Class    : ChromosomE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void ChromosomE::Setup()
{
   _myCrosser = 0;
   _myGenome  = 0;
   _myMutator = 0;
   _LowEndpoint  = 0.3;
   _HighEndpoint = 0.7;
} // end Setup          

//***********************************************************************************************
// Class    : ChromosomE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : SetCrossoverStrategy
// Purpose  : 
//***********************************************************************************************
void ChromosomE::SetCrossoverStrategy( void* theStrat )
{
   if( _myCrosser != 0 )
   {
      delete ((CrossoverStrategY*)_myCrosser);
   } // end if not null

   _myCrosser = theStrat; // Type: CrossoverStrategY*
} // end SetCrossoverStrategy

//***********************************************************************************************
// Class    : ChromosomE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Crossover
// Purpose  : 
//***********************************************************************************************
ChromosomE* ChromosomE::Crossover( ChromosomE* theMate )
{
   ChromosomE* theChild = this->Clone();
   ((CrossoverStrategY*)_myCrosser)->Crossover( this, theMate, theChild );
   theChild->SetMutationStrategy( this->CloneMutationStrategy() );
   theChild->SetCrossoverStrategy( this->CloneCrossoverStrategy() );
   
   return theChild;
} // end Crossover

//***********************************************************************************************
// Class    : ChromosomE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Mutate
// Purpose  : 
//***********************************************************************************************
void ChromosomE::Mutate()
{
   ((MutationStrategY*)_myMutator)->Mutate( this );
} // end Mutate

//***********************************************************************************************
// Class    : ChromosomE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : GetGenes
// Purpose  : This method returns the genes located at positions iStart to iEnd
//          : inclusive (zero-based).
//***********************************************************************************************
VectoR* ChromosomE::GetGenes( int iStart, int iEnd )
{
   VectoR* theGenes = new VectoR( iEnd - iStart + 1 );
   int iCounter = 0;

   for( int i=iStart; i<iEnd+1; i++ )
   {
      theGenes->pVariables[ iCounter++ ] = _myGenome->pVariables[ i ];
   } // end for loop
   
   return theGenes;
} // end GetGenes

//***********************************************************************************************
// Class    : ChromosomE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : AddGenes
// Purpose  : Adds the specified genes to the genome.
//***********************************************************************************************
void ChromosomE::AddGenes( VectoR* theGenes )
{
   if( _myGenome == 0 )
   {
      _myGenome = new VectoR( theGenes );
      delete theGenes;
      return;
   } // end if first add
   
   VectoR* tempVector = new VectoR( _myGenome->cnRows + theGenes->cnRows );
   int iCounter = 0;

   for( int i=0; i<_myGenome->cnRows; i++ )
   {
      tempVector->pVariables[ iCounter++ ] = _myGenome->pVariables[ i ];
   } // end for loop
   for( int i=0; i<theGenes->cnRows; i++ )
   {
      tempVector->pVariables[ iCounter++ ] = theGenes->pVariables[ i ];
   } // end for loop
      
   delete _myGenome;
   delete theGenes;
   _myGenome = tempVector;           
} // end AddGenes

//***********************************************************************************************
// Class    : ChromosomE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : SetMutationStrategy
// Purpose  : 
//***********************************************************************************************
void ChromosomE::SetMutationStrategy( void* theStrat )
{
   if( _myMutator != 0 )
   {
      delete ((CrossoverStrategY*)_myMutator);
   } // end if not null

   _myMutator = theStrat; // Type: MutationStrategY*
} // end SetMutationStrategy

//***********************************************************************************************
// Class    : ChromosomE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : SetGenome
// Purpose  : 
//***********************************************************************************************
void ChromosomE::SetGenome( VectoR* theGenome )
{
   if( _myGenome != 0 )
   {
      delete _myGenome;
   } // end if not null

   _myGenome = theGenome;
} // end setGenome

//***********************************************************************************************
// Class    : ChromosomE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : CloneMutationStrategy
// Purpose  : 
//***********************************************************************************************
void* ChromosomE::CloneMutationStrategy()
{
   return ((MutationStrategY*)_myMutator)->Clone();
} // end CloneMutationStrategy

//***********************************************************************************************
// Class    : ChromosomE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : CloneCrossoverStrategy
// Purpose  : 
//***********************************************************************************************
void* ChromosomE::CloneCrossoverStrategy()
{
   return ((CrossoverStrategY*)_myCrosser)->Clone();
} // end CloneCrossoverStrategy

