//***********************************************************************************************
// File     : StockSeries.cpp
// Purpose  : This class derives from the DataserieS class to provide additional
//          : functionality such as a moving average line. And minimums and maxs
//          : for both the stock line and the averages.
// Author   : Brandon Benham 
// Date     : 6/18/00
//***********************************************************************************************

#include"StockSeries.hpp"          

//***********************************************************************************************
// Class    : StockserieS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
StockserieS::StockserieS( long double ldSMA ) : DataserieS()
{
   Setup();
   pcSuffix = 0;
   ldSmoothing_Factor = ldSMA;
   theMoving_Average_Calculator = new SmoothedMovingAveragE( ldSmoothing_Factor );
} // end StockserieS default constructor

//***********************************************************************************************
// Class    : StockserieS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Constructor
// Purpose  : Performs the construction actions.
//***********************************************************************************************
StockserieS::StockserieS( int iOID, long double ldSMA ) : DataserieS( MakeFileName( iOID ) )
{
   Setup();
   iObjectID = iOID;
   ldSmoothing_Factor = ldSMA;
   theMoving_Average_Calculator = new SmoothedMovingAveragE( ldSmoothing_Factor );
   
   Recalculate();
} // end StockserieS constructor

//***********************************************************************************************
// Class    : StockserieS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Constructor
// Purpose  : Performs the construction actions.
//***********************************************************************************************
StockserieS::StockserieS( int iOID, char* pcSuf, long double ldSMA ) : DataserieS( MakeFileName( iOID, pcSuf ) )
{
   Setup();
   iObjectID = iOID;
   ldSmoothing_Factor = ldSMA;
   theMoving_Average_Calculator = new SmoothedMovingAveragE( ldSmoothing_Factor );
   
   Recalculate();
} // end StockserieS constructor

//***********************************************************************************************
// Class    : StockserieS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Constructor
// Purpose  : Performs the construction actions.
//***********************************************************************************************
StockserieS::StockserieS( char* theFile, long double ldFirst, long double ldSMA ) 
   : DataserieS( theFile )
{
   Setup();
   pcSuffix = 0;
   ldFirst_Average = ldFirst;
   ldSmoothing_Factor = ldSMA;
   theMoving_Average_Calculator = new SmoothedMovingAveragE( ldSmoothing_Factor, ldFirst_Average );
   
   Recalculate();
} // end StockserieS constructor

//***********************************************************************************************
// Class    : StockserieS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
StockserieS::~StockserieS()
{
   if( vThe_Averages != 0 )
      delete vThe_Averages;
   if( theMoving_Average_Calculator != 0 )
      delete theMoving_Average_Calculator;
   if( Fthe_Function != 0 )
      delete Fthe_Function;
   if( pcSuffix != 0 )
      delete[] pcSuffix;
} // end StockserieS destructor

//***********************************************************************************************
// Class    : StockserieS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void StockserieS::Setup()
{
   vThe_Averages                = new VectoR( iSize );
   Fthe_Function                = new FunctioN( _OVERFLOW );
   theMoving_Average_Calculator = 0;
   ldFirst_Average              = 0;
   ldSmoothing_Factor           = 0;
   ldData_Minimum               = 2e34;
   ldData_Maximum               = -2e34;
   ldSMA_Minimum                = 0;
   ldSMA_Maximum                = 0;
   iObjectID                    = 0;
} // end Setup                      

