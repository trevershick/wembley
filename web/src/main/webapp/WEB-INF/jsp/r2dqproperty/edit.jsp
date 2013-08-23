<%@ include file="/WEB-INF/jsp/d.jsp" %>

<html>
<head>
<meta name="decorator" content="clean-bootstrap" />
<title>Edit Source System</title>
<meta name="where" content="support.r2dqproperty.edit" />

</head>
<body>

	<div id="content">
		<form:form commandName="r2dqproperty" cssClass="form-horizontal well">

<fieldset>
    <legend><fmt:message key="r2dqproperty.form.header.editproperty" /></legend>

		
			<c:set var="cssClass"><spring:bind path="r2dqproperty.id"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="id" cssErrorClass="" cssClass="control-label"><fmt:message key="r2dqproperty.form.label.id" /></form:label>
				<div class="controls">
					<form:input path="id" readonly="true"/>
					<form:errors element="span" cssClass="help-inline" path="id"/>
				</div>
			</div>

			<jsp:include page="_form_editable.jsp"/>

			
	</fieldset>

	<%@ include file="/WEB-INF/jsp/_audit_data_fieldset.jsp" %>

			
			<div class="form-actions">
				<button type="submit" class="btn btn-primary" name="_save"><fmt:message key="r2dqproperty.form.button.save"/></button>
				<button type="submit" class="btn btn-inverse" name="_cancel"><fmt:message key="r2dqproperty.form.button.cancel"/></button>
			</div>

		</form:form>
	</div>
</body>

</html>

