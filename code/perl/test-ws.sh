#!/bin/bash

printUsage ()
{
   echo "Please specify test script and length of time (seconds) to wait";
   echo "e.g., test-ws.sh test-req.2.txt 70";
   exit 1;
}

if [ -z "$1" ]; then
   printUsage;
elif [ -z "$2" ]; then
   printUsage;
fi;

$HOME/bin/telfeed.sh $1 $2 | telnet;
