java -server -XX:+PrintGCTimeStamps -verbose:gc -XX:+PrintGCDetails -XX:NewSize=1024m -XX:MaxNewSize=1024m -Xms1178m -Xmx1178m -jar $HOME/dist/functionalNetwork/functionalNetwork.jar $*
