//**********************************************************************
// File    : Mathmatc.cpp
// Author  : Brandon Benham
// Purpose : This file contains the implementations for the MatriX and
//         : VectoR classes, which will ease programming 
//         : of calculated mathematical functions.
// Notes   : We use the numbering convention of a[row][column] to address
//         : a specific element of a matrix.
//**********************************************************************
#include "Mathmatc.hpp"

//**********************************************************************
// Class    :  MatriX
// Purpose  :  This class initializes two-dimensional array for storage 
//          :  of calculated mathematical functions.
// Notes    :  For now, we consider the entries as column vectors for the
//          :  magnitude variables.
//**********************************************************************
MatriX::MatriX(int nRows, int nColumns)
{
   int i = 0;

   cnColumns = nColumns;
   cnRows = nRows;

   pCol = new long double*[cnRows]; 
   cnMagnitudes = new long double[cnRows];
                              
   for(i=0;i<cnRows;i++)
   {
      pCol[i] = new long double[cnColumns];
   }  //end for loop
   Setup();
} //end class Matrix constructor

//**********************************************************************
// Class    :  MatriX
// Purpose  :  This class initializes two-dimensional array for storage 
//          :  of calculated mathematical functions.
// Notes    :  For now, we consider the entries as column vectors for the
//          :  magnitude variables.
//**********************************************************************
MatriX::MatriX( MatriX* theOtherMatrix )
{
   int i = 0;

   cnColumns = theOtherMatrix->cnColumns;
   cnRows = theOtherMatrix->cnRows;

   pCol = new long double*[cnRows];
   cnMagnitudes = new long double[cnRows];

   for(i=0;i<cnRows;i++)
   {
      pCol[i] = new long double[cnColumns];
   }  //end for loop
   Setup();
   for( int i=0; i<cnRows; i++ )
   {
      for( int j=0; j<cnColumns; j++ )
      {
         pCol[i][j] = theOtherMatrix->pCol[i][j];
      }
   }
} //end class Matrix constructor

//**********************************************************************
// Class    :  MatriX
// Purpose  :  Default constructor
//**********************************************************************
MatriX::MatriX()
{
   cnColumns = 0;
   cnRows = 0;
   cnDeterminant = 0;
   pCol = 0;
   cnMagnitudes = 0;
   RandomVariable = 0;
   _Q = 0;
   _R = 0;
} //end class Matrix constructor     

//**********************************************************************
// Class    :  MatriX
// Purpose  :  Destructor - clean up the heap
//**********************************************************************
MatriX::~MatriX()                 
{
int loop = 0;

   if(pCol != 0) // changed from *pCol != NULL
   {
      for(loop=0;loop<cnRows;loop++)
         delete[] pCol[loop];
   }
   if(cnMagnitudes != 0)
   {
      delete[] cnMagnitudes;
   }
   if(pCol != 0)
   {
      delete[] pCol;
   }
   if(RandomVariable != 0)
   {
      delete RandomVariable;
   }
   if(FunctionObject != 0)
   {
      delete FunctionObject;
   }
   if( _Q != 0 )
   {
      delete _Q;
      _Q = 0;
   }
   if( _R != 0 )
   {
      delete _R;
      _R = 0;
   }
} //end class Matrix constructor   

//**********************************************************************
// Class   : MatriX
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method  : Setup
// Purpose : Instantiates our data properties.
//**********************************************************************
void MatriX::Setup()
{
   int iL = 0;
   int iO = 0;
   
   for( iL=0; iL<cnRows; iL++ )
   {
      for( iO=0; iO<cnColumns; iO++ )
      {
         pCol[iL][iO] = 0;
      } //end for loop
   } //end for loop
      
   for( iL=0; iL<cnRows; iL++ )
   {
      cnMagnitudes[iL] = 0;
   } //end for loop

   cnDeterminant = 0;                        
   RandomVariable = new RandoM(-1, 1);
   FunctionObject = new FunctioN(_OVERFLOW_);
   RandomVariable->CalcSeed();
   _Q = 0;
   _R = 0;
} // end Setup

//**********************************************************************
// Class   : MatriX
// Purpose : Overloaded the + operator for the addition of matrices
//**********************************************************************
/*
MatriX& MatriX::operator + (const MatriX& b)
{
   MatriX* theResult = new MatriX( cnRows, cnColumns );

   for( int i=0; i<cnRows; i++ )
   {
      for( int j=0; j<cnColumns; j++ )
      {
         theResult->pCol[i][j] = pCol[i][j] + b.pCol[i][j];
      } // end for loop
   } // end for loop

   return *theResult;
} // end operator + overload
*/

//**********************************************************************
// Class   : MatriX
// Purpose : Overloaded the + operator for the addition of matrices
//**********************************************************************
MatriX* MatriX::Add( MatriX* b )
{
   MatriX* theResult = new MatriX( cnRows, cnColumns );

   for( int i=0; i<cnRows; i++ )
   {
      for( int j=0; j<cnColumns; j++ )
      {
         theResult->pCol[i][j] = pCol[i][j] + b->pCol[i][j];
      } // end for loop
   } // end for loop

   return theResult;
} // end operator + overload

//**********************************************************************
// Class   : MatriX
// Purpose : Overloaded the * operator for the addition of matrices
//**********************************************************************
/*
MatriX& MatriX::operator * (const MatriX& b)         
{
   MatriX* mRes = new MatriX( cnRows, b.cnColumns );

   for( int i=0; i<cnRows; i++ )
   {
      for( int j=0; j<b.cnColumns; j++ )
      {
         for( int k=0; k<cnColumns; k++ )
         {
            mRes->pCol[i][j] += (pCol[i][k] * b.pCol[k][j]);
         } //end for loop
      } //end for loop
   } //end for loop

   return *mRes;
} //end overload 
*/

//**********************************************************************
// Class   : MatriX
// Purpose : Overloaded the * operator for the addition of matrices
//**********************************************************************
MatriX* MatriX::Product( MatriX* b )
{
   MatriX* mRes = new MatriX( cnRows, b->cnColumns );

   for( int i=0; i<cnRows; i++ )
   {
      for( int j=0; j<b->cnColumns; j++ )
      {
         for( int k=0; k<cnColumns; k++ )
         {
            mRes->pCol[i][j] += (pCol[i][k] * b->pCol[k][j]);
         } //end for loop
      } //end for loop
   } //end for loop

   return mRes;
} //end overload 

//**********************************************************************
// Class   : MatriX
// Purpose : Overloaded the * operator for VectoRs
//**********************************************************************
/*
VectoR& MatriX::operator * (const VectoR& b)         
{
   VectoR* mRes = new VectoR( cnRows );

   for( int i=0; i<cnRows; i++ )
   {
      mRes->pVariables[i] = 0;
      for( int j=0; j<cnColumns; j++ )
      {
         mRes->pVariables[i] += (pCol[i][j] * b.pVariables[j]);
      } //end for loop
   } //end for loop

   return *mRes;
} //end overload 
*/

//**********************************************************************
// Class   : MatriX
// Purpose : Overloaded the * operator for VectoRs
//**********************************************************************
VectoR* MatriX::Product( VectoR* b )
{
   VectoR* mRes = new VectoR( cnRows );

   for( int i=0; i<cnRows; i++ )
   {
      mRes->pVariables[i] = 0;
      for( int j=0; j<cnColumns; j++ )
      {
         mRes->pVariables[i] += (pCol[i][j] * b->pVariables[j]);
      } //end for loop
   } //end for loop

   return mRes;
} //end overload 

