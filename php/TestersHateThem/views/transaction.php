<?php
if(!isset($_SESSION['token'])){
	header('Location: '. HOST . '/login');
}
?>