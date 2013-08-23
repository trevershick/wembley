<%@ include file="/WEB-INF/jsp/d.jsp" %>
<html>
<head>
<title>Support</title>
<meta name="decorator" content="clean-bootstrap" />
<meta name="where" content="support.home" />
</head>
<body>
	

	<div id="content">
	
	
<jsp:useBean id="currentDate" class="java.util.Date" scope="request" />
<div id="supportConsole">
	<div id="serverInfo" class="supportConsoleSection">
		<h3>Server Info</h3>
		<ul>
			<li>Host Name :  ${hostname }</li>
			<li>Address : ${address }</li>
			<li>Local Port : ${pageContext.request.localPort}</li>
			<li>Context Path : ${pageContext.request.contextPath}</li>
			<li>Server Info : ${ pageContext.servletConfig.servletContext.serverInfo }</li>
		</ul>
	</div>
	<div id="systemLinks" class="supportConsoleSection">
		<h3>Support Links</h3>
		<ul>
			<li>
				<c:url value="/internal/rem" var="remUrl"/>
				<a class="not-implemented" href="${remUrl}">REM Links</a>
				<ul>
					<c:forEach items="${remlinks }" var="rl">
						<spring:url value="/internal/rem/{component}" var="remComponentUrl">
							<spring:param name="component" value="rl"/>
						</spring:url>
						<li>
							<a href="${remComponentUrl}">${rl }</a>
						</li>
					</c:forEach>
				</ul>
			</li>
			<spring:url value="/internal/version" var="versionUrl"/>
			
			<li><a href="${versionUrl}">Version URL</a></li>
		</ul>
	</div>
	
	<div id="svnInfo" class="supportConsoleSection">
		<h3>SCM Revision Info</h3>
		<p>
			<c:forEach items="${svnRevisionText}" var="line">
				${line }<br/>
			</c:forEach>
		</p>
	</div>
	
	
	<h3>Build Info</h3>
	<p>${versionText}</p>
	
${servers }
</div>


</div>
</body>
</html>