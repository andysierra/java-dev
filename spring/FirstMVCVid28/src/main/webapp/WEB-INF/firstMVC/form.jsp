<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta charset="UTF-8">
<title>Form</title>
</head>
<body>

<!-- <form action="result1" method="POST"> -->
<form action="result2" method="POST">
	<div>
		<label for="numAge">Diga su edad</label>
		<input 
			name="age"
			type="number" 
			min="1" 
			max="99"/>
	</div>
	<button type="submit">Enviar</button>
</form>

</body>
</html>