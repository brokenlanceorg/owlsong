//*****************************************************************************
// File     :  Neural.cpp
// Purpose  :  Contains the implementations for the Neural class
// Author   :  Brandon Benham
//*****************************************************************************

#include"Neural.hpp"

//************************************************************************
// Class    :  NeuralneT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  Default Constructor
// Purpose  :  This class defines a Neural Net object which is an abstract
//          :  classs to be inhereted by the other type of neural nets.
//************************************************************************
NeuralneT::NeuralneT()
{
   pcFile_Name     = 0;
   iNum_Inputs     = 0;
   iNum_Hidden     = 0;
   iNum_Outputs    = 0;
   ulIteration     = 0;
   ulEpoch         = 0;
   usIsMature      = 0;
   _learningRate   = 0;
   fDecay_Rate     = 0;
   fTolerance      = 0;
   _momentum       = 0;
   _quit           = false;
   _maxEpoch       = 0;
} // end constructor

//************************************************************************
// Class    :  NeuralneT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  Default Constructor
// Purpose  :  This class defines a Neural Net object which is an abstract
//          :  classs to be inhereted by the other type of neural nets.
//************************************************************************
NeuralneT::NeuralneT(char* pcFname)
{
   _maxEpoch      = 0;
   _quit          = false;
   iNum_Inputs    = 0;
   iNum_Hidden    = 0;
   iNum_Outputs   = 0;
   ulIteration    = 0;
   ulEpoch        = 0;
   usIsMature     = 0;
   _learningRate  = 0;
   fDecay_Rate    = 0;
   fTolerance     = 0;
   _momentum      = 0;
   pcFile_Name    = new char[strlen(pcFname) + 1];
   pcFile_Name[0] = '\0';
   strcpy(pcFile_Name, pcFname);
   ReadDefinitionFile();
} // end constructor

//************************************************************************
// Class    :  NeuralneT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  Destructor
// Purpose  :  This class defines a Neural Net object which is an abstract
//          :  classs to be inhereted by the other type of neural nets.
//************************************************************************
NeuralneT::~NeuralneT()
{
   if( pcFile_Name != 0 )
   {
      delete[] pcFile_Name;
   }
} // end destructor

//************************************************************************
// Class    :  NeuralneT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Function :  ReadDefinitionFile
// Purpose  :  This function reads in the specified definition file which
//          :  defines the number of input,output nodes, tolerance, etc.
//************************************************************************
int NeuralneT::ReadDefinitionFile()
{
//   FPRINT( "ReadDefFile" );
   int length = 0;
   FilereadeR theFile( pcFile_Name );

   for( char* theWord = "1"; theWord != 0; theWord = theFile.GetNextWord() )
   {
      length = strlen( theWord );

      if(    ( strncmp( theWord, "inputs", length ) == 0 )
          || ( strncmp( theWord, "INPUTS", length ) == 0 ) )
      {
         iNum_Inputs = atoi( theFile.GetNextWord() );
      }
      else if(    ( strncmp( theWord, "outputs", length ) == 0 )
	       || ( strncmp( theWord, "OUTPUTS", length ) == 0 ) )
      {
         iNum_Outputs = atoi( theFile.GetNextWord() );
      }
      else if(    ( strncmp( theWord, "hidden", length ) == 0 )
	       || ( strncmp( theWord, "HIDDEN", length ) == 0 ) )
      {
         iNum_Hidden = atoi( theFile.GetNextWord() );
      }
      else if(    ( strncmp( theWord, "rate", length ) == 0 )
	       || ( strncmp( theWord, "RATE", length ) == 0 ) )
      {
         _learningRate = atof( theFile.GetNextWord() );
      }
      else if(    ( strncmp( theWord, "momentum", length ) == 0 )
	       || ( strncmp( theWord, "MOMENTUM", length ) == 0 ) )
      {
         _momentum = atof( theFile.GetNextWord() );
      }
      else if(    ( strncmp( theWord, "tolerance", length ) == 0 )
	       || ( strncmp( theWord, "TOLERANCE", length ) == 0 ) )
      {
         fTolerance = atof( theFile.GetNextWord() );
      }
      else if(    ( strncmp( theWord, "decay", length ) == 0 )
	       || ( strncmp( theWord, "DECAY", length ) == 0 ) )
      {
         fDecay_Rate = atof( theFile.GetNextWord() );
      }
   }

   return 1;
} // end ReadDefinitionFile
