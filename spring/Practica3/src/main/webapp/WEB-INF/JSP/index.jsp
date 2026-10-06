<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@include file="./init.jspf"%>

<%
String data = "you're right!";
pageContext.setAttribute("data", data);
%>

<html lang="es-CO">
<head>
<meta charset="UTF-8">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/CSS/index.css" />
<title>Prueba 3</title>
</head>
<body>

	<c:out value="${data}"></c:out>

</body>
</html>