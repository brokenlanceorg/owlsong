#!/bin/bash

export THE_DIR="$PWD";
export MEM_ARGS="-Xms1024m -Xmx1024m";
cd $HOME/projects/code/bin;
java $MEM_ARGS -Djava.ext.dirs="$HOME/dist/common" common.WebPageReader;
cd -;
