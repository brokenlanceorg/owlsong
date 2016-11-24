/**
 * 
 * This class will not only download data, but it will parse out the
 * downloaded data and perform a ranking algorithm on the teams.
 *
 */

package NCAA;

import java.net.*;
import java.io.*;
import java.util.*;
import common.*;

/**
 *  This class will download all the team data for all NCAA men's
 *  teams.
 */
public class NCAADataDownload
{
   String     _teamLinksName          = "TeamLinks.txt";
   String     _teamLinkMap            = "TeamLinkMap.txt";
   HashMap    _teamLinks              = new HashMap(); // http url --> Team Name
   HashMap    _teamLinkMapping        = new HashMap(); // yahoo 3-letter --> Team Name
   HashMap    _teamLinkReverseMapping = new HashMap(); // Team Name --> yahoo 3-letter
   HashMap    _teamDataMap            = new HashMap(); // yahoo 3-letter --> team data
   HashMap    _teamWinMap             = new HashMap(); // yahoo 3-letter --> team wins data
   HashMap    _teamLossMap            = new HashMap(); // yahoo 3-letter --> team loss data
   HashMap    _finalTeamWinMap        = null;          // yahoo 3-letter --> total team wins data

   /**
    *  Will parse out the data files and determine stats.
    */
   private void parseData()
   {
      readTeamLinkMap();
      readTeamDataMap();
      calculateFirstLevel();
      calculateSecondLevel();

      // now print out what we got:
      Iterator keys = _teamLinkMapping.keySet().iterator(); 

      while( keys.hasNext() )
      {
         String teamID = (String)keys.next();
         Integer myScore = (Integer)_finalTeamWinMap.get( teamID );
         String teamName = (String)_teamLinkMapping.get( teamID );
         System.out.println( teamName + "," + myScore );
         //System.out.println( teamName + " wins: " + _teamWinMap.get( teamID ) + " losses: " + _teamLossMap.get( teamID ) );
      }

   }

   /**
    * This method will make another pass of the data, but this time
    * it will add the defeated opponent's wins to their list.
    */
   private void calculateSecondLevel()
   {
      _finalTeamWinMap = new HashMap( _teamWinMap );
      Iterator keys = _teamLinkMapping.keySet().iterator(); 
      String marker = "teams";

      while( keys.hasNext() )
      {
         String teamID = (String)keys.next();
         String teamName = (String)_teamLinkMapping.get( teamID );
         String data = (String)_teamDataMap.get( teamID );
         int wins = 0;

         // find our starting position:
         int start = data.indexOf( "regular season games" );
         start = data.indexOf( marker, start );

         boolean quit = false;
         // while we're still looking...
         while( quit != true )
         {
            if( data.trim().length() == 0 )
               break;
            // need to figure out which team it is:
            String oppID = null;
            try {
            oppID = data.substring( start + 6, start + 9 );
            } catch( StringIndexOutOfBoundsException e )
            {
               System.out.println( "data is: " + data );
               System.exit( 1 );
            }

            // find the next position for a team result
            int end = data.indexOf( marker, start + 1 );
            if( end == -1 )
            {
               end = data.indexOf( "post season games", start + 1 );
            }

            // check the halting condition:
            if( end == -1 )
            {
               quit = true;
               break;
            }

            String temp = data.substring( start, end );

            // check for a win against this opponent
            if( temp.indexOf( "W " ) > -1 )
            {
               Integer myWins = (Integer)_finalTeamWinMap.get( teamID );
               Integer oppWins = (Integer)_teamWinMap.get( oppID );
//               System.out.println( "team is: " + teamID );
//               System.out.println( "opponent is: " + oppID );
               int twin = myWins.intValue();
               int owin = oppWins.intValue();
               twin += owin;
               _finalTeamWinMap.put( teamID, new Integer( twin ) );
            }
            // otherwise, it's a loss -- if you don't want the loss data
            // incorporated, then comment out this section.
            /*
            */
            else if( temp.indexOf( "L " ) > -1 )
            {
               Integer myWins = (Integer)_finalTeamWinMap.get( teamID );
               Integer oppLoss = (Integer)_teamLossMap.get( oppID );
//               System.out.println( "team is: " + teamID );
//               System.out.println( "opponent is: " + oppID );
               int twin = myWins.intValue();
               int oloss = oppLoss.intValue();
               twin -= oloss;
               _finalTeamWinMap.put( teamID, new Integer( twin ) );
            }

            start = end;
         }
      }

   }

