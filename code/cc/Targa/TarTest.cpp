#include<iostream.h>
#include<conio.h>
#include"targa.hpp"

void main()
{
int nW = 256;
int nH = 200;
TargA test("c:\\bc45\\bin\\2.tga", nW, nH);
int t = 0;
int i = 0;
int j = 0;

   test.Make_Header(24);
   test.Change_Pallette(10, 0, 0, 0, 768);
   /*
   test.Compress(11);
   test.Compress(11);
   test.Compress(11);
   test.Compress(11);
   test.Compress(11);
   test.Compress(11);
   test.Compress(11);
   test.Compress(11);
   test.Compress(11);
   test.Compress(11);
   */
   for(i=-(nH / 2);i<(nH / 2);i++)
      for(j=-(nW / 2);j<(nW / 2);j++)
      {
         if(i <= 0)
            t = i + 128;
         else
            t = j + 128;

         test.Do_Targa_File(t);
            
      /*
            t = j + 128;
   test.Compress(11);
         test.Compress(t);
       */       
      } // end for     
      
} // end main
