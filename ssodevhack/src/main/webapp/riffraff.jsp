<%@ page language="java"%>
<%@page import="com.railinc.sso.rt.UserService"%>
<%@page import="com.railinc.sso.rt.LoggedUser"%>
<%@page import="com.railinc.r2dq.ssodeveloperhack.config.Config"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%
	com.railinc.sso.rt.UserService userService = com.railinc.sso.rt.UserService.getInstance();
	Boolean isAuthenticated = new Boolean(userService.isAuthenticated(request));
	pageContext.setAttribute("authenticated", isAuthenticated);
	pageContext.setAttribute("loggedUser", com.railinc.sso.rt.UserService.getLoggedUser(request));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.railinc.r2dq.ssodeveloperhack.config.Config"%>

<form name="login" method="post" action="login.do">
	
<%
	String ssoConfig = System.getProperty("sso.configuration");
	int i = 0;
	Config c = Config.getInstance();
	String usr = c.getProperty("SSO_USR_" + i);
	
	List<String> usrs = new ArrayList<String>();
	while (usr != null) {
		usrs.add(usr);
		usr = c.getProperty("SSO_USR_" + i);
		i++;
	}
%>
<select name="sso.userId">
	<%
		for (i = 1; i < usrs.size(); i++) {
			String usrId = (String) usrs.get(i);
	%>
	<option value="<%=usrId %>"><%=usrId %></option>
	<%}%>
	<input type="submit"/>
</select>
</form>