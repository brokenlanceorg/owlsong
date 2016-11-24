#!/bin/bash
grep -B 4 FileNotFoundException ~/logs/download.log | grep name | awk '{print $NF;}' > dead.list;
cat allStocks.dat dead.list | sort | uniq -u > t.tmp;
