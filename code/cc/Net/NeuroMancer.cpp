//***********************************************************************************************
// File		: NeuroMancer.cpp
// Purpose	: This class makes or builds neural networks based on information
//          : passed to it. In some cases, this information is in the form of
//          : StockpoinTs or StockserieS, in other cases, the network can be
//          : constructed piece-by-piece.
//				: 
// Author	: Brandon Benham 
// Date		: 7/01/00
//***********************************************************************************************

#include"NeuroMancer.hpp"          

//***********************************************************************************************
// Class	   : NeuromanceR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Default Constructor
// Purpose	: Performs the basic construction actions.
//***********************************************************************************************
NeuromanceR::NeuromanceR()
{
	Setup();
} // end NeuromanceR default constructor

//***********************************************************************************************
// Class	   : NeuromanceR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Destructor
// Purpose	: Performs the destruction actions.
//***********************************************************************************************
NeuromanceR::~NeuromanceR()
{
} // end NeuromanceR destructor

//***********************************************************************************************
// Class	   : NeuromanceR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Setup
// Purpose	: Performs the basic setup actions.
//***********************************************************************************************
void NeuromanceR::Setup()
{
	ldMaximum       = 0;
   ldMinimum       = 0;
	ldCeiling       = 1.7;
   ldFloor         = 0.3;
   ldStep_Size     = 0.1666666666666666;
} // end Setup

//***********************************************************************************************
// Class	   : NeuromanceR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: MakeNeuralNetwork
// Purpose	: Creates a sequence neural network based on a StockpoinT ref.
// Notes    : It is up to the client to destroy the object.
//***********************************************************************************************
BackproP* NeuromanceR::MakeNeuralNetwork( StockpoinT* aStockPoint )
{
   pcThe_Output_File = MakeFileName( aStockPoint->GetObjectID(), "_Parallel.def" );
   BackproP* theNet = 0;
   if( DetermineIfNetExists( pcThe_Output_File ) )
   {
   	theNet = new BackproP( pcThe_Output_File, 1 );
   	delete[] pcThe_Output_File;
   	return theNet; 
   } // end if

   CreateDefinitionFile( 4, 4, 9, 0.00001, 0.5 );
   delete[] pcThe_Output_File;

   StockserieS* thePredictions = aStockPoint->GetPredictions();
   StockserieS* theActuals = aStockPoint->GetActuals();
   
 	pcThe_Output_File = MakeFileName( thePredictions->GetID(), "_Parallel.fac" );
   FilesourcE outputFile( pcThe_Output_File, _BYTE, 0 );

   if( outputFile.OpenFileWrite() == 0 )
   	return 0; // should probably throw errors at this point; we'll look at it later.....

   SetCeilingAndFloor();
   StockserieS* newPredictions = GetMappedSeries( thePredictions );
   StockserieS* newActuals = GetMappedSeries( theActuals );
   
   char acBuffer[35];

   outputFile.SetBuffer( "1 " );
   outputFile.WriteWord();
   
   newPredictions->Rewind();
   while( newPredictions->HasMoreElements() )
   {
   	MakeASCII( newPredictions->GetNextElement(), acBuffer );
      strcat( acBuffer, " " );
   	outputFile.SetBuffer( acBuffer );
	   outputFile.WriteWord();
   } // end while predictions

	newActuals->Rewind();      
   while( newActuals->HasMoreElements() )
   {
   	MakeASCII( newActuals->GetNextElement(), acBuffer );
      strcat( acBuffer, " " );
   	outputFile.SetBuffer( acBuffer );
	   outputFile.WriteWord();
   } // end while loop       

   outputFile.CloseFile();
   
   delete[] pcThe_Output_File;
   delete newPredictions;
   delete newActuals;
   pcThe_Output_File = MakeFileName( aStockPoint->GetObjectID(), "_Parallel.def" );
   theNet = new BackproP( pcThe_Output_File, 0 );
   delete[] pcThe_Output_File;

   if( theNet->Run() )  // Could take a while here.....
   	return 0;
                  
   return theNet;             
} // end MakeNeuralNetwork

