//***************************************************************************** 
// File     :  Filesrc.cpp                                                    
// Purpose  :  This file contains the implementations for the member
//          :  Functions of the various input functions.
// Author   :  Brandon Benham
//*****************************************************************************
#include"Filesrc.hpp"

//*************************************
// Initial Functions:
//*************************************
int Find_Max(int nX, int nY)
{
   int bTemp = nY;

   if(abs(nX) > abs(nY))
      bTemp = nX;

   return bTemp;
} // end Find_Max/2

//***********************************************************************************
// Class    :  FilesourcE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  :  Defines basic input functions from a file data source
//***********************************************************************************
FilesourcE::FilesourcE()
{
   Setup();
} // end FilesourcE constructor

//***********************************************************************************
// Class    :  FilesourcE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  :  Destructor
//***********************************************************************************
FilesourcE::~FilesourcE()
{
   Fthe_File.close();
   if(pcFile_Name != 0 )
      delete[] pcFile_Name;
   if(pcData_Buffer != 0 )
      delete[] pcData_Buffer; 
} // end destructor

//***********************************************************************************
// Class    :  FilesourcE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  :  Defines basic input functions from a file data source
//          :  This is the real constructor, it sets up the file name
//          :  and the data size members.
// Args     :  _SIZE tells us what are basic character size is, the usual
//          :  size is _BYTE; iReadOnly is 1 if it's only read mode, 2 if
//          :  we want to append to the pcName, or 0 if we want to ios::trunc
//          :  the filename in pcName.
//***********************************************************************************
FilesourcE::FilesourcE(char* pcName, _SIZE f, int iReadOnly)
{
   Setup();
   uData_Size = f;
   iOpen_Type = iReadOnly;
   pcFile_Name = new char[strlen(pcName) + 1];
   pcFile_Name[0] = '\0';
   strcpy(pcFile_Name, pcName);
   if(uData_Size == _BYTE && iReadOnly == 1)
      liFile_Size = GetSize();
   else {
      liFile_Size = 0;
      iLargest_Word = 127;
      pcData_Buffer = new char[iLargest_Word + 10];}
   if( liFile_Size != 0 )
      ClearBuffer();
} // end FilesourcE constructor

//***********************************************************************************
// Class    :  FilesourcE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  Setup
// Purpose  :  Does basic setup stuff
//***********************************************************************************
void FilesourcE::Setup()
{
   fstream Fthe_File();
   pcFile_Name      = 0;
   pcData_Buffer    = 0;
   uData_Size       = _BYTE;
   ucDelimiter      = ' ';
   liFile_Position  = 0;
   liBytes_Written  = 0;
   lNumber_Of_Words = 0;
   usReading        = 0;
   usWriting        = 0;
} // end Setup

//***********************************************************************************
// Class    :  FilesourcE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  SetFileSource
// Purpose  :  Sets the object to a new file source
//***********************************************************************************
void FilesourcE::SetFileSource(char* pcNewName, int iReadOnly, int iBuffSize)
{
int iSize = strlen(pcNewName);

   if(iSize > 0)
   {
      uData_Size = _BYTE;
      iOpen_Type = iReadOnly;
      if(usReading == 1 || usWriting == 1)
         if(!CloseFile()) {
             }
      if(pcFile_Name != 0)
         delete[] pcFile_Name;
      pcFile_Name = new char[iSize + 1];
      pcFile_Name[0] = '\0';
      strcpy(pcFile_Name, pcNewName);
      if(uData_Size == _BYTE && iReadOnly == 1)
         liFile_Size = GetSize();
      else
      {
         if(pcData_Buffer != 0)
            delete[] pcData_Buffer;
         pcData_Buffer = new char[iBuffSize];
      } // end else
   } // end if 
   liFile_Position = 0;
   liBytes_Written = 0;
} // end SetFileSource 

//***********************************************************************************
// Class    :  FilesourcE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  SetDelim
// Purpose  :  Sets the delimiter attribute, and returns previous delimiter
//***********************************************************************************
unsigned char FilesourcE::SetDelim(unsigned char ucC)
{
unsigned char ucPrev = ucDelimiter;

   ucDelimiter = ucC;

   return ucPrev;
} // end SetDelim

//***********************************************************************************
// Class    :  FilesourcE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  SetFileType
// Purpose  :  Sets the data type attribute
//***********************************************************************************
void FilesourcE::SetFileType(_SIZE usS)
{
   uData_Size = usS;
} // end SetFileType

