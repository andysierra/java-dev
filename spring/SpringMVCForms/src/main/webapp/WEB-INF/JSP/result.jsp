<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="init.jspf" %>
    
<!-- model > alumnoModel (@modelAttribute) -->
    
<html lang="es-CO">
<head>
<title>Result</title>
</head>
<body>

	<div class="card m-5">
		<div class="card-header">
			<h3>Results</h3>
		</div>
		<div class="card-body">
			<p>
				El alumno con nombre <strong>${alumnoModel.getNombre()}</strong> y apellido 
				<strong>${alumnoModel.getApellido()}</strong> ha elegido como materias optativas 
				<strong>${alumnoModel.getOptativa()}</strong> en la ciudad de
				<strong>${alumnoModel.getCiudadEstudios()}</strong> con Porvenir ID
				<strong>${alumnoModel.getPorvenirId()}</strong> con Contraseña
				<strong>${alumnoModel.getPassword()}</strong> con confirmación de contraseña 
				<strong>${alumnoModel.getPasswordConfirm()}</strong>
			</p>
		</div>
	</div>

</body>
</html>