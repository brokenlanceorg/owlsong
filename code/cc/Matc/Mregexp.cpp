/*************************************************************************
* Module: mregexp.c                                                      *
* Written by: Chris Coppeans                                             *
* Date: February 16, 1996; June 8 1996                                   *
* This program implements a set of functions to interpret strings        *
* according to a format incredibly similar to regular expressions.       *
**************************************************************************/
#include <iostream.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <conio.h>
#include "mregexp.h"

/* some globals */
char *prev_regexp_char;
char *next_regexp_char;
char *cTop_test_str;
/* These functions return 1 if the character passes and 0 if not.  If they
get a 0 back from a function sub-call, often they call another function
before giving up. */

/* This function acts as a front end.  It sets the previous pointer to
NULL and then returns a call to middle_mregexp. */
int mregexp(const char *regexp, const char *teststr) {
  prev_regexp_char = NULL;
  next_regexp_char = NULL;
  (const char *)cTop_test_str = teststr;
  cout<<"cTop_test_str is: "<<cTop_test_str<<endl;
  cout<<"test_str is: "<<teststr<<endl;
  getch();  
  return middle_mregexp((char *)regexp, (char *)teststr);
  }

/* This function acts as a switch box to the other, worker functions.
More on how it works later. The space/NULL regular expression character
must always be the last character in the expression*/
int middle_mregexp(char *regexp, char *teststr) {
  int ret_value;
  switch(*regexp) { 
	 case '\0':
		if (*teststr != '\0')
		  return 0;
		else
			return 1;
	 case '#':
		ret_value = do_number_sign(regexp, teststr);
		break;
	 case '&':
		  ret_value = do_amper(regexp, teststr);
		break;
	 case '.':
		ret_value = do_dot(regexp, teststr);
		break;
	 case '[':
		ret_value = do_bracket(regexp, teststr);
		break;
	 case '\\':
		ret_value = do_backslash(regexp, teststr);
		break;
	 case '*':
		if (prev_regexp_char != NULL)
		  return do_star(regexp, teststr);
		return -1;
		break;
	 case '+':
		if (prev_regexp_char != NULL)
		  return do_plus(regexp, teststr);
		return -1;
		break;
	 case '?':
		if (prev_regexp_char != NULL)
		  return do_quest_mark(regexp, teststr);
		return -1;
		break;
	 case '_': // always pass this character (to accept spaces)
    	cout<<"regexp: "<<regexp<<endl;
		cout<<"teststr: "<<teststr<<endl;
		cout<<"ret_value: "<<ret_value<<endl;
		getch();
		if(ret_value != 0)
			return 1;
      else
	      ret_value = do_space(regexp, teststr);
		break;
	 case '|':
		if(ret_value == 1) // if we already have success, there is no need to check further
		  return ret_value;
		else
		if(*teststr == '\0') // if the teststr is at the end, it survived previous tests,
		{ // so we can pass it on as successful
		  return 1;
		} //end if
		else // we need to check the string according to the regexp following the pipe
		{   
		  prev_regexp_char = NULL;
		  next_regexp_char = NULL;
		  regexp = regexp + 1;
		  ret_value =  middle_mregexp(regexp, cTop_test_str);
		} // end else
		break;
	 default: 
		prev_regexp_char = regexp;
		if ((*teststr == '\0' && *regexp != '\0') || *regexp != *teststr) {
		  next_regexp_char = regexp + 1;
		  ret_value = 0;
		  }     
		else
		  return middle_mregexp(regexp + 1, teststr + 1);
		break;
	 } //end switch

  if (ret_value == 0) 
  {
	 if (*(next_regexp_char) == '*' || *(next_regexp_char) == '?')
		return middle_mregexp(next_regexp_char + 1, teststr);
	 else
	 {
		regexp = next_regexp_char + 2;
		next_regexp_char = regexp + 1;
		return ret_value = middle_mregexp(regexp, cTop_test_str);
	 } // end else if
	 //else
		return 0;
  } //end if
  else
	 return 1;
} //end middle_mregexp


/* If the character is a digit, then pass on.  Otherwise, fail. */
int do_number_sign(char *regexp, char *teststr) {
  prev_regexp_char = regexp;
  if (*teststr < '0' || *teststr > '9') {
	 next_regexp_char = regexp + 1;
	 return 0;
	 }
  return middle_mregexp(regexp + 1, teststr + 1);
  }

