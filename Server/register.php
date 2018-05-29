<?php

	error_reporting(E_ALL);
	ini_set('display_errors',1);

	$host = 'localhost';
	$user = 'root';
	$pw = 'autoset';
	$dbName = 'MJC_SNS'; // 'mjc_sns'
	$tableName = 'userinfo';

	$mysqli = new mysqli($host, $user, $pw, $dbName);
	mysqli_set_charset($mysqli,"utf8");

	$id = isset($_POST['id']) ? $_POST['id'] : '';
	$pw = isset($_POST['pw']) ? $_POST['pw'] : '';
	$name = isset($_POST['name']) ? $_POST['name'] : '';
	$birthday = isset($_POST['birthday']) ? $_POST['birthday'] : '';
	$department = isset($_POST['department']) ? $_POST['department'] : '';
	$configure = isset($_POST['configure']) ? $_POST['configure'] : '';

	$rm = ", "; // restMark
	$sql_insert = "INSERT INTO userinfo 
						(id, password, name, birthday, department, configurenumber)";
	
	$sql_value = "VALUES (" .
							"'" . $id . "'" . $rm . 
							"'" . $pw . "'" . $rm . 
							"'" . $name . "'" . $rm . 
							$birthday . $rm . 
							"'" . $department . "'" . $rm . 
							$configure .
						 ")";

	$sql = $sql_insert . " " . $sql_value;
	$result = mysqli_query($mysqli, $sql);

	if ($result) {
		echo "REGISTER_OK";
	}
	else {
		echo "REGISTER_FAIL";
	}

	mysqli_close($mysqli);

?>