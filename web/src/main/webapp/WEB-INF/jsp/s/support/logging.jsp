<%@ include file="/WEB-INF/jsp/d.jsp" %>
<html>
<head>
<title>Logging</title>
<meta name="decorator" content="clean-bootstrap" />
<meta name="where" content="support.logging" />
<script src="${pageContext.request.contextPath }/s/support/logging/ferb/ui/lib/angular/angular.min.js"></script>
<script src="${pageContext.request.contextPath }/s/support/logging/ferb/ui/lib/angular/angular-cookies.min.js"></script>
</head>
<body>
	

<div id="content">
<jsp:include page="/s/support/logging/ferb/ui/angular/app.html" flush="true" />
</div>
</body>
</html>