#!/bin/bash

export THE_DIR="$PWD";
#cd $HOME/code/run

export MEM_ARGS="-Xms2G -Xmx2G";

#if [[ -z "$1" ]]; then
   #set $(date +"%m %d %Y")
#fi;

#tar xvfz datafiles/$1$2$3.tar.gz;

#java -Djava.ext.dirs="." stock.StockDriver $1 $2 $3;
# can pass: stock name, position, length, number of predictions
if [[ -n "$1" ]]; then
   if [[ "$1" == "linear" ]]; then
      java $MEM_ARGS -Djava.ext.dirs="$HOME/dist/stock" -Dlinear=true stock.StockDriver $*;
   elif [[ "$1" == "regular" ]]; then
      java $MEM_ARGS -Djava.ext.dirs="$HOME/dist/stock" -Dregular=true stock.StockDriver $*;
   elif [[ -z "$2" ]]; then
      java $MEM_ARGS -Djava.ext.dirs="$HOME/dist/stock" stock.StockDriver $*;
   else
      java $MEM_ARGS -Djava.ext.dirs="$HOME/dist/stock" -Dsingle=true stock.StockDriver $*;
   fi;
else
   java $MEM_ARGS -Djava.ext.dirs="$HOME/dist/stock" stock.StockDriver $*;
fi;

#cd $THE_DIR;
