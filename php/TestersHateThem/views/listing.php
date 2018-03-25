<?php
if(!isset($_SESSION['token'])){
	header('Location: '. HOST . '/login');
}
?>
<ul>
<?php
	// define("HOST", 'http://localhost/TestersHateThem');
	// define("API", '192.168.0.105:8000/');

	$ch = curl_init();
	curl_setopt($ch,CURLOPT_URL, API . $module);
	curl_setopt($ch,CURLOPT_POST,true);

	foreach ($listing as $key => $value)
	?>
	<li class="mb-2 well">
		<h3><a href="/profile/<?php echo $value['uid']; ?>"><?php echo $value['name']; ?></a></h3>
		<p><?php echo $value['city']; ?></p>
		<p><?php echo $value['address']; ?></p>
	</li>
	<? endforeach; 
?>
</ul>