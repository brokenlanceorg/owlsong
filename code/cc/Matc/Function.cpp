//***************************************************************************** 
// File		:	Function.cpp
// Purpose	:	This file contains some mathematical functions which
//				:  will be used in the various math and graphic
//				:  libraries. To use include function.hpp
//*****************************************************************************
#include<math.h>
#include<string.h>
#include<errno.h>
#include<complex.h>
#include<conio.h>
#include<signal.h>
#include"constant.h"
//#include"types.hpp"
//***********************************************
// Prototypes
//***********************************************
int Find_Max(long double nX,long double nY);

//***********************************************************************
// Miscellaneous Functions
// Purpose	:  The following functions are not mathematical functions in 
//				:  the truest sense - they provide various utilities such as
//				:  finding the maximum element, machine precision, etc.
//***********************************************************************


//***********************************************************************
// Function	:	Absolute
// Purpose	:  Returns the absolute value of a long double number
//				:  We have to write this func because C++ doesn't have one
//				:  defined for the long double type, only for int data type
//***********************************************************************
long double Absolute(long double nA)
{
	if(nA < 0)
		nA *= (-1);

	return nA;
} // end Absolute

//***********************************************************************
// Function	:	Find_Max
// Purpose	:  Finds the maximum of three elements
//***********************************************************************
long double Find_Max(long double nX,long double nY,long double nZ)
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
// Function	:	Find_Max
// Purpose	:  Finds the maximum of two elements, if the first element
//				:  is larger, we return 1, else return 0
//***********************************************************************
int Find_Max(long double nX, long double nY)
{
int bTemp = 0;

	if(nX > nY)
		bTemp = 1;

	return bTemp;
} // end Find_Max/2
									  
//***********************************************************************
// Function	:	Machine_Eps
// Purpose	:  Calculates the smallest numerical element given the base
//				:  of the number and the number of digits representable
//***********************************************************************
long double Machine_Eps(int nBase, int nDigits)
{
long double nResult = 0;

	nDigits -= 1;
	nDigits *= (-1);
	nResult = powl(nBase, nDigits);

	return nResult;
} // end Machine_Eps

//***********************************************************************
// Function	:	Find_Max
// Purpose	:  Finds the maximum of two elements Integer version
//***********************************************************************
/*
int Find_Max(int nX, int nY)
{
int bTemp = nY;

	if(abs(nX) > abs(nY))
		bTemp = nX;

	return bTemp;
} // end Find_Max/2 */
									  
//***********************************************************************
// Function	:	Check_Endpoints
// Purpose	:  Returns 1 if the funtion crosses the x-axis with a negative
//				:  slope, 0 if it crosses with positive slope, otherwise,
//				:  we return UNDERTM.
//***********************************************************************
int Check_Endpoints(long double (*Func)(long double nX),
	long double nA, long double nB, long double nZero)
{
int bRes = 1;
long double nTemp1 = 0;

	nTemp1 = Func(nA); // sample function at left endpoint
	if(nTemp1 <= nZero)
	{
		bRes = 0; // we're in case two: positive slope
		nTemp1 = Func(nB);
		if(nTemp1 <= nZero) // it appears that the interval contains no roots
			bRes = 2;
	} // end if     
	else
	{
		nTemp1 = Func(nB); // we're in case one: negative slope
		if(nTemp1 >= nZero) // it appears that the interval contains no roots
			bRes = 2;
	} // end else
	
	return bRes;
} // end Check_Enpoints


//***********************************************************************
// Function	:	Calc_Iters
// Purpose	:  Returns the number of iterations needed for the bisection
//				:  method given the degree of tolerance
//***********************************************************************
int Calc_Iters(long double nTau, long double nLeft, long double nRight)
{
long double nTemp1 = 0;
int nIterations = 0;

	nTemp1 = (Absolute(nRight) - Absolute(nLeft)) / nTau;
	nTemp1 = logl(nTemp1);
	nTemp1 /= logl(2);
	nTemp1 += 0.5;
	nIterations = (int)(nTemp1);

	return nIterations;

} // end Calc_Iters

