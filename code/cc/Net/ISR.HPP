//*******************************************************************************
// File		: ISR.hpp
// Purpose	: Contains the Declarations for the Interrupt Serivce Routines
//*******************************************************************************
#ifndef __ISR_HPP
#define __ISR_HPP
#include<iostream.h>
#include<dos.h>
#include<bios.h>
#include<stdio.h>
#include<conio.h>
#include<signal.h>
/******************* TIMER INTERRUPT STUFF ****************/
#define TIMER_VECT 8		// timer chip int. vector
//#define UNUSED_VECT 250         // user vector: used to link to std ISR
//#define UNUSED_VECT 104         // user vector: used to link to std ISR
//#define UNUSED_VECT 109         // user vector: used to link to std ISR
#define UNUSED_VECT 120         // user vector: used to link to std ISR
const Unused1 = 0x68;
const Unused2 = 0x6d;
const Unused3 = 0x78;
const UserTimer_Vector = 0x1c;
const Divide_Vector = 0x00; // Divide by Zero entry
const Non_Mask_Vector = 0x02; // Non-Maskable Interrupt
const Overflow_Vector = 0x04; // Arithmatic overflow
const Print_Screen = 0x05;
const Reserved1_Vector = 0x06;
const Reserved2_Vector = 0x07;
const TIMER = 0x08;
const KeyboardService_Vector = 0x09;
const Keyboard_Vector = 0x16;
const CntlTime_Vector = 0x16;
// Vectors: 68h - 6bh; 6dh - 6fh; 78h - 7fh are unused by dos.
#define PIC_CMD 0x20            // constants for PIC EOI command
#define NONSPEC_EOI 0x20
#define TIMER_MODE 0x34         // timer setup command
#define TIMER_CONTROL 0x43	// timer control register port
#define TIMER_0 0x40		// timer 0 port

#define LATCH_COUNT 0x00	// command to latch count
/**********************************************************/

//*******************************************************************************
// Class		: IsR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose	: Chains into the PC's vector table so that we can get more
//				: control over the typical errors such as Overflow and Divide by Zero
//*******************************************************************************
class IsR
{
	protected:
		static void interrupt (*Old_Interrupt)(...); // old interrupt
		static void interrupt (Error_Catcher)(...); // new interrupt
		static void (*The_Handler)(); // our pointer to the user supplied one
		unsigned long uiCounter;
		int iWhichVector;
		int iThere;

	public:
		IsR(); // default constructor
		IsR(void (*TheFunc)()); // the real constructor
		~IsR(); // destructor
		int Install_It(int); // chains into the vector table
		void DeInstall_It(); // resets the PC
		unsigned long GetCount();
}; // end class IsR

#endif
