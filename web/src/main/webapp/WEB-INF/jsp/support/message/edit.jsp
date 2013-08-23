<%@ include file="/WEB-INF/jsp/d.jsp" %>
<html>
<head>
<meta name="decorator" content="clean-bootstrap" />
<title>Edit Message</title>
<meta name="where" content="support.message.edit" />

</head>
<body>

	<div id="content">
		<form:form commandName="message" cssClass="form-horizontal well">

<fieldset>
    <legend>Edit Message</legend>


			
			<c:set var="cssClass"><spring:bind path="data"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="data" cssErrorClass="" cssClass="control-label"><fmt:message key="support.message.form.label.data" /></form:label>
				<div class="controls">
					<form:textarea path="data" rows="10" cols="120" cssClass="input-xxlarge"/>
					<form:errors element="span" cssClass="help-inline" path="data"/>
				</div>
			</div>			
	</fieldset>

	<%@ include file="/WEB-INF/jsp/_audit_data_fieldset.jsp" %>

			
			<div class="form-actions">
				<button type="submit" class="btn btn-primary" name="_save"><fmt:message key="sourcesystem.form.button.save"/></button>
				<button type="submit" class="btn btn-inverse" name="_cancel"><fmt:message key="sourcesystem.form.button.cancel"/></button>
			</div>

		</form:form>
	</div>
</body>

</html>

