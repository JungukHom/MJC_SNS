<?php
	error_reporting(E_ALL);
	ini_set('display_errors', 1);

	$host = 'localhost';
	$user = 'root';
	$pw = 'autoset';
	$dbName = 'MJC_SNS'; // 'mjc_sns'
	$tableName = 'commentinfo';

	$mysqli = new mysqli($host, $user, $pw, $dbName);

	$postnumber = isset($_POST['postnumber']) ? $_POST['postnumber'] : '';
	// $postnumber = isset($_GET['postnumber']) ? $_GET['postnumber'] : '';

	mysqli_set_charset($mysqli,"utf8");

	$sql = "select * from $tableName where postnumber = '$postnumber'";

	$result = mysqli_query($mysqli, $sql);

	$data = array();

	while($row = mysqli_fetch_array($result)){
		array_push(
			$data,
			array (
				'writer'=>$row["writer"],
				"writedaytime"=>$row['writedaytime'],
				"content"=>$row['content']
			)
		);
	} // while

	$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

	if ($android) {
		header('Content-Type: application/json; charset=utf8');
		$json = json_encode(array("POST_COMMENT"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
		echo $json;
	} // if
	else {
		header('Content-Type: application/json; charset=utf8');
		$json = json_encode(array("POST_COMMENT"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
		echo $json;
	}
	// mysqli_free_result($result);

	mysqli_close($mysqli);
?>