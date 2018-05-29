<?php
	error_reporting(E_ALL);
	ini_set('display_errors', 1);

	$host = 'localhost';
	$user = 'root';
	$pw = 'autoset';
	$dbName = 'MJC_SNS'; // 'mjc_sns'
	$tableName = 'commentinfo';

	$mysqli = new mysqli($host, $user, $pw, $dbName);
	mysqli_set_charset($mysqli,"utf8");

	$postnumber = isset($_POST['postnumber']) ? $_POST['postnumber'] : ''; 
	$writer = isset($_POST['writer']) ? $_POST['writer'] : '';
	$content = isset($_POST['content']) ? $_POST['content'] : '';

	$writedaytime = date("m-d A H:i", time());

	$rm = ", "; // restMark
	$sql_insert = "INSERT INTO commentinfo";
	
	$sql_value = "VALUES (" .
							$postnumber . $rm .
							"'" . $writer . "'" . $rm . 
							"'" . $writedaytime . "'" . $rm .
							"'" . $content . "'" .
						 ")";

	$sql = $sql_insert . " " . $sql_value;

	$sql2 = "UPDATE postinfo SET commentcount = commentcount + 1 WHERE postnumber = $postnumber";

	$addresult = mysqli_query($mysqli, $sql);

	if ($addresult) {
		echo "ADD_SUCCESS";
		mysqli_query($mysqli, $sql2);
	}
	else {
		echo "ADD_FAIL";
	}

	mysqli_close($mysqli);
?>