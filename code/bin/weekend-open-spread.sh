#!/bin/bash

#
# The idea with this process is the following:
#  1) Right after the opening bell, pull the amended data for NAS100 and Q
#  2) Train that for 6 hours to end about 30 mins before close
#  3) Pull the amended data again to refresh the high, low, and most likely close
#  4) Predict the open spread based on this data
#  5) Email the results for a potential buy
#

# Train phase
# Params are: training days back, time to train, ensemble size, genome size, use amended boolean
/home/brandon/projects/code/bin/stockDriver.sh 4 2d 9 57 > /home/brandon/logs/weekend-open-spread-train.log 2>&1;

# Predict phase  -- the negative tells it to predict and not train, the 1 tells it to be amended
/home/brandon/projects/code/bin/stockDriver.sh -4 1 9 57 > /home/brandon/logs/weekend-open-spread-predict.log 2>&1;

# Now send the data via email:
TEMP=`grep error /home/brandon/logs/weekend-open-spread-train.log`;
TEMP=$TEMP" "`tail -1 /home/brandon/logs/weekend-open-spread-predict.log`;
echo $TEMP;

# Finally, backup today's data to the previous folder for tomorrow:
cp /home/brandon/projects/code/bin/4-e9-l57-sopen.ser /home/brandon/projects/code/bin/SP500/previous;
