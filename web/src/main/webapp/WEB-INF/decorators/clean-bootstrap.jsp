<%@page import="com.railinc.r2dq.configuration.R2DQPropertyRoutes"%>
<%@page import="com.railinc.r2dq.web.Routes"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="decorator"
	uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ include file="/WEB-INF/jsp/d.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>${configurationService.applicationName } - <decorator:title />
</title>
<link href="https://www.railinc.com/railinc-theme/images/favicon.ico"
	rel="Shortcut Icon" />


<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/r/bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/r/css/jquery-ui-1.8.12.custom.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/r/css/standard.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/r/css/app.css" />
<script src="${pageContext.request.contextPath }/r/js/jquery-1.7.1.min.js" type='text/javascript'></script>
<script src="${pageContext.request.contextPath }/r/js/mustache-0.7.2.js" type='text/javascript'></script>




<decorator:head />
</head>
<body>
	<!-- Header Begin -->



	<div id="rail-header">
		<div id="util-nav-wrapper">
			
			<sec:authorize access="isAuthenticated()">
				
			<sec:authentication property="principal.employer" var="emp"/>
			<ul id="util-nav">
					<li><span id="rail-app-user"><sec:authentication property="principal.name"/></span>
						<c:if test="${not empty emp }"> 
							<span id="rail-app-user-company"> : ${emp }</span>
						</c:if>
					</li>


					<li><a id="rail-launch-pad-link">Launch Pad</a>
						<div id="rail-launch-pad">
							<ul>
								<li><h2>Applications</h2>
									<ul id="appList">
										<jsp:include page="/s/chili/s/apps.li" flush="true"/>
									</ul>
								</li>

							</ul>

							<ul>
								<li><h2>User Services</h2>
									<ul>
										<li><a
											href="/sso/user/setsession?redirect=/sso/editUserProfile.do">Edit
												Profile</a></li>
										<li><a
											href="/sso/user/setsession?redirect=/sso/editUserProfile.do">Change
												Password</a></li>
										<li><a
											href="/sso/user/setsession?redirect=/sso/editUserProfile.do">View
												/ Request Permissions</a></li>
										<li><a
											href="/sso/user/setsession?redirect=/sso/editUserProfile.do">Permission
												Request Status</a></li>
										<li><a href="/rportal/web/csc">Launch Pad</a></li>

									</ul>
								</li>
							</ul>

						</div>
					</li>
					<li><a href="http://www.railinc.com/contactus">Contact Us</a>
					</li>
					<li class="lastLK"><a href="${pageContext.request.contextPath }/g/logmeout">Sign Out</a></li>


				
				
				
			</ul>
			</sec:authorize>
		</div>
		<a href="/" id="railinc-logo"><img
			src="${pageContext.request.contextPath }/r/images/logo.png"
			alt="Go to www.railinc.com" width="150" height="26" border="0" /> </a>
		<c:url value="/" var="homeUrl" />
		<div id="rail-app-name">
			<a href="${homeUrl }">${configurationService.applicationName }</a>
		</div>
		<div class="clear"></div>

	</div>


	<c:set var="where">
		<decorator:getProperty property="meta.where" />
	</c:set>
	<div id="rail-app-container">
		<div class="navbar">
			<div class="navbar-inner">
								<a class="brand" href="#"><decorator:title/></a>
				<ul class="nav">
					<li class="${fn:startsWith(where, 'home') ? 'active' : '' }"><a
						href="${pageContext.request.contextPath }/">Home</a>
					</li>
					<sec:authorize url="/s/tasks/mytasks">
						<li class="${fn:contains(where, 'my.mytasks') ? 'active' : '' }"><a href="${pageContext.request.contextPath }/s/tasks/mytasks" > My Tasks</a></li>
					</sec:authorize>
					
					<sec:authorize url="/s/admin">
					<li class="dropdown ${fn:contains(where, 'admin') ? 'active' : '' }"><a
						href="#" class="dropdown-toggle" data-toggle="dropdown"> Admin
							<b class="caret"></b> </a>
						<ul class="dropdown-menu">
							<li
								class="${fn:startsWith(where, 'admin.sourcesystem.*') ? 'active' : '' }"><a
								href="${ pageContext.request.contextPath }/s/admin/sourcesystem/list">Source
									Systems</a></li>
							<li
								class="${fn:startsWith(where, 'admin.usergroup.*') ? 'active' : '' }"><a
								href="${ pageContext.request.contextPath }/s/admin/usergroup/list">User
									Groups</a></li>
							<li
								class="${fn:startsWith(where, 'admin.responsibility.*') ? 'active' : '' }"><a
								href="${ pageContext.request.contextPath }/s/admin/responsibility/list">Responsibilities</a>
								</li>
							<li
								class="${fn:startsWith(where, 'admin.implementation.*') ? 'active' : '' }"><a
								href="${ pageContext.request.contextPath }/s/admin/implementation/list">Implementations</a>
							</li>
							<sec:authorize url="${ routes:root('admintasks') }">
							<li class="${fn:startsWith(where, 'admin.admintasks') ? 'active' : '' }"><a
								href="${ pageContext.request.contextPath }${ routes:to('admintasks','list_path') }">Tasks</a>
							</li>
							</sec:authorize>
								
						</ul></li>
					</sec:authorize>
					<sec:authorize url="/s/support">
					<li class="dropdown ${fn:startsWith(where, 'support.*') ? 'active' : '' }"><a
						href="#" class="dropdown-toggle" data-toggle="dropdown">
							Support <b class="caret"></b> </a>
						<ul class="dropdown-menu">
							<li class="${fn:startsWith(where, 'support.home') ? 'active' : '' }"><a
								href="${ pageContext.request.contextPath }/s/support">Support
									Home</a>
							</li>
							<sec:authorize url="/s/support/message">
							<li
								class="${fn:startsWith(where, 'support.messagelist.*') ? 'active' : '' }"><a
								href="${ pageContext.request.contextPath }/s/support/message">Messages</a>
							</li>
							</sec:authorize>
							<sec:authorize url="/s/support/dataexception">
							<li
								class="${fn:startsWith(where, 'support.dataexception.*') ? 'active' : '' }"><a
								href="${ pageContext.request.contextPath }/s/support/dataexception">Data Exceptions</a>
							</li>
							</sec:authorize>
							<sec:authorize url="${ routes:root('r2dqproperty') }">
							<li class="${fn:startsWith(where, 'support.r2dqproperty') ? 'active' : '' }"><a
								href="${ pageContext.request.contextPath }${ routes:to('r2dqproperty','list_path') }">Configuration</a>
							</li>
							</sec:authorize>
							<sec:authorize url="${ routes:root('i18n') }">
							<li class="${fn:startsWith(where, 'support.i18n') ? 'active' : '' }"><a
								href="${ pageContext.request.contextPath }${ routes:to('i18n','list_path') }">i18n</a>
							</li>
							</sec:authorize>
							
							<sec:authorize url="/s/support/properties">
							<li
								class="${fn:startsWith(where, 'support.properties') ? 'active' : '' }"><a
								href="${ pageContext.request.contextPath }/s/support/properties">JVM
									Properties</a>
							</li>
							</sec:authorize>
							<sec:authorize url="/g/snoop">
							<li class="${fn:startsWith(where, 'support.snoop') ? 'active' : '' }"><a
								href="${ pageContext.request.contextPath }/g/snoop">Request
									Snoop</a>
							</li>
							</sec:authorize>
							<sec:authorize url="/s/support/logging">
							<li class="${fn:startsWith(where, 'support.logging') ? 'active' : '' }"><a
								href="${ pageContext.request.contextPath }/s/support/logging">Logging</a>
								</li>
							</sec:authorize>
							<sec:authorize url="/s/support/jmx">
							<li class="${fn:startsWith(where, 'support.jmx') ? 'active' : '' }"><a
								href="${ pageContext.request.contextPath }/s/support/jmx">JMX
									Console</a>
								</li>
							</sec:authorize>
							<sec:authorize url="/s/support/batchconsole">
							<li
								class="${fn:startsWith(where, 'support.batchconsole') ? 'active' : '' }"><a
								href="${ pageContext.request.contextPath }/s/support/batchconsole">Batch
									Console</a>
									</li>
							</sec:authorize>
							<li
								class="${fn:startsWith(where, 'support.auditlog') ? 'active' : '' }"><a
								href="${ pageContext.request.contextPath }/s/support/auditlog/list">System Audit Log</a>
									</li>
						</ul></li>
					</sec:authorize>
					
				</ul>
			</div>
		</div>

		<div id="content-container">

			<decorator:getProperty property="div.breadcrumbs" />

			<spring:hasBindErrors name="flash">
				<div class="alert alert-block alert-info">
					<button type="button" class="close" data-dismiss="alert">&times;</button>
					<form:errors path="flash" />
				</div>
			</spring:hasBindErrors>
			<spring:hasBindErrors name="flasherror">
				<div class="alert alert-block alert-error">
					<button type="button" class="close" data-dismiss="alert">&times;</button>
					<form:errors path="flasherror" />
				</div>
			</spring:hasBindErrors>



			<decorator:getProperty property="div.content" default="" />

			<div class="clear"></div>

		</div>
	</div>


	<!-- App Area End -->
	<div class="clear" style="min-height:50px;"></div>
	<!-- Footer Begin -->


	<div id="rail-footer" class="navbar navbar-fixed-bottom" style="">
		<ul class="nav" style="display: none;" id="footer-nav">
			<li><a href="/legal" class="first">legal notices</a></li>
			<li><a href="/privacy">privacy rights</a></li>
			<li><a href="/terms">terms of service</a></li>
			<li><a href="/contactus">contact us</a></li>
		</ul>
	</div>

	<script src="${pageContext.request.contextPath }/r/js/jquery-ui-1.8.17.custom.min.js" type='text/javascript'></script>
	<script src="${pageContext.request.contextPath }/r/bootstrap/js/bootstrap.min.js" type='text/javascript'></script>
	<script src="${pageContext.request.contextPath }/r/js/jquery.hoverIntent.minified.js" type='text/javascript'></script>
	<script src="${pageContext.request.contextPath }/r/js/standard.js" type='text/javascript'></script>
	<script src="${pageContext.request.contextPath }/r/js/app.js" type='text/javascript'></script>
	<decorator:getProperty property="div.post-content-js" />
</body>
</html>

