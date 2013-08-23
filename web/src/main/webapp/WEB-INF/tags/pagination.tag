<%@ include file="/WEB-INF/jsp/d.jsp" %>
<%@ attribute name="of" required="true" rtexprvalue="true" type="com.railinc.r2dq.util.PagedSearchForm"%>

<form:hidden path="paging.page" id="currentPage"/>
		<script>
		function gotoPage(idx) {
			$("#currentPage").val(idx);
			$("#currentPage").closest("form").submit();
		}
		</script>

<div class="pagination" style="height:20px;">
	<div class="span2">
	Total Count : <span class="label label-info">${ of.paging.totalCount}</span>
	</div>
	<div class="span8">
	<jsp:doBody/>
	</div>
	
	<%--<c:if test="${of.paging.pageCount gt 1 }"> --%>
	<div class="span8 pull-right">
		<ul class="pull-right">
		    <li class="${of.paging.page eq 0 ? 'disabled' : ''}"><a href="#" onclick="gotoPage(0);">&laquo;</a></li>
			<c:forEach items="${of.paging.staggeredPages}" var="pg">
			<li class="${of.paging.page eq pg ? 'active' : ''}"><a href="#" onclick="gotoPage(${pg});">${pg + 1}</a></li>
			</c:forEach>
			<li class="${of.paging.page eq (of.paging.pageCount - 1) ? 'disabled' : ''}"><a href="#" onclick="gotoPage(${of.paging.pageCount - 1});">&raquo;</a></li>
		</ul>
	</div>
	<%-- </c:if> --%>
</div>
