//***********************************************************************************************
// File    : LargeNetwork.hpp
// Purpose : Base class for LargeNetworK classes
//         :
//         :
// Author  : Brandon Benham
// Date    : 5/5/05
//***********************************************************************************************

#ifndef __LARGENETWORK_HPP
#define __LARGENETWORK_HPP

#include "RollingNetwork.hpp"
#include <vector>

//***********************************************************************************************
// Class   : LargeNetworK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose :
//         :
//***********************************************************************************************
class LargeNetworK
{
   public:

      LargeNetworK();          // Default Constructor declaration
      LargeNetworK( char*, int, int, int, int, int, int );
      LargeNetworK( char*, int, int, int, int, int );
      virtual ~LargeNetworK(); // Destructor declaration

      // Delegated calls to the base network:
      vector< VectoR* > Recall( VectoR* );
      VectoR* IdempotentRecall( VectoR* );

      // Methods for RollingNetwork:
      void AddTrainingPair( VectoR*, VectoR* );
      void AddTrainingPairWithRollback( VectoR*, VectoR* );
      int GetLargestNumberOfInstances();

   protected:

      void Setup( int );
      void Initialize( char*, int, int, int, int, int, int );
      int FindBestMatch( VectoR*, VectoR* );
      int TrainWithRollback( VectoR*, VectoR*, int, int );

   private:

      vector< RollingNetworK* > _theNetworks;
      int _currentNetwork;
      int _numberOfNetworks;
      int _maxNumberOfNetworks;
      int _depth;
      int _inputs;
      int _outputs;
      int _hidden;
      static VectoR _random;
      char _baseName[30];

}; // end LargeNetworK declaration

#endif
