<?php

	error_reporting(E_ALL);
	ini_set('display_errors', 1);

	$host = 'localhost';
	$user = 'root';
	$pw = 'autoset';
	$dbName = 'MJC_SNS'; // 'mjc_sns'

	$mysqli = new mysqli($host, $user, $pw, $dbName);

	mysqli_set_charset($mysqli,"utf8");

	// 포스트값의 제대로된 입력 여부 확인
	$id = isset($_POST['id']) ? $_POST['id'] : '';
	$pw = isset($_POST['pw']) ? $_POST['pw'] : '';

	$sql = "select password from userinfo where id='$id'";

	$result = mysqli_query($mysqli, $sql);

	while ($row = mysqli_fetch_array($result)) {
		$myPw = $row['password'];
	}

	// 비밀번호를 비교해서 값 확인
	if ($pw == $myPw) {
		$result = "LOGIN_OK";
	}
	else {
		$result = "LOGIN_FAIL";
	}
	echo (string) $result;

	mysqli_close($mysqli);
?>

