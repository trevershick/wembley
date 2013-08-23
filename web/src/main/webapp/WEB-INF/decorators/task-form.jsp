<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="decorator"
	uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ include file="/WEB-INF/jsp/d.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>R2DQ - <decorator:title />
</title>
<link href="https://www.railinc.com/railinc-theme/images/favicon.ico"
	rel="Shortcut Icon" />


<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/r/bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/r/css/jquery-ui-1.8.12.custom.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/r/css/standard.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/r/css/app.css" />

<decorator:head />
</head>
<body>
	<!-- Header Begin -->





	<div id="rail-app-container">
		


		<div id="content-container">
		<div class="page-header">
  			<h1><decorator:getProperty property="meta.task-name" /> <small><decorator:getProperty property="div.task-description" default="" /></small></h1>
		</div>

			<spring:hasBindErrors name="flash">
				<div class="alert alert-block alert-info">
					<button type="button" class="close" data-dismiss="alert">&times;</button>
					<form:errors path="flash" />
				</div>
			</spring:hasBindErrors>

			<decorator:getProperty property="div.content" default="" />

			<div class="clear"></div>

		</div>
	</div>


	<!-- App Area End -->
	<div class="clear"></div>
	<!-- Footer Begin -->




	<script src="${pageContext.request.contextPath }/r/js/jquery-1.7.1.min.js" type='text/javascript'></script>
	<script src="${pageContext.request.contextPath }/r/js/jquery-ui-1.8.17.custom.min.js" type='text/javascript'></script>
	<script src="${pageContext.request.contextPath }/r/bootstrap/js/bootstrap.min.js" type='text/javascript'></script>
	<script src="${pageContext.request.contextPath }/r/js/jquery.hoverIntent.minified.js" type='text/javascript'></script>
	<script src="${pageContext.request.contextPath }/r/js/standard.js" type='text/javascript'></script>
	<script src="${pageContext.request.contextPath }/r/js/app.js" type='text/javascript'></script>
	<decorator:getProperty property="div.post-content-js" />
</body>
</html>

