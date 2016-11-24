DROP TABLE pagecounts;
CREATE TABLE pagecounts ( date VARCHAR( 8 ), time VARCHAR( 6 ), project VARCHAR( 20 ), page VARCHAR( 500 ), count INT( 10 ), PRIMARY KEY ( date, time, project, page ) );
#Date:    Time:
#20130101-000000
#123456789012345
CREATE TABLE pages ( symbol VARCHAR( 8 ), page VARCHAR( 500 ), PRIMARY KEY ( symbol, page ) );
