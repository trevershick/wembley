<%@include file="/WEB-INF/jsp/d.jsp" %>
<html>
<head>
<meta name="decorator" content="clean-bootstrap" />
<title>Edit User Group</title>
<meta name="where" content="admin.usergroup.edit" />

</head>
<body>

	<div id="content">
	
		<form:form commandName="usergroup" cssClass="form-horizontal well">


<fieldset>
    <legend>Basic Information</legend>
		
			<c:set var="cssClass"><spring:bind path="usergroup.id"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="id" cssErrorClass="" cssClass="control-label"><fmt:message key="usergroup.form.label.id" /></form:label>
				<div class="controls">
					<form:input path="id" readonly="true"/>
					<form:errors element="span" cssClass="help-inline" path="id"/>
				</div>
			</div>

			<c:set var="cssClass"><spring:bind path="usergroup.name"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="name" cssClass="control-label"><fmt:message key="usergroup.form.label.name"/></form:label>
			
				<div class="controls">
					<form:input path="name" placeholder="Name"/>
					<form:errors element="span" cssClass="help-inline" path="name"/>
				</div>
			</div>
</fieldset>
			
	<%@include file="_members_fieldset.jsp" %>

			
	<%@ include file="/WEB-INF/jsp/_audit_data_fieldset.jsp" %>

			
			<div class="form-actions">
				<button type="submit" class="btn btn-primary" name="_save"><fmt:message key="usergroup.form.button.save"/></button>
				<button type="submit" class="btn btn-inverse" name="_cancel"><fmt:message key="usergroup.form.button.cancel"/></button>
			</div>

		</form:form>
	</div>
</body>

</html>