//***********************************************************************
// Function	:	Secant
// Purpose	:  Returns the root of an equation given two root guesses
//				:  and two function evaluations at those points
//***********************************************************************
long double Secant(long double (*Function1)(long double nX),
			long double nApprox1, long double nApprox2, long double nEval1,
			long double nEval2, long double nEps)
{
long double nNew_Eval = 0;
long double nNew_Root = 0;
long double nTemp = 0;
long double nTemp2 = 0;
static int nIters = 0;

	nIters += 1;
	if(nIters > 65000)
		return nApprox2;
	nTemp = nEval2 * (nApprox2 - nApprox1);
	nTemp2 = (nEval2 - nEval1);
	//cout<<"nTemp2 is: "<<nTemp2<<'\n';
	if(Absolute(nTemp2) >= nEps)
		nTemp = nTemp / nTemp2;
	else
		return nApprox2; // we can't perform the division, so root is approx2
	nNew_Root = nApprox2 - nTemp;
	nNew_Eval = Function1(nNew_Root); /*
	cout<<"new_root is: "<<nNew_Root<<'\n';
	cout<<"new_eval is: "<<nNew_Eval<<'\n';
	getch(); */
	if(Absolute(nNew_Eval) <= nEps)
	{
		cout<<"it took "<<nIters<<" iterations"<<endl;
		return nNew_Root;
	} // end if criterion
	else
		nNew_Root = Secant(Function1, nApprox2, nNew_Root, nEval2, nNew_Eval, nEps);

	return nNew_Root;
} // end Secant

//***********************************************************************
// Function	:	Check_Not_Zero
// Purpose	:  Checks the argument to make sure that it is not sufficiently
//				:  close to zero; if it is, we make it NOT zero, otherwise,
//				:  we simply return the argument back to the caller.
//***********************************************************************
long double Check_Not_Zero(long double nArg, long double nEps)
{
long double nTemp = 1e-10;

	if(Absolute(nArg) <= nEps)
		return nTemp;
	else
		return nTemp = nArg;
} // end Check_Not_Zero

//***********************************************************************
// Function	:	Catcher
// Purpose	:  Traps numerical errors through the global variable SIGFPE
//				:  This function catches overflow and divide by zero along
//				:  with other numerical errors. We fix things by returning
//				:  a one regardless.
//***********************************************************************
void Catcher(int* nSIG)
{
	switch(SIGFPE)
	{
		case 8 : // for floating point errors
			cout<<"contents of nSIG were: "<<*(nSIG)<<endl;
			*(nSIG) = 1;
			cout<<"An Exception occured!"<<endl;
			cout<<"ERRNO is: "<<errno<<endl;
			// exit(EXIT_SUCCESS); could exit if we want to
			// There seem to be problems ONLY when we divide by a number that
			// is on the order of 1e-13 to 1e-20, for some reason, numbers
			// smaller than the machine epsilon are okay!?
		break;
		case 9 : // for other errors
		break;
		default : // default case
		break;
	} // end switch
/*	signal(SIGFPE, (fptr)Catcher); // error handler
	signal(SIGABRT, (fptr)Catcher); // error handler */
	return;                     
} // end catcher

//***********************************************************************
// Function	:	Matherrl
// Purpose	:  Traps numerical errors through the global variable SIGFPE
//***********************************************************************
int matherrl(struct exception *eElement)
{
	switch(eElement->type)
	{
		case DOMAIN :
			if(!strcmp(eElement->name, "sqrtl"))
				eElement->retval = sqrtl(-(eElement->arg1));
			cout<<"in matherr!"<<endl;
			return 1;
			if(!strcmp(eElement->name, "logl"))
				eElement->retval = logl(-(eElement->arg1 + 1));
			cout<<"in matherr!"<<endl;
			return 1;                 
		break;      
		case SING :
			if(!strcmp(eElement->name, "logl"))
				eElement->retval = logl(-(eElement->arg1));
			if(!strcmp(eElement->name, "sinl"))
				eElement->retval = sinl(-(eElement->arg1));
			cout<<"in matherr!"<<endl;
			return 1;                 
		break;
	} // end switch                 
	return 0;
} // end matherrl

//*****************************************************************************
// Complex Functions:
//*****************************************************************************
complex Function1(complex nZ)
{
complex nZtemp;
long double nTemp1 = 0;

/*	if((nZ.re == 0) || (nZ.re < 1e-2000)) // some error handling junk
		nZ.re = 1;

	if((nZ.im == 0) || (nZ.im < 1e-2000))
		nZ.im = 1; 

	nTemp1 = nZ.re * nZ.re;
	nZtemp.re = (nZ.re * nTemp1) - 3 * nTemp1;

	nTemp1 = nZ.im * nZ.im * 3;
	nZtemp.re += nTemp1 - (nZ.im * nTemp1);

	nZtemp.im = nZ.re * nZ.re * 3 * nZ.im;
	nTemp1 = (6 * nZ.re * nZ.im) - (nZ.re * nZ.im);
	nZtemp.im += nTemp1;

	if((nZtemp.re == 0) || (nZtemp.re < 1e-2000)) // some error handling junk
		nZ.re = 1;

	if((nZtemp.im == 0) || (nZtemp.im < 1e-2000))
		nZtemp.im = 1; */

	return nZtemp;
} // end Function1

