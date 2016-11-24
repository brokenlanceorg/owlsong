//***********************************************************************************************
// File		: SequenceNetwork.cpp
// Purpose	: This class is an adapter class for a FuzzyNeural network.
//          : It will perform the following algorithm:
//          :    
//          :    FuzzyNet->Run()
//          :    if(!FuzzyNet->IsMature())
//          : 			WriteNewDataFiles()
//          : 			delete FuzzyNet   
//          : 			FuzzyNet = new FuzzyNet(deffilename)
//          : 			FuzzyNet->Run()
//          :        if(!FuzzyNet->IsMature())
//          : 				return error
//          :                          
//	Notes		: We derive from NeuralneT so that clients can use the same NeuralNetwork
//				: interface that they've been using.
// Author	: Brandon Benham 
// Date		: 6/04/00
//***********************************************************************************************

#include"SequenceNetwork.hpp"          

//***********************************************************************************************
// Class		: SequencenetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Default Constructor
// Purpose	: Performs the basic construction actions.
//***********************************************************************************************
SequencenetworK::SequencenetworK() : NeuralneT()
{
	Setup();
} // end SequencenetworK default constructor

//***********************************************************************************************
// Class		: SequencenetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Constructor
// Purpose	: Performs the construction actions.
//***********************************************************************************************
SequencenetworK::SequencenetworK( char* pcFileName, unsigned short usIsAliveYet ) : NeuralneT()
{
	Setup();
   
   Fuzzy_Neural_Network = new FuzzyneuraL( pcFileName, usIsAliveYet );
   
   pcDefinition_File_Name = new char[ strlen( pcFileName ) + 1 ];
   pcDefinition_File_Name[0] = '\0';
   strcpy( pcDefinition_File_Name, pcFileName );
   
   char* pcTemp = Fuzzy_Neural_Network->GetBaseName();
   pcFact_File_Name = new char[ strlen( pcTemp ) + 4 ];
   pcFact_File_Name[0]= '\0';
   strcpy( pcFact_File_Name, pcTemp );
   strcat( pcFact_File_Name, "fac" );
} // end SequencenetworK constructor

//***********************************************************************************************
// Class		: SequencenetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Destructor
// Purpose	: Performs the destruction actions.
//***********************************************************************************************
SequencenetworK::~SequencenetworK()
{
	if( Fuzzy_Neural_Network != 0 )
   	delete Fuzzy_Neural_Network;
	if( Fthe_Data_File != 0 )
   	delete Fthe_Data_File;
	if( pcDefinition_File_Name != 0 )
   	delete[] pcDefinition_File_Name;
	if( pcFact_File_Name != 0 )
   	delete[] pcFact_File_Name;
} // end SequencenetworK destructor

//***********************************************************************************************
// Class		: SequencenetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Setup
// Purpose	: Performs the basic setup actions.
//***********************************************************************************************
void SequencenetworK::Setup()
{
	Fuzzy_Neural_Network   = 0;
   Fthe_Data_File         = 0;
   pcDefinition_File_Name = 0;
   pcFact_File_Name       = 0;
} // end Setup

//***********************************************************************************************
// Class		: SequencenetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: WriteNewDataFiles
// Purpose	: This outputs the new data files for the adjusted network.
//***********************************************************************************************
void SequencenetworK::WriteNewDataFiles()
{
	if( Fthe_Data_File == 0 )
   	Fthe_Data_File = new FilesourcE( pcDefinition_File_Name, _BYTE, 0 );
	WriteDefinitionFile();
	WriteFactFile();
} // end WriteNewDataFiles

//***********************************************************************************************
// Class		: SequencenetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Recall
// Purpose  : 
//          : 
//***********************************************************************************************
int SequencenetworK::Recall( VectoR* theVec )
{
	return Fuzzy_Neural_Network->Recall( theVec );
} // end Recall

//***********************************************************************************************
// Class		: SequencenetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: Run
// Purpose  : It will perform the following algorithm:
//          :    
//          :    FuzzyNet->Run()
//          :    if(!FuzzyNet->IsMature())
//          : 			WriteNewDataFiles()
//          : 			delete FuzzyNet   
//          : 			FuzzyNet = new FuzzyNet(deffilename)
//          : 			FuzzyNet->Run()
//          :        if(!FuzzyNet->IsMature())
//          : 				return error
//          :                          
//***********************************************************************************************
int SequencenetworK::Run()
{
	Fuzzy_Neural_Network->Run();
   if( !Fuzzy_Neural_Network->IsMature() )
   {
   	WriteNewDataFiles();
	   FuzzyneuraL aNewNetwork( pcDefinition_File_Name, 0 );
      aNewNetwork.Run();
      if( !aNewNetwork.IsMature() )
         return 1;
   } // end if
   return 0;
} // end Run

