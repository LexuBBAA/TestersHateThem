<?php
	define("HOST", 'http://localhost/TestersHateThem');
	define("API", '192.168.0.105:8000/');
?>
<form id="login" method="POST" action="<?php echo HOST; ?>/api/post/0/login">
	<fieldset>
		<div class="input-group">
			<input type="text" name="email" placeholder="Email" class="form-control">
		</div>
		<div class="input-group">
			<input type="password" name="password" placeholder="Password" class="form-control">
		</div>
		<div class="text-center">
			<button class="btn btn-info">LOG IN</button>
		</div>
	</fieldset>
</form>