/* If the character is a null, fail.  Otherwise, always pass. */
int do_dot(char *regexp, char *teststr) {
  prev_regexp_char = regexp;
  if (*teststr == '\0') {
	 next_regexp_char = regexp + 1;
	 return 0;
	 }
  return middle_mregexp(regexp + 1, teststr + 1);
  }

/* Take the next character as a literal. */
int do_backslash(char *regexp, char *teststr) {
  prev_regexp_char = regexp;
  if (*teststr != *(regexp + 1)) {
	 next_regexp_char = regexp + 2;
	 return 0;
	 }
  return middle_mregexp(regexp + 2, teststr + 1);
  }

/* Check to see if it matches any of the characters in brackets.
If it does, accept it.  Else it fails.  If the first character is
a '^', check to make sure it DOESN'T match any of the characters.
If it matches one, it fails.  If it doesn't, it passes. */
int do_bracket(char *regexp, char *teststr) {
  int i, not = 0, success = 0;
  prev_regexp_char = regexp;
  if (*(regexp + 1) == '^') {
	 not = 1;
	 i = 2;
	 }
  else
	 i = 1;
  while(1) {
	 if (*(regexp + i) == '\0')
		return -1;
	 if (*(regexp + i) == ']')
		break;
	 if (*(regexp + i) == '-') {
		if (i == 1 || *(regexp + i + 1) == ']');
		  // don't do anything if at beginning or end.
		else {
		  if ((*teststr >= *(regexp + i - 1)) && (*teststr <= *(regexp + i + 1)))
			 success = 1;
		  i++;
		  }
		}
	 if (*(regexp + i) == *teststr)
		success = 1;
	 i++;
	 }
  if ((not && success) || (!not && !success)) { //failed.
	 next_regexp_char = regexp + i + 1;
	 return 0;
	 }
  else
	 return middle_mregexp(regexp + i + 1, teststr + 1);
  }

/* This function is called when a character before it has
matched.  If it doesn't match, we never get here cause the
whole thing is skipped in middle_mregexp().  This function
allows for the necessary recursion.  It's basically just a
stub.  It increments regexp but not teststr and keeps the
'prev' variables the same. */
int do_quest_mark(char *regexp, char *teststr) {
  int ret_value;
  ret_value = middle_mregexp(regexp + 1, teststr);
  next_regexp_char = regexp;
  return ret_value;
  }

/* This function is called the same as do_quest_mark() above.
It recurses by calling middle_mregexp() with the prev_regexp_char
as its argument, therefore looping back.  If it doesn't match, we
go on happy.  If it does but later there's a failure, middle_mregexp()
should be able to deal with it. */
int do_star(char *regexp, char *teststr) {
  int ret_value;
  ret_value = middle_mregexp(prev_regexp_char, teststr);
  if (!ret_value)
	 next_regexp_char = regexp;
  return ret_value;
  }

/* This function works basically the same as do_star above.  It
does the looping but middle_mregexp() doesn't allow it to mean 0
as it does in the two functions above. */
int do_plus(char *regexp, char *teststr) {
  int ret_value;
  ret_value = middle_mregexp(prev_regexp_char, teststr);
  if (!ret_value) {
	 next_regexp_char = regexp;
	 return middle_mregexp(regexp + 1, teststr);
	 }
  else
	 return 1;
  }


/* This function checks to see if the corresponding character
in the teststr is an alphabetic character. If it is, it passes
otherwise it fails it. */
int do_amper(char *regexp, char *teststr) 
{
  prev_regexp_char = regexp;	
  if(isalpha(*teststr))
  {
	return middle_mregexp(regexp + 1, teststr + 1);
  } //end if
  else
  {
	next_regexp_char = regexp + 1;	
	return 0; //not an alphabetic character
  } //end else

} //end do_amper

/* This function checks to see if the corresponding character
in the teststr is a space. If it is, it passes otherwise it fails it. */
int do_space(char *regexp, char *teststr) 
{
  prev_regexp_char = regexp;
  if(*teststr == '\0')
  	return 1;
  if(isspace(*teststr))
  {
	return middle_mregexp(regexp, teststr + 1);
  } //end if
  else
  {
	next_regexp_char = regexp + 1;	
	return 0; //not a space
  } //end else

} //end do_space
