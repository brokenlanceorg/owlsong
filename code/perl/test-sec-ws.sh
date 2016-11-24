#!/bin/bash

printUsage ()
{
   echo "Please specify test script and length of time (seconds) to wait";
   echo "e.g., test-sec-ws.sh test-req.2.txt 70";
   exit 1;
}

if [ -z "$1" ]; then
   printUsage;
elif [ -z "$2" ]; then
   printUsage;
fi;

$HOME/bin/secfeed.sh $1 $2 | openssl s_client -connect itkts.stage.aa.com:443 -state -debug;
