#!/bin/bash
# This script is used to send test requests to the stage 1.2 server
echo "open 10.97.100.105 9082"
#echo "open itkts.stage.aa.com 80"
sleep 1
echo "`cat $1`"
echo ""
echo ""
sleep $2
