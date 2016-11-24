//*******************************************************************************
// File		: ISR.cpp
// Purpose	: Contains the Definitions for the Interrupt Serivce Routines
// NOTE!!!	: It is up to the user's function to generate an interrupt for
//				: the unused vector, or eles the chaining method fails!!!!!
//*******************************************************************************
#include"isr.hpp"
class IsR;
void interrupt (*IsR::Old_Interrupt)(...) = NULL;
void (*IsR::The_Handler)();
//*******************************************************************************
// Class		: IsR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose	: Chains into the PC's vector table so that we can get more
//				: control over the typical errors such as Overflow and Divide by Zero
//*******************************************************************************
IsR::IsR()
{
	uiCounter = 0; // initialize the counter
	iThere = 0;
} // end IsR definition

//*******************************************************************************
// Class		: IsR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose	: Chains into the PC's vector table so that we can get more
//				: control over the typical errors such as Overflow and Divide by Zero
//*******************************************************************************
IsR::IsR(void(*TheFunc)())
{
	uiCounter = 0; // initialize the counter
	iThere = 0;
	if(TheFunc == NULL)
		return;
	The_Handler = TheFunc;
} // end IsR definition

//*******************************************************************************
// Class		: IsR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose	: Destruction is the Art of the Game
//*******************************************************************************
IsR::~IsR()
{
	DeInstall_It();
} // end IsR destructor

//*******************************************************************************
// Class		: IsR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	: Install_It
// Purpose	: Installs the interrupt handler into the PC's vector table
// 			: It returns zero for a successful operation, if we could'nt
//				: set the unused vector, we return one.
//*******************************************************************************
int IsR::Install_It(int iNum)
{
int iRes = 0;

	iWhichVector = iNum;
	Old_Interrupt = getvect(iWhichVector); // get the old vector
	setvect(UNUSED_VECT, Old_Interrupt); // re-set it to unused
	setvect(iWhichVector, Error_Catcher); // set the new one
	iThere = 1;
	/*
	if(getvect(Unused2) == 0)
	{
		setvect(Unused2, Old_Interrupt); // re-set it to unused
		iThere = 2;
		iRes = 1; 
	} else
	if(getvect(Unused3) == 0)
	{
		setvect(Unused3, Old_Interrupt); // re-set it to unused
		iThere = 3;
		iRes = 1; 
	} */

	return iRes;
} // end Install_It

//*******************************************************************************
// Class		: IsR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	: DeInstall_It
// Purpose	: Restores the PC's state to before the call.
//*******************************************************************************
void IsR::DeInstall_It()
{
	setvect(iWhichVector, Old_Interrupt); // re-set it to unused
} // end DeInstall_It

//*******************************************************************************
// Class		: IsR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	: Error_Catcher
// Purpose	: Takes care of whatever happend
//*******************************************************************************
void interrupt IsR::Error_Catcher(...)
{
	The_Handler();
} // end Error_Catcher

//*******************************************************************************
// Class		: IsR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Member	: GetCount
// Purpose	: Takes care of whatever happend
//*******************************************************************************
unsigned long IsR::GetCount()
{
	uiCounter++;
	return uiCounter;
} // end GetCount
/*

IsR* IsrCat;

void PrintIt()
{
	cout<<IsrCat->GetCount()<<endl;
	geninterrupt(UNUSED_VECT);
}    

void main()
{
char Key = 's';
int iN = 0;

	IsrCat = new IsR(PrintIt); // this is global
	cout<<"Enter vector number: \n";
	cin>>iN;
	//iN = UserTimer_Vector;
	if(IsrCat->Install_It(iN))
		exit(0);
	while(Key != 0x1b)
		Key = getch();

	delete IsrCat;
} // end main */
