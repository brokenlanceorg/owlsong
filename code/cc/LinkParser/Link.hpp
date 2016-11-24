//***********************************************************************************************
// File    : Link.hpp
// Purpose : Base class for LinK classes
//         :
//         :
// Author  : Brandon Benham
// Date    : 5/12/05
//***********************************************************************************************

#ifndef __LINK_HPP
#define __LINK_HPP

#include <iostream>
#include <string>
#include "Constituent.hpp"
#include "LinkItem.hpp"
#include "DebugLogger.hpp"

using namespace std;

//***********************************************************************************************
// Class   : LinK
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose :
//         :
//***********************************************************************************************
class LinK : public LinkIteM
{
   public:

      LinK();          // Default Constructor declaration
      LinK( string*, int, int, vector< string* > );          // Default Constructor declaration
      virtual ~LinK(); // Destructor declaration

      void SetLabel( string* l ) { _label = l; }
      void SetLinkValue( int v ) { _linkValue = v; }
      void SetVisited() { _visited = true; }
      void ClearVisited() { _visited = false; }
      bool HasBeenVisited() { return _visited; }

      int GetLinkValue() { return _linkValue; }
      int GetLeftLink() { return _left; }
      int GetRightLink() { return _right; }
      string* GetLabel() { return _label; }
      vector< string* > GetDomains() { return _domains; }
      int GetNumberOfDomains() { return _domains.size(); }

   protected:

      void Setup();

   private:

      string* _label;
      vector< string* > _domains;
      bool _visited;
      int _left;
      int _right;
      int _linkValue;

}; // end LinK declaration

#endif
