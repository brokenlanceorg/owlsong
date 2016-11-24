<?
   header('Content-type: text/xml');
   $_data = $_GET['data'];
   $_dataFile = "ultra-database.csv";
   $_fh = fopen( $_dataFile, 'w' ) or die( "can't open file" );
   fwrite( $_fh, $_data );
   fclose( $_fh );
   echo "true";
?>
