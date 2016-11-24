<?
   header('Content-type: text/xml');
   $_lockID = $_GET['ID'];
   $_lockFile = "lock.txt";
   $_fh = fopen( $_lockFile, 'r' ) or die( "can't open file" );
   $_fileID = fread( $_fh, filesize( $_lockFile ) );
   fclose( $_fh );
   if( $_fileID == $_lockID )
   {
      $_fh = fopen( $_lockFile, 'w' ) or die( "can't open file" );
      fwrite( $_fh, "OPEN" );
      fclose( $_fh );
      echo "true";
   }
   else
   {
      echo "false";
   }
?>
