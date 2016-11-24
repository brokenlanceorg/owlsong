//********************************************************
// Time routines
//********************************************************
#include<time.h>

//********************************************************
// Function	:	Get_time_Ascii
//	Purpose	:	Returns the current time in human readable
//				:	Ascii format
//********************************************************
char* Get_time_Ascii()
{
time_t* T_time;
char* _time_str = '\0';

	time(T_time);
	_time_str = ctime(T_time);
	return _time_str;		

} // end Get_time_Ascii

//********************************************************
// Function	:	Get_time_Secs
//	Purpose	:	Returns the current time in seconds passed
//				:	since 00:00:00 hours 1970
//********************************************************
long Get_Time_Secs()
{
time_t* T_time;
long _testint;

	_testint = time(T_time);
	return _testint;		
} // end Get_time_Secs

//**********************************************************
// Function	:	Convert_time_to_Ascii
//	Purpose	:	Returns the time passed in as a human readable
//				:	Ascii formated string
// Params	:	Accepts a long integer that represents the
//				:	number of seconds passed since 00:00:00 1970
//**********************************************************
char* Convert_time_to_Ascii(long int nTtime)
{
time_t* T_time;
char* _time_str = '\0';

	*T_time = (time_t)(nTtime);
	_time_str = ctime(T_time);
	return _time_str;		

} // end Get_time_Ascii

