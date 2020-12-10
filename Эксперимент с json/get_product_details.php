<?php
 
$response = array();
 
require 'db_connect.php';
 
$db = new DB_CONNECT();
 
if (isset($_GET["pid"])) {
    $pid = $_GET['pid'];
 //setlocale(LC_ALL, 'ru_RU.utf8')
 //setlocale(LC_ALL,'ru_RU.CP1251');
    mysql_query("SET NAMES 'utf8'"); 
	mysql_query("SET CHARACTER SET 'utf8'");
	mysql_query("SET SESSION collation_connection = 'utf8_general_ci'");
	$result = mysql_query("SELECT * FROM products WHERE pid = $pid");
 
    if (!empty($result)) {
        if (mysql_num_rows($result) > 0) {
 
            $result = mysql_fetch_array($result);
 
            $product = array();
            $product["pid"] = $result["pid"];
            $product["name"] = $result["name"];
            $product["price"] = $result["price"];
            $product["description"] = $result["description"];
            $product["created_at"] = $result["created_at"];
            $product["updated_at"] = $result["updated_at"];
            $response["success"] = 1;
 
            $response["product"] = array();
 
            array_push($response["product"], $product);
 
            echo json_encode($response);
        } else {
            $response["success"] = 0;
            $response["message"] = "No product found";
 
            echo json_encode($response);
        }
    } else {
        $response["success"] = 0;
        $response["message"] = "No product found";
 
        echo json_encode($response);
    }
} else {
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    echo json_encode($response);
}
?>