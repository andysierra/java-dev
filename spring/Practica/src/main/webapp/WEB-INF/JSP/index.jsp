<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="init.jsp"%>

<html lang="es-CO">
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/recursos/CSS/index.css">

<!-- Esta pieza de código no funciona porque WEB-INF es sólo para -->
<!-- <link rel="stylesheet" type="text/css" -->
<%-- 	href="${pageContext.request.contextPath}/perrosky.css"> --%>
</head>

<%
pageContext.setAttribute("bitch", "bitch loconcio");
%>

<body>

	<h1>Hello SpringMVC world!</h1>

	<hr />

	<c:out value="${pageContext.request.contextPath}" />
	<p>${pageContext.request.contextPath}/recursos/CSS/index.css</p>

</body>
</html>