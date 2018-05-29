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

	mysqli_close($mysqli);
?>