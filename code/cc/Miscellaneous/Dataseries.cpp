//***********************************************************************************************
// File     : Dataseries.cpp
// Purpose  : 
//          : 
// Author   : Brandon Benham 
// Date     : 6/11/00
//***********************************************************************************************

#include"Dataseries.hpp"          
long DataserieS::lElement_ID = 0;
//***********************************************************************************************
// Class    : DataserieS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
DataserieS::DataserieS() : EnumeratioN(), SerializablE()
{
   Setup();
   vThe_Data_Elements = new VectoR( iSize );
} // end DataserieS default constructor

//***********************************************************************************************
// Class    : DataserieS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Constructor
// Purpose  : Performs the construction actions.
//***********************************************************************************************
DataserieS::DataserieS( char* pcFileName ) : EnumeratioN(), SerializablE( pcFileName )
{
   Setup();
   Load();
} // end DataserieS constructor

//***********************************************************************************************
// Class    : DataserieS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
DataserieS::~DataserieS()
{
   if( bTransient == false )
      Store();
   if( vThe_Data_Elements != 0 )
      delete vThe_Data_Elements;
} // end DataserieS destructor

//***********************************************************************************************
// Class    : DataserieS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void DataserieS::Setup()
{
   vThe_Data_Elements = 0;
   iSize              = 50;
   bTransient         = false;
} // end Setup

//***********************************************************************************************
// Class    : DataserieS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Load
// Purpose  : Load the data elements from a file
// Notes    : In order for the GetNumberOfWords mechanism in the FilesourcE
//          : class to work, we must not have a blank at the end of the 
//          : last line of data.
//***********************************************************************************************
int DataserieS::Load()
{
   if( pcThe_File_Name == 0 ) // the File name hasn't been set yet!
      return 1;
      
   FilereadeR FaFileSrc( pcThe_File_Name );
   
   iSize = FaFileSrc.GetNumberOfWords();
   iNumber_Of_Elements = iSize;
   vThe_Data_Elements  = new VectoR( iSize );

   for( int i = 0; i < iSize; i++ )
      vThe_Data_Elements->pVariables[i] = _atold( FaFileSrc.GetNextWord() );

   return 0; // good return
} // end Load

//***********************************************************************************************
// Class    : DataserieS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Store
// Purpose  : Stores the data elements to a file
// Notes    : In order for the GetNumberOfWords mechanism in the FilesourcE
//          : class to work, we must not have a blank at the end of the 
//          : last line of data.
//***********************************************************************************************
int DataserieS::Store()
{
   if( vThe_Data_Elements == 0 )
      return 0;
      
   if( pcThe_File_Name == 0 ) // Generate the File Name
      GenerateFileName();
      
   ofstream FaFileSrc( pcThe_File_Name );
   FaFileSrc.precision( 18 );

   for( int i = 0; i < iNumber_Of_Elements; i++ )
      FaFileSrc << vThe_Data_Elements->pVariables[i] << " ";

   FaFileSrc << flush;
   FaFileSrc.close();

   return 0; // good return
} // end Store

//***********************************************************************************************
// Class    : DataserieS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : GenerateFileName
// Purpose  : Generates the File Name based on the lElement_ID variable.
//***********************************************************************************************
void DataserieS::GenerateFileName()
{
   pcThe_File_Name = new char[ 25 ];
   pcThe_File_Name[0] = '\0';
   ltoa( ++lElement_ID, pcThe_File_Name, 10 );
   strcat( pcThe_File_Name, ".dat" );
} // end GenerateFileName

//***********************************************************************************************
// Class    : DataserieS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : StoreElementID - DEPRECATED!!!!!!!!!!!
// Purpose  : StoreElementID - DEPRECATED!!!!!!!!!!!
// Notes    : <<<<<<<<<<<<<<<<<NO LONGER USED>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//***********************************************************************************************
void DataserieS::StoreElementID()
{
char acBuffer[64];

   FilesourcE FaFileSrc( "ElementID.dat", _BYTE, 0  );
   if( FaFileSrc.OpenFileWrite() == 0 ) // Something went wrong opening file!
      return;
   acBuffer[0] = '\0';
//   FaFileSrc.SetBuffer( " \n" );
//   FaFileSrc.WriteWord();
   ltoa( lElement_ID, acBuffer, 10 );
   FaFileSrc.SetBuffer( acBuffer );
   FaFileSrc.WriteWord();
   FaFileSrc.SetBuffer( " " );
   FaFileSrc.WriteWord();
   FaFileSrc.CloseFile();
} // StoreElementID

//***********************************************************************************************
// Class    : DataserieS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : HasMoreElements
// Purpose  : Returns true if there are more elements to iterate through
// Notes    : We consider no more when the Current pointer is equal to the
//          : total number of elements in the enumeration.
//          : iCurrent_Number points to the actual position in the C arrary
//          : iNumber_Of_Elements is 1 based while iCurrent_Number is 0 based.
//***********************************************************************************************
bool DataserieS::HasMoreElements()
{
   return iCurrent_Number < (iNumber_Of_Elements - 1);
} // end HasMoreElements

//***********************************************************************************************
// Class    : DataserieS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : GetNextElement
// Purpose  : Returns the next element and updates the iCurrent_Element member.
//***********************************************************************************************
long double DataserieS::GetNextElement()
{
   if( iCurrent_Number >= (iNumber_Of_Elements - 1) )
      return vThe_Data_Elements->pVariables[iNumber_Of_Elements - 1];
   return vThe_Data_Elements->pVariables[++iCurrent_Number];
} // end GetNextElement

//***********************************************************************************************
// Class    : DataserieS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : AddElement
// Purpose  : Adds an element to the enumeration
// Notes    : Performs the folowing:
//          :     if new element exceeds bounds
//          :       resize
//          :       copy old vector into new vector
//          :     add element to end
//***********************************************************************************************
void DataserieS::AddElement( long double ldAdd )
{
   if( iNumber_Of_Elements >= iSize )
      Resize();
   vThe_Data_Elements->pVariables[iNumber_Of_Elements++] = ldAdd;
} // end AddElement

//***********************************************************************************************
// Class    : DataserieS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Resize
// Purpose  : This method resizes the vector without destroying the contents
// Notes    : We double in size each time we need to resize.
//          : We could've used 
//          : 
//          : 
//          : 
//***********************************************************************************************
void DataserieS::Resize()
{
iSize += iNumber_Of_Elements;
VectoR* theNewVector = new VectoR( iSize );
int iOld_Current = iCurrent_Number;
int iLoop = 0;

   Rewind();
   while( HasMoreElements() )
      theNewVector->pVariables[iLoop++] = GetNextElement();

   delete vThe_Data_Elements;
   vThe_Data_Elements = theNewVector;
   iCurrent_Number = iOld_Current;

} // end Resize

//***********************************************************************************************
// Class    : DataserieS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Peel
// Purpose  : 
// Notes    : 
//          : 
//***********************************************************************************************
DataserieS* DataserieS::Peel( int iHowMany )
{
   return 0;
} // end Peel

//***********************************************************************************************
// Class    : DataserieS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : SetName
// Purpose  : 
// Notes    : 
//          : 
//***********************************************************************************************
void DataserieS::SetName( char* pcName )
{
   if( pcThe_File_Name != 0 )
      delete[] pcThe_File_Name;
   pcThe_File_Name = new char[ strlen( pcName ) + 1 ];
   pcThe_File_Name[ 0 ] = '\0';
   strcpy( pcThe_File_Name, pcName );
} // end SetName
