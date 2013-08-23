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
<form:form commandName="logView" action="/s/support/auditlog/list" cssClass="form-inline" method="POST" id="logView">
	<div class="well">
	
	<table class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th ><fmt:message key="auditlog.show.header.fieldname"/></th>
					<th><fmt:message key="auditlog.show.header.fieldvalue"/></th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><label><fmt:message key="auditlog.list.th.id"/></label></td>
					<td>${logView.id }</td>
				</tr>
				<tr>
					<td><label><fmt:message key="auditlog.list.th.action"/></label></td>
					<td>${logView.action }</td>
				</tr>
				<tr>
					<td><label><fmt:message key="auditlog.list.th.entityId"/></label></td>
					<td>${logView.entityId }</td>
				</tr>
				<tr>
					<td><label><fmt:message key="auditlog.list.th.entityName"/></label></td>
					<td>${logView.entityName }</td>
				</tr>
				<tr>
					<td><label><fmt:message key="auditlog.list.th.sourceEntityId"/></label></td>
					<td>${logView.sourceEntityId }</td>
				</tr>
				<tr>
					<td><label><fmt:message key="auditlog.list.th.sourceEntityName"/></label></td>
					<td>${logView.sourceEntityName }</td>
				</tr>
				<tr>
					<td><label><fmt:message key="auditlog.list.th.cause"/></label></td>
					<td>${logView.cause }</td>
				</tr>
				<tr>
					<td><label><fmt:message key="auditlog.list.th.createdDate"/></label></td>
					<td>${logView.createdDate }</td>
				</tr>
			</tbody>
		</table>
		
		
		<ul>
			<li><label><fmt:message key="auditlog.list.th.details"/></label> : <span>${logView.details }</span></li>
		</ul>
	</div>
	
	<div class="well">
		<button type="submit" class="btn btn-primary" name="_close" value="_close"><fmt:message key="support.auditlog.form.button.close"/></button>
	</div>
</form:form>
</div>
</body>
</html>
