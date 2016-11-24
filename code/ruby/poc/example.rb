#!/usr/bin/ruby
# 
# This file contains some of the more common examples of the
# syntactical structure of a typical Ruby program.
#

require 'date'
require 'yaml'

=begin
  Note that commands can be executed on the command line as:  
     ruby -e 'print "Hello Ruby!\n"'
=end

# Ruby constants begin with a capital letter
MYCONST = 3.14159
# Ruby is a dynamic type language
a, b, c = 100, 200, 300
d = [ 'first', 'second', 'third' ]

puts "what is my home dir?...#{ENV["HOME"]}"

res = (a.kind_of? Integer)
puts "is the variable a of type integer: #{res} the type is: #{a.class}"
puts "the type of d is: #{d.class}"
puts "the public methods of d is: #{d.public_methods}"
puts "here's c before: #{c}"
c = c.to_f
puts "here's c after: #{c}"
puts "here's c in binary: #{c.to_i.to_s(2)}"

=begin
   4 Types of Scope in Ruby:
   -----------------------------------------
   Name Begins With    Variable Scope
   _________________________________________
   $                   A global variable
   @                   An instance variable
   [a-z] or _          A local variable
   [A-Z]               A constant
   @@                  A class variable
=end
puts "what is b's scope type: #{defined? c}"

=begin
   Here are some useful global variables that are pre-defined:
   ------------------------------------------------------------
   Variable Name	Variable Value
   ____________________________________________________________
   $@              The location of latest error
   $_              The string last read by gets
   $.              The line number last read by interpreter
   $&              The string last matched by regexp
   $~              The last regexp match, as an array of subexpressions
   $n              The nth subexpression in the last match (same as $~[n])
   $=              The case-insensitivity flag
   $/              The input record separator
   $\              The output record separator
   $0              The name of the ruby script file currently executing
   $*              The command line arguments used to invoke the script
   $$              The Ruby interpreter's process ID
   $?              The exit status of last executed child process
=end
puts "an integer of 10.54 is: #{Integer( 10.54 )}"
puts "an integer of 0x3FAB is: #{Integer( 0x3FAB )}"
puts "the value of 'e' in ASCII is: #{Integer( ?e )}"

def displayStrings( *args )
   args.each { |str| puts str }
end
alias ds displayStrings
ds( "first", "second", "third" )

ten = (1..10).to_a
puts "The ten element inclusive array:"
puts ten
nine = (1...10).to_a
puts "The nine element exclusive array:"
puts nine

words = 'cab'..'car'
puts "the words are:"
words.each { |word| puts "a word: " + word }
puts "the min value is: #{words.min}"
puts "the max value is: #{words.max}"
puts "does it include 'can'? #{words.include? 'can'}"

res = (1..20) === 15
puts "is 15 between 1 and 20: #{res}"
# ranges are great for case statements:
score = 70
res = case score
      when 0..40: "Fail"
      when 41..60: "Pass"
      when 61..70: "Pass with Merit"
      when 71..100: "Pass with Distinction"
      else "Invalid score"
      end
puts "the result score is: #{res}"

days_of_week = Array.new
puts "is it empty? : #{days_of_week.empty?}"
days_of_week = Array.new 7
puts "is it empty now? : #{days_of_week.empty?}"
puts days_of_week
days_of_week = Array.new 5, "today"
puts days_of_week
days_of_week = [ 'mon', 'tue', 'wed', 'thr', 'fri', 'sat', 'sun' ]
puts "the size is: #{days_of_week.size}"
puts "the first element is: #{days_of_week[0]} or #{days_of_week.at(0)} or #{days_of_week.first}"
puts "the last element is: #{days_of_week[6]} or #{days_of_week.at(-1)} or #{days_of_week.last}"
puts "what is the index of 'wed': #{days_of_week.index 'wed' }"
puts "what is the last index of 'wed': #{days_of_week.rindex 'wed' }"
puts "tue through thr is: #{days_of_week[1, 3]}"
puts "tue through thr is: #{days_of_week[1..3]}"
puts "tue through thr is: #{days_of_week.slice 1..3}"
orig = days_of_week.slice(0..3) + days_of_week.slice(4..5)
orig << 'sun' << 'oth'
puts orig
puts "now by concat..."
orig = days_of_week.slice(0..3) .concat days_of_week.slice(4..6)
puts orig

=begin
   Ruby also offers powerful conjunctions on arrays:
   Operator    Description
   -           Difference - Returns a new array that is a copy of the first array 
                            with any items that also appear in second array removed.
   &           Intersection - Creates a new array from two existing arrays 
                              containing only elements that are common to both arrays. Duplicates are removed.
   |           Union - Concatenates two arrays. Duplicates are removed.
=end

