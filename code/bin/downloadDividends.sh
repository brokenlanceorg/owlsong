#!/bin/bash

cd $HOME/projects/code/bin

if [[ -z "$1" ]]; then
   set $(date +"%m %d %Y")
fi;

########################################################
# make sure we download all the stocks in Russell:
cp $HOME/projects/code/bin/SP500/allStocks.dat $HOME/projects/code/bin/SP500/SP500.dat;

# now, run the downloader:
java -Ddiv=true -Djava.ext.dirs="$HOME/dist/stock" stock.SP500Downloader $1 $2 $3;

# populate the DB:
java -Ddiv=true -Djava.ext.dirs="$HOME/dist/stock" -Xms768m -Xmx768m stock.SP500Formatter;

# Cleanup all the extraneous data files:
rm $HOME/projects/code/bin/SP500/*.csv;

cd -;
