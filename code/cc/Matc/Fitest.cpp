//***************************************************
// Test file for keydrv.lib
// Test file for t_time.lib
//***************************************************

#include<iostream.h>
#include<fstream.h>
#include<math.h>
#include"t_time.h"

void main()
{
fstream fFile;
const char* filespec = "c:\\bc45\\grph\\temp.txt";
unsigned char buffer;
int testbuff = 0x280;

	fFile.open(filespec, ios::binary | ios::beg | ios::out | ios::trunc);
	cout.setf(ios::hex | ios::internal);
	fFile.write((unsigned char far*)(&testbuff), sizeof(testbuff));
	fFile.write((unsigned char far*)(&buffer), sizeof(buffer));


} // end main
