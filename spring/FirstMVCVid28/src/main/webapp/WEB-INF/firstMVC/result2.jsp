<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/result2.css"/>
<title>Results</title>
</head>

<%
	int year = 1993;
	int calculated = 0;
	try {
		calculated = year + Integer.parseInt(request.getParameter("age"));
	}
	catch(NumberFormatException ex) {
		System.out.println("request obtained in result.jsp has thrown an NumberFormatException!");
		calculated = 0;
	}	
	pageContext.setAttribute("calculated", calculated);	// para usarlo en EL
%>

<body>

	<h1>You sent</h1>
	<hr />
	
	<!-- 
		Cuando se usa modelo genérico, EL puede tomar automáticamente
		su atributo: ${age}
	-->
	<p>the year when you're ${age} is ${calculated}</p>
	<hr/>
	<img src="${pageContext.request.contextPath}/resources/img/hatsune_miku.jpg" alt="Hatsune Miku!"/>

</body>
</html>