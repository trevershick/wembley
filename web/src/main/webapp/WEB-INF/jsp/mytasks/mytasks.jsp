<%@ include file="/WEB-INF/jsp/d.jsp" %>
<html>
<head>
<title>My Tasks</title>
<meta name="decorator" content="clean-bootstrap" />
<meta name="where" content="my.mytasks" />
</head>
<body>

	<div id="content">
			<form:form commandName="tasksearch" cssClass="form-inline" method="POST">
		<div class="well control-row">
			<!--<form:label path="query">Search</form:label>
			<form:input path="query"/>-->
			
			
			
			<label class="checkbox">
			<form:checkbox path="includeCompleted" onchange="this.form.submit();"/>	
			<fmt:message key="mytasks.form.includecompleted"/>
			</label>
			
		    <!-- 
		    <input type="submit" class="btn btn-search" value="Search"/>
		    <input type="button" class="btn btn-reset" value="Reset" onclick="$('#query').val('');$('#personType').val('');$('#sourceSystem').val('');this.form.submit();"/>
		     -->
		</div>
		
		
		<c:if test="${not empty tasksearch.results }">
		
			<r2dq:pagination of="${ tasksearch }"/>
		
		
			<table class="table table-striped table-bordered table-condensed">
				<colgroup>
				</colgroup>
				<thead>
					<tr>
						<th><fmt:message key="mytasks.list.th.name"/></th>
						<th><fmt:message key="mytasks.list.th.description"/></th>
						<th><fmt:message key="mytasks.list.th.createdwhen"/></th>
						<c:if test="${tasksearch.includeCompleted }">
						<th><fmt:message key="mytasks.list.th.donewhen"/></th>
						</c:if>
						<th><fmt:message key="mytasks.list.th.duewhen"/></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${tasksearch.results }" var="r">
						<tr>
						
		
							<td>
								<c:if test="${r.done }">
								<i class="icon-ok"></i>
								${r.name }
								</c:if>
								
								<c:if test="${ not r.done }">
								<spring:url value="/s/tasks/mytasks/{taskid}" var="taskUrl">
									<spring:param name="taskid" value="${r.id }"/>
								</spring:url>
								<a href="${taskUrl }">
									${r.name }
								</a>
								</c:if>
							</td>
							<td>
		${r.description}
							</td>
							<td>
							<fmt:formatDate value="${r.created}" dateStyle="short" type="date"/>
							</td>
						<c:if test="${tasksearch.includeCompleted }">
							<td>
							<fmt:formatDate value="${r.doneDate}" dateStyle="short" type="date"/>
							</td>
						</c:if>
							<td>
							<fmt:formatDate value="${r.due}" dateStyle="short" type="date"/>
							</td>
						</tr>
					</c:forEach>			
				</tbody>
			</table>
			<r2dq:pagination of="${ tasksearch }"/>
		</c:if>

		</form:form>

		<c:if test="${empty tasksearch.results  }">
			<div class="alert alert-info">
				<fmt:message key="mytasks.list.noresults">
					<fmt:param value="${param.q }"/>
				</fmt:message>
			</div>
		</c:if>




	</div>
	
	<div id="post-content-js">
				<script>
				$(function() {
					highlightSearchResult($("#query").attr("value"));
				});
				</script>	
	</div>
</body>

</html>

