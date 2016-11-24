//*********************************************************************
// Author      : Brandon Benham
// Purpose     : A class implementation that outputs a raster stream
//             : into a compressed RLE Targa format.
//*********************************************************************
#ifndef __TARGA_HPP
#define __TARGA_HPP

//#include<math.h>
//#include<stdlib.h>
//#include<string.h>
#include<fstream>

using namespace std;

typedef unsigned char BYTE;
typedef unsigned short int WORD;
typedef unsigned long int DWORD;

typedef struct
{
   BYTE b,g,r;
} BGRcolortype;

typedef BGRcolortype TARGApalette[768];

const BYTE TRUE_COLOR = 0x02;
const BYTE TRUE_COLOR_RLE = 0x0a;
const BYTE ZERO = 0x00;
const BYTE REPEAT = 0x80;
const BYTE LITERAL = 0x00;
const BYTE LEFT_BOTTOM = 0x00;
const BYTE RIGHT_BOTTOM = 0x10;
const BYTE LEFT_TOP = 0x20;
const BYTE RIGHT_TOP = 0x30;
                   
class TargA
{
   protected:

      TARGApalette cPal;
      short int cnBuffer[164];
      short int cnWidth;
      short int cnHeight;
      short int cCurr_char;
      short int cLast_char;
      short int cLine_Count;
      short int cBuffer_Count;
      short int cCount_byte;
      unsigned char cOut_byte;
      fstream fOutFile;
      char* fFilespec;

   public:

      TargA();
      TargA(char* fFile, short int nW, short int nH);
      ~TargA();
      DWORD Get_Real_Color(int nOffset);
      void Make_Header(int nBits_Pix);
      void Change_Pallette(int nBlue, int nGreen, int nRed, int nS = 0, int nE = 768);
      void Do_Targa_File(int nNum);
      void Do_Pallette();
      void Compress(int nNum);
      // Access member functions to the image specifications:
      inline short int GetHeight() {return cnHeight;}
      inline short int GetWidth() {return cnWidth;}
      // Had to do it

}; // end class definitions


#endif
