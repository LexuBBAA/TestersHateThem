<?php
	define("HOST", 'http://localhost/TestersHateThem');
	define("API", '192.168.0.105:8000/');
?>
<script type="text/javascript">
	$(document).on('change','#countries',function(){
		jQuery.get("<?php echo HOST; ?>/api/get/0/cities/"+jQuery("#countries").val(),
			function(cdata){
			var HTMLstring = '';
			var mydat = JSON.parse(cdata).data;
			for(let i = 0; i<mydat.length; i++){
				HTMLstring += '<option value="' + mydat[i].cid + '">' + mydat[i].name + '</option>';
			}
			jQuery('#cities').html(HTMLstring);
			jQuery('#cities').prop('disabled', false);
		});
	});
</script>

<?php
	$ch = curl_init();
	curl_setopt($ch,CURLOPT_URL, HOST . '/api/get/0/countries');
	curl_setopt($ch,CURLOPT_TIMEOUT,90);
	curl_setopt($ch,CURLOPT_MAXREDIRS,10);
	curl_setopt($ch,CURLOPT_RETURNTRANSFER,true);
	curl_setopt($ch,CURLOPT_FOLLOWLOCATION,true);
	$result = curl_exec($ch);
	$countries = json_decode($result, true);
	curl_close($ch);
?>

<form id="register" action="<?php echo HOST; ?>/api/post/0/register" method="POST">
	<fieldset>
		<div class="input-group mb-3">
			<input type="text" name="email" placeholder="Email" class="form-control">
		</div>
		<div class="input-group mb-3">
			<input type="password" name="password" placeholder="Password" class="form-control">
		</div>
		<div class="input-group mb-3">
			<input type="password" name="confirm_password" placeholder="Confirm password" class="form-control">
		</div>
		<div class="input-group mb-3">
			<input type="text" name="user_name" placeholder="Name" class="form-control">
		</div>
		<div class="text-center mb-3">
			<label><input type="radio" name="user_type" value="provider"><span>Provider</span></label>
			<label><input type="radio" name="user_type" value="consumer"><span>Consumer</span></label>
		</div>
		<div class="input-group mb-3">
			<textarea name="desc" placeholder="Descriere" rows="5" class="form-control"></textarea>
		</div>
		<div class="input-group mb-3">
			<select id="countries" class="form-control">
				<option value="0">Select a country</option>
			<?php foreach ($countries['data'] as $key => $value):
				?>
				<option value="<?php echo $value['cid']; ?>"><?php echo $value['name'] ?></option>
				<?php endforeach; 
			?>
			</select>
		</div>
		<div class="input-group mb-3">
			<select id="cities" class="form-control" disabled="disabled">
				<option value="0">Select city</option>
			</select>
		</div>
		<div class="input-group mb-3">
			<input type="text" name="address" placeholder="Address" class="form-control">
		</div>
		<div class="input-group mb-3">
			<input type="text" name="phone" placeholder="Phone" class="form-control">
		</div>
		<div class="text-center">
			<button type="submit" class="btn btn-info">Submit</button>
		</div>
	</fieldset>
</form>