//**********************************************************************
// Class   : MatriX
// Purpose : Overloaded the = operator for the addition of matrices
//**********************************************************************
MatriX& MatriX::operator = (const MatriX& b)
{
   int ilp = 0;
   int jlp = 0;
   
   for( ilp=0; ilp<cnRows; ilp++ )
   {
      for( jlp=0; jlp<cnColumns; jlp++ )
      {
         pCol[ilp][jlp] = b.pCol[ilp][jlp];
      } //end for loop
   } //end for loop
   
   return *this;
} //end overload pointer version

//**********************************************************************
// Class   : MatriX
// Purpose : Overloaded the += operator for the assignment of matrices
//**********************************************************************
MatriX& MatriX::operator += (const MatriX& rhs)
{
   int iL;
   int iO;

   for( iL=0; iL<rhs.cnRows; iL++ )
   {
      for( iO=0; iO<rhs.cnColumns; iO++ )
      {
         pCol[iL][iO] = pCol[iL][iO] + rhs.pCol[iL][iO];
      } // end inner for loop
   } // end inner for loop
   
   return *this;
} // end operator += overload

//**********************************************************************
// Class    : MatriX
// Purpose  : Overloaded the -= operator for the assignment of matrices
//**********************************************************************
MatriX& MatriX::operator -= (const MatriX& rhs)
{
   int iC;
   int iR;

   for( iC=0; iC<rhs.cnRows; iC++ )
   {
      for( iR=0; iR<rhs.cnColumns; iR++ )
      {
         pCol[iC][iR] = pCol[iC][iR] - rhs.pCol[iC][iR];
      } // end inner for loop
   } // end inner for loop

   return *this;
} // end operator -= overload

//**********************************************************************
// Class    : MatriX
// Purpose  : Overloaded the << operator for the assignment of matrices
//**********************************************************************
ostream& operator << (ostream& o, const MatriX& rhs)
{
   for(int i=0;i<rhs.cnRows;i++)
   {
      for(int j=0;j<rhs.cnColumns;j++)
      {
         o<<rhs.pCol[i][j]<<'\t'; // print out row vectors
      } //end for loop
      o<<'\n';
   } //end outer for loop
   o<<"Determinant: "<<rhs.cnDeterminant<<"\n";

   return o;
} // end ostream overload

//**********************************************************************
// Class    :  MatriX
// Purpose  :  Equates the second matrix to the first
// Notes    :  We had to define this member because the overloaded '='
//          :  wasn't working properly.
//**********************************************************************
void MatriX::Equate(MatriX* mFirst)
{
int iLoop = 0;
int jLoop = 0;

   if(mFirst->cnRows != cnRows)
   {
      //cout<<"MatriX equivalence undefined!"<<'\n';
      return;
   } 
   else if(mFirst->cnColumns != cnColumns)
   {
      //cout<<"MatriX equivalence undefined!"<<'\n';
      return;
   } // end else if     

   for(iLoop=0;iLoop<cnRows;iLoop++)
      for(jLoop=0;jLoop<cnColumns;jLoop++)
      {
         pCol[iLoop][jLoop] = mFirst->pCol[iLoop][jLoop];
      } // end inner for
} // end Equate


//**********************************************************************
// Class    :  MatriX
// Purpose  :  This member function computes the determinant of a matrix
//**********************************************************************
void MatriX::cDet()
{
long double* temp = 0;

   switch(cnColumns)
   {
      case 1:
         cnDeterminant = pCol[0][0];
      break;

      case 2:
         *temp = (pCol[0][0] * pCol[1][1]);
         *temp -= (pCol[1][0] * pCol[0][1]);
         cnDeterminant = *temp;
      break;

      case 3:
         *temp = pCol[0][0] * ((pCol[1][1] * pCol[2][2]) - (pCol[1][2] * pCol[2][1]));
         cnDeterminant = *temp;
         *temp = pCol[1][0] * ((pCol[0][1] * pCol[2][2]) - (pCol[0][2] * pCol[2][1]));
         cnDeterminant -= *temp;
         *temp = pCol[2][0] * ((pCol[0][1] * pCol[1][2]) - (pCol[0][2] * pCol[1][1]));
         cnDeterminant += *temp;
      break;                       

      case 4:
         *temp = pCol[0][0] * ((pCol[1][1] * (pCol[2][2] * pCol[3][3] - pCol[2][3] * pCol[3][2]))
            - (pCol[2][1] * (pCol[1][2] * pCol[3][3] - pCol[1][3] * pCol[3][2]))
            + (pCol[3][1] * (pCol[1][3] * pCol[2][2] - pCol[1][2] * pCol[2][3])));
         cnDeterminant = *temp;

         *temp = pCol[1][0] * ((pCol[0][1] * (pCol[2][2] * pCol[3][3] - pCol[2][3] * pCol[3][2]))
            - (pCol[2][1] * (pCol[0][2] * pCol[3][3] - pCol[0][3] * pCol[3][2]))
            + (pCol[3][1] * (pCol[0][2] * pCol[2][3] - pCol[0][3] * pCol[2][2])));
         cnDeterminant -= *temp;
         
         *temp = pCol[2][0] * ((pCol[0][1] * (pCol[1][2] * pCol[3][3] - pCol[1][3] * pCol[3][2]))
            - (pCol[1][1] * (pCol[0][2] * pCol[3][3] - pCol[0][3] * pCol[3][2]))
            + (pCol[3][1] * (pCol[0][2] * pCol[1][3] - pCol[0][3] * pCol[1][2])));
         cnDeterminant += *temp;
         
         *temp = pCol[3][0] * ((pCol[0][1] * (pCol[1][2] * pCol[2][3] - pCol[1][3] * pCol[2][2]))
            - (pCol[1][1] * (pCol[0][2] * pCol[2][3] - pCol[0][3] * pCol[2][2]))
            + (pCol[2][1] * (pCol[0][2] * pCol[1][3] - pCol[0][3] * pCol[1][2])));
         cnDeterminant -= *temp;
         
      break;

   } //end switch

} //end cDet

//*****************************************************************************
// Class    :  MatriX
// Purpose  :  This member function fills a matrix with random decimal numbers
//*****************************************************************************
void MatriX::Fill_Rand()
{
int iInner = 0;
int iOuter = 0;

   for(iInner=0;iInner<cnRows;iInner++)
      for(iOuter=0;iOuter<cnColumns;iOuter++)
      {
         pCol[iInner][iOuter] = RandomVariable->GetRandomLngDouble();
      } //end for loop
} //end Fill_Rand

//*****************************************************************************
// Class    :  MatriX
// Purpose  :  This member function fills a matrix with random Integer numbers
//          :  between zero and the passed in argument
//*****************************************************************************
void MatriX::Fill_Rand_Integer(int nMax)
{
int iIn = 0;
int iOut = 0;

   RandomVariable->SetEndPoints(0, (float)nMax);
   for(iIn=0;iIn<cnRows;iIn++)
      for(iOut=0;iOut<cnColumns;iOut++)
      {
         pCol[iIn][iOut] = (long double)RandomVariable->GetRandomInt();
      } //end for loop
} //end Fill_Rand_Integer

