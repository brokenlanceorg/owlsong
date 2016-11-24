//***************************************************************************** 
// File		:	Function.hpp
// Purpose	:	This file contains the headers for some mathematical 
//				:  functions which will be used in the various math and graphic
//				:  libraries. 
//*****************************************************************************
#ifndef __FUNCTION_HPP
#define __FUNCTION_HPP

#include<math.h>
#include<signal.h>
#include<complex.h>
#include"c:\bc45\graph\types.hpp"

#define BASE_TWO 2
#define BASE_TEN 10
#define SINGL_PREC 32
#define DBLE_PREC 64
#define LNG_DBLE_PREC 80
//*****************************************************************************
// Complex Functions:
//*****************************************************************************
complex Function1(complex nZ);
complex Function2(complex nZ);

//*****************************************************************************
// Real Valued Functions:
//*****************************************************************************
long double Cos2(long double nZ);
long double Dx_Cos2(long double nZ);
long double Radians_2_Deg(long double nRad);
long double Degrees_2_Rad(long double nDeg);
long double ae_to_btanX(long double nA, long double nB, long double nX);
long double ae_to_bsinX(long double nA, long double nB, long double nX);
long double ae_to_bcosX(long double nA, long double nB, long double nX);
long double ae_to_negbX2(long double nA, long double nB, long double nX);
long double ae_to_bX(long double nA, long double nB, long double nX);
long double Decimal_Power(long double nX1, long double nY1, long double nEps);
long double aX2_bX_c(long double nA, long double nB, long double nC, long double nX);
long double Dx_aX2_bX_c(long double nA, long double nB, long double nC, long double nX);
long double ae_to_negbX2_temp(long double nA, long double nB, long double nX);
long double Psych_Time(long double nA, long double nB, long double nC, long double nD);
long double Dx_Psych_Time(long double nA, long double nB, long double nC, long double nD);
long double Dx_Tan(long double nX);
unsigned long Factorial(unsigned long nP);
long int Gamma(long int nP, long int nM);
long double Bessel_Coeffs(long int nNu, unsigned long nM);
long double Bessel(long int nNu, long double nX, long double nEps);
long double Bessel_Primer(long int nNu, long double nX);
long double Cauchy_Euler(long double nAlpha, long double nBeta, long double nC1,
	long double nC2, long double nEps, long double nX);
long double Logistic(long double ldArg);
long double DxLogistic(long double ldArg);
//************************************************************************
// Miscellaneous functions:
//************************************************************************
long double Absolute(long double nA);
long double Find_Max(long double nX,long double nY,long double nZ);
int Find_Max(long double nX,long double nY);
long double Machine_Eps(int nBase, int nDigits);
//int Find_Max(int nX, int nY);
int Check_Endpoints(long double (*Func)(long double nX),
	long double nA, long double nB, long double nZero);
int Calc_Iters(long double nTau, long double nLeft, long double nRight);
long double Secant(long double (*Function1)(long double nX),
			long double nApprox1, long double nApprox2, long double nEval1,
			long double nEval2, long double nEps);
long double Check_Not_Zero(long double nArg, long double nEps);
void Catcher(int* nSIG);
long double ExpInvSquared(long double );
									
#endif
