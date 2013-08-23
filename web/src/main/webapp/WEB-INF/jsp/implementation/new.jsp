<%@ include file="/WEB-INF/jsp/d.jsp" %>
<html>
<head>
<meta name="decorator" content="clean-bootstrap" />
<meta name="where" content="admin.implementation.new" />
<title><fmt:message key="implementation.title.new" /></title>
</head>
<body>

	<div id="content">
		
	
		<form:form commandName="implementation" cssClass="form-horizontal well">


<fieldset>
    <legend><fmt:message key="implementation.title.new" /></legend>

			<c:set var="cssClass"><spring:bind path="implementation.sourceSystem"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="id" cssErrorClass="control-label" cssClass="control-label"><fmt:message key="implementation.form.label.sourceSystem" /></form:label>
				<div class="controls">
					<form:select path="sourceSystem" cssErrorClass="error">
						<spring:message var="any" code="implementation.sourcesystem.useasdefault" text=" - Select - "/>
				    	<form:option value="" label="${any }"/>
				    	<form:options items="${sourceSystems }" itemValue="identifier" itemLabel="name"/>
		    		</form:select>
					<form:errors element="span" cssClass="help-inline" path="sourceSystem"/>
				</div>
			</div>


			<c:set var="cssClass"><spring:bind path="implementation.ruleNumber"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="ruleNumber" cssClass="control-label"><fmt:message key="implementation.form.label.ruleNumber"/></form:label>
			
				<div class="controls">
					<form:input path="ruleNumber" placeholder="Rule Number" cssErrorClass="input-small error" cssClass="input-small"  />
					<form:errors element="span" cssClass="help-inline" path="ruleNumber"/>
				</div>
			</div>
			<c:set var="cssClass"><spring:bind path="implementation.implementationType"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<c:set var="cssClass2"><spring:bind path="implementation.validImplementionType"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass } ${cssClass2}">
				<form:label path="implementationType" cssErrorClass="control-label" cssClass="control-label"><fmt:message key="implementation.form.label.implementationType" /></form:label>
				<div class="controls">
					<form:select path="implementationType" cssErrorClass="error">
						<spring:message var="any" code="implementation.sourcesystem.useasdefault" text=" - Select - "/>
				    	<form:option value="" label="${any }"/>
						<form:options items="${implementationTypes }"/>
		    		</form:select>
					<form:errors element="span" cssClass="help-inline" path="implementationType"/>
					<form:errors element="span" cssClass="help-inline" path="validImplementionType"/>
				</div>
			</div>

			<c:set var="cssClass"><spring:bind path="implementation.precedence"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="id" cssErrorClass="control-label" cssClass="control-label"><fmt:message key="implementation.form.label.precedence" /></form:label>
				<div class="controls">
					<form:input path="precedence" placeholder="" cssErrorClass="input-small error" cssClass="input-small" maxlength="10"/>
					<form:errors element="span" cssClass="help-inline" path="precedence"/>
				</div>
			</div>

			<c:set var="cssClass"><spring:bind path="implementation.note"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="id" cssErrorClass="control-label" cssClass="control-label"><fmt:message key="implementation.form.label.note" /></form:label>
				<div class="controls">
					<form:textarea path="note" cssErrorClass="input-xlarge error" cssClass="input-xlarge" rows="4" cols="120"/>
					<form:errors element="span" cssClass="help-inline" path="note"/>
				</div>
			</div>
			
				
			

</fieldset>			

			


			
			<div class="form-actions">
				<button type="submit" class="btn btn-primary" name="_save"><fmt:message key="implementation.form.button.save"/></button>
				<button type="submit" class="btn btn-inverse" name="_cancel"><fmt:message key="implementation.form.button.cancel"/></button>
			</div>

		</form:form>
		
		<script>
		$(function(){ 
			$("input[type=text]").each(function(idx,el) {
				if (!$(el).hasClass("error")) {
					$(el).tooltip({placement:'right'}); 
				}
			});
		});
		
		</script>
	</div>
</body>

</html>

