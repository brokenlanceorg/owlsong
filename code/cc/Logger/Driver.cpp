#include"DebugLogger.hpp"
#include<iostream.h>

main( int argc, char *argv[] )
{
   cout << "The file is: " << __FILE__ << endl;
   cout << "The line is: " << __LINE__ << endl;

   FPRINT( "main" );
   FPRINT << "This is a test message from FPRINT";

   double pi = 3.14159278;
   FPRINT << "here's pi: ";
   FPRINT << pi;
}