puts "the duped array"
mod = days_of_week .concat days_of_week
puts mod
puts "now with uniq or uniq!"
puts mod.uniq!
mod.push 'red', 'green'
puts mod
puts "pop: #{mod.pop}"
puts "pop: #{mod.pop}"
puts "now the modified array is:"
puts mod

=begin
   Ruby arrays may be compared using the ==, <=> and eql? methods.
   The == method returns true if two arrays contain the same number of 
   elements and the same contents for each corresponding element.

   The eql? method is similar to the == method with the exception that the values 
   in corresponding elements are of the same value type.

   Finally, the <=> method (also known as the "spaceship" method) compares 
   two arrays and returns 0 if the arrays are equal, -1 one if the elements are 
   less than those in the other array, and 1 if they are greater. 
=end
mod[1..2] = 'red', 'green'
mod.insert 1, 'yellow'
puts "mod is now by range modification:"
puts mod
mod.delete 'yellow'
mod.delete_at 1
mod.sort!
puts "mod is now by delete modification:"
puts mod

puts "2^10 <=> s^9: #{2 ** 10 <=> 2 ** 9}"
=begin
   Combined Operator   Equivalent
   ~                   Bitwise NOT (Complement)
   |                   Bitwise OR
   &                   Bitwise AND
   ^                   Bitwise Exclusive OR
   <<                  Bitwise Shift Left
   >>                  Bitwise Shift Right
=end

puts "some math constants: #{Math.constants}"
puts "e math constant: #{Math::E}"
puts "pi math constant: #{Math::PI}"
puts "is 2 > 3 or 3 > 2?: #{2 > 3 or 3 > 2}"
puts "is 2 < 3 and 3 > 2?: #{2 < 3 and 3 > 2}"

class BankAccount
   def initialize
      @number = rand
      @name = "John Smith"
      @@interestRate = 54.8353
   end
   def getNumber
      return @number
   end
   def getInterestRate
      return @@interestRate
   end
end

accnt = BankAccount.new
puts "the account number is: #{accnt.getNumber}"
puts "the interest rate is: #{accnt.getInterestRate}"

class CheckingBankAccount < BankAccount
   def getNumber
      return @number * Math::PI
   end
   def to_s
      return @number.to_s + " the superclass: " + super
   end
end

checking = CheckingBankAccount.new
puts "the account number on checking is: #{checking.getNumber}"
puts "the interest rate on checking is: #{checking.getInterestRate}"
puts "the checking object is: #{checking}"

if 10 < 20 then
   print "10 is less than 20...type 1\n"
end

if 10 < 20 
   print "10 is less than 20...type 2\n"
end

print "10 is less than 20...type 3\n" if 10 < 20
if 10 < 20 : print "10 is less than 20...type 4\n" end

if checking.getNumber < accnt.getNumber
   puts "checking is less than account"
elsif checking.getNumber > accnt.getNumber
   puts "checking is greater than account"
else
   puts "must be equal"
end

checking.getInterestRate == accnt.getInterestRate ? (puts "interest rate is the same") : (puts "interest rate not the same")

i = 0
while 1..20 === i
   i += 1
   puts "still true #{i}"
   break if i == 19
end

i = 0
puts ((10..20) === i)
until (10..20) === i
   i += 1
   puts "still not true #{i}"
end

unless i >= 10
   puts "i is not greater than or equal to 10"
else
   puts "i is greater than or equal to 10"
end

puts "here is for looping 1..."
for j in 10..20
   puts "j is now: #{j}"
end

puts "here is for looping 2..."
j.times { |k| puts "k is now: #{k}"}

puts "here is for looping 3..."
j.upto(30) { |k| puts "k is now: #{k}"}

puts "here is for looping 4..."
j.downto(3) { |k| puts "k is now: #{k}"}

str1 = String.new "string one"
puts str1
str2 = "string two\nand another"
puts str2
str3 = 'string three\nand another'
puts str3

# the % char is a delimiter-definer: 
# note that %Q is double quotes while %q is single quotes and %x is the back-quote
str4 = %(here is "my" string for the 'uninitiated')
puts str4

str5 = <<ENDDOC

This is a long document of text, called, in Ruby, 
   a "Here Document." To create a 'here document', you
set the text equal to the << operator followed by the 
end delimiter.

   and then at the end, you place the end delimiter
ENDDOC

puts str5

unless str5.empty?
   str5.each { |char| puts "a character in the string: #{char}" }
end

str6 = "strings " + "can be " "concatened in " << "various ".concat( "ways in Ruby" )
puts str6

# the freeze method makes a string immutable
puts "is the string 'concat' found in the above string: #{str6[ "concat" ]}"
puts "is the string 'apples' found in the above string: #{str6[ "apples" ]}"
puts "position 6 of the string is: #{str6[ 6 ].chr}"
puts "ASCII code of position 6 of the string is: #{str6[ 6 ]}"
puts "section 2, 12 of the string is: #{str6[ 2, 12 ]}"
puts "section 12, 22 of the string is: #{str6[ 12..22 ]}"
puts "what is the position of Ruby: #{str6.index "Ruby" }"

