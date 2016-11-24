#java -server -XX:+PrintGCTimeStamps -verbose:gc -XX:+PrintGCDetails -XX:NewSize=1024m -XX:MaxNewSize=1024m -Xms4096m -Xmx4096m -jar $HOME/dist/genetic/genetic.jar
java -server -XX:+PrintGCTimeStamps -verbose:gc -XX:+PrintGCDetails -XX:NewSize=2048m -XX:MaxNewSize=2048m -Xms4096m -Xmx4096m -jar $HOME/dist/genetic/genetic.jar
