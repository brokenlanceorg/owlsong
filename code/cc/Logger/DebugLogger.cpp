//**************************************************************************
// File	   : DebugLogger.cpp
// Purpose : Defines the DebugloggeR class
//**************************************************************************
#include"DebugLogger.hpp"

// Initialize static members:
int DebugloggeR::_functionLevel = 0;

//**************************************************************************
// Class   : DebugloggeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member  : Default Constructor
// Purpose : Declares the DebugloggeR class, that will handle all of our
//         : debug logging with entries of the form: 
//         : HH:MM:SS +**** <ClassName>::<Method> :FileName, LineNumber
//**************************************************************************
DebugloggeR::DebugloggeR()
{
   Setup();
} // end DebugloggeR default constructor

//**************************************************************************
// Class    : DebugloggeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member   : Constructor
// Purpose  : Declares the DebugloggeR class, that will handle all of our
//          : debug logging with entries of the form: 
//          : HH:MM:SS +**** <ClassName>::<Method> :FileName, LineNumber
//**************************************************************************
DebugloggeR::DebugloggeR( char* pcTheMsg, char* pcFile, int iLine )
{
   Setup();
   _theMessage = new string( pcTheMsg );
   FormatMessage( pcFile, iLine );
} // end DebugloggeR constructor

//**************************************************************************
// Class   : DebugloggeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member  : Constructor
// Purpose : Declares the DebugloggeR class, that will handle all of our
//         : debug logging with entries of the form: 
//         : HH:MM:SS +**** <ClassName>::<Method>
//**************************************************************************
DebugloggeR::DebugloggeR( string ASTheMsg, char* pcFile, int iLine )
{
   Setup();
   _theMessage = new string( ASTheMsg );
   FormatMessage( pcFile, iLine );
} // end DebugloggeR constructor

//**************************************************************************
// Class   : DebugloggeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member  : Destructor
// Purpose : Clean up the memory.
//**************************************************************************
DebugloggeR::~DebugloggeR()
{
   FormatMessage( __FILE__, __LINE__, false );
   
   if( _functionLevel > 0 )
   {
      _functionLevel -= 1;
   }
      
   if( _theMessage != 0 )
   {
      delete _theMessage;
      _theMessage = 0;
   } // end if not null

   if( _theMessageChar != 0 )
   {
      delete _theMessageChar;
      _theMessageChar = 0;
   } // end if not null

   _theFile->close();

   if( _theFile != 0 )
   {
      delete _theFile;
      _theFile = 0;
   } // end if not null
} // end DebugloggeR destructor

//**************************************************************************
// Class   : DebugloggeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member  : Setup
// Purpose : Initialize the data members
//**************************************************************************
void DebugloggeR::Setup()
{
   _theMessageChar = 0;
   _theMessage = 0;
   _functionLevel += 1;
   _theFile = 0;
   _theFile = new fstream();
   string fileLocation( getenv( "HOME" ) );
   fileLocation += "/output.dat";
   _theFile->open( fileLocation.c_str(), ios::out | ios::app );
} // end Setup

//**************************************************************************
// Class    : DebugloggeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member   : FormatMessage
// Purpose  : Constructs the message that will be printed out to the 
//          : file; it is in the form of:
//          : HH:MM:SS +**** <ClassName>::<Method> :FileName, LineNumber
//**************************************************************************
void DebugloggeR::FormatMessage( char* pcFile, int iLine, bool bBegin , bool endInfo )
{
   if( pcFile == 0 )
   {
      pcFile = "";
   }
   string AStheFileName( pcFile );
   ostringstream AStheLineNumber;
   AStheLineNumber << iLine;
   const time_t theTime = time( 0 );
   string AStheMessage( ctime( &theTime ) );

   if( bBegin )
   {
      AStheMessage += " +";
   }
   else
   {
      AStheMessage += " -";
   }
   for( int i=0; i<_functionLevel; i++ )
   {
      AStheMessage += "*";
   }
   AStheMessage += " " + *_theMessage;
   if( endInfo )
   {
      AStheMessage += "\t\t\t\t :" + AStheFileName;
      AStheMessage += ", ";
      *_theFile << AStheMessage.c_str() << endl;
   } // end if endInfo
   else
   {
      *_theFile << AStheMessage.c_str();
   }

} // end FormatMessage

//**************************************************************************
// Class    : DebugloggeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member   : Operator <<
// Purpose  : Handles of our 'easy' outputs
//**************************************************************************
DebugloggeR& DebugloggeR::operator << ( const char* pcTheStr )
{
   if( _theFile != 0 && pcTheStr != 0 ) 
   {
      FormatMessage( 0, 0, true, false );
      *_theFile << "\t" << pcTheStr << endl; 
   }
	
   return *this;
} // end operator << overload

//**************************************************************************
// Class   : DebugloggeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member  : Operator <<
// Purpose : Handles of our 'easy' outputs
//**************************************************************************
DebugloggeR& DebugloggeR::operator << ( string ASTheStr )
{
   if( _theFile != 0 ) 
   {
      FormatMessage( 0, 0, true, false );
      *_theFile << "\t" << ASTheStr.c_str() << endl; 
   }

   return *this;
} // end operator << overload

//**************************************************************************
// Class    : DebugloggeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member   : Operator <<
// Purpose  : Handles of our 'easy' outputs
//**************************************************************************
DebugloggeR& DebugloggeR::operator << ( int iTheInt )
{
   if( _theFile != 0 ) 
   {
      FormatMessage( 0, 0, true, false );
      *_theFile << "\t" << iTheInt << endl; 
   }
	
   return *this;
} // end operator << overload

//**************************************************************************
// Class    : DebugloggeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member   : Operator <<
// Purpose  : Handles of our 'easy' outputs
//**************************************************************************
DebugloggeR& DebugloggeR::operator << ( long lTheLong )
{
   if( _theFile != 0 ) 
   {
      FormatMessage( 0, 0, true, false );
      *_theFile << "\t" << lTheLong << endl; 
   }
	
   return *this;
} // end operator << overload

//**************************************************************************
// Class   : DebugloggeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member  : Operator <<
// Purpose : Handles of our 'easy' outputs
//**************************************************************************
DebugloggeR& DebugloggeR::operator << ( double dTheDoub )
{
   if( _theFile != 0 ) 
   {
      FormatMessage( 0, 0, true, false );
      *_theFile << "\t" << dTheDoub << endl; 
   }
	
   return *this;
} // end operator << overload

//**************************************************************************
// Class   : DebugloggeR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member  : Operator <<
// Purpose : Handles of our 'easy' outputs
//**************************************************************************
DebugloggeR& DebugloggeR::operator << ( long double ldTheDoub )
{
   char buffer[80];

   if( _theFile != 0 ) 
   {
      FormatMessage( 0, 0, true, false );
      ostringstream converter;
      converter << ldTheDoub;
      *_theFile << "\t" << converter.str().c_str() << endl; 
   }

   return *this;
} // end operator << overload
