<?php
	error_reporting(E_ALL);
	ini_set('display_errors', 1);

	$host = 'localhost';
	$user = 'root';
	$pw = 'autoset';
	$dbName = 'MJC_SNS'; // 'mjc_sns'
	$tableName = 'postinfo';

	$mysqli = new mysqli($host, $user, $pw, $dbName);
	mysqli_set_charset($mysqli,"utf8");

	$title = isset($_POST['title']) ? $_POST['title'] : '';
	$writer = isset($_POST['writer']) ? $_POST['writer'] : '';
	$content = isset($_POST['content']) ? $_POST['content'] : '';

	$commentcount = "0";
	$like = "0";
	$dislike = "0";
	 /*
	$commentcount = isset($_POST['commentcount']) ? $_POST['commentcount'] : '';
	$like = isset($_POST['like']) ? $_POST['like'] : '';
	$dislike = isset($_POST['dislike']) ? $_POST['dislike'] : '';
	 */

	$amOrPm = date("A", time());
	if ($amOrPm == "AM") {
		$amPm = "오전";
	}
	else {
		$amPm = "오후";
	}
	//$month = date("n월 ", time());
	//$day = date("j일 ", time());
	//$hour = date("g:", time());
	//$minute = date("i", time());
	/*
	$dateString = date("n월 j일 ", time());
	$_dateString = date(" g:i", time());
	$writedaytime = $dateString . $amPm . $_dateString;
	*/

	// $writedaytime = date("m월d일 A H:i", time());
	// $writedaytime = $month . $day . $hour . $minute;
	
	$writedaytime = date("m-d A H:i", time());

	$postnumber = "0";

	$rm = ", "; // restMark
	$sql_insert = "INSERT INTO postinfo";
	
	$sql_value = "VALUES (" .
							$postnumber . $rm .
							"'" . $title . "'" . $rm . 
							"'" . $writer . "'" . $rm . 
							"'" . $writedaytime . "'" . $rm .
							"'" . $content . "'" . $rm . 
							$commentcount . $rm . 
							$like . $rm . 
							$dislike .
						 ")";

	$sql = $sql_insert . " " . $sql_value;

	$result = mysqli_query($mysqli, $sql);

	if ($result) {
		echo "COMMIT_OK";
	}
	else {
		echo "COMMIT_FAIL";
	}

	mysqli_close($mysqli);
?>