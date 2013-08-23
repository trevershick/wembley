<%@ page language="java"%>

<%
	String appName = Config.getInstance().getProperty("appName");
	appName = appName != null ? appName : "My Application";
%>
<%
	com.railinc.sso.rt.UserService userService = com.railinc.sso.rt.UserService.getInstance();
	Boolean isAuthenticated = new Boolean(userService.isAuthenticated(request));
	pageContext.setAttribute("authenticated", isAuthenticated);
	pageContext.setAttribute("loggedUser", com.railinc.sso.rt.UserService.getLoggedUser(request));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.railinc.r2dq.ssodeveloperhack.config.Config"%>
<script>
function loginTo(userId) {
	$("#logging-in-as").text(userId);
	$("#logging-in").center().fadeIn(function() {
		$("#userId").val(userId);
		$("#loginForm").submit();
	});
	return false;
}
</script>
<form name="login" method="post" action="" id="loginForm">
	<input type="hidden" name="sso.userId" id="userId"/>
	<jsp:include flush="true" page="userid.jsp" />
</form>
<div style="clear:both">&nbsp;</div>

   <div id="logging-in">
   <p>
    Logging in as <span id="logging-in-as">someone</span>
    </p>
    </div>
    