//************************************************************************
// Class    :  MatriX
// Purpose  :  This member function computes the magnitude of each column
//************************************************************************
void MatriX::Calc_Magnitudes()
{
int i = 0;
int j = 0;
long double nTemp = 0;

   for(i=0;i<cnRows;i++)
   {
      for(j=0;j<cnColumns;j++)
      {
         nTemp += (pCol[i][j] * pCol[i][j]);
      } // end for loop
      nTemp = sqrtl(nTemp);
      cnMagnitudes[i] = nTemp;
      nTemp = 0;
   } // end outer for
} // Calc_Magnitude

//************************************************************************
// Class    :  MatriX
// Purpose  :  This member function normalizes each column vector
//************************************************************************
void MatriX::Normalize()
{
int i = 0;
int j = 0;

   this->Calc_Magnitudes();

   for(i=0;i<cnRows;i++)
   {
      for(j=0;j<cnColumns;j++)
      {
         pCol[i][j] /= cnMagnitudes[i];
      } // end for loop
   } // end outer for
} // Normalize

//*****************************************************************************
// Class    :  MatriX
// Purpose  :  
// Notes    :  
//          :  
//          : 
//          :  
//*****************************************************************************
void MatriX::OutputToFile( char* fileName )
{
   fstream outputFile( fileName, ios::out | ios::trunc );
   outputFile << setprecision( 12 );
   outputFile << "Values for this matrix:\n";

   for( int i=0; i<cnRows; i++ )
   {
      outputFile << "Row: " << i << "\n";
      for( int j=0; j<cnColumns; j++ )
      {
         outputFile << pCol[i][j] << " ";
      }
      outputFile << "\n";
   }

   outputFile.close();
}

//*****************************************************************************
// Class    :  MatriX
// Purpose  :  This member function outputs a matrix to the stream
// Notes    :  The logical parameters are: lSpace = TRUE, if you want to
//          :  have spaces between the elements. lRows = TRUE, if you 
//          :  want to print out the row vectors, otherwise we print out
//          :  the column vectors.
//*****************************************************************************
/*void MatriX::Display(BOOL lSpace, BOOL lMag)
{

   for(int i=0;i<cnRows;i++)
   {
      for(int j=0;j<cnColumns;j++)
      {
               cout<<pCol[i][j]<<'\t'; // print out row vectors
      } //end for loop

      cout<<'\n';

      if(lMag == TRUE)
      {
         cout<<" Magnitude: "<<cnMagnitudes[i]<<'\n';
      } // end if

   } //end outer for loop
   cout<<"Determinant: "<<cnDeterminant<<"\n";
   cout<<"\n";
} //end Display */

//*****************************************************************************
// Class    :  MatriX
// Purpose  :  This member function zeroes out a matrix
//*****************************************************************************
void MatriX::Zero_Out()
{
int Inner = 0;
int Outer = 0;

   for(Inner=0;Inner<cnRows;Inner++)
      for(Outer=0;Outer<cnColumns;Outer++)
      {
         pCol[Inner][Outer] = 0;
      } // end for loop
} // end Zero out

//*****************************************************************************
// Class    :  MatriX
// Purpose  :  This member function ones out a matrix
//*****************************************************************************
void MatriX::Fill_Ones()
{
int FillInner = 0;
int FillOuter = 0;

   for(FillInner=0;FillInner<cnRows;FillInner++)
      for(FillOuter=0;FillOuter<cnColumns;FillOuter++)
      {
         pCol[FillInner][FillOuter] = 1;
      } // end for loop
} // end Fill_Ones

//*****************************************************************************
// Class    :  MatriX
// Purpose  :  This member function turns the current instance into an identity
//          :  matrix.
//*****************************************************************************
void MatriX::Make_Identity()
{
   for(int i=0;i<cnRows;i++)
      for(int j=0;j<cnColumns;j++)
      {
         if(i == j)
            pCol[i][j] = 1;
         else
            pCol[i][j] = 0;
      } // end for loop

} // end Make_Identity

//*****************************************************************************
// Class    :  MatriX
// Purpose  :  This member function turns the current instance into a translation
//          :  matrix with the given paramaters.
//*****************************************************************************
void MatriX::Make_Translation(long double nX, long double nY, long double nZ)
{
   if((cnColumns != 4) && (cnRows != 4))
      return;
   this->Make_Identity();
   pCol[3][0] = nX;
   pCol[3][1] = nY;
   pCol[3][2] = nZ;
} // end Make_Translation

//*****************************************************************************
// Class    :  MatriX
// Purpose  :  This member function turns the current instance into a scale
//          :  matrix with the given paramaters.
//*****************************************************************************
void MatriX::Make_Scale(long double nX, long double nY, long double nZ)
{
   if((cnColumns != 4) && (cnRows != 4))
      return;
   this->Make_Identity();
   pCol[1][1] = nX;
   pCol[2][2] = nY;
   pCol[3][3] = nZ;
} // end Make_Translation

//*****************************************************************************
// Class    :  MatriX
// Purpose  :  This member function turns the current instance into a rotation
//          :  matrix with the given paramaters.
//*****************************************************************************
void MatriX::Make_Rotation_X(long double nTheta)
{
long double nSin_Angle = 0;
long double nCos_Angle = 0;
long double nAngle = 0;

   if((cnColumns != 4) && (cnRows != 4))
      return;
   this->Make_Identity();
   nAngle = FunctionObject->DegreesToRadians(nTheta);
   nSin_Angle = sinl(nAngle);
   nCos_Angle = cosl(nAngle);
   pCol[1][1] = nCos_Angle;
   pCol[1][2] = (-1) * nSin_Angle;
   pCol[2][1] = nSin_Angle;
   pCol[2][2] = (-1) * nCos_Angle;
               
} // end Make_Rotation

//*****************************************************************************
// Class    :  MatriX
// Purpose  :  This member function turns the current instance into a rotation
//          :  matrix with the given paramaters.
//*****************************************************************************
void MatriX::Make_Rotation_Y(long double nTheta)
{
long double nSin_Angle = 0;
long double nCos_Angle = 0;
long double nAngle = 0;

   if((cnColumns != 4) && (cnRows != 4))
      return;
   this->Make_Identity();
   nAngle = FunctionObject->DegreesToRadians(nTheta);
   nSin_Angle = sinl(nAngle);
   nCos_Angle = cosl(nAngle);
   pCol[0][0] = nCos_Angle;
   pCol[2][0] = (-1) * nSin_Angle;
   pCol[0][2] = (-1) * nSin_Angle;
   pCol[2][2] = nCos_Angle;
               
} // end Make_Rotation

//*****************************************************************************
// Class    :  MatriX
// Purpose  :  This member function turns the current instance into a rotation
//          :  matrix with the given paramaters.
//*****************************************************************************
void MatriX::Make_Rotation_Z(long double nTheta)
{
long double nSin_Angle = 0;
long double nCos_Angle = 0;
long double nAngle = 0;

   if((cnColumns != 4) && (cnRows != 4))
      return;
   this->Make_Identity();
   nAngle = FunctionObject->DegreesToRadians(nTheta);
   nSin_Angle = sinl(nAngle);
   nCos_Angle = cosl(nAngle);
   pCol[0][0] = nCos_Angle;
   pCol[0][1] = (-1) * nSin_Angle;
   pCol[1][0] = nSin_Angle;
   pCol[1][1] = nCos_Angle;
               
} // end Make_Rotation