complex Function2(complex nZ)
{
complex nZtemp;
long double nTemp1 = 0;

/*	if((nZ.re == 0) || (nZ.re < 1e-2000)) // some error handling junk
		nZ.re = 1;

	if((nZ.im == 0) || (nZ.im < 1e-2000))
		nZ.im = 1;

	nTemp1 = (nZ.re * nZ.re) - (nZ.im * nZ.im);
	nZtemp.re = 3 * (nTemp1 - 2 * nZ.re);

	nTemp1 = nZ.re - 1;
	nZtemp.im = 6 * nZ.im * nTemp1;

	if((nZtemp.re == 0) || (nZtemp.re < 1e-2000)) // some error handling junk
		nZ.re = 1;

	if((nZtemp.im == 0) || (nZtemp.im < 1e-2000))
		nZtemp.im = 1; */

	return nZtemp;	
} // end Function2


//*****************************************************************************
// Real Valued Functions:
//*****************************************************************************
long double Cos2(long double nZ)
{
long double nZtemp;

	if((nZ > 1.0e-4000) && (nZ < 1e-2000)) // some error handling junk
		nZ = 1;

	nZtemp = cosl(nZ) * cosl(nZ);

	if((nZtemp > 1.0e-4000) && (nZtemp < 1e-2000)) // some error handling junk
		nZtemp = 1;

	return nZtemp;
} // end Function3

long double Dx_Cos2(long double nZ)
{
long double nZtemp;

	if((nZ > 1.0e-4000) && (nZ < 1.0e-2000)) // some error handling junk
		nZ = 1;
	// by a triginometric identity, this is just:
	nZtemp = -sinl(2 * nZ);

	if((nZtemp > 1.0e-4000) && (nZtemp < 1.0e-2000)) // some error handling junk
		nZtemp = 1;

	return nZtemp;
} // end Function4

//**************************************************************
// Function	: Radians_2_Deg
// Purpose	: Returns the argument expressed as degrees
//**************************************************************
long double Radians_2_Deg(long double nRad)
{
long double nTemp = 0;

	nTemp = nRad * PI_UNDER_180;
	return nTemp;

} // end Radians_2_Deg

//**************************************************************
// Function	: Radians_2_Deg
// Purpose	: Returns the argument expressed as Radians
//**************************************************************
long double Degrees_2_Rad(long double nDeg)
{
long double nTemp = 0;

	nTemp = nDeg * PI_OVER_180;
	return nTemp;

} // end Degrees_2_Rad

//**************************************************************
// Function	: Decimal_Power
// Purpose	: Raises nX1 floating point decimal to the power
//				: of the floating point decimal nY1
//**************************************************************
long double Decimal_Power(long double nX1, long double nY1, long double nEps)
{
long double nTemp;

	if(Absolute(nX1) <= nEps)
		nX1 = 1;						  
	if(Absolute(nY1) <= nEps)
		nY1 = 1;						  

	nTemp = (logl(nX1) * nY1);
	nTemp = exp(nTemp); // we could overflow, but we'll handle that
	if(Absolute(nTemp) <= nEps)
		nTemp = 1;

	return nTemp;
} // end Decimal_Power

//**************************************************************
// Function	: ae_^bx
// Purpose	: Raises nX1 floating point decimal to the power
//				: of the floating point decimal nY1
//**************************************************************
long double ae_to_bX(long double nA, long double nB, long double nX)
{
long double nTemp = 0;

	if((nA > 1.0e-4000) && (nA < 1.0e-2000)) // some error handling junk
		nA = 1;
	if((nB > 1.0e-4000) && (nB < 1.0e-2000)) // some error handling junk
		nB = 1;
	if((nX > 1.0e-4000) && (nX < 1.0e-2000)) // some error handling junk
		nX = 1;

	nTemp = nB * nX;
	nTemp = expl(nTemp);
	nTemp *= nA;

	return nTemp;		  
} // end ae_^bX     