str6[ "Ruby" ] = "Amazing Ruby"
puts "string replacement 1: #{str6}"
str6[ str6.index( " ways" ) ] = " amazing "
puts "string replacement 2: #{str6}"

# sub will do first substition, while gsub does all
str6.gsub! "amazing", "incredible"
puts str6

puts str6 * 3

str7 = "strings are easy to manipulate in Ruby!"
str7.insert 0, "Character "
puts str7

# The chop method removes the last character and comp removes $/, which is \n by default, from the string
puts str7.split( / [to]+ /)
# some useful methods: reverse, capitalize, downcase, upcase, swapcase, to_i, to_f, to_sym

puts "the current directory is: #{Dir.pwd}"
Dir.chdir ".." # can also use other things like mkdir, glob, foreach
puts "the current directory is now: #{Dir.pwd}"
puts "the contents of that directory is: "
(Dir.entries ".").each { |i| puts i }
# or can do 
puts "the contents of that directory is also: "
Dir.foreach(".") { |i| puts i }

=begin
   To create a new file:       File.new "<file name>", "<mode>"
   To open an existing file:   File.open "<file name>", "<mode>"
   To rename an existing file: File.rename "<file name>", "<new file name>"
   To delete an existing file: File.rename "<file name>"
   Various methods exist such as readable, writable, size, ctime, mtime, directory?, file?, exists?, ftype
   Mode  Description
   r     Read only access. Pointer is positioned at start of file.
   r+    Read and write access. Pointer is positioned at start of file.
   w     Write only access. Pointer is positioned at start of file.
   w+    Read and write access. Pointer is positioned at start of file.
   a     Write only access. Pointer is positioned at end of file.
   a+    Read and write access. Pointer is positioned at end of file.
   b     Binary File Mode. Used in conjunction with the above modes. Windows/DOS only.
=end
fileName = "iRuby/testdata.txt"
begin
   myfile = File.open fileName, "r"
   myfile.each { |line| puts line}
   myfile.close
rescue NameError => e
   puts "caught a name error: #{e}"
   raise # will re-throw $!
rescue StandardError => e
   puts "caught an error: #{e}"
   fileName = "Ruby/testdata.txt"
   retry
ensure
   myfile.close unless myfile.nil? or myfile.closed?
end

# Here are other ways of opening and reading a file:
myfile = File.open fileName, "r"
myfile.each_line { |line| puts "A line: #{line}"}
# each_line takes a param of a delimiter:
myfile = File.open fileName, "r"
myfile.each_line( " " ) { |line| puts "A line (2): #{line.chomp}"}
# or how about this:
IO.foreach( fileName ) { |line| puts "A line (3): #{line}"}


mydate = Date.new
puts "the day is #{mydate.day}"
puts "the month is #{mydate.month}"
puts "the year is #{mydate.year}"
mydate = DateTime.now
puts "the day is #{mydate.day}"
puts "the month is #{mydate.month}"
puts "the year is #{mydate.year}"
bday = Date.new 2011, 8, 13
days = bday - mydate
puts "bday in #{days} days"
puts "or in #{days.to_i} days"
hours, mins, secs, frac = Date.day_fraction_to_time( days ) 
[3053, 46, 57, Rational(1057, 180000000)]
puts "bday in hours: #{hours} mins: #{mins} secs: #{secs} fractions: #{frac}"

# Ruby allows one to pass code blocks as parameters to methods, this allows one to write their own iterators
def callBlock
   yield 1
   yield 2
end
callBlock { |i| puts "Control is now in passed in block: #{i}" }

module Inject
   def inject n
      each do |value|
         n = yield n, value
      end
      n
   end

   # the parens here are required
   def sum( initial = 0 )
      inject( initial ) { |n, value| n + value }
   end
   # the parens here are required
   def product( initial = 1 )
      inject( initial ) { |n, value| n * value }
   end
end

class Array
   include Inject
end

class Range
   include Inject
end

puts "The sum of the array is: #{[ 1, 2, 3, 4, 5 ].sum}"
puts "The product of the array is: #{[ 1, 2, 3, 4, 5 ].product}"
puts "The sum of the range is: #{(1..5).sum}"
puts "The product of the range is: #{(1..5).product}"
puts "The sum of the alpha range is: #{('a'..'z').sum("alpha sum: ")}"

=begin
   All you have to do is write an iterator called each, which returns the elements of your collection in turn. 
   Mix in Enumerable, and suddenly your class supports things such as map, include?, and find_all?. 
   If the objects in your collection implement meaningful ordering semantics using the <=> method, 
   you'll also get min, max, and sort. 
=end

# load <filename>.rb -- loads file every time executed
# require <filename> -- loads only once
