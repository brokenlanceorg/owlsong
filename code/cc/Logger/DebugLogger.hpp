//**************************************************************************
// File	   : DebugLogger.hpp
// Purpose : Declares the DebugloggeR class
//**************************************************************************
#ifndef __DEBUGLOGGER_HPP
#define __DEBUGLOGGER_HPP

#include<sstream>
#include<fstream>

#define __DEBUG__

// for the string class
using namespace std;

//**************************************************************************
// Class   : DebugloggeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose : Declares the DebugloggeR class, that will handle all of our
//         : debug logging with entries of the form: 
//         : HH:MM:SS +**** <ClassName>::<Method> :FileName, LineNumber
//         : 
//**************************************************************************
class DebugloggeR
{
   protected:

      // Protected data members:
      static int _functionLevel;

      //static TDateTime TDTthe_Date_Time;
      fstream* _theFile;

      // We use the string to "touch up" the message:
      string* _theMessage;

      // We use the char* to "output" the message:
      char* _theMessageChar;      

      // Protected member functions:
      void Setup();

      // FileName, LineNumber, if we want "+"
      void FormatMessage( char*, int, bool = true , bool = true );
   
   public:

      // Public functions:
      DebugloggeR();
      DebugloggeR( char*, char* = __FILE__, int = __LINE__ );
      DebugloggeR( string, char* = __FILE__, int = __LINE__ );
      ~DebugloggeR();

      // Used for testing purposes:
      string GetMessage() { return *_theMessage; }

      // Overloaded Operators:
      DebugloggeR& DebugloggeR::operator << ( const char* );
      DebugloggeR& DebugloggeR::operator << ( string );
      DebugloggeR& DebugloggeR::operator << ( int );
      DebugloggeR& DebugloggeR::operator << ( long );
      DebugloggeR& DebugloggeR::operator << ( double );
      DebugloggeR& DebugloggeR::operator << ( long double );
   
}; // end DebugloggeR declaration

#ifdef __DEBUG__
	#define FPRINT( msg ) DebugloggeR FPRINT( msg, __FILE__, __LINE__ );
#else
	#define FPRINT( msg );
#endif // ifdef __DEBUG__
      
#endif
