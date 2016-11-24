package common;

import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.activation.*;
import javax.mail.internet.*;

/**
 *
 */
public class PostMail
{
   Dictionary fields = new Hashtable();

   /**
    *
    */
   public void parseCommandLine(String args[])
   {
      if( args.length == 0 )
      {
         printUsage();
         System.exit( 1 );
      }

      String to = null;
      String from = null;
      String subject = null;
      String attachment = null;
      String body = null;

      for( int i = 0; i < args.length; i++ )
      {
         String arg = args[i];

         // Options
         if( !arg.startsWith( "-" ) )
         {
            body = arg;
         }
         else
         {
            if( arg.equals( "-t" ) )
            {
               if( (i == args.length - 1) || (args[ i+1 ].startsWith( "-" )) )
               {
                  System.err.println( "error: missing to address" );
                  printUsage();
                  System.exit( 1 );
               }
               to = args[++i];
               continue;
            }
            else if( arg.equals( "-f" ) )
            {
               if( (i == args.length - 1) || (args[ i+1 ].startsWith( "-" )) )
               {
                  System.err.println( "error: missing from address" );
                  printUsage();
                  System.exit( 1 );
               }
               from = args[++i];
               continue;
            }
            else if( arg.equals( "-s" ) )
            {
               if( (i == args.length - 1) || (args[i+1].startsWith( "-" )) )
               {
                  System.err.println( "error: missing subject" );
                  printUsage();
                  System.exit( 1 );
               }
               subject = args[++i];
               continue;
            }
            else if (arg.equals("-a"))
            {
               if( (i == args.length - 1) || (args[i+1].startsWith( "-" )) )
               {
                  System.err.println( "error: missing attachment filename" );
                  printUsage();
                  System.exit( 1 );
               }
               attachment = args[++i];
               continue;
            }
            else
            {
               printUsage();
               System.exit( 1 );
            }
         }
      }

      if( (null == to) || (null == from) )
      {
         printUsage();
         System.exit( 1 );
      }

      fields.put( "to", to );
      fields.put( "from", from );
      fields.put( "subject", subject );
      fields.put( "body", body );

      if (attachment != null )
      {
         try
         {
            int bytes_read;
            byte[] buffer = new byte[ 4096 ];
            File file = new File( attachment );
            FileInputStream in = new FileInputStream( file );
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            while( (bytes_read = in.read(buffer)) != -1 )
            {
               out.write( buffer, 0, bytes_read );
            }

            InternetHeaders headers = new InternetHeaders();
            MimeBodyPart bodyPart = new MimeBodyPart();
            DataSource ds = new ByteArrayDataSource( out.toByteArray(), null, attachment );
            bodyPart.setDataHandler( new DataHandler( ds ) );
            bodyPart.setDisposition( "attachment; filename=\"" + attachment + "\"" );
            bodyPart.setFileName( attachment );
            fields.put( "attachment", bodyPart );
         }
         catch (Exception e)
         {
            System.err.println(e.getMessage());
            System.exit(1);
         }
      }
   }

   /**
    *
    */
   public void postMail() throws MessagingException
   {
      boolean debug = false;

      //Set the host smtp address
      Properties props = new Properties();
      props.put( "mail.smtp.host", "smtp.mail.yahoo.com" );

      // create some properties and get the default Session
      Session session = Session.getDefaultInstance( props, null );
      session.setDebug( debug );

      // create a message
      Message msg = new MimeMessage( session );

      // set the from and to address
      InternetAddress addressFrom = new InternetAddress( (String)fields.get( "from" ) );
      msg.setFrom( addressFrom );

      InternetAddress[] tos = InternetAddress.parse( (String)fields.get( "to" ) );
      msg.setRecipients( Message.RecipientType.TO, tos );

      String subject = (String)fields.get( "subject" );
      if( subject != null ) 
      {
         msg.setSubject( (String)fields.get( "subject" ) );
      }

      msg.setSentDate( new Date() );
      if( null == fields.get( "attachment" ) )
      {
         msg.setText( (String)fields.get( "body" ) );
      }
      else
      {
         BodyPart body = new MimeBodyPart();
         BodyPart attachment = (BodyPart)fields.get( "attachment" );
         body.setText( (String)fields.get( "body" ) );
         MimeMultipart multipart = new MimeMultipart();
         multipart.addBodyPart( body );
         multipart.addBodyPart( attachment );
         msg.setContent( multipart );
      }

      Transport.send( msg );
   }

   /**
    *
    */
   private static void printUsage()
   {
      StringBuffer use = new StringBuffer();

      use.append( "Usage:\n" );
      use.append( "java PostMail (options) message\n\n" );
      use.append( "options:\n" );
      use.append( "-t toAddress\n" );
      use.append( "-f fromAddress\n" );
      use.append( "-s subject\n" );
      use.append( "-a attachment\n" );
      use.append( "-h help" );
      System.err.println( use );
   }

   /**
    *
    */
   class ByteArrayDataSource implements DataSource
   {
      byte[] bytes;
      String contentType, name;

      /**
       *
       */
      ByteArrayDataSource(byte[] bytes, String contentType, String name)
      {
         this.bytes = bytes;

         if( contentType == null )
         {
            this.contentType = "application/octet-stream";
         }
         else
         {
            this.contentType = contentType;
            this.name = name;
         }
      }

      /**
       *
       */
      public String getContentType()
      {
         return contentType;
      }

      /**
       *
       */
      public InputStream getInputStream()
      {
         // remove the final CR/LF
         return new ByteArrayInputStream( bytes, 0, bytes.length - 2 );
      }

      /**
       *
       */
      public String getName()
      {
         return name;
      }

      /**
       *
       */
      public OutputStream getOutputStream() throws IOException
      {
         throw new FileNotFoundException();
      }
   }
   
   /**
    *
    */
   public static void main(String[] args)
   {
      PostMail pm = new PostMail();
      try
      {
         pm.parseCommandLine(args);
         pm.postMail();
      }
      catch (Exception e)
      {
         System.err.println(e.getMessage());
      }
   }

}
