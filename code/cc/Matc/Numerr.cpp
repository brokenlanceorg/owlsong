//***************************************************************************** 
// File     :  Numerr.cpp
// Purpose  :  Contains the implementations for the NumErr class
// Author   :  Brandon Benham
//*****************************************************************************
#include"Numerr.hpp"
#include<stdlib.h>

//************************************************************************
// Class    :  NumerR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  Default Constructor
// Purpose  :  This class defines an error object used to handle the
//          :  various errors encountered in numerical computations.
// Notes    :  If usLogging is greater than or equal to 2, then we
//          :  write data out to a log file.
//************************************************************************
NumerR::NumerR()
{
   Kind_Of_Error = _DIVIDE_BY_ZERO_;
   TheUsersFunc = NULL;
   Setup();
} // end constructor  

//************************************************************************
// Class    :  NumerR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  Constructor
// Purpose  :  This class defines an error object used to handle the
//          :  various errors encountered in numerical computations.
//************************************************************************
NumerR::NumerR(_ERROR_TYPE_ TheKind)
{
   Kind_Of_Error = TheKind;
   TheUsersFunc = NULL;
   Setup();
} // end constructor

//************************************************************************
// Class    :  NumerR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  Constructor
// Purpose  :  This class defines an error object used to handle the
//          :  various errors encountered in numerical computations.
//************************************************************************
NumerR::NumerR(_ERROR_TYPE_ TheKind, long double (*UserFunc)(long double ldX))
{
   Kind_Of_Error = TheKind;
   TheUsersFunc = UserFunc;
   Setup();
} // end constructor

//************************************************************************
// Class    :  NumerR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  Default destructor
// Purpose  :  This class defines an error object used to handle the
//          :  various errors encountered in numerical computations.
//************************************************************************
NumerR::~NumerR()
{
/* NumerR::FlogFile->CloseFile();
   delete NumerR::FlogFile; */
   if( pcRoutine_Name != 0 )
   {
      delete[] pcRoutine_Name;
      pcRoutine_Name = 0;
   } // end if not null
   if( pcFunction_Name != 0 )
   {
      delete[] pcFunction_Name;
      pcFunction_Name = 0;
   } // end if not null
} // end destructor

//************************************************************************
// Class    :  NumerR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  Copy Constructor
// Purpose  :  This class defines an error object used to handle the
//          :  various errors encountered in numerical computations.
//************************************************************************
NumerR::NumerR(const NumerR& rhs)
{
   Kind_Of_Error = rhs.Kind_Of_Error;
   TheUsersFunc = rhs.TheUsersFunc;
   ldGood_Func_Value = rhs.ldGood_Func_Value;
   sHandled = rhs.sHandled;
} // end copy constructor  

//************************************************************************
// Class    :  NumerR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  Setup
// Purpose  :  Does the ol'setup stuff
//************************************************************************
void NumerR::Setup()
{
   pcRoutine_Name = new char[64];
   pcFunction_Name = new char[64];
   usLogging = 0;
   sHandled = 0;
} // end Setup

//************************************************************************
// Class    :  NumerR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  SetLogging
// Purpose  :  Sets our logging variable
//************************************************************************
void NumerR::SetLogging(unsigned short usOnOff)
{
   usLogging = usOnOff;
   /**
    * We no longer need use of this static FilesourcE object:
    *
   if(usLogging)
      if(!NumerR::FlogFile->OpenFileWrite())
         cout<<"Error opening log file!\n";
   else
   if(usLogging == 0)
      if(!NumerR::FlogFile->CloseFile())
         cout<<"Error closing log file!\n";
    */
} // end SetLogging

//************************************************************************
// Class    :  NumerR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  SetRoutineName
// Purpose  :  Sets our Routine name variable
// Notes    :  DEPRECATED -- No Longer in Use!!!!!!!!!!!!!
//************************************************************************
void NumerR::SetRoutineName(char* pcRoutine)
{
   /**
    *  DEPRECATED -- No Longer in Use!!!!!!!!!!!!!
    *
   if(pcRoutine_Name != NULL)
      delete[] pcRoutine_Name;
   pcRoutine_Name = new char[strlen(pcRoutine) + 1];
   pcRoutine_Name[0] = '\0';
   strcpy(pcRoutine_Name, pcRoutine);
   if(usLogging >= 2) {
      NumerR::FlogFile->SetBuffer("\nControl now inside ");
      NumerR::FlogFile->WriteWord();
      NumerR::FlogFile->SetBuffer(pcRoutine_Name);
      NumerR::FlogFile->WriteWord();
      NumerR::FlogFile->SetBuffer(" routine.\n");
      NumerR::FlogFile->WriteWord();
   } // end if              
    */
} // end RoutineName

