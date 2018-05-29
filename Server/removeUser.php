<?php
	error_reporting(E_ALL);
	ini_set('display_errors', 1);

	$host = 'localhost';
	$user = 'root';
	$pw = 'autoset';
	$dbName = 'MJC_SNS'; // 'mjc_sns'
	$tableName = 'userinfo';

	$mysqli = new mysqli($host, $user, $pw, $dbName);
	mysqli_set_charset($mysqli,"utf8");

	$id = isset($_POST['id']) ? $_POST['id'] : '';

	$sql = "delete from userinfo where id=$id";

	$result = mysqli_query($mysqli, $sql);

	if ($result) {
		echo "DELETE_SUCCESS";
	}
	else {
		echo "DELETE_FAIL";
	}

	mysqli_close($mysqli);
?>