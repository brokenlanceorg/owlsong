#include<iostream>
#include<fstream.h>
//#include<time.h>

using namespace std;

double getNumber( int num )
{
   double theNumber = (int)num;
   theNumber *= 3.14159;
   return theNumber;
}

void getTime()
{
   const time_t theTime = time( 0 );
   char* theString = ctime( &theTime );
   cout << "The time string is: " << theString << endl;
}

void getEnv()
{
	cout << "The HOME dir is: " << getenv( "HOME" ) << endl;
}

//---------------------------------------------------------------------------
// Main function
//---------------------------------------------------------------------------
main( int argc, char *argv[] )
{
	cout << "Hello World" << endl;
	cout << "The Number is: " << getNumber( 2.718 ) << endl;
   getTime();
   getEnv();
   fstream* fileStream = new fstream();
   //fileStream->open( "afile.txt", ios::app | ios::beg );
   fileStream->open( "afile.txt", ios::app | ios::trunc );
}
