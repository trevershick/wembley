<%@ include file="/WEB-INF/jsp/d.jsp" %>
<html>
<head>
<meta name="decorator" content="clean-bootstrap" />
<meta name="where" content="admin.dataexception.new" />
<title><fmt:message key="dataexception.title.new" /></title>
</head>
<body>

	<div id="content">
		
	
		<form:form commandName="dataexception" cssClass="form-horizontal well">


<fieldset>
    <legend><fmt:message key="dataexception.title.new" /></legend>
		


			<c:set var="cssClass"><spring:bind path="dataexception.sourceSystem"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="id" cssErrorClass="control-label" cssClass="control-label"><fmt:message key="dataexception.form.label.sourceSystem" /></form:label>
				<div class="controls">
				
					<form:select path="sourceSystem" cssErrorClass="error">
				    	<spring:message var="any" code="dataexception.sourcesystem.useasdefault" text=" - use as default - "/>
				    	<form:option value="" label="${any }"/>
					
				    	<form:options items="${sourceSystems }" itemValue="identifier" itemLabel="name"/>
		    		</form:select>
					<form:errors element="span" cssClass="help-inline" path="sourceSystem"/>
				</div>
			</div>


			<c:set var="cssClass"><spring:bind path="dataexception.ruleNumber"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="ruleNumber" cssClass="control-label"><fmt:message key="dataexception.form.label.ruleNumber"/></form:label>
			
				<div class="controls">
					<form:input path="ruleNumber" placeholder="Rule Number" cssErrorClass="input-small error" cssClass="input-small"  />
					<form:errors element="span" cssClass="help-inline" path="ruleNumber"/>
				</div>
			</div>

		
			    
			<c:set var="cssClass"><spring:bind path="dataexception.personType"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="id" cssErrorClass="control-label" cssClass="control-label"><fmt:message key="dataexception.form.label.personType" /></form:label>
				<div class="controls">
					<form:select path="personType" cssErrorClass="error">
						<form:options items="${personTypes }"/>
		    		</form:select>
					<form:errors element="span" cssClass="help-inline" path="personType"/>
				</div>
			</div>


			    
		
			<c:set var="cssClass"><spring:bind path="dataexception.person"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="id" cssErrorClass="control-label" cssClass="control-label"><fmt:message key="dataexception.form.label.person" /></form:label>
				<div class="controls">
					<form:input path="person" placeholder="" cssErrorClass="input-xlarge error" cssClass="input-xlarge" />
					<form:errors element="span" cssClass="help-inline" path="person"/>
				</div>
			</div>
			
			<c:set var="cssClass"><spring:bind path="dataexception.precedence"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="id" cssErrorClass="control-label" cssClass="control-label"><fmt:message key="dataexception.form.label.precedence" /></form:label>
				<div class="controls">
					<form:input path="precedence" placeholder="" cssErrorClass="input-small error" cssClass="input-small" maxlength="10"/>
					<form:errors element="span" cssClass="help-inline" path="precedence"/>
				</div>
			</div>

			<c:set var="cssClass"><spring:bind path="dataexception.note"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="id" cssErrorClass="control-label" cssClass="control-label"><fmt:message key="dataexception.form.label.note" /></form:label>
				<div class="controls">
					<form:textarea path="note" cssErrorClass="input-xlarge error" cssClass="input-xlarge" rows="4" cols="120"/>
					<form:errors element="span" cssClass="help-inline" path="note"/>
				</div>
			</div>
			
				
			

</fieldset>			

			


			
			<div class="form-actions">
				<button type="submit" class="btn btn-primary" name="_save"><fmt:message key="dataexception.form.button.save"/></button>
				<button type="submit" class="btn btn-inverse" name="_cancel"><fmt:message key="dataexception.form.button.cancel"/></button>
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

