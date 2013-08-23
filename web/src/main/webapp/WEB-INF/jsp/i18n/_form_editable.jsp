<%@ include file="/WEB-INF/jsp/d.jsp" %>
			<c:set var="cssClass"><spring:bind path="i18n.locale"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="locale" cssErrorClass="" cssClass="control-label"><fmt:message key="i18n.form.label.locale" /></form:label>
				<div class="controls">
					<form:input path="locale" placeholder="Locale" cssClass="input-xlarge"/>
					<form:errors element="span" cssClass="help-inline" path="locale"/>
				</div>
			</div>
			
			
			<c:set var="cssClass"><spring:bind path="i18n.code"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="code" cssErrorClass="" cssClass="control-label"><fmt:message key="i18n.form.label.code" /></form:label>
				<div class="controls">
					<form:input path="code" placeholder="Code" cssClass="input-xlarge"/>
					<form:errors element="span" cssClass="help-inline" path="code"/>
				</div>
			</div>
			
			
			
			<c:set var="cssClass"><spring:bind path="i18n.text"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="text" cssClass="control-label"><fmt:message key="i18n.form.label.text"/></form:label>
			
				<div class="controls">
				<form:textarea path="text" cols="120" rows="8" cssClass="input-xlarge"/>
					<form:errors element="span" cssClass="help-inline" path="text"/>
				</div>
			</div>
			
