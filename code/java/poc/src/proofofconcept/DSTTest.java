/* 
 * Timezone test to check that the new US 2007 DST rules have been 
 * picked up by the JRE. TimeZone used is the default one on OS.
 * Sample result :
 * $java DSTTest "11/04/2007 0:00 AM"
 * JRE version : 1.4.2_13
 * Sun Nov 04 00:00:00 EDT 2007
 * TimeZone tested : Eastern Daylight Time
 * Your tested time is in daylight-savings time.
 * $java DSTTest "11/04/2007 3:00 AM"
 * JRE version : 1.4.2_13
 * Sun Nov 04 03:00:00 EST 2007
 * TimeZone tested : Eastern Standard Time
 * Your tested time is not in daylight-savings time.
 * $echo $TZ
 * America/New_York
 * 
 */

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DSTTest 
{
   private static final String DATE_FORMAT_PATTERN = "MM/dd/yyyy hh:mm a";

   public static void main(String[] args) 
   {
      try 
      {
         String ver = System.getProperty("java.version");
         String testDate = args[0]; // "11/04/2007 1:00 AM";

         SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT_PATTERN);
         Date currentDate = df.parse(testDate, new ParsePosition(0));
         System.out.println("JRE version : " + ver);
         System.out.println(currentDate);
         // Default timezone from the system is used.
         TimeZone tz = TimeZone.getDefault();
         // Could manually set the timezone above. e.g :
         //TimeZone tz = TimeZone.getTimeZone("America/New_York");
         //        		TimeZone tz = TimeZone.getTimeZone("America/Chicago");
         System.out.println("TimeZone is: " + tz );
         System.out.println("TimeZone tested : " + 
            tz.getDisplayName(tz.inDaylightTime(currentDate), TimeZone.LONG));
         boolean indaylight = tz.inDaylightTime(currentDate);
         System.out.println("Your tested time is " + 
            (indaylight ? "in " : "not in ")  + "daylight-savings time.");
      } 
      catch (Exception e) 
      {
         System.out.println("Error encoutered. Please insure you supply a valid date format pattern.");
         System.out.println("Ensure you've quotes placed around the pattern!");
         System.out.println("e.g java DSTTest \"11/04/2007 1:00 AM\"");
      }
      } 
}