//*****************************************************************************
// Class    :  MatriX
// Purpose  :  This member function switches the rows specified by the arguments
//*****************************************************************************
void MatriX::Switch_Rows(int nDest, int nSource)
{
long double nTemp = 0;

   for(int i=0;i<cnColumns;i++)
   {
      nTemp = pCol[nDest][i];
      pCol[nDest][i] = pCol[nSource][i];
      pCol[nSource][i] = nTemp;
   } // end for loop
} // end Switch_Rows

//*****************************************************************************
// Class    :  MatriX
// Purpose  :  This member function puts a matrix in row eschalon form
//*****************************************************************************
void MatriX::Eschalon()
{
int nRows_done = 0;
int nTemp = 0;

   while(nRows_done <= cnRows)
   {
      nTemp = Find_Pivot(nRows_done);
      if(nTemp > nRows_done)
         Switch_Rows(nRows_done, nTemp);
      nRows_done += 1;
   } // end while
} // end Eschalon

//*****************************************************************************
// Class    :  MatriX
// Purpose  :  This member function returns a matrix's pivot row number
//          :  from the row passed in to the end of the matrix
//*****************************************************************************
int MatriX::Find_Pivot(int nRows)
{
   for(int nClms=0;nClms<cnColumns;nClms++)
      for(int nRws=nRows;nRws<cnRows;nRws++)
      {
         if(pCol[nRws][nClms] > 0)
            return nRws;
      } // end for
   return 1;
} // end Find_Pivot

//*****************************************************************************
// Class    :  MatriX
// Purpose  :  This member function recursively determins the determinant
//          :  I'll leave it to the user to make sure it is a n x n matrix
//*****************************************************************************
long double MatriX::Determinant()
{
long double nDeterm = 0;

   if((cnColumns == 1) && (cnRows == 1))
      return pCol[0][0];
   else
   {
      for(int nNum_Col=0;nNum_Col<cnColumns;nNum_Col++)
      {
         nDeterm += pCol[0][nNum_Col] * pow( (double)-1, (int)(2+nNum_Col)) * (this->Get_Cofactor(0,nNum_Col)).Determinant();
      } // end for loop
   } // end else
   cnDeterminant = nDeterm;
   return nDeterm;
} // end Determinant
   
//*****************************************************************************
// Class    :  MatriX
// Purpose  :  This member function puts a matrix in reduced row eschalon form
//          :  we assume that the matrix has previously been zeroed out.
//*****************************************************************************
void MatriX::Reduced_Eschalon()
{
	/*
VectoR* nTemp_row;
nTemp_row = new VectoR(cnColumns);
int nPiv_row = -1;
int nR;
int nOld_row = 5;
int nCol;
int nThis_col = 0;
int num = 0;
int nT = 0;
long double nScale = 0;

   Eschalon(); // before we begin, we put the matrix in eschalon form
   
   //for(nPiv_row=0;nPiv_row<cnRows;nPiv_row++) // this is our pivot row loop
   while(nPiv_row < cnRows)
   { // we complete one loop for each non-zero row
      nPiv_row = Find_Pivot(nPiv_row + 1);
      for(nR=0;nR<cnRows;nR++) // this is our row counter, to compare to
      { // our pivot row, thus these two variable cannot be equal simultaneously
         if((nR == nPiv_row))// || (nR == cnRows)) not sure
         { // it may be easier to check for equality
         }    
         else // the current row is not equal to the pivot row
         //if(nR != nPiv_row) not sure about this
         {
            if(nOld_row != nPiv_row) // find first non-zero 
            { 
               for(nCol=0;nCol<cnColumns;nCol++) // find first non-zero
               { // element in the current pivot row
                  if((pCol[nPiv_row][nCol] == 0))
                  {
                     //continue;
                     //nThis_col = cnColumns + 5; // just so we know we were here
                     nOld_row = nPiv_row;
                     //cout<<pCol[nPiv_row][nCol]<<" is zero"<<endl;
                     getch();
                  } else
                  {
                     for(nT=0;nT<cnColumns;nT++)
                        nTemp_row->pVariables[nT] = pCol[nPiv_row][nT]; // create a temporary row
                     nThis_col = nCol; // we must remember this column
                     //cout<<"nThis_col is: "<<nThis_col<<endl;
                     //cout<<pCol[nPiv_row][nCol]<<" is NOT zero"<<endl;
                     getch();
                     break;
                  } // end if
               } // end for loop nP
            //} // end if
   
            if((pCol[nR][nThis_col] == 0)) // see if the row needs to be killed
            { 
               //cout<<"column: "<<nThis_col<<" and row: "<<nR<<" is zero"<<endl;
               getch();
            } else
            {  
   
               //cout<<"nOld_row: "<<nOld_row<<endl;
               //cout<<"nPiv_Row: "<<
               nPiv_row<<endl;
               cout<<"our pivot is: "<<pCol[nThis_col][nPiv_row]<<endl;
               cout<<"column: "<<nThis_col<<" and row: "<<nR<<" is NOT zero"<<endl;
               getch();

               if(pCol[nPiv_row][nThis_col] == 0)
                  cout<<"tried to divide by zero!"<<endl;
               else
                  nScale = pCol[nR][nThis_col] / pCol[nPiv_row][nThis_col];
   
               if(pCol[nR][nThis_col] > 0)
                  if(nScale > 0)
                     nScale *= -1;
               if(pCol[nR][nThis_col] < 0)
                  if(nScale < 0)
                     nScale *= -1;
   
               cout<<"our scale is: "<<nScale<<endl;
               cout<<"adding to row: "<<nR<<endl;
               getch();
   
               for(nT=0;nT<cnColumns;nT++) // scale our temp row
                  nTemp_row->pVariables[nT] *= nScale;
   
               Add_vector(nR, nTemp_row);
            } // end if != 0
         } // end else if
      } // end for loop nR
      nOld_row = nPiv_row;
      Eschalon(); // we've killed all rows, so we re-organized our matrix
      //Display(FALSE, TRUE);
      //getch();
   } // end for loop nPiv_row
   delete nTemp_row;
   */
} // end Reduced_Eschalon

//*****************************************************************************
// Class    :  MatriX
// Purpose  :  This member function adds a vector to the specified row
//*****************************************************************************
void MatriX::Add_vector(int nRow, VectoR* nVec)
{
   if((nVec->cnRows != cnRows) || (nRow >= cnRows))
      return;
   else
   for(int nTempAdd=0;nTempAdd<cnRows;nTempAdd++)
   {
      //cout<<"adding: "<<nVec->pVariables[nTempAdd]<<" to: "<<this->pCol[nTempAdd][nRow]<<endl;
      this->pCol[nRow][nTempAdd] += nVec->pVariables[nTempAdd];
   } // end for loop
} // end Add_vector
          
