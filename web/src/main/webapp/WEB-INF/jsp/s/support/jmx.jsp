<%@ include file="/WEB-INF/jsp/d.jsp" %>
<html>
<head>
	<title>JMX Console</title>
	<meta name="decorator" content="clean-bootstrap" />
	<meta name="where" content="support.jmx" />
	<script src="${pageContext.request.contextPath }/s/support/jmx/phinneas/ui/lib/angular/angular.min.js"></script>
	<script src="${pageContext.request.contextPath }/s/support/jmx/phinneas/ui/lib/angular/angular-cookies.min.js"></script>
</head>
<body>
	<div id="content">
		<jsp:include page="/s/support/jmx/phinneas/ui/app.html" flush="true" />
	</div>
</body>
</html>