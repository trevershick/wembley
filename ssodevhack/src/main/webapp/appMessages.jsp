<%@page import="org.apache.commons.io.*"%>
<%@page import="java.io.FileReader"%>
<%@page import="java.io.File"%>
<%@ page language="java" %>
<%
File f = new File(System.getProperty("sso.configuration")); 
String contents = FileUtils.readFileToString(f);
%>
<%=contents%>