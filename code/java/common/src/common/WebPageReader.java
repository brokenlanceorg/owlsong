package common;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
import javax.xml.parsers.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

/**
 *
 */
public class WebPageReader implements Persistable
{

   // add to this the stock symbol a double-quote and >
//    private String         _realTimePrefix = "http://finance.yahoo.com/q/ecn?s=";
   private String         _realTimePrefix = "http://finance.yahoo.com/q?s=";
   private String         _pagePrefix = "http://finance.yahoo.com/q/op?s=";
   private String         _pricePrefix = "<span id=\"yfs_l10_";
   private String         _symbol;
   private URL            _url;
   private XMLReader      _xmlReader;
   protected StringBuffer _currentDataString;
   private double         _open;
   private double         _high;
   private double         _low;
   private double         _currentPrice;
   private double         _callPremium;
   private double         _ootmCallPremium;
   private double         _putPremium;
   private double         _payout;
   private double         _ootmPayout;
   private double         _strikePrice;
   private double         _ootmStrikePrice;
   private double         _last;
   private double         _bid;
   private double         _ask;
   private ArrayList< OptionDataItem > _ootmPuts;
   private ArrayList< OptionDataItem > _itmPuts;

   /**
    *
    */
   public WebPageReader()
   {
//       initialize();
   }

   /**
    *
    */
   public WebPageReader( String stock_name )
   {
      _symbol = stock_name;
//       try
//       {
//          _url = new URL( url );
//       }
//       catch( MalformedURLException e )
//       {
//          System.err.println( "Caught exception opening URL: " + e );
//       }
// //       initialize();
   }

   /**
    *
    */
   private void initialize()
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
    *
    */
   public double getCurrentPrice()
   {
      return _currentPrice;
   }

   /**
    *
    */
   public double getOpen()
   {
      return _open;
   }

   /**
    *
    */
   public double getHigh()
   {
      return _high;
   }

   /**
    *
    */
   public double getLow()
   {
      return _low;
   }

   /**
    *
    */
   private void parseRealTimeLine( String line )
   {
      line = line.substring( line.indexOf( "Last Trade:" ), line.indexOf( "Trade Time:" ) );
      Pattern p = Pattern.compile( ".*" + _symbol + "\"\\>([0-9]+\\.[0-9]+)<\\/span>.*" );
      Matcher m = p.matcher( line );

      if( m.matches() )
      {
         _currentPrice = Double.parseDouble( m.group( 1 ) );
      }
   }

   /**
    *
    */
   private void parseLine( String line )
   {
      Pattern p = Pattern.compile( ".*" + _pricePrefix + _symbol + "\"\\>" + "([0-9]+\\.[0-9]+)" + "<\\/span>.*" );
      Matcher m = p.matcher( line );

      if( m.matches() )
      {
         _currentPrice = Double.parseDouble( m.group( 1 ) );
      }

      // in the money options
      String[] lines = line.split( "<tr><td class=\"yfnc_h" );
      for( int i=0; i<lines.length; i++ )
      {
         String theLine = lines[ i ];

         p = Pattern.compile( ".*?c0+([1-9]+)0+\">([0-9]+\\.[0-9]+)<\\/span><\\/b><\\/td><td class=\"yfnc_h.*?" );
         m = p.matcher( theLine );

         if( m.matches() )
         {
            _callPremium = Double.parseDouble( m.group( 2 ) );
            p = Pattern.compile( ".*<a href.*>([0-9]+\\.[0-9]+)<\\/a><\\/b><\\/td><td class=\"yfnc_h.*" );
            m = p.matcher( theLine );

            if( m.matches() )
            {
               _strikePrice = Double.parseDouble( m.group( 1 ) );
            }
         }

         String[] l = theLine.split( "href" );
         for( int j=0; j<l.length; j++ )
         {
            String ll = l[ j ];
            p = Pattern.compile( ".*?>([0-9]+\\.[0-9]+)<\\/strong><\\/a><\\/td><td class=\"yfnc_tabledata1.*?" );
            m = p.matcher( ll );

            if( m.matches() && _ootmStrikePrice == 0 )
            {
               _ootmStrikePrice = Double.parseDouble( m.group( 1 ) );
            }

            p = Pattern.compile( ".*?>([0-9]+\\.[0-9]+)<\\/span><\\/b><\\/td><td class=\"yfnc_tabledata1.*?" );
            m = p.matcher( ll );

            if( m.matches() && _ootmCallPremium == 0 )
            {
               _ootmCallPremium = Double.parseDouble( m.group( 1 ) );
            }
         }
      }

      // out of the money options
      lines = line.split( "<tr><td class=\"yfnc_tabledata1" );
      for( int i=0; i<lines.length; i++ )
      {
         String theLine = lines[i];
         p = Pattern.compile( ".*" + _strikePrice + ".*" );
         m = p.matcher( theLine );
         if( m.matches() )
         {
            p = Pattern.compile( 
                  ".*?p0+([1-9]+)0+\">([0-9]+\\.[0-9]+)<\\/span><\\/b><\\/td><td class=\"yfnc_tabledata1.*?" );
            m = p.matcher( theLine );

            if( m.matches() )
            {
               _putPremium = Double.parseDouble( m.group( 2 ) );
            }
         }
         String ll = lines[ i ];
         p = Pattern.compile( ".*?>([0-9]+\\.[0-9]+)<\\/strong><\\/a><\\/td><td class=\"yfnc_tabledata1.*?" );
         m = p.matcher( ll );

         if( m.matches() && _ootmStrikePrice == 0 )
         {
            _ootmStrikePrice = Double.parseDouble( m.group( 1 ) );
         }
      }
   }

