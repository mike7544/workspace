<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>
	<title>MikeGames</title>
	<meta charset="utf-8" />
	
	<link rel="stylesheet" href="stylesheet.css" type="text/css" />
	<meta name="viewpot" content="width=device-width, initial-scale=1.0">
</head>

<body class="body">
	
	<header class="mainheader">
		<img src="img/logo.gif">
		
		<nav><ul>
			<li class="active"><a href="#">Home</a></li>
			<li><a href="screenshots.html">Screenshots</a></li>
			<li><a href="troubleshoot.html">Troubleshoot</a></li>
			<li><a href="about.html">About</a></li>
			<li><a href="login.html">Login</a></li>
		</ul></nav>
	</header>
	
	<div class="maincontent">
		<div class="content">
			<div class="topcontent">
				<header>
					<h2 class="title">GAME PLAYER</h2>
				</header>

	<%			
 	if (session.getAttribute("userName") != null) {
 		//|| (session.getAttribute("userName") != "null")) {
 		out.println("Welcome " + session.getAttribute("userName"));
 	}
	 %> 	
		</div>
		
	<div>
	  
	<object id ='applet' type='application/x-java-applet'  height='600' width='800'>
		
  		<param name='archive' value='Tetris.jar'>
		<param name='code' value='games/TetrisApplet.class' />
	<div class="middlecontent">
  			<p >
  				Applet failed to run.  No Java plug-in was found.
  			</p>
  		</div>
	</object>	
	</div>
		</div>
	</div>
	

	
	<footer class="mainfooter">
		<p>copyright &copy;: <a href="index.jsp" title="mikeGAMES">mikeGAMES.com</a></p>
	</footer>
</body>
</html>