//***********************************************************************************
// Class    :  FilesourcE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  SetBuffer
// Purpose  :  Sets the data
//***********************************************************************************
void FilesourcE::SetBuffer(char* pcData)
{
   if(strlen(pcData) >= 1)
   {
      ClearBuffer();
      strcpy(pcData_Buffer, pcData);
   } // end if
} // end SetBuffer

//***********************************************************************************
// Class    :  FilesourcE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  OpenFileRead
// Purpose  :  Opens a file for reading
// Notes    :  Returns non-zero on good open
//***********************************************************************************
int FilesourcE::OpenFileRead()
{
   liFile_Position = 0;
   if( usReading )
   {
      return 1;
   }
   if( uData_Size == _BYTE )
   {
      Fthe_File.open( pcFile_Name, ios::in );
   }
   else
   {
      Fthe_File.open( pcFile_Name, ios::in | ios::binary /*| ios::nocreate*/ );
   }
   if( !Fthe_File.good() ) // fail or bad can be checked
   {
      return 0;
   }
   else 
   {
      usReading = 1;
      return 1; 
   }
} // end OpenFileRead

//***********************************************************************************
// Class    :  FilesourcE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  OpenFileWrite
// Purpose  :  Opens a file for Writing
// Notes    :  Returns zero on fail, non-zero on success.
//***********************************************************************************
int FilesourcE::OpenFileWrite()
{
   liFile_Position = 0;
   liBytes_Written = 0;
   if(usWriting)
   {
      return 1;
   }
   if((uData_Size == _BYTE) && (iOpen_Type == 2)) // type 2 is append mode
   {
      Fthe_File.open(pcFile_Name, ios::out | ios::app);
   }
   else if(uData_Size == _BYTE)
   {
      Fthe_File.open(pcFile_Name, ios::out | ios::trunc);
   }
   else
   {
      Fthe_File.open(pcFile_Name, ios::out | ios::trunc | ios::binary);
   }

   if(!Fthe_File.good()) // fail or bad can be checked
   {
      return 0;
   }
   else 
   {
      usWriting = 1;
      return 1; 
   }
} // end OpenFileWrite

//***********************************************************************************
// Class    :  FilesourcE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  CloseFile
// Purpose  :  Closes a file after we're done using it
//***********************************************************************************
int FilesourcE::CloseFile()
{
   Fthe_File.close();
   liFile_Position = 0;
   if(!Fthe_File.good())
      return 0;
   else {
      usReading = 0;
      usWriting = 0;
      return 1; }
} // end CloseFile

//***********************************************************************************
// Class    :  FilesourcE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  GetSize
// Purpose  :  Returns the readable file size of a file
//***********************************************************************************
long int FilesourcE::GetSize()
{
char cThe_Char = '\0';
const char EOL = '\n';
int nS = 0;
int nL1 = 1;
int nL2 = 0;

   Fthe_File.open(pcFile_Name, ios::in );
   if(!Fthe_File.good()) // fail or bad can be checked
      return 0;
   while(!Fthe_File.eof())
   {
      nS += 1;
      Fthe_File.get(cThe_Char);
      if((cThe_Char == ucDelimiter) || (cThe_Char == EOL))
      {
         nL2 = Find_Max(nL1, nL2);
         nL1 = 0;
         lNumber_Of_Words++;
      } // end if cThe_Char
      else
      {
         if(isalnum(cThe_Char) || ispunct(cThe_Char))
            nL1 += 1;
      } // end else
   } // end while loop
   Fthe_File.close();
   nL2 = Find_Max(nL1, nL2);
   iLargest_Word = nL2;
   if(pcData_Buffer != 0)
      delete[] pcData_Buffer;
   pcData_Buffer = new char[iLargest_Word + 10]; // porque diez?
   
   return nS;
} // end Get_Size()

//***********************************************************************************
// Class    :  FilesourcE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  GetWord
// Purpose  :  Fills the databuffer with a word from the file stream, and
//          :  returns the length of that word.
//***********************************************************************************
int FilesourcE::GetWord()
{
int iRes = 0;

   switch(uData_Size)
   {
      case _BIT:
      break;

      case _BYTE:
         iRes = GetChar_Word();
      break;

      case _WORD:
      break;

      case _DOUBLEWORD:
      break;

      case _QUADWORD:
         iRes = GetLngDouble_Word();
      break;

      case _OCTLWORD:
      break;

      case _DECAWORD:
      break;
   } // end switch  

   return iRes;
} // end GetWord

