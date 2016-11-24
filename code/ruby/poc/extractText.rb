#!/usr/bin/ruby

require 'net/http'
require 'uri'

# 
# This class will extract the text from a typical web page
# and print to standard out.
#
class HTMLTextExtractor

   public

   #
   # Basic constructor
   #
   def initialize( url )
      if url =~ /^http/
         @url = URI.parse url
      else
         @file = url
      end
      @story = String.new
   end

   #
   # Returns the result of the text extraction.
   #
   def getText
      parseResource
      formatText
      return @story
   end

   private

   # 
   # This function will parse out each line.
   # 
   def handleLine line
      line.strip!
      if line.length > 0
         @story << " " << line
      end
   end

   #
   # This method will parse out the resource whether it be a file or a URL.
   #
   def parseResource
      if @url == nil
         ARGV.each do |arg|
            puts "Parsing arg: #{arg}"
            IO.foreach( arg ) { |line| handleLine line }
         end
      else
         @story = Net::HTTP.get( @url )
      end
   end

   #
   # This method will format the text removing unnecessary data elements.
   #
   def formatText
      @story.gsub!( /\n/, " " )
      @story.gsub!( /<script.*?script>/i, "" )
      @story.gsub!( /<style.*?style>/i, "" )
      @story.gsub!( /<!--.*?-->/, "" )
      @story.gsub!( /<[^>]+>/, "" )
      @story.gsub!( / {2,}/, " " )
      @story.gsub!( /&amp;/, "&" )
      @story.gsub!( /&mdash;/, "--" )
      @story.gsub!( /&ndash;/, "-" )
      @story.gsub!( /&nbsp;/, " " )
      @story.gsub!( /&middot;/, " " )
      @story.gsub!( /&copy;/, "@" )
      @story.gsub!( /&raquo;/, ">>" )
      @story.gsub!( /&laquo;/, "<<" )
      @story.gsub!( /&ls;/, "<" )
      @story.gsub!( /&rsaquo;/, ">" )
      @story.gsub!( /&lsaquo;/, "<" )
      @story.gsub!( /&gr;/, ">" )
      @story.gsub!( /&quot;/, '"' )
      @story.gsub!( /&iexcl;/, '!' )
      @story.gsub!( /&cent;/, '$' )
      @story.gsub!( /&pound;/, '$' )
      @story.gsub!( /&curren;/, '$' )
      @story.gsub!( /&brvbar;/, '|' )
      @story.gsub!( /&reg;/, '@' )
      @story.gsub!( /&acute;/, '`' )
      @story.gsub!( /&yen;/, '$' )
      @story.gsub!( /&sect;/, '#' )
      @story.gsub!( /&uml;/, '.' )
      @story.gsub!( /&ordf;/, '.' )
      @story.gsub!( /&not;/, '.' )
      @story.gsub!( /&shy;/, '.' )
      @story.gsub!( /&macr;/, '.' )
      @story.gsub!( /&deg;/, '.' )
      @story.gsub!( /&plusmn;/, '.' )
      @story.gsub!( /&sup2;/, '.' )
      @story.gsub!( /&sup3;/, '.' )
      @story.gsub!( /&acute;/, '.' )
      @story.gsub!( /&micro;/, '.' )
      @story.gsub!( /&para;/, '.' )
      @story.gsub!( /&middot;/, '.' )
      @story.gsub!( /&cedil;/, '.' )
      @story.gsub!( /&sup1;/, '.' )
      @story.gsub!( /&ordm;/, '.' )
      @story.gsub!( /&raquo;/, '.' )
      @story.gsub!( /&frac14;/, '.' )
      @story.gsub!( /&frac12;/, '.' )
      @story.gsub!( /&frac34;/, '.' )
      @story.gsub!( /&iquest;/, '.' )
      @story.gsub!( /&Agrave;/, '.' )
      @story.gsub!( /&Aacute;/, '.' )
      @story.gsub!( /&Acirc;/, '.' )
      @story.gsub!( /&Atilde;/, '.' )
      @story.gsub!( /&Auml;/, '.' )
      @story.gsub!( /&Aring;/, '.' )
      @story.gsub!( /&AElig;/, '.' )
      @story.gsub!( /&Ccedil;/, '.' )
      @story.gsub!( /&Egrave;/, '.' )
      @story.gsub!( /&Eacute;/, '.' )
      @story.gsub!( /&Ecirc;/, '.' )
      @story.gsub!( /&Euml;/, '.' )
      @story.gsub!( /&Igrave;/, '.' )
      @story.gsub!( /&Iacute;/, '.' )
      @story.gsub!( /&Icirc;/, '.' )
      @story.gsub!( /&Iuml;/, '.' )
      @story.gsub!( /&ETH;/, '.' )
      @story.gsub!( /&Ntilde;/, '.' )
      @story.gsub!( /&Ograve;/, '.' )
      @story.gsub!( /&Oacute;/, '.' )
      @story.gsub!( /&Ocirc;/, '.' )
      @story.gsub!( /&Otilde;/, '.' )
      @story.gsub!( /&Ouml;/, '.' )
      @story.gsub!( /&times;/, '.' )
      @story.gsub!( /&Oslash;/, '.' )
      @story.gsub!( /&Ugrave;/, '.' )
      @story.gsub!( /&Uacute;/, '.' )
      @story.gsub!( /&Ucirc;/, '.' )
      @story.gsub!( /&Uuml;/, '.' )
      @story.gsub!( /&Yacute;/, '.' )
      @story.gsub!( /&THORN;/, '.' )
      @story.gsub!( /&szlig;/, '.' )
      @story.gsub!( /&agrave;/, '.' )
      @story.gsub!( /&aacute;/, '.' )
      @story.gsub!( /&acirc;/, '.' )
      @story.gsub!( /&atilde;/, '.' )
      @story.gsub!( /&auml;/, '.' )
      @story.gsub!( /&aring;/, '.' )
      @story.gsub!( /&aelig;/, '.' )
      @story.gsub!( /&ccedil;/, '.' )
      @story.gsub!( /&egrave;/, '.' )
      @story.gsub!( /&eacute;/, '.' )
      @story.gsub!( /&ecirc;/, '.' )
      @story.gsub!( /&euml;/, '.' )
      @story.gsub!( /&igrave;/, '.' )
      @story.gsub!( /&iacute;/, '.' )
      @story.gsub!( /&icirc;/, '.' )
      @story.gsub!( /&iuml;/, '.' )
      @story.gsub!( /&eth;/, '.' )
      @story.gsub!( /&ntilde;/, '.' )
      @story.gsub!( /&ograve;/, '.' )
      @story.gsub!( /&oacute;/, '.' )
      @story.gsub!( /&ocirc;/, '.' )
      @story.gsub!( /&otilde;/, '.' )
      @story.gsub!( /&ouml;/, '.' )
      @story.gsub!( /&divide;/, '.' )
      @story.gsub!( /&oslash;/, '.' )
      @story.gsub!( /&ugrave;/, '.' )
      @story.gsub!( /&uacute;/, '.' )
      @story.gsub!( /&ucirc;/, '.' )
      @story.gsub!( /&uuml;/, '.' )
      @story.gsub!( /&yacute;/, '.' )
      @story.gsub!( /&thorn;/, '.' )
      @story.gsub!( /&yuml;/, '.' )
   end

end

#
# Build the instance to parse the page
#
#et = HTMLTextExtractor.new "http://finance.yahoo.com/q/h?s=KR+Headlines"
et = HTMLTextExtractor.new "http://www.thestreet.com/_yahoo/story/10871225/1/dividend-stocks-kroger-texas-instruments.html?cm_ven=YAHOO&cm_cat=FREE&cm_ite=NA"
#et = HTMLTextExtractor.new "http://www.basketball-reference.com/teams/DAL/2010.html"
#et = HTMLTextExtractor.new "http://blogs.barrons.com/stockstowatchtoday/2010/09/10/casey-stores-says-7-eleven-made-40-bid/?mod=yahoobarrons"
#et = HTMLTextExtractor.new "http://finance.yahoo.com/news/Senate-Republicans-say-theyll-apf-330227799.html?x=0&sec=topStories&pos=1&asset=&ccode="
#et = HTMLTextExtractor.new "http://news.google.com/news/section?pz=1&cf=all&ned=us&topic=b&ict=ln"

puts et.getText