//*****************************************************************************
// Class    :  MatriX
// Purpose  :  This member function return a MatriX instance that is
//          :  1 row and 1 column smaller than the calling instance
//*****************************************************************************
MatriX MatriX::Get_Cofactor(int nRow, int nColumn)
{
int a = 0;
int b = 0;

   if(cnColumns == 1)
   {
      //cout<<"returing in Get_Cofactor."<<endl;
      return *this;
   } // end if
   MatriX* Temp = new MatriX((cnColumns - 1), (cnRows - 1));
   for(int i=0;i<Temp->cnRows;i++)
   {
      for(int j=0;j<Temp->cnColumns;j++)
      {
         if(i == nRow)
            a = 1; // end if 
         
         if(j == nColumn)
            b = 1; // end if
         //cout<<pCol[(i + a)][(j + b)]<<endl;
                                      
         Temp->pCol[i][j] = pCol[(i + a)][(j + b)];
         /*cout<<Temp->pCol[i][j]<<endl;
         getch(); */
      } //end for loop
      b = 0;
   } // end i for loop
      /* here, we must return the contents of the pointer. The object
      has to be a pointer, not an instance, or the program will crash! */
      //Temp->Display(FALSE, TRUE);
      //getch();
   return *Temp;
} // end Get_Cofactor

//*****************************************************************************
// Class    :  MatriX
// Purpose  :  This member function return a MatriX instance that is
//          :  the transpose of the caller matrix.
//*****************************************************************************
MatriX MatriX::Transpose()
{
MatriX* mResult = new MatriX(cnColumns, cnRows); // we instantiate the reverse
int nR = 0;
int nC = 0;

   for(nR=0;nR<cnRows;nR++)
      for(nC=0;nC<cnColumns;nC++)
      {
         mResult->pCol[nR][nC] = pCol[nC][nR];
      } // end for loop

   return *mResult;
} // end Transpose

//*****************************************************************************
// Class    :  MatriX
// Purpose  :  This member function return a MatriX instance that is
//          :  the inverse of the caller matrix.
//*****************************************************************************
MatriX MatriX::Inverse()
{
MatriX* mInv = new MatriX(cnRows, cnColumns);
long double nDet = 0;
int iLp = 0;
int jLp = 0;

   cnDeterminant = Determinant();
   if(FunctionObject->Absolute(cnDeterminant) > FunctionObject->GetEpsilon())
      nDet = 1 / cnDeterminant;
   mInv->Zero_Out();
   for(iLp=0;iLp<cnRows;iLp++)
      for(jLp=0;jLp<cnColumns;jLp++)
      {
         mInv->pCol[iLp][jLp] = pow( (double)-1, (double)iLp + jLp) * 
                                (Get_Cofactor(iLp, jLp)).Determinant();
      } // end outer for

   mInv->Equate(&mInv->Transpose());
   mInv->Scale(nDet);

   return *mInv;
} // end Inverse

//*****************************************************************************
// Class    :  MatriX
// Purpose  :  This member function return a MatriX instance that is
//          :  the inverse of the caller matrix.
//*****************************************************************************
void MatriX::Scale(long double nAlpha)
{
int nA = 0;
int nB = 0;

   for(nA=0;nA<cnRows;nA++)
      for(nB=0;nB<cnColumns;nB++)
      {
         pCol[nA][nB] *= nAlpha;
      } // end inner for
} // end Scale

long double MatriX::Absolute( long double value )
{
   if( value < 0 )
   {
      value *= -1;
   }

   return value;
}

//*****************************************************************************
// Class : MatriX
// Purpose : Implements the Gaussian Elimination Algorithm
//*****************************************************************************
void MatriX::Print()
{
   FPRINT( "PrintMatrix" );

   for( int i=0; i<cnRows; i++ )
   {
      FPRINT << "Row number: " << i << "";
      for( int j=0; j<cnColumns; j++ )
      {
         FPRINT << pCol[i][j];
      }
   }
} // end print

//*****************************************************************************
// Class : MatriX
// Purpose : Implements the Gaussian Elimination Algorithm
//*****************************************************************************
int MatriX::FindFirstNonZeroRow( int column )
{
int row = 0;
long double largest = -2e20;

   for( int j=column; j<cnRows; j++ )
   {
      if( Absolute( pCol[j][column] ) > _EPSILON )
      {
         if( Absolute( pCol[j][column] ) > largest )
         {
            row = j;
            largest = Absolute( pCol[j][column] );
         }
      }
   }

   return row;
}

//*****************************************************************************
// Class : MatriX
// Purpose : Implements the Gaussian Elimination Algorithm
//*****************************************************************************
void MatriX::Swap( int row1, int row2 )
{
   long double temp = 0;

   for( int j=0; j<cnColumns; j++ )
   {
      temp = pCol[row1][j];
      pCol[row1][j] = pCol[row2][j];
      pCol[row2][j] = temp;
   }
}

//*****************************************************************************
// Class : MatriX
// Purpose : Implements the Gaussian Elimination Algorithm
//*****************************************************************************
VectoR* MatriX::Solve()
{
FPRINT( "Solve" );
// Don't construct here just yet:
VectoR* theSolution = 0;
long double mult = 0;
long double temp = 0;

   for( int i=0; i<(cnRows-1); i++ )
   {
//      Print();
      int nonZeroRow = FindFirstNonZeroRow( i );
      if( nonZeroRow != i )
      {
         Swap( nonZeroRow, i );
      }

      for( int j=(i+1); j<cnRows; j++ )
      {
         if( Absolute( pCol[i][i] ) < _EPSILON )
         {
            return 0;
         }
         mult = pCol[j][i] / pCol[i][i];

         for( int k=i; k<cnColumns; k++ )
         {
            pCol[j][k] -= mult * pCol[i][k];
         }
      }
   } // end Elemination for loop

   theSolution = new VectoR( cnRows );

   if( Absolute( pCol[cnRows-1][cnRows-1] ) < _EPSILON )
   {
      return 0;
   }

   theSolution->pVariables[ cnRows - 1 ] = pCol[cnRows-1][cnColumns-1] / pCol[cnRows-1][cnRows-1];
   FPRINT << theSolution->pVariables[ cnRows - 1 ];

   for( int i=cnRows-2; i>-1; i-- )
   {
      if( Absolute( pCol[i][i] ) < _EPSILON )
      {
         return 0;
      }

      temp = 0;
      for( int j=(i+1); j<cnRows; j++ )
      {
         temp += ( pCol[i][j] * theSolution->pVariables[ j ] );
      }

      theSolution->pVariables[ i ] = pCol[i][cnColumns-1] - temp;
      theSolution->pVariables[ i ] /= pCol[i][i];
      FPRINT << theSolution->pVariables[ i ];
   }

   return theSolution;
}

//*****************************************************************************
// Class : MatriX
// Purpose : 
//*****************************************************************************
long double MatriX::ComputeColumnNorm( int col )
{
   long double norm = 0;

   for( int i=0; i<cnRows; i++ )
   {
      norm += ( pCol[i][col] * pCol[i][col] );
   } // end row for

   norm = sqrtl( norm );

   return norm;
} // end ComputeColumnNorm