//************************************************************************
// Class    :  NumerR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  SetFunctionName
// Purpose  :  Sets our Routine name variable
// Notes    :  DEPRECATED -- No Longer in Use!!!!!!!!!!!!!
//************************************************************************
void NumerR::SetFunctionName(char* pcFunction)
{
   /**
    *  DEPRECATED -- No Longer in Use!!!!!!!!!!!!!
    *
   if(pcFunction_Name != NULL)
      delete[] pcFunction_Name;
   pcFunction_Name = new char[strlen(pcFunction) + 1];
   pcFunction_Name[0] = '\0';
   strcpy(pcFunction_Name, pcFunction);
   if(usLogging >= 2) {
      NumerR::FlogFile->SetBuffer("\nAttempting to execute the ");
      NumerR::FlogFile->WriteWord();
      NumerR::FlogFile->SetBuffer(pcFunction_Name);
      NumerR::FlogFile->WriteWord();
      NumerR::FlogFile->SetBuffer(" Function.\n");
      NumerR::FlogFile->WriteWord();
   } // end if              
    */
} // end FunctionName

//************************************************************************
// Class    :  NumerR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  HandleError
// Purpose  :  This member function should successfully handle one of
//          :  the three error types
//************************************************************************
long double NumerR::HandleError()
{
long double ldRetVal = 42;

   /**
    *  DEPRECATED -- No Longer in Use!!!!!!!!!!!!!
    *
   switch(Kind_Of_Error)
   {
      case _OVERFLOW:
         //cout<<"Caught an OVERFLOW!"<<endl;
         if(usLogging) {
            NumerR::FlogFile->SetBuffer("\nCaught an OVERFLOW! error in the ");
            NumerR::FlogFile->WriteWord();
            ReportIt(); }
         sHandled = 1;                      
      break;             
      case _UNDERFLOW:
         //cout<<"Caught an UNDERFLOW!"<<endl;
         if(usLogging) {
            NumerR::FlogFile->SetBuffer("\nCaught an UNDERFLOW! error in the ");
            NumerR::FlogFile->WriteWord();
            ReportIt(); }
         sHandled = 1;
      break;         
      case _DIVIDE_BY_ZERO:
         cout<<"Caught a DIVIDE_BY_ZERO!"<<endl;
         if(usLogging) {
            NumerR::FlogFile->SetBuffer("\nCaught a DIVIDE_BY_ZERO! in the ");
            NumerR::FlogFile->WriteWord();
            ReportIt(); }
         sHandled = 1;
      break;         
   } // end switch
   */
   return ldRetVal;
} // end HandleError

//************************************************************************
// Class    :  NumerR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  ReportIt
// Purpose  :  This member function sends the typical ascii data out to
//          :  our log file.
//          :  Here is the typedef of the _mexcep data type:
//{
//  DOMAIN = 1,    /* argument domain error -- log (-1)        */
//  SING,          /* argument singularity  -- pow (0,-2))     */
//  OVERFLOW,      /* overflow range error  -- exp (1000)      */
//  UNDERFLOW,     /* underflow range error -- exp (-1000)     */
//  TLOSS,         /* total loss of significance -- sin(10e70) */
//  PLOSS,         /* partial loss of signif. -- not used      */
//  STACKFAULT     /* floating point unit stack overflow       */
//}   _mexcep;
//************************************************************************
void NumerR::ReportIt()
{
   /**
    *  DEPRECATED -- No Longer in Use!!!!!!!!!!!!!
    *
   NumerR::FlogFile->SetBuffer(pcRoutine_Name);
   NumerR::FlogFile->WriteWord();
   NumerR::FlogFile->SetBuffer(" Routine while executing the ");
   NumerR::FlogFile->WriteWord();
   NumerR::FlogFile->SetBuffer(pcFunction_Name);
   NumerR::FlogFile->WriteWord();
   NumerR::FlogFile->SetBuffer(" Function.");
   NumerR::FlogFile->WriteWord();
   GetErrno();
    */
} // end ReportIt           

