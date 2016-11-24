#!/usr/bin/perl -w

# This will print colors to the screen when tailing or cating
# In less the colors can be viewed by running it as: less -R
# CLEAR, RESET, BOLD, DARK, UNDERLINE, UNDERSCORE, BLINK, REVERSE, CONCEALED,
# BLACK, RED, GREEN, YELLOW, BLUE, MAGENTA, CYAN, WHITE, ON_BLACK, ON_RED,
# ON_GREEN, ON_YELLOW, ON_BLUE, ON_MAGENTA, ON_CYAN, ON_WHITE

use strict;
use Term::ANSIColor qw(:constants);

my %target = ();

while( my $arg = shift )
{
   my $color = shift;
   $target{ $arg } = eval( $color );
}

my $reset = RESET;

while( <> )
{
   foreach my $x ( keys( %target ) )
   {
      s/($x)/$target{$x}$1$reset/g;
   }
   print;
}
