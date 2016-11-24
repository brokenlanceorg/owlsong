#!/bin/bash

export THE_DIR="$PWD";
cd $HOME/code/run

if [[ -z "$1" ]]; then
   set $(date +"%m %d %Y")
fi;

#java -Djava.ext.dirs="." stock.SP500Downloader $1 $2 $3;
java -Xms768m -Xmx768m -Djava.ext.dirs="." -Ddatabase=true stock.SP500Formatter;

#if [[ -d "datafiles" ]]; then
   #echo "found the dir";
#else
   #mkdir datafiles;
   #echo "created the dir";
#fi;

#cd SP500;

#tar cvfz ../datafiles/$1$2$3.tar.gz sp500hst.txt;

cd $THE_DIR;
