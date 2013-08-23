<%@ include file="/WEB-INF/jsp/d.jsp" %>
<html>
<head>
	<title><fmt:message key="i18n.list.title"/></title>
	<meta name="decorator" content="clean-bootstrap" />
	<meta name="where" content="support.i18n.list" />
</head>
<body>

	<div id="content">
		<form:form commandName="i18nsearch" method="POST" id="searchForm" class="form-inline">
	
		<div class="well">
			<form:input path="query" cssClass="input-small"/>
			
			<input type="submit" class="btn btn-search" value="Search" />
			<input type="button" class="btn btn-reset" value="Reset" onclick="$('#q').attr('value','');this.form.submit();"/>
		  	
			<div class="btn-group" id="add-new-button-group">
				<a href="#" id="btn-add-new" class="btn btn-add-new"><i class="icon-white icon-plus"></i><fmt:message key="i18n.list.actions.addnew"/></a>
				<a class="btn dropdown-toggle btn-add-new" data-toggle="dropdown">
				    <span class="caret"></span>
				</a>				
			  <ul class="dropdown-menu">
			  	<li><a href="#" template="task.remed.SCENARIO.description.RULENUMBER">Rule Description</a></li>
			    <li><a href="#" template="task.remed.SCENARIO.approve.RULENUMBER">Rule Approve Action</a></li>
			    <li><a href="#" template="task.remed.SCENARIO.suggest.RULENUMBER">Rule Suggest Action</a></li>
			    <li><a href="#" template="task.remed.SCENARIO.disapprove.RULENUMBER">Rule Disapprove Action</a></li>
			    <li><a href="#" template="task.remed.SCENARIO.ignore.RULENUMBER">Rule Ignore Action</a></li>
			  </ul>
			</div>
			<script>
			function addNew(args) {
				var url = "${pageContext.request.contextPath }${ routes:new('i18n') }";
				if (args) { url += args; }
				document.location = url;
			}
			function showRuleNumberTemplateDialog(theLink) {
				var tmp = theLink.attr("template");
				$("#template").val(tmp);
				$("#override-modal").modal();
			}
			$(function() {
				$("#add-new-button-group").delegate(".dropdown-menu a","click",function(){
					showRuleNumberTemplateDialog($(this));
				});
				$("#btn-add-new").click(function(){
					addNew(null);
				});
			});
			</script>
			
		</div>
		
		<c:if test="${empty i18nsearch.results}">
		
			<div class="alert alert-info">
				<fmt:message key="i18n.list.noresults">
					<fmt:param value="${param.q == null ? '' : param.q}"/>
				</fmt:message>
			</div>
		</c:if>


		<c:if test="${not empty i18nsearch.results }">

		<r2dq:pagination of="${ i18nsearch }"/>

		
		<table class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th><fmt:message key="i18n.list.th.locale"/></th>
					<th><fmt:message key="i18n.list.th.code"/></th>
					<th><fmt:message key="i18n.list.th.text"/></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${i18nsearch.results }" var="r">
					<tr>
						<td class="highlight-search-result">${r.locale }</td>
						<td class="highlight-search-result">${r.code }</td>
						<td class="highlight-search-result">
							<div class="actions">
							    <a class="btn btn-mini" href="${pageContext.request.contextPath }${ routes:edit('i18n', r.id) }"><i class="icon-pencil"></i> <fmt:message key="i18n.list.actions.edit"/></a>
						    	<a class="btn-danger btn btn-mini" onclick="$('#delete-i18n-name').text('${r.code }');$('#delete-i18n-id').text('${r.id }');$('#delete-confirm').modal();"><i class="icon-trash"></i> <fmt:message key="i18n.list.actions.delete"/></a>
							</div>
							<div>${r.text }</div>
 							<div class="audit-data">
							<fmt:message key="i18n.list.td.updated">
								<fmt:param value="${r.auditData.updatedBy }"/>
								<fmt:param value="${r.auditData.updated }"/>
							</fmt:message>
							</div>
						</td>
					</tr>
				</c:forEach>			
			</tbody>
		</table>
		<r2dq:pagination of="${ i18nsearch }"/>
		</c:if>



</form:form>
		<div id="override-modal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		  <div class="modal-header">
		    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
		    <h3 id="myModalLabel">Override Rule</h3>
		  </div>
		  <div class="modal-body">
		    <p>Enter the Rule Number Below</p>
		    <p>Rule Number : <input type="text" placeholder="Rule Number" id="rule-number"/></p>
		    
		    <p>Scenario : 
		    	<select id="scenario" class="input-xlarge">
		    		<option value="nn">Neither MDM nor Source System have a value</option>
		    		<option value="nv">MDM has no value, but Source System does.</option>
		    		<option value="vn">MDM has a value, but Source System doesn't</option>
		    		<option value="vv" selected>MDM and Source System Have Values</option>
		    	</select>
		    </p>
		    <input type="hidden" id="template"/>
		  </div>
		  <div class="modal-footer">
		    <button class="btn btn-primary" id="override-modal-ok">OK</button>
		    <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
		  </div>
		  <script>
		  $(function() {
			  $("#override-modal-ok").click(function(){
				  var tmp = $("#template").val();
				  tmp = tmp.replace(/RULENUMBER/, $("#rule-number").val());
				  tmp = tmp.replace(/SCENARIO/, $("#scenario").val());
				  args = "?code=" + tmp;
				  addNew(args);
			  });
		  });
		  </script>
		</div>
		


		<div class="modal hide fade" id="delete-confirm">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h3>Are you sure?</h3>
			</div>
			<div class="modal-body">
				<p>Are you sure you want to delete '<span id="delete-i18n-name">this</span>' ?</p>
			</div>
			<span class="hide" id="delete-i18n-id"></span>
			<div class="modal-footer">
				<button class="btn btn-inverse" data-dismiss="modal" aria-hidden="true">No</button> 
				<a href="#" onclick="document.location='${pageContext.request.contextPath }/s/support/i18n/' + $('#delete-i18n-id').text() + '/delete';" class="btn btn-danger">Yes, Delete It</a>
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

