//********************************************************
// File		:  T_time.hpp
// Time routines
//********************************************************
#ifndef __T_TIME_HPP
#define __T_TIME_HPP

#include<time.h>

//********************************************************
// Function	:	Get_time_Ascii
//	Purpose	:	Returns the current time in human readable
//				:	Ascii format
//********************************************************
char* Get_time_Ascii();

//********************************************************
// Function	:	Get_time_Secs
//	Purpose	:	Returns the current time in seconds passed
//				:	since 00:00:00 hours 1970
//********************************************************
long Get_Time_Secs();

//**********************************************************
// Function	:	Convert_time_to_Ascii
//	Purpose	:	Returns the time passed in as a human readable
//				:	Ascii formated string
// Params	:	Accepts a long integer that represents the
//				:	number of seconds passed since 00:00:00 1970
//**********************************************************
char* Convert_time_to_Ascii(long int nTtime);
#endif
