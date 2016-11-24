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
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar create garments
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar add garments SSB
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar add garments SSBL
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar add garments SSG
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar add garments LSB
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar add garments LSY
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar add garments PB
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar test garments
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar interpret garments pullover-blue
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar list garments
#
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar create short-sleeves
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar add short-sleeves SSB
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar add short-sleeves SSBL
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar add short-sleeves SSG
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar add short-sleeves LSB
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar add short-sleeves LSY
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar add short-sleeves PB
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar test short-sleeves
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar interpret short-sleeves pullover-blue
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar interpret short-sleeves short-sleeve-gray
java -Djava.awt.headless=true -jar ~/dist/functionalNetwork/functionalNetwork.jar interpret short-sleeves long-sleeve-blue
java -Djava.awt.headless=true -jar ~/dist/functionalNetwork/functionalNetwork.jar list short-sleeves
#
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar create long-sleeves
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar add long-sleeves SSB
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar add long-sleeves SSBL
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar add long-sleeves SSG
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar add long-sleeves LSB
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar add long-sleeves LSY
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar add long-sleeves PB
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar test long-sleeves
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar interpret long-sleeves long-sleeve-blue
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar interpret long-sleeves short-sleeve-blue
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar list long-sleeves
