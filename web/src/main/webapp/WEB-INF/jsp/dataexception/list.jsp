<%@ include file="/WEB-INF/jsp/d.jsp" %>
<html>
<head>
<title>Data Exceptions</title>
<meta name="decorator" content="clean-bootstrap" />
<meta name="where" content="admin.dataexception.list" />
</head>
<body>

	<div id="content">
		<form:form commandName="dataexceptionsearch" cssClass="form-inline" method="POST" id="searchForm">
		<div class="well">
				<form:label path="query">Search</form:label>
				<form:input path="query"/>
				
				
			    <form:label path="sourceSystem">Source System</form:label>
			    <form:select path="sourceSystem">
			    	<spring:message var="any" code="defaults.searchform.criteria.dropdown.any" />
			    	<form:option value="" label="${any }"/>
			    	<form:options items="${dataexceptionsearch.sourceSystems }" itemValue="identifier" itemLabel="name"/>
			    </form:select>

			    
			    <input type="submit" class="btn btn-search" value="Search"/>
			    <input type="button" class="btn btn-reset" value="Reset" onclick="$('#query').val('');$('#personType').val('');$('#sourceSystem').val('');this.form.submit();"/>
		  		<!-- <a class="btn btn-add-new" href="${pageContext.request.contextPath }/s/support/dataexception/new"><i class="icon-plus"></i> <fmt:message key="dataexception.list.actions.addnew"/></a> -->
		</div>
	

		<c:if test="${not empty dataexceptionsearch.results }">

		<r2dq:pagination of="${ dataexceptionsearch }"/>
		
		
		<table class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th></th>
					<th><fmt:message key="dataexception.list.th.sourcesystem"/></th>
					<th><fmt:message key="dataexception.list.th.rulenumber"/></th>
					<th><fmt:message key="dataexception.list.th.assignmenttype"/></th>
					<th><fmt:message key="dataexception.list.th.status"/></th>
					<th><fmt:message key="dataexception.list.th.actual"/> / <fmt:message key="dataexception.list.th.expected"/></th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${dataexceptionsearch.results }" var="r">
					<tr id="ss_${r.id }" class="${r.deleted ? 'warning' : '' }">
						<td>
							${r.id }
						</td>
						<td>
						<c:choose>
							<c:when test="${empty r.sourceSystem }">
								<span class="label label-inverse">default</span>
							</c:when>
							<c:otherwise>
								${r.sourceSystem.name }
							</c:otherwise>
						</c:choose>
							
							<span  class="highlight-search-result">
							<span style="display:none">(${r.sourceSystem})${r.sourceSystemKeyColumn}=</span>( ${r.sourceSystemKey} )
							</span>						
						
						</td>
						<td>
						<c:choose>
							<c:when test="${empty r.ruleNumber }">
								<span class="label label-inverse">default</span>
							</c:when>
							<c:otherwise>
								<span  class="highlight-search-result">${r.ruleNumber }</span>
							</c:otherwise>
						</c:choose>
						</td>
						<td>${r.personType } (<span class="highlight-search-result">${r.person }</span>)</td>
						<td class="p_deleted">
							<c:if test="${r.deleted }">
							<span class="label label-warning"><fmt:message key="dataexception.list.td.status.deleted"/></span>
							</c:if>
							<c:if test="${ not r.deleted }">
							<span class="label label-success"><fmt:message key="dataexception.list.td.status.notdeleted"/></span>
							</c:if>
							<c:if test="${not r.approvalDisposition.initial }">
								<br/><span class="label">${r.approvalDisposition }</span>
							</c:if>
							<c:if test="${not r.implementationDisposition.initial }">
								<br/><span class="label">${r.implementationDisposition }</span>
							</c:if>
						</td>
						<td>
							<div class="actions">
							    <c:if test="${ r.deleted }">
							    	<a class="btn btn-mini" onclick="$('#undelete-dataexception-name').text('${r.ruleNumber } exception from ${r.sourceSystem }');$('#undelete-dataexception-id').text('${r.id }');$('#undelete-confirm').modal();"><i class="icon-retweet"></i> <fmt:message key="dataexception.list.actions.undelete"/></a>
							    </c:if>
							    <c:if test="${ not r.deleted }">
								    <a class="btn btn-mini" href="${pageContext.request.contextPath }/s/support/dataexception/${r.id}"><i class="icon-pencil"></i> <fmt:message key="dataexception.list.actions.edit"/></a>
							    	<a class="btn-danger btn btn-mini" onclick="$('#delete-dataexception-name').text('${r.ruleNumber } exception from ${r.sourceSystem.name }');$('#delete-dataexception-id').text('${r.id }');$('#delete-confirm').modal();"><i class="icon-trash"></i> <fmt:message key="dataexception.list.actions.delete"/></a>
							    </c:if>
							</div>

						<div>
							<span  class="highlight-search-result">
							<span class="highlight-search-result">${r.sourceSystemValue}</span> / 
							<span style="display:none">(${r.mdmObjectType})${r.mdmObjectAttribute}=</span>${r.mdmAttributevalue}</span>
						</div>						
						<div class="audit-data">
								<fmt:message key="message.list.td.updated">
									<fmt:param value="${r.auditData.updatedBy }"/>
									<fmt:param value="${r.auditData.updated }"/>
								</fmt:message>
							</div>
						
						</td>
						
					
						
						
					</tr>
				</c:forEach>			
			</tbody>
		</table>
		</c:if>

		</form:form>

		<c:if test="${empty dataexceptionsearch.results  }">
			<div class="alert alert-info">
				<fmt:message key="dataexception.list.noresults">
					<fmt:param value="${param.q }"/>
				</fmt:message>
			</div>
		</c:if>





		<div class="modal hide fade" id="delete-confirm">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h3>Are you sure?</h3>
			</div>
			<div class="modal-body">
				<p>Are you sure you want to delete '<span id="delete-dataexception-name">this</span>' ?</p>
			</div>
			<span class="hide" id="delete-dataexception-id"></span>
			<div class="modal-footer">
				<button class="btn btn-inverse" data-dismiss="modal">No</button> 
				<a href="#" onclick="document.location='${pageContext.request.contextPath }/s/support/dataexception/' + $('#delete-dataexception-id').text() + '/delete';" class="btn btn-danger">Yes, Delete It</a>
			</div>
		</div>
		<div class="modal hide fade" id="undelete-confirm">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h3>Are you sure?</h3>
			</div>
			<div class="modal-body">
				<p>Are you sure you want to delete '<span id="undelete-dataexception-name">this</span>' ?</p>
			</div>
			<span class="hide" id="undelete-dataexception-id"></span>
			<div class="modal-footer">
				<button class="btn btn-inverse" data-dismiss="modal">No</button> 
				<a href="#" onclick="document.location='${pageContext.request.contextPath }/s/support/dataexception/' + $('#undelete-dataexception-id').text() + '/undelete';" class="btn btn-danger">Yes, Delete It</a>
			</div>
		</div>
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

