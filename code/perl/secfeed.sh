#!/bin/bash
# This script is used to send test requests to the stage 1.2 server
# openssl s_client -connect itkts.stage.aa.com:443 -state -debug
sleep 1
echo "`cat $1`"
echo ""
echo ""
sleep $2
