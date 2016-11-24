#!/bin/bash

export THE_DIR="$PWD";
cd $HOME/code/run

if [[ -z "$1" ]]; then
   set $(date +"%m %d %Y")
fi;

echo "The last date run was: $1 $2 $3" > last_date.txt;

java -Djava.ext.dirs="." stock.SP500Downloader $1 $2 $3;
java -Xms768m -Xmx768m -Djava.ext.dirs="." -Ddatabase=true stock.SP500Formatter;

cd $THE_DIR;
