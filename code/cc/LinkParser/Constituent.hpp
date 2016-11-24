//***********************************************************************************************
// File    : Constituent.hpp
// Purpose : Base class for ConstituenT classes
//         :
//         :
// Author  : Brandon Benham
// Date    : 5/12/05
//***********************************************************************************************

#ifndef __CONSTITUENT_HPP
#define __CONSTITUENT_HPP

#include "Link.hpp"
#include "LinkItem.hpp"
#include "SemanticDifferential.hpp"
#include "DebugLogger.hpp"
#include "ConstituentItem.hpp"

#include <iostream>
#include <vector>
#include <string>

using namespace std;

//***********************************************************************************************
// Class   : ConstituenT
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose :
//         :
//***********************************************************************************************
class ConstituenT : public LinkIteM
{
   public:

      ConstituenT( int, string*, string* );
      ConstituenT( char* );
      ConstituenT();          // Default Constructor declaration
      virtual ~ConstituenT(); // Destructor declaration

      void SetSemanticDifferential( SemanticDifferentiaL* s ) { _semanticDifferential = s; }
      void SetForm( string* s ) { _form = s; }
      void SetInflectedForm( string* s ) { _inflectedForm = s; }
      void SetOrdinal( int i ) { _ordinal = i; }
      void AddConstituentItem( ConstituentIteM* item ) { _constituentItems.push_back( item ); }
      int GetOrdinal() { return _ordinal; }

      string* GetForm() { return _form; }
      string* GetInflectedForm() { return _inflectedForm; }

      SemanticDifferentiaL* GetSemanticDifferential() { return _semanticDifferential; }

      vector< ConstituentIteM* > GetConstituentItems() { return _constituentItems; }

   protected:

      void Setup();

   private:

      int _ordinal;
      SemanticDifferentiaL* _semanticDifferential;
      string* _form;
      string* _inflectedForm;
      vector< ConstituentIteM* > _constituentItems;

}; // end ConstituenT declaration

#endif
