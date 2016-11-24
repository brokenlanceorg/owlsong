//***************************************************************************** 
// File		:	Prime.cpp
// Purpose	:	Writes out to disk the first 30 prime numbers
// Author	:  Brandon Benham
//*****************************************************************************
#include<iostream.h>                                              
#include<fstream.h>                                              
#include<conio.h>

void main()
{
long int ldaThePrimes[30];
int iCounter = 0;
fstream FtheFile;

	for(iCounter=0;iCounter<30;iCounter++)
		ldaThePrimes[iCounter] = 0;

	ldaThePrimes[0] = 2;
	ldaThePrimes[1] = 3;
	ldaThePrimes[2] = 5;
	ldaThePrimes[3] = 7;
	ldaThePrimes[4] = 11;
	ldaThePrimes[5] = 13;
	ldaThePrimes[6] = 17;
	ldaThePrimes[7] = 19;
	ldaThePrimes[8] = 23;
	ldaThePrimes[9] = 29;
	ldaThePrimes[10] = 31;
	ldaThePrimes[11] = 37;
	ldaThePrimes[12] = 41;
	ldaThePrimes[13] = 43;
	ldaThePrimes[14] = 47;
	ldaThePrimes[15] = 53;
	ldaThePrimes[16] = 59;
	ldaThePrimes[17] = 61;
	ldaThePrimes[18] = 67;
	ldaThePrimes[19] = 71;
	ldaThePrimes[20] = 73;
	ldaThePrimes[21] = 79;
	ldaThePrimes[22] = 83;
	ldaThePrimes[23] = 89;
	ldaThePrimes[24] = 97;
	ldaThePrimes[25] = 101;
	ldaThePrimes[26] = 103;
	ldaThePrimes[27] = 107;
	ldaThePrimes[28] = 109;
	ldaThePrimes[29] = 113;
	ldaThePrimes[30] = 127;

	FtheFile.open("primes.dat", ios::out | ios::binary | ios::trunc);

	for(iCounter=0;iCounter<30;iCounter++)
		FtheFile.write((unsigned char*)&ldaThePrimes[iCounter], 
			sizeof(ldaThePrimes[iCounter]));

	FtheFile.close();
} // end main
