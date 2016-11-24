#!/usr/bin/perl

use threads;
use threads::shared;

sub threadFunc
{
   print( "Hello Thread Func.\n" );
   print( "An argument is: " . $_[ 0 ] . "\n" );
}

print( "Hello World.\n" );

my $thr1 = threads->create( 'threadFunc', 'one' );
my $thr2 = threads->create( 'threadFunc', 'two' );
$thr1->join();
$thr2->join();

print( "After threads.\n" );