//*****************************************************************************
// Class : MatriX
// Purpose : Implements the Modified Gram-Schmidt Algorithm
//*****************************************************************************
int MatriX::ComputeMGS()
{
   //FPRINT( "ComputeMGS" );

   // Construct the necessary matrices:
   MatriX* V = new MatriX( this );
   if( _Q != 0 )
   {
      delete _Q;
   }
   if( _R != 0 )
   {
      delete _R;
   }
   _Q = new MatriX( cnRows, cnColumns );
   _R = new MatriX( cnColumns, cnColumns );
   _Q->Zero_Out();
   _R->Zero_Out();


   for( int n=0; n<cnColumns; n++ )
   {
      //FPRINT << "n: " << n;
      _R->pCol[n][n] = V->ComputeColumnNorm( n );

      if( Absolute( _R->pCol[n][n] ) < _EPSILON )
      {
         delete V;
         return 0;
      } // end if

      for( int m=0; m<cnRows; m++ )
      {
         //FPRINT << "m1: " << m;
         _Q->pCol[m][n] = V->pCol[m][n] / _R->pCol[n][n];
      } // end m for

      for( int j=(n+1); j<cnColumns; j++ )
      {
         //FPRINT << "j: " << j;
         _R->pCol[n][j] = 0;
         for( int m=0; m<cnRows; m++ )
         {
            //FPRINT << "m2: " << m;
            _R->pCol[n][j] += ( _Q->pCol[m][n] * V->pCol[m][j] );
         } // end m for
         for( int m=0; m<cnRows; m++ )
         {
            //FPRINT << "m3: " << m;
            V->pCol[m][j] -= (_R->pCol[n][j] * _Q->pCol[m][n] );
         } // end m for
      } // end j for
   } // end i for

   delete V;
   return 1;
} // end ComputeMGS

//*****************************************************************************
// Class : MatriX
// Purpose : 
//*****************************************************************************
VectoR* MatriX::SolveMGS( VectoR* b, bool createNewQR )
{
   if( createNewQR )
   {
      if( ComputeMGS() == 0 )
      {
         return 0;
      }
   }

   VectoR* qb = new VectoR( _Q->cnColumns );

   for( int i=0; i<cnColumns; i++ )
   {
      qb->pVariables[i] = 0;
      for( int j=0; j<cnRows; j++ )
      {
         qb->pVariables[i] += (b->pVariables[j] * _Q->pCol[j][i]);
      }
   }

   VectoR* theSolution = new VectoR( cnColumns );

   if( Absolute( _R->pCol[cnColumns-1][cnColumns-1] ) < _EPSILON )
   {
      delete qb;
      return 0;
   }

   theSolution->pVariables[ cnColumns - 1 ] = qb->pVariables[ cnColumns-1 ] / _R->pCol[ cnColumns-1 ][ cnColumns-1 ];
   //FPRINT << theSolution->pVariables[ cnRows - 1 ];

   long double temp = 0;

   for( int i=cnColumns-2; i>-1; i-- )
   {
      if( Absolute( _R->pCol[i][i] ) < _EPSILON )
      {
         delete qb;
         return 0;
      }

      temp = 0;
      for( int j=(i+1); j<cnColumns; j++ )
      {
         temp += ( _R->pCol[i][j] * theSolution->pVariables[ j ] );
      }

      theSolution->pVariables[ i ] = qb->pVariables[i] - temp;
      theSolution->pVariables[ i ] /= _R->pCol[i][i];
      //FPRINT << theSolution->pVariables[ i ];
   }

   delete qb;

   return theSolution;
} // end SolveMGS

//********************************************************************************
// Class   : VectoR
//********************************************************************************

//**********************************************************************
// Class    : VectoR
// Purpose  : This class initializes one-dimensional array for storage 
//          : of calculated mathematical functions.
//**********************************************************************
VectoR::VectoR()
{
   cnRows = 1;
   cnMagnitude = 0;
   pVariables = new long double[cnRows]; 
   Setup();
} //end class default constructor Vector

//**********************************************************************
// Class   : VectoR
// Purpose : This class initializes one-dimensional array for storage 
//         : of calculated mathematical functions.
//**********************************************************************
VectoR::VectoR(int nVariables)
{
   cnRows = nVariables;
   cnMagnitude = 0;
   pVariables = new long double[cnRows]; 
   Setup();
} //end class constructor Vector

//**********************************************************************
// Class   : VectoR
// Purpose : This class initializes one-dimensional array for storage 
//         : of calculated mathematical functions.
//**********************************************************************
VectoR::VectoR( VectoR* theTemplate )
{
   cnRows = theTemplate->cnRows;
   cnMagnitude = 0;
   pVariables = new long double[ cnRows ];
   Setup();

   for( int i=0; i<cnRows; i++ )
   {
      pVariables[ i ] = theTemplate->pVariables[ i ];
   } // end for loop
} // end constructor

//**********************************************************************
// Class   : VectoR
// Purpose : Destructor
//**********************************************************************
VectoR::~VectoR()                                           
{
   if( pVariables != 0 )
   {
      delete[] pVariables;
      pVariables = 0;
   } // end if
   if( FunctionObject != 0 )
   {
      delete FunctionObject;
      FunctionObject = 0;
   } // end if not null
   if( RandomVariable != 0 )
   {
      delete RandomVariable;
      RandomVariable = 0;
   } // end if not null
} //end class constructor Vector

//***********************************************************************
// Class    : VectoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  Setup
// Purpose  :  Sets up our data
//***********************************************************************
void VectoR::Setup()
{
   for(int i=0;i<cnRows;i++)
   {
      pVariables[i] = 0;
   } //end for loop
   RandomVariable = new RandoM(0, 1);
   FunctionObject = new FunctioN(_OVERFLOW_, 1);
   RandomVariable->CalcSeed();
} // end Setup

//***********************************************************************
// Class   : VectoR
// Purpose : Overload the + operator for the addition of two vecs
//***********************************************************************
/*
VectoR& VectoR::operator + (const VectoR& b)
{
   VectoR* theVec = new VectoR( this );

   for( int i=0; i<cnRows; i++ )
   {
      theVec->pVariables[i] += b.pVariables[i];
   } // end for

   return *theVec;
} // end operator + overload
*/

//***********************************************************************
// Class   : VectoR
// Purpose : Overload the + operator for the addition of two vecs
//***********************************************************************
VectoR* VectoR::Add( VectoR* b )
{
   VectoR* theVec = new VectoR( this );

   for( int i=0; i<cnRows; i++ )
   {
      theVec->pVariables[i] += b->pVariables[i];
   } // end for

   return theVec;
} // end operator + overload

//***********************************************************************
// Class   : VectoR
// Purpose : Overload the += operator for the assignment of two vecs
//***********************************************************************
VectoR& VectoR::operator += (const VectoR& rhs)
{
   for(int i=0; i<cnRows; i++ )
   {
      pVariables[i] += rhs.pVariables[i];
   }

   return *this;
} // end operator += overload

//***********************************************************************
// Class   : VectoR
// Purpose : Overload the * operator for the dot product of two vecs
// Notes   : Actually, this operation is a little different from
//         : the 'real' dot product, but we do this simply to preserve
//         : the operations, the caller do what he wishes with the result.
//***********************************************************************
/*
VectoR& VectoR::operator * (const VectoR& b)
{
   VectoR* theVec = new VectoR( this );

   for( int i=0; i<cnRows; i++ )
   {
      theVec->pVariables[i] *= b.pVariables[i];
   }

   return *theVec;
} //end overload
*/

//***********************************************************************
// Class   : VectoR
// Purpose : Overload the * operator for the dot product of two vecs
// Notes   : Actually, this operation is a little different from
//         : the 'real' dot product, but we do this simply to preserve
//         : the operations, the caller do what he wishes with the result.
//***********************************************************************
VectoR* VectoR::Product( VectoR* b )
{
   VectoR* theVec = new VectoR( this );

   for( int i=0; i<cnRows; i++ )
   {
      theVec->pVariables[i] *= b->pVariables[i];
   }

   return theVec;
} //end overload

