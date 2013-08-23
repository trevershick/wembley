<%@ include file="/WEB-INF/jsp/d.jsp" %>
<html>
<head>
<title>User Groups</title>
<meta name="decorator" content="clean-bootstrap" />
<meta name="where" content="admin.usergroup.list" />
</head>
<body>

	<div id="content">
	
		<form class="form-inline well">
			    <input type="text" class="span2" name="q" id="q" value="${param.q }" />
			    <input type="submit" class="btn btn-search" value="Search"/>
			    <input type="button" class="btn btn-reset" value="Reset" onclick="$('#q').attr('value','');this.form.submit();"/>
		  	<a class="btn btn-add-new" href="${pageContext.request.contextPath }/s/admin/usergroup/new"><i class="icon-plus"></i> <fmt:message key="usergroup.list.actions.addnew"/></a>
		</form>

		<c:if test="${empty results }">
			<div class="alert alert-info">
				<fmt:message key="usergroup.list.noresults">
					<fmt:param value="${param.q }"/>
				</fmt:message>
			</div>
		</c:if>

		<c:if test="${not empty results }">
		<table class="table table-striped table-bordered table-condensed">
			<colgroup>
				<col />
				<col />
				<col />
				<col />
				<col />
				<col width="20%" align="right"/>
			</colgroup>
			<thead>
				<tr>
					<th class="p_id"><fmt:message key="usergroup.list.th.id"/></th>
					<th class="p_name"><fmt:message key="usergroup.list.th.name"/></th>
					<th class="p_deleted"><fmt:message key="usergroup.list.th.status"/></th>
					<th class="p_users"><fmt:message key="usergroup.list.th.users"/></th>
					<th class=""><fmt:message key="usergroup.list.th.updater"/></th>
					<th class="p_actions"><fmt:message key="usergroup.list.th.actions"/></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${results }" var="r">
					<tr id="ss_${r.id }" class="${r.deleted ? 'warning' : '' }">
						<td class="p_id">${r.id  }
						</td>
						<td class="p_name">${r.name }</td>
						<td class="p_deleted">
						<c:if test="${r.deleted }">
						<span class="label label-warning"><fmt:message key="usergroup.list.td.status.deleted"/></span>
						</c:if>
						<c:if test="${ not r.deleted }">
						<span class="label label-success"><fmt:message key="usergroup.list.td.status.notdeleted"/></span>
							</c:if>
						</td>
						<td>
							<span class="badge">${r.memberCount }</span>
								<c:if test="${r.memberCount gt 0 }">
								 -
								<r2dq:joiner on=", " items="${ r.userIdsExcerpt }"/>  
								
								<c:if test="${r.userIdsExcerptSize < r.memberCount}">...</c:if>
							</c:if>
						</td>
						<td>
							<fmt:message key="usergroup.list.td.updated">
								<fmt:param value="${r.auditData.updatedBy }"/>
								<fmt:param value="${r.auditData.updated }"/>
							</fmt:message>
						</td>
						
						<td class="p_actions">
						    <c:if test="${ r.deleted }">
						    	<a class="btn btn-mini" onclick="$('#undelete-usergroup-name').text('${r.name }');$('#undelete-usergroup-id').text('${r.id }');$('#undelete-confirm').modal();"><i class="icon-retweet"></i> <fmt:message key="usergroup.list.actions.undelete"/></a>
						    </c:if>
						    <c:if test="${ not r.deleted }">
							    <a class="btn btn-mini" href="${pageContext.request.contextPath }/s/admin/usergroup/${r.id}"><i class="icon-pencil"></i> <fmt:message key="usergroup.list.actions.edit"/></a>
						    	<a class="btn-danger btn btn-mini" onclick="$('#delete-usergroup-name').text('${r.name }');$('#delete-usergroup-id').text('${r.id }');$('#delete-confirm').modal();"><i class="icon-trash"></i> <fmt:message key="usergroup.list.actions.delete"/></a>
						    </c:if>
						</td>
					</tr>
				</c:forEach>			
			</tbody>
		</table>
		</c:if>







		<div class="modal hide fade" id="delete-confirm">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h3>Are you sure?</h3>
			</div>
			<div class="modal-body">
				<p>Are you sure you want to delete '<span id="delete-usergroup-name">this</span>' ?</p>
			</div>
			<span class="hide" id="delete-usergroup-id"></span>
			<div class="modal-footer">
				<button class="btn btn-inverse" data-dismiss="modal" aria-hidden="true">No</button> 
				<a href="#" onclick="document.location='${pageContext.request.contextPath }/s/admin/usergroup/' + $('#delete-usergroup-id').text() + '/delete';" class="btn btn-danger">Yes, Delete It</a>
			</div>
		</div>
		<div class="modal hide fade" id="undelete-confirm">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h3>Are you sure?</h3>
			</div>
			<div class="modal-body">
				<p>Are you sure you want to delete '<span id="undelete-usergroup-name">this</span>' ?</p>
			</div>
			<span class="hide" id="undelete-usergroup-id"></span>
			<div class="modal-footer">
				<button class="btn btn-inverse" data-dismiss="modal" aria-hidden="true">No</button> 
				<a href="#" onclick="document.location='${pageContext.request.contextPath }/s/admin/usergroup/' + $('#undelete-usergroup-id').text() + '/undelete';" class="btn btn-danger">Yes, Delete It</a>
			</div>
		</div>
	</div>
</body>

</html>

