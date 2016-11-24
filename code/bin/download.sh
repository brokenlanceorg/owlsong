#!/bin/bash

cd $HOME/projects/code/bin

if [[ -z "$1" ]]; then
   set $(date +"%m %d %Y")
fi;

########################################################
# make sure we download all the stocks in Russell:
cp $HOME/projects/code/bin/SP500/allStocks.dat $HOME/projects/code/bin/SP500/SP500.dat;

# now, run the downloader:
java -Djava.ext.dirs="$HOME/dist/stock" stock.SP500Downloader $1 $2 $3;

# populate the DB:
java -Xms768m -Xmx768m -Djava.ext.dirs="$HOME/dist/stock" -Ddatabase=true stock.SP500Formatter;

# make sure we download all the misc stocks:
#cp $HOME/code/run/SP500/miscStocks.dat $HOME/code/run/SP500/SP500.dat;

# now, run the downloader again:
#java -Djava.ext.dirs="." stock.SP500Downloader $1 $2 $3;

# change the names of the downloaded misc stocks so there's no MYSQL keywords:
#cp $HOME/code/run/SP500/miscStocksMod.dat $HOME/code/run/SP500/SP500.dat;

# rename the downloaded files:
#mv $HOME/code/run/SP500/INT.csv $HOME/code/run/SP500/INTTT.csv;
#mv $HOME/code/run/SP500/ALL.csv $HOME/code/run/SP500/ALLT.csv;
#mv $HOME/code/run/SP500/KEY.csv $HOME/code/run/SP500/KEYY.csv;
#mv $HOME/code/run/SP500/KEYS.csv $HOME/code/run/SP500/KEYSY.csv;
#mv $HOME/code/run/SP500/IN.csv $HOME/code/run/SP500/INXX.csv;

# finally, populate the DB:
#java -Xms768m -Xmx768m -Djava.ext.dirs="." -Ddatabase=true stock.SP500Formatter;

# Cleanup all the extraneous data files:
rm $HOME/projects/code/bin/SP500/*.csv;

cd -;
