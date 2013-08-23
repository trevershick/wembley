<%@ include file="/WEB-INF/jsp/d.jsp" %>
<html>
<head>
<meta name="decorator" content="clean-bootstrap" />
<title>Edit Source System</title>
<meta name="where" content="admin.sourcesystem.edit" />

</head>
<body>

	<div id="content">
		<form:form commandName="sourcesystem" cssClass="form-horizontal well">

<fieldset>
    <legend>Source System</legend>

		
			<c:set var="cssClass"><spring:bind path="sourcesystem.id"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="id" cssErrorClass="" cssClass="control-label"><fmt:message key="sourcesystem.form.label.id" /></form:label>
				<div class="controls">
					<form:input path="id" readonly="true"/>
					<form:errors element="span" cssClass="help-inline" path="id"/>
				</div>
			</div>

			<c:set var="cssClass"><spring:bind path="sourcesystem.name"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="name" cssClass="control-label"><fmt:message key="sourcesystem.form.label.name"/></form:label>
			
				<div class="controls">
					<form:input path="name" placeholder="Name" cssClass="input-xlarge"/>
					<form:errors element="span" cssClass="help-inline" path="name"/>
				</div>
			</div>
			
			
			<c:set var="cssClass"><spring:bind path="sourcesystem.personType"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="id" cssErrorClass="control-label" cssClass="control-label"><fmt:message key="sourcesystem.form.label.personType" /></form:label>
				<div class="controls">
					<form:select path="personType" cssErrorClass="error">
						<form:options items="${personTypes }"/>
		    		</form:select>
					<form:errors element="span" cssClass="help-inline" path="personType"/>
				</div>
			</div>


			    
		
			<c:set var="cssClass"><spring:bind path="sourcesystem.person"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="id" cssErrorClass="control-label" cssClass="control-label"><fmt:message key="sourcesystem.form.label.person" /></form:label>
				<div class="controls">
					<form:input path="person" placeholder="" cssErrorClass="input-xlarge error" cssClass="input-xlarge" />
					<form:errors element="span" cssClass="help-inline" path="person"/>
				</div>
			</div>			
			
			
			
			<c:set var="cssClass"><spring:bind path="sourcesystem.outboundQueue"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="outboundQueue" cssClass="control-label"><fmt:message key="sourcesystem.form.label.outboundQueue"/></form:label>
			
				<div class="controls">
					<form:input path="outboundQueue" placeholder="Outbound Queue Name" cssClass="input-xlarge"/>
					<form:errors element="span" cssClass="help-inline" path="outboundQueue"/>
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

