<%@ include file="/WEB-INF/jsp/d.jsp" %>
<html>
<head>
<title>Tasks</title>
<meta name="decorator" content="clean-bootstrap" />
<meta name="where" content="admin.admintasks" />
</head>
<body>

	<div id="content">
		<form:form commandName="admintasksearch" cssClass="form-inline" method="POST">
		<div class="well control-row">
			<form:label path="query">Search</form:label>
			<form:input path="query"/>
			
			
			<form:label path="personType">Assignee</form:label>
		    <form:select path="personType">
		    	<spring:message var="any" code="defaults.searchform.criteria.dropdown.any" />
		    	<form:option value="" label="${any }"/>
				<form:options items="${personTypes }"/>
		    </form:select>
			<form:input path="person" cssClass="input-small"/>
			
			
			
			<label class="checkbox">
			<form:checkbox path="includeCompleted" onchange="this.form.submit();"/>	
			<fmt:message key="mytasks.form.includecompleted"/>
			</label>
			
		     
		    <input type="submit" class="btn btn-search" value="Search"/>
		    <input type="button" class="btn btn-reset" value="Reset" onclick="$('#query').val('');this.form.submit();"/>
		    
		</div>
			<script>
			function enableDisableToolbarActions() {
				var c = $("#content input.selectedTaskId:checked").length > 0;
				$("#_reassignToolbarAction").toggle(c);
			}
			$(function(){
				$("#_reassignToolbarAction").click(function(){
					$("#reassignform").modal();
				});
				$("#content").delegate(".selectedTaskId", "change", function(){
					enableDisableToolbarActions();
				});
				enableDisableToolbarActions();
			});
			</script>
		<r2dq:pagination of="${ admintasksearch }">
			<div id="toolbar-actions" class="btn-group">
				<input type="button" class="btn btn-mini" id="_reassignToolbarAction" value="Reassign" style="display:none;"/>
			</div>
		</r2dq:pagination>
		
		<c:if test="${not empty admintasksearch.results }">
		
		
		<table class="table table-striped table-bordered table-condensed">
			<colgroup>
			</colgroup>
			<thead>
				<tr>
					<th></th>
					<th></th>
					<th><fmt:message key="mytasks.list.th.name"/></th>
					<th><fmt:message key="mytasks.list.th.assignee"/></th>
					<th><fmt:message key="mytasks.list.th.createdwhen"/></th>
					<th><fmt:message key="mytasks.list.th.donewhen"/></th>
					<th><fmt:message key="mytasks.list.th.duewhen"/></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${admintasksearch.results }" var="r">
					<tr>
						<td>
							<form:checkbox cssClass="selectedTaskId" path="selectedTaskIds" value="${r.id }"/>
						</td>
						<td>${r.id }</td>
						<td>
							<c:if test="${r.done }">
							<i class="icon-ok"></i>
							${r.name }
							</c:if>
							
							<c:if test="${ not r.done }">
							
							<a href="${pageContext.request.contextPath }${ routes:view('admintasks', r.id) }">
								${r.name }
							</a>
							</c:if>
							<div>${r.description}</div>
						</td>
						
						<td>
							${r.who.type } - ${r.who.id }
							<c:if test="${ not empty r.notificationSent }">
								<div>
								notified on <fmt:formatDate value="${r.notificationSent}" dateStyle="short" type="date"/>
								</div>
							</c:if>
							
							
						</td>
						<td>
						<fmt:formatDate value="${r.created}" dateStyle="short" type="date"/>
						</td>
						<td>
						<fmt:formatDate value="${r.doneDate}" dateStyle="short" type="date"/>
						</td>
						<td>
						<fmt:formatDate value="${r.due}" dateStyle="short" type="date"/>
						</td>
						<td>
						
						</td>
					</tr>
				</c:forEach>			
			</tbody>
		</table>
		<r2dq:pagination of="${ admintasksearch }"/>
		</c:if>
		
		
		<div class="modal hide fade" id="reassignform" style="display:none;">
		
		  <div class="modal-header">
		    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		    <h3>Reassign To</h3>
		  </div>
		  <div class="modal-body">
			<form:label path="personType">Assignee</form:label>
		    <form:select path="newPersonType">
		    	<spring:message var="any" code="defaults.searchform.criteria.dropdown.any" />
		    	<form:option value="" label="${any }"/>
				<form:options items="${personTypes }"/>
		    </form:select>
			<form:input path="newPerson"/>
		  </div>
		  <div class="modal-footer">
			<input type="submit" class="btn btn-primary" name="_reassign" id="_reassign" value="Reassign"/>		    
		    <a href="#" class="btn" data-dismiss="modal">Close</a>
		  </div>
		</div>
		

		</form:form>

		<c:if test="${empty admintasksearch.results  }">
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