//***********************************************************************
// Class   : VectoR
// Purpose : Overload the * operator for the dot product of two vecs
// Notes   : Actually, this operation is a little different from
//         : the 'real' dot product, but we do this simply to preserve
//         : the operations, the caller do what he wishes with the result.
//***********************************************************************
VectoR& VectoR::operator *= (const VectoR& b)
{
   for( int i=0; i<cnRows; i++ )
   {
      pVariables[i] *= b.pVariables[i];
   }

   return *this;
} //end overload

//***********************************************************************
// Class   : VectoR
// Purpose : Overload the % operator for the dot product of two vecs
// Notes   : We're actually computing the outer product of two vectors.
//         : 
//         : 
//***********************************************************************
/*
MatriX& VectoR::operator % (const VectoR& b)
{
   MatriX* theMatrix = new MatriX( cnRows, b.cnRows );

   for( int i=0; i<cnRows; i++ )
   {
      for( int j=0; j<b.cnRows; j++ )
      {
         theMatrix->pCol[i][j] = (pVariables[i] * b.pVariables[j]);
      }
   }

   return *theMatrix;
} //end overload
*/

//***********************************************************************
// Class   : VectoR
// Purpose : Overload the % operator for the dot product of two vecs
// Notes   : We're actually computing the outer product of two vectors.
//         : 
//         : 
//***********************************************************************
MatriX* VectoR::OuterProduct( VectoR* b )
{
   MatriX* theMatrix = new MatriX( cnRows, b->cnRows );

   for( int i=0; i<cnRows; i++ )
   {
      for( int j=0; j<b->cnRows; j++ )
      {
         theMatrix->pCol[i][j] = (pVariables[i] * b->pVariables[j]);
      }
   }

   return theMatrix;
} //end overload

//***********************************************************************
// Class   : VectoR
// Purpose : Overload the = operator for the multiplication of a 
//         : vec and a matrix
// Notes   :  !!! Observe that the vector's number of rows MUST be
//         :  equivalent to the matrice's number of columns, or else
//         :  we're going to get some serious trouble!!
//***********************************************************************
VectoR& VectoR::operator = (const long double* rhs)
{
   for( int j=0; j<cnRows; j++ )
   {
      pVariables[j] = rhs[j];
   } //end for loop

   return *this;
} //end operator = b(x)
                 
//***********************************************************************
// Class   : VectoR
// Purpose : Overload the = operator for the assignment of two vecs
//***********************************************************************
VectoR& VectoR::operator = (const VectoR& b)
{
   for( int i=0; i<b.cnRows; i++ )
   {
      pVariables[i] = b.pVariables[i];
   } //end for loop

   return *this;
} //end overload

//***********************************************************************
// Class   : VectoR
// Purpose : Overload the == operator for the comparison of two vecs
//***********************************************************************
long double VectoR::operator == (const VectoR& rhs)
{
   if(cnRows != rhs.cnRows)
   {
      return -1;
   }
      
   long double ldRet = 0;
   long double ldTmp = 0;
   
   for( int iRs = 0; iRs < rhs.cnRows; iRs++ )
   {
      ldTmp = pVariables[iRs] - rhs.pVariables[iRs]; // get diff
      ldTmp *= ldTmp; // make non-negative
      ldRet += ldTmp;
   } // end for

   return ldRet;
} // end operator == overload

//***********************************************************************
// Class   : VectoR
// Purpose : Overload the - operator for the assignment of two vecs
//***********************************************************************
/*
VectoR& VectoR::operator - (const VectoR& rhs)
{
   VectoR* theVec = new VectoR( this );

   for( int i=0; i<cnRows; i++ )
   {
      theVec->pVariables[i] -= rhs.pVariables[i];
   }

   return *theVec;
} // end operator - overload
*/

//***********************************************************************
// Class   : VectoR
// Purpose : Overload the - operator for the assignment of two vecs
//***********************************************************************
VectoR* VectoR::Subtract( VectoR* rhs )
{
   VectoR* theVec = new VectoR( this );

   for( int i=0; i<cnRows; i++ )
   {
      theVec->pVariables[i] -= rhs->pVariables[i];
   }

   return theVec;
} // end operator - overload

//***********************************************************************
// Class   : VectoR
// Purpose : Overload the -= operator for the assignment of two vecs
//***********************************************************************
VectoR& VectoR::operator -= (const VectoR& rhs)
{
   for( int i=0; i<cnRows; i++ )
   {
      pVariables[i] -= rhs.pVariables[i];
   }

   return *this;
} // end operator -= overload

//***********************************************************************
// Class   : VectoR
// Purpose : Overload the << operator
//***********************************************************************
ostream& operator << (ostream& o, const VectoR& rhs)
{
int R;

   for(R=0;R<rhs.cnRows;R++)
      o<<rhs.pVariables[R]<<'\t';

   o<<"Magnitude: "<<rhs.cnMagnitude<<endl;

   return o;
} // end operator << overload

//***********************************************************************
// Class    :  VectoR
// Purpose  :  Member function that fills the vector with random decimals
//***********************************************************************
void VectoR::Fill_Rand()
{
   for(int i=0;i<cnRows;i++)
      pVariables[i] = RandomVariable->GetRandomLngDouble();

} //end Get_Rand

//***********************************************************************
// Class    :  VectoR
// Purpose  :  Member function that fills the vector with random Integers
//          :  between zero and the passed in argument.
//***********************************************************************
void VectoR::Fill_Rand_Integer(int nMax)
{
   RandomVariable->SetEndPoints(0, nMax);
   for(int i=0;i<cnRows;i++)
      pVariables[i] = (long double)RandomVariable->GetRandomInt();

} //end Get_Rand_Integer

//***********************************************************************
// Class    :  VectoR
// Purpose  :  Member function that zeroes out a vector
//***********************************************************************
void VectoR::Zero_Out()
{
   for(int i=0;i<cnRows;i++)
   {
      pVariables[i] = 0;
   } // end for loop
} // end Zero out member

//***********************************************************************
// Class    :  VectoR
// Purpose  :  Member function that fills out all entries with ones
//***********************************************************************
void VectoR::Fill_Ones()
{
   for(int i=0;i<cnRows;i++)
   {
      pVariables[i] = 1;
   } // end for loop
} // end Fill_ones member

//************************************************************************
// Class   : VectoR
// Purpose : This member function computes the magnitude of each column
//************************************************************************
void VectoR::CalcMagnitude()
{
   long double nTemp = 0;

   for( int i=0; i<cnRows; i++ )
   {
      nTemp += (pVariables[i] * pVariables[i]);
   } // end for loop
   cnMagnitude = sqrtl( nTemp );
} // end CalcMagnitude

//************************************************************************
// Class   : VectoR
// Purpos  : This member function normalizes the vector
//************************************************************************
void VectoR::Normalize()
{
   CalcMagnitude();

   for( int i=0; i<cnRows; i++ )
   {
      pVariables[i] /= cnMagnitude;
   } // end for loop
} // end Normalize

//************************************************************************
// Class    :  VectoR
// Purpose  :  This member function multiplies a vector by a scalar
//************************************************************************
void VectoR::Scale(long double nAlpha)
{
   for( int i=0; i<cnRows; i++ )
   {
      pVariables[i] *= nAlpha;
   } // end for loop
} // end Normalize

