<?
   header('Content-type: text/xml');
   $_lockID = $_GET['ID'];
   $_lockFile = "lock.txt";
   $_fh = fopen( $_lockFile, 'r' ) or die( "can't open file" );
   $_fileID = fread( $_fh, filesize( $_lockFile ) );
   fclose( $_fh );
   if( $_fileID == $_lockID || $_fileID == "OPEN" )
   {
      $_fh = fopen( $_lockFile, 'w' ) or die( "can't open file" );
      fwrite( $_fh, $_lockID );
      fclose( $_fh );
      echo "true";
   }
   else
   {
      echo "false";
   }
?>
