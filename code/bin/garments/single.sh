#!/bin/bash
export DISPLAY=:0.0;
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
#java -Djava.ext.dirs=$HOME/dist/functionalNetwork/ functional.entropic.EntropicAutoCorrelation create SSB short-sleeve-blue 3m 15 5 2 7 2 80
#java -Djava.ext.dirs=$HOME/dist/functionalNetwork/ functional.entropic.EntropicAutoCorrelation create SSBL short-sleeve-black 3m 15 5 2 7 2 80
#java -Djava.ext.dirs=$HOME/dist/functionalNetwork/ functional.entropic.EntropicAutoCorrelation create SSG short-sleeve-gray 3m 15 5 2 7 2 80
#java -Djava.ext.dirs=$HOME/dist/functionalNetwork/ functional.entropic.EntropicAutoCorrelation create LSB long-sleeve-blue 3m 15 5 2 7 2 80
#java -Djava.ext.dirs=$HOME/dist/functionalNetwork/ functional.entropic.EntropicAutoCorrelation create LSY long-sleeve-yellow 3m 15 5 2 7 2 80
java -Djava.awt.headless=true -Djava.ext.dirs=$HOME/dist/functionalNetwork/ functional.entropic.EntropicAutoCorrelation create PB pullover-blue 3m 15 5 2 7 2 80
#java -Djava.ext.dirs=$HOME/dist/functionalNetwork/ functional.entropic.EntropicAutoCorrelation compare BI-8 BI-8
