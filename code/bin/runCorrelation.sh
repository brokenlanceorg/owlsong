#!/bin/bash
#
#     * The syntax will be one of the following:
# command <create> <elapsed time> <ensemble size> <genome size>
# command <create|train> <elapsed time>
# command <create|train|test|correlate|predict>
# command <predict> <data points>
#
#

java -server -XX:+PrintGCTimeStamps -verbose:gc -XX:+PrintGCDetails -XX:NewSize=1024m -XX:MaxNewSize=1024m -Xms1178m -Xmx1178m -jar $HOME/dist/functionalNetwork/functionalNetwork.jar $*
