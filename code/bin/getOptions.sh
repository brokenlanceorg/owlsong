#/bin/bash
file_name="QQQ-`date +%H-%m-%d-%y`.html";
curl "http://finance.yahoo.com/q/op?s=QQQ+Options" > /home/brandon/options/$file_name;