   /**
    *
    */
   public String getContent( String symbol, String month, String year )
   {
      try
      {
         _symbol = symbol;
         _url = new URL( _pagePrefix + _symbol + ((month == null) ? "" : "&m=" + year + "-" + month ) );
         BufferedReader reader = new BufferedReader( new InputStreamReader( _url.openStream() ) );
         String line = null;
         while( ( line = reader.readLine() ) != null )
         {
            parseLine( line );
         }
         reader.close();
      }
      catch( IOException e )
      {
         System.err.println( "Caught exception during parsing: " + e );
      }
//       catch( MalformedURLException e )
//       {
//          System.err.println( "Caught exception during parsing: " + e );
//       }

      int numberOfPuts = 0;
      int shares = 0;
      if( _currentPrice != 0 )
      {
         shares = (int) (10000 / _currentPrice);
         _payout = shares * _callPremium;
         _ootmPayout = shares * _ootmCallPremium;
      }

      if( _putPremium != 0 )
      {
         numberOfPuts = (int)( _payout / _putPremium / 100 );
      }

//       System.out.println( "symbol, price, strike, ootmStrike, shares, call premium, ootm call premium, " +
//                           "put premium, payout, ootm payout, put contracts" );
//       System.out.print( symbol + ", " );
//       System.out.print( _currentPrice + ", " );
//       System.out.print( _strikePrice + ", " );
//       System.out.print( _ootmStrikePrice + ", " );
//       System.out.print( shares + ", " );
//       System.out.print( _callPremium + ", " );
//       System.out.print( _ootmCallPremium + ", " );
//       System.out.print( _putPremium + ", " );
//       System.out.print( _payout + ", " );
//       System.out.print( _ootmPayout + ", " );
//       System.out.println( numberOfPuts );

      return null;
   }

   /**
    *
    */
   public String getContent()
   {
      try
      {
         _xmlReader.setFeature( "http://xml.org/sax/features/validation", false );
         _xmlReader.setProperty( "validation", new Boolean( false ) );
         _xmlReader.parse( new InputSource( new InputStreamReader( _url.openStream() ) ) );
      }
      catch( IOException e )
      {
         System.err.println( "Caught exception during parsing: " + e );
      }
      catch( SAXException e )
      {
         System.err.println( "Caught exception during parsing: " + e );
      }
      return null;
   }

   /**
    *
    */
   private double parseLastPrice( String line )
   {
      int rpos = line.indexOf( "Real Time" );
      int lpos = line.indexOf( "Last Trade" );
      double lastPrice = -1.0;

      // Parse out the real time quote if possible:
      if( rpos > -1 && lpos > -1 )
      {
         line = line.substring( rpos + 20, lpos );
         Pattern p = Pattern.compile( "^[^>]+>([0-9]+\\.[0-9]+)<.*" );
         Matcher m = p.matcher( line );
         if( m.matches() )
         {
            lastPrice = Double.parseDouble( m.group( 1 ) );
         }
      }
      // Otherwise, just parse out the last trade quote if possible:
      else if( lpos > -1 )
      {
         line = line.substring( lpos );
         Pattern p = Pattern.compile( "^.*?>([0-9]+\\.[0-9]+)<.*" );
         Matcher m = p.matcher( line );
         if( m.matches() )
         {
            lastPrice = Double.parseDouble( m.group( 1 ) );
         }
      }

      return lastPrice;
   }

