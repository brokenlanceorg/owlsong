#!/bin/bash

# This will simply run a suite of tests to gather statistics
# to determine the efficacy of certain parameters.

# The data input were 200 instances of 3x1 with the 
# target being uniformly random transformed by
# a moving average of a window size of 10.
# These tests were run with a genome of powerset 6
# and running time of 10 minutes.
echo "Run INTC `date`" > intc.roc.3.csv;
cp intc.variables.dat variables.dat;
cp target-intc.dat target.dat;
runGenetic.sh >> intc.roc.3.csv 2>&1;
echo "Run IBM `date`" > ibm.roc.3.csv;
cp ibm.variables.dat variables.dat;
cp target-ibm.dat target.dat;
runGenetic.sh >> ibm.roc.3.csv 2>&1;
echo "Run MSFT `date`" > msft.roc.3.csv;
cp msft.variables.dat variables.dat;
cp target-msft.dat target.dat;
runGenetic.sh >> msft.roc.3.csv 2>&1;
echo "Run CSCO `date`" > csco.roc.3.csv;
cp csco.variables.dat variables.dat;
cp target-csco.dat target.dat;
runGenetic.sh >> csco.roc.3.csv 2>&1;
echo "Run AMD `date`" > amd.roc.3.csv;
cp amd.variables.dat variables.dat;
cp target-amd.dat target.dat;
runGenetic.sh >> amd.roc.3.csv 2>&1;
echo "Run SPY `date`" > spy.roc.3.csv;
cp spy.variables.dat variables.dat;
cp target-spy.dat target.dat;
runGenetic.sh >> spy.roc.3.csv 2>&1;
