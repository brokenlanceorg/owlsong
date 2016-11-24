//***********************************************************************************************
// File    : ConstituentItem.hpp
// Purpose : Base class for ConstituentItem classes
//         :
//         :
// Author  : Brandon Benham
// Date    : 7/8/05
//***********************************************************************************************

#ifndef __CONSTITUENTITEM_HPP
#define __CONSTITUENTITEM_HPP

using namespace std;

#include <vector>
#include <string>

//***********************************************************************************************
// Class   : ConstituentIteM
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// Purpose :
//         :
//***********************************************************************************************
class ConstituentIteM
{
   public:

      ConstituentIteM();          // Default Constructor declaration
      virtual ~ConstituentIteM(); // Destructor declaration

      void SetDomains( char* );
      void SetLinkType( char* );
      void SetContext( char* );
      void SetLinkedConstituent( char* );
      void SetConstituent( char* );
      void SetInflectedForm( char* );
      void SetLinkage( long double );
      void SetSerialNumber( long );

      vector< string* > GetDomains() { return _domains; }
      string* GetLinkType() { return _linkType; }
      string* GetContext() { return _context; }
      string* GetLinkedConstituent() { return _linkedConstituent; }
      string* GetConstituent() { return _constituent; }
      string* GetInflectedForm() { return _inflectedForm; }
      long double GetLinkage() { return _linkage; }
      long GetSerialNumber() { return _serialNumber; }

   protected:

      void Setup();

   private:

      vector< string* > _domains;
      string* _linkType;
      string* _context;
      string* _linkedConstituent;
      string* _constituent;
      string* _inflectedForm;
      long double _linkage;
      long _serialNumber;

}; // end ConstituentIteM declaration

#endif
