#!/bin/bash

# This will simply run a suite of tests to gather statistics
# to determine the efficacy of certain parameters.

export t=0;
while true
do
   (( t = $t + 1 ));
   runGenetic.sh > output.csv 2>&1;
   mv output.csv output-$t.csv;
   # Check for exit condition
   if [ "$t" -gt "20" ]; then
      exit;
   fi
done
