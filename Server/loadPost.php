<?php
	error_reporting(E_ALL);
	ini_set('display_errors', 1);

	$host = 'localhost';
	$user = 'root';
	$pw = 'autoset';
	$dbName = 'MJC_SNS'; // 'mjc_sns'

	$mysqli = new mysqli($host, $user, $pw, $dbName);

	mysqli_set_charset($mysqli,"utf8");

	$sql = "select * from postinfo";

	$result = mysqli_query($mysqli, $sql);

	$row_count = mysqli_num_rows($result);

	$data = array();

	if ($row_count == 0) {
		echo "ERROR";
	}
	else {
		while($row = mysqli_fetch_array($result)){
			array_push(
				$data,
				array (
					"postnumber"=>$row['postnumber'],
					'title'=>$row["title"],
					'writer'=>$row["writer"],
					"writedaytime"=>$row['writedaytime'],
					"content"=>$row['content'],
					"commentcount"=>$row['commentcount'],
					"like_"=>$row['like_'],
					"dislike"=>$row['dislike']
					)
			);
		} // while

		$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

		if ($android) {
			header('Content-Type: application/json; charset=utf8');
			$json = json_encode(array("POST_INFO"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
			echo $json;
		} // if
		else {
			header('Content-Type: application/json; charset=utf8');
			$json = json_encode(array("POST_INFO"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
			echo $json;
		}

		mysqli_free_result($result);
	}

	mysqli_close($mysqli);  
?>