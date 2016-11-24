#!/bin/bash
#export DISPLAY=:0.0;
# Parameters are: 
#   genome length          -- balanced binary trees are at: 15, 31, 63, 127, etc.
#   hilbert block size     -- raised to the power of two
#   hilbert block size NLR -- raised to the power of two
#   hidden matrix size     -- square matrix
#   final matrix size      -- square matrix
#   embedding time lag     -- dependent upon size of data
java -jar ~/dist/functionalNetwork/functionalNetwork.jar create BI-1 I30.BryantIrvin1 30m 15 5 2 7 2 80
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar create BI-2 I30.BryantIrvin2 30m 15 5 2 7 2 80
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar create BI-3 I30.BryantIrvin3 30m 15 5 2 7 2 80
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar create BI-4 I30.BryantIrvin4 30m 15 5 2 7 2 80
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar create BI-5 I30.BryantIrvin5 30m 15 5 2 7 2 80
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar create BI-6 I30.BryantIrvin6 30m 15 5 2 7 2 80
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar create BI-7 I30.BryantIrvin7 30m 15 5 2 7 2 80
# BI-1 is most similar to BI-5 0.4517982742617268
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-1 BI-2
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-1 BI-3
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-1 BI-4
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-1 BI-5
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-1 BI-6
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-1 BI-7
# BI-2 is most similar to BI-7 0.35466331071204754
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-2 BI-1
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-2 BI-3
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-2 BI-4
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-2 BI-5
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-2 BI-6
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-2 BI-7
# BI-3 is most similar to BI-5 0.2804186233530149
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-3 BI-1
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-3 BI-2
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-3 BI-4
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-3 BI-5
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-3 BI-6
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-3 BI-7
# BI-4 is most similar to BI-6 0.3006232361029522
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-4 BI-1
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-4 BI-2
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-4 BI-3
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-4 BI-5
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-4 BI-6
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-4 BI-7
# BI-5 is most similar to BI-3 0.26545849872260097
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-5 BI-1
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-5 BI-2
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-5 BI-3
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-5 BI-4
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-5 BI-6
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-5 BI-7
# BI-6 is most similar to BI-2 0.3479267122540676
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-6 BI-1
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-6 BI-2
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-6 BI-3
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-6 BI-4
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-6 BI-5
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-6 BI-7
# BI-7 is most similar to BI-3 0.3394381194010096
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-7 BI-1
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-7 BI-2
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-7 BI-3
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-7 BI-4
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-7 BI-5
#java -jar ~/dist/functionalNetwork/functionalNetwork.jar compare BI-7 BI-6
