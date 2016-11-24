//***************************************************************************** 
// File		:	DataSet.Hpp
// Purpose	:	Contains the declarations for the Data_seT class
// Author	:  Brandon Benham
//*****************************************************************************
#ifndef __DATASET_HPP
#define __DATASET_HPP

#include"filesrc.hpp"
#include"mathmatc.hpp"

//************************************************************************
// Class		:	Data_seT
// Purpose	:	This class defines a data_set object used for scientific
//				:  investigations, utilizes the input class
//************************************************************************
class Data_seT // : protected SpacE, I was going to derive this but
{					// I decided not to
	protected:
		int nNum_Data_Points; // number of data points sampled
		VectoR* xVariables;   // independent variables
		VectoR* yVariables;   // dependent variables
		FilesourcE* FInData;	 // the incomming data object

	public:
		Data_seT(); // default constructor
		// construct an object size of nNum_pts, with input file, and type
		Data_seT(char* cF_name, _SIZE dTyp); 
		Data_seT(int nNum_Pts); // constructor for input from keyboard
		~Data_seT();
		void Get_Data_Input(); // get the data points from input object
		inline int GetNumDataPts() {return nNum_Data_Points;}
		VectoR* Get_XDat();
		VectoR* Get_YDat();
		friend void main();

}; // end class declaration Data_seT

#endif