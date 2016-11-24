//***********************************************************************************************
// File     : StockPointManager.cpp
// Purpose  : 
//          : 
// Author   : Brandon Benham 
// Date     : 6/04/00
//***********************************************************************************************

#include"StockPointManager.hpp"          

//***********************************************************************************************
// Class    : StockpointmanageR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Default Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
StockpointmanageR::StockpointmanageR() : SerializablE()
{
   Setup();
   pcAlternate_File_Name = new char[ 25 ];
   pcAlternate_File_Name[0] = '\0';
   strcpy( pcAlternate_File_Name, "Actuals.dat" );
   
   pcThe_File_Name = new char[ 25 ];
   pcThe_File_Name[0] = '\0';
   strcpy( pcThe_File_Name, "Predictions.dat" );

   pcSuffix = new char[ 12 ];
   pcSuffix[0] = '\0';
   strcpy( pcSuffix, "_Actual.dat" );

   Load();
} // end StockpointmanageR default constructor

//***********************************************************************************************
// Class    : StockpointmanageR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Constructor
// Purpose  : Performs the basic construction actions.
//***********************************************************************************************
StockpointmanageR::StockpointmanageR( char* pcFile, char* pcAlt ) : SerializablE()
{
   Setup();
   pcAlternate_File_Name = new char[ strlen( pcAlt ) + 1 ];
   pcAlternate_File_Name[0] = '\0';
   strcpy( pcAlternate_File_Name, pcAlt );
   
   pcThe_File_Name = new char[ strlen( pcFile ) + 1 ];
   pcThe_File_Name[0] = '\0';
   strcpy( pcThe_File_Name, pcFile );

   pcSuffix = new char[17];
   pcSuffix[0] = '\0';
   strcpy( pcSuffix, "_StockActual.dat" );
   
   pcPrediction_Suffix = new char[21];
   pcPrediction_Suffix[0] = '\0';
   strcpy( pcPrediction_Suffix, "_StockPrediction.dat" );

   Load();                     
} // end StockpointmanageR default constructor

//***********************************************************************************************
// Class    : StockpointmanageR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Constructor
// Purpose  : Performs the construction actions.
//***********************************************************************************************
StockpointmanageR::StockpointmanageR( char* aString ) : SerializablE( aString )
{
   Setup();
   Load();
} // end StockpointmanageR constructor

//***********************************************************************************************
// Class    : StockpointmanageR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Destructor
// Purpose  : Performs the destruction actions.
//***********************************************************************************************
StockpointmanageR::~StockpointmanageR()
{
   Store();
   if( theStockPoints != 0 )
   {
      for( int i = 0; i < iNumber_Of_Elements; i++ )
         delete theStockPoints[i];
      delete[] theStockPoints;
   } // end if
      
   if( theActualSeries != 0 )
   {
      for( int i = 0; i < iNumber_Of_Actuals; i++ )
         delete theActualSeries[i];
      delete[] theActualSeries;
   } // end if
      
   if( pcAlternate_File_Name != 0 )
      delete[] pcAlternate_File_Name;

   if( pcThe_File_Name != 0 )
      delete[] pcThe_File_Name;

   if( pcSuffix != 0 )
      delete[] pcSuffix;

   if( pcPrediction_Suffix != 0 )
      delete[] pcPrediction_Suffix;

   if( funcHelper != 0 )
      delete funcHelper;
} // end StockpointmanageR destructor

//***********************************************************************************************
// Class    : StockpointmanageR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Setup
// Purpose  : Performs the basic setup actions.
//***********************************************************************************************
void StockpointmanageR::Setup()
{
   pcAlternate_File_Name = 0;
   pcSuffix              = 0;
   pcPrediction_Suffix   = 0;
   iNumber_Of_Elements   = 0;
   iPeel_Size            = 3;
   iStep_Size            = 3;
   iActual_Size          = iStep_Size;  // keeps track of the capacity of
   iPredict_Size         = iStep_Size; // the arrays of the objects.
   iNumber_Of_Actuals    = 0;
   ldSMA                 = 0.5;
   funcHelper            = new FunctioN( _OVERFLOW );
   theStockPoints        = new StockpoinT*[ iStep_Size ];
   theActualSeries       = new StockserieS*[ iStep_Size ];
} // end Setup

//***********************************************************************************************
// Class    : StockpointmanageR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : GetActual
// Purpose  : Returns the StockpoinT object by ObjectID
// Notes    : this needs to be tested.
//***********************************************************************************************
StockserieS* StockpointmanageR::GetActual( int iOID )
{
   if( iOID > (iNumber_Of_Actuals - 1) )
      return 0;
   else
      return theActualSeries[ iOID ];
} // end GetActual

