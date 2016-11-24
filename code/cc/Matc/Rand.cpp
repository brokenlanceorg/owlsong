//**************************************************************
// File		: Rand.cpp
// Purpose  : Implementations of the radom functions
//**************************************************************

#include<stdlib.h>

//**************************************************************
//Define Get_Rand_Integer	:	Returns an integer between zero
//									:	and the passed in parameter
//**************************************************************
float Get_Rand_Integer(int nMax)
{
float temp = 0;

	temp = (float)(rand() % nMax);
	return temp;

} //end Get_Rand_Integer

//**************************************************************
//Define Get_Rand_Decimal	:	Returns a decimal usually close
//		:	to zero and below .75 most of the time
//**************************************************************
float Get_Rand_Decimal()
{
float temp = 0;

	temp = (int)(rand() % 1000);
	temp = temp / 0x1000;
	return temp;

} //end Get_Rand

