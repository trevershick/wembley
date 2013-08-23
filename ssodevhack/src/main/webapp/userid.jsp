<%@page import="com.google.common.base.Joiner"%>
<%@page import="com.railinc.sso.rt.Permission"%>
<%@page import="java.util.Collection"%>
<%@page import="com.railinc.sso.rt.UserService"%>
<%@page import="com.railinc.sso.rt.LoggedUser"%>
<%@page import="com.railinc.r2dq.ssodeveloperhack.config.Config"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%
	String ssoConfig = System.getProperty("sso.configuration");
	int i = 0;
	Config c = Config.getInstance();
	String usr = c.getProperty("SSO_USR_" + i);
	String desc = c.getProperty("SSO_USR_DESC_" + i);
	String av = c.getProperty("SSO_USR_AVATAR_" + i);
	if (av == null) av = c.getProperty("DEFAULT_AVATAR");
	String fullstring = c.getProperty(usr);
	
	List<String> usrs = new ArrayList<String>();
	List<String> descriptions = new ArrayList<String>();
	List<String> avatars = new ArrayList<String>();
	List<String> full = new ArrayList<String>();

	while (usr != null) {
		usrs.add(usr);
		descriptions.add(desc == null ? "": desc);
		full.add(fullstring);
		avatars.add(av);
		
		usr = c.getProperty("SSO_USR_" + i);
		desc = c.getProperty("SSO_USR_DESC_" + i);
		av = c.getProperty("SSO_USR_AVATAR_" + i);
		if (av == null) av = c.getProperty("DEFAULT_AVATAR");
		if (usr != null) {
			fullstring = c.getProperty(usr);
		}
		i++;
	}
%>
<div id="user-buttons">
	<%
		for (i = 1; i < usrs.size(); i++) {
			String usrId = (String) usrs.get(i);
			String d = descriptions.get(i);
			String f = full.get(i);
			String a = avatars.get(i);
			LoggedUser lu = new LoggedUser(usrId);
			lu.populateFromString(f);
			Collection<Permission> ps = lu.getPermissions();
			String roles = Joiner.on(',').join(ps);
	%>
	<div class="user-button" id="<%=usrId%>" shortcut-key="<%='a' + i - 1 %>" style="background-image: url('/sso/images/<%=a%>');">
		<span class="shortcut-key"><span><%=(char) ('a' + i - 1) %></span></span>
		<p class="user-description"><%= d %></p>
		<p class="user-id">( <%=usrId %> )</p>
	</div>
	<%}%>
</div>
<script>
$(function() {
	$("#user-buttons").delegate(".user-button", "click", function(evt) {
		loginTo($(this).attr("id"));
	});
	$(document).keypress(function(evt) {
		var k = evt.which;
		k = k < 97 ? k + 32 : k;
		var sel =".user-button[shortcut-key='" + k + "']";
		var el = $(sel);
		var id = el.attr("id");
		if (id) {
			flash(el.find(".shortcut-key"),id)
		}
	});
	function flash(el, id) {
		el.css("background-color","red").animate({
			marginLeft: '-=10',
			marginTop: '-=10',
		    width:'+=20',
		    height:'+=20'
		  	}, 100, function() { 
			  loginTo(id); 
			});
	}
	jQuery.fn.center = function () {
	    this.css("position","absolute");
	    this.css("top", Math.max(0, (($(window).height() - $(this).outerHeight()) / 2) + 
	                                                $(window).scrollTop()) + "px");
	    this.css("left", Math.max(0, (($(window).width() - $(this).outerWidth()) / 2) + 
	                                                $(window).scrollLeft()) + "px");
	    return this;
	}
});

</script>