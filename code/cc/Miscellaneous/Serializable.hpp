//***********************************************************************************************
// File     : Serializable.hpp
// Purpose  : Provides an interface for classes implementing persistence.
//          : Derived classes must implement methods such as Load and Store.
//          : 
// Author   : Brandon Benham 
// Date     : 6/11/00
//***********************************************************************************************
#ifndef __SERIALIZABLE_HPP
#define __SERIALIZABLE_HPP

#include "FileSrc.hpp"
#include "FileReader.hpp"
//***********************************************************************************************
// Class    : SerializablE
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : Provides an interface for classes implementing persistence.
//          : Derived classes must implement methods such as Load and Store.
//***********************************************************************************************
class SerializablE
{
   public:
      SerializablE();         // Default Constructor declaration
      SerializablE( char* );  // Constructor declaration
      ~SerializablE(); // Destructor declaration
      virtual int Load()  = 0;  // Return non-zero on error
      virtual int Store() = 0; // Return non-zero on error
      virtual inline char* GetName() { return pcThe_File_Name; }

   protected:
      void Setup();
      FilesourcE* Fthe_Data_File;
      FilereadeR* _theFileReader;
      char*       pcThe_File_Name;

   private:
   
}; // end SerializablE declaration

#endif