//***********************************************************************************************
// Class		: SequencenetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: WriteDefinitionFile
// Purpose	: The data needed to be written is:
//          :   
//          :   Number of Inputs
//          :   Number of Hidden
//          :   Number of Outputs
//          :   Momentum
//          :   Tolerance
//          :   Learning Rate
//          :
//***********************************************************************************************
void SequencenetworK::WriteDefinitionFile()
{
float fTemp    = 0;
int   iTemp    = 0;
char* pcTemp   = 0;
char* pcBuffer = new char[ 256 ];

	Fthe_Data_File->OpenFileWrite();
   Fthe_Data_File->SetBuffer( "//This is a generated Definition File (ZoSo)" );
   Fthe_Data_File->WriteWord();
   Fthe_Data_File->SetBuffer( "\n" );
   Fthe_Data_File->WriteWord();
   
   Fthe_Data_File->SetBuffer( "INPUTS " );
   Fthe_Data_File->WriteWord();
   iTemp = Fuzzy_Neural_Network->GetNumberOfInputs() + 1;
   pcBuffer[0] = '\0';
   itoa( iTemp, pcBuffer, 10 );
   Fthe_Data_File->SetBuffer( pcBuffer );
   Fthe_Data_File->WriteWord();
   
   Fthe_Data_File->SetBuffer( " OUTPUTS " );
   Fthe_Data_File->WriteWord();
   iTemp = Fuzzy_Neural_Network->GetNumberOfOutputs();
   pcBuffer[0] = '\0';
   itoa( iTemp, pcBuffer, 10 );
   Fthe_Data_File->SetBuffer( pcBuffer );
   Fthe_Data_File->WriteWord();
              
   Fthe_Data_File->SetBuffer( " HIDDEN " );
   Fthe_Data_File->WriteWord();
   iTemp = Fuzzy_Neural_Network->GetNumberOfHidden();
   pcBuffer[0] = '\0';
   itoa( iTemp, pcBuffer, 10 );
   Fthe_Data_File->SetBuffer( pcBuffer );
   Fthe_Data_File->WriteWord();
                
   Fthe_Data_File->SetBuffer( " MOMENTUM " );
   Fthe_Data_File->WriteWord();
   fTemp = Fuzzy_Neural_Network->GetMomentum();
   pcBuffer[0] = '\0';
   gcvt( fTemp, 18, pcBuffer );
   Fthe_Data_File->SetBuffer( pcBuffer );
   Fthe_Data_File->WriteWord();
   
   Fthe_Data_File->SetBuffer( " TOLERANCE " );
   Fthe_Data_File->WriteWord();
   fTemp = Fuzzy_Neural_Network->GetTolerance();
   pcBuffer[0] = '\0';
   gcvt( fTemp, 18, pcBuffer );
   Fthe_Data_File->SetBuffer( pcBuffer );
   Fthe_Data_File->WriteWord();
     
   Fthe_Data_File->SetBuffer( " RATE " );
   Fthe_Data_File->WriteWord();
   fTemp = Fuzzy_Neural_Network->GetLearningRate();
   pcBuffer[0] = '\0';
   gcvt( fTemp, 18, pcBuffer );
   Fthe_Data_File->SetBuffer( pcBuffer );
   Fthe_Data_File->WriteWord();

   Fthe_Data_File->SetBuffer( "\n" ); // was: " \n\n\n"
   Fthe_Data_File->WriteWord();
   
   Fthe_Data_File->CloseFile();
   
	delete Fthe_Data_File;
	Fthe_Data_File = 0;
      
   delete[] pcBuffer;
   
} // end WriteDefinitionFile           

//***********************************************************************************************
// Class		: SequencenetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method	: WriteFactFile
// Purpose	: The data to be written is as follows:
//        	: 
//        	:    Number of instances
//        	:    input#(1)   input#(n)   output#(1)
//        	:    input#(2)   input#(n-1) output#(2)
//        	:     ...          ...          ...
//        	:     ...          ...          ...
//        	:    input#(n-1) input#(2)   output(n-1)
//        	:    input#(n)   input#(1)   output(n)
//        	: 
//***********************************************************************************************
void SequencenetworK::WriteFactFile()
{
MatriX* mInput   = 0;
MatriX* mOutput  = 0;
int   iTemp      = 0;
char* pcTemp     = 0;
char* pcBuffer   = new char[ 256 ];

   Fthe_Data_File = new FilesourcE( pcFact_File_Name, _BYTE, 0 );
      
	Fthe_Data_File->OpenFileWrite();
   Fthe_Data_File->SetBuffer( "//This is a generated Fact File (ZoSo)" );
   Fthe_Data_File->WriteWord();
   Fthe_Data_File->SetBuffer( "\n" );
   Fthe_Data_File->WriteWord();
   
   iTemp = Fuzzy_Neural_Network->GetNumberOfInstances();
   pcBuffer[0] = '\0';
   itoa( iTemp, pcBuffer, 10 );
   Fthe_Data_File->SetBuffer( pcBuffer );
   Fthe_Data_File->WriteWord();

   Fthe_Data_File->SetBuffer( " " );
   Fthe_Data_File->WriteWord();
      
  	mInput  = Fuzzy_Neural_Network->GetInputMatrix();
  	mOutput = Fuzzy_Neural_Network->GetOutputMatrix();
   for( int i = 0; i < iTemp; i++ )
   {
      pcBuffer[0] = '\0';
      gcvt( mInput->pCol[i][0], 18, pcBuffer );
      Fthe_Data_File->SetBuffer( pcBuffer );
      Fthe_Data_File->WriteWord();
      
      Fthe_Data_File->SetBuffer( " " );
      Fthe_Data_File->WriteWord();
      
      pcBuffer[0] = '\0';
      gcvt( mInput->pCol[(iTemp - 1) - i][0], 18, pcBuffer );
      Fthe_Data_File->SetBuffer( pcBuffer );
      Fthe_Data_File->WriteWord();
      
      Fthe_Data_File->SetBuffer( " " );
      Fthe_Data_File->WriteWord();
      
      pcBuffer[0] = '\0';
      gcvt( mOutput->pCol[i][0], 18, pcBuffer );
      Fthe_Data_File->SetBuffer( pcBuffer );
      Fthe_Data_File->WriteWord();
      
      Fthe_Data_File->SetBuffer( " " );
      Fthe_Data_File->WriteWord();
      
   } // end for loop  
   
   Fthe_Data_File->SetBuffer( "\n" );
   Fthe_Data_File->WriteWord();
   
   Fthe_Data_File->CloseFile();
   
   delete[] pcBuffer;
} // end WriteFactFile

