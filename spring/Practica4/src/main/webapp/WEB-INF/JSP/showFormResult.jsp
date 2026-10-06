<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ include file="init.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Results</title>
</head>
<body>

	<h4>Results</h4>
	<hr/>
	<p><%= request.getParameter("nombre") %></p>
	<hr/>
	<img src="${pageContext.request.contextPath}/resources/img/hatsune.png" alt="hatsune"/>

</body>
</html>