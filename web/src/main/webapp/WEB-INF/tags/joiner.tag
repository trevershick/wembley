<%@tag import="com.google.common.base.Joiner"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ attribute name="on" required="true" rtexprvalue="true" type="java.lang.String"%>
<%@ attribute name="items" required="true" rtexprvalue="true" type="java.lang.Iterable" %>
<%= Joiner.on(on).join(items) %>
