<%@ include file="/WEB-INF/jsp/d.jsp" %>


			<c:set var="cssClass"><spring:bind path="r2dqproperty.code"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="code" cssClass="control-label"><fmt:message key="r2dqproperty.form.label.code"/></form:label>
			
				<div class="controls">
					<form:input path="code" placeholder="Code" cssClass="input-large"/>
					<form:errors element="span" cssClass="help-inline" path="code"/>
				</div>
			</div>

			<c:set var="cssClass"><spring:bind path="r2dqproperty.name"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="name" cssClass="control-label"><fmt:message key="r2dqproperty.form.label.name"/></form:label>
			
				<div class="controls">
					<form:input path="name" placeholder="Name" cssClass="input-xlarge"/>
					<form:errors element="span" cssClass="help-inline" path="name"/>
				</div>
			</div>
			
			<c:set var="cssClass"><spring:bind path="r2dqproperty.value"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="name" cssClass="control-label"><fmt:message key="r2dqproperty.form.label.value"/></form:label>
			
				<div class="controls">
					<form:input path="value" placeholder="Value" cssClass="input-xlarge"/>
					<form:errors element="span" cssClass="help-inline" path="value"/>
				</div>
			</div>

			<c:set var="cssClass"><spring:bind path="r2dqproperty.description"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="name" cssClass="control-label"><fmt:message key="r2dqproperty.form.label.description"/></form:label>
			
				<div class="controls">
					<form:input path="description" placeholder="Description" cssClass="input-xxlarge"/>
					<form:errors element="span" cssClass="help-inline" path="description"/>
				</div>
			</div>