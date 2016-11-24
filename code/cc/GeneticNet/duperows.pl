#!c:\perl\bin\perl -w

open( THE_FILE, "<$ARGV[0]" ) or die( "Unable to open file.\n" );
open( OUT_FILE, ">$ARGV[0].out" ) or die( "Unable to open output file.\n" );
$first = 0;

while( <THE_FILE> )
{
   print( OUT_FILE "$_" );

   if( $_ =~ m/\/\//g )
   {
      next;
   }

   if( $first eq 1 )
   {
      print( OUT_FILE "$_" );
   }
   else
   {
      $first = 1;
   }
}

close( THE_FILE );
close( OUT_FILE );
