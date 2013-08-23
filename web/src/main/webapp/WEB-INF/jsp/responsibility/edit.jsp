<%@include file="/WEB-INF/jsp/d.jsp" %>
<html>
<head>
<meta name="decorator" content="clean-bootstrap" />
<title><fmt:message key="responsibility.title.edit" /></title>
<meta name="where" content="admin.responsibility.edit" />

</head>
<body>

	<div id="content">
	
		<form:form commandName="responsibility" cssClass="form-horizontal well">


<fieldset>
    <legend><fmt:message key="responsibility.title.edit" /></legend>
    
			<c:set var="cssClass"><spring:bind path="responsibility.sourceSystem"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
			    <form:hidden path="id"/>
				<form:label path="id" cssErrorClass="control-label" cssClass="control-label"><fmt:message key="responsibility.form.label.sourceSystem" /></form:label>
				<div class="controls">
					<form:select path="sourceSystem" cssErrorClass="error">
				    	<spring:message var="any" code="responsibility.sourcesystem.useasdefault" text=" - use as default - "/>
				    	<form:option value="" label="${any }"/>
				    	<form:options items="${sourceSystems }" itemValue="identifier" itemLabel="name"/>
		    		</form:select>
					<form:errors element="span" cssClass="help-inline" path="sourceSystem"/>
				</div>
			</div>


			<c:set var="cssClass"><spring:bind path="responsibility.ruleNumber"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="ruleNumber" cssClass="control-label"><fmt:message key="responsibility.form.label.ruleNumber"/></form:label>
			
				<div class="controls">
					<form:input path="ruleNumber" placeholder="Rule Number" cssErrorClass="input-small error" cssClass="input-small"  />
					<form:errors element="span" cssClass="help-inline" path="ruleNumber"/>
				</div>
			</div>

		
			    
			<c:set var="cssClass"><spring:bind path="responsibility.personType"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="id" cssErrorClass="control-label" cssClass="control-label"><fmt:message key="responsibility.form.label.personType" /></form:label>
				<div class="controls">
					<form:select path="personType" cssErrorClass="error">
						<form:options items="${personTypes }"/>
		    		</form:select>
					<form:errors element="span" cssClass="help-inline" path="personType"/>
				</div>
			</div>


			    
		
			<c:set var="cssClass"><spring:bind path="responsibility.person"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="id" cssErrorClass="control-label" cssClass="control-label"><fmt:message key="responsibility.form.label.person" /></form:label>
				<div class="controls">
					<form:input path="person" placeholder="" cssErrorClass="input-xlarge error" cssClass="input-xlarge" />
					<form:errors element="span" cssClass="help-inline" path="person"/>
				</div>
			</div>
			
			<c:set var="cssClass"><spring:bind path="responsibility.precedence"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="id" cssErrorClass="control-label" cssClass="control-label"><fmt:message key="responsibility.form.label.precedence" /></form:label>
				<div class="controls">
					<form:input path="precedence" placeholder="" cssErrorClass="input-small error" cssClass="input-small" maxlength="10"/>
					<form:errors element="span" cssClass="help-inline" path="precedence"/>
				</div>
			</div>
		
		<c:set var="cssClass"><spring:bind path="responsibility.note"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="id" cssErrorClass="control-label" cssClass="control-label"><fmt:message key="responsibility.form.label.note" /></form:label>
				<div class="controls">
					<form:textarea path="note" cssErrorClass="input-xlarge error" cssClass="input-xlarge" rows="4" cols="120"/>
					<form:errors element="span" cssClass="help-inline" path="note"/>
				</div>
			</div>
			

</fieldset>
			

			
	<%@ include file="/WEB-INF/jsp/_audit_data_fieldset.jsp" %>

			
			<div class="form-actions">
				<button type="submit" class="btn btn-primary" name="_save"><fmt:message key="responsibility.form.button.save"/></button>
				<button type="submit" class="btn btn-inverse" name="_cancel"><fmt:message key="responsibility.form.button.cancel"/></button>
			</div>

		</form:form>
	</div>
</body>

</html>