//***********************************************************************************************
// Class    : StockserieS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : AddElement
// Purpose  : Adds the element by calling DataserieS::AddElement and then
//          : calculates the moving average.
//***********************************************************************************************
void StockserieS::AddElement( long double ldElement )
{
   DataserieS::AddElement( ldElement );
   if( vThe_Averages->cnRows == iSize )
   { // No resize necessary
      vThe_Averages->pVariables[iNumber_Of_Elements - 1] = 
         theMoving_Average_Calculator->GetAverage( vThe_Data_Elements->pVariables[iNumber_Of_Elements - 1] );
   } else
   { // We must resize
      if( vThe_Averages != 0 )
         delete vThe_Averages;
      vThe_Averages = new VectoR( iSize );
      Recalculate();
   } // end else

   // Now, find the min and the max, without iterating through the whole set:
   if( Fthe_Function->Find_Max( vThe_Averages->pVariables[iNumber_Of_Elements - 1], ldSMA_Maximum ) )
      ldSMA_Maximum = vThe_Averages->pVariables[iNumber_Of_Elements - 1];
   if( Fthe_Function->Find_Max( ldSMA_Minimum, vThe_Averages->pVariables[iNumber_Of_Elements - 1] ) )
      ldSMA_Minimum = vThe_Averages->pVariables[iNumber_Of_Elements - 1];
   // And do the same for the Data elements:
   if( Fthe_Function->Find_Max( vThe_Data_Elements->pVariables[iNumber_Of_Elements - 1], ldData_Maximum ) )
      ldData_Maximum = vThe_Data_Elements->pVariables[iNumber_Of_Elements - 1];
   if( Fthe_Function->Find_Max( ldData_Minimum, vThe_Data_Elements->pVariables[iNumber_Of_Elements - 1] ) )
      ldData_Minimum = vThe_Data_Elements->pVariables[iNumber_Of_Elements - 1];
} // end AddElement           

//***********************************************************************************************
// Class    : StockserieS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Recalculate
// Purpose  : This 'recalculates' the smoothed moving average. This method is used
//          : during construction and when we have to resize the vectors.
//***********************************************************************************************
void StockserieS::Recalculate()
{
int iOld = iCurrent_Number;
int iLoop = 0;

   if( iNumber_Of_Elements <= 1 )
      return;
      
   Rewind();
   
   if( ldFirst_Average == 0 )
   {
      long double ldTmp = GetNextElement();
      vThe_Averages->pVariables[iLoop++] = 
         theMoving_Average_Calculator->GetAverage( ldTmp, ldTmp );
   } else
   {
      vThe_Averages->pVariables[iLoop++] = 
          theMoving_Average_Calculator->GetAverage( ldFirst_Average, GetNextElement() );
   } // end else
   
   while( HasMoreElements() )
   {
      vThe_Averages->pVariables[iLoop++] = 
         theMoving_Average_Calculator->GetAverage( GetNextElement() );
   } // end while

   CalculateMax();
   CalculateMin();

   iCurrent_Number = iOld;
} // end Recalculate

//***********************************************************************************************
// Class    : StockserieS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : CalculateMax
// Purpose  : Called from both Recalculate and add
//          : Simply finds the Maximum element.
// Notes    : This 'CalculateMax' algorithm is specifically formulated for a StockSeries
//***********************************************************************************************
void StockserieS::CalculateMax()
{
   ldData_Maximum = -100000;
   ldSMA_Maximum = -100000;
   for( int i = 0; i < iNumber_Of_Elements; i++ )
   {
      // First, we do the data elements:
      if( Fthe_Function->Find_Max( vThe_Data_Elements->pVariables[i], ldData_Maximum ) )
         ldData_Maximum = vThe_Data_Elements->pVariables[i];
      // Then, we do the SMA average elements:
      if( Fthe_Function->Find_Max( vThe_Averages->pVariables[i], ldSMA_Maximum ) )
         ldSMA_Maximum = vThe_Averages->pVariables[i];
   } // end for
   
} // end CalculateMax

