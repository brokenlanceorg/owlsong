#!/bin/bash
$HOME/projects/code/bin/stockDriver.sh > $HOME/logs/underCurrent.txt 2>&1;
mail -s "Gravitational Statistics" saynday72@yahoo.com < $HOME/logs/underCurrent.txt;
