<?php
   header('Content-type: text/xml');

   # Once these run, $_connection is the connection to the DB
   include '../common/db-config.php';
   include '../common/db-connect.php';

   $_username = $_POST['username'];
   $_password = $_POST['password'];

   function validateUser( $username, $password )
   {
      $data = "-1";
      $resultSet = mysql_query( "select level from users where username = '" . $username . "' and password = '" . $password . "';" );
      while( $line = mysql_fetch_array( $resultSet ) )
      {
         foreach( $line as $value )
         $data = $value;
      }

      mysql_close( $_databaseInstance );
      //$data = "user pass: " . $username . " pass: " . $password;

      return $data;
   }
?>
<xmlresponse>
   <user_level><?= validateUser( $_username, $_password ) ?></user_level>
</xmlresponse>
