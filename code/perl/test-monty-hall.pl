#!/usr/bin/perl

#
# In this perl script, we will test out the Monty Hall problem.
#
#
#
#

print( "In this game, there will be a number of closed doors.\n" );
print( "Behind one of the doors is a fancy dancy car, while behind\n" );
print( "all the others are just goats. The game show host will randomly\n" );
print( "place the car, then the contestant will randomly\n" );
print( "choose one of the doors which it believes the car is hidden behind.\n" );
print( "Then, the game show host will reveal all doors except \n" );
print( "the door the contestant chose and the door that hides the either the\n" );
print( "car (if the contestant didn't choose it) or a goat. Then, the contestant\n" );
print( "can either choose to switch doors, or stay with its initial choice.\n" );
print( "\n" );

print( "How many doors are there? " );
my $numberOfDoors = <STDIN>;
chomp( $numberOfDoors );

print( "How many games should be played? " );
my $numberOfGames = <STDIN>;
chomp( $numberOfGames );

my $numberOfSwitchWins = 0;
my $numberOfStationaryWins = 0;

for( $i = 0; $i < $numberOfGames; $i++ )
{
   print( "\nGame show host, which door are you hiding the car behind?\n" );
   my $randomCar = <STDIN>;
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "Contestant, which door do you choose?\n" );
   my $randomChoice = <STDIN>;
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );
   print( "\n" );

   print( "The game show host will now reveal all doors except the one the contestant chose and the car (if the contestant chose it) \nor a random door with a goat behind it.\n" );

   if( $randomCar == $randomChoice )
   {
      print( "The contestant chose right.\n" );
      print( "But they will switch and end up getting the goat...\n" );
      $numberOfStationaryWins++;
   }
   else
   {
      print( "The contestant chose wrong.\n" );
      print( "But they will switch to the door that now holds the car and win!!!\n" );
      $numberOfSwitchWins++;
   }

}

print( "\nThe probablity of winning via switching overall is: " . ( $numberOfSwitchWins / $numberOfGames ) . "\n" );
print( "The probablity of winning via staying overall is: " . ( $numberOfStationaryWins / $numberOfGames ) . "\n" );
