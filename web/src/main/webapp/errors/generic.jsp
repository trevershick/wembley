<%@page isErrorPage="true" language="java"%>
<html>
<head>
<meta name="decorator" content="clean" />
<title>Error</title>
</head>
<body>
	<div id="content">
		<p class="lone-content">An unforeseen error has occurred ( <%=exception.getMessage() %> ). You can log out and log back
			in or you can click continue to try again.</p>
		<div class="controlBar2">
			<html:button styleClass="btn pos-btn" property="continue"
				onclick="document.location = '';">
				<bean:message key="button.continue" />
			</html:button>
		</div>

	</div>
</body>
</html>