   /**
    * This method simply calcs the team's record based on the data.
    */
   private void calculateFirstLevel()
   {
      Iterator keys = _teamLinkMapping.keySet().iterator(); 
      String marker = "teams";

      while( keys.hasNext() )
      {
         String teamID = (String)keys.next();
         String teamName = (String)_teamLinkMapping.get( teamID );
         String data = (String)_teamDataMap.get( teamID );
         int wins = 0;
         int losses = 0;
         int winWeight = 1;
         int lossWeight = 1;

         // find our starting position:
         int start = data.indexOf( "regular season games" );
         start = data.indexOf( marker, start );

         boolean quit = false;
         // while we're still looking...
         while( quit != true )
         {
            // find the next position for a team result
            int end = data.indexOf( marker, start + 1 );
            if( end == -1 )
            {
               end = data.indexOf( "post season games", start + 1 );
            }

            // check the halting condition:
            if( end == -1 )
            {
               quit = true;
               break;
            }

            String temp = data.substring( start, end );

            // check for a win
            if( temp.indexOf( "W " ) > -1 )
            {
//                System.out.println( "about to add win..." );
               wins += (winWeight++);
//                System.out.println( "wins: " + wins );
            }
            // otherwise, it's a loss
            else if( temp.indexOf( "L " ) > -1 )
            {
//                System.out.println( "about to add loss..." );
               losses += (lossWeight++);
//                System.out.println( "losses: " + losses );
            }

            start = end;
         }
         _teamWinMap.put( teamID, new Integer( wins ) );
         _teamLossMap.put( teamID, new Integer( losses ) );
      }

   }

   /**
    *  This will assign the teamID to the actual data.
    */
   private void readTeamDataMap()
   {
      common.FileReader theFile = new common.FileReader();

      Iterator keys = _teamLinkMapping.keySet().iterator(); 

      while( keys.hasNext() )
      {
         String teamID = (String)keys.next();
         String teamName = (String)_teamLinkMapping.get( teamID );
         String teamData = theFile.getFileAsString( "teams/" + teamName + ".htm" );
         _teamDataMap.put( teamID, teamData );
      }

   }

   /**
    * Reads in the links from which to download all the team data.
    */
   private void readTeamLinkMap()
   {
      common.FileReader theFile = new common.FileReader( _teamLinkMap, "," );
      String[] theLinks = theFile.getArrayOfWords();

      for( int i=0; i<theLinks.length; i+=2 )
      {
         _teamLinkMapping.put( theLinks[i], theLinks[i+1] );
         _teamLinkReverseMapping.put( theLinks[i+1], theLinks[i] );
      }
   }

   /**
    * Reads in the links from which to download all the team data.
    */
   private void readTeamLinks()
   {
      common.FileReader theFile = new common.FileReader( _teamLinksName, "," );
      String[] theLinks = theFile.getArrayOfWords();

      for( int i=0; i<theLinks.length; i+=2 )
      {
         _teamLinks.put( theLinks[i+1], theLinks[i] );
      }
   }

   /**
    * Actually downloads the data from Yahoo.
    */
   private void downloadAllTeamData()
   {
      Iterator keys = _teamLinks.keySet().iterator(); 

      while( keys.hasNext() )
      {
         String teamName = (String)keys.next();
         String teamLink = (String)_teamLinks.get( teamName );
         System.out.println( "Downloading data for team name: " + teamName );
         downloadTeamData( teamName, teamLink );
      }
   }

   /**
    * Actually downloads the data from Yahoo.
    */
   private void downloadTeamData( String teamName, String teamLink )
   {
      URL                theURL         = null;
      BufferedWriter     theOutputFile  = null;
      BufferedReader     theReader      = null;
      InputStream        theStream      = null;
      String             line           = null;

      System.out.println( "The download link is: " + teamLink );

      try
      {
         theURL = new URL( teamLink );
         theOutputFile = new BufferedWriter( new FileWriter( "teams\\" + teamName + ".htm" ) );
         theStream = theURL.openStream();
         theReader = new BufferedReader( new InputStreamReader( theStream ) );

         while( ( line = theReader.readLine() ) != null )
         {
            theOutputFile.write( line + "\n" );
         }
         theOutputFile.close();
         theReader.close();
      }
      catch( MalformedURLException me )
      {
         System.out.println( "Caught a MalformedURLException: " + me );
      }
      catch( UnknownHostException uh )
      {
         System.out.println( "Caught an UnkownHostException: " + uh );
      }
      catch( IOException ie )
      {
         System.out.println( "Caught an IOException: " + ie );
      }
   }

   /**
    * The main method
    */
   public static void main( String[] args )
   {
      if( args.length == 0 )
      {
         System.out.println( "Will perform full download..." );
         NCAADataDownload theLoader = new NCAADataDownload();
         theLoader.readTeamLinks();
         theLoader.downloadAllTeamData();
      }
      else
      {
         System.out.println( "Will perform data parse..." );
         NCAADataDownload theLoader = new NCAADataDownload();
         theLoader.parseData();
      }
   }

}
