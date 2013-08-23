<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Railinc Single Sign On</title>
<link rel="stylesheet" type="text/css" href="css/reset.css"/>
<link rel="stylesheet" type="text/css" href="css/layout.css"/>
<link rel="stylesheet" type="text/css" href="css/color.css"/>
<link rel="stylesheet" type="text/css" href="css/typography.css"/>
<link rel="stylesheet" type="text/css" href="css/app.css"/>
<script src="js/jquery-1.9.1.min.js"></script>
</head>
<body>
<div id="wrapper">
	<div id="header">
		<!-- 
        <ul id="util-nav" class="horizontal-menu">
        	<li class="userName">signed in as ipacp01</li>
           
            <li><a href="/rportal">your workspace</a></li>
            
            <li><a href="/sso">user services</a></li>
           
            <li><a href="/help">help</a></li>
           
            <li><a href="/contactus">contact us</a></li>
        </ul>
         -->
        <div id="logoArea">
        <a href="http://www.railinc.com" class="logo"><img src="images/Railinc-Logo.png" alt="Go to www.railinc.com" /></a>
        <h1 class="logoText">Railinc</h1>
        <h2 class="headerDescription">R2DQ Dev Hack</h2>        
        </div>
    </div>
        <div id="container" style="min-height:500px">
	    	<div>
		    	<div>
		    		<jsp:include flush="true" page="form.jsp"/>
		    	</div>
		    	
		    	<div>
		    		<h1 style="display:inline-block;">SSO Dev Hack Details</h1>
		    		( <a id="click-me" href="#">click for more details</a> )
		    		
		    		<div id="details" style="display:none;">
		    		<pre>
		    		<jsp:include flush="true" page="appMessages.jsp"/>
		    		</pre>
		    		</div>
		    		<script>
		    		$(function() {
		    			$("#click-me").click(function() {
		    				$(this).text($("#details").is(":visible") ? "click for more details":"hide details");
		    				$("#details").toggle();
		    			});
		    		});
		    		</script>
		    	</div>
	    	</div>
    	</div>

 

    <div id="footer">
    	
   		<div class="footer-information">
    		<ul class="horizontal-menu">
     			<li><a href="/legal" class="first">legal notices</a></li>
     			<li><a href="/privacy">privacy rights</a></li>
        		<li><a href="/terms">terms of service</a></li>
                <li><a href="/contactus">contact us</a></li>
    		</ul>
    		<div class="copyright">
    			 Copyright &copy; 2010 Railinc. All rights reserved.
    		</div>	
  		</div>	
 	

    </div>
</div>


</body>
</html>
