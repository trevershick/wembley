<%@ include file="/WEB-INF/jsp/d.jsp" %>
<html>
<head>
<title>${taskform.name }</title>
<meta name="decorator" content="clean-bootstrap" />
<meta name="task-name" content="${taskform.name }" />
</head>
<body>

	<div id="task-description">
	${taskform.description }
	</div>
	
	<div id="content">
		<div class="page-header">
  			<h2>Perform Task</h2>
		</div>
		<p class="lead">${taskform.description }</p>
		<p>
		Manual tasks are created for source systems that are not automated.  This means if you choose to accept the data quality advice from
		MDM you <em>MUST MANUALLY</em> update the source system, then complete the task.
		</p>
	
	
		<form:form method="POST" commandName="taskform" cssClass="form form-horizontal">
			<fieldset>
			<legend>Details</legend>
				<c:if test="${not empty taskform.created }">
				<div class="control-group">
					<label class="control-label">Created</label>
					<div class="controls">
						<span class="control-text">
						<fmt:formatDate value="${taskform.created }" dateStyle="short" timeStyle="short" type="both"/>
						</span>
					</div>
				</div>
				</c:if>
				
				<c:if test="${not empty taskform.due }">
				<div class="control-group">
					<label class="control-label">Due</label>
					<div class="controls">
						<span class="control-text">${taskform.due }</span>
					</div>
				</div>
				</c:if>
			</fieldset>
		
			<fieldset>
				<legend>Data Values</legend>


				
				
					<c:forEach items="${taskform.exceptions }" var="r" varStatus="s">
						<form:hidden path="exceptions[${s.index }].exceptionId"/>
						<c:choose>
							<c:when test="${ r.failingAssertion}">
								<div class="control-group failing-assertion">
									<label class="control-label">${r.fieldTitle }</label>
									<div class="controls row">
											<div class="control-text span5">
												<p>
												<c:set var="desckey" value="${r.descriptionKey}"/>
												<spring:message code="${desckey }" text="${ r.violationDescription }" htmlEscape="false" arguments="${r.fieldTitle },${r.actualValue },${r.expectedValue },${r.sourceSystem }"/>
												</p>
											</div>
										
											<div class="remediation-actions pull-right span6">
												<h3>Actions</h3>
												<c:if test="${r.canApprove }">
													<label class="radio text-success">
													  <form:radiobutton path="exceptions[${s.index }].action" value="approve"/>
													  <c:set var="def">
													  Yes, '${r.expectedValue }' is the correct value for '${r.fieldTitle }'
													  </c:set>
													  <spring:message text="${def }" code="${r.approveActionKey }" htmlEscape="false" arguments="${r.fieldTitle },${r.actualValue },${r.expectedValue },${r.sourceSystem }"/>
												</label>
												</c:if>
			
												<c:if test="${r.canDisapprove }">
												<label class="radio text-error">
												  <form:radiobutton path="exceptions[${s.index }].action" value="disapprove"/>
													  	<c:set var="def">
														No, '${r.expectedValue }' is an incorrect value for '${r.fieldTitle }', '${r.actualValue }' is the correct value.
														</c:set>
														<spring:message text="${def }" code="${r.disapproveActionKey }" htmlEscape="false" arguments="${r.fieldTitle },${r.actualValue },${r.expectedValue },${r.sourceSystem }"/>
												</label>
												</c:if>
			
												<c:if test="${r.canIgnore }">
												<label class="radio text-warning">
												  <form:radiobutton path="exceptions[${s.index }].action" value="ignore"/>
													  	<c:set var="def">
												  			Well, '${r.actualValue }' is the correct value in ${r.sourceSystem }, please do not notify me again.
												  		</c:set>
												  		<spring:message text="${def }" code="${r.ignoreActionKey }" htmlEscape="false" arguments="${r.fieldTitle },${r.actualValue },${r.expectedValue },${r.sourceSystem }"/>
												  		
												</label>
												</c:if>
												
												<label class="control-label">Comment</label>
												<div class="controls">
													<form:input path="exceptions[${s.index }].userComment" cssClass="input-xlarge" />
												</div>
												
												
											</div>
									</div>
								</div>							
							</c:when>
							<c:otherwise>
								<div class="control-group">
									<label class="control-label">${r.fieldTitle }</label>
									<div class="controls">
										<span class="control-text">
											${r.actualValue}
										</span>
									</div>
								</div>
							</c:otherwise>
						</c:choose>
					
					
						
					</c:forEach>
			
				
			</fieldset>
			<div class="form-actions">
				<div class="pull-right">
			    <input type="submit" class="btn btn-ok" value="OK" name="_ok"/>
			    <input type="submit" class="btn btn-cancel" value="Cancel" name="_cancel"/>
			    </div>
		    </div>
		</form:form>
	</div>

</body>

</html>