   /**
    *
    */
   private static double parseLowPrice( String line )
   {
      int pos = line.indexOf( "Day's Range" );
      double lowPrice = -1.0;

      // Parse out the real time quote if possible:
      if( pos > -1 )
      {
         line = line.substring( pos + 70 );
         Pattern p = Pattern.compile( "^[^>]+>([0-9]+\\.[0-9]+)<.*" );
         Matcher m = p.matcher( line );
         if( m.matches() )
         {
            lowPrice = Double.parseDouble( m.group( 1 ) );
         }
      }

      return lowPrice;
   }

   /**
    *
    */
   private static double parseHighPrice( String line )
   {
      int pos = line.indexOf( "Day's Range" );
      double highPrice = -1.0;

      // Parse out the real time quote if possible:
      if( pos > -1 )
      {
         pos = line.indexOf( "> - <", pos );
         if( pos > -1 )
         {
            line = line.substring( pos + 20 );
            Pattern p = Pattern.compile( "^[^>]+>([0-9]+\\.[0-9]+)<.*" );
            Matcher m = p.matcher( line );
            if( m.matches() )
            {
               highPrice = Double.parseDouble( m.group( 1 ) );
            }
         }
      }

      return highPrice;
   }

   /**
    *
    */
   private static double parseOpenPrice( String line )
   {
      int pos = line.indexOf( ">Open:<" );
      double openPrice = -1.0;

      // Parse out the real time quote if possible:
      if( pos > -1 )
      {
         line = line.substring( pos + 30 );
         Pattern p = Pattern.compile( "^[^>]+>([0-9]+\\.[0-9]+)<.*" );
         Matcher m = p.matcher( line );
         if( m.matches() )
         {
            openPrice = Double.parseDouble( m.group( 1 ) );
         }
      }

      return openPrice;
   }

   /**
    *
    */
   public double getLastTrade( String symbol )
   {
      try
      {
         _symbol = symbol;
         _url = new URL( _realTimePrefix + _symbol );
         BufferedReader reader = new BufferedReader( new InputStreamReader( _url.openStream() ) );
         String page = "";
         String line = null;
         while( ( line = reader.readLine() ) != null )
         {
            page += line;
         }
         reader.close();
//          parseRealTimeLine( page );
         _currentPrice = parseLastPrice( page );
         _open = parseOpenPrice( page );
         _high = parseHighPrice( page );
         _low = parseLowPrice( page );
      }
      catch( IOException e )
      {
         System.err.println( _symbol + " -- Caught IO exception during parsing: " + e );
      }
      catch( Exception e )
      {
         System.err.println( _symbol + " -- Caught exception during parsing: " + e );
      }

      return getCurrentPrice();
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
         System.out.println( "Beginning the document..." );
      }

      /**
       *  This method is called once at the end of the document.
       */
      public void endDocument() throws SAXException
      {
         System.out.println( "Finished the document..." );
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
         if( "html".matches( localName + "/i" ) )
         {
            System.out.println( "Found the html element: " + localName );
         }
         else if( "head".matches( localName + "/i" ) )
         {
            System.out.println( "Found the head element: " + localName );
         }
         else if( "body".matches( localName + "/i" ) )
         {
            System.out.println( "Found the body element: " + localName );
         }
         _currentDataString = new StringBuffer();
      }

      /**
       *  This method is called for each element in a document.
       */
      public void endElement( String nameSpaceURI, 
                              String localName, 
                              String qName ) 
         throws SAXException
      {
         if( "html".matches( localName + "/i" ) )
         {
            System.out.println( "Ended the html element: " + localName );
         }
         else if( "head".matches( localName + "/i" ) )
         {
            System.out.println( "Ended the head element: " + localName );
         }
         else if( "body".matches( localName + "/i" ) )
         {
            System.out.println( "Ended the body element: " + localName );
         }

         System.out.println( "The current data string is: " + _currentDataString );
      }