//***********************************************************************************************
// Class    : StockpointmanageR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
//***********************************************************************************************
void StockpointmanageR::AddActual( long double ldAct )
{
   StockserieS* NewPoint = GetLastActual()->Peel( iPeel_Size );
   NewPoint->AddElement( ldAct );
   theActualSeries[ iNumber_Of_Actuals++ ] = NewPoint;
   if( iNumber_Of_Actuals >= iActual_Size )
      ResizeActuals();
} // end AddActual

//***********************************************************************************************
// Class    : StockpointmanageR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : 
// Purpose  : 
// Notes    : We should set the first average appropriately here...
//***********************************************************************************************
void StockpointmanageR::AddPredicted( long double ldPred )
{
   StockpoinT* NewPoint = GetLastPoint()->Peel( iPeel_Size );
   NewPoint->AddPredictionElement( ldPred );
   theStockPoints[ iNumber_Of_Elements++ ] = NewPoint;
   if( iNumber_Of_Elements > 1 )
   {
      theStockPoints[ iNumber_Of_Elements - 1 ]->SetFirstAverage( 
         theStockPoints[ iNumber_Of_Elements - 2 ]->GetFirstAverage() );
   } // end if
   if( iNumber_Of_Elements >= iPredict_Size )
      ResizePredictions();
} // end AddPredicted

//***********************************************************************************************
// Class    : StockpointmanageR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Load
// Purpose  : Will load data from data files
// Notes    : This method also sets the first average for each set appropriately
//          : according to - 
//***********************************************************************************************
int StockpointmanageR::Load()
{
   // Do the Actuals first:
   FilereadeR ActualFile( pcAlternate_File_Name );

   int iTemp    = 0;
   char* pcWord = 0;

   while( pcWord = ActualFile.GetNextWord() )
   {
      iTemp = atoi( pcWord );
      if( pcPrediction_Suffix == 0 )  // we have a NASDAQ series:
         theActualSeries[ iNumber_Of_Actuals++ ] = new StockserieS( iTemp, pcSuffix, 0.5 );
      else                            // we have a Stock series:
         theActualSeries[ iNumber_Of_Actuals++ ] = new StockserieS( iTemp, pcSuffix );
      if( iNumber_Of_Actuals > 1 )
      {
         theActualSeries[ iNumber_Of_Actuals - 1 ]->SetFirstAverage( 
            theActualSeries[ iNumber_Of_Actuals - 2 ]->GetFirstAverage() );
      } // end if
      if( iNumber_Of_Actuals >= iActual_Size )
         ResizeActuals();
   } // end while

   // Now, do the Prediction data:
   FilereadeR PredictionFile( pcThe_File_Name );

   iTemp  = 0;
   pcWord = 0;

   while( pcWord = PredictionFile.GetNextWord() )
   {
      iTemp = atoi( pcWord );
      if( pcPrediction_Suffix == 0 ) // we have a NASDAQ series:
         theStockPoints[ iNumber_Of_Elements++ ] = new StockpoinT( iTemp, this, 0.5 );
      else                           // we have a Stock series:
         theStockPoints[ iNumber_Of_Elements++ ] = new StockpoinT( iTemp, pcPrediction_Suffix, this );
      if( iNumber_Of_Elements > 1 )
      {
         theStockPoints[ iNumber_Of_Elements - 1 ]->SetFirstAverage( 
            theStockPoints[ iNumber_Of_Elements - 2 ]->GetFirstAverage() );
      } // end if
      if( iNumber_Of_Elements >= iPredict_Size )
         ResizePredictions();
   } // end while

   return 0;   
} // end Load

//***********************************************************************************************
// Class    : StockpointmanageR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : Store
// Purpose  : 
// Notes    : Returns zero on success, non-zero on failure
//***********************************************************************************************
int StockpointmanageR::Store()
{
   ofstream myFile( pcThe_File_Name );
   // Since it's unsigned, we can overflow without regard:
   unsigned long ulCounter = 1;

   for( int i = 0; i < iNumber_Of_Elements; i++ )
   {
        myFile << theStockPoints[ i ]->GetObjectID();
        if( (ulCounter++ % 25) == 0 )
           myFile << "\n";
        else
           myFile << " ";
   } // end for

   myFile << flush;
   myFile.close();

   ofstream myOtherFile( pcAlternate_File_Name );
   ulCounter = 1;
   for( int i = 0; i < iNumber_Of_Actuals; i++ )
   {
        myOtherFile << theActualSeries[ i ]->GetID();
        if( (ulCounter++ % 25) == 0 )
           myOtherFile << "\n";
        else
           myOtherFile << " ";
   } // end for

   myOtherFile << flush;
   myOtherFile.close();
                
   return 0;
} // end Store

