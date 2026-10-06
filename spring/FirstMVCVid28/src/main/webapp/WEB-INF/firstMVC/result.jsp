<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta charset="UTF-8">
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
	pageContext.setAttribute("calculated", calculated);
%>

<body>

	<h1>You sent</h1>
	<hr />
	<p>the year when you're ${param.age} is ${calculated}</p>

</body>
</html>