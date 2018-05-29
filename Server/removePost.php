<?php
	error_reporting(E_ALL);
	ini_set('display_errors', 1);

	$host = 'localhost';
	$user = 'root';
	$pw = 'autoset';
	$dbName = 'MJC_SNS'; // 'mjc_sns'

	$postnumber = isset($_GET['postnumber']) ? $_GET['postnumber'] : '';

	$mysqli = new mysqli($host, $user, $pw, $dbName);
	mysqli_set_charset($mysqli,"utf8");

	$sql = "DELETE FROM postinfo WHERE postnumber = $postnumber";

	$result = mysqli_query($mysqli, $sql);

	if ($result) {
		echo "DELETE_OK";
	}
	else {
		echo "DELETE_FAIL";
	}

	mysqli_close($mysqli);
?>