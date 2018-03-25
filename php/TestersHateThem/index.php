<?php

if ( ! session_id() ) @ session_start();

error_reporting(0);
$parts = explode('/', $_SERVER['REQUEST_URI']);
$module = $parts[2];

define("HOST", 'http://localhost/TestersHateThem');
define("API", '192.168.0.105:8000/');
define("STATIC_PATH","D:\wamp64\www\TestersHateThem\\");

if($module == 'api'){

	$_GET['view'] = 'api';

	require_once(HOST . '/api.php');

}else{

	$_GET['view'] = $module;

	require_once(STATIC_PATH . 'header.php');

	require_once(STATIC_PATH . "views/$module.php");

	require_once(STATIC_PATH . 'footer.php');

}