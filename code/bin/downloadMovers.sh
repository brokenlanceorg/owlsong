#!/bin/bash

echo "Beginning download of movers at: `date`";
cd $HOME/options/ser;
FILE_NAME="movers_`date +%m-%d-%y`.csv";
~/projects/code/bin/stockDriver.sh regular > $FILE_NAME 2>&1;
cd -;
echo "Finished download of movers at: `date`";
