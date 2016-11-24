//***************************************************************************** 
// File     :  gtst.cpp
// Purpose  :  This file contains the test calls to the 
//          :  Functions of the various graphical and mathematical classes
//*****************************************************************************
#include<conio.h>
#include<iostream.h>
#include<fstream.h>

#include"targa2.hpp"

void main()           
{
TargA GraphicFile("c:\\bc45\\bin\\2.tga", 640, 480);
fstream fracfile;
int Bits_per_pixel = 24;
BOOL lrle = TRUE;
BYTE in_char;
int convert = 0;

   GraphicFile.Make_Header(Bits_per_pixel, lrle);
   fracfile.open("c:\\tcwin\\bin\\a5000.dat", ios::binary | ios::in);
   GraphicFile.Compress(1); // to initialize it

   for(int i=0;i<1000;i++)
   {        
      fracfile.read((unsigned char*)(&convert), sizeof(convert));
      GraphicFile.Compress(convert);
   } // end while

   GraphicFile.Compress(1000); // to end it
                                               
} //end main             
