//***************************************************************************** 
// File		:	net1.cpp
// Purpose	:	Contains test calls to the neural net class
// Author	:  Brandon Benham
//*****************************************************************************
#include<iostream.h>
#include"backprop.hpp"
#include"neural.hpp"

FilesourcE* NumerR::FlogFile = new FilesourcE("c:\\new.log", _BYTE, 0);
void main()
{
BackproP* TestNet;
char cFileName[13];
int iAlive = 0;

	cout<<"Enter Definition File Name: \n";
	cin>>cFileName;
	cout.precision(18);
	cout<<"Is the network alive?\n";
	cin>>iAlive;
	TestNet = new BackproP(cFileName, iAlive);
	cout<<"Is the network mature?\n";
	cin>>iAlive;
	TestNet->SetMature(iAlive);
	if(TestNet->Run())
		cout<<"No Good.";
	delete TestNet;
} // end main