//***********************************************************************************
// Class    :  FilesourcE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  GetCharWord
// Purpose  :  Returns a 'word' based on how big our data is
// Notes    :  In the default switch, we check to see if the delimiter is
//          :  a space, if it isn't we check for whatever the delimiter is
//          :  because the whitespace is more typical, and has more than
//          :  just a single character which could be designated as THE
//          :  delimiter to check for.
//***********************************************************************************
int FilesourcE::GetChar_Word()
{
char cThe_Char = '\0';
const char _DELIM = ucDelimiter;
static int nOff_set1 = 0;
int nRet_Val = 0;

   ClearBuffer();
   nOff_set1 = 0;
   while(liFile_Position < liFile_Size)
   {
      liFile_Position += 1;
      Fthe_File.get(cThe_Char);

      switch(cThe_Char)
      {
         case _COMMENT :
            while(cThe_Char != _EOL)
            {
               Fthe_File.get(cThe_Char);
               liFile_Position += 1;
            } // end while loop
         break;
         
         case _EOL :
            if(Fthe_File.eof())
               pcData_Buffer[0] = _END;
            else
               pcData_Buffer[nOff_set1] = '\0';
            nRet_Val = nOff_set1;
            nOff_set1 = 0;
            if(nRet_Val >= 1)
               return nRet_Val;
         break;
         
         default :
            if(ucDelimiter != ' ')
            if(cThe_Char == _DELIM)
            {
               if(nOff_set1 >= 1)
               {
                  pcData_Buffer[nOff_set1] = '\0';
                  nRet_Val = nOff_set1;
                  nOff_set1 = 0;
                  return nRet_Val;
               }
               else
               {
                  nOff_set1 = 0;
                  continue;
               } // end else  
            } // end if

            if(isalnum(cThe_Char) || ispunct(cThe_Char))
            {
               pcData_Buffer[nOff_set1] = cThe_Char;
               nOff_set1 += 1;
            } // end if
            else
            {
               if(isspace(cThe_Char))
               {
                  if(nOff_set1 >= 1)
                  {
                     pcData_Buffer[nOff_set1] = '\0';
                     nRet_Val = nOff_set1;
                     nOff_set1 = 0;
                     return nRet_Val;
                  }
                  else
                  {
                     nOff_set1 = 0;
                     continue;
                  } // end else  
               } // end if whitespace
            } // end else of if alphanumeric
         break; // break for default
      } // end switch
   } // end while not end of file
   pcData_Buffer[0] = _END;

   return nOff_set1;
} // end getCharWord

//***********************************************************************************
// Class    :  FilesourcE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  ClearBuffer
// Purpose  :  Clears the character buffer attribute
//***********************************************************************************
void FilesourcE::ClearBuffer()
{
      pcData_Buffer[0] = '\0';
} // end ClearBuffer

//***********************************************************************************
// Class    :  FilesourcE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  GetLngDouble_Word
// Purpose  :  The function that actually retreives the data from disk
//***********************************************************************************
int FilesourcE::GetLngDouble_Word()
{
int iLDRes = 0;

   //Fthe_File.read( (unsigned char*)(&ldLng_Dble_Buff), sizeof(ldLng_Dble_Buff));
   Fthe_File.read( (char*)(&ldLng_Dble_Buff), sizeof(ldLng_Dble_Buff));
   if(!Fthe_File.good())
      iLDRes = 1;

   return iLDRes;
} // end GetLngDouble_Word

//***********************************************************************************
// Class    :  FilesourcE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  WriteWord
// Purpose  :  Writes the data that is in the respective data buffer
//***********************************************************************************
void FilesourcE::WriteWord()
{
   switch(uData_Size)
   {
      case _BIT:
      break;

      case _BYTE:
         WriteChar_Word();
      break;

      case _WORD:
      break;

      case _DOUBLEWORD:
      break;

      case _QUADWORD:
         WriteQuad_Word();
      break;

      case _OCTLWORD:
      break;

      case _DECAWORD:
      break;
   } // end switch
} // end WriteWord

//***********************************************************************************
// Class    :  FilesourcE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  WriteChar_Word
// Purpose  :  Writes the data that is in the respective data buffer
//***********************************************************************************
void FilesourcE::WriteChar_Word()
{
   liBytes_Written += strlen(pcData_Buffer);
   Fthe_File<<pcData_Buffer;
} // end WriteChar_Word

//***********************************************************************************
// Class    :  FilesourcE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  WriteQuad_Word
// Purpose  :  Writes the data that is in the ldLng_Dble_Buff data buffer
//***********************************************************************************
void FilesourcE::WriteQuad_Word()
{
   liBytes_Written += sizeof(ldLng_Dble_Buff);
   //Fthe_File.write((unsigned char*)(&ldLng_Dble_Buff), sizeof(ldLng_Dble_Buff));
   Fthe_File.write((char*)(&ldLng_Dble_Buff), sizeof(ldLng_Dble_Buff));
} // end WriteQuad_Word
