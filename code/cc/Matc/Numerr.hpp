//***************************************************************************** 
// File     :  Numerr.hpp
// Purpose  :  Contains the declarations for the NumErr class
// Author   :  Brandon Benham
//*****************************************************************************
#ifndef __NUMERR_HPP
#define __NUMERR_HPP

#include"Filesrc.hpp"

typedef enum e_type
{
   _OVERFLOW_,
   _UNDERFLOW_,
   _DIVIDE_BY_ZERO_,
} _ERROR_TYPE_;

//************************************************************************
// Class    :  NumerR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose  :  This class defines an error object used to handle the
//          :  various errors encountered in numerical computations.
//************************************************************************
class NumerR
{
   protected:
      _ERROR_TYPE_ Kind_Of_Error;
      long double (*TheUsersFunc)(long double ldX);
      long double ldGood_Func_Value;
      unsigned short usLogging;  // logical variable for logging to file
      char* pcRoutine_Name;
      char* pcFunction_Name;

   public:
      NumerR();
      NumerR(_ERROR_TYPE_ TheKind);
      NumerR(_ERROR_TYPE_ TheKind, long double (*UserFunc)(long double ldX));
      ~NumerR();
      NumerR(const NumerR& rhs);
      // static FilesourcE* FlogFile; We *really* don't need this anymore....
      void Setup();
      void SetLogging(unsigned short);
      void SetRoutineName(char*);
      void SetFunctionName(char*);
      void ReportIt();
      void GetErrno();
      long double GetGoodVal() {return ldGood_Func_Value;}
      void SetGoodVal(long double ldG) {ldGood_Func_Value = ldG;}
      void SetErrorType(_ERROR_TYPE_ It) {Kind_Of_Error = It; sHandled = 0;}
      long double HandleError();
      short sHandled;
}; // end class declaration

#endif

