package com.andysierra.request.impl;

import java.util.Objects;
import com.andysierra.entity.impl.Usuario;
import com.andysierra.request.BaseRequest;

public class UsuarioCreateRequest implements BaseRequest {

	public Long 	id;
	public String 	nombre;
	public int 		edad;
	public String 	correo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(correo, edad, id, nombre);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(correo, other.getCorreo()) && edad == other.getEdad() && Objects.equals(id, other.getId())
				&& Objects.equals(nombre, other.getNombre());
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nombre=" + nombre + ", edad=" + edad + ", correo=" + correo + "]";
	}
	
}
