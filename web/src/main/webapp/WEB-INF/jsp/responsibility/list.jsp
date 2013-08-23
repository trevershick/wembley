<%@ include file="/WEB-INF/jsp/d.jsp" %>
<html>
<head>
<title>Responsibilities</title>
<meta name="decorator" content="clean-bootstrap" />
<meta name="where" content="admin.responsibility.list" />
</head>
<body>

	<div id="content">
		<form:form commandName="responsibilitysearch" cssClass="form-inline" method="POST">
			<div class="well">
				<form:label path="query">Search</form:label>
				<form:input path="query"/>
				
				
				
			    <form:label path="sourceSystem">Source System</form:label>
			    <form:select path="sourceSystem">
			    	<spring:message var="any" code="defaults.searchform.criteria.dropdown.any" />
			    	<form:option value="" label="${any }"/>
			    	<form:options items="${sourceSystems }" itemValue="identifier" itemLabel="name"/>
			    </form:select>
			    <form:label path="personType">Type</form:label>
			    <form:select path="personType">
			    	<spring:message var="any" code="defaults.searchform.criteria.dropdown.any" />
			    	<form:option value="" label="${any }"/>
					<form:options items="${personTypes }"/>
			    </form:select>
			    
			    <input type="submit" class="btn btn-search" value="Search"/>
			    <input type="button" class="btn btn-reset" value="Reset" onclick="$('#query').val('');$('#personType').val('');$('#sourceSystem').val('');this.form.submit();"/>
		  		<a class="btn btn-add-new" href="${pageContext.request.contextPath }/s/admin/responsibility/new"><i class="icon-plus"></i> <fmt:message key="responsibility.list.actions.addnew"/></a>
			</div>
	

		<c:if test="${not empty responsibilitysearch.results }">
		
		<r2dq:pagination of="${ responsibilitysearch }"/>
		
		
		<table class="table table-striped table-bordered table-condensed">
			<colgroup>
				<col />
				<col width="5%"/>
				<col />
				<col />
				<col  width="5%" />
				<col />
				<col width="10%" align="right"/>
			</colgroup>
			<thead>
				<tr>
					<th><fmt:message key="responsibility.list.th.sourcesystem"/></th>
					<th><fmt:message key="responsibility.list.th.rulenumber"/></th>
					<th><fmt:message key="responsibility.list.th.assignmenttype"/></th>
					<th><fmt:message key="responsibility.list.th.precedence"/></th>
					<th><fmt:message key="responsibility.list.th.status"/></th>
					<th><fmt:message key="responsibility.list.th.updater"/></th>
					<th class="p_actions"><fmt:message key="responsibility.list.th.actions"/></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${responsibilitysearch.results }" var="r">
					<tr id="ss_${r.id }" class="${r.deleted ? 'warning' : '' }">
						<td class="">
						<c:choose>
							<c:when test="${empty r.sourceSystem }">
								<span class="label label-inverse">default</span>
							</c:when>
							<c:otherwise>
								${r.sourceSystem.name }
							</c:otherwise>
						</c:choose>
						
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
						<td>${r.precedence }</td>
						<td class="p_deleted">
						<c:if test="${r.deleted }">
						<span class="label label-warning"><fmt:message key="responsibility.list.td.status.deleted"/></span>
						</c:if>
						<c:if test="${ not r.deleted }">
						<span class="label label-success"><fmt:message key="responsibility.list.td.status.notdeleted"/></span>
							</c:if>
						</td>
						<td>
							<fmt:message key="responsibility.list.td.updated">
								<fmt:param value="${r.auditData.updatedBy }"/>
								<fmt:param value="${r.auditData.updated }"/>
							</fmt:message>
						</td>
						
						<td class="p_actions">
						    <c:if test="${ r.deleted }">
						    	<a class="btn btn-mini" onclick="$('#undelete-responsibility-name').text('${r.ruleNumber }');$('#undelete-responsibility-id').text('${r.id }');$('#undelete-confirm').modal();"><i class="icon-retweet"></i> <fmt:message key="responsibility.list.actions.undelete"/></a>
						    </c:if>
						    <c:if test="${ not r.deleted }">
							    <a class="btn btn-mini" href="${pageContext.request.contextPath }/s/admin/responsibility/${r.id}"><i class="icon-pencil"></i> <fmt:message key="responsibility.list.actions.edit"/></a>
						    	<a class="btn-danger btn btn-mini" onclick="$('#delete-responsibility-name').text('${r.ruleNumber }');$('#delete-responsibility-id').text('${r.id }');$('#delete-confirm').modal();"><i class="icon-trash"></i> <fmt:message key="responsibility.list.actions.delete"/></a>
						    </c:if>
						</td>
					</tr>
				</c:forEach>			
			</tbody>
		</table>
		</c:if>

		</form:form>

		<c:if test="${empty responsibilitysearch.results  }">
			<div class="alert alert-info">
				<fmt:message key="responsibility.list.noresults">
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
				<p>Are you sure you want to delete '<span id="delete-responsibility-name">this</span>' ?</p>
			</div>
			<span class="hide" id="delete-responsibility-id"></span>
			<div class="modal-footer">
				<button class="btn btn-inverse" data-dismiss="modal">No</button> 
				<a href="#" onclick="document.location='${pageContext.request.contextPath }/s/admin/responsibility/' + $('#delete-responsibility-id').text() + '/delete';" class="btn btn-danger">Yes, Delete It</a>
			</div>
		</div>
		<div class="modal hide fade" id="undelete-confirm">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h3>Are you sure?</h3>
			</div>
			<div class="modal-body">
				<p>Are you sure you want to delete '<span id="undelete-responsibility-name">this</span>' ?</p>
			</div>
			<span class="hide" id="undelete-responsibility-id"></span>
			<div class="modal-footer">
				<button class="btn btn-inverse" data-dismiss="modal">No</button> 
				<a href="#" onclick="document.location='${pageContext.request.contextPath }/s/admin/responsibility/' + $('#undelete-responsibility-id').text() + '/undelete';" class="btn btn-danger">Yes, Delete It</a>
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

