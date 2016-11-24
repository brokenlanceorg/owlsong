//***************************************************************************** 
// File    : Func.cpp
// Purpose : This file contains the object oriented paradigm for the 
//         : mathematical functions to be used in various applications
// Notes   : This will be basically an abstract 'function' class to be
//         : inherited by the single, double, etc., function classes
//         : Note also that 1 / GetEpsilon() is equal to: 6.04463e+23
//Author   : Brandon Benham
//*****************************************************************************
#include"Func.hpp"

#define JOHNNY_BENCH catch(NumerR* eE) {eE->HandleError();}

//***************************************************************************** 
// Class   : FunctioN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method  : Default Constructor
// Purpose : Declares an abstract class for other specialized classes to inherit.
// Notes   : Constructs a numericalerror object with checking for divide
//         : by zero, and a machine epsilon
//*****************************************************************************
FunctioN::FunctioN(int iBase)
{
//FuncFPRINT( "FunctioN::FunctioN(int iBase)" );

   NumericalError = new NumerR(_DIVIDE_BY_ZERO_);
   if(iBase == 0)
      ldEpsilon = Machine_Eps(BASE_TWO, LNG_DBLE_PREC);
   else
      ldEpsilon = Machine_Eps(BASE_TEN, LNG_DBLE_PREC);
} // end default constructor

//***************************************************************************** 
// Class   : FunctioN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  Destructor
// Purpose  :  Declares an abstract class for other specialized classes to
//          :  inherit.
//*****************************************************************************
FunctioN::~FunctioN()
{
//FuncFPRINT( "FunctioN::~FunctioN()" );

   if ( NumericalError != 0 )
   {
      delete NumericalError;
      NumericalError = 0;
   } // end if not null
} // end destructor

//***************************************************************************** 
// Class    :  FunctioN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  Constructor
// Purpose  :  Declares an abstract class for other specialized classes to
//          :  inherit.
//*****************************************************************************
FunctioN::FunctioN(_ERROR_TYPE_ EtheEr, int iBase)
{
//FuncFPRINT( "FunctioN::FunctioN(_ERROR_TYPE_ EtheEr, int iBase)" );

   NumericalError = new NumerR(EtheEr);
   if(iBase == 0)
      ldEpsilon = Machine_Eps(BASE_TWO, LNG_DBLE_PREC);
   else
      ldEpsilon = Machine_Eps(BASE_TEN, LNG_DBLE_PREC);
} // end constructor

//***************************************************************************** 
// Class    :  FunctioN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  Constructor
// Purpose  :  Declares an abstract class for other specialized classes to
//          :  inherit. This constructor also sets the numerical error
//          :  object's function pointer to point to some other user-defined
//          :  handler routine.
//*****************************************************************************
FunctioN::FunctioN(_ERROR_TYPE_ EtheEr, long double (*UserFunc)(long double ldX),
   int iBase)
{
//FuncFPRINT( "FunctioN::FunctioN(_ERROR_TYPE_ EtheEr, int iBase, UserFunc)" );

   NumericalError = new NumerR(EtheEr, UserFunc);
   if(iBase == 0)
      ldEpsilon = Machine_Eps(BASE_TWO, LNG_DBLE_PREC);
   else
      ldEpsilon = Machine_Eps(BASE_TEN, LNG_DBLE_PREC);
} // end constructor

//***************************************************************************** 
// Class    :  FunctioN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  SetPrecision
// Purpose  :  Sets the machine epsilon variable
//*****************************************************************************
void FunctioN::SetPrecision(int iRadx, int iBits)
{
   ldEpsilon = Machine_Eps(iRadx, iBits);
} // end setprecision

//***************************************************************************** 
// Class    :  FunctioN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  Machine_Eps
// Purpose  :  Used to determine the level of accuracy to zero
//*****************************************************************************
long double FunctioN::Machine_Eps(int iBase, int iNumBits)
{
long double ldResult = 0;

   iNumBits -= 1;
   iNumBits *= (-1);
   ldResult = powl(iBase, iNumBits);

   return ldResult;
} // end Machine_Eps

//***************************************************************************** 
// Class    :  FunctioN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   :  Absolute
// Purpose  :  Used to determine the absolute value of a long double
//*****************************************************************************
long double FunctioN::Absolute(long double nA)
{
   if(nA < 0)
      nA *= (-1);

   return nA;
} // end Absolute

