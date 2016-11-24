<?php
   $_connection = mysql_connect( $_databaseHost, $_databaseUser, $_databasePass ) or 
      die( "Unable to connect to the database!" );
   mysql_select_db( $_databaseInstance );
?>
