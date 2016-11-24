//***************************************************************************** 
// File		:	Analysis.cpp
// Purpose	:	Contains the main function for numerical analysis calls
// Author	:  Brandon Benham
//*****************************************************************************
#include"onevar.hpp"
#include"mathmatc.hpp"
#include"bisect.hpp"
FilesourcE* NumerR::FlogFile = new FilesourcE("PolyTest.Log", _BYTE, 0);
//************************************************************************
// MAIN ~~~~~~~~~~~~~~~~~~~~~~~~
//************************************************************************
void main()  
{
VectoR* vTestVec = new VectoR(9);
QuadratiC* Qbisector;
int iNumCoeffs;
char cC = '\0';
long double ldTemp;

	cout.precision(10);
/*   cout<<"\nEnter Number of Coefficients: ";
   cin>>iNumCoeffs;
   vTestVec = new VectoR(iNumCoeffs);
   
   for(int i=0;i<iNumCoeffs;i++)
   {
	   cout<<"\nEnter Coefficient number "<<i<<" :";
   	cin>>vTestVec->pVariables[i];
   } // end for */
   
   vTestVec->pVariables[0] = -18;
   vTestVec->pVariables[1] = 144;
   vTestVec->pVariables[2] = -672;
   vTestVec->pVariables[3] = 2016;
   vTestVec->pVariables[4] = -4032;
   vTestVec->pVariables[5] = 5376;
   vTestVec->pVariables[6] = -4608;
   vTestVec->pVariables[7] = 2304;
   vTestVec->pVariables[8] = -512;
   
   Qbisector = new QuadratiC(3, 3);
   Qbisector->SetPolyCoeffs(vTestVec);      
	Qbisector->SetRandomInterval(1, 2);
   
   int i = 0;
   while(i<20)
   {
      Qbisector->CalcPeturbValue();
	   Qbisector->SetInterval(1, 3);
      cout<<"\nRoot is: "<<Qbisector->PeturbBisect();
      i++;
   } // end while
   
   NumerR::FlogFile->CloseFile();
   delete NumerR::FlogFile;
   delete vTestVec;
   delete Qbisector;
} // end main      
