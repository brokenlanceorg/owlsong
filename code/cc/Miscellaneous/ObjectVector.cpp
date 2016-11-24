//***********************************************************************************************
// File     : ObjectVector.cpp
// Purpose  : This class is a container for any kind of object.
//          : 
// Author   : Brandon Benham 
// Date     : 9/12/01 The Day After
//***********************************************************************************************

#include"ObjectVector.hpp"          

//***********************************************************************************************
// Class    : ObjectVectoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
ObjectVectoR::ObjectVectoR()
{
   Setup();
   Initialize();
} // end ObjectVectoR default constructor

//***********************************************************************************************
// Class    : ObjectVectoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Constructor
// Purpose  : Performs the construction actions.
// Params   : int iSize indicates initial size.
//***********************************************************************************************
ObjectVectoR::ObjectVectoR( int iSize )
{
   Setup();
   _iMaxSize = iSize;
   Initialize();
} // end ObjectVectoR constructor

//***********************************************************************************************
// Class    : ObjectVectoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
// Notes    : The destruction of the objects is up to the user...
//***********************************************************************************************
ObjectVectoR::~ObjectVectoR()
{
   if( theObjects != 0 )
   {
      delete[] theObjects;
      theObjects = 0;
   } // end if not null
} // end ObjectVectoR destructor

//***********************************************************************************************
// Class    : ObjectVectoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void ObjectVectoR::Setup()
{
   _iGrowSize        = 50;
   _iMaxSize         = 100;
   _iCurrentIterator = -1;
   _iCurrentPointer  = 0;
} // end Setup                                 

//***********************************************************************************************
// Class    : ObjectVectoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Initialize
// Purpose  : Performs the actual creation of the vector array.
//***********************************************************************************************
void ObjectVectoR::Initialize()
{
   theObjects = new void*[ _iMaxSize ];
   for( int i=0; i<_iMaxSize; i++ )
   {
      theObjects[ i ] = 0;
   } // end for
} // end Initialize

//***********************************************************************************************
// Class    : ObjectVectoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : AddObject
// Purpose  : Adds an object to the object array, but first checks size.
//***********************************************************************************************
void ObjectVectoR::AddObject( void* theObj )
{
   if( _iCurrentPointer >= _iMaxSize )
   {
      Resize();
   } // end if need resize
   
   theObjects[ _iCurrentPointer++ ] = theObj;
} // end addObject

//***********************************************************************************************
// Class    : ObjectVectoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : NextObject
// Purpose  : Returns the next object in the array. Returns null if past end.
//***********************************************************************************************
void* ObjectVectoR::NextObject()
{
   if( ++_iCurrentIterator < _iMaxSize )
   {
      return theObjects[ _iCurrentIterator ];
   } else
   {
      return 0;
   } // end else
} // end nextObject

//***********************************************************************************************
// Class    : ObjectVectoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Resize
// Purpose  : Resizes the object array in case we've exceeded its boundaries.
//***********************************************************************************************
void ObjectVectoR::Resize()
{
   int iTemp = _iMaxSize;
   _iMaxSize += _iGrowSize;

   void** theNewArray = new void*[ _iMaxSize ];
   for( int i=0; i<_iMaxSize; i++ )
   {
      theNewArray[ i ] = 0;
   } // end for

   for( int i=0; i<iTemp; i++ )
   {
      theNewArray[ i ] = theObjects[ i ];
   } // end for

   void** temp = theObjects;
   theObjects = theNewArray;
   
   delete[] temp;
} // end Resize

//***********************************************************************************************
// Class    : ObjectVectoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : LastObject
// Purpose  : Returns the last object in the array.
//***********************************************************************************************
void* ObjectVectoR::LastObject()
{
   return theObjects[ _iCurrentPointer - 1 ];
} // end nextObject


