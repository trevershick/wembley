<%@ include file="/WEB-INF/jsp/d.jsp" %>
<html>
<head>
	<title><fmt:message key="r2dqproperty.list.title"/></title>
	<meta name="decorator" content="clean-bootstrap" />
	<meta name="where" content="support.r2dqproperty.list" />
</head>
<body>

	<div id="content">
		<form class="form-inline well">
			    <input type="text" class="span2" name="q" id="q" value="${param.q }" />

			    
			    <input type="submit" class="btn btn-search" value="Search"/>
			    <input type="button" class="btn btn-reset" value="Reset" onclick="$('#q').attr('value','');this.form.submit();"/>
		  	<a class="btn btn-add-new" href="${pageContext.request.contextPath }${ routes:new('r2dqproperty') }"><i class="icon-plus"></i> <fmt:message key="r2dqproperty.list.actions.addnew"/></a>
		</form>
		
		<c:if test="${empty results}">
			<div class="alert alert-info">
				<fmt:message key="r2dqproperty.list.noresults">
					<fmt:param value="${param.q == null ? '' : param.q}"/>
				</fmt:message>
			</div>
		</c:if>


		<c:if test="${not empty results }">
		<table class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th><fmt:message key="r2dqproperty.list.th.id"/></th>
					<th><fmt:message key="r2dqproperty.list.th.name"/></th>
					<th><fmt:message key="r2dqproperty.list.th.value"/></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${results }" var="r">
					<tr id="ss_${r.id }">
						<td class="highlight-search-result">${r.code }</td>
						<td class="highlight-search-result">${r.name }</td>

						<td class="highlight-search-result">
							<div class="actions">
							    <a class="btn btn-mini" href="${pageContext.request.contextPath }${ routes:edit('r2dqproperty', r.id) }"><i class="icon-pencil"></i> <fmt:message key="r2dqproperty.list.actions.edit"/></a>
						    	<a class="btn-danger btn btn-mini" onclick="$('#delete-r2dqproperty-name').text('${r.name }');$('#delete-r2dqproperty-id').text('${r.id }');$('#delete-confirm').modal();"><i class="icon-trash"></i> <fmt:message key="r2dqproperty.list.actions.delete"/></a>
							</div>
							<div>${r.value }</div>
 							<div class="audit-data">
							<fmt:message key="r2dqproperty.list.td.updated">
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







		<div class="modal hide fade" id="delete-confirm">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h3>Are you sure?</h3>
			</div>
			<div class="modal-body">
				<p>Are you sure you want to delete '<span id="delete-r2dqproperty-name">this</span>' ?</p>
			</div>
			<span class="hide" id="delete-r2dqproperty-id"></span>
			<div class="modal-footer">
				<button class="btn btn-inverse" data-dismiss="modal" aria-hidden="true">No</button> 
				<a href="#" onclick="document.location='${pageContext.request.contextPath }/s/support/r2dqproperty/' + $('#delete-r2dqproperty-id').text() + '/delete';" class="btn btn-danger">Yes, Delete It</a>
			</div>
		</div>
	</div>
	
	
    <div id="post-content-js">
	<script>
	$(function() {
		highlightSearchResult($("#q").attr("value"));
	});
	</script>
	</div>	
</body>

</html>

