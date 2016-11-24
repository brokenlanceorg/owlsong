#!/usr/bin/ruby
# 
# This file contains some of the more common examples of the
# syntactical structure of a typical Ruby program.
#

require 'yaml'

=begin
  Note that commands can be executed on the command line as:  
     ruby -e 'print "Hello Ruby!\n"'
=end
#File.open( 'test.yaml' ) { |yf| YAML::load( yf ) };
yobj = nil;
File.open( 'test.yaml' ) { |yf| yobj = YAML::load( yf ) };
#puts 'cell is: ' + yobj[ 'cell' ];
#puts 'cluster is: ' + yobj[ 'cluster' ];
#puts 'name is: ' + yobj[ 'name' ];
#puts 'node is: ' + yobj[ 'node' ];
