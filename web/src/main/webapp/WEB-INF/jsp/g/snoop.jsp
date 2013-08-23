<%@ include file="/WEB-INF/jsp/d.jsp" %>
<html>
<head>
<title>Snoop</title>
<meta name="decorator" content="clean-bootstrap" />
<meta name="where" content="snoop" />
<script src="${pageContext.request.contextPath }/g/snoop/y/ui/lib/angular/angular.min.js"></script>
<script src="${pageContext.request.contextPath }/g/snoop/y/ui/lib/angular/angular-cookies.min.js"></script>
</head>
<body>
<div id="content">
<jsp:include page="/g/snoop/y/ui/angular/app.html" flush="true" />
</div>
</body>
</html>