//**************************************************************
// Function	: ae_to_negbX2
// Purpose	: Raises nX1 floating point decimal to the power
//				: of the floating point decimal nY1
//**************************************************************
long double ae_to_negbX2(long double nA, long double nB, long double nX)
{
long double nTemp = 0;

	if((nA > 1.0e-4000) && (nA < 1.0e-2000)) // some error handling junk
		nA = 1;
	if((nB > 1.0e-4000) && (nB < 1.0e-2000)) // some error handling junk
		nB = 1;
	if((nX > 1.0e-4000) && (nX < 1.0e-2000)) // some error handling junk
		nX = 1;

	nTemp = nB * nX * nX;
	nTemp = 1 / nTemp;
	nTemp = expl(nTemp);
	nTemp *= nA;

	return nTemp;
} // end ae_to_negbX2

//**************************************************************
// Function	: ae_to_negbX2
// Purpose	: Raises nX1 floating point decimal to the power
//				: of the floating point decimal nY1
// ********************* test function *******************
//**************************************************************
long double ae_to_negbX2_temp(long double nA, long double nB, long double nX)
{
long double nTemp = 0;

	if((nA > 1.0e-4000) && (nA < 1.0e-2000)) // some error handling junk
		nA = 1;
	if((nB > 1.0e-4000) && (nB < 1.0e-2000)) // some error handling junk
		nB = 1;
	if((nX > 1.0e-4000) && (nX < 1.0e-2000)) // some error handling junk
		nX = 1;

	nTemp = nA * expl(1 / (nB * nX * nX));

	return nTemp;
} // end ae_to_negbX2 test*******

//**************************************************************
// Function	: ae_to_bcosX
// Purpose	: Raises nX1 floating point decimal to the power
//				: of the floating point decimal nY1
//**************************************************************
long double ae_to_bcosX(long double nA, long double nB, long double nX)
{
long double nTemp = 0;

	if((nA > 1.0e-4000) && (nA < 1.0e-2000)) // some error handling junk
		nA = 1;
	if((nB > 1.0e-4000) && (nB < 1.0e-2000)) // some error handling junk
		nB = 1;
	if((nX > 1.0e-4000) && (nX < 1.0e-2000)) // some error handling junk
		nX = 1;

	nTemp = nB * cosl(nX);
	nTemp = expl(nTemp);
	nTemp *= nA;

	return nTemp;
} // end ae_to_bcosX

//**************************************************************
// Function	: ae_to_bsinX
// Purpose	: Raises nX1 floating point decimal to the power
//				: of the floating point decimal nY1
//**************************************************************
long double ae_to_bsinX(long double nA, long double nB, long double nX)
{
long double nTemp = 0;

	if((nA > 1.0e-4000) && (nA < 1.0e-2000)) // some error handling junk
		nA = 1;
	if((nB > 1.0e-4000) && (nB < 1.0e-2000)) // some error handling junk
		nB = 1;
	if((nX > 1.0e-4000) && (nX < 1.0e-2000)) // some error handling junk
		nX = 1;

	nTemp = nB * sinl(nX);
	nTemp = expl(nTemp);
	nTemp *= nA;

	return nTemp;
} // end ae_to_bsinX

//**************************************************************
// Function	: ae_to_btanX
// Purpose	: Raises nX1 floating point decimal to the power
//				: of the floating point decimal nY1
//**************************************************************
long double ae_to_btanX(long double nA, long double nB, long double nX)
{
long double nTemp = 0;

	if((nA > 1.0e-4000) && (nA < 1.0e-2000)) // some error handling junk
		nA = 1;
	if((nB > 1.0e-4000) && (nB < 1.0e-2000)) // some error handling junk
		nB = 1;
	if((nX > 1.0e-4000) && (nX < 1.0e-2000)) // some error handling junk
		nX = 1;

	nTemp = nB * tanl(nX);
	nTemp = expl(nTemp);
	nTemp *= nA;

	return nTemp;
} // end ae_to_btanX

//**************************************************************
// Function	: aX2_bX_c
// Purpose	: Multiplies a time x squared plus b times x plus c
//**************************************************************
long double aX2_bX_c(long double nA, long double nB, long double nC, long double nX)
{
long double nTemp = 0;

	nTemp = nX * nX;
	nTemp *= nA;
	nTemp += (nB * nX);
	nTemp += nC;

	if((nTemp > 1e-4000) && (nTemp < 1e-2000) || (nTemp == 0))
		nTemp = 1;

	return nTemp;
} // end aX2_bX_c