      /**
       *  This method is called for each element in a document.
       */
      public void characters( char[] chars, int start, int length )
      {
         _currentDataString.append( new String( chars, start, length ) );
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
    *
    */
   public double getOOTMPayout()
   {
      return _ootmPayout;
   }

   /**
    *
    */
   public double getPayout()
   {
      return _payout;
   }

   /**
    *
    */
   public double getStrikePrice()
   {
      return _strikePrice;
   }

   /**
    *
    */
   public double getOOTMStrikePrice()
   {
      return _ootmStrikePrice;
   }

   /**
    *
    */
   public double getCallPremium()
   {
      return _callPremium;
   }

   /**
    *
    */
   public double getOOTMCallPremium()
   {
      return _ootmCallPremium;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public ArrayList< OptionDataItem > getOOTMPuts()
   {
      return _ootmPuts;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public ArrayList< OptionDataItem > getITMPuts()
   {
      return _itmPuts;
   }

   /**
    *
    */
   private void parseCurrentPrice( String line )
   {
      // Grab the data between these two markers:
      int p1 = line.indexOf( "At <" );
      int p2 = line.indexOf( "More On " );
      if( p1 > 0 && p2 > 0 )
      {
         line = line.substring( p1, p2 );
         Pattern p = Pattern.compile( "^.*>([0-9]+\\.[0-9]{2,2})<\\/span><.*" );
         Matcher m = p.matcher( line );
         if( m.matches() )
         {
            _currentPrice = Double.parseDouble( m.group( 1 ) );
//             System.out.println( "a current stock price: " + _currentPrice );
         }
      }
   }

   /**
    *
    */
   private void parsePutPage( String line )
   {
      parseCurrentPrice( line );
      // Grab the data between these two markers:
      int p1 = line.indexOf( "Put Options" );
      int p2 = line.indexOf( "Highlighted options" );
      if( p1 > 0 && p2 > 0 )
      {
         line = line.substring( p1, p2 );
      }

      _ootmPuts = new ArrayList< OptionDataItem >();
      _itmPuts = new ArrayList< OptionDataItem >();
      // Then, break the data into lines by the markup "<strong>":
      String[] lines = line.split( "<strong>" );
      for( int i=1; i<lines.length; i++ )
      {
//          System.out.println( "a line: " + lines[i] );
         boolean isOOTM = false;
         if( lines[i].indexOf( "yfnc_tabledata1" ) != -1 )
         {
            isOOTM = true;
         }
         OptionDataItem item = new OptionDataItem();

         // first, let's get the strike price
         Pattern p = Pattern.compile( "^([0-9]+\\.[0-9]{2,2}).*" );
         Matcher m = p.matcher( lines[i] );
         if( m.matches() )
         {
            item.setStrike( Double.parseDouble( m.group( 1 ) ) );
//             System.out.println( "a strike: " + item.getStrike() );
         }

         // next, let's get the symbol
         p1 = lines[i].indexOf( "href=\"/q?s=" );
         String l = lines[i].substring( p1 + 11 );
         p = Pattern.compile( "^([^\"]+).*" );
         m = p.matcher( l );
         if( m.matches() )
         {
            item.setSymbol( m.group( 1 ) );
//             System.out.println( "a symbol: " + item.getSymbol() );
         }

         // next, let's get the last price
         p = Pattern.compile( ".*([0-9]+\\.[0-9]{2,2})</span></b>.*" );
         m = p.matcher( l );
         if( m.matches() )
         {
            item.setLastPrice( Double.parseDouble( m.group( 1 ) ) );
//             System.out.println( "a last price: " + item.getLastPrice() );
         }

         // next, let's get the volume
         p1 = l.indexOf( "yfs_v" );
         l = l.substring( p1 );
         p1 = l.indexOf( ">" );
         l = l.substring( p1 + 1 );
         p = Pattern.compile( "^([0-9,]+)</span></td>.*" );
         m = p.matcher( l );
         if( m.matches() )
         {
            item.setVolume( Long.parseLong( m.group( 1 ).replaceAll( ",", "" ) ) );
//             System.out.println( "a volume: " + item.getVolume() );
         }

         // finally, let's get the open interest:
         p1 = l.indexOf( "right\">" );
         l = l.substring( p1 + 7 );
         p = Pattern.compile( "^([0-9,]+)</td>.*" );
         m = p.matcher( l );
         if( m.matches() )
         {
            item.setOpenInterest( Long.parseLong( m.group( 1 ).replaceAll( ",", "" ) ) );
//             System.out.println( "a open interest: " + item.getOpenInterest() );
         }

         // finally, let's compute the days to expire:
         GregorianCalendar cal = new GregorianCalendar();
         int largestDay = cal.getActualMaximum( cal.DAY_OF_MONTH );
         int currentDay = cal.get( cal.DAY_OF_MONTH );
         int currentMonth = cal.get( cal.MONTH ) + 1;
//          System.out.println( "largest day of month: " + largestDay );
//          System.out.println( "current day of month: " + currentDay );
//          System.out.println( "current month: " + currentMonth );
         l = item.getSymbol();
         p = Pattern.compile( "^[A-Z]+([0-9]{6,6})P[0-9]+$" );
         m = p.matcher( l );
         if( m.matches() )
         {
//             System.out.println( "a day to expire: " + m.group( 1 ) );
            int dte = Integer.parseInt( m.group( 1 ).substring( 4 ) );
            int month = Integer.parseInt( m.group( 1 ).substring( 2, 4 ) );
//             System.out.println( "a day to expire: " + dte );
//             System.out.println( "a month to expire: " + month );
            if( currentMonth == month )
            {
               item.setDaysToExpire( dte - currentDay );
            }
            else if( (currentMonth + 1) == month )
            {
               item.setDaysToExpire( (largestDay - currentDay) + dte );
            }
         }

         if( isOOTM )
         {
            _ootmPuts.add( 0, item );
         }
         else
         {
            _itmPuts.add( item );
         }
      }
      
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void parsePutOptions( String symbol )
   {
      try
      {
         _symbol = symbol;
         _url = new URL( _pagePrefix + _symbol + "+Options" );
         BufferedReader reader = new BufferedReader( new InputStreamReader( _url.openStream() ) );
         String page = "";
         String line = null;
         while( ( line = reader.readLine() ) != null )
         {
            page += line;
         }
         reader.close();
         parsePutPage( page );
      }
      catch( IOException e )
      {
         System.err.println( "Caught exception during parsing: " + e );
      }
   }

   public class OptionDataItem implements Serializable
   {
      private double _strike;
      private String _optionSymbol;
      private double _last;
      private long   _volume;
      private long   _openInterest;
      private int    _daysToExpire = -1;

      /**
       *
       * @param TYPE
       * @return TYPE
       */
      public String getStockSymbol()
      {
         return _symbol;
      }

      /**
       *
       * @param TYPE
       * @return TYPE
       */
      public double getStrike()
      {
         return _strike;
      }

      /**
       *
       * @param TYPE
       * @return TYPE
       */
      public void setStrike( double s )
      {
         _strike = s;
      }

      /**
       *
       * @param TYPE
       * @return TYPE
       */
      public double getLastPrice()
      {
         return _last;
      }

      /**
       *
       * @param TYPE
       * @return TYPE
       */
      public void setLastPrice( double p )
      {
         _last = p;
      }

      /**
       *
       * @param TYPE
       * @return TYPE
       */
      public String getSymbol()
      {
         return _optionSymbol;
      }

      /**
       *
       * @param TYPE
       * @return TYPE
       */
      public void setSymbol( String s )
      {
         _optionSymbol = s;
      }

      /**
       *
       * @param TYPE
       * @return TYPE
       */
      public long getVolume()
      {
         return _volume;
      }

      /**
       *
       * @param TYPE
       * @return TYPE
       */
      public void setVolume( long v )
      {
         _volume = v;
      }

      /**
       *
       * @param TYPE
       * @return TYPE
       */
      public long getOpenInterest()
      {
         return _openInterest;
      }

      /**
       *
       * @param TYPE
       * @return TYPE
       */
      public void setOpenInterest( long o )
      {
         _openInterest = o;
      }

      /**
       *
       * @param TYPE
       * @return TYPE
       */
      public int getDTE()
      {
         return _daysToExpire;
      }

      /**
       *
       * @param TYPE
       * @return TYPE
       */
      public void setDaysToExpire( int d )
      {
         _daysToExpire = d;
      }

      /**
       *
       * @param TYPE
       * @return TYPE
       */
      public String toString()
      {
         String returnValue = "Stock symbol: " + _symbol;
         returnValue += "\nOption symbol: " + _optionSymbol;
         returnValue += "\nStrike price: " + _strike;
         returnValue += "\nLast price: " + _last;
         returnValue += "\nVolume: " + _volume;
         returnValue += "\nOpen interest: " + _openInterest;
         returnValue += "\nDays to expire: " + _daysToExpire;

         return returnValue;
      }

   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public void parseOptions()
   {
      try
      {
         BufferedReader reader = new BufferedReader( new java.io.FileReader( "op.htm" ) );
         String line = null;
         while( ( line = reader.readLine() ) != null )
         {
            parseOptionsData( line );
            parseQuote( line );
         }
         reader.close();
      }
      catch( IOException e )
      {
         System.err.println( "Caught exception during parsing: " + e );
      }
   }

   /**
    *
    */
   private void parseOptionsData( String line )
   {

      int    index  = line.indexOf( "Call Options" );
      int    c      = -1;
      String calls  = null;
      String puts   = null;
      String data   = null;


      if( index > -1 )
      {
         line  = line.substring( index );
         index = line.indexOf( "Put Options" );
         puts  = line.substring( index ).replaceAll( "N/A", "0.0" );
         calls = line.substring( 0, index ).replaceAll( "N/A", "0.0" );
      }

      Pattern p = Pattern.compile( ".*?>(\\d+\\.\\d+)<.*" );
      while( index > -1 )
      {
         Matcher m = p.matcher( calls );
         data      = "BOGUS";

         if( m.matches() )
         {
            data = m.group( 1 );
            c++;
            if( c % 5 == 0 )
            {
               _strikePrice = parseDouble( data );
// System.out.println( "Strike: " + strike );
            }
            else if( c % 5 == 1 )
            {
               _last = parseDouble( data );
// System.out.println( "Last: " + last );
            }
            else if( c % 5 == 2 )
            {
               // NOP
            }
            else if( c % 5 == 3 )
            {
               _bid = parseDouble( data );
// System.out.println( "Bid: " + bid );
            }
            else if( c % 5 == 4 )
            {
               _ask = parseDouble( data );
// System.out.println( "Ask: " + ask );
               if( _ask > 0.0 )
               {
                  double t = ( _currentPrice / _strikePrice );
                  System.out.println( _strikePrice + ", Strike Ratio:, " + t + ", " + ( t * _ask ) );
               }
            }
         }

         index = calls.indexOf( ">" + data + "<" );
         calls = calls.substring( index + 1 );
      }
   }

   /**
    *
    */
   private void parseQuote( String line )
   {
      int    index  = line.indexOf( "quote_summary" );
      int    c      = -1;
      String data   = null;

      if( index > -1 )
      {
         line      = line.substring( index );
         Pattern p = Pattern.compile( ".*?>(\\d+\\.\\d+)<.*" );
         Matcher m = p.matcher( line );
         if( m.matches() )
         {
            data      = m.group( 1 );
            _currentPrice = parseDouble( data );
// System.out.println( "found current price: " + _currentPrice );
         }
      }

   }

   /**
    *
    */
   private double parseDouble( String n )
   {
      double value = Double.MIN_VALUE;
      try
      {
         value = Double.parseDouble( n );
      }
      catch( NumberFormatException e )
      {
         System.out.println( "Unable to parse double: " + e );
      }
      return value;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public DAOBase getDAO()
   {
      DAOBase dao = new WebPageDAO();
      dao.setFileName( _symbol + ".ser" );
      return dao;
   }

   /**
    *
    */
   public class WebPageDAO extends DAOBase< WebPageReader >
   {
   }

   /**
    *
    */
   public static void main( String[] args )
   {
      WebPageReader reader = null;
//       FileReader securityFile = new FileReader( "SP500/sp500.dat", "," );
//       String[] securities = securityFile.getArrayOfWords();

//       for( int i=0; i<securities.length; i++ )
//       {
//          reader = new WebPageReader();
//          reader.getContent( securities[i].toLowerCase() );
//       }

//       reader = new WebPageReader();
//       double last = reader.getLastTrade( "kr" );
//       System.out.println( "Last trade for KR: " + last );
//       reader = new WebPageReader();
//       reader.getContent( "kr" );
//       reader = new WebPageReader();
//       reader.getContent( "ba" );
      reader = new WebPageReader();
//       reader.getContent( "qqqq", "04", "2010" );
      reader.parseOptions();
//       double last = reader.getLastTrade( "qqq" );
//       System.out.println( "Last trade for QQQ: " + last );
//       System.out.println( "Open price for QQQ: " + reader.getOpen() );
//       System.out.println( "Open High for QQQ: " + reader.getHigh() );
//       System.out.println( "Open Low for QQQ: " + reader.getLow() );
//       reader = new WebPageReader();
//       reader.getContent( "spy" );
   }

}
