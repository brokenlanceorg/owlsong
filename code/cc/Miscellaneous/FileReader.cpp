//***********************************************************************************************
// File     : FileReader.cpp
// Purpose  : This class provides functionality for reading character data from a
//          : file and storing it in an array.
//          : 
//          : 
// Author   : Brandon Benham 
// Date     : 7/11/00
//***********************************************************************************************

#include "FileReader.hpp"

//***********************************************************************************************
// Class    : FilereadeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//***********************************************************************************************
FilereadeR::FilereadeR( char* pcFileName, const char* pcDel, int initialSize, int stepSize )
{
   Setup();
   iSize_Of_Array   = initialSize;  // initial size of array
   iStep_Size       = stepSize;  // increment size
   pcDelimiter = new char[ strlen(pcDel) + 1 ];
   pcDelimiter[0] = '\0';
   strcpy( pcDelimiter, pcDel );
   ppcThe_Words = new char*[ iSize_Of_Array ];
   BuildArray( pcFileName );
}  // end constructor

//***********************************************************************************************
// Class    : FilereadeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//***********************************************************************************************
FilereadeR::~FilereadeR()
{
   DeleteArray();
}  // end destructor

//***********************************************************************************************
// Class    : FilereadeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//***********************************************************************************************
void FilereadeR::Setup()
{
   ppcThe_Words     = 0;
   iNumber_Of_Words = 0;   // this counter is 1-based
   iCurrent_Word    = -1;   // this counter is 1-based
   iSize_Of_Array   = 50;  // initial size of array
   iStep_Size       = 20;  // increment size
   pcDelimiter      = " ";
} // end Setup

//***********************************************************************************************
// Class    : FilereadeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//***********************************************************************************************
void FilereadeR::BuildArray( char* pcFileName )
{
   int size = 1024;
   char pcTheLine[ size ]; // do we need to null out this array?
   char* pcTheWord = 0;
   fstream theFile( pcFileName, ios::in );

   if( theFile.fail() )
      return;

   while( !theFile.eof() ) // file is the file object
   {
      pcTheLine[ 0 ] = '\0';
      theFile.getline( pcTheLine, size ); 

      if( pcTheLine[0] == '/' && pcTheLine[1] == '/' )
         continue;

      if( !pcTheLine )
         continue;

      if( pcTheLine[0] == '\0' )
         continue;

      pcTheWord = strtok( pcTheLine, pcDelimiter );
      AddWord( pcTheWord );
      while( (pcTheWord = strtok( NULL, pcDelimiter )) != NULL )
      {
         AddWord( pcTheWord );
      } // end while loop

   } // end while loop

   theFile.close();
} // end BuildArray

//***********************************************************************************************
// Class    : FilereadeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//***********************************************************************************************
void FilereadeR::DeleteArray()
{
   for( int i = 0; i < iNumber_Of_Words; i++ )
      delete[] ppcThe_Words[ i ];
   delete[] ppcThe_Words;
} // end DeleteArray

//***********************************************************************************************
// Class    : FilereadeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//***********************************************************************************************
void FilereadeR::ResizeArray()
{
   iSize_Of_Array = iNumber_Of_Words + iStep_Size;    // set our new size
   char** ppcTempArray = new char*[ iSize_Of_Array ]; // create a new array

   // now we need to copy all elements into this new array:
   for( int i = 0; i < iNumber_Of_Words; i++ )
      AddWordToArray( ppcThe_Words[ i ], ppcTempArray, i );
   DeleteArray();
   ppcThe_Words = ppcTempArray;
} // end ResizeArray

//***********************************************************************************************
// Class    : FilereadeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//***********************************************************************************************
void FilereadeR::AddWordToArray( char* pcWord, char** ppcArray, int iWhere )
{
   ppcArray[ iWhere ] = new char[ strlen( pcWord ) + 1 ];
   ppcArray[ iWhere ][0] = '\0';
   strcpy( ppcArray[ iWhere ], pcWord );
} // end AddWordToArray

//***********************************************************************************************
// Class    : FilereadeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//***********************************************************************************************
void FilereadeR::AddWord( char* pcWord )
{
   AddWordToArray( pcWord, ppcThe_Words, iNumber_Of_Words++ );
   if( iNumber_Of_Words == iSize_Of_Array ) // we may actually leave a slot open
      ResizeArray();
} // end AddWord

//***********************************************************************************************
// Class    : FilereadeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : GetNextWord
// Purpose  : Returns the next word from the file.
// Notes    : Return null when no more words exist.
//***********************************************************************************************
char* FilereadeR::GetNextWord()
{
   if( iCurrent_Word == (iNumber_Of_Words - 1) )
      return 0;
   return ppcThe_Words[ ++iCurrent_Word ];
} // end GetNextWord

