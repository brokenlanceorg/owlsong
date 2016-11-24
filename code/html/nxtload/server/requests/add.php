<?php
   header('Content-type: text/xml');

   $_callBack = $_POST['callback'];
   $_custName = $_POST['name'];

   function sendEmail( $callBack, $custName )
   {
      $to = "Saynday72@yahoo.com";
      $subject = "Nextload schedule request";
      $body = "You have received a request from: " . $custName . " they can be reached at: " . $callBack;
      mail( $to, $subject, $body );
      mail( "8179448730@cingularme.com", $subject, $body );
      return $body;
   }
?>
<xmlresponse>
   <result><?= sendEmail( $_callBack, $_custName ) ?></result>
</xmlresponse>
