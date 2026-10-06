<%@page import="com.andysierra.services.PopulateExampleFormService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="init.jspf" %>
    
<!-- model > alumnoModel -->
    
<html lang="es-CO">
<head>
<meta charset="UTF-8">
<title>ExampleForm</title>
</head>
<body>

	<h1>Example Form</h1>
	<hr/>
	
	<form:form action="result" modelAttribute="alumnoModel" class="form w-25 m-5">
		<div class="form-group my-2">
			<form:label path="nombre" class="form-label">Nombre:</form:label>
			<form:input class="form-control form-control-sm" path="nombre"/>
			<form:errors path="nombre"/>
		</div>
		
		<div class="form-group my-2">
			<form:label path="apellido" class="form-label">Apellido:</form:label>
			<form:input class="form-control form-control-sm" path="apellido"/>
			<form:errors path="apellido"/>
		</div>
		
		<div class="form-group my-2">
			<form:label path="optativa" class="form-label">Optativa:</form:label>
			<form:select path="optativa" multiple="true" class="form-select">
				<form:options items="${alumnoValues.optativaValues()}"/>
			</form:select>	
			<form:errors path="optativa"/>
		</div>
		
		<div class="form-group my-2">
			<form:label path="ciudadEstudios" class="form-label">Ciudad Estudios:</form:label>
			<form:radiobuttons path="ciudadEstudios" items="${alumnoValues.ciudadEstudioValues()}" class="mx-2"/>
			<form:errors path="ciudadEstudios"/>
		</div>
		
		<div class="form-group my-2">
			<form:label path="porvenirId" class="form-label">PorvenirID:</form:label>
			<form:input class="form-control form-control-sm" path="porvenirId"/>
			<form:errors path="porvenirId"/>
		</div>
		
		<div class="form-group my-2">
			<form:label path="password" class="form-label">Contraseña:</form:label>
			<form:input class="form-control form-control-sm" path="password"/>
			<form:errors path="password"/>
		</div>
		
		<div class="form-group my-2">
			<form:label path="passwordConfirm" class="form-label">Confirmación de Contraseña:</form:label>
			<form:input class="form-control form-control-sm" path="passwordConfirm"/>
			<form:errors path="passwordConfirm"/>
		</div>
		
		<br/>
		<button type="submit" class="btn btn-sm btn-success">Enviar</button>
	</form:form>

</body>
</html>