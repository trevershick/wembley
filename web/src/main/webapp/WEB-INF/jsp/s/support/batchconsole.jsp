<%@ include file="/WEB-INF/jsp/d.jsp" %>
<html>
<head>
<title>Logging</title>
<meta name="decorator" content="clean-bootstrap" />
<meta name="where" content="support.logging" />
<script src="${pageContext.request.contextPath }/s/support/batchconsole/nmr/ui/lib/angular/angular.min.js"></script>
<script src="${pageContext.request.contextPath }/s/support/batchconsole/nmr/ui/lib/angular/angular-cookies.min.js"></script>
</head>
<body>
	

<div id="content">
<jsp:include page="/s/support/batchconsole/nmr/ui/app.html" flush="true" />
</div>
</body>
</html>