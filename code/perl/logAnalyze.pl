#!/usr/local/bin/perl -w

use strict;

sub printUsage
{
   print( "\nThis script will parse output files generated by\n" );
   print( "the PerformanceAnalysis tool run with the -a option.\n" );
   print( "These output files will contain entries such as:\n" );
   print( "\nEndPNRRequest\n" );
   print( "GUI Start Time: 2004-06-26 00:02:11, End Time: 2004-06-26 00:02:13\n" );
   print( "MT Timings: (2.333)  -  Includes Transit Time: (0.353) and MT Total: (1.980) \n" );
   print( "MT Total is  -  MT Business Logic: (0.307) Sabre: (6)@(1.673)\n" );
   print( "GUI TIMINGS:  UpdatePage: (0.100) \n" );
   print( "NO MT SERVER TRANSACTIONS FOUND\n" );
   print( "\nThis script will parse this data and extract the Transit Time\n" );
   print( "to compute the average and max transit times for each hour, and\n" );
   print( "it will also count the number of transactions over 5 seconds.\n" );
   print( "The result of these calculations will be written to a comma-delimited\n" );
   print( "file called hourlyData.csv\n" );
   print( "\nUsage:\n" );
   print( "\nlogAnal.pl <file_name1> <file_name2> ... <file_nameN>\n\n" );
}

sub readFiles
{
   my ( $cCount, $rCount, @files, $_fileName, $time, $hour, $cTime, %cTimeHash, %cCountHash, $rTime, %rTimeHash, %rCountHash );
   @files = glob( "*.log" );
   foreach( @files )
   {
      $_fileName = $_;
      open( THE_FILE, "<$_fileName" ) or die( "Unable to open the file!!!! $!" );
      while( <THE_FILE> )
      {
         if( "$_" =~ m/^\*\*\*\([0-9]/ )
         {
            $time = (split( " ", $_ ))[2];
            $hour = (split( ":", $time ))[0];
         }
         if( "$_" =~ m/create time:/ )
         {
            $cTime = (split( " ", $_ ))[3];
            $cCount = $cCount + 1;
            $cTimeHash{ $hour } += $cTime;
            $cCountHash{ $hour } += 1;
         }
         if( "$_" =~ m/remove time:/ )
         {
            $rTime = (split( " ", $_ ))[3];
            $rCount = $rCount + 1;
            $rTimeHash{ $hour } += $rTime;
            $rCountHash{ $hour } += 1;
         }
         if( defined( $hour ) && defined( $cTime ) && $hour < 5 && $cTime > 900 )
         {
            print( $_fileName . " ==> " . $_ . "\n" );
         }
      }
      close( THE_FILE );
   }

   open( OUTPUT_FILE, ">hourlyData.csv" ) or die( "Unable to open output file!\n" );
   print( OUTPUT_FILE "Hour, Average Transmit Time, Max Transmit Time, Count of Transactions Over 5 sec., Transaction Name \n" );

   foreach( sort( keys( %cTimeHash ) ) )
   {
      if( $cCountHash{ $_ } != 0 )
      {
         $cTimeHash{ $_ } /= $cCountHash{ $_ };
      }

      if( $rCountHash{ $_ } != 0 )
      {
         $rTimeHash{ $_ } /= $rCountHash{ $_ };
      }

      print( OUTPUT_FILE "$_ , $cTimeHash{ $_ } , $rTimeHash{ $_ } \n" );
   }

   close( OUTPUT_FILE );

}

readFiles();
