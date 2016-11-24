#!/usr/bin/perl

our @_pnrs, @_lines, @_ordered;
our %_formats;

sub readLogFiles
{
   my @_files = glob( "pnrs.txt" );
   #my @_files = glob( "p.txt" );
   foreach( @_files )
   {
      my $_fileName = $_;
      open( THE_FILE, "<$_fileName" ) or die( "Unable to open file! $!" );
      while( <THE_FILE> )
      {
         chomp( $_ );
         push( @_pnrs, $_ );
      }
      close( THE_FILE );
   }

   my @_files = glob( "*.log" );
   #my @_files = glob( "i.log" );
   foreach( @_files )
   {
      my $_fileName = $_;
      open( THE_FILE, "<$_fileName" ) or die( "Unable to open file! $!" );
      while( <THE_FILE> )
      {
         handleLogLine( $_ );
      }
      close( THE_FILE );
   }
}

sub handleLogLine
{
   my $line = $_;

   if( $line =~ m/SabreSessionImpl - /g )
   {
      chomp( $line );
      push( @_lines, $line );
   }
}

sub parseData
{
   foreach $pnr ( @_pnrs )
   {
      foreach $line ( @_lines )
      {
         if( $line =~ m/.*$pnr.*/g )
         {
            push( @_ordered, $line );
         }
      }
   }
}

sub gatherStats
{
   my $time   = 0;
   my $state  = 0;
   my $format = "";
   foreach $line ( @_ordered )
   {
      if( $state == 1 )
      {
         $state = 0;
         $time  = 0;
         $format = ( split( " - ", $line ) )[ 1 ];
      }
      if( $line =~ m/SABRE COMMAND START/g )
      {
         $state = 1;
         if( defined( $_formats{ $format } ) )
         {
            push( @{ $_formats{ $format } },  $time );
            #print( "2 Will push time value: " . $time . "\n" );
            #print( "2 Will push format value: " . $format . "\n" );
            ##foreach( keys( %_formats ) )
            #{
            #foreach( @{ $_formats{ $_ } } )
            #{
            #print( "time value: " . $_ . "\n" );
            #}
            #}
         }
         else
         {
            #print( "Will push time value: " . $time . "\n" );
            #print( "Will push format value: " . $format . "\n" );
            $_formats{ $format } = [ $time ];
         }
      }
      elsif( $line =~ m/Sabre response time/g )
      {
         my $t = ( split( "time: ", $line ) )[ 1 ];
         $time += $t;
      }
   }

   foreach( keys( %_formats ) )
   {
      #my @temp = $_formats{ $_ };
      print( "The format is: " . $_ . "\n" );
      foreach( @{ $_formats{ $_ } } )
      {
         print( "time value: " . $_ . "\n" );
      }
   }
}

print( "Will read the log files.\n" );
readLogFiles();
print( "Will parse out the log files.\n" );
parseData();
print( "Will gather stats from the log files.\n" );
gatherStats();
