<%@ include file="/WEB-INF/jsp/d.jsp" %>
<html>
<head>
<title>JVM Properties</title>
<meta name="decorator" content="clean-bootstrap" />
<meta name="where" content="support.properties" />
</head>
<body>
	

	<div id="content">
	
	
	
<c:if test="${properties != null}">
	<table class="table table-striped table-bordered">
	<thead>
		<tr><th>Property</th>
			<th class="col2">Value</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${properties}" var="result">
			<tr>
				<td>${result.key}</td>
				<td>
					<c:choose>
						<c:when test="${result.key == 'SSO_USRDATA' }">
						${fn:replace(result.value,"*"," ")}
						</c:when>
						<c:otherwise>${fn:replace(fn:replace(result.value,";","; "), ",", ", ")}</c:otherwise>
					</c:choose>
					
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</c:if>
</div>
</body>
</html>