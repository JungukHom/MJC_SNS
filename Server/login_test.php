<?php
	
	error_reporting(E_ALL); 
	ini_set('display_errors',1); 

	$host = 'localhost';
    $user = 'root';
    $pw = 'autoset';
    $dbName = 'MJC_SNS'; // 'mjc_sns'

	$mysqli = new mysqli($host, $user, $pw, $dbName);

	/*
	$sql = "insert into 'mjc_sns'.'userinfo' values ('schascon', 'ne07080979', '함정욱', 19980223, 소프트웨어콘텐츠과, 2017662016)";
	$mysqli->query($sql);
	*/

	$sql="select * from userinfo";

	$result=mysqli_query($mysqli,$sql);
    $data = array();   
    if($result){  
    
        $row_count = mysqli_num_rows($result);

        if ( 0 == $row_count ){
            echo "Can't find '";
            echo $id;
            echo "' in DB";
        } // if
        else{

            while($row=mysqli_fetch_array($result)){
                array_push($data, 
                    array(
					'id'=>$row["id"],
                    'password'=>$row["password"],
					'name'=>$row["name"],
					'birthday'=>$row["birthday"],
					'department'=>$row["department"],
					'configurenumber'=>$row["configurenumber"]
                ));
            }

			header('Content-Type: application/json; charset=utf8');
            $json = json_encode(array("DU"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);

            echo $json;

        } // else
        mysqli_free_result($result);
    }
    else{  
        echo "Error occurring SQL : "; 
        echo mysqli_error($mysqli);
    }
	
	/*
			$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

			if ($android) {
				header('Content-Type: application/json; charset=utf8');
                $json = json_encode(array("DU"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
                echo $json;
			}
			*/
	
	mysqli_close($mysqli);
?>