#!/bin/bash

export THE_DIR="$PWD";

export MEM_ARGS="-Xms2G -Xmx2G";

java $MEM_ARGS -Dwatcher=true -Djava.ext.dirs="$HOME/dist/common" common.PostMail $*;
