#!/bin/bash
/home/brandon/projects/code/bin/stockDriver.sh -${1} 0 5 17 > /home/brandon/logs/predict-${1}.txt 2>&1;
#cat /home/brandon/logs/predict-${1}.txt | mail -s "Prediction for ${1}" saynday72@yahoo.com;
