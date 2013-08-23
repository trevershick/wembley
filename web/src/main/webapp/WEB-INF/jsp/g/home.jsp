<%@ include file="/WEB-INF/jsp/d.jsp" %>
<html>
<head>
	<meta name="decorator" content="clean-bootstrap" />
	<meta name="where" content="home"/>
	<title>Home</title>
</head>
<body>
<div id="content">


<div class="row-fluid">

	<sec:authorize url="/s/tasks/mytasks">
		<c:redirect url="/s/tasks/mytasks"/>
	</sec:authorize>

	<div class="hero-unit span6">
	<h4>Admin Functions</h4>
	<ul class="pull-right nav">
		<li><a href="${ pageContext.request.contextPath }/s/admin/sourcesystem/list">Source Systems</a>						
		<li><a href="${ pageContext.request.contextPath }/s/admin/usergroup/list">User Groups</a>						
		<li><a href="${ pageContext.request.contextPath }/s/admin/responsibility/list">Responsibilities</a>		
		<li><a href="${ pageContext.request.contextPath }/s/admin/implementation/list">Implementations</a>						
	</ul>
	<div style="clear:both"></div>
	<h4>Support Functions</h4>
	<ul class="pull-right nav">
		<li><a href="${ pageContext.request.contextPath }/s/support/dataexception">Data Exceptions</a></li>
	</ul>
	<div style="clear:both"></div>
	<h4>Services / Channels</h4>
	<ul>
		<li>CorrespondenceService (wired in as gwCorrespondenceService)</li>
		<li>-Dspring.profiles.active=seed (not required unless you want data seeded into your schema upon startup)</li>
	</ul>
	<div style="clear:both"></div>
	</div>




	<div class="hero-unit span6">
	<h4>Done</h4>
	<ul>
		<li>JMX Console - DONE</li>
		<li>JMX - Expose Services</li>
		<li>JMX Enable (in support console )</li>
		<li>Version Servlet ( /internal/version )</li>
		<li>REMServlet ( /internal/rem )</li>
		<li>Put in Chili</li>
		<li>SSO Dev Hack</li>
		<li>SSO Runtime</li>
		<li>Spring Security</li>
		<li>Get My Tasks</li>
		<li>Responsible Person Identification</li>
	</ul>
	</div>

	<div class="hero-unit span6">
	<h4>TODO</h4>
	<ul>
		<li>Setup nice error pages for 404 and 500</li>
		<li>Exception Ingest</li>
		<li>Exemption Processing
			</li>
		<li>Exemption Notification</li>
		<li>Bundling</li>
		<li>Task Creation from Bundles
			<p>(task type based on responsible person?)</p>
			<ul>
				<li>Data Task</li>
				<li>Pass through Exceptions</li>
			</ul>
		</li>
		<li>Stewardship by Source System <p>this is accomplished through local groups</p>
		</li>
		<li>Task Assignment</li>
		<li>Task Notification
			<p>Spring batch job, bundle notifications</p>
		</li>
		<li>RSS Feed by System?
		</li>
		<li>Task Completion Notification to MDM</li>
	</ul>
	<div style="clear:both"></div>
	</div>
	
</div>

</div>
</body>

</html>
