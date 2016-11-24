#include<iostream>
#include "Targa.hpp"

//---------------------------------------------------------------------------
// Main function
//---------------------------------------------------------------------------
int main( int argc, char *argv[] )
{
   int nW = 256;
   int nH = 200;
   TargA test("test.tga", nW, nH);
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
   for( int i=0; i<nH; i++ )
      for( int j=0; j<nW; j++ )
      {
         test.Do_Targa_File( (t++)%2048 );
      }
   /*
   for(i=-(nH / 2);i<(nH / 2);i++)
      for(j=-(nW / 2);j<(nW / 2);j++)
      {
         if(i <= 0)
            t = i + 128;
         else
            t = j + 128;

         test.Do_Targa_File(t);
            
      } // end for     
      */
      
   return 0;
}