//***********************************************************************************************
// Class	   : NeuromanceR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: MakeNeuralNetwork
// Purpose	: Creates a backpropagation neural network based on a StockserieS ref.
// Notes    : It is up to the client to destroy the object.
//***********************************************************************************************
SequencenetworK* NeuromanceR::MakeNeuralNetwork( StockserieS* aStockSeries )
{
   pcThe_Output_File = MakeFileName( aStockSeries->GetID(), ".def" );
   SequencenetworK* theNet = 0;
   if( DetermineIfNetExists( pcThe_Output_File ) ) // Should never find it....
   {
   	theNet = new SequencenetworK( pcThe_Output_File, 1 );
      delete[] pcThe_Output_File;
      return theNet;
   } // end if

   CreateDefinitionFile( 1, 1, 9, 0.00001, 0.5 );
   delete[] pcThe_Output_File;

 	pcThe_Output_File = MakeFileName( aStockSeries->GetID(), ".fac" );
   FilesourcE outputFile( pcThe_Output_File, _BYTE, 0 );

   if( outputFile.OpenFileWrite() == 0 )
   	return 0; // should probably throw errors at this point; we'll look at it later.....

   StockserieS* anotherSeries = GetMappedSeries( aStockSeries );
	// Now write out to disk:
   int iLen = anotherSeries->GetNumberOfElements();
   char acBuffer[35];

   MakeASCII( iLen, acBuffer );
   strcat( acBuffer, " " );
   outputFile.SetBuffer( acBuffer );
   outputFile.WriteWord();
  
   long double ldXarg = 0;
   anotherSeries->Rewind();
   while( anotherSeries->HasMoreElements() )
   {
   	MakeASCII( ldXarg, acBuffer );
	   strcat( acBuffer, " " );
   	outputFile.SetBuffer( acBuffer );
	   outputFile.WriteWord();
  
   	MakeASCII( anotherSeries->GetNextElement(), acBuffer );
	   strcat( acBuffer, "\n" );
   	outputFile.SetBuffer( acBuffer );
	   outputFile.WriteWord();

  		ldXarg += ldStep_Size;
   } // end while loop

   outputFile.CloseFile();

   delete[] pcThe_Output_File;
   delete anotherSeries;
   pcThe_Output_File = MakeFileName( aStockSeries->GetID(), ".def" );
   theNet = new SequencenetworK( pcThe_Output_File, 0 );
   delete[] pcThe_Output_File;

   if( theNet->Run() )  // Could take a while here.....
   	return 0;
                  
   return theNet;
} // end MakeNeuralNetwork

//***********************************************************************************************
// Class	   : NeuromanceR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: MakeNeuralNetwork
// Purpose	: Creates a Sequence neural network based on a VectoR ref.
// Notes    : It is up to the client to destroy the object.
//***********************************************************************************************
SequencenetworK* NeuromanceR::MakeNeuralNetwork( VectoR* aVec )
{
    // Write out the fac file first:
    StockserieS* newSeries = new StockserieS( 0.1 );
    newSeries->SetTransient();
    for( int i = 0; i < aVec->cnRows; i++ )
        newSeries->AddElement( aVec->pVariables[i] );
    StockserieS* theSeries = GetMappedSeries( newSeries );
    // Get the file:
    pcThe_Output_File = MakeFileName( 0, "_StockPrediction.fac" ); // we really don't care what the name is
    FilesourcE outputFile( pcThe_Output_File, _BYTE, 0 );
    if( outputFile.OpenFileWrite() == 0 )
        return 0; // should probably throw errors at this point; we'll look at it later.....
    long double ldArg  = 0;
    long double ldStep = 0.25;
    char        acBuffer[35];
    acBuffer[0] = '\0';
    // Write out the number of instances:
    itoa( aVec->cnRows, acBuffer, 10 );
    strcat( acBuffer, " " );
    outputFile.SetBuffer( acBuffer );
    outputFile.WriteWord();
    theSeries->Rewind();
    while( theSeries->HasMoreElements() )
    {
        acBuffer[0] = '\0';
        gcvt( ldArg, 18, acBuffer );
        strcat( acBuffer, " " );
        outputFile.SetBuffer( acBuffer );
        outputFile.WriteWord();

        acBuffer[0] = '\0';
        gcvt( theSeries->GetNextElement(), 18, acBuffer );
        strcat( acBuffer, "\n" );
        outputFile.SetBuffer( acBuffer );
        outputFile.WriteWord();

        ldArg += ldStep;
    } // end while loop

    outputFile.CloseFile();
        
    // Now we'll do the Def file:
    delete[] pcThe_Output_File;
    delete   newSeries;
    pcThe_Output_File = MakeFileName( 0, "_StockPrediction.def" );
    CreateDefinitionFile( 1, 1, 9, 0.00001, 0.5 );

    // Now make the network:
    SequencenetworK* theSeqNet = new SequencenetworK( pcThe_Output_File, 0 );
    delete[] pcThe_Output_File;

    if( theSeqNet->Run() )
        return 0;

    return theSeqNet;
} // end MakeNeuralNetwork Vector version

