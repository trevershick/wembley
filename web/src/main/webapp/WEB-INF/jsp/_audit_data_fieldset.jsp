<%@ include file="/WEB-INF/jsp/d.jsp" %>
<fieldset>
<legend>Audit Data</legend>	
			<div class="control-group">
				<div class="controls">	
					<spring:bind path="auditData.updatedBy"><c:set var="who" value="${status.actualValue }"></c:set></spring:bind>
					<spring:bind path="auditData.updated"><c:set var="when" value="${status.actualValue }"></c:set></spring:bind>
	
					<fmt:message key="form.label.updated">
						<fmt:param value="${who }"/>
						<fmt:param value="${when }"/>
					</fmt:message>
					<form:hidden path="auditData.updatedBy" readonly="true"/> 
					<form:hidden path="auditData.updated" readonly="true"/>
				</div>
			</div>

			<div class="control-group">
				<div class="controls">	

					<spring:bind path="auditData.createdBy"><c:set var="who" value="${status.actualValue }"></c:set></spring:bind>
					<spring:bind path="auditData.created"><c:set var="when" value="${status.actualValue }"></c:set></spring:bind>
	
					<fmt:message key="form.label.created">
						<fmt:param value="${who }"/>
						<fmt:param value="${when }"/>
					</fmt:message>
					<form:hidden path="auditData.createdBy" readonly="true"/> 
					<form:hidden path="auditData.created" readonly="true"/>
				</div>
			</div>
</fieldset>