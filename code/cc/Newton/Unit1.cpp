#include"Newton.hpp"
#include<iostream>

//---------------------------------------------------------------------------
// Main function
//---------------------------------------------------------------------------
main( int argc, char *argv[] )
{
   cout << "Initializing the newton object..." << endl;

   NewtoN* NewtonSolver = new NewtoN( 24, 25, 0.0001 );
   NewtonSolver->SetEquationParams( 1, 1, 0 );
   NewtonSolver->SetDerivativeParams( 1, 1, 0 );
   //NewtonSolver->SetFileLimits( 256, 256, "output.tga" );
   NewtonSolver->SetFileLimits( 1024, 1024, "output.tga" );
   NewtonSolver->SetDomains( -10, 10, -10, 10 );

   int totalCount = 0;
   int iFDepth = 512;
   int iLoopVar1;
   int iLoopVar2;
   long lCount = 0;
   int iTargHeight = (int)NewtonSolver->GetTargaHeight();
   int iTargWidth = (int)NewtonSolver->GetTargaWidth();
   long double ldTheVariableX;
   long double ldTheVariableY;
   long double ldTheRealVariable;

   // NewtonSolver->ChangePallete(100, 0, 70);
   NewtonSolver->ChangePallete(150, 20, 200);
   cout << "Computing the newton object..." << endl;
   // *************************************************************************
   // For Targa Output Only:
   for(iLoopVar1=0;iLoopVar1<iTargHeight;iLoopVar1++) {
      for(iLoopVar2=0;iLoopVar2<iTargWidth;iLoopVar2++)
      {
         totalCount++;
         ldTheVariableX = (long double)(iLoopVar2)/(long double)(iTargWidth);
         ldTheVariableY = (long double)(iLoopVar1)/(long double)(iTargHeight);
         ldTheVariableX *= NewtonSolver->GetDomainLengthX();
         ldTheVariableY *= NewtonSolver->GetDomainLengthY();
         ldTheVariableX += NewtonSolver->GetDomainFirstX();
         ldTheVariableY += NewtonSolver->GetDomainFirstY();
         ldTheVariableX *= ldTheVariableX; // x^2
         ldTheVariableY *= ldTheVariableY; // y^2
         ldTheRealVariable = ldTheVariableX + ldTheVariableY;
         /* NewtonFractal stuff: */
         while( NewtonSolver->GetStatus() != 1 )
            ldTheRealVariable = NewtonSolver->SolveIt( ldTheRealVariable );
         NewtonSolver->DoTargaFile( iFDepth ); //Implicitly uses lTotalIterations
         NewtonSolver->SetIsSolved( -1 );
	 //cout << NewtonSolver->GetTotalIterations() << endl;
//         ShowMessage("Press Enter");

         /**
          * Fuzzy Logic Stuff:
         FuzzySolver->SetGuess( ldTheRealVariable );
         ldTheRealVariable = FuzzySolver->SolveIt(); 
         NewtonSolver->DoTargaFile( FuzzySolver->GetNumberOfIterations() );
          */
//       NewtonSolver->DoTargaFile( lCount++ );
      } // end inside loop
   } // end outside loop

   delete NewtonSolver;
   cout << "Finished computing the newton object..." << endl;
   cout << "Total count is: " << totalCount << endl;

}