//***********************************************************************************************
// Class    : StockpointmanageR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : IsActualLast
// Purpose  : Returns true if the Actuals contains the last element in the series
// Notes    : If this returns true, then in order to get the last Actual,
//          : one should call GetLastActual, not GetLastPoint()->GetActual()
//          : as is done below.
//***********************************************************************************************
bool StockpointmanageR::IsActualLast()
{
   if( (GetLastPoint())->GetActuals() == 0 )
      return false;
   else
      return true;
} // end IsActualLast

//***********************************************************************************************
// Class    : StockpointmanageR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : ResizePredictions
// Purpose  : 
// Notes    : 
//***********************************************************************************************
void StockpointmanageR::ResizePredictions()
{
   iPredict_Size = iNumber_Of_Elements + iStep_Size;
   StockpoinT** temp = new StockpoinT*[ iPredict_Size ];

   for( int i = 0; i < iNumber_Of_Elements; i++ )
      temp[i] = theStockPoints[i];

   delete[] theStockPoints;
   theStockPoints = temp;
} // end ResizePredictions

//***********************************************************************************************
// Class    : StockpointmanageR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : ResizeActuals
// Purpose  : 
// Notes    : 
//***********************************************************************************************
void StockpointmanageR::ResizeActuals()
{
   iActual_Size = iNumber_Of_Actuals + iStep_Size;
   StockserieS** temp = new StockserieS*[ iActual_Size ];

   for( int i = 0; i < iNumber_Of_Actuals; i++ )
      temp[i] = theActualSeries[i];

   delete[] theActualSeries;
   theActualSeries = temp;
} // end ResizeActuals

//***********************************************************************************************
// Class    : StockpointmanageR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : FindBestPoint
// Purpose  : 
// Notes    : Assume that at least one point exists.
//***********************************************************************************************
StockpoinT* StockpointmanageR::FindBestPoint( VectoR* theVec )
{
   long double ldLowest = 2000000;   // Initialize to big number
   long double ldTemp   = 0;
   StockpoinT* BestMatch = theStockPoints[ 0 ];

   for( int i = 0; i < iNumber_Of_Elements; i++ )
   {
      if( theStockPoints[i]->GetActuals() != 0 )
      {
         ldTemp = theStockPoints[i]->Compare( theVec );
         if( funcHelper->Find_Max( ldLowest, ldTemp ) )
         {
            ldLowest = ldTemp;
            BestMatch = theStockPoints[i];
         } // end if Find_Max
      } // end if actuals is not null
   } // end for

   return BestMatch;
} // end FindBestPoint

//***********************************************************************************************
// Class    : StockpointmanageR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : GetLastDifferences
// Purpose  : 
// Notes    : 
//***********************************************************************************************
VectoR* StockpointmanageR::GetLastDifferences()
{
   StockserieS* theLastSeries = GetLastPoint()->GetPredictions();
   long double ldLastAverage = ((GetSecondToLastPoint())->GetPredictions())->GetLastAverage();
   theLastSeries->SetFirstAverage( ldLastAverage );
   theLastSeries->SetSmoothingFactor( ldSMA );
   theLastSeries->Rewind();
   VectoR* vDiff = new VectoR( theLastSeries->GetNumberOfElements() );
   VectoR* vAvs  = theLastSeries->GetAverages();
   int i = 0;
   while( theLastSeries->HasMoreElements() )
      vDiff->pVariables[ i++ ] = theLastSeries->GetNextElement() - vAvs->pVariables[ i ];

   return vDiff;
} // end GetLastDifferences

//***********************************************************************************************
// Class    : StockpointmanageR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : GetLastSeries
// Purpose  : Returns the last series be it Actual or Predicted
// Notes    : 
//***********************************************************************************************
StockserieS* StockpointmanageR::GetLastSeries()
{
StockserieS* theSeries = 0;

   if( IsActualLast() )
      theSeries = GetLastActual();
   else
      theSeries = GetLastPoint()->GetPredictions();

   return theSeries;
} // end GetLastSeries

//***********************************************************************************************
// Class    : StockpointmanageR
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Method   : IsLastIncreasing
// Purpose  : 
// Notes    : 
//***********************************************************************************************
bool StockpointmanageR::IsLastIncreasing()
{
int iTotal = 0;
long double ldPrevious = 0;
long double ldCurrent = 0;

   StockserieS* theLastSeries = GetLastSeries();
   theLastSeries->Rewind();
   ldPrevious = theLastSeries->GetNextElement();
   while( theLastSeries->HasMoreElements() )
   {
      ldCurrent = theLastSeries->GetNextElement();
      if( ldCurrent >= ldPrevious )
         iTotal += 1;
      else
         iTotal += -1;
      ldPrevious = ldCurrent;
   } // end while loop

   return ( iTotal >= 0 );
} // end IsLastIncreasing