//**************************************************************
// Function	: Dx_aX2_bX_c
// Purpose	: Calculates the derivative of aX2_bX_c
//				: we don't use argument nC, but it's there for
//				: compatibility with other general functions
//**************************************************************
long double Dx_aX2_bX_c(long double nA, long double nB, long double nC, long double nX)
{
long double nTemp = 0;

	nTemp = 2 * nA * nX;
	nTemp += nB;

	if((nTemp > 1e-4000) && (nTemp < 1e-2000) || (nTemp == 0))
		nTemp = 1;

	return nTemp;
} // end Dx_aX2_bX_c

//**************************************************************
// Function	: Psyc_Time
// Purpose	: Calculates the time variable of the psyche
//				: we don't use argument nC, but it's there for
//				: compatibility with other general functions
//**************************************************************
long double Psych_Time(long double nA, long double nB, long double nC, long double nD)
{
long double nTemp = 0;
long double nA_2 = nA * nA;
long double nB_2 = nB * nB;
long double nC_2 = nC * nC;
long double nD_2 = nD * nD;

	if(nA != 0)
	{
		nTemp = .5 * nA_2 * (logl(1 / nA) + .5);
	} // end if
	if(nB != 0)
	{
		nTemp += .5 * nB_2 * (logl(1 / nB) + .5);
	} // end if 
	if(nC != 0)
	{
		nTemp += .5 * nC_2 * (logl(1 / nC) + .5);
	} // end if 
	if(nD != 0)
	{
		nTemp += .5 * nD_2 * (logl(1 / nD) + .5);
	} // end if 

	return nTemp;
} // end Psych_Time

//**************************************************************
// Function	: Dx_Psyc_Time
// Purpose	: Calculates the derivative of the time variable 
//				: of the psyche
//**************************************************************
long double Dx_Psych_Time(long double nA, long double nB, long double nC, long double nD)
{
long double nTemp = 0;

	if(nA != 0)
	{
		nTemp = nA * logl(1 / nA);
	} // end if
	if(nB != 0)
	{
		nTemp += nB * logl(1 / nB);
	} // end if 
	if(nC != 0)
	{
		nTemp += nC * logl(1 / nC);
	} // end if 
	if(nD != 0)
	{
		nTemp += nD * logl(1 / nD);
	} // end if 

	return nTemp;
} // end Dx_Psych_Time

//***********************************************************************
// Function	:	Dx_Tan
// Purpose	:  Computes the derivative of the tangent function
//***********************************************************************
long double Dx_Tan(long double nX)
{
long double nTemp1 = 0;

	nTemp1 = cosl(nX); // temp = sec(x)
	if(Absolute(nTemp1) > EPS)
   	nTemp1 = 1 / nTemp1;
	nTemp1 *= nTemp1; // temp = temp^2

	return nTemp1;
} // end Dx_Tan

//***********************************************************************
// Function	:	The Factorial function
// Purpose	:  Computes the factorial of a number
//***********************************************************************
unsigned long Factorial(unsigned long nP)
{
unsigned long nResult = nP;

	if(nResult >= 17) // this is as large as we can go with an unsigned long
		return 4294000000; // a nice big safe number
	if((nResult == 1) || (nResult == 0))
		return 1;
	else
		nResult = nResult * Factorial(nResult - 1);

	return nResult;
} // end Factorial

//***********************************************************************
// Function	:	The Gamma function (integer version)
// Purpose	:  Computes the Gamma function of a number
// Notes		:  The gamma function is the integral from zero to infinity
//				:  of INT t^(nP - 1) * e-t dt
//				:  The argument nP is a constant, nM is the variable in the
//				:  sense of the factorial operation.
//***********************************************************************
long int Gamma(long int nP, long int nM)
{
long int nResult = 1;

	if((nP + nM) >= 13) // this is as big as we can go
		return 2147483000; // a big long int number
	if(nM == 0)
		return Factorial(nP);
	if(nM == 1)
		return (nP + 1);
	else
		nResult = (nP + nM) * Gamma(nP, (nM - 1));

	return nResult;
} // end Gamma

//***********************************************************************
// Function	:	Bessel_Coeffs
// Purpose	:  Computes the respective coefficient to the Bessel
// Notes		:  function of order nNu of the first kind
//***********************************************************************
long double Bessel_Coeffs(long int nNu, unsigned long nM)
{
long double nCoeff = 0;
short int sSign = -1;
long int iFact = 0;
unsigned long lGam = 0;

	sSign = pow(sSign, nM);
	iFact = Factorial(nM);
	if(nM == 0)
		lGam = Gamma((long int)nNu, (long int)nM);
	else
		lGam = Gamma((long int)nNu, (long int)(nM + 1));

	nCoeff = (long double)(iFact) * (long double)(lGam);
	nCoeff = (long double)(sSign) / nCoeff;                         
	return nCoeff;     
} // end Bessel_Coeffs

