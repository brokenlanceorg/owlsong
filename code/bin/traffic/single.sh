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
#           <block size> <block size NLR> <hidden size> <final size> <lag> ] [<scale size>]
# train     <name> [ <running time> ]
# test      <name>
# add       <name> <JPEG filename without ext> <running time> <genome length> 
#           <block size> <block size NLR> <hidden size> <final size> <lag>
# add       <name> <entropic auto correlation name>
# interpret <name> <JPEG filename without ext>
#
#java -Djava.ext.dirs=$HOME/dist/functionalNetwork/ functional.entropic.EntropicAutoCorrelation create BI-4.3 I30.BryantIrvin4 3m 15 5 2 7 2 80
#java -Djava.ext.dirs=$HOME/dist/functionalNetwork/ functional.entropic.EntropicAutoCorrelation compare BI-8 BI-1
#java -Djava.ext.dirs=$HOME/dist/functionalNetwork/ functional.entropic.EntropicAutoCorrelation compare BI-8 BI-2
#java -Djava.ext.dirs=$HOME/dist/functionalNetwork/ functional.entropic.EntropicAutoCorrelation compare BI-8 BI-3
#java -Djava.ext.dirs=$HOME/dist/functionalNetwork/ functional.entropic.EntropicAutoCorrelation compare BI-8 BI-4
#java -Djava.ext.dirs=$HOME/dist/functionalNetwork/ functional.entropic.EntropicAutoCorrelation compare BI-8 BI-5
#java -Djava.ext.dirs=$HOME/dist/functionalNetwork/ functional.entropic.EntropicAutoCorrelation compare BI-8 BI-6
#java -Djava.ext.dirs=$HOME/dist/functionalNetwork/ functional.entropic.EntropicAutoCorrelation compare BI-8 BI-7
java -Djava.ext.dirs=$HOME/dist/functionalNetwork/ functional.entropic.EntropicAutoCorrelation compare BI-4.2 BI-4.3
