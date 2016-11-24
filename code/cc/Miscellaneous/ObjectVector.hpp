//***********************************************************************************************
// File     : ObjectVector.hpp
// Purpose  : This class is a container for any kind of object.
//          : 
//          : 
// Author   : Brandon Benham 
// Date     : 9/12/01 The Day After
//***********************************************************************************************
#ifndef __OBJECTVECTOR_HPP
#define __OBJECTVECTOR_HPP

//***********************************************************************************************
// Class    : ObjectVectoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : This class is a container for any kind of object.
//          : Similar in nature to Java's Vector or ArrayList classes.
//***********************************************************************************************
class ObjectVectoR
{
   public:
      ObjectVectoR(); // Default Constructor declaration
      ObjectVectoR( int ); // Constructor declaration
      ~ObjectVectoR(); // Destructor declaration

      void AddObject( void* ); // Add an object to the data store
      void* NextObject();      // Get the next object
      void* LastObject();      // Get the last object
      void Rewind() { _iCurrentIterator = -1; }           // Go back to the first element
      int  GetSize() { return _iCurrentPointer; }

   protected:
      void Setup();
      void Initialize();
      void Resize();           // Resize the array bigger
             
   private:
      int _iGrowSize;
      int _iMaxSize;
      int _iCurrentIterator;
      int _iCurrentPointer;
      void** theObjects;
   
}; // end ObjectVectoR declaration

#endif

