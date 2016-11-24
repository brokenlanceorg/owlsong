//***********************************************************************************************
// File     : Enumeration.hpp
// Purpose  : Provides an interface for an enumeration-type data set.
//          : Derived classes must furnish implementations for the operations
//          : such as GetNextElement and HasMoreElements.
//          : 
// Author   : Brandon Benham 
// Date     : 6/11/00
//***********************************************************************************************
#ifndef __ENUMERATION_HPP
#define __ENUMERATION_HPP


//***********************************************************************************************
// Class    : EnumeratioN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  : Provides an interface for an enumeration-type data set.
//          : Derived classes must furnish implementations for the operations
//          : such as GetNextElement and HasMoreElements.
//          : 
//***********************************************************************************************
class EnumeratioN
{
   public:
      EnumeratioN();  // Default Constructor declaration
      ~EnumeratioN(); // Destructor declaration
      virtual bool HasMoreElements() = 0;
      virtual long double GetNextElement() = 0;
      virtual void AddElement( long double ) = 0;
      virtual void Rewind() { iCurrent_Number = -1; }
      inline int GetCurrentElementNumber() { return iCurrent_Number; }
      int GetNumberOfElements() { return iNumber_Of_Elements; }

   protected:
      void Setup();
      int iNumber_Of_Elements;
      int iCurrent_Number;

   private:
   
}; // end EnumeratioN declaration

#endif

