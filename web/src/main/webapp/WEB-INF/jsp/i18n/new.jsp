<%@ include file="/WEB-INF/jsp/d.jsp" %>
<html>
<head>
<meta name="decorator" content="clean-bootstrap" />
<meta name="where" content="support.i18n.new" />

</head>
<body>

	<div id="content">
		<form:form commandName="i18n" cssClass="form-horizontal well">


<fieldset>
    <legend><fmt:message key="i18n.form.header.newproperty" /></legend>
		
			
			<jsp:include page="_form_editable.jsp"/>
			
			
		</fieldset>
			<div class="form-actions">
				<button type="submit" class="btn btn-primary" name="_save"><fmt:message key="i18n.form.button.save"/></button>
				<button type="submit" class="btn btn-inverse" name="_cancel"><fmt:message key="i18n.form.button.cancel"/></button>
			</div>

		</form:form>
		
</div>
</body>

</html>

