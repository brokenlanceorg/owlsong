#!/bin/bash
pushd .;
cd /home/brandon/projects/code/java;
find /home/brandon/projects/code/java -name "*.java" -exec ctags -a --extra=+q --fields=+ainSt '{}' \;
find /home/brandon/projects/code/java -name "*.java" > cscope.files;
cscope -b;
popd;
