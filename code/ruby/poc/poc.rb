#!/usr/bin/ruby

#
# Simple Proof Of Concept test script for Ruby
#

require 'soap/wsdlDriver'

wsdl_url = 'http://127.0.0.1:9876/poc?wsdl'

service = SOAP::WSDLDriverFactory.new( wsdl_url ).create_rpc_driver()
service.wiredump_file_base = 'soapmessages'

#result = service.performProofOfConcept()
result = service.performProofOfConcept( 'version' )

# This will tell us what are only new methods in this result
# (as opposed to "normal" object methods)
(result.methods() - SOAP::Mapping::Object.instance_methods()).sort().each do |method|
   puts( "An INSTANCE method: #{method}" )
end

# A list of all the methods
result.methods().sort().each do |method|
   puts( "A method: #{method}" )
end

puts( "The result is: #{result.return()}" )
