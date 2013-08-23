<%@ include file="/WEB-INF/jsp/d.jsp" %>
<html>
<head>
<title>Messages</title>
<meta name="decorator" content="clean-bootstrap" />
<meta name="where" content="support.messagelist" />
</head>
<body>

	<div id="content">
		<form:form commandName="messageSearchForm" cssClass="form-inline" method="POST" id="searchForm">
		
			<div class="well">
				<form:label path="query">Search</form:label>
				<form:input path="query" cssClass="input-medium"/>
				<form:label path="type">Type</form:label>
			    <form:select path="type" cssClass="input-small">
			    	<spring:message var="any" code="defaults.searchform.criteria.dropdown.any" />
			    	<form:option value="" label="${any }"/>
					<form:options items="${messageSearchForm.typeOptions }"/>
			    </form:select>
				
			    <form:label path="source">Source</form:label>
			    <form:select path="source" cssClass="input-small">
			    	<spring:message var="any" code="defaults.searchform.criteria.dropdown.any" />
			    	<form:option value="" label="${any }"/>
			    	<form:options items="${messageSearchForm.sourceOptions }" />
			    </form:select>
			    <form:label path="processed">Processed</form:label>
			    <form:select path="processed" cssClass="input-small">
			    	<spring:message var="any" code="defaults.searchform.criteria.dropdown.any" />
			    	<form:option value="" label="${any }"/>
					<form:options items="${messageSearchForm.processedOptions }"/>
			    </form:select>
			    
			    <input type="submit" class="btn btn-search" value="Search"/>
			    <input type="button" class="btn btn-reset" value="Reset" onclick="$('#q').attr('value','');this.form.submit();"/>
		  		
				<sec:authorize url="/s/support/message/send">
				<a class="btn btn-success" href="${ pageContext.request.contextPath }/s/support/message/send"><i class="icon-plus"></i> <fmt:message key="message.list.actions.addnew"/></a>
				</sec:authorize>
		
</div>




		

		<c:if test="${not empty messageSearchForm.results }">

		
		<r2dq:pagination of="${ messageSearchForm }"/>
		
		
		<table class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th></th>
					<th ><fmt:message key="message.list.th.sourceOrDest"/></th>
					<th ><fmt:message key="message.list.th.type"/></th>
					<th><fmt:message key="message.list.th.status"/></th>
					<th ><fmt:message key="message.list.th.data"/></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${messageSearchForm.results }" var="r">
					<tr id="ss_${r.identifier }">
						<td>${r.identifier }</td>
						<td class="">${r.sourceOrDest  }</td>
						<td class="">
							<div class="actions">
								<c:if test="${ r.outbound}">
									<i class="icon-share-alt"></i>
						    	</c:if>
						    	<c:if test="${ r.inbound}">
									<i class="icon-arrow-down"></i>
						    	</c:if>
						    </div>
						</td>
						<td class="p_deleted">
						<c:if test="${not r.processed }">
						<span class="label label-warning"><fmt:message key="message.list.td.status.notprocessed"/></span>
						</c:if>
						<c:if test="${ r.processed }">
						<span class="label label-success"><fmt:message key="message.list.td.status.processed"/></span>
							</c:if>
						</td>
						<td class="highlight-search-result">
							<div class="actions">
								<c:if test="${ not r.processed || r.outbound}">
						    	<a class="btn-inverse btn btn-mini" onclick="$('#message-data').text('${r.identifier }');$('#reprocess-id').text('${r.identifier }');$('#reprocess-confirm').modal();"> <fmt:message key="message.list.actions.process"/></a>
							    <a class="btn btn-mini" href="${pageContext.request.contextPath }/s/support/message/${r.identifier}"><i class="icon-pencil"></i> <fmt:message key="message.list.actions.edit"/></a>
						    	</c:if>
						    	
						    </div>
						
							<div class="raw-message-data">
								<c:if test="${r.data.length()<250}"><c:out value="${r.data}"/></c:if>
								<c:if test="${r.data.length()>250}"><c:out value="${r.data.substring(0,250)  }"/></c:if>
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
		
		<r2dq:pagination of="${ messageSearchForm }"/>
		</c:if>

		</form:form>

		<c:if test="${empty messageSearchForm.results  }">
			<div class="alert alert-info">
				<fmt:message key="message.list.noresults">
					<fmt:param value="${param.q }"/>
				</fmt:message>
			</div>
		</c:if>


		<div class="modal hide fade" id="error-dialog">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h3>Error</h3>
			</div>
			<div class="modal-body">
				<p>An error occurred. Sorry</p>
			</div>
			<div class="modal-footer">
				<a href="#" class="btn" data-dismiss="modal">Close</a>
			</div>
		</div>


		<div class="modal hide fade" id="reprocess-confirm">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h3>Are you sure?</h3>
			</div>
			<div class="modal-body">
				<p>Are you sure you want to reprocess '<span id="message-data">this</span>' ?</p>
			</div>
			<span class="hide" id="reprocess-id"></span>
			<div class="modal-footer">
				<button class="btn btn-inverse" id="n-button">No</button> 
				<a href="#" class="btn btn-success" id="y-button">Yes, Reprocess It</a>
			</div>
			<script>
			$(function(){
				$("#reprocess-confirm").delegate("#n-button","click",function(){ $("#reprocess-confirm").modal('hide'); });
				$("#reprocess-confirm").delegate("#y-button","click",function(){ close(); });
			});
			function close() {
				var u = '${pageContext.request.contextPath }/s/support/message/' + $('#reprocess-id').text() + '/reprocess';
				var closeDialog = function(){$("#reprocess-confirm").modal('hide');};
				var refreshSearch = function(){$("#searchForm").submit();};
				var error = $("#error-dialog");
				$.ajax(u)
					.done(function(data, textStatus, jqXHR) { 
						closeDialog(); 
						refreshSearch(); 
					}).fail(function(jqXHR, textStatus, errorThrown) { 
						closeDialog(); 
						error.modal('show'); 
					});
				
			}
			</script>
		</div>
		
		
	</div>

<div id="post-content-js">
<script>
$(function() {
	//highlightSearchResult($("#query").attr("value"));
});
</script>
</div>

</body>

</html>