//************************************************************************
// Class    :  VectoR
// Purpose  :  
//************************************************************************
void VectoR::Add( long double nAlpha )
{
   for(int i=0;i<cnRows;i++)
   {
      pVariables[i] += nAlpha;
   } // end for loop
      
} // end Add

//***********************************************************************
// Class    :  VectoR
// Purpose  :  Member function that outputs the vector to standard output
//***********************************************************************
/* void VectoR::Display(BOOL lSpace)
{
   for(int i=0;i<cnRows;i++)
   {
      if(lSpace)
         cout<<pVariables[i]<<" ";
      else
         cout<<pVariables[i]<<"\n";
   } //end for loop
} //end Display */

//***********************************************************************
// Class    :  VectoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  GetSum
// Purpose  :  Returns the sum of the components of the vector
//***********************************************************************
long double VectoR::GetSum()
{
long double ldTheSum = 0;
int iSumCount = 0;

   for(iSumCount=0;iSumCount<cnRows;iSumCount++)
   {
      ldTheSum += pVariables[iSumCount];
   } // end for loop
   
   return ldTheSum;
} // end GetSum

//***********************************************************************
// Class    :  VectoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  GetSumSquared
// Purpose  :  Returns the sum of the components of the vector
//***********************************************************************
long double VectoR::GetSumSquared()
{
long double ldTheSum = 0;
int iSumCount = 0;

   for(iSumCount=0;iSumCount<cnRows;iSumCount++)
   {
      ldTheSum += (pVariables[iSumCount] * pVariables[iSumCount]);
   } // end for loop
   
   return ldTheSum;
} // end GetSum

//***********************************************************************
// Class    :  VectoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  CheckForZeros
// Purpose  :  Checks all the members of the vector for zeros; if it
//          :  finds them, it turns them to absolute zero.
//***********************************************************************
void VectoR::CheckForZeros()
{
int iCheckCnt = 0;

   for(iCheckCnt=0;iCheckCnt<cnRows;iCheckCnt++)
   {
      if(FunctionObject->Absolute(pVariables[iCheckCnt]) <
         FunctionObject->GetEpsilon())
            pVariables[iCheckCnt] = 0;
   } // end for 
} // end CheckForZeros

//***********************************************************************
// Class    :  VectoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  CheckForInfinity
// Purpose  :  Checks all the members of the vector for INF; if it
//          :  finds them, it turns them to a big number.
//***********************************************************************
void VectoR::CheckForInfinity()
{
int iCheckCnt2 = 0;
long double ldTooBig = 1e2300;

   for(iCheckCnt2=0;iCheckCnt2<cnRows;iCheckCnt2++)
   {
      if(pVariables[iCheckCnt2] >= ldTooBig)
         pVariables[iCheckCnt2] = 1e2000;
   } // end for 
} // end CheckForInfinity

//***********************************************************************
// Class :  VectoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  GetAbsoluteVector
// Purpose  :  Checks all the members of the vector for INF; if it
//       :  finds them, it turns them to a big number.
//***********************************************************************
VectoR* VectoR::GetAbsoluteVector()
{
VectoR* theRetVector = new VectoR( cnRows );

    for( int i = 0; i < cnRows; i++ )
        theRetVector->pVariables[ i ] = FunctionObject->Absolute( pVariables[ i ] );

    return theRetVector;
} // end GetAbsoluteVector

//***********************************************************************
// Class    :  VectoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  GetSmallestNumber
// Purpose  :  
//          :  
//***********************************************************************
long double VectoR::GetSmallestNumber()
{
   long double theSmallest = pVariables[ 0 ];

   for( int i=1; i<cnRows; i++ )
   {
      if( pVariables[ i ] < theSmallest )
      {
         theSmallest = pVariables[ i ];
      } // end if smaller
   } // end for loop

   return theSmallest;
} // end GetSmallestNumber

//***********************************************************************
// Class    :  VectoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  GetMinElement
// Purpose  :  
//          :  
//***********************************************************************
long double VectoR::GetMinElement( int start, int length )
{
   long double theSmallest = pVariables[ start ];
   int stop = (cnRows - length);
   if( stop < -1 )
   {
      stop = -1;
   }

   for( int i=(start-1); i>stop; i-- )
   {
      if( pVariables[ i ] < theSmallest )
      {
         theSmallest = pVariables[ i ];
      } // end if smaller
   } // end for loop

   return theSmallest;
} // end GetSmallestNumber

//***********************************************************************
// Class    :  VectoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  GetMaxElement
// Purpose  :
//          :
//***********************************************************************
long double VectoR::GetMaxElement( int start, int length )
{
   long double theLargest = pVariables[ start ];
   int stop = (cnRows - length);
   if( stop < -1 )
   {
      stop = -1;
   }

   for( int i=(start-1); i>stop; i-- )
   {
      if( pVariables[ i ] > theLargest )
      {
         theLargest = pVariables[ i ];
      } // end if larger
   } // end for loop

   return theLargest;
} // end GetMaxElement

//***********************************************************************
// Class    :  VectoR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  GetLargestNumber
// Purpose  :  
//          :  
//***********************************************************************
long double VectoR::GetLargestNumber()
{
long double theLargest = pVariables[ 0 ];

   for( int i=1; i<cnRows; i++ )
   {
      if( pVariables[ i ] > theLargest )
      {
         theLargest = pVariables[ i ];
      } // end if larger
   } // end for loop

   return theLargest;
} // end GetLargestNumber

//***********************************************************************
// Class    :  VectoR
// Purpose  :  Member function that fills the vector with random decimals
// Notes    :  The Gaussian distribution is the following:
//          :   f(y) = (sqrt(2pi)sigma)^-1 * exp-( (y-u)^2 / 2sigma^2 )
//          :    where y is a uniformly distributed ramdom, u is the mean
//          :    and sigma is the variance.
//***********************************************************************
void VectoR::FillGaussianRandom()
{
   // Here, we will default to mean = zero and sigma = 1.
   // sqrt(2pi) = 2.50662827463100050241576528481105
   // sqrt(2pi)^-1 = 0.398942280401432677939946059934382
   long double ldTemp = 0;
   long double w, x1, x2, y1, y2;
   for(int i=0;i<cnRows;i+=2)
   {
      do
      {
         x1 = 2 * RandomVariable->GetRandomLngDouble() - 1;
         x2 = 2 * RandomVariable->GetRandomLngDouble() - 1;
         w = x1 * x1 + x2 * x2;
      } while( w >= 1 );
      w = sqrtl( (-2 * logl( w )) / w );
      y1 = x1 * w;
      y2 = x2 * w;
      pVariables[i] = y1;
      if( i+1 < cnRows )
         pVariables[i+1] = y2;
   } // end for loop

} // end FillGaussianRandom

//***********************************************************************
// Class    :  VectoR
// Purpose  :  Member function that fills the vector with random decimals
//***********************************************************************
void VectoR::FillUniformRandom()
{
   for(int i=0;i<cnRows;i++)
      pVariables[i] = 2 * PI * RandomVariable->GetRandomLngDouble();

} // end FillUniformRandom

//***********************************************************************
// Class    :  VectoR
// Purpose  :  
//***********************************************************************
void VectoR::Print()
{
   FPRINT( "VectoR::Print" );

   for( int i=0; i<cnRows; i++ )
   {
      FPRINT << pVariables[i];
   }
} // end FillUniformRandom
