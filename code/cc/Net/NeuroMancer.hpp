//***********************************************************************************************
// File		: NeuroMancer.hpp
// Purpose	: This class makes or builds neural networks based on information
//          : passed to it. In some cases, this information is in the form of
//          : StockpoinTs or StockserieS, in other cases, the network can be
//          : constructed piece-by-piece.
//				: 
// Author	: Brandon Benham 
// Date		: 7/01/00
//***********************************************************************************************
#ifndef __NEUROMANCER_HPP
#define __NEUROMANCER_HPP

#include "Filesrc.hpp"
#include "SequenceNetwork.hpp"
#include "Backprop.hpp"
#include "StockSeries.hpp"
#include "StockPoint.hpp"

//***********************************************************************************************
// Class	   : NeuromanceR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose	: This class makes or builds neural networks based on information
//          : passed to it. In some cases, this information is in the form of
//          : StockpoinTs or StockserieS, in other cases, the network can be
//          : constructed piece-by-piece.
//***********************************************************************************************
class NeuromanceR
{
    public:
        NeuromanceR(); // Default Constructor declaration
        ~NeuromanceR(); // Destructor declaration
    
        // Public methods:
        SequencenetworK* MakeNeuralNetwork( StockserieS* );
        SequencenetworK* MakeNeuralNetwork( VectoR*      );
        BackproP*        MakeNeuralNetwork( StockpoinT*  );
        void  InvertVector( VectoR* );
        void  MapVector( VectoR* );
        long double InvertPoint( long double );
        void  SetCeilingAndFloor( long double ldC = 1, long double ldF = 0 ) 
            { ldCeiling = ldC; ldFloor = ldF; }
        StockserieS* GetMappedSeries( StockserieS* );
    
    protected:
        void  Setup();
        bool  DetermineIfNetExists( char* );
        char* MakeFileName( int, char* );
        void  CreateDefinitionFile( int, int, int, long double, long double );
        void  OutputStatus();
    
    private:
        long double ldMinimum;
        long double ldMaximum;
        long double ldCeiling;
        long double ldFloor;
        char*       pcThe_Output_File;
        long double ldStep_Size; // this is for the sequence networks
        void        MakeASCII( int, char* );
        void        MakeASCII( long double, char* );

}; // end NeuromanceR declaration

#endif

