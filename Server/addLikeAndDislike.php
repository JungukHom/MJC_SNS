<?php
	error_reporting(E_ALL);
	ini_set('display_errors', 1);

	$host = 'localhost';
	$user = 'root';
	$pw = 'autoset';
	$dbName = 'MJC_SNS'; // 'mjc_sns'
	$tableName = 'commentinfo';

	$postnumber = isset($_POST['postnumber']) ? $_POST['postnumber'] : '';
	$likeDislike = isset($_POST['likeDislike']) ? $_POST['likeDislike'] : 'null';

	$mysqli = new mysqli($host, $user, $pw, $dbName);
	mysqli_set_charset($mysqli,"utf8");

	if ($likeDislike == 'null') {
		echo "ADD_FAIL";
	}
	else {
		$sql = "UPDATE postinfo SET $likeDislike = $likeDislike + 1 WHERE postnumber = $postnumber";

		$result = mysqli_query($mysqli, $sql);
		if ($result) {
			echo "ADD_SUCCESS";
		}
		else {
			echo "ADD_FAIL";
		}
	}

	mysqli_close($mysqli);
?>