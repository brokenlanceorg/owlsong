#!/bin/bash

cd $HOME/dist;

if [ -z "$1" ]; then

   set $(date +"%m %d %Y")

   echo "Downloading for month/day/year:";
   echo $1; # month
   echo $2; # day
   echo $3; # year
fi;

wget https://dumps.wikimedia.org/other/pagecounts-raw/$3/$3-$1/pagecounts-$3$1$2-000000.gz;
wget https://dumps.wikimedia.org/other/pagecounts-raw/$3/$3-$1/pagecounts-$3$1$2-010000.gz;
wget https://dumps.wikimedia.org/other/pagecounts-raw/$3/$3-$1/pagecounts-$3$1$2-020000.gz;
wget https://dumps.wikimedia.org/other/pagecounts-raw/$3/$3-$1/pagecounts-$3$1$2-030000.gz;
wget https://dumps.wikimedia.org/other/pagecounts-raw/$3/$3-$1/pagecounts-$3$1$2-040000.gz;
wget https://dumps.wikimedia.org/other/pagecounts-raw/$3/$3-$1/pagecounts-$3$1$2-050000.gz;
wget https://dumps.wikimedia.org/other/pagecounts-raw/$3/$3-$1/pagecounts-$3$1$2-060000.gz;
wget https://dumps.wikimedia.org/other/pagecounts-raw/$3/$3-$1/pagecounts-$3$1$2-070000.gz;
wget https://dumps.wikimedia.org/other/pagecounts-raw/$3/$3-$1/pagecounts-$3$1$2-080000.gz;
wget https://dumps.wikimedia.org/other/pagecounts-raw/$3/$3-$1/pagecounts-$3$1$2-090000.gz;
wget https://dumps.wikimedia.org/other/pagecounts-raw/$3/$3-$1/pagecounts-$3$1$2-100000.gz;
wget https://dumps.wikimedia.org/other/pagecounts-raw/$3/$3-$1/pagecounts-$3$1$2-110000.gz;
wget https://dumps.wikimedia.org/other/pagecounts-raw/$3/$3-$1/pagecounts-$3$1$2-120000.gz;
wget https://dumps.wikimedia.org/other/pagecounts-raw/$3/$3-$1/pagecounts-$3$1$2-130000.gz;
wget https://dumps.wikimedia.org/other/pagecounts-raw/$3/$3-$1/pagecounts-$3$1$2-140000.gz;
wget https://dumps.wikimedia.org/other/pagecounts-raw/$3/$3-$1/pagecounts-$3$1$2-150000.gz;
wget https://dumps.wikimedia.org/other/pagecounts-raw/$3/$3-$1/pagecounts-$3$1$2-160000.gz;
wget https://dumps.wikimedia.org/other/pagecounts-raw/$3/$3-$1/pagecounts-$3$1$2-170000.gz;
wget https://dumps.wikimedia.org/other/pagecounts-raw/$3/$3-$1/pagecounts-$3$1$2-180000.gz;
wget https://dumps.wikimedia.org/other/pagecounts-raw/$3/$3-$1/pagecounts-$3$1$2-190000.gz;
wget https://dumps.wikimedia.org/other/pagecounts-raw/$3/$3-$1/pagecounts-$3$1$2-200000.gz;
wget https://dumps.wikimedia.org/other/pagecounts-raw/$3/$3-$1/pagecounts-$3$1$2-210000.gz;
wget https://dumps.wikimedia.org/other/pagecounts-raw/$3/$3-$1/pagecounts-$3$1$2-220000.gz;
wget https://dumps.wikimedia.org/other/pagecounts-raw/$3/$3-$1/pagecounts-$3$1$2-230000.gz;

echo "Decompressing...";

gzip -d pagecounts-$3$1$2-000000.gz;
gzip -d pagecounts-$3$1$2-010000.gz;
gzip -d pagecounts-$3$1$2-020000.gz;
gzip -d pagecounts-$3$1$2-030000.gz;
gzip -d pagecounts-$3$1$2-040000.gz;
gzip -d pagecounts-$3$1$2-050000.gz;
gzip -d pagecounts-$3$1$2-060000.gz;
gzip -d pagecounts-$3$1$2-070000.gz;
gzip -d pagecounts-$3$1$2-080000.gz;
gzip -d pagecounts-$3$1$2-090000.gz;
gzip -d pagecounts-$3$1$2-100000.gz;
gzip -d pagecounts-$3$1$2-110000.gz;
gzip -d pagecounts-$3$1$2-120000.gz;
gzip -d pagecounts-$3$1$2-130000.gz;
gzip -d pagecounts-$3$1$2-140000.gz;
gzip -d pagecounts-$3$1$2-150000.gz;
gzip -d pagecounts-$3$1$2-160000.gz;
gzip -d pagecounts-$3$1$2-170000.gz;
gzip -d pagecounts-$3$1$2-180000.gz;
gzip -d pagecounts-$3$1$2-190000.gz;
gzip -d pagecounts-$3$1$2-200000.gz;
gzip -d pagecounts-$3$1$2-210000.gz;
gzip -d pagecounts-$3$1$2-220000.gz;
gzip -d pagecounts-$3$1$2-230000.gz;

cd -;
