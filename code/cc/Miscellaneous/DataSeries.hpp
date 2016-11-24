//***********************************************************************************************
// File     : Dataseries.hpp
// Purpose  : This class captures the behavior of a data-series element.
//          : It implements the interface EnumeratioN and since it also
//          : implements serializable, it can be persisted to file.
//          : 
// Author   : Brandon Benham 
// Date     : 6/11/00
//***********************************************************************************************
#ifndef __DATASERIES_HPP
#define __DATASERIES_HPP

#include "Enumeration.hpp"
#include "Serializable.hpp"
#include "Mathmatc.hpp"
#include "Filesrc.hpp"
//***********************************************************************************************
// Class    : DataserieS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : This class captures the behavior of a data-series element.
//          : It implements the interface EnumeratioN and since it also
//          : implements serializable, it can be persisted to file.
//***********************************************************************************************
class DataserieS : public EnumeratioN, SerializablE
{
   public:
      DataserieS();         // Default Constructor declaration
      DataserieS( char* );  // Constructor declaration for fileName
      ~DataserieS(); // Destructor declaration

      // Inherited pure virtual methods:
      virtual int Load();  // Return non-zero on error
      virtual int Store(); // Return non-zero on error
      // Inherited from EnumeratioN:
      virtual inline bool HasMoreElements();
      virtual long double GetNextElement();
      virtual void AddElement( long double );
      virtual DataserieS* Peel( int ); // "peels" off the last int elements
      inline int GetSize() { return iSize; }
      inline void SetSize( int i ) { iSize = i; }
      long GetID() { return lElement_ID; }
      virtual inline char* GetName() { return pcThe_File_Name; }
      virtual inline void  SetName( char* );
      inline int GetNumberOfElements() { return EnumeratioN::GetNumberOfElements(); }
      virtual void SetTransient( bool bT ) { bTransient = bT; }
                                                
   protected:
      void Setup();
      void GenerateFileName();
      void StoreElementID();
      void Resize();
      VectoR* vThe_Data_Elements;
      int iSize; // Maintains our size of the set, used when we re-size the set
                           
   private:
      static long lElement_ID; // from this member, Peel will generate a file name
      bool        bTransient;
   
}; // end DataserieS declaration

#endif

