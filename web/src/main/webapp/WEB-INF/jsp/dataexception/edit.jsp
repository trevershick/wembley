<%@include file="/WEB-INF/jsp/d.jsp" %>
<html>
<head>
<meta name="decorator" content="clean-bootstrap" />
<title><fmt:message key="dataexception.title.edit" /></title>
<meta name="where" content="admin.dataexception.edit" />

</head>
<body>

	<div id="content">
	
		<form:form commandName="dataexception" cssClass="form-horizontal well">

<div class="span6">
	<fieldset>
    <legend><fmt:message key="dataexception.title.sourcesystem" /></legend>
    
			<c:set var="cssClass"><spring:bind path="dataexception.sourceSystem"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
			    <form:hidden path="id"/>
				<form:label path="id" cssErrorClass="control-label" cssClass="control-label"><fmt:message key="dataexception.form.label.sourceSystem" /></form:label>
				<div class="controls">
					<form:select path="sourceSystem" cssErrorClass="error">
				    	<spring:message var="any" code="dataexception.sourcesystem.useasdefault" text=" - use as default - "/>
				    	<form:option value="" label="${any }"/>
				    	<form:options items="${dataexception.sourceSystems }" itemValue="identifier" itemLabel="name"/>
		    		</form:select>
					<form:errors element="span" cssClass="help-inline" path="sourceSystem"/>
				</div>
			</div>
			


			
			
			<c:set var="cssClass"><spring:bind path="dataexception.sourceSystemObjectData"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="sourceSystemObjectData" cssClass="control-label"><fmt:message key="dataexception.form.label.sourceSystemObjectData"/></form:label>
			
				<div class="controls">
					<form:input path="sourceSystemObjectData" placeholder="" cssErrorClass="input-large error" cssClass="input-large"  />
					<form:errors element="span" cssClass="help-inline" path="sourceSystemObjectData"/>
				</div>
			</div>
			
			<c:set var="cssClass"><spring:bind path="dataexception.sourceSystemValue"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="sourceSystemValue" cssClass="control-label"><fmt:message key="dataexception.form.label.sourceSystemValue"/></form:label>
			
				<div class="controls">
					<form:input path="sourceSystemValue" placeholder="" cssErrorClass="input-large error" cssClass="input-large"  />
					<form:errors element="span" cssClass="help-inline" path="sourceSystemValue"/>
				</div>
			</div>

			<c:set var="cssClass"><spring:bind path="dataexception.sourceSystemKeyColumn"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="sourceSystemKeyColumn" cssClass="control-label"><fmt:message key="dataexception.form.label.sourceSystemKeyColumn"/></form:label>
			
				<div class="controls">
					<form:input path="sourceSystemKeyColumn" placeholder="Rule Number" cssErrorClass="input-small error" cssClass="input-small"  />
					<form:errors element="span" cssClass="help-inline" path="sourceSystemKeyColumn"/>
				</div>
			</div>
			
			

			<c:set var="cssClass"><spring:bind path="dataexception.sourceSystemKey"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="sourceSystemKey" cssClass="control-label"><fmt:message key="dataexception.form.label.sourceSystemKeyValue"/></form:label>
			
				<div class="controls">
					<form:input path="sourceSystemKey" placeholder="Source System Key" cssErrorClass="input-small error" cssClass="input-small"  />
					<form:errors element="span" cssClass="help-inline" path="sourceSystemKey"/>
				</div>
			</div>
			

			<!--  end source system stuff -->
			
			</fieldset>