//**************************************************************
// Class    :  FunctioN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Decimal_Power
// Purpose  : Raises nX1 floating point decimal to the power
//          : of the floating point decimal nY1
// Notes    : Since the c expl funtion overflows on 1.1e4, we
//          : simply check for this possiblity.
//**************************************************************
long double FunctioN::DecimalPower(long double ldX1, long double ldY1)
{
long double ldTemp;
int iGreater = 0;

   NumericalError->SetErrorType(_UNDERFLOW_);
   if(Absolute(ldX1) <= ldEpsilon) // we know the result will effectively
      return 0;                    // be zero
//   else             Why in the hell is this here???
//    ldX1 = 1;                    
   // Set this because we know we're good to this point
   NumericalError->SetGoodVal(ldX1);
   try {
      // If it's zero, logl will choke, so throw it
      if(Absolute(ldX1) <= ldEpsilon)
         throw NumericalError;
      ldTemp = logl(Absolute(ldX1));
      ldTemp *= ldY1;
      // If this is zero, then we know that the good value will be one.
      if(Absolute(ldTemp) <= ldEpsilon) {
         NumericalError->SetGoodVal(1);
         throw NumericalError;}
      NumericalError->SetGoodVal(ldTemp);
      if(ldX1 >= 1.1e4) {
         NumericalError->SetGoodVal(1.1e4);
         throw NumericalError;
         } // end if too big
      ldTemp = expl(ldTemp); // we could overflow, but we'll handle that
   } JOHNNY_BENCH;
   if(NumericalError->sHandled)
      ldTemp = NumericalError->GetGoodVal();
   return ldTemp;
} // end Decimal_Power

//***********************************************************************
// Function :  Find_Max
// Purpose  :  Finds the maximum of three elements
//***********************************************************************
long double FunctioN::Find_Max(long double nX,long double nY,long double nZ)
{
int bTemp1 = 0;

   bTemp1 = Find_Max(Absolute(nX), Absolute(nY));
   if(bTemp1 == 1)
   {
      bTemp1 = Find_Max(Absolute(nX), Absolute(nZ));
      if(bTemp1 == 1)
         return nX;
      else
         return nZ;
   } // end if 
   else
   {
      bTemp1 = Find_Max(Absolute(nY), Absolute(nZ));
      if(bTemp1 == 1)
         return nY;
      else
         return nZ;
   } // end else   

} // end Find_Max/3

//***********************************************************************
// Function :  Find_Max
// Purpose  :  Finds the maximum of two elements, if the first element
//          :  is larger, we return TRUE, else return FALSE
//***********************************************************************
int FunctioN::Find_Max(long double nX, long double nY)
{
int bTemp = 0;

   if(nX > nY)
      bTemp = 1;

   return bTemp;
} // end Find_Max/2
                             
//**************************************************************
// Class    : Function
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member   : RadiansToDegrees
// Purpose  : Returns the argument expressed as degrees
//**************************************************************
long double FunctioN::RadiansToDegrees(long double nRad)
{
   return (nRad * PI_OVER_180);
} // end RadiansToDegrees

//**************************************************************
// Class    : Function
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : DegreesToRadians
// Purpose  : Returns the argument expressed as Radians
//**************************************************************
long double FunctioN::DegreesToRadians(long double nDeg)
{
   return (nDeg * PI_UNDER_180);
} // end DegreesToRadians

//**************************************************************
// Class    :  FunctioN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Exponential
// Purpose  : Evaluates the exponential function.
//          : We check for overflow, because if the argument is
//          : larger than 1.2e4, we get an overflow.
//**************************************************************
long double FunctioN::Exponential(long double ldX1)
{
long double ldE;

   if(ldX1 > 11000)
      ldX1 = 11000;
   if(Absolute(ldX1) <= ldEpsilon)
      return ldE = 1;
   ldE = expl(ldX1);
//        long double temp = tanhl( ldX1 );
   if(Absolute(ldE) <= ldEpsilon)
      ldE = 0;

   return ldE;
} // end Exponential

//**************************************************************
// Class   : FunctioN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method  : NaturalLog
// Purpose : Compute the Natural Log of a number.
//         : 
//         : 
//**************************************************************
long double FunctioN::NaturalLog(long double ldXA)
{
long double ldL;

   ldXA = Absolute(ldXA);
   if(ldXA <= ldEpsilon)
      return 0;
   ldL = logl(ldXA);
   if(Absolute(ldL) <= ldEpsilon)
      ldL = 0;

   return ldL;
} // end NaturalLog

//**************************************************************
// Class   : FunctioN
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method  : VectorRadianAngle
// Purpose : Returns the angle in radians between the two Vectors.
// Notes   : Observe that we are considering these vectors as
//         : 2-Vectors.
//         :
//         : We're not going to break this up into other functions
//         : such as calcMagnitude, dotProduct, etc., because
//         : this method will only be used with the UAV code.
//**************************************************************
double FunctioN::VectorRadianAngle( double* A, double* B )
{

   double angle = A[0] * B[0] + A[1] * B[1];
   double magA = sqrt( A[0] * A[0] + A[1] * A[1] );
   double magB = sqrt( B[0] * B[0] + B[1] * B[1] );
   angle /= (magA * magB);
   if( angle > 1 )
   {
FPRINT( "AngleComp" );
FPRINT << angle;
      angle = 1;
   }
   else if( angle < -1 )
   {
FPRINT( "AngleComp" );
FPRINT << angle;
      angle = -1;
   }

   angle = acos( angle );

   return angle;
} // end VectorRadianAngle
