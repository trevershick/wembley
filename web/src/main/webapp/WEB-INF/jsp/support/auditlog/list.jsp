<%@ include file="/WEB-INF/jsp/d.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
<head>
<title>Audit Log</title>
<meta name="decorator" content="clean-bootstrap" />
<meta name="where" content="support.auditlog" />
</head>
<body>

	<div id="content">
		<form:form commandName="systemAuditLogSearchForm" cssClass="form-inline" method="POST" id="searchForm">
		
			<div class="well">
				<form:label path="entityName">Entity Name</form:label>
				<form:input path="entityName" cssClass="input-medium"/>
				
				<form:label path="entityId">Entity Id</form:label>
				<form:input path="entityId" cssClass="input-medium"/>
				
				

			    <button type="submit" class="btn btn-primary" name="_search"><fmt:message key="support.auditlog.form.button.search"/></button>
			    <button type="submit" class="btn btn-primary" name="_findExceptions"><fmt:message key="support.auditlog.form.button.search.exceptions"/></button>
				<button type="submit" class="btn btn-inverse" name="_cancel"><fmt:message key="support.auditlog.form.button.cancel"/></button>
			</div>
			
			

		<c:if test="${not empty systemAuditLogSearchForm.results }">

		
		<r2dq:pagination of="${ systemAuditLogSearchForm }"/>
		
		
		<table class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th ><fmt:message key="auditlog.list.th.id"/></th>
					<th><fmt:message key="auditlog.list.th.action"/></th>
					<th><fmt:message key="auditlog.list.th.entityId"/></th>
					<th><fmt:message key="auditlog.list.th.entityName"/></th>
					<th><fmt:message key="auditlog.list.th.sourceEntityId"/></th>
					<th><fmt:message key="auditlog.list.th.sourceEntityName"/></th>
					<th><fmt:message key="auditlog.list.th.cause"/></th>
					<th><fmt:message key="auditlog.list.th.createdDate"/></th>
					<th ><fmt:message key="auditlog.list.th.details"/></th>
					<th ><fmt:message key="auditlog.list.th.type"/></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${systemAuditLogSearchForm.results }" var="r">
					<spring:url value="{x}/show" var="showUrl">
						<spring:param name="x" value="${r.id }"/>
					</spring:url>
					<tr>
						<td>${r.id }</td>
						<td>${r.action }</td>
						<td>${r.entityId }</td>
						<td>${r.entityName }</td>
						<td>${r.sourceEntityId }</td>
						<td>${r.sourceEntityName }</td>
						<td>${r.cause }</td>
						<td>${r.createdDate }</td>
						<td id="ss_${r.id }">
<%-- 							<a href="popupex.html" onclick="return popitup('popupex.html')">${fn:substring(r.details, 1, 21)}</a> --%>
							<a href="${showUrl}">${fn:substring(r.details, 1, 21)}</a>
						</td>
						<td>${r.type }</td>
					</tr>
				</c:forEach>			
			</tbody>
		</table>
		
		<r2dq:pagination of="${ systemAuditLogSearchForm }"/>
		</c:if>

		</form:form>

		<c:if test="${empty systemAuditLogSearchForm.results  }">
			<div class="alert alert-info">
				<fmt:message key="message.list.noresults">
					<fmt:param value="${param.q }"/>
				</fmt:message>
			</div>
		</c:if>


<!-- 		<div class="modal hide fade" id="error-dialog"> -->
<!-- 			<div class="modal-header"> -->
<!-- 				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button> -->
<!-- 				<h3>Error</h3> -->
<!-- 			</div> -->
<!-- 			<div class="modal-body"> -->
<!-- 				<p>An error occurred. Sorry</p> -->
<!-- 			</div> -->
<!-- 			<div class="modal-footer"> -->
<!-- 				<a href="#" class="btn" data-dismiss="modal">Close</a> -->
<!-- 			</div> -->
<!-- 		</div> -->

	</div>

<div id="post-content-js">
<script language="javascript" type="text/javascript">
<!--
$("a").click(function(){
    $("#test").hide();
  });


function popitup(url) {
	newwindow=window.open(url,'name','height=200,width=150');
	if (window.focus) {newwindow.focus()}
	return false;
}

function mouseX(evt) {
	if (evt.pageX) return evt.pageX;
	else if (evt.clientX)
	   return evt.clientX + (document.documentElement.scrollLeft ?
	   document.documentElement.scrollLeft :
	   document.body.scrollLeft);
	else return null;
	}
	
function mouseY(evt) {
	if (evt.pageY) return evt.pageY;
	else if (evt.clientY)
	   return evt.clientY + (document.documentElement.scrollTop ?
	   document.documentElement.scrollTop :
	   document.body.scrollTop);
	else return null;
	}


// -->
</script>




<script>
// $(function() {
// 	highlightSearchResult($("#query").attr("value"));
// });
</script>
</div>

</body>

</html>

