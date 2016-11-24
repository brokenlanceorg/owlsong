#!/usr/bin/perl

my $pid;
my @children;
for( 1..10 )
{
   print( "Entering for loop\n" );
   $pid = fork();
   print( "Forked: " . $pid . "\n" );

   if( $pid )
   {
      push( @children, $pid );
   }
   elsif( $pid == 0 )
   {
      print( "A child, will test: " . $pid . " \n" );
      `/home/custtech/bin/test-ws.sh /home/custtech/req/pseat-sec3.txt 60`;
      exit( 0 );
   }
}

foreach( @children )
{
   print( "waiting for child of: " . $_ . "\n" );
   waitpid( $_, 0 );
}
