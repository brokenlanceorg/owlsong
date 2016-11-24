//***********************************************************************************************
// File     : FileReader.hpp
// Purpose  : This class provides functionality for reading character data from a
//          : file and storing it in an array.
//          : 
//          : 
// Author   : Brandon Benham 
// Date     : 7/14/00
//***********************************************************************************************

#ifndef __FILEREADER__HPP
#define __FILEREADER__HPP

#include<fstream>
//#include<iostream>

using namespace std;

//***********************************************************************************************
// Class    : FilereadeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : Reads in character files that contain "words" separated by
//          : delimeters.
//***********************************************************************************************
class FilereadeR
{
   public:
      // file name, delimiter string, intial size, step size:
      FilereadeR( char*, const char* = " ", int = 50, int = 20 ); 
      ~FilereadeR();

      char*  GetNextWord();
      char** GetArrayOfWords() { return ppcThe_Words; }
      void   Rewind() { iCurrent_Word = -1; }
      int    GetNumberOfWords() { return iNumber_Of_Words; }

   protected:
      char** ppcThe_Words;
      int    iNumber_Of_Words; // 1-based
      int    iSize_Of_Array;
      int    iStep_Size;
      int    iCurrent_Word; // 0-based
      char*  pcDelimiter;

      void Setup();
      void AddWord( char* );
      void AddWordToArray( char*, char**, int ); // pointer to word, pointer to array, and index
      void DeleteArray();
      void BuildArray( char* ); // file name
      void ResizeArray();

   private:

}; // end class declaration FilereadeR

#endif
