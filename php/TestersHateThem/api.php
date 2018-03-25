<?php 
	//header('Content-Type: application/json');
	define("HOST", 'http://localhost/TestersHateThem');
	define("API", '192.168.0.105:8000/');
	$parts = explode('/', $_SERVER['REQUEST_URI']);
	if ( ! session_id() ) @ session_start();
	$request = $parts[3];
	$token = $parts[4];
	$module = $parts[5];
	$rest = '';
	if(isset($parts[6])){
		for($i = 6;$i <= count($parts);$i++){
			if(isset($parts[$i])){
				$rest .= '/'.$parts[$i];
			}
		}
	}
	$ch = curl_init();
	if($request == 'post'){
		curl_setopt($ch,CURLOPT_URL, API . $module);
		curl_setopt($ch,CURLOPT_POST,true);
		$post_body = '';
		foreach($_POST as $key=>$value) { $post_body .= $key.'='.$value.'&'; }
		rtrim($post_body, '&');
		curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($_POST) );
		if($token == 1 && $_SESSION['token']){
			$authorization = "Token:" . $_SESSION['token'];
			curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/json' , $authorization ));
		}else{
			curl_setopt($ch, CURLOPT_HEADER, 0);
		}
		curl_setopt($ch,CURLOPT_TIMEOUT,90);
		curl_setopt($ch,CURLOPT_MAXREDIRS,10);
		curl_setopt($ch,CURLOPT_RETURNTRANSFER,true);
		curl_setopt($ch,CURLOPT_FOLLOWLOCATION,true);
	}else if ($request == 'get'){
		curl_setopt($ch,CURLOPT_URL, API . $module . $rest);
		curl_setopt($ch,CURLOPT_TIMEOUT,90);
		curl_setopt($ch,CURLOPT_MAXREDIRS,10);
		curl_setopt($ch,CURLOPT_RETURNTRANSFER,true);
		curl_setopt($ch,CURLOPT_FOLLOWLOCATION,true);
	}
	$result = curl_exec($ch);
	$result_decoded = json_decode($result, true);
	//print_r($result_decoded);
	if($module == 'login' || 'register' && isset($result_decoded['token']) ) {
		$_SESSION['token'] = $result_decoded['token'];
		header('Location: '. HOST . '/listing');
	}
	print $result;
	curl_close($ch);