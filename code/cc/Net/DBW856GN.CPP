//----------------------------------------------------------------------------
//  Project Daemon
//  Quality Systems Management
//  Copyright © 1996. All Rights Reserved.
//
//  SUBSYSTEM:    Daemon Application
//  FILE:         qdbinter.cpp
//  AUTHOR:       Chris Coppeans
//
//  OVERVIEW
//  ~~~~~~~~
//  Database portion of the 856 generating program.
//
//----------------------------------------------------------------------------

#include "qdbinter.h"

// This is the constructor.  It initializes all variables and then
// calls the get_all_generals function to get all of the general info
// variables set up.
TDBW856gen::TDBW856gen() {
  header = NULL;
  current_id = 0;
  get_all_generals();
  }

// This function clears out any memory that's been allocated.
TDBW856gen::~TDBW856gen() {
  Tgeneral_info *old;
  while(header != NULL) {
    old = header;
    header = header->next;
    delete old;
    }
  }

// This function gets all of the bits of general information and
// sets them up in a general_info linked list.
void TDBW856gen::get_all_generals() {
  char *qualifier, *arg;
  qualifier = new char[9];
  strcpy(qualifier, "Finished");
  arg = new char[5];
  strcpy(arg, "True");
  do_select("orders.db", "Customer", "Whirlpool", "WPart", &qualifier, &arg, 1);
  while(!my_select_query->AtEOF) {
    current = new Tgeneral_info(32);
    get_string_field_by_name(current->po, 32, "PO", my_select_query);
    get_string_field_by_name(current->pn, 32, "WPart", my_select_query);
    get_string_field_by_name(current->duedate, 32, "Due_Date", my_select_query);
    get_string_field_by_name(current->release, 32, "Release", my_select_query);
    current->idnum = get_numeric_field_by_name("AutoID", my_select_query);
    current->next = header;
    header = current;
    }
  delete[] qualifier;
  delete[] arg;
  }

// This function removes a record from the orders table and a lot of records
// from the shipments table.  All of it corresponds to the numerical identifier
// of that record.
void TDBW856gen::delete_specific_general(int id) {
  extra_query->SQL->Clear();
  extra_query->SQL->Add("DELETE FROM orders.db WHERE AutoID = :arg;");
  extra_query->ParamByName(string("arg"))->AsInteger = id;
  extra_query->ExecSQL();   
  extra_query->SQL->Clear();
  extra_query->SQL->Add("DELETE FROM shipmnts.db WHERE Order_ID = :arg;");
  extra_query->ParamByName(string("arg"))->AsInteger = id;
  extra_query->ExecSQL();
  }

// This function pops the next bit of general info off the stack and returns
// it by reference.  It also sets up the query for specific information.
void TDBW856gen::get_general_info(char *po, char *pn, char *release, char *duedate) {
  if (header == NULL) {
    po[0] = pn[0] = release[0] = duedate[0] = '\0';
    return;
    }
  if (current_id)
    delete_specific_general(current_id);
  strcpy(po, header->po);
  strcpy(pn, header->pn);
  strcpy(duedate, header->duedate);
  strcpy(release, header->release);
  current_id = header->idnum;
  current = header->next;
  delete header;
  header = current;
  char *tmp = new char[32];
  itoa(current_id, tmp, 10);
  do_select("shipmnts.db", "Order_ID", tmp, "Serial");
  }

// This function gets the individual bits of specific information and
// returns them by reference.
void TDBW856gen::get_specific_info(int *wgt, char *serial) {
  if (my_select_query->AtEOF) {
    *wgt = 0;
    serial[0] = '\0';
    return;
    }
  *wgt = get_numeric_field_by_name("Weight", my_select_query);
  get_string_field_by_name(serial, 17, "Serial", my_select_query);
  my_select_query->Next();
  }