//***********************************************************************
// Function	:	Bessel
// Purpose	:  Computes a partial sum of the infinite series known as
// Notes		:  Bessel's function of order nNu of the first kind
//				:  We use the third parameter as the stopping critirion.
//***********************************************************************
long double Bessel(long int nNu, long double nX, long double nEps)
{
long double nResult = 0;
static long double nResult2 = 0;
long double nCoeff = 0;
long int iPower = 0;
static unsigned long nCnt = 0;

	if(nCnt == 0)
		nResult2 = 0;
	nCoeff = Bessel_Coeffs(nNu, nCnt); // get the nth coefficient
	nResult = nX / 2;         
	iPower = (2 * nCnt) + nNu;
	nResult = powl(nResult, iPower); 
	nResult *= nCoeff;	// result of the nth term
	nCnt += 1;
	nResult2 += nResult;
	
	if((nCnt > 20) || (Absolute(nResult) < nEps))
	{
		nCnt = 0;
		return nResult;
	}
	else
		Bessel(nNu, nX, nEps);

	return nResult2;
} // end Bessel

//***********************************************************************
// Function	:	Bessel_Primer
// Purpose	:  Computes a partial sum of the infinite series known as
// Notes		:  Bessel's function of order nNu of the first kind
//				:  This functions as a 'primer' for the graphics class.
//***********************************************************************
long double Bessel_Primer(long int nNu, long double nX)
{
long double nR1 = 0;
long double nTau = 1e-9;

	nR1 = Bessel(nNu, nX, nTau);
	return nR1;
} // end Bessel_Primer

//***********************************************************************
// Function	:	Cauchy_Euler
// Purpose	:  Computes the function known as the Cauchy_Euler equation
// Notes		:  This equation is a solution to the Cuachy-Euler differential
//				:  equation found in mathematics.
//***********************************************************************
long double Cauchy_Euler(long double nAlpha, long double nBeta, long double nC1,
	long double nC2, long double nEps, long double nX)
{
long double nResult = 0;
long double nTemp1 = 0;
long double nTemp2 = 0;

	if(nX == 0)               
		nX = 1;
	nTemp1 = Absolute(nX) * nBeta;
	nTemp2 = logl(nTemp1);
	nResult = (nC1 * cosl(nTemp2)); // we can improve all this by using
	if((nTemp2 <= nEps) || (nTemp2 <= .1)); // some trig identities...
	else
		nResult += (nC2 * sinl(nTemp2));
	nTemp1 = Decimal_Power(nX, nAlpha, nEps);
	nResult *= nTemp1;          
	return nResult;             
} // end Cauchy_Euler

//***********************************************************************
// Function	:	ExpInvSquared
// Purpose	:  Computes the exponential function raised to the negative
// 			:  argument squared
//***********************************************************************
long double ExpInvSquared(long double ldArgX)
{
long double ExpResult = 0;
long double ldTmp = 0;

	ldTmp = ldArgX * ldArgX;
	ExpResult = expl(ldTmp);
	if(ExpResult != 0)
		ExpResult = 1 / ExpResult;

	return ExpResult;
} // end ExpInvSquared

//***********************************************************************
// Function	:	Logistic
// Purpose	:  The famous logistic equation. It could be more general,
// 			:  but we can always add that later; probably when we
//				:  make it a class
//***********************************************************************
long double Logistic(long double ldArg)
{
long double ldLogRes = 0;
long double ldTemp = 0;

	ldTemp = 1 / expl(ldArg);
	ldTemp += 1;
	if(Absolute(ldTemp) > 1e-25)
		ldLogRes = 1 / ldTemp;

	return ldLogRes;
} // end Logistic equation

//***********************************************************************
// Function	:	DxLogistic
// Purpose	:  The famous logistic equation. It could be more general,
// 			:  but we can always add that later; probably when we
//				:  make it a class
//***********************************************************************
long double DxLogistic(long double ldArg)
{
long double ldLogRes = 0;
long double ldLogTmp = 0;

	ldLogTmp = 1 / expl(ldArg);
	ldLogTmp += 1;
	ldLogTmp *= ldLogTmp;
	ldLogRes = 1 / expl(ldArg);
	ldLogRes /= ldLogTmp;

	return ldLogRes;
} // end DxLogistic equation

