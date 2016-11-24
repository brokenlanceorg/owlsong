#!/bin/bash
#export DISPLAY=:0.0;
# Parameters are: 
#   genome length          -- balanced binary trees are at: 15, 31, 63, 127, etc.
#   hilbert block size     -- raised to the power of two
#   hilbert block size NLR -- raised to the power of two
#   hidden matrix size     -- square matrix
#   final matrix size      -- square matrix
#   embedding time lag     -- dependent upon size of data
#
# Usage:
# create    <name> [ <JPEG filename without ext> <running time> <genome length> 
#           <block size> <block size NLR> <hidden size> <final size> <lag> ]
# train     <name> [ <running time> ]
# test      <name>
# add       <name> <JPEG filename without ext> <running time> <genome length> 
#           <block size> <block size NLR> <hidden size> <final size> <lag>
# add       <name> <entropic auto correlation name>
# interpret <name> <JPEG filename without ext>
#
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar create bryant-irvin
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar add bryant-irvin BI-1
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar add bryant-irvin BI-2
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar add bryant-irvin BI-3
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar add bryant-irvin BI-4
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar add bryant-irvin BI-5
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar add bryant-irvin BI-6
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar add bryant-irvin BI-7
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar test bryant-irvin
java -jar ~/dist/functionalNetwork/functionalNetwork.jar interpret bryant-irvin I30.BryantIrvin8
java -jar ~/dist/functionalNetwork/functionalNetwork.jar list bryant-irvin