//***********************************************************************************************
// Class	   : NeuromanceR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: DeterminIfNetExists
// Purpose	: 
// Notes    : 
//***********************************************************************************************
bool NeuromanceR::DetermineIfNetExists( char* pcFileName )
{
	FilesourcE aFile( pcFileName );

   if( aFile.OpenFileRead() == 0 )
   	return false;
   else 
      return true;
} // end DeterminIfNetExists

//***********************************************************************************************
// Class	   : NeuromanceR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: MakeFileName
// Purpose	: 
// Notes    : It is up to the client to clean up the memory alloc.
//***********************************************************************************************
char* NeuromanceR::MakeFileName( int iOID, char* pcSuffix )
{
	char* pcFileName = new char[25];
   pcFileName[ 0 ] = '\0';
   itoa( iOID, pcFileName, 10 );
   strcat( pcFileName, pcSuffix );

   return pcFileName;
} // end MakeFileName

//***********************************************************************************************
// Class	   : NeuromanceR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: CreateDefinitionFile
// Purpose	: 
// Notes    : 
//***********************************************************************************************
void  NeuromanceR::CreateDefinitionFile( int iInputs, int iOutputs, int iHidden, 
	long double ldTolerance, long double ldRate )
{
	FilesourcE outputFile( pcThe_Output_File, _BYTE, 0 );

   if( outputFile.OpenFileWrite() == 0 )
   	return;

	char acBuffer[ 35 ];
   
   outputFile.SetBuffer( "INPUTS " );
   outputFile.WriteWord();

   MakeASCII( iInputs, acBuffer );
   strcat( acBuffer, " " );
   outputFile.SetBuffer( acBuffer );
   outputFile.WriteWord();

   outputFile.SetBuffer( "OUTPUTS " );
   outputFile.WriteWord();

   MakeASCII( iOutputs, acBuffer );
   strcat( acBuffer, " " );
   outputFile.SetBuffer( acBuffer );
   outputFile.WriteWord();
   
   outputFile.SetBuffer( "HIDDEN " );
   outputFile.WriteWord();

   MakeASCII( iHidden, acBuffer );
   strcat( acBuffer, " " );
   outputFile.SetBuffer( acBuffer );
   outputFile.WriteWord();
   
   outputFile.SetBuffer( "MOMENTUM " );
   outputFile.WriteWord();
   outputFile.SetBuffer( "0.02 " );
   outputFile.WriteWord();

   outputFile.SetBuffer( "TOLERANCE " );
   outputFile.WriteWord();

   MakeASCII( ldTolerance, acBuffer );
   strcat( acBuffer, " " );
   outputFile.SetBuffer( acBuffer );
   outputFile.WriteWord();
   
   outputFile.SetBuffer( "RATE " );
   outputFile.WriteWord();

   MakeASCII( ldRate, acBuffer );
   strcat( acBuffer, " " );
   outputFile.SetBuffer( acBuffer );
   outputFile.WriteWord();

   outputFile.CloseFile();
   
} // end CreateDefinitionFile    