//***********************************************************************************************
// Class    : StockserieS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : CalculateMin
// Purpose  : Called from both Recalculate and add
//          : Simply finds the Maximum element.
// Notes    : This 'CalculateMin' algorithm is specifically formulated for a StockSeries
//***********************************************************************************************
void StockserieS::CalculateMin()
{
   ldData_Minimum = 100000;
   ldSMA_Minimum = 100000;
   for( int i = 0; i < iNumber_Of_Elements; i++ )
   {
      // First, we do the data elements:
      if( Fthe_Function->Find_Max( ldData_Minimum, vThe_Data_Elements->pVariables[i] ) )
         ldData_Minimum = vThe_Data_Elements->pVariables[i];
      // Then, we do the SMA average elements:
      if( Fthe_Function->Find_Max( ldSMA_Minimum, vThe_Averages->pVariables[i] ) )
         ldSMA_Minimum = vThe_Averages->pVariables[i];
   } // end for
   
} // end CalculateMin

//***********************************************************************************************
// Class    : StockserieS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Compare
// Purpose  : Computes a distance metric between two vectors.
//          : 
//***********************************************************************************************
long double StockserieS::Compare( VectoR* vec )
{
   return *vThe_Data_Elements == *vec;
} // end Compare

//***********************************************************************************************
// Class    : StockserieS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Peel
// Purpose  : "Peels" off the last iWhich elements to a new StockserieS object
//          : 
//***********************************************************************************************
StockserieS* StockserieS::Peel( int iWhich )
{
   StockserieS* theRetVal = 0;
   theRetVal = new StockserieS();
   theRetVal->SetID( GetID() + 1 );
   theRetVal->SetSuffix( GetSuffix() );
   theRetVal->GenerateFileName();
   
   int iStart = (iNumber_Of_Elements - iWhich);

   for( int i = iStart; i < iNumber_Of_Elements; i++ )
   {
      theRetVal->AddElement( vThe_Data_Elements->pVariables[i] );
   } // end for   

   return theRetVal;
} // end Peel

//***********************************************************************************************
// Class    : StockserieS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : GenerateFileName
// Purpose  : 
//          : 
//***********************************************************************************************
void StockserieS::GenerateFileName()
{
   char* pcFile = new char[ 35 ];
   pcFile[0] = '\0';
   itoa( iObjectID, pcFile, 10 );
   strcat( pcFile, pcSuffix );
   SetName( pcFile );
} // end GenerateFileName

//***********************************************************************************************
// Class    : StockserieS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : MakeFileName
// Purpose  : 
//          : 
//***********************************************************************************************
char* StockserieS::MakeFileName( int iOID )
{
   char* pcFile = new char[35];
   pcFile[0]    = '\0';
   pcSuffix     = new char[ 7 ];
   pcSuffix[0]  = '\0';
   strcpy( pcSuffix, ".dat" );  // default suffix for predictions

   itoa( iOID, pcFile, 10 );
   strcat( pcFile, pcSuffix );

   return pcFile;
} // end MakeFileName

//***********************************************************************************************
// Class    : StockserieS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : MakeFileName
// Purpose  : 
//          : 
//***********************************************************************************************
char* StockserieS::MakeFileName( int iOID, char* pcSuf )
{
   char* pcFile = new char[35];
   pcFile[0]    = '\0';
   pcSuffix     = new char[ strlen( pcSuf ) + 1 ];
   pcSuffix[0]  = '\0';
   strcpy( pcSuffix, pcSuf );  // suffix for actuals

   itoa( iOID, pcFile, 10 );
   strcat( pcFile, pcSuffix );

   return pcFile;
} // end MakeFileName

//***********************************************************************************************
// Class    : StockserieS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : SetSuffix
// Purpose  : 
//          : 
//***********************************************************************************************
void StockserieS::SetSuffix( char* pcS )
{
   if( pcSuffix != 0 )
      delete[] pcSuffix;
   pcSuffix    = new char[ strlen( pcS ) + 1 ];
   pcSuffix[0] = '\0';
   strcpy( pcSuffix, pcS );
} // end SetSuffix

//***********************************************************************************************
// Class    : StockserieS
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//          : 
//***********************************************************************************************
int StockserieS::GetNumberOfElements()
{ 
   return DataserieS::GetNumberOfElements(); 
} // end GetNumberOfElements

