//***************************************************************************** 
// File		:	net1.cpp
// Purpose	:	Contains test calls to the neural net class
// Author	:  Brandon Benham
//*****************************************************************************
#include<iostream.h>
#include"backprop.hpp"
#include"neural.hpp"
#include"function.hpp"            

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
	TestNet->SetActivationFunc(Logistic);
	TestNet->SetActivationDerivative(DxLogistic);
	if(TestNet->Run())
		cout<<"Damn. Oh Well.";
	delete TestNet;
} // end main