</div>
<div class="span6">			
			
			

	<fieldset>
    <legend><fmt:message key="dataexception.title.mdm" /></legend>

			<c:set var="cssClass"><spring:bind path="dataexception.mdmObjectType"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="id" cssErrorClass="control-label" cssClass="control-label"><fmt:message key="dataexception.form.label.mdmObjectType" /></form:label>
				<div class="controls">
					<form:input path="mdmObjectType" cssErrorClass="input-xlarge error" cssClass="input-large" />
					<form:errors element="span" cssClass="help-inline" path="mdmObjectType"/>
				</div>
			</div>

			<c:set var="cssClass"><spring:bind path="dataexception.mdmObjectAttribute"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="id" cssErrorClass="control-label" cssClass="control-label"><fmt:message key="dataexception.form.label.mdmObjectAttribute" /></form:label>
				<div class="controls">
					<form:input path="mdmObjectAttribute" cssErrorClass="input-xlarge error" cssClass="input-xlarge" />
					<form:errors element="span" cssClass="help-inline" path="mdmObjectAttribute"/>
				</div>
			</div>
			
			<c:set var="cssClass"><spring:bind path="dataexception.mdmAttributevalue"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="id" cssErrorClass="control-label" cssClass="control-label"><fmt:message key="dataexception.form.label.mdmAttributevalue" /></form:label>
				<div class="controls">
					<form:input path="mdmAttributevalue" cssErrorClass="input-xlarge error" cssClass="input-xlarge" />
					<form:errors element="span" cssClass="help-inline" path="mdmAttributevalue"/>
				</div>
			</div>
	</fieldset>
				</div>
			
			

			<div class="span6" style="clear:both;">
				<fieldset>
				<legend><fmt:message key="dataexception.title.ruledetails" /></legend>

				<c:set var="cssClass"><spring:bind path="dataexception.ruleNumber"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
				<div class="control-group ${cssClass }">
					<form:label path="ruleNumber" cssClass="control-label"><fmt:message key="dataexception.form.label.ruleNumber"/></form:label>
				
					<div class="controls">
						<form:input path="ruleNumber" placeholder="Rule Number" cssErrorClass="input-small error" cssClass="input-small"  />
						<form:errors element="span" cssClass="help-inline" path="ruleNumber"/>
					</div>
				</div>
	
				<c:set var="cssClass"><spring:bind path="dataexception.description"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
				<div class="control-group ${cssClass }">
					<form:label path="id" cssErrorClass="control-label" cssClass="control-label"><fmt:message key="dataexception.form.label.description" /></form:label>
					<div class="controls">
						<form:textarea path="description"  cssErrorClass="input-xlarge error" cssClass="input-xlarge"  rows="4"/>
						<form:errors element="span" cssClass="help-inline" path="description"/>
					</div>
				</div>
			</fieldset>
			
			</div>
			    
			<div class="span6">
			 	<fieldset>
			 	<legend><fmt:message key="dataexception.title.responsibleperson" /></legend>
				<c:set var="cssClass"><spring:bind path="dataexception.personType"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
				<div class="control-group ${cssClass }">
					<form:label path="id" cssErrorClass="control-label" cssClass="control-label"><fmt:message key="dataexception.form.label.personType" /></form:label>
					<div class="controls">
						<form:select path="personType" cssErrorClass="error">
							<form:options items="${dataexception.personTypes }"/>
			    		</form:select>
						<form:errors element="span" cssClass="help-inline" path="personType"/>
					</div>
				</div>
				<div class="control-group ${cssClass }">
					<form:label path="id" cssErrorClass="control-label" cssClass="control-label"><fmt:message key="dataexception.form.label.person" /></form:label>
					<div class="controls">
						<form:input path="person" placeholder="" cssErrorClass="input-xlarge error" cssClass="input-xlarge" />
						<form:errors element="span" cssClass="help-inline" path="person"/>
					</div>
				</div>

				</fieldset>
			</div>

			<div class="span12">
				<fieldset>
					<legend><fmt:message key="dataexception.title.disposition" /></legend>
					<div class="control-group">
						<form:label path="implementationType" cssErrorClass="control-label" cssClass="control-label"><fmt:message key="dataexception.form.label.implementationType" /></form:label>
						<div class="controls">
							<form:select path="implementationType" cssErrorClass="error">
								<form:options items="${dataexception.implementationTypes }"/>
				    		</form:select>
						</div>
					</div>

					<div class="control-group">
						<form:label path="implementationDisposition" cssErrorClass="control-label" cssClass="control-label"><fmt:message key="dataexception.form.label.implementationDisposition" /></form:label>
						<div class="controls">
							<form:select path="implementationDisposition" cssErrorClass="error">
								<form:options items="${dataexception.implementationDispositions }"/>
				    		</form:select>
						</div>
					</div>

				</fieldset>
			</div>
			
			<div class="span12">
				<fieldset>
					<legend><fmt:message key="dataexception.title.approval" /></legend>
					<div class="control-group">
						<form:label path="approvalDisposition" cssErrorClass="control-label" cssClass="control-label"><fmt:message key="dataexception.form.label.approvalDisposition" /></form:label>
						<div class="controls">
							<form:select path="approvalDisposition" cssErrorClass="error">
								<form:options items="${dataexception.approvalDispositions }"/>
				    		</form:select>
						</div>
					</div>

					<div class="control-group">
						<form:label path="implementationDisposition" cssErrorClass="control-label" cssClass="control-label"><fmt:message key="dataexception.form.label.userComment" /></form:label>
						<div class="controls">
							<form:input path="userComment" cssClass="input-xxlarge"/>
						</div>
					</div>

				</fieldset>
			</div>
			

			<div class="span12">
				<fieldset>
					<legend><fmt:message key="dataexception.title.details" /></legend>
					<div class="control-group">
						<form:label path="exceptionCreated" cssErrorClass="control-label" cssClass="control-label"><fmt:message key="dataexception.form.label.exceptionCreated" /></form:label>
						<div class="controls">
						<form:input path="exceptionCreated" cssErrorClass="input-large error" cssClass="input-large" readonly="true"/>
						</div>
					</div>
				</fieldset>
			</div>

	<div class="span12">
	<%@ include file="/WEB-INF/jsp/_audit_data_fieldset.jsp" %>
	</div>

			
			<div class="form-actions span12" style="clear:both;">
				<div class="pull-right">
				<button type="submit" class="btn btn-primary" name="_save"><fmt:message key="dataexception.form.button.save"/></button>
				<button type="submit" class="btn btn-inverse" name="_cancel"><fmt:message key="dataexception.form.button.cancel"/></button>
				</div>
			</div>
	<div style="clear:both;"></div>
		</form:form>
	</div>
</body>

</html>

