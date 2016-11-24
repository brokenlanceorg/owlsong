#!/bin/bash

export THE_DIR="$PWD";

export MEM_ARGS="-Xms2G -Xmx2G";

java $MEM_ARGS -Dwatcher=true -Djava.ext.dirs="$HOME/dist/stock" stock.StockDriver $*;

if [[ -f "/tmp/watched-stocks.txt" ]]; then
   echo "Found file will send notification email.";
   cat /tmp/watched-stocks.txt | mail -s "Strike Price Alert" saynday72@yahoo.com;
   cat /tmp/watched-stocks.txt | mail -s "Strike Price Alert" brandon.benham@gmail.com;
   rm /tmp/watched-stocks.txt;
else
   echo "Did not find file, will exit.";
fi;
