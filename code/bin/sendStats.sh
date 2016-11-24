#!/bin/bash

grep Fitness /home/brandon/projects/code/bin/output.csv | mail -s "Fitness Values" saynday72@yahoo.com
#grep Fitness /home/brandon/dist/output.csv | mail -s "Fitness Values 2" saynday72@yahoo.com
#export DIR="/home/brandon/projects/code/bin";
#export STATS="Deriviance 1: `grep Fitness $DIR/deriviance.1.csv | tail -1`";
#if [[ -f "$DIR/deriviance.2.csv" ]];
#then
   #export STATS=$STATS" Deriviance 2: `grep Fitness $DIR/deriviance.2.csv | tail -1`";
#fi
#if [[ -f "$DIR/deriviance.3.csv" ]];
#then
   #export STATS=$STATS" Deriviance 3: `grep Fitness $DIR/deriviance.3.csv | tail -1`";
#fi
#if [[ -f "$DIR/deriviance.4.csv" ]];
#then
   #export STATS=$STATS" Deriviance 4: `grep Fitness $DIR/deriviance.4.csv | tail -1`";
#fi
#if [[ -f "$DIR/deriviance.5.csv" ]];
#then
   #export STATS=$STATS" Deriviance 5: `grep Fitness $DIR/deriviance.5.csv | tail -1`";
#fi
#echo $STATS | mail -s "Deriviance Fitness Values" saynday72@yahoo.com
