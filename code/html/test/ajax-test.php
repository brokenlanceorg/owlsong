<?php
  header('Content-type: text/xml');
  
  $rating = $_POST['rating'];
  
  function record_rating($rating) {
    // Add data to database here
    $data = "default string";
    $connection = mysql_connect( 'mysql', 'saynday72', 'saynday72' ) or 
       ($data = $data + "connection failed");
    mysql_select_db( 'testdb' );
    $resultSet = mysql_query( 'select * from users;' );
    while( $line = mysql_fetch_array( $resultSet ) )
    {
       foreach( $line as $value )
       //print( $value );
       //$data = $data + $value;
       $data = $data . " " . $value;
    }
    mysql_close( 'testdb' );
    $rating = $data;
    return $data;
  }
?>
<xmlresponse>
    <rating><?= $rating ?></rating>
    <result><?= record_rating($rating) ?></result>
</xmlresponse>