//***********************************************************************************************
// Class	   : NeuromanceR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: MakeASCII
// Purpose	: 
// Notes    : 
//***********************************************************************************************
void NeuromanceR::MakeASCII( long double ldDoub, char* pcChars )
{
	pcChars[ 0 ] = '\0';
   gcvt( ldDoub, 18, pcChars );
} // end MakeASCII

//***********************************************************************************************
// Class	   : NeuromanceR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: MakeASCII
// Purpose	: 
// Notes    : 
//***********************************************************************************************
void NeuromanceR::MakeASCII( int iInt, char* pcChars )
{
	pcChars[ 0 ] = '\0';
   itoa( iInt, pcChars, 10 );
} // end MakeASCII

//***********************************************************************************************
// Class	   : NeuromanceR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: GetMappedSeries
// Purpose	: 
// Notes    : It is the client's responsibility to clean up memory usage.
//***********************************************************************************************
StockserieS* NeuromanceR::GetMappedSeries( StockserieS* initialSeries )
{
   StockserieS aNewSeries( 0.1 );
   aNewSeries.SetTransient();
   
   initialSeries->Rewind();
   ldMinimum = initialSeries->GetDataMinimum();
   ldMinimum -= ldFloor;
   long double ldTemp;
   while( initialSeries->HasMoreElements() )
   	aNewSeries.AddElement( initialSeries->GetNextElement() - ldMinimum );

   StockserieS* anotherSeries = new StockserieS( 0.1 );
   anotherSeries->SetTransient();
   
   aNewSeries.Rewind();
	ldMaximum = aNewSeries.GetDataMaximum();
   ldMaximum *= ldCeiling;
   // anotherSeries now contains writable data:
   while( aNewSeries.HasMoreElements() )
   	anotherSeries->AddElement( aNewSeries.GetNextElement() / ldMaximum );

   return anotherSeries;
} // end GetMappedSeries

//***********************************************************************************************
// Class	   : NeuromanceR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: MapVector
// Purpose	: Fix ME!!!
// Notes    : IF GetMappedSeries IS NOT CALLED, THEN THIS WILL GIVE A DIV BY ZERO ERROR!!!!!
//***********************************************************************************************
void NeuromanceR::MapVector( VectoR* theVec )
{
	for( int i = 0; i < theVec->cnRows; i++ )
   {
   	theVec->pVariables[ i ] -= ldMinimum;
   	theVec->pVariables[ i ] /= ldMaximum;
   } // end for loop
} // end MapVector

//***********************************************************************************************
// Class	   : NeuromanceR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: InvertVector
// Purpose	: 
// Notes    : 
//***********************************************************************************************
void NeuromanceR::InvertVector( VectoR* theVec )
{
   OutputStatus();
	for( int i = 0; i < theVec->cnRows; i++ )
   {
   	theVec->pVariables[ i ] *= ldMaximum;
   	theVec->pVariables[ i ] += ldMinimum;
   } // end for loop
} // end InvertVector

//***********************************************************************************************
// Class	   : NeuromanceR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: InvertPoint
// Purpose	: 
// Notes    : This method doesn't affect the argument.
//***********************************************************************************************
long double NeuromanceR::InvertPoint( long double ldPoint )
{
    OutputStatus();
    long double ldTheValue = ldPoint;
    ldTheValue *= ldMaximum;
    ldTheValue += ldMinimum;

    return ldTheValue;
} // end InvertPoint

//***********************************************************************************************
// Class	   : NeuromanceR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: OutputStatus
// Purpose	: 
// Notes    : 
//***********************************************************************************************
void NeuromanceR::OutputStatus()
{
ofstream outputFile( "Status.dat", ios::app );

   outputFile << "NeuromanceR::ldMinimum: " << ldMinimum << "\n";
   outputFile << "NeuromanceR::ldMaximum: " << ldMaximum << "\n";

   outputFile << flush;
   outputFile.close();
} // end OutputStatus

