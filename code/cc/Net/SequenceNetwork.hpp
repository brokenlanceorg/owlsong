//***********************************************************************************************
// File		: SequenceNetwork.hpp
// Purpose	: This class is an adapter class for a FuzzyNeural network.
//				: 
// Author	: Brandon Benham 
// Date		: 6/04/00
//***********************************************************************************************
#ifndef __SEQUENCENET_HPP
#define __SEQUENCENET_HPP

#include "Neural.hpp"
#include "FuzzyNeural.hpp"
#include "Filesrc.hpp"
#include "mathmatc.hpp"
//***********************************************************************************************
// Class	   : SequencenetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose	: This class is an adapter class for a FuzzyNeural network.
//          : It will perform the following algorithm:
//          :    
//          :    FuzzyNet->Run()
//          :    if(!FuzzyNet->IsMature())
//          : 			WriteNewDataFiles()
//          : 			delete FuzzyNet   
//          : 			FuzzyNet = new FuzzyNet(deffilename)
//          : 			FuzzyNet->Run()
//          :        if(!FuzzyNet->IsMature())
//          : 				return error
//          :                          
//***********************************************************************************************
class SequencenetworK : public NeuralneT
{
	public:
   	SequencenetworK();   // Default Constructor declaration
   	SequencenetworK( char*, unsigned short = 0 ); // Real Constructor declaration
   	~SequencenetworK();  // Destructor declaration
      int Run();				// Override Neural's pure virtual method
		virtual int Recall(VectoR*); // Override Neural's pure virtual method
		VectoR& GetOutput() { return Fuzzy_Neural_Network->GetOutput(); }
		inline unsigned long GetEpochs() { return Fuzzy_Neural_Network->GetEpochs(); }
      int inline GetNumberOfInputs() { return Fuzzy_Neural_Network->GetNumberOfInputs(); }
                 
   protected:
   	void Setup();
      void WriteNewDataFiles();
      void WriteDefinitionFile();
      void WriteFactFile();
		virtual int SaveWeights()          { return 0; }
		virtual int LoadWeights()          { return 0; }
		virtual int EncodeDataPair()       { return 0; }
		virtual long double CycleThruNet() { return 0; }
		virtual void TrainNet()            {}
		virtual float TestNet()            { return 0; }
                                  
   private:
   	FuzzyneuraL* Fuzzy_Neural_Network;
      FilesourcE*  Fthe_Data_File;
      char*        pcDefinition_File_Name;
      char*        pcFact_File_Name;
   
}; // end SequencenetworK declaration

#endif

