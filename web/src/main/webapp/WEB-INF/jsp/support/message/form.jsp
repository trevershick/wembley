<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
<meta name="decorator" content="clean-bootstrap" />
<title>Send a Message</title>
<meta name="where" content="support.sendmessage" />

</head>
<body>

	<div id="content">
		<form:form commandName="messageSubmissionForm" cssClass="form-horizontal well" enctype="multipart/form-data">

<fieldset>
    <legend>Message Data</legend>

			<c:set var="cssClass"><spring:bind path="times"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="data" cssErrorClass="" cssClass="control-label"><fmt:message key="support.message.form.label.times" /></form:label>
				<div class="controls">
					<form:input path="times" />
					<form:errors element="span" cssClass="help-inline" path="times"/>
				</div>
			</div>
		
			<c:set var="cssClass"><spring:bind path="data"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="data" cssErrorClass="" cssClass="control-label"><fmt:message key="support.message.form.label.data" /></form:label>
				<div class="controls">
					<form:textarea path="data" rows="10" cols="120" cssClass="input-xxlarge"/>
					<form:errors element="span" cssClass="help-inline" path="data"/>
				</div>
			</div>


			<c:set var="cssClass"><spring:bind path="file"><c:if test="${status.error}">error</c:if></spring:bind></c:set>
			<div class="control-group ${cssClass }">
				<form:label path="file" cssErrorClass="" cssClass="control-label"><fmt:message key="support.message.form.label.file" /></form:label>
				<div class="controls">
					<input type="file" name="file" id="file"/>
					<form:errors element="span" cssClass="help-inline" path="file"/>
				</div>
			</div>
			
			<pre>
			
			{
"created":"2013-05-05 16:09",
"sourcesystem":"SSO",
"sourcekeycol":"keycol1",
"sourcekeyvalue":"keyval1",
"sourcevalue":"srcvalue1",
"sourceinfo":"srcinfo1",
"sourcerecordid":"srcid1",
"code":1034,
"description":"mdmdesc1",
"type":"mdmobjtype",
"attr":"mdmattrtype",
"value":"mdmattrvalue",
 "id":1237
}
</pre>
	</fieldset>

			
			<div class="form-actions">
				<button type="submit" class="btn btn-primary" name="_submit"><fmt:message key="support.message.form.button.submit"/></button>
				<button type="submit" class="btn" name="_upload"><fmt:message key="support.message.form.button.upload"/></button>
				<button type="submit" class="btn btn-inverse" name="_cancel"><fmt:message key="support.message.form.button.cancel"/></button>
			</div>

		</form:form>
	</div>
</body>

</html>