//************************************************************************
// Class    :  NumerR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  GetErrno
// Purpose  :  This member function sends the typical ascii data out to
//          :  our log file.
//          :  In addition, errno.h contains the following for errno:
//#define ENOENT   2      /* No such file or directory*/
//#define EMFILE   4      /* Too many open files      */
//#define EACCES   5      /* Permission denied        */
//#define EBADF    6      /* Bad file number          */
//#define ENOMEM   8      /* Not enough core          */
//#define EFAULT  14      /* Unknown error            */
//#define ENODEV  15      /* No such device           */
//#define EINVAL  19      /* Invalid argument         */
//#define E2BIG   20      /* Arg list too long        */
//#define ENOEXEC 21      /* Exec format error        */
//#define EXDEV   22      /* Cross-device link        */
//#define ENFILE  23      /* Too many open files      */
//#define ECHILD  24      /* No child process         */
//#define ENOTTY  25      /* UNIX - not MSDOS         */
//#define ETXTBSY 26      /* UNIX - not MSDOS         */
//#define EFBIG   27      /* UNIX - not MSDOS         */
//#define ENOSPC  28      /* No space left on device  */
//#define ESPIPE  29      /* Illegal seek             */
//#define EROFS   30      /* Read-only file system    */
//#define EMLINK  31      /* UNIX - not MSDOS         */
//#define EPIPE   32      /* Broken pipe              */
//#define EDOM    33      /* Math argument            */
//#define ERANGE  34      /* Result too large         */
//#define EEXIST  35      /* File already exists      */
//#define EDEADLOCK 36    /* Locking violation        */
//#define EPERM   37      /* Operation not permitted  */
//#define ESRCH   38      /* UNIX - not MSDOS         */
//#define EINTR   39      /* Interrupted function call */
//#define EIO     40      /* Input/output error       */
//#define ENXIO   41      /* No such device or address */
//#define EAGAIN  42      /* Resource temporarily unavailable */
//#define ENOTBLK 43      /* UNIX - not MSDOS         */
//#define EBUSY   44      /* Resource busy            */
//#define ENOTDIR 45      /* UNIX - not MSDOS         */
//#define EISDIR  46      /* UNIX - not MSDOS         */
//#define EUCLEAN 47      /* UNIX - not MSDOS         */
//************************************************************************
void NumerR::GetErrno()
{
char acTemp[15];

   /**
    *  DEPRECATED -- No Longer in Use!!!!!!!!!!!!!
    *
   NumerR::FlogFile->SetBuffer(" The c errno value is: ");
   NumerR::FlogFile->WriteWord();
   acTemp[0] = '\0';
   itoa(errno, acTemp, 10);
   NumerR::FlogFile->SetBuffer(acTemp);
   NumerR::FlogFile->WriteWord();

   switch(errno)
   {
      case 0 :
         NumerR::FlogFile->SetBuffer(" (No error).");
      break;
      case 2 :
         NumerR::FlogFile->SetBuffer(" (No Such file or directory).");
      break;
      case 4 :
         NumerR::FlogFile->SetBuffer(" (Too many open files).");
      break;
      case 8 :                                     
         NumerR::FlogFile->SetBuffer(" (Not enough core memory).");
      break;                                       
      case 14 :
         NumerR::FlogFile->SetBuffer(" (Unknown dos error).");
      break;
      case 23 :
         NumerR::FlogFile->SetBuffer(" (Too many open files).");
      break;
      case 32 :
         NumerR::FlogFile->SetBuffer(" (Broken Pipe).");
      break;
      case 33 :
         NumerR::FlogFile->SetBuffer(" (Math function argument).");
      break;
      case 34 :
         NumerR::FlogFile->SetBuffer(" (Result too large).");
      break;
      case 37 :
         NumerR::FlogFile->SetBuffer(" (Operation not permitted).");
      break;
      case 39 :
         NumerR::FlogFile->SetBuffer(" (Interrupted function call).");
      break;
      case 40 :
         NumerR::FlogFile->SetBuffer(" (Input/Output device error).");
      break;
      case 41 :
         NumerR::FlogFile->SetBuffer(" (No such device or address).");
      break;
      default :                                       
         NumerR::FlogFile->SetBuffer(" (Unknown Value). ");
      break;
   } // end switch
   NumerR::FlogFile->WriteWord();

   NumerR::FlogFile->SetBuffer(" The c _doserrno value is: ");
   NumerR::FlogFile->WriteWord();
   acTemp[0] = '\0';
   itoa(_doserrno, acTemp, 10);
   NumerR::FlogFile->SetBuffer(acTemp);
   NumerR::FlogFile->WriteWord();
     */

} // end GetErrno
