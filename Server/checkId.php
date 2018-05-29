<?php
	// error_reporting(E_ALL);
	// ini_set('display_errors', 1);

	$host = 'localhost';
	$user = 'root';
	$pw = 'autoset';
	$dbName = 'MJC_SNS'; // 'mjc_sns'
	$tableName = 'userinfo';

	$mysqli = new mysqli($host, $user, $pw, $dbName);
	mysqli_set_charset($mysqli,"utf8");

	$id = isset($_POST['id']) ? $_POST['id'] : '';

	$sql = "select id from userinfo where id='$id'";

	$result = mysqli_query($mysqli, $sql);

	while ($row = mysqli_fetch_array($result)) {
		$myId = $row['id'];
	}

	if ($id == $myId) {
		echo "ID_EXIST";
	}
	else {
		echo "ID_NOT_EXIST";
	}

	mysqli_close($mysqli);
?>