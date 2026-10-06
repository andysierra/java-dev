<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="init.jsp" %>

<%
	String greeting = "Hola bastardos!";
	pageContext.setAttribute("greeting", greeting);
%>

<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<h1>${ greeting }</h1>

</body>
</html>