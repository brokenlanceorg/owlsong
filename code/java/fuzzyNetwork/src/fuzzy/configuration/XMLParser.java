/**
 * Notes: DefaultHandler is a SAX helper class that implements the SAX
 * ContentHandler interface by providing no-op methods.  This class
 * overrides some of the methods by extending DefaultHandler.  This program
 * turns on namespace processing and uses SAX2 interfaces to process XML
 * documents which may or may not be using namespaces.
 * 
 *  This class (and associated configuration objects) shouldn't really
 *  be a part of another separate project, but rather made a package
 *  in the parent project so that it can be customized for each project.
 * 
 * 
 */

package fuzzy.configuration;

import org.xml.sax.helpers.*;
import org.xml.sax.*;

import javax.xml.parsers.*;
import java.util.*;
import java.io.*;


/**
 * Notes: DefaultHandler is a SAX helper class that implements the SAX
 * ContentHandler interface by providing no-op methods.  This class
 * overrides some of the methods by extending DefaultHandler.  This program
 * turns on namespace processing and uses SAX2 interfaces to process XML
 * documents which may or may not be using namespaces.
 * 
 *  This class (and associated configuration objects) shouldn't really
 *  be a part of another separate project, but rather made a package
 *  in the parent project so that it can be customized for each project.
 * 
 * 
 */
public class XMLParser
{

   /**
    * This variable is re-initialized for each document since
    * we have made our assumption (see above).
    */
   FuzzyNetworkConfiguration _record;

   /**
    * This variable is re-initialized for each element since
    * we have made our assumption (see above).
    */
   LayerConfiguration _layer;
   
   /**
    * Holds the current data string from the parser.
    */
   String _currentDataString;

   private XMLReader _xmlReader;

   /**
    *
    */
   public XMLParser()
   {
      try
      {
         // Create a JAXP SAXParserFactory and configure it
         SAXParserFactory spf = SAXParserFactory.newInstance();

         // Set namespaceAware to true to get a parser that corresponds to
         // the default SAX2 namespace feature setting.  This is necessary
         // because the default value from JAXP 1.0 was defined to be false.
         spf.setNamespaceAware( true );

         // Create a JAXP SAXParser
         SAXParser saxParser = spf.newSAXParser();

         // Get the encapsulated SAX XMLReader
         _xmlReader = saxParser.getXMLReader();

         // Set the ContentHandler of the XMLReader
         _xmlReader.setContentHandler( new MyParser() );

         // Set an ErrorHandler before parsing
         _xmlReader.setErrorHandler( new MyErrorHandler( System.err ) );
      }
      catch( Exception e )
      {
         System.err.println( "Caught exception: " + e );
      }

   }

   /**
    * The single entry point for this class.
    * This will parse the xml string and return an object
    * representing the values in the XML message.
    */
   public FuzzyNetworkConfiguration parseXML( String xml )
   {
      try
      {
         InputStream inputStream = 
            Thread.currentThread().getContextClassLoader().getResourceAsStream( xml + ".xml" );
         InputSource src = new InputSource( inputStream );
         _xmlReader.parse( src );
      }
      catch( IOException e )
      {
         System.err.println( "Caught exception during parsing: " + e );
      }
      catch( SAXException e )
      {
         System.err.println( "Caught exception during parsing: " + e );
      }

      return _record;
   }

   /**
    * The inner class to handle the parsing.
    */
   private class MyParser extends DefaultHandler
   {
      /**
       *  This method is called once at the begining.
       */
      public void startDocument() throws SAXException
      {
         _record = new FuzzyNetworkConfiguration();
      }

      /**
       *  This method is called once at the end of the document.
       */
      public void endDocument() throws SAXException
      {
      }

      /**
       *  This method is called for each element in a document.
       */
      public void startElement( String nameSpaceURI, 
                                String localName, 
                                String qName, 
                                Attributes attributes ) 
         throws SAXException
      {
         if( "layer-configuration".equals( localName ) )
         {
            _layer = new LayerConfiguration();
            _record.addLayer( _layer );
         }
      }

      /**
       *  This method is called for each element in a document.
       */
      public void endElement( String nameSpaceURI, 
                              String localName, 
                              String qName ) 
         throws SAXException
      {
         if( "number-of-neurons".equals( localName ) )
         {
            _layer.setNumberOfNeurons( _currentDataString );
         }
         else if( "number-of-fuzzy-sets".equals( localName ) )
         {
            _layer.setNumberOfFuzzySets( _currentDataString );
         }
         else if( "data-location".equals( localName ) )
         {
            _record.setDataLocation( _currentDataString );
         }
         else if( "network-parameters".equals( localName ) )
         {
            StringTokenizer tok = new StringTokenizer( _currentDataString, "," );
            ArrayList<Double> params = new ArrayList<Double>();
            while( tok.hasMoreTokens() )
            {
               try {
                  Double temp = Double.parseDouble( tok.nextToken() );
System.out.println( "Setting a param to: " + temp );
                  params.add( temp );
//                  params.add( Double.parseDouble( tok.nextToken() ) );
               } catch( NumberFormatException e ) {}
            }
            _record.setParameters( params );
         }
      }

      /**
       *  This method is called for each element in a document.
       */
      public void characters( char[] chars, int start, int length )
      {
         _currentDataString = new String( chars, start, length );
         _currentDataString.trim();
      }
   }

   /**
    * This is the error handler class
    */
   private static class MyErrorHandler implements ErrorHandler 
   {
      /** Error handler output goes here */
      private PrintStream _outstream;

      /**
       * Constructor.
       */
      MyErrorHandler( PrintStream out ) 
      {
          _outstream = out;
      }

      /**
       * Returns a string describing parse exception details
       */
      private String getParseExceptionInfo( SAXParseException spe ) 
      {
         String systemId = spe.getSystemId();

         if (systemId == null) 
         {
             systemId = "null";
         }

         String info = "URI=" + systemId + " Line=" + spe.getLineNumber() + ": " + spe.getMessage();

         return info;
      }

      /**
       * 
       */
      public void warning( SAXParseException spe ) throws SAXException 
      {
         _outstream.println( "Warning: " + getParseExceptionInfo( spe ) );
      }
      
      /**
       *
       */
      public void error( SAXParseException spe ) throws SAXException 
      {
         String message = "Error: " + getParseExceptionInfo( spe );
         throw new SAXException( message );
      }

      /**
       * 
       */
      public void fatalError( SAXParseException spe ) throws SAXException 
      {
         String message = "Fatal Error: " + getParseExceptionInfo( spe );
         throw new SAXException( message );
      }
   }

   /**
    * Main method for testing.
    */
   public static void main( String[] args )
   {
      XMLParser inst = new XMLParser();
      FuzzyNetworkConfiguration config = inst.parseXML( "config" );

      ArrayList<LayerConfiguration> layers = config.getLayerConfigurations();
      for( LayerConfiguration layer : layers )
      {
         System.out.println( "A layer is: " );
         System.out.println( "  number neurons is: " + layer.getNumberOfNeurons() );
         System.out.println( "  number sets is: " + layer.getNumberOfFuzzySets() );
      }
   }
  
}
