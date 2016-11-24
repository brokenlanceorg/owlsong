//***********************************************************************************************
// File    : LinkItem.hpp
// Purpose : Base class for LinkItem classes
//         :
//         :
// Author  : Brandon Benham
// Date    : 5/12/05
//***********************************************************************************************

#ifndef __LINKITEM_HPP
#define __LINKITEM_HPP

#include <vector>
#include "DebugLogger.hpp"

using namespace std;

//***********************************************************************************************
// Class   : LinkIteM
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose :
//         :
//***********************************************************************************************
class LinkIteM
{
   public:

      LinkIteM();          // Default Constructor declaration
      virtual ~LinkIteM(); // Destructor declaration

      void AddLeftLink( LinkIteM* i ) { _leftLinks.push_back( i ); }
      void AddRightLink( LinkIteM* i ) { _rightLinks.push_back( i ); }

      void SetLeftConstituent( LinkIteM* );
      void SetRightConstituent( LinkIteM* );

      vector< LinkIteM* > GetLeftLinks() { return _leftLinks; }
      vector< LinkIteM* > GetRightLinks() { return _rightLinks; }

      LinkIteM* GetLeftConstituent() { return _leftConstituent; }
      LinkIteM* GetRightConstituent() { return _rightConstituent; }

   protected:

      void Setup();

      vector< LinkIteM* > _leftLinks;
      vector< LinkIteM* > _rightLinks;
      LinkIteM* _leftConstituent;
      LinkIteM* _rightConstituent;

   private:

}; // end LinkIteM declaration

#endif
