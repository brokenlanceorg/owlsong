//***********************************************************************************
// File     :  Filesrc.hpp
// Purpose  :  This file contains the class declarations for the input library
//***********************************************************************************
#ifndef __FILESRC_HPP
#define __FILESRC_HPP

#include<fstream>

using namespace std;

//**************************************
// Common constants in use:
const char _EOL = '\n';
const char _COMMENT = '/';
const char _END = '*';

//**************************************
// Typedef for the data type argument
typedef enum filesizearg
{
   _BIT,
   _BYTE,
   _WORD,
   _DOUBLEWORD,
   _QUADWORD,
   _OCTLWORD,
   _DECAWORD
} _SIZE;

//***********************************************************************************
// Class    :  FilesourcE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  :  Defines basic input functions from a file data source
//***********************************************************************************
class FilesourcE
{
   protected:
      _SIZE uData_Size;    // size of our "word"
      char* pcFile_Name;   // pointer to file name string
      char* pcData_Buffer; // data buffer for data collection
      unsigned char ucBit_Buff;
      unsigned char ucDelimiter;
      unsigned int uiInt_Buff;
      unsigned long ulLong_Buff;
      float fFloat_Buff;
      double dDouble_Buff;
      long double ldLng_Dble_Buff;
      int iLargest_Word;
      long lNumber_Of_Words;
      long int liFile_Size;
      long int liFile_Position;
      long int liBytes_Written;
      unsigned short usReading;
      unsigned short usWriting;
      int iOpen_Type;
      fstream Fthe_File;   // handle to the data file
      // protected functions
      void WriteChar_Word();
      void WriteQuad_Word();
                            
   public:
      FilesourcE(); // constuctor
      FilesourcE(char* , _SIZE = _BYTE, int iReadOnly = 1);
      ~FilesourcE();
      void Setup();
      long int GetSize();
      unsigned char GetDelim(){return ucDelimiter;}
      unsigned char SetDelim(unsigned char );
      void SetFileSource(char*, int = 1, int = 64);
      void SetFileType(_SIZE);
      inline void SetFileSize(long int liSiz) {liFile_Size = liSiz;}
      int GetWord();
      int GetChar_Word();
      int GetInt_Word();
      int GetLong_Word();
      int GetFloat_Word();
      int GetDouble_Word();
      int GetLngDouble_Word();
      int OpenFileRead();
      int OpenFileWrite();
      int CloseFile();
      inline void ClearBuffer();
      void WriteWord();
      inline void SetQuadBuffer(long double ldX) { ldLng_Dble_Buff = ldX; }
      inline long double GetQuadBuffer() { return ldLng_Dble_Buff; }
      inline int GetLargest_Word() { return iLargest_Word; }
      inline long GetNumberOfWords() { return lNumber_Of_Words; }
      inline char* RetWord() { return pcData_Buffer; }
      void SetBuffer(char*);
}; // end FilesoucE declaration


#endif
