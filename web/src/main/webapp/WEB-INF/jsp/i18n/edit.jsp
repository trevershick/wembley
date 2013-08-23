<%@ include file="/WEB-INF/jsp/d.jsp" %>

<html>
<head>
<meta name="decorator" content="clean-bootstrap" />
<title>Edit i18n</title>
<meta name="where" content="support.i18n.edit" />

</head>
<body>

	<div id="content">
		<form:form commandName="i18n" cssClass="form-horizontal well">

<fieldset>
    <legend><fmt:message key="i18n.form.header.editproperty" /></legend>

		
			<c:set var="cssClass"><spring:bind path="i18n.id"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="id" cssErrorClass="" cssClass="control-label"><fmt:message key="i18n.form.label.id" /></form:label>
				<div class="controls">
					<form:input path="id" readonly="true"/>
					<form:errors element="span" cssClass="help-inline" path="id"/>
				</div>
			</div>

			<jsp:include page="_form_editable.jsp"/>

			
	</fieldset>

	<%@ include file="/WEB-INF/jsp/_audit_data_fieldset.jsp" %>

			
			<div class="form-actions">
				<button type="submit" class="btn btn-primary" name="_save"><fmt:message key="i18n.form.button.save"/></button>
				<button type="submit" class="btn btn-inverse" name="_cancel"><fmt:message key="i18n.form.button.cancel"/></button>
			</div>

		</form:form>
	</div>
</body>

</html>

