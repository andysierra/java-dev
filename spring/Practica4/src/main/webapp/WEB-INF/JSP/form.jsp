<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ include file="init.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Form</title>
</head>
<body>

<h4>Form:</h4>
<form method="POST" action="show">
	<div class="form-group">
		<label for="nombre">Nombre:&nbsp;</label>
		<input
			type="text"
			class="form-control w-100"
			name="nombre"
	 	/>
	</div>
	<button type="submit" class="btn btn-success">
		send
	</button>
</form>

</body>
</html>