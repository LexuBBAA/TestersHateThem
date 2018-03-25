<?php
?>
<html>
	<head>
        <link rel="stylesheet" href="<?php echo HOST; ?>/css/bootstrap.min.css">
		<script src="<?php echo HOST; ?>/js/jquery.min.js"></script>
		<script src="<?php echo HOST; ?>/js/bootstrap.min.js"></script>
	</head>

	<body>
		<nav>
			<ul class="nav nav-pills nav-fill">
				<li class="nav-item"><a class="nav-link <?php if($_GET['view'] == 'listing') echo 'active'; ?>" href="<?php echo HOST; ?>/listing">Listing</a></li>
				<li class="nav-item"><a class="nav-link <?php if($_GET['view'] == 'own_profile') echo 'active'; ?>" href="<?php echo HOST; ?>/own_profile">Profile</a></li>
				<li class="nav-item"><a class="nav-link <?php if($_GET['view'] == 'transactions') echo 'active'; ?>" href="<?php echo HOST; ?>/transactions">Transactions</a></li>
				<li class="nav-item"><a class="nav-link <?php if($_GET['view'] == 'rankings') echo 'active'; ?>" href="<?php echo HOST; ?>/rankings">Rankings</a></li>
			</ul>
		</nav>
		<main id="main" class="container-fluid mt-3">
			<div class="container">